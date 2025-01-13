import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Item {
    public final String name;
    public final String category;
    public final int price;
    public int sellPrice;
    public List<Effect> activeEffects;

    public Item(String name, int price, String category) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.activeEffects = new ArrayList<>();
        Random random = new Random();
        switch (category) {
            case "Houses":
                float multiplier = 0.8f + (1.2f - 0.8f) * random.nextFloat();
                this.sellPrice = (int) (price * multiplier);
                break;
            case "Cars":
                this.sellPrice = (int) (price * 0.8);
                break;
            default:
                System.err.println("Invalid category: " + category);
                this.sellPrice = 0;
                break;
        }
    }

    public void addActiveEffect(Effect effect) {
        activeEffects.add(effect);
    }

    public String getName() {
        return name;
    }

    public void increaseValue(float percentage) {
        sellPrice += sellPrice * percentage;
    }
}