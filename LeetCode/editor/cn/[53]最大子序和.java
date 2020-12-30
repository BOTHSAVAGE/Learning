//给定一个整数数组 nums ，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。 
//
// 示例: 
//
// 输入: [-2,1,-3,4,-1,2,1,-5,4]
//输出: 6
//解释: 连续子数组 [4,-1,2,1] 的和最大，为 6。
// 
//
// 进阶: 
//
// 如果你已经实现复杂度为 O(n) 的解法，尝试使用更为精妙的分治法求解。 
// Related Topics 数组 分治算法 动态规划 
// 👍 2764 👎 0


//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    //双层循环，第一层遍历nums中每一个，然后第二层遍历就从当前i开始，用 Math.max取最大
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
//leetcode submit region end(Prohibit modification and deletion)
