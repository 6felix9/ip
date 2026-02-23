package lyra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TaskListTest {

    private TaskList tasks;

    @BeforeEach
    public void setUp() {
        tasks = new TaskList();
    }

    // -------------------------------------------------------------------------
    // addTask
    // -------------------------------------------------------------------------

    @Test
    public void addTask_singleTask_sizeIncreasesToOne() throws LyraException {
        tasks.addTask(new Todo("read book"));
        assertEquals(1, tasks.getSize());
    }

    @Test
    public void addTask_multipleTasks_sizeCorrect() throws LyraException {
        tasks.addTask(new Todo("read book"));
        tasks.addTask(new Todo("buy groceries"));
        assertEquals(2, tasks.getSize());
    }

    @Test
    public void addTask_duplicate_exceptionThrown() throws LyraException {
        tasks.addTask(new Todo("read book"));
        LyraException e = assertThrows(LyraException.class, () -> tasks.addTask(new Todo("read book")));
        assertEquals("This task already exists in your list!", e.getMessage());
    }

    // -------------------------------------------------------------------------
    // removeTask
    // -------------------------------------------------------------------------

    @Test
    public void removeTask_validIndex_returnsRemovedTask() throws LyraException {
        Todo todo = new Todo("read book");
        tasks.addTask(todo);
        Task removed = tasks.removeTask(0);
        assertEquals(todo, removed);
        assertEquals(0, tasks.getSize());
    }

    @Test
    public void removeTask_invalidIndex_exceptionThrown() {
        LyraException e = assertThrows(LyraException.class, () -> tasks.removeTask(0));
        assertEquals("I couldn't find a task with that number. Use list to see all your tasks.",
                e.getMessage());
    }

    @Test
    public void removeTask_negativeIndex_exceptionThrown() throws LyraException {
        tasks.addTask(new Todo("read book"));
        assertThrows(LyraException.class, () -> tasks.removeTask(-1));
    }

    @Test
    public void removeTask_indexTooLarge_exceptionThrown() throws LyraException {
        tasks.addTask(new Todo("read book"));
        assertThrows(LyraException.class, () -> tasks.removeTask(1));
    }

    // -------------------------------------------------------------------------
    // getTask
    // -------------------------------------------------------------------------

    @Test
    public void getTask_validIndex_returnsCorrectTask() throws LyraException {
        Todo todo = new Todo("read book");
        tasks.addTask(todo);
        assertEquals(todo, tasks.getTask(0));
    }

    @Test
    public void getTask_invalidIndex_exceptionThrown() {
        assertThrows(LyraException.class, () -> tasks.getTask(0));
    }

    // -------------------------------------------------------------------------
    // getSize
    // -------------------------------------------------------------------------

    @Test
    public void getSize_emptyList_returnsZero() {
        assertEquals(0, tasks.getSize());
    }

    @Test
    public void getSize_afterTwoAdds_returnsTwo() throws LyraException {
        tasks.addTask(new Todo("task one"));
        tasks.addTask(new Todo("task two"));
        assertEquals(2, tasks.getSize());
    }

    // -------------------------------------------------------------------------
    // markTask
    // -------------------------------------------------------------------------

    @Test
    public void markTask_validIndex_taskMarkedDone() throws LyraException {
        tasks.addTask(new Todo("read book"));
        Task marked = tasks.markTask(0);
        assertTrue(marked.getIsDone());
    }

    @Test
    public void markTask_invalidIndex_exceptionThrown() {
        assertThrows(LyraException.class, () -> tasks.markTask(0));
    }

    // -------------------------------------------------------------------------
    // unmarkTask
    // -------------------------------------------------------------------------

    @Test
    public void unmarkTask_previouslyMarked_taskNotDone() throws LyraException {
        tasks.addTask(new Todo("read book"));
        tasks.markTask(0);
        Task unmarked = tasks.unmarkTask(0);
        assertFalse(unmarked.getIsDone());
    }

    @Test
    public void unmarkTask_invalidIndex_exceptionThrown() {
        assertThrows(LyraException.class, () -> tasks.unmarkTask(0));
    }

    // -------------------------------------------------------------------------
    // findTasks
    // -------------------------------------------------------------------------

    @Test
    public void findTasks_todoType_returnsOnlyTodos() throws LyraException {
        tasks.addTask(new Todo("read book"));
        tasks.addTask(new Deadline("submit report", LocalDateTime.of(2024, 12, 2, 18, 0)));
        ArrayList<Task> found = tasks.findTasks(TaskType.TODO);
        assertEquals(1, found.size());
        assertEquals(TaskType.TODO, found.get(0).getType());
    }

    @Test
    public void findTasks_deadlineType_returnsOnlyDeadlines() throws LyraException {
        tasks.addTask(new Todo("read book"));
        tasks.addTask(new Deadline("submit report", LocalDateTime.of(2024, 12, 2, 18, 0)));
        ArrayList<Task> found = tasks.findTasks(TaskType.DEADLINE);
        assertEquals(1, found.size());
        assertEquals(TaskType.DEADLINE, found.get(0).getType());
    }

    @Test
    public void findTasks_eventType_returnsOnlyEvents() throws LyraException {
        tasks.addTask(new Todo("read book"));
        tasks.addTask(new Event("team meeting",
                LocalDateTime.of(2024, 12, 2, 10, 0),
                LocalDateTime.of(2024, 12, 2, 12, 0)));
        ArrayList<Task> found = tasks.findTasks(TaskType.EVENT);
        assertEquals(1, found.size());
        assertEquals(TaskType.EVENT, found.get(0).getType());
    }

    @Test
    public void findTasks_noMatch_returnsEmptyList() throws LyraException {
        tasks.addTask(new Todo("read book"));
        ArrayList<Task> found = tasks.findTasks(TaskType.EVENT);
        assertEquals(0, found.size());
    }

    // -------------------------------------------------------------------------
    // updateTask
    // -------------------------------------------------------------------------

    @Test
    public void updateTask_updateDescriptionOnTodo_descriptionChanges() throws LyraException {
        tasks.addTask(new Todo("old title"));
        UpdateCommandData data = new UpdateCommandData(0, "new title");
        Task updated = tasks.updateTask(data);
        assertEquals("new title", updated.getDescription());
    }

    @Test
    public void updateTask_updateByOnDeadline_byChanges() throws LyraException {
        LocalDateTime original = LocalDateTime.of(2024, 12, 2, 18, 0);
        LocalDateTime updated = LocalDateTime.of(2025, 1, 1, 9, 0);
        tasks.addTask(new Deadline("submit report", original));
        UpdateCommandData data = new UpdateCommandData(0, UpdateType.BY, updated);
        Task result = tasks.updateTask(data);
        assertEquals(updated, ((Deadline) result).getBy());
    }

    @Test
    public void updateTask_updateFromOnEvent_fromChanges() throws LyraException {
        tasks.addTask(new Event("meeting",
                LocalDateTime.of(2024, 12, 2, 10, 0),
                LocalDateTime.of(2024, 12, 2, 12, 0)));
        LocalDateTime newFrom = LocalDateTime.of(2024, 12, 2, 9, 0);
        UpdateCommandData data = new UpdateCommandData(0, UpdateType.FROM, newFrom);
        Task result = tasks.updateTask(data);
        assertEquals(newFrom, ((Event) result).getFrom());
    }

    @Test
    public void updateTask_updateToOnEvent_toChanges() throws LyraException {
        tasks.addTask(new Event("meeting",
                LocalDateTime.of(2024, 12, 2, 10, 0),
                LocalDateTime.of(2024, 12, 2, 12, 0)));
        LocalDateTime newTo = LocalDateTime.of(2024, 12, 2, 13, 0);
        UpdateCommandData data = new UpdateCommandData(0, UpdateType.TO, newTo);
        Task result = tasks.updateTask(data);
        assertEquals(newTo, ((Event) result).getTo());
    }

    @Test
    public void updateTask_byOnNonDeadline_exceptionThrown() throws LyraException {
        tasks.addTask(new Todo("read book"));
        UpdateCommandData data = new UpdateCommandData(0, UpdateType.BY,
                LocalDateTime.of(2025, 1, 1, 9, 0));
        LyraException e = assertThrows(LyraException.class, () -> tasks.updateTask(data));
        assertEquals("/by only works with deadline tasks. Check your task type with list.", e.getMessage());
    }

    @Test
    public void updateTask_fromOnNonEvent_exceptionThrown() throws LyraException {
        tasks.addTask(new Todo("read book"));
        UpdateCommandData data = new UpdateCommandData(0, UpdateType.FROM,
                LocalDateTime.of(2025, 1, 1, 9, 0));
        LyraException e = assertThrows(LyraException.class, () -> tasks.updateTask(data));
        assertEquals("/from only works with event tasks. Check your task type with list.", e.getMessage());
    }

    @Test
    public void updateTask_toOnNonEvent_exceptionThrown() throws LyraException {
        tasks.addTask(new Todo("read book"));
        UpdateCommandData data = new UpdateCommandData(0, UpdateType.TO,
                LocalDateTime.of(2025, 1, 1, 9, 0));
        LyraException e = assertThrows(LyraException.class, () -> tasks.updateTask(data));
        assertEquals("/to only works with event tasks. Check your task type with list.", e.getMessage());
    }

    @Test
    public void updateTask_invalidIndex_exceptionThrown() {
        UpdateCommandData data = new UpdateCommandData(5, "new title");
        assertThrows(LyraException.class, () -> tasks.updateTask(data));
    }

    @Test
    public void updateTask_wouldCreateDuplicate_exceptionThrown() throws LyraException {
        tasks.addTask(new Todo("old title"));
        tasks.addTask(new Todo("new title"));
        UpdateCommandData data = new UpdateCommandData(0, "new title");
        LyraException e = assertThrows(LyraException.class, () -> tasks.updateTask(data));
        assertEquals("This task already exists in your list!", e.getMessage());
    }
}
