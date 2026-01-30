package lyra;

import java.util.Scanner;
import java.util.ArrayList;

public class Ui {
    private final String SEPARATOR = "____________________________________________________________";
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public String readCommand() {
        System.out.print("> ");
        String command = scanner.nextLine();
        return command;
    }

    public void showLoadTasks(TaskList taskList) {
        prettyPrint("""
                Here are the tasks in your list:
                """);
        for (int i = 0; i < taskList.getSize(); i++) {
            Task task = taskList.getTask(i);
            prettyPrint(task.toString());
        }
    }

    public void showAllTasks(TaskList taskList) {
        String listString = "  Here are the tasks in your list:\n";
        for (int i = 0; i < taskList.getSize(); i++) {
            Task task = taskList.getTask(i);
            listString += ("  " + (i + 1) + ". " + task.toString() + "\n");
        }
        prettyPrint(listString);
    }

    public void showMarked(Task task) {
        prettyPrint("""
                Great! I've marked this task as done:
                [%s] %s
                """.formatted(task.getStatusIcon(), task.getDescription()));
    }

    public void showUnmarked(Task task) {
        prettyPrint("""
                OK, I've marked this task as not done yet:
                [%s] %s
                """.formatted(task.getStatusIcon(), task.getDescription()));
    }

    public void showAddedTodo(Task task) {
        prettyPrint("""
                Got it. I've added this task:
                %s
                """.formatted(task.toString()));
    }

    public void showAddedDeadline(Task task) {
        prettyPrint("""
                Got it. I've added this task:
                %s
                """.formatted(task.toString()));
    }

    public void showAddedEvent(Task task) {
        prettyPrint("""
                Got it. I've added this task:
                %s
                """.formatted(task.toString()));
    }

    public void showRemovedTask(Task task) {
        prettyPrint("""
                Okay. I've removed this task:
                %s
                """.formatted(task.toString()));
    }

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

    public void showWelcome() {
        prettyPrint("""
                Hello! I'm Lyra
                What can I do for you?
                """);
    }

    public void showGoodbye() {
        prettyPrint("""
                Bye. Hope to see you again soon!
                """);
    }

    public void showError(String errorMessage) {
        prettyPrint(String.format("""
                Oh No!!! %s
                """, errorMessage));
    }

    private void prettyPrint(String message) {
        System.out.println(SEPARATOR);
        System.out.print(message);
        if (!message.isEmpty() && !message.endsWith("\n")) {
            System.out.println();
        }
        System.out.println(SEPARATOR);
    }
}
