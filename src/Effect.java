public class Effect {
    public final String name;
    private final String type;
    private final int change;
    private long remainingTicks; 
    private final int activationFrequency;
    private final boolean isLinearFunc;

    public Effect(String name, int change, long durationTicks, int activationFrequency, String type, boolean isLinearFunc) {
        this.name = name;
        this.type = type;
        this.change = change;
        this.remainingTicks = durationTicks;
        this.activationFrequency = activationFrequency;
        this.isLinearFunc = isLinearFunc;
    }

    public void applyEffect(Player player, long tickCounter) {
        if (tickCounter % activationFrequency == 0) {
            int appliedChange = change;
            if (isLinearFunc) 
                appliedChange = calculateLinearChange();

            if ("happy".equals(type)) {
                player.changeHappiness(appliedChange);
            } else if ("money".equals(type)) 
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