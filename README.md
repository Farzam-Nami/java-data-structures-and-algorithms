# Data Structures and Algorithms in Java

A collection of Java implementations and small applications developed from undergraduate **Data Structures** coursework, subsequently refactored and extended into one consistent project.

The project emphasizes the underlying structures and algorithms rather than graphical interfaces or frameworks. Each implementation has explicit edge-case behavior, focused documentation, and executable regression tests.

## Contents and recommended study order

| # | Topic | Java implementation | Scope |
|---|---|---|---|
| 01 | Dynamic arrays | `linear/DynamicArray` | resizing, indexing, insertion, removal |
| 02 | Singly linked lists | `linear/SinglyLinkedList` | head/tail operations, reverse, selection and merge sort |
| 03 | Doubly linked lists | `linear/DoublyLinkedList` | bidirectional traversal, indexed insertion, insertion sort |
| 04 | Circular linked lists | `linear/CircularLinkedList` | circular links and rotation |
| 05 | Positional lists | `linear/PositionalList` | positions, validation, iterators |
| 06 | Stacks (LIFO) | `linear/LinkedStack` | push, pop, peek |
| 07 | Queues (FIFO) | `linear/LinkedQueue` | enqueue, dequeue, peek |
| 08 | Binary search trees | `trees/BinarySearchTree` | insert, search, remove, three depth-first traversals |
| 09 | Binary heaps | `trees/BinaryHeap` | min/max ordering, heap insert/remove |
| 10 | Hash tables | `hashing/HashTable` | separate chaining, resizing, collisions |
| 11 | Graphs | `graphs/UndirectedGraph` | adjacency lists, BFS, DFS, shortest unweighted path |
| 12 | Searching | `algorithms/Searching` | linear and binary search |
| 13 | Sorting | `algorithms/Sorting` | selection, insertion, merge and quick sort |
| 14 | Applications | `applications/*` | Task Manager, Music Playlist, Syntax Checker |

See [complexity notes](docs/COMPLEXITY.md) for asymptotic trade-offs.

## Requirements

- Java Development Kit (**JDK 11 or newer**)
- Optional: Apache Maven 3.8+ for a conventional build and the GitHub CI pipeline
- No runtime libraries or third-party Java dependencies

## Build and run

### With Maven

From the project root:

```bash
mvn test
mvn package
java -cp target/classes datastructures.App
```

`mvn test` invokes the self-contained regression suite as part of the Maven `test` phase. On first use, Maven may need to download standard build plugins.

### Without Maven (Windows PowerShell)

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\test.ps1
java -cp .build datastructures.App
```

### Without Maven (Linux or macOS)

```bash
bash scripts/test.sh
java -cp .build datastructures.App
```

The demo prints examples of the structures, sorting/searching algorithms, applications, and syntax checker. No interactive input is required.

### Check the delimiters in a Java source file

After building, run:

```bash
java -cp .build datastructures.App check src/main/java/datastructures/App.java
```

The checker reports unclosed or mismatched brackets, braces, and parentheses, ignoring Java-style comments and quoted literals. **It is a delimiter checker, not a Java parser or compiler**; a structurally balanced file may still contain Java syntax errors.

## Tests

The regression suite covers the core operations, invalid inputs, empty/single-element structures, sorting duplicates, hash collisions and resizing, tree deletion, graph traversals, and application behavior. Several randomized tests compare our implementations against equivalent Java standard-library collections using fixed seeds.

- Test entry point: `src/test/java/datastructures/RegressionSuite.java`
- Run with: `mvn test`, `scripts/test.sh`, or `scripts/test.ps1`
- GitHub Actions runs the suite for pushes and pull requests.

## Project structure

```text
src/
  main/java/datastructures/
    App.java
    linear/           # Dynamic arrays, linked lists, positional lists, stack, queue
    trees/            # BST, binary heap
    hashing/          # Chained hash table
    graphs/           # Unweighted, undirected graph
    algorithms/       # Searching and sorting
    applications/     # Tasks, music, delimiter checking
  test/java/datastructures/
    RegressionSuite.java
scripts/              # Cross-platform test commands
docs/                 # Learning order, complexity, original exercise mapping
.github/workflows/    # Continuous integration
pom.xml
```

## Design notes and limitations

- BST is intentionally **unbalanced**: a sorted insertion sequence can produce a linear-height tree.
- The hash table demonstrates **separate chaining**, allows null values, and rejects null keys.
- Graphs are undirected/unweighted; the shortest-path method uses BFS rather than Dijkstra.
- The playlist and task manager are **in-memory demonstrations**, not persistent applications.
- Iterators are intended for traversal, not safe concurrent mutation of the same collection.
- The source checker checks delimiter consistency, not complete Java grammar.

## Coursework provenance

The initial coursework included linked lists, stack-based source checking, a positional list, a dynamic array, a task manager, a music playlist and linked-list sorting. Existing assignments were normalized into a single package structure; additional core structures and algorithms were implemented to broaden coverage. See [Coursework mapping](docs/COURSEWORK_MAPPING.md) for a more detailed distinction.

The standalone Scoreboard exercise and teacher-provided reference-code folders are not part of this repository.
