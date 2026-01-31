package fanta;

public enum Command {
    BYE, LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, FIND, EMPTY, UNKNOWN;

    public static Command of(String input) {
        if (input == null || input.isEmpty()) {
            return EMPTY;
        }
        String trimmed = input.trim();
        if (trimmed.equals("bye")) return BYE;
        if (trimmed.equals("list")) return LIST;
        if (trimmed.startsWith("mark ")) return MARK;
        if (trimmed.startsWith("unmark ")) return UNMARK;
        if (trimmed.startsWith("todo")) return TODO;
        if (trimmed.startsWith("deadline")) return DEADLINE;
        if (trimmed.startsWith("event")) return EVENT;
        if (trimmed.startsWith("delete ")) return DELETE;
        if (trimmed.startsWith("find ")) return FIND;
        return UNKNOWN;
    }
}
