







springboot-configure-processor 这个和业务无关只是为了开发方便





## 静态资源配置管理

* 启动默认加载xxxautoconfiguration（自动配置类）

* 先去找controller看能不能处理，再去匹配静态资源

* webmvcautoconfiguration（web的自动配置类）

* ```
  public WebMvcAutoConfigurationAdapter(ResourceProperties resourceProperties, WebMvcProperties mvcProperties,
        ListableBeanFactory beanFactory, ObjectProvider<HttpMessageConverters> messageConvertersProvider,
        ObjectProvider<ResourceHandlerRegistrationCustomizer> resourceHandlerRegistrationCustomizerProvider,
        ObjectProvider<DispatcherServletPath> dispatcherServletPath,
        ObjectProvider<ServletRegistrationBean<?>> servletRegistrations) {
     this.resourceProperties = resourceProperties;
     this.mvcProperties = mvcProperties;
     this.beanFactory = beanFactory;
     this.messageConvertersProvider = messageConvertersProvider;
     this.resourceHandlerRegistrationCustomizer = resourceHandlerRegistrationCustomizerProvider.getIfAvailable();
     this.dispatcherServletPath = dispatcherServletPath;
     this.servletRegistrations = servletRegistrations;
  }
  ```



> 如果一个配置类默认只有一个有参构造器，那么所有参数的值都会在容器中确定
>
> ListableBeanFactory就是ioc容器 
>
> messageConvertersProvider  各种converter
>
> resourceHandlerRegistrationCustomizerProvider     资源处理器的自定义器

在静态资源映射的时候源代码设置了cache，所以就算使用了disable-cahe也是304请求，可以在header中看到



handlermapping 保存了每一个handler能处理哪些请求





## 请求处理

#### rest原理

表的提交要使用rest风格，要使用_method(postman这种直接传的，直接放行)

* 请求过来先被过滤器拦截

* 原生的post，包装模式->重写了请求方法

* ```
  filterChain.doFilter(requestToUse, response);
  随后放行
  ```



## 请求映射原理

访问dispatcherServlet发现他就为servlet的子类

使用c+h发现在frameworkservlet中有deget的实现

doget的主要实现又是processRequest

processRequest 又调用doService

doservice是子类dispatcherServlet实现的（听说是模板设计方法）

后面又进到doDispatch







所以分析直接分析doDispatcth

//1.判断是不是文件是上传

//2.handlermapping 处理器映射,传入的时request，返回的是一个handler

![1610120753145](C:\Users\ADMINI~1\AppData\Local\Temp\1610120753145.png)

可以看到现在的话是有5个handlermapping

* requestMappingHandlerMaping
  * 保存了@requstmapping和handler的映射规则
  * ![1610121716762](C:\Users\ADMINI~1\AppData\Local\Temp\1610121716762.png)
  * 所有的请求映射都存储在hanlermapping中
    * springboot自动配置欢迎页的handlermapping，访问/  能访问到index.html
* BeanNameUrlHandlermapping
* routeFunctionMapping
* SimpleUrlHandlerMapping
* WelCompePageHandlerMapping



sprinhgboot在webmvcautoconfiguration中为我们自动配置了前五个



如果想自己做一定映射处理，就自己写handlermapper







## 1.普通参数与基本注解

```
@pathVariable
@RequestHeader
@ModelAttribute
@RequestParam
@MatrixVarialble
@cookieValue
@RequestBody
```



![1610125621446](C:\Users\ADMINI~1\AppData\Local\Temp\1610125621446.png)]





* 可以直接入参Httpsession，那么业务逻辑里面就可以操作session
* ![1610126621482](C:\Users\ADMINI~1\AppData\Local\Temp\1610126621482.png)

@requestparam也可以使用map



@requestA





服务器通过jsessionid获取session对象，jsessionid存放在cookie里面



![1610187727644](C:\Users\ADMINI~1\AppData\Local\Temp\1610187727644.png)

这个矩阵变量应该没卵用









### 参数处理原理

* handlermapping中找到可以处理请求的handelr（就是controller的方法）
* 为handler找一个适配器adapter
  * 一共四个
  * requestmappingHanderAdapter 支持方法上标注@requestmapping
  * handlerfuctionAdapter 支持函数式编程
  * ...
* 执行目标方法 也就是执行handler中的handle方法





#### 参数解析器（ArgumentResolver   26个）

确定要执行的目标方法的每一个参数值是什么

springmvc具体能写多少参数类型，主要还是看参数解析器

![1610194832108](C:\Users\ADMINI~1\AppData\Local\Temp\1610194832108.png)

* 当前解析器是否支持
* 直接进行解析
* 有缓存机制，所以第一次处理很慢
* 主要核心就是一个增强for循环



### 如何确定目标方法每一个参数的值





### 返回值处理器

![1610194908796](C:\Users\ADMINI~1\AppData\Local\Temp\1610194908796.png)





## 复杂参数

Map ，mode（里面的数据会被默认放在request请求雨中）

![1610201815361](C:\Users\ADMINI~1\AppData\Local\Temp\1610201815361.png)





![1610201949759](C:\Users\ADMINI~1\AppData\Local\Temp\1610201949759.png)



设置cookie如上



Map或者model类型的参数，会返回mavcontainner的getmodel





## 目标方法执行完成，把所有的数据都放在modelAndViewContainer里面，宝行要去的页面地址view

还有model数据



![1610202695366](C:\Users\ADMINI~1\AppData\Local\Temp\1610202695366.png)



可以在上面代码中看到，request最后就是把model放进去了





回顾源代码发现handler匹配的时候是加了锁的





pojo的解析过程是由ServletModelAttributeMethodProcessor来解析的





webdatabinder  binder = binderFactory.createBinder(request,attribute,name)

binder 就是web数据的绑定器，将请求参数的值绑定到指定的javabean中

利用里面的converter 将请求属猪转换成指定的数据类型，然后再次封装到javabean

conveter也是要用缓存的，所以常用缓存（弹幕说是，策略模式）



generic conversion service 在设置每一个值的时候，找他里面的convertor，从而转换到我们的特定的类型







## 数据响应和响应页面

1.响应json

```
stater 场景里面加入了json场景（使用jackson）
```

