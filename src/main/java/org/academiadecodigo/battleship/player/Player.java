package org.academiadecodigo.battleship.player;

import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.input.KeyType;
import org.academiadecodigo.battleship.server.Object;
import org.academiadecodigo.battleship.Position;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by codecadet on 18/11/16.
 */
public class Player {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Position[][] myGrid;
    private Position[][] enemyGrid;
    private boolean horizontal;
    private Position myPos;
    private Lanterna lanterna;
    private int shipSize;
    private final int NUMSHIPS;
    private int shipsCreated;
    private boolean canShoot;
    private boolean startGame;
    private List<Ship> ships;

    public Player() {
        NUMSHIPS = 4;
        shipSize = 1;
        horizontal = true;
        ships = new LinkedList<>();
    }

    public static void main(String[] args) {
        Player player = new Player();
        player.start();
    }

    private void start() {

        try {
            socket = new Socket(InetAddress.getByName("localhost"), 2000);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            myPos = new Position(0, 0);

           /* while (true) {
                java.lang.Object object = in.readObject();
                if (object instanceof String) {
                    System.out.println((String) object);
                } else {
                    System.out.println("another type");
                }
            }*/

            String message = (String) in.readObject();
            System.out.println(message);

            message = (String) in.readObject();
            System.out.println(message);

            myGrid = new Position[10][10];

            createGrid(myGrid);

            lanterna = new Lanterna(this);
            Thread gfx = new Thread(lanterna);
            gfx.start();

            canShoot = (boolean) in.readObject();
            startGame = (boolean) in.readObject();
            enemyGrid = (Position[][]) in.readObject();
            ships = (LinkedList) in.readObject();

            if (canShoot) {
                lanterna.changePanelTitle("It's your turn");
            } else {
                lanterna.changePanelTitle("It's your enemy's turn");
            }
            lanterna.rePaintEnemyGrid(myPos);


            while (true) {
                myGrid = (Position[][]) in.readObject();

                lanterna.rePaintMyGrid2();

                canShoot = true;
                lanterna.changePanelTitle("It's your turn");

            }

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

    public synchronized void shoot() {
        //TODO: myGrid = Objects.getSymbol();
        if (!canShoot || !startGame) {
            return;
        }

        if (collisonDetectorInShooting()) {
            canShoot = false;
            if(enemyGrid[myPos.getCol()][myPos.getRow()].getType() == 'S'){
                for (int i = 0; i < ships.size(); i++) {
                    ships.get(i).verifyHit(myPos);
                    if (ships.get(i).isCrashed()){
                        for (int j = 0; j < ships.get(i).getPositions().length; j++) {
                            enemyGrid[ships.get(i).getPositions()[j].getCol()][ships.get(i).getPositions()[j].getRow()].setType(Object.SHIP_CRASHED.getSymbol());
                        }
                        ships.remove(i);
                    }
                }

                if(ships.size() == 0){
                    System.out.println("winner");
                }
            }
            lanterna.changePanelTitle("It's your enemy's turn");

            enemyGrid[myPos.getCol()][myPos.getRow()].setType(Object.getReverse(enemyGrid[myPos.getCol()][myPos.getRow()].getType()));



            //repaint
            lanterna.rePaintEnemyGrid(myPos);

            try {
                out.reset();
                out.writeObject(enemyGrid);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void shipDestroyed(int row, int col) {
        // TODO: 18/11/16 change all elementes of the ship to a new one
        myGrid[row][col].setType(Object.SHIP_CRASHED.getSymbol());
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

    public void moveCursor(KeyType keyType) {
        if (!outOfBounds(keyType)) {

            if (lanterna.getKeyboardHandler().isCreatingGrid()) {
                lanterna.rePaintMyGrid(myPos, shipSize, horizontal);
            } else {
                if (startGame) {
                    lanterna.rePaintEnemyGrid(myPos);
                }
            }

        }
    }

    private boolean collisionDetectorInPlacement(boolean isHorizontal, int shipSize) {
        if (isHorizontal) {
            for (int i = 0; i < shipSize; i++) {
                if ((myGrid[myPos.getCol() + i][myPos.getRow()].getType() == 'W')) {
                    continue;
                }
                return true;
            }
        } else {
            for (int i = 0; i < shipSize; i++) {
                if ((myGrid[myPos.getCol()][myPos.getRow() + i].getType() == 'W')) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }

    private boolean collisonDetectorInShooting() {
        return (enemyGrid[myPos.getCol()][myPos.getRow()].getType() == 'W' ||
                enemyGrid[myPos.getCol()][myPos.getRow()].getType() == 'S');
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

    public Position[][] getMyGrid() {
        return myGrid;
    }

    public void shipPlacement() {
        if (shipsCreated < NUMSHIPS) {
            if (!collisionDetectorInPlacement(horizontal, shipSize)) {
                placeShip(myPos, horizontal, shipSize, myGrid);
                ships.add(new Ship(myPos, horizontal, shipSize));
                shipsCreated++;
                shipSize++;
                myPos.setCol(0);
                myPos.setRow(0);
                if (shipsCreated == NUMSHIPS) {
                    shipSize = 1;
                    lanterna.getKeyboardHandler().setCreatingGrid(false);
                    try {
                        lanterna.changePanelTitle("Waiting for your enemy...");
                        lanterna.rePaintMyGrid2();
                        out.writeObject(myGrid);
                        out.writeObject(ships);
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                lanterna.rePaintMyGrid(myPos, shipSize, horizontal);
            }
        }
    }

    public Position[][] getEnemyGrid() {
        return enemyGrid;
    }
}
