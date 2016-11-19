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
    private Label[][] myLabelsMatrix;
    private Label[][] enemyLabelsMatrix;
    private Player player;
    private KeyboardHandler keyboardHandler;
    private Panel mainPanel;
    private Panel leftPanel;
    private Panel rightPanel;
    private BasicWindow window;


    public Lanterna(Player player) {
        this.player = player;
    }

    public void rePaintMyGrid(Position position, int boatSize, boolean isHorizontal) {

        position = new Position((position.getCol() * 2) + 1, position.getRow());

        if (isHorizontal) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (myLabelsMatrix[j][i].getPosition().getColumn() == position.getCol()
                            && myLabelsMatrix[j][i].getPosition().getRow() == position.getRow()) {
                        for (int k = 0; k < boatSize; k++) {
                            myLabelsMatrix[j + k][i].setText("  ");
                            myLabelsMatrix[j + k][i].setBackgroundColor(TextColor.ANSI.RED);
                        }
                        j += boatSize - 1;
                    } else {
                        if (player.getMyGrid()[j][i].getType() == 'S') {
                            myLabelsMatrix[j][i].setBackgroundColor(new TextColor.RGB(172, 126, 250));
                        } else {
                            myLabelsMatrix[j][i].setText("  ");
                            myLabelsMatrix[j][i].setBackgroundColor(new TextColor.RGB(72, 116, 250));
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (myLabelsMatrix[i][j].getPosition().getColumn() == position.getCol()
                            && myLabelsMatrix[i][j].getPosition().getRow() == position.getRow()) {
                        for (int k = 0; k < boatSize; k++) {
                            myLabelsMatrix[i][j + k].setText("  ");
                            myLabelsMatrix[i][j + k].setBackgroundColor(TextColor.ANSI.RED);
                        }
                        j += boatSize - 1;
                    } else {
                        if (player.getMyGrid()[i][j].getType() == 'S') {
                            myLabelsMatrix[i][j].setBackgroundColor(new TextColor.RGB(172, 126, 250));
                        } else {
                            myLabelsMatrix[i][j].setText("  ");
                            myLabelsMatrix[i][j].setBackgroundColor(new TextColor.RGB(72, 116, 250));
                        }
                    }
                }
            }
        }
    }

    public void rePaintMyGrid2() {
        System.out.println("painting grid");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                myLabelsMatrix[j][i].setBackgroundColor(getColor(player.getMyGrid()[j][i].getType()));
            }
        }
    }

    private TextColor getColor(char c){
        TextColor color;
        switch (c){
            case 'S':
                color = new TextColor.RGB(172, 126, 250);
                break;
            case 'H':
                color = TextColor.ANSI.YELLOW;
                break;
            case 'C':
                color = TextColor.ANSI.RED;
                break;
            case 'M':
                color = TextColor.ANSI.BLACK;
                break;
            default:
                color = new TextColor.RGB(72, 116, 250);
        }
        return color;
    }

    public void rePaintEnemyGrid(Position position) {
        position = new Position((position.getCol() * 2) + 1, position.getRow());
     /*   TextColor color;

        switch (player.getEnemyGrid()[position.getCol()][position.getRow()].getType()){
            case 'M':
                color = TextColor.ANSI.CYAN;
                break;
            case 'H':
                color = TextColor.ANSI.YELLOW;
                break;
            case 'C':
                color = TextColor.ANSI.RED;
                break;
            default:
                color = new TextColor.RGB(72, 116, 250);
        }*/

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (enemyLabelsMatrix[j][i].getPosition().getColumn() == position.getCol()
                        && enemyLabelsMatrix[j][i].getPosition().getRow() == position.getRow()) {

                    enemyLabelsMatrix[j][i].setText("  ");
                    enemyLabelsMatrix[j][i].setBackgroundColor(TextColor.ANSI.RED);

                } else {
                    if (player.getEnemyGrid()[j][i].getType() == 'S') {
                        enemyLabelsMatrix[j][i].setBackgroundColor(TextColor.ANSI.BLUE);
                    }else if(player.getEnemyGrid()[j][i].getType() == 'H'){
                        enemyLabelsMatrix[j][i].setBackgroundColor(TextColor.ANSI.YELLOW);
                    }else if(player.getEnemyGrid()[j][i].getType() == 'M'){
                        enemyLabelsMatrix[j][i].setBackgroundColor(TextColor.ANSI.CYAN);
                    }else {
                        enemyLabelsMatrix[j][i].setBackgroundColor(TextColor.ANSI.BLUE);
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

            mainPanel = new Panel();


            rightPanel = new Panel();
            rightPanel.setLayoutManager(new GridLayout(10).setHorizontalSpacing(0));
            rightPanel.withBorder(Borders.doubleLine());
            rightPanel.setPreferredSize(new TerminalSize(22, 10));

            leftPanel = new Panel();
            leftPanel.setLayoutManager(new GridLayout(10).setHorizontalSpacing(0));
            leftPanel.withBorder(Borders.doubleLine("MyGrid"));
            leftPanel.setPreferredSize(new TerminalSize(22, 10));


            mainPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
            mainPanel.addComponent(leftPanel.withBorder(Borders.singleLine("MyGrid")));
            mainPanel.addComponent(rightPanel.withBorder(Borders.singleLine("EnemyGrid")));

            myLabelsMatrix = new Label[10][10];
            enemyLabelsMatrix = new Label[10][10];

            //rows
            for (int i = 0; i < 10; i++) {
                //cols
                for (int j = 0; j < 10; j++) {
                    myLabelsMatrix[j][i] = new Label("  ").setBackgroundColor(new TextColor.RGB(72, 116, 250)).setLabelWidth(2);
                    leftPanel.addComponent(myLabelsMatrix[j][i]);
                    enemyLabelsMatrix[j][i] = new Label("  ").setBackgroundColor(new TextColor.RGB(72, 116, 250)).setLabelWidth(2);
                    rightPanel.addComponent(enemyLabelsMatrix[j][i]);
                }
            }

            for (int i = 0; i < player.getShipSize(); i++) {
                myLabelsMatrix[i][0].setBackgroundColor(TextColor.ANSI.RED);
            }

            window = new BasicWindow();

            window.setComponent(mainPanel.withBorder(Borders.singleLine("Populate your grid")));


            MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.RED));

            keyboardHandler = new KeyboardHandler(this, player);
            Thread keyboard = new Thread(keyboardHandler);
            keyboard.start();

            gui.addWindowAndWait(window);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public KeyboardHandler getKeyboardHandler() {
        return keyboardHandler;
    }

    public void changePanelTitle(String title) {
        window.setComponent(mainPanel.withBorder(Borders.singleLine(title)));

    }

    public Panel getMainPanel() {
        return mainPanel;
    }

    public Panel getLeftPanel() {
        return leftPanel;
    }

    public Panel getRightPanel() {
        return rightPanel;
    }

    public BasicWindow getWindow() {
        return window;
    }

    /* public void teste() {
        if (horizontal) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (myLabelsMatrix[j][i].getPosition().getColumn() == myPos.getColumn()
                            && myLabelsMatrix[j][i].getPosition().getRow() == myPos.getRow()) {
                        for (int k = 0; k < boatSize; k++) {
                            myLabelsMatrix[j + k][i].setText("  ");
                            myLabelsMatrix[j + k][i].setBackgroundColor(TextColor.ANSI.RED);
                        }
                        j += boatSize - 1;
                    } else {
                        myLabelsMatrix[j][i].setText("  ");
                        myLabelsMatrix[j][i].setBackgroundColor(new TextColor.RGB(72, 116, 250));
                    }
                }
            }
        } else {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (myLabelsMatrix[i][j].getPosition().getColumn() == myPos.getColumn()
                            && myLabelsMatrix[i][j].getPosition().getRow() == myPos.getRow()) {
                        for (int k = 0; k < boatSize; k++) {
                            myLabelsMatrix[i][j + k].setText("  ");
                            myLabelsMatrix[i][j + k].setBackgroundColor(TextColor.ANSI.RED);
                        }
                        j += boatSize - 1;
                    } else {
                        myLabelsMatrix[i][j].setText("  ");
                        myLabelsMatrix[i][j].setBackgroundColor(new TextColor.RGB(72, 116, 250));
                    }
                }
            }
        }

    }*/
}
