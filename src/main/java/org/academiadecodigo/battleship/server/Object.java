package org.academiadecodigo.battleship.server;

/**
 * Created by codecadet on 18/11/16.
 */

public enum Object {
    SHIP ('S'),
    SHIP_HIT('H'),
    SHIP_CRASHED('C'),
    WATER('W'),
    MISSED('M');

    private char symbol;

    Object(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public char getReverse(){
        switch (this){
            case SHIP:
                return 'H';
            case WATER:
                return 'M';
            default:
                return 'A';
        }
    }
}


