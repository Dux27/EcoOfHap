public class MainLoop {
    public static boolean running = false; // Game loop running status
    public static int TICK_DURATION_MS = 1000; 
    public static long TIC_COUNTER = 0; 
    
    static long previousTime = System.currentTimeMillis();

    private static void tick() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - previousTime;
            
        if (elapsedTime >= TICK_DURATION_MS) {
            previousTime = currentTime; 
            TIC_COUNTER++;
        }
    }

    public static void startGame() {
        running = true;
        new Thread(() -> {
            boolean printed = false;
            while (running) {
                tick();
                // DEBUG
                if (TIC_COUNTER % 2 == 0 && !printed && TIC_COUNTER != 0) {
                    System.out.println("TICK: " + TIC_COUNTER);
                    printed = true;
                } else if (TIC_COUNTER % 2 != 0) {
                    printed = false;
                }
            }
        }).start();                             
    }

    public static void stopGame() {
        running = false;
    }

    public static void main(String[] args) {
        UI ui = new UI();
        ui.setVisible(true);
    }
}
