package services.taskmanagers;

import services.history.HistoryManager;
import services.history.InMemoryHistoryManager;
import services.printmanager.PrintManager;

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
}
