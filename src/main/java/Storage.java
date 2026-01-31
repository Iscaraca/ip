import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading tasks from disk.
 */
public class Storage {
    private final Path filePath;

    public Storage(String first, String... more) {
        this.filePath = Paths.get(first, more);
    }

    public List<Task> load() throws FantaException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        try {
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            List<Task> tasks = new ArrayList<>();
            for (String line : lines) {
                try {
                    Task task = decode(line);
                    tasks.add(task);
                } catch (FantaException ignored) {
                    // Skip corrupted lines silently to keep startup output unchanged.
                }
            }
            return tasks;
        } catch (IOException e) {
            throw new FantaException("Unable to load tasks: " + e.getMessage());
        }
    }

    public void save(List<Task> tasks) throws FantaException {
        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
            List<String> encoded = new ArrayList<>();
            for (Task task : tasks) {
                encoded.add(encode(task));
            }
            Files.write(filePath, encoded, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new FantaException("Unable to save tasks: " + e.getMessage());
        }
    }

    private String encode(Task task) {
        String status = task.done ? "1" : "0";

        if (task instanceof Todo) {
            return String.join(" | ", "T", status, task.description);
        }
        if (task instanceof Deadline) {
            Deadline d = (Deadline) task;
            return String.join(" | ", "D", status, task.description, d.getByStorageString());
        }
        if (task instanceof Event) {
            Event e = (Event) task;
            return String.join(" | ", "E", status, task.description, e.getFrom(), e.getTo());
        }
        return String.join(" | ", "?", status, task.description);
    }

    private Task decode(String line) throws FantaException {
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3) {
            throw new FantaException("Corrupted task entry: " + line);
        }

        String type = parts[0].trim();
        String status = parts[1].trim();
        String desc = parts[2].trim();

        Task task;
        switch (type) {
        case "T":
            task = new Todo(desc);
            break;
        case "D":
            if (parts.length < 4) {
                throw new FantaException("Corrupted deadline entry: " + line);
            }
            task = new Deadline(desc, parts[3].trim());
            break;
        case "E":
            if (parts.length < 5) {
                throw new FantaException("Corrupted event entry: " + line);
            }
            task = new Event(desc, parts[3].trim(), parts[4].trim());
            break;
        default:
            throw new FantaException("Unknown task type: " + line);
        }

        if (status.equals("1")) {
            task.markDone();
        } else if (!status.equals("0")) {
            throw new FantaException("Invalid status value: " + line);
        }

        return task;
    }
}
