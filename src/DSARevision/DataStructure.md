
1. Arrays
What it is 
. Fixed - size contiguous memory
. Index based access

Why use Arrays
Fastest Access
Low Overhead
Perfect when size is known

Why not use..?
Coz Fixed Size 
Insert Delete Operation in O(n)
```java
int [] arr = new int[5];
arr[0] = 10;
```
Common Problems
.Prefix sum
.Sliding Window
.Two Pointers
.Kadane's Algorithm

2 ArrayList
What it is
.Dynamic array
Why use ArrayList
. size can grow
. index based access
. Random access O(1)
Why not use..?
. Insert Delete O(n)
. Not Thread Safe
```java
List<Integer> list = new ArrayList<>();
list.add(10);
list.get(0);
```
Common Problems
. Dynamic Storage
. Graph adjacency list
. Storage variable inputs

3 LinkedList
What it is
. Nodes with value and pointer to next
. Can be singly or doubly linked
Why use LinkedList
. Dynamic size
. Fast insert delete O(1) if node ref is known
. Good for queues and stacks
Why not use..?
. No random access O(n) to access ith element
```java
class Node {
    int value;
    Node next;
    Node(int value) { this.value = value; }
}
LinkedList<Integer> list = new LinkedList<>();
list.add(10);
list.remove();
```
Common Problems
. Implement stacks and queues
. Reverse linked list
. Detect cycles
. Merge k sorted lists

4 Stack
What it is
.LIFO data structure
. Push and Pop operations

Why use Stack
. Backtracking problems
. Expression evaluation
. Function call management
. Reversal problems
```java
Stack<Integer> stack = new Stack<>();
stack.push(10);
int val = stack.pop();
Deque<Integer> deque = new ArrayDeque<>();
stack.push(10);
stack.pop();
```
Common Problems
. Valid Parentheses
. Next Greater Element
. Stock Span Problem
. Histogram
. DFS(iterative)

5 Queue
What it is
.FIFO data structure
. Enqueue and Dequeue operations

Why use Queue
. Task scheduling
. BFS traversal
. Resource management
. Level wise processing
order Matters
```java
Queue<Integer> queue = new LinkedList<>();
queue.offer(10);
int val = queue.poll();
Queue<Integer> deque = new ArrayDeque<>();
deque.offer(10);
deque.poll();
```
Common Problems
. BFS traversal
. Sliding Window Maximum
. Task Scheduling
. Design Circular Queue

6 Deque( Underated but very Important)
What it is
.Double Ended Queue
. Insert Delete from both ends
Why use Deque
. Flexibility of stack and queue
. Sliding window problems
. Palindrome checking
. Monotonic Queue
```java
Deque<Integer> deque = new ArrayDeque<>();
deque.offerFirst(10);
deque.offerLast(20);
int first = deque.pollFirst();
int last = deque.pollLast();
```
Common Problems
. Sliding Window Maximum/Minimum
. Palindrome Checker
. Monotonic Queue
. Task Scheduler with priorities
. Max in subarrays

7 HashMap
What it is
.Key-Value pairs
. Fast lookups O(1) average
Why use HashMap
. Associative arrays
. Caching
. Frequency counting
. Grouping data
. Track Visited
Why not use..?
. No order guarantee
. Collision handling overhead
. Worst case O(n) lookups
```java
Map<String, Integer> map = new HashMap<>();
map.put("key", 10);
int val = map.get("key");
map.containsKey("key");
map.getOrDefault("key", 0);

```
Common Problems
. Two Sum
. Group Anagrams
. Longest Substring Without Repeating Characters
. Top K Frequent Elements
. Subarray Sum Equals K 
8 HashSet
What it is
.Set of unique elements
. Backed by HashMap
Why use HashSet
. Fast membership checks O(1)
. Remove duplicates
. Set operations (union, intersection)
```java
Set<Integer> set = new HashSet<>();
set.add(10);
boolean exists = set.contains(10);
set.remove(10);
```
Common Problems
. Contains Duplicate
. Longest Consecutive Sequence
. Intersection of Two Arrays
. Happy Number
. Detect Cycle in Graph

9 TreeSet
What it is
.Sorted set/Map of unique elements
. Backed by Red-Black Tree
Why use TreeSet
. Sorted order storage
. Range queries
. Floor/Ceiling operations
```java
O(logn) operations
TreeSet<Integer> treeSet = new TreeSet<>();
treeSet.add(10);
boolean exists = treeSet.contains(10);
treeSet.remove(10);
Integer floor = treeSet.floor(9);
Integer ceiling = treeSet.ceiling(11);
```
Common Problems
. calendar problems
. Interval scheduling
. Kth largest/smallest element
. Sliding window median
. Range queries
. Order statistics

10 PriorityQueue (Heap)
What it is
. Min-Heap or Max-Heap
. Fast access to min/max O(1)
. Insert/Delete O(log n)
Why use PriorityQueue
. Task scheduling
. Dijkstra's algorithm
. Merging sorted lists
. Top K elements
. Partial Sorting
```java
PriorityQueue<Integer> minHeap = new PriorityQueue<>();
minHeap.offer(10);
int min = minHeap.poll();  // removes and returns min
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
maxHeap.offer(20);
int max = maxHeap.poll();  // removes and returns max
int max = maxHeap.peek(); // returns min/max without removing
Alternativve of Collection.reverseOrder()
Comparator<Integer> maxHeapComparator = (a, b) -> b - a;
PriorityQueue<Integer> maxHeap2 = new PriorityQueue<>(maxHeapComparator);
```
Common Problems
. Kth Largest Element
. Merge K Sorted Lists
. Top K Frequent Elements
. Task Scheduling
. Median in Data Stream

11 Graph (Adjacency List)
What it is
.Nodes and edges
. Adjacency list representation
Why Adjacency List Over Adjacency Matrix
. Space efficient for sparse graphs
. Faster iteration over neighbors
```java
class Graph {
    private Map<Integer, List<Integer>> adjList = new HashMap<>();
    public void addEdge(int u, int v) {
        adjList.putIfAbsent(u, new ArrayList<>());
        adjList.get(u).add(v);
    }
    public List<Integer> getNeighbors(int u) {
        return adjList.getOrDefault(u, new ArrayList<>());
    }
}
List<List<Integer>> adjList = new ArrayList<>();
adjList.get(u).add(v);

```
Common Problems
. DFS/BFS Traversal
. Detect Cycle in Graph
. Topological Sort
. Dijkstra's Algorithm
. Connected Components
. Minimum Spanning Tree (Kruskal's, Prim's)

12 Graph (Adjacency Matrix)
What it is
.Nodes and edges
. Adjacency matrix representation
Why Adjacency Matrix Over Adjacency List
. Fast edge existence check O(1)
. Simpler implementation for dense graphs
```java
int V = 5; // number of vertices
int[][] adjMatrix = new int[V][V];
// Add edge from u to v
adjMatrix[u][v] = 1;
// Check if edge exists from u to v
boolean exists = adjMatrix[u][v] == 1;
```
Common Problems
. Floyd-Warshall Algorithm
. Graph Density Analysis
. Bipartite Graph Check
. Warshall's Algorithm for Transitive Closure
. Graph Isomorphism Check

13 Trie
What it is
.Prefix tree for strings
.Each node represents a character
Why use Trie
.Fast prefix searches
.Autocomplete systems
.Spell checking
.Dictionary implementations
. Word Search
```java
class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEndOfWord = false;
}
class Trie {
    private TrieNode root = new TrieNode();
    public void insert(String word) {
        TrieNode node = root;
        for (char ch : word.toCharArray()) {
            node.children.putIfAbsent(ch, new TrieNode());
            node = node.children.get(ch);
        }
        node.isEndOfWord = true;
    }
    public boolean search(String word) {
        TrieNode node = root;
        for (char ch : word.toCharArray()) {
            if (!node.children.containsKey(ch)) return false;
            node = node.children.get(ch);
        }
        return node.isEndOfWord;
    }
}
```
Common Problems
. Implement Trie
. Word Search II
. Auto-complete System
. Longest Common Prefix
. Replace Words
. Palindrome Pairs

14 HashTable
What it is
.Key-Value pairs with hashing
. Fast lookups O(1) average
Why use HashTable
. Associative arrays
. Caching
. Frequency counting
. Grouping data
. Track Visited
Why not use..?
. No order guarantee
. Collision handling overhead
. Worst case O(n) lookups
```java
Hashtable<String, Integer> table = new Hashtable<>();
table.put("key", 10);
int val = table.get("key");
table.containsKey("key");
table.getOrDefault("key", 0);
```
Common Problems
. Two Sum
. Group Anagrams
. Longest Substring Without Repeating Characters  
. Top K Frequent Elements
. Subarray Sum Equals K

15 BitSet
What it is
.Efficient storage of bits
. Compact representation of boolean arrays
Why use BitSet
.Memory efficient for large boolean arrays
.Fast bitwise operations
. Set operations (AND, OR, XOR)
. Track presence/absence
```java
BitSet bitSet = new BitSet();
bitSet.set(0); // set bit at index 0
boolean isSet = bitSet.get(0); // check if bit at index 0 is set
bitSet.clear(0); // clear bit at index 0
```
Common Problems
. Sieve of Eratosthenes
. Subset Generation 
. Bit Manipulation Problems
. Track Visited in Graphs
. Counting Distinct Elements

16 CircularBuffer
What it is 
.Fixed-size buffer that wraps around
.Index based access with head/tail pointers
Why use CircularBuffer
.Efficient use of fixed memory
.Fast insert/delete O(1)
.Good for streaming data
. Implement queues with fixed size
```java
class CircularBuffer {
    private int[] buffer;
    private int head = 0;
    private int tail = 0;
    private int size = 0;
    public CircularBuffer(int capacity) {
        buffer = new int[capacity];
    }
    public void add(int value) {
        if (size == buffer.length) throw new RuntimeException("Buffer full");
        buffer[tail] = value;
        tail = (tail + 1) % buffer.length;
        size++;
    }
    public int remove() {
        if (size == 0) throw new RuntimeException("Buffer empty");
        int value = buffer[head];
        head = (head + 1) % buffer.length;
        size--;
        return value;
    }
}
```
Common Problems
. Implement Circular Queue
. Sliding Window Problems
. Data Stream Processing
. Rate Limiting
. Fixed-size Caches

17 DisjointSet(Union-Find)
What it is
.Data structure to track disjoint sets
.Supports union and find operations
Why use DisjointSet
.Efficiently manage connected components
.Cycle detection in graphs
.Kruskal's algorithm for MST
.Network connectivity problems
```java
class DisjointSet {
    private int[] parent;
    private int[] rank;
    public DisjointSet(int size) {
        parent = new int[size];
        rank = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }
    public int find(int u) {
        if (parent[u] != u) {
            parent[u] = find(parent[u]); // path compression
        }
        return parent[u];
    }
    public void union(int u, int v) {
        int rootU = find(u);
        int rootV = find(v);
        if (rootU != rootV) {
            if (rank[rootU] > rank[rootV]) {
                parent[rootV] = rootU;
            } else if (rank[rootU] < rank[rootV]) {
                parent[rootU] = rootV;
            } else {
                parent[rootV] = rootU;
                rank[rootU]++;
            }
        }
    }
}
```
Common Problems
. Connected Components in Graph
. Cycle Detection in Undirected Graph
. Kruskal's Algorithm for MST
. Friend Circles
. Number of Islands
. Redundant Connection  

18 Segment Tree
What it is
.Tree data structure for range queries
.Supports efficient updates and queries
Why use Segment Tree
.Fast range queries (sum, min, max)
.Efficient updates to elements
.Good for dynamic arrays
.Range-based problems
```java
class SegmentTree {
    private int[] tree;
    private int n;
    public SegmentTree(int[] arr) {
        n = arr.length;
        tree = new int[4 * n];
        build(arr, 0, 0, n - 1);
    }
    private void build(int[] arr, int node, int start, int end) {
        if (start == end) {
            tree[node] = arr[start];
        } else {
            int mid = (start + end) / 2;
            build(arr, 2 * node + 1, start, mid);
            build(arr, 2 * node + 2, mid + 1, end);
            tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
        }
    }
    public int query(int L, int R) {
        return query(0, 0, n - 1, L, R);
    }
    private int query(int node, int start, int end, int L, int R) {
        if (R < start || end < L) return 0; // out of range
        if (L <= start && end <= R) return tree[node]; // fully in range
        int mid = (start + end) / 2;
        int leftSum = query(2 * node + 1, start, mid, L, R);
        int rightSum = query(2 * node + 2, mid + 1, end, L, R);
        return leftSum + rightSum;
    }
}
```
Common Problems
.Range Sum Query
.Range Minimum/Maximum Query
. Update Element in Array
. Count of Elements in Range
. Inversion Count
. Merge Intervals

19 Fenwick Tree (Binary Indexed Tree)
What it is
.Tree data structure for cumulative frequency tables
.Supports efficient updates and prefix sum queries
Why use Fenwick Tree
.Fast prefix sum queries O(log n)
.Efficient updates to elements O(log n)
.Lower memory overhead than Segment Tree
.Good for dynamic arrays
```java
class FenwickTree {
    private int[] tree;
    private int n;
    public FenwickTree(int size) {
        n = size;
        tree = new int[n + 1];
    }
    public void update(int index, int delta) {
        index++; // 1-based indexing
        while (index <= n) {
            tree[index] += delta;
            index += index & -index;
        }
    }
    public int query(int index) {
        index++; // 1-based indexing
        int sum = 0;
        while (index > 0) {
            sum += tree[index];
            index -= index & -index;
        }
        return sum;
    }
}
```
Common Problems
. Range Sum Query
. Inversion Count
. Count of Smaller Numbers After Self
. Frequency Counting
. Dynamic Median Finding
. Kth Order Statistic   

20 LRU Cache
What it is
.Cache that evicts least recently used items
. Combines HashMap and Doubly Linked List
Why use LRU Cache
. Efficient O(1) get and put operations
. Manages limited memory effectively
. Common in web caching, databases
```java
class LRUCache {
    private class Node {
        int key, value;
        Node prev, next;
        Node(int k, int v) { key = k; value = v; }
    }
    private int capacity;
    private Map<Integer, Node> map;
    private Node head, tail;
    public LRUCache(int capacity) {
        this.capacity = capacity;
        map = new HashMap<>();
        head = new Node(0, 0);
        tail = new Node(0, 0);
        head.next = tail;
        tail.prev = head;
    }
    public int get(int key) {
        if (!map.containsKey(key)) return -1;
        Node node = map.get(key);
        remove(node);
        insertAtFront(node);
        return node.value;
    }
    public void put(int key, int value) {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            remove(node);
            node.value = value;
            insertAtFront(node);
        } else {
            if (map.size() == capacity) {
                Node lru = tail.prev;
                remove(lru);
                map.remove(lru.key);
            }
            Node newNode = new Node(key, value);
            insertAtFront(newNode);
            map.put(key, newNode);
        }
    }
    private void remove(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
    private void insertAtFront(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }
}
```
Common Problems
. Design LRU Cache
. Web Caching
. Database Caching
. Image Caching
. File System Caching
. Session Management

21 DoublyLinkedList
What it is
.Nodes with value, next and prev pointers
.Bidirectional traversal
Why use DoublyLinkedList
.Fast insert/delete O(1) if node ref is known
.Bidirectional traversal
.Good for LRU Cache, Deque implementations
```java
class Node {
    int value;
    Node next;
    Node prev;
    Node(int value) { this.value = value; }
}
DoublyLinkedList<Integer> list = new DoublyLinkedList<>();
list.addFirst(10);
list.addLast(20);
list.remove(node);
```
Common Problems
. Implement LRU Cache
. Design Deque
. Reverse Doubly Linked List
. Flatten Multilevel Doubly Linked List
. Remove Duplicates from Sorted Doubly Linked List
. Swap Nodes in Pairs

22 CircularLinkedList
What it is
.Nodes where last node points to first
.Forms a circular structure
Why use CircularLinkedList
.Efficient for round-robin scheduling
.Good for buffering data
.Fast insert/delete O(1) if node ref is known
```java
class Node {
    int value;
    Node next;
    Node(int value) { this.value = value; }
}
class CircularLinkedList {
    private Node tail;
    public void add(int value) {
        Node newNode = new Node(value);
        if (tail == null) {
            tail = newNode;
            tail.next = tail; // point to itself
        } else {
            newNode.next = tail.next;
            tail.next = newNode;
            tail = newNode;
        }
    } 
    public void remove(Node node) {
        if (tail == null) return;
        Node current = tail;
        do {
            if (current.next == node) {
                current.next = node.next;
                if (node == tail) tail = current; // update tail if needed
                return;
            }
            current = current.next;
        } while (current != tail);
    } 
}
```
Common Problems
. Implement Circular Queue
. Josephus Problem
. Round-Robin Scheduling
. Buffer Management
. Circular List Traversal
. Music Playlist Management

