package fanta;

/**
 * Represents a task that recurs on a given schedule.
 */
public class Recurring extends Task {
    private final String cadence;

    public Recurring(String description, String cadence) {
        super(description);
        this.cadence = cadence;
    }

    public String getCadence() {
        return cadence;
    }

    @Override
    public String toString() {
        return "[R]" + status() + " (every: " + cadence + ")";
    }
}
