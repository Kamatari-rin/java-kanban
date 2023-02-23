package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task{
    private List<Integer> subtaskList = new ArrayList();

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription, Status.NEW);
    }

    public Epic(String taskName, String taskDescription, Status taskStatus) {
        super(taskName, taskDescription, taskStatus);
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

    public void setSubtaskList(List<Integer> subtaskList) {
        this.subtaskList = subtaskList;
    }
}
