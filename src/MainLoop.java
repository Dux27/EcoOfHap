import javax.swing.SwingUtilities;

public class MainLoop {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UI ui = new UI();
                ui.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
}
