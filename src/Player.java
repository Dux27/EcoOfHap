import java.util.ArrayList;

public class Player {
    public String name;
    public int age;
    public float happiness;
    public float money;
    public float health;

    public String iconPath;
    public ArrayList<Effect> effects = new ArrayList<>();
    public Inventory inventory = new Inventory();

    public Player(String name, int age, float happiness, float money, float health, String iconPath) {
        this.name = name;
        this.age = age;
        this.happiness = happiness;
        this.money = money;
        this.health = health;
        this.iconPath = iconPath;
    }

    public void removeEffect(String effectName) {
        effects.removeIf(effect -> effect.name.equals(effectName));
    }

    public void useItem(Item item) {
        item.use(this);
    }
}
