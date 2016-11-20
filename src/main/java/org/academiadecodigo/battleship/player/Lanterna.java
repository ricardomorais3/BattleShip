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
    private Screen screen;
    private Label[][] myLabelsMatrix;
    private Label[][] enemyLabelsMatrix;
    private Player player;
    private KeyboardHandler keyboardHandler;
    private Panel mainPanel;
    private BasicWindow window;

    public Lanterna(Player player) {
        this.player = player;
    }

    public void rePaintMyGrid(Position position, int boatSize, boolean isHorizontal) {
        position = new Position((position.getCol() * 2) + 1, position.getRow());

        if (isHorizontal) {
            for (int i = 0; i < Player.ROWS; i++) {
                for (int j = 0; j < Player.COLS; j++) {
                    if (myLabelsMatrix[j][i].getPosition().getColumn() == position.getCol()
                            && myLabelsMatrix[j][i].getPosition().getRow() == position.getRow()) {
                        for (int k = 0; k < boatSize; k++) {
                            myLabelsMatrix[j + k][i].setText("  ");
                            myLabelsMatrix[j + k][i].setBackgroundColor(new TextColor.RGB(125, 56, 192));
                        }
                        j += boatSize - 1;
                    } else {
                        if (player.getMyGrid()[j][i].getType() == 'S') {
                            myLabelsMatrix[j][i].setBackgroundColor(new TextColor.RGB(59, 4, 113));
                        } else {
                            myLabelsMatrix[j][i].setText("  ");
                            myLabelsMatrix[j][i].setBackgroundColor(new TextColor.RGB(72, 116, 250));
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < Player.ROWS; i++) {
                for (int j = 0; j < Player.COLS; j++) {
                    if (myLabelsMatrix[i][j].getPosition().getColumn() == position.getCol()
                            && myLabelsMatrix[i][j].getPosition().getRow() == position.getRow()) {
                        for (int k = 0; k < boatSize; k++) {
                            myLabelsMatrix[i][j + k].setText("  ");
                            myLabelsMatrix[i][j + k].setBackgroundColor(new TextColor.RGB(125, 56, 192));
                        }
                        j += boatSize - 1;
                    } else {
                        if (player.getMyGrid()[i][j].getType() == 'S') {
                            myLabelsMatrix[i][j].setBackgroundColor(new TextColor.RGB(59, 4, 113));
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
        for (int i = 0; i < Player.ROWS; i++) {
            for (int j = 0; j < Player.COLS; j++) {
                myLabelsMatrix[j][i].setBackgroundColor(getColor(player.getMyGrid()[j][i].getType()));
            }
        }
    }

    private TextColor getColor(char c) {
        TextColor color;
        switch (c) {
            case 'S':
                color = new TextColor.RGB(77, 7, 144);//PURPLE
                break;
            case 'H':
                color = new TextColor.RGB(156, 0, 28);//DARK RED
                break;
            case 'C':
                color = new TextColor.RGB(243, 58, 91);//RED
                break;
            case 'M':
                color = new TextColor.RGB(0, 53, 140);//DARK BLUE
                break;
            default:
                color = new TextColor.RGB(72, 116, 250);//LIGHT BLUE
        }
        return color;
    }

    public void rePaintEnemyGrid(Position position) {
        position = new Position((position.getCol() * 2) + 1, position.getRow());

        for (int i = 0; i < Player.ROWS; i++) {
            for (int j = 0; j < Player.COLS; j++) {
                if (enemyLabelsMatrix[j][i].getPosition().getColumn() == position.getCol()
                        && enemyLabelsMatrix[j][i].getPosition().getRow() == position.getRow()) {

                    enemyLabelsMatrix[j][i].setText("  ");
                    enemyLabelsMatrix[j][i].setBackgroundColor(new TextColor.RGB(76, 153, 0));

                } else {
                    if (player.getEnemyGrid()[j][i].getType() == 'H') {
                        enemyLabelsMatrix[j][i].setBackgroundColor(new TextColor.RGB(156, 0, 28));
                    } else if (player.getEnemyGrid()[j][i].getType() == 'M') {
                        enemyLabelsMatrix[j][i].setBackgroundColor(new TextColor.RGB(0, 53, 140));
                    } else if (player.getEnemyGrid()[j][i].getType() == 'C') {
                        enemyLabelsMatrix[j][i].setBackgroundColor(new TextColor.RGB(243, 58, 91));
                    } else {
                        enemyLabelsMatrix[j][i].setBackgroundColor(new TextColor.RGB(72, 116, 250));
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            screen = new TerminalScreen(terminal);
            screen.startScreen();
            mainPanel = new Panel();

            Panel rightPanel = new Panel();
            rightPanel.setLayoutManager(new GridLayout(Player.COLS).setHorizontalSpacing(0));
            rightPanel.withBorder(Borders.doubleLine());
            rightPanel.setPreferredSize(new TerminalSize(22, 10));

            Panel leftPanel = new Panel();
            leftPanel.setLayoutManager(new GridLayout(Player.COLS).setHorizontalSpacing(0));
            leftPanel.withBorder(Borders.doubleLine("MyGrid"));
            leftPanel.setPreferredSize(new TerminalSize(22, 10));

            mainPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
            mainPanel.addComponent(leftPanel.withBorder(Borders.singleLine("MyGrid")));
            mainPanel.addComponent(rightPanel.withBorder(Borders.singleLine("EnemyGrid")));

            myLabelsMatrix = new Label[Player.COLS][Player.ROWS];
            enemyLabelsMatrix = new Label[Player.COLS][Player.ROWS];
            //rows
            for (int i = 0; i < Player.ROWS; i++) {
                //cols
                for (int j = 0; j < Player.COLS; j++) {
                    myLabelsMatrix[j][i] = new Label("  ").setBackgroundColor(new TextColor.RGB(72, 116, 250)).setLabelWidth(2);
                    leftPanel.addComponent(myLabelsMatrix[j][i]);
                    enemyLabelsMatrix[j][i] = new Label("  ").setBackgroundColor(new TextColor.RGB(72, 116, 250)).setLabelWidth(2);
                    rightPanel.addComponent(enemyLabelsMatrix[j][i]);
                }
            }

            for (int i = 0; i < player.getShipSize(); i++) {
                myLabelsMatrix[i][0].setBackgroundColor(new TextColor.RGB(125, 56, 192));
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

    public Screen getScreen() {
        return screen;
    }
}
