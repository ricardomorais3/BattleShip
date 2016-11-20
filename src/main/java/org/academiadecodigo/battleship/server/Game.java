package org.academiadecodigo.battleship.server;

import org.academiadecodigo.battleship.Position;
import org.academiadecodigo.battleship.player.Ship;
import java.util.List;

/**
 * Created by codecadet on 18/11/16.
 */
public class Game {
    private PlayerHandler playerHandler1;
    private PlayerHandler playerHandler2;
    private Position[][] p1Grid;
    private Position[][] p2Grid;
    private boolean startShooting;
    private List<Ship> p1Ships;
    private List<Ship> p2Ships;

    public Game(PlayerHandler playerHandler1, PlayerHandler playerHandler2) {
        this.playerHandler1 = playerHandler1;
        this.playerHandler2 = playerHandler2;
        startShooting = true;
    }

    public void startGame() {
        playerHandler1.setGame(this);
        playerHandler2.setGame(this);
        playerHandler1.setName("Player1");
        playerHandler2.setName("Player2");

        playerHandler1.sendMessage("Populate your grid");
        playerHandler2.sendMessage("Populate your grid");
    }

    public synchronized void updateGrid(Position[][] grid) {
        if(Thread.currentThread().getName().equals("Player1")){
            p2Grid = grid;
            playerHandler2.sendGrid(grid);
        }else {
            p1Grid = grid;
            playerHandler1.sendGrid(grid);
        }
    }

    public synchronized void initialGrid(Position[][] grid, List ships){
        System.out.println(Thread.currentThread().getName());
        if(Thread.currentThread().getName().equals("Player1")){
            p1Grid = grid;
            p1Ships = ships;
            startShooting = !startShooting;
        }else {
            p2Grid = grid;
            p2Ships = ships;
            startShooting = !startShooting;
        }if (startShooting){
            sendTurn();
        }
    }

    public synchronized void gameOver() {
        if(Thread.currentThread().getName().equals("Player1")){
            playerHandler2.sendMessage("You Lost");
        }else {
            playerHandler1.sendMessage("You Lost");
        }
    }

    private void sendTurn(){
        if(Math.random() < 0.5){
            playerHandler1.sendMessage(true);
            playerHandler1.sendMessage(true);
            playerHandler2.sendMessage(false);
            playerHandler2.sendMessage(true);
        }else {
            playerHandler1.sendMessage(false);
            playerHandler1.sendMessage(true);
            playerHandler2.sendMessage(true);
            playerHandler2.sendMessage(true);
        }

        playerHandler1.sendGrid(p2Grid);
        playerHandler1.sendMessage(p2Ships);
        playerHandler2.sendGrid(p1Grid);
        playerHandler2.sendMessage(p1Ships);
    }
}