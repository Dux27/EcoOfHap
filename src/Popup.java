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

    /**
     * Constructor for Popup class.
     * @param player The player instance.
     */
    public Popup(Player player) {
        this.player = player; // Use the player instance from MainLoop
        this.events = createEventLibrary();
        frame = new JFrame("Random Event");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }

    /**
     * Start the popup logic.
     */
    public void start() {
        scheduleNextPopup();
    }

    /**
     * Schedule the next popup event.
     */
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

    /**
     * Show a random event popup.
     */
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

    /**
     * Select a random event from the available events.
     * @return The selected event.
     */
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

    /**
     * Create a library of events.
     * @return The list of events.
     */
    private List<Event> createEventLibrary() {
        return Arrays.asList(
            new Event("Stock Market Crash", "The stock market crashed and you lost 2000 PLN.", EventType.OK, Rarity.RARE, 48, () -> {
                player.addEffect(new Effect("Stock Market Crash", -2000, 12, 12, "money", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "The stock market crashed and you lost 2000 PLN.");
            }),
            new Event("Salary Increase", "You received a salary increase of 1000 PLN per year.", EventType.OK, Rarity.UNCOMMON, 36, () -> {
                player.addEffect(new Effect("Salary Increase", 1000, 36, 12, "money", true)); // Apply effect for three years
                JOptionPane.showMessageDialog(frame, "You received a salary increase of 1000 PLN per year.");
            }),
            new Event("Unexpected Medical Bill", "You had to pay an unexpected medical bill of 500 PLN.", EventType.OK, Rarity.COMMON, 12, () -> {
                player.addEffect(new Effect("Medical Bill", -500, 12, 12, "money", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You had to pay an unexpected medical bill of 500 PLN.");
            }),
            new Event("Bonus", "You received a bonus of 1500 PLN.", EventType.OK, Rarity.UNCOMMON, 24, () -> {
                player.addEffect(new Effect("Bonus", 1500, 12, 12, "money", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You received a bonus of 1500 PLN.");
            }),
            new Event("Car Repair", "Your car needed repairs costing 800 PLN.", EventType.OK, Rarity.COMMON, 12, () -> {
                player.addEffect(new Effect("Car Repair", -800, 12, 12, "money", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "Your car needed repairs costing 800 PLN.");
            }),
            new Event("Investment Opportunity", "You have an opportunity to invest 3000 PLN. Do you want to invest?", EventType.YES_NO, Rarity.UNCOMMON, 36, () -> {
                if (player.money >= 3000) {
                    player.money -= 3000;
                    boolean investmentSuccess = random.nextBoolean();
                    if (investmentSuccess) {
                        JOptionPane.showMessageDialog(frame, "The investment was successful! You gained 6000 PLN.");
                        player.addEffect(new Effect("Investment Gain", 6000, 12, 12, "money", false)); // Apply effect for one year
                    } else {
                        JOptionPane.showMessageDialog(frame, "The investment failed. You lost 3000 PLN.");
                        player.addEffect(new Effect("Investment Loss", -3000, 12, 12, "money", false)); // Apply effect for one year
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "You don't have enough money to invest.");
                }
            }),
            new Event("Tax Refund", "You received a tax refund of 1000 PLN.", EventType.OK, Rarity.COMMON, 12, () -> {
                player.addEffect(new Effect("Tax Refund", 1000, 12, 12, "money", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You received a tax refund of 1000 PLN.");
            }),
            new Event("Home Renovation", "You decided to renovate your home, costing 5000 PLN.", EventType.OK, Rarity.RARE, 48, () -> {
                player.addEffect(new Effect("Home Renovation", -5000, 12, 12, "money", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You decided to renovate your home, costing 5000 PLN.");
            }),
            new Event("Job Promotion", "You got a promotion at work! Your income increases by 2000 PLN per year.", EventType.OK, Rarity.UNCOMMON, 36, () -> {
                player.addEffect(new Effect("Job Promotion", 2000, 36, 12, "money", true)); // Apply effect for three years
                JOptionPane.showMessageDialog(frame, "You got a promotion at work! Your income increases by 2000 PLN per year.");
            }),
            new Event("Loan Payment", "You had to make a loan payment of 1000 PLN.", EventType.OK, Rarity.COMMON, 12, () -> {
                player.addEffect(new Effect("Loan Payment", -1000, 12, 12, "money", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You had to make a loan payment of 1000 PLN.");
            }),
            new Event("Unexpected Expense", "You had an unexpected expense of 700 PLN.", EventType.OK, Rarity.COMMON, 12, () -> {
                player.addEffect(new Effect("Unexpected Expense", -700, 12, 12, "money", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You had an unexpected expense of 700 PLN.");
            }),
            new Event("Business Opportunity", "You have a business opportunity to invest 4000 PLN. Do you want to invest?", EventType.YES_NO, Rarity.RARE, 48, () -> {
                if (player.money >= 4000) {
                    player.money -= 4000;
                    boolean investmentSuccess = random.nextBoolean();
                    if (investmentSuccess) {
                        JOptionPane.showMessageDialog(frame, "The business was successful! You gained 8000 PLN.");
                        player.addEffect(new Effect("Business Gain", 8000, 12, 12, "money", false)); // Apply effect for one year
                    } else {
                        JOptionPane.showMessageDialog(frame, "The business failed. You lost 4000 PLN.");
                        player.addEffect(new Effect("Business Loss", -4000, 12, 12, "money", false)); // Apply effect for one year
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "You don't have enough money to invest.");
                }
            }),
            new Event("Family Gathering", "You attended a family gathering and your happiness increased.", EventType.OK, Rarity.COMMON, 12, () -> {
                player.addEffect(new Effect("Family Gathering", 20, 12, 12, "happiness", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You attended a family gathering and your happiness increased.");
            }),
            new Event("Exercise Routine", "You started a new exercise routine and your happiness increased.", EventType.OK, Rarity.COMMON, 12, () -> {
                player.addEffect(new Effect("Exercise Routine", 15, 12, 12, "happiness", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You started a new exercise routine and your happiness increased.");
            }),
            new Event("Volunteer Work", "You did some volunteer work and your happiness increased.", EventType.OK, Rarity.COMMON, 12, () -> {
                player.addEffect(new Effect("Volunteer Work", 25, 12, 12, "happiness", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You did some volunteer work and your happiness increased.");
            }),
            new Event("New Hobby", "You started a new hobby and your happiness increased.", EventType.OK, Rarity.COMMON, 12, () -> {
                player.addEffect(new Effect("New Hobby", 30, 12, 12, "happiness", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You started a new hobby and your happiness increased.");
            }),
            new Event("Social Event", "You attended a social event and your happiness increased.", EventType.OK, Rarity.COMMON, 12, () -> {
                player.addEffect(new Effect("Social Event", 20, 12, 12, "happiness", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You attended a social event and your happiness increased.");
            }),
            new Event("Vacation", "You went on a vacation and your happiness increased.", EventType.OK, Rarity.COMMON, 12, () -> {
                player.addEffect(new Effect("Vacation", 40, 12, 12, "happiness", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You went on a vacation and your happiness increased.");
            }),
            new Event("Charity Donation", "You donated 500 PLN to charity. Your happiness increased.", EventType.OK, Rarity.COMMON, 12, () -> {
                player.addEffect(new Effect("Charity Donation", -500, 12, 12, "money", false)); // Apply effect for one year
                player.addEffect(new Effect("Charity Happiness", 30, 12, 12, "happiness", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You donated 500 PLN to charity. Your happiness increased.");
            }),
            new Event("New Friend", "You made a new friend and your happiness increased.", EventType.OK, Rarity.COMMON, 12, () -> {
                player.addEffect(new Effect("New Friend", 25, 12, 12, "happiness", false)); // Apply effect for one year
                JOptionPane.showMessageDialog(frame, "You made a new friend and your happiness increased.");
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

        /**
         * Constructor for Event class.
         * @param name The name of the event.
         * @param description The description of the event.
         * @param type The type of the event.
         * @param rarity The rarity of the event.
         * @param cooldown The cooldown period for the event.
         * @param effect The effect of the event.
         */
        public Event(String name, String description, EventType type, Rarity rarity, long cooldown, Runnable effect) {
            this.name = name;
            this.description = description;
            this.type = type;
            this.rarity = rarity;
            this.cooldown = cooldown;
            this.effect = effect;
        }

        /**
         * Apply the effect of the event.
         */
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
