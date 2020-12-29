## [[zuul开发实战（限流，超时解决）](https://www.cnblogs.com/wlwl/p/10413151.html)](https://www.cnblogs.com/wlwl/p/10413151.html)

## 简介

* 系统唯一对外的入口
* 处理非业务功能
* 路由请求，鉴权，监控，缓存，限流

> 统一接入 　　　
>
>  * 智能路由 　　　
>  * AB测试、灰度测试 　　　
>  * 负载均衡、容灾处理 　　
>  * 日志埋点（类似Nignx日志） 　
>
> 　流量监控 　　　
>
>  * 限流处理 　
>  * 服务降级 　
>
> 　安全防护 　　　
>
>  * 鉴权处理 　　　
>  * 监控 　　　
>  * 机器网络隔离 
>
> Zuul 2.0比1.0的性能提高很多 

## 配置文件

### POM

```xml
<dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>

<dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
</dependency>
```

### YML

```properties
server:
  port: 9000

#服务的名称
spring:
  application:
    name: api_gatway

#指定注册中心地址ַ
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/

zuul:
  routes:
    #拒绝order直接访问
    order-service: /apigateway/order/** 
    product-service: /apigateway/product/**
    ignored-patterns: /*-service/**
    #解决http请求头为空的问题
    sensitive-headers:

ribbon:
  ReadTimeout: 7000 
  ConnectTimeout: 2000 
  MaxAutoRetries: 1 
  #重试负载均衡其他的实例最大重试次数,不包括首次调用
  MaxAutoRetriesNextServer: 2 
  #是否所有操作都重试
  OkToRetryOnAllOperations: false  
  
feign:
  hystrix: 
    enabled: true
hystrix:
  command:
    default:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 7000


```

### Config

```java
@EnableZuulProxy
@Configuration
public class ZuulConfig {}
```

## Filter

### 权限控制过滤器

```java
@Component
public class UrlFilter extends ZuulFilter {

    private String pre_filterType = PRE_TYPE;    // 前置过滤器
    private String post_filterType = POST_TYPE;  // 后置过滤器
    private String error_filterType = ERROR_TYPE;// 异常过滤器
    
    //指定过滤器类型
    @Override
    public String filterType() {return PRE_TYPE;}
    
    //指定优先级，0比1先执行
    @Override
    public int filterOrder() {return 3;}

    //过滤器是否生效
    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest  request = requestContext.getRequest();
        if ("xxx/URL".equalsIgnoreCase(request.getRequestURI())){
            return true;
        }
        return false;
    }

    //主要的逻辑
    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext =  RequestContext.getCurrentContext();
        HttpServletRequest  request = requestContext.getRequest();

        //token对象
        String id = request.getHeader("id");

	    //校验
        if(StringUtils.isBlank((id))){
            id  = request.getParameter("id");
        }
        if (StringUtils.isBlank(id)) {
            requestContext.setSendZuulResponse(false);
            requestContext.
                    setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }
}
```

### 限流过滤器

```java
@Component
public class RequestFilter extends ZuulFilter {

    private String pre_filterType = PRE_TYPE;    // 前置过滤器
    private String post_filterType = POST_TYPE;  // 后置过滤器
    private String error_filterType = ERROR_TYPE;// 异常过滤器
    
    //每秒产生1000个令牌
    private static final RateLimiter RATE_LIMITER = RateLimiter.create(1000);

    //指定过滤器类型
    @Override
    public String filterType() {
        return pre_filterType;
    }

    //指定过滤器优先级
    @Override
    public int filterOrder() {
        return -4;
    }

    //指定过滤器是否生效
    @Override
    public boolean shouldFilter() {
        return true;
    }

    //业务逻辑
    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        boolean tryAcquire = RATE_LIMITER.tryAcquire();
        // 如果获取不到就直接停止
        if(!tryAcquire){
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpStatus.TOO_MANY_REQUESTS.value());
        }
        return null;
    }
}
```

