package http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import filebacked.FileBackedTasksManager;
import models.Epic;
import models.Subtask;
import models.Task;
import services.taskmanagers.Managers;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HttpTaskManager extends FileBackedTasksManager {

    private static String key_tasks;
    private static String key_epics;
    private static String key_subtasks;
    private static String key_history;
    private static KVTaskClient kvTaskClient;
    private static Gson gson;

    // Новая реализация менеджера задач
    // Теперь можно создать новую реализацию интерфейса TaskManager — класс HttpTaskManager. Он будет наследовать от FileBackedTasksManager.
    // Конструктор HttpTaskManager должен будет вместо имени файла принимать URL к серверу KVServer.
    // Также HttpTaskManager создаёт KVTaskClient, из которого можно получить исходное состояние менеджера.
    // Вам нужно заменить вызовы сохранения состояния в файлах на вызов клиента.
    // В конце обновите статический метод getDefault() в утилитарном классе Managers, чтобы он возвращал HttpTaskManager.


    // Получается через HttpTaskManager вызываем переопределенный метод save, save создаёт request через экземпляр KVTaskClient,
    // KVServer отдаёт слепок TaskManager,
    // и KVTaskClient хранит у себя в памяти JSON таскменеджера актуального на момент запроса save?
    // Да, только kvserver хранит в памяти слепок.

    // Kvserver полная аналогия файла csv.
    // Таким образом в мэйне или тестах сначала запускается kvserver, потом taskserver,
    // который в свою очередь получает экземпляр менеджера, которой через kvclient загружает с kvserver предыдущие данные.

    // т.е логика что http запросы кидаются на HttpServer, тот их обрабатывает и мапит на методы тасок с помощью HttpTaskManager,
    // как буферного хранилища данных.  А тот в свою очередь через KVClient через http обновляет состояние в KVSerever.

   public HttpTaskManager(URL url) {
       super();
       key_tasks = "Tasks";
       key_epics = "Epics";
       key_subtasks = "Subtasks";
       key_history = "History";
       kvTaskClient = new KVTaskClient(url);
       gson = Managers.getGson();
       gson.serializeNulls();
    }

    @Override
    protected void save() throws IOException, InterruptedException {

       try {
           kvTaskClient.put(key_tasks, gson.toJson(tasksMap));
           kvTaskClient.put(key_epics, gson.toJson(epicsMap));
           kvTaskClient.put(key_subtasks, gson.toJson(subtasksMap));
           kvTaskClient.put(key_history, gson.toJson(getHistory()));


       } catch (RuntimeException e) {
           System.out.println("Ошибка " + e.getMessage());
       }
    }

    public void load() throws IOException, InterruptedException {
       Optional<String> tasksMapJSON = kvTaskClient.load(key_tasks);
       Optional<String> epicsMapJSON = kvTaskClient.load(key_epics);
       Optional<String> subtasksMapJSON = kvTaskClient.load(key_subtasks);
       Optional<String> historyListJSON = kvTaskClient.load(key_history);

       if (tasksMapJSON.isPresent()) {
           this.tasksMap =  gson.fromJson(tasksMapJSON.get(), new TypeToken<Map<Integer, Task>>(){}.getType());
       }
       if (epicsMapJSON.isPresent()) {
           this.epicsMap = gson.fromJson(epicsMapJSON.get(), new TypeToken<Map<Integer, Epic>>(){}.getType());
       }
       if (subtasksMapJSON.isPresent()) {
           this.subtasksMap = gson.fromJson(subtasksMapJSON.get(), new TypeToken<Map<Integer, Subtask>>(){}.getType());
       }
       if (historyListJSON.isPresent()) {
           Type historyListType = new TypeToken<List<Task>>() {}.getType();
           List<Task> historyList = gson.fromJson(historyListJSON.get(), historyListType);
           for (Task task : historyList) {
               historyManager.add(task);
           }
       }
    }
}
