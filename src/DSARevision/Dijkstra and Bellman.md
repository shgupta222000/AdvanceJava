1️⃣ Story (Why Dijkstra Exists)
Imagine:
•	You are in a city
•	Roads have different costs
•	You want the cheapest path from source to all places

Brute force tries all paths → too slow
Dijkstra says:
“Always expand the currently cheapest path first.”
⸻
2️⃣ When to THINK of Dijkstra (INTERVIEW TRIGGERS)
Immediately think Dijkstra when you hear:
✅ Graph
✅ Weighted edges
✅ Shortest path
❌ No negative weights

If negative weights → NOT Dijkstra (Bellman Ford) why ..? 
Coz Dijkstra finalized the value too early and negative weights can reduce it later While Bellman Ford keeps updating distances multiple times (V-1 times) edge relaxation
So Dijkstra is GREEDY and Bellman Ford is DP and can handle negative weights and even negative cycles(in next iteration we can detect negative cycle if we can relax further)
trick to remember:
Dijkstra = Greedy = FINAL distance once picked
Bellman Ford = DP = keep updating distances multiple times
⸻
3️⃣ Core Mental Model (VERY IMPORTANT)
•	Maintain shortest distance so far
•	Always pick node with minimum distance
•	Once picked → its distance is FINAL

This is why we use Min Heap
⸻
4️⃣ Dijkstra Algorithm Steps
```java
class Pair {
    int node;
    int dist;

    Pair(int node, int dist) {
        this.node = node;
        this.dist = dist;
    }
}

public int[] dijkstra(int V, List<List<Pair>> adj, int src) {

    int[] dist = new int[V];
    Arrays.fill(dist, Integer.MAX_VALUE);
    dist[src] = 0;

    PriorityQueue<Pair> pq =
        new PriorityQueue<>((a, b) -> a.dist - b.dist);

    pq.offer(new Pair(src, 0));

    while (!pq.isEmpty()) {
        Pair cur = pq.poll();
        int u = cur.node;
        int d = cur.dist;

        if (d > dist[u]) continue; // VERY IMPORTANT

        for (Pair nei : adj.get(u)) {
            int v = nei.node;
            int wt = nei.dist;

            if (dist[u] + wt < dist[v]) {
                dist[v] = dist[u] + wt;
                pq.offer(new Pair(v, dist[v]));
            }
        }
    }
    return dist;
}
```
⸻
5️⃣ Time and Space Complexity
Time: O((V + E) log V)
•	Priority Queue operations: O(log V)
•	Each vertex processed once: O(V)
•	Each edge processed once: O(E)
Space: O(V)
•	Dist array: O(V)
•	Priority Queue: O(V) in worst case
⸻
6️⃣ LC Problems to Practice
1.	743. Network Delay Time
2.	787. Cheapest Flights Within K Stops
3.	1631. Path With Minimum Effort
4.	1514. Path with Maximum Probability
5.	778. Swim in Rising Water
6.	882. Reachable Nodes In Subdivided Graph
7.	1630. Arithmetic Subarrays
8.	2250. Count Prefixes of a Given String

6️⃣ Interview Traps (THEY LOVE THIS)
❌ Forgetting if (d > dist[u]) continue
→ causes TLE
❌ Using BFS for weighted graph
→ wrong
❌ Using Dijkstra with negative edges
→ instant reject
👉 If graph is unweighted → BFS
👉 If weighted & positive → Dijkstra
👉 If weighted & negative → Bellman Ford
👉 If all-pairs shortest → Floyd Warshall

⸻ Algo 2 Bellman Ford (next doc)
1️⃣ Story — Why Bellman-Ford Exists
Imagine:
•	You are traveling between cities
•	Some roads give cashback (negative weight)
•	You still want the shortest path
👉 Dijkstra fails here because it assumes:
“Once I pick the cheapest node, it can never improve later”
Negative edges break this assumption.
So Bellman-Ford says:
“Relax all edges again and again until no improvement is possible.”
⸻
2️⃣ When to THINK of Bellman-Ford (Interview Triggers)
Immediately think Bellman-Ford when you hear:
✅ Weighted graph
✅ Negative edges allowed
✅ Shortest path from single source, Almost K edges stops etc
✅ Need to detect negative cycle

❌ If graph is large & no negative edges → use Dijkstra
⸻
3️⃣ Core Mental Model (VERY IMPORTANT)
Forget graph traversal for a moment.
Bellman-Ford works on edges, not nodes.
Key idea:
•	Longest possible shortest path = V - 1 edges
•	So repeat:
•	Relax all edges
•	Do this V - 1 times
If after that:
•	Any edge still relaxes → negative cycle exists
⸻
4️⃣ Relaxation (Heart of the Algorithm)
Relaxing an edge u → v (wt) means:
```text
If distance[u] + wt < distance[v]
→ update distance[v]
```
```java
class Edge {
    int u, v, wt;

    Edge(int u, int v, int wt) {
        this.u = u;
        this.v = v;
        this.wt = wt;
    }
}
public int[] bellmanFord(int V, List<Edge> edges, int src) {

    int[] dist = new int[V];
    Arrays.fill(dist, Integer.MAX_VALUE);
    dist[src] = 0;

    // Relax edges V-1 times
    for (int i = 1; i <= V - 1; i++) {
        for (Edge e : edges) {
            if (dist[e.u] != Integer.MAX_VALUE &&
                    dist[e.u] + e.wt < dist[e.v]) {

                dist[e.v] = dist[e.u] + e.wt;
            }
        }
    }

    // Check negative cycle
    for (Edge e : edges) {
        if (dist[e.u] != Integer.MAX_VALUE &&
                dist[e.u] + e.wt < dist[e.v]) {

            System.out.println("Negative Cycle Detected");
            return new int[]{};
        }
    }
    return dist;
}
```
⸻
5️⃣ Time and Space Complexity
Time: O(V * E)
•	Relaxing all edges V - 1 times: O(V * E)
Space: O(V)
•	Dist array: O(V)
⸻
6️⃣ LC Problems to Practice
1.	787. Cheapest Flights Within K Stops
2.	1334. Find the City With the Smallest Number of Neighbors at a Threshold Distance
3.	1514. Path with Maximum Probability
4.	Detect Negative Cycle in a graph
⸻
7️⃣ Interview Traps (THEY LOVE THIS)
❌ Not checking dist[u] != Integer.MAX_VALUE before relaxing
→ causes overflow bugs
❌ Forgetting to do V - 1 iterations
→ wrong answers
❌ Missing negative cycle detection step
→ wrong answers

9️⃣ Sharp Interview Thinking Tricks 🧠
🔥 Trick 1 — “Edges count matters”
If problem says:
“At most K edges / stops”
👉 Bellman-Ford style DP
⸻
🔥 Trick 2 — Detect Arbitrage
Currency exchange → negative cycle
👉 Bellman-Ford instantly
⸻
🔥 Trick 3 — Why BFS sometimes replaces BF?
If weights are only 0 and 1
👉 Use 0–1 BFS (Deque)
⸻
10️⃣ Dijkstra vs Bellman-Ford (ONE-LINE MEMORY)
Dijkstra = Greedy = FINAL distance once picked
Bellman Ford = DP = keep updating distances multiple times
