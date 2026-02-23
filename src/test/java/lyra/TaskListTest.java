package lyra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class TaskListTest {
    @Test
    public void removeTask_invalidIndex_exceptionThrown() {
        TaskList tasks = new TaskList();
        try {
            tasks.removeTask(0); // empty list
            fail();
        } catch (LyraException e) {
            assertEquals("I couldn't find a task with that number. Use list to see all your tasks.", e.getMessage());
        }
    }
}
