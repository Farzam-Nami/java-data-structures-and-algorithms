package datastructures.linear;

import java.util.NoSuchElementException;

/** LIFO stack backed by a singly linked list; all operations take O(1) time. */
public final class LinkedStack<T> {
    private final SinglyLinkedList<T> values = new SinglyLinkedList<>();
    public void push(T value) { values.addFirst(value); }
    public T pop() { return values.removeFirst(); }
    public T peek() { return values.first(); }
    public int size() { return values.size(); }
    public boolean isEmpty() { return values.isEmpty(); }
}
