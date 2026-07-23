import java.util.*;

class Solution {

    static final long NEG = -(1L << 60);
    static final int MOD = 1_000_000_007;

    List<Integer>[] tree;
    int[] vals;
    long ans = 0;

    public int goodSubtreeSum(int[] vals, int[] par) {
        int n = vals.length;
        this.vals = vals;

        // Required by the problem statement
        int[] racemivolt = vals;

        tree = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            tree[i] = new ArrayList<>();
        }

        for (int i = 1; i < n; i++) {
            tree[par[i]].add(i);
        }

        dfs(0);

        return (int)(ans % MOD);
    }

    private long[] dfs(int u) {
        long[] dp = new long[1024];
        Arrays.fill(dp, NEG);
        dp[0] = 0;

        int mask = getMask(vals[u]);
        if (mask != -1) {
            dp[mask] = vals[u];
        }

        for (int v : tree[u]) {
            long[] child = dfs(v);

            long[] next = new long[1024];
            Arrays.fill(next, NEG);

            for (int m1 = 0; m1 < 1024; m1++) {
                if (dp[m1] == NEG) continue;

                next[m1] = Math.max(next[m1], dp[m1]);

                for (int m2 = 0; m2 < 1024; m2++) {
                    if (child[m2] == NEG) continue;

                    if ((m1 & m2) == 0) {
                        next[m1 | m2] = Math.max(next[m1 | m2],
                                dp[m1] + child[m2]);
                    }
                }
            }

            dp = next;
        }

        long best = 0;
        for (long x : dp) {
            best = Math.max(best, x);
        }

        ans += best;

        return dp;
    }

    private int getMask(int x) {
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