public class Effect {
    public final String name;
    public final String category;
    public int change;
    public long remainingTicks; 
    public final int activationFrequency;
    public final boolean isLinearFunc;
    public final int totalActivations; // New variable to store total activations

    private boolean activated = false;
    private int counter; // Counter to track remaining activations

    public Effect(String name, int change, long durationTicks, int activationFrequency, String category, boolean isLinearFunc) {
        this.name = name;
        this.category = category;
        this.change = change;
        this.remainingTicks = durationTicks;
        this.activationFrequency = activationFrequency;
        this.isLinearFunc = isLinearFunc;
        this.totalActivations = (int) (durationTicks / activationFrequency); // Calculate total activations
        this.counter = totalActivations; // Initialize counter
    }

    public void applyEffect(Player player) {
        if (MainLoop.TIC_COUNTER % activationFrequency == 0) {
            System.out.println("Effect applied: " + name);

            if (isLinearFunc && activated) {
                calculateLinearChange(player);
            } else if ("happiness".equals(category)) {
                if (change > 0 && !activated) {
                    player.happinessGain += change;
                    activated = true;
                } else if (change < 0 && !activated) {
                    player.happinessLoss += (change * -1);
                    activated = true;
                }
            } else if ("money".equals(category)) {
                if (change > 0 && !activated) {
                    player.moneyGain += change;
                    activated = true;
                } else if (change < 0 && !activated) {
                    player.moneyLoss += (change * -1);
                    activated = true;
                }
            }
        }
    }

    private void calculateLinearChange(Player player) {
        int currentChange = change / totalActivations;

        if ("happiness".equals(category)) {
            if (counter > 0) {
                if (change > 0) {
                    player.happinessGain = Math.max(player.happinessGain - currentChange, 0);
                } else {
                    player.happinessLoss = Math.max(player.happinessLoss - currentChange * -1, 0);
                }
                counter--;
                System.out.println("counter: " + counter);
            }
            
        } else if ("money".equals(category)) {
            if (counter > 0) {
                if (change > 0) {
                    player.moneyGain = Math.max(player.moneyGain - currentChange, 0);
                } else {
                    player.moneyLoss = Math.max(player.moneyLoss - currentChange * -1, 0);
                }
                counter--;
                System.out.println("counter: " + counter);
            }
        }
    }

    public void reduceTicks() {
        if (remainingTicks > 0) {
            remainingTicks--;
        }
    }

    public boolean isExpired(Player player) {
        if (remainingTicks <= 0) {
            removeEffect(player);
            return true;
        }
        return false;
    }

    private void removeEffect(Player player) {
        if ("happiness".equals(category)) {
            if (change > 0) {
                player.happinessGain = Math.max(player.happinessGain - change, 0);
            } else {
                player.happinessLoss = Math.max(player.happinessLoss - (change * -1), 0);
            }
        } else if ("money".equals(category)) {
            if (change > 0) {
                player.moneyGain = Math.max(player.moneyGain - change, 0);
            } else {
                player.moneyLoss = Math.max(player.moneyLoss - (change * -1), 0);
            }
        }
    }
}