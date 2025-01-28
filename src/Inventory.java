import java.awt.*;
import javax.swing.*;

public class Inventory {
    private final Player player;

    public Inventory(Player player) {
        this.player = player;
    }

    public void updateInventoryPanel(JPanel panel) {
        panel.removeAll();
        if (player != null && player.inventory != null) {
            for (Item item : player.inventory) {
                addItemToPanel(panel, item);
            }
        }
        panel.revalidate();
        panel.repaint();
    }

    public static void updateItemsPanel(JPanel itemsShopPanel, String category, int quantity, Player player) {
        itemsShopPanel.removeAll();

        int[] housePrices = {500000, 880000, 1350000};
        int[] carPrices = {5000, 15000, 32000, 74500, 148000};

        for (int i = 0; i < quantity; i++) {
            JPanel itemPanel = createItemPanel(category, i, housePrices, carPrices, player);
            itemsShopPanel.add(itemPanel);
        }

        itemsShopPanel.revalidate();
        itemsShopPanel.repaint();
    }

    private static JPanel createItemPanel(String category, int itemIndex, int[] housePrices, int[] carPrices, Player player) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setPreferredSize(new Dimension(450, 200)); 
        itemPanel.setMaximumSize(new Dimension(450, 200));
        itemPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        String imagePath = "assets/" + category.toLowerCase() + "_item_" + (itemIndex + 1) + ".png";
        ImageIcon itemIcon = new ImageIcon(imagePath);
        Image scaledItemImage = itemIcon.getImage().getScaledInstance(480, 200, Image.SCALE_SMOOTH); // Scale the image
        JButton itemButton = new JButton(new ImageIcon(scaledItemImage));
        itemButton.addActionListener(e -> handleItemPurchase(category, itemIndex, housePrices, carPrices, player, imagePath));

        itemPanel.add(itemButton, BorderLayout.CENTER);
        return itemPanel;
    }

    private static void handleItemPurchase(String category, int itemIndex, int[] housePrices, int[] carPrices, Player player, String imagePath) {
        Widgets.playClickSound();
        int price = calculatePrice(category, itemIndex, housePrices, carPrices);
        if (player.money >= price) {
            int response = JOptionPane.showConfirmDialog(null, "Do you want to buy this item for " + price + " PLN?", "Confirm Purchase", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                processPurchase(player, category, imagePath, price);
            }
        } else {
            handleInsufficientFunds(player, category, itemIndex, housePrices, carPrices, price, imagePath);
        }
    }

    private static void processPurchase(Player player, String category, String imagePath, int price) {
        System.out.println("Item purchased: " + imagePath);
        long purchaseTick = MainLoop.TIC_COUNTER % 12;
        purchaseTick -= MainLoop.TIC_COUNTER;
        Item item = new Item(imagePath, price, category, purchaseTick); // Set item name to icon file name
        player.addItemToInventory(item);
        player.printInventory();

        // Add linear happiness gain effect for 3 years (36 months)
        if (category.equals("Houses")) {
            Effect happinessEffect = new Effect("House Happiness", 100, 36, 12, "happiness", true);
            player.addEffect(happinessEffect);
        }
    }

    private static void handleInsufficientFunds(Player player, String category, int itemIndex, int[] housePrices, int[] carPrices, int price, String imagePath) {
        double interestRate = 0.05; // 5% interest rate
        int loanAmount = price - player.money;
        int monthlyRepayment = (int) ((loanAmount * (interestRate / 12)) * Math.pow(1 + (interestRate / 12), 12 * 30) / (Math.pow(1 + (interestRate / 12), 12 * 30) - 1));
        int yearlyRepayment = monthlyRepayment * 12;

        JOptionPane.showMessageDialog(null, 
            "You don't have enough money to buy this item.\n" +
            "Loan Amount: " + loanAmount + " PLN\n" +
            "Yearly Repayment: " + yearlyRepayment + " PLN", 
            "Insufficient Funds", 
            JOptionPane.INFORMATION_MESSAGE
        );

        int initialResponse = JOptionPane.showConfirmDialog(null, "Do you want to take a loan?", "Loan Option", JOptionPane.YES_NO_OPTION);
        if (initialResponse == JOptionPane.YES_OPTION) {
            if (player.moneyGain - player.moneyLoss < yearlyRepayment) {
                JOptionPane.showMessageDialog(null, 
                    "You don't have enough yearly income to cover the loan repayment.", 
                    "Insufficient Income", 
                    JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            String loanDetails = 
                """
                Taking this loan will allow you to purchase the item, but you will have to repay the loan over the next 30 years.
                Make sure you can manage the yearly repayments with your current income and expenses.
                Loan Details:
                Loan Amount: %d PLN
                Interest Rate: 5%%
                Yearly Repayment: %d PLN
                Your Current Money: %d PLN
                Your Yearly Income: %d PLN
                Your Yearly Expenses: %d PLN
                Do you want to proceed with the loan?
                """.formatted(loanAmount, yearlyRepayment, player.money, player.moneyGain, player.moneyLoss);
            int detailedResponse = JOptionPane.showConfirmDialog(null, loanDetails, "Loan Details", JOptionPane.YES_NO_OPTION);
            if (detailedResponse == JOptionPane.YES_OPTION) {
                player.money += loanAmount;
                Effect creditEffect = new Effect("Mortgage Repayment", -yearlyRepayment, 30 * 12, 12, "money", false);
                player.addEffect(creditEffect);

                // Handle the purchase logic here
                System.out.println("Item purchased with mortgage: " + imagePath);
                long purchaseTick = MainLoop.TIC_COUNTER % 12;
                purchaseTick -= MainLoop.TIC_COUNTER;
                Item item = new Item(imagePath, price, category, purchaseTick); // Set item name to icon file name
                player.addItemToInventory(item);
                String houseIcon = Inventory.convertShopToImg(imagePath);
                Game.updateHouseIcon(houseIcon);    
                player.printInventory();

                // Add linear happiness gain effect for 3 years (36 months)
                if (category.equals("Houses")) {
                    Effect happinessEffect = new Effect("House Happiness", 100, 36, 12, "happiness", true);
                    player.addEffect(happinessEffect);
                }
            }
        }
    }

    private static int calculatePrice(String category, int itemIndex, int[] housePrices, int[] carPrices) {
        return switch (category) {
            case "Houses" -> housePrices[itemIndex % housePrices.length];
            case "Cars" -> carPrices[itemIndex % carPrices.length];
            default -> 1000 + (itemIndex * 1000);
        }; // Default price for other items
    }

    private void addItemToPanel(JPanel panel, Item item) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setPreferredSize(new Dimension(450, 200)); 
        itemPanel.setMaximumSize(new Dimension(480, 200));
        itemPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        String imagePath = item.name; // Use item name to load the icon
        ImageIcon itemIcon = new ImageIcon(imagePath);
        Image scaledImage = itemIcon.getImage().getScaledInstance(480, 200, Image.SCALE_SMOOTH);
        JLabel itemLabel = new JLabel(new ImageIcon(scaledImage), JLabel.CENTER);

        itemPanel.add(itemLabel, BorderLayout.CENTER);
        panel.add(itemPanel);

        itemPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int response = JOptionPane.showConfirmDialog(null, "Do you want to sell this item for " + item.sellPrice + " PLN?", "Confirm Sale", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    player.money += item.sellPrice;
                    player.inventory.remove(item);
                    updateInventoryPanel(panel);
                    System.out.println("Item sold: " + item.name);
                }
            }
        });
    }

    public static String convertShopToImg(String itemName) {
        String[] parts = itemName.split("/");
        if (parts.length >= 2) {
            String fileName = parts[parts.length - 1];
            String[] fileParts = fileName.split("_");
            if (fileParts.length >= 3) {
                String category = fileParts[0];
                String number = fileParts[2].split("\\.")[0]; // Remove the file extension
                return "assets/" + category + "_" + number + ".png";
            }
        }
        return "";
    }

    public void checkAndUpdateHouseIcon() {
        if (!player.hasItem("Houses")) {
            Game.updateHouseIcon("assets/houses_0.png");
        }
    }
}