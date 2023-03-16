package Tests;

import models.Epic;
import models.Subtask;
import models.Task;

import org.junit.jupiter.api.Test;
import services.taskmanagers.TaskManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.Calendar.*;
import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    abstract void chooseTaskManager() throws IOException;

////////////////////////////////////////////  Create Task Test   ///////////////////////////////////////////////////////
    @Test
    void shouldCreateNewTask() throws IOException {

        Task task = new Task("Задача 1", "Описание первой задачи", Task.Status.NEW, LocalDateTime.now(), 1, 25);

        final int taskID = taskManager.createTask(task);
        final Task savedTask = taskManager.getTaskById(taskID);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final Map<Integer, Task> tasks = taskManager.getAllTask();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, (Task) tasks.get(taskID), "Задачи не совпадают при запросе мапы.");
    }

////////////////////////////////////////////  Create Epic Test   ///////////////////////////////////////////////////////
    @Test
    void shouldCreateNewEpicWithEmptySubtasksList() throws IOException {

        Epic epic = new Epic("Эпик 1", "Описание первого эпика");
        final int epicID = taskManager.createTask(epic);

        final RuntimeException exceptionGetSubtaskList = assertThrows(
                RuntimeException.class,
                () -> epic.getSubtaskList()
        );
        assertEquals("Список подзадач пуст.", exceptionGetSubtaskList.getMessage());

        final RuntimeException exceptionGetAllSubtaskByEpicID = assertThrows(
                RuntimeException.class,
                () -> taskManager.getAllSubtaskByEpicID(epicID)
        );
        assertEquals("Список подзадач пуст.", exceptionGetAllSubtaskByEpicID.getMessage());

        final Epic savedTask = (Epic) taskManager.getTaskById(epicID);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(epic, savedTask, "Задачи не совпадают.");
        assertEquals(Task.Status.NEW, epic.getTaskStatus(), "Статус эпика не совпадает.");

        final Map<Integer, Epic> epics = taskManager.getAllEpic();

        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, (Epic) epics.get(epicID), "Задачи не совпадают при запросе из мапы.");
        assertEquals(Task.Status.NEW, epics.get(epicID).getTaskStatus(), "Статус эпика не совпадает.");
    }

    @Test
    void shouldCreateNewEpicWithNewSubtask() throws IOException {

        Epic epic = new Epic("Эпик 1", "Описание первого эпика");
        final int epicID = taskManager.createTask(epic);
        final Map<Integer, Epic> epics = Collections.unmodifiableMap(taskManager.getAllEpic());

        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(Task.Status.NEW, epic.getTaskStatus(), "Статус Epic задачи не совпадает.");

        Subtask subtask = new Subtask("Подзадача 1 от Эпика №1", "Описание первой подзадачи от Эпика №1",
                Task.Status.NEW, epicID, LocalDateTime.of(2022, JANUARY, 1, 0, 0), 1, 15);
        final int subtaskID = taskManager.createTask(subtask);

        final Epic epicFromGetById = (Epic) taskManager.getTaskById(epicID);
        final Epic epicFromEpicsMap = epics.get(epicID);
        final Subtask subtaskFromGetById = (Subtask) taskManager.getTaskById(subtaskID);

        final List<Integer> shouldBeOneSubtaskList = Collections.unmodifiableList(epic.getSubtaskList());
        final Map<Integer, Subtask> shouldBeOneSubtaskMap = Collections.unmodifiableMap(taskManager.getAllSubtaskByEpicID(epicID));

        final Subtask subtaskFromSubtaskMap = shouldBeOneSubtaskMap.get(subtaskID);

        assertNotNull(epicFromGetById, "Epic задача не найдена.");
        assertNotNull(epicFromEpicsMap, "Epic задача не найдена.");

        assertNotNull(subtaskFromGetById, "Subtask задача не найдена.");
        assertNotNull(subtaskFromSubtaskMap, "Subtask задача не найдена.");

        assertEquals(epic, epicFromGetById, "Epic задачи не совпадают.");
        assertEquals(epic, epicFromEpicsMap, "Epic задачи не совпадают при запросе из мапы.");

        assertEquals(subtask, subtaskFromGetById, "Subtask задачи не совпадают.");
        assertEquals(subtask, subtaskFromSubtaskMap, "Subtask задачи не совпадают.");

        assertEquals(Task.Status.NEW, epicFromGetById.getTaskStatus(), "Статус Epic задачи не совпадает.");
        assertEquals(Task.Status.NEW, epicFromEpicsMap.getTaskStatus(), "Статус Epic задачи не совпадает.");

        assertEquals(Task.Status.NEW, subtask.getTaskStatus(), "Статус Subtask задачи не совпадает.");
        assertEquals(Task.Status.NEW, subtaskFromGetById.getTaskStatus(), "Статус Subtask задачи не совпадает.");
        assertEquals(Task.Status.NEW, subtaskFromSubtaskMap.getTaskStatus(), "Статус Subtask задачи не совпадает.");

        assertEquals(subtask.getEpicID(), epic.getTaskID(), "Id Epic в подзадаче не совпадает");

        assertNotNull(epics, "Epic задачи на возвращаются.");
        assertNotNull(shouldBeOneSubtaskMap, "Subtask задачи на возвращаются.");

        assertEquals(1, shouldBeOneSubtaskList.size(), "Неверное количество подзадач в листе Epic.");
        assertEquals(1, shouldBeOneSubtaskMap.size(), "Неверное количество Subtask задач.");
    }

    @Test
    void shouldCreateNewEpicWithDoneSubtasks() throws IOException {

        Epic epic = new Epic("Эпик 1", "Описание первого эпика");
        final int epicID = taskManager.createTask(epic);
        final Map<Integer, Epic> epics = Collections.unmodifiableMap(taskManager.getAllEpic());

        Subtask subtask = new Subtask("Подзадача 1 от Эпика №1", "Описание первой подзадачи от Эпика №1",
                Task.Status.DONE, epicID, LocalDateTime.of(2022, JANUARY, 1, 0, 0), 1, 15);
        final int subtaskID = taskManager.createTask(subtask);

        final Epic epicFromGetById = (Epic) taskManager.getTaskById(epicID);
        final Epic epicFromEpicsMap = epics.get(epicID);

        final Subtask subtaskFromGetById = (Subtask) taskManager.getTaskById(subtaskID);

        final List<Integer> shouldBeOneSubtaskList = Collections.unmodifiableList(epic.getSubtaskList());
        final Map<Integer, Subtask> shouldBeOneSubtaskMap = Collections.unmodifiableMap(taskManager.getAllSubtaskByEpicID(epicID));

        final Subtask subtaskFromSubtaskMap = shouldBeOneSubtaskMap.get(subtaskID);

        assertNotNull(epicFromGetById, "Epic задача не найдена.");
        assertNotNull(epicFromEpicsMap, "Epic задача не найдена.");

        assertNotNull(subtaskFromGetById, "Subtask задача не найдена.");
        assertNotNull(subtaskFromSubtaskMap, "Subtask задача не найдена.");

        assertEquals(epic, epicFromGetById, "Epic задачи не совпадают.");
        assertEquals(epic, epicFromEpicsMap, "Epic задачи не совпадают при запросе из мапы.");

        assertEquals(subtask, subtaskFromGetById, "Subtask задачи не совпадают.");
        assertEquals(subtask, subtaskFromSubtaskMap, "Subtask задачи не совпадают.");

        assertEquals(Task.Status.DONE, epicFromGetById.getTaskStatus(), "Статус Epic задачи не совпадает.");
        assertEquals(Task.Status.DONE, epicFromEpicsMap.getTaskStatus(), "Статус Epic задачи не совпадает.");

        assertEquals(Task.Status.DONE, subtask.getTaskStatus(), "Статус Subtask задачи не совпадает.");
        assertEquals(Task.Status.DONE, subtaskFromGetById.getTaskStatus(), "Статус Subtask задачи не совпадает.");
        assertEquals(Task.Status.DONE, subtaskFromSubtaskMap.getTaskStatus(), "Статус Subtask задачи не совпадает.");
        assertEquals(subtask.getEpicID(), epic.getTaskID(), "Id Epic в подзадаче не совпадает");

        assertNotNull(epics, "Epic задачи на возвращаются.");
        assertNotNull(shouldBeOneSubtaskMap, "Subtask задачи на возвращаются.");

        assertEquals(1, shouldBeOneSubtaskList.size(), "Неверное количество подзадач в листе Epic.");
        assertEquals(1, shouldBeOneSubtaskMap.size(), "Неверное количество Subtask задач.");
    }

    @Test
    void shouldCreateNewEpicWithNewAndDoneSubtasks() throws IOException {

        Epic epic = new Epic("Эпик 1", "Описание первого эпика");
        final int epicID = taskManager.createTask(epic);
        final Map<Integer, Epic> epics = Collections.unmodifiableMap(taskManager.getAllEpic());

        Subtask subtaskNew = new Subtask("Подзадача NEW от Эпика №1", "Описание NEW подзадачи от Эпика №1",
                Task.Status.NEW, epicID, LocalDateTime.of(2022, JANUARY, 1, 0, 0), 1, 15);
        Subtask subtaskDone = new Subtask("Подзадача DONE от Эпика №1", "Описание DONE подзадачи от Эпика №1",
                Task.Status.DONE, epicID, LocalDateTime.of(2022, JANUARY, 1, 0, 50), 1, 45);

        final int subtaskNewID = taskManager.createTask(subtaskNew);
        final int subtaskDoneID = taskManager.createTask(subtaskDone);

        final Epic epicFromGetById = (Epic) taskManager.getTaskById(epicID);
        final Epic epicFromEpicsMap = epics.get(epicID);

        final Subtask subtaskNewFromGetById = (Subtask) taskManager.getTaskById(subtaskNewID);
        final Subtask subtaskDoneFromGetById = (Subtask) taskManager.getTaskById(subtaskDoneID);

        final List<Integer> shouldBeTwoSubtasksList = Collections.unmodifiableList(epic.getSubtaskList());
        final Map<Integer, Subtask> shouldBeTwoSubtasksMap = Collections.unmodifiableMap(taskManager.getAllSubtaskByEpicID(epicID));

        final Subtask subtaskNewFromSubtasksMap = shouldBeTwoSubtasksMap.get(subtaskNewID);
        final Subtask subtaskDoneFromSubtasksMap = shouldBeTwoSubtasksMap.get(subtaskDoneID);

        assertNotNull(epicFromGetById, "Epic задача не найдена по id.");
        assertNotNull(epicFromEpicsMap, "Epic задача не найдена в Map.");

        assertNotNull(subtaskNewFromGetById, "New Subtask задача не найдена по id.");
        assertNotNull(subtaskDoneFromGetById, "Done Subtask задача не найдена по id.");
        assertNotNull(subtaskNewFromSubtasksMap, "New Subtask задача не найдена в Map.");
        assertNotNull(subtaskNewFromSubtasksMap, "Done Subtask задача не найдена в Map.");

        assertEquals(epic, epicFromGetById, "Epic задачи не совпадают.");
        assertEquals(epic, epicFromEpicsMap, "Epic задачи не совпадают при запросе из Map.");

        assertEquals(subtaskNew, subtaskNewFromGetById, "New Subtask задачи не совпадают (Subtask from subtaskGetById).");
        assertEquals(subtaskNew, subtaskNewFromSubtasksMap, "New Subtask задачи не совпадают  (Subtask from subtasksMap).");
        assertEquals(subtaskDone, subtaskDoneFromGetById, "Done Subtask задачи не совпадают (Subtask from subtaskGetById).");
        assertEquals(subtaskDone, subtaskDoneFromSubtasksMap, "Done Subtask задачи не совпадают  (Subtask from subtasksMap).");

        assertEquals(Task.Status.IN_PROGRESS, epic.getTaskStatus(), "Статус Epic задачи не совпадает.");
        assertEquals(Task.Status.IN_PROGRESS, epicFromGetById.getTaskStatus(), "Статус Epic задачи не совпадает.");
        assertEquals(Task.Status.IN_PROGRESS, epicFromEpicsMap.getTaskStatus(), "Статус Epic задачи не совпадает.");

        assertEquals(Task.Status.NEW, subtaskNew.getTaskStatus(), "Статус New Subtask задачи не совпадает.");
        assertEquals(Task.Status.NEW, subtaskNewFromGetById.getTaskStatus(), "Статус New Subtask задачи не совпадает (Subtask from subtaskGetById).");
        assertEquals(Task.Status.NEW, subtaskNewFromSubtasksMap.getTaskStatus(), "Статус New Subtask задачи не совпадает (Subtask from subtasksMap).");

        assertEquals(Task.Status.DONE, subtaskDone.getTaskStatus(), "Статус Done Subtask задачи не совпадает.");
        assertEquals(Task.Status.DONE, subtaskDoneFromGetById.getTaskStatus(), "Статус Done Subtask задачи не совпадает (Subtask from subtaskGetById).");
        assertEquals(Task.Status.DONE, subtaskDoneFromSubtasksMap.getTaskStatus(), "Статус Done Subtask задачи не совпадает (Subtask from subtasksMap).");


        assertEquals(subtaskNew.getEpicID(), epic.getTaskID(), "Id Epic в подзадаче New не совпадает");
        assertEquals(subtaskDone.getEpicID(), epic.getTaskID(), "Id Epic в подзадаче Done не совпадает");

        assertNotNull(epics, "Epic задачи на возвращаются.");
        assertNotNull(shouldBeTwoSubtasksMap, "Subtask задачи на возвращаются.");

        assertEquals(2, shouldBeTwoSubtasksList.size(), "Неверное количество подзадач в листе Epic.");
        assertEquals(2, shouldBeTwoSubtasksMap.size(), "Неверное количество Subtask задач.");
    }

    @Test
    void shouldCreateNewEpicWithTwoInProgressSubtasks() throws IOException {

        Epic epic = new Epic("Эпик 1", "Описание первого эпика");
        final int epicID = taskManager.createTask(epic);
        final Map<Integer, Epic> epics = Collections.unmodifiableMap(taskManager.getAllEpic());

        Subtask subtaskOne = new Subtask("Подзадача NEW от Эпика №1", "Описание NEW подзадачи от Эпика №1",
                Task.Status.IN_PROGRESS, epicID, LocalDateTime.of(2022, JANUARY, 1, 0, 0), 1, 15);
        Subtask subtaskTwo = new Subtask("Подзадача DONE от Эпика №1", "Описание DONE подзадачи от Эпика №1",
                Task.Status.IN_PROGRESS, epicID, LocalDateTime.of(2022, JANUARY, 2, 0, 0), 1, 45);

        final int subtaskNewID = taskManager.createTask(subtaskOne);
        final int subtaskDoneID = taskManager.createTask(subtaskTwo);

        final Epic epicFromGetById = (Epic) taskManager.getTaskById(epicID);
        final Epic epicFromEpicsMap = epics.get(epicID);

        final Subtask subtaskOneFromGetById = (Subtask) taskManager.getTaskById(subtaskNewID);
        final Subtask subtaskTwoFromGetById = (Subtask) taskManager.getTaskById(subtaskDoneID);

        final List<Integer> shouldBeTwoSubtasksList = Collections.unmodifiableList(epic.getSubtaskList());
        final Map<Integer, Subtask> shouldBeTwoSubtasksMap = Collections.unmodifiableMap(taskManager.getAllSubtaskByEpicID(epicID));

        final Subtask subtaskOneFromSubtasksMap = shouldBeTwoSubtasksMap.get(subtaskNewID);
        final Subtask subtaskTwoFromSubtasksMap = shouldBeTwoSubtasksMap.get(subtaskDoneID);

        assertNotNull(epicFromGetById, "Epic задача не найдена по id.");
        assertNotNull(epicFromEpicsMap, "Epic задача не найдена в Map.");

        assertNotNull(subtaskOneFromGetById, "Subtask One задача не найдена по id.");
        assertNotNull(subtaskTwoFromGetById, "Subtask Two задача не найдена по id.");
        assertNotNull(subtaskOneFromSubtasksMap, "Subtask One задача не найдена в Map.");
        assertNotNull(subtaskOneFromSubtasksMap, "Subtask Two задача не найдена в Map.");

        assertEquals(epic, epicFromGetById, "Epic задачи не совпадают.");
        assertEquals(epic, epicFromEpicsMap, "Epic задачи не совпадают при запросе из Map.");

        assertEquals(subtaskOne, subtaskOneFromGetById, "Subtask One задачи не совпадают (Subtask from subtaskGetById).");
        assertEquals(subtaskOne, subtaskOneFromSubtasksMap, "Subtask One задачи не совпадают  (Subtask from subtasksMap).");
        assertEquals(subtaskTwo, subtaskTwoFromGetById, "Subtask Two задачи не совпадают (Subtask from subtaskGetById).");
        assertEquals(subtaskTwo, subtaskTwoFromSubtasksMap, "Subtask Two задачи не совпадают  (Subtask from subtasksMap).");

        assertEquals(Task.Status.IN_PROGRESS, epic.getTaskStatus(), "Статус Epic задачи не совпадает.");
        assertEquals(Task.Status.IN_PROGRESS, epicFromGetById.getTaskStatus(), "Статус Epic задачи не совпадает.");
        assertEquals(Task.Status.IN_PROGRESS, epicFromEpicsMap.getTaskStatus(), "Статус Epic задачи не совпадает.");

        assertEquals(Task.Status.IN_PROGRESS, subtaskOne.getTaskStatus(), "Статус Subtask One задачи не совпадает.");
        assertEquals(Task.Status.IN_PROGRESS, subtaskOneFromGetById.getTaskStatus(), "Статус Subtask One задачи не совпадает (Subtask from subtaskGetById).");
        assertEquals(Task.Status.IN_PROGRESS, subtaskOneFromSubtasksMap.getTaskStatus(), "Статус Subtask One задачи не совпадает (Subtask from subtasksMap).");

        assertEquals(Task.Status.IN_PROGRESS, subtaskTwo.getTaskStatus(), "Статус Subtask Two задачи не совпадает.");
        assertEquals(Task.Status.IN_PROGRESS, subtaskTwoFromGetById.getTaskStatus(), "Статус Subtask Two задачи не совпадает (Subtask from subtaskGetById).");
        assertEquals(Task.Status.IN_PROGRESS, subtaskTwoFromSubtasksMap.getTaskStatus(), "Статус Subtask Two задачи не совпадает (Subtask from subtasksMap).");


        assertEquals(subtaskOne.getEpicID(), epic.getTaskID(), "Id Epic в подзадаче One не совпадает");
        assertEquals(subtaskTwo.getEpicID(), epic.getTaskID(), "Id Epic в подзадаче Two не совпадает");

        assertNotNull(epics, "Epic задачи на возвращаются.");
        assertNotNull(shouldBeTwoSubtasksMap, "Subtask задачи на возвращаются.");

        assertEquals(2, shouldBeTwoSubtasksList.size(), "Неверное количество подзадач в листе Epic.");
        assertEquals(2, shouldBeTwoSubtasksMap.size(), "Неверное количество Subtask задач.");
    }

///////////////////////////////////////////////////  Task Test   ///////////////////////////////////////////////////////

    @Test
    public void getAllTaskTest() throws IOException {
        Task taskOne = new Task("Задача 1", "Описание первой задачи", Task.Status.NEW,
                LocalDateTime.of(2023, JANUARY, 1, 12, 0), 1, 25);
        final int taskOneID = taskManager.createTask(taskOne);
        final Map<Integer, Task> shouldBeOneTask = taskManager.getAllTask();
        final Task taskOneFromTasksMap = shouldBeOneTask.get(taskOneID);

        assertNotNull(taskOneFromTasksMap);
        assertEquals(taskOne, taskOneFromTasksMap);
        assertEquals(1, shouldBeOneTask.size());

        Task taskTwo = new Task("Задача 2", "Описание второй задачи", Task.Status.DONE,
                LocalDateTime.of(2023, JANUARY, 1, 13, 0), 1, 25);
        final int taskTwoID = taskManager.createTask(taskTwo);
        final Map<Integer, Task> shouldBeTwoTasks = taskManager.getAllTask();
        final Task taskTwoFromTasksMap = shouldBeTwoTasks.get(taskTwoID);

        assertNotNull(taskTwoFromTasksMap);
        assertEquals(taskTwo,taskTwoFromTasksMap);
        assertEquals(2, shouldBeTwoTasks.size());

        Task taskThree = new Task("Задача 3", "Описание третьей задачи", Task.Status.IN_PROGRESS,
                LocalDateTime.of(2023, JANUARY, 2, 0, 0), 1, 25);
        final int taskThreeID = taskManager.createTask(taskThree);
        final Map<Integer, Task> shouldBeThreeTasks = taskManager.getAllTask();
        final Task taskThreeFromTasksMap = shouldBeThreeTasks.get(taskThreeID);

        assertNotNull(taskThreeFromTasksMap);
        assertEquals(taskThree,taskThreeFromTasksMap);
        assertEquals(3, shouldBeThreeTasks.size());
    }

    @Test
    public void getAllTaskWithEmptyTasksMapTest() {
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.getAllTask()
        );
        assertEquals("Список задач пуст.", exception.getMessage());
    }

    @Test
    public void getTaskByIdTest() throws IOException {
        Task task = new Task("Задача 1", "Описание первой задачи", Task.Status.NEW,
                LocalDateTime.of(2022, JANUARY, 1, 0, 0), 1, 25);
        final int taskID = taskManager.createTask(task);

        final Task taskFromGetByID = taskManager.getTaskById(taskID);
        assertNotNull(taskFromGetByID);
        assertEquals(task, taskFromGetByID);
    }

    @Test
    public void getTaskByIdByIncorrectIdTest(){
        final int incorrectID = 1;
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.getTaskById(incorrectID)
        );
        assertEquals("Задача не найдена.", exception.getMessage());
    }

    @Test
    public void deleteAllTasksTest() throws IOException {
        Task taskOne = new Task("Задача 1", "Описание первой задачи", Task.Status.NEW,
                LocalDateTime.of(2022, JANUARY, 1, 0, 0), 1, 25);
        taskManager.createTask(taskOne);
        final Map<Integer, Task> shouldBeOneTask = Collections.unmodifiableMap(taskManager.getAllTask());

        assertEquals(1, shouldBeOneTask.size());

        taskManager.deleteAllTasks();
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.getAllTask()
        );

        assertEquals("Список задач пуст.", exception.getMessage());
    }

    @Test
    public void deleteAllTasksWithEmptyTasksMapTest() {
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.deleteAllTasks()
        );

        assertEquals("Список задач пуст.", exception.getMessage());
    }

    @Test
    public void deleteTaskByIdTest() throws IOException {
        Task taskOne = new Task("Задача 1", "Описание первой задачи", Task.Status.NEW,
                LocalDateTime.of(2022, JANUARY, 1, 0, 0), 1, 25);
        final int taskOneID = taskManager.createTask(taskOne);

        Task taskTwo = new Task("Задача 2", "Описание второй задачи", Task.Status.DONE,
                LocalDateTime.of(2022, APRIL, 1, 3, 0), 1, 25);
        taskManager.createTask(taskTwo);

        Task taskThree = new Task("Задача 3", "Описание третьей задачи", Task.Status.IN_PROGRESS,
                LocalDateTime.of(2022, JANUARY, 1, 5, 0), 1, 25);
        taskManager.createTask(taskThree);

        final Map<Integer, Task> shouldBeThreeTasks = taskManager.getAllTask();
        assertEquals(3, shouldBeThreeTasks.size(), "Количество задач не соотвествует.");

        taskManager.deleteTaskById(taskOneID);

        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.getTaskById(taskOneID)
        );

        assertEquals("Задача не найдена.", exception.getMessage());
        assertEquals(2, shouldBeThreeTasks.size());
    }

    @Test
    public void deleteTaskByIdByIncorrectTaskIdTest() {
        int incorrectID = 2;
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.deleteTaskById(incorrectID)
        );

        assertEquals("Задача не найдена.", exception.getMessage());
    }

    @Test
    public void updateTaskTest() throws IOException {
        Task task = new Task("Задача 1", "Описание первой задачи", Task.Status.NEW,
                LocalDateTime.of(2022, JANUARY, 1, 0, 0), 1, 25);
        final int taskID = taskManager.createTask(task);

        Task taskUpdate = new Task("Задача 1 Update", "Описание первой задачи update", Task.Status.IN_PROGRESS,
                LocalDateTime.of(2022, JANUARY, 1, 4, 0), 1, 25);

        taskManager.updateTask(taskUpdate, taskID);

        Task taskUpdateGetById = taskManager.getTaskById(taskID);

        assertNotNull(taskUpdateGetById, "Задача не найдена.");
        assertEquals(Task.Status.IN_PROGRESS, taskUpdateGetById.getTaskStatus(), "Статус задач не соотвествует.");
        assertEquals(taskUpdate, taskUpdateGetById, "Задачи не совпадают.");
    }

    @Test
    public void updateTaskIncorrectTaskIdTest() {
        int incorrectID = 3;
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.deleteTaskById(incorrectID)
        );

        assertEquals("Задача не найдена.", exception.getMessage());
    }

///////////////////////////////////////////////////  Epic Test   ///////////////////////////////////////////////////////

    @Test
    public void deleteAllEpicsTest() throws IOException {
        Epic epicOne = new Epic("Эпик 1", "Описание первого эпика");
        final int epicOneID = taskManager.createTask(epicOne);
        Epic epicTwo = new Epic("Эпик 1", "Описание первого эпика");
        final int epicTwoID = taskManager.createTask(epicTwo);
        Epic epicThree = new Epic("Эпик 1", "Описание первого эпика");
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

        final int subtaskOneByFirstEpicID = taskManager.createTask(subtaskOneByFirstEpic);
        final int subtaskTwoByFirstEpicID = taskManager.createTask(subtaskTwoByFirstEpic);
        final int subtaskOneBySecondEpicID = taskManager.createTask(subtaskOneBySecondEpic);
        final int subtaskTwoBySecondEpicID = taskManager.createTask(subtaskTwoBySecondEpic);
        final int subtaskOneByThirdEpicID = taskManager.createTask(subtaskOneByThirdEpic);

        final Map<Integer, Epic> shouldBeThreeEpicsMap = Collections.unmodifiableMap(taskManager.getAllEpic());
        final Map<Integer, Epic> shouldBeFiveSubtasksMap = Collections.unmodifiableMap(taskManager.getAllSubtask());

        assertNotNull(shouldBeThreeEpicsMap);
        assertNotNull(shouldBeFiveSubtasksMap);
        assertEquals(3, shouldBeThreeEpicsMap.size(), "Количество Epic задач не соотвествует.");
        assertEquals(5, shouldBeFiveSubtasksMap.size(), "Количество Subtask задач не соотвествует.");

        taskManager.deleteAllEpics();

        final RuntimeException getSubtaskException = assertThrows(
                RuntimeException.class,
                () -> taskManager.getTaskById(subtaskOneByFirstEpicID)
        );
        assertEquals("Задача не найдена.", getSubtaskException.getMessage());

        final RuntimeException getEpicException = assertThrows(
                RuntimeException.class,
                () -> taskManager.getTaskById(epicTwoID)
        );
        assertEquals("Задача не найдена.", getEpicException.getMessage());
    }

    @Test
    public void deleteAllEpicsWithEmptyEpicsMapTest() {
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.deleteAllEpics()
        );
        assertEquals("Список Epic задач пуст", exception.getMessage());
    }

    @Test
    public void deleteEpicByIdTest() throws IOException {
        Epic epicOne = new Epic("Эпик 1", "Описание первого эпика");
        final int epicOneID = taskManager.createTask(epicOne);

        Subtask subtaskOneByFirstEpic = new Subtask("Подзадача 1 от Эпика №1", "Описание первой подзадачи от Эпика №1",
                Task.Status.NEW, epicOneID, LocalDateTime.of(2023, JULY, 1, 0, 0), 1, 15);
        Subtask subtaskTwoByFirstEpic = new Subtask("Подзадача 2 от Эпика №1", "Описание второй подзадачи от Эпика №1",
                Task.Status.IN_PROGRESS, epicOneID, LocalDateTime.of(2022, JANUARY, 2, 0, 0), 1, 17);
        final int subtaskOneByFirstEpicID = taskManager.createTask(subtaskOneByFirstEpic);
        final int subtaskTwoByFirstEpicID = taskManager.createTask(subtaskTwoByFirstEpic);

        final Map<Integer, Epic> shouldBeOneEpicsMap = Collections.unmodifiableMap(taskManager.getAllEpic());
        final Map<Integer, Epic> shouldBeTwoSubtasksMap = Collections.unmodifiableMap(taskManager.getAllSubtask());

        assertNotNull(shouldBeOneEpicsMap);
        assertNotNull(shouldBeTwoSubtasksMap);
        assertEquals(1, shouldBeOneEpicsMap.size(), "Количество Epic задач не соотвествует.");
        assertEquals(2, shouldBeTwoSubtasksMap.size(), "Количество Subtask задач не соотвествует.");

        taskManager.deleteEpicById(epicOneID);

        final RuntimeException getSubtaskException = assertThrows(
                RuntimeException.class,
                () -> taskManager.getTaskById(subtaskOneByFirstEpicID)
        );
        assertEquals("Задача не найдена.", getSubtaskException.getMessage());

        final RuntimeException getEpicException = assertThrows(
                RuntimeException.class,
                () -> taskManager.getTaskById(epicOneID)
        );
        assertEquals("Задача не найдена.", getEpicException.getMessage());
    }

    @Test
    public void deleteEpicByIdIncorrectEpicIdTest() {
        int incorrectID = 3;
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.deleteEpicById(incorrectID)
        );

        assertEquals("Задача не найдена.", exception.getMessage());
    }

    @Test
    public void deleteAllSubtaskInEpicTest() throws IOException {
        Epic epicOne = new Epic("Эпик 1", "Описание первого эпика");
        final int epicOneID = taskManager.createTask(epicOne);

        Subtask subtaskOneByFirstEpic = new Subtask("Подзадача 1 от Эпика №1", "Описание первой подзадачи от Эпика №1",
                Task.Status.NEW, epicOneID, LocalDateTime.of(2024, JANUARY, 1, 0, 0), 1, 15);
        Subtask subtaskTwoByFirstEpic = new Subtask("Подзадача 2 от Эпика №1", "Описание второй подзадачи от Эпика №1",
                Task.Status.IN_PROGRESS, epicOneID, LocalDateTime.of(2022, JANUARY, 1, 0, 0), 1, 17);
        final int subtaskOneByFirstEpicID = taskManager.createTask(subtaskOneByFirstEpic);
        final int subtaskTwoByFirstEpicID = taskManager.createTask(subtaskTwoByFirstEpic);

        final List<Integer> subtasksList = Collections.unmodifiableList(epicOne.getSubtaskList());
        assertEquals(2, subtasksList.size());

        taskManager.deleteAllSubtaskInEpic(epicOneID);

        final List<Integer> shouldBeEmptySubtasksList = Collections.unmodifiableList(epicOne.getSubtaskList());

        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.getAllSubtask()
        );

        assertEquals("Список подзадач пуст.", exception.getMessage());
        assertEquals(0, shouldBeEmptySubtasksList.size());

        final RuntimeException exceptionOne = assertThrows(
                RuntimeException.class,
                () -> taskManager.getTaskById(subtaskOneByFirstEpicID)
        );

        assertEquals("Задача не найдена.", exceptionOne.getMessage());

        final RuntimeException exceptionTwo = assertThrows(
                RuntimeException.class,
                () -> taskManager.getTaskById(subtaskTwoByFirstEpicID)
        );

        assertEquals("Задача не найдена.", exceptionTwo.getMessage());
    }

    @Test
    public void deleteAllSubtaskInEpicWithIncorrectEpicIdTest() {
        int incorrectID = 3;
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.deleteAllSubtaskInEpic(incorrectID)
        );

        assertEquals("Задача не найдена.", exception.getMessage());
    }

    @Test
    public void deleteAllSubtaskInEpicWithEmptySubtaskListTest() throws IOException {
        Epic epicOne = new Epic("Эпик 1", "Описание первого эпика");
        final int epicOneID = taskManager.createTask(epicOne);

        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.deleteAllSubtaskInEpic(epicOneID)
        );

        assertEquals("Список подзадач пуст.", exception.getMessage());
    }

    @Test
    public void updateEpicTest() throws IOException {
        Epic epic = new Epic("Эпик 1", "Описание первого эпика");
        final int epicID = taskManager.createTask(epic);

        Epic epicUpdate = new Epic("Эпик 1 Update", "Описание первого эпика Update");
        Subtask subtaskOne = new Subtask("Подзадача 1 от Эпика №1", "Описание первой подзадачи от Эпика №1",
                Task.Status.NEW, epicID, LocalDateTime.of(2022, JANUARY, 2, 0, 0), 1, 15);
        Subtask subtaskTwo = new Subtask("Подзадача 2 от Эпика №1", "Описание второй подзадачи от Эпика №1",
                Task.Status.IN_PROGRESS, epicID, LocalDateTime.of(2022, JANUARY, 1, 0, 0), 1, 17);
        taskManager.createTask(subtaskOne);
        taskManager.createTask(subtaskTwo);

        taskManager.updateEpic(epicUpdate, epicID);

        Epic epicUpdateFromGetByID = (Epic) taskManager.getTaskById(epicID);
        assertEquals(epicUpdate, epicUpdateFromGetByID);
    }

    @Test
    public void updateEpicIncorrectIdTest() throws IOException {
        Epic epicUpdate = new Epic("Эпик 1 Update", "Описание первого эпика Update");

        int incorrectID = 7;
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.updateEpic(epicUpdate, incorrectID)
        );

        assertEquals("Задача не найдена.", exception.getMessage());
    }

    @Test
    public void getAllSubtaskByEpicIDTest() throws IOException {
        Epic epicOne = new Epic("Эпик 1", "Описание первого эпика");
        final int epicOneID = taskManager.createTask(epicOne);

        Subtask subtaskOne = new Subtask("Подзадача 1 от Эпика №1", "Описание первой подзадачи от Эпика №1",
                Task.Status.NEW, epicOneID, LocalDateTime.of(2022, JANUARY, 2, 0, 0), 1, 15);
        Subtask subtaskTwo = new Subtask("Подзадача 2 от Эпика №1", "Описание второй подзадачи от Эпика №1",
                Task.Status.IN_PROGRESS, epicOneID, LocalDateTime.of(2022, JANUARY, 1, 0, 0), 1, 17);
        final int subtaskOneID = taskManager.createTask(subtaskOne);
        final int subtaskTwoID = taskManager.createTask(subtaskTwo);

        final Map<Integer, Subtask> subtasksMap = Collections.unmodifiableMap(taskManager.getAllSubtaskByEpicID(epicOneID));

        final Subtask subtaskOneFromMap = subtasksMap.get(subtaskOneID);
        final Subtask subtaskTwoFromMap = subtasksMap.get(subtaskTwoID);

        assertNotNull(subtaskOneFromMap, "Задача не возвращается.");
        assertNotNull(subtaskTwoFromMap, "Задача не возвращается.");

        assertEquals(subtaskOne, subtaskOneFromMap, "Задачи не совпадают.");
        assertEquals(subtaskTwo, subtaskTwoFromMap, "Задачи не совпадают.");
    }

    @Test
    public void getAllSubtaskByEpicIDWithIncorrectEpicIdTest() throws IOException {
        Epic epic = new Epic("Эпик 1", "Описание первого эпика");
        final int epicID = taskManager.createTask(epic);

        int incorrectID = 7;
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.getAllSubtaskByEpicID(incorrectID)
        );

        assertEquals("Задача не найдена.", exception.getMessage());
    }

    @Test
    public void getAllSubtaskByEpicIDWithEmptyEpicsMapTest() {
        int incorrectID = 7;
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.getAllSubtaskByEpicID(incorrectID)
        );

        assertEquals("Список задач пуст.", exception.getMessage());
    }

    @Test
    public void getAllSubtaskByEpicIDWithNoSubtaskInEpicTest() throws IOException {
        Epic epic = new Epic("Эпик 1", "Описание первого эпика");
        final int epicID = taskManager.createTask(epic);

        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.getAllSubtaskByEpicID(epicID)
        );

        assertEquals("Список подзадач пуст.", exception.getMessage());
    }

    @Test
    public void getAllEpicTest() throws IOException {
        Epic epicOne = new Epic("Эпик 1", "Описание первого эпика");
        Epic epicTwo = new Epic("Эпик 1", "Описание первого эпика");
        Epic epicThree = new Epic("Эпик 1", "Описание первого эпика");

        final int epicOneID = taskManager.createTask(epicOne);
        final int epicTwoID = taskManager.createTask(epicTwo);
        final int epicThreeID = taskManager.createTask(epicThree);

        final Map<Integer, Epic> controlEpicsMap = new HashMap<>();
        controlEpicsMap.put(epicOneID, epicOne);
        controlEpicsMap.put(epicTwoID, epicTwo);
        controlEpicsMap.put(epicThreeID, epicThree);

        Subtask subtaskOneByFirstEpic = new Subtask("Подзадача 1 от Эпика №1", "Описание первой подзадачи от Эпика №1",
                Task.Status.NEW, epicOneID, LocalDateTime.of(2023, JANUARY, 1, 0, 0), 1, 15);
        Subtask subtaskTwoByFirstEpic = new Subtask("Подзадача 2 от Эпика №1", "Описание второй подзадачи от Эпика №1",
                Task.Status.IN_PROGRESS, epicOneID, LocalDateTime.of(2022, JANUARY, 1, 0, 0), 1, 17);
        Subtask subtaskOneBySecondEpic = new Subtask("Подзадача 1 от Эпика №2", "Описание первой подзадачи от Эпика №2",
                Task.Status.IN_PROGRESS, epicTwoID, LocalDateTime.of(2024, JANUARY, 1, 0, 0), 1, 27);
        Subtask subtaskTwoBySecondEpic = new Subtask("Подзадача 2 от Эпика №2", "Описание второй подзадачи от Эпика №2",
                Task.Status.DONE, epicTwoID, LocalDateTime.of(2022, JANUARY, 1, 8, 0), 1, 27);
        Subtask subtaskOneByThirdEpic = new Subtask("Подзадача 1 от Эпика №3", "Описание второй подзадачи от Эпика №3",
                Task.Status.DONE, epicThreeID, LocalDateTime.of(2022, APRIL, 1, 0, 0), 1, 23);

        final int subtaskOneByFirstEpicID = taskManager.createTask(subtaskOneByFirstEpic);
        final int subtaskTwoByFirstEpicID = taskManager.createTask(subtaskTwoByFirstEpic);
        final int subtaskOneBySecondEpicID = taskManager.createTask(subtaskOneBySecondEpic);
        final int subtaskTwoBySecondEpicID = taskManager.createTask(subtaskTwoBySecondEpic);
        final int subtaskOneByThirdEpicID = taskManager.createTask(subtaskOneByThirdEpic);

        final Map<Integer, Subtask> subtasksControlMap = new HashMap<>();
        subtasksControlMap.put(subtaskOneByFirstEpicID, subtaskOneByFirstEpic);
        subtasksControlMap.put(subtaskTwoByFirstEpicID, subtaskTwoByFirstEpic);
        subtasksControlMap.put(subtaskOneBySecondEpicID, subtaskOneBySecondEpic);
        subtasksControlMap.put(subtaskTwoBySecondEpicID, subtaskTwoBySecondEpic);
        subtasksControlMap.put(subtaskOneByThirdEpicID, subtaskOneByThirdEpic);

        Map<Integer, Epic> epicMap = Collections.unmodifiableMap(taskManager.getAllEpic());
        Map<Integer, Subtask> subtaskMap = Collections.unmodifiableMap(taskManager.getAllSubtask());

        assertNotNull(epicMap);
        assertNotNull(subtaskMap);

        for (Integer epicID : controlEpicsMap.keySet()) {
            Epic epicFromMap = epicMap.get(epicID);
            Epic epicFromControlMap = controlEpicsMap.get(epicID);

            assertNotNull(epicFromMap, "Задача не возвращается.");
            assertEquals(epicFromMap, epicFromControlMap, "Задачи не равны.");
        }

        for (Integer subtaskID : subtasksControlMap.keySet()) {
            Subtask subtaskFromMap = subtaskMap.get(subtaskID);
            Subtask subtaskFromControlMap = subtasksControlMap.get(subtaskID);

            assertNotNull(subtaskFromMap, "Задача не возвращается.");
            assertEquals(subtaskFromMap, subtaskFromControlMap, "Задачи не равны.");
        }
    }

    @Test
    public void getAllEpicWithEmptyEpicsMapTest() {
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.getAllEpic()
        );

        assertEquals("Список задач пуст.", exception.getMessage());
    }

////////////////////////////////////////////////  Subtask Test   ///////////////////////////////////////////////////////

    @Test
    public void deleteSubtaskByIdTest() throws IOException {
        Epic epic = new Epic("Эпик 1", "Описание первого эпика");
        final int epicID = taskManager.createTask(epic);

        Subtask subtaskOne = new Subtask("Подзадача 1 от Эпика №1", "Описание первой подзадачи от Эпика №1",
                Task.Status.NEW, epicID, LocalDateTime.of(2022, JANUARY, 1, 0, 0), 1, 15);
        Subtask subtaskTwo = new Subtask("Подзадача 1 от Эпика №1", "Описание первой подзадачи от Эпика №1",
                Task.Status.NEW, epicID, LocalDateTime.of(2022, JANUARY, 2, 0, 0), 1, 15);
        final int subtaskOneID = taskManager.createTask(subtaskOne);
        final int subtaskTwoID = taskManager.createTask(subtaskTwo);

        taskManager.deleteSubtaskById(subtaskTwoID);

        final List<Integer> subtasksListInEpic = Collections.unmodifiableList(epic.getSubtaskList());
        final Map<Integer, Subtask> subtasksMap = Collections.unmodifiableMap(taskManager.getAllSubtask());
        assertEquals(1, subtasksListInEpic.size());
        assertEquals(1, subtasksMap.size());

        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.getTaskById(subtaskTwoID)
        );

        assertEquals("Задача не найдена.", exception.getMessage());
    }

    @Test
    public void deleteSubtaskByIncorrectIdTest() {
        int incorrectID = 9;
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.deleteSubtaskById(incorrectID)
        );

        assertEquals("Задача не найдена.", exception.getMessage());
    }

    @Test
    public void updateSubtaskTest() throws IOException {
        Epic epic = new Epic("Эпик 1", "Описание первого эпика");
        final int epicID = taskManager.createTask(epic);

        Subtask subtask = new Subtask("Подзадача 1 от Эпика №1", "Описание первой подзадачи от Эпика №1",
                Task.Status.NEW, epicID, LocalDateTime.of(2023, JANUARY, 1, 0, 0), 1, 15);
        final int subtaskID = taskManager.createTask(subtask);

        Subtask subtaskUpdate = new Subtask("Подзадача 1 от Эпика №1 Update", "Описание первой подзадачи от Эпика №1 Update",
                Task.Status.IN_PROGRESS, epicID, LocalDateTime.of(2022, JANUARY, 1, 0, 0), 1, 45);
        taskManager.updateSubtask(subtaskUpdate, subtaskID);

        final Map<Integer, Subtask> subtasksMap = Collections.unmodifiableMap(taskManager.getAllSubtask());
        final Map<Integer, Epic> epicsMap = Collections.unmodifiableMap(taskManager.getAllEpic());

        assertNotNull(subtasksMap);
        assertNotNull(epicsMap);

        final Subtask subtaskFromMap = subtasksMap.get(subtaskID);
        final Epic epicFromMap = epicsMap.get(epicID);

        assertNotNull(subtaskFromMap);
        assertNotNull(epicFromMap);

        assertEquals(subtaskUpdate, subtaskFromMap);
        assertEquals(epic, epicFromMap);
        assertEquals(Task.Status.IN_PROGRESS, epicFromMap.getTaskStatus());
    }

    @Test
    public void updateSubtaskIncorrectIdTest() {
        int incorrectID = 11;
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.deleteSubtaskById(incorrectID)
        );

        assertEquals("Задача не найдена.", exception.getMessage());
    }

    @Test
    public void deleteAllSubtaskTest() throws IOException {
        Epic epicOne = new Epic("Эпик 1", "Описание первого эпика");
        Epic epicTwo = new Epic("Эпик 1", "Описание первого эпика");
        Epic epicThree = new Epic("Эпик 1", "Описание первого эпика");

        final int epicOneID = taskManager.createTask(epicOne);
        final int epicTwoID = taskManager.createTask(epicTwo);
        final int epicThreeID = taskManager.createTask(epicThree);

        final List<Integer> epicsIDList = new ArrayList<>(Arrays.asList(
                epicOneID,
                epicTwoID,
                epicThreeID
        ));

        Subtask subtaskOneByFirstEpic = new Subtask("Подзадача 1 от Эпика №1", "Описание первой подзадачи от Эпика №1",
                Task.Status.NEW, epicOneID, LocalDateTime.of(2023, JANUARY, 1, 0, 0), 1, 15);
        Subtask subtaskTwoByFirstEpic = new Subtask("Подзадача 2 от Эпика №1", "Описание второй подзадачи от Эпика №1",
                Task.Status.IN_PROGRESS, epicOneID, LocalDateTime.of(2024, JANUARY, 1, 0, 0), 1, 17);
        Subtask subtaskOneBySecondEpic = new Subtask("Подзадача 1 от Эпика №2", "Описание первой подзадачи от Эпика №2",
                Task.Status.IN_PROGRESS, epicTwoID, LocalDateTime.of(2022, JANUARY, 2, 0, 0), 1, 27);
        Subtask subtaskTwoBySecondEpic = new Subtask("Подзадача 2 от Эпика №2", "Описание второй подзадачи от Эпика №2",
                Task.Status.DONE, epicTwoID, LocalDateTime.of(2022, JANUARY, 1, 7, 0), 1, 27);
        Subtask subtaskOneByThirdEpic = new Subtask("Подзадача 1 от Эпика №3", "Описание второй подзадачи от Эпика №3",
                Task.Status.DONE, epicThreeID, LocalDateTime.of(2022, JANUARY, 1, 0, 45), 1, 23);

        final int subtaskOneByFirstEpicID = taskManager.createTask(subtaskOneByFirstEpic);
        final int subtaskTwoByFirstEpicID = taskManager.createTask(subtaskTwoByFirstEpic);
        final int subtaskOneBySecondEpicID = taskManager.createTask(subtaskOneBySecondEpic);
        final int subtaskTwoBySecondEpicID = taskManager.createTask(subtaskTwoBySecondEpic);
        final int subtaskOneByThirdEpicID = taskManager.createTask(subtaskOneByThirdEpic);

        final List<Integer> subtasksIDList = new ArrayList<>(Arrays.asList(
                subtaskOneByFirstEpicID,
                subtaskTwoByFirstEpicID,
                subtaskOneBySecondEpicID,
                subtaskTwoBySecondEpicID,
                subtaskOneByThirdEpicID
        ));

        final Map<Integer, Epic> shouldBeThreeEpicsMap = Collections.unmodifiableMap(taskManager.getAllEpic());
        final Map<Integer, Subtask> shouldBeFiveSubtasksMap = Collections.unmodifiableMap(taskManager.getAllSubtask());

        assertEquals(3, shouldBeThreeEpicsMap.size());
        assertEquals(5, shouldBeFiveSubtasksMap.size());

        taskManager.deleteAllSubtask();

        for (Integer id : subtasksIDList) {
            final RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> taskManager.getTaskById(id)
            );

            assertEquals("Задача не найдена.", exception.getMessage());
        }

        for (Integer id : epicsIDList) {
            List<Integer> subtasksList = shouldBeThreeEpicsMap.get(id).getSubtaskList();
            assertEquals(0, subtasksList.size());
        }

        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.getAllSubtask()
        );

        assertEquals("Список подзадач пуст.", exception.getMessage());
    }

    @Test
    public void deleteAllSubtaskWithEmptySubtasksMapTest() {
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.deleteAllSubtask()
        );

        assertEquals("Список подзадач пуст.", exception.getMessage());
    }

    @Test
    public void getAllSubtaskTaskTest() throws IOException {
        Epic epicOne = new Epic("Эпик 1", "Описание первого эпика");
        Epic epicTwo = new Epic("Эпик 1", "Описание первого эпика");
        Epic epicThree = new Epic("Эпик 1", "Описание первого эпика");

        final int epicOneID = taskManager.createTask(epicOne);
        final int epicTwoID = taskManager.createTask(epicTwo);
        final int epicThreeID = taskManager.createTask(epicThree);

        Subtask subtaskOneByFirstEpic = new Subtask("Подзадача 1 от Эпика №1", "Описание первой подзадачи от Эпика №1",
                Task.Status.NEW, epicOneID, LocalDateTime.of(2022, JANUARY, 2, 0, 0), 1, 15);
        Subtask subtaskTwoByFirstEpic = new Subtask("Подзадача 2 от Эпика №1", "Описание второй подзадачи от Эпика №1",
                Task.Status.IN_PROGRESS, epicOneID, LocalDateTime.of(2022, JANUARY, 1, 4, 0), 1, 17);
        Subtask subtaskOneBySecondEpic = new Subtask("Подзадача 1 от Эпика №2", "Описание первой подзадачи от Эпика №2",
                Task.Status.IN_PROGRESS, epicTwoID, LocalDateTime.of(2022, JANUARY, 1, 0, 50), 1, 27);
        Subtask subtaskTwoBySecondEpic = new Subtask("Подзадача 2 от Эпика №2", "Описание второй подзадачи от Эпика №2",
                Task.Status.DONE, epicTwoID, LocalDateTime.of(2023, JANUARY, 1, 0, 0), 1, 27);
        Subtask subtaskOneByThirdEpic = new Subtask("Подзадача 1 от Эпика №3", "Описание второй подзадачи от Эпика №3",
                Task.Status.DONE, epicThreeID, LocalDateTime.of(2022, APRIL, 1, 0, 0), 1, 23);

        final int subtaskOneByFirstEpicID = taskManager.createTask(subtaskOneByFirstEpic);
        final int subtaskTwoByFirstEpicID = taskManager.createTask(subtaskTwoByFirstEpic);
        final int subtaskOneBySecondEpicID = taskManager.createTask(subtaskOneBySecondEpic);
        final int subtaskTwoBySecondEpicID = taskManager.createTask(subtaskTwoBySecondEpic);
        final int subtaskOneByThirdEpicID = taskManager.createTask(subtaskOneByThirdEpic);

        final Map<Integer, Subtask> subtasksControlMap = new HashMap<>();
        subtasksControlMap.put(subtaskOneByFirstEpicID, subtaskOneByFirstEpic);
        subtasksControlMap.put(subtaskTwoByFirstEpicID, subtaskTwoByFirstEpic);
        subtasksControlMap.put(subtaskOneBySecondEpicID, subtaskOneBySecondEpic);
        subtasksControlMap.put(subtaskTwoBySecondEpicID, subtaskTwoBySecondEpic);
        subtasksControlMap.put(subtaskOneByThirdEpicID, subtaskOneByThirdEpic);

        Map<Integer, Subtask> subtaskMap = Collections.unmodifiableMap(taskManager.getAllSubtask());

        assertNotNull(subtaskMap);

        for (Integer subtaskID : subtasksControlMap.keySet()) {
            Subtask subtaskFromMap = subtaskMap.get(subtaskID);
            Subtask subtaskFromControlMap = subtasksControlMap.get(subtaskID);

            assertNotNull(subtaskFromMap, "Задача не возвращается.");
            assertEquals(subtaskFromMap, subtaskFromControlMap, "Задачи не равны.");
        }
    }

    @Test
    public void getAllSubtaskWithEmptySubtasksMapTaskTest() {
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.getAllSubtask()
        );

        assertEquals("Список подзадач пуст.", exception.getMessage());
    }
}