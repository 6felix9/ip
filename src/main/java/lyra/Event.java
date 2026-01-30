package lyra;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    private static final String DONE_MARKER = "1";
    private static final String NOT_DONE_MARKER = "0";

    private LocalDateTime from;
    private LocalDateTime to;

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    @Override
    public String toFileString() {
        return "E | " + (getIsDone() ? DONE_MARKER : NOT_DONE_MARKER) + " | " + getDescription() + " | " +
                from.format(DateTimeFormatter.ofPattern("d/MM/yyyy HHmm")) + " | " +
                to.format(DateTimeFormatter.ofPattern("d/MM/yyyy HHmm"));
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + 
               from.format(DateTimeFormatter.ofPattern("MMM d yyyy, h:mma")) + " to: " +
               to.format(DateTimeFormatter.ofPattern("MMM d yyyy, h:mma")) + ")";
    }
}
