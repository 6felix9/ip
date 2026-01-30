package lyra;

import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

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

    public Task markTask(int index) throws LyraException {
        if (index < 0 || index >= this.tasks.size()) {
            throw new LyraException("Invalid task number!");
        }
        this.tasks.get(index).markDone();
        return this.tasks.get(index);
    }

    public Task unmarkTask(int index) throws LyraException {
        if (index < 0 || index >= this.tasks.size()) {
            throw new LyraException("Invalid task number!");
        }
        this.tasks.get(index).unmarkDone();
        return this.tasks.get(index);
    }

    public ArrayList<Task> findTasks(String keyword) {
        ArrayList<Task> foundTasks = new ArrayList<>();
        for (Task task : this.tasks) {
            if (task.getDescription().contains(keyword)) {
                foundTasks.add(task);
            }
        }
        return foundTasks;
    }
}
