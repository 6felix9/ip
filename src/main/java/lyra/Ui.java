package lyra;

import java.util.Scanner;
import java.util.ArrayList;

/**
 * Ui class for Lyra.
 */
public class Ui {
    private final String SEPARATOR = "____________________________________________________________";
    private final Scanner scanner;

    /**
     * Constructor for Ui.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Read the command from the user.
     */
    public String readCommand() {
        System.out.print("> ");
        String command = scanner.nextLine();
        return command;
    }

    /**
     * Show the tasks in the list.
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
     * Show all tasks in the list.
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
     * Show the marked task.
     */
    public void showMarked(Task task) {
        prettyPrint("""
                Great! I've marked this task as done:
                [%s] %s
                """.formatted(task.getStatusIcon(), task.getDescription()));
    }

    /**
     * Show the unmarked task.
     */
    public void showUnmarked(Task task) {
        prettyPrint("""
                OK, I've marked this task as not done yet:
                [%s] %s
                """.formatted(task.getStatusIcon(), task.getDescription()));
    }

    /**
     * Show the added todo task.
     */
    public void showAddedTodo(Task task) {
        prettyPrint("""
                Got it. I've added this task:
                %s
                """.formatted(task.toString()));
    }

    /**
     * Show the added deadline task.
     */
    public void showAddedDeadline(Task task) {
        prettyPrint("""
                Got it. I've added this task:
                %s
                """.formatted(task.toString()));
    }

    /**
     * Show the added event task.
     */
    public void showAddedEvent(Task task) {
        prettyPrint("""
                Got it. I've added this task:
                %s
                """.formatted(task.toString()));
    }

    /**
     * Show the removed task.
     */
    public void showRemovedTask(Task task) {
        prettyPrint("""
                Okay. I've removed this task:
                %s
                """.formatted(task.toString()));
    }

    /**
     * Show the found tasks.
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
     * Show the welcome message.
     */
    public void showWelcome() {
        prettyPrint("""
                Hello! I'm Lyra
                What can I do for you?
                """);
    }

    /**
     * Show the goodbye message.
     */
    public void showGoodbye() {
        prettyPrint("""
                Bye. Hope to see you again soon!
                """);
    }

    /**
     * Show the error message.
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
        System.out.println(SEPARATOR);
        System.out.print(message);
        if (!message.isEmpty() && !message.endsWith("\n")) {
            System.out.println();
        }
        System.out.println(SEPARATOR);
    }
}
