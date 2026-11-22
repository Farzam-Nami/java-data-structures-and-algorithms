# Complexity reference

Assume `n` stored elements, `V` graph vertices, `E` edges and an ordinarily distributed hash function.

| Structure / operation | Typical time | Notes |
|---|---|---|
| DynamicArray append | amortized O(1) | A resize copies O(n) items |
| DynamicArray indexed access | O(1) | Bounds-checked |
| DynamicArray arbitrary insert/remove | O(n) | Elements must shift |
| SinglyLinkedList add/remove first | O(1) | Head pointer |
| SinglyLinkedList append | O(1) | Tail pointer |
| SinglyLinkedList remove last | O(n) | Must locate preceding node |
| DoublyLinkedList add/remove first/last | O(1) | Maintains head and tail |
| DoublyLinkedList index lookup | O(n) | Traverses from nearer end |
| CircularLinkedList rotate | O(1) | Advances tail |
| PositionalList add/remove at valid position | O(1) | Ownership checks are constant time |
| Stack push/pop / Queue enqueue/dequeue | O(1) | Linked-backed |
| BinarySearchTree insert/find/remove | O(h), worst O(n) | `h` is tree height; no balancing |
| BinaryHeap offer/poll | O(log n) | Peek O(1) |
| HashTable put/get/remove | expected O(1), worst O(n) | Chaining and resizing |
| Graph BFS/DFS | O(V + E) | Visited set prevents repeated visits |
| Graph shortest unweighted path | O(V + E) | BFS predecessor tracing |
| Linear search | O(n) | Unsorted input accepted |
| Binary search | O(log n) | Requires sorted input |
| Selection sort | O(n²) | In-place; not generally stable |
| Insertion sort | worst O(n²), best O(n) | Stable; in-place |
| Merge sort (array) | O(n log n) | Stable; O(n) extra space |
| Quick sort (array) | average O(n log n), worst O(n²) | In-place partitioning; not stable |
| Selection sort (linked list) | O(n²) | Node payload swaps |
| Insertion sort (doubly linked) | O(n²) | Stable payload shifts |
| Merge sort (singly linked) | O(n log n) | Stable node merges, O(log n) recursion |

Insertion order may influence BST height. Quick sort's worst-case O(n²) can still occur even when choosing a middle-element pivot by *position*.
