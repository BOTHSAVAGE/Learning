





[SpringCloud-Eureka 服务注册与发现，搭建 服务端和客户端](https://blog.csdn.net/qq_41712271/article/details/104751819)





## 客户端

### POM

```
<dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

### YML文件

```
server:
  port: 8771
 
eureka:
  client:
    serviceUrl:
      defaultZone: http://..../eureka/

 
#服务的名称
spring:
  application:
    name: eureka-product
```

### 配置

```
@EnableEurekaClient 
@Configuration
public class EurekaClientConfiguration {}
```



## 服务端

### POM

```
<dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

### YML文件

```
server:
  port: 8761
 
eureka:
  instance:
    hostname: 192.168.0.12
  client:
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
  server:
    #关闭保护模式 (默认是开启)
    enable-self-preservation: false
 
#服务的名称
spring:
  application:
    name: eureka-service
```

### 配置

```
@EnableEurekaServer
@Configuration
public class EurekaServerConfiguration {}
```

