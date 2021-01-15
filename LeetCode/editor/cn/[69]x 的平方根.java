//实现 int sqrt(int x) 函数。 
//
// 计算并返回 x 的平方根，其中 x 是非负整数。 
//
// 由于返回类型是整数，结果只保留整数的部分，小数部分将被舍去。 
//
// 示例 1: 
//
// 输入: 4
//输出: 2
// 
//
// 示例 2: 
//
// 输入: 8
//输出: 2
//说明: 8 的平方根是 2.82842..., 
//     由于返回类型是整数，小数部分将被舍去。
// 
// Related Topics 数学 二分查找 
// 👍 571 👎 0


//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    //使用牛顿迭代法，牛顿迭代法也是暴力法，只不过收敛更快
    int s;

    public int mySqrt(int x) {
        s=x;//随便取一个数
        if(x==0) return 0;//判断异常情况
        return ((int)(sqrts(x)));
    }

    public double sqrts(double x){
        double res = (x + s / x) / 2;
        if (res == x) {
            return x;
        } else {
            return sqrts(res);
        }
    }
}
//leetcode submit region end(Prohibit modification and deletion)
