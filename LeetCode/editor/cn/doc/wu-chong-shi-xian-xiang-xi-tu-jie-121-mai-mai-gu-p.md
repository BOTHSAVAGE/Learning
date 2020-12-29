
## 暴力
暴力解法很简单，通过两层循环遍历数组即可   
因为只交易**一次**，且必须是先买再买。所以买的那一天的下标i，必须是小于卖的那一天的下标j。   
于是我们通过两层循环，在数组中找到两个值满足```nums[i]<nums[j]```，并且```i<j```   
即：max(prices[j]−prices[i])
![1.jpg](https://pic.leetcode-cn.com/589d43d2dc37d84bd99317dfacd1fb25e5c9691b0e832fc74ee6e3d66e82fba0-1.jpg)


暴力解法python版本无法通过，java版本也可以通过   
时间复杂度:O(N^2)    
空间复杂度:O(1)   
   
代码实现:
```java []
class Solution {
    public int maxProfit(int[] prices) {
        if(prices==null || prices.length==0) {
            return 0;
        }
        int n = prices.length;
        int res = 0;
        //两层循环找到 max(nums[j]>nums[i])
        for(int i=0;i<n-1;++i) {
            for(int j=i+1;j<n;++j) {
                int tmp = prices[j]-prices[i];
                if(tmp>res) {
                    res = tmp;
                }
            }
        }
        return res;
    }
}
```
```python []
class Solution(object):
    def maxProfit(self, prices):
        if not prices:
            return 0
        n = len(prices)
        res = 0
        # 两层循环找到 max(nums[j]>nums[i])
        for i in xrange(n-1):
            for j in xrange(i+1,n):
                tmp = prices[j]-prices[i]
                if res<tmp:
                    res = max(res,tmp)
        return res
```




## 动态规划-1
既然用了动态规划，肯定是要用**空间换时间**   
我们先看下执行状态，这是一个买入和卖出的过程，而且能交易一次即只能买卖一次，所以**买入**之后，状态可以转换到**卖出**，但是**卖出**之后就不能再**买入**了，因为只能**买卖一次**     
![2.jpg](https://pic.leetcode-cn.com/25e14cb8588c4b28b334942a378973679cf4dc41341395bee9a9e7ea8052669e-2.jpg)



我们需要用开辟额外的空间，来不断更新买入和卖出的最大收益
我们先看一个最简单的例子，假设只数组长度是**1**，即只有一天   
![3.jpg](https://pic.leetcode-cn.com/3ba6f1ee2fbb3dc2239a316c6ae0af9132f22b851a05c035ae0dd1b9de63b8ba-3.jpg)

这时候不管股价是多少，我的收益都是**0**，为什么呢？   
因为必须要**先买后卖**，只有一天的情况下无法卖出，所以卖出的收益是0。   
股价是7美元，也就是花了7美元买进这个股票，那么对应到第一天买入的最大收益就是-7美元。两者一合计当然是坐地不动更划算。   

我们再来看一个复杂点的，假设有三天
这里**卖出**数组记录了每次卖出的最大收益，数组下标i即对应第i天的最大收益   
**买入**数组记录了每次买入的最大收益，数组下标i即对应第i天的最大收益   
第一天的时候还是老样子，卖出是0，买入是-7
![4.jpg](https://pic.leetcode-cn.com/11b946e368f6e265b3d2b743e4a94754d07397e3dac0f4589ac96d9861723b6e-4.jpg)

第二天卖出的最大利润仍然是0   
卖出的收益是：第一天买入的值-7，加上第二天股票价格，也就是第一天我花7美元买入(买入对应收益就是-7)，第二天1美元卖出，所以第二天的卖出收益就是-6美元。这当时是亏本了，于是继续不动。   
买入的收益是：记录当天买入的最大收益，因为买入都是负的，所以最大收益就是找一个价格最低的，即前一天买入的最大收益vs第二天的股价，第一天花了7美元买入的，第二天股票跌到1美元，当然是买第二天的
![5.jpg](https://pic.leetcode-cn.com/2066080bd8b8b7fd603f260b12b375fdd75ce699b98286c22c98ffcee48262ce-5.jpg)


第三天卖出的利润就变了   
卖出收益是：第二天买入的值-1，加上第三天的股票价格，也就是前一天买入的最大收益 **-1**加上第三天的股票价格5，于是第三天买入的收益就变成了4   
买入的收益是：第二天买入的值-1vs第三天买入的股价-5，选择不动
![6.jpg](https://pic.leetcode-cn.com/f9b42e79f7f785b18c64785169049621c919bbdd331269ba286170be84baf781-6.jpg)



第i天卖出的最大收益计算公式为：
- i-1天卖出的最大收益 **vs** i-1天买入的最大收益+第i天的股价
- sell[i] = max(sell[i-1],buy[i-1]+prices[i])

第i天买入的最大收益计算公式为：(注意第i天前面有个负号)
- i-1天买入的最大收益 **vs** -第i天的股价
- buy[i] = max(buy[i-1],-prices[i])

完整执行过程请点击下面幻灯片查看

 ![1.jpg](https://pic.leetcode-cn.com/e3a141b0163034f28800f79d6893729a10ee028c58df9328d503d39227fd3ef0-1.jpg) ![2.jpg](https://pic.leetcode-cn.com/0554ebf9c2630ab79f81fc7d9e2e347a5a091210abbea5aaa50503a626756c8b-2.jpg) ![3.jpg](https://pic.leetcode-cn.com/d7784eca8a1006490e904caab5e8886fad86223606fb1604b1520788ea86ff39-3.jpg) ![4.jpg](https://pic.leetcode-cn.com/fa724b728790ed798ac920b6c59879cde2a3e9cda7c3762c736a73fab126219a-4.jpg) ![5.jpg](https://pic.leetcode-cn.com/b9c3675bad65445e15260d110c432c86e9632d4aeada6e459147b39a429a0031-5.jpg) ![6.jpg](https://pic.leetcode-cn.com/bd9b6f726e1118805e300c854b79bc7d15f43aee690490f9e55abce00397dbea-6.jpg) ![7.jpg](https://pic.leetcode-cn.com/653b431ade1f6c55d1210685975af0286080b42a0be44ca99ad51a2294b1baf0-7.jpg) ![8.jpg](https://pic.leetcode-cn.com/ce84b1a0e471f6f724785b2016b9afc66cca4b633bda10fa72168298d0eb76b4-8.jpg) 


时间复杂度:O(N)       
空间复杂度:O(N)    

代码实现:
```java []
class Solution {
    public int maxProfit(int[] prices) {
        if(prices==null || prices.length==0) {
            return 0;
        }
        int n = prices.length;
        //创建两个数组，一个记录每次卖出的最大收益，一个记录每次买入最大收益
        int[] sell = new int[n];
        int[] buy = new int[n];
        //初始化第一天收益
        sell[0] = 0;
        buy[0] = -prices[0];
        for(int i=1;i<n;++i) {
            //第i天卖出收益 = max(第i-1天卖出收益，第i-1天买入收益+当天股价)
            sell[i] = Math.max(sell[i-1],buy[i-1]+prices[i]);
            //第i天买入收益 = max(第i-1天买入收益，-当天股价)
            buy[i] = Math.max(buy[i-1],-prices[i]);
        }
        return Math.max(sell[n-1],buy[n-1]);
    }
}
```
```python []
class Solution(object):
    def maxProfit(self, prices):
        if not prices:
            return 0
        n = len(prices)
        # 创建两个数组，一个记录每次卖出的最大收益，一个记录每次买入最大收益
        sell = [0 for _ in xrange(n)]
        buy  = [0 for _ in xrange(n)]
        # 初始化第一天收益
        sell[0] = 0
        buy[0] = -prices[0]
        res = 0
        for i in xrange(1,n):
            # 第i天卖出收益 = max(第i-1天卖出收益，第i-1天买入收益+当天股价)
            sell[i] = max(sell[i-1],buy[i-1]+prices[i])
            # 第i天买入收益 = max(第i-1天买入收益，-当天股价)
            buy[i]  = max(buy[i-1],-prices[i])
            res = max(res,sell[i],buy[i])
        return res	
```
        
            
      
            
## 动态规划-2
这里的实现跟动态规划-1基本上是一样的，只是把两个一维数组合并成一个二维数组   
![8.jpg](https://pic.leetcode-cn.com/7cbefdc48a4389a5e5e428de417a5ec9c651f83b609c599b9c772a6fc2fefb78-8.jpg)


我们用```dp[n][2]```来代替原先的卖出和买入两个数组    
```dp[i][0]```表示第i天卖出的最大收益   
```dp[i][1]```表示第i天买入的最大收益   

计算公式为:   
- dp[i][0] = max(dp[i-1][0],dp[i-1][1]+prices[i])
- dp[i][1] = max(dp[i-1][1],-prices[i])
   
时间复杂度:O(N)       
空间复杂度:O(N)   
   
代码实现:   
```java []
class Solution {
    public int maxProfit(int[] prices) {
        if(prices==null || prices.length==0) {
            return 0;
        }
        int n = prices.length;
        int[][] dp = new int[n][2];
        dp[0][0] = 0;
        dp[0][1] = -prices[0];
        for(int i=1;i<n;++i) {
            //第i天卖出的最大收益  
            dp[i][0] = Math.max(dp[i-1][0],dp[i-1][1]+prices[i]);
            //第i天买入的最大收益  
            dp[i][1] = Math.max(dp[i-1][1],-prices[i]);
        }
        return dp[n-1][0];
    }
}
```
```python []
class Solution(object):
    def maxProfit(self, prices):
        if not prices:
            return 0
        n = len(prices)
        dp = [[0 for _ in xrange(2)] for _ in xrange(n)]
        dp[0][0] = 0
        dp[0][1] = -prices[0]
        for i in xrange(1,n):
            # 第i天卖出的最大收益  
            dp[i][0] = max(dp[i-1][0],dp[i-1][1]+prices[i])
            # 第i天买入的最大收益  
            dp[i][1] = max(dp[i-1][1],-prices[i])
        return dp[-1][0]
```


## 动态规划+空间优化
动态规划的两个版本中我们分别创建了两个一维数组，以及一个二维数组   
根据前面的计算公式我们可以看出，求第i天的买入收益，卖出收益时，我们只需要用到第i-1天的值就可以了   
i-1天之前的值可以忽略掉

我们求第i天卖出收益，只需要知道第i-1天买入收益，第i-1天卖出收益，第i天股价这几个变量就可以了   
求第i天买入收益，只需要知道第i-1天买入收益，第天股价即可   
![9.jpg](https://pic.leetcode-cn.com/f7906aecebd0fd810a8f0a3d440e2a782ae2727c2ebb186d62e86417afbada4c-9.jpg)


i-1天的值又通过i-2天得来，i-2天通过i-3天得来   
所以我们只要初始化好第一天的值，一直用两个变量来往前推算就可以得到最终结果了   
时间复杂度:O(N)       
空间复杂度:O(1)  


代码实现:
```java []
class Solution {
    public int maxProfit(int[] prices) {
        if(prices==null || prices.length==0) {
            return 0;
        }
        int n = prices.length;
        //初始化第一天的值
        int dp0 = 0;
        int dp1 = -prices[0];
        for(int i=1;i<n;++i) {
            //dp0表示第i天卖出的最大收益
            dp0 = Math.max(dp0,dp1+prices[i]);
            //dp1表示第i天买入的最大收益
            dp1 = Math.max(dp1,-prices[i]);
        }
        return Math.max(dp0,dp1);
    }
}
```
```python []
class Solution(object):
    def maxProfit(self, prices):
        if not prices:
            return 0
        n = len(prices)
        # 初始化第一天的值
        dp0 = 0
        dp1 = -prices[0]
        for i in xrange(1,n):
            # dp0表示第i天卖出的最大收益
            dp0 = max(dp0,dp1+prices[i])
            # dp1表示第i天买入的最大收益
            dp1 = max(dp1,-prices[i])
        return max(dp0,dp1)
```
 

## 贪心

我们把价格数组```[7,1,5,3,6,4]```按照高度来展开   
第i天的价格就对应i层的高度   
![10.jpg](https://pic.leetcode-cn.com/023c3e6e9d99cb0e99f2ff8d7ca216676ba9ba63749c1fa2d715f51b7ff972ff-10.jpg)


所谓的最大利润就是找到一个最小值prices[i]，最大值prices[j]，并且下标```i<j```
![11.jpg](https://pic.leetcode-cn.com/dc9b00ff2dc6cdeae11c08211cf7a03ef8c722d910c092408d8d0b8a5b8090eb-11.jpg)

我们遍历一遍数组，不断更新最小值，再计算出max(当前值-最小值) 即为最终结果。 

时间复杂度:O(N)       
空间复杂度:O(1)

代码实现:
```java []
class Solution {
    public int maxProfit(int[] prices) {
        if(prices==null || prices.length==0) {
            return 0;
        }
        int n = prices.length;
        int minVal = Integer.MAX_VALUE;
        int res = 0;
        for(int i : prices) {
            //遍历数组，不断更新最小价格
            //再计算出max(当前值-最小值)
            if(minVal>i) {
                minVal = i;
            }
            int tmp = i-minVal;
            if(tmp>res) {
                res = tmp;
            }
        }
        return res;
    }
}
```
```python []
class Solution(object):
    def maxProfit(self, prices):
        if not prices:
            return 0
        n = len(prices)
        min_val = float("inf")
        res = 0
        for i in xrange(n):
            # 遍历数组，不断更新最小价格
            # 再计算出max(当前值-最小值)
            if min_val>prices[i]:
                min_val = prices[i]
            tmp = prices[i]-min_val
            if tmp>res:
                res = tmp
        return res
```

股票系列文章汇总
| 题目 | 题解 | 难度等级 
|---| --- | --- |
|[买卖股票的最佳时机(交易一次)](https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock/) | [五种实现+图解](https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock/solution/wu-chong-shi-xian-xiang-xi-tu-jie-121-mai-mai-gu-p/) | 简单 |
|[买卖股票的最佳时机2(交易多次)](https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-ii/)| [四种实现+图解](https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-ii/solution/si-chong-shi-xian-xiang-xi-tu-jie-122-mai-mai-gu-p/)| 简单 |
|[买卖股票的最佳时机含手续费](https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/)|[四种实现+图解](https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/solution/si-chong-shi-xian-tu-jie-714-mai-mai-gu-piao-de-zu/)| 中等 |
|[买卖股票的最佳时机含冷冻期](https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/)|[五种实现+图解](https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/solution/wu-chong-jie-fa-xiang-xi-tu-jie-309zui-jia-mai-mai/)| 中等 |
|[买卖股票的最佳时机(交易两次)](https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-iii/)|[五种实现+图解](https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-iii/solution/wu-chong-shi-xian-xiang-xi-tu-jie-123mai-mai-gu-pi/)| 困难 |
|[买卖股票的最佳时机(k次交易)](https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-iv/)|[四种实现+图解](https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-iv/solution/si-chong-jie-fa-tu-jie-188mai-mai-gu-piao-de-zui-j/)| 困难 |

**欢迎关注 👉👉👉  [【公众号】](https://share.weiyun.com/0KDRUnfK) 👈👈👈**   

**如果能再点个赞👍👍 就更感激啦💓💓**
