package models;

import java.util.Objects;
public class Task {
    protected String taskName;
    protected String taskDescription;
    protected Status taskStatus;
    protected int taskID;

    public Task(String taskName, String taskDescription, Status taskStatus) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskStatus(Status taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Status getTaskStatus() {
        return taskStatus;
    }

    @Override
    public String toString() {
        return  "\n   * " + taskName +
                ", " + taskDescription +
                ", " + taskStatus +
                ", [id : " + taskID +
                "]";
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public enum Status {
        NEW,
        IN_PROGRESS,
        DONE
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj;
        return Objects.equals(taskName, otherTask.taskName)
                && Objects.equals(taskDescription, otherTask.taskDescription)
                && taskStatus == otherTask.taskStatus;
    }
    @Override
    public int hashCode() {
        return Objects.hash(taskName, taskDescription, taskStatus);
    }
}
