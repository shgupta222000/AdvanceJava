TopoLogical Sort (DAG Ordering)

u always comes before v for every u-->v

1️⃣ Story — Why Topological Sort Exists

Imagine:

You must finish Task B before Task A

Multiple tasks depend on others

Question:

“In what order can I complete all tasks?”

That’s Topological Sort.

2️⃣ When to THINK of Topological Sort (INTERVIEW TRIGGERS)

Instantly think Topo Sort when you hear:

✅ Dependencies
✅ Prerequisites
✅ Ordering
✅ DAG
✅ Course schedule
✅ Build systems

If cycle exists → impossible.

3️⃣ Core Mental Model (CRITICAL)

You can only do a task when:

All its prerequisites are done

Graph view:

Directed graph

Edge u → v means:

u must come before v

4️⃣ Two Ways (MUST KNOW BOTH)
1️⃣ BFS (Kahn’s Algorithm) — MOST USED
2️⃣ DFS (Postorder) — good for theory

5️⃣ Kahn’s Algorithm (INTERVIEW STANDARD)
Step-by-step:

Calculate indegree

Push nodes with indegree = 0

Remove them and update neighbors

```java
import java.util.LinkedList;

public int[] topoSort(int n, List<List<Integer>> adj) {
    int[] indegree = new int[n];

    for (int u = 0; u < n; u++) {
        for (int v : adj.get(u)) {
            indegree[v]++;
        }
    }

    Queue<Integer> q = new LinkedList<>();
    for(int i=0;i<n;i++){
        if(indegree[i]==0)q.offer(i);
    }
    int[] order = new int[n];
    int idx =0;
    while(!q.isEmpty()){
        int u = q.poll();
        order[idx++]=u;
        for(int v: adj.get(u)){
            indegree[v]--;
            if(indegree[v]==0){
                q.offer(v);
            }
        }
    }
    //cycle check
    if(idx!=n) return new int[]{};
    return order;
}
```

6️⃣ DFS-Based Topo (WHY IT WORKS)
Idea:

Visit node

Visit all neighbors

Push node after children
```java
void dfs(int u, boolean[] vis, Stack<Integer> st, List<List<Integer>> adj) {
    vis[u] = true;
    for (int v : adj.get(u)) {
        if (!vis[v]) dfs(v, vis, st, adj);
    }
    st.push(u);
}
```

7️⃣ Famous LeetCode Problems (MUST MASTER)
🔹 LC 207 — Course Schedule

Check:

Can we finish all courses?

If topo count < n → cycle

🔹 LC 210 — Course Schedule II

Return order using topo.

🔹 LC 1203 — Sort Items by Groups Respecting Dependencies

Advanced:

Topo on two levels

Group graph + item graph

8️⃣ Interview Traps (VERY COMMON)

❌ Forgetting cycle detection
❌ Using DFS topo for cycle-heavy problems
❌ Thinking topo exists for cyclic graph
❌ Using DSU here (wrong)

9️⃣ Sharp Interview Thinking Tricks 🧠
🔥 Trick 1 — Cycle detection in Directed Graph

Topo count < n → cycle

🔥 Trick 2 — Longest path in DAG

Topo order + DP

🔥 Trick 3 — Multiple valid answers exist

Any topo order is acceptable.

10️⃣ One-Line Recognition Rule

If order matters & dependencies exist → Topological Sort
