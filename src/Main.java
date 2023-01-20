import managers.InMemoryTaskManager;
import managers.TaskManager;
import objects.Epic;
import objects.Subtask;
import objects.Task;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new InMemoryTaskManager();
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
        taskManager.deleteTaskById(2);
        taskManager.deleteEpicById(6);

        System.out.println("Список всех задач \n" + taskManager.getAllTask().toString());
        System.out.println("Список всех objects.Epic задач \n" + taskManager.getAllEpic().toString());
        System.out.println("Список всех objects.Subtask задач \n" + taskManager.getAllSubtask().toString());

        // Удаление всех задач
        taskManager.deleteAllTasks();
        taskManager.deleteAllEpics();
        taskManager.deleteAllSubtask();
        System.out.println("Список всех задач \n" + taskManager.getAllTask().toString());
        System.out.println("Список всех objects.Epic задач \n" + taskManager.getAllEpic().toString());
        System.out.println("Список всех objects.Subtask задач \n" + taskManager.getAllSubtask().toString());
    }
}
