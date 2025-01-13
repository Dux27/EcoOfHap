import java.util.ArrayList;
import java.util.List;

public class Item {
    public final String name;
    public final String category;
    public final int price;
    public int sellPrice;
    public List<Effect> activeEffects;
    public final long purchaseTick; // Add field to store the purchase tick

    public Item(String name, int price, String category, long purchaseTick) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.purchaseTick = purchaseTick; // Initialize purchase tick
        this.activeEffects = new ArrayList<>();
        switch (category) {
            case "Houses":
                this.sellPrice = price;
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

    public void increaseValue(float percentage) {
        sellPrice += sellPrice * percentage;
    }
}