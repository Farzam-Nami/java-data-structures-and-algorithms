package datastructures.linear;

/** FIFO queue with O(1) enqueue and dequeue. */
public final class LinkedQueue<T> {
    private final SinglyLinkedList<T> values = new SinglyLinkedList<>();
    public void enqueue(T value) { values.addLast(value); }
    public T dequeue() { return values.removeFirst(); }
    public T peek() { return values.first(); }
    public int size() { return values.size(); }
    public boolean isEmpty() { return values.isEmpty(); }
}
