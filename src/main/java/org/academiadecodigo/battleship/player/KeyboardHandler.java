package org.academiadecodigo.battleship.player;

import com.googlecode.lanterna.input.KeyStroke;

import java.io.IOException;

/**
 * Created by codecadet on 18/11/16.
 */
public class KeyboardHandler implements Runnable {

    private Lanterna lanterna;
    private Player player;
    private boolean creatingGrid;


    public KeyboardHandler(Lanterna lanterna, Player player) {
        this.lanterna = lanterna;
        this.player = player;
        creatingGrid = true;
    }

    @Override
    public void run() {

        try {

            KeyStroke key;

            while (true) {
                key = lanterna.getScreen().readInput();
                //System.out.println("using key " + key.getKeyType().name());

                switch (key.getKeyType()) {
                    case Enter:
                        if(creatingGrid){
                            player.shipPlacement();

                        }else {
                            player.shoot();
                        }
                        break;
                    default:
                        player.moveCursor(key.getKeyType());
                }

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

                if (horizontal) {


                    System.out.println(player.getMyPos());
                    if (key.getKeyType().equals(KeyType.ArrowDown)) {
                        if (player.getMyPos().getRow() < 9) {
                            player.setMyPos(new Position(player.getMyPos().getCol(), player.getMyPos().getRow() + 1));
                            lanterna.teste();
                        }


                    } else if (key.getKeyType().equals(KeyType.ArrowRight)) {
                        if ((player.getMyPos().getCol() + (player.getShipSize() * 2) - 2) < 19) {

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
                        if ((player.getMyPos().getRow() + (player.getShipSize()) - 1) < 9) {
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

    public void setCreatingGrid(boolean creatingGrid) {
        this.creatingGrid = creatingGrid;
    }

    public boolean isCreatingGrid() {
        return creatingGrid;
    }
}

