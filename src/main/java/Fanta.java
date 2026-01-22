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

            if ("bye".equals(input)) {
                System.out.println(DIVIDER);
                System.out.println("  See you soon!");
                System.out.println(DIVIDER);
                break;
            }

            if ("list".equals(input)) {
                System.out.println(DIVIDER);
                for (int i = 0; i < numTasks; i++) {
                    System.out.println("  " + (i + 1) + ". " + tasks[i]);
                }
                System.out.println(DIVIDER);
                continue;
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

            if (numTasks < tasks.length) {
                tasks[numTasks] = new Task(input);
                numTasks++;
            }
            System.out.println(DIVIDER);
            System.out.println("  added: " + input);
            System.out.println(DIVIDER);
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
}
