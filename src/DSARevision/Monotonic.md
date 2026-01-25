MONOTONIC STACK

This is not a new data structure — it’s a discipline.
1️⃣ Story — Why Monotonic Stack Exists

Imagine you are scanning numbers left to right.

For each number you want to know:

nearest bigger on left?

nearest smaller on right?

Brute force:

look left and right every time → O(n²)

Monotonic Stack says:

“Maintain only useful elements in order.”

2️⃣ When to THINK of Monotonic Stack (INTERVIEW TRIGGERS)

The moment you hear:

✅ Next Greater / Smaller
✅ Previous Greater / Smaller
✅ Histogram
✅ Span
✅ Temperature problems
✅ Subarray min/max contribution

👉 Monotonic Stack

3️⃣ Core Mental Model (VERY IMPORTANT)

Stack always maintains:

Increasing OR

Decreasing order

Elements that can never be useful again → popped

Each element is:

pushed once

popped once

👉 O(n) guaranteed.

Next Greater Element(Right)
```java
public int[] nextGreater(int[]nums){
    int n= nums.length;
    int[]res = new int[n];
    Stack<Integer>st = new Stack<>();
    
    for(int i= n-1;i>=0;i--){
        While(!st.isEmpty() && st.peek()<= nums[i]){
            st.pop();
        }
        res[i]= st.isEmpty()? -1 : st.peek();
        st.push(nums[i]);
    }
    return res;
}
```
Previous Smaller Element (Left)
```java
public int[] prevSmaller(int[]nums){
    int n= nums.length;
    int[] res = new int[n];
    for(int i=0;i<n;i++){
        while(!st.isEmpty() && st.peek()>=nums[i]){
            st.pop();
        }
        res[i]= st.isEmpty()?-1 : st.peek();
        st.push(nums[i]);
    }
    return res;
}
```

5️⃣ MOST IMPORTANT PROBLEM — HISTOGRAM
🔹 LC 84 — Largest Rectangle in Histogram
Story:

Each bar wants to expand:

left until smaller

right until smaller

Area:

height[i] * (rightSmaller - leftSmaller - 1)

```java
public int largestRectangleArea(int[] heights){
    Stack<Integer> st = new Stack<>();
    int maxArea =0;
    
    for(int i=0; i<= heights.length;i++){
        int curr = (i== heights.length)? 0 : heights[i];
        while(!st.isEmpty && curr < heights[st.peek()]){
            int h= heights[st.pop()];
            int width = st.isEmpty()? i : i-st.peek()-1;
            maxArea = Math.max(maxArea, h*width);
        }
        st.push(i);
    }
    return maxArea;
}
```
6️⃣ Famous LeetCode Problems (MUST KNOW)

LC 84 — Histogram

LC 85 — Max Rectangle

LC 739 — Daily Temperatures

LC 496 — Next Greater Element

LC 901 — Stock Span

LC 907 — Sum of Subarray Minimums

7️⃣ Interview Traps (VERY COMMON)

❌ Confusing index vs value
❌ Forgetting sentinel 0 at end
❌ Using nested loops
❌ Wrong comparison (>= vs >)

8️⃣ Sharp Interview Thinking Tricks 🧠
🔥 Trick 1 — Contribution Technique

Each element contributes as:

minimum,
maximum
over a range

🔥 Trick 2 — Direction matters

Right scan → next
Left scan → previous

🔥 Trick 3 — Stack stores INDEX for range problems
9️⃣ One-Line Recognition Rule

If nearest greater/smaller matters → Monotonic Stack