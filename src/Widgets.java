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
import javax.sound.sampled.*;
import javax.swing.*;

public class Widgets {

    /**
     * Create a bottom panel with back and help buttons.
     * @param backAction The action to perform when the back button is clicked.
     * @param helpAction The action to perform when the help button is clicked.
     * @return The created bottom panel.
     */
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

        backButton.addActionListener(e -> {
            playClickSound();
            backAction.accept(e);
        });
        helpButton.addActionListener(e -> {
            playClickSound();
            helpAction.accept(e);
        });

        bottomPanel.add(backButton, BorderLayout.WEST);
        bottomPanel.add(helpButton, BorderLayout.EAST);

        return bottomPanel;
    }

    /**
     * Add a click sound to a button.
     * @param button The button to add the click sound to.
     */
    public static void addClickSound(JButton button) {
        button.addActionListener(e -> playClickSound());
    }

    /**
     * Play a click sound.
     */
    public static void playClickSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("assets/mouse_click.wav").getAbsoluteFile());
            Clip clickClip = AudioSystem.getClip();
            clickClip.open(audioInputStream);
            clickClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error playing click sound: " + e.getMessage());
        }
    }

    public class BarPanel extends JPanel {
        private int positive;
        private int negative;
        private final Random random = new Random();
        private final BufferedImage[] positivePatterns;
        private final BufferedImage[] negativePatterns;
        private int index = 0;

        /**
         * Constructor for BarPanel class.
         * @param positive The positive value.
         * @param negative The negative value.
         * @param positivePattern The positive pattern name.
         * @param negativePattern The negative pattern name.
         */
        public BarPanel(int positive, int negative, String positivePattern, String negativePattern) {
            this.positive = positive;
            this.negative = negative;
            this.positivePatterns = loadPatterns(positivePattern);
            this.negativePatterns = loadPatterns(negativePattern);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    index = (index + 1) % negativePatterns.length;
                    repaint();
                }
            }, 0, 500);

            setPreferredSize(new Dimension(340, 30));
            setMaximumSize(new Dimension(340, 30));
        }

        /**
         * Update the values of the bar.
         * @param positive The new positive value.
         * @param negative The new negative value.
         */
        public void updateValues(int positive, int negative) {
            this.positive = positive;
            this.negative = negative;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            g2d.setColor(Color.BLACK);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

            if (positivePatterns[index] != null && negativePatterns[index] != null) {
                BufferedImage scaledPositivePattern = scalePattern(positivePatterns[index], 4);
                BufferedImage scaledNegativePattern = scalePattern(negativePatterns[index], 4);

                BufferedImage positiveImage = getRandomSubImage(scaledPositivePattern);
                BufferedImage negativeImage = getRandomSubImage(scaledNegativePattern);

                TexturePaint positivePaint = new TexturePaint(positiveImage, new Rectangle(0, 0, getWidth(), getHeight()));
                TexturePaint negativePaint = new TexturePaint(negativeImage, new Rectangle(0, 0, getWidth(), getHeight()));

                drawBar(g2d, positivePaint, negativePaint, positive, negative);
            }
        }

        /**
         * Load patterns from files.
         * @param patternName The pattern name.
         * @return An array of loaded patterns.
         */
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

        /**
         * Scale a pattern.
         * @param pattern The pattern to scale.
         * @param scale The scale factor.
         * @return The scaled pattern.
         */
        private BufferedImage scalePattern(BufferedImage pattern, int scale) {
            BufferedImage scaledPattern = new BufferedImage(pattern.getWidth() / scale, pattern.getHeight() / scale,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = scaledPattern.createGraphics();
            g.drawImage(pattern, 0, 0, scaledPattern.getWidth(), scaledPattern.getHeight(), null);
            g.dispose();
            return scaledPattern;
        }

        /**
         * Get a random subimage from a pattern.
         * @param pattern The pattern to get the subimage from.
         * @return The random subimage.
         */
        private BufferedImage getRandomSubImage(BufferedImage pattern) {
            int x = Math.max(0, random.nextInt(Math.max(1, pattern.getWidth() - getWidth())));
            int y = Math.max(0, random.nextInt(Math.max(1, pattern.getHeight() - getHeight())));
            int width = Math.min(getWidth(), pattern.getWidth() - x);
            int height = Math.min(getHeight(), pattern.getHeight() - y);
            return pattern.getSubimage(x, y, width, height);
        }

        /**
         * Draw the bar with positive and negative values.
         * @param g2d The Graphics2D object.
         * @param positivePaint The positive paint.
         * @param negativePaint The negative paint.
         * @param positive The positive value.
         * @param negative The negative value.
         */
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
    }

    /**
     * Calculate the price of an item based on its category and index.
     * @param category The category of the item.
     * @param itemIndex The index of the item.
     * @param housePrices The array of house prices.
     * @param carPrices The array of car prices.
     * @return The calculated price.
     */
    public static int calculatePrice(String category, int itemIndex, int[] housePrices, int[] carPrices) {
        if (category.equals("Houses")) {
            return housePrices[itemIndex % housePrices.length];
        } else if (category.equals("Cars")) {
            return carPrices[itemIndex % carPrices.length];
        } else {
            return 1000 + (itemIndex * 1000); // Default price for other items
        }
    }
}
