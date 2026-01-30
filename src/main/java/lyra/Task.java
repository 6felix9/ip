package lyra;

public class Task {
    private static final String DONE_MARKER = "1";
    private static final String NOT_DONE_MARKER = "0";
    private static final String DONE_ICON = "X";
    private static final String NOT_DONE_ICON = " ";

    private String description;
    private boolean isDone;
    private TaskType type;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
        this.type = TaskType.TODO;
    }

    public Task(String description, TaskType type) {
        this.description = description;
        this.isDone = false;
        this.type = type;
    }

    public String getStatusIcon() {
        return (isDone ? DONE_ICON : NOT_DONE_ICON);
    }

    public String getDescription() {
        return this.description;
    }

    public void markDone() {
        this.isDone = true;
    }

    public void unmarkDone() {
        this.isDone = false;
    }

    public boolean getIsDone() {
        return this.isDone;
    }

    public TaskType getType() {
        return this.type;
    }

    public String toFileString() {
        return this.type.getSymbol() + " | " + (this.isDone ? DONE_MARKER : NOT_DONE_MARKER) + " | " + this.description;
    }

    @Override
    public String toString() {
        return ("[%s] %s".formatted(this.getStatusIcon(), this.description));
    }
}
