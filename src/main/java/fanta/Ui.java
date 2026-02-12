package fanta;

import java.util.Scanner;

/**
 * Handles user interaction.
 */
public class Ui {
    private static final String DIVIDER = "  ====================================================";
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /** Greets the user. */
    public void showWelcome() {
        System.out.println("  Hello! I'm Fanta");
        System.out.println("  What can I do for you?");
        showDivider();
    }

    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    /** Reads the next user input line. */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /** Prints a divider line. */
    public void showDivider() {
        System.out.println(DIVIDER);
    }

    /**
     * Prints a formatted error message.
     *
     * @param message details of the error
     */
    public void showError(String message) {
        showDivider();
        System.out.println("  " + message);
        showDivider();
    }

    public void showAdded(Task task, int count) {
        showDivider();
        System.out.println("  added: " + task);
        System.out.println("  Now you have " + count + " tasks in the list.");
        showDivider();
    }

    /**
     * Displays all tasks in the list.
     *
     * @param tasks the task list
     */
    public void showList(TaskList tasks) {
        showDivider();
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + tasks.get(i));
        }
        showDivider();
    }

    public void showMark(Task task) {
        showDivider();
        System.out.println("  Nice! I've marked this task as done:");
        System.out.println("    " + task);
        showDivider();
    }

    public void showUnmark(Task task) {
        showDivider();
        System.out.println("  OK, I've marked this task as not done yet:");
        System.out.println("    " + task);
        showDivider();
    }

    public void showDelete(Task task, int count) {
        showDivider();
        System.out.println("  Noted. I've removed this task:");
        System.out.println("    " + task);
        System.out.println("  Now you have " + count + " tasks in the list.");
        showDivider();
    }

    /**
     * Displays tasks that match a find query.
     *
     * @param matches matching tasks
     */
    public void showFind(Task... matches) {
        showDivider();
        System.out.println("  Here are the matching tasks in your list:");
        for (int i = 0; i < matches.length; i++) {
            System.out.println("  " + (i + 1) + ". " + matches[i]);
        }
        showDivider();
    }

    public void showBye() {
        showDivider();
        System.out.println("  See you soon!");
        showDivider();
    }

    /** Shows a generic load failure notice. */
    public void showLoadingError() {
        showDivider();
        System.out.println("  Unable to load previous tasks. Starting fresh.");
        showDivider();
    }

    public void close() {
        scanner.close();
    }
}
