import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Player {
    public String name;
    public int age;
    public int happiness;
    public int money;
    public int health;
    public String icon;
    public List<Item> inventory;
    private List<Effect> activeEffects;
    private Random random = new Random();

    public Player(String name, int age, int happiness, int money, int health, String icon) {
        this.name = name;
        this.age = age;
        this.happiness = happiness;
        this.money = money;
        this.health = health;
        this.icon = icon;
        this.inventory = new ArrayList<>();
        this.activeEffects = new ArrayList<>();
    }

    public void addEffect(Effect effect) {
        activeEffects.add(effect);
    }

    public void updateEffects(long tickCounter) {
        Iterator<Effect> iterator = activeEffects.iterator();
        while (iterator.hasNext()) {
            Effect effect = iterator.next();
            effect.applyEffect(this, tickCounter); 
            effect.reduceTicks();                 
            if (effect.isExpired()) {
                System.out.println("Effect expired: " + effect.name);
                iterator.remove();                
            }
        }
    }

    public void changeHappiness(int amount) {
        happiness += amount;
    }

    public void changeMoney(int amount) {
        money += amount;
    }

    public void addItemToInventory(Item item) {
        inventory.add(item);
    }

    public boolean calculateChanceToDie() {
        if (age < 60) {
            return false; // No chance to die if age is less than 60
        }

        double baseChance = 0.001; // Base chance of dying
        double ageFactor = Math.pow((age / 100.0), 2); // Age factor increases with age
        double healthFactor = (100 - health) / 100.0; // Health factor increases as health decreases

        double chanceToDie = baseChance + ageFactor + healthFactor;
        double randomValue = random.nextDouble();

        System.out.printf("Chance to die: %.2f%%, Random value: %.2f%n", chanceToDie * 100, randomValue * 100);

        return randomValue < chanceToDie;
    }
}

