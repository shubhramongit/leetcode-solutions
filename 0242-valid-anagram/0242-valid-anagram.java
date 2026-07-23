class Solution {
    public boolean isAnagram(String s, String t) {
        // If the lengths aren't equal, they physically cannot be anagrams.
        // Don't waste CPU cycles checking anything else.
        if (s.length() != t.length()) {
            return false;
        }

        // Frequency map for 26 lowercase English letters
        int[] charCounts = new int[26];
        
        // Increment the count for characters in s, decrement for characters in t
        for (int i = 0; i < s.length(); i++) {
            charCounts[s.charAt(i) - 'a']++;
            charCounts[t.charAt(i) - 'a']--;
        }
        
        // If they are valid anagrams, every bucket should be exactly 0
        for (int count : charCounts) {
            if (count != 0) {
                return false;
            }
        }
        
        return true;
    }
}