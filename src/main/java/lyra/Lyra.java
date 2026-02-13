package lyra;

import java.util.ArrayList;

/**
 * Main class for the Lyra task management application.
 */
public class Lyra {
    private Ui ui;
    private Storage storage;
    private TaskList taskList;
    private Parser parser;

    /**
     * Creates a Lyra application with the specified file path for data storage.
     *
     * @param filePath The path to the data file
     */
    public Lyra(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        this.parser = new Parser();
        try {
            this.taskList = new TaskList(storage.loadTasks());
        } catch (LyraException e) {
            this.ui.showError(e.getMessage());
            this.taskList = new TaskList();
        }
    }

    /**
     * Processes a user command and returns the bot's response.
     *
     * @param input The user's input command
     * @return The bot's response as a string
     */
    public String getResponse(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        try {
            String fullCommand = input.trim();
            Command commandType = parser.getCommand(fullCommand);
            return executeCommand(commandType, fullCommand);
        } catch (LyraException e) {
            return ui.getErrorMessage(e.getMessage());
        }
    }

    /**
     * Executes a command and returns the response message.
     *
     * @param commandType The type of command to execute
     * @param fullCommand The full command string
     * @return The response message as a string
     * @throws LyraException If the command execution fails
     */
    private String executeCommand(Command commandType, String fullCommand) throws LyraException {
        switch (commandType) {
        case BYE:
            return handleByeCommand();

        case MARK:
            return handleMarkCommand(fullCommand);

        case UNMARK:
            return handleUnmarkCommand(fullCommand);

        case LIST:
            return handleListCommand();

        case TODO:
            return handleTodoCommand(fullCommand);

        case DEADLINE:
            return handleDeadlineCommand(fullCommand);

        case EVENT:
            return handleEventCommand(fullCommand);

        case DELETE:
            return handleDeleteCommand(fullCommand);

        case FIND:
            return handleFindCommand(fullCommand);

        default:
            throw new LyraException("I'm sorry, but I don't know what that means :-(");
        }
    }

    /**
     * Handles the BYE command.
     *
     * @return The goodbye message
     */
    private String handleByeCommand() {
        return ui.getGoodbyeMessage();
    }

    /**
     * Handles the MARK command.
     *
     * @param fullCommand The full command string
     * @return The response message
     * @throws LyraException If the command execution fails
     */
    private String handleMarkCommand(String fullCommand) throws LyraException {
        int markIndex = parser.parseIndex(fullCommand);
        Task markedTask = taskList.markTask(markIndex);
        saveTasks();
        return ui.getMarkedMessage(markedTask);
    }

    /**
     * Handles the UNMARK command.
     *
     * @param fullCommand The full command string
     * @return The response message
     * @throws LyraException If the command execution fails
     */
    private String handleUnmarkCommand(String fullCommand) throws LyraException {
        int unmarkIndex = parser.parseIndex(fullCommand);
        Task unmarkedTask = taskList.unmarkTask(unmarkIndex);
        saveTasks();
        return ui.getUnmarkedMessage(unmarkedTask);
    }

    /**
     * Handles the LIST command.
     *
     * @return The response message
     */
    private String handleListCommand() {
        return ui.getAllTasksMessage(taskList);
    }

    /**
     * Handles the TODO command.
     *
     * @param fullCommand The full command string
     * @return The response message
     * @throws LyraException If the command execution fails
     */
    private String handleTodoCommand(String fullCommand) throws LyraException {
        Todo newTodo = parser.parseTodo(fullCommand);
        taskList.addTask(newTodo);
        saveTasks();
        return ui.getAddedTaskMessage(newTodo);
    }

    /**
     * Handles the DEADLINE command.
     *
     * @param fullCommand The full command string
     * @return The response message
     * @throws LyraException If the command execution fails
     */
    private String handleDeadlineCommand(String fullCommand) throws LyraException {
        Deadline newDeadline = parser.parseDeadline(fullCommand);
        taskList.addTask(newDeadline);
        saveTasks();
        return ui.getAddedTaskMessage(newDeadline);
    }

    /**
     * Handles the EVENT command.
     *
     * @param fullCommand The full command string
     * @return The response message
     * @throws LyraException If the command execution fails
     */
    private String handleEventCommand(String fullCommand) throws LyraException {
        Event newEvent = parser.parseEvent(fullCommand);
        taskList.addTask(newEvent);
        saveTasks();
        return ui.getAddedTaskMessage(newEvent);
    }

    /**
     * Handles the DELETE command.
     *
     * @param fullCommand The full command string
     * @return The response message
     * @throws LyraException If the command execution fails
     */
    private String handleDeleteCommand(String fullCommand) throws LyraException {
        int deleteIndex = parser.parseIndex(fullCommand);
        Task removedTask = taskList.removeTask(deleteIndex);
        saveTasks();
        return ui.getRemovedTaskMessage(removedTask);
    }

    /**
     * Handles the FIND command.
     *
     * @param fullCommand The full command string
     * @return The response message
     * @throws LyraException If the command execution fails
     */
    private String handleFindCommand(String fullCommand) throws LyraException {
        TaskType typeToFind = parser.parseTaskType(fullCommand);
        ArrayList<Task> foundTasks = taskList.findTasks(typeToFind);
        return ui.getFoundTasksMessage(foundTasks);
    }

    /**
     * Saves tasks to storage.
     *
     * @throws LyraException If saving fails
     */
    private void saveTasks() throws LyraException {
        storage.saveTasks(taskList.getTasks());
    }

    /**
     * Runs the Lyra application.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;

        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();

                if (fullCommand.isEmpty()) {
                    continue;
                }

                Command commandType = parser.getCommand(fullCommand);
                String response = executeCommand(commandType, fullCommand);
                displayResponse(commandType, response);

                if (commandType == Command.BYE) {
                    isExit = true;
                }
            } catch (LyraException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * Displays the response for CLI mode.
     *
     * @param commandType The type of command that was executed
     * @param response The response message
     */
    private void displayResponse(Command commandType, String response) {
        if (commandType == Command.BYE) {
            ui.showGoodbye();
        } else {
            ui.displayMessage(response);
        }
    }

    /**
     * Main method to run the Lyra application.
     */
    public static void main(String[] args) {
        new Lyra("data/lyra.txt").run();
    }
}
