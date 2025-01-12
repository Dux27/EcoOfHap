import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Player {
    public String name;
    public int age;
    public int happiness;
    public int money;
    public int health;
    public String icon;
    public List<Item> inventory;
    private List<Effect> activeEffects;

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
}

