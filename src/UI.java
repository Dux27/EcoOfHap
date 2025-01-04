import java.awt.*;
import javax.swing.*;

public class UI extends JFrame {
    public Player player;
    public Menu menu;
    public Game game;

    public UI() {
        menu = new Menu(this);
        game = new Game(this);
        setupWindow();
        menu.showMenu();
    }

    private void setupWindow() {
        setTitle("Economy of Happiness");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("assets/game_icon.png"));
        Image image = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        setIconImage(image);
    }

    public void createPlayer(int age) {
        player = new Player("Player", age, 100, 1000, 100, "default_icon.png");
    }
}
