package org.academiadecodigo.battleship.player;

import com.googlecode.lanterna.input.KeyStroke;

import java.io.IOException;

/**
 * Created by codecadet on 18/11/16.
 */
public class KeyboardHandler implements Runnable {

    /**
     * Java library
     */
    private Lanterna lanterna;

    /**
     * Reference to the player
     */
    private Player player;

    /**
     * Flag the indicates the functionality of the ENTER key
     */
    private boolean creatingGrid;

    /**
     * Creates a Keyboard handler
     * @param lanterna Java Library
     * @param player Reference to the player
     */
    public KeyboardHandler(Lanterna lanterna, Player player) {
        this.lanterna = lanterna;
        this.player = player;
        creatingGrid = true;
    }

    /**
     * Gets input from the keyboard and calls the desired method
     */
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

    /**
     * Sets the creatingGrid flag
     * @param creatingGrid creatingGrid flag
     */
    public void setCreatingGrid(boolean creatingGrid) {
        this.creatingGrid = creatingGrid;
    }

    /**
     * Gets the value of the flag creatingGrid
     * @return
     */
    public boolean isCreatingGrid() {
        return creatingGrid;
    }
}