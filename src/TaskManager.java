import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {

    private Map<Integer, Task> tasksMap = new HashMap<>(); // id -> Task
    private Map<Integer, Epic> epicsMap = new HashMap<>();
    private Map<Integer, Subtask> subtasksMap = new HashMap<>();

    private HashMap<Integer, TaskType> allTasksMap = new HashMap<>(); // id -> taskType
    private enum TaskType {
        TASK,
        EPIC,
        SUBTASK
    }

    private int id = 0;

    //----------------------------------------------|          |------------------------------------------------------//
    //----------------------------------------------|   Task   |------------------------------------------------------//
    //----------------------------------------------|          |------------------------------------------------------//

    // Создание объекта Task
    public int createTask(Task task) {
        task.setTaskID(++id);
        tasksMap.put(task.getTaskID(), task);
        allTasksMap.put(task.getTaskID(), TaskType.TASK);
        return task.getTaskID();
    }

    // Получение списка задач Task
    // По ТЗ вообще не понятно было что нужно возвращать.
    // Почему возвращать Map является плохой практикой?
    // Не понял что вы имели ввиду под заданием параметров для возвращаемой Map, как можно задать параметры?
    public HashMap getAllTask() {
        HashMap<Integer, Task> printTaskMap = (HashMap<Integer, Task>) tasksMap;
        return printTaskMap;
    }

    // Получение задачи по идентификатору
    public Task getTaskById(int id) {
        if (!tasksMap.containsKey(id)) {
            return null;
        }
        Task task = tasksMap.get(id);
        return task;
    }

    // Удаление всех задач Task
    public boolean deleteAllTasks() {
        tasksMap.clear();
        return true;
    }

    // Удаление Task по идентификатору
    public boolean deleteTaskById(int id) {
        if (tasksMap.containsKey(id)) {
            tasksMap.remove(id);
            allTasksMap.remove(id);
            return true;
        }
        return false;
    }

    // Обновление Task
    public boolean updateTask(Task task, int oldTaskID) {
        task.setTaskID(oldTaskID);
        if (!tasksMap.containsKey(oldTaskID)) return false;
        tasksMap.put(oldTaskID, task);
        System.out.println(task.getTaskID());
        return true;
    }

    //----------------------------------------------|          |------------------------------------------------------//
    //----------------------------------------------|   Epic   |------------------------------------------------------//
    //----------------------------------------------|          |------------------------------------------------------//

    // Создание объекта Epic
    public int createEpic(Epic epic) {
        epic.setTaskID(++id);
        epicsMap.put(epic.getTaskID(), epic);
        allTasksMap.put(epic.getTaskID(), TaskType.EPIC);
        return epic.getTaskID();
    }

    // Получение Epic по id
    public Epic getEpicById(int id) {
        if (!epicsMap.containsKey(id)) {
            return null;
        }
        Epic epic = epicsMap.get(id);
        return epic;
    }

    // Удаление всех Epic задач
    public boolean deleteAllEpics() {
        epicsMap.clear();
        return true;
    }

    // Удаление Epic задачи по идентификатору
    public boolean deleteEpicById(int id) {
        if (epicsMap.containsKey(id)) {
            Epic bufEpic = epicsMap.get(id);
            ArrayList<Integer> subtaskList = (ArrayList<Integer>) bufEpic.getSubtaskList();

            for (Integer SubtaskID : subtaskList) {
                allTasksMap.remove(SubtaskID);
                subtasksMap.remove(SubtaskID);
            }

            epicsMap.remove(id);
            return true;
        }
        return false;
    }

    // Удаление всех Subtask задач в выбранном Epic
    public boolean deleteAllSubtaskInEpic(int id) {
        if (epicsMap.containsKey(id)) {
            Epic bufEpic = epicsMap.get(id);
            ArrayList<Integer> subtaskList = (ArrayList<Integer>) bufEpic.getSubtaskList();
            for (Integer SubtaskID : subtaskList) {
                subtasksMap.remove(SubtaskID);
                allTasksMap.remove(SubtaskID);
                return  true;
            }
        }
        return false;
    }

    // Обновление Epic
    public boolean updateEpic(Epic epic, int oldEpicID) {
        if (!epicsMap.containsKey(oldEpicID)) return false;

        epic.setTaskID(oldEpicID);
        epicUpdateStatus(epic);

        epicsMap.put(oldEpicID, epic);

        return true;
    }

    public Epic epicUpdateStatus(Epic epic) {
        HashMap<Integer, Subtask> thisEpicSubtaskMap = getAllTaskByEpicID(epic.getTaskID());

        boolean areThereNewOrProgressSubtask = areThereNewOrProgressSubtask(thisEpicSubtaskMap);

        if (!areThereNewOrProgressSubtask) {
            epic.setTaskStatus(Task.Status.DONE);
        } else {
            for (Integer subtaskID : thisEpicSubtaskMap.keySet()) {
                Subtask bufSubtask = thisEpicSubtaskMap.get(subtaskID);
                if (bufSubtask.getTaskStatus() == Task.Status.DONE || bufSubtask.getTaskStatus() == Task.Status.IN_PROGRESS) {
                    epic.setTaskStatus(Task.Status.IN_PROGRESS);
                }
            }
        }
        return epic;
    }

    public HashMap getAllTaskByEpicID(int epicID) {
        Epic bufEpic = epicsMap.get(epicID);
        ArrayList<Integer> subtaskList = bufEpic.getSubtaskList();
        HashMap<Integer, Subtask> thisEpicSubtaskMap = new HashMap<>();

        for (Integer subtaskID : subtasksMap.keySet()) {
            for (Integer subtaskIDInEpic : subtaskList) {
                if (subtaskID == subtaskIDInEpic) {
                    thisEpicSubtaskMap.put(subtaskID, subtasksMap.get(subtaskID));
                }
            }
        }
        return thisEpicSubtaskMap;
    }

    private boolean areThereNewOrProgressSubtask(HashMap<Integer, Subtask> subtasks) {
        for (Integer subtaskID : subtasks.keySet()) {
            Subtask bufSubtask = subtasks.get(subtaskID);
            if (bufSubtask.getTaskStatus() == Task.Status.NEW || bufSubtask.getTaskStatus() == Task.Status.IN_PROGRESS) {
                return true;
            }
        }
        return false;
    }

    // Получение списка Epic задач
    public HashMap getAllEpic() {
        HashMap<Integer, Epic> printEpicsMap = (HashMap<Integer, Epic>) epicsMap;
        return printEpicsMap;
    }

    //---------------------------------------------|             |----------------------------------------------------//
    //---------------------------------------------|   Subtask   |----------------------------------------------------//
    //---------------------------------------------|             |----------------------------------------------------//

    // Создание объекта Subtask
    public int createSubtask(Subtask subtask) {
        subtask.setTaskID(++id);
        Epic bufEpic = epicsMap.get(subtask.getEpicID());
        ArrayList<Integer> epicTaskList = bufEpic.getSubtaskList();

        epicTaskList.add(subtask.getTaskID());
        subtasksMap.put(subtask.getTaskID(), subtask);
        allTasksMap.put(subtask.getTaskID(), TaskType.SUBTASK);
        return subtask.getTaskID();
    }

    // Получение Subtask задачи по идентификатору
    public Subtask getSubtaskById(int id) {
        if (!subtasksMap.containsKey(id)) {
            return null;
        }
        Subtask subtask = subtasksMap.get(id);
        return subtask;
    }

    // Удаление Subtask задачи по идентификатору
    public boolean deleteSubtaskById(int id) {
        if (subtasksMap.containsKey(id)) {
            Subtask bufSubtask = subtasksMap.get(id);
            int epicID = bufSubtask.getEpicID();
            Epic epicBuf = epicsMap.get(epicID);
            ArrayList subtaskList = epicBuf.getSubtaskList();

            subtaskList.remove(id);
            subtasksMap.remove(id);
            allTasksMap.remove(id);
            return true;
        }
        return false;
    }

    // Обновление Subtask
    public boolean updateSubtask(Subtask subtask, int oldSubtaskID) {
        subtask.setTaskID(oldSubtaskID);
        if (!subtasksMap.containsKey(oldSubtaskID)) return false;
        subtasksMap.put(oldSubtaskID, subtask);

        Epic bufEpic = epicUpdateStatus(epicsMap.get(subtask.getEpicID()));
        epicsMap.put(bufEpic.getTaskID(),bufEpic);
        return true;
    }

    // Удаление всех Subtask
    public boolean deleteAllSubtask() {
        subtasksMap.clear();
        return true;
    }

    // Получение все Subtask
    public HashMap getAllSubtask() {
        HashMap<Integer, Subtask> printSubtasksMap = (HashMap<Integer, Subtask>) subtasksMap;
        return printSubtasksMap;
    }
}
