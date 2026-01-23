import java.util.Scanner;

public class Lyra {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Task[] todoList = new Task[100];
        int size = 0;

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
                                
                for (int i = 0; i < size; i++) {
                    Task task = todoList[i];
                    listString += ("  " + (i + 1) + "." + task.toString() + "\n");
                }

                listString += "____________________________________________________________";
                System.out.println(listString);
                break;

            case "mark":
                String taskNum = inputString.split(" ")[1];
                int idx = Integer.parseInt(taskNum);
                Task task = todoList[idx - 1];
                task.markDone();

                String markString =
                        """
                        ____________________________________________________________
                          Great! I've marked this task as done:
                          [%s] %s
                        ____________________________________________________________
                        """.formatted(task.getStatusIcon(), task.getDescription());

                System.out.println(markString);
                break;

            case "unmark":
                String taskNum2 = inputString.split(" ")[1];
                int idx2 = Integer.parseInt(taskNum2);
                Task task2 = todoList[idx2 - 1];
                task2.unmarkDone();

                String unmarkString =
                        """
                        ____________________________________________________________
                          OK, I've marked this task as not done yet:
                          [%s] %s
                        ____________________________________________________________
                        """.formatted(task2.getStatusIcon(), task2.getDescription());

                System.out.println(unmarkString);
                break;

            case "todo":
                String todoDescription = inputString.split(" ", 2)[1].trim();
                Task newTodo = new Todo(todoDescription);
                todoList[size] = newTodo;
                size++;

                String addedTodoString =
                        """
                        ____________________________________________________________
                          Got it. I've added this task: 
                            %s
                          Now you have %d tasks in the list.
                        ____________________________________________________________
                        """.formatted(newTodo.toString(), size);

                System.out.println(addedTodoString);
                break;

            case "event":
                String[] parts = inputString.split(" ", 2);
                String rest = parts[1];

                String[] descAndFrom = rest.split(" /from ", 2);
                String eventDescription = descAndFrom[0];

                String[] fromAndTo = descAndFrom[1].split(" /to ", 2);
                String from = fromAndTo[0];
                String to = fromAndTo[1];

                Task newEvent = new Event(eventDescription, from, to);
                todoList[size] = newEvent;
                size++;

                String addedEventString =
                        """
                        ____________________________________________________________
                          Got it. I've added this task:
                            %s
                          Now you have %d tasks in the list.
                        ____________________________________________________________
                        """.formatted(newEvent.toString(), size);

                System.out.println(addedEventString);
                break;

            case "deadline":
                String[] parts2 = inputString.split(" ", 2);
                String rest2 = parts2[1];

                String[] descAndBy = rest2.split(" /by ", 2);
                String deadlineDescription = descAndBy[0];
                String by = descAndBy[1];

                Task newDeadline = new Deadline(deadlineDescription, by);
                todoList[size] = newDeadline;
                size++;

                String addedDeadlineString =
                        """
                        ____________________________________________________________
                          Got it. I've added this task:
                            %s
                          Now you have %d tasks in the list.
                        ____________________________________________________________
                        """.formatted(newDeadline.toString(), size);

                System.out.println(addedDeadlineString);
                break;

            default:
                Task newTask = new Task(inputString);
                todoList[size] = newTask;
                size++;

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
