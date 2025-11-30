package LockFreeStack;

public class SynchronizedStack<T> {
    private static class Node<E> {
        final E value;
        Node<E> next;
        Node(E value) { this.value = value; }
    }

    private Node<T> head;

    public synchronized void push(T value) {
        Node<T> n = new Node<>(value);
        n.next = head;
        head = n;
    }

    public synchronized T pop() {
        if (head == null) return null;
        T v = head.value;
        head = head.next;
        return v;
    }

    public synchronized boolean isEmpty() {
        return head == null;
    }
}
