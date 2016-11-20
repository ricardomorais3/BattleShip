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

            // Waiting for player input
            while (true) {
                readingObj = in.readObject();
                if (readingObj == null) {
                    System.out.println("closing connection");
                    return;
                }
                // If a String is sent by a client, it means an end result has happened.
                if (readingObj instanceof String) {
                    System.out.println("inside reading object");
                    // Game over method
                    game.gameOver();
                    // String that includes the end result
                    readingObj = in.readObject();
                }
                //Refresh grid with the latest round
                updatedGrid = (Position[][]) readingObj;
                game.updateGrid(updatedGrid);
            }

        } catch (EOFException e) {
            System.out.println("Closing Connection");
            System.exit(1);
        } catch (ClassNotFoundException e) {
            //When a client sends an unknown object, print the stack trace
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends an object
     * @param obj Object
     */
    private void send(Object obj){
        try {
            out.reset(); //To avoid accumulating the buffer, the buffer shall be reset before writing in it.
            out.writeObject(obj);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Send message
     * @param obj Message
     */
    public void sendMessage(java.lang.Object obj) {
        send(obj);
    }

    /**
     * Sends the grid
     * @param grid
     */

    public synchronized void sendGrid(Position[][] grid) {
        send(grid);
    }

    /**
     * Sets the name of the current thread
     * @param name String
     */
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
