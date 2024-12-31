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

        JButton playButton = new JButton("Play");
        JButton quitButton = new JButton("Quit");

        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        playButton.addActionListener(e -> showPlayerSelect());
        quitButton.addActionListener(e -> System.exit(0));

        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(playButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(quitButton);
        menuPanel.add(Box.createVerticalGlue());
    }

    private void setupPlayerSelectPanel() {
        playerSelectPanel = new JPanel();
        playerSelectPanel.setLayout(new BoxLayout(playerSelectPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("SELECT PLAYER");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        playerSelectPanel.add(Box.createVerticalGlue());
        playerSelectPanel.add(titleLabel);
        playerSelectPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        JButton player1Button = new JButton("Player 1 (Age: 18)");
        JButton player2Button = new JButton("Player 2 (Age: 27)");
        JButton backButton = new JButton("Back");

        player1Button.setAlignmentX(Component.CENTER_ALIGNMENT);
        player2Button.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        player1Button.addActionListener(e -> {
            parentUI.createPlayer(18);
            showGameDuration();
        });
        player2Button.addActionListener(e -> {
            parentUI.createPlayer(27);
            showGameDuration();
        });
        backButton.addActionListener(e -> showMenu());

        playerSelectPanel.add(Box.createVerticalGlue());
        playerSelectPanel.add(player1Button);
        playerSelectPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        playerSelectPanel.add(player2Button);
        playerSelectPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        playerSelectPanel.add(backButton);
        playerSelectPanel.add(Box.createVerticalGlue());
    }

    private void setupGameDurationPanel() {
        gameDurationPanel = new JPanel();
        gameDurationPanel.setLayout(new BoxLayout(gameDurationPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("SELECT GAME DURATION");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        gameDurationPanel.add(Box.createVerticalGlue());
        gameDurationPanel.add(titleLabel);
        gameDurationPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        JLabel gameDurationLabel = new JLabel("Game Duration");
        gameDurationLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gameDurationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton shortButton = new JButton("Blitz");
        JButton mediumButton = new JButton("Normal");
        JButton longButton = new JButton("Long");

        shortButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mediumButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        longButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 1 month in ms; average life expectancy => 75 years
        shortButton.addActionListener(e -> MainLoop.TICK_DURATION_MS = 667); // 1 year in 8 seconds; life in 10 minutes
        mediumButton.addActionListener(e -> MainLoop.TICK_DURATION_MS = 1000); // 1 year in 12 seconds; life in 15 minutes
        longButton.addActionListener(e -> MainLoop.TICK_DURATION_MS = 1750); // 1 year in 21 seconds; life in 25 minutes

        gameDurationPanel.add(Box.createVerticalGlue());
        gameDurationPanel.add(gameDurationLabel);
        gameDurationPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        gameDurationPanel.add(shortButton);
        gameDurationPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        gameDurationPanel.add(mediumButton);
        gameDurationPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        gameDurationPanel.add(longButton);
        gameDurationPanel.add(Box.createVerticalGlue());
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