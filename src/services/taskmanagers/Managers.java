package services.taskmanagers;

import services.history.HistoryManager;
import services.history.InMemoryHistoryManager;
import services.printmanager.PrintManager;

public class Managers {

    public TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public PrintManager getDefaultPrintManager() {
        return new PrintManager();
    }
}
