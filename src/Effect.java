public class Effect {
    public final String name;
    public final String category;
    public int change;
    public long remainingTicks; 
    public final int activationFrequency;
    public final boolean isLinearFunc;

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
            if (isLinearFunc && activated) 
                calculateLinearChange(player);
                
            System.out.println("Effect applied: " + name);
            if ("happiness".equals(category)) {
                if (change > 0 && !activated) {
                    player.happinessGain += change;
                    activated = true;
                } else if (change < 0 && !activated) {
                    player.happinessLoss += (change * -1);
                    activated = true;
                }
                player.changeHappiness(change);
            } else if ("money".equals(category)) {
                if (change > 0 && !activated) {
                    player.moneyGain += change;
                    activated = true;
                } else if (change < 0 && !activated) {
                    player.moneyLoss += (change * -1);
                    activated = true;
                }
                player.changeMoney(change);
            }
        }
    }

    private void calculateLinearChange(Player player) {
        double decrement = (double) change / (remainingTicks / activationFrequency);
        if ("happiness".equals(category)) {
            if (change > 0) {
                player.happinessGain -= decrement;
                if (remainingTicks <= activationFrequency) {
                    player.happinessGain -= player.happinessGain % decrement;
                }
            } else {
                player.happinessLoss -= decrement * -1;
                if (remainingTicks <= activationFrequency) {
                    player.happinessLoss -= player.happinessLoss % (decrement * -1);
                }
            }
            player.changeHappiness(change);
        } else if ("money".equals(category)) {
            if (change > 0) {
                player.moneyGain -= decrement;
                if (remainingTicks <= activationFrequency) {
                    player.moneyGain -= player.moneyGain % decrement;
                }
            } else {
                player.moneyLoss -= decrement * -1;
                if (remainingTicks <= activationFrequency) {
                    player.moneyLoss -= player.moneyLoss % (decrement * -1);
                }
            }
            player.changeMoney(change);
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