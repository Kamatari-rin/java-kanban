package tests;

import com.google.gson.Gson;
import http.HttpTaskManager;
import http.HttpTaskServer;
import http.KVServer;
import models.Epic;
import models.Subtask;
import models.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.taskmanagers.Managers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

import static java.time.Month.JANUARY;
import static java.util.Calendar.JULY;

class HttpTaskManagerTest {
    private final int PORT = 8080;
    private URL url;
    private HttpTaskServer taskServer;
    private final Gson gson = Managers.getGson();
    private HttpTaskManager taskManager;
    private KVServer kvServer;

    @BeforeEach
    void init() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        url = new URL("http://localhost:" + PORT);
        taskManager = Managers.getDefault();
        taskServer = Managers.getDefaultHttpTaskServer(taskManager, "localhost");
        taskServer.start();
    }

    @AfterEach
    void tearDown(){
        taskServer.stop();
        kvServer.stop(0);
    }

    @Test
    void save() throws IOException, InterruptedException {
        Task taskOne = new Task("Задача 1", "Описание первой задачи", Task.Status.NEW,
                LocalDateTime.of(2023, JANUARY, 1, 12, 0), 1, 25);
        Task taskTwo = new Task("Задача 2", "Описание второй задачи", Task.Status.DONE,
                LocalDateTime.of(2023, JANUARY, 1, 13, 0), 1, 25);
        Task taskTree = new Task("Задача 3", "Описание третьей задачи", Task.Status.IN_PROGRESS,
                LocalDateTime.of(2023, JANUARY, 2, 0, 0), 1, 25);
        taskManager.createTask(taskOne);
        taskManager.createTask(taskTwo);
        taskManager.createTask(taskTree);

        Epic epicOne = new Epic("Эпик 1", "Описание 1 эпика");
        Epic epicTwo = new Epic("Эпик 2", "Описание 2 эпика");
        Epic epicThree = new Epic("Эпик 3", "Описание 3 эпика");

        final int epicOneID = taskManager.createTask(epicOne);
        final int epicTwoID = taskManager.createTask(epicTwo);
        final int epicThreeID = taskManager.createTask(epicThree);

        Subtask subtaskOneByFirstEpic = new Subtask("Подзадача 1 от Эпика №1", "Описание первой подзадачи от Эпика №1",
                Task.Status.NEW, epicOneID, LocalDateTime.of(2022, JANUARY, 1, 0, 0), 1, 15);
        Subtask subtaskTwoByFirstEpic = new Subtask("Подзадача 2 от Эпика №1", "Описание второй подзадачи от Эпика №1",
                Task.Status.IN_PROGRESS, epicOneID, LocalDateTime.of(2022, JANUARY, 1, 3, 0), 1, 17);
        Subtask subtaskOneBySecondEpic = new Subtask("Подзадача 1 от Эпика №2", "Описание первой подзадачи от Эпика №2",
                Task.Status.IN_PROGRESS, epicTwoID, LocalDateTime.of(2024, JANUARY, 2, 0, 0), 1, 27);
        Subtask subtaskTwoBySecondEpic = new Subtask("Подзадача 2 от Эпика №2", "Описание второй подзадачи от Эпика №2",
                Task.Status.DONE, epicTwoID, LocalDateTime.of(2023, JANUARY, 1, 8, 0), 1, 27);
        Subtask subtaskOneByThirdEpic = new Subtask("Подзадача 1 от Эпика №3", "Описание второй подзадачи от Эпика №3",
                Task.Status.DONE, epicThreeID, LocalDateTime.of(2022, JULY, 1, 0, 0), 1, 23);

        taskManager.createTask(subtaskOneByFirstEpic);
        taskManager.createTask(subtaskTwoByFirstEpic);
        taskManager.createTask(subtaskOneBySecondEpic);
        taskManager.createTask(subtaskTwoBySecondEpic);
        taskManager.createTask(subtaskOneByThirdEpic);

        final Map<Integer, Task> tasks = new HashMap<>();
        tasks.putAll(taskManager.getAllTask());
        tasks.putAll(taskManager.getAllEpic());
        tasks.putAll(taskManager.getAllSubtask());

        List<Integer> tasksIDList = new ArrayList<>();

        for (Integer taskID : tasks.keySet()) {
            tasksIDList.add(taskID);
        }

        for (Integer taskID : tasksIDList) {
            taskManager.getTaskById(taskID);
        }

        final List<Task> controlHisoryList = taskManager.getHistory();
        final Map<Integer, Task> controlTaskMap = Collections.unmodifiableMap(taskManager.getAllTask());
        final Map<Integer, Task> controlEpicsMap = Collections.unmodifiableMap(taskManager.getAllEpic());
        final Map<Integer, Task> controlSubtaskMap = Collections.unmodifiableMap(taskManager.getAllSubtask());

//////////////////////////////////// Востанавливаемся из KVServer //////////////////////////////////////////////////////

        // Не пойму как востановиться из данных на сервере.
        new HttpTaskManager().load();
    }
}