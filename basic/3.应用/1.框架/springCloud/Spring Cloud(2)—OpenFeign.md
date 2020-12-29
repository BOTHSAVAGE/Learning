# [openfeign](https://www.cnblogs.com/leeego-123/p/12664909.html)

## [3千字详细讲解*OpenFeign*的使用姿势！](https://zhuanlan.zhihu.com/p/265667808)

## 简介

OpenFeign支持SpringMVC注解，集成了Ribbon做了负载均衡。这里所有的定义都是在消费端

## 配置文件

### POM

```xml
<dependency>
     <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>

 <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

### YML

```properties
#指定微服务名称
openFeignService: service-name

server:
  port: 80

eureka:
  client:
    register-with-eureka: false    
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka/
      
ribbon:
  ReadTimeout: 5000
  ConnectTimeout: 5000

#开启熔断
feign:
  hystrix:
    enabled: true
    
    
logging:
  level:
    xx.xx.xx.package: debug
```

### Config

```
import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@Configuration
public class FeignConfig {

    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }
}
```

* feignLoggerLevel   (openfeign的日志显示调整)
  * Logger.Level.NONE 不显示
  * Logger.Level.BASIC 仅记录请求方法，URL，响应状态码及执行时间
  * Logger.Level.HEADERS 除了BASIC中定义的信息外，还有请求和响应的头信息
  * Logger.Level.FULL 以上所有再加上正文及元素

### Service

```
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Component
@FeignClient(value = "service-name")  
public interface SomeFeignService
{
    @GetMapping(value = "/get/{id}")
    public CommonResult<String> getSthById(@PathVariable("id") Long id);
}
```

## Riboon

* 随机
* 轮询
* 重试
* 响应时间权重
* 最空闲连接策略

### Hystrix

