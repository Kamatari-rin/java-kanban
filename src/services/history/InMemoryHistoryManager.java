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

    // Долго тупил как и где лучше бросать ошибку и как ее принимать
    // В итоге сделал что ошибка создается в CustomLinkedList, приниматся тут и создается новая такая же
    // Но как пробросить ошибку которая создается в CustomLinkedList дальше я не понял.
    @Override
    public List<Task> getHistory() throws RuntimeException {
        return viewHistory.getTasks();
    }


    @Override
    public void remove(int taskID) throws RuntimeException {
        viewHistory.removeById(viewHistory, taskID);
    }

    @Override
    public boolean isHistoryContainsTask(int taskID) {
        if (viewHistory.contains(taskID)) return true;
        else return false;
    }
}
