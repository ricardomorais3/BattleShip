package org.academiadecodigo.battleship.server;

import org.academiadecodigo.battleship.Position;
import org.academiadecodigo.battleship.player.Ship;

import java.io.*;
import java.lang.*;
import java.lang.Object;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by codecadet on 18/11/16.
 */

public class PlayerHandler implements Runnable {

    /**
     * Connection between the player and the server
     */
    private Socket clientSocket;

    /**
     * Allows communication with the other player
     */
    private Game game;

    /**
     * Allows sending information to the player
     */
    private ObjectOutputStream out;

    /**
     * Creates a player handler and gives it a client socket
     * @param clientSocket Connection between the player and the server
     */
    public PlayerHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     * Receives the initial grids.
     * Contains the game logic: each player sends his enemy's grid updated, which is then sent to the enemy
     */
    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            // Send message to the Player handled by this object
            sendMessage("Waiting for other player");

            // Creates a reference for the initial grid, waits for the Player to send it
            // and when he does, stores it in the initialGrid reference
            Position[][] initialGrid;
            List<Ship> ships;

            initialGrid = (Position[][]) in.readObject();
            ships = (LinkedList) in.readObject();

            // Store the initial grid on the Game's reference to the grid
            game.initialGrid(initialGrid, ships);
            System.out.println("Begin Game!");

            Position[][] updatedGrid;
            Object readingObj;

            while (true) {
                readingObj = in.readObject();
                if (readingObj == null) {
                    System.out.println("closing connection");
                    return;
                }
                if (readingObj instanceof String) {
                    System.out.println("inside reading object");
                    game.gameOver();
                    readingObj = in.readObject();
                }
                updatedGrid = (Position[][]) readingObj;
                game.updateGrid(updatedGrid);
            }

        } catch (EOFException e) {
            System.out.println("Closing Connection");
            System.exit(1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void send(Object obj){
        try {
            out.reset();
            out.writeObject(obj);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(java.lang.Object obj) {
        send(obj);
    }

    public synchronized void sendGrid(Position[][] grid) {
        send(grid);
    }

    public void setName(String name) {
        Thread.currentThread().setName(name);
    }

    /**
     * Assigns a Game to this player
     * @param game Allows communication with the other player
     */
    public void setGame(Game game) {
        this.game = game;
    }
}
