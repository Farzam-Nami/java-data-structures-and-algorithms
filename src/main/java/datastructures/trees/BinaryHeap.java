package datastructures.trees;

import datastructures.linear.DynamicArray;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;

/** Binary heap supporting min-heap and max-heap ordering through a comparator. */
public final class BinaryHeap<T> {
    private final DynamicArray<T> values = new DynamicArray<>();
    private final Comparator<? super T> order;

    public BinaryHeap(Comparator<? super T> order) {
        this.order = Objects.requireNonNull(order, "order");
    }
    public static <E extends Comparable<? super E>> BinaryHeap<E> minHeap() {
        return new BinaryHeap<>(Comparator.naturalOrder());
    }
    public static <E extends Comparable<? super E>> BinaryHeap<E> maxHeap() {
        return new BinaryHeap<>(Comparator.reverseOrder());
    }
    public int size() { return values.size(); }
    public boolean isEmpty() { return values.isEmpty(); }
    public void offer(T value) {
        Objects.requireNonNull(value, "value");
        values.add(value); siftUp(values.size() - 1);
    }
    public T peek() {
        if (isEmpty()) throw new NoSuchElementException("heap is empty");
        return values.get(0);
    }
    public T poll() {
        T answer = peek();
        T last = values.removeLast();
        if (!values.isEmpty()) { values.set(0, last); siftDown(0); }
        return answer;
    }
    private void siftUp(int child) {
        while (child > 0) {
            int parent = (child - 1) / 2;
            if (order.compare(values.get(parent), values.get(child)) <= 0) break;
            swap(parent, child); child = parent;
        }
    }
    private void siftDown(int parent) {
        while (parent * 2 + 1 < size()) {
            int child = parent * 2 + 1, right = child + 1;
            if (right < size() && order.compare(values.get(right), values.get(child)) < 0) child = right;
            if (order.compare(values.get(parent), values.get(child)) <= 0) break;
            swap(parent, child); parent = child;
        }
    }
    private void swap(int a, int b) {
        T temp = values.get(a); values.set(a, values.get(b)); values.set(b, temp);
    }
}
