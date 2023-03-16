package Tests;

import org.junit.jupiter.api.BeforeEach;
import services.taskmanagers.InMemoryTaskManager;

import java.io.IOException;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{
    @BeforeEach
    @Override
    void chooseTaskManager() throws IOException {
        taskManager = new InMemoryTaskManager();
    }
}