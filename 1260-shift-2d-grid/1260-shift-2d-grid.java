import java.util.ArrayList;
import java.util.List;
class Solution {
    public List<List<Integer>> shiftGrid(int[][] grid, int k) {
        int m=grid.length;
        int n=grid[0].length;
        int total=m*n;

        k=k%total;

        List<List<Integer>> result=new ArrayList<>();
        for(int r=0;r<m;r++){
            result.add(new ArrayList<>());
        }
        for(int i=0;i< total;i++){
            int original1DIndex=(i-k+total)% total;

            int origRow=original1DIndex /n;
            int origCol=original1DIndex %n;

            int targetRow=i/n;

            result.get(targetRow).add(grid[origRow][origCol]);
        }
        return result;
    }
}