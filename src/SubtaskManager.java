import java.util.ArrayList;
import java.util.HashMap;
public class SubtaskManager extends EpicManager {

    // Создание объекта Subtask
    public int createSubTask(String name, String description, int whatEpic, int priority) {
        boolean isExists = isSubtaskExists(whatEpic,name);
        if (!isExists) return 0;
        Subtask newSubTask = new Subtask(name, description, id, Task.Status.NEW, whatEpic, priority);

        Epic bufEpic = epicsList.get(whatEpic);
        HashMap<Integer, Subtask> addNewSubtask = bufEpic.getSubtaskList();
        addNewSubtask.put(id, newSubTask);
        epicsList.put(whatEpic, bufEpic);

        id++;
        return id - 1;
    }
    // Получение Subtask задачи по идентификатору
    public String getSubtaskById(int epicId, int subtaskId) {
        if (!epicsList.containsKey(epicId)) return "Задача не найдена!";
        Epic bufEpic = epicsList.get(epicId);
        HashMap<Integer, Subtask> bufSubtaskList = bufEpic.getSubtaskList();
        return bufSubtaskList.get(subtaskId).toString();
    }
    // Удаление Subtask задачи по идентификатору
    public boolean deleteSubtaskById(int epicId, int subtaskId) {
        if (!epicsList.containsKey(epicId)) return false;

        Epic bufEpic = epicsList.get(epicId);
        HashMap<Integer, Subtask> bufSubtaskList = bufEpic.getSubtaskList();
        bufSubtaskList.remove(subtaskId);
        return true;
    }
    // Обновление. Новая обновленная версия Subtask с обновленным статусом
    public boolean updateSubtaskStatus(int epicId, int subtaskId, Task.Status subtaskStatus) {
        if (!epicsList.containsKey(epicId)) return false;

        Epic bufEpic = epicsList.get(epicId);
        HashMap<Integer, Subtask> bufSubtaskList = bufEpic.getSubtaskList();
        Subtask bufSubtask = bufSubtaskList.get(subtaskId);

        Subtask newSubTask = new Subtask(bufSubtask.name, bufSubtask.description, bufSubtask.id,
                subtaskStatus, epicId, bufSubtask.getPriority());
        bufSubtaskList.put(subtaskId, newSubTask);
        epicsList.put(epicId, bufEpic);
        setEpicNewStatus(epicId);
        return true;
    }

    private boolean isSubtaskExists(int epicID, String name) {
        Epic bufEpic = epicsList.get(epicID);
        HashMap<Integer, Subtask> bufSubtaskList = bufEpic.getSubtaskList();

        for (Integer subtaskId : bufSubtaskList.keySet()) {
            Subtask bufSubtask = bufSubtaskList.get(subtaskId);
            if (bufSubtask.name == name) return false;
        }
        return true;
    }
}
