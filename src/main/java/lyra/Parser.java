package lyra;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Parses user input into commands and task objects.
 */
public class Parser {
    private static final String[] UPDATE_DELIMITERS = {" /description ", " /by ", " /from ", " /to "};
    private static final String[] UPDATE_TYPES = {
        UpdateType.DESCRIPTION, UpdateType.BY, UpdateType.FROM, UpdateType.TO
    };

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d/MM/uuuu HHmm")
            .withResolverStyle(ResolverStyle.STRICT);

    /**
     * Validates that the input does not contain the pipe character, which would corrupt the data file.
     *
     * @param input The string to validate
     * @throws LyraException If the input contains the pipe character
     */
    private void validateNoPipe(String input) throws LyraException {
        if (input != null && input.contains("|")) {
            throw new LyraException("The pipe character (|) is not allowed in task descriptions.");
        }
    }

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
            throw new LyraException("That date format doesn't look right. Please use d/MM/yyyy HHmm "
                    + "— for example: 2/12/2024 1800.");
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
            throw new LyraException("I'm not sure what you mean. Try: todo, deadline, event, list, "
                    + "mark, unmark, delete, find, update, or bye.");
        }
    }

    /**
     * Parses a todo command and creates a Todo task.
     *
     * @param command The todo command string
     * @return A Todo object
     */
    public Todo parseTodo(String command) throws LyraException {
        int spaceIndex = command.indexOf(" ");
        if (spaceIndex < 0) {
            throw new LyraException("A todo needs a description. Try: todo your task.");
        }
        String description = command.substring(spaceIndex + 1).trim();
        if (description.isEmpty()) {
            throw new LyraException("A todo needs a description. Try: todo your task.");
        }
        validateNoPipe(description);
        return new Todo(description);
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

        if (parts.length > 2 || (parts.length == 2 && parts[1].contains(" /by "))) {
            throw new LyraException("Please specify /by only once. Use: deadline description /by date.");
        }
        if (parts.length < 2) {
            throw new LyraException("A deadline needs a due time — add /by followed by the date "
                    + "(e.g., deadline return book /by 2/12/2024 1800).");
        }

        String desc = parts[0].trim();
        String timeStr = parts[1].trim();
        if (desc.isEmpty()) {
            throw new LyraException("A deadline needs a description. Try: deadline return book /by 2/12/2024 1800.");
        }
        if (timeStr.isEmpty()) {
            throw new LyraException("A deadline needs a due time — add /by followed by the date.");
        }
        validateNoPipe(desc);

        // 3. Return the Deadline object
        Deadline deadline = new Deadline(desc, parseDateTime(timeStr));
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
        if (firstSplit.length > 2 || (firstSplit.length == 2 && firstSplit[1].contains(" /from "))) {
            throw new LyraException("Please specify /from only once. Use: event desc /from start /to end.");
        }
        if (firstSplit.length < 2) {
            throw new LyraException("An event needs a start time — please include /from followed by the date.");
        }

        // 3. Split by the delimiter
        String[] secondSplit = firstSplit[1].split(" /to ", 2);
        if (secondSplit.length > 2 || (secondSplit.length == 2 && secondSplit[1].contains(" /to "))) {
            throw new LyraException("Please specify /to only once. Use: event desc /from start /to end.");
        }
        if (secondSplit.length < 2) {
            throw new LyraException("An event also needs an end time — please include /to followed by the date.");
        }

        String desc = firstSplit[0].trim();
        String fromStr = secondSplit[0].trim();
        String toStr = secondSplit[1].trim();
        if (desc.isEmpty()) {
            throw new LyraException("An event needs a description. Try: event meeting "
                    + "/from 2/12/2024 1000 /to 2/12/2024 1200.");
        }
        if (fromStr.isEmpty() || toStr.isEmpty()) {
            throw new LyraException("An event needs both /from and /to dates.");
        }
        validateNoPipe(desc);

        // 4. Return the Event object
        return new Event(desc, parseDateTime(fromStr), parseDateTime(toStr));
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
                throw new LyraException("Could you include a task number? For example: mark 2.");
            }

            // 3. Parse the string to an int
            int userIndex = Integer.parseInt(parts[1]);

            // 4. Convert 1-based (User) to 0-based (Java)
            return userIndex - 1;

        } catch (NumberFormatException e) {
            throw new LyraException("That doesn't look like a number. Please use digits, like: mark 2.");
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
                throw new LyraException("Which type would you like to find? Try: find todo, "
                        + "find deadline, or find event.");
            }
            return TaskType.valueOf(parts[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new LyraException("I don't recognise that task type. Please use one of: todo, deadline, or event.");
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
            throw new LyraException("To update a task, include the task number and what to change "
                    + "— e.g., update 1 /description new title.");
        }

        for (int i = 0; i < UPDATE_DELIMITERS.length; i++) {
            String delimiter = UPDATE_DELIMITERS[i];
            String updateType = UPDATE_TYPES[i];
            if (content.contains(delimiter)) {
                return parseUpdateWithDelimiter(content, delimiter, updateType);
            }
        }
        throw new LyraException("I couldn't parse that update. Use: update <number> /description <text>, "
                + "or /by, /from, /to with a date.");
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
        if (parts.length > 2 || (parts.length == 2 && parts[1].contains(delimiter))) {
            throw new LyraException("Please specify each update parameter only once.");
        }
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            String hint = UpdateType.DESCRIPTION.equals(updateType)
                    ? "What's the new description? Try: update 1 /description your new task name."
                    : "I need a date after " + delimiter.trim() + ". For example: 2/12/2024 1800.";
            throw new LyraException(hint);
        }
        int index = parseUpdateIndex(parts[0].trim());
        if (UpdateType.DESCRIPTION.equals(updateType)) {
            String newDesc = parts[1].trim();
            validateNoPipe(newDesc);
            return new UpdateCommandData(index, newDesc);
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
                throw new LyraException("Task numbers start from 1 — please use a number like 1, 2, or 3.");
            }
            return userIndex - 1;
        } catch (NumberFormatException e) {
            throw new LyraException("That doesn't look like a number. Please use digits, "
                    + "like: update 2 /description new title.");
        }
    }
}
