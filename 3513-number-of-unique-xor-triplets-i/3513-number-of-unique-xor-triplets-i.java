class Solution {
    public int uniqueXorTriplets(int[] nums) {
      int n = nums.length;
        
        // Base cases for n = 1 and n = 2
        if (n < 3) {
            return n;
        }
        
        // Find the highest power of 2 less than or equal to n, 
        // then shift left by 1 (multiply by 2) to get the strict upper bound power of 2.
        // For example: if n = 4, highestOneBit is 4. 4 << 1 = 8.
        // if n = 7, highestOneBit is 4. 4 << 1 = 8.
        return Integer.highestOneBit(n) << 1;
    }
}