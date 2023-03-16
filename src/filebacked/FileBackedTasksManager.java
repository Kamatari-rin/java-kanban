package filebacked;

import exceptions.FileCreateException;
import exceptions.FileReadException;
import exceptions.FileWriteException;
import models.Epic;
import models.Subtask;
import models.Task;

import services.taskmanagers.InMemoryTaskManager;
import services.taskmanagers.TaskType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileBackedTasksManager<T extends Task> extends InMemoryTaskManager<T> {

    private  Path userData;

    public FileBackedTasksManager(Path path) {
        userData = path;
        if (Files.exists(path)) loadFromFile(path);
    }

    ///////////////////////////////////////////// Запись в Файл ////////////////////////////////////////////////////////
    public void save() {
        createFile(this.userData);

        final List<String> tasksList = new ArrayList<>();
        String historyList;

        try {
             historyList = historyToString(List.copyOf(super.getHistory()));
        } catch (RuntimeException e) {
            historyList = "";
        }

        if (!super.tasksMap.isEmpty()){
            tasksList.addAll(taskMapToStringList(Map.copyOf(super.getAllTask())));
        }
        if (!super.epicsMap.isEmpty()) {
            tasksList.addAll(taskMapToStringList(Map.copyOf(super.getAllEpic())));
        }
        if (!super.subtasksMap.isEmpty()) {
            tasksList.addAll(taskMapToStringList(Map.copyOf(super.getAllSubtask())));
        }

        try (Writer fileWriter = new FileWriter(this.userData.toFile())) {
            fileWriter.write("id,type,name,status,description,start_date_time,end_date_time,duration,epic,\n");
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл");
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
    //  0  1    2      3        4            5              6           7      8
    // id,type,name,status,description,start_date_time,end_date_time,duration,epic,
    public void loadFromFile(Path path) {
        List<String> dataFromFile = writeDataFromFileInList(path);
        Map<Integer, List<Integer>> subtasksByEpic = new HashMap<>();

        for (String data : dataFromFile) {
            if (data.isBlank()) continue;
            String[] taskData = data.split(",");

            int id = Integer.parseInt(taskData[0]);
            String title = taskData[2];
            String description = taskData[4];
            ZonedDateTime taskStartTime = null;
            ZonedDateTime taskEndTime = null;
            ZoneId zoneID = null;
            Duration taskDuration = null;


            int epicID = -1;

            if (taskData.length == 9) {
                epicID = Integer.parseInt(taskData[8]);
            }
            if (taskData.length == 8 || taskData.length == 9) {
                final DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
                taskStartTime = ZonedDateTime.parse(taskData[5], formatter);
                taskEndTime = ZonedDateTime.parse(taskData[6], formatter);
                zoneID = taskStartTime.getZone();
                taskDuration = Duration.parse(taskData[7]);
            }

            String stringStatus = taskData[3];
            Task.Status taskStatus = null;
            switch (stringStatus) {
                case "NEW":
                    taskStatus = Task.Status.NEW;
                    break;
                case "IN_PROGRESS":
                    taskStatus = Task.Status.IN_PROGRESS;
                    break;
                case "DONE":
                    taskStatus = Task.Status.DONE;
            }

            String taskType = taskData[1];
            TaskType type = null;

            switch (taskType) {
                case "Task":
                    type = TaskType.TASK;
                    Task task = new Task(title, description, taskStatus, taskStartTime, taskEndTime, zoneID, taskDuration);
                    task.setTaskID(id);
                    this.tasksMap.put(id, task);
                    break;
                case "Epic":
                    type = TaskType.EPIC;
                    Epic epic = new Epic(title, description);
                    epic.setTaskID(id);
                    this.epicsMap.put(id, epic);
                    break;
                case "Subtask":
                    type = TaskType.SUBTASK;
                    Subtask subtask = new Subtask(title, description, taskStatus, epicID, taskStartTime, taskEndTime, zoneID, taskDuration);
                    subtask.setTaskID(id);
                    this.subtasksMap.put(id, subtask);
                    List<Integer> subtaskList = subtasksByEpic.getOrDefault(epicID, new ArrayList<>());
                    subtaskList.add(subtask.getTaskID());
                    subtasksByEpic.put(epicID, subtaskList);
            }

            if (!taskData[1].equals("Task") && !taskData[1].equals("Epic") && !taskData[1].equals("Subtask") ) {
                for (int i = 0; i < taskData.length; i++) {
                    if (tasksMap.containsKey(Integer.parseInt(taskData[i]))) {
                        Task task = (Task) tasksMap.get(Integer.parseInt(taskData[i]));
                        this.historyManager.add(task);
                    } else if (epicsMap.containsKey(Integer.parseInt(taskData[i]))) {
                        Epic task = (Epic) epicsMap.get(Integer.parseInt(taskData[i]));
                        this.historyManager.add(task);
                    } else if (subtasksMap.containsKey(Integer.parseInt(taskData[i]))) {
                        Subtask task = (Subtask) subtasksMap.get(Integer.parseInt(taskData[i]));
                        this.historyManager.add(task);
                    }
                }
            }
        }
        putSubtasksListOnEpic(epicsMap, subtasksByEpic);
    }


    private List<String> writeDataFromFileInList(final Path path) {
        final List<String> dataFromFile = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(this.userData.toFile(), StandardCharsets.UTF_8))) {
            while (br.ready()) {
                String line = br.readLine();
                dataFromFile.add(line);
            }
            dataFromFile.remove(0);
            return dataFromFile;
        } catch (IOException e) {
            throw new FileReadException("Ошибка при чтении файла.");
        }
    }

    private void putSubtasksListOnEpic(Map<Integer, Epic> epicsMap, Map<Integer, List<Integer>> subtasksByEpic) {
        for (Integer epicID : subtasksByEpic.keySet()) {
            Epic epic = epicsMap.get(epicID);
            epic.setSubtaskList(subtasksByEpic.get(epicID));
            epicUpdateStatus(epic);
        }


    }

    @Override
    public int createTask(T task) throws IOException {
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
    public Epic epicUpdateStatus(Epic epic) {
        Epic updatedEpic = super.epicUpdateStatus(epic);
        save();
        return updatedEpic;
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
    @Override
    public Set<Task> getPrioritizedTasks() throws RuntimeException {
        return super.getPrioritizedTasks();
    }
}
