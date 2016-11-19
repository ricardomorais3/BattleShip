package org.academiadecodigo.battleship.player;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import org.academiadecodigo.battleship.Position;

import java.io.IOException;

/**
 * Created by codecadet on 18/11/16.
 */
public class Lanterna implements Runnable {

    private Terminal terminal;
    private Screen screen;
    private Label[][] labelsMatrix;
    private Player player;

    public Lanterna(Player player){
        this.player = player;
    }

   /* public void teste() {
        if (horizontal) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (labelsMatrix[j][i].getPosition().getColumn() == myPos.getColumn()
                            && labelsMatrix[j][i].getPosition().getRow() == myPos.getRow()) {
                        for (int k = 0; k < boatSize; k++) {
                            labelsMatrix[j + k][i].setText("  ");
                            labelsMatrix[j + k][i].setBackgroundColor(TextColor.ANSI.RED);
                        }
                        j += boatSize - 1;
                    } else {
                        labelsMatrix[j][i].setText("  ");
                        labelsMatrix[j][i].setBackgroundColor(new TextColor.RGB(72, 116, 250));
                    }
                }
            }
        } else {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (labelsMatrix[i][j].getPosition().getColumn() == myPos.getColumn()
                            && labelsMatrix[i][j].getPosition().getRow() == myPos.getRow()) {
                        for (int k = 0; k < boatSize; k++) {
                            labelsMatrix[i][j + k].setText("  ");
                            labelsMatrix[i][j + k].setBackgroundColor(TextColor.ANSI.RED);
                        }
                        j += boatSize - 1;
                    } else {
                        labelsMatrix[i][j].setText("  ");
                        labelsMatrix[i][j].setBackgroundColor(new TextColor.RGB(72, 116, 250));
                    }
                }
            }
        }

    }*/

    public void rePaint(Position position, int boatSize, boolean isHorizontal){

        position = new Position((position.getCol()*2)+1, position.getRow());

        if (isHorizontal) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (labelsMatrix[j][i].getPosition().getColumn() == position.getCol()
                            && labelsMatrix[j][i].getPosition().getRow() == position.getRow()) {
                        for (int k = 0; k < boatSize; k++) {
                            labelsMatrix[j + k][i].setText("  ");
                            labelsMatrix[j + k][i].setBackgroundColor(TextColor.ANSI.RED);
                        }
                        j += boatSize - 1;
                    } else {
                        if (player.getGrid()[j][i].getType() == 'S'){
                            labelsMatrix[j][i].setBackgroundColor(new TextColor.RGB(172, 126, 250));
                        }else {
                            labelsMatrix[j][i].setText("  ");
                            labelsMatrix[j][i].setBackgroundColor(new TextColor.RGB(72, 116, 250));
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (labelsMatrix[i][j].getPosition().getColumn() == position.getCol()
                            && labelsMatrix[i][j].getPosition().getRow() == position.getRow()) {
                        for (int k = 0; k < boatSize; k++) {
                            labelsMatrix[i][j + k].setText("  ");
                            labelsMatrix[i][j + k].setBackgroundColor(TextColor.ANSI.RED);
                        }
                        j += boatSize - 1;
                    } else {
                        if(player.getGrid()[i][j].getType() == 'S'){
                            labelsMatrix[i][j].setBackgroundColor(new TextColor.RGB(172, 126, 250));
                        }else{
                            labelsMatrix[i][j].setText("  ");
                            labelsMatrix[i][j].setBackgroundColor(new TextColor.RGB(72, 116, 250));
                        }
                    }
                }
            }
        }
    }

    public Screen getScreen() {
        return screen;
    }

    @Override
    public void run() {
        try {

            terminal = new DefaultTerminalFactory().createTerminal();
            screen = new TerminalScreen(terminal);
            screen.startScreen();

            Panel panel = new Panel();
            panel.setLayoutManager(new GridLayout(10).setHorizontalSpacing(0));
            panel.withBorder(Borders.doubleLine());
            panel.setPreferredSize(new TerminalSize(30, 10));

            labelsMatrix = new Label[10][10];
            //rows
            for (int i = 0; i < 10; i++) {
                //cols
                for (int j = 0; j < 10; j++) {
                    labelsMatrix[j][i] = new Label("  ").setBackgroundColor(new TextColor.RGB(72, 116, 250)).setLabelWidth(2);
                    panel.addComponent(labelsMatrix[j][i]);
                }
            }

            for (int i = 0; i < player.getShipSize(); i++) {
                labelsMatrix[i][0].setBackgroundColor(TextColor.ANSI.RED);
            }

            BasicWindow window = new BasicWindow();

            window.setComponent(panel);

            MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.RED));

            KeyboardHandler keyboardHandler = new KeyboardHandler(this, player);
            Thread keyboard = new Thread(keyboardHandler);
            keyboard.start();

            gui.addWindowAndWait(window);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
