package lyra;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles user interface interactions.
 */
public class Ui {
    private final String separator = "____________________________________________________________";
    private final Scanner scanner;

    /**
     * Constructor for Ui.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads a command from the user.
     *
     * @return The user's input command
     */
    public String readCommand() {
        System.out.print("> ");
        String command = scanner.nextLine();
        return command;
    }

    /**
     * Displays all tasks when loading from storage.
     *
     * @param taskList The task list to display
     */
    public void showLoadTasks(TaskList taskList) {
        prettyPrint("""
                Here are the tasks in your list:
                """);
        for (int i = 0; i < taskList.getSize(); i++) {
            Task task = taskList.getTask(i);
            prettyPrint(task.toString());
        }
    }

    /**
     * Displays all tasks in the task list.
     *
     * @param taskList The task list to display
     */
    public void showAllTasks(TaskList taskList) {
        String listString = "  Here are the tasks in your list:\n";
        for (int i = 0; i < taskList.getSize(); i++) {
            Task task = taskList.getTask(i);
            listString += ("  " + (i + 1) + ". " + task.toString() + "\n");
        }
        prettyPrint(listString);
    }

    /**
     * Displays a message when a task is marked as done.
     *
     * @param task The task that was marked
     */
    public void showMarked(Task task) {
        prettyPrint("""
                Great! I've marked this task as done:
                [%s] %s
                """.formatted(task.getStatusIcon(), task.getDescription()));
    }

    /**
     * Displays a message when a task is unmarked.
     *
     * @param task The task that was unmarked
     */
    public void showUnmarked(Task task) {
        prettyPrint("""
                OK, I've marked this task as not done yet:
                [%s] %s
                """.formatted(task.getStatusIcon(), task.getDescription()));
    }

    /**
     * Displays a message when a todo is added.
     *
     * @param task The todo task that was added
     */
    public void showAddedTodo(Task task) {
        prettyPrint("""
                Got it. I've added this task:
                %s
                """.formatted(task.toString()));
    }

    /**
     * Displays a message when a deadline is added.
     *
     * @param task The deadline task that was added
     */
    public void showAddedDeadline(Task task) {
        prettyPrint("""
                Got it. I've added this task:
                %s
                """.formatted(task.toString()));
    }

    /**
     * Displays a message when an event is added.
     *
     * @param task The event task that was added
     */
    public void showAddedEvent(Task task) {
        prettyPrint("""
                Got it. I've added this task:
                %s
                """.formatted(task.toString()));
    }

    /**
     * Displays a message when a task is removed.
     *
     * @param task The task that was removed
     */
    public void showRemovedTask(Task task) {
        prettyPrint("""
                Okay. I've removed this task:
                %s
                """.formatted(task.toString()));
    }

    /**
     * Displays tasks that match the search criteria.
     *
     * @param foundTasks The list of matching tasks
     */
    public void showFoundTasks(ArrayList<Task> foundTasks) {
        if (foundTasks.isEmpty()) {
            prettyPrint("No matching tasks found.");
            return;
        }
        String findString = "Here are the matching tasks in your list:\n";
        for (int i = 0; i < foundTasks.size(); i++) {
            findString += ((i + 1) + "." + foundTasks.get(i).toString() + "\n");
        }
        prettyPrint(findString);
    }

    /**
     * Displays the welcome message.
     */
    public void showWelcome() {
        prettyPrint("""
                Hello! I'm Lyra
                What can I do for you?
                """);
    }

    /**
     * Displays the goodbye message.
     */
    public void showGoodbye() {
        prettyPrint("""
                Bye. Hope to see you again soon!
                """);
    }

    /**
     * Displays an error message.
     *
     * @param errorMessage The error message to display
     */
    public void showError(String errorMessage) {
        prettyPrint(String.format("""
                Oh No!!! %s
                """, errorMessage));
    }

    /**
     * Pretty print the message.
     */
    private void prettyPrint(String message) {
        System.out.println(separator);
        System.out.print(message);
        if (!message.isEmpty() && !message.endsWith("\n")) {
            System.out.println();
        }
        System.out.println(separator);
    }
}
