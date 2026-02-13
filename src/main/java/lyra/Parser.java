package lyra;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parses user input into commands and task objects.
 */
public class Parser {
    private static final String[] UPDATE_DELIMITERS = {" /description ", " /by ", " /from ", " /to "};
    private static final String[] UPDATE_TYPES = {"description", "by", "from", "to"};

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy HHmm");

    /**
     * Parse the date string into a Date object.
     *
     * @param input The date string to parse
     * @return The parsed LocalDateTime
     * @throws LyraException If the date format is invalid
     */
    private LocalDateTime parseDateTime(String input) throws LyraException {
        try {
            return LocalDateTime.parse(input, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            throw new LyraException("Invalid format. Use: d/MM/yyyy HHmm (e.g., 2/12/2024 1800)");
        }
    }

    /**
     * Extracts the command type from a user input string.
     *
     * @param command The user input string
     * @return The Command enum value
     * @throws LyraException If the command is not recognized
     */
    public Command getCommand(String command) throws LyraException {
        try {
            String commandWord = command.trim().split(" ")[0].toUpperCase();
            return Command.valueOf(commandWord);
        } catch (IllegalArgumentException e) {
            throw new LyraException("I'm sorry, but I don't know what that means :-(");
        }
    }

    /**
     * Parses a todo command and creates a Todo task.
     *
     * @param command The todo command string
     * @return A Todo object
     */
    public Todo parseTodo(String command) {
        int spaceIndex = command.indexOf(" ");
        // Assertion: After finding space index, it should be valid (assumes format: "todo <description>")
        assert spaceIndex >= 0 : "Todo command must contain a space separator";
        String description = command.substring(spaceIndex + 1);
        // Assertion: Description should not be empty after parsing
        assert !description.isEmpty() : "Todo description should not be empty";
        Todo todo = new Todo(description);
        // Assertion: Created todo should not be null
        assert todo != null : "Created Todo should not be null";
        return todo;
    }

    /**
     * Parses a deadline command and creates a Deadline task.
     *
     * @param command The deadline command string
     * @return A Deadline object
     * @throws LyraException If the command format is invalid
     */
    public Deadline parseDeadline(String command) throws LyraException {
        String content = command.substring(command.indexOf(" ") + 1);

        // 2. Split by the delimiter
        String[] parts = content.split(" /by ", 2);

        if (parts.length < 2) {
            throw new LyraException("A deadline must have a /by time!");
        }

        // Assertion: After validation, parts array should have exactly 2 elements
        assert parts.length == 2 : "Parts array should have description and time after split";
        // Assertion: Description and time should not be empty
        assert !parts[0].isEmpty() : "Deadline description should not be empty";
        assert !parts[1].isEmpty() : "Deadline time should not be empty";

        // 3. Return the Deadline object
        Deadline deadline = new Deadline(parts[0], parseDateTime(parts[1]));
        // Assertion: Created deadline should not be null
        assert deadline != null : "Created Deadline should not be null";
        return deadline;
    }

    /**
     * Parses an event command and creates an Event task.
     *
     * @param command The event command string
     * @return An Event object
     * @throws LyraException If the command format is invalid
     */
    public Event parseEvent(String command) throws LyraException {
        String content = command.substring(command.indexOf(" ") + 1);

        // 2. Split by the delimiter
        String[] firstSplit = content.split(" /from ", 2);
        if (firstSplit.length < 2) {
            throw new LyraException("Event needs /from!");
        }

        // 3. Split by the delimiter
        String[] secondSplit = firstSplit[1].split(" /to ", 2);
        if (secondSplit.length < 2) {
            throw new LyraException("Event needs /to!");
        }

        // 4. Return the Event object
        return new Event(firstSplit[0], parseDateTime(secondSplit[0]), parseDateTime(secondSplit[1]));
    }

    /**
     * Parses a task index from a command string.
     *
     * @param command The command string containing an index
     * @return The zero-based index
     * @throws LyraException If the index is invalid or missing
     */
    public int parseIndex(String command) throws LyraException {
        try {
            String[] parts = command.split(" ");

            // 2. Check if the user actually provided a number
            if (parts.length < 2) {
                throw new LyraException("Please specify a task number!");
            }

            // 3. Parse the string to an int
            int userIndex = Integer.parseInt(parts[1]);

            // 4. Convert 1-based (User) to 0-based (Java)
            return userIndex - 1;

        } catch (NumberFormatException e) {
            throw new LyraException("That's not a valid number! Please use digits (e.g., 1, 2, 3).");
        }
    }

    /**
     * Parses a task type from a find command.
     *
     * @param input The find command string
     * @return The TaskType enum value
     * @throws LyraException If the task type is invalid or missing
     */
    public TaskType parseTaskType(String input) throws LyraException {
        try {
            String[] parts = input.split(" ");
            if (parts.length < 2) {
                throw new LyraException("Please specify a task type (todo, deadline, or event).");
            }
            return TaskType.valueOf(parts[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new LyraException("Invalid task type! Please use: todo, deadline, event.");
        }
    }

    /**
     * Parses an update command.
     * Format: update &lt;index&gt; /description &lt;new desc&gt; or update &lt;index&gt; /by|/from|/to &lt;date&gt;
     *
     * @param command The update command string
     * @return UpdateCommandData with parsed index, type, and value
     * @throws LyraException If the command format is invalid
     */
    public UpdateCommandData parseUpdateCommand(String command) throws LyraException {
        String content = command.substring("update".length()).trim();
        if (content.isEmpty()) {
            throw new LyraException("Please specify a task number and update type (e.g., /description or /by).");
        }

        for (int i = 0; i < UPDATE_DELIMITERS.length; i++) {
            String delimiter = UPDATE_DELIMITERS[i];
            String updateType = UPDATE_TYPES[i];
            if (content.contains(delimiter)) {
                return parseUpdateWithDelimiter(content, delimiter, updateType);
            }
        }
        throw new LyraException("Invalid update format. Use: update <index> /description <new desc> "
                + "or update <index> /by <date> (for deadlines) or update <index> /from <date> "
                + "or update <index> /to <date> (for events).");
    }

    /**
     * Parses a single update type from content using the given delimiter.
     *
     * @param content The content after "update" keyword
     * @param delimiter The delimiter to split by (e.g., " /description ")
     * @param updateType The update type (e.g., "description", "by", "from", "to")
     * @return UpdateCommandData with parsed index and value
     * @throws LyraException If the format is invalid
     */
    private UpdateCommandData parseUpdateWithDelimiter(String content, String delimiter, String updateType)
            throws LyraException {
        String[] parts = content.split(delimiter, 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            String hint = "description".equals(updateType)
                    ? "Please provide a new description after /description"
                    : "Please provide a date after " + delimiter.trim() + " (e.g., 2/12/2024 1800)";
            throw new LyraException(hint);
        }
        int index = parseUpdateIndex(parts[0].trim());
        if ("description".equals(updateType)) {
            return new UpdateCommandData(index, parts[1].trim());
        }
        LocalDateTime dateValue = parseDateTime(parts[1].trim());
        return new UpdateCommandData(index, updateType, dateValue);
    }

    /**
     * Parses the index from the first part of an update command (e.g., "1" -> 0).
     */
    private int parseUpdateIndex(String indexPart) throws LyraException {
        try {
            int userIndex = Integer.parseInt(indexPart);
            if (userIndex < 1) {
                throw new LyraException("Task number must be at least 1.");
            }
            return userIndex - 1;
        } catch (NumberFormatException e) {
            throw new LyraException("That's not a valid number! Please use digits (e.g., 1, 2, 3).");
        }
    }
}
