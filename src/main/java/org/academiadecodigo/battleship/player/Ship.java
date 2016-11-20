package org.academiadecodigo.battleship.player;

import org.academiadecodigo.battleship.Position;

import java.io.Serializable;

/**
 * Created by codecadet on 20/11/16.
 */
public class Ship implements Serializable{
    private Position[] positions;
    private boolean crashed;
    private int numberHits;

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

    public boolean isCrashed() {
        return crashed;
    }

    public Position[] getPositions() {
        return positions;
    }
}
