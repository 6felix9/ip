package lyra;

/**
 * Task class for Lyra.
 */
public class Task {
    protected String description;
    protected boolean isDone;
    protected TaskType type;

    /**
     * Constructor for Task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
        this.type = TaskType.TODO;
    }

    /**
     * Constructor for Task.
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
        return (isDone ? "X" : " "); // mark done task with X
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
    }

    /**
     * Mark the task as not done.
     */
    public void unmarkDone() {
        this.isDone = false;
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
        return this.type.getSymbol() + " | " + (this.isDone ? "1" : "0") + " | " + this.description;
    }

    /**
     * Convert the Task object to a string for display.
     */
    @Override
    public String toString() {
        return ("[%s] %s".formatted(this.getStatusIcon(), this.description));
    }
}
