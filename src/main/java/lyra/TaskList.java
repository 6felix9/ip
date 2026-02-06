package lyra;

import java.util.ArrayList;

/**
 * Manages a list of tasks.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

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
        return this.tasks.remove(index);
    }

    public Task getTask(int index) {
        return this.tasks.get(index);
    }

    public ArrayList<Task> getTasks() {
        return this.tasks;
    }

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
        this.tasks.get(index).markDone();
        return this.tasks.get(index);
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
