import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
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

        JPanel earningsnegativeBar = createBar(50, 30, "green", "red");
        earningsnegativeBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel happinessBar = createBar(50, 30, "yellow", "grey");
        happinessBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(avatarLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(ageLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(earningsnegativeBar);
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

    private JPanel createBar(int positive, int negative, String positivePattern, String negativePattern) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)) {
            private final Random random = new Random();

            private final BufferedImage[] positivePatterns;
            private final BufferedImage[] negativePatterns;
            private int index = 0;

            {
                positivePatterns = loadPatterns(positivePattern);
                negativePatterns = loadPatterns(negativePattern);

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        index = (index + 1) % negativePatterns.length;
                        repaint();
                    }
                }, 0, 150);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Rectangle for the outline
                g2d.setColor(Color.BLACK);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Scaling and drawing the positive and negative patterns
                if (positivePatterns[index] != null && negativePatterns[index] != null) {
                    BufferedImage scaledPositivePattern = scalePattern(positivePatterns[index], 4);
                    BufferedImage scaledNegativePattern = scalePattern(negativePatterns[index], 4);

                    BufferedImage positiveImage = getRandomSubImage(scaledPositivePattern);
                    BufferedImage negativeImage = getRandomSubImage(scaledNegativePattern);

                    TexturePaint positivePaint = new TexturePaint(positiveImage,
                            new Rectangle(0, 0, getWidth(), getHeight()));
                    TexturePaint negativePaint = new TexturePaint(negativeImage,
                            new Rectangle(0, 0, getWidth(), getHeight()));

                    drawBar(g2d, positivePaint, negativePaint, positive, negative);
                }
            }

            // Load the patterns from the assets folder
            private BufferedImage[] loadPatterns(String patternName) {
                BufferedImage[] patterns = new BufferedImage[3];
                try {
                    for (int i = 0; i < 3; i++) {
                        patterns[i] = ImageIO.read(new File("assets/patterns/" + patternName + "_pattern_" + (i + 1) + ".jpg"));
                        System.out.println("Loaded pattern: " + "assets/patterns/" + patternName + "_pattern_" + (i + 1) + ".jpg");
                    }
                } catch (IOException e) {
                    System.err.println("Error loading patterns: " + e.getMessage());
                }
                return patterns;
            }

            // Scale the pattern image
            private BufferedImage scalePattern(BufferedImage pattern, int scale) {
                BufferedImage scaledPattern = new BufferedImage(pattern.getWidth() / scale, pattern.getHeight() / scale,
                        BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = scaledPattern.createGraphics();
                g.drawImage(pattern, 0, 0, scaledPattern.getWidth(), scaledPattern.getHeight(), null);
                g.dispose();
                return scaledPattern;
            }

            // Get a random subimage from the pattern
            private BufferedImage getRandomSubImage(BufferedImage pattern) {
                int x = Math.max(0, random.nextInt(Math.max(1, pattern.getWidth() - getWidth())));
                int y = Math.max(0, random.nextInt(Math.max(1, pattern.getHeight() - getHeight())));
                int width = Math.min(getWidth(), pattern.getWidth() - x);
                int height = Math.min(getHeight(), pattern.getHeight() - y);
                return pattern.getSubimage(x, y, width, height);
            }

            // Draw the bar with the positive and negative patterns
            private void drawBar(Graphics2D g2d, TexturePaint positivePaint, TexturePaint negativePaint, int positive, int negative) {
                int total = positive + negative;
                int positiveWidht = (int) ((positive / (double) total) * getWidth());
                int negativeWidth = getWidth() - positiveWidht;

                boolean roundPositive = false;
                boolean roundNegative = false;
                if (positive == 0 && negative == 0) {
                    positiveWidht = getWidth() / 2;
                    negativeWidth = getWidth() / 2;
                } else if (negative == 0 || positive / negative > 12) {
                    positiveWidht = getWidth();
                    negativeWidth = 0;
                    roundPositive = true;
                } else if (positive == 0 || negative / positive > 12 ) {
                    positiveWidht = 0;
                    negativeWidth = getWidth();
                    roundNegative = true;
                }

                g2d.setPaint(positivePaint);
                g2d.fillRoundRect(3, 3, positiveWidht - 3, getHeight() - 6, 20, 20);
                if (roundPositive)
                    g2d.fillRoundRect(3, 3, positiveWidht - 3, getHeight() - 6, 20, 20);
                else
                    g2d.fillRect(positiveWidht - 20, 3, 20, getHeight() - 6);

                g2d.setPaint(negativePaint);
                g2d.fillRoundRect(positiveWidht, 3, negativeWidth - 3, getHeight() - 6, 20, 20);
                if (roundNegative)
                    g2d.fillRoundRect(positiveWidht, 3, negativeWidth - 3, getHeight() - 6, 20, 20);
                else
                    g2d.fillRect(positiveWidht, 3, 20, getHeight() - 6); 
            }
        };
        panel.setPreferredSize(new Dimension(340, 30)); // Adjusted size
        panel.setMaximumSize(new Dimension(340, 30)); // Ensure it doesn't stretch
        return panel;
    }
}
