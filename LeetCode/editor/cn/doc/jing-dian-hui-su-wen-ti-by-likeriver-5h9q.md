```java
class Solution {
    public List<String> restoreIpAddresses(String s) {
        List<String> res = new ArrayList<>();
        if(s.length() <= 3 || s.length() > 12)
            return res;
        
        dfs(s, 0, 0, res, "");
        return res;
    }

    private void dfs(String s, int index, int depth, List<String> res, String cur){
        if(depth == 3){
            String tmp2 = s.substring(index, s.length());
            if(judge(tmp2))
                res.add(cur+tmp2);
            return ;
        }

        //每次可以选1 2 3位
        for(int i = 1; i <= 3; i++){
            if(index+i >= s.length())
                break;
            String tmp = s.substring(index, index+i);
            if(!judge(tmp))
                continue;  

            dfs(s, index+i, depth+1, res, cur+tmp+".");
        }
    }

    private boolean judge(String str){
        //长度大于3的错误
        if(str.length() > 3)
            return false;
        //0开头的错误
        if(str.length() > 1 && str.charAt(0) == '0')
            return false;
        //有特殊字符的错误
        for(int i = 0; i < str.length(); i++){
            if(str.charAt(i) > '9' || str.charAt(i) < '0')
                return false;
        }  
        //大于255的错误
        if(Integer.parseInt(str) > 255)
            return false;
        
        return true;    
    }
}
```
