package org.academiadecodigo.battleship;

import org.academiadecodigo.battleship.server.Object;

import java.io.Serializable;

/**
 * Created by codecadet on 18/11/16.
 */
public class Position implements Serializable{


    private int col;
    private int row;
    private char type;

    public Position(int col, int row) {
        this.col = col;
        this.row = row;
        type = Object.WATER.getSymbol();

    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

}