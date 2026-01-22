public class Fanta {
    private static final String DIVIDER = "  ====================================================";

    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        Task[] tasks = new Task[100];
        int numTasks = 0;

        System.out.println("  Hello! I'm Fanta");
        System.out.println("  What can I do for you?");
        System.out.println(DIVIDER);

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();

            if ("list".equals(input)) {
                System.out.println(DIVIDER);
                for (int i = 0; i < numTasks; i++) {
                    System.out.println("  " + (i + 1) + ". " + tasks[i]);
                }
                System.out.println(DIVIDER);
                continue;
            }

            if ("bye".equals(input)) {
                System.out.println(DIVIDER);
                System.out.println("  See you soon!");
                System.out.println(DIVIDER);
                break;
            }

            if (input.startsWith("mark ")) {
                int idx = toIndex(input.substring(5), numTasks);
                if (idx >= 0) {
                    tasks[idx].markDone();
                    System.out.println(DIVIDER);
                    System.out.println("  Nice! I've marked this task as done:");
                    System.out.println("    " + tasks[idx]);
                    System.out.println(DIVIDER);
                }
                continue;
            }

            if (input.startsWith("unmark ")) {
                int idx = toIndex(input.substring(7), numTasks);
                if (idx >= 0) {
                    tasks[idx].markUndone();
                    System.out.println(DIVIDER);
                    System.out.println("  OK, I've marked this task as not done yet:");
                    System.out.println("    " + tasks[idx]);
                    System.out.println(DIVIDER);
                }
                continue;
            }

            if (input.startsWith("todo ")) {
                int before = numTasks;
                if (numTasks < tasks.length) {
                    tasks[numTasks] = new Todo(input.substring(5).trim());
                    numTasks++;
                }
                if (numTasks > before) {
                    printAdded(tasks, numTasks);
                }
                continue;
            }

            if (input.startsWith("deadline ")) {
                int before = numTasks;
                String[] parts = input.substring(9).split(" /by ", 2);
                if (parts.length == 2 && numTasks < tasks.length) {
                    tasks[numTasks] = new Deadline(parts[0].trim(), parts[1].trim());
                    numTasks++;
                }
                if (numTasks > before) {
                    printAdded(tasks, numTasks);
                }
                continue;
            }

            if (input.startsWith("event ")) {
                int before = numTasks;
                String[] parts = input.substring(6).split(" /from ", 2);
                if (parts.length == 2) {
                    String[] times = parts[1].split(" /to ", 2);
                    if (times.length == 2 && numTasks < tasks.length) {
                        tasks[numTasks] = new Event(parts[0].trim(), times[0].trim(), times[1].trim());
                        numTasks++;
                    }
                }
                if (numTasks > before) {
                    printAdded(tasks, numTasks);
                }
                continue;
            }

            int before = numTasks;
            if (numTasks < tasks.length) {
                tasks[numTasks] = new Task(input);
                numTasks++;
            }
            if (numTasks > before) {
                printAdded(tasks, numTasks);
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

    private static void printAdded(Task[] tasks, int count) {
        if (count == 0) {
            return;
        }
        System.out.println(DIVIDER);
        System.out.println("  added: " + tasks[count - 1]);
        System.out.println("  Now you have " + count + " tasks in the list.");
        System.out.println(DIVIDER);
    }
}
