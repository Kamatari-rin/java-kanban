import services.history.HistoryManager;
import services.printmanager.PrintManager;
import services.taskmanagers.InMemoryTaskManager;
import services.taskmanagers.Managers;
import services.taskmanagers.TaskManager;
import services.history.InMemoryHistoryManager;
import models.Epic;
import models.Subtask;
import models.Task;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();
        PrintManager printManager = Managers.getDefaultPrintManager();

        Task taskOne = new Task("Задача 1", "Описание первой задачи", Task.Status.NEW);
        Task taskTwo = new Task("Задача 2", "Описание второй задачи", Task.Status.NEW);
        Task taskTree = new Task("Задача 3", "Описание третьей задачи", Task.Status.NEW);

        Epic epicOne = new Epic("Эпик 1", "Описание первого эпика");
        Subtask subtaskOneByFirstEpic = new Subtask("Подзадача 1 от Эпика №1", "Описание первой подзадачи от Эпика №1", Task.Status.NEW, 4);
        Subtask subtaskTwoByFirstEpic = new Subtask("Подзадача 2 от Эпика №1", "Описание второй подзадачи от Эпика №1", Task.Status.NEW, 4);

        Epic epicTwo = new Epic("Эпик 2", "Описание второго эпика");
        Subtask subtaskBySecondEpic = new Subtask("Подзадача 1 от Эпика №2", "Описание первой подзадачи от Эпика №2", Task.Status.NEW, 7);

        taskManager.createTask(taskOne);
        taskManager.createTask(taskTwo);
        taskManager.createTask(taskTree);

        taskManager.createEpic(epicOne);
        taskManager.createSubtask(subtaskOneByFirstEpic);
        taskManager.createSubtask(subtaskTwoByFirstEpic);

        taskManager.createEpic(epicTwo);
        taskManager.createSubtask(subtaskBySecondEpic);


        // Получение списка задач
//        System.out.println("Список всех задач \n" + taskManager.getAllTask().toString());
//        System.out.println("Список всех objects.Epic задач \n" + taskManager.getAllEpic().toString());
//        System.out.println("Список всех objects.Subtask задач \n" + taskManager.getAllSubtask().toString());
//        System.out.println("Список всех objects.Subtask задач в первом objects.Epic\n" + taskManager.getAllTaskByEpicID(3));
//
//        // Обновление задачи
//        Task taskOneUpdate = new Task("Task_1 Update", "Task_1 Description", Task.Status.IN_PROGRESS);
//        taskManager.updateTask(taskOneUpdate, 1);
//        System.out.println("Список всех задач \n" + taskManager.getAllTask().toString());
//
//        Epic epicOneUpdate = new Epic("Epic_1 Update", "Epic_1 Description_Update");
//        taskManager.updateEpic(epicOneUpdate,3);
//        System.out.println(taskManager.getAllEpic().toString());
//
//        Subtask subtaskTwoUpdate = new Subtask("SubtaskBySecondEpic_2 Update", "Subtask_2 Description_Update", Task.Status.IN_PROGRESS, 3);
//        taskManager.updateSubtask(subtaskTwoUpdate, 5);
//        System.out.println(taskManager.getAllEpic().toString());
//        System.out.println(taskManager.getSubtaskById(5).toString());

        // Удаление задачи по id
//        taskManager.deleteTaskById(2);
//        taskManager.deleteEpicById(6);
//
//        System.out.println("Список всех задач \n" + taskManager.getAllTask().toString());
//        System.out.println("Список всех objects.Epic задач \n" + taskManager.getAllEpic().toString());
//        System.out.println("Список всех objects.Subtask задач \n" + taskManager.getAllSubtask().toString());
//
//        // Удаление всех задач
//        taskManager.deleteAllTasks();
//        taskManager.deleteAllEpics();
//        taskManager.deleteAllSubtask();
//        System.out.println("Список всех задач \n" + taskManager.getAllTask().toString());
//        System.out.println("Список всех objects.Epic задач \n" + taskManager.getAllEpic().toString());
//        System.out.println("Список всех objects.Subtask задач \n" + taskManager.getAllSubtask().toString());

        // Получение задачи по id


        System.out.println("Получение задачи по id:");
        printManager.printTask(taskManager.getTaskById(1));
        printManager.printTask(taskManager.getTaskById(3));
        printManager.printTask(taskManager.getTaskById(2));

        System.out.println("История запроса задач:");
        printManager.printArrayList(taskManager.getHistory());

        System.out.println("Получение задачи по id:");
        printManager.printTask(taskManager.getEpicById(4));
        printManager.printTask(taskManager.getSubtaskById(6));
        printManager.printTask(taskManager.getSubtaskById(5));


        System.out.println("История запроса задач:");
        printManager.printArrayList(taskManager.getHistory());
        System.out.println("Получение задачи по id:");
        printManager.printTask(taskManager.getTaskById(3));
        printManager.printTask(taskManager.getSubtaskById(6));

        System.out.println("История запроса задач:");
        printManager.printArrayList(taskManager.getHistory());

        System.out.println("Удаление задачи: №2");
        taskManager.deleteTaskById(2);
        System.out.println("История запроса задач:");
        printManager.printArrayList(taskManager.getHistory());
    }
}
