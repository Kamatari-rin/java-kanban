package http;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import models.Epic;
import models.Subtask;
import models.Task;
import services.taskmanagers.Constants;
import services.taskmanagers.Managers;
import services.taskmanagers.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.regex.Pattern;


public class HttpTaskServer implements HttpHandler {
    private HttpServer server;
    private Gson gson;

    private TaskManager taskManager;

    public HttpTaskServer(HttpTaskManager taskManager, String hostname) throws IOException {
        this.taskManager = taskManager;
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress(hostname, Constants.PORT_HTTP_TASK_SERVER), 0);
        server.createContext("/tasks/task/", this::taskHandle);
        server.createContext("/epics/epic/", this::epicHandle);
        server.createContext("/subtasks/subtask/", this::subtaskHandle);
        server.createContext("/tasks/history/", this::historyHandle);
    }

    private void historyHandle(HttpExchange exchange) {
        {
            try {
                writeResponse(exchange, gson.toJson(taskManager.getHistory()), 200);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                exchange.close();
            }
        }
    }


    public void taskHandle(HttpExchange exchange) throws IOException {
        try {
            URI uri = exchange.getRequestURI();
            String requestMethod = exchange.getRequestMethod();
            String rawQuery = uri.getRawQuery();
            String path = uri.getPath();

            if (rawQuery != null) {
                path += rawQuery;
            }

            Endpoint endpoint = getEndpoint(path, requestMethod);

            Task task = null;
            int id = -1;

            if (rawQuery != null && rawQuery.contains("id=")) {
            String pathID = rawQuery.replaceFirst("^id=", "");
            id = parsePathId(pathID);
                if (id == -1) {
                    writeResponse(exchange, "Получен некорректный id.", 405);
                    // writeResponse(exchange, rawQuery, 405);
                }
            } else if (requestMethod.equals("POST") || requestMethod.equals("PUT")) {
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), Constants.DEFAULT_CHARSET);
                try {
                    task = gson.fromJson(body, Task.class);
                } catch (JsonSyntaxException e) {
                    writeResponse(exchange, "Получен некорректный JSON", 400);
                }
            }

            switch (endpoint) {
                case GET_TASK: {
                    writeResponse(exchange, gson.toJson(taskManager.getTaskById(id)), 200);
                    break;
                }
                case PUT_TASK: {
                    taskManager.updateTask(task);
                    writeResponse(exchange, "Задача была успешно обновлена.", 200);
                    break;
                }
                case GET_ALL_TASKS: {
                    writeResponse(exchange, gson.toJson(taskManager.getAllTask()), 200);
                    break;
                }
                case POST_TASK: {
                    taskManager.createTask(task);
                    writeResponse(exchange, "Задача была успешно добавлена.", 200);
                    break;
                }
                case DELETE_TASK: {
                    taskManager.deleteTaskById(id);
                    writeResponse(exchange, "Задача была успешно удалена.", 200);
                    break;
                }
                case DELETE_ALL_TASKS: {
                    taskManager.deleteAllTasks();
                    writeResponse(exchange, "Все задачи были успешно удалены.", 200);
                    break;
                }
                default: {
                    writeResponse(exchange, "Не коректный метод! " + path, 405);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void subtaskHandle(HttpExchange exchange) {
        try {
            URI uri = exchange.getRequestURI();
            String requestMethod = exchange.getRequestMethod();
            String rawQuery = uri.getRawQuery();
            String path = uri.getPath();

            if (rawQuery != null) {
                path += rawQuery;
            }

            Endpoint endpoint = getEndpoint(path, requestMethod);

            Subtask task = null;
            int id = -1;

            if (rawQuery != null && rawQuery.contains("id=")) {
                String pathID = rawQuery.replaceFirst("^id=", "");
                id = parsePathId(pathID);
                if (id == -1) {
                    writeResponse(exchange, "Получен некорректный id.", 405);
                }
            } else if (requestMethod.equals("POST") || requestMethod.equals("PUT")) {
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), Constants.DEFAULT_CHARSET);
                try {
                    task = gson.fromJson(body, Subtask.class);
                } catch (JsonSyntaxException e) {
                    writeResponse(exchange, "Получен некорректный JSON", 400);
                }
            }

            switch (endpoint) {
                case GET_TASK: {
                    writeResponse(exchange, gson.toJson(taskManager.getTaskById(id)), 200);
                    break;
                }
                case POST_TASK: {
                    taskManager.createTask(task);
                    writeResponse(exchange, "Задача была успешно добавлена.", 200);
                    break;
                }
                case GET_ALL_SUBTASKS: {
                    writeResponse(exchange, gson.toJson(taskManager.getAllSubtask()), 200);
                    break;
                }
                case PUT_SUBTASK: {
                    taskManager.updateSubtask(task);
                    writeResponse(exchange, "SUBTASK задача была успешно обновлена.", 200);
                    break;
                }
                case DELETE_SUBTASK: {
                    taskManager.deleteSubtaskById(id);
                    writeResponse(exchange, "SUBTASK задача были успешно удалена.", 200);
                    break;
                }
                case DELETE_ALL_SUBTASKS: {
                    taskManager.deleteAllSubtask();
                    writeResponse(exchange, "Все SUBTASK задачи были успешно удалены.", 200);
                    break;
                }
                default: {
                    System.out.println("Не коректный метод! " + requestMethod);
                    exchange.sendResponseHeaders(405, 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void epicHandle(HttpExchange exchange) {
        try {
            URI uri = exchange.getRequestURI();
            String requestMethod = exchange.getRequestMethod();
            String rawQuery = uri.getRawQuery();
            String path = uri.getPath();

            if (rawQuery != null) {
                path += rawQuery;
            }

            Endpoint endpoint = getEndpoint(path, requestMethod);

            Epic task = null;
            int id = -1;

            if (rawQuery != null && rawQuery.contains("id=")) {
                String pathID = rawQuery.replaceFirst("^id=", "");
                id = parsePathId(pathID);
                if (id == -1) {
                    writeResponse(exchange, "Получен некорректный id.", 405);
                    // writeResponse(exchange, rawQuery, 405);
                }
            } else if (requestMethod.equals("POST") || requestMethod.equals("PUT")) {
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), Constants.DEFAULT_CHARSET);
                try {
                    task = gson.fromJson(body, Epic.class);
                } catch (JsonSyntaxException e) {
                    writeResponse(exchange, "Получен некорректный JSON", 400);
                }
            }

            switch (endpoint) {
                case GET_TASK: {
                    writeResponse(exchange, gson.toJson(taskManager.getTaskById(id)), 200);
                    break;
                }
                case POST_TASK: {
                    taskManager.createTask(task);
                    writeResponse(exchange, "Задача была успешно добавлена.", 200);
                    break;
                }
                case GET_ALL_EPICS: {
                    writeResponse(exchange, gson.toJson(taskManager.getAllEpic()), 200);
                    break;
                }
                case PUT_EPIC: {
                    taskManager.updateEpic(task);
                    writeResponse(exchange, "EPIC задача была успешно обновлена.", 200);
                    break;
                }
                case DELETE_EPIC: {
                    taskManager.deleteEpicById(id);
                    writeResponse(exchange, "EPIC задача была успешно удалена.", 200);
                    break;
                }
                case DELETE_ALL_EPICS: {
                    taskManager.deleteAllEpics();
                    writeResponse(exchange, "Все EPIC задачи были успешно удалены.", 200);
                    break;
                }
                case GET_ALL_SUBTASKS: {
                    //taskManager.getAllSubtaskByEpicID(id);
                    System.out.println("Все SUBTASK задачи были успешно получены.");
                    exchange.sendResponseHeaders(200, 0);
                    break;
                }
                default: {
                    // "Не коректный метод!"
                    writeResponse(exchange, path, 405);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + Constants.PORT_HTTP_TASK_SERVER);
        System.out.println("Открой в браузере http://localhost:" + Constants.PORT_HTTP_TASK_SERVER + "/");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер на порту " + Constants.PORT_HTTP_TASK_SERVER);
    }

    private void writeResponse(HttpExchange exchange,
                               String responseString,
                               int responseCode) throws IOException {
        if (responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(Constants.DEFAULT_CHARSET);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), Constants.DEFAULT_CHARSET);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(Constants.DEFAULT_CHARSET);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        
    }

    private Endpoint getEndpoint(String path, String requestMethod) {
        if ((Pattern.matches("^/tasks/task/?id=\\d+$", path) && requestMethod.equals("GET"))
                 || (Pattern.matches("^/epics/epic/?id=\\d+$", path) && requestMethod.equals("GET"))
                 || (Pattern.matches("^/subtasks/subtask/?id=\\d+$", path) && requestMethod.equals("GET"))) {
            return Endpoint.GET_TASK;
        } else if (Pattern.matches("^/tasks/task/?id=\\d+$", path) && requestMethod.equals("DELETE")) {
            return Endpoint.DELETE_TASK;
        } else if (Pattern.matches("^/epics/epic/?id=\\d+$", path) && requestMethod.equals("DELETE")) {
            return Endpoint.DELETE_EPIC;
        } else if (Pattern.matches("^/subtasks/subtask/?id=\\d+$", path) && requestMethod.equals("DELETE")) {
            return Endpoint.DELETE_SUBTASK;
        } else if (Pattern.matches("^/tasks/task/", path) && requestMethod.equals("GET")) {
            return Endpoint.GET_ALL_TASKS;
        } else if ((Pattern.matches("^/tasks/task/", path) && requestMethod.equals("POST"))
                || (Pattern.matches("^/epics/epic/", path) && requestMethod.equals("POST"))
                || (Pattern.matches("^/subtasks/subtask/", path) && requestMethod.equals("POST"))) {
            return Endpoint.POST_TASK;
        }  else if (Pattern.matches("^/tasks/task/", path) && requestMethod.equals("PUT")) {
            return Endpoint.PUT_TASK;
        } else if (Pattern.matches("^/tasks/task/", path) && requestMethod.equals("DELETE")) {
            return Endpoint.DELETE_ALL_TASKS;
        } else if (Pattern.matches("^/epics/epic/", path) && requestMethod.equals("GET")) {
            return Endpoint.GET_ALL_EPICS;
        } else if (Pattern.matches("^/epics/epic/", path) && requestMethod.equals("PUT")) {
            return Endpoint.PUT_EPIC;
        } else if (Pattern.matches("^/epics/epic/", path) && requestMethod.equals("DELETE")) {
            return Endpoint.DELETE_ALL_EPICS;
        } else if (Pattern.matches("^/subtasks/subtask/", path) && requestMethod.equals("GET")) {
            return Endpoint.GET_ALL_SUBTASKS;
        } else if (Pattern.matches("^/subtasks/subtask/", path) && requestMethod.equals("PUT")) {
            return Endpoint.PUT_SUBTASK;
        } else if (Pattern.matches("^/subtasks/subtask/", path) && requestMethod.equals("DELETE")) {
            return Endpoint.DELETE_ALL_SUBTASKS;
        } return Endpoint.UNKNOWN;
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    enum Endpoint {
        GET_TASK, // getTaskById Получение Task, Subtask, Epic по id.
        POST_TASK, // createTask(T task) Создание Task, Subtask, Epic.


        GET_ALL_TASKS,
        PUT_TASK, // Обновление Task.
        DELETE_TASK,
        DELETE_ALL_TASKS,

        GET_ALL_EPICS,
        PUT_EPIC, // Обновление Epic.
        DELETE_EPIC,
        DELETE_ALL_EPICS,

        GET_ALL_SUBTASKS,
        PUT_SUBTASK, // Обновление Subtask.
        DELETE_SUBTASK,
        DELETE_ALL_SUBTASKS,

        UNKNOWN
    }
}
