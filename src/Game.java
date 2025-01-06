import java.awt.*;
import javax.swing.*;

public class Game {
    private JPanel mainPanel;
    private JPanel housePanel;
    private JPanel shopPanel;

    private final UI parentUI;

    public Game(UI parentUI) {
        this.parentUI = parentUI;
        setupGamePanel();
        setupHousePanel();
        setupShopPanel();
    }

    private JPanel createEarningsSpendingBar(int earnings, int spending) {
        JPanel barPanel = new JPanel();
        barPanel.setLayout(new BoxLayout(barPanel, BoxLayout.X_AXIS));

        JProgressBar earningsBar = new JProgressBar(0, 100);
        earningsBar.setValue(earnings);
        earningsBar.setForeground(Color.GREEN);
        earningsBar.setStringPainted(true);

        JProgressBar spendingBar = new JProgressBar(0, 100);
        spendingBar.setValue(spending);
        spendingBar.setForeground(Color.RED);
        spendingBar.setStringPainted(true);

        barPanel.add(earningsBar);
        barPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        barPanel.add(spendingBar);

        return barPanel;
    }

    private void setupGamePanel() {
        mainPanel = new JPanel(new BorderLayout());

        JPanel upperPanel = new JPanel(new BorderLayout());
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        JPanel bottomPanel = new JPanel(new BorderLayout());

        ImageIcon houseIcon = new ImageIcon("assets/house_button.png");
        ImageIcon shopIcon = new ImageIcon("assets/shop_button.png");
        ImageIcon menuIcon = new ImageIcon("assets/menu_button.png");
        ImageIcon helpIcon = new ImageIcon("assets/help_button.png");

        Image scaledHouseImage = houseIcon.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH);
        Image scaledShopImage = shopIcon.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH);
        Image scaledMenuImage = menuIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        Image scaledHelpImage = helpIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);

        JButton houseButton = new JButton(new ImageIcon(scaledHouseImage));
        JButton shopButton = new JButton(new ImageIcon(scaledShopImage));
        JButton menuButton = new JButton(new ImageIcon(scaledMenuImage));
        JButton helpButton = new JButton(new ImageIcon(scaledHelpImage));

        houseButton.setPreferredSize(new Dimension(65, 65));
        shopButton.setPreferredSize(new Dimension(65, 65));
        menuButton.setPreferredSize(new Dimension(50, 50));
        helpButton.setPreferredSize(new Dimension(50, 50));

        upperPanel.add(houseButton, BorderLayout.WEST);
        upperPanel.add(shopButton, BorderLayout.EAST);

        bottomPanel.add(menuButton, BorderLayout.WEST);
        bottomPanel.add(helpButton, BorderLayout.EAST);

        String iconPath = parentUI.player != null ? parentUI.player.icon : "assets/game_icon.png";
        JLabel avatarLabel = new JLabel(new ImageIcon(iconPath));
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel ageLabel = new JLabel("Age: " + (parentUI.player != null ? parentUI.player.age : "N/A"));
        ageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        ageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JProgressBar happinessBar = new JProgressBar(0, 100);
        happinessBar.setValue(parentUI.player != null ? (int) parentUI.player.happiness : 0);
        happinessBar.setStringPainted(true);
        happinessBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        happinessBar.setMaximumSize(new Dimension(350, 40)); 

        JPanel earningsSpendingBar = createEarningsSpendingBar(50, 30); // Example values
        earningsSpendingBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(avatarLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(ageLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(earningsSpendingBar);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(happinessBar);
        centerPanel.add(Box.createVerticalGlue());

        mainPanel.add(upperPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        houseButton.addActionListener(e -> showHouse());
        shopButton.addActionListener(e -> showShop());
        menuButton.addActionListener(e -> parentUI.activateMenu());
    }

    private void setupHousePanel() {
        housePanel = new JPanel();
        housePanel.setLayout(new BoxLayout(housePanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("HOUSE");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        housePanel.add(Box.createVerticalGlue());
        housePanel.add(titleLabel);
        housePanel.add(Box.createRigidArea(new Dimension(0, 40)));

        JButton backButton = new JButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> showGame());

        housePanel.add(Box.createVerticalGlue());
        housePanel.add(backButton);
        housePanel.add(Box.createVerticalGlue());
    }

    private void setupShopPanel() {
        shopPanel = new JPanel();
        shopPanel.setLayout(new BoxLayout(shopPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("SHOP");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        shopPanel.add(Box.createVerticalGlue());
        shopPanel.add(titleLabel);
        shopPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        JButton backButton = new JButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> showGame());

        shopPanel.add(Box.createVerticalGlue());
        shopPanel.add(backButton);
        shopPanel.add(Box.createVerticalGlue());
    }

    public void showGame() {
        parentUI.getContentPane().removeAll();
        parentUI.getContentPane().add(mainPanel);
        parentUI.revalidate();
        parentUI.repaint();
    }

    public void showHouse() {
        parentUI.getContentPane().removeAll();
        parentUI.getContentPane().add(housePanel);
        parentUI.revalidate();
        parentUI.repaint();
    }

    public void showShop() {
        parentUI.getContentPane().removeAll();
        parentUI.getContentPane().add(shopPanel);
        parentUI.revalidate();
        parentUI.repaint();
    }
}
