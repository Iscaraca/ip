public class Task {
    private final String description;
    private boolean done;

    public Task(String description) {
        this.description = description;
    }

    public void markDone() {
        done = true;
    }

    public void markUndone() {
        done = false;
    }

    @Override
    public String toString() {
        return "[" + (done ? "X" : " ") + "] " + description;
    }
}
