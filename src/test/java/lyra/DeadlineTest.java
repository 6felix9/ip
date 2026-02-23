package lyra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

public class DeadlineTest {

    private static final DateTimeFormatter DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("MMM d yyyy, h:mma");

    private static final LocalDateTime DEC_2_1800 = LocalDateTime.of(2024, 12, 2, 18, 0);
    private static final LocalDateTime JAN_1_0900 = LocalDateTime.of(2025, 1, 1, 9, 0);

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Test
    public void toString_notDone_formattedCorrectly() {
        Deadline deadline = new Deadline("return book", DEC_2_1800);
        String expected = "[D][ ] return book (by: " + DEC_2_1800.format(DISPLAY_FORMATTER) + ")";
        assertEquals(expected, deadline.toString());
    }

    @Test
    public void toString_done_formattedCorrectly() {
        Deadline deadline = new Deadline("return book", DEC_2_1800);
        deadline.markDone();
        String expected = "[D][X] return book (by: " + DEC_2_1800.format(DISPLAY_FORMATTER) + ")";
        assertEquals(expected, deadline.toString());
    }

    // -------------------------------------------------------------------------
    // toFileString
    // -------------------------------------------------------------------------

    @Test
    public void toFileString_notDone_formattedCorrectly() {
        Deadline deadline = new Deadline("return book", DEC_2_1800);
        assertEquals("D | 0 | return book | 2/12/2024 1800", deadline.toFileString());
    }

    @Test
    public void toFileString_done_formattedCorrectly() {
        Deadline deadline = new Deadline("return book", DEC_2_1800);
        deadline.markDone();
        assertEquals("D | 1 | return book | 2/12/2024 1800", deadline.toFileString());
    }

    // -------------------------------------------------------------------------
    // getBy / setBy
    // -------------------------------------------------------------------------

    @Test
    public void getBy_returnsCorrectDateTime() {
        Deadline deadline = new Deadline("return book", DEC_2_1800);
        assertEquals(DEC_2_1800, deadline.getBy());
    }

    @Test
    public void setBy_updatesDeadlineDate() {
        Deadline deadline = new Deadline("return book", DEC_2_1800);
        deadline.setBy(JAN_1_0900);
        assertEquals(JAN_1_0900, deadline.getBy());
    }

    // -------------------------------------------------------------------------
    // getType / getDescription
    // -------------------------------------------------------------------------

    @Test
    public void getType_deadline_returnsDeadlineType() {
        Deadline deadline = new Deadline("return book", DEC_2_1800);
        assertEquals(TaskType.DEADLINE, deadline.getType());
    }

    @Test
    public void getDescription_returnsCorrectDescription() {
        Deadline deadline = new Deadline("return book", DEC_2_1800);
        assertEquals("return book", deadline.getDescription());
    }

    // -------------------------------------------------------------------------
    // equals / hashCode
    // -------------------------------------------------------------------------

    @Test
    public void equals_sameDescriptionAndDate_returnsTrue() {
        Deadline d1 = new Deadline("return book", DEC_2_1800);
        Deadline d2 = new Deadline("return book", DEC_2_1800);
        assertEquals(d1, d2);
    }

    @Test
    public void equals_differentDate_returnsFalse() {
        Deadline d1 = new Deadline("return book", DEC_2_1800);
        Deadline d2 = new Deadline("return book", JAN_1_0900);
        assertNotEquals(d1, d2);
    }

    @Test
    public void equals_differentDescription_returnsFalse() {
        Deadline d1 = new Deadline("return book", DEC_2_1800);
        Deadline d2 = new Deadline("submit report", DEC_2_1800);
        assertNotEquals(d1, d2);
    }

    @Test
    public void equals_nullObject_returnsFalse() {
        Deadline d = new Deadline("return book", DEC_2_1800);
        assertNotEquals(d, null);
    }

    @Test
    public void equals_differentType_returnsFalse() {
        Deadline deadline = new Deadline("return book", DEC_2_1800);
        Todo todo = new Todo("return book");
        assertNotEquals(deadline, todo);
    }

    @Test
    public void hashCode_equalDeadlines_sameHashCode() {
        Deadline d1 = new Deadline("return book", DEC_2_1800);
        Deadline d2 = new Deadline("return book", DEC_2_1800);
        assertEquals(d1.hashCode(), d2.hashCode());
    }
}
