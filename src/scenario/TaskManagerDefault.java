package scenario;

import services.printmanager.PrintManager;
import services.taskmanagers.Managers;
import services.taskmanagers.TaskManager;

import models.Epic;
import models.Subtask;
import models.Task;

import java.io.IOException;
import java.util.Scanner;

public class TaskManagerDefault {
    TaskManager taskManager = Managers.getDefault();
    PrintManager printManager = Managers.getDefaultPrintManager();
    Scanner scanner = new Scanner(System.in);

    Task taskOne = new Task("Задача 1", "Описание первой задачи", Task.Status.NEW);
    Task taskTwo = new Task("Задача 2", "Описание второй задачи", Task.Status.NEW);
    Task taskTree = new Task("Задача 3", "Описание третьей задачи", Task.Status.NEW);

    Epic epicOne = new Epic("Эпик 1", "Описание первого эпика");
    Subtask subtaskOneByFirstEpic = new Subtask("Подзадача 1 от Эпика №1", "Описание первой подзадачи от Эпика №1", Task.Status.NEW, 4);
    Subtask subtaskTwoByFirstEpic = new Subtask("Подзадача 2 от Эпика №1", "Описание второй подзадачи от Эпика №1", Task.Status.NEW, 4);

    Epic epicTwo = new Epic("Эпик 2", "Описание второго эпика");
    Subtask subtaskBySecondEpic = new Subtask("Подзадача 1 от Эпика №2", "Описание первой подзадачи от Эпика №2", Task.Status.NEW, 7);

    public void createTasks() throws IOException {
        taskManager.createTask(taskOne);
        taskManager.createTask(taskTwo);
        taskManager.createTask(taskTree);
    }

    public void createEpics() throws IOException {
        taskManager.createEpic(epicOne);
        taskManager.createSubtask(subtaskOneByFirstEpic);
        taskManager.createSubtask(subtaskTwoByFirstEpic);
    }

    public void createSubtask() throws IOException {
        taskManager.createEpic(epicTwo);
        taskManager.createSubtask(subtaskBySecondEpic);
    }

    public void getAllTasks() {
        System.out.println("Список всех задач: \n");
        printManager.printMap(taskManager.getAllTask());

        System.out.println("Список всех objects.Epic задач: \n");
        printManager.printMap(taskManager.getAllEpic());

        System.out.println("Список всех objects.Subtask задач: \n");
        printManager.printMap(taskManager.getAllSubtask());

        System.out.println("Введите id Epic подзадачи которого вы хотите увидеть.");
        int epicID = scanner.nextInt();
        System.out.println("Список всех Subtask задач в " + epicID + "-ом Epic:\n");
        printManager.printMap(taskManager.getAllTaskByEpicID(epicID));
    }

    public void removeAllTask() throws IOException {
        System.out.println("Все активные Task на данный момент:");
        printManager.printMap(taskManager.getAllTask());
        System.out.println("Удаляем все Task");
        taskManager.deleteAllTasks();
        printManager.printMap(taskManager.getAllTask());

        System.out.println("Все активные Epic на данный момент:");
        printManager.printMap(taskManager.getAllEpic());
        System.out.println("Удаляем все Epic");
        taskManager.deleteAllEpics();
        printManager.printMap(taskManager.getAllEpic());

        System.out.println("Все активные Subtask на данный момент:");
        printManager.printMap(taskManager.getAllSubtask());
        System.out.println("Удаляем все Subtask");
        taskManager.deleteAllSubtask();
        printManager.printMap(taskManager.getAllSubtask());
    }

    public void removeTaskById() throws IOException {
        System.out.println("Все активные Task на данный момент:");
        printManager.printMap(taskManager.getAllTask());
        System.out.println("Введите id Task которую хотите удалить.");
        int taskID = scanner.nextInt();
        System.out.println("Удаляем Task " + taskID + "...");
        taskManager.deleteTaskById(taskID);
        System.out.println("Все активные Task на данный момент:");
        printManager.printMap(taskManager.getAllTask());

        System.out.println("Все активные Epic на данный момент:");
        printManager.printMap(taskManager.getAllEpic());
        System.out.println("Введите id Epic которую хотите удалить.");
        taskID = scanner.nextInt();
        System.out.println("Удаляем Epic" + taskID + "...");
        taskManager.deleteEpicById(taskID);
        System.out.println("Все активные Epic на данный момент:");
        printManager.printMap(taskManager.getAllEpic());

        System.out.println("Все активные Subtask на данный момент:");
        printManager.printMap(taskManager.getAllSubtask());
        System.out.println("Введите id Subtask которую хотите удалить.");
        taskID = scanner.nextInt();
        System.out.println("Удаляем Subtask" + taskID + "...");
        taskManager.deleteSubtaskById(taskID);
        System.out.println("Все активные Subtask на данный момент:");
        printManager.printMap(taskManager.getAllSubtask());
    }

    public void updateTaskScenario() throws IOException {

        System.out.println("Все активные Task на данный момент:");
        printManager.printMap(taskManager.getAllTask());

        System.out.println("Введите id Task которую хотите изменить.");
        int taskID = scanner.nextInt();
        System.out.println("Введите новое имя:");
        String newTaskName = scanner.next();
        System.out.println("Введите введите новое описание:");
        String newTaskDescription = scanner.next();
        System.out.println("Введите новый статус (NEW, IN_PROGRESS, DONE):");
        String newTaskStatus = scanner.next();

        switch (newTaskStatus) {
            case "NEW": {
                Task taskUpdate = new Task(newTaskName, newTaskDescription, Task.Status.NEW);
                taskManager.updateTask(taskUpdate, taskID);
                break;
            }
            case "IN_PROGRESS": {
                Task taskUpdate = new Task(newTaskName, newTaskDescription, Task.Status.IN_PROGRESS);
                taskManager.updateTask(taskUpdate, taskID);
                break;
            }
            case "DONE": {
                Task taskUpdate = new Task(newTaskName, newTaskDescription, Task.Status.DONE);
                taskManager.updateTask(taskUpdate, taskID);
            }
        }

        printManager.printTask(taskManager.getTaskById(taskID));
    }
}
