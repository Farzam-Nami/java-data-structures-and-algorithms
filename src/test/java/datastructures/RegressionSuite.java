package datastructures;

import datastructures.algorithms.Searching;
import datastructures.algorithms.Sorting;
import datastructures.applications.MusicPlaylist;
import datastructures.applications.SyntaxChecker;
import datastructures.applications.TaskManager;
import datastructures.graphs.UndirectedGraph;
import datastructures.hashing.HashTable;
import datastructures.linear.CircularLinkedList;
import datastructures.linear.DoublyLinkedList;
import datastructures.linear.DynamicArray;
import datastructures.linear.LinkedQueue;
import datastructures.linear.LinkedStack;
import datastructures.linear.PositionalList;
import datastructures.linear.SinglyLinkedList;
import datastructures.trees.BinaryHeap;
import datastructures.trees.BinarySearchTree;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/** Dependency-free regression tests and randomized comparisons against JDK collections. */
public final class RegressionSuite {
    private static int assertions;
    private static int groups;
    private RegressionSuite() { }

    public static void main(String[] args) {
        run("DynamicArray", RegressionSuite::dynamicArray);
        run("SinglyLinkedList", RegressionSuite::singlyLinkedList);
        run("DoublyLinkedList", RegressionSuite::doublyLinkedList);
        run("CircularLinkedList", RegressionSuite::circularLinkedList);
        run("Stack and Queue", RegressionSuite::stackAndQueue);
        run("PositionalList", RegressionSuite::positionalList);
        run("BinarySearchTree", RegressionSuite::binarySearchTree);
        run("BinaryHeap", RegressionSuite::binaryHeap);
        run("HashTable", RegressionSuite::hashTable);
        run("UndirectedGraph", RegressionSuite::graph);
        run("Sorting and Searching", RegressionSuite::sortingAndSearching);
        run("TaskManager", RegressionSuite::tasks);
        run("MusicPlaylist", RegressionSuite::playlist);
        run("SyntaxChecker", RegressionSuite::checker);
        System.out.println("PASS: " + groups + " groups; " + assertions + " assertions.");
    }

    private static void run(String name, Runnable check) {
        try { check.run(); groups++; System.out.println("PASS " + name); }
        catch (Throwable cause) { throw new AssertionError("FAIL " + name, cause); }
    }
    private static void ok(boolean condition) {
        assertions++;
        if (!condition) throw new AssertionError("expected true");
    }
    private static void equal(Object actual, Object expected) {
        assertions++;
        if (!java.util.Objects.equals(actual, expected))
            throw new AssertionError("expected: " + expected + ", got: " + actual);
    }
    private static void raises(Class<? extends Throwable> type, Runnable work) {
        assertions++;
        try { work.run(); }
        catch (Throwable e) {
            if (type.isInstance(e)) return;
            throw new AssertionError("expected " + type.getSimpleName() + " but got " + e, e);
        }
        throw new AssertionError("expected " + type.getSimpleName() + " but no exception was thrown");
    }
    private static <T> List<T> collect(Iterable<T> source) {
        List<T> result = new ArrayList<>(); source.forEach(result::add); return result;
    }

    private static void dynamicArray() {
        DynamicArray<Integer> actual = new DynamicArray<>(1);
        List<Integer> expected = new ArrayList<>();
        raises(IndexOutOfBoundsException.class, () -> actual.get(0));
        raises(NoSuchElementException.class, actual::removeLast);
        raises(IllegalArgumentException.class, () -> new DynamicArray<>(0));
        Random random = new Random(41);
        for (int i = 0; i < 750; i++) {
            int operation = expected.isEmpty() ? 0 : random.nextInt(4);
            if (operation == 0) {
                int value = random.nextInt(200), index = random.nextInt(expected.size() + 1);
                actual.insert(index, value); expected.add(index, value);
            } else if (operation == 1) {
                int index = random.nextInt(expected.size());
                equal(actual.remove(index), expected.remove(index));
            } else if (operation == 2) {
                int index = random.nextInt(expected.size());
                equal(actual.set(index, i), expected.set(index, i));
            } else {
                int index = random.nextInt(expected.size());
                equal(actual.get(index), expected.get(index));
            }
            equal(collect(actual), expected);
            ok(actual.capacity() >= actual.size());
        }
        raises(IndexOutOfBoundsException.class, () -> actual.insert(actual.size() + 1, 3));
    }

    private static void singlyLinkedList() {
        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
        raises(NoSuchElementException.class, list::removeFirst);
        raises(NoSuchElementException.class, list::removeLast);
        list.addLast(5); list.addFirst(3); list.addLast(5); list.addFirst(1);
        equal(collect(list), Arrays.asList(1, 3, 5, 5));
        ok(list.remove(5)); equal(collect(list), Arrays.asList(1, 3, 5));
        equal(list.removeLast(), 5); equal(list.removeFirst(), 1);
        equal(list.first(), 3); equal(list.last(), 3);
        list.reverse(); equal(collect(list), Arrays.asList(3));
        equal(list.removeLast(), 3); ok(list.isEmpty());
        list.addLast(7); list.addLast(1); list.addLast(3); list.addLast(1); list.addLast(5);
        list.selectionSort(Comparator.naturalOrder());
        equal(collect(list), Arrays.asList(1, 1, 3, 5, 7));
        list.reverse(); list.mergeSort(Comparator.naturalOrder());
        equal(collect(list), Arrays.asList(1, 1, 3, 5, 7));
        list.addLast(9); equal(list.last(), 9);
        ok(!list.remove(100));
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) iterator.next();
        raises(NoSuchElementException.class, iterator::next);
    }

    private static void doublyLinkedList() {
        DoublyLinkedList<Integer> actual = new DoublyLinkedList<>();
        LinkedList<Integer> expected = new LinkedList<>();
        raises(NoSuchElementException.class, actual::removeFirst);
        Random random = new Random(8);
        for (int i = 0; i < 500; i++) {
            int operation = expected.isEmpty() ? 0 : random.nextInt(6);
            switch (operation) {
                case 0: actual.addFirst(i); expected.addFirst(i); break;
                case 1: actual.addLast(i); expected.addLast(i); break;
                case 2: equal(actual.removeFirst(), expected.removeFirst()); break;
                case 3: equal(actual.removeLast(), expected.removeLast()); break;
                case 4:
                    int index = random.nextInt(expected.size() + 1);
                    actual.insert(index, i); expected.add(index, i); break;
                default:
                    int removed = random.nextInt(expected.size());
                    equal(actual.removeAt(removed), expected.remove(removed)); break;
            }
            equal(collect(actual), expected);
            List<Integer> backwards = collect(actual.reverseView());
            java.util.Collections.reverse(backwards);
            equal(backwards, expected);
        }
        actual.addLast(2); actual.addLast(-1); actual.addLast(2);
        expected.add(2); expected.add(-1); expected.add(2);
        actual.insertionSort(Comparator.naturalOrder());
        expected.sort(Comparator.naturalOrder());
        equal(collect(actual), expected);
        if (!actual.isEmpty()) {
            equal(actual.set(0, 123), expected.set(0, 123));
            equal(actual.get(0), 123);
        }
        raises(IndexOutOfBoundsException.class, () -> actual.get(actual.size()));
    }

    private static void circularLinkedList() {
        CircularLinkedList<String> list = new CircularLinkedList<>();
        raises(NoSuchElementException.class, list::first);
        list.rotate(); list.addLast("A"); list.rotate(); equal(list.first(), "A");
        list.addLast("B"); list.addFirst("Z");
        equal(collect(list), Arrays.asList("Z", "A", "B"));
        list.rotate(); equal(collect(list), Arrays.asList("A", "B", "Z"));
        equal(list.removeFirst(), "A"); equal(list.last(), "Z");
        equal(list.removeFirst(), "B"); equal(list.removeFirst(), "Z");
        ok(list.isEmpty()); list.addFirst("new"); equal(list.first(), "new");
    }

    private static void stackAndQueue() {
        LinkedStack<Integer> stack = new LinkedStack<>();
        LinkedQueue<Integer> queue = new LinkedQueue<>();
        raises(NoSuchElementException.class, stack::pop);
        raises(NoSuchElementException.class, queue::dequeue);
        for (int i = 0; i < 200; i++) { stack.push(i); queue.enqueue(i); }
        equal(stack.peek(), 199); equal(queue.peek(), 0);
        for (int i = 0; i < 200; i++) {
            equal(stack.pop(), 199 - i); equal(queue.dequeue(), i);
        }
        ok(stack.isEmpty()); ok(queue.isEmpty());
    }

    private static void positionalList() {
        PositionalList<String> list = new PositionalList<>();
        equal(list.first(), null); equal(list.last(), null);
        PositionalList.Position<String> b = list.addFirst("B");
        list.addBefore(b, "A"); list.addAfter(b, "C");
        equal(collect(list), Arrays.asList("A", "B", "C"));
        equal(list.before(b).get(), "A"); equal(list.after(b).get(), "C");
        equal(list.set(b, "New B"), "B"); equal(b.get(), "New B");
        equal(list.remove(b), "New B");
        raises(IllegalArgumentException.class, b::get);
        raises(IllegalArgumentException.class, () -> list.remove(b));
        equal(collect(list), Arrays.asList("A", "C"));
        PositionalList<String> other = new PositionalList<>();
        PositionalList.Position<String> foreign = other.addFirst("foreign");
        raises(IllegalArgumentException.class, () -> list.after(foreign));
        int positions = 0;
        for (PositionalList.Position<String> ignored : list.positions()) positions++;
        equal(positions, 2);
    }

    private static void binarySearchTree() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        TreeSet<Integer> reference = new TreeSet<>();
        Random random = new Random(55);
        for (int i = 0; i < 250; i++) {
            int value = random.nextInt(100);
            equal(tree.add(value), reference.add(value));
            equal(tree.size(), reference.size());
        }
        equal(tree.inOrder(), new ArrayList<>(reference));
        for (int value = 0; value < 100; value++) {
            equal(tree.contains(value), reference.contains(value));
            equal(tree.remove(value), reference.remove(value));
            equal(tree.inOrder(), new ArrayList<>(reference));
        }
        ok(tree.isEmpty());
        raises(NullPointerException.class, () -> tree.add(null));
        tree.add(5); tree.add(3); tree.add(7); tree.add(6); tree.add(8);
        equal(tree.preOrder(), Arrays.asList(5, 3, 7, 6, 8));
        equal(tree.postOrder(), Arrays.asList(3, 6, 8, 7, 5));
        ok(tree.remove(5)); equal(tree.inOrder(), Arrays.asList(3, 6, 7, 8));
    }

    private static void binaryHeap() {
        BinaryHeap<Integer> min = BinaryHeap.minHeap(), max = BinaryHeap.maxHeap();
        PriorityQueue<Integer> expectedMin = new PriorityQueue<>();
        PriorityQueue<Integer> expectedMax = new PriorityQueue<>(Comparator.reverseOrder());
        raises(NoSuchElementException.class, min::peek);
        Random random = new Random(97);
        for (int i = 0; i < 1000; i++) {
            if (expectedMin.isEmpty() || random.nextBoolean()) {
                int value = random.nextInt(120) - 60;
                min.offer(value); max.offer(value);
                expectedMin.offer(value); expectedMax.offer(value);
            } else {
                equal(min.poll(), expectedMin.poll());
                equal(max.poll(), expectedMax.poll());
            }
            equal(min.size(), expectedMin.size());
            if (!min.isEmpty()) { equal(min.peek(), expectedMin.peek()); equal(max.peek(), expectedMax.peek()); }
        }
        while (!expectedMin.isEmpty()) {
            equal(min.poll(), expectedMin.poll()); equal(max.poll(), expectedMax.poll());
        }
        raises(NullPointerException.class, () -> min.offer(null));
    }

    private static void hashTable() {
        HashTable<CollisionKey, Integer> table = new HashTable<>();
        Map<CollisionKey, Integer> reference = new HashMap<>();
        for (int i = 0; i < 180; i++) {
            CollisionKey key = new CollisionKey(i);
            equal(table.put(key, i * 3), reference.put(key, i * 3));
        }
        equal(table.size(), reference.size());
        for (int i = 0; i < 180; i++) {
            CollisionKey key = new CollisionKey(i);
            equal(table.get(key), reference.get(key));
            ok(table.containsKey(key));
            if (i % 2 == 0) equal(table.put(key, -i), reference.put(key, -i));
        }
        for (int i = 0; i < 180; i++) {
            CollisionKey key = new CollisionKey(i);
            equal(table.remove(key), reference.remove(key));
        }
        ok(table.isEmpty());
        equal(table.remove(new CollisionKey(10000)), null);
        raises(NullPointerException.class, () -> table.put(null, 3));
    }
    private static final class CollisionKey {
        final int value;
        CollisionKey(int value) { this.value = value; }
        @Override public int hashCode() { return Integer.MIN_VALUE; }
        @Override public boolean equals(Object obj) {
            return obj instanceof CollisionKey && value == ((CollisionKey) obj).value;
        }
    }

    private static void graph() {
        UndirectedGraph<String> graph = new UndirectedGraph<>();
        for (String value : Arrays.asList("A", "B", "C", "D", "E", "F")) ok(graph.addVertex(value));
        ok(!graph.addVertex("A"));
        graph.addEdge("A", "B"); graph.addEdge("A", "C");
        graph.addEdge("B", "D"); graph.addEdge("C", "D");
        graph.addEdge("D", "E"); graph.addEdge("A", "B");
        equal(graph.bfs("A"), Arrays.asList("A", "B", "C", "D", "E"));
        equal(graph.dfs("A"), Arrays.asList("A", "B", "D", "C", "E"));
        equal(graph.shortestPath("A", "E"), Arrays.asList("A", "B", "D", "E"));
        equal(graph.shortestPath("A", "F"), java.util.Collections.emptyList());
        ok(graph.hasEdge("B", "A")); ok(!graph.hasEdge("E", "A"));
        raises(IllegalArgumentException.class, () -> graph.bfs("absent"));
        graph.addEdge("F", "F"); ok(graph.hasEdge("F", "F"));
    }

    private static void sortingAndSearching() {
        Random random = new Random(112);
        for (int length = 0; length <= 45; length++) {
            for (int repeat = 0; repeat < 8; repeat++) {
                Integer[] source = new Integer[length];
                for (int i = 0; i < source.length; i++) source[i] = random.nextInt(20) - 10;
                Integer[] expected = source.clone(); Arrays.sort(expected);
                Integer[] selection = source.clone(), insertion = source.clone();
                Integer[] merge = source.clone(), quick = source.clone();
                Sorting.selectionSort(selection, Comparator.naturalOrder());
                Sorting.insertionSort(insertion, Comparator.naturalOrder());
                Sorting.mergeSort(merge, Comparator.naturalOrder());
                Sorting.quickSort(quick, Comparator.naturalOrder());
                ok(Arrays.equals(selection, expected));
                ok(Arrays.equals(insertion, expected));
                ok(Arrays.equals(merge, expected));
                ok(Arrays.equals(quick, expected));
                for (int i = 0; i < expected.length; i++) {
                    int location = Searching.binarySearch(expected, expected[i], Comparator.naturalOrder());
                    ok(location >= 0 && expected[location].equals(expected[i]));
                }
                equal(Searching.binarySearch(expected, 12345, Comparator.naturalOrder()), -1);
                equal(Searching.linearSearch(expected, 12345), -1);
            }
        }
        Integer[] descending = {1, 7, 4, 2, 4};
        Sorting.quickSort(descending, Comparator.reverseOrder());
        ok(Arrays.equals(descending, new Integer[]{7, 4, 4, 2, 1}));
        equal(Searching.linearSearch(descending, 4), 1);
        Integer[] alreadySorted = new Integer[2500];
        for (int i = 0; i < alreadySorted.length; i++) alreadySorted[i] = i;
        Sorting.quickSort(alreadySorted, Comparator.naturalOrder());
        equal(alreadySorted[2499], 2499);
        raises(NullPointerException.class, () -> Sorting.quickSort(null, Comparator.naturalOrder()));
    }

    private static void tasks() {
        TaskManager tasks = new TaskManager();
        equal(tasks.completeNext(), null);
        tasks.addTask("normal1", false); tasks.addTask("normal2", false);
        tasks.addTask("emergency", true);
        equal(tasks.currentTask(), "emergency");
        equal(tasks.completeNext(), "emergency");
        equal(tasks.completeNext(), "normal1");
        equal(tasks.pendingCount(), 1); equal(tasks.completedCount(), 2);
        equal(collect(tasks.completedTasks()), Arrays.asList("normal1", "emergency"));
        raises(IllegalArgumentException.class, () -> tasks.addTask(" ", false));
    }

    private static void playlist() {
        MusicPlaylist playlist = new MusicPlaylist();
        ok(playlist.add("B", "b.mp3")); ok(playlist.add("A", "a.mp3"));
        ok(!playlist.add("A", "other.mp3"));
        ok(playlist.play("B")); ok(playlist.play("B")); ok(!playlist.play("missing"));
        equal(playlist.byName().get(0).name(), "A");
        equal(playlist.byPlayCount().get(0).name(), "B");
        equal(playlist.get(0).playCount(), 2);
        equal(playlist.shuffled(new Random(17)).size(), 2);
        equal(playlist.size(), 2);
        ok(playlist.remove("B")); ok(!playlist.remove("B"));
        equal(playlist.size(), 1); equal(playlist.get(0).name(), "A");
        raises(IllegalArgumentException.class, () -> playlist.add("", "x.mp3"));
    }

    private static void checker() {
        ok(SyntaxChecker.check("").isValid());
        ok(SyntaxChecker.check("class T { int[] arr = {1,2}; }").isValid());
        ok(SyntaxChecker.check("// unmatched ((( } \n class T { }").isValid());
        ok(SyntaxChecker.check("/* { ignored } */ class T { }").isValid());
        ok(SyntaxChecker.check("class T { String a = \"( [ } \\\" hi\"; }").isValid());
        ok(SyntaxChecker.check("char a = '\\''; { }").isValid());
        ok(!SyntaxChecker.check("{ ( ] }").isValid());
        ok(!SyntaxChecker.check("}").isValid());
        ok(!SyntaxChecker.check("{").isValid());
        ok(!SyntaxChecker.check("/* never closed").isValid());
        ok(!SyntaxChecker.check("String s = \"abc").isValid());
        equal(SyntaxChecker.check("\n}").issues().get(0).line(), 2);
        equal(SyntaxChecker.check("\n  }").issues().get(0).column(), 3);
    }
}
