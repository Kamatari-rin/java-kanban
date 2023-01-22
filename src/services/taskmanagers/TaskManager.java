package services.taskmanagers;
import models.Epic;
import models.Subtask;
import models.Task;

import java.util.HashMap;

public interface TaskManager {

    // Получить историю обращения к задачам
    //----------------------------------------------|   objects.Task   |------------------------------------------------------//

    // Создание объекта objects.Task
    int createTask(Task task);

    // Получить все объекты Task
    HashMap getAllTask();

    // Получение задачи по идентификатору
    Task getTaskById(int id);

    // Удаление всех задач objects.Task
    boolean deleteAllTasks();

    // Удаление objects.Task по идентификатору
    boolean deleteTaskById(int id);

    // Обновление objects.Task
    boolean updateTask(Task task, int oldTaskID);

    //----------------------------------------------|   objects.Epic   |------------------------------------------------------//

    // Создание объекта objects.Epic
    int createEpic(Epic epic);

    // Получение objects.Epic по id
    Task getEpicById(int id);

    // Удаление всех objects.Epic задач
    boolean deleteAllEpics();

    // Удаление objects.Epic задачи по идентификатору
    boolean deleteEpicById(int id);

    // Удаление всех objects.Subtask задач в выбранном objects.Epic
    boolean deleteAllSubtaskInEpic(int id);

    // Обновление objects.Epic
    boolean updateEpic(Epic epic, int oldEpicID);

    Epic epicUpdateStatus(Epic epic);

    HashMap getAllTaskByEpicID(int epicID);

    // Получение списка objects.Epic задач
    HashMap getAllEpic();

    //---------------------------------------------|   objects.Subtask   |----------------------------------------------------//

    // Создание объекта objects.Subtask
    int createSubtask(Subtask subtask);

    // Получение objects.Subtask задачи по идентификатору
    Task getSubtaskById(int id);

    // Удаление objects.Subtask задачи по идентификатору
    boolean deleteSubtaskById(int id);

    // Обновление objects.Subtask
    boolean updateSubtask(Subtask subtask, int oldSubtaskID);

    // Удаление всех objects.Subtask
    boolean deleteAllSubtask();

    // Получение все objects.Subtask
    HashMap getAllSubtask();
}
