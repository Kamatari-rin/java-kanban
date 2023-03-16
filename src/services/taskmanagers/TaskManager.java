package services.taskmanagers;
import models.Epic;
import models.Subtask;
import models.Task;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TaskManager<T extends Task> {

    // Получить историю обращения к задачам
    //----------------------------------------------|   objects.Task   |------------------------------------------------------//

    // Создание объекта objects.Task
    int createTask(T task) throws IOException;

    // Получить все объекты Task
    Map<Integer, Task> getAllTask();

    // Получение задачи по идентификатору
    Task getTaskById(int id) throws IOException;

    // Удаление всех задач objects.Task
    boolean deleteAllTasks() throws IOException;

    // Удаление objects.Task по идентификатору
    boolean deleteTaskById(int id) throws IOException;

    // Обновление objects.Task
    boolean updateTask(T task, int oldTaskID) throws IOException;

    //----------------------------------------------|   objects.Epic   |------------------------------------------------------//

    // Удаление всех objects.Epic задач
    boolean deleteAllEpics() throws IOException;

    // Удаление objects.Epic задачи по идентификатору
    boolean deleteEpicById(int id) throws IOException;

    // Удаление всех objects.Subtask задач в выбранном objects.Epic
    boolean deleteAllSubtaskInEpic(int id) throws IOException;

    // Обновление objects.Epic
    boolean updateEpic(Epic epic, int oldEpicID) throws IOException;

    Map<Integer, Subtask> getAllSubtaskByEpicID(int epicID);

    // Получение списка objects.Epic задач
    Map<Integer, Epic> getAllEpic();

    //---------------------------------------------|   objects.Subtask   |----------------------------------------------------//

    // Удаление objects.Subtask задачи по идентификатору
    boolean deleteSubtaskById(int id) throws IOException;

    // Обновление objects.Subtask
    boolean updateSubtask(Subtask subtask, int oldSubtaskID) throws IOException;

    // Удаление всех objects.Subtask
    boolean deleteAllSubtask() throws IOException;

    // Получение все objects.Subtask
    Map<Integer, Subtask> getAllSubtask();

    //---------------------------------------------|   objects.Other   |----------------------------------------------------//
    List<Task> getHistory();
    Set<Task> getPrioritizedTasks();
    void save() throws IOException;
}
