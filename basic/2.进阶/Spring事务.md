## 事务概念

 ### 事务的三种灾难

名称 | 描述 
  -|-
 脏读(读取未提交数据) |脏读发生在一个事务读取了被另一个事务改写但尚未提交的数据时。如果这些改变在稍后被回滚了，那么第一个事务读取的数据就会是无效的。
 不可重复读( 前后多次读取，数据内容不一致) | 不可重复读发生在一个事务执行相同的查询两次或两次以上，但每次查询结果都不相同时。这通常是由于另一个并发事务在两次查询之间更新了数据。
 幻读(前后多次读取，数据总量不一致) | 幻读和不可重复读相似。当一个事务（T1）读取几行记录后，另一个并发事务（T2）插入了一些记录时，幻读就发生了。在后来的查询中，第一个事务（T1）就会发现一些原来没有的额外记录。（重点是新增和删除） 


### 事务的四种特性
名称 | 描述 
  -|- 
A原子性(Atomicity) |  事务是一个原子操作，由一系列动作组成。事务的原子性确保动作要么全部完成，要么完全不起作用。 
C一致性 (Consistency) | 一旦事务完成（不管成功还是失败），系统必须确保它所建模的业务处于一致的状态，而不会是部分完成部分失败。在现实中的数据不应该被破坏。 
I-隔离性(Isolation) | 可能有许多事务会同时处理相同的数据，因此每个事务都应该与其他事务隔离开来，防止数据损坏。 
D持久性(Durability )| 一旦事务完成，无论发生什么系统错误，它的结果都不应该受到影响，这样就能从任何系统崩溃中恢复过来。通常情况下，事务的结果被写到持久化存储器中。 

### 事务的五种隔离

| 名称                       | 描述                                                         |
| -------------------------- | ------------------------------------------------------------ |
| ISOLATION_DEFAULT          | 使用后端数据库默认的隔离级别                                 |
| ISOLATION_READ_UNCOMMITTED | 允许读取尚未提交的更改。可能导致脏读、幻读或不可重复读。     |
| ISOLATION_READ_COMMITTED   | （Oracle 默认级别）允许从已经提交的并发事务读取。可防止脏读，但幻读和不可重复读仍可能会发生。 |
| ISOLATION_REPEATABLE_READ  | （MYSQL默认级别）对相同字段的多次读取的结果是一致的，除非数据被当前事务本身改变。可防止脏读和不可重复读，但幻读仍可能发生。 |
| ISOLATION_SERIALIZABLE     | 完全服从ACID的隔离级别，确保不发生脏读、不可重复读和幻影读。这在所有隔离级别中也是最慢的，因为它通常是通过完全锁定当前事务所涉及的数据表来完成的。 |

### 事务的七种传播

名称 | 描述
-|-
PROPAGATION_REQUIRED | 如果外层有事务，则当前事务加入到外层事务，一块提交，一块回滚。如果外层没有事务，新建一个事务执行 
PROPAGATION_REQUES_NEW  | 该事务传播机制是每次都会新开启一个事务，同时把外层事务挂起，当当前事务执行完毕，恢复上层事务的执行。如果外层没有事务，执行当前新开启的事务即可
PROPAGATION_SUPPORT | 如果外层有事务，则加入外层事务，如果外层没有事务，则直接使用非事务方式执行。完全依赖外层的事务
PROPAGATION_NOT_SUPPORT | 该传播机制不支持事务，如果外层存在事务则挂起，执行完当前代码，则恢复外层事务，无论是否异常都不会回滚当前的代码
PROPAGATION_NEVER | 该传播机制不支持外层事务，即如果外层有事务就抛出异常
PROPAGATION_MANDATORY | 与NEVER相反，如果外层没有事务，则抛出异常
PROPAGATION_NESTED | 该传播机制的特点是可以保存状态保存点，当前事务回滚到某一个点，从而避免所有的嵌套事务都回滚，即各自回滚各自的，如果子事务没有把异常吃掉，基本还是会引起全部回滚的。

## Spring中使用

### 声明式事务

```java
@Transactional(rollbackFor{XXXException.class},
               timeout=30,
               propagation=Propagation.XXXXX,
               isolation=Isolation.XXXXX,
               readOnly=true/false)
```

> * rollbackFor 
>   * 可以传入一个集合，当异常执行的时候回滚事务
> * timeout
>   * 设置当前事务的超时时间
> * propagation
>   * 设置当前事务的传播机制
> * isolation
>   * 设置当前事务的隔离级别
> * readOnly
>   * 设置当前事务是否为只读事务 ，默认为false
>   * 它只是一个“暗示”，提示数据库驱动程序和数据库系统，这个事务并不包含更改数据的操作 
>   * “只读事务”仅仅是一个性能优化的推荐配置而已，并非强制你要这样做不可 

### @Transactional注解失效 

* 数据库存储引擎不支持

  * innodb支持，myisam不支持

* 类不在IOC中

* 内部自身调用

  * > ```java
    > @Service
    > public class doClass{
    >     @Override
    >     public void doSomething() {
    >         saveStudent();
    >     }
    >     @Transactional(rollbackFor = Exception.class)
    >     public void doSomethingAndTranscational() {
    >     	// sql 
    >     }
    > }
    > 要使用@Autowired 把doClass注入到IOC，然后通过代理类执行doSomethingAndTranscational方法
    > ```

* 异常被try/catch住

* 异常类型不准确

  * 如IOException 不是RuntimeException 

## 参考

[1].[有关Spring事务，看这一篇就足够了](https://www.cnblogs.com/mseddl/p/11577846.html)

[2].[Spring 事务 readOnly 到底是怎么回事？](https://www.cnblogs.com/tiancai/p/10213057.html)

[3].[Spring的@Transactional注解失效的场景](https://zhuanlan.zhihu.com/p/339536376)



