package org.academiadecodigo.battleship.server;

import org.academiadecodigo.battleship.Position;
import org.academiadecodigo.battleship.player.Ship;

import java.io.*;
import java.lang.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by codecadet on 18/11/16.
 */

public class PlayerHandler implements Runnable {
    private Socket clientSocket;
    private Game game;
    private ObjectInputStream in;
    private ObjectOutputStream out;


    public PlayerHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void setGame(Game game) {
        this.game = game;
    }


    public void sendMessage(java.lang.Object obj) {
        try {
            out.reset();
            out.writeObject(obj);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendGrid(Position[][] grid){
        try {
            out.reset();
            out.writeObject(grid);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

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

            while (true) {
                updatedGrid = (Position[][]) in.readObject();
                game.updateGrid(updatedGrid);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setName(String name) {
        Thread.currentThread().setName(name);
    }
}
