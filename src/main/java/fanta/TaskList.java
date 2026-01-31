package fanta;

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

    /** @return number of tasks. */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns task by index.
     *
     * @param index zero-based index
     * @return task
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /** @return all tasks as a mutable list reference. */
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
