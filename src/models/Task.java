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
    protected String type;

    public Task(String taskName, String taskDescription, Status status) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = status;
        this.type = "Task";
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
        this.type = "Task";
    }

    public Task(String taskName,
                String taskDescription,
                Status taskStatus,
                LocalDateTime taskStartTime,
                int zoneID,
                int durationInMinutes,
                int id
    ) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
        this.zone = ZoneId.of(zones.get(zoneID));
        this.taskStartTime = ZonedDateTime.of(taskStartTime, zone);
        this.taskDuration = Duration.ofMinutes(durationInMinutes);
        this.getTaskEndTime = this.taskStartTime.plus(taskDuration);
        this.taskID = id;
        this.type = "Task";
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
        this.type = "Task";
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskID == task.taskID
                && Objects.equals(taskName, task.taskName)
                && Objects.equals(taskDescription, task.taskDescription)
                && taskStatus == task.taskStatus
                && Objects.equals(taskStartTime, task.taskStartTime)
                && Objects.equals(taskDuration, task.taskDuration)
                && Objects.equals(getTaskEndTime, task.getTaskEndTime)
                && Objects.equals(zone, task.zone) && Objects.equals(zones, task.zones);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskName, taskDescription, taskStatus, taskID, taskStartTime, taskDuration, getTaskEndTime, zone, zones);
    }

    public String getType() {
        return type;
    }

    public Duration getTaskDuration() {
        return taskDuration;
    }

    public ZoneId getZone() {
        return zone;
    }

    public enum Status {
        NEW,
        IN_PROGRESS,
        DONE
    }
}
