package lyra;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("d/MM/yyyy HHmm");
            return LocalDateTime.parse(input, formatter);
        } catch (DateTimeParseException e) {
            throw new LyraException("Invalid format. Use: d/MM/yyyy HHmm (e.g., 2/12/2024 1800)");
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
                String[] parts = line.split(" \\| ");
                // Assertion: File format assumes at least task type and status marker
                assert parts.length >= 2 : "File line must have at least task type and status";
                int sizeBeforeAdd = tasks.size();
                Task task = createTaskFromParts(parts);
                markTaskIfDone(task, parts[1]);
                tasks.add(task);
                // Assertion: Task was successfully added to list (size should increase by 1)
                assert tasks.size() == sizeBeforeAdd + 1 : "Task should be added to list";
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
            // Assertion: Todo format assumes: T | status | description
            assert parts.length >= 3 : "Todo line must have type, status, and description";
            return new Todo(parts[2]);
        case TASK_TYPE_DEADLINE:
            // Assertion: Deadline format assumes: D | status | description | datetime
            assert parts.length >= 4 : "Deadline line must have type, status, description, and datetime";
            return new Deadline(parts[2], parseDateTime(parts[3]));
        case TASK_TYPE_EVENT:
            // Assertion: Event format assumes: E | status | description | start | end
            assert parts.length >= 5
                    : "Event line must have type, status, description, start, and end datetime";
            return new Event(parts[2], parseDateTime(parts[3]), parseDateTime(parts[4]));
        default:
            throw new LyraException("Invalid task type in file.");
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
        try (PrintWriter fileWriter = new PrintWriter(dataFile)) {
            for (Task task : tasks) {
                fileWriter.println(task.toFileString());
            }
        } catch (FileNotFoundException e) {
            throw new LyraException("Unable to write to data file.");
        }
    }
}
