package datastructures.linear;

import java.util.Iterator;
import java.util.NoSuchElementException;

/** Circular singly linked list whose tail points back to its head. */
public final class CircularLinkedList<T> implements Iterable<T> {
    private static final class Node<E> {
        E value; Node<E> next;
        Node(E value) { this.value = value; }
    }
    private Node<T> tail;
    private int size;
    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
    public T first() {
        if (tail == null) throw new NoSuchElementException();
        return tail.next.value;
    }
    public T last() {
        if (tail == null) throw new NoSuchElementException();
        return tail.value;
    }
    public void addFirst(T value) {
        Node<T> added = new Node<>(value);
        if (tail == null) { added.next = added; tail = added; }
        else { added.next = tail.next; tail.next = added; }
        size++;
    }
    public void addLast(T value) { addFirst(value); tail = tail.next; }
    public T removeFirst() {
        T value = first();
        if (size == 1) tail = null;
        else tail.next = tail.next.next;
        size--; return value;
    }
    /** Moves the head to the end without allocating a new node. */
    public void rotate() { if (tail != null) tail = tail.next; }
    @Override public Iterator<T> iterator() {
        return new Iterator<T>() {
            Node<T> current = tail == null ? null : tail.next;
            int visited;
            public boolean hasNext() { return visited < size; }
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                T value = current.value; current = current.next; visited++; return value;
            }
        };
    }
    @Override public String toString() {
        StringBuilder text = new StringBuilder("[");
        for (T value : this) { if (text.length() > 1) text.append(", "); text.append(value); }
        return text.append(']').toString();
    }
}
