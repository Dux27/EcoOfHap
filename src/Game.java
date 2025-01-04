import java.awt.*;
import javax.swing.*;

public class Game {
    private JPanel mainPanel;

    private final UI parentUI;

    public Game(UI parentUI) {
        this.parentUI = parentUI;
        setupMainPanel();
    }

    private void setupMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        ImageIcon leftIcon = new ImageIcon("assets/left_icon.png");
        ImageIcon rightIcon = new ImageIcon("assets/right_icon.png");

        Image scaledLeftImage = leftIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        Image scaledRightImage = rightIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);

        JButton leftButton = new JButton(new ImageIcon(scaledLeftImage));
        JButton rightButton = new JButton(new ImageIcon(scaledRightImage));

        leftButton.setPreferredSize(new Dimension(50, 50));
        rightButton.setPreferredSize(new Dimension(50, 50));

        leftPanel.add(leftButton);
        rightPanel.add(rightButton);

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        mainPanel.add(centerPanel, BorderLayout.CENTER);
    }

    public void showMainPanel() {
        parentUI.getContentPane().removeAll();
        parentUI.getContentPane().add(mainPanel);
        parentUI.revalidate();
        parentUI.repaint();
    }
}
