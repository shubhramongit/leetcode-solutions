class Solution {
    public int lengthOfLongestSubstring(String s) {
        // Map to store the next index of each ASCII character
        int[] indexMap = new int[128];
        
        int maxLength = 0;
        int left = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            
            // If we have seen this character before, jump the left pointer.
            // We use Math.max to ensure the left pointer only moves forward, 
            // preventing it from jumping backward to an old duplicate outside our current window.
            left = Math.max(left, indexMap[c]);
            
            // Calculate the max length for the current valid window
            maxLength = Math.max(maxLength, right - left + 1);
            
            // Store the index immediately following the current character 
            // so we know exactly where to jump left if we see it again
            indexMap[c] = right + 1;
        }
        
        return maxLength;
    }
}