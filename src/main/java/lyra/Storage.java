package lyra;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles loading and saving tasks to a file.
 */
public class Storage {
    private static final String DONE_MARKER = "1";
    private static final String TASK_TYPE_TODO = "T";
    private static final String TASK_TYPE_DEADLINE = "D";
    private static final String TASK_TYPE_EVENT = "E";

    private File dataFile;

    /**
     * Creates a Storage object with the specified file path.
     *
     * @param filePath The path to the data file
     */
    public Storage(String filePath) {
        this.dataFile = new File(filePath);
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
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/uuuu HHmm")
                    .withResolverStyle(ResolverStyle.STRICT);
            return LocalDateTime.parse(input, formatter);
        } catch (DateTimeParseException e) {
            throw new LyraException("That date format doesn't look right. Please use d/MM/yyyy HHmm "
                    + "— for example: 2/12/2024 1800.");
        }
    }

    /**
     * Loads tasks from the data file.
     *
     * @return The list of tasks loaded from the file
     * @throws LyraException If the file is not found or contains invalid data
     */
    public ArrayList<Task> loadTasks() throws LyraException {
        try (Scanner fileScanner = new Scanner(dataFile)) {
            ArrayList<Task> tasks = new ArrayList<>();
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split(" \\| ");
                if (parts.length < 2) {
                    throw new LyraException("Corrupted data file: invalid line format.");
                }
                Task task = createTaskFromParts(parts);
                markTaskIfDone(task, parts[1]);
                tasks.add(task);
            }
            return tasks;
        } catch (FileNotFoundException e) {
            throw new LyraException("Data file not found!");
        }
    }

    /**
     * Creates a task from parsed file line parts.
     *
     * @param parts The split line parts
     * @return The created task
     * @throws LyraException If the task type is invalid
     */
    private Task createTaskFromParts(String[] parts) throws LyraException {
        switch (parts[0]) {
        case TASK_TYPE_TODO:
            if (parts.length < 3) {
                throw new LyraException("Corrupted data file: todo task has invalid format.");
            }
            return new Todo(parts[2]);
        case TASK_TYPE_DEADLINE:
            if (parts.length < 4) {
                throw new LyraException("Corrupted data file: deadline task has invalid format.");
            }
            return new Deadline(parts[2], parseDateTime(parts[3]));
        case TASK_TYPE_EVENT:
            if (parts.length < 5) {
                throw new LyraException("Corrupted data file: event task has invalid format.");
            }
            return new Event(parts[2], parseDateTime(parts[3]), parseDateTime(parts[4]));
        default:
            throw new LyraException("I don't recognise that task type in your data file. "
                    + "Please use one of: todo, deadline, or event.");
        }
    }

    /**
     * Marks the task as done if the status marker indicates it.
     *
     * @param task The task to potentially mark
     * @param statusMarker The status marker from the file ("1" or "0")
     */
    private void markTaskIfDone(Task task, String statusMarker) {
        if (statusMarker.equals(DONE_MARKER)) {
            task.markDone();
        }
    }
    /**
     * Saves tasks to the data file.
     *
     * @param tasks The list of tasks to save
     * @throws LyraException If unable to write to the file
     */
    public void saveTasks(ArrayList<Task> tasks) throws LyraException {
        try {
            File parent = dataFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            try (PrintWriter fileWriter = new PrintWriter(dataFile)) {
                for (Task task : tasks) {
                    fileWriter.println(task.toFileString());
                }
            }
        } catch (FileNotFoundException e) {
            throw new LyraException("Unable to write to data file. Check that the path is valid.");
        } catch (SecurityException e) {
            throw new LyraException("Access to the data file was denied. Check your permissions.");
        }
    }
}
