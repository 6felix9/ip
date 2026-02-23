package lyra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TaskTest {

    // -------------------------------------------------------------------------
    // markDone / unmarkDone / getIsDone
    // -------------------------------------------------------------------------

    @Test
    public void markDone_notDoneTask_taskIsDone() {
        Task task = new Task("read book");
        task.markDone();
        assertTrue(task.getIsDone());
    }

    @Test
    public void unmarkDone_doneTask_taskIsNotDone() {
        Task task = new Task("read book");
        task.markDone();
        task.unmarkDone();
        assertFalse(task.getIsDone());
    }

    @Test
    public void markDone_thenUnmark_cycleCorrect() {
        Task task = new Task("read book");
        assertFalse(task.getIsDone());
        task.markDone();
        assertTrue(task.getIsDone());
        task.unmarkDone();
        assertFalse(task.getIsDone());
    }

    // -------------------------------------------------------------------------
    // getStatusIcon
    // -------------------------------------------------------------------------

    @Test
    public void getStatusIcon_notDone_returnsSpace() {
        Task task = new Task("read book");
        assertEquals(" ", task.getStatusIcon());
    }

    @Test
    public void getStatusIcon_done_returnsX() {
        Task task = new Task("read book");
        task.markDone();
        assertEquals("X", task.getStatusIcon());
    }

    // -------------------------------------------------------------------------
    // getDescription / setDescription
    // -------------------------------------------------------------------------

    @Test
    public void getDescription_returnsCorrectDescription() {
        Task task = new Task("read book");
        assertEquals("read book", task.getDescription());
    }

    @Test
    public void setDescription_updatesDescription() {
        Task task = new Task("read book");
        task.setDescription("buy groceries");
        assertEquals("buy groceries", task.getDescription());
    }

    // -------------------------------------------------------------------------
    // getType
    // -------------------------------------------------------------------------

    @Test
    public void getType_defaultConstructor_returnsTodo() {
        Task task = new Task("read book");
        assertEquals(TaskType.TODO, task.getType());
    }

    @Test
    public void getType_deadlineType_returnsDeadline() {
        Task task = new Task("submit report", TaskType.DEADLINE);
        assertEquals(TaskType.DEADLINE, task.getType());
    }

    @Test
    public void getType_eventType_returnsEvent() {
        Task task = new Task("team meeting", TaskType.EVENT);
        assertEquals(TaskType.EVENT, task.getType());
    }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Test
    public void toString_notDone_formattedCorrectly() {
        Task task = new Task("read book");
        assertEquals("[ ] read book", task.toString());
    }

    @Test
    public void toString_done_formattedCorrectly() {
        Task task = new Task("read book");
        task.markDone();
        assertEquals("[X] read book", task.toString());
    }

    // -------------------------------------------------------------------------
    // toFileString
    // -------------------------------------------------------------------------

    @Test
    public void toFileString_notDone_formattedCorrectly() {
        Task task = new Task("read book");
        assertEquals("T | 0 | read book", task.toFileString());
    }

    @Test
    public void toFileString_done_formattedCorrectly() {
        Task task = new Task("read book");
        task.markDone();
        assertEquals("T | 1 | read book", task.toFileString());
    }

    @Test
    public void toFileString_deadlineType_usesCorrectSymbol() {
        Task task = new Task("submit report", TaskType.DEADLINE);
        assertEquals("D | 0 | submit report", task.toFileString());
    }

    // -------------------------------------------------------------------------
    // equals
    // -------------------------------------------------------------------------

    @Test
    public void equals_sameTypeAndDescription_returnsTrue() {
        Task t1 = new Task("read book");
        Task t2 = new Task("read book");
        assertEquals(t1, t2);
    }

    @Test
    public void equals_differentDescription_returnsFalse() {
        Task t1 = new Task("read book");
        Task t2 = new Task("buy groceries");
        assertNotEquals(t1, t2);
    }

    @Test
    public void equals_differentType_returnsFalse() {
        Task t1 = new Task("read book", TaskType.TODO);
        Task t2 = new Task("read book", TaskType.DEADLINE);
        assertNotEquals(t1, t2);
    }

    @Test
    public void equals_nullObject_returnsFalse() {
        Task task = new Task("read book");
        assertNotEquals(task, null);
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        Task task = new Task("read book");
        assertEquals(task, task);
    }

    @Test
    public void equals_doneStatusDoesNotAffectEquality() {
        Task t1 = new Task("read book");
        Task t2 = new Task("read book");
        t2.markDone();
        assertEquals(t1, t2);
    }

    // -------------------------------------------------------------------------
    // hashCode
    // -------------------------------------------------------------------------

    @Test
    public void hashCode_equalTasks_sameHashCode() {
        Task t1 = new Task("read book");
        Task t2 = new Task("read book");
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    public void hashCode_differentTasks_differentHashCode() {
        Task t1 = new Task("read book");
        Task t2 = new Task("buy groceries");
        assertNotEquals(t1.hashCode(), t2.hashCode());
    }
}
