package services.taskmanagers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import filebacked.FileBackedTasksManager;
import http.HttpTaskManager;
import http.HttpTaskServer;
import http.KVServer;
import models.Epic;
import models.Subtask;
import models.Task;
import services.history.HistoryManager;
import services.history.InMemoryHistoryManager;
import services.printmanager.PrintManager;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Managers {

    public static HttpTaskManager getDefault(URL url) {
        return new HttpTaskManager(url);
    } // Должен возвращать HttpTaskManager

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static PrintManager getDefaultPrintManager() {
        return new PrintManager();
    }

    public static FileBackedTasksManager loadFromFile(Path path) throws IOException, InterruptedException {
        return new FileBackedTasksManager(Paths.get(path.toUri()));
    }

    public static HttpTaskServer getDefaultHttpTaskServer(HttpTaskManager taskManager, String hostname) throws IOException {
        return new HttpTaskServer(taskManager, hostname);
    }

    public static KVServer getDefaultKVServer(String hostname, int port) throws IOException {
        return new KVServer();
    }
    public static Gson getGsonForDeserializer() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(ZoneId.class, new ZoneIdAdapter());
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new ZoneDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        return gsonBuilder.create();
    }
    public static Gson getGson() {
        TaskDeserializer deserializer = new TaskDeserializer("type");
        deserializer.registerBarnType("Subtask", Subtask.class);
        deserializer.registerBarnType("Epic", Epic.class);
        deserializer.registerBarnType("Task", Task.class);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(ZoneId.class, new ZoneIdAdapter());
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new ZoneDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        gsonBuilder.registerTypeAdapter(Task.class, deserializer);
        return gsonBuilder.create();
    }
}
