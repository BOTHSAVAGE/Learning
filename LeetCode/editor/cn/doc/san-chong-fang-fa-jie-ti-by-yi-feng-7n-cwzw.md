### 解题思路
此处撰写解题思路
三种方法解题
方法一：暴力解双层循环比较最优解getMaxSubMine
方法二：动态规划找规律总结公式f(i) = Math.max(f(i-1)+nums[i],nums[i])-》getMaxSubDp
方法三：分治算法，分成无数小段来解析数组getMaxSubFz
### 代码

```java
class Solution {
    public int maxSubArray(int[] nums) {
        return getMaxSubFz(nums);
    }

     /**
     * 双层循环暴力解
     *
     * @param nums
     * @return
     */
    private int getMaxSubMine(int[] nums) {
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

     /**
     * f(i)表示第i个为结尾的子数组的最大和
     * f(i) = Math.max(f(i-1)+nums[i],nums[i])
     *
     * @param nums
     * @return
     */
    private int getMaxSubDp(int[] nums) {
        int fn = 0;
        int res = nums[0];
        for (int i = 0; i < nums.length; i++) {
            fn = Math.max(fn + nums[i], nums[i]);
            res = Math.max(fn, res);
        }
        return res;
    }

     /**
     * 分治学习
     *
     * @param nums
     * @return
     */
    private int getMaxSubFz(int[] nums) {
        int statusm = getInfo(nums, 0, nums.length - 1).statusm;
        return statusm;
    }

    public class Status {
        int statusl; // 已左为端点的子集最大和

        int statusr; // 已右为端点的子集最大和

        int statusm; // 区间内子集最大和

        int statusa; // 区间内元素的和

        public Status(int statusl, int statusr, int statusm, int statusa) {
            this.statusl = statusl;
            this.statusr = statusr;
            this.statusm = statusm;
            this.statusa = statusa;
        }
    }

    private Status getInfo(int[] nums, int st, int en) {
        if (st == en) {
            return new Status(nums[st], nums[st], nums[st], nums[st]);
        }
        // 取中间index
        int m = (st + en) >> 1;
        Status statusl = getInfo(nums, st, m);
        Status statusr = getInfo(nums, m + 1, en);
        Status pubush = pubush(statusl, statusr);
        return pubush;
    }

    private Status pubush(Status statusl, Status statusr) {
        int statusl1 = Math.max(statusl.statusl, statusl.statusa + statusr.statusl);
        int statusr1 = Math.max(statusr.statusr, statusr.statusa + statusl.statusr);
        int statusm = Math.max(Math.max(statusl.statusm, statusr.statusm), statusl.statusr + statusr.statusl);
        int statusa = statusl.statusa + statusr.statusa;
        return new Status(statusl1, statusr1, statusm, statusa);
    }
}
```