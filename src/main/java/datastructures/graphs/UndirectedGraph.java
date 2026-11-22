package datastructures.graphs;

import datastructures.linear.DynamicArray;
import datastructures.linear.LinkedQueue;
import datastructures.linear.LinkedStack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/** Undirected, unweighted graph with deterministic insertion-order adjacency lists. */
public final class UndirectedGraph<T> {
    private final Map<T, DynamicArray<T>> adjacency = new LinkedHashMap<>();
    public int vertexCount() { return adjacency.size(); }
    public boolean addVertex(T vertex) {
        Objects.requireNonNull(vertex, "vertex");
        if (adjacency.containsKey(vertex)) return false;
        adjacency.put(vertex, new DynamicArray<>()); return true;
    }
    /** Adds one undirected edge; repeated edges are ignored. */
    public void addEdge(T a, T b) {
        requireVertex(a); requireVertex(b);
        if (!hasNeighbor(a, b)) adjacency.get(a).add(b);
        if (!a.equals(b) && !hasNeighbor(b, a)) adjacency.get(b).add(a);
    }
    public boolean hasEdge(T a, T b) {
        requireVertex(a); requireVertex(b); return hasNeighbor(a, b);
    }
    private boolean hasNeighbor(T a, T b) {
        for (T neighbor : adjacency.get(a)) if (neighbor.equals(b)) return true;
        return false;
    }
    private void requireVertex(T value) {
        if (!adjacency.containsKey(value)) throw new IllegalArgumentException("unknown vertex: " + value);
    }
    /** Breadth-first traversal, visiting each reachable vertex once; O(V+E). */
    public List<T> bfs(T start) {
        requireVertex(start);
        List<T> order = new ArrayList<>(); Set<T> seen = new HashSet<>();
        LinkedQueue<T> queue = new LinkedQueue<>();
        seen.add(start); queue.enqueue(start);
        while (!queue.isEmpty()) {
            T current = queue.dequeue(); order.add(current);
            for (T next : adjacency.get(current))
                if (seen.add(next)) queue.enqueue(next);
        }
        return order;
    }
    /** Iterative depth-first traversal with duplicate suppression. */
    public List<T> dfs(T start) {
        requireVertex(start);
        List<T> order = new ArrayList<>(); Set<T> seen = new HashSet<>();
        LinkedStack<T> stack = new LinkedStack<>(); stack.push(start);
        while (!stack.isEmpty()) {
            T current = stack.pop();
            if (!seen.add(current)) continue;
            order.add(current);
            // Reverse push order to visit the earliest-added neighbor first.
            DynamicArray<T> neighbors = adjacency.get(current);
            for (int i = neighbors.size() - 1; i >= 0; i--)
                if (!seen.contains(neighbors.get(i))) stack.push(neighbors.get(i));
        }
        return order;
    }
    /** Returns a minimum-edge path, or an empty list when no route exists. */
    public List<T> shortestPath(T source, T destination) {
        requireVertex(source); requireVertex(destination);
        Map<T, T> predecessor = new HashMap<>(); Set<T> seen = new HashSet<>();
        LinkedQueue<T> queue = new LinkedQueue<>(); queue.enqueue(source); seen.add(source);
        while (!queue.isEmpty()) {
            T current = queue.dequeue();
            if (current.equals(destination)) break;
            for (T next : adjacency.get(current)) {
                if (seen.add(next)) { predecessor.put(next, current); queue.enqueue(next); }
            }
        }
        if (!seen.contains(destination)) return Collections.emptyList();
        List<T> route = new ArrayList<>();
        for (T step = destination; step != null; step = predecessor.get(step)) route.add(step);
        Collections.reverse(route); return route;
    }
}
