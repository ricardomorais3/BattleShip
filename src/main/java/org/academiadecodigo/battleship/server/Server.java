package org.academiadecodigo.battleship.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by codecadet on 18/11/16.
 */
public class Server {
    /**
     * Indicates the port where the server is listening
     */
    private int portNumber;

    /**
     * Creates a Server, given a port number
     * @param portNumber port where the server is listening
     */
    public Server(int portNumber) {
        this.portNumber = portNumber;
    }

    public static void main(String[] args) {
        Server server = new Server(2000);
        server.start();
    }

    /**
     * Initializes the server socket and waits for players to connect to the server.
     * Creates a player handler for each player and runs it on a new thread
     */
    public void start() {
        try {
            // Initialize the Server Socket
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Socket clientSocket;

            // Program stops, waiting for Player 1 to connect
            System.out.println("Waiting for clients...");
            clientSocket = serverSocket.accept();
            System.out.println("client " + clientSocket.getInetAddress().getHostAddress() + " is now connected!");

            // Create a player handler for Player 1, which will run on a new thread
            PlayerHandler playerHandler = new PlayerHandler(clientSocket);
            Thread thread = new Thread(playerHandler);
            thread.setName("Player1");
            thread.start();

            // Program stops, waiting for Player 2 to connect
            System.out.println("Waiting for a second client...");
            clientSocket = serverSocket.accept();
            System.out.println("client " + clientSocket.getInetAddress().getHostAddress() + "connected");

            // Create a player handler for Player 1, which will run on a new thread
            PlayerHandler playerHandler2 = new PlayerHandler(clientSocket);
            Thread thread2 = new Thread(playerHandler2);
            thread2.setName("Player2");
            thread2.start();

            //Delay to let both PlayerHandler threads be created, so the game doesn't start while they are being created.
            Thread.sleep(1000);
            Game game = new Game(playerHandler,playerHandler2);
            game.startGame();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
