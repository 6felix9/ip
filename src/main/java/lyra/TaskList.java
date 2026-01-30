package lyra;

import java.util.ArrayList;

/**
 * TaskList class for Lyra.
 */
public class TaskList {
    private ArrayList<Task> tasks;

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
     * Remove a task from the list.
     */
    public Task removeTask(int index) throws LyraException {
        if (index < 0 || index >= this.tasks.size()) {
            throw new LyraException("Invalid task number!");
        }
        return this.tasks.remove(index);
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
     * Mark a task as done.
     */
    public Task markTask(int index) throws LyraException {
        if (index < 0 || index >= this.tasks.size()) {
            throw new LyraException("Invalid task number!");
        }
        this.tasks.get(index).markDone();
        return this.tasks.get(index);
    }

    /**
     * Mark a task as not done.
     */
    public Task unmarkTask(int index) throws LyraException {
        if (index < 0 || index >= this.tasks.size()) {
            throw new LyraException("Invalid task number!");
        }
        this.tasks.get(index).unmarkDone();
        return this.tasks.get(index);
    }

    /**
     * Find tasks by type.
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
