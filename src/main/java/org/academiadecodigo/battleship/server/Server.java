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
            serverSocket = new ServerSocket(portNumber);

            System.out.println("waiting for clients...");
            clientSocket = serverSocket.accept();
            System.out.println("client " + clientSocket.getInetAddress().getHostAddress() + "connected");
            PlayerHandler playerHandler = new PlayerHandler(clientSocket);

            Thread thread = new Thread(playerHandler);
            thread.start();


            System.out.println("waiting for player 2..");
            clientSocket = serverSocket.accept();
            System.out.println("client " + clientSocket.getInetAddress().getHostAddress() + "connected");
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
