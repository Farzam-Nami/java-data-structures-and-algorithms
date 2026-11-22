# Suggested learning path

The order below progresses from contiguous memory and pointers to abstract data types, nonlinear structures, and general algorithms.

1. **Arrays and dynamic storage:** `DynamicArray` — capacity, growth, indexed access, amortized costs.
2. **Singly linked lists:** `SinglyLinkedList` — nodes, head/tail invariants, reverse, deletion.
3. **Doubly linked lists:** `DoublyLinkedList` — previous/next pointers and reverse traversal.
4. **Circular linked lists:** `CircularLinkedList` — circular tail/head invariants and rotation.
5. **Positional lists:** `PositionalList` — opaque positions, ownership, invalidation and iterators.
6. **Stack ADT:** `LinkedStack` — LIFO operations; apply it to delimiter checking.
7. **Queue ADT:** `LinkedQueue` — FIFO operations; use it in BFS.
8. **Binary search tree:** `BinarySearchTree` — ordered insertion, search, three traversals, deletion cases.
9. **Binary heap:** `BinaryHeap` — heap ordering, percolation and priority queues.
10. **Hashing:** `HashTable` — hash functions, collisions, separate chaining and rehashing.
11. **Graphs:** `UndirectedGraph` — adjacency lists, BFS, DFS, unweighted shortest paths.
12. **Searching:** `Searching` — linear scan and binary search on sorted input.
13. **Sorting:** `Sorting` — selection/insertion, then divide-and-conquer merge/quick sort.
14. **Applications:** `TaskManager`, `MusicPlaylist`, `SyntaxChecker` — combine and exercise structures.

## Recommended practice

For each structure: implement basic operations, state invariants, test empty and one-element cases, then examine time and space complexity. Use `RegressionSuite` to confirm behavior before changing an implementation.

The learning order is conceptual: individual classes do not need to depend on every earlier topic.
