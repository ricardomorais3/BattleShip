package org.academiadecodigo.battleship.player;

import org.academiadecodigo.battleship.Position;

import java.io.Serializable;

/**
 * Created by codecadet on 20/11/16.
 */
public class Ship implements Serializable{

    /**
     * Contains the positions of this ship
     */
    private Position[] positions;

    /**
     * Flag that indicate that all positions were hit
     */
    private boolean crashed;

    /**
     * Number of positions that were hit
     */
    private int numberHits;

    /**
     * Creates a ship given a position, orientation and size
     * @param position Initial position of the ship
     * @param horizontal Ship orientation
     * @param size Ship size
     */
    public Ship(Position position, boolean horizontal, int size){
        positions = new Position[size];

        for (int i = 0; i < size; i++) {
            if(horizontal){
                positions[i] = new Position(position.getCol() + i, position.getRow());
            }else {
                positions[i] = new Position(position.getCol(), position.getRow() + i);
            }
        }
    }

    /**
     * Verifies if some ship was hit and changes the flag "crashed" if all position of a ship have been hit
     * @param position gives a position to verify
     * @return if some ship was hit or not
     */
    public boolean verifyHit(Position position){
        if(!crashed) {
            for (int i = 0; i < positions.length; i++) {
                if (position.getCol() == positions[i].getCol() &&
                        position.getRow() == positions[i].getRow()) {
                    numberHits++;
                    if (numberHits == positions.length) {
                        crashed = true;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets the boolean crashed
     */
    public boolean isCrashed() {
        return crashed;
    }

    /**
     * Gets the position
     */
    public Position[] getPositions() {
        return positions;
    }
}
