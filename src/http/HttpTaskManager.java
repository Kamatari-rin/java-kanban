package http;

import com.google.gson.Gson;
import filebacked.FileBackedTasksManager;
import services.taskmanagers.Managers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class HttpTaskManager extends FileBackedTasksManager {

    private static String key_tasks;
    private static String key_epics;
    private static String key_subtasks;
    private static String key_history;
    private static KVTaskClient kvTaskClient;
    private static Gson gson;

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

    public Optional<String> load() throws IOException, InterruptedException {
       Optional<String> tasksMapJSON = kvTaskClient.load(key_tasks);
       Optional<String> epicsMapJSON = kvTaskClient.load(key_epics);
       Optional<String> subtasksMapJSON = kvTaskClient.load(key_subtasks);
       Optional<String> historyListJSON = kvTaskClient.load(key_history);

       if (tasksMapJSON.isPresent()) {
           return tasksMapJSON;
           //this.tasksMap =  gson.fromJson(tasksMapJSON.get(), new TypeToken<Map<Integer, Task>>(){}.getType());
       }
//       if (epicsMapJSON.isPresent()) {
//           this.epicsMap = gson.fromJson(epicsMapJSON.get(), new TypeToken<Map<Integer, Epic>>(){}.getType());
//       }
//       if (subtasksMapJSON.isPresent()) {
//           this.subtasksMap = gson.fromJson(subtasksMapJSON.get(), new TypeToken<Map<Integer, Subtask>>(){}.getType());
//       }
//       if (historyListJSON.isPresent()) {
//           Type historyListType = new TypeToken<List<Task>>() {}.getType();
//           List<Task> historyList = gson.fromJson(historyListJSON.get(), historyListType);
//           for (Task task : historyList) {
//               historyManager.add(task);
//           }
//       }
        return null;
    }
}
