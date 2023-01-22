import services.history.HistoryManager;
import services.taskmanagers.InMemoryTaskManager;
import services.taskmanagers.TaskManager;
import services.history.InMemoryHistoryManager;
import models.Epic;
import models.Subtask;
import models.Task;

public class Main {


    // На счет красивого вывода, в задании не было ничего про то как чем нужно выводить. Да и сам вывод
    // тут используется только для проверки методов, поэтому я даже особо не парился.
    // Подскажите что за специализированные методы для вывода я изучу тему и буду применять.

    public static void main(String[] args) {

        TaskManager taskManager = new InMemoryTaskManager();
        HistoryManager historyManager = new InMemoryHistoryManager();

        Task taskOne = new Task("Task_1", "Task_1 Description", Task.Status.NEW);
        Task taskTwo = new Task("Task_2", "Task_2 Description", Task.Status.NEW);

        Epic epicOne = new Epic("Epic_1", "Epic_1 Description");
        Subtask subtaskOneByFirstEpic = new Subtask("SubtaskByFirstEpic_1", "Subtask_1 Description", Task.Status.NEW, 3);
        Subtask subtaskTwoByFirstEpic = new Subtask("SubtaskByFirstEpic_2", "Subtask_2 Description", Task.Status.NEW, 3);

        Epic epicTwo = new Epic("Epic_2", "Epic_2 Description");
        Subtask subtaskBySecondEpic = new Subtask("SubtaskBySecondEpic", "SubtaskBySecondEpic Description", Task.Status.NEW, 6);

        taskManager.createTask(taskOne);
        taskManager.createTask(taskTwo);

        taskManager.createEpic(epicOne);
        taskManager.createSubtask(subtaskOneByFirstEpic);
        taskManager.createSubtask(subtaskTwoByFirstEpic);

        taskManager.createEpic(epicTwo);
        taskManager.createSubtask(subtaskBySecondEpic);


        // Получение списка задач
        System.out.println("Список всех задач \n" + taskManager.getAllTask().toString());
        System.out.println("Список всех objects.Epic задач \n" + taskManager.getAllEpic().toString());
        System.out.println("Список всех objects.Subtask задач \n" + taskManager.getAllSubtask().toString());
        System.out.println("Список всех objects.Subtask задач в первом objects.Epic\n" + taskManager.getAllTaskByEpicID(3));

        // Обновление задачи
        Task taskOneUpdate = new Task("Task_1 Update", "Task_1 Description", Task.Status.IN_PROGRESS);
        taskManager.updateTask(taskOneUpdate, 1);
        System.out.println("Список всех задач \n" + taskManager.getAllTask().toString());

        Epic epicOneUpdate = new Epic("Epic_1 Update", "Epic_1 Description_Update");
        taskManager.updateEpic(epicOneUpdate,3);
        System.out.println(taskManager.getAllEpic().toString());

        Subtask subtaskTwoUpdate = new Subtask("SubtaskBySecondEpic_2 Update", "Subtask_2 Description_Update", Task.Status.IN_PROGRESS, 3);
        taskManager.updateSubtask(subtaskTwoUpdate, 5);
        System.out.println(taskManager.getAllEpic().toString());
        System.out.println(taskManager.getSubtaskById(5).toString());

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

        System.out.println("Задача id 1 " + taskManager.getTaskById(1).toString());
        System.out.println("Задача id 2 " + taskManager.getTaskById(2).toString());
        System.out.println("Задача id 2 " + taskManager.getTaskById(2).toString());
        System.out.println("Задача id 1 " + taskManager.getTaskById(1).toString());

        System.out.println("История запроса задач " + historyManager.getHistory().toString());
    }
}
