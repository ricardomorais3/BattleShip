package org.academiadecodigo.battleship.player;

import com.googlecode.lanterna.input.KeyType;
import org.academiadecodigo.battleship.server.Object;
import org.academiadecodigo.battleship.Position;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by codecadet on 18/11/16.
 */
public class Player {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private BufferedReader keyBoardInput;
    private Position[][] grid;
    private boolean horizontal;
    private Position myPos;
    private Lanterna lanterna;
    private int shipSize;
    private KeyboardHandler keyboardHandler;


    public static void main(String[] args) {
        Player player = new Player();
        player.start();
    }

    private void start() {

        try {
            socket = new Socket(InetAddress.getByName("localhost"), 2000);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            keyBoardInput = new BufferedReader(new InputStreamReader(System.in));
            myPos = new Position(0,0);

           /* while (true) {
                java.lang.Object object = in.readObject();
                if (object instanceof String) {
                    System.out.println((String) object);
                } else {
                    System.out.println("another type");
                }
            }*/

            String message = (String)in.readObject();
            System.out.println(message);
            message = (String)in.readObject();
            System.out.println(message);

            grid = new Position[10][10];

            createGrid(grid);

            lanterna = new Lanterna(this);
            Thread gfx = new Thread(lanterna);
            gfx.start();

            System.out.println("i'm here");
            shipSize = 3;
            horizontal = true;

            in.read();

        } catch (IOException e) {
            System.out.println("Server hasn't answered the connection.");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void placeShip(Position position, boolean isHorizontal, int shipSize, Position[][] grid) {
        for (int i = 0; i < shipSize; i++) {
            if (isHorizontal) {
                grid[position.getCol() + i][position.getRow()].setType(Object.SHIP.getSymbol());

            } else {
                grid[position.getCol()][position.getRow() + i].setType(Object.SHIP.getSymbol());
            }
        }
    }

    private void hitShip(int col, int row) {

        //TODO: grid = Objects.getSymbol();
        grid[row][col].setType(Object.SHIP.getReverse());
    }

    private void shipDestroyed(int row, int col) {
        // TODO: 18/11/16 change all elementes of the ship to a new one
        grid[row][col].setType(Object.SHIP_CRASHED.getSymbol());
    }

    public void createGrid(Position[][] grid) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                grid[j][i] = new Position(i, j);
            }
        }
    }


    public boolean outOfBounds(KeyType key) {

        if (horizontal) {
            if (key.equals(KeyType.ArrowDown)) {
                if (myPos.getRow() < 9) {
                    myPos = new Position(myPos.getCol(), myPos.getRow() + 1);
                    return false;
                }
            } else if (key.equals(KeyType.ArrowRight)) {
                if ((myPos.getCol() + (shipSize) - 1) < 9) {
                    myPos = new Position(myPos.getCol() + 1, myPos.getRow());
                    return false;
                }
            } else if (key.equals(KeyType.ArrowLeft)) {
                if (myPos.getCol() > 0) {
                    myPos = new Position(myPos.getCol() - 1, myPos.getRow());
                    return false;
                }
            } else if (key.equals(KeyType.ArrowUp)) {
                if (myPos.getRow() > 0) {
                    myPos = new Position(myPos.getCol(), myPos.getRow() - 1);
                    return false;
                }
            } else if (key.equals(KeyType.Tab)) {
                if (((myPos.getRow() + (shipSize) - 1) < 10) && myPos.getRow() >= 0) {
                    horizontal = !horizontal;
                    return false;
                }
            }
            return true;
        } else {
            if (key.equals(KeyType.ArrowDown)) {
                if ((myPos.getRow() + (shipSize) - 1) < 9) {
                    myPos = new Position(myPos.getCol(), myPos.getRow() + 1);
                    return false;
                }
            } else if (key.equals(KeyType.ArrowRight)) {
                if (myPos.getCol() < 9) {
                    myPos = new Position(myPos.getCol() + 1, myPos.getRow());
                    return false;
                }
            } else if (key.equals(KeyType.ArrowLeft)) {
                if (myPos.getCol() > 0) {
                    myPos = new Position(myPos.getCol() - 1, myPos.getRow());
                    return false;
                }
            } else if (key.equals(KeyType.ArrowUp)) {
                if (myPos.getRow() > 0) {
                    myPos = new Position(myPos.getCol(), myPos.getRow() - 1);
                    return false;
                }
            } else if (key.equals(KeyType.Tab)) {
                if ((myPos.getCol() + (shipSize) - 1) < 10 && myPos.getCol() >= 0) {
                    horizontal = !horizontal;
                    return false;
                }
            }
            return true;
        }
    }

    public void moveCursor(KeyType keyType){
        if(keyType.equals(KeyType.Enter)){
            if(!collisionDetector(myPos, horizontal, shipSize, grid)){
                placeShip(myPos, horizontal, shipSize, grid);
                lanterna.rePaint(myPos, shipSize, horizontal);
            }
            return;
        }
        if(!outOfBounds(keyType)){
            System.out.println(myPos.getCol() + " " + myPos.getRow());
            lanterna.rePaint(myPos, shipSize, horizontal);
        }
    }

    private boolean collisionDetector(Position position, boolean isHorizontal, int shipSize, Position[][] grid){
        if(isHorizontal){
            for (int i = 0; i < shipSize; i++) {
                if((grid[position.getCol() + i][position.getRow()].getType() == 'W')){
                    continue;
                }
                return true;
            }
        }else{
            for (int i = 0; i < shipSize; i++) {
                if((grid[position.getCol()][position.getRow() + i].getType() == 'W')){
                    continue;
                }
                return true;
            }
        }
        return false;
    }

    public Position getMyPos() {
        return myPos;
    }

    public void setMyPos(Position myPos) {
        this.myPos = myPos;
    }

    public int getShipSize() {
        return shipSize;
    }

    public Position[][] getGrid() {
        return grid;
    }
}
