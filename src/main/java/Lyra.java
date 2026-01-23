import java.util.Scanner;

public class Lyra {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Task[] todoList = new Task[100];
        int index = 0;

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

            // Bye Command
            if (inputString.equalsIgnoreCase("bye")) {
                System.out.println(exitString);
                break;

            // Display list command
            } else if (inputString.equalsIgnoreCase("list")) {
                String listString = "____________________________________________________________\n";

                for (int i = 0; i < index; i++) {
                    Task task = todoList[i];
                    listString += ("  [%s] " + (i + 1) + ". " + task.getDescription() + "\n")
                        .formatted(task.getStatusIcon());
                }

                listString += "____________________________________________________________";
                System.out.println(listString);

            // Mark task command
            } else if (inputString.startsWith("mark")) {
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

            // Unmark task command
            } else if (inputString.startsWith("unmark")) {
                String taskNum = inputString.split(" ")[1];
                int idx = Integer.parseInt(taskNum);
                Task task = todoList[idx - 1];
                task.unmarkDone();

                String unmarkString =                 
                        """
                        ____________________________________________________________
                        OK, I've marked this task as not done yet:
                          [%s] %s
                        ____________________________________________________________
                        """.formatted(task.getStatusIcon(), task.getDescription());
                        
                System.out.println(unmarkString);

            // Add task command
            } else {
                Task task = new Task(inputString);
                todoList[index] = task;
                index++;
                String addedString =
                        """
                        ____________________________________________________________
                          added: %s
                        ____________________________________________________________
                        """.formatted(task.getDescription());
                System.out.println(addedString);
            }
        }

        scanner.close();

    }
}
