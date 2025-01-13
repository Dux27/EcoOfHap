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

    private void addItemToPanel(JPanel panel, Item item) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setPreferredSize(new Dimension(450, 120)); 
        itemPanel.setMaximumSize(new Dimension(480, 120));
        itemPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        String imagePath = "assets/" + item.category.toLowerCase() + "_shopItem_1" + ".png";
        ImageIcon itemIcon = new ImageIcon(imagePath);
        Image scaledImage = itemIcon.getImage().getScaledInstance(480, 120, Image.SCALE_SMOOTH);
        JLabel itemLabel = new JLabel(new ImageIcon(scaledImage), JLabel.CENTER);

        itemPanel.add(itemLabel, BorderLayout.CENTER);
        panel.add(itemPanel);
    }
}