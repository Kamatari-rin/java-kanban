package tests;

import org.junit.jupiter.api.BeforeEach;
import services.history.InMemoryHistoryManager;

import java.io.IOException;

class InMemoryHistoryManagerTest extends HistoryManagerTest<InMemoryHistoryManager> {

    public InMemoryHistoryManagerTest() throws IOException, InterruptedException {
    }

    @BeforeEach
    @Override
    void getHistoryManager() {
        historyManager = new InMemoryHistoryManager();
    }
}