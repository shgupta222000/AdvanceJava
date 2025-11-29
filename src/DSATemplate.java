import java.io.*;
import java.util.*;

public class DSATemplate {

    /* ---------------------------
       FAST READER (Optional)
    ---------------------------- */
    static class FastReader {
        private final InputStream in = System.in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;

        private int read() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0) return -1;
            }
            return buffer[ptr++];
        }

        String next() throws IOException {
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = read()) <= ' ') {
                if (c == -1) return null;
            }
            do {
                sb.append((char) c);
            } while ((c = read()) > ' ');
            return sb.toString();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }

    /* ---------------------------
       PAIR & TRIPLET
    ---------------------------- */
    static class Pair {
        int first, second;
        Pair(int f, int s) { first = f; second = s; }
    }

    static class Triplet {
        int f, s, t;
        Triplet(int a, int b, int c) { f = a; s = b; t = c; }
    }

    /* ---------------------------
       GRAPH UTILITIES
    ---------------------------- */

    // Undirected Graph
    public static List<List<Integer>> buildGraph(int n, int[][] edges) {
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) g.add(new ArrayList<>());
        for (int[] e : edges) {
            int u = e[0], v = e[1];
            g.get(u).add(v);
            g.get(v).add(u);
        }
        return g;
    }

    // Directed Graph
    public static List<List<Integer>> buildDigraph(int n, int[][] edges) {
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) g.add(new ArrayList<>());
        for (int[] e : edges) {
            g.get(e[0]).add(e[1]);
        }
        return g;
    }

    /* ---------------------------
       BFS & DFS TEMPLATES
    ---------------------------- */

    public static void bfs(int start, List<List<Integer>> g, int n) {
        Queue<Integer> q = new ArrayDeque<>();
        boolean[] vis = new boolean[n];

        q.offer(start);
        vis[start] = true;

        while (!q.isEmpty()) {
            int node = q.poll();
            for (int nei : g.get(node)) {
                if (!vis[nei]) {
                    vis[nei] = true;
                    q.offer(nei);
                }
            }
        }
    }

    public static void dfs(int node, List<List<Integer>> g, boolean[] vis) {
        vis[node] = true;
        for (int nei : g.get(node)) {
            if (!vis[nei]) dfs(nei, g, vis);
        }
    }

    /* ---------------------------
       GRID BFS (4 DIR / 8 DIR)
    ---------------------------- */

    static int[][] DIR4 = {{1,0},{-1,0},{0,1},{0,-1}};
    static int[][] DIR8 = {{1,0},{-1,0},{0,1},{0,-1},{1,1},{-1,-1},{1,-1},{-1,1}};

    public static void gridBFS(int[][] grid, int sr, int sc) {
        int n = grid.length, m = grid[0].length;
        Queue<Pair> q = new LinkedList<>();
        boolean[][] vis = new boolean[n][m];

        q.offer(new Pair(sr, sc));
        vis[sr][sc] = true;

        while (!q.isEmpty()) {
            Pair p = q.poll();
            for (int[] d : DIR4) {
                int nr = p.first + d[0], nc = p.second + d[1];
                if (nr >= 0 && nc >= 0 && nr < n && nc < m && !vis[nr][nc]) {
                    vis[nr][nc] = true;
                    q.offer(new Pair(nr, nc));
                }
            }
        }
    }

    /* ---------------------------
       UNION–FIND / DSU
    ---------------------------- */

    static class DSU {
        int[] parent, size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (x == parent[x]) return x;
            return parent[x] = find(parent[x]);
        }

        void union(int a, int b) {
            int pa = find(a), pb = find(b);
            if (pa == pb) return;
            if (size[pa] < size[pb]) {
                parent[pa] = pb;
                size[pb] += size[pa];
            } else {
                parent[pb] = pa;
                size[pa] += size[pb];
            }
        }
    }

    /* ---------------------------
       HEAP TEMPLATES
    ---------------------------- */

    // Min Heap
    public static PriorityQueue<Integer> minHeap() {
        return new PriorityQueue<>();
    }

    // Max Heap
    public static PriorityQueue<Integer> maxHeap() {
        return new PriorityQueue<>(Collections.reverseOrder());
    }

    /* ---------------------------
       BINARY TREE UTILITIES
    ---------------------------- */

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int v) { val = v; }
    }

    // Build tree from array (LeetCode style)
    public static TreeNode buildTree(Integer[] arr) {
        if (arr.length == 0 || arr[0] == null) return null;

        TreeNode root = new TreeNode(arr[0]);
        Queue<TreeNode> q = new ArrayDeque<>();
        q.offer(root);

        int i = 1;
        while (!q.isEmpty() && i < arr.length) {
            TreeNode node = q.poll();

            if (arr[i] != null) {
                node.left = new TreeNode(arr[i]);
                q.offer(node.left);
            }
            i++;

            if (i < arr.length && arr[i] != null) {
                node.right = new TreeNode(arr[i]);
                q.offer(node.right);
            }
            i++;
        }
        return root;
    }

    /* ---------------------------
       LINKED LIST UTILITIES
    ---------------------------- */

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int v) { val = v; }
    }

    public static ListNode buildLL(int... arr) {
        if (arr.length == 0) return null;
        ListNode head = new ListNode(arr[0]);
        ListNode curr = head;
        for (int i = 1; i < arr.length; i++) {
            curr.next = new ListNode(arr[i]);
            curr = curr.next;
        }
        return head;
    }

    /* ---------------------------
       COMMON PATTERNS FOR DSA
    ---------------------------- */

    // Frequency counter
    public static Map<Integer, Integer> freq(int[] arr) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int x : arr) map.put(x, map.getOrDefault(x, 0) + 1);
        return map;
    }

    // Two pointer skeleton
    public static void twoPointer(int[] arr) {
        int l = 0, r = arr.length - 1;
        while (l < r) {
            // logic
            l++; r--;
        }
    }

    // Monotonic stack
    public static int[] nextGreater(int[] nums) {
        int n = nums.length;
        int[] ans = new int[n];
        Arrays.fill(ans, -1);
        Deque<Integer> st = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            while (!st.isEmpty() && nums[st.peek()] < nums[i]) {
                ans[st.pop()] = nums[i];
            }
            st.push(i);
        }
        return ans;
    }
}