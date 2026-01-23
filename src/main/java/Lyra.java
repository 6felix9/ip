import java.util.ArrayList;
import java.util.Scanner;

public class Lyra {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> todoList = new ArrayList<>(100);

        String welcomeString =
                """
                ____________________________________________________________
                  Hello! I'm Lyra
                  What can I do for you?
                ____________________________________________________________
                """;

        String exitString =
                """
                ____________________________________________________________
                  Bye. Hope to see you again soon!
                ____________________________________________________________
                """;

        System.out.println(welcomeString);

        while (true) {
            String inputString = scanner.nextLine();
            if (inputString == null) break;

            String command = inputString.split(" ")[0].toLowerCase();

            switch (command) {

            case "bye":
                System.out.println(exitString);
                return;

            case "list":
                String listString = 
                        """
                        ____________________________________________________________
                          Here are the tasks in your list:
                        """;
                                
                for (int i = 0; i < todoList.size(); i++) {
                    Task task = todoList.get(i);
                    listString += ("  " + (i + 1) + "." + task.toString() + "\n");
                }

                listString += "____________________________________________________________\n";
                System.out.println(listString);
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

                    String markString =
                            """
                            ____________________________________________________________
                              Great! I've marked this task as done:
                              [%s] %s
                            ____________________________________________________________
                            """.formatted(task.getStatusIcon(), task.getDescription());

                    System.out.println(markString);
                } catch (LyraException e) {
                    System.out.println(
                            """
                            ____________________________________________________________
                             Oh No!!! %s
                            ____________________________________________________________
                            """.formatted(e.getMessage()));
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

                    String unmarkString =
                            """
                            ____________________________________________________________
                              OK, I've marked this task as not done yet:
                              [%s] %s
                            ____________________________________________________________
                            """.formatted(task.getStatusIcon(), task.getDescription());

                    System.out.println(unmarkString);
                } catch (LyraException e) {
                    System.out.println(
                            """
                            ____________________________________________________________
                             Oh No!!! %s
                            ____________________________________________________________
                            """.formatted(e.getMessage()));
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

                    String addedTodoString =
                            """
                            ____________________________________________________________
                              Got it. I've added this task: 
                                %s
                              Now you have %d tasks in the list.
                            ____________________________________________________________
                            """.formatted(newTodo.toString(), todoList.size());

                    System.out.println(addedTodoString);
                } catch (LyraException e) {
                    System.out.println(
                            """
                            ____________________________________________________________
                             Oh No!!! %s
                            ____________________________________________________________
                            """.formatted(e.getMessage()));
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

                    String addedEventString =
                            """
                            ____________________________________________________________
                              Got it. I've added this task:
                                %s
                              Now you have %d tasks in the list.
                            ____________________________________________________________
                            """.formatted(newEvent.toString(), todoList.size());

                    System.out.println(addedEventString);
                } catch (LyraException e) {
                    System.out.println(
                            """
                            ____________________________________________________________
                             Oh No!!! %s
                            ____________________________________________________________
                            """.formatted(e.getMessage()));
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

                    String addedDeadlineString =
                            """
                            ____________________________________________________________
                              Got it. I've added this task:
                                %s
                              Now you have %d tasks in the list.
                            ____________________________________________________________
                            """.formatted(newDeadline.toString(), todoList.size());

                    System.out.println(addedDeadlineString);
                } catch (LyraException e) {
                    System.out.println(
                            """
                            ____________________________________________________________
                             Oh No!!! %s
                            ____________________________________________________________
                            """.formatted(e.getMessage()));
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

                    String deleteString =
                            """
                            ____________________________________________________________
                              Okay. I've removed this task:
                                %s
                              Now you have %d tasks in the list.
                            ____________________________________________________________
                            """.formatted(removedTask.toString(), todoList.size());

                    System.out.println(deleteString);
                } catch (LyraException e) {
                    System.out.println(
                            """
                            ____________________________________________________________
                             Oh No!!! %s
                            ____________________________________________________________
                            """.formatted(e.getMessage()));
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

                    String findString = 
                            """
                            ____________________________________________________________
                              Here are your %s tasks:
                            """.formatted(typeStr);

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

                    findString += "____________________________________________________________\n";
                    System.out.println(findString);
                } catch (LyraException e) {
                    System.out.println(
                            """
                            ____________________________________________________________
                             Oh No!!! %s
                            ____________________________________________________________
                            """.formatted(e.getMessage()));
                }
                break;

            default:
                Task newTask = new Task(inputString);
                todoList.add(newTask);

                String addedString =
                        """
                        ____________________________________________________________
                          added: %s
                        ____________________________________________________________
                                                """.formatted(newTask.getDescription());

                System.out.println(addedString);
                break;
            }
        }


        scanner.close();

    }
}
