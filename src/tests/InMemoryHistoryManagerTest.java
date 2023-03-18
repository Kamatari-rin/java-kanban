package tests;

import org.junit.jupiter.api.BeforeEach;
import services.history.InMemoryHistoryManager;

class InMemoryHistoryManagerTest extends HistoryManagerTest<InMemoryHistoryManager> {

    @BeforeEach
    @Override
    void getHistoryManager() {
        historyManager = new InMemoryHistoryManager();
    }
}