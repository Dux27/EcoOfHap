public class Effect {
    private final String name;
    private final int happinessImpact;
    private final int moneyImpact;
    private long remainingTicks; 
    private final int activationFrequency; 

    public Effect(String name, int happinessImpact, int moneyImpact, long durationTicks, int activationFrequency) {
        this.name = name;
        this.happinessImpact = happinessImpact;
        this.moneyImpact = moneyImpact;
        this.remainingTicks = durationTicks;
        this.activationFrequency = activationFrequency;
    }

    public String getName() {
        return name;
    }

    public void applyEffect(Player player, long tickCounter) {
        if (tickCounter % activationFrequency == 0) { 
            player.changeHappiness(happinessImpact);
            player.changeMoney(moneyImpact);
            System.out.println("Effect applied: " + name);
        }
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