## 简介

* RESTful web接口 
* Java开发
* 搜索，分析，存储一体
* 竞争对手solar也是基于Lucene 

> 搜索引擎步骤：爬虫，分词，索引
>
> * 反向索引又叫倒排索引，是根据文章内容中的关键字建立索引。
> * 搜索引擎原理就是建立反向索引。
> * Elasticsearch 中的索引、类型和文档的概念比较重要，类似于 MySQL 中的数据库、表和行。
> * Elasticsearch 也是 Master-slave 架构，也实现了数据的分片和备份。
> * Elasticsearch 一个典型应用就是 ELK 日志分析系统。

## 索引

### B-Tree索引

二叉树查找效率是logN，同时插入新的节点不必移动全部节点，兼顾了插入和查询的性能。再结合磁盘的读取特性(顺序读/随机读)，传统关系型数据库采用了B-Tree/B+Tree这样的数据结构。为了提高查询的效率，减少磁盘寻道次数，将多个值作为一个数组通过连续区间存放，一次寻道读取多个数据，同时也降低树的高度。

https://pic4.zhimg.com/v2-05b6af61dcf19b8208ec0ee1f85b9e2f_b.jpg 

### 倒排索引

https://pic2.zhimg.com/v2-43a962b238dec71f0e63c124aa2b7039_b.jpg

Elasticsearch为每一个field(mysql单元格)都建立了倒排索引，[行号1，行号2，行号3]就称为Posting List（int 数组），为了快速找到term（mysql表），就有了term Dictionary。上面过程和B+类似，但是比B+快。B-Tree通过减少磁盘寻道次数来提高查询性能，Elasticsearch也是采用同样的思路，直接通过内存查找term，不读磁盘，但是如果term太多，term dictionary也会很大，放内存不现实，于是有了**Term Index**，就像字典里的索引页一样，A开头的有哪些term，分别在哪页，可以理解term index是一颗树：

https://pic3.zhimg.com/v2-46df13a213316d1085040da8774e4f3a_b.jpg

这棵树不会包含所有的term，它包含的是term的一些前缀。通过term index可以快速地定位到term dictionary的某个offset，然后从这个位置再往后顺序查找。 

https://pic1.zhimg.com/v2-d8745df4b88a9f386a70d41440937460_b.jpg

所以term index不需要存下所有的term，而仅仅是他们的一些前缀与Term Dictionary的block之间的映射关系，再结合FST(Finite State Transducers)的压缩技术，可以使term index缓存到内存中。从term index查到对应的term dictionary的block位置之后，再去磁盘上找term，大大减少了磁盘随机读的次数。

 https://www.cnblogs.com/jiu0821/p/7688669.html  -> fst

 https://cs.nyu.edu/~mohri/pub/fla.pdf 论文

 **压缩技巧**

Elasticsearch里除了上面说到用FST压缩term index外，对posting list也有压缩技巧。 
小明喝完咖啡又举手了:"posting list不是已经只存储文档id了吗？还需要压缩？"

嗯，我们再看回最开始的例子，如果Elasticsearch需要对同学的性别进行索引(这时传统关系型数据库已经哭晕在厕所……)，会怎样？如果有上千万个同学，而世界上只有男/女这样两个性别，每个posting list都会有至少百万个文档id。 Elasticsearch是如何有效的对这些文档id压缩的呢？

### **Frame Of Reference**

> 增量编码压缩，将大数变小数，按字节存储

首先，Elasticsearch要求posting list是有序的(为了提高搜索的性能，再任性的要求也得满足)，这样做的一个好处是方便压缩，看下面这个图例：

 https://pic1.zhimg.com/v2-6ba6af34e9c97ea9fcca5a6cddf9ac64_b.jpg

 原理就是通过增量，将原来的大数变成小数仅存储增量值，再精打细算按bit排好队，最后通过字节存储，而不是大大咧咧的尽管是2也是用int(4个字节)来存储。 

 

 

 

 

 

### **Roaring bitmaps**

说到Roaring bitmaps，就必须先从bitmap说起。Bitmap是一种数据结构，假设有某个posting list：

[1,3,4,7,10]

对应的bitmap就是：

[1,0,1,1,0,0,1,0,0,1]

非常直观，用0/1表示某个值是否存在，比如10这个值就对应第10位，对应的bit值是1，这样用一个字节就可以代表8个文档id，旧版本(5.0之前)的Lucene就是用这样的方式来压缩的，但这样的压缩方式仍然不够高效，如果有1亿个文档，那么需要12.5MB的存储空间，这仅仅是对应一个索引字段(我们往往会有很多个索引字段)。于是有人想出了Roaring bitmaps这样更高效的数据结构。

Bitmap的缺点是存储空间随着文档个数线性增长，Roaring bitmaps需要打破这个魔咒就一定要用到某些指数特性：

将posting list按照65535为界限分块，比如第一块所包含的文档id范围在0~65535之间，第二块的id范围是65536~131071，以此类推。再用<商，余数>的组合表示每一组id，这样每组里的id范围都在0~65535内了，剩下的就好办了，既然每组id不会变得无限大，那么我们就可以通过最有效的方式对这里的id存储。

![img](https://pic1.zhimg.com/v2-fdd617f17fc277d4cfe6b89d1946bc08_b.jpg)

细心的小明这时候又举手了:"为什么是以65535为界限?"

程序员的世界里除了1024外，65535也是一个经典值，因为它=2^16-1，正好是用2个字节能表示的最大数，一个short的存储单位，注意到上图里的最后一行“If a block has more than 4096 values, encode as a bit set, and otherwise as a simple array using 2 bytes per value”，如果是大块，用节省点用bitset存，小块就豪爽点，2个字节我也不计较了，用一个short[]存着方便。

那为什么用4096来区分大块还是小块呢？

个人理解：都说程序员的世界是二进制的，4096*2bytes ＝ 8192bytes < 1KB, 磁盘一次寻道可以顺序把一个小块的内容都读出来，再大一位就超过1KB了，需要两次读。

### **联合索引**

上面说了半天都是单field索引，如果多个field索引的联合查询，倒排索引如何满足快速查询的要求呢？

- 利用跳表(Skip list)的数据结构快速做“与”运算，或者
- 利用上面提到的bitset按位“与”

先看看跳表的数据结构：

![img](https://pic2.zhimg.com/v2-f89738fcce97ad003c4a60fe05b8c845_b.jpg)

将一个有序链表level0，挑出其中几个元素到level1及level2，每个level越往上，选出来的指针元素越少，查找时依次从高level往低查找，比如55，先找到level2的31，再找到level1的47，最后找到55，一共3次查找，查找效率和2叉树的效率相当，但也是用了一定的空间冗余来换取的。

假设有下面三个posting list需要联合索引：

![img](https://pic3.zhimg.com/v2-b98bc5a862a5630989da2a122d3edaba_b.jpg)

如果使用跳表，对最短的posting list中的每个id，逐个在另外两个posting list中查找看是否存在，最后得到交集的结果。

如果使用bitset，就很直观了，直接按位与，得到的结果就是最后的交集。

### **总结和思考**

Elasticsearch的索引思路:

> 将磁盘里的东西尽量搬进内存，减少磁盘随机读取次数(同时也利用磁盘顺序读特性)，结合各种奇技淫巧的压缩算法，用及其苛刻的态度使用内存。

所以，对于使用Elasticsearch进行索引时需要注意:

- 不需要索引的字段，一定要明确定义出来，因为默认是自动建索引的
- 同样的道理，对于String类型的字段，不需要analysis的也需要明确定义出来，因为默认也是会analysis的
- 选择有规律的ID很重要，随机性太大的ID(比如java的UUID)不利于查询

关于最后一点，个人认为有多个因素:

其中一个(也许不是最重要的)因素: 上面看到的压缩算法，都是对Posting list里的大量ID进行压缩的，那如果ID是顺序的，或者是有公共前缀等具有一定规律性的ID，压缩比会比较高；

另外一个因素: 可能是最影响查询性能的，应该是最后通过Posting list里的ID到磁盘中查找Document信息的那步，因为Elasticsearch是分Segment存储的，根据ID这个大范围的Term定位到Segment的效率直接影响了最后查询的性能，如果ID是有规律的，可以快速跳过不包含该ID的Segment，从而减少不必要的磁盘读次数，具体可以参考这篇[如何选择一个高效的全局ID方案](https://link.zhihu.com/?target=http%3A//blog.mikemccandless.com/2014/05/choosing-fast-unique-identifier-uuid.html)(评论也很精彩)

 

 

## 倒排索引

* term：就是字段 
* term Dictionary：排序完的term，采用2分查找
* term index：字典的索引树

 

 

## 问题

1. 比关系型数据库快

   因为Lucene的倒排索引技术，b-tree索引用于写入优化。Mysql只有term dictionary这一层，是以b-tree排序的方式存储在磁盘上的。检索一个term需要若干次的random access的磁盘操作。而Lucene在term dictionary的基础上添加了term index来加速检索，term index以树的形式缓存在内存中。从term index查到对应的term dictionary的block位置之后，再去磁盘上找term，大大减少了磁盘的random access次数

    

    额外值得一提的两点是：term index在内存中是以FST（finite state transducers）的形式保存的，其特点是非常节省内存。Term dictionary在磁盘上是以分block的方式保存的，一个block内部利用公共前缀压缩，比如都是Ab开头的单词就可以把Ab省去。这样term dictionary可以比b-tree更节约磁盘空间。

    

    

    

    

    

    

    

    

2. 