import java.util.HashMap;
import java.util.Map;

public class TaskManager {

    private Map<Integer, Task> tasksList = new HashMap<>(); // Ключ - id
    protected static int id = 1;

    // Создание объекта Task
    // Долго думал что метод должен возвращать. Решил что нужно возвращать id задачи, так как с помощью него будет
    // осуществляться взаимодействие с другими методами.
    public int createTask(String name, String description) {
        boolean isTaskExists = isTaskExists(name);
        if (!isTaskExists) return 0;

        Task newTask = new Task(name, description, id, Task.Status.NEW);
        tasksList.put(id, newTask);
        id++;
        return id - 1;
    }

    // Получение списка задач
    public String getAllTask() {
        String allTask = "Список всех задач: ";
        for (Integer id : tasksList.keySet()) {
            Task bufTask = tasksList.get(id);
            allTask += bufTask.toString();
        }
        return allTask;
    }

    // Получение задачи по идентификатору
    public String getTaskById(int id) {
        if (!tasksList.containsKey(id)) {
            return "Задача не найдена.";
        }
        Task bufTask = tasksList.get(id);
        return bufTask.toString();
    }

    // Удаление всех задач
    public boolean deleteAllTasks() {
        tasksList.clear();
        return true;
    }

    // Удаление по идентификатору
    public boolean deleteTaskById(int id) {
        if (tasksList.containsKey(id)) {
            tasksList.remove(id);
            return true;
        }
        return false;
    }

    // Обновление. Новая обновленная версия с обновленным статусом
    public boolean updateTaskStatus(int taskId, Task.Status status) {
        if (!tasksList.containsKey(taskId)) return false;

        Task bufTask = tasksList.get(taskId);
        Task updateTask = new Task(bufTask.name, bufTask.description, taskId, status);
        tasksList.put(taskId, updateTask);
        return true;
    }

    private boolean isTaskExists(String name) {
        for (Integer epicID : tasksList.keySet()) {
            Task bufTask = tasksList.get(epicID);
            if (bufTask.name == name) return false;
        }
        return true;
    }
}
