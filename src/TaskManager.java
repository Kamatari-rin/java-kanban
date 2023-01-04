import java.util.HashMap;
import java.util.Map;

public class TaskManager {

    private Map<Integer, Task> tasksMap = new HashMap<>(); // Ключ - id
    protected static int id = 0;

    // Создание объекта Task
    // Долго думал что метод должен возвращать. Решил что нужно возвращать id задачи, так как с помощью него будет
    // осуществляться взаимодействие с другими методами.
    public int createTask(Task task) {
        if (isTaskExists(task)) return 0;
        task.setTaskID(++id);
        tasksMap.put(task.getTaskID(), task);
        return task.getTaskID();
    }

    // Получение списка задач
    public Map getAllTask() {
        return tasksMap;
    }

    // Получение задачи по идентификатору
    public Task getTaskById(int id) {
        if (!tasksMap.containsKey(id)) {
            return null;
        }
        Task task = tasksMap.get(id);
        return task;
    }

    // Удаление всех задач
    public boolean deleteAllTasks() {
        tasksMap.clear();
        return true;
    }

    // Удаление по идентификатору
    public boolean deleteTaskById(int id) {
        if (tasksMap.containsKey(id)) {
            tasksMap.remove(id);
            return true;
        }
        return false;
    }

    // Обновление. Новая обновленная версия
    public boolean updateTask(Task task, int oldTaskID) {
        if (!tasksMap.containsKey(oldTaskID)) return false;
        tasksMap.put(oldTaskID, task);
        return true;
    }

    private boolean isTaskExists(Task newTask) {
        for (Integer task : tasksMap.keySet()) {
            if (task.equals(newTask)) return true;
        }
        return false;
    }
}
