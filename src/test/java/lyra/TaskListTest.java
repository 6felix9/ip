package lyra;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TaskListTest {
    @Test
    public void removeTask_invalidIndex_exceptionThrown() {
        TaskList tasks = new TaskList();
        try {
            tasks.removeTask(0); // empty list
            fail();
        } catch (LyraException e) {
            assertEquals("Invalid task number!", e.getMessage());
        }
    }

    @Test
    public void findTasks_keywordPresent_tasksFound() {
        TaskList tasks = new TaskList();
        tasks.addTask(new Todo("read book"));
        tasks.addTask(new Todo("return book"));
        tasks.addTask(new Todo("buy bread"));
        
        ArrayList<Task> found = tasks.findTasks("book");
        assertEquals(2, found.size());
        assertEquals("read book", found.get(0).getDescription());
        assertEquals("return book", found.get(1).getDescription());
    }

    @Test
    public void findTasks_keywordAbsent_noTasksFound() {
        TaskList tasks = new TaskList();
        tasks.addTask(new Todo("read book"));
        
        ArrayList<Task> found = tasks.findTasks("water");
        assertEquals(0, found.size());
    }
}