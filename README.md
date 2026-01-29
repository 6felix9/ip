# Lyra

Lyra is a personal task management chatbot, originally based on the Duke project template.

## Setting up in IntelliJ

Prerequisites: JDK 17, IntelliJ IDEA.

1. Open IntelliJ and select **Open**.
2. Select the project directory and click **OK**.
3. Ensure the project is configured to use **JDK 17**.
4. Locate `src/main/java/Lyra.java`, right-click it, and choose **Run Lyra.main()**.

## Usage

Lyra supports the following commands:
- `todo <description>` - Adds a todo task.
- `deadline <description> /by <time>` - Adds a deadline task.
- `event <description> /from <start> /to <end>` - Adds an event task.
- `list` - Shows all tasks.
- `mark <index>` / `unmark <index>` - Marks a task as done or not done.
- `delete <index>` - Removes a task.
- `find <type>` - Finds tasks of a specific type.
- `bye` - Exits the application.

Tasks are automatically saved to `data/lyra.txt`.
