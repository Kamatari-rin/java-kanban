package tests;

import filebacked.FileBackedTasksManager;
import models.Epic;
import models.Subtask;
import models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.taskmanagers.TaskManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

import static java.time.Month.JANUARY;
import static java.util.Calendar.JULY;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    @Override
    void chooseTaskManager() throws IOException, InterruptedException {
        if (Files.exists(Paths.get(("savefortest.csv")))) Files.delete(Paths.get(("savefortest.csv")));
        Path saveForTest = Paths.get("savefortest.csv");
        taskManager = new FileBackedTasksManager(saveForTest);
    }

    @Test
    void WriteAndReadFromFile() throws IOException, InterruptedException {

        Path saveForTest = Paths.get("savefortest.csv");

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

        //////////////////////////////////////Востанавливаемся из файла/////////////////////////////////////////////////

        TaskManager loadFromFileTaskManager = new FileBackedTasksManager(saveForTest);

        final List<Task> HistoryListFromFile = loadFromFileTaskManager.getHistory();
        final Map<Integer, Task> TaskMapFromFile = Collections.unmodifiableMap(loadFromFileTaskManager.getAllTask());
        final Map<Integer, Task> EpicsMapFromFile = Collections.unmodifiableMap(loadFromFileTaskManager.getAllEpic());
        final Map<Integer, Task> SubtaskMapFromFile = Collections.unmodifiableMap(loadFromFileTaskManager.getAllSubtask());

        for (Integer taskID : controlTaskMap.keySet()) {
            Task taskFromControlMap = controlTaskMap.get(taskID);
            Task taskFromFile = TaskMapFromFile.get(taskID);
            assertEquals(taskFromFile, taskFromControlMap);
        }

        for (Integer epicID : controlEpicsMap.keySet()) {
            Epic epicFromControlMap = (Epic) controlEpicsMap.get(epicID);
            Epic epicFromFile = (Epic) EpicsMapFromFile.get(epicID);
            assertEquals(epicFromFile, epicFromControlMap);
        }
        for (Integer subtaskID : controlSubtaskMap.keySet()) {
            Subtask subtaskFromControlMap = (Subtask) controlSubtaskMap.get(subtaskID);
            Subtask subtaskFromFile = (Subtask) SubtaskMapFromFile.get(subtaskID);
            assertEquals(subtaskFromFile, subtaskFromControlMap);
        }
    }
}