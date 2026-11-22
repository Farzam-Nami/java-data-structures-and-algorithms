package datastructures.linear;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/** A singly linked list supporting constant-time insertion at either end. */
public final class SinglyLinkedList<T> implements Iterable<T> {
    private static final class Node<E> {
        E value;
        Node<E> next;
        Node(E value, Node<E> next) { this.value = value; this.next = next; }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    public void addFirst(T value) {
        head = new Node<>(value, head);
        if (tail == null) tail = head;
        size++;
    }

    public void addLast(T value) {
        Node<T> node = new Node<>(value, null);
        if (tail == null) head = node;
        else tail.next = node;
        tail = node;
        size++;
    }

    public T first() {
        if (isEmpty()) throw new NoSuchElementException("list is empty");
        return head.value;
    }

    public T last() {
        if (isEmpty()) throw new NoSuchElementException("list is empty");
        return tail.value;
    }

    public T removeFirst() {
        T value = first();
        head = head.next;
        if (--size == 0) tail = null;
        return value;
    }

    public T removeLast() {
        T value = last();
        if (head == tail) { head = tail = null; size = 0; return value; }
        Node<T> current = head;
        while (current.next != tail) current = current.next;
        current.next = null;
        tail = current;
        size--;
        return value;
    }

    public boolean remove(T value) {
        Node<T> before = null;
        for (Node<T> current = head; current != null; current = current.next) {
            if (Objects.equals(current.value, value)) {
                if (before == null) head = current.next;
                else before.next = current.next;
                if (current == tail) tail = before;
                size--;
                return true;
            }
            before = current;
        }
        return false;
    }

    public boolean contains(T value) {
        for (T item : this) if (Objects.equals(item, value)) return true;
        return false;
    }

    /** Reverses links in-place in O(n) time and O(1) auxiliary space. */
    public void reverse() {
        Node<T> previous = null;
        Node<T> current = head;
        tail = head;
        while (current != null) {
            Node<T> next = current.next;
            current.next = previous;
            previous = current;
            current = next;
        }
        head = previous;
    }

    /** Selection sort by swapping payloads; O(n^2) time, O(1) extra space. */
    public void selectionSort(Comparator<? super T> order) {
        Objects.requireNonNull(order, "order");
        for (Node<T> i = head; i != null; i = i.next) {
            Node<T> minimum = i;
            for (Node<T> j = i.next; j != null; j = j.next)
                if (order.compare(j.value, minimum.value) < 0) minimum = j;
            T value = i.value; i.value = minimum.value; minimum.value = value;
        }
    }

    /** Stable linked-list merge sort; O(n log n) time, O(log n) recursion space. */
    public void mergeSort(Comparator<? super T> order) {
        Objects.requireNonNull(order, "order");
        head = mergeSortNodes(head, order);
        tail = head;
        if (tail != null) while (tail.next != null) tail = tail.next;
    }

    private Node<T> mergeSortNodes(Node<T> first, Comparator<? super T> order) {
        if (first == null || first.next == null) return first;
        Node<T> slow = first, fast = first.next;
        while (fast != null && fast.next != null) { slow = slow.next; fast = fast.next.next; }
        Node<T> second = slow.next;
        slow.next = null;
        return merge(mergeSortNodes(first, order), mergeSortNodes(second, order), order);
    }

    private Node<T> merge(Node<T> left, Node<T> right, Comparator<? super T> order) {
        Node<T> dummy = new Node<>(null, null), end = dummy;
        while (left != null && right != null) {
            if (order.compare(left.value, right.value) <= 0) {
                end.next = left; left = left.next;
            } else {
                end.next = right; right = right.next;
            }
            end = end.next;
        }
        end.next = left != null ? left : right;
        return dummy.next;
    }

    @Override public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = head;
            public boolean hasNext() { return current != null; }
            public T next() {
                if (current == null) throw new NoSuchElementException();
                T result = current.value; current = current.next; return result;
            }
        };
    }

    @Override public String toString() {
        StringBuilder text = new StringBuilder("[");
        for (T item : this) { if (text.length() > 1) text.append(", "); text.append(item); }
        return text.append(']').toString();
    }
}
