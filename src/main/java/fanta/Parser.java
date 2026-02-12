package fanta;

/**
 * Parses user input into commands and indexes.
 */
public class Parser {
    /**
     * Converts raw input into a {@link Command}.
     *
     * @param input user input
     * @return parsed command
     */
    public static Command parse(String input) {
        return Command.of(input);
    }

    /**
     * Parses 1-based index strings into 0-based indexes with bounds check.
     *
     * @param value index string
     * @param limit size of the list
     * @return zero-based index, or -1 when invalid
     */
    public static int parseIndex(String value, int limit) {
        assert limit >= 0 : "List size should not be negative";
        try {
            int idx = Integer.parseInt(value.trim()) - 1;
            return idx >= 0 && idx < limit ? idx : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
