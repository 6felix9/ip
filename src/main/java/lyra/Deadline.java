package lyra;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Deadline class for Lyra.
 */
public class Deadline extends Task {
    private static final String DONE_MARKER = "1";
    private static final String NOT_DONE_MARKER = "0";

    private LocalDateTime by;

    /**
     * Constructor for Deadline.
     */
    public Deadline(String description, LocalDateTime by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    /**
     * Get the by date and time.
     */
    public LocalDateTime getBy() {
        return by;
    }

    /**
     * Convert the Deadline object to a string for file storage.
     */
    @Override
    public String toFileString() {
        return "D | " + (getIsDone() ? DONE_MARKER : NOT_DONE_MARKER) + " | " + getDescription() + " | " +
            by.format(DateTimeFormatter.ofPattern("d/MM/yyyy HHmm"));
    }

    /**
     * Convert the Deadline object to a string for display.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + 
            by.format(DateTimeFormatter.ofPattern("MMM d yyyy, h:mma")) + ")";
    }
}
