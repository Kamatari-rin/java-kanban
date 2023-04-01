package tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import http.HttpTaskManager;
import http.HttpTaskServer;
import http.KVServer;
import models.Subtask;
import models.Task;
import models.Epic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.taskmanagers.Managers;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.*;

import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    private final int PORT = 8080;
    private URL url;
    private HttpTaskServer taskServer;
    private final Gson gson = Managers.getGson();
    private HttpTaskManager taskManager;
    private KVServer kvServer;

    private Epic epic;
    private Subtask subtask;


    @BeforeEach
    void init() throws IOException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();

        url = new URL("http://localhost:" + PORT);
        taskManager = Managers.getDefault(url);
        taskServer = Managers.getDefaultHttpTaskServer(taskManager, "localhost");

        Task taskOne = new Task("Задача 1", "Описание первой задачи", Task.Status.NEW,
                LocalDateTime.of(2023, JANUARY, 1, 12, 0), 1, 25);
        final int taskOneID = taskManager.createTask(taskOne);

        Task taskTwo = new Task("Задача 2", "Описание второй задачи", Task.Status.DONE,
                LocalDateTime.of(2023, JANUARY, 1, 13, 0), 1, 25);
        final int taskTwoID = taskManager.createTask(taskTwo);

        Task taskThree = new Task("Задача 3", "Описание третьей задачи", Task.Status.IN_PROGRESS,
                LocalDateTime.of(2023, JANUARY, 2, 0, 0), 1, 25);
        final int taskThreeID = taskManager.createTask(taskThree);

        epic = new Epic("Эпик 1", "Описание первого эпика");
        taskManager.createTask(epic);

        subtask = new Subtask("Подзадача 1 от Эпика №1", "Описание первой подзадачи от Эпика №1",
                Task.Status.NEW, epic.getTaskID(), LocalDateTime.of(2022, JANUARY, 1, 0, 0), 1, 15);
        taskManager.createTask(subtask);

        taskManager.getTaskById(1);
        taskManager.getTaskById(4);
        taskManager.getTaskById(3);
        taskManager.getTaskById(5);
        taskManager.getTaskById(2);

        taskServer.start();
    }

    @AfterEach
    void tearDown() {
        taskServer.stop();
        kvServer.stop(0);
    }

    @Test
    void getTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), response.body());
        Type type = new TypeToken<Task>() {}.getType();
        Task taskGetFromServer = gson.fromJson(response.body(), type);
        assertEquals(taskManager.getTaskById(2), taskGetFromServer);
    }

    @Test
    void getEpicById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/epic/?id=4");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), response.body());
        Type type = new TypeToken<Epic>() {}.getType();
        Epic taskGetFromServer = gson.fromJson(response.body(), type);
        assertEquals(taskManager.getTaskById(4), taskGetFromServer);
    }

    @Test
    void getSubtaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/subtask/?id=5");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), response.body());
        Type type = new TypeToken<Subtask>() {}.getType();
        Subtask taskGetFromServer = gson.fromJson(response.body(), type);
        assertNotNull(taskGetFromServer);
        assertEquals(taskManager.getTaskById(5), taskGetFromServer);
    }
    @Test
    void getAllTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), response.body());
        Type type = new TypeToken<Map<Integer, Task>>() {}.getType();
        Map<Integer, Task> tasksMapFromServer = gson.fromJson(response.body(), type);
        assertEquals(taskManager.getAllTask(), tasksMapFromServer);
    }

    @Test
    void getAllEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), response.body());
        Type type = new TypeToken<Map<Integer, Epic>>() {}.getType();
        Map<Integer, Epic> tasksMapFromServer = gson.fromJson(response.body(), type);
        assertEquals(taskManager.getAllEpic(), tasksMapFromServer);
    }

    @Test
    void getAllSubtasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), response.body());
        Type type = new TypeToken<Map<Integer, Subtask>>() {}.getType();
        Map<Integer, Subtask> tasksMapFromServer = gson.fromJson(response.body(), type);
        assertEquals(taskManager.getAllSubtask(), tasksMapFromServer);
    }

    @Test
    void addNewTask() throws IOException, InterruptedException {
        Task controlTask = new Task("Задача 4", "Описание четвертой задачи", Task.Status.NEW,
                LocalDateTime.of(2023, JANUARY, 21, 12, 0), 1, 29);

        String jsonTask = gson.toJson(controlTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(4, taskManager.getAllTask().size());
    }

    @Test
    void addNewEpic() throws IOException, InterruptedException {
        Epic epicNew = new Epic("Эпик 2", "Описание второго эпика");
        String jsonTask = gson.toJson(epicNew);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/epic/");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(2, taskManager.getAllEpic().size());
    }

    @Test
    void addNewSubtask() throws IOException, InterruptedException {
        Subtask subtaskNew = new Subtask("Подзадача 2 от Эпика №1", "Описание второй подзадачи от Эпика №1",
                Task.Status.NEW, epic.getTaskID(), LocalDateTime.of(2022, JANUARY, 2, 0, 0), 1, 25);
        String jsonTask = gson.toJson(subtaskNew);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/subtask/");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertEquals(2, taskManager.getAllSubtask().size());
    }

    @Test
    void updateTask() throws IOException, InterruptedException {
        Task taskControl = taskManager.getTaskById(1);
        Task taskOneUpdate = new Task("Задача 1 Update", "Описание первой задачи Update", Task.Status.IN_PROGRESS,
                LocalDateTime.of(2023, JANUARY, 1, 12, 0), 1, 25, taskControl.getTaskID());
        String jsonTask = gson.toJson(taskOneUpdate);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .PUT(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(taskOneUpdate, taskManager.getTaskById(1));
    }

    @Test
    void updateEpic() throws IOException, InterruptedException {
        Epic controlEpic = (Epic) taskManager.getTaskById(4);
        Epic epicUpdate= new Epic("Эпик 1 Update", "Описание первого эпика Update", controlEpic.getTaskStatus(), controlEpic.getTaskID());
        String jsonTask = gson.toJson(epicUpdate);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/epic/");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .PUT(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epicFromMap = (Epic) taskManager.getTaskById(4);

        assertEquals(200, response.statusCode());
        assertEquals(epicUpdate, taskManager.getTaskById(4));
    }

    @Test
    void updateSubtask() throws IOException, InterruptedException {
        Subtask controlSubtask = (Subtask) taskManager.getTaskById(5);
        Subtask subtaskUpdate= new Subtask("Подзадача 1 от Эпика №1 Update", "Описание второй подзадачи от Эпика №1",
                Task.Status.IN_PROGRESS, epic.getTaskID(), LocalDateTime.of(2022, JANUARY, 2, 0, 0), 1, 35, controlSubtask.getTaskID());
        String jsonTask = gson.toJson(subtaskUpdate);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/subtask/");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .PUT(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(subtaskUpdate, taskManager.getTaskById(5));
    }

    @Test
    void deleteTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), response.body());
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.deleteTaskById(2)
        );

        assertEquals("Задача не найдена.", exception.getMessage());
    }

    @Test
    void deleteEpicById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/epic/?id=4");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), response.body());
        assertEquals(0, taskManager.getAllEpic().size());

        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.deleteEpicById(5)
        );

        assertEquals("Задача не найдена.", exception.getMessage());
    }

    @Test
    void deleteSubtaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/subtask/?id=5");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), response.body());
        assertEquals(0, taskManager.getAllSubtask().size());

        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.deleteSubtaskById(5)
        );

        assertEquals("Задача не найдена.", exception.getMessage());
    }

    @Test
    void deleteAllTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), response.body());
        assertEquals(0, taskManager.getAllTask().size());

        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.deleteSubtaskById(2)
        );

        assertEquals("Задача не найдена.", exception.getMessage());
    }

    @Test
    void deleteAllEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), response.body());
        assertEquals(0, taskManager.getAllEpic().size());

        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.deleteEpicById(4)
        );

        assertEquals("Задача не найдена.", exception.getMessage());
    }

    @Test
    void deleteAllSubtasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), response.body());
        assertEquals(0, taskManager.getAllSubtask().size());

        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskManager.deleteSubtaskById(5)
        );

        assertEquals("Задача не найдена.", exception.getMessage());
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/history/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type historyList = new TypeToken<List<Task>>() {}.getType();

        List<Task> controlHistoryList = taskManager.getHistory();
        List<Task> historyListFromServer = gson.fromJson(response.body(), historyList);

        assertNotNull(historyListFromServer);
        assertEquals(controlHistoryList, historyListFromServer);
    }
}