## 摘要

* 分别演示静态代理和动态代理
## 静态代理

### 手动代理

#### interface

```java
public interface Interface_ {
    public void do();
}
```

#### 需要被加强的类

```java
public class Object_ implements Interface_{
    @Override
    public void do() {
        System.out.println("do");
    }
}
```

#### 代理类

```java
public class Object_Agent implements Interface_{

    private Object_ object_;
    public Object_Agent(Object_ object_) {
        this.object_ = object_;
    }

    @Override
    public  void do() {
        System.out.println("enhance");
        object_.do();
    }
    
    public static void main(String[] args) {
        Object_ object_ = new Object_();
        Object_Agent agent = new Object_Agent(object_);
        agent.do();
    }

}
```



### AspectJ静态

* 编写方式和spring aop没有区别

- Aspectj并不是动态的在运行时生成代理类，而是在编译的时候就植入代码到class文件
- 由于是静态织入的，所以性能相对来说比较好



## 动态代理

> * JDK动态代理是基于接口的方式，代理类和目标类都实现同一个接口。
> * CGLib动态代理是代理类去继承目标类，然后重写其中目标类的方法。

### JDK动态代理

#### 接口

```java
public interface Interface_ {
    public void do();
}
```

#### 需要被加强的类

```java
public class Object_ implements Interface_{
    @Override
    public void do() {
        System.out.println("do");
    }
}
```

#### 处理器实现类

```java
public class InvocationHandlerImpl implements InvocationHandler{

    private Object object;
    public InvocationHandlerImpl(Object object)
    {
        this.object = object;
    }
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        System.out.println("enhance");
        Object object = method.invoke(object, args);
        return object;
    }
}
```

#### 测试

```java
public class DynamicProxyDemonstration
{
    public static void main(String[] args)
    {
        Interface_ object_ = new Object_();
        InvocationHandler handler = new InvocationHandlerImpl(object_);
        ClassLoader loader = object_.getClass().getClassLoader();
        Class[] interfaces = object_.getClass().getInterfaces();
        Interface_ object__ = (Interface_) Proxy.newProxyInstance(loader, interfaces, handler); 
        object__.do();
    }
 
}
```



### CGlib动态代理

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
public class Object_{
    public  void do() {
        System.out.println("do");
    }
}
```

#### 方法拦截器

```java
public class MyMethodInterceptor implements MethodInterceptor{
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("enhance");
        Object object = proxy.invokeSuper(obj, args);
        return object;
    }  
}
```

#### 测试

```java
public class CgLibProxy {
    public static void main(String[] args) {
        //创建Enhancer对象，类似于JDK动态代理的Proxy类，下一步就是设置几个参数
        Enhancer enhancer = new Enhancer();
        //设置目标类的字节码文件
        enhancer.setSuperclass(Object_.class);
        //设置回调函数
        enhancer.setCallback(new MyMethodInterceptor());
        //创建加运行
        Object_ proxyObject_ = (Object_)enhancer.create();
        proxyObject_.do();       
    }
}
```

## 为啥转变被CGlib

一般写代码：

```java
@Autowired
UserService userService;
```

出错代码：

```
@Autowired
UserServiceImpl userService;
```

![启动报错](https://img2018.cnblogs.com/blog/1822265/201910/1822265-20191029133357222-1265293381.png)

 JDK 动态代理是基于接口的，代理生成的对象只能赋值给接口变量。

而 CGLIB 就不存在这个问题。因为 CGLIB 是通过生成子类来实现的，代理对象无论是赋值给接口还是实现类这两者都是代理对象的父类。

## 总结

* Spring5 默认aop实现为CGlib
* JDK动态代理是基于接口的，CGlib动态代理是基于继承的

## 参考

[1].[CGLib动态代理](https://www.cnblogs.com/wyq1995/p/10945034.html)

[2].[java反射和代理](https://www.cnblogs.com/wyq1995/p/10936286.html)

[3].[惊人！Spring5 AOP 默认使用Cglib? 从现象到源码深度分析](https://www.cnblogs.com/coderxiaohei/p/11758239.html)

> 本文作者: Both Savage

>本文链接: https://bothsavage.github.io/2020/12/29/SpringBoot/SpringBoot%E7%B3%BB%E5%88%97(2)%E2%80%94%E2%80%94AOP-%E9%9D%99%E6%80%81%E4%BB%A3%E7%90%86%E4%B8%8E%E5%8A%A8%E6%80%81%E4%BB%A3%E7%90%86/
  
>版权声明: 本博客所有文章除特别声明外，均采用 BY-NC-SA 许可协议。转载请注明出处！
