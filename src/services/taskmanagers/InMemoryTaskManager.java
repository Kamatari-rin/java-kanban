package services.taskmanagers;

import services.history.HistoryManager;
import models.Epic;
import models.Subtask;
import models.Task;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

public class InMemoryTaskManager<T extends Task> implements TaskManager<T> {
    protected Map<Integer, Task> tasksMap = new HashMap<>(); // id -> objects.Task
    protected Map<Integer, Epic> epicsMap = new HashMap<>();
    protected Map<Integer, Subtask> subtasksMap = new HashMap<>();
    protected HistoryManager historyManager = Managers.getDefaultHistory();

    protected Comparator<Task> comparator = (taskOne, taskTwo) -> {
        if (taskOne.equals(taskTwo)) return 1;
        if (taskOne.getTaskStartTime().isAfter(taskTwo.getGetTaskEndTime())
                && taskTwo.getTaskStartTime().isBefore(taskOne.getGetTaskEndTime())) {
            return 1;
        } else if (taskOne.getTaskStartTime().isBefore(taskTwo.getGetTaskEndTime())
                && taskTwo.getTaskStartTime().isAfter(taskOne.getGetTaskEndTime())) {
            return -1;
        } else throw new RuntimeException("Время начала выполнения пересекается " +
                "с уже существующей задачей.");
    };
    protected Set<Task> prioritizedTasks = new TreeSet<>(comparator);
    private int id = 0;

    //----------------------------------------------|   objects.Task   |------------------------------------------------------//
    // Создание объекта objects.Task
    @Override
    public int createTask(T task) throws IOException, RuntimeException {
        task.setTaskID(++id);

        String taskClass = "";
        if (task.getClass() == Task.class) {
            taskClass = "Task";
        } else if (task.getClass() == Epic.class) {
            taskClass = "Epic";
        } else taskClass = "Subtask";

        switch (taskClass) {
            case "Task":
                prioritizedTasks.add(task);
                tasksMap.put(task.getTaskID(), task);
                break;
            case "Epic":
                epicsMap.put(task.getTaskID(), (Epic) task);
                break;
            case "Subtask":
                subtasksMap.put(task.getTaskID(), (Subtask) task);
                Epic epic = epicsMap.get(((Subtask) task).getEpicID());
                try {
                    epic.getSubtaskList();
                } catch (RuntimeException e) {
                    epic.setSubtaskList(new ArrayList<Integer>());
                }
                List<Integer> subtasksList = epic.getSubtaskList();
                subtasksList.add(task.getTaskID());
                prioritizedTasks.add(task);
                epicsMap.put(epic.getTaskID(), epicUpdateStatus(epic));
        }
        return task.getTaskID();
    }

    @Override
    public Map<Integer, Task> getAllTask() {
        if (tasksMap.isEmpty()) {
            throw new RuntimeException("Список задач пуст.");
        }
        return (Map<Integer, Task>) Collections.unmodifiableMap(tasksMap);
    }

    // Получение задачи по идентификатору
    @Override
    public Task getTaskById(int id) throws IOException {
        if (tasksMap.containsKey(id)) {
            historyManager.add(tasksMap.get(id));
            return (T) tasksMap.get(id);
        } else if (epicsMap.containsKey(id)) {
            historyManager.add(epicsMap.get(id));
            return (T) epicsMap.get(id);
        } else if (subtasksMap.containsKey(id)) {
            historyManager.add(subtasksMap.get(id));
            return (T) subtasksMap.get(id);
        } else {
            throw new RuntimeException("Задача не найдена.");
        }
    }

    // Удаление всех задач objects.Task
    @Override
    public boolean deleteAllTasks() throws IOException {
        if (tasksMap.isEmpty()) throw new RuntimeException("Список задач пуст.");
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
            if (historyManager.isHistoryContainsTask(id)){
                historyManager.remove(id);
            }
            tasksMap.remove(id);
            return true;
        } else throw new RuntimeException("Задача не найдена.");
    }

    // Обновление Task/Epic/Subtask
    @Override
    public boolean updateTask(Task task, int oldTaskID) throws IOException {
        if (!tasksMap.containsKey(oldTaskID)) throw new RuntimeException("Задача не найдена.");
        task.setTaskID(oldTaskID);
        tasksMap.put(oldTaskID, task);
        return true;
    }

    //----------------------------------------------|   objects.Epic   |------------------------------------------------------//

    // Удаление всех objects.Epic задач
    @Override
    public boolean deleteAllEpics() throws IOException {
        if (epicsMap.isEmpty()) throw new RuntimeException("Список Epic задач пуст");

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
    public boolean deleteEpicById(final int id) throws IOException {
        if (epicsMap.containsKey(id)) {
            Epic bufEpic = (Epic) epicsMap.get(id);
            List<Integer> subtaskList = bufEpic.getSubtaskList();

            for (Integer SubtaskID : subtaskList) {
                subtasksMap.remove(SubtaskID);
                if (historyManager.isHistoryContainsTask(SubtaskID)) {
                    historyManager.remove(SubtaskID);
                }
            }

            epicsMap.remove(id);

            if (historyManager.isHistoryContainsTask(id)) {
                historyManager.remove(id);
            }

            return true;
        } throw new RuntimeException("Задача не найдена.");
    }

    // Удаление всех objects.Subtask задач в выбранном objects.Epic
    @Override
    public boolean deleteAllSubtaskInEpic(int id) throws IOException {
        if (epicsMap.containsKey(id)) {
            Epic bufEpic = (Epic) epicsMap.get(id);
            List<Integer> subtaskList = bufEpic.getSubtaskList();
            if (subtaskList == null || subtaskList.isEmpty()) {
                throw new RuntimeException("Подзадач не найдено.");
            }
            for (Integer SubtaskID : subtaskList) {
                subtasksMap.remove(SubtaskID);
                if (historyManager.isHistoryContainsTask(SubtaskID)) {
                    historyManager.remove(SubtaskID);
                }
            }

            subtaskList.clear();
            return  true;
        } throw new RuntimeException("Задача не найдена.");
    }

    // Обновление objects.Epic
    @Override
    public boolean updateEpic(Epic epic, int oldEpicID) throws IOException {
        if (!epicsMap.containsKey(oldEpicID)) throw new RuntimeException("Задача не найдена.");
        epic.setTaskID(oldEpicID);
        epicUpdateStatus(epic);
        epicsMap.put(oldEpicID, epic);
        return true;
    }

    protected Epic epicUpdateStatus(Epic epic) {

        Map<Integer, Subtask> thisEpicSubtaskMap = getAllSubtaskByEpicID(epic.getTaskID());
        boolean areThereNewOrProgressSubtask = areThereAnyNewOrProgressSubtasks(thisEpicSubtaskMap);

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
    public Map<Integer, Subtask> getAllSubtaskByEpicID(final int epicID) {
        if (epicsMap.isEmpty()) throw new RuntimeException("Список задач пуст.");
        if (!epicsMap.containsKey(epicID)) throw new RuntimeException("Задача не найдена.");

        Epic bufEpic = epicsMap.get(epicID);
        List<Integer> subtaskList = bufEpic.getSubtaskList();
        if (subtaskList == null || subtaskList.isEmpty()) throw new RuntimeException("Список подзадач пуст.");

        Map<Integer, Subtask> thisEpicSubtaskMap = new HashMap<>();

        for (Integer subtaskID : subtasksMap.keySet()) {
            for (Integer subtaskIDInEpic : subtaskList) {
                if (subtaskID == subtaskIDInEpic) {
                    thisEpicSubtaskMap.put(subtaskID, (Subtask) subtasksMap.get(subtaskID));
                }
            }
        }
        return thisEpicSubtaskMap;
    }

    private boolean areThereAnyNewOrProgressSubtasks(Map<Integer, Subtask> subtasks) {
        for (Integer subtaskID : subtasks.keySet()) {
            Subtask bufSubtask = subtasks.get(subtaskID);
            if (bufSubtask.getTaskStatus() == Task.Status.NEW || bufSubtask.getTaskStatus() == Task.Status.IN_PROGRESS) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Map<Integer, Epic> getAllEpic() {
        if (epicsMap == null || epicsMap.isEmpty()) throw new RuntimeException("Список задач пуст.");
        return (Map<Integer, Epic>) epicsMap;
    }

    //---------------------------------------------|   objects.Subtask   |----------------------------------------------------//

    // Удаление objects.Subtask задачи по идентификатору
    @Override
    public boolean deleteSubtaskById(int id) throws IOException {
        if (subtasksMap.containsKey(id)) {
            Subtask bufSubtask = (Subtask) subtasksMap.get(id);
            int epicID = bufSubtask.getEpicID();
            Epic epicBuf = (Epic) epicsMap.get(epicID);
            List subtaskList = epicBuf.getSubtaskList();

            subtaskList.remove(Integer.valueOf(id));
            subtasksMap.remove(id);

            if (historyManager.isHistoryContainsTask(id)){
                historyManager.remove(id);
            }
            return true;
        } throw new RuntimeException("Задача не найдена.");
    }

    // Обновление objects.Subtask
    @Override
    public boolean updateSubtask(Subtask subtask, int oldSubtaskID) throws IOException {
        if (!subtasksMap.containsKey(oldSubtaskID)) throw new RuntimeException("Задача не найдена.");

        subtask.setTaskID(oldSubtaskID);
        subtasksMap.put(oldSubtaskID, subtask);

        Epic bufEpic = epicUpdateStatus(epicsMap.get(subtask.getEpicID()));
        epicsMap.put(bufEpic.getTaskID(),bufEpic);
        return true;
    }

    // Удаление всех objects.Subtask
    @Override
    public boolean deleteAllSubtask() throws IOException {
        if (subtasksMap.isEmpty()) throw new RuntimeException("Список подзадач пуст.");
        for (Integer subtaskID: subtasksMap.keySet()) {
            Subtask subtask = (Subtask) subtasksMap.get(subtaskID);
            int epicID = subtask.getEpicID();

            Epic epic = (Epic) epicsMap.get(epicID);
            List subtaskList = epic.getSubtaskList();
            subtaskList.remove(Integer.valueOf(subtaskID));

            if (historyManager.isHistoryContainsTask(subtaskID)) {
                historyManager.remove(subtaskID);
            }
        }
        subtasksMap.clear();
        return true;
    }

    // Получение все objects.Subtask
    @Override
    public Map<Integer, Subtask> getAllSubtask() {
        if (subtasksMap.isEmpty()) throw new RuntimeException("Список подзадач пуст.");
        return (Map<Integer, Subtask>) subtasksMap;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        if (prioritizedTasks.isEmpty()) throw new RuntimeException("Список задач пуст.");
        return prioritizedTasks;
    }

    @Override
    public void save() throws IOException {

    }
}
