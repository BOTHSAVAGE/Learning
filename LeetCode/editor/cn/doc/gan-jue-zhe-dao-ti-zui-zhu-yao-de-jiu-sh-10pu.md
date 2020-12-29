```
class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int [] temp12=new int [nums1.length+nums2.length];
        merge(nums1,nums2,temp12);
        int  s1s2=temp12.length/2;
        double temp=0.0d;
        if(temp12.length %2==0){
           double a= temp12[s1s2];
           double b= temp12[s1s2-1];
           double c=2d;
            temp=(a+b)/c;
        }else{
            temp=temp12[s1s2];
        }
        return  temp;
    }
     public  static void merge(int[] nums1,int [] nums2, int[] temparr) {
        // 标记左半区第一个未排序的下标
        int lpos = 0;
        // 标记右边的第一个区域的第一个区域
        int rpos = 0;
        // 临时数组元素的下标
        int pos = 0;
        // 合并
        while (lpos<nums1.length && rpos<nums2.length) {
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
```
