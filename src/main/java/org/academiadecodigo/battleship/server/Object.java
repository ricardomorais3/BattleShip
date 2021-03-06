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

    public static char getReverse(char c){
        switch (c){
            case 'S':
                return 'H';
            case 'W':
                return 'M';
            case 'C':
                return 'C';
            default:
                return 'A';
        }
    }
}


