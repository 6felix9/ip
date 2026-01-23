import java.util.Scanner;

public class Lyra {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

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
            String echo = scanner.nextLine();
            if (echo == null) break;

            if (echo.equalsIgnoreCase("bye")) {
                System.out.println(exitString);
                break;
            } else {
                String echoString =
                        """
                        ____________________________________________________________
                          %s
                        ____________________________________________________________
                        """.formatted(echo);

                System.out.println(echoString);
            }
        }

        scanner.close();

    }
}
