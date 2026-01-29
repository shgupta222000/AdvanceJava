BFS VARIANTS (Matrix + Shortest Path Patterns)

1️⃣ Story — Why BFS Exists

You want:

minimum steps

shortest path

closest distance

All edges have equal cost → BFS beats Dijkstra.

2️⃣ Base BFS Template (MEMORIZE)

```java
import java.util.LinkedList;
import java.util.Queue;

Queue<int[]> q = new LinkedList<>();
bolean[][] visited = new boolean[n][m];
q.offer(new int[]{sr,sc});
visited[sr][sc]=true;
int steps =0;
while(!q.isEmpty()){
    int size = q.size();
    for(int i=0;i<size;i++){
        int[] curr = q.poll();
        for( each direction){
            if(valid and not visited){
                visited = true;
                q.offer(next);
            }
        }
    }
    steps++;
}
```

3️⃣ BFS ON MATRIX — MOST COMMON
Direction array (DON’T REWRITE EACH TIME)
```java
int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
```
🟢 BFS VARIANT 1 — MULTI-SOURCE BFS ⭐⭐⭐
4️⃣ Story

You have multiple starting points.
Distance spreads outward equally.

Instead of BFS many times:

push all sources at once.

🔹 LC 542 — 01 Matrix

0 cells → sources

find distance to nearest 0

Key Insight

All zeros start at distance 0

BFS spreads layer by layer

```java
Queue<int[]> q = new LinkedList<>();
int[][] dist = new int[n][m];

for (int i = 0; i < n; i++) {
    for (int j = 0; j < m; j++) {
        if (mat[i][j] == 0) {
            q.offer(new int[]{i, j});
        } else {
            dist[i][j] = -1;
        }
    }
}

while (!q.isEmpty()) {
    int[] cur = q.poll();
    for (int[] d : dirs) {
        int r = cur[0] + d[0];
        int c = cur[1] + d[1];
        if (valid && dist[r][c] == -1) {
            dist[r][c] = dist[cur[0]][cur[1]] + 1;
            q.offer(new int[]{r, c});
        }
    }
}
```
Recognition Rule 🧠

“distance from nearest X” → multi-source BFS

🟢 BFS VARIANT 2 — SHORTEST PATH IN BINARY MATRIX
🔹 LC 1091

Grid:

0 → free

1 → blocked

Moves:

8 directions

Key Interview Point

BFS because each move costs same

🟢 BFS VARIANT 3 — 0–1 BFS ⭐⭐⭐
5️⃣ Story — Why 0–1 BFS Exists

Edges have weights:

0

1

Dijkstra works but slower.

0–1 BFS:

Use Deque instead of heap.

🔹 LC 1368 — Minimum Cost to Make Valid Path
Core Idea

Cost 0 → push front

Cost 1 → push back

Java 
```java
Deque<int[]> dq = new ArrayDeque<>();
int[][] dist = new int[n][m];
Arrays.fill(dist, INF);

dq.offerFirst(new int[]{0, 0});
dist[0][0] = 0;

while (!dq.isEmpty()) {
    int[] cur = dq.pollFirst();
    for (edge) {
        int newDist = dist[cur] + cost;
        if (newDist < dist[next]) {
            dist[next] = newDist;
            if (cost == 0) dq.offerFirst(next);
            else dq.offerLast(next);
        }
    }
}
```

🟢 BFS VARIANT 4 — LEVEL ORDER BFS
🔹 LC 127 — Word Ladder

Key:

Each BFS level = one transformation

Interview Tip

Use:
```java
for (int size = q.size(); size > 0; size--)
```

🟢 BFS VARIANT 5 — BFS WITH STATE

🔹 LC 1293 — Shortest Path with Obstacles Elimination

State = (row, col, remainingK)

Visited:

visited[row][col][k]

Recognition Rule

If position alone is insufficient → BFS with state

8️⃣ BFS QUICK CHECKLIST 🧠

Equal weights? → BFS

Multiple sources? → Multi-source BFS

0/1 weights? → 0–1 BFS

Extra constraint? → BFS with state

9️⃣ Must-Do BFS Problems

LC 542 — 01 Matrix

LC 1091 — Shortest Path Binary Matrix

LC 127 — Word Ladder

LC 994 — Rotting Oranges

LC 1368 — Minimum Cost Grid

LC 1293 — Obstacles Elimination