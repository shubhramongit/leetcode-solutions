import java.util.*;

class Solution {
    class Group {
        int start, end, length;
        Group(int s, int e) {
            start = s;
            end = e;
            length = e - s + 1;
        }
    }

    class SparseTable {
        int[][] st;
        int[] log2;

        public SparseTable(int[] arr) {
            int n = arr.length;
            if (n == 0) return;
            log2 = new int[n + 1];
            for (int i = 2; i <= n; i++) log2[i] = log2[i / 2] + 1;
            
            int k = log2[n] + 1;
            st = new int[k][n];
            for (int i = 0; i < n; i++) st[0][i] = arr[i];
            
            for (int j = 1; j < k; j++) {
                for (int i = 0; i + (1 << j) <= n; i++) {
                    st[j][i] = Math.max(st[j - 1][i], st[j - 1][i + (1 << (j - 1))]);
                }
            }
        }

        public int query(int L, int R) {
            if (L > R) return 0;
            int j = log2[R - L + 1];
            return Math.max(st[j][L], st[j][R - (1 << j) + 1]);
        }
    }

    public List<Integer> maxActiveSectionsAfterTrade(String s, int[][] queries) {
        int n = s.length();
        int totalOnes = 0;
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '1') totalOnes++;
        }
        
        List<Group> zeroGroups = new ArrayList<>();
        int[] zeroGroupIndex = new int[n];
        Arrays.fill(zeroGroupIndex, -1);
        
        int[] nextZero = new int[n];
        int[] prevZero = new int[n];
        
        int start = -1;
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '0') {
                if (start == -1) start = i;
            } else {
                if (start != -1) {
                    zeroGroups.add(new Group(start, i - 1));
                    start = -1;
                }
            }
        }
        if (start != -1) {
            zeroGroups.add(new Group(start, n - 1));
        }
        
        for (int i = 0; i < zeroGroups.size(); i++) {
            Group g = zeroGroups.get(i);
            for (int j = g.start; j <= g.end; j++) {
                zeroGroupIndex[j] = i;
            }
        }
        
        int lastZero = -1;
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '0') lastZero = i;
            prevZero[i] = lastZero;
        }
        
        int nxtZero = n;
        for (int i = n - 1; i >= 0; i--) {
            if (s.charAt(i) == '0') nxtZero = i;
            nextZero[i] = nxtZero;
        }
        
        int[] A = new int[Math.max(0, zeroGroups.size() - 1)];
        for (int i = 0; i < zeroGroups.size() - 1; i++) {
            A[i] = zeroGroups.get(i).length + zeroGroups.get(i + 1).length;
        }
        
        SparseTable st = new SparseTable(A);
        
        int q = queries.length;
        // Changed to List<Integer> to match the boilerplate
        List<Integer> ans = new ArrayList<>(q);
        
        for (int i = 0; i < q; i++) {
            int l = queries[i][0];
            int r = queries[i][1];
            
            int pFirst = nextZero[l];
            int pLast = prevZero[r];
            
            if (pFirst > r || pFirst == pLast || zeroGroupIndex[pFirst] == zeroGroupIndex[pLast]) {
                ans.add(totalOnes);
                continue;
            }
            
            int idFirst = zeroGroupIndex[pFirst];
            int idLast = zeroGroupIndex[pLast];
            
            int cFirst = zeroGroups.get(idFirst).end - pFirst + 1;
            int cLast = pLast - zeroGroups.get(idLast).start + 1;
            
            if (idFirst + 1 == idLast) {
                ans.add(totalOnes + cFirst + cLast);
            } else {
                int gain1 = cFirst + zeroGroups.get(idFirst + 1).length;
                int gain2 = zeroGroups.get(idLast - 1).length + cLast;
                int gain3 = 0;
                
                if (idFirst + 1 <= idLast - 2) {
                    gain3 = st.query(idFirst + 1, idLast - 2);
                }
                
                ans.add(totalOnes + Math.max(gain1, Math.max(gain2, gain3)));
            }
        }
        
        return ans;
    }
}