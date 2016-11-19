package org.academiadecodigo.battleship.server;

import org.academiadecodigo.battleship.Position;

import java.io.*;
import java.net.Socket;

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


    public void sendMessage(String s) {
        try {
            out.writeObject(s);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendGrid(Position[][] grid){
        try {
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



            sendMessage("Waiting for other player");
            Position[][] initialGrid;


            initialGrid = (Position[][]) in.readObject();
            game.initialGrid(initialGrid);

            Position[][] updateGrid;


            while (true) {

                updateGrid = (Position[][]) in.readObject();
                game.updateGrid(updateGrid);
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
