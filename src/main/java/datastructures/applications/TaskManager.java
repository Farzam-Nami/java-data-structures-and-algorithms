package datastructures.applications;

import datastructures.linear.SinglyLinkedList;
import java.util.Objects;

/** Task queue adapted from the original normal/emergency coursework exercise. */
public final class TaskManager {
    private final SinglyLinkedList<String> pending = new SinglyLinkedList<>();
    private final SinglyLinkedList<String> completed = new SinglyLinkedList<>();

    /** Normal tasks join the tail; emergency tasks are processed before existing tasks. */
    public void addTask(String title, boolean emergency) {
        Objects.requireNonNull(title, "title");
        if (title.trim().isEmpty()) throw new IllegalArgumentException("title cannot be blank");
        if (emergency) pending.addFirst(title); else pending.addLast(title);
    }
    public String currentTask() { return pending.isEmpty() ? null : pending.first(); }
    /** Marks the next task complete; returns null if there are no pending tasks. */
    public String completeNext() {
        if (pending.isEmpty()) return null;
        String title = pending.removeFirst();
        completed.addFirst(title); return title;
    }
    public int pendingCount() { return pending.size(); }
    public int completedCount() { return completed.size(); }
    public Iterable<String> pendingTasks() { return pending; }
    public Iterable<String> completedTasks() { return completed; }
}
