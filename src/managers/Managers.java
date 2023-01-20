package managers;

import history.InMemoryHistoryManager;

public class Managers {

    InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
