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
     * Cette procédure affiche dans une JFrame, le jeu passé en paramètre, en affichant une grille de la taille correspondante, avec quelques cases supplémentaire
     * qui gèrent le temps de jeu, le meilleur temps pour atteindre 2048, la plus grosse case constituée etc
     * On affecte également un MenuBar, où il est possible de modifier le nombre de déblocages et la taille du jeu entre autres.
     * On y affecte également des listener pour le clavier et la souris, afin de pouvoir soliciter les actions de l'utilisateur et les calculs du modèle.
     * @param game Le jeu que l'on souhaite afficher sur la fenêtre
     */
    public Swing2048(Game game) {
        this.game = game;
        setTitle("2048 GAME");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(this.game.getSize() * PIXEL_PER_SQUARE, 65+(this.game.getSize()+3) * PIXEL_PER_SQUARE);
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

    /**
     * Cette procédure, appelée dans le cas où l'on souhaite changer la taille du jeu via le menu, va réinitialiser la fenêtre, avant de refaire un nouveau jeu
     * de la taille souhaitée avec le constructeur, et de l'afficher de nouveau.
     * @param size la taille du nouveau jeu que l'on souhaite afficher
     */
    private void Swing2048(int size){
        getContentPane().invalidate();
        getContentPane().validate();
        getContentPane().repaint();
        this.game = new Game(size);
        setSize(this.game.getSize() * PIXEL_PER_SQUARE, 65+(this.game.getSize()+3) * PIXEL_PER_SQUARE);
        tabC = new JLabel[this.game.getSize()+3][this.game.getSize()];

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
        refresh();

    }

    /**
     * Initialisation du MenuBar, avec les différents items et les fonctions appelées lors d'un clic sur un item
     * On retrouve la possibilité de changer la taille de la grille, de changer notre nombre de déblocages (changer la difficulté par la même occasion)
     * On a aussi un item qui permet de quitter le jeu, et d'autres items qui permettent de consulter les règles etc
     */
    private void addMenuBar() {
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

        Menu size=new Menu("Game size");
        MenuItem size3 = new MenuItem("3");
        MenuItem size4 = new MenuItem("4");
        MenuItem size5 = new MenuItem("5");
        MenuItem size6 = new MenuItem("6");
        size.add(size3);
        size.add(size4);
        size.add(size5);
        size.add(size6);
        size3.addActionListener(e -> Swing2048(3));
        size4.addActionListener(e -> Swing2048(4));
        size5.addActionListener(e -> Swing2048(5));
        size6.addActionListener(e -> Swing2048(6));
        settings.add(size);


        quitm.addActionListener(e -> System.exit(1));
        settings.add(quitm);

        ret.add(settings);

        Menu game1=new Menu("Game");
        game1.addSeparator();
        MenuItem project = new MenuItem("LIFAP7 Project");
        Menu dev = new Menu("Developped by");
        MenuItem dev1 = new MenuItem("ABDULSATTAR Ahmed");
        MenuItem dev2 = new MenuItem("JEANNIN Dylan");
        dev.add(dev1);
        dev.add(dev2);
        Menu rules = new Menu("Rules");
        MenuItem rule1 = new MenuItem("Use directional arrows to move");
        MenuItem rule2 = new MenuItem("You can have your best score on the bottom");
        MenuItem rule3 = new MenuItem("You can have your best time to get 2048 too");
        MenuItem rule4 = new MenuItem("You can reset it pressing B");
        MenuItem rule5 = new MenuItem("You can restart game pressing R");
        MenuItem rule6 = new MenuItem("You can switch two cells by sliding one to the other") ;
        rules.add(rule1);
        rules.add(rule2);
        rules.add(rule3);
        rules.add(rule4);
        rules.add(rule5);
        rules.add(rule6);
        game1.add(project);
        game1.add(dev);
        game1.add(rules);
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
                if (game.isGameOver()) {
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
                for (int i = game.getSize()+1; i <= game.getSize()+2; i++) {
                    for (int j = 0; j<=3; j++) {
                        if (j<game.getSize()) {
                            tabC[i][j].setFont(new Font("Serif", Font.BOLD, 12));
                        }
                    }
                }
                tabC[game.getSize()+1][0].setText("Best score :");
                tabC[game.getSize()+1][1].setText("Best time :");
                tabC[game.getSize()+1][2].setText("Timer :");
                if (game.getSize()>3) tabC[game.getSize()+1][game.getSize()-1].setText("R restart");

                tabC[game.getSize()+2][0].setText(game.getBestScore()+"");
                tabC[game.getSize()+2][1].setText(game.getBestTime() + "");
                tabC[game.getSize()+2][2].setText(game.getTimeElapsed()+ "s");
                if (game.getSize()>3) tabC[game.getSize()+2][game.getSize()-1].setText("B reset");

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
     * Correspond à la fonctionnalité de Contrôleur : écoute les évènements, et déclenche des traitements sur le modèle
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
     * Correspond à la fonctionnalité de Contrôleur : écoute les évènements, et déclenche des traitements sur le modèle
     */
    public void addMouseListener() {
        addMouseListener(new MouseAdapter() { // new MouseAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void mousePressed(MouseEvent e) {
                game.setUnlocked(e.getX(), e.getY());
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                game.switchPosition(e.getX(), e.getY());
            }
        });
    }



    @Override
    public void update(Observable o, Object arg) {
        refresh();
    }
}