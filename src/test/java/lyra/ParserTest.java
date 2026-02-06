package lyra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

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
}
