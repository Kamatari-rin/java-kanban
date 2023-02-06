package services.history;

import models.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomLinkedList<T> {

    private final Map<Integer, Node<T>> nodeHashMap = new HashMap<>();
    private Node<T> head;
    private Node<T> tail;
    private int size = 0;

    public void linkLast(T task) {
        final Node<T> oldTail = tail;

        // Я не понял почему все работает в такой реализации.
        // В конструкторе Node стоит (Node<T> prev, T data, Node<T> next) - Ссылка на предыдущий нод, данные, ссылка на следующий нод
        // То есть, как я понял мы должны при создании нового нода который станет новым хвостом передавать данные так - (oldTale, task, null)
        // Но почему-то если передавать так, то prev задается как null, а next присваивается значение prev.
        final Node<T> newNode = new Node<>(null, task, oldTail);
        tail = newNode;

        if (oldTail == null) head = newNode;
        else oldTail.next = newNode;
        size++;

        Task bufTask = (Task) task;

        if (nodeHashMap.containsKey(bufTask.getTaskID())) {
            removeNode(nodeHashMap.get(bufTask.getTaskID()));
        }

        nodeHashMap.put(bufTask.getTaskID(), newNode);
    }

    private void removeNode(Node<T> node) {

        Node<T> removebleNode = node;
        Node<T> nodePrev = removebleNode.prev;
        Node<T> nodeNext = removebleNode.next;

        nodePrev.next = removebleNode.next;
        nodeNext.prev = removebleNode.prev;
    }

    public List<Task> getTasks() {

        final List<Task> taskHistoryList = new ArrayList<>();
        Node<T> bufNode = this.head;

        while (bufNode != null) {
            taskHistoryList.add((Task) bufNode.data);
            bufNode = bufNode.next;
        }

        return taskHistoryList;
    }

    public int getSize() {
        return size;
    }

    public void removeHead(CustomLinkedList<T> customLinkedList) {
        Node<T> newdHead = customLinkedList.head;
        newdHead = newdHead.next;
        newdHead.prev = null;
        size--;
    }

    public void removeById(CustomLinkedList<T> customLinkedList, int taskID) {
        final Map<Integer, Node<T>> nodeHashMap = customLinkedList.getNodeHashMap();
        Node<T> removedNode = nodeHashMap.get(taskID);
        removeNode(removedNode);
    }

    public Map<Integer, Node<T>> getNodeHashMap() {
        return nodeHashMap;
    }
}
