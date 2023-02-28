package filebacked;

import exceptions.FileCreateException;
import exceptions.FileReadException;
import exceptions.FileWriteException;
import models.Epic;
import models.Subtask;
import models.Task;
import services.history.HistoryManager;
import services.history.InMemoryHistoryManager;
import services.printmanager.PrintManager;
import services.taskmanagers.InMemoryTaskManager;
import services.taskmanagers.Managers;
import services.taskmanagers.TaskManager;
import services.taskmanagers.TaskType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    private  Path userData;

    public FileBackedTasksManager(Path path) {

        userData = path;

        if (Files.exists(path)) {
            loadFromFile(path);
        }
    }

    // Не совсем понял на счет своего не проверяемого исключения ManagerSaveException что нужно сделать что бы он отлавливал?


    ///////////////////////////////////////////// Запись в Файл ////////////////////////////////////////////////////////
    public void save() throws IOException {
        createFile(this.userData);

        final List<String> tasksList = new ArrayList<>();
        String historyList = historyToString(List.copyOf(super.getHistory()));

        tasksList.addAll(taskMapToStringList(Map.copyOf(super.getTasksMap())));
        tasksList.addAll(taskMapToStringList(Map.copyOf(super.getEpicsMap())));
        tasksList.addAll(taskMapToStringList(Map.copyOf(super.getSubtasksMap())));

        try (Writer fileWriter = new FileWriter(this.userData.toFile())) {
            fileWriter.write("id,type,name,status,description,epic,\n");
        }

        writeDataInFile(tasksList, historyList);
    }

    private void createFile(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                throw new FileCreateException("Не удалось создать файл.");
            }
        }
    }

    private String historyToString(final List<Task> historyList) {
        String viewHistory = "";
        for (Task task : historyList) {
            viewHistory += task.getTaskID() + ",";
        }
        return viewHistory;
    }

    private List<String> taskMapToStringList(final Map<Integer, Task> tasksMap) {
        List<String> tasksList = new ArrayList<>();

        for (Integer taskID : tasksMap.keySet()) {
            Task task = tasksMap.get(taskID);
            tasksList.add(task.toString());
        }
        return tasksList;
    }

    private void writeDataInFile(final List<String> tasksList, final String viewHistory) {

        try(BufferedWriter fr = new BufferedWriter(new FileWriter(this.userData.toFile(), StandardCharsets.UTF_8, true))) {
            for (String task : tasksList) {
                fr.write(task + "\n");
            }
            fr.write("\n\n" + viewHistory);
            fr.flush();
        } catch (IOException e) {
            throw new FileWriteException("Не удалось сохранить данные.");
        }
    }

    ///////////////////////////////////////////// Востановление из Файла ///////////////////////////////////////////////

    public void loadFromFile(Path path) {
        List<String> dataFromFile = writeDataFromFileInList(path);

        Map<Integer, Task> tasksMap = new HashMap<>();
        Map<Integer, Epic> epicsMap = new HashMap<>();
        Map<Integer, Subtask> subtasksMap = new HashMap<>();
        HashMap<Integer, TaskType> allTasksMap = new HashMap<>();

        Map<Integer, List<Integer>> subtasksByEpic = new HashMap<>();

        HistoryManager historyManager = new InMemoryHistoryManager();

        for (String line : dataFromFile) {
            if (line.isBlank()) break;
            String[] element = line.split(",");
            if (element[1].equals("Task")) {
                if (element[3].equals("NEW")) {
                    Task task = new Task(element[2], element[4], Task.Status.NEW);
                    task.setTaskID(Integer.parseInt(element[0]));
                    tasksMap.put(Integer.parseInt(element[0]), task);
                    allTasksMap.put(Integer.parseInt(element[0]), TaskType.TASK);
                } else if (element[3].equals("IN_PROGRESS")) {
                    Task task = new Task(element[2], element[4], Task.Status.IN_PROGRESS);
                    task.setTaskID(Integer.parseInt(element[0]));
                    tasksMap.put(Integer.parseInt(element[0]), task);
                    allTasksMap.put(Integer.parseInt(element[0]), TaskType.TASK);
                } else if (element[3].equals("DONE")) {
                    Task task = new Task(element[2], element[4], Task.Status.DONE);
                    task.setTaskID(Integer.parseInt(element[0]));
                    tasksMap.put(Integer.parseInt(element[0]), task);
                    allTasksMap.put(Integer.parseInt(element[0]), TaskType.TASK);
                }
            } else if (element[1].equals("Epic")) {
                if (element[3].equals("NEW")) {
                    Epic epic = new Epic(element[2], element[4], Task.Status.NEW);
                    epic.setTaskID(Integer.parseInt(element[0]));
                    epicsMap.put(Integer.parseInt(element[0]), epic);
                    allTasksMap.put(Integer.parseInt(element[0]), TaskType.EPIC);
                } else if (element[3].equals("IN_PROGRESS")) {
                    Epic epic = new Epic(element[2], element[4], Task.Status.IN_PROGRESS);
                    epic.setTaskID(Integer.parseInt(element[0]));
                    epicsMap.put(Integer.parseInt(element[0]), epic);
                    allTasksMap.put(Integer.parseInt(element[0]), TaskType.EPIC);
                } else if (element[3].equals("DONE")) {
                    Epic epic = new Epic(element[2], element[4], Task.Status.DONE);
                    epic.setTaskID(Integer.parseInt(element[0]));
                    epicsMap.put(Integer.parseInt(element[0]), epic);
                    allTasksMap.put(Integer.parseInt(element[0]), TaskType.EPIC);
                }
            } else if (element[1].equals("Subtask")) {
                if (element[3].equals("NEW")) {
                    Subtask subtask = new Subtask(element[2], element[4], Task.Status.NEW, Integer.parseInt(element[5]));
                    subtask.setTaskID(Integer.parseInt(element[0]));
                    subtasksMap.put(Integer.parseInt(element[0]), subtask);
                    allTasksMap.put(Integer.parseInt(element[0]), TaskType.SUBTASK);

                    List<Integer> buf = subtasksByEpic.getOrDefault(Integer.parseInt(element[5]), new ArrayList<Integer>());
                    buf.add(Integer.parseInt(element[0]));
                    subtasksByEpic.put(Integer.parseInt(element[5]), buf);
                } else if (element[3].equals("IN_PROGRESS")) {
                    Subtask subtask = new Subtask(element[2], element[4], Task.Status.IN_PROGRESS, Integer.parseInt(element[5]));
                    subtask.setTaskID(Integer.parseInt(element[0]));
                    subtasksMap.put(Integer.parseInt(element[0]), subtask);
                    allTasksMap.put(Integer.parseInt(element[0]), TaskType.SUBTASK);

                    List<Integer> buf = subtasksByEpic.getOrDefault(Integer.parseInt(element[5]), new ArrayList<Integer>());
                    buf.add(Integer.parseInt(element[0]));
                    subtasksByEpic.put(Integer.parseInt(element[5]), buf);
                } else if (element[3].equals("DONE")) {
                    Subtask subtask = new Subtask(element[2], element[4], Task.Status.DONE, Integer.parseInt(element[5]));
                    subtask.setTaskID(Integer.parseInt(element[0]));
                    subtasksMap.put(Integer.parseInt(element[0]), subtask);
                    allTasksMap.put(Integer.parseInt(element[0]), TaskType.SUBTASK);

                    List<Integer> buf = subtasksByEpic.getOrDefault(Integer.parseInt(element[5]), new ArrayList<Integer>());
                    buf.add(Integer.parseInt(element[0]));
                    subtasksByEpic.put(Integer.parseInt(element[5]), buf);
                }
            } else if (!element[0].equals("id")) {
                for (int i = 0; i < element.length; i++) {
                    if (tasksMap.containsKey(element[i])) {
                        Task task = tasksMap.get(element[i]);
                        historyManager.add(task);
                    } else if (epicsMap.containsKey(element[i])) {
                        Task task = epicsMap.get(element[i]);
                        historyManager.add(task);
                    } else if (subtasksMap.containsKey(element[i])) {
                        Task task = subtasksMap.get(element[i]);
                        historyManager.add(task);
                    }
                }
            }
        }

        putSubtasksListOnEpic(epicsMap, subtasksByEpic);
        super.setTasksMap(tasksMap);
        super.setEpicsMap(epicsMap);
        super.setSubtasksMap(subtasksMap);
        super.setAllTasksMap(allTasksMap);
        super.setHistoryManager(historyManager);
    }

    private List<String> writeDataFromFileInList(final Path path) {
        final List<String> dataFromFile = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(this.userData.toFile(), StandardCharsets.UTF_8))) {
            while (br.ready()) {
                String line = br.readLine();
                dataFromFile.add(line);
            }
            return dataFromFile;
        } catch (IOException e) {
            throw new FileReadException("Ошибка при чтении файла.");
        }
    }

    private static void putSubtasksListOnEpic(Map<Integer, Epic> epicsMap, Map<Integer, List<Integer>> subtasksByEpic) {
        for (Integer epicID : subtasksByEpic.keySet()) {
            Epic epic = epicsMap.get(epicID);
            epic.setSubtaskList(subtasksByEpic.get(epicID));
        }
    }

    @Override
    public int createTask(final Task task) throws IOException {
        int taskID = super.createTask(task);
        save();
        return taskID;
    }

    @Override
    public Task getTaskById(int id) throws IOException {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public boolean deleteAllTasks() throws IOException {
        boolean isDeleted = super.deleteAllTasks();
        save();
        return isDeleted;
    }

    @Override
    public boolean deleteTaskById(int id) throws IOException {
        boolean isDeleted = super.deleteTaskById(id);
        save();
        return isDeleted;
    }

    @Override
    public boolean updateTask(Task task, int oldTaskID) throws IOException {
        boolean isDeleted = super.updateTask(task, oldTaskID);
        save();
        return isDeleted;
    }

    @Override
    public int createEpic(Epic epic) throws IOException {
        int epicID = super.createEpic(epic);
        save();
        return epicID;
    }

    @Override
    public Task getEpicById(int id) throws IOException {
        Task epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public boolean deleteAllEpics() throws IOException {
        boolean isDeleted = super.deleteAllEpics();
        save();
        return isDeleted;
    }

    @Override
    public boolean deleteEpicById(int id) throws IOException {
        boolean isDeleted = super.deleteEpicById(id);
        save();
        return isDeleted;
    }

    @Override
    public boolean deleteAllSubtaskInEpic(int id) throws IOException {
        boolean isDeleted = super.deleteAllSubtaskInEpic(id);
        save();
        return isDeleted;
    }

    @Override
    public boolean updateEpic(Epic epic, int oldEpicID) throws IOException {
        boolean isUpdated = super.updateEpic(epic, oldEpicID);
        save();
        return isUpdated;
    }

    @Override
    public Epic epicUpdateStatus(Epic epic) throws IOException {
        Epic updatedEpic = super.epicUpdateStatus(epic);
        save();
        return updatedEpic;
    }

    @Override
    public int createSubtask(Subtask subtask) throws IOException {
        int taskID = super.createSubtask(subtask);
        save();
        return taskID;
    }

    @Override
    public Task getSubtaskById(int id) throws IOException {
        Task task = super.getSubtaskById(id);
        save();
        return task;
    }

    @Override
    public boolean deleteSubtaskById(int id) throws IOException {
        boolean isDeleted = super.deleteSubtaskById(id);
        save();
        return isDeleted;
    }

    @Override
    public boolean updateSubtask(Subtask subtask, int oldSubtaskID) throws IOException {
        boolean isUpdated = super.updateSubtask(subtask, oldSubtaskID);
        save();
        return isUpdated;
    }

    @Override
    public boolean deleteAllSubtask() throws IOException {
        boolean isDeleted = super.deleteAllSubtask();
        save();
        return isDeleted;
    }


    // Вроде сделал, но честно говоря не понял как с этим работать.
    // static void main(String[] args) - как этот метод запустить?
    // И почему именно так это должно выглядеть?
    static void main(String[] args) throws IOException {
        if (Files.exists(Paths.get(("savefortest.csv")))) Files.delete(Paths.get(("savefortest.csv")));

        Path saveForTest = Files.createFile(Paths.get("savefortest.csv"));
        TaskManager controlTaskManager = new FileBackedTasksManager(saveForTest);

        Task taskOne = new Task("Задача 1", "Описание первой задачи", Task.Status.NEW);
        Task taskTwo = new Task("Задача 2", "Описание второй задачи", Task.Status.NEW);
        Task taskTree = new Task("Задача 3", "Описание третьей задачи", Task.Status.NEW);

        controlTaskManager.createTask(taskOne);
        controlTaskManager.createTask(taskTwo);
        controlTaskManager.createTask(taskTree);

        Epic epicOne = new Epic("Эпик 1", "Описание первого эпика");
        Epic epicTwo = new Epic("Эпик 2", "Описание второго эпика");

        controlTaskManager.createEpic(epicOne);
        controlTaskManager.createEpic(epicTwo);

        int epicOneID = 0;
        int epicTwoID = 0;
        Map<Integer, Epic> epicMap = controlTaskManager.getAllEpic();
        for (Integer epicID : epicMap.keySet()) {
            Epic epic = epicMap.get(epicID);
            if (epic.getTaskName().equals("Эпик 1")) {
                epicOneID = epic.getTaskID();
            } else if (epic.getTaskName().equals("Эпик 2")) {
                epicTwoID = epic.getTaskID();
            }
        }

        Subtask subtaskOneByFirstEpic = new Subtask("Подзадача 1 от Эпика №1", "Описание первой подзадачи от Эпика №1", Task.Status.NEW, epicOneID);
        Subtask subtaskTwoByFirstEpic = new Subtask("Подзадача 2 от Эпика №1", "Описание второй подзадачи от Эпика №1", Task.Status.NEW, epicOneID);
        Subtask subtaskBySecondEpic = new Subtask("Подзадача 1 от Эпика №2", "Описание первой подзадачи от Эпика №2", Task.Status.NEW, epicTwoID);

        controlTaskManager.createSubtask(subtaskOneByFirstEpic);
        controlTaskManager.createSubtask(subtaskTwoByFirstEpic);
        controlTaskManager.createSubtask(subtaskBySecondEpic);

        Map<Integer, Task> taskMap = controlTaskManager.getAllTask();
        Map<Integer, Subtask> subtaskMap = controlTaskManager.getAllSubtask();

        List<Integer> tasksIDList = new ArrayList<>();

        for (Integer taskID : taskMap.keySet()) {
            Task task = taskMap.get(taskID);
            tasksIDList.add(task.getTaskID());
        }
        for (Integer taskID : epicMap.keySet()) {
            Epic task = epicMap.get(taskID);
            tasksIDList.add(task.getTaskID());
        }
        for (Integer taskID : subtaskMap.keySet()) {
            Subtask task = subtaskMap.get(taskID);
            tasksIDList.add(task.getTaskID());
        }

        Collections.shuffle(tasksIDList);

        for (Integer taskID : tasksIDList) {
            controlTaskManager.getTaskById(taskID);
        }

        final List<Task> controlHisoryList = controlTaskManager.getHistory();
        final Map<Integer, Task> controlTaskMap = Collections.unmodifiableMap(controlTaskManager.getAllTask());
        final Map<Integer, Task> controlEpicsMap = Collections.unmodifiableMap(controlTaskManager.getAllEpic());
        final Map<Integer, Task> controlSubtaskMap = Collections.unmodifiableMap(controlTaskManager.getAllSubtask());

        //////////////////////////////////////Востанавливаемся из файла/////////////////////////////////////////////////

        TaskManager loadFromFileTaskManager = new FileBackedTasksManager(saveForTest);

        final List<Task> HistoryListFromFile = controlTaskManager.getHistory();
        final Map<Integer, Task> TaskMapFromFile = Collections.unmodifiableMap(loadFromFileTaskManager.getAllTask());
        final Map<Integer, Task> EpicsMapFromFile = Collections.unmodifiableMap(loadFromFileTaskManager.getAllEpic());
        final Map<Integer, Task> SubtaskMapFromFile = Collections.unmodifiableMap(loadFromFileTaskManager.getAllSubtask());

        boolean isEqual = Objects.equals(controlHisoryList, HistoryListFromFile);
        if (!isEqual) System.out.println("Ошибка HistoryList не верно загружен.");
        isEqual =Objects.equals(controlTaskMap, TaskMapFromFile);
        if (!isEqual) System.out.println("Ошибка TaskMap не верно загружен!");
        isEqual = Objects.equals(controlEpicsMap, EpicsMapFromFile);
        if (!isEqual) System.out.println("Ошибка EpicMap не верно загружен!");
        isEqual = Objects.equals(controlSubtaskMap, SubtaskMapFromFile);
        if (!isEqual) System.out.println("Ошибка SubtaskMap не верно загружен!");

        System.out.println("Запись данных в файл и востановление из файла прошло успешно!");
    }
}
