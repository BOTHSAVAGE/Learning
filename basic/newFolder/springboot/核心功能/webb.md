





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