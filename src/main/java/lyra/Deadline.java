package lyra;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a deadline task with a due date.
 */
public class Deadline extends Task {
    private static final String DONE_MARKER = "1";
    private static final String NOT_DONE_MARKER = "0";

    private LocalDateTime by;

    /**
     * Creates a Deadline task with the specified description and due date.
     *
     * @param description The task description
     * @param by The due date
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
     * Set the by date and time.
     *
     * @param by The new due date
     */
    public void setBy(LocalDateTime by) {
        this.by = by;
    }

    /**
     * Convert the Deadline object to a string for file storage.
     */
    @Override
    public String toFileString() {
        return "D | " + (getIsDone() ? DONE_MARKER : NOT_DONE_MARKER) + " | " + getDescription() + " | "
                + by.format(DateTimeFormatter.ofPattern("d/MM/yyyy HHmm"));
    }

    /**
     * Convert the Deadline object to a string for display.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: "
                + by.format(DateTimeFormatter.ofPattern("MMM d yyyy, h:mma")) + ")";
    }
}
