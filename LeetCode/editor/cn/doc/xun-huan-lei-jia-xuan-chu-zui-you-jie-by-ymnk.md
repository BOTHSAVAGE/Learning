### 解题思路
此处撰写解题思路
双层循环，从一个开始，从第二个开始。。。。到最后一个
### 代码

```java
class Solution {
    public int maxSubArray(int[] nums) {
        int res = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; i++) {
            int all = 0;
            for (int j = i; j < nums.length; j++) {
                all += nums[j];
                res = Math.max(all, res);
            }
        }
        return res;
    }
}
```