package services.taskmanagers;

import filebacked.FileBackedTasksManager;
import services.history.HistoryManager;
import services.history.InMemoryHistoryManager;
import services.printmanager.PrintManager;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static PrintManager getDefaultPrintManager() {
        return new PrintManager();
    }

    public static FileBackedTasksManager loadFromFile(Path path) {
        return new FileBackedTasksManager(Paths.get(path.toUri()));
    }
}
