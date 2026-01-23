import java.util.Scanner;

public class Lyra {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] todoList = new String[100];
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

            if (inputString.equalsIgnoreCase("bye")) {
                System.out.println(exitString);
                break;

            } else if (inputString.equalsIgnoreCase("list")) {
                String listString = "____________________________________________________________\n";

                for (int i = 0; i < index; i++) {
                    listString += "  " + (i + 1) + ". " + todoList[i] + "\n";
                }

                listString += "____________________________________________________________";
                System.out.println(listString);

            } else {
                String addedString =
                        """
                        ____________________________________________________________
                          added: %s
                        ____________________________________________________________
                        """.formatted(inputString);

                todoList[index] = inputString;
                index++;
                System.out.println(addedString);
            }
        }

        scanner.close();

    }
}
