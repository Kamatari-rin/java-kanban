package Tests;

import filebacked.FileBackedTasksManager;
import org.junit.jupiter.api.BeforeEach;
import services.history.InMemoryHistoryManager;
import services.taskmanagers.InMemoryTaskManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class InMemoryHistoryManagerTest extends HistoryManagerTest<InMemoryHistoryManager> {

    @BeforeEach
    @Override
    void getHistoryManager() {
        historyManager = new InMemoryHistoryManager();
    }
}