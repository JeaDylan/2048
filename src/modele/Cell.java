package modele;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import static java.sql.Types.NULL;

public class Cell {
    private int value;
    private Game game;
    private boolean isMerged;
    private Color color;


    /**
     * Constructeur d'une case avec une valeur définie en paramètre
     * @param value la valeur que l'on souhaite affecter à la case nouvellement créée
     */
    public Cell(int value) {
        this.game = null;
        this.value = value;
        this.isMerged = false;
    }

    /**
     * Constructeur par défaut d'une nouvelle case, on lui affecte la valeur 0
     */
    public Cell(){
        this.value = NULL;
        this.game = null;
        this.isMerged = false;
    }

    /**
     * Procédure qui va déplacer la case sur laquelle on l'appelle, dans la direction passée en paramètre.
     * Elle ne bouge pas si elle est au bord de la grille dans la direction donnée, ou si son voisin le plus proche est collé à elle et qu'il n'a pas la même valeur
     * Si son voisin le plus proche a la même valeur, et que ce voisin n'a pas déjà été fusionné au cours du déplacement, on appelle ces deux cellules à se fusionner
     * Si son voisin est null, alors on déplace la case au maximum de la grille dans la direction
     * @param direction la direction dans laquelle on souhaite déplacer la case
     * @return true si la case a changé de position, false sinon
     */
    public boolean shift(Direction direction){
        switch (direction){
            case up :
                if(game.getCoord(this).x != 0) {
                    Cell cellUp = game.getNext(this, Direction.up);
                    if (cellUp != null && cellUp.getValue() != this.getValue() && game.getDistance(this, cellUp, Direction.up) > 1) {
                        Point oldPoint = this.game.getCoord(this);
                        game.updateCell(this, new Point(game.getCoord(cellUp).x+1, game.getCoord(cellUp).y));
                        game.updateCell(new Cell(), oldPoint);
                        return true;
                    }

                    if(cellUp != null && cellUp.getValue() == this.getValue() ){
                        if (!cellUp.isMerged()) {
                            game.merge(this, cellUp);
                        }
                        else {
                            Point oldPoint = game.getCoord(this);
                            game.updateCell(this, new Point(game.getCoord(cellUp).x+1, game.getCoord(cellUp).y));
                            game.updateCell(new Cell(), oldPoint);
                        }
                        return true;
                    }

                    if(cellUp == null){
                        Point oldPoint = game.getCoord(this);
                        game.updateCell(this, new Point(0, game.getCoord(this).y));
                        game.updateCell(new Cell(), oldPoint);
                        return true;
                    }

                }

                break;

            case down :
                if(game.getCoord(this).x != this.game.getSize()-1) {
                    Cell cellDown = game.getNext(this, Direction.down);
                    if (cellDown != null && cellDown.getValue() != this.getValue() && game.getDistance(this, cellDown, Direction.down) > 1) {
                        Point oldPoint = game.getCoord(this);
                        game.updateCell(this, new Point(game.getCoord(cellDown).x-1, game.getCoord(cellDown).y));
                        game.updateCell(new Cell(), oldPoint);
                        return true;
                    }

                    if(cellDown != null && cellDown.getValue() == this.getValue() ){
                        if (!cellDown.isMerged()) {
                            game.merge(this, cellDown);
                        }
                        else {
                            Point oldPoint = game.getCoord(this);
                            game.updateCell(this, new Point(game.getCoord(cellDown).x-1, game.getCoord(cellDown).y));
                            game.updateCell(new Cell(), oldPoint);
                        }
                        return true;
                    }

                    if(cellDown == null){
                        Point oldPoint = game.getCoord(this);
                        game.updateCell(this, new Point(game.getSize()-1, game.getCoord(this).y));
                        game.updateCell(new Cell(), oldPoint);
                        return true;
                    }
                }

                break;

            case right :
                if(game.getCoord(this).y != this.game.getSize()) {
                    Cell cellRight = game.getNext(this, Direction.right);
                    if (cellRight != null && cellRight.getValue() != this.getValue() && game.getDistance(this, cellRight, Direction.right) > 1) {
                        Point oldPoint = game.getCoord(this);
                        game.updateCell(this, new Point(game.getCoord(cellRight).x, game.getCoord(cellRight).y-1));
                        game.updateCell(new Cell(), oldPoint);
                        return true;
                    }

                    if(cellRight != null && cellRight.getValue() == this.getValue() ){
                        if (!cellRight.isMerged()) {
                            game.merge(this, cellRight);
                        }
                        else {
                            Point oldPoint = game.getCoord(this);
                            game.updateCell(this, new Point(game.getCoord(cellRight).x, game.getCoord(cellRight).y - 1));
                            game.updateCell(new Cell(), oldPoint);
                        }
                        return true;
                    }

                    if(cellRight == null){
                        Point oldPoint = game.getCoord(this);
                        game.updateCell(this, new Point(game.getCoord(this).x, game.getSize()-1));
                        game.updateCell(new Cell(), oldPoint);
                        return true;
                    }
                }

                break;

            case left :
                if(game.getCoord(this).y != 0) {
                    Cell cellLeft = game.getNext(this, Direction.left);
                    if (cellLeft != null && cellLeft.getValue() != this.getValue() && game.getDistance(this, cellLeft, Direction.left) > 1) {
                        Point oldPoint = game.getCoord(this);
                        game.updateCell(this, new Point(game.getCoord(cellLeft).x, game.getCoord(cellLeft).y+1));
                        game.updateCell(new Cell(), oldPoint);
                        return true;
                    }

                    if(cellLeft != null && cellLeft.getValue() == this.getValue() ){
                        if (!cellLeft.isMerged()) {
                            game.merge(this, cellLeft);
                        }
                        else {
                            Point oldPoint = game.getCoord(this);
                            game.updateCell(this, new Point(game.getCoord(cellLeft).x, game.getCoord(cellLeft).y+1));
                            game.updateCell(new Cell(), oldPoint);
                        }
                        return true;
                    }

                    if(cellLeft == null){
                        Point oldPoint = game.getCoord(this);
                        game.updateCell(this, new Point(game.getCoord(this).x, 0));
                        game.updateCell(new Cell(), oldPoint);
                        return true;
                    }
                }

                break;
        }
        return false;
    }

    /**
     * @return le booléen isMerged qui indique si la cellule a déjà fusionner au cours du déplacement, auquel cas elle ne peut pas fusionner une deuxième fois pendant le tour
     */
    public boolean isMerged() {
        return isMerged;
    }

    /**
     * set le booléen isMerged à la valeur du paramètre
     * @param merged
     */
    public void setMerged(boolean merged) {
        isMerged = merged;
    }

    /**
     * @return la couleur de la case sur laquelle on appelle la méthode
     */
    public Color getColor() {
        return color;
    }

    /**
     * affecte le jeu passé en paramètre à la cellule
     * @param game le jeu que l'on souhaite affecter à la cellule
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Met la couleur de la case à jour, en fonction de la valeur
     */
    public void updateColor() {
        switch (value) {
            case 2:
                color = new Color(238,228,218);
                break;
            case 4:
                color = new Color(238,225,201);
                break;
            case 8:
                color = new Color(243,178,122);
                break;
            case 16:
                color = new Color(246,150,100);
                break;
            case 32:
                color = new Color(236, 229, 29);
                break;
            case 64:
                color = new Color(255, 150, 0);
                break;
            case 128:
                color = new Color(200, 50, 17);
                break;
            case 256:
                color = new Color(255, 0, 8);
                break;
            case 512:
                color = new Color(255, 0, 100);
                break;
            case 1024:
                color = new Color(255, 0, 200);
                break;
            case 2048:
                color = new Color(138, 250, 37);
                break;
            case 4096:
                color = new Color(50, 50, 100);
                break;
            case 8192:
                color = new Color(100, 100, 100);
                break;
            case 16384:
                color = new Color(160, 160, 160);
                break;
        }
    }

    /**
     * @return la valeur de la case
     */
    public int getValue() {
        return value;
    }


    /**
     * @return une chaine de caractère qui sert à afficher le jeu sous forme console
     */
    @Override
    public String toString() {
        if (value != 0) {
            return "{" +
                    "" + value +
                    "}" ;
        } else {
            return "{ }";
        }

    }
}
