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

            switch (commandType) {
            case BYE:
                return ui.getGoodbyeMessage();

            case MARK:
                int markIndex = parser.parseIndex(fullCommand);
                Task markedTask = taskList.markTask(markIndex);
                storage.saveTasks(taskList.getTasks());
                return ui.getMarkedMessage(markedTask);

            case UNMARK:
                int unmarkIndex = parser.parseIndex(fullCommand);
                Task unmarkedTask = taskList.unmarkTask(unmarkIndex);
                storage.saveTasks(taskList.getTasks());
                return ui.getUnmarkedMessage(unmarkedTask);

            case LIST:
                return ui.getAllTasksMessage(taskList);

            case TODO:
                Todo newTodo = parser.parseTodo(fullCommand);
                taskList.addTask(newTodo);
                storage.saveTasks(taskList.getTasks());
                return ui.getAddedTaskMessage(newTodo);

            case DEADLINE:
                Deadline newDeadline = parser.parseDeadline(fullCommand);
                taskList.addTask(newDeadline);
                storage.saveTasks(taskList.getTasks());
                return ui.getAddedTaskMessage(newDeadline);

            case EVENT:
                Event newEvent = parser.parseEvent(fullCommand);
                taskList.addTask(newEvent);
                storage.saveTasks(taskList.getTasks());
                return ui.getAddedTaskMessage(newEvent);

            case DELETE:
                int deleteIndex = parser.parseIndex(fullCommand);
                Task removedTask = taskList.removeTask(deleteIndex);
                storage.saveTasks(taskList.getTasks());
                return ui.getRemovedTaskMessage(removedTask);

            case FIND:
                TaskType typeToFind = parser.parseTaskType(fullCommand);
                ArrayList<Task> foundTasks = taskList.findTasks(typeToFind);
                return ui.getFoundTasksMessage(foundTasks);

            default:
                throw new LyraException("I'm sorry, but I don't know what that means :-(");
            }
        } catch (LyraException e) {
            return ui.getErrorMessage(e.getMessage());
        }
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

                switch (commandType) {
                case BYE:
                    ui.showGoodbye();
                    isExit = true;
                    break;

                case MARK:
                    int markIndex = parser.parseIndex(fullCommand);
                    Task markedTask = taskList.markTask(markIndex);
                    ui.showMarked(markedTask);
                    storage.saveTasks(taskList.getTasks());
                    break;

                case UNMARK:
                    int unmarkIndex = parser.parseIndex(fullCommand);
                    Task unmarkedTask = taskList.unmarkTask(unmarkIndex);
                    ui.showUnmarked(unmarkedTask);
                    storage.saveTasks(taskList.getTasks());
                    break;

                case LIST:
                    ui.showAllTasks(taskList);
                    break;

                case TODO:
                    Todo newTodo = parser.parseTodo(fullCommand);
                    taskList.addTask(newTodo);
                    ui.showAddedTodo(newTodo);
                    storage.saveTasks(taskList.getTasks());
                    break;

                case DEADLINE:
                    Deadline newDeadline = parser.parseDeadline(fullCommand);
                    taskList.addTask(newDeadline);
                    ui.showAddedDeadline(newDeadline);
                    storage.saveTasks(taskList.getTasks());
                    break;

                case EVENT:
                    Event newEvent = parser.parseEvent(fullCommand);
                    taskList.addTask(newEvent);
                    ui.showAddedEvent(newEvent);
                    storage.saveTasks(taskList.getTasks());
                    break;

                case DELETE:
                    int deleteIndex = parser.parseIndex(fullCommand);
                    Task removedTask = taskList.removeTask(deleteIndex);
                    ui.showRemovedTask(removedTask);
                    storage.saveTasks(taskList.getTasks());
                    break;

                case FIND:
                    TaskType typeToFind = parser.parseTaskType(fullCommand);
                    ArrayList<Task> foundTasks = taskList.findTasks(typeToFind);
                    ui.showFoundTasks(foundTasks);
                    break;

                default:
                    throw new LyraException("I'm sorry, but I don't know what that means :-(");
                }
            } catch (LyraException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * Main method to run the Lyra application.
     */
    public static void main(String[] args) {
        new Lyra("data/lyra.txt").run();
    }
}
