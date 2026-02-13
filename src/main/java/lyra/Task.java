package lyra;

/**
 * Represents a task with a description and completion status.
 */
public class Task {
    private static final String DONE_MARKER = "1";
    private static final String NOT_DONE_MARKER = "0";
    private static final String DONE_ICON = "X";
    private static final String NOT_DONE_ICON = " ";

    private String description;
    private boolean isDone;
    private TaskType type;

    /**
     * Creates a Task with the specified description (defaults to TODO type).
     *
     * @param description The task description
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
        this.type = TaskType.TODO;
    }

    /**
     * Creates a Task with the specified description and type.
     *
     * @param description The task description
     * @param type The task type
     */
    public Task(String description, TaskType type) {
        this.description = description;
        this.isDone = false;
        this.type = type;
    }

    /**
     * Get the status icon.
     */
    public String getStatusIcon() {
        return (isDone ? DONE_ICON : NOT_DONE_ICON);
    }

    /**
     * Get the description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Mark the task as done.
     */
    public void markDone() {
        this.isDone = true;
        // Assertion: After marking, task should be done
        assert this.isDone : "Task should be marked as done after markDone()";
    }

    /**
     * Mark the task as not done.
     */
    public void unmarkDone() {
        this.isDone = false;
        // Assertion: After unmarking, task should not be done
        assert !this.isDone : "Task should be marked as not done after unmarkDone()";
    }

    /**
     * Get the done status.
     */
    public boolean getIsDone() {
        return this.isDone;
    }

    /**
     * Get the type.
     */
    public TaskType getType() {
        return this.type;
    }

    /**
     * Convert the Task object to a string for file storage.
     */
    public String toFileString() {
        return this.type.getSymbol() + " | " + (this.isDone ? DONE_MARKER : NOT_DONE_MARKER) + " | " + this.description;
    }

    /**
     * Convert the Task object to a string for display.
     */
    @Override
    public String toString() {
        return ("[%s] %s".formatted(this.getStatusIcon(), this.description));
    }
}
