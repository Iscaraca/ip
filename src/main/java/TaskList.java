import java.util.ArrayList;
import java.util.List;

/**
 * Wraps the task collection.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> existing) {
        this.tasks = new ArrayList<>(existing);
    }

    public int size() {
        return tasks.size();
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public List<Task> all() {
        return tasks;
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task remove(int index) throws FantaException {
        requireValidIndex(index);
        return tasks.remove(index);
    }

    public Task mark(int index) throws FantaException {
        requireValidIndex(index);
        Task t = tasks.get(index);
        t.markDone();
        return t;
    }

    public Task unmark(int index) throws FantaException {
        requireValidIndex(index);
        Task t = tasks.get(index);
        t.markUndone();
        return t;
    }

    private void requireValidIndex(int idx) throws FantaException {
        if (idx < 0 || idx >= tasks.size()) {
            throw new FantaException("Please give a valid task number.");
        }
    }
}
