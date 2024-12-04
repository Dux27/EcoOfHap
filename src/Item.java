
import java.util.ArrayList;

public class Item {
    private String name;
    private float price;
    private ArrayList<Effect> activeEffects;
    private ArrayList<Effect> passiveEffects;
    
    public Item(String name, float price) {
        this.name = name;
        this.price = price;
        this.activeEffects = new ArrayList<>();
        this.passiveEffects = new ArrayList<>();
    }
    
    public void addActiveEffect(Effect effect) {
        activeEffects.add(effect);
    }
    
    public void addPassiveEffect(Effect effect) {
        passiveEffects.add(effect);
    }
    
    public void use(Player player) {
        for(Effect effect : activeEffects) {
            player.effects.add(effect);
        }
    }
}