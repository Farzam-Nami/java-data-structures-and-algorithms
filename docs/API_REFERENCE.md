# API and Implementation Reference

A function-by-function reference for **Data Structures and Algorithms in Java**.

This document describes the code in `src/main/java/datastructures/` (including package-private and private implementation helpers) and the regression harness in `src/test/java/datastructures/RegressionSuite.java`. It follows the project's study order. Names, signatures, return conventions, and exceptions correspond to the Java 11 source; this is a reference to the existing implementation, not a proposed API.

## Contents

1. [Reading the reference](#reading-the-reference)
2. [DynamicArray](#1-dynamicarrayt)
3. [SinglyLinkedList](#2-singlylinkedlistt)
4. [DoublyLinkedList](#3-doublylinkedlistt)
5. [CircularLinkedList](#4-circularlinkedlistt)
6. [PositionalList](#5-positionallistt)
7. [LinkedStack](#6-linkedstackt)
8. [LinkedQueue](#7-linkedqueuet)
9. [BinarySearchTree](#8-binarysearchtreet)
10. [BinaryHeap](#9-binaryheapt)
11. [HashTable](#10-hashtablek-v)
12. [UndirectedGraph](#11-undirectedgrapht)
13. [Searching](#12-searching)
14. [Sorting](#13-sorting)
15. [TaskManager](#14-taskmanager)
16. [MusicPlaylist](#15-musicplaylist)
17. [SyntaxChecker](#16-syntaxchecker)
18. [App](#17-app)
19. [RegressionSuite](#18-regressionsuite)
20. [Cross-cutting contracts and limitations](#cross-cutting-contracts-and-limitations)

## Reading the reference

- **Public API** tables describe methods that callers can use directly. `T`, `K`, and `V` stand for generic element/key/value types; `Comparator<? super T>` defines the ordering used by comparisons.
- **Internal methods** and nested types are documented to explain the implementation; they are not part of the public API. Compiler-generated default constructors, anonymous iterator implementation details, and standard-library methods are not exposed as independent project APIs.
- `n` = number of elements; `h` = tree height; `V` = vertices; `E` = edges; `d(v)` = degree of vertex `v`. Complexities refer to the implementation shown, not necessarily to all implementations of the abstract data type.
- Operations described as **amortized O(1)** may occasionally take O(n) when an underlying array grows.
- Unless a method says otherwise, `null` payload elements are allowed in the linked lists and dynamic array. Null handling may be more restrictive for keys, tree values, heap entries, and application input.
- Iterators in this code are **not fail-fast** and are not designed for modification of the collection during traversal.

---

## 1. `DynamicArray<T>`

**Source:** [`src/main/java/datastructures/linear/DynamicArray.java`](../src/main/java/datastructures/linear/DynamicArray.java)  
**Package:** `datastructures.linear`  
**Design:** `Object[]` backing storage, logical `size`, capacity doubling when full.

### Constructors and public methods

| Method | Purpose, inputs, and result | Time |
|---|---|---|
| `DynamicArray()` | Creates an empty array with initial capacity **4**. | O(1) |
| `DynamicArray(int initialCapacity)` | Allocates an empty array of that capacity; throws `IllegalArgumentException` for capacity < 1. | O(capacity) allocation |
| `int size()` | Returns the number of stored elements, not allocated slots. | O(1) |
| `boolean isEmpty()` | Checks whether `size == 0`. | O(1) |
| `int capacity()` | Returns the current backing-array length. | O(1) |
| `T get(int index)` | Returns the element at `index` (`0` through `size - 1`); invalid indices throw `IndexOutOfBoundsException`. | O(1) |
| `T set(int index, T value)` | Replaces the element at `index`; returns the previous value. Index is validated. | O(1) |
| `void add(T value)` | Appends at index `size`; may trigger a resize. | Amortized O(1) |
| `void insert(int index, T value)` | Inserts before `index`, allowing `index == size`; shifts later elements right. Invalid positions throw `IndexOutOfBoundsException`. | O(n) |
| `T remove(int index)` | Deletes the indexed element, shifts remaining entries left, clears the now-unused last slot, and returns the deleted value. | O(n) |
| `T removeLast()` | Removes and returns the last element; throws `NoSuchElementException` if empty. | O(1) |
| `Iterator<T> iterator()` | Returns a forward iterator over logical entries, from index `0` onward. `next()` throws `NoSuchElementException` when exhausted. | O(1) to create; O(1) per step |
| `String toString()` | Formats logical elements as `[a, b, c]`. | O(n) |

### Internal methods

| Method | Role |
|---|---|
| `ensureCapacity(int minimum)` | When needed, allocates a new array of `max(minimum, 2 * oldCapacity)` and copies the stored slots. O(n) only on growth. |
| `checkElement(int index)` | Rejects indices outside `[0, size)`; used for reading, replacing, and removing existing elements. |
| `checkPosition(int index)` | Rejects insertion positions outside `[0, size]`; the end position is legal. |
| Iterator `hasNext()` / `next()` | The anonymous iterator compares its cursor against the current `size`; `next()` delegates to `get(index++)`. |

**Example:**

```java
DynamicArray<Integer> values = new DynamicArray<>();
values.add(20);
values.insert(0, 10);
int old = values.set(1, 30); // old == 20; values is [10, 30]
```

## 2. `SinglyLinkedList<T>`

**Source:** [`src/main/java/datastructures/linear/SinglyLinkedList.java`](../src/main/java/datastructures/linear/SinglyLinkedList.java)  
**Package:** `datastructures.linear`  
**Design:** nodes with a value and `next` pointer; `head`, `tail`, and `size` maintained explicitly.

### Public methods

| Method | Purpose, inputs, and result | Time |
|---|---|---|
| `int size()` | Returns the node count. | O(1) |
| `boolean isEmpty()` | Reports whether the list is empty. | O(1) |
| `void addFirst(T value)` | Prepends a node and initializes `tail` if the list was empty. | O(1) |
| `void addLast(T value)` | Appends through `tail`, updating `head` if necessary. | O(1) |
| `T first()` | Reads the head value; throws `NoSuchElementException` if empty. | O(1) |
| `T last()` | Reads the tail value; throws `NoSuchElementException` if empty. | O(1) |
| `T removeFirst()` | Removes and returns the head; clears `tail` when the final element is removed. | O(1) |
| `T removeLast()` | Removes and returns the tail, locating its predecessor by traversal. Throws `NoSuchElementException` if empty. | O(n) |
| `boolean remove(T value)` | Deletes the **first** matching value using `Objects.equals`; returns `false` if not found. | O(n) |
| `boolean contains(T value)` | Finds whether any node contains an equal value. | O(n) |
| `void reverse()` | Reverses node links in place, then updates `head` and `tail`. | O(n) time; O(1) extra space |
| `void selectionSort(Comparator<? super T> order)` | Selects the smallest remaining value on each pass and swaps node payloads; rejects a null comparator. Not stable in general. | O(n²) time; O(1) extra space |
| `void mergeSort(Comparator<? super T> order)` | Splits and merges linked nodes in comparator order; retains stability by preferring the left value on equality. Rejects a null comparator. | O(n log n) time; O(log n) recursion depth |
| `Iterator<T> iterator()` | Returns a head-to-tail iterator; exhausted `next()` throws `NoSuchElementException`. | O(1) per step |
| `String toString()` | Formats values as `[a, b, c]`. | O(n) |

### Internal types and methods

| Member | Role |
|---|---|
| `Node<E>` / `Node(E value, Node<E> next)` | Private singly linked node and constructor, with a payload and successor pointer. |
| `mergeSortNodes(Node<T> first, Comparator<? super T> order)` | Recursive merge-sort worker: uses slow/fast pointers to divide the chain, recursively sorts both halves, and returns the new head. |
| `merge(Node<T> left, Node<T> right, Comparator<? super T> order)` | Combines two sorted chains, reusing original nodes; a temporary dummy node simplifies head construction. |
| Iterator `hasNext()` / `next()` | Advances a cursor through node links and returns the current payload. |

**Important:** `selectionSort` exchanges **values**, whereas `mergeSort` rearranges **nodes**. For either algorithm, callers should supply a comparator consistent with the intended ordering.

## 3. `DoublyLinkedList<T>`

**Source:** [`src/main/java/datastructures/linear/DoublyLinkedList.java`](../src/main/java/datastructures/linear/DoublyLinkedList.java)  
**Package:** `datastructures.linear`  
**Design:** nodes track `previous` and `next`, supporting traversal from both ends.

### Public methods

| Method | Purpose, inputs, and result | Time |
|---|---|---|
| `int size()` / `boolean isEmpty()` | Return element count / emptiness. | O(1) |
| `void addFirst(T value)` / `void addLast(T value)` | Insert at the head / tail, updating both neighboring links. | O(1) |
| `T first()` / `T last()` | Return head / tail payload; throw `NoSuchElementException` on an empty list. | O(1) |
| `T removeFirst()` / `T removeLast()` | Remove and return the head / tail payload. | O(1) |
| `boolean remove(T value)` | Remove the first matching value (`Objects.equals`); return whether anything was removed. | O(n) |
| `T get(int index)` | Read an indexed value, walking from whichever end is closer. | O(min(index, n−1−index)) |
| `T set(int index, T value)` | Replace an indexed value and return the old one. | O(n) worst case |
| `void insert(int index, T value)` | Insert before the indexed element, or append if `index == size`. | O(n) worst case |
| `T removeAt(int index)` | Unlink and return the indexed element. | O(n) worst case |
| `void insertionSort(Comparator<? super T> order)` | Stable insertion sort by shifting **payload values** toward the tail until the correct insertion point is found. A null comparator is rejected. | O(n²) time; O(1) extra space |
| `Iterable<T> reverseView()` | Returns an iterable whose iterator starts at `tail` and follows `previous` links; no separate reversed list is allocated. | O(1) to create; O(1) per step |
| `Iterator<T> iterator()` | Returns a forward head-to-tail iterator. | O(1) per step |
| `String toString()` | Formats the list in forward order. | O(n) |

### Internal types and methods

| Member | Role |
|---|---|
| `Node<E>` / `Node(E value, Node<E> previous, Node<E> next)` | Private node storing its payload and adjacent links. |
| `requireNode(Node<T> node)` | Throws `NoSuchElementException` if the requested end node is absent. |
| `unlink(Node<T> n)` | Reconnects both neighbors, updates `head`/`tail`, decrements `size`, and returns the removed payload. O(1). |
| `node(int index)` | Validates `[0, size)` and searches from the nearest end. |
| Forward/reverse iterator `hasNext()` / `next()` | Traverse the respective link direction; exhausted `next()` throws `NoSuchElementException`. |

## 4. `CircularLinkedList<T>`

**Source:** [`src/main/java/datastructures/linear/CircularLinkedList.java`](../src/main/java/datastructures/linear/CircularLinkedList.java)  
**Package:** `datastructures.linear`  
**Design:** `tail.next` is the head; a single-node list points back to itself.

### Public methods

| Method | Purpose, inputs, and result | Time |
|---|---|---|
| `int size()` / `boolean isEmpty()` | Return node count / emptiness. | O(1) |
| `T first()` | Read `tail.next.value`; throws `NoSuchElementException` if empty. | O(1) |
| `T last()` | Read `tail.value`; throws `NoSuchElementException` if empty. | O(1) |
| `void addFirst(T value)` | Insert between `tail` and the current head; initializes the cycle if empty. | O(1) |
| `void addLast(T value)` | Insert using `addFirst`, then move `tail` to the new node. | O(1) |
| `T removeFirst()` | Remove and return the head; handles the single-node case by clearing `tail`. | O(1) |
| `void rotate()` | Move the logical head to the end (`tail = tail.next`); has no effect when empty. | O(1) |
| `Iterator<T> iterator()` | Visits exactly `size` values, starting at the current head, so iteration terminates despite the cycle. | O(1) per step |
| `String toString()` | Formats a finite pass through the cycle. | O(n) |

**Internal:** `Node<E>` holds `value`/`next`; `Node(E value)` initializes the payload. The iterator's `hasNext()` counts visited nodes and `next()` advances around the cycle, throwing `NoSuchElementException` after its visit limit.

## 5. `PositionalList<T>`

**Source:** [`src/main/java/datastructures/linear/PositionalList.java`](../src/main/java/datastructures/linear/PositionalList.java)  
**Package:** `datastructures.linear`  
**Design:** a doubly linked list addressed through validated `Position<T>` handles rather than integer indices.

### Public types and methods

| Method / type | Purpose, inputs, and result | Time |
|---|---|---|
| `Position<E>.get()` | Read the value at a position; a removed position throws `IllegalArgumentException`. | O(1) |
| `int size()` / `boolean isEmpty()` | Return element count / emptiness. | O(1) |
| `Position<T> first()` / `last()` | Return the first / last position, or `null` if empty. | O(1) |
| `Position<T> after(Position<T> position)` | Validate the handle and return its successor, or `null` at the end. | O(1) |
| `Position<T> before(Position<T> position)` | Validate the handle and return its predecessor, or `null` at the beginning. | O(1) |
| `Position<T> addFirst(T value)` / `addLast(T value)` | Insert at the respective end and return the new position. | O(1) |
| `Position<T> addBefore(Position<T> position, T value)` | Insert immediately before an existing validated position; return the new handle. | O(1) |
| `Position<T> addAfter(Position<T> position, T value)` | Insert immediately after an existing validated position; return the new handle. | O(1) |
| `T set(Position<T> position, T value)` | Replace a position's value and return its old value. | O(1) |
| `T remove(Position<T> position)` | Remove the node, invalidate its handle, and return its value. | O(1) |
| `Iterator<T> iterator()` | Iterate over values from head to tail. | O(1) per step |
| `Iterable<Position<T>> positions()` | Iterate over position objects from head to tail. | O(1) per step |

### Internal types and methods

| Member | Role |
|---|---|
| Private `Node` / `Node(T value, Node previous, Node next)` | Stores value, neighbors, owning-list identity, and an `active` flag; implements `Position<T>`. |
| `link(T value, Node before, Node after)` | Creates a node between two neighbors and updates `head`, `tail`, and `size`. |
| `validate(Position<T> position)` | Rejects a wrong position type, a handle owned by another list, or a previously removed handle with `IllegalArgumentException`. |
| Value/position iterator `hasNext()` / `next()` | Advance through nodes; exhausted `next()` throws `NoSuchElementException`. |

**Position validity:** `first()` and `last()` may return `null`; do not pass `null` or a handle from another list into `after`, `before`, `set`, `remove`, `addBefore`, or `addAfter`.

## 6. `LinkedStack<T>`

**Source:** [`src/main/java/datastructures/linear/LinkedStack.java`](../src/main/java/datastructures/linear/LinkedStack.java)  
**Package:** `datastructures.linear`  
**Design:** LIFO wrapper around `SinglyLinkedList<T>` head operations.

| Method | Purpose, inputs, and result | Time |
|---|---|---|
| `void push(T value)` | Push a value onto the stack's top. | O(1) |
| `T pop()` | Remove and return the top; throws `NoSuchElementException` if empty. | O(1) |
| `T peek()` | Read the top without removing it; throws `NoSuchElementException` if empty. | O(1) |
| `int size()` | Return stack height. | O(1) |
| `boolean isEmpty()` | Return whether no items remain. | O(1) |

**Internal delegation:** `push → addFirst`, `pop → removeFirst`, and `peek → first`; no additional helper methods are declared.

## 7. `LinkedQueue<T>`

**Source:** [`src/main/java/datastructures/linear/LinkedQueue.java`](../src/main/java/datastructures/linear/LinkedQueue.java)  
**Package:** `datastructures.linear`  
**Design:** FIFO wrapper around `SinglyLinkedList<T>` head/tail operations.

| Method | Purpose, inputs, and result | Time |
|---|---|---|
| `void enqueue(T value)` | Append a value to the back of the queue. | O(1) |
| `T dequeue()` | Remove and return the oldest value; throws `NoSuchElementException` if empty. | O(1) |
| `T peek()` | Read the next value without removing it; throws `NoSuchElementException` if empty. | O(1) |
| `int size()` | Return the queued item count. | O(1) |
| `boolean isEmpty()` | Return whether the queue has no items. | O(1) |

**Internal delegation:** `enqueue → addLast`, `dequeue → removeFirst`, and `peek → first`.

## 8. `BinarySearchTree<T extends Comparable<? super T>>`

**Source:** [`src/main/java/datastructures/trees/BinarySearchTree.java`](../src/main/java/datastructures/trees/BinarySearchTree.java)  
**Package:** `datastructures.trees`  
**Design:** unbalanced BST with **unique, non-null** keys in natural ordering.

| Method | Purpose, inputs, and result | Time |
|---|---|---|
| `int size()` / `boolean isEmpty()` | Return stored key count / whether root is null. | O(1) |
| `boolean add(T value)` | Iteratively insert a non-null key; return `false` if an equal key exists; otherwise insert and return `true`. | O(h) |
| `boolean contains(T value)` | Iteratively follow comparisons to determine whether a non-null key exists. | O(h) |
| `boolean remove(T value)` | Return `false` for a missing key; otherwise delete it and decrement `size`. Handles leaf, one-child and two-child cases. | O(h) |
| `List<T> inOrder()` | Return ascending-order keys in a new list (`left → node → right`). | O(n) |
| `List<T> preOrder()` | Return keys in `node → left → right` order. | O(n) |
| `List<T> postOrder()` | Return keys in `left → right → node` order. | O(n) |

### Internal types and methods

| Member | Role |
|---|---|
| `Node<E>` / `Node(E value)` | Private tree node with `value`, `left`, and `right`. |
| `delete(Node<T> node, T value)` | Recursive subtree deletion. For a node with two children, replaces its value with the in-order successor (smallest key of right subtree), then deletes that successor. |
| `inOrder(Node<T> n, List<T> out)` | Recursive in-order traversal helper that appends to the supplied list. |
| `preOrder(Node<T> n, List<T> out)` | Recursive pre-order traversal helper. |
| `postOrder(Node<T> n, List<T> out)` | Recursive post-order traversal helper. |

**Error and performance notes:** `add`, `contains`, and `remove` reject null input with `NullPointerException`. There is **no balancing**: `h` can be O(n), so operations that use tree height may become linear and recursive functions may use O(n) call-stack space.

## 9. `BinaryHeap<T>`

**Source:** [`src/main/java/datastructures/trees/BinaryHeap.java`](../src/main/java/datastructures/trees/BinaryHeap.java)  
**Package:** `datastructures.trees`  
**Design:** comparator-ordered binary heap backed by `DynamicArray<T>`; priority is at index `0`.

| Method | Purpose, inputs, and result | Time |
|---|---|---|
| `BinaryHeap(Comparator<? super T> order)` | Create an empty heap; a null comparator throws `NullPointerException`. | O(1) |
| `static <E extends Comparable<? super E>> BinaryHeap<E> minHeap()` | Factory for natural ascending priority (smallest value at top). | O(1) |
| `static <E extends Comparable<? super E>> BinaryHeap<E> maxHeap()` | Factory for reverse priority (largest value at top). | O(1) |
| `int size()` / `boolean isEmpty()` | Return item count / emptiness. | O(1) |
| `void offer(T value)` | Append a **non-null** entry, then move it upward until heap order is restored. | O(log n) amortized |
| `T peek()` | Read the highest-priority entry; throw `NoSuchElementException` if empty. | O(1) |
| `T poll()` | Remove and return the highest-priority entry; replace the root with the last value and restore order downward. Throws `NoSuchElementException` if empty. | O(log n) |

### Internal methods

| Method | Role |
|---|---|
| `siftUp(int child)` | Compare child with parent at `(child − 1) / 2`, repeatedly swapping while child has greater priority. |
| `siftDown(int parent)` | Choose higher-priority child among indices `2p + 1` and `2p + 2`; swap downward until heap order holds. |
| `swap(int a, int b)` | Exchange two entries through the underlying dynamic array. |

**Note:** Comparator order determines priority; this class is not a fully general keyed priority queue (there is no decrease-key operation).

## 10. `HashTable<K, V>`

**Source:** [`src/main/java/datastructures/hashing/HashTable.java`](../src/main/java/datastructures/hashing/HashTable.java)  
**Package:** `datastructures.hashing`  
**Design:** separate chaining using linked entries, initial bucket capacity **16**, automatic doubling beyond load factor **0.75**.

| Method | Purpose, inputs, and result | Time |
|---|---|---|
| `HashTable()` | Create empty bucket array of length 16. | O(1) |
| `int size()` / `boolean isEmpty()` | Return mapping count / emptiness. | O(1) |
| `V put(K key, V value)` | Add a mapping or replace the value for an equal key; return the previous value, or `null` if none. Rejects null keys. Resizes when necessary. | Average O(1), worst O(n) |
| `V get(K key)` | Return the mapped value, or `null` if the key is absent. Rejects null keys. | Average O(1), worst O(n) |
| `boolean containsKey(K key)` | Distinguish a present key (including one mapped to `null`) from an absent key. | Average O(1), worst O(n) |
| `V remove(K key)` | Unlink a matching chain entry and return its previous value; return `null` if absent. Rejects null keys. | Average O(1), worst O(n) |

### Internal types and methods

| Member | Role |
|---|---|
| `Entry<A, B>` / `Entry(A key, B value, Entry<A, B> next)` | Private chain node containing key, mutable value, and link to the next collision entry. |
| `find(K key)` | Scan the key's bucket for an entry with equal key; reused by `get` and `containsKey`. |
| `index(K key, int capacity)` | Mix high hash bits with `hash ^ (hash >>> 16)` and apply `(capacity - 1)` as a mask. Assumes power-of-two bucket capacity. |
| `resize()` | Double bucket count and re-link all entries into new buckets, recalculating indices. O(n) work. |

**Important:** Values may be `null`. Consequently, `get(key) == null` does **not** establish that the key is absent; call `containsKey(key)` for that distinction. Keys must obey Java's `equals`/`hashCode` contract and should not change their hash-relevant state while stored.

## 11. `UndirectedGraph<T>`

**Source:** [`src/main/java/datastructures/graphs/UndirectedGraph.java`](../src/main/java/datastructures/graphs/UndirectedGraph.java)  
**Package:** `datastructures.graphs`  
**Design:** unweighted, undirected adjacency lists stored in insertion order; uses `DynamicArray` for neighbors and the project's queue/stack for traversal.

| Method | Purpose, inputs, and result | Time |
|---|---|---|
| `int vertexCount()` | Return the number of vertices. | O(1) |
| `boolean addVertex(T vertex)` | Add a non-null vertex if missing; return `false` for a duplicate. | Expected O(1) lookup |
| `void addEdge(T a, T b)` | Add both directions for an undirected edge; ignore duplicates. Both vertices must already exist. A self-loop is stored once. | O(d(a) + d(b)) |
| `boolean hasEdge(T a, T b)` | Check whether `b` is a neighbor of `a`; reject unknown vertices. | O(d(a)) |
| `List<T> bfs(T start)` | Return breadth-first order of vertices **reachable** from `start`, visiting each once. | O(V + E) |
| `List<T> dfs(T start)` | Return iterative depth-first order of reachable vertices using a stack. Reverse neighbor push order favors earlier-added neighbors. | O(V + E) |
| `List<T> shortestPath(T source, T destination)` | Return a vertex list for a path with the fewest edges using BFS predecessors; return an empty list if unreachable. | O(V + E) |

### Internal methods

| Method | Role |
|---|---|
| `hasNeighbor(T a, T b)` | Linearly scan `a`'s neighbor array for `b`; avoids duplicate edges. |
| `requireVertex(T value)` | Throw `IllegalArgumentException` when a vertex is unknown (including `null`, which cannot have been registered). |

**Path note:** `shortestPath(x, x)` returns the singleton path `[x]` for an existing vertex. These paths minimize **edge count**, not weighted distance; there is no weighted-edge support.

## 12. `Searching`

**Source:** [`src/main/java/datastructures/algorithms/Searching.java`](../src/main/java/datastructures/algorithms/Searching.java)  
**Package:** `datastructures.algorithms`  
**Design:** static algorithms working on ordinary Java arrays; private constructor prevents instantiation.

| Method | Purpose, inputs, and result | Time |
|---|---|---|
| `static <T> int linearSearch(T[] values, T target)` | Return the index of the **first** equal element using `Objects.equals`, or `-1` if absent. Accepts null target; array itself cannot be null. | O(n) |
| `static <T> int binarySearch(T[] values, T target, Comparator<? super T> order)` | Search an array already sorted **ascending under the supplied comparator**; return an equal index or `-1`. For duplicates, the returned index is not guaranteed to be the first. | O(log n) |

**Internal:** `Searching()` is a private, empty constructor. A null array or comparator is rejected with `NullPointerException`; an incompatible comparator or unsorted input is outside the algorithm's preconditions.

## 13. `Sorting`

**Source:** [`src/main/java/datastructures/algorithms/Sorting.java`](../src/main/java/datastructures/algorithms/Sorting.java)  
**Package:** `datastructures.algorithms`  
**Design:** in-place sorting of arrays with a caller-provided comparator; all four public algorithms return `void`.

### Public algorithms

| Method | Behavior | Time; extra space; stability |
|---|---|---|
| `static <T> void insertionSort(T[] values, Comparator<? super T> order)` | Repeatedly inserts each element into the sorted prefix by shifting larger values right. | Best O(n), worst O(n²); O(1); **stable** |
| `static <T> void selectionSort(T[] values, Comparator<? super T> order)` | Finds the minimum remaining element and swaps it into each position. | O(n²); O(1); **not generally stable** |
| `static <T> void mergeSort(T[] values, Comparator<? super T> order)` | Recursively sorts half-open ranges and merges them into a temporary array. Equal elements are taken from the left first. | O(n log n); O(n); **stable** |
| `static <T> void quickSort(T[] values, Comparator<? super T> order)` | Partitions around the **middle-index element** as pivot; recurses into the smaller partition and loops over the larger. | Average O(n log n), worst O(n²); O(log n) stack worst in this implementation; **not stable** |

### Internal methods

| Method | Role |
|---|---|
| `Sorting()` | Private constructor: prevents instances of this static utility class. |
| `mergeSortRange(T[] values, Object[] temporary, int from, int to, Comparator<? super T> order)` | Sort and merge half-open range `[from, to)`, using a shared temporary buffer. |
| `quickSortRange(T[] values, int low, int high, Comparator<? super T> order)` | Partition inclusive range `[low, high]`; tail-iterate the larger partition to bound recursive depth. |
| `check(T[] values, Comparator<? super T> order)` | Reject null array and null comparator with `NullPointerException`. |
| `swap(T[] values, int first, int second)` | Exchange array entries at two indices. |

**Precondition:** The comparator must define a consistent ordering for the values being sorted. A method's “in-place” description means the caller's array is modified; merge sort still allocates an auxiliary buffer.

**Example:**

```java
Integer[] values = {8, 3, 5, 3, 1};
Sorting.mergeSort(values, Comparator.naturalOrder()); // [1, 3, 3, 5, 8]
Sorting.quickSort(values, Comparator.reverseOrder()); // [8, 5, 3, 3, 1]
```

## 14. `TaskManager`

**Source:** [`src/main/java/datastructures/applications/TaskManager.java`](../src/main/java/datastructures/applications/TaskManager.java)  
**Package:** `datastructures.applications`  
**Design:** two singly linked lists hold pending and completed task titles.

| Method | Purpose, inputs, and result | Time |
|---|---|---|
| `void addTask(String title, boolean emergency)` | Add a non-null, non-blank title. Ordinary tasks go to the pending tail; emergency tasks go to its head. Rejects null with `NullPointerException` and blank titles with `IllegalArgumentException`. | O(1) |
| `String currentTask()` | Return the next pending title without removing it, or `null` if none. | O(1) |
| `String completeNext()` | Remove next pending title, prepend it to completed items, and return it; return `null` when nothing is pending. | O(1) |
| `int pendingCount()` / `int completedCount()` | Return the sizes of the two lists. | O(1) |
| `Iterable<String> pendingTasks()` | Return the pending list as an iterable, in processing order. | O(1) to obtain |
| `Iterable<String> completedTasks()` | Return completed titles, **most recently completed first**. | O(1) to obtain |

**Scheduling behavior:** Consecutive emergency additions are effectively **LIFO among emergencies** because each is prepended. Ordinary tasks remain FIFO when no new emergency task intervenes. The application keeps only titles and does not persist data.

## 15. `MusicPlaylist`

**Source:** [`src/main/java/datastructures/applications/MusicPlaylist.java`](../src/main/java/datastructures/applications/MusicPlaylist.java)  
**Package:** `datastructures.applications`  
**Design:** a dynamic array of unique track names, with alternative ordering views.

### `MusicPlaylist` methods

| Method | Purpose, inputs, and result | Time |
|---|---|---|
| `int size()` / `boolean isEmpty()` | Return number of tracks / emptiness. | O(1) |
| `boolean add(String name, String path)` | Validate non-blank name and path, reject a duplicate **exact name** by returning `false`, or append a new track and return `true`. | O(n) lookup; amortized O(1) append |
| `Track find(String name)` | Return the first track with exactly equal name (`String.equals`), or `null` if absent. | O(n) |
| `boolean play(String name)` | Increment a matching track's play counter and return `true`; return `false` if missing. Does not open or play an audio file. | O(n) |
| `boolean remove(String name)` | Remove a matching track by name; return whether it existed. | O(n) |
| `Track get(int index)` | Return the track at its insertion-order index; invalid indices throw `IndexOutOfBoundsException`. | O(1) |
| `List<Track> byName()` | Return a new list sorted lexicographically by `Track.name`. | O(n log n) |
| `List<Track> byPlayCount()` | Return a new list sorted by descending play count and then by ascending name. | O(n log n) |
| `List<Track> shuffled(Random random)` | Return a shuffled snapshot using the supplied non-null random generator. Fixed seed yields reproducible results. | O(n) |
| `Iterator<Track> iterator()` | Iterate over a snapshot of the tracks in insertion order. | O(n) to build snapshot; O(1) per step |

### Internal `MusicPlaylist` method

| Method | Role |
|---|---|
| `copy()` | Copy track **references** from the dynamic array into an `ArrayList`; used by sorting, shuffling, and iteration. O(n). |

### Nested `MusicPlaylist.Track`

| Method | Role |
|---|---|
| `Track(String name, String path)` | Private constructor storing track name and path; play count starts at zero. |
| `String name()` | Read the track's name. |
| `String path()` | Read the associated path string. |
| `int playCount()` | Read accumulated number of calls to `play` on this track. |
| `String toString()` | Format a track as `Name (N plays)`. |

**View semantics:** Sorted and shuffled lists are new lists but contain the **same `Track` objects**; a later play can change the counts visible through an earlier view. Track names are compared case-sensitively. This class stores metadata; it does not perform audio playback or validate that the path exists on disk.

## 16. `SyntaxChecker`

**Source:** [`src/main/java/datastructures/applications/SyntaxChecker.java`](../src/main/java/datastructures/applications/SyntaxChecker.java)  
**Package:** `datastructures.applications`  
**Design:** a simple character scanner with lexical modes and a `LinkedStack` of opening delimiters.

### Public entry point

| Method | Purpose, inputs, and result | Time |
|---|---|---|
| `static Result check(String source)` | Scan source text for matching `()`, `[]`, and `{}`; ignore delimiter characters inside ordinary quoted literals, `//` line comments and `/* ... */` block comments. Return a result containing issues with **one-based** line/column locations. Null source throws `IllegalArgumentException`. | O(n) time; O(n) worst-case stack/issues |

### Nested `SyntaxChecker.Issue`

| Method | Role |
|---|---|
| `Issue(int line, int column, String message)` | Package-private constructor of an immutable diagnostic record. |
| `int line()` | Return the one-based line number. |
| `int column()` | Return the one-based column number. |
| `String message()` | Return diagnostic text. |
| `String toString()` | Format a diagnostic as `line:column message`. |

### Nested `SyntaxChecker.Result`

| Method | Role |
|---|---|
| `Result(List<Issue> issues)` | Package-private constructor that makes an unmodifiable copy of the supplied issue list. |
| `boolean isValid()` | Return `true` only when the issues list is empty. |
| `List<Issue> issues()` | Return the read-only list of diagnostics. |

### Internal types and methods

| Member | Role |
|---|---|
| `SyntaxChecker()` | Private constructor: the utility class is not instantiable. |
| `Mode` | Private enum: `CODE`, `LINE_COMMENT`, `BLOCK_COMMENT`, `SINGLE_QUOTE`, `DOUBLE_QUOTE`. Tracks which characters should be interpreted as syntax. |
| `Opening` / `Opening(char token, int line, int column)` | Private stack record of an opening delimiter and its source position. |
| `matches(char opening, char closing)` | Return whether the characters form one matching delimiter pair (`()`, `[]`, or `{}`). |

**Diagnostic behavior:** Unexpected closings, mismatched pairs, unclosed openings, unterminated block comments, and unclosed ordinary quoted literals are reported. The scanner does **not** parse Java grammar: successful delimiter checking cannot establish that a Java program compiles. It does not comprehensively handle newer Java lexical constructs such as text blocks.

## 17. `App`

**Source:** [`src/main/java/datastructures/App.java`](../src/main/java/datastructures/App.java)  
**Package:** `datastructures`  
**Design:** demonstration entry point and optional file-based delimiter checker.

| Method | Purpose, inputs, and result |
|---|---|
| `App()` | Private constructor: the entry-point class is not intended to be instantiated. |
| `static void main(String[] args) throws Exception` | With zero arguments, prints deterministic examples of all major structures and applications. With `check <path>`, reads a file using `Files.readString` and prints delimiter-check results. Invalid argument patterns print usage and exit status **2**. The checker exits with status **1** for a reported issue and otherwise completes normally. File-read exceptions may propagate. |
| `static Object[] toArray(Iterable<?> values)` | Private helper: materializes an iterable into a new `ArrayList<Object>` and converts it to an array for demo output. |

**Commands:**

```bash
# After building with Maven
java -cp target/classes datastructures.App
java -cp target/classes datastructures.App check src/main/java/datastructures/App.java
```

For the script-based `.build` output instead of Maven's `target/classes`, use `java -cp .build datastructures.App`.

## 18. `RegressionSuite`

**Source:** [`src/test/java/datastructures/RegressionSuite.java`](../src/test/java/datastructures/RegressionSuite.java)  
**Package:** `datastructures`  
**Design:** dependency-free, deterministic regression runner. Tests are deliberately **not** included in production packages.

### Runner and assertion helpers

| Method | Purpose |
|---|---|
| `RegressionSuite()` | Private constructor: test runner cannot be instantiated. |
| `static void main(String[] args)` | Runs the 14 named test groups and prints group/total assertion counts. |
| `run(String name, Runnable check)` | Execute one group, increment the group count and report `PASS` or wrap a failure in `AssertionError`. |
| `ok(boolean condition)` | Increment assertion count; require a true condition. |
| `equal(Object actual, Object expected)` | Increment assertion count; compare two objects with `Objects.equals`. |
| `raises(Class<? extends Throwable> type, Runnable work)` | Increment assertion count; require the supplied operation to throw the specified throwable class or a subclass. |
| `<T> List<T> collect(Iterable<T> source)` | Materialize any iterable in encounter order for assertions. |

### Individual test groups

| Method | What it verifies |
|---|---|
| `dynamicArray()` | Index errors, growth and capacity, insertion/removal/replacement, randomized behavior compared with `ArrayList`. |
| `singlyLinkedList()` | Empty-list exceptions, head/tail updates, removal, reverse, selection sort, merge sort and iteration exhaustion. |
| `doublyLinkedList()` | Randomized insert/remove behavior versus `LinkedList`, forward/reverse iteration, insertion sorting and index validation. |
| `circularLinkedList()` | Empty/single/multiple-node behavior, rotation, iteration and repeated removal. |
| `stackAndQueue()` | LIFO/FIFO ordering, empty-structure exceptions and repeated insertion/removal. |
| `positionalList()` | Relative insertion, value replacement, removal, invalidated handles, foreign-list handles and position iteration. |
| `binarySearchTree()` | Random insertions and deletions versus `TreeSet`, sorted traversal, missing keys, null rejection and deletion with two children. |
| `binaryHeap()` | Min/max results compared against JDK `PriorityQueue`, randomized insert/remove operations and null rejection. |
| `hashTable()` | Repeated collisions, rehashing, replacement, lookup and removal versus `HashMap`. |
| `graph()` | Duplicate edges/vertices, BFS/DFS order, shortest paths, disconnected vertices, missing vertices and self-loops. |
| `sortingAndSearching()` | Random arrays and duplicate values versus `Arrays.sort`, searches, descending-order quick sort and argument validation. |
| `tasks()` | Emergency task priority, normal task order, completion history and title validation. |
| `playlist()` | Duplicate names, play counts, sorted views, shuffling, deletion and title validation. |
| `checker()` | Balanced and unbalanced delimiters, comments, quoted literals, unclosed modes and source locations. |

### Nested test fixture `CollisionKey`

| Method | Purpose |
|---|---|
| `CollisionKey(int value)` | Construct a key with an integer identity used by hash-table tests. |
| `int hashCode()` | Always return `Integer.MIN_VALUE` to force different keys into the same bucket. |
| `boolean equals(Object obj)` | Compare fixture keys by integer value. |

`RegressionSuite` uses fixed random seeds, so its randomized comparisons are reproducible. It is a practical regression suite, **not** a formal proof of correctness or performance benchmark.

---

## Cross-cutting contracts and limitations

| Area | Contract / limitation |
|---|---|
| Build | Java 11 or newer; Maven build and platform-specific scripts are described in [`README.md`](../README.md). |
| Generics | Data structures use generic element types; primitive values must use boxed equivalents such as `Integer`. |
| Empty removals | Linked lists, stacks, queues and heaps generally throw `NoSuchElementException` on operations that require an existing element; individual exceptions are described above. |
| Iterator mutation | Iterators are not fail-fast and do not support coordinated mutation of the same collection during traversal. |
| Array indices | Existing elements use `[0, size)`; insertion positions can use `[0, size]`. |
| Data persistence | The task manager, playlist, and data structures store in-memory data only. |
| Graphs | Only undirected/unweighted graphs; paths are measured by number of edges. |
| Trees | BST does not rebalance; the heap is comparator-priority based. |
| Syntax checking | Delimiter checks are not complete syntax validation. |
| Test scope | Regression tests exercise many regular and edge cases but cannot guarantee absence of bugs. |

