package org.academiadecodigo.battleship.server;

import org.academiadecodigo.battleship.Position;
import org.academiadecodigo.battleship.player.Ship;
import java.util.List;

/**
 * Created by codecadet on 18/11/16.
 */
public class Game {

    /**
     * Player1 connection with a game assigned to it
     */
    private PlayerHandler playerHandler1;

    /**
     * Player2 connection with a game assigned to it
     */
    private PlayerHandler playerHandler2;

    /**
     * Player1 grid (matrix)
     */
    private Position[][] p1Grid;

    /**
     * Player2 grid (matrix)
     */
    private Position[][] p2Grid;

    /**
     * Flag that warns the beginning of the rounds.
     */
    private boolean startShooting;

    /**
     * Player1 list of ships
     */
    private List<Ship> p1Ships;

    /**
     * Player2 list of ships
     */
    private List<Ship> p2Ships;

    /**
     * Creates a game given two player handlers
     * @param playerHandler1 Player1
     * @param playerHandler2 Player2
     */
    public Game(PlayerHandler playerHandler1, PlayerHandler playerHandler2) {
        this.playerHandler1 = playerHandler1;
        this.playerHandler2 = playerHandler2;
        startShooting = true;
    }

    /**
     * Assigns the same game to both players, and sets a name for each Player threads
     */
    public void startGame() {
        playerHandler1.setGame(this);
        playerHandler2.setGame(this);
        playerHandler1.setName("Player1");
        playerHandler2.setName("Player2");

        playerHandler1.sendMessage("Populate your grid");
        playerHandler2.sendMessage("Populate your grid");
    }

    /**
     * Identifies the player calling this method,
     * stores his initial grid and list of ships
     * @param grid Player's initial grid
     * @param ships Player's list of ships
     */
    public synchronized void initialGrid(Position[][] grid, List ships){
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

    /**
     * Updates the enemy's grid and sends it to the player
     * @param grid Enemy's grid
     */
    public synchronized void updateGrid(Position[][] grid) {
        if(Thread.currentThread().getName().equals("Player1")){
            p2Grid = grid;
            playerHandler2.sendGrid(grid);
        }else {
            p1Grid = grid;
            playerHandler1.sendGrid(grid);
        }
    }

    /**
     * Sends a message to the looser
     */
    public synchronized void gameOver() {
        if(Thread.currentThread().getName().equals("Player1")){
            playerHandler2.sendMessage("You Lost");
        }else {
            playerHandler1.sendMessage("You Lost");
        }
    }

    /**
     * Chooses the first player to shoot,
     * sends the enemy's grid and list of ships to the player
     */
    private void sendTurn(){
        if(Math.random() < 0.5){
            playerHandler1.sendMessage(true); // Flag canShoot
            playerHandler1.sendMessage(true); // Flag startGame
            playerHandler2.sendMessage(false); // Flag canShoot
            playerHandler2.sendMessage(true); // Flag startGame
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