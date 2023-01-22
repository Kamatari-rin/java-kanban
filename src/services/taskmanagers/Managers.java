package services.taskmanagers;

import services.history.HistoryManager;
import services.history.InMemoryHistoryManager;

public class Managers {

    TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
