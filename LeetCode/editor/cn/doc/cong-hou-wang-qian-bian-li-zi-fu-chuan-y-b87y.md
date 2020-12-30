```
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
```
