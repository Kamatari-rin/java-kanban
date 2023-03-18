package models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
public class Task {
    protected String taskName;
    protected String taskDescription;
    protected Status taskStatus;
    protected int taskID;
    protected ZonedDateTime taskStartTime;
    protected Duration taskDuration = Duration.ZERO;
    protected ZonedDateTime getTaskEndTime;

    protected ZoneId zone;
    protected final List<String> zones = Arrays.asList("America/New_York", "Asia/Vladivostok", "Europe/Moscow");

    public Task(String taskName, String taskDescription, Status status) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = status;
    }

    public Task(String taskName,
                String taskDescription,
                Status taskStatus,
                LocalDateTime taskStartTime,
                int zoneID,
                int durationInMinutes
                ) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
        this.zone = ZoneId.of(zones.get(zoneID));
        this.taskStartTime = ZonedDateTime.of(taskStartTime, zone);
        this.taskDuration = Duration.ofMinutes(durationInMinutes);
        this.getTaskEndTime = this.taskStartTime.plus(taskDuration);
    }
    public Task(String taskName,
                String taskDescription,
                Status taskStatus,
                ZonedDateTime taskStartTime,
                ZonedDateTime taskEndTime,
                ZoneId zoneID,
                Duration duration
    ) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
        this.zone = zoneID;
        this.taskStartTime = taskStartTime;
        this.taskDuration = duration;
        this.getTaskEndTime = taskEndTime;
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
        return  taskID + ",Task," + taskName + "," + taskStatus + "," + taskDescription + ","
                + taskStartTime + "," + getTaskEndTime + "," + taskDuration + ",";
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public ZonedDateTime getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(ZonedDateTime taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public ZonedDateTime getGetTaskEndTime() {
        return getTaskEndTime;
    }

    public void setGetTaskEndTime(ZonedDateTime getTaskEndTime) {
        this.getTaskEndTime = getTaskEndTime;
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
                && taskStatus == otherTask.taskStatus
                && taskStartTime == otherTask.taskStartTime
                && taskDuration == otherTask.taskDuration
                && getTaskEndTime == otherTask.getTaskEndTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskName, taskDescription, taskStatus);
    }
}
