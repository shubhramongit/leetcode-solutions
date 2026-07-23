import java.util.*;

/**
 * LeetCode 3575. Maximum Good Subtree Score
 *
 * For each node u, maxScore[u] = max sum of a subset of nodes within u's
 * subtree such that, across the selected node values, every decimal digit
 * (0-9) appears at most once. Return sum(maxScore) mod 1e9+7.
 *
 * Approach: tree DP with a 10-bit "digits used" mask.
 *   - dp(u) is a table: mask -> best achievable sum using exactly that
 *     digit-set, considering only nodes inside subtree(u).
 *   - Merge children pairwise: for any two disjoint masks m1, m2 you can
 *     combine their sums under mask (m1 | m2).
 *   - A node whose OWN value already has a repeated digit (e.g. 22) can
 *     never be picked (it already breaks the "at most once" rule), so it's
 *     folded in only as an optional item, and skipped if invalid.
 *   - Tables are kept as compact (mask[], value[]) arrays instead of full
 *     1024-length scans on every merge, so real-world performance is far
 *     below the theoretical O(n * 4^10) worst case.
 *
 * Time:  worst case O(n * 2^10 * 2^10), practically much less due to sparsity.
 * Space: O(n) recursion + O(2^10) scratch buffer.
 */
class Solution {
    private static final int MOD = 1_000_000_007;
    private static final int FULL = 1024; // 2^10 possible digit-masks

    private List<Integer>[] children;
    private int[] vals;
    private long answer = 0;
    private long[] scratch = new long[FULL]; // reusable accumulator, always -1 outside merges

    @SuppressWarnings("unchecked")
    public int goodSubtreeSum(int[] vals, int[] par) {
        int n = vals.length;
        this.vals = vals;
        children = new List[n];
        for (int i = 0; i < n; i++) children[i] = new ArrayList<>();
        for (int i = 1; i < n; i++) children[par[i]].add(i);

        Arrays.fill(scratch, -1L);
        dfs(0);
        return (int) (answer % MOD);
    }

    // Compact DP table for a subtree: parallel arrays of (mask, bestValue).
    private static final class Table {
        final int[] masks;
        final long[] vals;
        Table(int[] m, long[] v) { masks = m; vals = v; }
    }

    private Table dfs(int u) {
        int[] curMasks = {0};
        long[] curVals = {0L};

        for (int c : children[u]) {
            Table child = dfs(c);
            int[] cm = child.masks;
            long[] cv = child.vals;

            // Sparse pairwise merge: only touch combinations that actually exist.
            for (int i = 0; i < curMasks.length; i++) {
                int m1 = curMasks[i];
                long v1 = curVals[i];
                for (int j = 0; j < cm.length; j++) {
                    int m2 = cm[j];
                    if ((m1 & m2) != 0) continue; // digit collision, invalid combo
                    int nm = m1 | m2;
                    long v = v1 + cv[j];
                    if (v > scratch[nm]) scratch[nm] = v;
                }
            }
            Table merged = drainScratch();
            curMasks = merged.masks;
            curVals = merged.vals;
        }

        int selfMask = digitMask(vals[u]);
        if (selfMask != -1) {
            // Baseline: keep every existing option as-is (self not selected).
            for (int i = 0; i < curMasks.length; i++) {
                scratch[curMasks[i]] = curVals[i];
            }
            // Try adding self on top of every compatible existing option.
            for (int i = 0; i < curMasks.length; i++) {
                int m1 = curMasks[i];
                if ((m1 & selfMask) != 0) continue;
                int nm = m1 | selfMask;
                long v = curVals[i] + vals[u];
                if (v > scratch[nm]) scratch[nm] = v;
            }
            Table merged = drainScratch();
            curMasks = merged.masks;
            curVals = merged.vals;
        }

        long best = 0;
        for (long v : curVals) if (v > best) best = v;
        answer += best;

        return new Table(curMasks, curVals);
    }

    // Reads all set entries out of `scratch`, resets them to -1, returns a compact Table.
    private Table drainScratch() {
        int count = 0;
        for (int m = 0; m < FULL; m++) if (scratch[m] != -1L) count++;

        int[] masks = new int[count];
        long[] vals = new long[count];
        int idx = 0;
        for (int m = 0; m < FULL; m++) {
            if (scratch[m] != -1L) {
                masks[idx] = m;
                vals[idx] = scratch[m];
                scratch[m] = -1L;
                idx++;
            }
        }
        return new Table(masks, vals);
    }

    // Returns the 10-bit digit mask of v, or -1 if v itself has a repeated digit.
    private int digitMask(int v) {
        int mask = 0;
        while (v > 0) {
            int d = v % 10;
            int bit = 1 << d;
            if ((mask & bit) != 0) return -1;
            mask |= bit;
            v /= 10;
        }
        return mask;
    }
}