package modele;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Observable;
import java.util.Random;
import java.util.Scanner;

import static java.sql.Types.NULL;
import static vue_controleur.Swing2048.PIXEL_PER_SQUARE;

public class Game extends Observable {

    private HashMap<Cell, Point> cells;
    private Cell[][] tabCells;
    private static Random rnd = new Random(4);
    private File data = new File("score.txt");
    private Instant instantStart;
    private double timeElapsed;
    private boolean gameOver;
    private int unlock;
    private boolean unlockRunning;
    private Point unlockedPosition;


    /**
     * Constructeur du jeu en fonction de la taille de la grille passée en paramètre
     * @param size entier représentant la taille de la grille du jeu
     */
    public Game(int size) {
        unlock = 100;
        unlockRunning = false;
        gameOver = false;
        this.tabCells = new Cell[size][size];

        this.cells = new HashMap<>();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                tabCells[i][j] = new Cell();
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tabCells[i][j].setGame(this);
            }
        }

        rnd();
        rnd();
        instantStart = Instant.now();
        ThreadGetActualTime();
    }

    /**
     * retourne un booléen qui dit si la partie est perdue ou non
     */
    public boolean getGameOver() {
        return gameOver;
    }

    /**
     * Met à jour la cellule passée en paramètre à la position passée en paramètre.
     * Si la cellule existe déjà dans le jeu, elle est simplement remplacée dans la hashmap, sinon, elle est ajoutée dans la hashmap
     * On appelle également updateColor, et on lui affecte le jeu courant en attribut
     * @param cell la cellule que l'on veut mettre ou modifier
     * @param point le point auquel on veut ajouter ou déplacer la dite cellule
     */
    public void updateCell(Cell cell, Point point) {
        if (point.x >= getSize() | point.y >= getSize() | point.x < 0 | point.y < 0) {
            throw new IllegalArgumentException("Point must have coordinates inside the game board");
        }

        setCell(cell, new Point(point.x, point.y));
        cell.setGame(this);
        cell.updateColor();

        if (!cells.containsKey(cell) && cell.getValue() != 0) {
            cells.put(getCell(point.x, point.y), point);
        } else {
            cells.replace(cell, cell.getCoord(), point);
        }
    }

    /**
     * Renvoie un booléen à true si la partie n'a plus de mouvement disponible (autrement dit si le joueur est bloqué) et false sinon.
     */
    public boolean hasNextMove() {
        for (Cell cell : getCells().keySet()) {
            if (cell.getNext(Direction.up) != null) {
                if (cell.getNext(Direction.up).getValue() == cell.getValue()) return true;
            }
            if (cell.getNext(Direction.down) != null) {
                if (cell.getNext(Direction.down).getValue() == cell.getValue()) return true;
            }
            if (cell.getNext(Direction.left) != null) {
                if (cell.getNext(Direction.left).getValue() == cell.getValue()) return true;
            }
            if (cell.getNext(Direction.right) != null) {
                if (cell.getNext(Direction.right).getValue() == cell.getValue()) return true;
            }
        }
        return false;
    }

    public void move(Direction direction) {
        new Thread() {
            public void run() {
                boolean hasMoved = false;
                if (!unlockRunning) {
                    switch (direction) {
                        case up:
                            for (int y = 0; y < getSize(); y++) {
                                for (int x = 1; x < getSize(); x++) {
                                    Cell cell = getCell(x, y);
                                    if (cell.getValue() != NULL) {
                                        if (cell.shift(Direction.up)) {
                                            try {
                                                Thread.sleep(50);
                                                setChanged();
                                                notifyObservers();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            hasMoved = true;
                                        }
                                    }

                                }
                            }

                            break;

                        case down:
                            for (int y = 0; y < getSize(); y++) {
                                for (int x = getSize() - 2; x >= 0; x--) {
                                    Cell cell = getCell(x, y);
                                    if (cell.getValue() != NULL) {
                                        if (cell.shift(Direction.down)) {
                                            try {
                                                Thread.sleep(50);
                                                setChanged();
                                                notifyObservers();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            hasMoved = true;
                                        }
                                    }
                                }
                            }

                            break;

                        case right:
                            for (int x = 0; x < getSize(); x++) {
                                for (int y = getSize() - 2; y >= 0; y--) {
                                    Cell cell = getCell(x, y);
                                    if (cell.getValue() != NULL) {
                                        if (cell.shift(Direction.right)) {
                                            try {
                                                Thread.sleep(50);
                                                setChanged();
                                                notifyObservers();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            hasMoved = true;
                                        }
                                    }

                                }
                            }

                            break;

                        case left:
                            for (int x = 0; x < getSize(); x++) {
                                for (int y = 1; y < getSize(); y++) {
                                    Cell cell = getCell(x, y);
                                    if (cell.getValue() != NULL) {
                                        if (cell.shift(Direction.left)) {
                                            try {
                                                Thread.sleep(50);
                                                setChanged();
                                                notifyObservers();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            hasMoved = true;
                                        }
                                    }
                                }
                            }

                            break;
                    }
                }
                if (hasMoved == true) {
                    for (Cell cell : getCells().keySet()) {
                        cell.setMerged(false);
                    }
                    rnd();
                    System.out.println("hm size :" + cells.size());
                }

                if (getCells().keySet().size() == getSize() * getSize() && !hasNextMove()) {
                    System.out.println("game is over");
                    gameOver = true;
                }
            }
        }.start();



        System.out.println(getCells().size());
        setChanged();
        notifyObservers();
    }


    public void rnd() {

        int r, x, y;

        x = rnd.nextInt(getSize());
        y = rnd.nextInt(getSize());
        while (tabCells[x][y].getValue() != NULL) {
            x = rnd.nextInt(getSize());
            y = rnd.nextInt(getSize());
        }
        r = rnd.nextInt(2);


        switch (r) {
            case 0:
                updateCell(new Cell(2), new Point(x, y));
                break;
            case 1:
                updateCell(new Cell(512), new Point(x, y));
                break;
        }
        getCell(x, y).updateFile(data);


        setChanged();
        notifyObservers();


    }

    public void setCell(Cell cell, Point point) {
        tabCells[point.x][point.y] = cell;
    }

    public String toString() {
        String boardTitle = "Board" + this.getClass().getName() + "\n"
                + "\t" + "(" + getSize() + "," + getSize() + ")" + "\n";

        StringBuilder stringBuilder = new StringBuilder(boardTitle);
        for (int i = 0; i < getSize(); i++) {
            stringBuilder.append("  | ");
            for (int j = 0; j < getSize(); j++) {
                stringBuilder.append(tabCells[i][j] + " ");
            }
            stringBuilder.append("|" + "\n");
        }
        return stringBuilder.toString();
    }

    public HashMap<Cell, Point> getCells() {
        return this.cells;
    }

    public int getSize() {
        return tabCells.length;
    }

    public Cell getCell(int i, int j) {
        return tabCells[i][j];
    }


    public void resetBestScore() {
        try {

            if (data.delete()) {
                System.out.println(data.getName() + " est supprimé.");
            } else {
                System.out.println("Opération de suppression echouée");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public File getFile() {
        return this.data;
    }

    public int getBestScore() {
        if (data.exists()) {
            try {
                Scanner scanner = new Scanner(data);
                if (scanner.hasNextLine())
                    return Integer.parseInt(scanner.nextLine());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public double getBestTime() {

        if (data.exists()) {
            try {
                Scanner scanner = new Scanner(data);
                if (scanner.hasNextLine())
                    scanner.nextLine();
                    if (scanner.hasNextLine()) return Double.parseDouble(scanner.nextLine());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }


    public double getTimeElapsedMillis() {
        return timeElapsed/1000.0;
    }
    public int getTimeElapsed() {
        return (int)timeElapsed/1000;
    }

    public synchronized void setTimeElapsed() {
        Instant instantStop = Instant.now();
        //System.out.println(Duration.between(instantStart, instantStop).toMillis());
        timeElapsed = Duration.between(instantStart, instantStop).toMillis();
        setChanged();
        notifyObservers();
    }

    public void ThreadGetActualTime() {
        new Thread() {
            public synchronized void run() {
                while (!gameOver) {
                    //System.out.println(gameOver);
                    setTimeElapsed();
                    //System.out.println(activeCount());

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void restart() {
        gameOver = false;
        int size=this.getSize();
        this.cells.clear();

        for (int i = 0; i<size; i++){
            for (int j = 0; j<size; j++){;
                tabCells[i][j] = new Cell(NULL);
            }
        }

        for (int i = 0; i<size; i++){
            for (int j = 0; j<size; j++){
                tabCells[i][j].setGame(this);
            }
        }

        rnd();
        rnd();
        ThreadGetActualTime();
        instantStart = Instant.now();
    }


    public void setUnlocked(int mouseX, int mouseY) {
        int tabX = (mouseY-35)/PIXEL_PER_SQUARE;
        int tabY = mouseX/PIXEL_PER_SQUARE;
        if (tabX<getSize() && tabY<getSize()) {
            if (unlock > 0 && !unlockRunning) {
                System.out.println(unlock + ", " + unlockRunning);
                unlockRunning = true;
                unlockedPosition = new Point(tabX, tabY);
                System.out.println(unlock + ", " + unlockRunning + ", " + tabCells[tabX][tabY].getValue() + ", " + unlockedPosition);
            }
        }
    }

    public void setUnlock(int a) {
        unlock = a;
        restart();
    }

    public void switchPosition(int mouseX, int mouseY) {
        int tabX = (mouseY-35)/PIXEL_PER_SQUARE;
        int tabY = mouseX/PIXEL_PER_SQUARE;
        if (unlockRunning) {
            if (tabX < getSize() && tabY < getSize()) {
                if (tabX != unlockedPosition.x || tabY != unlockedPosition.y) {
                    unlock -= 1;
                    Cell unlocked =null;
                    unlocked = getCell(unlockedPosition.x, unlockedPosition.y);
                    updateCell(getCell(tabX, tabY), new Point(unlockedPosition.x, unlockedPosition.y));
                    updateCell(unlocked, new Point(tabX, tabY));
                    System.out.println(unlock + ", " + unlockRunning);
                    /*Cell tmp = (Cell) tabCells[tabX][tabY].clone();
                    tabCells[tabX][tabY] = unlocked;
                    cells.replace(tabCells[tabX][tabY], unlocked.getCoord(), new Point (tabX, tabY));
                    unlocked = tmp;*/
                    System.out.println(unlock + ", " + unlockRunning + ", " + tabCells[tabX][tabY].getValue());
                }
            }
        }
        unlockedPosition = null;
        unlockRunning = false;

        if (this.getCells().keySet().size() == getSize() * getSize() ) {
            if (!gameOver && !hasNextMove()) {
                System.out.println("game is over");
                gameOver = true;
            }
            else if (gameOver && hasNextMove()) {
                gameOver = false;
            }
        }

        setChanged();
        notifyObservers();
    }

}