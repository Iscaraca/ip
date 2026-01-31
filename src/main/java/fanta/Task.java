package fanta;

public class Task {
    protected final String description;
    protected boolean done;

    public Task(String description) {
        this.description = description;
    }

    public void markDone() {
        done = true;
    }

    public void markUndone() {
        done = false;
    }

    protected String status() {
        return "[" + (done ? "X" : " ") + "] " + description;
    }

    @Override
    public String toString() {
        return status();
    }
}
