package org.academiadecodigo.battleship.server;

import org.academiadecodigo.battleship.Position;

/**
 * Created by codecadet on 18/11/16.
 */
public class Game {

    private PlayerHandler playerHandler1;
    private PlayerHandler playerHandler2;
    private Position[][] p1Grid;
    private Position[][] p2Grid;

    public Game(PlayerHandler playerHandler1, PlayerHandler playerHandler2) {
        this.playerHandler1 = playerHandler1;
        this.playerHandler2 = playerHandler2;
    }


    public void startGame() {

        playerHandler1.setGame(this);
        playerHandler2.setGame(this);
        playerHandler1.setName("Player1");
        playerHandler2.setName("Player2");

        playerHandler1.sendMessage("Populate your grid");
        playerHandler2.sendMessage("Populate your grid");

    }

    public void updateGrid(Position[][] grid) {
        if(Thread.currentThread().getName().equals("Player1")){
            p2Grid = grid;
        }else {
            p1Grid = grid;
        }
    }

    public synchronized void initialGrid(Position[][] grid){
        if(Thread.currentThread().getName().equals("Player1")){
            p1Grid = grid;
        }else {
            p2Grid = grid;
        }
    }
}
