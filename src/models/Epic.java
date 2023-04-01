package models;

import java.time.Duration;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task{
    private final List<Integer> subtaskList = new ArrayList<>();
    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription, Status.NEW);
        type = "Epic";
    }
    public Epic(String taskName, String taskDescription, Status taskStatus, int id) {
        super(taskName, taskDescription, taskStatus);
        this.taskID = id;
        type = "Epic";
    }

    public Epic(String taskName, String taskDescription, Status taskStatus) {
        super(taskName, taskDescription, taskStatus);
        type = "Epic";
    }

    public List<Integer> getSubtaskList() {
        return subtaskList;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskList);
    }

    @Override
    public String toString() {
        return taskID + ",Epic," + taskName + "," + taskStatus + "," + taskDescription + ",";
    }

    public Duration getEpicDuration() {
        return taskDuration;
    }

    public void setEpicDuration(Duration duration) {
        this.taskDuration = duration;
    }

    public void setZoneID(ZoneId zoneID) {
        this.zone = zoneID;
    }
}
