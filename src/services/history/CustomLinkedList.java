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
        if (removebleNode.next == null) {
            tail = removebleNode.prev;
        } else if (removebleNode.prev == null) {
            head = removebleNode.next;
        } else if (removebleNode.prev != null && removebleNode.next != null) {
            Node<T> nodePrev = removebleNode.prev;
            Node<T> nodeNext = removebleNode.next;
            nodePrev.next = removebleNode.next;
            nodeNext.prev = removebleNode.prev;
        } else {
            head = null;
            tail = null;
            nodeHashMap.clear();
        }

    }

    public List<Task> getTasks() {
        if (nodeHashMap.isEmpty()) throw new RuntimeException("История пуста.");
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
        Task task = (Task) head.data;
        removeById(customLinkedList, task.getTaskID());
        size--;
    }

    public void removeById(CustomLinkedList<T> customLinkedList, int taskID) {
        if (!nodeHashMap.containsKey(taskID)) throw new RuntimeException("В истории такой задачи не существует.");
        final Map<Integer, Node<T>> nodeHashMap = customLinkedList.getNodeHashMap();
        Node<T> removedNode = nodeHashMap.get(taskID);
        removeNode(removedNode);
        nodeHashMap.remove(taskID);
    }

    public Map<Integer, Node<T>> getNodeHashMap() {
        return nodeHashMap;
    }

    public boolean contains(int taskID) {
        if (!nodeHashMap.containsKey(taskID)) return false;
        return true;
    }
}
