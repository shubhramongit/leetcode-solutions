class Solution {
    public String lexPalindromicPermutation(String s, String target) {
        int n = s.length();
        int[] freq = new int[26];
        for (char c : s.toCharArray()) {
            freq[c - 'a']++;
        }
        
        int oddCount = 0;
        int midChar = -1;
        for (int i = 0; i < 26; i++) {
            if (freq[i] % 2 != 0) {
                oddCount++;
                midChar = i;
            }
        }
        
        // A palindrome can have at most one character with an odd frequency
        if (oddCount > 1) {
            return "";
        }
        
        int[] avail = new int[26];
        for (int i = 0; i < 26; i++) {
            avail[i] = freq[i] / 2;
        }
        
        int m = n / 2;
        
        // Iterate backward to find the longest possible prefix match first
        for (int i = m; i >= 0; i--) {
            int[] rem = avail.clone();
            boolean possible = true;
            StringBuilder leftHalf = new StringBuilder();
            
            // Step 1: Exactly match target up to index i - 1
            for (int j = 0; j < i; j++) {
                int c = target.charAt(j) - 'a';
                if (rem[c] == 0) {
                    possible = false;
                    break;
                }
                rem[c]--;
                leftHalf.append((char) (c + 'a'));
            }
            
            if (!possible) continue;
            
            // Step 2: Handle the divergence boundary at index i
            if (i == m) {
                String L = leftHalf.toString();
                String P = buildPalindrome(L, midChar);
                if (P.compareTo(target) > 0) {
                    return P;
                }
            } else {
                int targetChar = target.charAt(i) - 'a';
                int pick = -1;
                
                // Find the smallest available character strictly greater than target[i]
                for (int c = targetChar + 1; c < 26; c++) {
                    if (rem[c] > 0) {
                        pick = c;
                        break;
                    }
                }
                
                if (pick == -1) continue;
                
                rem[pick]--;
                leftHalf.append((char) (pick + 'a'));
                
                // Step 3: Greedily append the smallest remaining characters
                for (int c = 0; c < 26; c++) {
                    while (rem[c] > 0) {
                        leftHalf.append((char) (c + 'a'));
                        rem[c]--;
                    }
                }
                
                String L = leftHalf.toString();
                return buildPalindrome(L, midChar);
            }
        }
        
        return "";
    }
    
    private String buildPalindrome(String L, int midChar) {
        StringBuilder sb = new StringBuilder(L);
        if (midChar != -1) {
            sb.append((char) (midChar + 'a'));
        }
        for (int i = L.length() - 1; i >= 0; i--) {
            sb.append(L.charAt(i));
        }
        return sb.toString();
    }
}