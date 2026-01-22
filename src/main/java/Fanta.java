public class Fanta {
    private static final String DIVIDER = "  ====================================================";

    public static void main(String[] args) {        
        System.out.println("  Hello! I'm Fanta");
        System.out.println("  What can I do for you?");
        System.out.println(DIVIDER);

        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String[] tasks = new String[100];
        int numTasks = 0;

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

            tasks[numTasks] = input;
            numTasks++;
            System.out.println(DIVIDER);
            System.out.println("  added: " + input);
            System.out.println(DIVIDER);
        }

        scanner.close();
    }
}
