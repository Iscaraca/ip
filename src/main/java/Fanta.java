public class Fanta {
    private static final String DIVIDER = "  ====================================================";

    public static void main(String[] args) {        
        System.out.println("  Hello! I'm Fanta");
        System.out.println("  What can I do for you?");
        System.out.println(DIVIDER);

        java.util.Scanner scanner = new java.util.Scanner(System.in);

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if ("bye".equals(input)) {
                System.out.println(DIVIDER);
                System.out.println("  See you soon!");
                System.out.println(DIVIDER);
                break;
            }

            System.out.println(DIVIDER);
            System.out.println("  " + input);
        }

        scanner.close();
    }
}
