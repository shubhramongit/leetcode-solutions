import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

class Solution{
    public long[] minTimeMaxPower(int n,int[][] edges, int power, int[] cost, int source,int target){
        if(source== target){
            return new long[]{0,power};
        }
        List<int[]>[] graph=new ArrayList[n];
        for(int i=0;i <n;i++){
            graph[i]=new ArrayList<>();
        
        }
        for(int[] e: edges){
            graph[e[0]].add(new int[]{e[1],e[2]});
        }
        PriorityQueue<long[]> pq=new PriorityQueue<>((a,b) -> {
            if(a[0] != b[0]) 
                return Long.compare(a[0],b[0]);
            return Long.compare(b[1],a[1]);
        });

        int[] maxPowerAtNode=new int[n];
        Arrays.fill(maxPowerAtNode,-1);
        
        pq.offer(new long[]{0,power,source});

        while(!pq.isEmpty()){
            long[] curr=pq.poll();
            long currentTime=curr[0];
            int currentPower=(int) curr[1];
            int u=(int) curr[2];

            if(u==target){
                return new long[]{currentTime,currentPower};
            }
            if(currentPower <= maxPowerAtNode[u]){
                continue;
            }
        maxPowerAtNode[u]= currentPower;
            if(currentPower < cost[u]){
                continue;
            }
            int nextPower=currentPower-cost[u];
            for(int[] edge: graph[u]){
                int v=edge[0];
                long nextTime=currentTime + edge[1]; 
                pq.offer(new long[]{nextTime,nextPower,v});
            }
        }
        return new long[]{-1,-1};
    }
}