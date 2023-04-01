package tests;

import models.Epic;
import models.Subtask;
import models.Task;
import org.junit.jupiter.api.Test;
import services.history.HistoryManager;
import services.taskmanagers.Managers;
import services.taskmanagers.TaskManager;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.*;

public abstract class HistoryManagerTest<T extends HistoryManager> {
    protected HistoryManager historyManager;
    protected TaskManager taskManager;

    public HistoryManagerTest() throws IOException, InterruptedException {
        this.taskManager = Managers.loadFromFile(Paths.get("savefortest.csv"));
    }

    abstract void getHistoryManager();

    @Test
    void add() throws IOException, InterruptedException {
        Task task = new Task("Задача 1", "Описание первой задачи", Task.Status.NEW, LocalDateTime.now(), 1, 25);
        taskManager.createTask(task);
        historyManager.add(task);

        final List<Task> historyTaskList = Collections.unmodifiableList(historyManager.getHistory());

        assertNotNull(historyTaskList);
        assertEquals(1, historyTaskList.size());

        final Task taskFromHistoryList = historyTaskList.get(0);
        assertNotNull(taskFromHistoryList);
        assertEquals(task, taskFromHistoryList);
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        final Map<Integer, Task> controlTaskMap = new HashMap<>();

        Task taskOne = new Task("Задача 1", "Описание 1 задачи", Task.Status.NEW,
                LocalDateTime.of(2022, JANUARY, 1, 0, 0), 1, 15);
        Task taskTwo = new Task("Задача 2", "Описание 2 задачи", Task.Status.NEW,
                LocalDateTime.of(2022, JANUARY, 1, 3, 0), 1, 55);
        Task taskThree = new Task("Задача 3", "Описание 3 задачи", Task.Status.NEW,
                LocalDateTime.of(2022, JANUARY, 1, 4, 0), 1, 25);

        final int taskOneID = taskManager.createTask(taskOne);
        final int taskTwoID = taskManager.createTask(taskTwo);
        final int taskThreeID = taskManager.createTask(taskThree);

        controlTaskMap.put(taskTwoID, taskTwo);
        controlTaskMap.put(taskThreeID, taskThree);

        Epic epicOne = new Epic("Эпик 1", "Описание первого эпика");
        Epic epicTwo = new Epic("Эпик 1", "Описание первого эпика");
        Epic epicThree = new Epic("Эпик 1", "Описание первого эпика");

        final int epicOneID = taskManager.createTask(epicOne);
        final int epicTwoID = taskManager.createTask(epicTwo);
        final int epicThreeID = taskManager.createTask(epicThree);

        controlTaskMap.put(epicOneID, epicOne);
        controlTaskMap.put(epicTwoID, epicTwo);
        controlTaskMap.put(epicThreeID, epicThree);

        Subtask subtaskOneByFirstEpic = new Subtask("Подзадача 1 от Эпика №1", "Описание первой подзадачи от Эпика №1",
                Task.Status.NEW, epicOneID, LocalDateTime.of(2022, JANUARY, 10, 0, 0), 1, 15);
        Subtask subtaskTwoByFirstEpic = new Subtask("Подзадача 2 от Эпика №1", "Описание второй подзадачи от Эпика №1",
                Task.Status.IN_PROGRESS, epicOneID, LocalDateTime.of(2022, JANUARY, 11, 0, 0), 1, 17);
        Subtask subtaskOneBySecondEpic = new Subtask("Подзадача 1 от Эпика №2", "Описание первой подзадачи от Эпика №2",
                Task.Status.IN_PROGRESS, epicTwoID, LocalDateTime.of(2022, JANUARY, 22, 0, 0), 1, 27);
        Subtask subtaskTwoBySecondEpic = new Subtask("Подзадача 2 от Эпика №2", "Описание второй подзадачи от Эпика №2",
                Task.Status.DONE, epicTwoID, LocalDateTime.of(2022, JANUARY, 2, 0, 0), 1, 27);
        Subtask subtaskOneByThirdEpic = new Subtask("Подзадача 1 от Эпика №3", "Описание второй подзадачи от Эпика №3",
                Task.Status.DONE, epicThreeID, LocalDateTime.of(2022, JANUARY, 5, 0, 0), 1, 23);

        final int subtaskOneByFirstEpicID = taskManager.createTask(subtaskOneByFirstEpic);
        final int subtaskTwoByFirstEpicID = taskManager.createTask(subtaskTwoByFirstEpic);
        final int subtaskOneBySecondEpicID = taskManager.createTask(subtaskOneBySecondEpic);
        final int subtaskTwoBySecondEpicID = taskManager.createTask(subtaskTwoBySecondEpic);
        final int subtaskOneByThirdEpicID = taskManager.createTask(subtaskOneByThirdEpic);

        controlTaskMap.put(subtaskOneByFirstEpicID, subtaskOneByFirstEpic);
        controlTaskMap.put(subtaskTwoByFirstEpicID, subtaskTwoByFirstEpic);
        controlTaskMap.put(subtaskOneBySecondEpicID, subtaskOneBySecondEpic);
        controlTaskMap.put(subtaskTwoBySecondEpicID, subtaskTwoBySecondEpic);
        controlTaskMap.put(subtaskOneByThirdEpicID, subtaskOneByThirdEpic);

        for (Integer taskID : controlTaskMap.keySet()) {
            historyManager.add(controlTaskMap.get(taskID));
        }

        List<Task> historyTaskList = historyManager.getHistory();
        assertNotNull(historyTaskList);

        for (Task task : historyTaskList) {
            for (Integer id : controlTaskMap.keySet()) {
                assertEquals(true, historyManager.isHistoryContainsTask(id), "Задача не найдена в истории");
            }
        }
    }

    @Test
    void getHistoryWithEmptyHistory() {
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> historyManager.getHistory()
        );

        assertEquals("История пуста.", exception.getMessage());
    }

    @Test
    void removeFirst() throws IOException, InterruptedException {
        Task taskFirst = new Task("Задача 1", "Описание первой задачи", Task.Status.NEW,
                LocalDateTime.of(2022, JANUARY, 1, 0, 0), 1, 25);
        final int taskFirstID = taskManager.createTask(taskFirst);
        Task taskCenter = new Task("Задача 1", "Описание первой задачи", Task.Status.NEW,
                LocalDateTime.of(2022, JANUARY, 3, 0, 0), 1, 25);
        final int taskCenterID = taskManager.createTask(taskCenter);
        Task taskLast = new Task("Задача 1", "Описание первой задачи", Task.Status.NEW,
                LocalDateTime.of(2022, JANUARY, 1, 4, 0), 1, 25);
        final int taskLastID = taskManager.createTask(taskLast);
        historyManager.add(taskFirst);
        historyManager.add(taskCenter);
        historyManager.add(taskLast);

        historyManager.remove(taskFirstID);

        assertEquals(false, historyManager.isHistoryContainsTask(taskFirstID));
        assertEquals(true, historyManager.isHistoryContainsTask(taskCenterID));
        assertEquals(true, historyManager.isHistoryContainsTask(taskLastID));
    }

    @Test
    void removeCenterElement() throws IOException, InterruptedException {
        Task taskFirst = new Task("Задача First", "Описание первой задачи", Task.Status.NEW,
                LocalDateTime.of(2022, JANUARY, 1, 0, 0), 1, 25);
        final int taskFirstID = taskManager.createTask(taskFirst);
        Task taskCenter = new Task("Задача Center", "Описание первой задачи", Task.Status.NEW,
                LocalDateTime.of(2022, JANUARY, 1, 3, 0), 1, 25);
        final int taskCenterID = taskManager.createTask(taskCenter);
        Task taskLast = new Task("Задача Last", "Описание первой задачи", Task.Status.NEW,
                LocalDateTime.of(2022, JANUARY, 1, 9, 0), 1, 25);
        final int taskLastID = taskManager.createTask(taskLast);
        historyManager.add(taskFirst);
        historyManager.add(taskCenter);
        historyManager.add(taskLast);

        historyManager.remove(taskCenterID);

        assertEquals(true, historyManager.isHistoryContainsTask(taskFirstID));
        assertEquals(false, historyManager.isHistoryContainsTask(taskCenterID));
        assertEquals(true, historyManager.isHistoryContainsTask(taskLastID));
    }

    @Test
    void removeLastElement() throws IOException, InterruptedException {
        Task taskFirst = new Task("Задача First", "Описание первой задачи", Task.Status.NEW,
                LocalDateTime.of(2022, JANUARY, 1, 0, 0), 1, 25);
        final int taskFirstID = taskManager.createTask(taskFirst);
        Task taskCenter = new Task("Задача Center", "Описание первой задачи", Task.Status.NEW,
                LocalDateTime.of(2022, JANUARY, 1, 3, 0), 1, 25);
        final int taskCenterID = taskManager.createTask(taskCenter);
        Task taskLast = new Task("Задача Last", "Описание первой задачи", Task.Status.NEW,
                LocalDateTime.of(2022, JANUARY, 1, 9, 0), 1, 25);
        final int taskLastID = taskManager.createTask(taskLast);
        historyManager.add(taskFirst);
        historyManager.add(taskCenter);
        historyManager.add(taskLast);

        historyManager.remove(taskLastID);
        assertEquals(true, historyManager.isHistoryContainsTask(taskFirstID));
        assertEquals(true, historyManager.isHistoryContainsTask(taskCenterID));
        assertEquals(false, historyManager.isHistoryContainsTask(taskLastID));
    }

    @Test
    void removeIncorrectId() {
        final int incorrectID = 1;
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> historyManager.remove(incorrectID)
        );

        assertEquals("В истории такой задачи не существует.", exception.getMessage());
    }

    @Test
    void removeWithEmptyHistory() {
        final int incorrectID = 1;
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> historyManager.remove(incorrectID)
        );

        assertEquals("В истории такой задачи не существует.", exception.getMessage());
    }

    @Test
    void isHistoryContainsTask() throws IOException, InterruptedException {
        Task task = new Task("Задача 1", "Описание первой задачи", Task.Status.NEW, LocalDateTime.now(), 1, 25);
        final int taskID = taskManager.createTask(task);
        final int incorrectID = 2;
        historyManager.add(task);

        assertEquals(true, historyManager.isHistoryContainsTask(taskID));
        assertEquals(false, historyManager.isHistoryContainsTask(incorrectID));
    }
}