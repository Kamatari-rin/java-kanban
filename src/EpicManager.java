import java.util.HashMap;
import java.util.Map;

public class EpicManager extends TaskManager{

    protected static Map<Integer, Epic> epicsList = new HashMap<>(); // Epic id, Epic

    // Создание объекта Epic
    public int createEpic(String name, String description) {
        boolean isExists = isEpicExists(name);
        if (!isExists) return 0;

        Epic newEpic = new Epic(name, description, Task.Status.NEW, id, new HashMap<Integer, Subtask>(), false);
        epicsList.put(id, newEpic);
        id++;
        return id - 1;
    }

    // Получение списка Epic задач
    public String getAllEpics() {
        String allEpics = "Список всех больших задач: ";
        for (Integer id : epicsList.keySet()) {
            Epic bufEpic = epicsList.get(id);
            allEpics += bufEpic.toString();
        }
        return allEpics;
    }

    // Получение Epic задачи по идентификатору
    public String getEpicById(int epicId) {
        if (!epicsList.containsKey(epicId)) return "Задача не найдена!";
        return epicsList.get(epicId).toString();
    }

    // Удаление всех Epic задач
    public boolean deleteAllTasks() {
        epicsList.clear();
        return true;
    }

    // Удаление Epic задачи по идентификатору
    public boolean deleteEpicById(int epicId) {
        if (!epicsList.containsKey(epicId)) {
            return false;
        } else {
            epicsList.remove(epicId);
            return true;
        }
    }

    // Удаление всех Subtask задач в выбранном Epic
    public boolean deleteAllSubtaskInEpic(int epicId) {
        if (!epicsList.containsKey(epicId)) {
            return false;
        } else {
            Epic buf = epicsList.get(epicId);
            HashMap<Integer, Subtask> bufSubtaskList = buf.getSubtaskList();
            bufSubtaskList.clear();
            return true;
        }
    }

    // Обновление. Новая обновленная версия Epic с обновленным статусом
    public void setEpicNewStatus(int epicId){
        Epic bufEpic = epicsList.get(epicId);
        HashMap<Integer, Subtask> bufSubtaskList = bufEpic.getSubtaskList();

        for (Integer subtaskId : bufSubtaskList.keySet()) {
            Subtask bufSubtask = bufSubtaskList.get(subtaskId);
            if (bufSubtask.status == Task.Status.IN_PROGRESS) {
                Epic updateEpicStatus = new Epic(bufEpic.name, bufEpic.description, Task.Status.IN_PROGRESS,
                        epicId, bufEpic.getSubtaskList(), false);
                epicsList.put(epicId, updateEpicStatus);
            } else if (bufSubtask.status == Task.Status.DONE) {

                boolean otherNewOrInProgress = true;
                for (Integer subtaskId2 : bufSubtaskList.keySet()) {
                    Subtask bufSubtask2 = bufSubtaskList.get(subtaskId2);

                    if (bufSubtask2.status == Task.Status.NEW || bufSubtask2.status == Task.Status.IN_PROGRESS) {
                        otherNewOrInProgress = false;
                    }

                    if (!otherNewOrInProgress) {
                        Epic updateEpicStatus = new Epic(bufEpic.name, bufEpic.description, Task.Status.IN_PROGRESS,
                             epicId, bufEpic.getSubtaskList(), false);
                        epicsList.put(epicId, updateEpicStatus);
                    } else {
                        Epic updateEpicStatus = new Epic(bufEpic.name, bufEpic.description, Task.Status.DONE,
                             epicId, bufEpic.getSubtaskList(), false);
                        epicsList.put(epicId, updateEpicStatus);
                 }
                }
            }
        }
    }

    private boolean isEpicExists(String name) {
        for (Integer epicID : epicsList.keySet()) {
            Epic bufEpic = epicsList.get(epicID);
            if (bufEpic.name == name) return false;
        }
        return true;
    }
}
