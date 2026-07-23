import java.util.*;

class Solution {
    private static final int MOD = 1_000_000_007;
    private List<Integer>[] tree;
    private int[] vals;
    private long ans = 0;

    public int goodSubtreeSum(int[] vals, int[] par) {
        int n = vals.length;
        this.vals = vals;

        // Suppress the warning if you're going to use an array of generics
        @SuppressWarnings("unchecked")
        List<Integer>[] tempTree = new ArrayList[n];
        this.tree = tempTree;
        
        for (int i = 0; i < n; i++) {
            tree[i] = new ArrayList<>();
        }

        for (int i = 1; i < n; i++) {
            tree[par[i]].add(i);
        }

        dfs(0);

        return (int) ans;
    }

    private long[] dfs(int u) {
        long[] dp = new long[1024];
        Arrays.fill(dp, -1);
        dp[0] = 0;

        int mask = getMask(vals[u]);
        if (mask != -1) {
            dp[mask] = vals[u];
        }

        for (int v : tree[u]) {
            long[] child = dfs(v);
            long[] next = new long[1024];
            Arrays.fill(next, -1);

            for (int m1 = 0; m1 < 1024; m1++) {
                if (dp[m1] == -1) continue;
                
                // Keep the current state if we don't pick anything from the child
                next[m1] = Math.max(next[m1], dp[m1]);

                int comp = 1023 ^ m1;
                // Submask Enumeration: ONLY iterate over disjoint masks
                for (int m2 = comp; m2 > 0; m2 = (m2 - 1) & comp) {
                    if (child[m2] != -1) {
                        next[m1 | m2] = Math.max(next[m1 | m2], dp[m1] + child[m2]);
                    }
                }
            }
            dp = next;
        }

        long best = 0;
        for (long x : dp) {
            best = Math.max(best, x);
        }
        
        // Apply modulo at the accumulation step
        ans = (ans + best) % MOD;

        return dp;
    }

    private int getMask(int x) {
        if (x == 0) return 1; // Explicitly handle the 0 edge case
        
        int mask = 0;
        while (x > 0) {
            int d = x % 10;
            if ((mask & (1 << d)) != 0) {
                return -1;
            }
            mask |= 1 << d;
            x /= 10;
        }
        return mask;
    }
}