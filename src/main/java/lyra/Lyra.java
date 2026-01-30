package lyra;

import java.util.ArrayList;

public class Lyra {
    private Ui ui;
    private Storage storage;
    private TaskList taskList;
    private Parser parser;

    /**
     * Constructor for Lyra.
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
