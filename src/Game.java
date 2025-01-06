import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
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

        JPanel earningsSpendingBar = createEarningsSpendingBar(50, 30);
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

    private JPanel createEarningsSpendingBar(int earnings, int spending) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Rectangle for the outline
                g2d.setColor(Color.BLACK);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Load custom pattern images
                BufferedImage earningsPatternImage = null;
                BufferedImage spendingPatternImage = null;
                try {
                    earningsPatternImage = ImageIO.read(new File("assets/patterns/green_pattern_1.jpg"));
                    spendingPatternImage = ImageIO.read(new File("assets/patterns/red_pattern_1.jpg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (earningsPatternImage != null && spendingPatternImage != null) {
                    // Further scale down the pattern images to make the patterns appear larger
                    BufferedImage scaledEarningsPattern = new BufferedImage(earningsPatternImage.getWidth() / 4, earningsPatternImage.getHeight() / 4, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D gEarnings = scaledEarningsPattern.createGraphics();
                    gEarnings.drawImage(earningsPatternImage, 0, 0, scaledEarningsPattern.getWidth(), scaledEarningsPattern.getHeight(), null);
                    gEarnings.dispose();

                    BufferedImage scaledSpendingPattern = new BufferedImage(spendingPatternImage.getWidth() / 4, spendingPatternImage.getHeight() / 4, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D gSpending = scaledSpendingPattern.createGraphics();
                    gSpending.drawImage(spendingPatternImage, 0, 0, scaledSpendingPattern.getWidth(), scaledSpendingPattern.getHeight(), null);
                    gSpending.dispose();

                    TexturePaint earningsPaint = new TexturePaint(scaledEarningsPattern, new Rectangle(0, 0, scaledEarningsPattern.getWidth(), scaledEarningsPattern.getHeight()));
                    TexturePaint spendingPaint = new TexturePaint(scaledSpendingPattern, new Rectangle(0, 0, scaledSpendingPattern.getWidth(), scaledSpendingPattern.getHeight()));

                    // Rectangles for earnings and spending
                    int total = earnings + spending;
                    int earningsWidth = (int) ((earnings / (double) total) * getWidth());
                    int spendingWidth = getWidth() - earningsWidth;

                    g2d.setPaint(earningsPaint);
                    g2d.fillRoundRect(3, 3, earningsWidth - 3, getHeight() - 6, 20, 20); 
                    g2d.fillRect(earningsWidth - 20, 3, 20, getHeight() - 6);
                    
                    g2d.setPaint(spendingPaint);
                    g2d.fillRoundRect(earningsWidth, 3, spendingWidth - 3, getHeight() - 6, 20, 20); 
                    g2d.fillRect(earningsWidth, 3, 20, getHeight() - 6); 
                }
            }
        };
        panel.setPreferredSize(new Dimension(300, 40)); // Adjusted size
        panel.setMaximumSize(new Dimension(300, 40)); // Ensure it doesn't stretch
        return panel;
    }
}
