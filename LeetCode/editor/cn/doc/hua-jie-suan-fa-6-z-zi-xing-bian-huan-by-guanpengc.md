### 思路

- 标签：字符串
- 整体的思路是遍历字符串，遍历过程中将每行都看成新的字符串构成字符串数组，最后再将该数组拼接起来即可
- 如果 *numRows=1* 则说明当前字符串即为结果，直接返回
- 否则整个字符串需要经历，向下向右，向下向右，这样的反复循环过程，设定 *down* 变量表示是否向下，*loc* 变量表示当前字符串数组的下标
- 如果 *down* 为 *true*，则 *loc+=1*，字符串数组下标向后移动，将当前字符加入当前字符串中
- 如果 *down* 为 *false*，则表示向右，则 *loc-=1*，字符串数组下标向前移动，将当前字符加入当前字符串中
- 时间复杂度：*O(n)*，*n*为字符串s的长度

### 代码

```java []
class Solution {
    public String convert(String s, int numRows) {
        if(numRows == 1)
            return s;
        
        int len = Math.min(s.length(), numRows);
        String []rows = new String[len];
        for(int i = 0; i< len; i++) rows[i] = "";
        int loc = 0;
        boolean down = false;

        for(int i=0;i<s.length();i++) {
            rows[loc] += s.substring(i,i+1);
            if(loc == 0 || loc == numRows - 1)
                down = !down;
            loc += down ? 1 : -1;
        }
        
        String ans = "";
        for(String row : rows) {
            ans += row;
        }
        return ans;
    }
}
```
```javascript []
/**
 * @param {string} s
 * @param {number} numRows
 * @return {string}
 */
var convert = function(s, numRows) {
    if(numRows == 1)
        return s;

    const len = Math.min(s.length, numRows);
    const rows = [];
    for(let i = 0; i< len; i++) rows[i] = "";
    let loc = 0;
    let down = false;

    for(const c of s) {
        rows[loc] += c;
        if(loc == 0 || loc == numRows - 1)
            down = !down;
        loc += down ? 1 : -1;
    }

    let ans = "";
    for(const row of rows) {
        ans += row;
    }
    return ans;
};
```

### 画解


 ![frame_00001.png](https://pic.leetcode-cn.com/c6404badf766aceaf55cf3639b56e29b88d4a39417f3eee0a1b7ca2db9acc52f-frame_00001.png) ![frame_00002.png](https://pic.leetcode-cn.com/96aae93ad1194374acc94d7180523d5686422803c16614cfc773b1141aae6cd5-frame_00002.png) ![frame_00003.png](https://pic.leetcode-cn.com/910a298171a0ee7b7b999c20ba3fc183c438d6cf24e1ada15c3d57f5676ebf05-frame_00003.png) ![frame_00004.png](https://pic.leetcode-cn.com/b5487a78e0825899e28f52252a214399f1e98a7d65815f37c3ab6f01f38361cc-frame_00004.png) ![frame_00005.png](https://pic.leetcode-cn.com/657c0447980d912bf08d05bd254d64bfa64abf5c39f99f171f636abe0291647a-frame_00005.png) ![frame_00006.png](https://pic.leetcode-cn.com/37f06115b2dfe2d2b79c415f0846e9c6ac2743cf6bfc386ca404624fbacc635e-frame_00006.png) ![frame_00007.png](https://pic.leetcode-cn.com/a47246a12513fca7746d81917ffe7de4d7305cc2ed01f811903976f29bb69296-frame_00007.png) ![frame_00008.png](https://pic.leetcode-cn.com/ba05cd664682ab5254cdb3fc0464099647c1c7c77e430905afc7b10bcc517feb-frame_00008.png) ![frame_00009.png](https://pic.leetcode-cn.com/dfba4a3c476550e54e7be51d42759629600e26228cd4991f2a4ab5cd5fe6ac7d-frame_00009.png) ![frame_00010.png](https://pic.leetcode-cn.com/45a5966034fd3d5141569d5fcdc3a3f6943f029aee2dd3cd419eccaafc3bac02-frame_00010.png) 


想看大鹏画解更多高频面试题，欢迎阅读大鹏的 LeetBook：[《画解剑指 Offer 》](https://leetcode-cn.com/leetbook/detail/illustrate-lcof/)，O(∩_∩)O