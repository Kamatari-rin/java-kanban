package services.history;
import models.Task;
import java.util.List;

public interface HistoryManager {

    void add(Task task);

    void remove (int taskID);

    List<Task> getHistory();

    boolean isHistoryContainsTask(int taskID);
}
