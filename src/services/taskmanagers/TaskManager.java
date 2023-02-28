package services.taskmanagers;
import models.Epic;
import models.Subtask;
import models.Task;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TaskManager {

    // Получить историю обращения к задачам
    //----------------------------------------------|   objects.Task   |------------------------------------------------------//

    // Создание объекта objects.Task
    int createTask(Task task) throws IOException;

    // Получить все объекты Task
    Map<Integer, Task> getAllTask();

    // Получение задачи по идентификатору
    Task getTaskById(int id) throws IOException;

    // Удаление всех задач objects.Task
    boolean deleteAllTasks() throws IOException;

    // Удаление objects.Task по идентификатору
    boolean deleteTaskById(int id) throws IOException;

    // Обновление objects.Task
    boolean updateTask(Task task, int oldTaskID) throws IOException;

    //----------------------------------------------|   objects.Epic   |------------------------------------------------------//

    // Создание объекта objects.Epic
    int createEpic(Epic epic) throws IOException;

    // Получение objects.Epic по id
    Task getEpicById(int id) throws IOException;

    // Удаление всех objects.Epic задач
    boolean deleteAllEpics() throws IOException;

    // Удаление objects.Epic задачи по идентификатору
    boolean deleteEpicById(int id) throws IOException;

    // Удаление всех objects.Subtask задач в выбранном objects.Epic
    boolean deleteAllSubtaskInEpic(int id) throws IOException;

    // Обновление objects.Epic
    boolean updateEpic(Epic epic, int oldEpicID) throws IOException;

    Epic epicUpdateStatus(Epic epic) throws IOException;

    HashMap getAllTaskByEpicID(int epicID);

    // Получение списка objects.Epic задач
    Map<Integer, Epic> getAllEpic();

    //---------------------------------------------|   objects.Subtask   |----------------------------------------------------//

    // Создание объекта objects.Subtask
    int createSubtask(Subtask subtask) throws IOException;

    // Получение objects.Subtask задачи по идентификатору
    Task getSubtaskById(int id) throws IOException;

    // Удаление objects.Subtask задачи по идентификатору
    boolean deleteSubtaskById(int id) throws IOException;

    // Обновление objects.Subtask
    boolean updateSubtask(Subtask subtask, int oldSubtaskID) throws IOException;

    // Удаление всех objects.Subtask
    boolean deleteAllSubtask() throws IOException;

    // Получение все objects.Subtask
    Map<Integer, Subtask> getAllSubtask();

    List<Task> getHistory();

    void save() throws IOException;
}
