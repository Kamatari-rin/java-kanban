import com.google.gson.Gson;
import http.HttpTaskManager;
import http.HttpTaskServer;
import http.KVServer;
import models.Task;
import services.printmanager.PrintManager;
import services.taskmanagers.Managers;
import services.taskmanagers.TaskManager;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        final String hostname = "localhost";
        final int KVServer_PORT = 8078;

        KVServer kvServer = Managers.getDefaultKVServer(hostname, KVServer_PORT);
        kvServer.start();

        Gson gson = new Gson();
        Map<Integer, Task> taskMap = new HashMap<>();
        String map = gson.toJson(taskMap, taskMap.getClass());
//
//        HttpTaskManager taskManager = (HttpTaskManager) Managers.getDefault(new URL("http://" + hostname + ":" +KVServer_PORT));
//        HttpTaskServer taskServer = Managers.getDefaultHttpTaskServer(taskManager, hostname);
//        taskServer.start();

//        System.out.println("Для завершения нажмите 0");
//        if (new Scanner(System.in).nextInt() == 0) {
//            kvServer.stop(0);
//            taskServer.stop();
//        }

        PrintManager printManager = Managers.getDefaultPrintManager();
        // TaskManager taskManager = Managers.loadFromFile(Paths.get("save.csv"));
    }
}
