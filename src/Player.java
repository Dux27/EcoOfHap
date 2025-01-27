import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Player {
    public String name;
    public int age;
    public int happiness;
    public int happinessLoss;
    public int happinessGain;
    public int money;
    public int moneyLoss;
    public int moneyGain;
    public int health;
    public String icon;
    public List<Item> inventory;
    public List<Effect> activeEffects; // Make activeEffects accessible
    private final Random random = new Random();

    private final UI parentUI;

    public Player(UI parentUI, String name, int age, int happiness, int money, int health, String icon) {
        this.parentUI = parentUI;
        this.name = name;
        this.age = age;
        this.happiness = happiness;
        this.happinessLoss = 1;
        this.happinessGain = 10;
        this.money = money;
        this.moneyLoss = 1;
        this.moneyGain = 1;
        this.health = health;
        this.icon = icon;
        this.inventory = new ArrayList<>();
        this.activeEffects = new ArrayList<>();
    }

    public void addEffect(Effect effect) {
        activeEffects.add(effect);
    }

    public void updateEffects() {
        Iterator<Effect> iterator = activeEffects.iterator();
        while (iterator.hasNext()) {
            Effect effect = iterator.next();
            effect.applyEffect(this); 
            effect.remainingTicks--;

            // Remove effect if it has expired
            if (effect.isExpired(this)) {
                if (effect.isLinearFunc) {
                    if (effect.change > 0) {
                        if (effect.category.equals("money")) {
                            moneyGain = Math.max(moneyGain - effect.change, 0);
                        } else if (effect.category.equals("happiness")) {
                            happinessGain = Math.max(happinessGain - effect.change, 0);
                        }
                    } else {
                        if (effect.category.equals("money")) {
                            moneyLoss = Math.max(moneyLoss - effect.change, 0);
                        } else if (effect.category.equals("happiness")) {
                            happinessLoss = Math.max(happinessLoss - effect.change, 0);
                        }
                    }
                }
                System.out.println("Effect expired: " + effect.name);
                iterator.remove();                
            }
        }
    }

    public void changeHappiness() {
        happiness += happinessGain;
        happiness -= happinessLoss;
        if (happiness < 0) happiness = 0; // Ensure happiness does not go below 0
        parentUI.game.updateBars();
        System.out.println("Happiness: " + happiness);
        System.out.println("Happiness Gain: " + happinessGain);
        System.out.println("Happiness Loss: " + happinessLoss + "\n");
    
    }

    public void changeMoney() {
        money += moneyGain;
        money -= moneyLoss;
        if (money < 0) money = 0; // Ensure money does not go below 0
        parentUI.game.updateBars();
        System.out.println("Money: " + money);
        System.out.println("Money Gain: " + moneyGain);
        System.out.println("Money Loss: " + moneyLoss + "\n");
    }

    public void addItemToInventory(Item item) {
        if (!hasItem(item.name)) {
            if (money >= item.price) {
                inventory.add(item);
                money -= item.price;
                System.out.println("Item purchased: " + item.name);
            } else {
                System.out.println("Not enough money to purchase: " + item.name);
            }
        } else {
            System.out.println("Item already exists in inventory: " + item.name);
        }
    }

    public boolean hasItem(String itemName) {
        for (Item item : inventory) {
            if (item.name.equals(itemName)) {
                return true;
            }
        }
        return false;
    }

    public void printInventory() {
        System.out.println("Player Inventory:");
        for (Item item : inventory) {
            System.out.println("- " + item.name);
        }
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

