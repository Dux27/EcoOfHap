import java.awt.*;
import javax.swing.*;

public class Menu {
    private JPanel menuPanel;
    private JPanel playerSelectPanel;
    private JPanel gameDurationPanel;

    private final UI parentUI;

    public Menu(UI parentUI) {
        this.parentUI = parentUI;
        setupMenuPanel();
        setupPlayerSelectPanel();
        setupGameDurationPanel();
    }

    private void setupMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("MENU");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(titleLabel);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        JButton newGameButton = new JButton("New Game");
        JButton continueButton = new JButton("Continue");
        JButton quitButton = new JButton("Quit");

        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        newGameButton.setFocusable(false);
        continueButton.setFocusable(false);
        quitButton.setFocusable(false);

        newGameButton.addActionListener(e -> showPlayerSelect());
        continueButton.addActionListener(e -> {
            if (parentUI.continueGame)
                parentUI.activateGame();
            else
                continueButton.setToolTipText("No saved game found");
        });
        quitButton.addActionListener(e -> System.exit(0));

        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(newGameButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(continueButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(quitButton);
        menuPanel.add(Box.createVerticalGlue());
    }

    private void setupPlayerSelectPanel() {
        playerSelectPanel = new JPanel();
        playerSelectPanel.setLayout(new BorderLayout());

        JPanel upperPanel = new JPanel(new BorderLayout());
        JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.Y_AXIS));

        ImageIcon backIcon = new ImageIcon("assets/menu_button.png");
        Image scaledBackImage = backIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
    
        JButton backButton = new JButton(new ImageIcon(scaledBackImage));

        backButton.setPreferredSize(new Dimension(40, 40));
        backButton.setToolTipText("Go back");
        backButton.addActionListener(e -> showMenu());
        upperPanel.add(backButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("SELECT PLAYER");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        lowerPanel.add(Box.createVerticalGlue());
        lowerPanel.add(titleLabel);
        lowerPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        JButton player1Button = new JButton("Player 1 (Age: 18)");
        JButton player2Button = new JButton("Player 2 (Age: 27)");

        player1Button.setAlignmentX(Component.CENTER_ALIGNMENT);
        player2Button.setAlignmentX(Component.CENTER_ALIGNMENT);

        player1Button.addActionListener(e -> {
            parentUI.createPlayer(18, "assets/player1_icon.png");
            showGameDuration();
        });
        player2Button.addActionListener(e -> {
            parentUI.createPlayer(27, "assets/player2_icon.png");
            showGameDuration();
        });

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(player1Button);
        buttonBox.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonBox.add(player2Button);

        lowerPanel.add(Box.createVerticalGlue());
        lowerPanel.add(buttonBox);
        lowerPanel.add(Box.createVerticalGlue());

        playerSelectPanel.add(upperPanel, BorderLayout.NORTH);
        playerSelectPanel.add(lowerPanel, BorderLayout.CENTER);
    }

    private void setupGameDurationPanel() {
        gameDurationPanel = new JPanel();
        gameDurationPanel.setLayout(new BorderLayout());

        JPanel upperPanel = new JPanel(new BorderLayout());
        JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.Y_AXIS));

        ImageIcon backIcon = new ImageIcon("assets/menu_button.png");
        Image scaledBackImage = backIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);

        JButton backButton = new JButton(new ImageIcon(scaledBackImage));

        backButton.setPreferredSize(new Dimension(40, 40));
        backButton.setToolTipText("Go back");
        backButton.addActionListener(e -> showPlayerSelect());
        upperPanel.add(backButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("SELECT GAME DURATION");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        lowerPanel.add(Box.createVerticalGlue());
        lowerPanel.add(titleLabel);
        lowerPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        JButton shortButton = new JButton("Short");
        JButton mediumButton = new JButton("Normal");
        JButton longButton = new JButton("Long");

        shortButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mediumButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        longButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 1 month in ms; average life expectancy => 75 years
        shortButton.addActionListener(e -> selectGameDuration(667)); // 1 year in 8 seconds; life in 10 minutes 
        mediumButton.addActionListener(e -> selectGameDuration(1000)); // 1 year in 12 seconds; life in 15 minutes 
        longButton.addActionListener(e -> selectGameDuration(1750)); // 1 year in 21 seconds; life in 25 minutes

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(shortButton);
        buttonBox.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonBox.add(mediumButton);
        buttonBox.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonBox.add(longButton);

        lowerPanel.add(Box.createVerticalGlue());
        lowerPanel.add(buttonBox);
        lowerPanel.add(Box.createVerticalGlue());

        gameDurationPanel.add(upperPanel, BorderLayout.NORTH);
        gameDurationPanel.add(lowerPanel, BorderLayout.CENTER);
    }
    
    public void selectGameDuration(int tick_duration){
        MainLoop.TIC_COUNTER = 0;
        MainLoop.TICK_DURATION_MS = tick_duration;
        System.out.println("TICK DURATION: " + MainLoop.TICK_DURATION_MS + " ms"); // DEBUG
        parentUI.activateGame();
    }
    
    public void showMenu() {
        parentUI.getContentPane().removeAll();
        parentUI.getContentPane().add(menuPanel);
        parentUI.revalidate();
        parentUI.repaint();
    }

    public void showPlayerSelect() {
        parentUI.getContentPane().removeAll();
        parentUI.getContentPane().add(playerSelectPanel);
        parentUI.revalidate();
        parentUI.repaint();
    }
    
    public void showGameDuration() {
        parentUI.getContentPane().removeAll();
        parentUI.getContentPane().add(gameDurationPanel);
        parentUI.revalidate();
        parentUI.repaint();
    }
}