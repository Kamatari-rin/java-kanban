package models;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task{
    private List<Integer> subtaskList;

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription, Status.NEW);
    }

    public Epic(String taskName, String taskDescription, Status taskStatus) {
        super(taskName, taskDescription, taskStatus);
    }

    public List<Integer> getSubtaskList() {
        if (subtaskList == null) throw new RuntimeException("Список подзадач пуст.");
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

    public void setSubtaskList(List<Integer> subtaskList) {
        this.subtaskList = subtaskList;
    }
}
