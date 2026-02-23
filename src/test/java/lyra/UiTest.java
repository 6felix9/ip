package lyra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UiTest {

    private Ui ui;

    @BeforeEach
    public void setUp() {
        ui = new Ui();
    }

    // -------------------------------------------------------------------------
    // getWelcomeMessage
    // -------------------------------------------------------------------------

    @Test
    public void getWelcomeMessage_returnsExpectedString() {
        assertEquals("Hi there! I'm Lyra, your personal task helper.\nWhat would you like to get done today?",
                ui.getWelcomeMessage());
    }

    // -------------------------------------------------------------------------
    // getGoodbyeMessage
    // -------------------------------------------------------------------------

    @Test
    public void getGoodbyeMessage_returnsExpectedString() {
        assertEquals("Take care! I'm here whenever you need me.", ui.getGoodbyeMessage());
    }

    // -------------------------------------------------------------------------
    // getErrorMessage
    // -------------------------------------------------------------------------

    @Test
    public void getErrorMessage_prefixesWithOops() {
        assertEquals("Oops! something went wrong", ui.getErrorMessage("something went wrong"));
    }

    @Test
    public void getErrorMessage_emptyMessage_stillPrefixed() {
        assertEquals("Oops! ", ui.getErrorMessage(""));
    }

    // -------------------------------------------------------------------------
    // getMarkedMessage
    // -------------------------------------------------------------------------

    @Test
    public void getMarkedMessage_containsTaskString() {
        Todo task = new Todo("read book");
        task.markDone();
        String message = ui.getMarkedMessage(task);
        assertTrue(message.contains(task.toString()),
                "Marked message should contain task string");
    }

    @Test
    public void getMarkedMessage_startsWithExpectedPrefix() {
        Todo task = new Todo("read book");
        String message = ui.getMarkedMessage(task);
        assertTrue(message.startsWith("Well done!"),
                "Marked message should start with 'Well done!'");
    }

    // -------------------------------------------------------------------------
    // getUnmarkedMessage
    // -------------------------------------------------------------------------

    @Test
    public void getUnmarkedMessage_containsTaskString() {
        Todo task = new Todo("read book");
        String message = ui.getUnmarkedMessage(task);
        assertTrue(message.contains(task.toString()));
    }

    @Test
    public void getUnmarkedMessage_startsWithExpectedPrefix() {
        Todo task = new Todo("read book");
        String message = ui.getUnmarkedMessage(task);
        assertTrue(message.startsWith("No worries"));
    }

    // -------------------------------------------------------------------------
    // getAddedTaskMessage
    // -------------------------------------------------------------------------

    @Test
    public void getAddedTaskMessage_containsTaskString() {
        Todo task = new Todo("read book");
        String message = ui.getAddedTaskMessage(task);
        assertTrue(message.contains(task.toString()));
    }

    @Test
    public void getAddedTaskMessage_startsWithAdded() {
        Todo task = new Todo("read book");
        String message = ui.getAddedTaskMessage(task);
        assertTrue(message.startsWith("Added!"));
    }

    // -------------------------------------------------------------------------
    // getRemovedTaskMessage
    // -------------------------------------------------------------------------

    @Test
    public void getRemovedTaskMessage_containsTaskString() {
        Todo task = new Todo("read book");
        String message = ui.getRemovedTaskMessage(task);
        assertTrue(message.contains(task.toString()));
    }

    @Test
    public void getRemovedTaskMessage_startsWithExpectedPrefix() {
        Todo task = new Todo("read book");
        String message = ui.getRemovedTaskMessage(task);
        assertTrue(message.startsWith("Done —"));
    }

    // -------------------------------------------------------------------------
    // getUpdatedTaskMessage
    // -------------------------------------------------------------------------

    @Test
    public void getUpdatedTaskMessage_containsTaskString() {
        Todo task = new Todo("updated task");
        String message = ui.getUpdatedTaskMessage(task);
        assertTrue(message.contains(task.toString()));
    }

    @Test
    public void getUpdatedTaskMessage_startsWithExpectedPrefix() {
        Todo task = new Todo("updated task");
        String message = ui.getUpdatedTaskMessage(task);
        assertTrue(message.startsWith("All set!"));
    }

    // -------------------------------------------------------------------------
    // getFoundTasksMessage
    // -------------------------------------------------------------------------

    @Test
    public void getFoundTasksMessage_emptyList_returnsNoResultsMessage() {
        String message = ui.getFoundTasksMessage(new ArrayList<>());
        assertEquals("I couldn't find any tasks of that type. Try a different type?", message);
    }

    @Test
    public void getFoundTasksMessage_oneTask_containsNumberedEntry() {
        ArrayList<Task> tasks = new ArrayList<>();
        Todo task = new Todo("read book");
        tasks.add(task);
        String message = ui.getFoundTasksMessage(tasks);
        assertTrue(message.contains("1. " + task.toString()));
    }

    @Test
    public void getFoundTasksMessage_multipleTasksNumberedCorrectly() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("task one"));
        tasks.add(new Todo("task two"));
        String message = ui.getFoundTasksMessage(tasks);
        assertTrue(message.contains("1. "));
        assertTrue(message.contains("2. "));
    }

    // -------------------------------------------------------------------------
    // getAllTasksMessage
    // -------------------------------------------------------------------------

    @Test
    public void getAllTasksMessage_emptyTaskList_returnsEmptyMessage() throws LyraException {
        String message = ui.getAllTasksMessage(new TaskList());
        assertEquals("Your list is clear right now — you're all caught up!", message);
    }

    @Test
    public void getAllTasksMessage_oneTask_containsNumberedEntry() throws LyraException {
        TaskList taskList = new TaskList();
        Todo task = new Todo("read book");
        taskList.addTask(task);
        String message = ui.getAllTasksMessage(taskList);
        assertTrue(message.contains("1. " + task.toString()));
    }

    @Test
    public void getAllTasksMessage_multipleTasksNumberedCorrectly() throws LyraException {
        TaskList taskList = new TaskList();
        taskList.addTask(new Todo("task one"));
        taskList.addTask(new Todo("task two"));
        String message = ui.getAllTasksMessage(taskList);
        assertTrue(message.contains("1. "));
        assertTrue(message.contains("2. "));
    }
}
