import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class MoneyAndHappinessGraph extends JPanel {
    private final List<Integer> yearlyMoney;
    private final List<Integer> yearlyHappiness;

    public MoneyAndHappinessGraph(List<Integer> yearlyMoney, List<Integer> yearlyHappiness) {
        this.yearlyMoney = yearlyMoney;
        this.yearlyHappiness = yearlyHappiness;
        setPreferredSize(new Dimension(1200, 400)); // Make the graph wider
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();
        int padding = 50;
        int labelPadding = 25;
        int pointWidth = 4;

        double xScale = ((double) width - 2 * padding - labelPadding) / (yearlyMoney.size() - 1);
        double yScaleMoney = ((double) height - 2 * padding - labelPadding) / (getMaxValue(yearlyMoney) - getMinValue(yearlyMoney));
        double yScaleHappiness = ((double) height - 2 * padding - labelPadding) / (getMaxValue(yearlyHappiness) - getMinValue(yearlyHappiness));

        List<Point> moneyPoints = new ArrayList<>();
        List<Point> happinessPoints = new ArrayList<>();
        for (int i = 0; i < yearlyMoney.size(); i++) {
            int x = (int) (i * xScale + padding + labelPadding);
            int yMoney = (int) ((getMaxValue(yearlyMoney) - yearlyMoney.get(i)) * yScaleMoney + padding);
            int yHappiness = (int) ((getMaxValue(yearlyHappiness) - yearlyHappiness.get(i)) * yScaleHappiness + padding);
            moneyPoints.add(new Point(x, yMoney));
            happinessPoints.add(new Point(x, yHappiness));
        }

        g2d.setColor(Color.WHITE);
        g2d.fillRect(padding + labelPadding, padding, width - 2 * padding - labelPadding, height - 2 * padding - labelPadding);
        g2d.setColor(Color.BLACK);

        // Draw axes with arrows
        g2d.drawLine(padding + labelPadding, height - padding, padding + labelPadding, padding - 10);
        g2d.drawLine(padding + labelPadding, padding - 10, padding + labelPadding - 5, padding);
        g2d.drawLine(padding + labelPadding, padding - 10, padding + labelPadding + 5, padding);

        g2d.drawLine(padding + labelPadding, height - padding, width - padding + 10, height - padding);
        g2d.drawLine(width - padding + 10, height - padding, width - padding, height - padding - 5);
        g2d.drawLine(width - padding + 10, height - padding, width - padding, height - padding + 5);

        // Draw axis labels
        g2d.drawString("Year", width / 2, height - padding + 30);
        g2d.drawString("Value", padding - 30, padding - 10);

        // Draw year marks on the x-axis
        for (int i = 0; i < yearlyMoney.size(); i++) {
            int x = (int) (i * xScale + padding + labelPadding);
            g2d.drawLine(x, height - padding, x, height - padding + 5);
            g2d.drawString(String.valueOf(i + 1), x - 5, height - padding + 20);
        }

        for (int i = 0; i < moneyPoints.size() - 1; i++) {
            int x1 = moneyPoints.get(i).x;
            int y1 = moneyPoints.get(i).y;
            int x2 = moneyPoints.get(i + 1).x;
            int y2 = moneyPoints.get(i + 1).y;
            g2d.setColor(new Color(0, 100, 0)); // Dark green
            g2d.drawLine(x1, y1, x2, y2);
        }

        for (int i = 0; i < happinessPoints.size() - 1; i++) {
            int x1 = happinessPoints.get(i).x;
            int y1 = happinessPoints.get(i).y;
            int x2 = happinessPoints.get(i + 1).x;
            int y2 = happinessPoints.get(i + 1).y;
            g2d.setColor(new Color(204, 204, 0)); // Dark yellow
            g2d.drawLine(x1, y1, x2, y2);
        }

        for (Point point : moneyPoints) {
            g2d.setColor(new Color(0, 100, 0)); // Dark green
            g2d.fillOval(point.x - pointWidth / 2, point.y - pointWidth / 2, pointWidth, pointWidth);
        }

        for (Point point : happinessPoints) {
            g2d.setColor(new Color(204, 204, 0)); // Dark yellow
            g2d.fillOval(point.x - pointWidth / 2, point.y - pointWidth / 2, pointWidth, pointWidth);
        }

        // Draw legend
        g2d.setColor(Color.BLACK);
        g2d.drawString("Money", width - padding - 60, padding + 20);
        g2d.setColor(new Color(0, 100, 0)); // Dark green
        g2d.fillRect(width - padding - 80, padding + 10, 10, 10);

        g2d.setColor(Color.BLACK);
        g2d.drawString("Happiness", width - padding - 60, padding + 40);
        g2d.setColor(new Color(204, 204, 0)); // Dark yellow
        g2d.fillRect(width - padding - 80, padding + 30, 10, 10);
    }

    private int getMinValue(List<Integer> values) {
        int minValue = Integer.MAX_VALUE;
        for (int value : values) {
            minValue = Math.min(minValue, value);
        }
        return minValue;
    }

    private int getMaxValue(List<Integer> values) {
        int maxValue = Integer.MIN_VALUE;
        for (int value : values) {
            maxValue = Math.max(maxValue, value);
        }
        return maxValue;
    }

    public static void showGraph(List<Integer> yearlyMoney, List<Integer> yearlyHappiness) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Money and Happiness Over Time");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 400); // Make the window wider
            frame.setLocationRelativeTo(null); // Center the window on the screen
            frame.add(new MoneyAndHappinessGraph(yearlyMoney, yearlyHappiness));
            frame.setVisible(true);
        });
    }
}
