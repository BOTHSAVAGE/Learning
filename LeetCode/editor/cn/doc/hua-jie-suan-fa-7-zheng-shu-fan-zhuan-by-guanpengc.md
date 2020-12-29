### 思路

- 标签：数学
- 本题如果不考虑溢出问题，是非常简单的。解决溢出问题有两个思路，第一个思路是通过字符串转换加`try catch`的方式来解决，第二个思路就是通过数学计算来解决。
- 由于字符串转换的效率较低且使用较多库函数，所以解题方案不考虑该方法，而是通过数学计算来解决。
- 通过循环将数字`x`的每一位拆开，在计算新值时每一步都判断是否溢出。
- 溢出条件有两个，一个是大于整数最大值`MAX_VALUE`，另一个是小于整数最小值`MIN_VALUE`，设当前计算结果为`ans`，下一位为`pop`。
- 从`ans * 10 + pop > MAX_VALUE`这个溢出条件来看
  - 当出现 `ans > MAX_VALUE / 10` 且 `还有pop需要添加` 时，则一定溢出
  - 当出现 `ans == MAX_VALUE / 10` 且 `pop > 7` 时，则一定溢出，`7`是`2^31 - 1`的个位数
- 从`ans * 10 + pop < MIN_VALUE`这个溢出条件来看
  - 当出现 `ans < MIN_VALUE / 10` 且 `还有pop需要添加` 时，则一定溢出
  - 当出现 `ans == MIN_VALUE / 10` 且 `pop < -8` 时，则一定溢出，`8`是`-2^31`的个位数

### 代码

```java
class Solution {
    public int reverse(int x) {
        int ans = 0;
        while (x != 0) {
            int pop = x % 10;
            if (ans > Integer.MAX_VALUE / 10 || (ans == Integer.MAX_VALUE / 10 && pop > 7)) 
                return 0;
            if (ans < Integer.MIN_VALUE / 10 || (ans == Integer.MIN_VALUE / 10 && pop < -8)) 
                return 0;
            ans = ans * 10 + pop;
            x /= 10;
        }
        return ans;
    }
}
```

### 画解

 ![frame_00001.png](https://pic.leetcode-cn.com/b1452fb9e9d44368a402f2d64b3da349bd7c38d8ce44b770c625ef4c23ff8f23-frame_00001.png) ![frame_00002.png](https://pic.leetcode-cn.com/3c56793c0fce22deb6e897c5561a278169eaff054726a1d34ce70e6dba47903b-frame_00002.png) ![frame_00003.png](https://pic.leetcode-cn.com/5afd276b102b1ba658d9998ba8ba2b52705ea98e115ad502fcccc976ba59ee18-frame_00003.png) ![frame_00004.png](https://pic.leetcode-cn.com/b75f704d74328a4e348c62ad6105b4a48571c0291d7028b91b6b960ff85b4a4c-frame_00004.png) 

点击我的头像加关注，和我一起打卡天天算法

想看大鹏画解更多高频面试题，欢迎阅读大鹏的 LeetBook：[《画解剑指 Offer 》](https://leetcode-cn.com/leetbook/detail/illustrate-lcof/)，O(∩_∩)O