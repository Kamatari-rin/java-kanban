import java.util.HashMap;

public class Task {

    protected String taskName;
    protected String taskDescription;
    protected TaskType taskType;
    protected Status taskStatus;
    protected int taskID;
    private int epicID;
    private int taskPriority;
    private HashMap subtaskList;
    private boolean isAllSubTaskIsDone;

    public Task(String taskName, String taskDescription, TaskType taskType, Status taskStatus) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskType = taskType;
        this.taskStatus = taskStatus;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getTaskID() {
        return taskID;
    }

    protected enum Status {
        NEW,
        IN_PROGRESS,
        DONE
    }
    protected enum TaskType {
        TASK,
        EPIC,
        SUBTASK
    }
}
