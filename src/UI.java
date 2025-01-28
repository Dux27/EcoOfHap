import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import javax.swing.*;

public class UI extends JFrame {
    public Player player;
    public Menu menu;
    public Game game;
    private Clip gameClip;

    public boolean continueGame;

    /**
     * Constructor for UI class.
     */
    public UI() {
        menu = new Menu(this);
        setupWindow();
        playBackgroundMusic("assets/game_music.wav");
        menu.showMenu();
        //  Game is being initialized in createPlayer method
    }

    /**
     * Setup the main window.
     */
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

    /**
     * Play background music.
     * @param musicPath The path to the music file.
     */
    private void playBackgroundMusic(String musicPath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(musicPath).getAbsoluteFile());
            gameClip = AudioSystem.getClip();
            gameClip.open(audioInputStream);
            gameClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error playing background music: " + e.getMessage());
        }
    }

    /**
     * Activate the game.
     */
    public void activateGame() {
        continueGame = true;
        game.showGame();
        MainLoop.startGame(this); // Pass the UI instance to the MainLoop
    }

    /**
     * Activate the menu.
     */
    public void activateMenu() {
        menu.showMenu();
        MainLoop.stopGame();
    }

    /**
     * Create a new player.
     * @param age The age of the player.
     * @param icon The icon of the player.
     */
    public void createPlayer(int age, String icon) {
        player = new Player(this, "Player", age, 100, 2000, 100, icon);
        System.out.println(
                "PLAYER CREATED \nname = " + player.name +
                "\nAge = " + player.age +
                "\nIcon path = " + player.icon
                ); // DEBUG
        
        game = new Game(this); // Initialize game after player is created
    }
}
