import java.util.ArrayList;
import java.util.List;

class Solution {
    class Block {
        int type;
        int len;
        Block(int type, int len) {
            this.type = type;
            this.len = len;
        }
    }

    public int maxActiveSectionsAfterTrade(String s) {
        if (s == null || s.length() == 0) 
        return 0;
        
        List<Block> blocks = new ArrayList<>();
        int currentType = s.charAt(0) - '0';
        int currentLen = 0;
        int totalOnes = 0;
        
        for (int i = 0; i < s.length(); i++) {
            int type = s.charAt(i) - '0';
            if (type == 1) totalOnes++;
            
            if (type == currentType) {
                currentLen++;
            } 
            else {
                blocks.add(new Block(currentType, currentLen));
                currentType = type;
                currentLen = 1;
            }
        }
        blocks.add(new Block(currentType, currentLen));
        
        int[][] top3 = new int[3][2];
        for(int i = 0; i < 3; i++) {
            top3[i][0] = -1;
            top3[i][1] = -1;
        }
        
        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i).type == 0) {
                int len = blocks.get(i).len;
                if (len > top3[0][1]) {
                    top3[2][0] = top3[1][0]; top3[2][1] = top3[1][1];
                    top3[1][0] = top3[0][0]; top3[1][1] = top3[0][1];
                    top3[0][0] = i; top3[0][1] = len;
                } 
                else if (len > top3[1][1]) {
                    top3[2][0] = top3[1][0]; top3[2][1] = top3[1][1];
                    top3[1][0] = i; top3[1][1] = len;
                } 
                else if (len > top3[2][1]) {
                    top3[2][0] = i; top3[2][1] = len;
                }
            }
        }
        
        int maxOnes = totalOnes; 
        
        for (int i = 1; i < blocks.size() - 1; i++) {
            if (blocks.get(i).type == 1) {
                int mergedZeroes = blocks.get(i-1).len + blocks.get(i).len + blocks.get(i+1).len;
                
                int otherMax = 0;
                for (int j = 0; j < 3; j++) {
                    int idx = top3[j][0];
                    int len = top3[j][1];
                    if (idx != -1 && idx != i - 1 && idx != i + 1) {
                        otherMax = len;
                        break;
                    }
                }
                int bestZeroFlip = Math.max(mergedZeroes, otherMax);
                int currentOnes = totalOnes - blocks.get(i).len + bestZeroFlip;
                
                maxOnes = Math.max(maxOnes, currentOnes);
            }
        }
        return maxOnes;
    }
}