package org.academiadecodigo.battleship.player;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import org.academiadecodigo.battleship.Position;

import java.io.IOException;

/**
 * Created by codecadet on 18/11/16.
 */
public class KeyboardHandler implements Runnable {
    private Lanterna lanterna;
    private Player player;


    public KeyboardHandler(Lanterna lanterna, Player player) {
        this.lanterna = lanterna;
        this.player = player;

        System.out.println("construtor");
    }

    @Override
    public void run() {
        try {
            KeyStroke key;


            while (true) {
                key = lanterna.getScreen().readInput();
                System.out.println("using keyboard");

                player.moveCursor(key.getKeyType());


                /*switch (key.getKeyType()){
                    case Tab:

                        break;
                    case ArrowDown:

                        break;
                    case ArrowUp:

                        break;
                    case ArrowLeft:

                        break;
                    case ArrowRight:

                        break;
                }*/


/*                if (key.getKeyType().equals(KeyType.Tab)) {
                    if (player.outOfBounds(KeyType.Tab)) {
                        horizontal = !horizontal;
                        lanterna.teste();
                    }
                }
// TODO: 18/11/16 Limits


                if (horizontal) {


                    System.out.println(player.getMyPos());
                    if (key.getKeyType().equals(KeyType.ArrowDown)) {
                        if (player.getMyPos().getRow() < 9) {
                            player.setMyPos(new Position(player.getMyPos().getCol(), player.getMyPos().getRow() + 1));
                            lanterna.teste();
                        }


                    } else if (key.getKeyType().equals(KeyType.ArrowRight)) {
                        if ((player.getMyPos().getCol() + (player.getBoatSize() * 2) - 2) < 19) {

                            player.setMyPos(new Position(player.getMyPos().getCol() + 2, player.getMyPos().getRow()));

                            lanterna.teste();
                        }

                    } else if (key.getKeyType().equals(KeyType.ArrowLeft)) {
                        if (player.getMyPos().getCol() > 1) {

                            player.setMyPos(new Position(player.getMyPos().getCol() - 2, player.getMyPos().getRow()));
                            lanterna.teste();
                        }
                    } else if (key.getKeyType().equals(KeyType.ArrowUp)) {
                        if (player.getMyPos().getRow() > 0) {

                            player.setMyPos(new Position(player.getMyPos().getCol(), player.getMyPos().getRow() - 1));
                            lanterna.teste();
                        }

                    }
                } else {
                    if (key.getKeyType().equals(KeyType.ArrowDown)) {
                        if ((player.getMyPos().getRow() + (player.getBoatSize()) - 1) < 9) {
                            player.setMyPos(new Position(player.getMyPos().getCol(), player.getMyPos().getRow() + 1));
                            lanterna.teste();
                        }


                    } else if (key.getKeyType().equals(KeyType.ArrowRight)) {
                        if (player.getMyPos().getCol() < 19) {

                            player.setMyPos(new Position(player.getMyPos().getCol() + 2, player.getMyPos().getRow()));

                            lanterna.teste();
                        }


                    } else if (key.getKeyType().equals(KeyType.ArrowLeft)) {
                        if (player.getMyPos().getCol() > 1) {

                            player.setMyPos(new Position(player.getMyPos().getCol() - 2, player.getMyPos().getRow()));
                            lanterna.teste();
                        }
                    } else if (key.getKeyType().equals(KeyType.ArrowUp)) {
                        if (player.getMyPos().getRow() > 0) {

                            player.setMyPos(new Position(player.getMyPos().getCol(), player.getMyPos().getRow() - 1));
                            lanterna.teste();
                        }

                    }
                }*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

