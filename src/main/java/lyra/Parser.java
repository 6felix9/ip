package lyra;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class Parser {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy HHmm");

    /**
     * Parses a date-time string into a LocalDateTime object based on a specific pattern.
     */
    private LocalDateTime parseDateTime(String input) throws LyraException {
        try {
            return LocalDateTime.parse(input, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            throw new LyraException("Invalid format. Use: d/MM/yyyy HHmm (e.g., 2/12/2024 1800)");
        }
    }

    /**
     * Identifies and returns the Command type from the raw user input.
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
     * Extracts the description from the input and creates a new Todo task.
     */
    public Todo parseTodo(String command) {
        String description = command.substring(command.indexOf(" ") + 1);
        return new Todo(description);
    }

    /**
     * Parses the description and deadline time to create a new Deadline task.
     */
    public Deadline parseDeadline(String command) throws LyraException {
        String content = command.substring(command.indexOf(" ") + 1);
        String[] parts = content.split(" /by ", 2);
        
        if (parts.length < 2) {
            throw new LyraException("A deadline must have a /by time!");
        }
        
        return new Deadline(parts[0], parseDateTime(parts[1])); 
    }

    /**
     * Parses the description, start time, and end time to create a new Event task.
     */
    public Event parseEvent(String command) throws LyraException {
        String content = command.substring(command.indexOf(" ") + 1);
        
        String[] firstSplit = content.split(" /from ", 2);
        if (firstSplit.length < 2) {
            throw new LyraException("Event needs /from!");
        }
        
        String[] secondSplit = firstSplit[1].split(" /to ", 2);
        if (secondSplit.length < 2) {
            throw new LyraException("Event needs /to!");
        }
        
        return new Event(firstSplit[0], parseDateTime(secondSplit[0]), parseDateTime(secondSplit[1]));
    }

    /**
     * Extracts the task index from the command and converts it to a zero-based integer.
     */
    public int parseIndex(String command) throws LyraException {
        try {
            String[] parts = command.split(" ");
            
            if (parts.length < 2) {
                throw new LyraException("Please specify a task number!");
            }
            
            int userIndex = Integer.parseInt(parts[1]);
            return userIndex - 1; 
            
        } catch (NumberFormatException e) {
            throw new LyraException("That's not a valid number! Please use digits (e.g., 1, 2, 3).");
        }
    }

    /**
     * Extracts the keyword from the find command.
     */
    public String parseKeyword(String command) throws LyraException {
        String[] parts = command.trim().split(" ", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new LyraException("Please specify a keyword to find!");
        }
        return parts[1].trim();
    }
}