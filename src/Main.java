import modele.Cell;
import modele.Direction;
import modele.Game;
import vue_controleur.Console2048;
import vue_controleur.Swing2048;

import java.awt.*;

public class Main {

    public static void main(String[] args) throws Exception {
        mainConsole();
        mainSwing();

    }

    public static void mainConsole() {
        Game game = new Game(4);
        Console2048 vue = new Console2048(game);
        game.addObserver(vue);

        vue.start();
    }

    public static void mainSwing() {

        Game game = new Game(4);
        Swing2048 vue = new Swing2048(game);
        game.addObserver(vue);

        vue.setVisible(true);


    }



}
