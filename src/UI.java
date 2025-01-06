import java.awt.*;
import javax.swing.*;

public class UI extends JFrame {
    public Player player;
    public Menu menu;
    public Game game;

    public boolean continueGame;

    public UI() {
        menu = new Menu(this);
        game = new Game(this);
        setupWindow();
        menu.showMenu();
    }

    private void setupWindow() {
        setTitle("Economy of Happiness");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        ImageIcon mainIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("assets/game_icon.png"));
        Image mainImage = mainIcon.getImage().getScaledInstance(64, 64, 
        Image.SCALE_SMOOTH);
        setIconImage(mainImage);
    }

    public void activateGame() {
        continueGame = true;
        game.showGame();
        MainLoop.startGame();
    }

    public void activateMenu() {
        menu.showMenu();
        MainLoop.stopGame();
    }
}
