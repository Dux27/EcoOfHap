import java.awt.*;
import javax.swing.*;

public class UI extends JFrame {
    public Player player;
    public Menu menu;
    public Game game;

    public boolean continueGame;

    public UI() {
        menu = new Menu(this);
        setupWindow();
        menu.showMenu();
        //  Game is being initialized in createPlayer method
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
        MainLoop.startGame(this); // Pass the UI instance to the MainLoop
    }

    public void activateMenu() {
        menu.showMenu();
        MainLoop.stopGame();
    }

    public void createPlayer(int age, String icon) {
        player = new Player(this, "Player", age, 100, 1000, 100, icon);
        System.out.println(
                "PLAYER CREATED \nname = " + player.name +
                "\nAge = " + player.age +
                "\nIcon path = " + player.icon
                ); // DEBUG
        
        game = new Game(this); // Initialize game after player is created
    }

}
