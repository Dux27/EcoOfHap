
public class MainLoop {
    public static boolean running = true;
    public static long TIC_COUNTER = 0;

    static long previousTime = System.currentTimeMillis();

    private static void tick(){
        int TICK_DURATION_MS = 100;
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - previousTime;
            
        if (elapsedTime >= TICK_DURATION_MS) {
            previousTime = currentTime; // Reset the timer
            TIC_COUNTER++;
        }
    
    }
    
    public static void main(String[] args) {
        UI ui = new UI();
        ui.setVisible(true);
    
        boolean printed = false;
        while (running) {
            tick();
            if (TIC_COUNTER % 10 == 0 && !printed) {
                System.console().printf("Tic: %d\n", TIC_COUNTER);
                printed = true;
            } else if (TIC_COUNTER % 10 != 0) {
                printed = false;
            }
        }
    }
}
