//给定一个仅包含大小写字母和空格 ' ' 的字符串 s，返回其最后一个单词的长度。如果字符串从左向右滚动显示，那么最后一个单词就是最后出现的单词。 
//
// 如果不存在最后一个单词，请返回 0 。 
//
// 说明：一个单词是指仅由字母组成、不包含任何空格字符的 最大子字符串。 
//
// 
//
// 示例: 
//
// 输入: "Hello World"
//输出: 5
// 
// Related Topics 字符串 
// 👍 261 👎 0


//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    /**
     * 数组嘛，直接从后往前遍历，直接开始计数就完了
     * @param s
     * @return
     */
    public int lengthOfLastWord(String s) {
        /**
         * 从后往前遍历字符串，遇到空格只有两种情况：
         * 1、开始计数
         * 2、结束计数
         */
        int count = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == ' ') {
                if (count == 0) {
                    // 跳过末尾的空格
                    continue;
                } else {
                    // 结束统计
                    break;
                }
            }
            count++;
        }
        return count;
    }
}
//leetcode submit region end(Prohibit modification and deletion)
