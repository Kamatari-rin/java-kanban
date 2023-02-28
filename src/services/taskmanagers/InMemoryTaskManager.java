package services.taskmanagers;

import services.history.HistoryManager;
import models.Epic;
import models.Subtask;
import models.Task;
import services.history.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> tasksMap = new HashMap<>(); // id -> objects.Task
    private Map<Integer, Epic> epicsMap = new HashMap<>();
    private Map<Integer, Subtask> subtasksMap = new HashMap<>();

    private HashMap<Integer, TaskType> allTasksMap = new HashMap<>(); // id -> taskType
    private HistoryManager historyManager = Managers.getDefaultHistory();

    private int id = 0;

    //----------------------------------------------|   objects.Task   |------------------------------------------------------//


    // Создание объекта objects.Task
    @Override
    public int createTask(Task task) throws IOException {
        task.setTaskID(++id);
        tasksMap.put(task.getTaskID(), task);
        allTasksMap.put(task.getTaskID(), TaskType.TASK);
        return task.getTaskID();
    }

    @Override
    public HashMap getAllTask() {
        HashMap<Integer, Task> printTaskMap = (HashMap<Integer, Task>) tasksMap;
        return printTaskMap;
    }

    // Получение задачи по идентификатору
    @Override
    public Task getTaskById(int id) throws IOException {
        if (!tasksMap.containsKey(id)) {
            return null;
        } else {
            Task task = tasksMap.get(id);
            historyManager.add(task);
            return task;
        }
    }

    // Удаление всех задач objects.Task
    @Override
    public boolean deleteAllTasks() throws IOException {

        for (Integer taskID : tasksMap.keySet()) {
            if (historyManager.isHistoryContainsTask(taskID)) {
                historyManager.remove(taskID);
            }
        }
        tasksMap.clear();
        return true;
    }

    // Удаление objects.Task по идентификатору
    @Override
    public boolean deleteTaskById(int id) throws IOException {
        if (tasksMap.containsKey(id)) {
            tasksMap.remove(id);
            allTasksMap.remove(id);
            historyManager.remove(id);
            return true;
        }
        return false;
    }

    // Обновление objects.Task
    @Override
    public boolean updateTask(Task task, int oldTaskID) throws IOException {
        task.setTaskID(oldTaskID);
        if (!tasksMap.containsKey(oldTaskID)) return false;
        tasksMap.put(oldTaskID, task);
        System.out.println(task.getTaskID());
        return true;
    }

    //----------------------------------------------|   objects.Epic   |------------------------------------------------------//

    // Создание объекта objects.Epic
    @Override
    public int createEpic(Epic epic) throws IOException {
        epic.setTaskID(++id);
        epicsMap.put(epic.getTaskID(), epic);
        allTasksMap.put(epic.getTaskID(), TaskType.EPIC);
        return epic.getTaskID();
    }

    @Override
    public Task getEpicById(int id) throws IOException {
        if (!epicsMap.containsKey(id)) {
            return null;
        } else {
            Task task = epicsMap.get(id);
            historyManager.add(task);
            return task;
        }
    }

    // Удаление всех objects.Epic задач
    @Override
    public boolean deleteAllEpics() throws IOException {

        for (Integer taskID : epicsMap.keySet()) {
            if (historyManager.isHistoryContainsTask(taskID)) {
                historyManager.remove(taskID);
            }
        }

        for (Integer taskID : subtasksMap.keySet()) {
            if (historyManager.isHistoryContainsTask(taskID)) {
                historyManager.remove(taskID);
            }
        }

        subtasksMap.clear();
        epicsMap.clear();
        return true;
    }

    // Удаление objects.Epic задачи по идентификатору
    @Override
    public boolean deleteEpicById(int id) throws IOException {
        if (epicsMap.containsKey(id)) {
            Epic bufEpic = epicsMap.get(id);
            ArrayList<Integer> subtaskList = (ArrayList<Integer>) bufEpic.getSubtaskList();

            for (Integer SubtaskID : subtaskList) {
                allTasksMap.remove(SubtaskID);
                subtasksMap.remove(SubtaskID);
                historyManager.remove(SubtaskID);
            }

            epicsMap.remove(id);
            historyManager.remove(id);

            return true;
        }
        return false;
    }

    // Удаление всех objects.Subtask задач в выбранном objects.Epic
    @Override
    public boolean deleteAllSubtaskInEpic(int id) throws IOException {
        if (epicsMap.containsKey(id)) {
            Epic bufEpic = epicsMap.get(id);
            ArrayList<Integer> subtaskList = (ArrayList<Integer>) bufEpic.getSubtaskList();
            for (Integer SubtaskID : subtaskList) {
                subtasksMap.remove(SubtaskID);
                allTasksMap.remove(SubtaskID);
                historyManager.remove(SubtaskID);
                return  true;
            }
        }
        return false;
    }

    // Обновление objects.Epic
    @Override
    public boolean updateEpic(Epic epic, int oldEpicID) throws IOException {
        if (!epicsMap.containsKey(oldEpicID)) return false;

        epic.setTaskID(oldEpicID);
        epicUpdateStatus(epic);

        epicsMap.put(oldEpicID, epic);

        return true;
    }

    @Override
    public Epic epicUpdateStatus(Epic epic) throws IOException {
        HashMap<Integer, Subtask> thisEpicSubtaskMap = getAllTaskByEpicID(epic.getTaskID());

        boolean areThereNewOrProgressSubtask = areNewOrProgressSubtask(thisEpicSubtaskMap);

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

    @Override
    public HashMap getAllTaskByEpicID(int epicID) {
        Epic bufEpic = epicsMap.get(epicID);
        ArrayList<Integer> subtaskList = (ArrayList<Integer>) bufEpic.getSubtaskList();
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

    // Честно говоря не знаю как будет лучше переименовать данный метод.
    // Есть ли новые задачи или задачи в прогрессе, в мапе которую ему передают? Он отвечает на этот вопрос.
    private boolean areNewOrProgressSubtask(HashMap<Integer, Subtask> subtasks) {
        for (Integer subtaskID : subtasks.keySet()) {
            Subtask bufSubtask = subtasks.get(subtaskID);
            if (bufSubtask.getTaskStatus() == Task.Status.NEW || bufSubtask.getTaskStatus() == Task.Status.IN_PROGRESS) {
                return true;
            }
        }
        return false;
    }

    // Получение списка objects.Epic задач
    @Override
    public HashMap getAllEpic() {
        HashMap<Integer, Epic> printEpicsMap = (HashMap<Integer, Epic>) epicsMap;
        return printEpicsMap;
    }

    //---------------------------------------------|   objects.Subtask   |----------------------------------------------------//

    // Создание объекта objects.Subtask
    @Override
    public int createSubtask(Subtask subtask) throws IOException {
        subtask.setTaskID(++id);
        Epic bufEpic = epicsMap.get(subtask.getEpicID());
        ArrayList<Integer> epicTaskList = (ArrayList<Integer>) bufEpic.getSubtaskList();

        epicTaskList.add(subtask.getTaskID());
        subtasksMap.put(subtask.getTaskID(), subtask);
        allTasksMap.put(subtask.getTaskID(), TaskType.SUBTASK);
        return subtask.getTaskID();
    }

    @Override
    public Task getSubtaskById(int id) throws IOException {
        if (!subtasksMap.containsKey(id)) {
            return null;
        } else {
            Task task = subtasksMap.get(id);
            historyManager.add(task);
            return task;
        }
    }

    // Удаление objects.Subtask задачи по идентификатору
    @Override
    public boolean deleteSubtaskById(int id) throws IOException {
        if (subtasksMap.containsKey(id)) {
            Subtask bufSubtask = subtasksMap.get(id);
            int epicID = bufSubtask.getEpicID();
            Epic epicBuf = epicsMap.get(epicID);
            ArrayList subtaskList = (ArrayList<Integer>) epicBuf.getSubtaskList();

            subtaskList.remove(id);
            subtasksMap.remove(id);
            allTasksMap.remove(id);
            historyManager.remove(id);
            return true;
        }
        return false;
    }

    // Обновление objects.Subtask
    @Override
    public boolean updateSubtask(Subtask subtask, int oldSubtaskID) throws IOException {
        subtask.setTaskID(oldSubtaskID);
        if (!subtasksMap.containsKey(oldSubtaskID)) return false;
        subtasksMap.put(oldSubtaskID, subtask);

        Epic bufEpic = epicUpdateStatus(epicsMap.get(subtask.getEpicID()));
        epicsMap.put(bufEpic.getTaskID(),bufEpic);
        return true;
    }

    // Удаление всех objects.Subtask
    @Override
    public boolean deleteAllSubtask() throws IOException {
        for (Integer taskID : subtasksMap.keySet()) {
            if (historyManager.isHistoryContainsTask(taskID)) {
                historyManager.remove(taskID);
            }
        }
        subtasksMap.clear();
        return true;
    }

    // Получение все objects.Subtask
    @Override
    public HashMap getAllSubtask() {
        HashMap<Integer, Subtask> printSubtasksMap = (HashMap<Integer, Subtask>) subtasksMap;
        return printSubtasksMap;
    }
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void save() throws IOException {

    }

    public Map<Integer, Task> getTasksMap() {
        return tasksMap;
    }

    public Map<Integer, Epic> getEpicsMap() {
        return epicsMap;
    }

    public Map<Integer, Subtask> getSubtasksMap() {
        return subtasksMap;
    }

    public void setTasksMap(Map<Integer, Task> tasksMap) {
        this.tasksMap = tasksMap;
    }

    public void setEpicsMap(Map<Integer, Epic> epicsMap) {
        this.epicsMap = epicsMap;
    }

    public void setSubtasksMap(Map<Integer, Subtask> subtasksMap) {
        this.subtasksMap = subtasksMap;
    }

    public void setHistoryManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public void setAllTasksMap(HashMap<Integer, TaskType> allTasksMap) {
        this.allTasksMap = allTasksMap;
    }
}
