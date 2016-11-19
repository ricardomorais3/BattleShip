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

    private List<PlayerHandler> clientList;
    private int portNumber;
    private Socket clientSocket;
    private ServerSocket serverSocket;
    private Game game;


    public Server(int portNumber) {

        this.portNumber = portNumber;
        clientList = new ArrayList<>();
    }

    public static void main(String[] args) {
        Server server = new Server(2000);
        server.start();
    }


    public void start() {

        try {

            // Initialize the Server Socket
            serverSocket = new ServerSocket(portNumber);

            // Program stops here, waiting for Player 1 to connect
            System.out.println("Waiting for clients...");
            clientSocket = serverSocket.accept();
            System.out.println("client " + clientSocket.getInetAddress().getHostAddress() + " is now connected!");

            // Create a player handler for Player 1, which will run on a new thread
            PlayerHandler playerHandler = new PlayerHandler(clientSocket);
            Thread thread = new Thread(playerHandler);
            thread.start();

            // Program stops here, waiting for Player 2 to connect
            System.out.println("Waiting for a second client...");
            clientSocket = serverSocket.accept();
            System.out.println("client " + clientSocket.getInetAddress().getHostAddress() + "connected");

            // Create a player handler for Player 1, which will run on a new thread
            PlayerHandler playerHandler2 = new PlayerHandler(clientSocket);
            Thread thread2 = new Thread(playerHandler2);
            thread2.start();

            game = new Game(playerHandler,playerHandler2);
            game.startGame();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
