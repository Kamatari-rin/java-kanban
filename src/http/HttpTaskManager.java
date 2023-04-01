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

   public HttpTaskManager() {
       super();
       key_tasks = "tasks";
       key_epics = "epics";
       key_subtasks = "subtasks";
       key_history = "history";
       kvTaskClient = new KVTaskClient();
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

    public Optional<String> load() throws IOException, InterruptedException {
        try {
            String tasksMapJSON = kvTaskClient.load(key_tasks);
            String epicsMapJSON = kvTaskClient.load(key_epics);
            String subtasksMapJSON = kvTaskClient.load(key_subtasks);
            String historyListJSON = kvTaskClient.load(key_history);
            if (tasksMapJSON != null || !tasksMapJSON.isBlank()) {
                this.tasksMap = gson.fromJson(tasksMapJSON, new TypeToken<Map<Integer, Task>>() {}.getType());
            }
            if (epicsMapJSON != null || !epicsMapJSON.isBlank()) {
                this.epicsMap = gson.fromJson(epicsMapJSON, new TypeToken<Map<Integer, Epic>>() {}.getType());
            }
            if (subtasksMapJSON != null || !subtasksMapJSON.isBlank()) {
                this.subtasksMap = gson.fromJson(subtasksMapJSON, new TypeToken<Map<Integer, Subtask>>() {
                }.getType());
            }
            if (historyListJSON != null || !historyListJSON.isBlank()) {
                Type historyListType = new TypeToken<List<Task>>() {
                }.getType();
                List<Task> historyList = gson.fromJson(historyListJSON, historyListType);
                for (Task task : historyList) {
                    historyManager.add(task);
                }
            }
        } catch (IOException e) {
            e.getMessage();
        }


        return null;
    }
}
