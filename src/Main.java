import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
        Task task = new Task("Task_1", "Task_1 Description", Task.Status.NEW);
        Epic epic = new Epic("Epic_1", "Epic_1 Description");
        Subtask subtaskOne = new Subtask("Subtask_1", "Subtask_1 Description", Task.Status.NEW, 2, 1);
        Subtask subtaskTwo = new Subtask("Subtask_2", "Subtask_2 Description", Task.Status.NEW, 2, 2);

        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtaskOne);
        taskManager.createSubtask(subtaskTwo);


        // Получение списка задач
//        System.out.println(taskManager.getAllTask());
//        System.out.println(taskManager.getAllEpic());
//        System.out.println(taskManager.getAllSubtask());
//        System.out.println(taskManager.getAllTaskByEpicID(2));


        // Получение задачи по id
//        System.out.println(taskManager.getEpicById(2));
//        System.out.println(taskManager.getTaskById(1));
//        System.out.println(taskManager.getSubtaskById(4));

        // Удаление всех задач
//        taskManager.deleteAllTasks();
//        taskManager.deleteAllEpics();
//        taskManager.deleteAllSubtask();

        // Удаление задачи по id
//        taskManager.deleteTaskById(1);
//        taskManager.deleteEpicById(2);
//        taskManager.deleteSubtaskById(4);

        // Обновление задачи
//        Task taskOneUpdate = new Task("Task_1", "Task_1 Description", Task.Status.IN_PROGRESS);
//        taskManager.updateTask(taskOneUpdate, 1);
//        System.out.println(taskManager.getAllTask());

//        Epic epicOneUpdate = new Epic("Epic_1Update", "Epic_1 Description_Update");
//        taskManager.updateEpic(epicOneUpdate,2);
//        System.out.println(taskManager.getAllEpic());

//        Subtask subtaskTwoUpdate = new Subtask("Subtask_2_Update", "Subtask_2 Description_Update", Task.Status.IN_PROGRESS, 2, 2);
//        taskManager.updateSubtask(subtaskTwoUpdate, 4);
//        System.out.println(taskManager.getAllEpic());
//        System.out.println(taskManager.getSubtaskById(4));

    }
}
