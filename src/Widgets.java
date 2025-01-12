import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Widgets {

    public static JPanel bottomPanel(Consumer<ActionEvent> backAction, Consumer<ActionEvent> helpAction) { 
        JPanel bottomPanel = new JPanel(new BorderLayout());

        ImageIcon backIcon = new ImageIcon("assets/back_button.png");
        ImageIcon helpIcon = new ImageIcon("assets/help_button.png");
        
        Image scaledBackImage = backIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        Image scaledHelpImage = helpIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);

        JButton backButton = new JButton(new ImageIcon(scaledBackImage));
        JButton helpButton = new JButton(new ImageIcon(scaledHelpImage));

        backButton.setPreferredSize(new Dimension(50, 50));
        helpButton.setPreferredSize(new Dimension(50, 50));

        backButton.addActionListener(e -> backAction.accept(e));
        helpButton.addActionListener(e -> helpAction.accept(e));

        bottomPanel.add(backButton, BorderLayout.WEST);
        bottomPanel.add(helpButton, BorderLayout.EAST);

        return bottomPanel;
    }

    public static JPanel barPanel(int positive, int negative, String positivePattern, String negativePattern) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)) {
            private final Random random = new Random();

            private final BufferedImage[] positivePatterns;
            private final BufferedImage[] negativePatterns;
            private int index = 0;

            // Constructor
            {
                positivePatterns = loadPatterns(positivePattern);
                negativePatterns = loadPatterns(negativePattern);

                // Next pattern every 150ms
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        index = (index + 1) % negativePatterns.length;
                        repaint();
                    }
                }, 0, 500);
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
                int positiveWidth = (int) ((positive / (double) total) * getWidth());
                int negativeWidth = getWidth() - positiveWidth;
                
                boolean roundPositive = false;
                boolean roundNegative = false;
                if (positive == 0 && negative == 0) {
                    positiveWidth = getWidth() / 2;
                    negativeWidth = getWidth() / 2;
                } else if (negative == 0 || positive / negative > 12) {
                    positiveWidth = getWidth();
                    negativeWidth = 0;
                    roundPositive = true;
                } else if (positive == 0 || negative / positive > 12 ) {
                    positiveWidth = 0;
                    negativeWidth = getWidth();
                    roundNegative = true;
                }

                g2d.setPaint(positivePaint);
                g2d.fillRoundRect(3, 3, positiveWidth - 3, getHeight() - 6, 20, 20);
                if (roundPositive)
                    g2d.fillRoundRect(3, 3, positiveWidth - 3, getHeight() - 6, 20, 20);
                else
                    g2d.fillRect(positiveWidth - 20, 3, 20, getHeight() - 6);

                g2d.setPaint(negativePaint);
                g2d.fillRoundRect(positiveWidth, 3, negativeWidth - 3, getHeight() - 6, 20, 20);
                if (roundNegative)
                    g2d.fillRoundRect(positiveWidth, 3, negativeWidth - 3, getHeight() - 6, 20, 20);
                else
                    g2d.fillRect(positiveWidth, 3, 20, getHeight() - 6); 
            }
        };
        panel.setPreferredSize(new Dimension(340, 30)); 
        panel.setMaximumSize(new Dimension(340, 30)); 
        return panel;
    }

    public static void updateItemsPanel(JPanel itemsShopPanel, String category, int quantity) {
        itemsShopPanel.removeAll();

        for (int i = 0; i < quantity; i++) {
            JPanel itemPanel = new JPanel();
            itemPanel.setPreferredSize(new Dimension(450, 120)); 
            itemPanel.setMaximumSize(new Dimension(450, 120));
            itemPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            JLabel itemLabel = new JLabel(category + " Item " + (i + 1));
            itemPanel.add(itemLabel);

            itemsShopPanel.add(itemPanel);
        }

        itemsShopPanel.revalidate();
        itemsShopPanel.repaint();
    }
}
