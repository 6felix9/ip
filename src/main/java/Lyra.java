import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Lyra {
    private static final String SEPARATOR = "____________________________________________________________";

    /**
     * Prints the given content with the standard separator line above and below.
     */
    private static void prettyPrint(String content) {
        System.out.println(SEPARATOR);
        System.out.print(content);
        if (!content.isEmpty() && !content.endsWith("\n")) {
            System.out.println();
        }
        System.out.println(SEPARATOR);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> todoList = new ArrayList<>(100);

        prettyPrint("""
                  Hello! I'm Lyra
                  What can I do for you?
                """);

        while (true) {
            String inputString = scanner.nextLine();
            if (inputString == null) break;

            String command = inputString.split(" ")[0].toLowerCase();

            switch (command) {

            case "bye":
                prettyPrint("  Bye. Hope to see you again soon!");
                return;

            case "list":
                String listString = "  Here are the tasks in your list:\n";
                for (int i = 0; i < todoList.size(); i++) {
                    Task task = todoList.get(i);
                    listString += ("  " + (i + 1) + "." + task.toString() + "\n");
                }
                prettyPrint(listString);
                break;

            case "mark":
                try {
                    String[] parts = inputString.split(" ", 2);
                    if (parts.length < 2 || parts[1].isBlank()) {
                        throw new LyraException("Please specify a task number.");
                    }

                    String taskNum = parts[1].trim();
                    if (!taskNum.matches("\\d+")) {
                        throw new LyraException("Please specify a task number.");
                    }

                    int idx = Integer.parseInt(taskNum);
                    if (idx <= 0 || idx > todoList.size()) {
                        throw new LyraException("Task number out of range.");
                    }

                    Task task = todoList.get(idx - 1);
                    if (task == null) {
                        throw new LyraException("Task number out of range.");
                    }

                    task.markDone();

                    prettyPrint("""
                              Great! I've marked this task as done:
                              [%s] %s
                            """.formatted(task.getStatusIcon(), task.getDescription()));
                } catch (LyraException e) {
                    prettyPrint("  Oh No!!! " + e.getMessage());
                }
                break;

            case "unmark":
                try {
                    String[] parts = inputString.split(" ", 2);
                    if (parts.length < 2 || parts[1].isBlank()) {
                        throw new LyraException("Please specify a task number.");
                    }

                    String taskNum = parts[1].trim();
                    if (!taskNum.matches("\\d+")) {
                        throw new LyraException("Please specify a task number.");
                    }

                    int idx = Integer.parseInt(taskNum);
                    if (idx <= 0 || idx > todoList.size()) {
                        throw new LyraException("Task number out of range.");
                    }

                    Task task = todoList.get(idx - 1);
                    if (task == null) {
                        throw new LyraException("Task number out of range.");
                    }

                    task.unmarkDone();

                    prettyPrint("""
                              OK, I've marked this task as not done yet:
                              [%s] %s
                            """.formatted(task.getStatusIcon(), task.getDescription()));
                } catch (LyraException e) {
                    prettyPrint("  Oh No!!! " + e.getMessage());
                }
                break;

            case "todo":
                try {
                    String[] parts = inputString.split(" ", 2);
                    if (parts.length < 2 || parts[1].trim().isEmpty()) {
                        throw new LyraException("The description of a todo cannot be empty.");
                    }

                    String todoDescription = parts[1].trim();

                    Task newTodo = new Todo(todoDescription);
                    todoList.add(newTodo);

                    prettyPrint("""
                              Got it. I've added this task:
                                %s
                              Now you have %d tasks in the list.
                            """.formatted(newTodo.toString(), todoList.size()));
                } catch (LyraException e) {
                    prettyPrint("  Oh No!!! " + e.getMessage());
                }
                break;

            case "event":
                try {
                    String[] parts = inputString.split(" ", 2);
                    if (parts.length < 2) {
                        throw new LyraException("Please specify event time using /from and /to.");
                    }

                    String rest = parts[1];
                    if (!rest.contains(" /from ") || !rest.contains(" /to ")) {
                        throw new LyraException("Please specify event time using /from and /to.");
                    }

                    String[] descAndFrom = rest.split(" /from ", 2);
                    String eventDescription = descAndFrom[0];

                    String[] fromAndTo = descAndFrom[1].split(" /to ", 2);
                    String from = fromAndTo[0];
                    String to = fromAndTo[1];

                    Task newEvent = new Event(eventDescription, from, to);
                    todoList.add(newEvent);

                    prettyPrint("""
                              Got it. I've added this task:
                                %s
                              Now you have %d tasks in the list.
                            """.formatted(newEvent.toString(), todoList.size()));
                } catch (LyraException e) {
                    prettyPrint("  Oh No!!! " + e.getMessage());
                }
                break;

            case "deadline":
                try {
                    String[] parts = inputString.split(" ", 2);
                    if (parts.length < 2) {
                        throw new LyraException("Please specify a deadline using /by.");
                    }

                    String rest = parts[1];
                    if (!rest.contains(" /by ")) {
                        throw new LyraException("Please specify a deadline using /by.");
                    }

                    String[] descAndBy = rest.split(" /by ", 2);
                    String deadlineDescription = descAndBy[0];
                    String by = descAndBy[1];

                    Task newDeadline = new Deadline(deadlineDescription, by);
                    todoList.add(newDeadline);

                    prettyPrint("""
                              Got it. I've added this task:
                                %s
                              Now you have %d tasks in the list.
                            """.formatted(newDeadline.toString(), todoList.size()));
                } catch (LyraException e) {
                    prettyPrint("  Oh No!!! " + e.getMessage());
                }
                break;

            case "delete":
                try {
                    String[] parts = inputString.split(" ", 2);
                    if (parts.length < 2 || parts[1].isBlank()) {
                        throw new LyraException("Please specify a task number to delete.");
                    }

                    String taskNum = parts[1].trim();
                    if (!taskNum.matches("\\d+")) {
                        throw new LyraException("Please specify a valid task number to delete.");
                    }

                    int idx = Integer.parseInt(taskNum);
                    if (idx <= 0 || idx > todoList.size()) {
                        throw new LyraException("Task number out of range.");
                    }

                    Task removedTask = todoList.remove(idx - 1);

                    prettyPrint("""
                              Okay. I've removed this task:
                                %s
                              Now you have %d tasks in the list.
                            """.formatted(removedTask.toString(), todoList.size()));
                } catch (LyraException e) {
                    prettyPrint("  Oh No!!! " + e.getMessage());
                }
                break;

            case "find":
                try {
                    String[] parts = inputString.split(" ", 2);
                    if (parts.length < 2 || parts[1].isBlank()) {
                        throw new LyraException("Please specify a task type (todo, deadline, or event).");
                    }

                    String typeStr = parts[1].trim().toLowerCase();
                    TaskType filterType;

                    if (typeStr.equals("todo")) {
                        filterType = TaskType.TODO;
                    } else if (typeStr.equals("deadline")) {
                        filterType = TaskType.DEADLINE;
                    } else if (typeStr.equals("event")) {
                        filterType = TaskType.EVENT;
                    } else {
                        throw new LyraException("Invalid task type. Use: todo, deadline, or event.");
                    }

                    String findString = "  Here are your " + typeStr + " tasks:\n";

                    boolean found = false;
                    int displayIndex = 1;
                    for (int i = 0; i < todoList.size(); i++) {
                        Task task = todoList.get(i);
                        if (task.getType() == filterType) {
                            findString += ("  " + displayIndex + "." + task.toString() + "\n");
                            displayIndex++;
                            found = true;
                        }
                    }

                    if (!found) {
                        throw new LyraException("No " + typeStr + " tasks found.");
                    }

                    prettyPrint(findString);
                } catch (LyraException e) {
                    prettyPrint("  Oh No!!! " + e.getMessage());
                }
                break;

            default:
                try {
                    throw new LyraException("I'm sorry, but I don't know what that means :-(");
                } catch (LyraException e) {
                    prettyPrint("  Oh No!!! " + e.getMessage());
                }
                break;
            }
        }


        scanner.close();

    }
}
