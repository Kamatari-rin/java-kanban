package services.history;

public class Node<T> {
    public Node<T> prev;
    public T data;
    public Node<T> next;

    public Node(Node<T> prev, T data, Node<T> next) {
        this.next = prev;
        this.data = data;
        this.prev = next;
    }
}
