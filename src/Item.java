import java.util.ArrayList;
import java.util.List;

public class Item {
    private String name;
    private final float price;
    private List<Effect> activeEffects;

    public Item(String name, float price) {
        this.name = name;
        this.price = price;
        this.activeEffects = new ArrayList<>();
    }

    public void addActiveEffect(Effect effect) {
        activeEffects.add(effect);
    }
}