class Solution {
    public int uniqueXorTriplets(int[] nums) {
        int n = nums.length;
        
        // 2048 covers all possible 11-bit XOR combinations (since nums[i] <= 1500)
        boolean[] hasPair = new boolean[2048];
        boolean[] isValid = new boolean[2048];
        
        // Step 1: Base combinations. 
        // Any overlapping indices (i=j or j=k) just result in the array elements themselves.
        for (int x : nums) {
            isValid[x] = true;
        }
        
        // Step 2: Find all distinct combinations where i < j < k
        // We iterate backward to build the (j, k) pairs ahead of our current i
        for (int i = n - 1; i >= 0; i--) {
            
            // Check all previously found pairs (which exist strictly after index i)
            for (int v = 0; v < 2048; v++) {
                if (hasPair[v]) {
                    isValid[nums[i] ^ v] = true;
                }
            }
            
            // Now, add new pairs formed by nums[i] and any element after it
            // These pairs will be used by elements BEFORE i in future outer loop iterations
            for (int k = i + 1; k < n; k++) {
                hasPair[nums[i] ^ nums[k]] = true;
            }
        }
        
        // Step 3: Count unique valid XOR values
        int count = 0;
        for (int i = 0; i < 2048; i++) {
            if (isValid[i]) {
                count++;
            }
        }
        
        return count;
    }
}