#### 概述

这道题是要在 haystack 字符串中找到 needle 字符串。下面会给出的三种解法，这三种解法都基于滑动窗口。

子串逐一比较的解法最简单，将长度为 L 的滑动窗口沿着 haystack 字符串逐步移动，并将窗口内的子串与 needle 字符串相比较，时间复杂度为 *O((N - L)L)*

显示上面这个方法是可以优化的。双指针方法虽然也是线性时间复杂度，不过它可以避免比较所有的子串，因此最优情况下的时间复杂度为 *O(N)*，但最坏情况下的时间复杂度依然为 *O((N - L)L)*。

有 *O(N)* 复杂度的解法嘛？答案是有的，有两种方法可以实现：

- Rabin-Karp，通过哈希算法实现常数时间窗口内字符串比较。

- 比特位操作，通过比特掩码来实现常数时间窗口内字符串比较。

#### 方法一：子串逐一比较 - 线性时间复杂度

最直接的方法 - 沿着字符换逐步移动滑动窗口，将窗口内的子串与 needle 字符串比较。

![fig](https://pic.leetcode-cn.com/Figures/28/substrings.png)

**实现**

```python [solution1-Python]
class Solution:
    def strStr(self, haystack: str, needle: str) -> int:
        L, n = len(needle), len(haystack)

        for start in range(n - L + 1):
            if haystack[start: start + L] == needle:
                return start
        return -1
```

```java [solution1-Java]
class Solution {
  public int strStr(String haystack, String needle) {
    int L = needle.length(), n = haystack.length();

    for (int start = 0; start < n - L + 1; ++start) {
      if (haystack.substring(start, start + L).equals(needle)) {
        return start;
      }
    }
    return -1;
  }
}
```

**复杂度分析**

* 时间复杂度：*O((N - L)L)*，其中 N 为 haystack 字符串的长度，L 为 needle 字符串的长度。内循环中比较字符串的复杂度为 L，总共需要比较 (N - L) 次。 

* 空间复杂度：*O(1)*。

#### 方法二：双指针 - 线性时间复杂度

上一个方法的缺陷是会将 haystack 所有长度为 L 的子串都与 needle 字符串比较，实际上是不需要这么做的。

首先，只有子串的第一个字符跟 needle 字符串第一个字符相同的时候才需要比较。

![fig](https://pic.leetcode-cn.com/Figures/28/two_pointers_start2.png)

其次，可以一个字符一个字符比较，一旦不匹配了就立刻终止。

![fig](https://pic.leetcode-cn.com/Figures/28/mismatch2.png)

如下图所示，比较到最后一位时发现不匹配，这时候开始回溯。需要注意的是，pn 指针是移动到 pn = pn - curr_len + 1 的位置，而 _不是_ pn = pn - curr_len 的位置。

![fig](https://pic.leetcode-cn.com/Figures/28/backtrack2.png)

这时候再比较一次，就找到了完整匹配的子串，直接返回子串的开始位置 pn - L。 

![fig](https://pic.leetcode-cn.com/Figures/28/match.png)

**算法**

- 移动 pn 指针，直到 pn 所指向位置的字符与 needle 字符串第一个字符相等。

- 通过 pn，pL，curr_len 计算匹配长度。

- 如果完全匹配（即 curr_len == L），返回匹配子串的起始坐标（即 pn - L）。

- 如果不完全匹配，回溯。使 pn = pn - curr_len + 1， pL = 0， curr_len = 0。

**实现**

```python [solution2-Python]
class Solution:
    def strStr(self, haystack: str, needle: str) -> int:
        L, n = len(needle), len(haystack)
        if L == 0:
            return 0

        pn = 0
        while pn < n - L + 1:
            # find the position of the first needle character
            # in the haystack string
            while pn < n - L + 1 and haystack[pn] != needle[0]:
                pn += 1
            
            # compute the max match string
            curr_len = pL = 0
            while pL < L and pn < n and haystack[pn] == needle[pL]:
                pn += 1
                pL += 1
                curr_len += 1
            
            # if the whole needle string is found,
            # return its start position
            if curr_len == L:
                return pn - L
            
            # otherwise, backtrack
            pn = pn - curr_len + 1
            
        return -1
```

```java [solution2-Java]
class Solution {
  public int strStr(String haystack, String needle) {
    int L = needle.length(), n = haystack.length();
    if (L == 0) return 0;

    int pn = 0;
    while (pn < n - L + 1) {
      // find the position of the first needle character
      // in the haystack string
      while (pn < n - L + 1 && haystack.charAt(pn) != needle.charAt(0)) ++pn;

      // compute the max match string
      int currLen = 0, pL = 0;
      while (pL < L && pn < n && haystack.charAt(pn) == needle.charAt(pL)) {
        ++pn;
        ++pL;
        ++currLen;
      }

      // if the whole needle string is found,
      // return its start position
      if (currLen == L) return pn - L;

      // otherwise, backtrack
      pn = pn - currLen + 1;
    }
    return -1;
  }
}
```

**复杂度分析**

* 时间复杂度：最坏时间复杂度为 *O((N - L)L)*，最优时间复杂度为 *O(N)*。

* 空间复杂度：*O(1)*。

#### 方法三： Rabin Karp - 常数复杂度

有一种最坏时间复杂度也为 *O(N)* 的算法。思路是这样的，先生成窗口内子串的哈希码，然后再跟 needle 字符串的哈希码做比较。

这个思路有一个问题需要解决，如何在常数时间生成子串的哈希码？

**滚动哈希：常数时间生成哈希码**

生成一个长度为 L 数组的哈希码，需要 *O(L)* 时间。

> 如何在常数时间生成滑动窗口数组的哈希码？利用滑动窗口的特性，每次滑动都有一个元素进，一个出。 

由于只会出现小写的英文字母，因此可以将字符串转化成值为 0 到 25 的整数数组： `arr[i] = (int)S.charAt(i) - (int)'a'`。按照这种规则，`abcd` 整数数组形式就是 `[0, 1, 2, 3]`，转换公式如下所示。

![h_0=0\times26^3+1\times26^2+2\times26^1+3\times26^0 ](./p___h_0_=_0_times_26^3_+_1_times_26^2_+_2_times_26^1_+_3_times_26^0__.png) 

可以将上面的公式写成通式，如下所示。其中 *c_i* 为整数数组中的元素，*a = 26*，其为字符集的个数。

*
h_0 = c_0 a^{L - 1} + c_1 a^{L - 2} + ... + c_i a^{L - 1 - i} + ... + c_{L - 1} a^1 + c_L a^0
*

![h_0=\sum_{i=0}^{L-1}{c_ia^{L-1-i}} ](./p___h_0_=_sum_{i_=_0}^{L_-_1}{c_i_a^{L_-_1_-_i}}__.png) 

下面来考虑窗口从 `abcd` 滑动到 `bcde` 的情况。这时候整数形式数组从 `[0, 1, 2, 3]` 变成了 `[1, 2, 3, 4]`，数组最左边的 0 被移除，同时最右边新添了 4。滑动后数组的哈希值可以根据滑动前数组的哈希值来计算，计算公式如下所示。

![h_1=(h_0-0\times26^3)\times26+4\times26^0 ](./p___h_1_=__h_0_-_0_times_26^3__times_26_+_4_times_26^0__.png) 

写成通式如下所示。

*
h_1 = (h_0 a - c_0 a^L) + c_{L + 1}
*

**如何避免溢出**

*a^L* 可能是一个很大的数字，因此需要设置数值上限来避免溢出。设置数值上限可以用取模的方式，即用 `h % modulus` 来代替原本的哈希值。

理论上，modules 应该取一个很大数，但具体应该取多大的数呢? [详见这篇文章](https://en.wikipedia.org/wiki/Linear_congruential_generator#Parameters_in_common_use)，对于这个问题来说 *2^{31}* 就足够了。

**算法**

- 计算子字符串 `haystack.substring(0, L)` 和 `needle.substring(0, L)` 的哈希值。

- 从起始位置开始遍历：从第一个字符遍历到第 N - L 个字符。

    - 根据前一个哈希值计算滚动哈希。

    - 如果子字符串哈希值与 needle 字符串哈希值相等，返回滑动窗口起始位置。
    
- 返回 -1，这时候 haystack 字符串中不存在 needle 字符串。

**实现**

```python [solution3-Python]
class Solution:
    def strStr(self, haystack: str, needle: str) -> int:
        L, n = len(needle), len(haystack)
        if L > n:
            return -1
        
        # base value for the rolling hash function
        a = 26
        # modulus value for the rolling hash function to avoid overflow
        modulus = 2**31
        
        # lambda-function to convert character to integer
        h_to_int = lambda i : ord(haystack[i]) - ord('a')
        needle_to_int = lambda i : ord(needle[i]) - ord('a')
        
        # compute the hash of strings haystack[:L], needle[:L]
        h = ref_h = 0
        for i in range(L):
            h = (h * a + h_to_int(i)) % modulus
            ref_h = (ref_h * a + needle_to_int(i)) % modulus
        if h == ref_h:
            return 0
              
        # const value to be used often : a**L % modulus
        aL = pow(a, L, modulus) 
        for start in range(1, n - L + 1):
            # compute rolling hash in O(1) time
            h = (h * a - h_to_int(start - 1) * aL + h_to_int(start + L - 1)) % modulus
            if h == ref_h:
                return start

        return -1
```

```java [solution3-Java]
class Solution {
  // function to convert character to integer
  public int charToInt(int idx, String s) {
    return (int)s.charAt(idx) - (int)'a';
  }

  public int strStr(String haystack, String needle) {
    int L = needle.length(), n = haystack.length();
    if (L > n) return -1;

    // base value for the rolling hash function
    int a = 26;
    // modulus value for the rolling hash function to avoid overflow
    long modulus = (long)Math.pow(2, 31);

    // compute the hash of strings haystack[:L], needle[:L]
    long h = 0, ref_h = 0;
    for (int i = 0; i < L; ++i) {
      h = (h * a + charToInt(i, haystack)) % modulus;
      ref_h = (ref_h * a + charToInt(i, needle)) % modulus;
    }
    if (h == ref_h) return 0;

    // const value to be used often : a**L % modulus
    long aL = 1;
    for (int i = 1; i <= L; ++i) aL = (aL * a) % modulus;

    for (int start = 1; start < n - L + 1; ++start) {
      // compute rolling hash in O(1) time
      h = (h * a - charToInt(start - 1, haystack) * aL
              + charToInt(start + L - 1, haystack)) % modulus;
      if (h == ref_h) return start;
    }
    return -1;
  }
}
```

**复杂度分析**

* 时间复杂度：*O(N)*，计算 needle 字符串的哈希值需要 *O(L)* 时间，之后需要执行 *(N - L)* 次循环，每次循环的计算复杂度为常数。

* 空间复杂度：*O(1)*。