import javax.swing.*;

public class UI extends JFrame {
    private Player player;
    private Menu menu;

    public UI() {
        setupWindow();
        menu = new Menu(this);
        menu.showMenu();
    }

    private void setupWindow() {
        setTitle("Life Simulation");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void createPlayer(int age) {
        player = new Player("Player", age, 100, 1000, 100, "default_icon.png");
        // Here you can start the game with the selected player
    }
}
