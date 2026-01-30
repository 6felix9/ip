import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class Parser {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy HHmm");

    /**
     * Parse the date string into a Date object.
     */
    private LocalDateTime parseDateTime(String input) throws LyraException {
        try {
            return LocalDateTime.parse(input, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            throw new LyraException("Invalid format. Use: d/MM/yyyy HHmm (e.g., 2/12/2024 1800)");
        }
    }

    // For Command: "bye", "list", "mark", "unmark", "todo", "deadline", "event", "delete", "find"
    public Command getCommand(String command) throws LyraException {
        try {
            String commandWord = command.trim().split(" ")[0].toUpperCase();
            return Command.valueOf(commandWord);
        } catch (IllegalArgumentException e) {
            throw new LyraException("I'm sorry, but I don't know what that means :-(");
        }
    }

    // For Todo: "todo borrow book"
    public Todo parseTodo(String command) {
        // 1. Remove the command word ("todo ")
        String description = command.substring(command.indexOf(" ") + 1);
        // 2. Return the Todo object
        return new Todo(description);
    }

    // For Deadline: "deadline return book /by Sunday"
    public Deadline parseDeadline(String command) throws LyraException {
        // 1. Remove the command word ("deadline ")
        String content = command.substring(command.indexOf(" ") + 1);
        
        // 2. Split by the delimiter
        String[] parts = content.split(" /by ", 2);
        
        if (parts.length < 2) {
            throw new LyraException("A deadline must have a /by time!");
        }
        
        // 3. Return the Deadline object
        return new Deadline(parts[0], parseDateTime(parts[1])); 
    }

    // For Event: "event project meeting /from Mon 2pm /to 4pm"
    public Event parseEvent(String command) throws LyraException {
        // 1. Remove the command word ("event ")
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

    // For Index: "delete 1" or "mark 1" or "unmark 1"
    public int parseIndex(String command) throws LyraException {
        try {
            // 1. Split by the delimiter
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
            // 5. Throw an error if the user typed "delete abc"
            throw new LyraException("That's not a valid number! Please use digits (e.g., 1, 2, 3).");
        }
    }

    // For Type: "find todo"
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
}