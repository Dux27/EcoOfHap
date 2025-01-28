import java.awt.*;
import javax.swing.*;

public class Game {
    private JPanel mainPanel;
    private JPanel housePanel;
    private JPanel shopPanel;
    private JPanel itemsShopPanel;
    private JScrollPane scrollPane;
    private JPanel inventoryPanel;
    private JPanel deadPanel;

    // Center main panel components
    private JPanel centerMainPanel;
    private JLabel moneyLabel; 
    private JLabel ageLabel;
    private static ImageIcon houseImageIcon;
    private static JLabel houseImageLabel;
    private Widgets.BarPanel moneyBar; 
    private Widgets.BarPanel happyBar;
    private JLabel avatarLabel; // Add avatarLabel as a class member

    private final UI parentUI;
    private final Inventory inventory;

    public Game(UI parentUI) {
        this.parentUI = parentUI;
        this.inventory = new Inventory(parentUI.player);
        setupGamePanel();
        setupHousePanel();
        setupShopPanel();
        setupInventoryPanel();
        setupDeadPanel();

        // Initialize player with a loan and house if they start at age 27
        if (parentUI.player.age == 27) {
            int housePrice = 500000; // Example price for house_1
            int loanAmount = housePrice;
            int duration = 30; // 30 years loan
            double interestRate = 0.05; // 5% interest rate
            int monthlyRepayment = (int) ((loanAmount * (interestRate / 12)) * Math.pow(1 + (interestRate / 12), 12 * duration) / (Math.pow(1 + (interestRate / 12), 12 * duration) - 1));
            int yearlyRepayment = monthlyRepayment * 12;

            parentUI.player.money += loanAmount;
            Effect creditEffect = new Effect("Mortgage Repayment", -yearlyRepayment, duration * 12, 12, "money", false);
            parentUI.player.addEffect(creditEffect);

            Item house = new Item("assets/houses_item_1.png", housePrice, "Houses", MainLoop.TIC_COUNTER);
            parentUI.player.addItemToInventory(house);

            updateHouseIcon("assets/houses_1.png");
        }
    }

    private void setupGamePanel() {
        mainPanel = createBackgroundPanel("assets/game_background2.png");
        mainPanel.setLayout(new BorderLayout());

        JPanel upperMaiPanel = new JPanel(new BorderLayout());
        upperMaiPanel.setOpaque(false); // Ensure transparency
        centerMainPanel = new JPanel();
        centerMainPanel.setLayout(new BoxLayout(centerMainPanel, BoxLayout.Y_AXIS));
        centerMainPanel.setOpaque(false); // Ensure transparency
        JPanel bottomMainPanel = Widgets.bottomPanel(
            e -> {
                Widgets.addClickSound((JButton) e.getSource());
                parentUI.activateMenu();
            }, 
            e -> {
                Widgets.addClickSound((JButton) e.getSource());
                showHelp();
            }
        );

        ImageIcon houseIcon = new ImageIcon("assets/house_button.png");
        ImageIcon shopIcon = new ImageIcon("assets/shop_button.png");

        Image scaledHouseImage = houseIcon.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH);
        Image scaledShopImage = shopIcon.getImage().getScaledInstance(65, 65, Image.SCALE_SMOOTH);

        JButton houseButton = new JButton(new ImageIcon(scaledHouseImage));
        JButton shopButton = new JButton(new ImageIcon(scaledShopImage));

        houseButton.setPreferredSize(new Dimension(65, 65));
        shopButton.setPreferredSize(new Dimension(65, 65));

        Widgets.addClickSound(houseButton);
        Widgets.addClickSound(shopButton);

        upperMaiPanel.add(houseButton, BorderLayout.WEST);
        upperMaiPanel.add(shopButton, BorderLayout.EAST);

        String iconPath = parentUI.player != null ? parentUI.player.icon : "assets/game_icon.png";
        avatarLabel = new JLabel(new ImageIcon(iconPath));
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        moneyLabel = new JLabel("Money: " + (parentUI.player != null ? parentUI.player.money : "N/A") + " PLN"); // Initialize moneyLabel
        moneyLabel.setFont(new Font("Arial", Font.BOLD, 16));
        moneyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ageLabel = new JLabel("Age: " + (parentUI.player != null ? parentUI.player.age : "N/A")); // Initialize ageLabel
        ageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        ageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        moneyBar = new Widgets().new BarPanel(parentUI.player.moneyGain, parentUI.player.moneyLoss, "green", "red");
        happyBar = new Widgets().new BarPanel(parentUI.player.happinessGain, parentUI.player.happinessLoss, "yellow", "grey");

        centerMainPanel.add(Box.createVerticalGlue());
        centerMainPanel.add(avatarLabel);
        centerMainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerMainPanel.add(ageLabel);
        centerMainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerMainPanel.add(moneyLabel);
        centerMainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerMainPanel.add(moneyBar);
        centerMainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerMainPanel.add(happyBar);
        centerMainPanel.add(Box.createVerticalGlue());

        mainPanel.add(upperMaiPanel, BorderLayout.NORTH);
        mainPanel.add(centerMainPanel, BorderLayout.CENTER);
        mainPanel.add(bottomMainPanel, BorderLayout.SOUTH);

        houseButton.addActionListener(e -> showHouse());
        shopButton.addActionListener(e -> showShop());
    }

    private void setupHousePanel() {
        housePanel = new JPanel(new BorderLayout());

        // UPPER PANEL
        JPanel upperHousePanel = new JPanel(new BorderLayout());
        ImageIcon upperHouseIcon = new ImageIcon("assets/house_logo.png");
        JLabel upperHouseLabel = new JLabel(new ImageIcon(upperHouseIcon.getImage().getScaledInstance(500, 40, Image.SCALE_SMOOTH)));
        upperHouseLabel.setPreferredSize(new Dimension(500, 40));

        ImageIcon inventoryIcon = new ImageIcon("assets/inventory_button.png");
        Image scaledInventoryImage = inventoryIcon.getImage().getScaledInstance(250, 40, Image.SCALE_SMOOTH);
        JButton inventoryButton = new JButton(new ImageIcon(scaledInventoryImage));
        inventoryButton.setPreferredSize(new Dimension(250, 40));
        Widgets.addClickSound(inventoryButton);
        inventoryButton.addActionListener(e -> {
            Widgets.addClickSound(inventoryButton);
            showInventory();
        });

        JPanel inventoryButtonPanel = new JPanel();
        inventoryButtonPanel.add(inventoryButton);

        upperHousePanel.add(upperHouseLabel, BorderLayout.NORTH);
        upperHousePanel.add(inventoryButtonPanel, BorderLayout.SOUTH);

        // CENTER PANEL
        JPanel centerHousePanel = new JPanel();
        centerHousePanel.setLayout(new BoxLayout(centerHousePanel, BoxLayout.Y_AXIS));

        // Load the house image if the player has bought a house
        houseImageLabel = new JLabel();
        houseImageIcon = new ImageIcon("assets/house_0.png");
        Image scaledHouseImage = houseImageIcon.getImage().getScaledInstance(500, 375, Image.SCALE_SMOOTH);
        houseImageLabel.setIcon(new ImageIcon(scaledHouseImage));

        centerHousePanel.add(Box.createVerticalGlue());
        centerHousePanel.add(houseImageLabel);
        centerHousePanel.add(Box.createVerticalGlue());

        // BOTTOM PANEL
        JPanel bottomHousePanel = Widgets.bottomPanel(
            e -> {
                Widgets.addClickSound((JButton) e.getSource());
                showGame();
            }, 
            e -> {
                Widgets.addClickSound((JButton) e.getSource());
                showHelp();
            }
        );

        housePanel.add(upperHousePanel, BorderLayout.NORTH);
        housePanel.add(centerHousePanel, BorderLayout.CENTER);
        housePanel.add(bottomHousePanel, BorderLayout.SOUTH);
    }

    private void setupShopPanel() {
        shopPanel = new JPanel(new BorderLayout());

        // UPPER PANEL
        JPanel upperShopPanel = new JPanel(new GridLayout(2, 1));
        ImageIcon upperPanelIcon = new ImageIcon("assets/shop_logo.png");
        JLabel upperShopLabel = new JLabel(new ImageIcon(upperPanelIcon.getImage().getScaledInstance(500, 40, Image.SCALE_SMOOTH)));
        upperShopLabel.setPreferredSize(new Dimension(upperShopLabel.getWidth(), 40));

        JPanel categoryButtonPanel = new JPanel(new GridLayout(1, 2));
        
        ImageIcon houseButtonIcon = new ImageIcon("assets/houses_button.png");
        ImageIcon carButtonIcon = new ImageIcon("assets/cars_button.png");

        Image scaledHouseButtonImage = houseButtonIcon.getImage().getScaledInstance(250, 40, Image.SCALE_SMOOTH);
        Image scaledCarButtonImage = carButtonIcon.getImage().getScaledInstance(250, 40, Image.SCALE_SMOOTH);

        JButton houseButton = new JButton(new ImageIcon(scaledHouseButtonImage));
        JButton carButton = new JButton(new ImageIcon(scaledCarButtonImage));

        houseButton.setPreferredSize(new Dimension(250, 40));
        carButton.setPreferredSize(new Dimension(250, 40));

        Widgets.addClickSound(houseButton);
        Widgets.addClickSound(carButton);

        categoryButtonPanel.add(houseButton);
        categoryButtonPanel.add(carButton);

        upperShopPanel.add(upperShopLabel);
        upperShopPanel.add(categoryButtonPanel);

        // ITEMS PANEL
        itemsShopPanel = new JPanel(new GridLayout(0, 1));
        scrollPane = new JScrollPane(itemsShopPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        Inventory.updateItemsPanel(itemsShopPanel, "Houses", 3, parentUI.player);

        houseButton.addActionListener(e -> {
            Widgets.addClickSound(houseButton);
            Inventory.updateItemsPanel(itemsShopPanel, "Houses", 3, parentUI.player);
        });
        carButton.addActionListener(e -> {
            Widgets.addClickSound(carButton);
            Inventory.updateItemsPanel(itemsShopPanel, "Cars", 5, parentUI.player);
        });

        // BOTTOM PANEL
        JPanel bottomShopPanel = Widgets.bottomPanel(
            e -> {
                Widgets.addClickSound((JButton) e.getSource());
                showGame();
            }, 
            e -> {
                Widgets.addClickSound((JButton) e.getSource());
                showHelp();
            }
        );

        // MAIN PANEL 
        shopPanel.add(upperShopPanel, BorderLayout.NORTH);
        shopPanel.add(scrollPane, BorderLayout.CENTER);
        shopPanel.add(bottomShopPanel, BorderLayout.SOUTH);
    }

    private void setupInventoryPanel() {
        inventoryPanel = new JPanel(new BorderLayout());
        inventoryPanel.setPreferredSize(new Dimension(450, 120)); // Set preferred size

        // UPPER PANEL
        JPanel upperInventoryPanel = new JPanel(new BorderLayout());
        ImageIcon upperInventoryIcon = new ImageIcon("assets/inventory_logo.png");
        JLabel upperInventoryLabel = new JLabel(new ImageIcon(upperInventoryIcon.getImage().getScaledInstance(500, 40, Image.SCALE_SMOOTH)));
        upperInventoryLabel.setPreferredSize(new Dimension(500, 40));

        upperInventoryPanel.add(upperInventoryLabel, BorderLayout.NORTH);

        // CENTER PANEL
        JPanel centerInventoryPanel = new JPanel();
        centerInventoryPanel.setLayout(new BoxLayout(centerInventoryPanel, BoxLayout.Y_AXIS)); 
        JScrollPane inventoryScrollPane = new JScrollPane(centerInventoryPanel);
        inventoryScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        inventory.updateInventoryPanel(centerInventoryPanel);

        // BOTTOM PANEL
        JPanel bottomInventoryPanel = Widgets.bottomPanel(
            e -> {
                Widgets.addClickSound((JButton) e.getSource());
                showHouse();
            }, 
            e -> {
                Widgets.addClickSound((JButton) e.getSource());
                showHelp();
            }
        );

        inventoryPanel.add(upperInventoryPanel, BorderLayout.NORTH);
        inventoryPanel.add(inventoryScrollPane, BorderLayout.CENTER);
        inventoryPanel.add(bottomInventoryPanel, BorderLayout.SOUTH);
    }

    private void setupDeadPanel() {
        deadPanel = new JPanel(new BorderLayout());

        JLabel deadLabel = new JLabel("You have died.", SwingConstants.CENTER);
        deadLabel.setFont(new Font("Arial", Font.BOLD, 24));
        deadPanel.add(deadLabel, BorderLayout.CENTER);

        JButton quitButton = new JButton("Quit");
        quitButton.setFont(new Font("Arial", Font.BOLD, 16));
        quitButton.addActionListener(e -> System.exit(0));
        deadPanel.add(quitButton, BorderLayout.SOUTH);
    }

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

    public void showGame() {
        parentUI.getContentPane().removeAll();
        parentUI.getContentPane().add(mainPanel);
        parentUI.revalidate();
        parentUI.repaint();
        MainLoop.startGame(parentUI); // Pass the UI instance to the MainLoop
    }

    public void showHouse() {
        inventory.checkAndUpdateHouseIcon();
        parentUI.getContentPane().removeAll();
        parentUI.getContentPane().add(housePanel);
        parentUI.revalidate();
        parentUI.repaint();
        MainLoop.stopGame(); // Stop the game loop
    }

    public void showShop() {
        parentUI.getContentPane().removeAll();
        parentUI.getContentPane().add(shopPanel);
        parentUI.revalidate();
        parentUI.repaint();
        MainLoop.stopGame(); // Stop the game loop
    }

    public void showInventory() {
        inventory.updateInventoryPanel((JPanel) ((JScrollPane) inventoryPanel.getComponent(1)).getViewport().getView());
        parentUI.getContentPane().removeAll();
        parentUI.getContentPane().add(inventoryPanel);
        parentUI.revalidate();
        parentUI.repaint();
        MainLoop.stopGame(); // Stop the game loop
    }

    public void showHelp() {
        // Help functionality
    }

    public void showDeadScreen() {
        parentUI.getContentPane().removeAll();
        parentUI.getContentPane().add(deadPanel);
        parentUI.revalidate();
        parentUI.repaint();
        JOptionPane.showMessageDialog(parentUI, "You have died at age: " + parentUI.player.age, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0); // Quit the game after showing the dead screen
    }

    public void updateAgeLabel() {
        ageLabel.setText("Age: " + parentUI.player.age);
        if (parentUI.player.age >= 27 && parentUI.player.icon.equals("assets/player1_icon.png")) {
            parentUI.player.icon = "assets/player2_icon.png";
            avatarLabel.setIcon(new ImageIcon(parentUI.player.icon));
        }
    }

    public void updateMoneyLabel() {
        moneyLabel.setText("Money: " + parentUI.player.money + " PLN");
    }

    public static void updateHouseIcon(String path) {
        houseImageIcon = new ImageIcon(path);
        Image scaledHouseImage = houseImageIcon.getImage().getScaledInstance(500, 375, Image.SCALE_SMOOTH);
        houseImageLabel.setIcon(new ImageIcon(scaledHouseImage));
    }

    public void updateBars() {
        moneyBar.updateValues(parentUI.player.moneyGain, parentUI.player.moneyLoss);
        happyBar.updateValues(parentUI.player.happinessGain, parentUI.player.happinessLoss);
        centerMainPanel.revalidate();
        centerMainPanel.repaint();
    }
}
