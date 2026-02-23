package lyra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

public class EventTest {

    private static final DateTimeFormatter DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("MMM d yyyy, h:mma");

    private static final LocalDateTime FROM = LocalDateTime.of(2024, 12, 2, 10, 0);
    private static final LocalDateTime TO = LocalDateTime.of(2024, 12, 2, 12, 0);
    private static final LocalDateTime EARLIER = LocalDateTime.of(2024, 12, 2, 8, 0);
    private static final LocalDateTime LATER = LocalDateTime.of(2024, 12, 2, 14, 0);

    // -------------------------------------------------------------------------
    // Constructor validation
    // -------------------------------------------------------------------------

    @Test
    public void constructor_validTimes_success() throws LyraException {
        Event event = new Event("meeting", FROM, TO);
        assertEquals("meeting", event.getDescription());
        assertEquals(FROM, event.getFrom());
        assertEquals(TO, event.getTo());
    }

    @Test
    public void constructor_startAfterEnd_exceptionThrown() {
        LyraException e = assertThrows(LyraException.class,
                () -> new Event("meeting", TO, FROM));
        assertEquals("Start time must be before end time.", e.getMessage());
    }

    @Test
    public void constructor_startEqualsEnd_exceptionThrown() {
        assertThrows(LyraException.class, () -> new Event("meeting", FROM, FROM));
    }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Test
    public void toString_notDone_formattedCorrectly() throws LyraException {
        Event event = new Event("team meeting", FROM, TO);
        String expected = "[E][ ] team meeting (from: "
                + FROM.format(DISPLAY_FORMATTER) + " to: "
                + TO.format(DISPLAY_FORMATTER) + ")";
        assertEquals(expected, event.toString());
    }

    @Test
    public void toString_done_formattedCorrectly() throws LyraException {
        Event event = new Event("team meeting", FROM, TO);
        event.markDone();
        String expected = "[E][X] team meeting (from: "
                + FROM.format(DISPLAY_FORMATTER) + " to: "
                + TO.format(DISPLAY_FORMATTER) + ")";
        assertEquals(expected, event.toString());
    }

    // -------------------------------------------------------------------------
    // toFileString
    // -------------------------------------------------------------------------

    @Test
    public void toFileString_notDone_formattedCorrectly() throws LyraException {
        Event event = new Event("team meeting", FROM, TO);
        assertEquals("E | 0 | team meeting | 2/12/2024 1000 | 2/12/2024 1200",
                event.toFileString());
    }

    @Test
    public void toFileString_done_formattedCorrectly() throws LyraException {
        Event event = new Event("team meeting", FROM, TO);
        event.markDone();
        assertEquals("E | 1 | team meeting | 2/12/2024 1000 | 2/12/2024 1200",
                event.toFileString());
    }

    // -------------------------------------------------------------------------
    // getFrom / getTo
    // -------------------------------------------------------------------------

    @Test
    public void getFrom_returnsCorrectDateTime() throws LyraException {
        Event event = new Event("meeting", FROM, TO);
        assertEquals(FROM, event.getFrom());
    }

    @Test
    public void getTo_returnsCorrectDateTime() throws LyraException {
        Event event = new Event("meeting", FROM, TO);
        assertEquals(TO, event.getTo());
    }

    // -------------------------------------------------------------------------
    // setFrom
    // -------------------------------------------------------------------------

    @Test
    public void setFrom_validTime_updatesFrom() throws LyraException {
        Event event = new Event("meeting", FROM, TO);
        event.setFrom(EARLIER);
        assertEquals(EARLIER, event.getFrom());
    }

    @Test
    public void setFrom_equalToEnd_exceptionThrown() throws LyraException {
        Event event = new Event("meeting", FROM, TO);
        assertThrows(LyraException.class, () -> event.setFrom(TO));
    }

    @Test
    public void setFrom_afterEnd_exceptionThrown() throws LyraException {
        Event event = new Event("meeting", FROM, TO);
        assertThrows(LyraException.class, () -> event.setFrom(LATER));
    }

    // -------------------------------------------------------------------------
    // setTo
    // -------------------------------------------------------------------------

    @Test
    public void setTo_validTime_updatesTo() throws LyraException {
        Event event = new Event("meeting", FROM, TO);
        event.setTo(LATER);
        assertEquals(LATER, event.getTo());
    }

    @Test
    public void setTo_equalToStart_exceptionThrown() throws LyraException {
        Event event = new Event("meeting", FROM, TO);
        assertThrows(LyraException.class, () -> event.setTo(FROM));
    }

    @Test
    public void setTo_beforeStart_exceptionThrown() throws LyraException {
        Event event = new Event("meeting", FROM, TO);
        assertThrows(LyraException.class, () -> event.setTo(EARLIER));
    }

    // -------------------------------------------------------------------------
    // getType
    // -------------------------------------------------------------------------

    @Test
    public void getType_event_returnsEventType() throws LyraException {
        Event event = new Event("meeting", FROM, TO);
        assertEquals(TaskType.EVENT, event.getType());
    }

    // -------------------------------------------------------------------------
    // equals / hashCode
    // -------------------------------------------------------------------------

    @Test
    public void equals_sameDescriptionAndTimes_returnsTrue() throws LyraException {
        Event e1 = new Event("meeting", FROM, TO);
        Event e2 = new Event("meeting", FROM, TO);
        assertEquals(e1, e2);
    }

    @Test
    public void equals_differentFrom_returnsFalse() throws LyraException {
        Event e1 = new Event("meeting", FROM, TO);
        Event e2 = new Event("meeting", EARLIER, TO);
        assertNotEquals(e1, e2);
    }

    @Test
    public void equals_differentTo_returnsFalse() throws LyraException {
        Event e1 = new Event("meeting", FROM, TO);
        Event e2 = new Event("meeting", FROM, LATER);
        assertNotEquals(e1, e2);
    }

    @Test
    public void equals_differentDescription_returnsFalse() throws LyraException {
        Event e1 = new Event("meeting", FROM, TO);
        Event e2 = new Event("workshop", FROM, TO);
        assertNotEquals(e1, e2);
    }

    @Test
    public void equals_nullObject_returnsFalse() throws LyraException {
        Event event = new Event("meeting", FROM, TO);
        assertNotEquals(event, null);
    }

    @Test
    public void equals_differentType_returnsFalse() throws LyraException {
        Event event = new Event("meeting", FROM, TO);
        Todo todo = new Todo("meeting");
        assertNotEquals(event, todo);
    }

    @Test
    public void hashCode_equalEvents_sameHashCode() throws LyraException {
        Event e1 = new Event("meeting", FROM, TO);
        Event e2 = new Event("meeting", FROM, TO);
        assertEquals(e1.hashCode(), e2.hashCode());
    }
}
