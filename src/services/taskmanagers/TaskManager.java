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
    int createTask(T task) throws IOException, InterruptedException;

    // Получить все объекты Task
    Map<Integer, Task> getAllTask();

    // Получение задачи по идентификатору
    Task getTaskById(int id) throws IOException, InterruptedException;

    // Удаление всех задач objects.Task
    boolean deleteAllTasks() throws IOException, InterruptedException;

    // Удаление objects.Task по идентификатору
    boolean deleteTaskById(int id) throws IOException, InterruptedException;

    // Обновление objects.Task
    boolean updateTask(T task) throws IOException, InterruptedException;

    //----------------------------------------------|   objects.Epic   |------------------------------------------------------//

    // Удаление всех objects.Epic задач
    boolean deleteAllEpics() throws IOException, InterruptedException;

    // Удаление objects.Epic задачи по идентификатору
    boolean deleteEpicById(int id) throws IOException, InterruptedException;

    // Удаление всех objects.Subtask задач в выбранном objects.Epic
    boolean deleteAllSubtaskInEpic(int id) throws IOException, InterruptedException;

    // Обновление objects.Epic
    boolean updateEpic(Epic epic) throws IOException, InterruptedException;

    Map<Integer, Subtask> getAllSubtaskByEpicID(int epicID);

    // Получение списка objects.Epic задач
    Map<Integer, Epic> getAllEpic();

    //---------------------------------------------|   objects.Subtask   |----------------------------------------------------//

    // Удаление objects.Subtask задачи по идентификатору
    boolean deleteSubtaskById(int id) throws IOException, InterruptedException;

    // Обновление objects.Subtask
    boolean updateSubtask(Subtask subtask) throws IOException, InterruptedException;

    // Удаление всех objects.Subtask
    boolean deleteAllSubtask() throws IOException, InterruptedException;

    // Получение все objects.Subtask
    Map<Integer, Subtask> getAllSubtask();

    //---------------------------------------------|   objects.Other   |----------------------------------------------------//
    List<Task> getHistory();
    Set<Task> getPrioritizedTasks();

}
