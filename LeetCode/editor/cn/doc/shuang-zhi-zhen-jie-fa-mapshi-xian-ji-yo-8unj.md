```
public int lengthOfLongestSubstring(String s) {
        char[] chars = s.toCharArray();
        // 存储重复出现的下标
        HashMap<Character, Integer> map = new HashMap<>();
        // 以当前位置结尾的不重复最长子串最前索引
        int left = 0;
        int max = 0;
        for (int i = 0; i < chars.length; i++) {
            // 当出现了重复的字符串时，left要从上一次出现的字符的后面一位开始，保证i和left之间没有重复的字符
            if (map.containsKey(chars[i])) {
                left = Math.max(left, map.get(chars[i]) + 1);
            }
            map.put(chars[i], i);
            max = Math.max(max, i - left + 1);
        }
        return max;
    }
```
用数组优化：
```
public int lengthOfLongestSubstring(String s) {
        int[] ints = new int[128];
		Arrays.fill(ints, -1);
        // 以当前位置结尾的不重复最长子串的索引
        int left = 0;
        int max = 0;
        for (int i = 0; i < s.length(); i++) {
            left = Math.max(left, ints[s.charAt(i)] + 1);
            max = Math.max(max, i - left + 1);
			ints[s.charAt(i)] = i;
        }
        return max;
    }
```
