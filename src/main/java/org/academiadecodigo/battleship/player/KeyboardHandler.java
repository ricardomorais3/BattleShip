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

                switch (key.getKeyType()) {
                    case Enter:
                        if (creatingGrid) {
                            player.shipPlacement();
                        } else {
                            player.shoot();
                        }
                        break;
                    default:
                        player.moveCursor(key.getKeyType());
                }
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