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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/** Small, deterministic demonstrations for every major coursework topic. */
public final class App {
    private App() { }
    public static void main(String[] args) throws Exception {
        if (args.length == 2 && args[0].equals("check")) {
            SyntaxChecker.Result report = SyntaxChecker.check(Files.readString(Path.of(args[1])));
            if (report.isValid()) System.out.println("Syntax delimiters: valid");
            else {
                report.issues().forEach(System.out::println);
                System.exit(1);
            }
            return;
        }
        if (args.length != 0) {
            System.err.println("Usage: App [check <path-to-java-source>]");
            System.exit(2); return;
        }

        DynamicArray<Integer> vector = new DynamicArray<>();
        vector.add(3); vector.insert(0, 1); vector.add(2);
        System.out.println("Dynamic array: " + vector);

        SinglyLinkedList<Integer> singly = new SinglyLinkedList<>();
        singly.addLast(5); singly.addLast(1); singly.addLast(3);
        singly.selectionSort(Comparator.naturalOrder());
        System.out.println("Singly linked list (selection sort): " + singly);
        singly.reverse(); singly.mergeSort(Comparator.naturalOrder());
        System.out.println("Singly linked list (merge sort): " + singly);

        DoublyLinkedList<Integer> doubly = new DoublyLinkedList<>();
        doubly.addLast(4); doubly.addLast(2); doubly.addLast(3);
        doubly.insertionSort(Comparator.naturalOrder());
        System.out.println("Doubly linked list (insertion sort): " + doubly);

        CircularLinkedList<String> circular = new CircularLinkedList<>();
        circular.addLast("A"); circular.addLast("B"); circular.rotate();
        System.out.println("Circular linked list: " + circular);

        LinkedStack<String> stack = new LinkedStack<>();
        stack.push("bottom"); stack.push("top");
        System.out.println("Stack pop: " + stack.pop());

        LinkedQueue<String> queue = new LinkedQueue<>();
        queue.enqueue("first"); queue.enqueue("second");
        System.out.println("Queue dequeue: " + queue.dequeue());

        PositionalList<Integer> positional = new PositionalList<>();
        PositionalList.Position<Integer> p = positional.addFirst(2);
        positional.addBefore(p, 1); positional.addAfter(p, 3);
        System.out.println("Positional list: " + Arrays.toString(toArray(positional)));

        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        for (int n : new int[]{5, 3, 7, 1, 4}) bst.add(n);
        System.out.println("BST inorder: " + bst.inOrder());

        BinaryHeap<Integer> heap = BinaryHeap.minHeap();
        heap.offer(9); heap.offer(2); heap.offer(5);
        System.out.println("Min heap poll: " + heap.poll());

        HashTable<String, Integer> table = new HashTable<>();
        table.put("Alice", 10); table.put("Bob", 20);
        System.out.println("Hash table lookup: " + table.get("Bob"));

        UndirectedGraph<String> graph = new UndirectedGraph<>();
        graph.addVertex("A"); graph.addVertex("B"); graph.addVertex("C");
        graph.addEdge("A", "B"); graph.addEdge("B", "C");
        System.out.println("Graph BFS: " + graph.bfs("A"));
        System.out.println("Graph DFS: " + graph.dfs("A"));

        Integer[] values = {9, 4, 1, 6, 2};
        Sorting.quickSort(values, Comparator.naturalOrder());
        System.out.println("Quick sort: " + Arrays.toString(values));
        Integer[] other = {9, 4, 1, 6, 2};
        Sorting.mergeSort(other, Comparator.naturalOrder());
        System.out.println("Merge sort: " + Arrays.toString(other));
        System.out.println("Binary search for 4: " + Searching.binarySearch(other, 4, Comparator.naturalOrder()));

        TaskManager tasks = new TaskManager();
        tasks.addTask("Read chapter", false); tasks.addTask("Submit assignment", true);
        System.out.println("Task manager next: " + tasks.completeNext());

        MusicPlaylist playlist = new MusicPlaylist();
        playlist.add("Study", "study.mp3"); playlist.add("Focus", "focus.mp3");
        playlist.play("Focus"); playlist.play("Focus");
        System.out.println("Most played: " + playlist.byPlayCount());
        System.out.println("Shuffled playlist: " + playlist.shuffled(new Random(42)));
        System.out.println("Syntax checker: " + SyntaxChecker.check("class X { int[] a = {1, 2}; }").isValid());
    }
    private static Object[] toArray(Iterable<?> values) {
        java.util.ArrayList<Object> result = new java.util.ArrayList<>();
        values.forEach(result::add);
        return result.toArray();
    }
}
