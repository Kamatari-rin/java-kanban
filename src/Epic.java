import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task{
    private HashMap subtaskList;
    private boolean isAllSubTaskIsDone;

    public Epic(String name, String description, Status status, int id, HashMap subtaskList, boolean isAllSubTaskIsDone) {
        super(name, description, id, status);
        this.subtaskList = subtaskList;
        this.isAllSubTaskIsDone = isAllSubTaskIsDone;
    }
    // Пытался убрать из вывода ключи HashMap (1=, 2=, ...), но так и не понял как это сделать(.
    @Override
    public String toString() {
        return "\nЗадача: "  + name + "\nОписание: " + description + "\nСтатус: " + status + "\nСписок подзадач:\n"
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
