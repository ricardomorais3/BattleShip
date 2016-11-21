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
    /**
     * Allows sending information to the server
     */
    private ObjectOutputStream out;

    /**
     * The Player's grid of positions
     */
    private Position[][] myGrid;

    /**
     * The enemy's grid of positions
     */
    private Position[][] enemyGrid;

    /**
     * Contains the enemy's list of ships
     */
    private List<Ship> ships;

    /**
     * Current position of the cursor
     */
    private Position myPos;

    /**
     * Lanterna object
     */
    private Lanterna lanterna;

    /**
     * Orientation of the ship being placed, during the ship placement phase
     */
    private boolean horizontal;

    /**
     * Flag that signals when it's the player's turn to shoot
     */
    private boolean canShoot;

    /**
     * Flag that signals the beginning of the rounds
     */
    private boolean startGame;

    /**
     * Size of the ship being placed, during the ship placement phase
     */
    private int shipSize;

    /**
     * Number of ships already created during the ship placement phase
     */
    private int shipsCreated;

    /**
     * Total number of ships to place in the grid
     */
    private final int NUMSHIPS;

    /**
     * Dimensions of the grid
     */
    public static final int COLS = 10;
    public static final int ROWS = 10;

    /**
     * Creates a player
     */
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

    /**
     * Contains the logic of the player
     */
    private void start() {
        try {
            Socket socket = new Socket(InetAddress.getByName("localhost"), 2000);
            out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Cursor's initial position
            myPos = new Position(0, 0);

            String message = (String) in.readObject();
            System.out.println(message);

            message = (String) in.readObject();
            System.out.println(message);

            // Creates the grid
            myGrid = new Position[COLS][ROWS];
            createGrid(myGrid);

            // Initializes the the lanterna object and runs it on a new thread
            lanterna = new Lanterna(this);
            Thread gfx = new Thread(lanterna);
            gfx.start();

            // Receives the Flags that signal the beginning of the rounds
            // and if this player is the first to shoot or not
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
            java.lang.Object readingObj;

            // Receives the player's updated grid until someone wins
            while (true) {
                readingObj = in.readObject();
                lanterna.changePanelTitle("It's your turn");

                if (readingObj instanceof String){
                    lanterna.changePanelTitle("You Lost");

                    Thread.sleep(5000);

                    System.exit(1);

                    readingObj = in.readObject();
                }

                myGrid = (Position[][]) readingObj;
                lanterna.rePaintMyGrid2();
                canShoot = true;
            }
        }catch (EOFException e){
            System.out.println("Server closed the connection");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Server hasn't answered the connection.");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logic of placing the ships
     */
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

    /**
     * Places a ship on the grid
     * @param position Initial postion of the ship
     * @param isHorizontal Orientation of the ship
     * @param shipSize Size of the ship
     * @param grid Grid where the place is placed
     */
    public void placeShip(Position position, boolean isHorizontal, int shipSize, Position[][] grid) {
        for (int i = 0; i < shipSize; i++) {
            if (isHorizontal) {
                grid[position.getCol() + i][position.getRow()].setType(Object.SHIP.getSymbol());

            } else {
                grid[position.getCol()][position.getRow() + i].setType(Object.SHIP.getSymbol());
            }
        }
    }

    /**
     * Shoots the enemy's grid
     */
    public synchronized void shoot() {

        if (!canShoot || !startGame) {
            return;
        }

        if (collisonDetectorInShooting()) {
            canShoot = false;
            // If a ship was hit, verifies if it was crashed
            // if yes, removes it from the list of ships
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
            }

            lanterna.changePanelTitle("It's your enemy's turn");
            enemyGrid[myPos.getCol()][myPos.getRow()].setType(Object.getReverse(enemyGrid[myPos.getCol()][myPos.getRow()].getType()));
            lanterna.rePaintEnemyGrid(myPos);

            // When all enemy ships are removed from the grid, the player wins
            try {
                out.reset();
                if(ships.size() == 0){
                    lanterna.changePanelTitle("Winner");
                    out.writeObject("Winner");
                    Thread.sleep(5000);
                    System.exit(1);
                }
                out.writeObject(enemyGrid);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Populates the player's grid with positions
     * @param grid The player's grid
     */
    public void createGrid(Position[][] grid) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                grid[j][i] = new Position(i, j);
            }
        }
    }

    /**
     * Verifies if the cursor is trying to move outside of bounds, and keeps it inside
     * @param key The input key
     * @return If the move allowed or not
     */
    public boolean outOfBounds(KeyType key) {
        if (horizontal) {
            if (key.equals(KeyType.ArrowDown)) {
                if (myPos.getRow() < ROWS - 1) {
                    myPos = new Position(myPos.getCol(), myPos.getRow() + 1);
                    return false;
                }
            } else if (key.equals(KeyType.ArrowRight)) {
                if ((myPos.getCol() + (shipSize)) < COLS) {
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
                if (((myPos.getRow() + (shipSize) - 1) < ROWS) && myPos.getRow() >= 0) {
                    horizontal = !horizontal;
                    return false;
                }
            }
            return true;
        } else {
            if (key.equals(KeyType.ArrowDown)) {
                if ((myPos.getRow() + (shipSize)) < ROWS) {
                    myPos = new Position(myPos.getCol(), myPos.getRow() + 1);
                    return false;
                }
            } else if (key.equals(KeyType.ArrowRight)) {
                if (myPos.getCol() < COLS-1) {
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
                if ((myPos.getCol() + (shipSize) - 1) < COLS && myPos.getCol() >= 0) {
                    horizontal = !horizontal;
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Moves the cursor in the desired direction
     * @param keyType Keyboard input
     */
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

    /**
     * Verifies if the ship can be placed without colliding with another ship
     * @param isHorizontal Orientation
     * @param shipSize Size of the ship
     * @return If the placement is allowed
     */
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

    /**
     * Verifies if the position has already been shot or not
     * @return true is the position hasn't been shot
     */
    private boolean collisonDetectorInShooting() {
        return (enemyGrid[myPos.getCol()][myPos.getRow()].getType() == 'W' ||
                enemyGrid[myPos.getCol()][myPos.getRow()].getType() == 'S');
    }

    /**
     * Gets the size of the ship being placed
     * @return Ship size
     */
    public int getShipSize() {
        return shipSize;
    }

    /**
     * Gets the player's grid
     * @return Player's grid
     */
    public Position[][] getMyGrid() {
        return myGrid;
    }

    /**
     * Gets the player's grid
     * @return Enemy's grid
     */
    public Position[][] getEnemyGrid() {
        return enemyGrid;
    }
}
