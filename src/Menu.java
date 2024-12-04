import java.awt.*;
import javax.swing.*;

public class Menu {
    private JPanel menuPanel;
    private JPanel playerSelectPanel;
    private UI parentUI;

    public Menu(UI parentUI) {
        this.parentUI = parentUI;
        parentUI.setTitle("Economy of Happiness");    
        parentUI.setSize(300, 400);   
        setupMenuPanel();
        setupPlayerSelectPanel();
    }

    private void setupMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setPreferredSize(new Dimension(300, 400)); 

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
        playerSelectPanel.setPreferredSize(new Dimension(300, 400));

        JLabel titleLabel = new JLabel("SELECT PLAYER");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        playerSelectPanel.add(Box.createVerticalGlue());
        playerSelectPanel.add(titleLabel);
        playerSelectPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        JButton player1Button = new JButton("Player 1 (Age: 28)");
        JButton player2Button = new JButton("Player 2 (Age: 20)");
        JButton backButton = new JButton("Back");

        player1Button.setAlignmentX(Component.CENTER_ALIGNMENT);
        player2Button.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        player1Button.addActionListener(e -> parentUI.createPlayer(28));
        player2Button.addActionListener(e -> parentUI.createPlayer(20));
        backButton.addActionListener(e -> showMenu());

        playerSelectPanel.add(Box.createVerticalGlue());
        playerSelectPanel.add(player1Button);
        playerSelectPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        playerSelectPanel.add(player2Button);
        playerSelectPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        playerSelectPanel.add(backButton);
        playerSelectPanel.add(Box.createVerticalGlue());
    }

    public void showMenu() {
        parentUI.getContentPane().removeAll();
        parentUI.getContentPane().add(menuPanel);
        parentUI.revalidate();
        parentUI.repaint();
    }

    private void showPlayerSelect() {
        parentUI.getContentPane().removeAll();
        parentUI.getContentPane().add(playerSelectPanel);
        parentUI.revalidate();
        parentUI.repaint();
    }
}