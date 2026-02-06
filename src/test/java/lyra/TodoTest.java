package lyra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TodoTest {
    @Test
    public void toString_newTodo_formattedCorrectly() {
        Todo todo = new Todo("read book");
        assertEquals("[T][ ] read book", todo.toString());
    }

    @Test
    public void toString_markedTodo_formattedCorrectly() {
        Todo todo = new Todo("read book");
        todo.markDone();
        assertEquals("[T][X] read book", todo.toString());
    }

    @Test
    public void toFileString_markedTodo_formattedCorrectly() {
        Todo todo = new Todo("read book");
        todo.markDone();
        assertEquals("T | 1 | read book", todo.toFileString());
    }
}
