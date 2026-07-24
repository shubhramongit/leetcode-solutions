import java.util.ArrayList;
import java.util.List;

class Solution {
    public List<Integer> partitionLabels(String s) {
        // Array to store the last index of each character (26 lowercase English letters)
        int[] last = new int[26];
        for (int i = 0; i < s.length(); i++) {
            last[s.charAt(i) - 'a'] = i;
        }
        
        int end = 0;
        int anchor = 0;
        List<Integer> result = new ArrayList<>();
        
        // Iterate through the string to establish partition boundaries
        for (int i = 0; i < s.length(); i++) {
            // Update the furthest index we need to reach for the current partition
            end = Math.max(end, last[s.charAt(i) - 'a']);
            
            // If the current index reaches the maximum required end index, close the partition
            if (i == end) {
                result.add(i - anchor + 1);
                anchor = i + 1; // Start the new partition at the next character
            }
        }
        
        return result;
    }
}