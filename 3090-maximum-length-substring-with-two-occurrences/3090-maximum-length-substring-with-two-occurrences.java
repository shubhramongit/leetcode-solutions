class Solution {
    public int maximumLengthSubstring(String s) {
        // Frequency array for the 26 lowercase English letters
        int[] count = new int[26];
        int maxLength = 0;
        int left = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char rightChar = s.charAt(right);
            count[rightChar - 'a']++;
            
            // If the window becomes invalid (a character appears 3 times),
            // shrink it from the left until the duplicate is removed.
            while (count[rightChar - 'a'] > 2) {
                char leftChar = s.charAt(left);
                count[leftChar - 'a']--;
                left++;
            }
            
            // The window is guaranteed to be valid here. 
            // Update the maximum length found so far.
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
}