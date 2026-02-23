package lyra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ParserTest {

    private Parser parser;

    @BeforeEach
    public void setUp() {
        parser = new Parser();
    }

    // -------------------------------------------------------------------------
    // getCommand
    // -------------------------------------------------------------------------

    @Test
    public void getCommand_validCommand_success() throws LyraException {
        assertEquals(Command.BYE, parser.getCommand("bye"));
        assertEquals(Command.LIST, parser.getCommand("list"));
    }

    @Test
    public void getCommand_allValidCommands_success() throws LyraException {
        assertEquals(Command.TODO, parser.getCommand("todo buy milk"));
        assertEquals(Command.DEADLINE, parser.getCommand("deadline submit /by 2/12/2024 1800"));
        assertEquals(Command.EVENT, parser.getCommand("event meeting /from 1/1/2025 1000 /to 1/1/2025 1200"));
        assertEquals(Command.MARK, parser.getCommand("mark 1"));
        assertEquals(Command.UNMARK, parser.getCommand("unmark 1"));
        assertEquals(Command.DELETE, parser.getCommand("delete 1"));
        assertEquals(Command.FIND, parser.getCommand("find todo"));
        assertEquals(Command.UPDATE, parser.getCommand("update 1 /description new title"));
    }

    @Test
    public void getCommand_caseInsensitive_success() throws LyraException {
        assertEquals(Command.BYE, parser.getCommand("BYE"));
        assertEquals(Command.LIST, parser.getCommand("LIST"));
        assertEquals(Command.TODO, parser.getCommand("TODO something"));
    }

    @Test
    public void getCommand_invalidCommand_exceptionThrown() {
        LyraException e = assertThrows(LyraException.class, () -> parser.getCommand("invalid"));
        assertEquals("I'm not sure what you mean. Try: todo, deadline, event, list, "
                + "mark, unmark, delete, find, update, or bye.", e.getMessage());
    }

    @Test
    public void getCommand_emptyWord_exceptionThrown() {
        assertThrows(LyraException.class, () -> parser.getCommand("123abc"));
    }

    // -------------------------------------------------------------------------
    // parseTodo
    // -------------------------------------------------------------------------

    @Test
    public void parseTodo_validInput_success() throws LyraException {
        Todo todo = parser.parseTodo("todo read book");
        assertEquals("read book", todo.getDescription());
        assertEquals(TaskType.TODO, todo.getType());
    }

    @Test
    public void parseTodo_multiWordDescription_success() throws LyraException {
        Todo todo = parser.parseTodo("todo buy groceries and cook dinner");
        assertEquals("buy groceries and cook dinner", todo.getDescription());
    }

    @Test
    public void parseTodo_noDescription_exceptionThrown() {
        LyraException e = assertThrows(LyraException.class, () -> parser.parseTodo("todo"));
        assertEquals("A todo needs a description. Try: todo your task.", e.getMessage());
    }

    @Test
    public void parseTodo_whitespaceOnlyDescription_exceptionThrown() {
        assertThrows(LyraException.class, () -> parser.parseTodo("todo    "));
    }

    @Test
    public void parseTodo_pipeCharacter_exceptionThrown() {
        LyraException e = assertThrows(LyraException.class, () -> parser.parseTodo("todo read|book"));
        assertEquals("The pipe character (|) is not allowed in task descriptions.", e.getMessage());
    }

    // -------------------------------------------------------------------------
    // parseDeadline
    // -------------------------------------------------------------------------

    @Test
    public void parseDeadline_validInput_success() throws LyraException {
        Deadline deadline = parser.parseDeadline("deadline return book /by 2/12/2024 1800");
        assertEquals("return book", deadline.getDescription());
        assertEquals(LocalDateTime.of(2024, 12, 2, 18, 0), deadline.getBy());
    }

    @Test
    public void parseDeadline_missingByClause_exceptionThrown() {
        LyraException e = assertThrows(LyraException.class, () -> parser.parseDeadline("deadline return book"));
        assertEquals("A deadline needs a due time — add /by followed by the date "
                + "(e.g., deadline return book /by 2/12/2024 1800).", e.getMessage());
    }

    @Test
    public void parseDeadline_emptyDescription_exceptionThrown() {
        // Two spaces after "deadline" so the parser finds " /by " but the description part is empty
        LyraException e = assertThrows(LyraException.class, () -> parser.parseDeadline("deadline  /by 2/12/2024 1800"));
        assertEquals("A deadline needs a description. Try: deadline return book /by 2/12/2024 1800.",
                e.getMessage());
    }

    @Test
    public void parseDeadline_emptyTime_exceptionThrown() {
        assertThrows(LyraException.class, () -> parser.parseDeadline("deadline return book /by "));
    }

    @Test
    public void parseDeadline_invalidDateFormat_exceptionThrown() {
        LyraException e = assertThrows(LyraException.class, () -> parser.parseDeadline(
                "deadline return book /by 2024-12-02 18:00"));
        assertEquals("That date format doesn't look right. Please use d/MM/yyyy HHmm "
                + "— for example: 2/12/2024 1800.", e.getMessage());
    }

    @Test
    public void parseDeadline_pipeInDescription_exceptionThrown() {
        assertThrows(LyraException.class, () -> parser.parseDeadline("deadline return|book /by 2/12/2024 1800"));
    }

    // -------------------------------------------------------------------------
    // parseEvent
    // -------------------------------------------------------------------------

    @Test
    public void parseEvent_validInput_success() throws LyraException {
        Event event = parser.parseEvent("event team meeting /from 2/12/2024 1000 /to 2/12/2024 1200");
        assertEquals("team meeting", event.getDescription());
        assertEquals(LocalDateTime.of(2024, 12, 2, 10, 0), event.getFrom());
        assertEquals(LocalDateTime.of(2024, 12, 2, 12, 0), event.getTo());
    }

    @Test
    public void parseEvent_missingFrom_exceptionThrown() {
        LyraException e = assertThrows(LyraException.class, () -> parser.parseEvent(
                "event meeting /to 2/12/2024 1200"));
        assertEquals("An event needs a start time — please include /from followed by the date.",
                e.getMessage());
    }

    @Test
    public void parseEvent_missingTo_exceptionThrown() {
        LyraException e = assertThrows(LyraException.class, () -> parser.parseEvent(
                "event meeting /from 2/12/2024 1000"));
        assertEquals("An event also needs an end time — please include /to followed by the date.",
                e.getMessage());
    }

    @Test
    public void parseEvent_emptyDescription_exceptionThrown() {
        // Two spaces after "event" so the parser finds " /from " but the description part is empty
        LyraException e = assertThrows(LyraException.class, () -> parser.parseEvent(
                "event  /from 2/12/2024 1000 /to 2/12/2024 1200"));
        assertEquals("An event needs a description. Try: event meeting "
                + "/from 2/12/2024 1000 /to 2/12/2024 1200.", e.getMessage());
    }

    @Test
    public void parseEvent_startAfterEnd_exceptionThrown() {
        assertThrows(LyraException.class, () -> parser.parseEvent(
                "event meeting /from 2/12/2024 1200 /to 2/12/2024 1000"));
    }

    @Test
    public void parseEvent_startEqualsEnd_exceptionThrown() {
        assertThrows(LyraException.class, () -> parser.parseEvent(
                "event meeting /from 2/12/2024 1000 /to 2/12/2024 1000"));
    }

    @Test
    public void parseEvent_pipeInDescription_exceptionThrown() {
        assertThrows(LyraException.class, () -> parser.parseEvent(
                "event team|meeting /from 2/12/2024 1000 /to 2/12/2024 1200"));
    }

    @Test
    public void parseEvent_invalidFromDate_exceptionThrown() {
        assertThrows(LyraException.class, () -> parser.parseEvent(
                "event meeting /from bad-date /to 2/12/2024 1200"));
    }

    // -------------------------------------------------------------------------
    // parseIndex
    // -------------------------------------------------------------------------

    @Test
    public void parseIndex_validIndex_returnsZeroBased() throws LyraException {
        assertEquals(0, parser.parseIndex("mark 1"));
        assertEquals(2, parser.parseIndex("mark 3"));
        assertEquals(9, parser.parseIndex("delete 10"));
    }

    @Test
    public void parseIndex_noNumber_exceptionThrown() {
        LyraException e = assertThrows(LyraException.class, () -> parser.parseIndex("mark"));
        assertEquals("Could you include a task number? For example: mark 2.", e.getMessage());
    }

    @Test
    public void parseIndex_nonNumeric_exceptionThrown() {
        LyraException e = assertThrows(LyraException.class, () -> parser.parseIndex("mark abc"));
        assertEquals("That doesn't look like a number. Please use digits, like: mark 2.", e.getMessage());
    }

    // -------------------------------------------------------------------------
    // parseTaskType
    // -------------------------------------------------------------------------

    @Test
    public void parseTaskType_todo_success() throws LyraException {
        assertEquals(TaskType.TODO, parser.parseTaskType("find todo"));
    }

    @Test
    public void parseTaskType_deadline_success() throws LyraException {
        assertEquals(TaskType.DEADLINE, parser.parseTaskType("find deadline"));
    }

    @Test
    public void parseTaskType_event_success() throws LyraException {
        assertEquals(TaskType.EVENT, parser.parseTaskType("find event"));
    }

    @Test
    public void parseTaskType_caseInsensitive_success() throws LyraException {
        assertEquals(TaskType.TODO, parser.parseTaskType("find TODO"));
        assertEquals(TaskType.DEADLINE, parser.parseTaskType("find DEADLINE"));
    }

    @Test
    public void parseTaskType_noType_exceptionThrown() {
        LyraException e = assertThrows(LyraException.class, () -> parser.parseTaskType("find"));
        assertEquals("Which type would you like to find? Try: find todo, "
                + "find deadline, or find event.", e.getMessage());
    }

    @Test
    public void parseTaskType_invalidType_exceptionThrown() {
        LyraException e = assertThrows(LyraException.class, () -> parser.parseTaskType("find task"));
        assertEquals("I don't recognise that task type. Please use one of: todo, deadline, or event.",
                e.getMessage());
    }

    // -------------------------------------------------------------------------
    // parseUpdateCommand
    // -------------------------------------------------------------------------

    @Test
    public void parseUpdateCommand_descriptionPath_success() throws LyraException {
        UpdateCommandData data = parser.parseUpdateCommand("update 1 /description new title");
        assertEquals(0, data.getIndex());
        assertEquals(UpdateType.DESCRIPTION, data.getUpdateType());
        assertEquals("new title", data.getDescriptionValue());
    }

    @Test
    public void parseUpdateCommand_byPath_success() throws LyraException {
        UpdateCommandData data = parser.parseUpdateCommand("update 2 /by 5/01/2025 0900");
        assertEquals(1, data.getIndex());
        assertEquals(UpdateType.BY, data.getUpdateType());
        assertEquals(LocalDateTime.of(2025, 1, 5, 9, 0), data.getDateValue());
    }

    @Test
    public void parseUpdateCommand_fromPath_success() throws LyraException {
        UpdateCommandData data = parser.parseUpdateCommand("update 3 /from 1/06/2025 1000");
        assertEquals(2, data.getIndex());
        assertEquals(UpdateType.FROM, data.getUpdateType());
        assertEquals(LocalDateTime.of(2025, 6, 1, 10, 0), data.getDateValue());
    }

    @Test
    public void parseUpdateCommand_toPath_success() throws LyraException {
        UpdateCommandData data = parser.parseUpdateCommand("update 1 /to 1/06/2025 1200");
        assertEquals(0, data.getIndex());
        assertEquals(UpdateType.TO, data.getUpdateType());
        assertEquals(LocalDateTime.of(2025, 6, 1, 12, 0), data.getDateValue());
    }

    @Test
    public void parseUpdateCommand_empty_exceptionThrown() {
        assertThrows(LyraException.class, () -> parser.parseUpdateCommand("update"));
    }

    @Test
    public void parseUpdateCommand_noDelimiter_exceptionThrown() {
        assertThrows(LyraException.class, () -> parser.parseUpdateCommand("update 1 something"));
    }

    @Test
    public void parseUpdateCommand_nonNumericIndex_exceptionThrown() {
        assertThrows(LyraException.class, () -> parser.parseUpdateCommand("update abc /description new title"));
    }

    @Test
    public void parseUpdateCommand_negativeIndex_exceptionThrown() {
        assertThrows(LyraException.class, () -> parser.parseUpdateCommand("update -1 /description new title"));
    }

    @Test
    public void parseUpdateCommand_emptyDescriptionValue_exceptionThrown() {
        assertThrows(LyraException.class, () -> parser.parseUpdateCommand("update 1 /description "));
    }

    @Test
    public void parseUpdateCommand_pipeInDescription_exceptionThrown() {
        assertThrows(LyraException.class, () -> parser.parseUpdateCommand("update 1 /description new|title"));
    }
}
