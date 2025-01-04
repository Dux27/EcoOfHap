public class MainLoop {
    public static boolean running = true;
    public static int TICK_DURATION_MS = 1000;
    public static long TIC_COUNTER;
    
    static long previousTime = System.currentTimeMillis();

    private static void tick(){
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - previousTime;
            
        if (elapsedTime >= TICK_DURATION_MS) {
            previousTime = currentTime; // Reset the timer
            TIC_COUNTER++;
        }
    
    }
    
    @SuppressWarnings("UnusedAssignment")
    public static void main(String[] args) {
        UI ui = new UI();
        ui.setVisible(true);
    
        boolean printed = false;
        while (running) {
            tick();
            if (TIC_COUNTER % 12 == 0 && !printed) {
                int month = 0;
                System.console().printf("Month: %d\n", month);
                month += 1;
                printed = true;
            } else if (TIC_COUNTER % 12 != 0) {
                printed = false;
            }
        }
    }
}
