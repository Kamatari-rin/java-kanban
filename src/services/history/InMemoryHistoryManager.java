package services.history;

import models.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private final List<Task> viewHistory = new ArrayList<>();

    @Override
    public List<Task> getHistory() {
        return viewHistory;
    }

    @Override
    public void add(Task task) {
        if (viewHistory.size() < 10) {
            viewHistory.add(task);
        } else {
            viewHistory.remove(0);
            viewHistory.add(task);
        }
    }
}
