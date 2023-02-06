package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task{
    private final List<Integer> subtaskList = new ArrayList();

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription, Status.NEW);
    }

    public List<Integer> getSubtaskList() {
        return subtaskList;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskList);
    }
}
