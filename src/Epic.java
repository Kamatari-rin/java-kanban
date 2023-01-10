import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task{
    private final List<Integer> subtaskList = new ArrayList();

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription, Status.NEW);
    }



    public ArrayList getSubtaskList() {
        ArrayList<Integer> getSubtaskList = (ArrayList<Integer>) subtaskList;
        return  getSubtaskList;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskList);
    }

    @Override
    public String toString() {
        return "Epic{" +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskStatus=" + taskStatus +
                ", taskID=" + taskID +
                " subtaskList= " + subtaskList +
                '}'+"\n";
    }
}
