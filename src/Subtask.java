import java.util.Objects;

public class Subtask extends Task {
    private int epicID;
    private int taskPriority;

    public Subtask(String taskName, String taskDescription, Status taskStatus, int epicID, int taskPriority) {
        super(taskName, taskDescription, taskStatus);
        this.epicID = epicID;
        this.taskPriority = taskPriority;
    }


    public int getPriority() {
        return taskPriority;
    }

    public int getEpicID() {
        return epicID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskName, taskDescription, taskStatus, taskPriority);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicID=" + epicID +
                ", taskPriority=" + taskPriority +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskStatus=" + taskStatus +
                ", taskID=" + taskID +
                '}';
    }
}
