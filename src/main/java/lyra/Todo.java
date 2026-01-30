package lyra;

/**
 * Todo class for Lyra.
 */
public class Todo extends Task {

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
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Convert the Todo object to a string for display.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
