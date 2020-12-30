## 摘要

## 概念

> 使用场景：与业务无关的且经常使用到的公共功能如鉴权，日志，性能优化，事务，错误处理，资源池，同步，审计，幂等等

>  优点：降低耦合度，易扩展，高复用
>
>  实现方式：静态代理(AspectJ) + 动态代理（CGlib/Jdk）

### aop常识

- **连接点（Joinpoint)**  连接点就是增强的实现
- **切点（PointCut） **就是那些需要应用切面的方法 
- **增强（Advice）** 
  - **前置通知(before)**
  - **后置通知(after)**
  - **异常通知（afterThrowing）**
  - **返回通知(afterReturning)**
  - **环绕通知(around)**
- **目标对象（Target）**
- **织入（Weaving）** 添加到对目标类具体连接点上的过程。
- **代理类（Proxy）** 一个类被AOP织入增强后，就产生了一个代理类。
- **切面（Aspect）** 切面由切点和增强组成，它既包括了横切逻辑的定义，也包括了连接点的定义，SpringAOP就是将切面所定义的横切逻辑织入到切面所制定的连接点中。
  比如我们常写的拦截器Interceptor，这就是一个切面类

![](https://gitee.com/BothSavage/PicGo/raw/master/20201230134938.png)



## Spring aop测试

## 参考

[1].[Spring AOP——简单粗暴，小白教学](https://blog.csdn.net/qq_41981107/article/details/87920537)

[2].[CGLib动态代理](https://www.cnblogs.com/wyq1995/p/10945034.html)

[3].[关于 Spring AOP (AspectJ) 你该知晓的一切](https://zhuanlan.zhihu.com/p/25522841)

https://www.cnblogs.com/joy99/p/10941543.html

https://www.cnblogs.com/bj-xiaodao/p/10777914.html#_label0_0

https://blog.csdn.net/asdasdasd123123123/article/details/82081870

https://blog.csdn.net/qq_30359699/article/details/105603949