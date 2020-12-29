### 解题思路
使用StringBuilder来操作字符串，效率提高了将近9倍！附上图片说明！

![image.png](https://pic.leetcode-cn.com/85ce06e628eef48e8ebaa33b27c9653762b28cd63505da41668af9877a0f337c-image.png)

### 代码

```java
class Solution {
    public String countAndSay(int n) {
                StringBuilder s = new StringBuilder();
        int p1 = 0;
        int cur = 1;
        if ( n == 1 )
            return "1";
        String str = countAndSay(n - 1);
        for ( cur = 1; cur < str.length(); cur++ ) {
            if ( str.charAt(p1) != str.charAt(cur) ) {// 如果碰到当前字符与前面紧邻的字符不等则更新此次结果
                int count = cur - p1;
                s.append(count).append(str.charAt(p1));
                p1 = cur;
            }
        }
        if ( p1 != cur ){// 防止最后一段数相等，如果不等说明p1到cur-1这段字符串是相等的
            int count = cur - p1;
            s.append(count).append(str.charAt(p1));
        }
        return s.toString();
    }
}
```
使用String操作的代码
![1.png](https://pic.leetcode-cn.com/4475d23193296c3490ad688e7c42f2dd701872d004eacbdfba9f40034301120f-1.png)

```java
class Solution {
    public String countAndSay(int n) {
        String s = "";
        int p1 = 0;
        int cur = 1;
        if ( n == 1 )
            return "1";
        String str = countAndSay(n - 1);
        for ( cur = 1; cur < str.length(); cur++ ) {
            if ( str.charAt(p1) != str.charAt(cur) ) {
                int count = cur - p1;
                s = s + count + ""+str.charAt(p1);
                p1 = cur;
            }
        }
        if ( p1 != cur ){
            int count = cur - p1;
            s = s + count +""+ str.charAt(p1);
        }
        return s;
    }
}
```