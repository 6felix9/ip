package lyra;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Storage {
    private static final String DONE_MARKER = "1";

    private File dataFile;

    /**
     * Parse the date string into a Date object.
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

    public Storage(String filePath) {
        this.dataFile = new File(filePath);
    }

    /**
     * Loads tasks from the data file.
     */
    public ArrayList<Task> loadTasks() throws LyraException {
        try {
            Scanner fileScanner = new Scanner(dataFile);
            ArrayList<Task> tasks = new ArrayList<>();
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(" \\| ");
                
                switch (parts[0]) {
                    case "T":
                        tasks.add(new Todo(parts[2]));
                        if (parts[1].equals(DONE_MARKER)) {
                            tasks.get(tasks.size() - 1).markDone();
                        }
                        break;
                    case "D":
                        tasks.add(new Deadline(parts[2], parseDateTime(parts[3])));
                        if (parts[1].equals(DONE_MARKER)) {
                            tasks.get(tasks.size() - 1).markDone();
                        }
                        break;
                    case "E":
                        tasks.add(new Event(parts[2], parseDateTime(parts[3]), parseDateTime(parts[4])));
                        if (parts[1].equals(DONE_MARKER)) {
                            tasks.get(tasks.size() - 1).markDone();
                        }
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
