package datastructures.algorithms;

import java.util.Comparator;
import java.util.Objects;

/** In-place array sorting algorithms for comparing complexity and stability. */
public final class Sorting {
    private Sorting() { }

    /** Stable insertion sort: O(n^2) worst case, O(1) extra space. */
    public static <T> void insertionSort(T[] values, Comparator<? super T> order) {
        check(values, order);
        for (int i = 1; i < values.length; i++) {
            T current = values[i]; int j = i - 1;
            while (j >= 0 && order.compare(values[j], current) > 0) {
                values[j + 1] = values[j]; j--;
            }
            values[j + 1] = current;
        }
    }

    /** Selection sort: O(n^2) comparisons and O(1) extra space. */
    public static <T> void selectionSort(T[] values, Comparator<? super T> order) {
        check(values, order);
        for (int i = 0; i < values.length; i++) {
            int minimum = i;
            for (int j = i + 1; j < values.length; j++)
                if (order.compare(values[j], values[minimum]) < 0) minimum = j;
            swap(values, i, minimum);
        }
    }

    /** Stable merge sort: O(n log n) time and O(n) auxiliary space. */
    public static <T> void mergeSort(T[] values, Comparator<? super T> order) {
        check(values, order);
        mergeSortRange(values, new Object[values.length], 0, values.length, order);
    }
    private static <T> void mergeSortRange(T[] values, Object[] temporary, int from, int to,
                                            Comparator<? super T> order) {
        if (to - from <= 1) return;
        int middle = from + (to - from) / 2;
        mergeSortRange(values, temporary, from, middle, order);
        mergeSortRange(values, temporary, middle, to, order);
        int i = from, j = middle, k = from;
        while (i < middle && j < to)
            temporary[k++] = order.compare(values[i], values[j]) <= 0 ? values[i++] : values[j++];
        while (i < middle) temporary[k++] = values[i++];
        while (j < to) temporary[k++] = values[j++];
        for (int p = from; p < to; p++) {
            @SuppressWarnings("unchecked") T element = (T) temporary[p];
            values[p] = element;
        }
    }

    /** In-place quick sort using the middle element as pivot; average O(n log n). */
    public static <T> void quickSort(T[] values, Comparator<? super T> order) {
        check(values, order);
        if (values.length > 1) quickSortRange(values, 0, values.length - 1, order);
    }
    private static <T> void quickSortRange(T[] values, int low, int high,
                                            Comparator<? super T> order) {
        while (low < high) {
            int left = low, right = high;
            T pivot = values[low + (high - low) / 2];
            while (left <= right) {
                while (order.compare(values[left], pivot) < 0) left++;
                while (order.compare(values[right], pivot) > 0) right--;
                if (left <= right) swap(values, left++, right--);
            }
            // Sort the smaller partition recursively; iterate over the larger one.
            if (right - low < high - left) {
                if (low < right) quickSortRange(values, low, right, order);
                low = left;
            } else {
                if (left < high) quickSortRange(values, left, high, order);
                high = right;
            }
        }
    }

    private static <T> void check(T[] values, Comparator<? super T> order) {
        Objects.requireNonNull(values, "values"); Objects.requireNonNull(order, "order");
    }
    private static <T> void swap(T[] values, int first, int second) {
        T old = values[first]; values[first] = values[second]; values[second] = old;
    }
}
