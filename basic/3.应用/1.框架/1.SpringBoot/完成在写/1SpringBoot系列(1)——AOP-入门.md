---
title: SpringBoot系列(1)——AOP-入门
date: 2020-12-28  12:13:11
categories:
- Spring Boot
tags:
- Spring Boot
- aop
- demo
---

## 摘要

* aop关键词
* spring aop小demo

<!--more-->

## 概念

> 使用场景：与业务无关的且经常使用到的公共功能如鉴权，日志，性能优化，事务，错误处理，资源池，同步，审计，幂等等

>  优点：降低耦合度，易扩展，高复用
>
>  实现方式：静态代理(AspectJ) + 动态代理（CGlib/Jdk）

### aop关键词

- **连接点(Joinpoint)**  连接点就是增强的实现
- **切点(PointCut)**就是那些需要应用切面的方法 
- **增强(Advice)** 
  - **前置通知(before)**
  - **后置通知(after)**
  - **异常通知(afterThrowing)**
  - **返回通知(afterReturning)**
  - **环绕通知(around)**
- **目标对象(Target)**
- **织入(Weaving)**添加到对目标类具体连接点上的过程。
- **代理类(Proxy)** 一个类被AOP织入增强后，就产生了一个代理类。
- **切面(Aspect）** 切面由切点和增强组成，它既包括了横切逻辑的定义，也包括了连接点的定义
![](http://assets.processon.com/chart_image/5fec99ba7d9c0863d3ffd467.png)
## Spring aop测试

### pom

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.3.5.RELEASE</version>
    <relativePath/>
</parent>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-aop</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### aspect

```java
@Component
@Aspect 
public class DemoAspect {


    //切入点1：匹配xxx类下的方法名以demo开头、参数类型为int的public方法
    @Pointcut("execution(public * com.bothsavage.service.DemoService.demo*(int))")
    public void matchCondition() {}

    //切入点2：匹配xxx类下的方法名以demo开头、参数类型为long的public方法
    @Pointcut("execution(public * com.bothsavage.service.DemoService.demo1*(long))")
    public void matchCondition_() {}

    //前置
    @Before("matchCondition()")
    public void before() {
        System.out.println("Before");
    }

    //全局后置
    @After("matchCondition()")
    public void after(){
        System.out.println("after");
    }

    //返回后置
    @AfterReturning("matchCondition()")
    public void afterReturning(){
        System.out.println("afterReturning");
    }

    //抛出后置
    @AfterThrowing("matchCondition()")
    public void afterThrowing(){
        System.out.println("afterThrowing");
    }

    @Around("matchCondition_()")
    public Object around(ProceedingJoinPoint joinPoint) {
        Object result = null;
        System.out.println("before");
        try{
            result = joinPoint.proceed(joinPoint.getArgs());//获取参数
            System.out.println("after");
        } catch (Throwable e) {
            System.out.println("after exception");
            e.printStackTrace();
        } finally {
            System.out.println("finally");
        }
        return result;
    }

}
```

### service

```java
@Service
public class DemoService {

    public void demo(int arg1){
        System.out.println(arg1);
    }

    public void demo1(long arg1){
        System.out.println(arg1);
    }
    
}
```



### test

```java
@SpringBootTest
public class DemoServiceTest {
    
    @Autowired
    DemoService demoService;

    //测试单独四个
    @Test
    public void testDemo1(){
        demoService.demo(1);
    }
    
    //测试around
    @Test
    public void testDemo2(){
        demoService.demo1(1L);
    }
}
```

## 参考

[1].[Spring AOP——简单粗暴，小白教学](https://blog.csdn.net/qq_41981107/article/details/87920537)

[2].[CGLib动态代理](https://www.cnblogs.com/wyq1995/p/10945034.html)

[3].[关于 Spring AOP (AspectJ) 你该知晓的一切](https://zhuanlan.zhihu.com/p/25522841)

[4].[Spring AOP用法详解](https://www.cnblogs.com/liantdev/p/10125284.html)
