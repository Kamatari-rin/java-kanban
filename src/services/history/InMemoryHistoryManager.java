package services.history;

import models.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager{

    private final CustomLinkedList<Task> viewHistory = new CustomLinkedList<>();

    @Override
    public void add(Task task) {

        if (viewHistory.getSize() < 10) {
            viewHistory.linkLast(task);
        } else {
            viewHistory.removeHead(viewHistory);
            viewHistory.linkLast(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return viewHistory.getTasks();
    }


    @Override
    public void remove(int taskID) {
        viewHistory.removeById(viewHistory, taskID);
    }

    @Override
    public boolean isHistoryContainsTask(int taskID) {
        if (viewHistory.contains(taskID)) return true;
        else return false;
    }
}
