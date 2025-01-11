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

        JPanel upperMaiPanel = new JPanel(new BorderLayout());
        JPanel centerMainPanel = new JPanel();
        centerMainPanel.setLayout(new BoxLayout(centerMainPanel, BoxLayout.Y_AXIS));
        JPanel bottomMainPanel = Widgets.bottomPanel(
            e -> parentUI.activateMenu(), 
            e -> showHelp()
        );

        ImageIcon houseIcon = new ImageIcon("assets/house_button.png");
        ImageIcon shopIcon = new ImageIcon("assets/shop_button.png");

        Image scaledHouseImage = houseIcon.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH);
        Image scaledShopImage = shopIcon.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH);

        JButton houseButton = new JButton(new ImageIcon(scaledHouseImage));
        JButton shopButton = new JButton(new ImageIcon(scaledShopImage));

        houseButton.setPreferredSize(new Dimension(65, 65));
        shopButton.setPreferredSize(new Dimension(65, 65));

        upperMaiPanel.add(houseButton, BorderLayout.WEST);
        upperMaiPanel.add(shopButton, BorderLayout.EAST);

        String iconPath = parentUI.player != null ? parentUI.player.icon : "assets/game_icon.png";
        JLabel avatarLabel = new JLabel(new ImageIcon(iconPath));
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel ageLabel = new JLabel("Age: " + (parentUI.player != null ? parentUI.player.age : "N/A"));
        ageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        ageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //JPanel moneyBar = Widgets.barPanel(40, 30, "green", "red");
        //moneyBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        //JPanel happinessBar = Widgets.barPanel(20, 80, "yellow", "grey");
        //happinessBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerMainPanel.add(Box.createVerticalGlue());
        centerMainPanel.add(avatarLabel);
        centerMainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerMainPanel.add(ageLabel);
        centerMainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        //centerMainPanel.add(moneyBar);
        //centerMainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        //centerMainPanel.add(happinessBar);
        centerMainPanel.add(Box.createVerticalGlue());

        mainPanel.add(upperMaiPanel, BorderLayout.NORTH);
        mainPanel.add(centerMainPanel, BorderLayout.CENTER);
        mainPanel.add(bottomMainPanel, BorderLayout.SOUTH);

        houseButton.addActionListener(e -> showHouse());
        shopButton.addActionListener(e -> showShop());
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
        shopPanel = new JPanel(new BorderLayout());

        // UPPER PANEL
        JPanel upperShopPanel = new JPanel(new GridLayout(2, 1));
        ImageIcon upperPanelIcon = new ImageIcon("assets/upper_panel_image.png");
        JLabel upperShopLabel = new JLabel(new ImageIcon(upperPanelIcon.getImage().getScaledInstance(-1, 40, Image.SCALE_SMOOTH)));
        upperShopLabel.setPreferredSize(new Dimension(upperShopLabel.getWidth(), 40));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        ImageIcon stockIcon = new ImageIcon("assets/stock_button.png");
        ImageIcon itemIcon = new ImageIcon("assets/item_button.png");

        Image scaledStockImage = stockIcon.getImage().getScaledInstance(250, 40, Image.SCALE_SMOOTH);
        Image scaledItemImage = itemIcon.getImage().getScaledInstance(250, 40, Image.SCALE_SMOOTH);

        JButton stockButton = new JButton(new ImageIcon(scaledStockImage));
        JButton itemButton = new JButton(new ImageIcon(scaledItemImage));

        stockButton.setPreferredSize(new Dimension(250, 40));
        itemButton.setPreferredSize(new Dimension(250, 40));

        buttonPanel.add(stockButton);
        buttonPanel.add(itemButton);

        upperShopPanel.add(upperShopLabel);
        upperShopPanel.add(buttonPanel);

        JPanel itemsShopPanel = new JPanel();
        itemsShopPanel.setLayout(new BoxLayout(itemsShopPanel, BoxLayout.Y_AXIS));
        JPanel bottomShopPanel = Widgets.bottomPanel(
            e -> showGame(), 
            e -> showHelp()
        );

        // MAIN PANEL 
        shopPanel.add(upperShopPanel, BorderLayout.NORTH);
        shopPanel.add(itemsShopPanel, BorderLayout.CENTER);
        shopPanel.add(bottomShopPanel, BorderLayout.SOUTH);
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

    public void showHelp() {
        // Help functionality
    }
}
