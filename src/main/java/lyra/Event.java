package lyra;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Event class for Lyra.
 */
public class Event extends Task {
    private static final String DONE_MARKER = "1";
    private static final String NOT_DONE_MARKER = "0";

    private LocalDateTime from;
    private LocalDateTime to;

    /**
     * Constructor for Event.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    /**
     * Get the from date and time.
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Get the to date and time.
     */
    public LocalDateTime getTo() {
        return to;
    }

    /**
     * Convert the Event object to a string for file storage.
     */
    @Override
    public String toFileString() {
        return "E | " + (getIsDone() ? DONE_MARKER : NOT_DONE_MARKER) + " | " + getDescription() + " | " +
                from.format(DateTimeFormatter.ofPattern("d/MM/yyyy HHmm")) + " | " +
                to.format(DateTimeFormatter.ofPattern("d/MM/yyyy HHmm"));
    }

    /**
     * Convert the Event object to a string for display.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + 
               from.format(DateTimeFormatter.ofPattern("MMM d yyyy, h:mma")) + " to: " +
               to.format(DateTimeFormatter.ofPattern("MMM d yyyy, h:mma")) + ")";
    }
}
