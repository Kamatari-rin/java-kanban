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
}
