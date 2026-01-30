package lyra;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ParserTest {
    @Test
    public void getCommand_validCommand_success() throws LyraException {
        Parser parser = new Parser();
        assertEquals(Command.BYE, parser.getCommand("bye"));
        assertEquals(Command.LIST, parser.getCommand("list"));
    }

    @Test
    public void getCommand_invalidCommand_exceptionThrown() {
        try {
            new Parser().getCommand("invalid");
            fail(); // the test should not reach this line
        } catch (LyraException e) {
            assertEquals("I'm sorry, but I don't know what that means :-(", e.getMessage());
        }
    }

    @Test
    public void parseKeyword_validKeyword_success() throws LyraException {
        Parser parser = new Parser();
        assertEquals("book", parser.parseKeyword("find book"));
        assertEquals("read book", parser.parseKeyword("find read book"));
    }

    @Test
    public void parseKeyword_emptyKeyword_exceptionThrown() {
        Parser parser = new Parser();
        try {
            parser.parseKeyword("find");
            fail();
        } catch (LyraException e) {
            assertEquals("Please specify a keyword to find!", e.getMessage());
        }

        try {
            parser.parseKeyword("find   ");
            fail();
        } catch (LyraException e) {
            assertEquals("Please specify a keyword to find!", e.getMessage());
        }
    }
}