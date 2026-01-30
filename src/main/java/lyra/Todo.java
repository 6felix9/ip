package lyra;

public class Todo extends Task {
    private static final String DONE_MARKER = "1";
    private static final String NOT_DONE_MARKER = "0";

    public Todo(String description) {
        super(description, TaskType.TODO);
    }

    @Override
    public String toFileString() {
        return "T | " + (getIsDone() ? DONE_MARKER : NOT_DONE_MARKER) + " | " + getDescription();
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
