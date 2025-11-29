import java.util.*;

public class DSAUtils {

    /* ---------------------------
       LIST OPERATIONS
    ---------------------------- */

    // Create list with values
    public static List<Integer> listOf(int... arr) {
        List<Integer> list = new ArrayList<>();
        for (int x : arr) list.add(x);
        return list;
    }
    int[] arr = new int[10];
    //Arrays.fill(arr, 0);
    List<Integer> list = Arrays.stream(arr).boxed().toList();
    // Initialize list with n zeros
    public static List<Integer> zeroList(int n) {
        List<Integer> list = new ArrayList<>(Collections.nCopies(n, 0));
        return list;
    }

    // Print list
    public static <T> void printList(List<T> list) {
        System.out.println(list);
    }

    // Increment value at index
    public static void increment(List<Integer> list, int index) {
        list.set(index, list.get(index) + 1);
    }

    /* ---------------------------
       MAP OPERATIONS
    ---------------------------- */

    // Frequency map builder
    public static Map<Integer, Integer> freq(int[] arr) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int x : arr)
            map.put(x, map.getOrDefault(x, 0) + 1);
        return map;
    }

    // Increment map value
    public static void inc(Map<Integer, Integer> map, int key) {
        map.put(key, map.getOrDefault(key, 0) + 1);
    }

    /* ---------------------------
       SET OPERATIONS
    ---------------------------- */

    // Convert list to set
    public static <T> Set<T> toSet(List<T> list) {
        return new HashSet<>(list);
    }

    /* ---------------------------
       HEAP OPERATIONS
    ---------------------------- */

    // Min-heap
    public static PriorityQueue<Integer> minHeap() {
        return new PriorityQueue<>();
    }

    // Max-heap
    public static PriorityQueue<Integer> maxHeap() {
        return new PriorityQueue<>(Collections.reverseOrder());
    }

    /* ---------------------------
       STACK (Using Deque)
    ---------------------------- */

    public static Deque<Integer> stack() {
        return new ArrayDeque<>();
    }

    /* ---------------------------
       QUEUE (Using Deque)
    ---------------------------- */

    public static Deque<Integer> queue() {
        return new ArrayDeque<>();
    }

    /* ---------------------------
       CONVERSIONS
    ---------------------------- */

    // Array → List
    public static List<Integer> toList(int[] arr) {
        List<Integer> res = new ArrayList<>();
        for (int x : arr) res.add(x);
        return res;
    }

    // List → Array
    public static int[] toArray(List<Integer> list) {
        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    /* ---------------------------
       COMMON PATTERNS
    ---------------------------- */

    // Two-pointer pattern
    public static void twoPointerDemo(List<Integer> list) {
        int l = 0, r = list.size() - 1;
        while (l < r) {
            int leftVal = list.get(l);
            int rightVal = list.get(r);
            l++;
            r--;
        }
    }

    // Monotonic stack
    public static List<Integer> nextGreater(int[] nums) {
        List<Integer> res = new ArrayList<>(Collections.nCopies(nums.length, -1));
        Deque<Integer> st = new ArrayDeque<>();

        for (int i = 0; i < nums.length; i++) {
            while (!st.isEmpty() && nums[st.peek()] < nums[i]) {
                res.set(st.pop(), nums[i]);
            }
            st.push(i);
        }
        return res;
    }
}
