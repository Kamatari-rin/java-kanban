import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task{
    private ArrayList subtaskList = new ArrayList();

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription, Status.NEW);
    }

    @Override
    public String toString() {
        return "\nЗадача: "  + taskName + "\nОписание: " + taskDescription + "\nСтатус: " + taskStatus + "\nСписок подзадач:"
                + subtaskList.toString();
    }

    public ArrayList getSubtaskList() {
        return subtaskList;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskList);
    }
}
