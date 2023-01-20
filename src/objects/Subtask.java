package objects;

import java.util.Objects;
public class Subtask extends Task {
    private int epicID;

    public Subtask(String taskName, String taskDescription, Status taskStatus, int epicID) {
        super(taskName, taskDescription, taskStatus);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskName, taskDescription, taskStatus);
    }

    @Override
    public String toString() {
        return "objects.Subtask{" +
                "epicID=" + epicID  +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskStatus=" + taskStatus +
                ", taskID=" + taskID +
                '}'+"\n";
    }
}
