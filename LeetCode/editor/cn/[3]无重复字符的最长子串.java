//给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。 
//
// 
//
// 示例 1: 
//
// 
//输入: s = "abcabcbb"
//输出: 3 
//解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
// 
//
// 示例 2: 
//
// 
//输入: s = "bbbbb"
//输出: 1
//解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
// 
//
// 示例 3: 
//
// 
//输入: s = "pwwkew"
//输出: 3
//解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
//     请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
// 
//
// 示例 4: 
//
// 
//输入: s = ""
//输出: 0
// 
//
// 
//
// 提示： 
//
// 
// 0 <= s.length <= 5 * 104 
// s 由英文字母、数字、符号和空格组成 
// 
// Related Topics 哈希表 双指针 字符串 Sliding Window 
// 👍 4756 👎 0


//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    //双指针 解法
    public int lengthOfLongestSubstring(String s) {
        //转为字符串数组
        char[] chars = s.toCharArray();
        // 存储重复出现的下标
        HashMap<Character, Integer> map = new HashMap<>();
        // 以当前位置结尾的不重复最长子串最前索引
        int left = 0;//从0开始遍历
        int max = 0;//当前获取的最大字符串的长度
        for (int i = 0; i < chars.length; i++) {
            // 当出现了重复的字符串时，left要从上一次出现的字符的后面一位开始，保证i和left之间没有重复的字符
            if (map.containsKey(chars[i])) {//todo 这里来的话直接走判断，遍历语句都放在后面
                left = Math.max(left, map.get(chars[i]) + 1);
            }
            //把当前遍历过的字符放入map中
            map.put(chars[i], i);
            //获取最大
            max = Math.max(max, i - left + 1);
        }
        return max;
    }
}
//leetcode submit region end(Prohibit modification and deletion)
