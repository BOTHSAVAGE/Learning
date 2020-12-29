由于可以多次买卖,所以只要后一天价钱大于前一天,我们就执行买卖的操作.
比如价格为1,2,3,4时,最大值为4-1=3,也可看成(2-1)+(3-2)+(4-3)的操作.
```
class Solution {
    public int maxProfit(int[] prices) {
        if (prices==null || prices.length==0) return 0;
        int result=0;
        for (int i=1;i<prices.length;i++) result+=(prices[i]-prices[i-1]>0)? prices[i]-prices[i-1]:0;
        return result;
    }
}
```
