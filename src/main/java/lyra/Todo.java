package lyra;

/**
 * Represents a todo task without any date/time.
 */
public class Todo extends Task {
    private static final String DONE_MARKER = "1";
    private static final String NOT_DONE_MARKER = "0";

    /**
     * Constructor for Todo.
     */
    public Todo(String description) {
        super(description, TaskType.TODO);
    }

    /**
     * Convert the Todo object to a string for file storage.
     */
    @Override
    public String toFileString() {
        return "T | " + (getIsDone() ? DONE_MARKER : NOT_DONE_MARKER) + " | " + getDescription();
    }

    /**
     * Convert the Todo object to a string for display.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
