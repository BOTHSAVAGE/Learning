## 摘要

* 代理模式是什么
* 比较静态与动态代理的区别
* 分别演示静态代理和动态代理

## 比较

## 静态代理

### 手动静态

### AspectJ静态



## 动态代理

> * JDK动态代理是基于接口的方式，代理类和目标类都实现同一个接口。
> * CGLib动态代理是代理类去继承目标类，然后重写其中目标类的方法。

### JDK动态代理



### CGlib动态代理

![img](https://img2018.cnblogs.com/blog/1368608/201906/1368608-20190601212347256-869513516.png) 

#### POM

```xml
<dependency>
        <groupId>cglib</groupId>
        <artifactId>cglib</artifactId>
        <version>2.2.2</version>
</dependency>
```

#### 需要被加强的类

```java
public class Dog{
    
    final public void run(String name) {
        System.out.println("狗"+name+"----run");
    }
    
    public void eat() {
        System.out.println("狗----eat");
    }
}
```

#### 方法拦截器

```java
public class MyMethodInterceptor implements MethodInterceptor{
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("增强");
        //注意这里的方法调用，不是用的反射
        Object object = proxy.invokeSuper(obj, args);
        return object;
    }  
}
```

#### 测试类

```java
public class CgLibProxy {
    public static void main(String[] args) {
        //创建Enhancer对象，类似于JDK动态代理的Proxy类，下一步就是设置几个参数
        Enhancer enhancer = new Enhancer();
        //设置目标类的字节码文件
        enhancer.setSuperclass(Dog.class);
        //设置回调函数
        enhancer.setCallback(new MyMethodInterceptor());
        //创建加运行
        Dog proxyDog = (Dog)enhancer.create();
        proxyDog.eat();       
    }
}
```

## 总结

1,2是目前参考的，还没有看完

## 参考

[1].[CGLib动态代理](https://www.cnblogs.com/wyq1995/p/10945034.html)

[2].[java反射和代理](https://www.cnblogs.com/wyq1995/p/10936286.html)