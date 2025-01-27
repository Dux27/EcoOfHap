import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;
import javax.swing.*;

public class Popup {
    private final JFrame frame;
    private final Random random = new Random();
    private final List<Event> events;
    private final Map<String, Long> eventCooldowns = new HashMap<>();
    private boolean isPopupActive = false;
    public final Player player;

    public Popup(Player player) {
        this.player = player; // Use the player instance from MainLoop
        this.events = createEventLibrary();
        frame = new JFrame("Random Event");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }

    public void start() {
        scheduleNextPopup();
    }

    private void scheduleNextPopup() {
        int delay = (1 + random.nextInt(4)) * 12 * 1000; // Random delay between 1-4 years (12 ticks per year)
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isPopupActive) {
                    showRandomEvent();
                }
                scheduleNextPopup(); // Schedule the next popup
            }
        }, delay);
    }

    public void showRandomEvent() {
        if (random.nextInt(5) != 0) { // Only proceed if the random number between 1 and 5 is 1
            return;
        }

        isPopupActive = true;
        MainLoop.stopGame(); // Stop the game loop

        Event event = selectRandomEvent();
        if (event == null) {
            isPopupActive = false;
            MainLoop.startGame(MainLoop.uiInstance); // Restart the game loop if no event is selected
            return;
        }

        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JLabel nameLabel = new JLabel(event.name, SwingConstants.CENTER);
        JLabel descriptionLabel = new JLabel("<html><body style='text-align: center;'>" + event.description + "</body></html>", SwingConstants.CENTER);

        frame.add(nameLabel, BorderLayout.NORTH);
        frame.add(descriptionLabel, BorderLayout.CENTER);

        if (event.type == EventType.OK) {
            JButton okButton = new JButton("OK");
            okButton.addActionListener(e -> {
                event.applyEffect();
                frame.dispose();
                isPopupActive = false;
                MainLoop.startGame(MainLoop.uiInstance); // Restart the game loop
            });
            frame.add(okButton, BorderLayout.SOUTH);
        } else if (event.type == EventType.YES_NO) {
            JPanel buttonPanel = new JPanel();
            JButton yesButton = new JButton("Yes");
            JButton noButton = new JButton("No");

            yesButton.addActionListener(e -> {
                event.applyEffect();
                frame.dispose();
                isPopupActive = false;
                MainLoop.startGame(MainLoop.uiInstance); // Restart the game loop
            });

            noButton.addActionListener(e -> {
                frame.dispose();
                isPopupActive = false;
                MainLoop.startGame(MainLoop.uiInstance); // Restart the game loop
            });

            buttonPanel.add(yesButton);
            buttonPanel.add(noButton);
            frame.add(buttonPanel, BorderLayout.SOUTH);
        }

        frame.setVisible(true);
        eventCooldowns.put(event.name, MainLoop.TIC_COUNTER + event.cooldown);
    }

    private Event selectRandomEvent() {
        List<Event> availableEvents = new ArrayList<>();
        long currentTick = MainLoop.TIC_COUNTER;

        for (Event event : events) {
            if (!eventCooldowns.containsKey(event.name) || eventCooldowns.get(event.name) <= currentTick) {
                availableEvents.add(event);
            }
        }

        if (availableEvents.isEmpty()) {
            return null;
        }

        int rarityRoll = random.nextInt(100);
        List<Event> filteredEvents;
        if (rarityRoll < 60) {
            filteredEvents = availableEvents.stream().filter(e -> e.rarity == Rarity.COMMON).collect(Collectors.toList());
        } else if (rarityRoll < 90) {
            filteredEvents = availableEvents.stream().filter(e -> e.rarity == Rarity.UNCOMMON).collect(Collectors.toList());
        } else {
            filteredEvents = availableEvents.stream().filter(e -> e.rarity == Rarity.RARE).collect(Collectors.toList());
        }

        if (filteredEvents.isEmpty()) {
            return null;
        }

        return filteredEvents.get(random.nextInt(filteredEvents.size()));
    }

    private List<Event> createEventLibrary() {
        return Arrays.asList(
            new Event("Risky Investment", "Your friend offered you a risky investment. Do you want to invest 2000 PLN?", EventType.YES_NO, Rarity.UNCOMMON, 48, () -> {
                if (player.money >= 2000) {
                    player.money -= 2000;
                    boolean investmentSuccess = random.nextBoolean();
                    if (investmentSuccess) {
                        JOptionPane.showMessageDialog(frame, "The investment went well! You gained money.");
                        player.addEffect(new Effect("Investment Gain", 5000, 12, 12, "money", false)); // Apply effect for one year
                    } else {
                        JOptionPane.showMessageDialog(frame, "The investment went bad. You lost money.");
                        player.addEffect(new Effect("Investment Loss", -2000, 12, 12, "money", false)); // Apply effect for one year
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "You don't have enough money to invest.");
                }
            }),
            new Event("Fired", "You have been fired from your job. Your happiness decreases and your income is reduced.", EventType.OK, Rarity.RARE, 36, () -> {
                player.addEffect(new Effect("Job Loss", -2400, 12, 12, "money", false)); // Apply effect for one year
                player.addEffect(new Effect("Job Loss Happiness", -50, 12, 12, "happiness", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You have been fired from your job. Your happiness decreases and your income is reduced.");
            }),
            new Event("Raise", "You got a raise! Your happiness increases for 3 years and your income is increased.", EventType.OK, Rarity.UNCOMMON, 36, () -> {
                player.addEffect(new Effect("Raise Income", 4000, 36, 12, "money", true)); // Apply effect for three years
                player.addEffect(new Effect("Job Happiness", 50, 36, 12, "happiness", true)); // Apply effect for three years
            }),
            new Event("Found Money", "You found 200 PLN on the street!", EventType.OK, Rarity.COMMON, 12, () -> {
                player.addEffect(new Effect("Found Money", 200, 12, 12, "money", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You found 200 PLN on the street!");
            }),
            new Event("Lost Wallet", "You lost your wallet and 200 PLN.", EventType.OK, Rarity.COMMON, 12, () -> {
                player.addEffect(new Effect("Lost Wallet", -200, 12, 12, "money", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You lost your wallet and 200 PLN.");
            }),
            new Event("Health Checkup", "You had a health checkup and your health improved.", EventType.OK, Rarity.COMMON, 12, () -> {
                player.addEffect(new Effect("Health Checkup", 10, 12, 12, "health", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You had a health checkup and your health improved.");
            }),
            new Event("Caught a Cold", "You caught a cold and your health decreased.", EventType.OK, Rarity.COMMON, 12, () -> {
                player.addEffect(new Effect("Caught a Cold", -10, 12, 12, "health", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You caught a cold and your health decreased.");
            }),
            new Event("Birthday Gift", "You received a birthday gift of 400 PLN!", EventType.OK, Rarity.COMMON, 12, () -> {
                player.addEffect(new Effect("Birthday Gift", 400, 12, 12, "money", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You received a birthday gift of 400 PLN!");
            }),
            new Event("Car Repair", "Your car broke down and you had to pay 800 PLN for repairs.", EventType.OK, Rarity.COMMON, 12, () -> {
                player.addEffect(new Effect("Car Repair", -800, 12, 12, "money", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "Your car broke down and you had to pay 800 PLN for repairs.");
            }),
            new Event("Promotion", "You got a promotion at work! Your income increases.", EventType.OK, Rarity.COMMON, 12, () -> {
                player.addEffect(new Effect("Promotion", 2000, 12, 12, "money", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You got a promotion at work! Your income increases.");
            }),
            new Event("Charity Donation", "You donated 200 PLN to charity. Your happiness increases.", EventType.OK, Rarity.COMMON, 12, () -> {
                player.addEffect(new Effect("Charity Donation", -200, 12, 12, "money", false)); // Apply effect for one year
                player.addEffect(new Effect("Charity Happiness", 10, 12, 12, "happiness", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You donated 200 PLN to charity. Your happiness increases.");
            }),
            new Event("Unexpected Bill", "You received an unexpected bill of 400 PLN.", EventType.OK, Rarity.COMMON, 12, () -> {
                player.addEffect(new Effect("Unexpected Bill", -400, 12, 12, "money", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You received an unexpected bill of 400 PLN.");
            }),
            new Event("Exercise Routine", "You started a new exercise routine. Your health improves.", EventType.OK, Rarity.COMMON, 12, () -> {
                player.addEffect(new Effect("Exercise Routine", 10, 12, 12, "health", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You started a new exercise routine. Your health improves.");
            }),
            new Event("Lottery Win", "You won the lottery and gained 4000 PLN!", EventType.OK, Rarity.RARE, 48, () -> {
                player.addEffect(new Effect("Lottery Win", 4000, 12, 12, "money", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You won the lottery and gained 4000 PLN!");
            }),
            new Event("Car Accident", "You were in a car accident and had to pay 2000 PLN for repairs.", EventType.OK, Rarity.UNCOMMON, 24, () -> {
                player.addEffect(new Effect("Car Accident", -2000, 12, 12, "money", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You were in a car accident and had to pay 2000 PLN for repairs.");
            }),
            new Event("New Job Offer", "You received a new job offer with a higher salary. Do you want to accept it?", EventType.YES_NO, Rarity.UNCOMMON, 36, () -> {
                player.addEffect(new Effect("New Job Offer", 2000, 12, 12, "money", false)); // Apply effect for one year
                player.addEffect(new Effect("New Job Happiness", 20, 12, 12, "happiness", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You accepted the new job offer and your salary increased.");
            }),
            new Event("Vacation", "You went on a vacation and your happiness increased.", EventType.OK, Rarity.COMMON, 12, () -> {
                player.addEffect(new Effect("Vacation Happiness", 30, 12, 12, "happiness", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You went on a vacation and your happiness increased.");
            }),
            new Event("Medical Emergency", "You had a medical emergency and had to pay 4000 PLN for treatment.", EventType.OK, Rarity.RARE, 48, () -> {
                player.addEffect(new Effect("Medical Emergency", -4000, 12, 12, "money", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You had a medical emergency and had to pay 4000 PLN for treatment.");
            }),
            new Event("New Friend", "You made a new friend and your happiness increased.", EventType.OK, Rarity.COMMON, 12, () -> {
                player.addEffect(new Effect("New Friend Happiness", 20, 12, 12, "happiness", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You made a new friend and your happiness increased.");
            }),
            new Event("House Repair", "You had to pay 1200 PLN for house repairs.", EventType.OK, Rarity.UNCOMMON, 24, () -> {
                player.addEffect(new Effect("House Repair", -1200, 12, 12, "money", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You had to pay 1200 PLN for house repairs.");
            }),
            new Event("Gym Membership", "You bought a gym membership and your health improved.", EventType.OK, Rarity.COMMON, 12, () -> {
                player.addEffect(new Effect("Gym Membership", 20, 12, 12, "health", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You bought a gym membership and your health improved.");
            }),
            new Event("Pet Adoption", "You adopted a pet and your happiness increased.", EventType.OK, Rarity.COMMON, 12, () -> {
                player.addEffect(new Effect("Pet Adoption Happiness", 30, 24, 12, "happiness", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You adopted a pet and your happiness increased.");
            })
        );
    }

    public static class Event {
        public final String name;
        public final String description;
        public final EventType type;
        public final Rarity rarity;
        public final long cooldown; // Cooldown in ticks
        private final Runnable effect;

        public Event(String name, String description, EventType type, Rarity rarity, long cooldown, Runnable effect) {
            this.name = name;
            this.description = description;
            this.type = type;
            this.rarity = rarity;
            this.cooldown = cooldown;
            this.effect = effect;
        }

        public void applyEffect() {
            effect.run();
        }
    }

    public enum EventType {
        OK,
        YES_NO
    }

    public enum Rarity {
        COMMON,
        UNCOMMON,
        RARE
    }
}
