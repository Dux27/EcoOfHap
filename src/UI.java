import javax.swing.*;

public class UI extends JFrame {
    private Player player;
    private Menu menu;

    public UI() {
        menu = new Menu(this);
        setupWindow();
        menu.showMenu();
    }

    private void setupWindow() {
        setTitle("Economy of Happiness");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void createPlayer(int age) {
        player = new Player("Player", age, 100, 1000, 100, "default_icon.png");
    }
}
