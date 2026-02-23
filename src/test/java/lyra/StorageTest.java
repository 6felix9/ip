package lyra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class StorageTest {

    @TempDir
    Path tempDir;

    private Storage storageFor(String filename) {
        return new Storage(tempDir.resolve(filename).toString());
    }

    private File fileFor(String filename) {
        return tempDir.resolve(filename).toFile();
    }

    // -------------------------------------------------------------------------
    // saveTasks / loadTasks round-trips
    // -------------------------------------------------------------------------

    @Test
    public void saveAndLoad_singleTodoNotDone_restoredCorrectly() throws LyraException {
        Storage storage = storageFor("tasks.txt");
        ArrayList<Task> original = new ArrayList<>();
        original.add(new Todo("read book"));

        storage.saveTasks(original);
        ArrayList<Task> loaded = storage.loadTasks();

        assertEquals(1, loaded.size());
        assertEquals(TaskType.TODO, loaded.get(0).getType());
        assertEquals("read book", loaded.get(0).getDescription());
        assertFalse(loaded.get(0).getIsDone());
    }

    @Test
    public void saveAndLoad_todoMarkedDone_restoredWithDoneStatus() throws LyraException {
        Storage storage = storageFor("tasks.txt");
        ArrayList<Task> original = new ArrayList<>();
        Todo todo = new Todo("read book");
        todo.markDone();
        original.add(todo);

        storage.saveTasks(original);
        ArrayList<Task> loaded = storage.loadTasks();

        assertEquals(1, loaded.size());
        assertTrue(loaded.get(0).getIsDone());
    }

    @Test
    public void saveAndLoad_deadline_restoredCorrectly() throws LyraException {
        Storage storage = storageFor("tasks.txt");
        LocalDateTime by = LocalDateTime.of(2024, 12, 2, 18, 0);
        ArrayList<Task> original = new ArrayList<>();
        original.add(new Deadline("submit report", by));

        storage.saveTasks(original);
        ArrayList<Task> loaded = storage.loadTasks();

        assertEquals(1, loaded.size());
        assertEquals(TaskType.DEADLINE, loaded.get(0).getType());
        assertEquals("submit report", loaded.get(0).getDescription());
        assertEquals(by, ((Deadline) loaded.get(0)).getBy());
    }

    @Test
    public void saveAndLoad_event_restoredCorrectly() throws LyraException, Exception {
        Storage storage = storageFor("tasks.txt");
        LocalDateTime from = LocalDateTime.of(2024, 12, 2, 10, 0);
        LocalDateTime to = LocalDateTime.of(2024, 12, 2, 12, 0);
        ArrayList<Task> original = new ArrayList<>();
        original.add(new Event("team meeting", from, to));

        storage.saveTasks(original);
        ArrayList<Task> loaded = storage.loadTasks();

        assertEquals(1, loaded.size());
        assertEquals(TaskType.EVENT, loaded.get(0).getType());
        assertEquals("team meeting", loaded.get(0).getDescription());
        assertEquals(from, ((Event) loaded.get(0)).getFrom());
        assertEquals(to, ((Event) loaded.get(0)).getTo());
    }

    @Test
    public void saveAndLoad_multipleTasks_allRestoredInOrder() throws LyraException, Exception {
        Storage storage = storageFor("tasks.txt");
        ArrayList<Task> original = new ArrayList<>();
        original.add(new Todo("task one"));
        original.add(new Deadline("task two", LocalDateTime.of(2025, 1, 1, 9, 0)));
        original.add(new Event("task three",
                LocalDateTime.of(2025, 6, 1, 10, 0),
                LocalDateTime.of(2025, 6, 1, 12, 0)));

        storage.saveTasks(original);
        ArrayList<Task> loaded = storage.loadTasks();

        assertEquals(3, loaded.size());
        assertEquals("task one", loaded.get(0).getDescription());
        assertEquals("task two", loaded.get(1).getDescription());
        assertEquals("task three", loaded.get(2).getDescription());
    }

    @Test
    public void saveAndLoad_emptyList_loadsEmpty() throws LyraException {
        Storage storage = storageFor("tasks.txt");
        storage.saveTasks(new ArrayList<>());
        ArrayList<Task> loaded = storage.loadTasks();
        assertEquals(0, loaded.size());
    }

    // -------------------------------------------------------------------------
    // loadTasks error cases
    // -------------------------------------------------------------------------

    @Test
    public void loadTasks_fileNotFound_exceptionThrown() {
        Storage storage = new Storage(tempDir.resolve("nonexistent.txt").toString());
        assertThrows(LyraException.class, storage::loadTasks);
    }

    @Test
    public void loadTasks_corruptedLine_exceptionThrown() throws IOException {
        File file = fileFor("corrupt.txt");
        try (PrintWriter pw = new PrintWriter(file)) {
            pw.println("this line has no pipe separators");
        }
        Storage storage = new Storage(file.getAbsolutePath());
        assertThrows(LyraException.class, storage::loadTasks);
    }

    @Test
    public void loadTasks_unknownTaskType_exceptionThrown() throws IOException {
        File file = fileFor("unknown.txt");
        try (PrintWriter pw = new PrintWriter(file)) {
            pw.println("X | 0 | mystery task");
        }
        Storage storage = new Storage(file.getAbsolutePath());
        assertThrows(LyraException.class, storage::loadTasks);
    }

    @Test
    public void loadTasks_todoMissingDescription_exceptionThrown() throws IOException {
        File file = fileFor("todo_missing.txt");
        try (PrintWriter pw = new PrintWriter(file)) {
            pw.println("T | 0");
        }
        Storage storage = new Storage(file.getAbsolutePath());
        assertThrows(LyraException.class, storage::loadTasks);
    }

    @Test
    public void loadTasks_deadlineMissingDate_exceptionThrown() throws IOException {
        File file = fileFor("deadline_missing.txt");
        try (PrintWriter pw = new PrintWriter(file)) {
            pw.println("D | 0 | submit report");
        }
        Storage storage = new Storage(file.getAbsolutePath());
        assertThrows(LyraException.class, storage::loadTasks);
    }

    @Test
    public void loadTasks_eventMissingToDate_exceptionThrown() throws IOException {
        File file = fileFor("event_missing.txt");
        try (PrintWriter pw = new PrintWriter(file)) {
            pw.println("E | 0 | meeting | 2/12/2024 1000");
        }
        Storage storage = new Storage(file.getAbsolutePath());
        assertThrows(LyraException.class, storage::loadTasks);
    }

    @Test
    public void loadTasks_blankLinesSkipped() throws IOException, LyraException {
        File file = fileFor("blanks.txt");
        try (PrintWriter pw = new PrintWriter(file)) {
            pw.println("T | 0 | read book");
            pw.println("");
            pw.println("   ");
            pw.println("T | 0 | buy groceries");
        }
        Storage storage = new Storage(file.getAbsolutePath());
        ArrayList<Task> loaded = storage.loadTasks();
        assertEquals(2, loaded.size());
    }

    @Test
    public void loadTasks_doneStatusRestoredCorrectly() throws IOException, LyraException {
        File file = fileFor("done.txt");
        try (PrintWriter pw = new PrintWriter(file)) {
            pw.println("T | 1 | read book");
            pw.println("T | 0 | buy groceries");
        }
        Storage storage = new Storage(file.getAbsolutePath());
        ArrayList<Task> loaded = storage.loadTasks();
        assertTrue(loaded.get(0).getIsDone());
        assertFalse(loaded.get(1).getIsDone());
    }

    // -------------------------------------------------------------------------
    // saveTasks creates parent directories
    // -------------------------------------------------------------------------

    @Test
    public void saveTasks_nestedDirectory_createsParentAndSaves() throws LyraException {
        Storage storage = new Storage(tempDir.resolve("nested/dir/tasks.txt").toString());
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("read book"));
        storage.saveTasks(tasks);

        ArrayList<Task> loaded = storage.loadTasks();
        assertEquals(1, loaded.size());
        assertEquals("read book", loaded.get(0).getDescription());
    }
}
