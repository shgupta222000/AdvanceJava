package LockFreeStack;

import java.util.concurrent.atomic.AtomicReference;

public class TreiberStack<T> {
    private static class Node<E> {
        final E value;
        final Node<E> next;
        Node(E value, Node<E> next) { this.value = value; this.next = next; }
    }

    private final AtomicReference<Node<T>> head = new AtomicReference<>(null);

    public void push(T value) {
        Node<T> newNode;
        Node<T> oldHead;
        do {
            oldHead = head.get();
            newNode = new Node<>(value, oldHead);
            // attempt CAS: if head == oldHead -> set to newNode
        } while (!head.compareAndSet(oldHead, newNode));
    }

    public T pop() {
        Node<T> oldHead;
        Node<T> newHead;
        do {
            oldHead = head.get();
            if (oldHead == null) return null;
            newHead = oldHead.next;
            // attempt CAS: if head == oldHead -> set to newHead
        } while (!head.compareAndSet(oldHead, newHead));
        return oldHead.value;
    }

    public boolean isEmpty() {
        return head.get() == null;
    }
}
