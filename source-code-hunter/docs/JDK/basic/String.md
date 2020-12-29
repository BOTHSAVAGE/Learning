String 的源码大家应该都能看懂，这里就不一一分析咯，重点讲一下 equals()和 hashcode()方法，然后看一下 String 类常用方法的实现，就当一起温习一下咯。

```java
public final class String
    implements java.io.Serializable, Comparable<String>, CharSequence {

    /** 保存String的字节数组（String底层其实就是char数组）*/
    private final char value[];

    /** 缓存这个String的hash值 （hash值的目的是为了比较是否完全相同）*/
    private int hash; // Default to 0

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = -6849794470754667710L;

    /**
     * 1、Object的 hashCode()返回该对象的内存地址编号，而equals()比较的是内存地址是否相等；（就是看hashcode是否相等）
     * 2、需要注意的是当equals()方法被重写时，hashCode()也要被重写；
     * 3、按照一般hashCode()方法的实现来说，equals()相等的两个对象，hashcode()必须保持相等；
     *    equals()不相等的两个对象，hashcode()未必不相等
     * 4、一个类如果要作为 HashMap 的 key，必须重写equals()和hashCode()方法（这里就涉及到hashmap重复校验)
     */
    public boolean equals(Object anObject) {
        //1.第一步判断是否和当前this指针相等
        if (this == anObject) {
            return true;
        }
        //2.判断类型
        if (anObject instanceof String) {
            //2.1入参是父类，所以强转
            String anotherString = (String)anObject;
            //2.2获取this的char数组的长度
            int n = value.length;
            //2.3判断长度一样不
            if (n == anotherString.value.length) {
                //2.3.1 长度一样逐一比对
                char v1[] = value;
                char v2[] = anotherString.value;
                int i = 0;
                while (n-- != 0) {
                    if (v1[i] != v2[i])
                        return false;
                    i++;
                }
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        //1.从当前缓存获取hash值
        int h = hash;
        if (h == 0 && value.length > 0) {
            char val[] = value;
            //2.遍历字符串长度，然后31*缓存的hash值 再 加上char
            for (int i = 0; i < value.length; i++) {
                h = 31 * h + val[i];
            }
            hash = h;
        }
        return h;
    }

    /**
     * 指定下标的char
     */
    public char charAt(int index) {
        //不能像python一样倒着取
        if ((index < 0) || (index >= value.length)) {
            throw new StringIndexOutOfBoundsException(index);
        }
        return value[index];
    }

    /**
     * 是否以 prefix 为前缀
     */
    public boolean startsWith(String prefix) {
        //这里应该有一个重载，0表示从开头开始匹配
        return startsWith(prefix, 0);
    }

    /**
     * 是否以 suffix 为后缀
     */
    public boolean endsWith(String suffix) {
        //这里调用的是startsWith，然后传入后面的索引
        return startsWith(suffix, value.length - suffix.value.length);
    }

    /**
     * 该String对象 是否满足 regex正则表达式
     */
    public boolean matches(String regex) {
        return Pattern.matches(regex, this);
    }

    /**
     * 字符替换
     * 
     * 疑问：
     * 如果旧的字符串有重复的char
     */
    public String replace(char oldChar, char newChar) {
        //判断是否相等
        if (oldChar != newChar) {
            //获取当前string的长度
            int len = value.length;
            int i = -1;
            //在一个方法中需要大量引用实例域变量的时候，使用方法中的局部变量代替引用可以减少getfield操作的次数，提高性能。
            char[] val = value; /* avoid getfield opcode */
            while (++i < len) {//todo 把循环控制，循环条件放在一起
                //找到oldchar的索引
                if (val[i] == oldChar) {
                    break;
                }
            }
            if (i < len) {
                //再创建一个新的字符数组
                char buf[] = new char[len];
                //把index之前的string的字符数组全部拷贝过去
                for (int j = 0; j < i; j++) {
                    buf[j] = val[j];
                }
                while (i < len) {
                    //逐个查找
                    char c = val[i];
                    //如果相同就直接替代
                    buf[i] = (c == oldChar) ? newChar : c;
                    i++;
                }
                //调用构造方法返回一个新的
                return new String(buf, true);
            }
        }
        return this;
    }

    /**
     * 子串替换
     */
    public String replaceAll(String regex, String replacement) {
        //这里是有用重载吗？
        return Pattern.compile(regex).matcher(this).replaceAll(replacement);
    }

    /**
     * 子串替换，只替换第一个
     */
    public String replaceFirst(String regex, String replacement) {
        //这里是有用重载吗？
        return Pattern.compile(regex).matcher(this).replaceFirst(replacement);
    }

    /**
     * 按 regex 切割成多个子串
     */
    public String[] split(String regex) {
        return split(regex, 0);
    }

    /**
     * 剪切指定范围的字符串
     */
    public String substring(int beginIndex) {
        //进来就做校验，左边界
        if (beginIndex < 0) {
            throw new StringIndexOutOfBoundsException(beginIndex);
        }
        //进来就做校验，右边界
        int subLen = value.length - beginIndex;
        if (subLen < 0) {
            throw new StringIndexOutOfBoundsException(subLen);
        }
        //三目运算调用构造
        return (beginIndex == 0) ? this : new String(value, beginIndex, subLen);
    }

    public String substring(int beginIndex, int endIndex) {
        //进来就校验
        if (beginIndex < 0) {
            throw new StringIndexOutOfBoundsException(beginIndex);
        }
        if (endIndex > value.length) {
            throw new StringIndexOutOfBoundsException(endIndex);
        }
        int subLen = endIndex - beginIndex;
        if (subLen < 0) {
            throw new StringIndexOutOfBoundsException(subLen);
        }
        //三目运算返回构造
        return ((beginIndex == 0) && (endIndex == value.length)) ? this
                : new String(value, beginIndex, subLen);
    }

    /**
     * 获取该String 对应的 char[]
     */
    public char[] toCharArray() {
        // Cannot use Arrays.copyOf because of class initialization order issues
        char result[] = new char[value.length];
        //虽然String 和Arrays 都属于rt.jar中的类，但是BootstrapClassloader 在加载这两个类的顺序是不同的。所以当String.class被加载进内存的时候,Arrays此时没有被加载，所以直接使用肯定会抛异常。而System.arrayCopy是使用native代码，则不会有这个问题。
        System.arraycopy(value, 0, result, 0, value.length);
        return result;
    }

    /**
     * 大小写转换
     */
    public String toLowerCase() {
        //这里应该使用调用的naive
        return toLowerCase(Locale.getDefault());
    }
    public String toUpperCase() {
        //这里应该使用调用的naive
        return toUpperCase(Locale.getDefault());
    }

    /**
     * str在本String对象中第一次出现的下标
     */
    public int indexOf(String str) {
        return indexOf(str, 0);
    }

    /**
     * str在本String对象中最后一次出现的下标
     */
    public int lastIndexOf(String str) {
        return lastIndexOf(str, value.length);
    }
}
```
