package datastructures.algorithms;

import java.util.Comparator;
import java.util.Objects;

/** Linear and binary search over arrays. */
public final class Searching {
    private Searching() { }
    public static <T> int linearSearch(T[] values, T target) {
        Objects.requireNonNull(values, "values");
        for (int i = 0; i < values.length; i++)
            if (Objects.equals(values[i], target)) return i;
        return -1;
    }
    /** Binary search requires values to be sorted in ascending comparator order. */
    public static <T> int binarySearch(T[] values, T target, Comparator<? super T> order) {
        Objects.requireNonNull(values, "values"); Objects.requireNonNull(order, "order");
        int low = 0, high = values.length - 1;
        while (low <= high) {
            int middle = low + (high - low) / 2;
            int difference = order.compare(values[middle], target);
            if (difference == 0) return middle;
            if (difference < 0) low = middle + 1; else high = middle - 1;
        }
        return -1;
    }
}
