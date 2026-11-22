package datastructures.linear;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/** A bidirectional linked list with O(1) operations at both ends. */
public final class DoublyLinkedList<T> implements Iterable<T> {
    private static final class Node<E> {
        E value; Node<E> previous, next;
        Node(E value, Node<E> previous, Node<E> next) {
            this.value = value; this.previous = previous; this.next = next;
        }
    }

    private Node<T> head, tail;
    private int size;
    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    public void addFirst(T value) {
        Node<T> node = new Node<>(value, null, head);
        if (head != null) head.previous = node; else tail = node;
        head = node; size++;
    }
    public void addLast(T value) {
        Node<T> node = new Node<>(value, tail, null);
        if (tail != null) tail.next = node; else head = node;
        tail = node; size++;
    }
    public T first() {
        if (head == null) throw new NoSuchElementException("list is empty");
        return head.value;
    }
    public T last() {
        if (tail == null) throw new NoSuchElementException("list is empty");
        return tail.value;
    }
    public T removeFirst() { return unlink(requireNode(head)); }
    public T removeLast() { return unlink(requireNode(tail)); }

    public boolean remove(T value) {
        for (Node<T> n = head; n != null; n = n.next)
            if (Objects.equals(n.value, value)) { unlink(n); return true; }
        return false;
    }

    public T get(int index) { return node(index).value; }
    public T set(int index, T value) {
        Node<T> n = node(index); T old = n.value; n.value = value; return old;
    }

    public void insert(int index, T value) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("index: " + index);
        if (index == size) { addLast(value); return; }
        Node<T> after = node(index), before = after.previous;
        Node<T> added = new Node<>(value, before, after);
        after.previous = added;
        if (before == null) head = added; else before.next = added;
        size++;
    }

    public T removeAt(int index) { return unlink(node(index)); }

    private Node<T> requireNode(Node<T> node) {
        if (node == null) throw new NoSuchElementException("list is empty");
        return node;
    }
    private T unlink(Node<T> n) {
        if (n.previous == null) head = n.next; else n.previous.next = n.next;
        if (n.next == null) tail = n.previous; else n.next.previous = n.previous;
        size--; return n.value;
    }
    private Node<T> node(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("index: " + index);
        Node<T> current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) current = current.next;
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) current = current.previous;
        }
        return current;
    }

    /** Stable insertion sort by moving values backward through earlier nodes. */
    public void insertionSort(Comparator<? super T> order) {
        Objects.requireNonNull(order, "order");
        for (Node<T> current = head == null ? null : head.next; current != null; current = current.next) {
            T value = current.value;
            Node<T> previous = current.previous;
            while (previous != null && order.compare(previous.value, value) > 0) {
                previous.next.value = previous.value;
                previous = previous.previous;
            }
            if (previous == null) head.value = value;
            else previous.next.value = value;
        }
    }

    public Iterable<T> reverseView() {
        return () -> new Iterator<T>() {
            Node<T> current = tail;
            public boolean hasNext() { return current != null; }
            public T next() {
                if (current == null) throw new NoSuchElementException();
                T value = current.value; current = current.previous; return value;
            }
        };
    }

    @Override public Iterator<T> iterator() {
        return new Iterator<T>() {
            Node<T> current = head;
            public boolean hasNext() { return current != null; }
            public T next() {
                if (current == null) throw new NoSuchElementException();
                T value = current.value; current = current.next; return value;
            }
        };
    }
    @Override public String toString() {
        StringBuilder text = new StringBuilder("[");
        for (T value : this) { if (text.length() > 1) text.append(", "); text.append(value); }
        return text.append(']').toString();
    }
}
