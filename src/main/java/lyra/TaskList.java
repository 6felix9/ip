package lyra;

import java.util.ArrayList;

/**
 * Manages a list of tasks.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Constructor for TaskList.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Constructor for TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Add a task to the list.
     */
    public void addTask(Task task) {
        this.tasks.add(task);
    }

    /**
     * Removes a task at the specified index.
     *
     * @param index The index of the task to remove
     * @return The removed task
     * @throws LyraException If the index is invalid
     */
    public Task removeTask(int index) throws LyraException {
        if (index < 0 || index >= this.tasks.size()) {
            throw new LyraException("Invalid task number!");
        }
        // Assertion: After validation, index must be within valid range
        assert index >= 0 && index < this.tasks.size() : "Index must be valid after bounds check";
        Task removed = this.tasks.remove(index);
        // Assertion: Removed task should not be null (tasks list should not contain nulls)
        assert removed != null : "Removed task should not be null";
        return removed;
    }

    /**
     * Get a task from the list.
     */
    public Task getTask(int index) {
        return this.tasks.get(index);
    }

    /**
     * Get all tasks from the list.
     */
    public ArrayList<Task> getTasks() {
        return this.tasks;
    }

    /**
     * Get the size of the list.
     */
    public int getSize() {
        return this.tasks.size();
    }

    /**
     * Marks a task as done at the specified index.
     *
     * @param index The index of the task to mark
     * @return The marked task
     * @throws LyraException If the index is invalid
     */
    public Task markTask(int index) throws LyraException {
        if (index < 0 || index >= this.tasks.size()) {
            throw new LyraException("Invalid task number!");
        }
        // Assertion: After validation, index must be within valid range
        assert index >= 0 && index < this.tasks.size() : "Index must be valid after bounds check";
        Task task = this.tasks.get(index);
        // Assertion: Task at index should not be null
        assert task != null : "Task at index should not be null";
        task.markDone();
        // Assertion: After marking, task should be done
        assert task.getIsDone() : "Task should be marked as done after markDone()";
        return task;
    }

    /**
     * Unmarks a task at the specified index.
     *
     * @param index The index of the task to unmark
     * @return The unmarked task
     * @throws LyraException If the index is invalid
     */
    public Task unmarkTask(int index) throws LyraException {
        if (index < 0 || index >= this.tasks.size()) {
            throw new LyraException("Invalid task number!");
        }
        this.tasks.get(index).unmarkDone();
        return this.tasks.get(index);
    }

    /**
     * Finds all tasks of the specified type.
     *
     * @param type The type of tasks to find
     * @return A list of matching tasks
     */
    public ArrayList<Task> findTasks(TaskType type) {
        ArrayList<Task> foundTasks = new ArrayList<>();
        for (Task task : this.tasks) {
            if (task.getType() == type) {
                foundTasks.add(task);
            }
        }
        return foundTasks;
    }
}
