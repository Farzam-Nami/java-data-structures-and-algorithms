package datastructures.linear;

import java.util.Iterator;
import java.util.NoSuchElementException;

/** A doubly linked list accessed through validated node positions. */
public final class PositionalList<T> implements Iterable<T> {
    /** Opaque handle used to refer to an element without exposing list links. */
    public interface Position<E> { E get(); }

    private final Object identity = new Object();
    private final class Node implements Position<T> {
        final Object owner = identity;
        T value; Node previous, next;
        boolean active = true;
        Node(T value, Node previous, Node next) {
            this.value = value; this.previous = previous; this.next = next;
        }
        public T get() {
            if (!active) throw new IllegalArgumentException("position has been removed");
            return value;
        }
    }
    private Node head, tail;
    private int size;
    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
    public Position<T> first() { return head; }
    public Position<T> last() { return tail; }
    public Position<T> after(Position<T> position) { return validate(position).next; }
    public Position<T> before(Position<T> position) { return validate(position).previous; }
    public Position<T> addFirst(T value) { return link(value, null, head); }
    public Position<T> addLast(T value) { return link(value, tail, null); }
    public Position<T> addBefore(Position<T> position, T value) {
        Node node = validate(position); return link(value, node.previous, node);
    }
    public Position<T> addAfter(Position<T> position, T value) {
        Node node = validate(position); return link(value, node, node.next);
    }
    public T set(Position<T> position, T value) {
        Node node = validate(position); T old = node.value; node.value = value; return old;
    }
    public T remove(Position<T> position) {
        Node node = validate(position);
        if (node.previous == null) head = node.next; else node.previous.next = node.next;
        if (node.next == null) tail = node.previous; else node.next.previous = node.previous;
        size--; node.active = false; node.previous = node.next = null;
        return node.value;
    }
    private Node link(T value, Node before, Node after) {
        Node node = new Node(value, before, after);
        if (before == null) head = node; else before.next = node;
        if (after == null) tail = node; else after.previous = node;
        size++; return node;
    }
    /** Verifies both ownership and liveness in O(1) time. */
    private Node validate(Position<T> position) {
        if (!(position instanceof PositionalList<?>.Node))
            throw new IllegalArgumentException("invalid position type");
        Node node = (Node) position;
        if (node.owner != identity) throw new IllegalArgumentException("position belongs to another list");
        if (!node.active) throw new IllegalArgumentException("position has been removed");
        return node;
    }
    @Override public Iterator<T> iterator() {
        return new Iterator<T>() {
            Node current = head;
            public boolean hasNext() { return current != null; }
            public T next() {
                if (current == null) throw new NoSuchElementException();
                T value = current.value; current = current.next; return value;
            }
        };
    }
    public Iterable<Position<T>> positions() {
        return () -> new Iterator<Position<T>>() {
            Node current = head;
            public boolean hasNext() { return current != null; }
            public Position<T> next() {
                if (!hasNext()) throw new NoSuchElementException();
                Node old = current; current = current.next; return old;
            }
        };
    }
}
