### 解题思路

- 标签：二分查找
- 如果该题目暴力解决的话需要 *O(n)* 的时间复杂度，但是如果二分的话则可以降低到 *O(logn)* 的时间复杂度
- 整体思路和普通的二分查找几乎没有区别，先设定左侧下标 `left` 和右侧下标 `right`，再计算中间下标 `mid`
- 每次根据 `nums[mid]` 和 `target` 之间的大小进行判断，相等则直接返回下标，`nums[mid] < target` 则 left 右移，`nums[mid] > target` 则 right 左移
- 查找结束如果没有相等值则返回 left，该值为插入位置
- 时间复杂度：*O(logn)*

二分查找的思路不难理解，但是边界条件容易出错，比如 **循环结束条件中 left 和 right 的关系，更新 left 和 right 位置时要不要加 1 减 1**。

下面给出两个可以直接套用的模板，记住就好了，免除边界条件出错。

```Java []
class Solution {
    public int searchInsert(int[] nums, int target) {
        int left = 0, right = nums.length - 1; // 注意
        while(left <= right) { // 注意
            int mid = (left + right) / 2; // 注意
            if(nums[mid] == target) { // 注意
                // 相关逻辑
            } else if(nums[mid] < target) {
                left = mid + 1; // 注意
            } else {
                right = mid - 1; // 注意
            }
        }
        // 相关返回值
        return 0;
    }
}
```


```Java []
class Solution {
    public int searchInsert(int[] nums, int target) {
        int left = 0, right = nums.length; // 注意
        while(left < right) { // 注意
            int mid = (left + right) / 2; // 注意
            if(nums[mid] == target) {
                // 相关逻辑
            } else if(nums[mid] < target) {
                left = mid + 1; // 注意
            } else {
                right = mid; // 注意
            }
        }
        // 相关返回值
        return 0;
    }
}
```

### 代码

```Java []
class Solution {
    public int searchInsert(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while(left <= right) {
            int mid = (left + right) / 2;
            if(nums[mid] == target) {
                return mid;
            } else if(nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }
}
```

### 画解

 ![frame_00001.png](https://pic.leetcode-cn.com/5a0a4dcb1d8a219fa0107277c7913c4d7cce13827135453ec05f4143ac6a6074-frame_00001.png) ![frame_00002.png](https://pic.leetcode-cn.com/3af8dbf7c6fa8709696349cdfd55408a2c6d3bd2ab1bd7f6a4f6f304a961ede9-frame_00002.png) ![frame_00003.png](https://pic.leetcode-cn.com/918d4aac41675880f1a6aea7d01ee7ce462c9200cbc34d748ad2a205dfdf7f88-frame_00003.png) ![frame_00004.png](https://pic.leetcode-cn.com/d9568ad2f55a33524f19b2c8ce57e8459821c630fb9dfbc6b4a27d04c4ddfb60-frame_00004.png) ![frame_00005.png](https://pic.leetcode-cn.com/dfb8df989032749452ad09b9e7b91640bb836cedc8b1317b856511ae663ec019-frame_00005.png) ![frame_00006.png](https://pic.leetcode-cn.com/f1718773b86de8122c05b7ab985c643375e47081546618378aa638e2df0e2514-frame_00006.png) 

想看大鹏画解更多高频面试题，欢迎阅读大鹏的 LeetBook：[《画解剑指 Offer 》](https://leetcode-cn.com/leetbook/detail/illustrate-lcof/)，O(∩_∩)O