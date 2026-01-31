package fanta;

/**
 * Parses user input into commands and indexes.
 */
public class Parser {
    public static Command parse(String input) {
        return Command.of(input);
    }

    public static int parseIndex(String value, int limit) {
        try {
            int idx = Integer.parseInt(value.trim()) - 1;
            return idx >= 0 && idx < limit ? idx : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
