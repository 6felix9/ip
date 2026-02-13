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
        try {
            Scanner fileScanner = new Scanner(dataFile);
            ArrayList<Task> tasks = new ArrayList<>();
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(" \\| ");

                // Assertion: File format assumes at least task type and status marker
                assert parts.length >= 2 : "File line must have at least task type and status";

                switch (parts[0]) {
                case "T":
                    // Assertion: Todo format assumes: T | status | description
                    assert parts.length >= 3 : "Todo line must have type, status, and description";
                    tasks.add(new Todo(parts[2]));
                    if (parts[1].equals(DONE_MARKER)) {
                        tasks.get(tasks.size() - 1).markDone();
                    }
                    // Assertion: Task was successfully added to list
                    assert tasks.size() > 0 : "Task should be added to list";
                    break;
                case "D":
                    // Assertion: Deadline format assumes: D | status | description | datetime
                    assert parts.length >= 4 : "Deadline line must have type, status, description, and datetime";
                    tasks.add(new Deadline(parts[2], parseDateTime(parts[3])));
                    if (parts[1].equals(DONE_MARKER)) {
                        tasks.get(tasks.size() - 1).markDone();
                    }
                    // Assertion: Task was successfully added to list
                    assert tasks.size() > 0 : "Task should be added to list";
                    break;
                case "E":
                    // Assertion: Event format assumes: E | status | description | start | end
                    assert parts.length >= 5 : "Event line must have type, status, description, start, and end datetime";
                    tasks.add(new Event(parts[2], parseDateTime(parts[3]), parseDateTime(parts[4])));
                    if (parts[1].equals(DONE_MARKER)) {
                        tasks.get(tasks.size() - 1).markDone();
                    }
                    // Assertion: Task was successfully added to list
                    assert tasks.size() > 0 : "Task should be added to list";
                    break;
                default:
                    throw new LyraException("Invalid task type in file.");
                }

            }
            fileScanner.close();
            return tasks;
        } catch (FileNotFoundException e) {
            throw new LyraException("Data file not found!");
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
            PrintWriter fileWriter = new PrintWriter(dataFile);

            for (Task task : tasks) {
                fileWriter.println(task.toFileString());
            }
            fileWriter.close();
        } catch (FileNotFoundException e) {
            throw new LyraException("Unable to write to data file.");
        }
    }
}
