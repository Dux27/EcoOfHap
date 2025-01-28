import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class Menu {
    private JPanel menuPanel;
    private JPanel playerSelectPanel;
    private JPanel gameDurationPanel;

    private final UI parentUI;

    /**
     * Constructor for Menu class.
     * @param parentUI The parent UI instance.
     */
    public Menu(UI parentUI) {
        this.parentUI = parentUI;
        setupMenuPanel();
        setupPlayerSelectPanel();
        setupGameDurationPanel();
    }

    /**
     * Setup the main menu panel.
     */
    private void setupMenuPanel() {
        menuPanel = createBackgroundPanel("assets/game_background.png");
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        ImageIcon titleIcon = new ImageIcon("assets/menu_logo.png");
        JLabel titleLabel = new JLabel(titleIcon);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(titleLabel);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        JButton newGameButton = createButton("assets/new_game_button.png", 250, 50);
        JButton continueButton = createButton("assets/continue_button.png", 250, 50);
        JButton quitButton = createButton("assets/quit_button.png", 250, 50);

        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        newGameButton.setFocusable(false);
        continueButton.setFocusable(false);
        quitButton.setFocusable(false);

        Widgets.addClickSound(newGameButton);
        Widgets.addClickSound(continueButton);
        Widgets.addClickSound(quitButton);

        newGameButton.addActionListener(e -> showPlayerSelect());
        continueButton.addActionListener(e -> {
            if (parentUI.continueGame)
                parentUI.activateGame();
            else
                continueButton.setToolTipText("No saved game found");
        });
        quitButton.addActionListener(e -> System.exit(0));

        menuPanel.add(newGameButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(continueButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(quitButton);
        menuPanel.add(Box.createVerticalGlue());
    }

    /**
     * Setup the player selection panel.
     */
    private void setupPlayerSelectPanel() {
        playerSelectPanel = createBackgroundPanel("assets/game_background.png");
        playerSelectPanel.setLayout(new BorderLayout());

        JPanel upperPanel = new JPanel(new BorderLayout());
        upperPanel.setOpaque(false); // Ensure transparency
        JPanel lowerPanel = new JPanel();
        lowerPanel.setOpaque(false); // Ensure transparency
        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.Y_AXIS));

        ImageIcon backIcon = new ImageIcon("assets/back_button.png");
        Image scaledBackImage = backIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);

        JButton backButton = new JButton(new ImageIcon(scaledBackImage));

        backButton.setPreferredSize(new Dimension(40, 40));
        backButton.setToolTipText("Go back");
        Widgets.addClickSound(backButton);
        backButton.addActionListener(e -> showMenu());
        upperPanel.add(backButton, BorderLayout.WEST);

        ImageIcon selectPlayerIcon = new ImageIcon("assets/select_player_logo.png");
        JLabel selectPlayerLabel = new JLabel(selectPlayerIcon);
        selectPlayerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        lowerPanel.add(Box.createVerticalGlue());
        lowerPanel.add(selectPlayerLabel);
        lowerPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        // Combined player image
        ImageIcon combinedPlayerImageIcon = new ImageIcon("assets/2_player.png");
        Image scaledCombinedPlayerImage = combinedPlayerImageIcon.getImage().getScaledInstance(300, -1, Image.SCALE_SMOOTH);
        JLabel combinedPlayerImageLabel = new JLabel(new ImageIcon(scaledCombinedPlayerImage));
        combinedPlayerImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        lowerPanel.add(combinedPlayerImageLabel);
        lowerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Player 2 button
        JButton player2Button = createButton("assets/player2_button.png", 120, 50);
        player2Button.setAlignmentX(Component.CENTER_ALIGNMENT);
        Widgets.addClickSound(player2Button);
        player2Button.addActionListener(e -> {
            parentUI.createPlayer(27, "assets/player2_icon.png");
            parentUI.player.addEffect(new Effect("Job", 105600, Long.MAX_VALUE, 12, "money", false));
            parentUI.player.addEffect(new Effect("Monthly Expenses", -50000, Long.MAX_VALUE, 12, "money", false));
            showGameDuration();
        });

        // Player 1 button
        JButton player1Button = createButton("assets/player1_button.png", 120, 50);
        player1Button.setAlignmentX(Component.CENTER_ALIGNMENT);
        Widgets.addClickSound(player1Button);
        player1Button.addActionListener(e -> {
            parentUI.createPlayer(18, "assets/player1_icon.png");
            parentUI.player.addEffect(new Effect("Job", 62500, Long.MAX_VALUE, 12, "money", false));
            parentUI.player.addEffect(new Effect("Monthly Expenses", -30000, Long.MAX_VALUE, 12, "money", false));
            showGameDuration();
        });

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(player2Button);
        buttonBox.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonBox.add(player1Button);

        lowerPanel.add(Box.createVerticalGlue());
        lowerPanel.add(buttonBox);
        lowerPanel.add(Box.createVerticalGlue());

        playerSelectPanel.add(upperPanel, BorderLayout.NORTH);
        playerSelectPanel.add(lowerPanel, BorderLayout.CENTER);
    }

    /**
     * Setup the game duration selection panel.
     */
    private void setupGameDurationPanel() {
        gameDurationPanel = createBackgroundPanel("assets/game_background.png");
        gameDurationPanel.setLayout(new BorderLayout());

        JPanel upperPanel = new JPanel(new BorderLayout());
        upperPanel.setOpaque(false); // Ensure transparency
        JPanel lowerPanel = new JPanel();
        lowerPanel.setOpaque(false); // Ensure transparency
        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.Y_AXIS));

        ImageIcon backIcon = new ImageIcon("assets/back_button.png");
        Image scaledBackImage = backIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);

        JButton backButton = new JButton(new ImageIcon(scaledBackImage));

        backButton.setPreferredSize(new Dimension(40, 40));
        backButton.setToolTipText("Go back");
        Widgets.addClickSound(backButton);
        backButton.addActionListener(e -> showPlayerSelect());
        upperPanel.add(backButton, BorderLayout.WEST);

        ImageIcon gameDurationIcon = new ImageIcon("assets/select_duration_logo.png");
        JLabel gameDurationLabel = new JLabel(gameDurationIcon);
        gameDurationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        lowerPanel.add(Box.createVerticalGlue());
        lowerPanel.add(gameDurationLabel);
        lowerPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        JButton shortButton = createButton("assets/short_button.png", 120, 50);
        JButton mediumButton = createButton("assets/medium_button.png", 120, 50);
        JButton longButton = createButton("assets/long_button.png", 120, 50);

        shortButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mediumButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        longButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        Widgets.addClickSound(shortButton);
        Widgets.addClickSound(mediumButton);
        Widgets.addClickSound(longButton);

        // 1 month in ms; average life expectancy => 75 years
        shortButton.addActionListener(e -> selectGameDuration(100)); // 1 year in 8 seconds; life in 10 minutes 
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

    /**
     * Create a background panel with the specified image.
     * @param imagePath The path to the background image.
     * @return A JPanel with the background image.
     */
    private JPanel createBackgroundPanel(String imagePath) {
        return new JPanel() {
            private final Image backgroundImage = new ImageIcon(imagePath).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
    }
    
    /**
     * Create a button with the specified image and dimensions.
     * @param imagePath The path to the button image.
     * @param width The width of the button.
     * @param height The height of the button.
     * @return A JButton with the specified image and dimensions.
     */
    private JButton createButton(String imagePath, int width, int height) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(scaledImage));
        button.setPreferredSize(new Dimension(width, height));
        button.setMaximumSize(new Dimension(width, height));
        button.setMinimumSize(new Dimension(width, height));
        button.setContentAreaFilled(false);
        button.setBorderPainted(true);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }
        });

        return button;
    }

    /**
     * Select the game duration and start the game.
     * @param tick_duration The duration of each tick in milliseconds.
     */
    public void selectGameDuration(int tick_duration){
        MainLoop.TIC_COUNTER = 0;
        MainLoop.TICK_DURATION_MS = tick_duration;
        System.out.println("TICK DURATION: " + MainLoop.TICK_DURATION_MS + " ms"); // DEBUG
        parentUI.activateGame();
    }
    
    /**
     * Show the main menu panel.
     */
    public void showMenu() {
        parentUI.getContentPane().removeAll();
        parentUI.getContentPane().add(menuPanel);
        parentUI.revalidate();
        parentUI.repaint();
    }

    /**
     * Show the player selection panel.
     */
    public void showPlayerSelect() {
        parentUI.getContentPane().removeAll();
        parentUI.getContentPane().add(playerSelectPanel);
        parentUI.revalidate();
        parentUI.repaint();
    }
    
    /**
     * Show the game duration selection panel.
     */
    public void showGameDuration() {
        parentUI.getContentPane().removeAll();
        parentUI.getContentPane().add(gameDurationPanel);
        parentUI.revalidate();
        parentUI.repaint();
    }
}