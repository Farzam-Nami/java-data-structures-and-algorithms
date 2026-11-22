package datastructures.trees;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Unbalanced binary search tree of unique, naturally ordered values. */
public final class BinarySearchTree<T extends Comparable<? super T>> {
    private static final class Node<E> {
        E value; Node<E> left, right;
        Node(E value) { this.value = value; }
    }
    private Node<T> root;
    private int size;
    public int size() { return size; }
    public boolean isEmpty() { return root == null; }

    /** Inserts a unique value; returns false when it is already present. */
    public boolean add(T value) {
        Objects.requireNonNull(value, "value");
        if (root == null) { root = new Node<>(value); size++; return true; }
        Node<T> parent = null, current = root;
        while (current != null) {
            parent = current;
            int difference = value.compareTo(current.value);
            if (difference == 0) return false;
            current = difference < 0 ? current.left : current.right;
        }
        if (value.compareTo(parent.value) < 0) parent.left = new Node<>(value);
        else parent.right = new Node<>(value);
        size++; return true;
    }

    public boolean contains(T value) {
        Objects.requireNonNull(value, "value");
        Node<T> current = root;
        while (current != null) {
            int comparison = value.compareTo(current.value);
            if (comparison == 0) return true;
            current = comparison < 0 ? current.left : current.right;
        }
        return false;
    }

    public boolean remove(T value) {
        Objects.requireNonNull(value, "value");
        if (!contains(value)) return false;
        root = delete(root, value);
        size--; return true;
    }

    private Node<T> delete(Node<T> node, T value) {
        if (node == null) return null;
        int comparison = value.compareTo(node.value);
        if (comparison < 0) node.left = delete(node.left, value);
        else if (comparison > 0) node.right = delete(node.right, value);
        else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            // Replace a two-child node with its inorder successor, then unlink the successor.
            Node<T> successor = node.right;
            while (successor.left != null) successor = successor.left;
            node.value = successor.value;
            node.right = delete(node.right, successor.value);
        }
        return node;
    }

    public List<T> inOrder() {
        List<T> result = new ArrayList<>();
        inOrder(root, result); return result;
    }
    public List<T> preOrder() {
        List<T> result = new ArrayList<>();
        preOrder(root, result); return result;
    }
    public List<T> postOrder() {
        List<T> result = new ArrayList<>();
        postOrder(root, result); return result;
    }
    private void inOrder(Node<T> n, List<T> out) {
        if (n != null) { inOrder(n.left, out); out.add(n.value); inOrder(n.right, out); }
    }
    private void preOrder(Node<T> n, List<T> out) {
        if (n != null) { out.add(n.value); preOrder(n.left, out); preOrder(n.right, out); }
    }
    private void postOrder(Node<T> n, List<T> out) {
        if (n != null) { postOrder(n.left, out); postOrder(n.right, out); out.add(n.value); }
    }
}
