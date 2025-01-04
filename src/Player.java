import java.util.Iterator;
import java.util.List;

public class Player {
    public String name;
    public int age;
    public float happiness;
    public float money;
    public float health;

    public String iconPath;
    private List<Effect> activeEffects;
    public Inventory inventory = new Inventory();

    public Player(String name, int age, float happiness, float money, float health, String iconPath) {
        this.name = name;
        this.age = age;
        this.happiness = happiness;
        this.money = money;
        this.health = health;
        this.iconPath = iconPath;
    }

    public void addEffect(Effect effect) {
        activeEffects.add(effect);
    }

    public void updateEffects(long tickCounter) {
        Iterator<Effect> iterator = activeEffects.iterator();
        while (iterator.hasNext()) {
            Effect effect = iterator.next();
            effect.applyEffect(this, tickCounter); // Wpływ efektu na gracza co określoną liczbę tików
            effect.reduceTicks();                 // Redukcja czasu trwania
            if (effect.isExpired()) {
                System.out.println("Effect expired: " + effect.getName());
                iterator.remove();                // Usunięcie efektu po wygaśnięciu
            }
        }
    }

    public void changeHappiness(int amount) {
        happiness += amount;
    }

    public void changeMoney(int amount) {
        money += amount;
    }

}

