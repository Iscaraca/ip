package fanta;

/**
 * Represents a simple task without time constraints.
 */
public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + status();
    }
}
