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
     * @throws LyraException If the task list contains invalid data
     */
    public void showLoadTasks(TaskList taskList) throws LyraException {
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
     * @throws LyraException If the task list contains invalid data
     */
    public void showAllTasks(TaskList taskList) throws LyraException {
        prettyPrint(getAllTasksMessage(taskList));
    }

    /**
     * Returns a formatted string of all tasks in the task list.
     *
     * @param taskList The task list to display
     * @return Formatted string of all tasks
     * @throws LyraException If the task list contains invalid data
     */
    public String getAllTasksMessage(TaskList taskList) throws LyraException {
        String listString = "Here are the tasks in your list:\n";
        for (int i = 0; i < taskList.getSize(); i++) {
            Task task = taskList.getTask(i);
            listString += ((i + 1) + ". " + task.toString() + "\n");
        }
        return listString;
    }

    /**
     * Displays a message when a task is marked as done.
     *
     * @param task The task that was marked
     */
    public void showMarked(Task task) {
        prettyPrint(getMarkedMessage(task));
    }

    /**
     * Returns a formatted string when a task is marked as done.
     *
     * @param task The task that was marked
     * @return Formatted message string
     */
    public String getMarkedMessage(Task task) {
        return "Great! I've marked this task as done:\n" + task.toString();
    }

    /**
     * Displays a message when a task is unmarked.
     *
     * @param task The task that was unmarked
     */
    public void showUnmarked(Task task) {
        prettyPrint(getUnmarkedMessage(task));
    }

    /**
     * Returns a formatted string when a task is unmarked.
     *
     * @param task The task that was unmarked
     * @return Formatted message string
     */
    public String getUnmarkedMessage(Task task) {
        return "OK, I've marked this task as not done yet:\n" + task.toString();
    }

    /**
     * Displays a message when a todo is added.
     *
     * @param task The todo task that was added
     */
    public void showAddedTodo(Task task) {
        prettyPrint(getAddedTaskMessage(task));
    }

    /**
     * Displays a message when a deadline is added.
     *
     * @param task The deadline task that was added
     */
    public void showAddedDeadline(Task task) {
        prettyPrint(getAddedTaskMessage(task));
    }

    /**
     * Displays a message when an event is added.
     *
     * @param task The event task that was added
     */
    public void showAddedEvent(Task task) {
        prettyPrint(getAddedTaskMessage(task));
    }

    /**
     * Returns a formatted string when a task is added.
     *
     * @param task The task that was added
     * @return Formatted message string
     */
    public String getAddedTaskMessage(Task task) {
        return "Got it. I've added this task:\n" + task.toString();
    }

    /**
     * Displays a message when a task is removed.
     *
     * @param task The task that was removed
     */
    public void showRemovedTask(Task task) {
        prettyPrint(getRemovedTaskMessage(task));
    }

    /**
     * Returns a formatted string when a task is removed.
     *
     * @param task The task that was removed
     * @return Formatted message string
     */
    public String getRemovedTaskMessage(Task task) {
        return "Okay. I've removed this task:\n" + task.toString();
    }

    /**
     * Displays tasks that match the search criteria.
     *
     * @param foundTasks The list of matching tasks
     */
    public void showFoundTasks(ArrayList<Task> foundTasks) {
        prettyPrint(getFoundTasksMessage(foundTasks));
    }

    /**
     * Returns a formatted string of tasks that match the search criteria.
     *
     * @param foundTasks The list of matching tasks
     * @return Formatted message string
     */
    public String getFoundTasksMessage(ArrayList<Task> foundTasks) {
        if (foundTasks.isEmpty()) {
            return "No matching tasks found.";
        }
        String findString = "Here are the matching tasks in your list:\n";
        for (int i = 0; i < foundTasks.size(); i++) {
            findString += ((i + 1) + ". " + foundTasks.get(i).toString() + "\n");
        }
        return findString;
    }

    /**
     * Displays the welcome message.
     */
    public void showWelcome() {
        prettyPrint(getWelcomeMessage());
    }

    /**
     * Returns the welcome message.
     *
     * @return Welcome message string
     */
    public String getWelcomeMessage() {
        return "Hello! I'm Lyra\nWhat can I do for you?";
    }

    /**
     * Displays the goodbye message.
     */
    public void showGoodbye() {
        prettyPrint(getGoodbyeMessage());
    }

    /**
     * Returns the goodbye message.
     *
     * @return Goodbye message string
     */
    public String getGoodbyeMessage() {
        return "Bye. Hope to see you again soon!";
    }

    /**
     * Displays an error message.
     *
     * @param errorMessage The error message to display
     */
    public void showError(String errorMessage) {
        prettyPrint(getErrorMessage(errorMessage));
    }

    /**
     * Returns a formatted error message.
     *
     * @param errorMessage The error message
     * @return Formatted error message string
     */
    public String getErrorMessage(String errorMessage) {
        return "Oh No!!! " + errorMessage;
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

    /**
     * Displays a message with formatting for CLI mode.
     *
     * @param message The message to display
     */
    public void displayMessage(String message) {
        prettyPrint(message);
    }
}
