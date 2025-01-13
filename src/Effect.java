public class Effect {
    public final String name;
    public final String category;
    public int change;
    public long remainingTicks; 
    private final int activationFrequency;
    private final boolean isLinearFunc;

    private boolean activated = false;

    public Effect(String name, int change, long durationTicks, int activationFrequency, String category, boolean isLinearFunc) {
        this.name = name;
        this.category = category;
        this.change = change;
        this.remainingTicks = durationTicks;
        this.activationFrequency = activationFrequency;
        this.isLinearFunc = isLinearFunc;
    }

    public void applyEffect(Player player) {
        if (MainLoop.TIC_COUNTER % activationFrequency == 0) {
            if (isLinearFunc) 
                change = calculateLinearChange();

            if ("happy".equals(category)) {
                player.changeHappiness(change);
            } else if ("money".equals(category)) 
                if (change > 0 && !activated) {
                    player.moneyGain += change;
                    activated = true;
                } else if (change < 0 && !activated) {
                    player.moneyLoss += (change * -1);
                    activated = true;
                }
                player.changeMoney(change);
                
            System.out.println("Effect applied: " + name);
        }
    }

    private int calculateLinearChange() {
        return (int) (change * (remainingTicks / (double) activationFrequency));
    }

    public void reduceTicks() {
        if (remainingTicks > 0) {
            remainingTicks--;
        }
    }

    public boolean isExpired() {
        return remainingTicks <= 0;
    }
}