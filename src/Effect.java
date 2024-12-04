public class Effect {
    public String name;
    public float happinessChange;
    public float moneyChange;
    public float healthChange;
    public int duration; // in ticks/turns
    
    public Effect(String name, float happinessChange, float moneyChange, float healthChange, int duration) {
        this.name = name;
        this.happinessChange = happinessChange;
        this.moneyChange = moneyChange;
        this.healthChange = healthChange;
        this.duration = duration;
    }
    
    public void applyEffect(Player player) {
        player.happiness += happinessChange;
        player.money += moneyChange;
        player.health += healthChange;
    }
    
    public boolean tick() {
        duration--;
        return duration > 0;
    }
}