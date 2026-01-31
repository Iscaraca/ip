package fanta;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class Deadline extends Task {
    private static final DateTimeFormatter OUTPUT_DATE = DateTimeFormatter.ofPattern("MMM dd yyyy");
    private static final DateTimeFormatter OUTPUT_DATETIME = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");
    private static final List<DateTimeFormatter> DATE_TIME_PATTERNS = Arrays.asList(
            DateTimeFormatter.ISO_LOCAL_DATE_TIME,
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"),
            DateTimeFormatter.ofPattern("d/M/yyyy HHmm"),
            DateTimeFormatter.ofPattern("d-M-yyyy HHmm")
    );
    private static final List<DateTimeFormatter> DATE_ONLY_PATTERNS = Arrays.asList(
            DateTimeFormatter.ISO_LOCAL_DATE,
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("d-M-yyyy")
    );

    private final LocalDateTime by;
    private final boolean hasTime;

    public Deadline(String description, String byRaw) throws FantaException {
        super(description);
        ParseResult parsed = parse(byRaw);
        this.by = parsed.dateTime;
        this.hasTime = parsed.hasTime;
    }

    public String getByStorageString() {
        return by.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public String getByDisplay() {
        return hasTime ? by.format(OUTPUT_DATETIME) : by.format(OUTPUT_DATE);
    }

    @Override
    public String toString() {
        return "[D]" + status() + " (by: " + getByDisplay() + ")";
    }

    private static ParseResult parse(String input) throws FantaException {
        String trimmed = input.trim();
        for (DateTimeFormatter fmt : DATE_TIME_PATTERNS) {
            try {
                LocalDateTime dt = LocalDateTime.parse(trimmed, fmt);
                return new ParseResult(dt, true);
            } catch (DateTimeParseException ignored) {
            }
        }
        for (DateTimeFormatter fmt : DATE_ONLY_PATTERNS) {
            try {
                LocalDate d = LocalDate.parse(trimmed, fmt);
                return new ParseResult(d.atStartOfDay(), false);
            } catch (DateTimeParseException ignored) {
            }
        }
        throw new FantaException("Unable to understand date/time: " + input);
    }

    private static class ParseResult {
        private final LocalDateTime dateTime;
        private final boolean hasTime;

        private ParseResult(LocalDateTime dateTime, boolean hasTime) {
            this.dateTime = dateTime;
            this.hasTime = hasTime;
        }
    }
}
