package datastructures.linear;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/** A resizable array with constant-time indexed access and amortized constant-time append. */
public final class DynamicArray<T> implements Iterable<T> {
    private Object[] elements;
    private int size;

    public DynamicArray() { this(4); }

    public DynamicArray(int initialCapacity) {
        if (initialCapacity < 1) throw new IllegalArgumentException("capacity must be positive");
        elements = new Object[initialCapacity];
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
    public int capacity() { return elements.length; }

    @SuppressWarnings("unchecked")
    public T get(int index) { checkElement(index); return (T) elements[index]; }

    public T set(int index, T value) {
        T previous = get(index);
        elements[index] = value;
        return previous;
    }

    public void add(T value) { insert(size, value); }

    public void insert(int index, T value) {
        checkPosition(index);
        ensureCapacity(size + 1);
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = value;
        size++;
    }

    public T remove(int index) {
        T old = get(index);
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        elements[--size] = null; // Release a reference that is no longer logically stored.
        return old;
    }

    public T removeLast() {
        if (isEmpty()) throw new NoSuchElementException("array is empty");
        return remove(size - 1);
    }

    private void ensureCapacity(int minimum) {
        if (minimum > elements.length) {
            int newCapacity = Math.max(minimum, elements.length * 2);
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }

    private void checkElement(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("index: " + index);
    }

    private void checkPosition(int index) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("index: " + index);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int index;
            public boolean hasNext() { return index < size; }
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                return get(index++);
            }
        };
    }

    @Override public String toString() {
        StringBuilder text = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) text.append(", ");
            text.append(elements[i]);
        }
        return text.append(']').toString();
    }
}
