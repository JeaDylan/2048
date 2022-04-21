package vue_controleur;

import modele.Cell;
import modele.Direction;
import modele.Game;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import static java.sql.Types.NULL;

public class Swing2048 extends JFrame implements Observer {
    public static final int PIXEL_PER_SQUARE = 100;
    // tableau de cases : i, j -> case graphique
    private JLabel[][] tabC;
    private Game game;


    /**
     * Correspond à la fonctionnalité de Vue : Constructeur de la fenêtre correspondant au jeu passé en paramètre
     * @param game modèle du jeu sur lequel on joue
     */
    public Swing2048(Game game) {
        this.game = game;
        setTitle("2048 GAME");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(this.game.getSize() * PIXEL_PER_SQUARE, (this.game.getSize()+3) * PIXEL_PER_SQUARE);
        tabC = new JLabel[this.game.getSize()+3][this.game.getSize()];

        addMenuBar();


        JPanel contentPane = new JPanel(new GridLayout(this.game.getSize()+3, this.game.getSize()));

        for (int i = 0; i < this.game.getSize()+3; i++) {
            for (int j = 0; j < this.game.getSize(); j++) {
                tabC[i][j] = new JLabel();
                if (i== game.getSize()) {
                    Border border = BorderFactory.createLineBorder(Color.darkGray, 90);
                    tabC[i][j].setBorder(border);
                }
                else {
                    Border border = BorderFactory.createLineBorder(Color.darkGray, 5);
                    tabC[i][j].setBorder(border);
                }
                tabC[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                tabC[i][j].setFont(new Font("Serif", Font.BOLD, 20));
                tabC[i][j].setOpaque(true);



                contentPane.add(tabC[i][j]);

            }
        }
        setContentPane(contentPane);
        addKeyboardListener();
        addMouseListener();
        refresh();

    }


    public void addMenuBar() {
        MenuBar ret=new MenuBar();
        Menu settings=new Menu("Settings");
        Menu switchNb=new Menu("Switch number");
        MenuItem switchNb0=new MenuItem("0");
        MenuItem switchNb1=new MenuItem("1");
        MenuItem switchNb3=new MenuItem("3");
        MenuItem switchNb5=new MenuItem("5");
        MenuItem quitm=new MenuItem("Quit");
        settings.addSeparator();
        switchNb.add(switchNb0);
        switchNb.add(switchNb1);
        switchNb.add(switchNb3);
        switchNb.add(switchNb5);
        switchNb1.addActionListener(e -> game.setUnlock(1));
        switchNb3.addActionListener(e -> game.setUnlock(3));
        switchNb5.addActionListener(e -> game.setUnlock(5));
        switchNb0.addActionListener(e -> game.setUnlock(0));
        settings.add(switchNb);

        quitm.addActionListener(e -> System.exit(1));
        settings.add(quitm);

        ret.add(settings);

        Menu game1=new Menu("Game");
        MenuItem project = new MenuItem("LIFAP7 Project");
        Menu dev = new Menu("Developped by");
        MenuItem dev1 = new MenuItem("ABDULSATTAR Ahmed");
        MenuItem dev2 = new MenuItem("JEANNIN Dylan");
        dev.add(dev1);
        dev.add(dev2);
        game1.add(project);
        game1.add(dev);
        ret.add(game1);
        setMenuBar(ret);
    }



    /**
     * Correspond à la fonctionnalité de Vue : affiche les données du modèle
     */
    private void refresh()  {

        SwingUtilities.invokeLater(new Runnable() { // demande au processus graphique de réaliser le traitement
            @Override
            public void run() {
                if (game.getGameOver()) {
                    Border border = BorderFactory.createLineBorder(Color.darkGray, 5);
                    tabC[game.getSize() ][1].setBorder(border);
                    tabC[game.getSize() ][2].setBorder(border);
                    tabC[game.getSize() ][1].setText("GAME");
                    tabC[game.getSize() ][2].setText("OVER");
                }
                else {
                    Border border = BorderFactory.createLineBorder(Color.darkGray, 90);
                    tabC[game.getSize() ][1].setBorder(border);
                    tabC[game.getSize() ][2].setBorder(border);
                }

                tabC[game.getSize()+1][0].setText("Best score :");
                tabC[game.getSize()+1][1].setText("Best time :");
                tabC[game.getSize()+1][2].setText("Timer :");
                tabC[game.getSize()+1][game.getSize()-1].setText("R restart");

                tabC[game.getSize()+2][0].setText(game.getBestScore()+"");
                tabC[game.getSize()+2][1].setText(game.getBestTime() + "");
                tabC[game.getSize()+2][2].setText(game.getTimeElapsed()+ "s");
                tabC[game.getSize()+2][game.getSize()-1].setText("B resetRecord");

                for (int i = 0; i < game.getSize(); i++) {
                    for (int j = 0; j < game.getSize(); j++) {
                        Cell cell = game.getCell(i, j);

                        if (cell.getValue() == NULL) {

                            tabC[i][j].setText("");
                            tabC[i][j].setBackground(new Color(255, 255, 255));

                        } else {
                            tabC[i][j].setText(cell.getValue() + "");
                            tabC[i][j].setBackground(cell.getColor());
                        }


                    }
                }
            }
        });


    }

    /**
     * Correspond à la fonctionnalité de Contrôleur : écoute les évènements (touche clavier), et déclenche des traitements sur le modèle
     */
    private void addKeyboardListener() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT : game.move(Direction.left); break;
                    case KeyEvent.VK_RIGHT : game.move(Direction.right); break;
                    case KeyEvent.VK_DOWN : game.move(Direction.down); break;
                    case KeyEvent.VK_UP : game.move(Direction.up); break;
                    case KeyEvent.VK_R : game.restart(); break;
                    case KeyEvent.VK_B : game.resetBestScore(); break;
                }
            }
        });
    }
    /**
     * Correspond à la fonctionnalité de Contrôleur : écoute les évènements (clic souris), et déclenche des traitements sur le modèle
     */
    public void addMouseListener() {
        addMouseListener(new MouseAdapter() { // new MouseAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void mousePressed(MouseEvent e) { 
                System.out.println((-35+e.getY())/PIXEL_PER_SQUARE + "," + e.getX()/PIXEL_PER_SQUARE);
                game.setUnlocked(e.getX(), e.getY());
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println((-35+e.getY())/PIXEL_PER_SQUARE + "," + e.getX()/PIXEL_PER_SQUARE);
                game.switchPosition(e.getX(), e.getY());
            }
        });
    }


    @Override
    public void update(Observable o, Object arg) {
        refresh();
    }
}