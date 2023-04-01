package models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
public class Subtask extends Task {
    private int epicID;

    public Subtask(
            String taskName,
            String taskDescription,
            Status taskStatus,
            int epicID,
            LocalDateTime taskStartTime,
            int zoneID,
            int durationInMinutes) {
        super(taskName, taskDescription, taskStatus, taskStartTime, zoneID, durationInMinutes);
        this.epicID = epicID;
        type = "Subtask";
    }

    public Subtask(
            String taskName,
            String taskDescription,
            Status taskStatus,
            int epicID,
            LocalDateTime taskStartTime,
            int zoneID,
            int durationInMinutes,
            int id) {
        super(taskName, taskDescription, taskStatus, taskStartTime, zoneID, durationInMinutes);
        this.epicID = epicID;
        this.taskID = id;
        type = "Subtask";
    }

    public Subtask(
            String taskName,
            String taskDescription,
            Status taskStatus,
            int epicID,
            ZonedDateTime taskStartTime,
            ZonedDateTime taskEndTime,
            ZoneId zoneID,
            Duration duration) {
        super(taskName, taskDescription, taskStatus, taskStartTime, taskEndTime, zoneID, duration);
        this.epicID = epicID;
        type = "Subtask";
    }

    public int getEpicID() {
        return epicID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskName, taskDescription, taskStatus);
    }

    public Duration getSubtaskDuration() {
        return taskDuration;
    }
    public ZoneId getSubtaskZoneID() {
        return zone;
    }

    @Override
    public String toString() {
        return taskID + ",Subtask," + taskName + "," + taskStatus + "," + taskDescription + "," + taskStartTime + ","
                + getTaskEndTime + "," + taskDuration + "," + epicID + ",";
    }
}
