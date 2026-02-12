package fanta;

/**
 * Main entry point for the Fanta task manager application.
 */
public class Fanta {
    private static final String DATA_FILE = "data/fanta.txt";

    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    public Fanta(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        TaskList loaded;
        try {
            loaded = new TaskList(storage.load());
        } catch (FantaException e) {
            ui.showLoadingError();
            loaded = new TaskList();
        }
        tasks = loaded;
    }

    public static void main(String[] args) {
        new Fanta(DATA_FILE).run();
    }

    /**
     * Produces a textual response for GUI usage.
     *
     * @param input user command
     * @return response text
     */
    public String respond(String input) {
        StringBuilder out = new StringBuilder();
        Command cmd = Parser.parse(input);
        try {
            switch (cmd) {
                case BYE:
                    ui.showBye();
                    return "See you soon!";
                case EMPTY:
                    return "";
                case LIST:
                    out.append(tasks.asString());
                    break;
                case MARK:
                    out.append(handleMarkGui(input));
                    break;
                case UNMARK:
                    out.append(handleUnmarkGui(input));
                    break;
                case TODO:
                    out.append(handleTodoGui(input));
                    break;
                case DEADLINE:
                    out.append(handleDeadlineGui(input));
                    break;
                case EVENT:
                    out.append(handleEventGui(input));
                    break;
                case DELETE:
                    out.append(handleDeleteGui(input));
                    break;
                case FIND:
                    out.append(handleFindGui(input));
                    break;
                default:
                    throw new FantaException("Sorry, I don't recognize that command.");
            }
        } catch (FantaException e) {
            return e.getMessage();
        }
        return out.toString();
    }

    /**
     * Starts the command loop, delegating input/output to {@link Ui} and storage to {@link Storage}.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit && ui.hasNextLine()) {
            String input = ui.readCommand();
            Command cmd = Parser.parse(input);

            try {
                switch (cmd) {
                    case BYE:
                        ui.showBye();
                        isExit = true;
                        break;
                    case EMPTY:
                        break;
                    case LIST:
                        ui.showList(tasks);
                        break;
                    case MARK:
                        handleMark(input);
                        break;
                    case UNMARK:
                        handleUnmark(input);
                        break;
                    case TODO:
                        handleTodo(input);
                        break;
                    case DEADLINE:
                        handleDeadline(input);
                        break;
                    case EVENT:
                        handleEvent(input);
                        break;
                    case DELETE:
                        handleDelete(input);
                        break;
                    case FIND:
                        handleFind(input);
                        break;
                    default:
                        throw new FantaException("Sorry, I don't recognize that command.");
                }
            } catch (FantaException e) {
                ui.showError(e.getMessage());
            }
        }

        ui.close();
    }

    /**
     * Persists the given tasks to disk.
     *
     * @param tasks current task list
     * @throws FantaException if saving fails
     */
    private void save(TaskList tasks) throws FantaException {
        storage.save(tasks.all());
    }

    private void handleFind(String input) {
        String keyword = input.length() > 4 ? input.substring(4).trim() : "";
        java.util.List<Task> matches = find(keyword, tasks);
        ui.showFind(matches.toArray(new Task[0]));
    }

    private String handleMarkGui(String input) throws FantaException {
        int idx = Parser.parseIndex(input.substring(5), tasks.size());
        Task marked = tasks.mark(idx);
        save(tasks);
        return "Nice! I've marked this task as done:\n" + marked;
    }

    private String handleUnmarkGui(String input) throws FantaException {
        int idx = Parser.parseIndex(input.substring(7), tasks.size());
        Task unmarked = tasks.unmark(idx);
        save(tasks);
        return "OK, I've marked this task as not done yet:\n" + unmarked;
    }

    private String handleTodoGui(String input) throws FantaException {
        String desc = input.length() > 4 ? input.substring(4).trim() : "";
        if (desc.isEmpty()) {
            throw new FantaException("Todo needs a description.");
        }
        Task todo = new Todo(desc);
        tasks.add(todo);
        save(tasks);
        return "added: " + todo + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String handleDeadlineGui(String input) throws FantaException {
        String body = input.length() > 8 ? input.substring(8).trim() : "";
        String[] parts = body.split(" /by ", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new FantaException("Deadline needs a description and a /by time.");
        }
        Task deadline = new Deadline(parts[0].trim(), parts[1].trim());
        tasks.add(deadline);
        save(tasks);
        return "added: " + deadline + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String handleEventGui(String input) throws FantaException {
        String body = input.length() > 5 ? input.substring(5).trim() : "";
        String[] parts = body.split(" /from ", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty()) {
            throw new FantaException("Event needs a description and /from ... /to ... timings.");
        }
        String[] times = parts[1].split(" /to ", 2);
        if (times.length < 2 || times[0].trim().isEmpty() || times[1].trim().isEmpty()) {
            throw new FantaException("Event timings must include both /from and /to values.");
        }
        Task event = new Event(parts[0].trim(), times[0].trim(), times[1].trim());
        tasks.add(event);
        save(tasks);
        return "added: " + event + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String handleDeleteGui(String input) throws FantaException {
        int idx = Parser.parseIndex(input.substring(7), tasks.size());
        Task removed = tasks.remove(idx);
        save(tasks);
        return "Noted. I've removed this task:\n" + removed + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String handleFindGui(String input) {
        String keyword = input.length() > 4 ? input.substring(4).trim() : "";
        java.util.List<Task> matches = find(keyword, tasks);
        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:");
        for (int i = 0; i < matches.size(); i++) {
            sb.append(System.lineSeparator()).append(i + 1).append(". ").append(matches.get(i));
        }
        return sb.toString();
    }

    private void handleMark(String input) throws FantaException {
        int idx = Parser.parseIndex(input.substring(5), tasks.size());
        Task marked = tasks.mark(idx);
        save(tasks);
        ui.showMark(marked);
    }

    private void handleUnmark(String input) throws FantaException {
        int idx = Parser.parseIndex(input.substring(7), tasks.size());
        Task unmarked = tasks.unmark(idx);
        save(tasks);
        ui.showUnmark(unmarked);
    }

    private void handleTodo(String input) throws FantaException {
        String desc = input.length() > 4 ? input.substring(4).trim() : "";
        if (desc.isEmpty()) {
            throw new FantaException("Todo needs a description.");
        }
        Task todo = new Todo(desc);
        tasks.add(todo);
        save(tasks);
        ui.showAdded(todo, tasks.size());
    }

    private void handleDeadline(String input) throws FantaException {
        String body = input.length() > 8 ? input.substring(8).trim() : "";
        String[] parts = body.split(" /by ", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new FantaException("Deadline needs a description and a /by time.");
        }
        Task deadline = new Deadline(parts[0].trim(), parts[1].trim());
        tasks.add(deadline);
        save(tasks);
        ui.showAdded(deadline, tasks.size());
    }

    private void handleEvent(String input) throws FantaException {
        String body = input.length() > 5 ? input.substring(5).trim() : "";
        String[] parts = body.split(" /from ", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty()) {
            throw new FantaException("Event needs a description and /from ... /to ... timings.");
        }
        String[] times = parts[1].split(" /to ", 2);
        if (times.length < 2 || times[0].trim().isEmpty() || times[1].trim().isEmpty()) {
            throw new FantaException("Event timings must include both /from and /to values.");
        }
        Task event = new Event(parts[0].trim(), times[0].trim(), times[1].trim());
        tasks.add(event);
        save(tasks);
        ui.showAdded(event, tasks.size());
    }

    private void handleDelete(String input) throws FantaException {
        int idx = Parser.parseIndex(input.substring(7), tasks.size());
        Task removed = tasks.remove(idx);
        save(tasks);
        ui.showDelete(removed, tasks.size());
    }

    private java.util.List<Task> find(String keyword, TaskList tasks) {
        java.util.ArrayList<Task> result = new java.util.ArrayList<>();
        if (keyword.isEmpty()) {
            return result;
        }
        for (Task task : tasks.all()) {
            if (task.description.toLowerCase().contains(keyword.toLowerCase())) {
                result.add(task);
            }
        }
        return result;
    }
}
