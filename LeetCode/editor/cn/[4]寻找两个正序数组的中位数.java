//给定两个大小为 m 和 n 的正序（从小到大）数组 nums1 和 nums2。请你找出并返回这两个正序数组的中位数。 
//
// 进阶：你能设计一个时间复杂度为 O(log (m+n)) 的算法解决此问题吗？ 
//
// 
//
// 示例 1： 
//
// 输入：nums1 = [1,3], nums2 = [2]
//输出：2.00000
//解释：合并数组 = [1,2,3] ，中位数 2
// 
//
// 示例 2： 
//
// 输入：nums1 = [1,2], nums2 = [3,4]
//输出：2.50000
//解释：合并数组 = [1,2,3,4] ，中位数 (2 + 3) / 2 = 2.5
// 
//
// 示例 3： 
//
// 输入：nums1 = [0,0], nums2 = [0,0]
//输出：0.00000
// 
//
// 示例 4： 
//
// 输入：nums1 = [], nums2 = [1]
//输出：1.00000
// 
//
// 示例 5： 
//
// 输入：nums1 = [2], nums2 = []
//输出：2.00000
// 
//
// 
//
// 提示： 
//
// 
// nums1.length == m 
// nums2.length == n 
// 0 <= m <= 1000 
// 0 <= n <= 1000 
// 1 <= m + n <= 2000 
// -106 <= nums1[i], nums2[i] <= 106 
// 
// Related Topics 数组 二分查找 分治算法 
// 👍 3464 👎 0


//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        //初始化最后数组
        int temp12[]=new int [nums1.length+nums2.length];
        //合并
        merge(nums1,nums2,temp12);
        int  s1s2=temp12.length/2;//取中间的索引
        double temp=0.0d;//用来保存找到的值，注意为double
        if(temp12.length %2==0){//偶数个
            double a= temp12[s1s2];
            double b= temp12[s1s2-1];
            double c=2d;
            temp=(a+b)/c;
        }else{//奇数个
            temp=temp12[s1s2];
        }
        return  temp;
    }


    /**
     * 这里才是重点，两个有序合并成一个有序
     * @param nums1
     * @param nums2
     * @param temparr
     */
    public  static void merge(int[] nums1,int [] nums2, int[] temparr) {
        // 标记左半区第一个未排序的下标
        int lpos = 0;
        // 标记右边的第一个区域的第一个区域
        int rpos = 0;
        // 临时数组元素的下标
        int pos = 0;
        // 合并
        while (lpos<nums1.length && rpos<nums2.length) {//把左下标和长度1做对比，右下标和
            if (nums1[lpos] < nums2[rpos]) {
                // 左边第一个区域值更小
                temparr[pos++] = nums1[lpos++];
            } else {
                // 右边的值更小
                temparr[pos++] = nums2[rpos++];
            }
        }
        // 合并最右边的
        while (rpos < nums2.length) {
            temparr[pos++] = nums2[rpos++];
        }
        // 合并最左边的
        while (lpos < nums1.length) {
            temparr[pos++] = nums1[lpos++];
        }

    }
}
//leetcode submit region end(Prohibit modification and deletion)
