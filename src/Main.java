import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
        Task task = new Task("Task_1", "Task_1 Description", Task.TaskType.TASK, Task.Status.NEW);
        Epic epic = new Epic("Epic_1", "Epic_1 Description", Task.TaskType.EPIC, Task.Status.NEW,
                new HashMap<Integer, Subtask>(), false);
        Subtask subtaskOne = new Subtask("Subtask_1", "Subtask_1 Description", Task.TaskType.SUBTASK, Task.Status.NEW, 2, 1);
        Subtask subtaskTwo = new Subtask("Subtask_2", "Subtask_2 Description", Task.TaskType.SUBTASK, Task.Status.NEW, 2, 2);

        taskManager.createTask(task);
        taskManager.createTask(epic);
        taskManager.createTask(subtaskOne);
        taskManager.createTask(subtaskTwo);
    }
}
