UNION FIND (DISJOINT SET UNION – DSU)

This is THE algorithm for:

Connectivity

Cycles

Components

Imagine you are repeatedly asked questions like:

Are A and B connected?

Connect A and B

How many separate groups / components exist?

If I add an edge, will it form a cycle?

This happens in:

Graph connectivity

Dynamic connections

Grid islands

Network problems

Kruskal’s MST

Naive ways ❌

Use DFS/BFS every time → too slow

Maintain adjacency lists → still costly for repeated queries

👉 DSU exists to solve this efficiently when connections keep changing.

Interviewers love it because it tests thinking + implementation discipline.

1️⃣ Story — Why Union Find Exists

Imagine:

You have N people

Some become friends

You are repeatedly asked:

“Are A and B in the same group?”

Brute force:

Traverse graph every time ❌

Union Find says:

“Maintain groups dynamically.”

2️⃣ When to THINK of Union Find (INTERVIEW TRIGGERS)

Immediately think DSU when you hear:

✅ Undirected graph
✅ Connected components
✅ Cycle detection
✅ Merge groups
✅ Dynamic connectivity
✅ “Initially disconnected, then queries”

If graph is directed → usually NOT DSU.

3️⃣ Core Mental Model (NO JARGON)

Each element:

Points to a parent

Root represents the group

Two operations:

Find(x) → find group leader

Union(x, y) → merge groups

4️⃣ Base DSU Template (INTERVIEW SAFE)
Step 1: Data Structures
```java
class DSU{
    int[]parent;
    int[]rank;
    DSU(int n){
        parent = new int[n];
        rank = new int[n];
        for(int i=0;i<n;i++){
            parent[i]=i;// each node is its own parent
            rank[i]=0;
        }
    }
}
```
Step 2: Find with Path Compression
```java
int find(int x){
    if(parent[x]!=x){
        parent[x]= find(parent[x]);
    }
    return parent[x];
}
```
Step 3: Union by Rank
```java
void union(int x, int y){
    int px = find(x);
    int py = find(y);
    if(px == py)return;
    if(rank[px]<rank[py]){
        parent[px]=py;
    }else if(rank[px]>rank[py]){
        parent[py]=px;
    }else{
        parent[py]=px;
        rank[px]++;
    }
}
```
5️⃣ Why Rank + Path Compression Matters

Without them:

Worst case → O(n)

With both:

Almost O(1) (α(n), inverse Ackermann)

👉 Interviewers expect both

6️⃣ Famous LeetCode Problems (MUST KNOW)

🔹 LC 547 — Number of Provinces

Story:

Count connected components

Approach:

Union all connected cities

Count unique parents

🔹 LC 684 — Redundant Connection

Story:

Find edge creating a cycle

Logic:

If find(u) == find(v) → cycle found

🔹 LC 721 — Accounts Merge

Story:

Merge emails belonging to same person

Key:

Email → node

Same email → union

7️⃣ Interview Traps (VERY COMMON)

❌ Forgetting path compression
❌ Using DSU for directed graph
❌ Not initializing parent properly
❌ Confusing rank with height exactly

8️⃣ Sharp Interview Thinking Tricks 🧠
🔥 Trick 1 — Cycle in Undirected Graph

DSU beats DFS/BFS for simplicity.

🔥 Trick 2 — Kruskal’s MST

Sort edges → DSU decides safely.

🔥 Trick 3 — Grid problems

Convert (i,j) → i * cols + j
Then use DSU.

9️⃣ One-Line Recognition Rule

If components merge dynamically → Union Find

What is DSU (Union–Find) in plain words

DSU = Disjoint Set Union

You have elements

They are divided into groups

Each group has one leader (representative)

You only care about:

Which group does this element belong to?

Can I merge two groups?

Two operations:
```text
find(x)   → tells the leader of x’s group
union(a,b) → merges groups of a and b
```

Mapping Grid → DSU (MOST CONFUSING PART, explained)

Grid:

rows = R, cols = C
cell = (i, j)

DSU works on 1D indices, so we convert:

id = i * cols + j
What this depicts

Think row by row flattening:

Example (3×4 grid):

(0,0)=0   (0,1)=1   (0,2)=2   (0,3)=3
(1,0)=4   (1,1)=5   (1,2)=6   (1,3)=7
(2,0)=8   (2,1)=9   (2,2)=10  (2,3)=11

👉 Unique mapping
👉 Adjacent cells differ by ±1 or ±cols

🔁 Neighbors in DSU terms

From (i,j) → id = i*cols + j

Direction	Neighbor ID
Right	id + 1
Left	id - 1
Down	id + cols
Up	id - cols

(After bounds check)

Typical Grid + DSU Flow
Example: Number of Islands

Initialize DSU for all cells

Only activate land cells

For each land cell:

Union with adjacent land cells

Count unique parents of land cells

Example: Dynamic Islands (LeetCode 305)

Why DSU is mandatory here:

Lands added one by one

After each add → number of islands

DFS every time = ❌
DSU:

Each new land starts as island

Union neighbors

If merged → islands--

Mental Model (interview explanation)

“DSU maintains connected components efficiently.
In grid problems, each cell is treated as a node, flattened into a 1D index.
Neighboring land cells are unioned, and find helps identify whether two cells belong to the same island.”

That sentence alone is 🔥 in interviews.

When DSU is a MUST

✅ Cycle detection (undirected graph)
✅ Kruskal MST
✅ Dynamic connectivity
✅ Grid island variants
✅ Offline queries (connect/remove)

Kruskal Algorithm (step-by-step)
Step 1: Sort edges by weight

Cheapest first.

Step 2: Initialize DSU

Each node is its own component.

Step 3: Iterate edges

For each edge (u, v, w):

If find(u) != find(v):

Add edge to MST

union(u, v)

Else:

Skip (would form cycle)

Step 4: Stop

When you’ve added N-1 edges
```java
class Edge {
    int u, v, w;
}

int kruskal(int n, List<Edge> edges) {
    Collections.sort(edges, (a, b) -> a.w - b.w);

    DSU dsu = new DSU(n);
    int mstWeight = 0;
    int count = 0;

    for (Edge e : edges) {
        if (dsu.find(e.u) != dsu.find(e.v)) {
            dsu.union(e.u, e.v);
            mstWeight += e.w;
            count++;
            if (count == n - 1) break;
        }
    }
    return mstWeight;
}
```