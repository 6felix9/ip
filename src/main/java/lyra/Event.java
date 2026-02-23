package lyra;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Represents an event task with a start and end time.
 */
public class Event extends Task {
    private static final String START_AFTER_END_MSG = "Start time must be before end time.";

    private static final String DONE_MARKER = "1";
    private static final String NOT_DONE_MARKER = "0";

    private LocalDateTime from;
    private LocalDateTime to;

    /**
     * Creates an Event task with the specified description and time period.
     *
     * @param description The task description
     * @param from The start time
     * @param to The end time
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) throws LyraException {
        super(description, TaskType.EVENT);
        if (!from.isBefore(to)) {
            throw new LyraException(START_AFTER_END_MSG);
        }
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
     * Set the from date and time.
     *
     * @param from The new start time
     */
    public void setFrom(LocalDateTime from) throws LyraException {
        if (!from.isBefore(this.to)) {
            throw new LyraException(START_AFTER_END_MSG);
        }
        this.from = from;
    }

    /**
     * Set the to date and time.
     *
     * @param to The new end time
     */
    public void setTo(LocalDateTime to) throws LyraException {
        if (!this.from.isBefore(to)) {
            throw new LyraException(START_AFTER_END_MSG);
        }
        this.to = to;
    }

    /**
     * Convert the Event object to a string for file storage.
     */
    @Override
    public String toFileString() {
        return "E | " + (getIsDone() ? DONE_MARKER : NOT_DONE_MARKER) + " | " + getDescription() + " | "
                + from.format(DateTimeFormatter.ofPattern("d/MM/yyyy HHmm")) + " | "
                + to.format(DateTimeFormatter.ofPattern("d/MM/yyyy HHmm"));
    }

    /**
     * Convert the Event object to a string for display.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: "
                + from.format(DateTimeFormatter.ofPattern("MMM d yyyy, h:mma")) + " to: "
                + to.format(DateTimeFormatter.ofPattern("MMM d yyyy, h:mma")) + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof Event)) {
            return false;
        }
        Event other = (Event) obj;
        return Objects.equals(from, other.from) && Objects.equals(to, other.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), from, to);
    }
}
