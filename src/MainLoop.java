public class MainLoop {
    public static boolean running = false; // Game loop running status
    public static int TICK_DURATION_MS = 1000; 
    public static long TIC_COUNTER = 0; 
    private static boolean tickPrinted = false; // Prevent multiple prints
    private static boolean yearUpdatePrinted = false; // Prevent multiple prints for age update
    public static UI uiInstance; // Store the UI instance
    
    static long previousTime = System.currentTimeMillis();

    private static void tick() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - previousTime;
            
        if (elapsedTime >= TICK_DURATION_MS) {
            previousTime = currentTime; 
            TIC_COUNTER++;
        }
    }

    public static void startGame(UI ui) { 
        if (running) 
            return; // If the game loop is already running, do not start a new thread. Preventing multiple threads from running the game loop.
        running = true;
        uiInstance = ui; // Store the UI instance

        // Create and start the Popup instance
        Popup popup = new Popup(ui.player);
        popup.start();

        new Thread(() -> {
            while (running) {
                tick();
                // DEBUG
                if (TIC_COUNTER % 2 == 0 && !tickPrinted && TIC_COUNTER != 0) {
                    ui.player.updateEffects(); // Apply player effects
                    System.out.println("TICK: " + TIC_COUNTER);
                    tickPrinted = true;
                } else if (TIC_COUNTER % 2 != 0) {
                    tickPrinted = false;
                }
                // Update values at the end of each year 
                if (TIC_COUNTER % 12 == 0 && TIC_COUNTER != 0 && !yearUpdatePrinted) {
                    yearUpdatePrinted = true;
                    for (Item item : ui.player.inventory) {
                        if (("Houses".equals(item.category) && (TIC_COUNTER - item.purchaseTick) % 12 == 0)) {
                            item.increaseValue(0.02f); // Increase value by 2%
                            System.out.println(
                                    "House values updated for year: " + ((TIC_COUNTER / 12) - (item.purchaseTick / 12))
                                            + " - " + item.name + ": $" + item.sellPrice);
                        }
                    }
                    // Update player age
                    ui.player.age++;
                    ui.player.changeHappiness();
                    ui.player.changeMoney();
                    ui.game.updateAgeLabel();

                    // Check if the player dies
                    if (ui.player.calculateChanceToDie()) {
                        System.out.println("Player has died at age: " + ui.player.age);
                        stopGame();
                        ui.activateMenu();
                    }

                    // Trigger a random event popup every year
                    popup.showRandomEvent();
                } else if (TIC_COUNTER % 12 != 0) {
                    yearUpdatePrinted = false;
                }
            }
        }).start();                             
    }

    public static void stopGame() {
        running = false;
    }

    public static void main(String[] args) {
        UI ui = new UI();
        uiInstance = ui; // Initialize the UI instance
        ui.setVisible(true);
    }
}
