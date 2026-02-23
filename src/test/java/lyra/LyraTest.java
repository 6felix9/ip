package lyra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class LyraTest {

    @TempDir
    Path tempDir;

    private Lyra lyra;

    @BeforeEach
    public void setUp() {
        lyra = new Lyra(tempDir.resolve("lyra.txt").toString());
    }

    // -------------------------------------------------------------------------
    // getResponse — null / empty input
    // -------------------------------------------------------------------------

    @Test
    public void getResponse_nullInput_returnsEmptyNonError() {
        Lyra.LyraResponse response = lyra.getResponse(null);
        assertEquals("", response.getMessage());
        assertFalse(response.isError());
    }

    @Test
    public void getResponse_emptyString_returnsEmptyNonError() {
        Lyra.LyraResponse response = lyra.getResponse("");
        assertEquals("", response.getMessage());
        assertFalse(response.isError());
    }

    @Test
    public void getResponse_whitespaceOnly_returnsEmptyNonError() {
        Lyra.LyraResponse response = lyra.getResponse("   ");
        assertEquals("", response.getMessage());
        assertFalse(response.isError());
    }

    // -------------------------------------------------------------------------
    // getResponse — invalid / unknown command
    // -------------------------------------------------------------------------

    @Test
    public void getResponse_unknownCommand_isError() {
        Lyra.LyraResponse response = lyra.getResponse("foobar");
        assertTrue(response.isError());
    }

    // -------------------------------------------------------------------------
    // getResponse — list on empty task list
    // -------------------------------------------------------------------------

    @Test
    public void getResponse_listEmptyTasks_returnsEmptyListMessage() {
        Lyra.LyraResponse response = lyra.getResponse("list");
        assertFalse(response.isError());
        assertTrue(response.getMessage().contains("clear") || response.getMessage().contains("Here are"));
    }

    // -------------------------------------------------------------------------
    // getResponse — bye
    // -------------------------------------------------------------------------

    @Test
    public void getResponse_bye_returnsGoodbyeMessage() {
        Lyra.LyraResponse response = lyra.getResponse("bye");
        assertFalse(response.isError());
        assertEquals("Take care! I'm here whenever you need me.", response.getMessage());
    }

    // -------------------------------------------------------------------------
    // getResponse — todo command
    // -------------------------------------------------------------------------

    @Test
    public void getResponse_validTodo_addsAndReturnsMessage() {
        Lyra.LyraResponse response = lyra.getResponse("todo read book");
        assertFalse(response.isError());
        assertTrue(response.getMessage().contains("read book"));
    }

    @Test
    public void getResponse_todoNoDescription_isError() {
        Lyra.LyraResponse response = lyra.getResponse("todo");
        assertTrue(response.isError());
    }

    @Test
    public void getResponse_todoDuplicate_isError() {
        lyra.getResponse("todo read book");
        Lyra.LyraResponse response = lyra.getResponse("todo read book");
        assertTrue(response.isError());
    }

    // -------------------------------------------------------------------------
    // getResponse — deadline command
    // -------------------------------------------------------------------------

    @Test
    public void getResponse_validDeadline_addsAndReturnsMessage() {
        Lyra.LyraResponse response = lyra.getResponse("deadline submit report /by 2/12/2024 1800");
        assertFalse(response.isError());
        assertTrue(response.getMessage().contains("submit report"));
    }

    @Test
    public void getResponse_deadlineMissingBy_isError() {
        Lyra.LyraResponse response = lyra.getResponse("deadline submit report");
        assertTrue(response.isError());
    }

    // -------------------------------------------------------------------------
    // getResponse — event command
    // -------------------------------------------------------------------------

    @Test
    public void getResponse_validEvent_addsAndReturnsMessage() {
        Lyra.LyraResponse response = lyra.getResponse(
                "event team meeting /from 2/12/2024 1000 /to 2/12/2024 1200");
        assertFalse(response.isError());
        assertTrue(response.getMessage().contains("team meeting"));
    }

    @Test
    public void getResponse_eventMissingFrom_isError() {
        Lyra.LyraResponse response = lyra.getResponse("event meeting /to 2/12/2024 1200");
        assertTrue(response.isError());
    }

    // -------------------------------------------------------------------------
    // getResponse — mark / unmark
    // -------------------------------------------------------------------------

    @Test
    public void getResponse_markValidTask_isNotError() {
        lyra.getResponse("todo read book");
        Lyra.LyraResponse response = lyra.getResponse("mark 1");
        assertFalse(response.isError());
    }

    @Test
    public void getResponse_markInvalidIndex_isError() {
        Lyra.LyraResponse response = lyra.getResponse("mark 99");
        assertTrue(response.isError());
    }

    @Test
    public void getResponse_unmarkValidTask_isNotError() {
        lyra.getResponse("todo read book");
        lyra.getResponse("mark 1");
        Lyra.LyraResponse response = lyra.getResponse("unmark 1");
        assertFalse(response.isError());
    }

    // -------------------------------------------------------------------------
    // getResponse — delete
    // -------------------------------------------------------------------------

    @Test
    public void getResponse_deleteValidTask_isNotError() {
        lyra.getResponse("todo read book");
        Lyra.LyraResponse response = lyra.getResponse("delete 1");
        assertFalse(response.isError());
    }

    @Test
    public void getResponse_deleteInvalidIndex_isError() {
        Lyra.LyraResponse response = lyra.getResponse("delete 5");
        assertTrue(response.isError());
    }

    // -------------------------------------------------------------------------
    // getResponse — find
    // -------------------------------------------------------------------------

    @Test
    public void getResponse_findTodo_returnsMatchingTasks() {
        lyra.getResponse("todo read book");
        Lyra.LyraResponse response = lyra.getResponse("find todo");
        assertFalse(response.isError());
        assertTrue(response.getMessage().contains("read book"));
    }

    @Test
    public void getResponse_findNoMatch_returnsNoResultsMessage() {
        Lyra.LyraResponse response = lyra.getResponse("find event");
        assertFalse(response.isError());
        assertTrue(response.getMessage().contains("couldn't find"));
    }

    @Test
    public void getResponse_findInvalidType_isError() {
        Lyra.LyraResponse response = lyra.getResponse("find unknown");
        assertTrue(response.isError());
    }

    // -------------------------------------------------------------------------
    // getResponse — update
    // -------------------------------------------------------------------------

    @Test
    public void getResponse_updateDescription_messageContainsNewDescription() {
        lyra.getResponse("todo old title");
        Lyra.LyraResponse response = lyra.getResponse("update 1 /description new title");
        assertFalse(response.isError());
        assertTrue(response.getMessage().contains("new title"));
    }

    @Test
    public void getResponse_updateByOnDeadline_isNotError() {
        lyra.getResponse("deadline submit report /by 2/12/2024 1800");
        Lyra.LyraResponse response = lyra.getResponse("update 1 /by 5/01/2025 0900");
        assertFalse(response.isError());
    }

    @Test
    public void getResponse_updateByOnTodo_isError() {
        lyra.getResponse("todo read book");
        Lyra.LyraResponse response = lyra.getResponse("update 1 /by 5/01/2025 0900");
        assertTrue(response.isError());
    }

    @Test
    public void getResponse_updateInvalidIndex_isError() {
        Lyra.LyraResponse response = lyra.getResponse("update 99 /description new title");
        assertTrue(response.isError());
    }

    // -------------------------------------------------------------------------
    // Sequential integration scenarios
    // -------------------------------------------------------------------------

    @Test
    public void sequential_addMarkUnmarkDelete_listEmptyAfter() {
        assertFalse(lyra.getResponse("todo read book").isError());
        assertFalse(lyra.getResponse("mark 1").isError());
        assertFalse(lyra.getResponse("unmark 1").isError());
        assertFalse(lyra.getResponse("delete 1").isError());

        Lyra.LyraResponse listResponse = lyra.getResponse("list");
        assertFalse(listResponse.isError());
        assertTrue(listResponse.getMessage().contains("clear"));
    }

    @Test
    public void sequential_addDeadlineUpdateDescription_updatedStringInResponse() {
        lyra.getResponse("deadline old title /by 2/12/2024 1800");
        Lyra.LyraResponse updateResponse = lyra.getResponse("update 1 /description new title");
        assertFalse(updateResponse.isError());
        assertTrue(updateResponse.getMessage().contains("new title"));
    }

    @Test
    public void sequential_addMultipleTasks_listShowsAll() {
        lyra.getResponse("todo task one");
        lyra.getResponse("todo task two");
        Lyra.LyraResponse listResponse = lyra.getResponse("list");
        assertFalse(listResponse.isError());
        assertTrue(listResponse.getMessage().contains("task one"));
        assertTrue(listResponse.getMessage().contains("task two"));
    }

    @Test
    public void sequential_addAndFindByType_onlyMatchingTypeReturned() {
        lyra.getResponse("todo read book");
        lyra.getResponse("deadline submit report /by 2/12/2024 1800");
        Lyra.LyraResponse findTodo = lyra.getResponse("find todo");
        assertFalse(findTodo.isError());
        assertTrue(findTodo.getMessage().contains("read book"));
        assertFalse(findTodo.getMessage().contains("submit report"));
    }
}
