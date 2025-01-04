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

    private void setupGamePanel() {
        mainPanel = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel(new GridBagLayout());
        JPanel rightPanel = new JPanel(new GridBagLayout());

        ImageIcon houseIcon = new ImageIcon("assets/house_button.png");
        ImageIcon shopIcon = new ImageIcon("assets/shop_button.png");
        ImageIcon menuIcon = new ImageIcon("assets/menu_button.png");
        ImageIcon helpIcon = new ImageIcon("assets/help_button.png");

        Image scaledHouseImage = houseIcon.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH);
        Image scaledShopImage = shopIcon.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH);
        Image scaledMenuImage = menuIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        Image scaledHelpImage = helpIcon.getImage().getScaledInstance(50,50, Image.SCALE_SMOOTH);

        JButton houseButton = new JButton(new ImageIcon(scaledHouseImage));
        JButton shopButton = new JButton(new ImageIcon(scaledShopImage));
        JButton menuButton = new JButton(new ImageIcon(scaledMenuImage));
        JButton helpButton = new JButton(new ImageIcon(scaledHelpImage));

        houseButton.setPreferredSize(new Dimension(65, 65));
        shopButton.setPreferredSize(new Dimension(65, 65));
        menuButton.setPreferredSize(new Dimension(50, 50));
        helpButton.setPreferredSize(new Dimension(50, 50));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        leftPanel.add(houseButton, gbc);

        gbc.gridy = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        leftPanel.add(menuButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        rightPanel.add(shopButton, gbc);

        gbc.gridy = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        rightPanel.add(helpButton, gbc);

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.EAST);

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
