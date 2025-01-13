public class Effect {
    public final String name;
    public final String category;
    public final String type;
    public final int change;
    public long remainingTicks; 
    private final int activationFrequency;
    private final boolean isLinearFunc;

    public Effect(String name, int change, long durationTicks, int activationFrequency, String category, String type, boolean isLinearFunc) {
        this.name = name;
        this.category = category;
        this.type = type;
        this.change = change;
        this.remainingTicks = durationTicks;
        this.activationFrequency = activationFrequency;
        this.isLinearFunc = isLinearFunc;
    }

    public void applyEffect(Player player) {
        if (MainLoop.TIC_COUNTER % activationFrequency == 0) {
            int appliedChange = change;
            if (isLinearFunc) 
                appliedChange = calculateLinearChange();

            if ("happy".equals(category)) {
                player.changeHappiness(appliedChange);
            } else if ("money".equals(category)) 
                player.changeMoney(appliedChange);
                
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