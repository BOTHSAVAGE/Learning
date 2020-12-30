## 摘要

## 概念

> 使用场景：与业务无关的且经常使用到的公共功能如鉴权，日志，性能优化，事务，错误处理，资源池，同步，审计，幂等等

>  优点：降低耦合度，易扩展，高复用

### 预编译和动态代理



### AOP术语

- **连接点（Joinpoint)** -> 方法
- **切点（PointCut）**-> 范围
- **增强（Advice）** 
  - **前置通知(before)**
  - **后置通知(after)**
  - **异常通知（afterThrowing）**
  - **返回通知(afterReturning)**
  - **环绕通知(around)**
- **目标对象（Target）** -> 对象
- **织入（Weaving）** 添加到对目标类具体连接点上的过程。
- **代理类（Proxy）** 一个类被AOP织入增强后，就产生了一个代理类。
- **切面（Aspect）** 切面由切点和增强组成，它既包括了横切逻辑的定义，也包括了连接点的定义，SpringAOP就是将切面所定义的横切逻辑织入到切面所制定的连接点中。
  比如我们常写的拦截器Interceptor，这就是一个切面类

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190225155448621.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQxOTgxMTA3,size_16,color_FFFFFF,t_70) 



@Before： 标识一个前置增强方法，相当于BeforeAdvice的功能.
@After： final增强，不管是抛出异常或者正常退出都会执行.
@AfterReturning： 后置增强，似于AfterReturningAdvice, 方法正常退出时执行.
@AfterThrowing： 异常抛出增强，相当于ThrowsAdvice.
@Around： 环绕增强，相当于MethodInterceptor.
execution：用于匹配方法执行的连接点；

eg.

- 任意公共方法的执行：execution(public * *(..))
- 任何一个以“set”开始的方法的执行：execution(* set*(..))
- AccountService 接口的任意方法的执行：execution(* com.xyz.service.AccountService.*(..))
- 定义在service包里的任意方法的执行： execution(* com.xyz.service.*.*(..))
- 定义在service包和所有子包里的任意类的任意方法的执行：execution(* com.xyz.service..*.*(..))

第一个*表示匹配任意的方法返回值， …(两个点)表示零个或多个，第一个…表示service包及其子包,第二个*表示所有类, 第三个*表示所有方法，第二个…表示方法的任意参数个数

- 定义在pointcutexp包和所有子包里的JoinPointObjP2类的任意方法的执行：execution(*com.test.spring.aop.pointcutexp..JoinPointObjP2.*(..))")
- pointcutexp包里的任意类： within(com.test.spring.aop.pointcutexp.*)
- pointcutexp包和所有子包里的任意类：within(com.test.spring.aop.pointcutexp..*)
- 实现了Intf接口的所有类,如果Intf不是接口,限定Intf单个类：this(com.test.spring.aop.pointcutexp.Intf)
- 当一个实现了接口的类被AOP的时候,用getBean方法必须cast为接口类型,不能为该类的类型
- 带有@Transactional标注的所有类的任意方法： @within(org.springframework.transaction.annotation.Transactional) @target(org.springframework.transaction.annotation.Transactional)
- 带有@Transactional标注的任意方法：
  @annotation(org.springframework.transaction.annotation.Transactional)
  @within和@target针对类的注解,@annotation是针对方法的注解
- 参数带有@Transactional标注的方法：@args(org.springframework.transaction.annotation.Transactional)
- 参数为String类型(运行是决定)的方法： args(String)

## 参考

[1].[Spring AOP——简单粗暴，小白教学](https://blog.csdn.net/qq_41981107/article/details/87920537)

[2].[CGLib动态代理](https://www.cnblogs.com/wyq1995/p/10945034.html)

https://www.cnblogs.com/joy99/p/10941543.html

https://www.cnblogs.com/bj-xiaodao/p/10777914.html#_label0_0

https://blog.csdn.net/asdasdasd123123123/article/details/82081870

https://blog.csdn.net/qq_30359699/article/details/105603949