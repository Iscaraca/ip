public class Fanta {
    private static final String DIVIDER = "  ====================================================";
    private static final String DATA_FILE = "data/duke.txt";

    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        Storage storage = new Storage(DATA_FILE);
        java.util.ArrayList<Task> tasks;
        try {
            tasks = new java.util.ArrayList<>(storage.load());
        } catch (FantaException e) {
            tasks = new java.util.ArrayList<>();
            System.out.println(DIVIDER);
            System.out.println("  Unable to load previous tasks. Starting fresh.");
            System.out.println(DIVIDER);
        }

        System.out.println("  Hello! I'm Fanta");
        System.out.println("  What can I do for you?");
        System.out.println(DIVIDER);

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            Command cmd = Command.of(input);

            try {
                if (cmd == Command.BYE) {
                    System.out.println(DIVIDER);
                    System.out.println("  See you soon!");
                    System.out.println(DIVIDER);
                    break;
                }

                if (cmd == Command.LIST) {
                    System.out.println(DIVIDER);
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println("  " + (i + 1) + ". " + tasks.get(i));
                    }
                    System.out.println(DIVIDER);
                    continue;
                }

                if (cmd == Command.MARK) {
                    int idx = toIndex(input.substring(5), tasks.size());
                    requireValidIndex(idx, tasks.size());
                    tasks.get(idx).markDone();
                    save(storage, tasks);
                    System.out.println(DIVIDER);
                    System.out.println("  Nice! I've marked this task as done:");
                    System.out.println("    " + tasks.get(idx));
                    System.out.println(DIVIDER);
                    continue;
                }

                if (cmd == Command.UNMARK) {
                    int idx = toIndex(input.substring(7), tasks.size());
                    requireValidIndex(idx, tasks.size());
                    tasks.get(idx).markUndone();
                    save(storage, tasks);
                    System.out.println(DIVIDER);
                    System.out.println("  OK, I've marked this task as not done yet:");
                    System.out.println("    " + tasks.get(idx));
                    System.out.println(DIVIDER);
                    continue;
                }

                if (cmd == Command.TODO) {
                    String desc = input.length() > 4 ? input.substring(4).trim() : "";
                    if (desc.isEmpty()) {
                        throw new FantaException("Todo needs a description.");
                    }
                    tasks.add(new Todo(desc));
                    save(storage, tasks);
                    printAdded(tasks);
                    continue;
                }

                if (cmd == Command.DEADLINE) {
                    String body = input.length() > 8 ? input.substring(8).trim() : "";
                    String[] parts = body.split(" /by ", 2);
                    if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                        throw new FantaException("Deadline needs a description and a /by time.");
                    }
                    tasks.add(new Deadline(parts[0].trim(), parts[1].trim()));
                    save(storage, tasks);
                    printAdded(tasks);
                    continue;
                }

                if (cmd == Command.EVENT) {
                    String body = input.length() > 5 ? input.substring(5).trim() : "";
                    String[] parts = body.split(" /from ", 2);
                    if (parts.length < 2 || parts[0].trim().isEmpty()) {
                        throw new FantaException("Event needs a description and /from ... /to ... timings.");
                    }
                    String[] times = parts[1].split(" /to ", 2);
                    if (times.length < 2 || times[0].trim().isEmpty() || times[1].trim().isEmpty()) {
                        throw new FantaException("Event timings must include both /from and /to values.");
                    }
                    tasks.add(new Event(parts[0].trim(), times[0].trim(), times[1].trim()));
                    save(storage, tasks);
                    printAdded(tasks);
                    continue;
                }

                if (cmd == Command.DELETE) {
                    int idx = toIndex(input.substring(7), tasks.size());
                    requireValidIndex(idx, tasks.size());
                    Task removed = tasks.remove(idx);
                    save(storage, tasks);
                    System.out.println(DIVIDER);
                    System.out.println("  Noted. I've removed this task:");
                    System.out.println("    " + removed);
                    System.out.println("  Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println(DIVIDER);
                    continue;
                }

                if (cmd == Command.EMPTY) {
                    continue;
                }

                throw new FantaException("Sorry, I don't recognize that command.");
            } catch (FantaException e) {
                System.out.println(DIVIDER);
                System.out.println("  " + e.getMessage());
                System.out.println(DIVIDER);
            }
        }

        scanner.close();
    }

    private static int toIndex(String value, int limit) {
        try {
            int idx = Integer.parseInt(value.trim()) - 1;
            return idx >= 0 && idx < limit ? idx : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void requireValidIndex(int idx, int limit) throws FantaException {
        if (idx < 0 || idx >= limit) {
            throw new FantaException("Please give a valid task number.");
        }
    }

    private static void printAdded(java.util.ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            return;
        }
        System.out.println(DIVIDER);
        System.out.println("  added: " + tasks.get(tasks.size() - 1));
        System.out.println("  Now you have " + tasks.size() + " tasks in the list.");
        System.out.println(DIVIDER);
    }

    private static void save(Storage storage, java.util.List<Task> tasks) throws FantaException {
        storage.save(tasks);
    }
}
