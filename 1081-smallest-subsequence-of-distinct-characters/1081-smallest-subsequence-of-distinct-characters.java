class Solution {
    public String smallestSubsequence(String s) {
      int[] count= new int[26];
        boolean[] visited=new boolean[26];
        StringBuilder sb=new StringBuilder();

        for(char c:s.toCharArray()){
            count[c-'a']++;
        }
        for(char c: s.toCharArray()){
            count[c-'a']--;
            
            if(visited[c-'a']){
                continue;
            }
            while(sb.length() > 0 &&
            sb.charAt(sb.length() -1) > c &&
            count[sb.charAt(sb.length()-1)-'a']>0){

                visited[sb.charAt(sb.length()-1)-'a']=false;

                sb.deleteCharAt(sb.length()-1);
            }
            sb.append(c);
            visited[c-'a']=true;
        }
        return sb.toString();
    }
}