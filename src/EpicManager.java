import java.util.HashMap;
import java.util.Map;

public class EpicManager extends TaskManager{

    protected static Map<Integer, Epic> epicsMap = new HashMap<>(); // Epic id, Epic

    // Получение списка Epic задач
    public Map getAllEpics() {
        return epicsMap;
    }

    // Получение Epic задачи по идентификатору
    public Epic getEpicById(int epicID) {
        if (!epicsMap.containsKey(epicID)) return null;
        return epicsMap.get(epicID);
    }

    // Удаление всех Epic задач
    public boolean deleteAllTasks() {
        epicsMap.clear();
        return true;
    }

    // Удаление Epic задачи по идентификатору
    public boolean deleteEpicById(int epicID) {
        if (!epicsMap.containsKey(epicID)) {
            return false;
        } else {
            epicsMap.remove(epicID);
            return true;
        }
    }

    // Удаление всех Subtask задач в выбранном Epic
    public boolean deleteAllSubtaskInEpic(int epicID) {
        if (!epicsMap.containsKey(epicID)) {
            return false;
        } else {
            Epic buf = epicsMap.get(epicID);
            HashMap<Integer, Subtask> bufSubtaskList = buf.getSubtaskList();
            bufSubtaskList.clear();
            return true;
        }
    }

    // Обновление. Новая обновленная версия Epic
    public void updateEpicStatus(int epicID){
        Epic bufEpic = epicsMap.get(epicID);
        HashMap<Integer, Subtask> bufSubtaskList = bufEpic.getSubtaskList();

        for (Integer subtaskId : bufSubtaskList.keySet()) {
            Subtask bufSubtask = bufSubtaskList.get(subtaskId);
            if (bufSubtask.taskStatus == Task.Status.IN_PROGRESS) {
                Epic updateEpicStatus = new Epic(bufEpic.taskName, bufEpic.taskDescription, Task.TaskType.EPIC,
                        Task.Status.IN_PROGRESS, bufEpic.getSubtaskList(), false);
                epicsMap.put(epicID, updateEpicStatus);
            } else if (bufSubtask.taskStatus == Task.Status.DONE) {

                boolean otherNewOrInProgress = true;

                for (Integer subtaskId2 : bufSubtaskList.keySet()) {
                    Subtask bufSubtask2 = bufSubtaskList.get(subtaskId2);
                    if (bufSubtask2.taskStatus == Task.Status.NEW || bufSubtask2.taskStatus == Task.Status.IN_PROGRESS) {
                        otherNewOrInProgress = false;
                    }
                }

                if (!otherNewOrInProgress) {
                    Epic updateEpicStatus = new Epic(bufEpic.taskName, bufEpic.taskDescription, Task.TaskType.EPIC,
                            Task.Status.IN_PROGRESS, bufEpic.getSubtaskList(), false);
                    epicsMap.put(epicID, updateEpicStatus);
                } else {
                    Epic updateEpicStatus = new Epic(bufEpic.taskName, bufEpic.taskDescription, Task.TaskType.EPIC,
                            Task.Status.DONE, bufEpic.getSubtaskList(), false);
                    epicsMap.put(epicID, updateEpicStatus);
                }
            }
        }
    }

    private boolean isEpicExists(Epic newEpic) {
        for (Integer epic : epicsMap.keySet()) {
            if (epic.equals(newEpic)) return true;
        }
        return false;
    }
}
