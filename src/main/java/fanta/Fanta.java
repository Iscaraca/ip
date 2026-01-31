package fanta;

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
                default:
                    throw new FantaException("Sorry, I don't recognize that command.");
                }
            } catch (FantaException e) {
                ui.showError(e.getMessage());
            }
        }

        ui.close();
    }

    private void save(TaskList tasks) throws FantaException {
        storage.save(tasks.all());
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
}
