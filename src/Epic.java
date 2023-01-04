import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task{
    private HashMap subtaskList;
    private boolean isAllSubTaskIsDone;

    public Epic(String taskName, String taskDescription, TaskType taskType, Status taskStatus, HashMap subtaskList, boolean isAllSubTaskIsDone) {
        super(taskName, taskDescription, taskType, taskStatus);
        this.subtaskList = subtaskList;
        this.isAllSubTaskIsDone = isAllSubTaskIsDone;
    }

    @Override
    public String toString() {
        return "\nЗадача: "  + taskName + "\nОписание: " + taskDescription + "\nСтатус: " + taskStatus + "\nСписок подзадач:\n"
                + subtaskList.toString()
                .replaceAll("^\\[|\\]$", "")
                .replaceAll(",", "")
                .replace("{", " ")
                .replace("}", "");
    }

    public boolean isAllSubTaskIsDone() {
        return isAllSubTaskIsDone;
    }

    public void setAllSubTaskIsDone(boolean allSubTaskIsDone) {
        isAllSubTaskIsDone = allSubTaskIsDone;
    }

    public HashMap getSubtaskList() {
        return subtaskList;
    }
}
