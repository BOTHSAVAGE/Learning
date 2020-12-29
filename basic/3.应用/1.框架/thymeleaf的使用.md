[SpringBoot页面展示Thymeleaf](https://www.jianshu.com/p/a842e5b5012e)

https://www.cnblogs.com/msi-chen/p/10974009.html	

https://zhuanlan.zhihu.com/p/103089477

https://www.imooc.com/article/273588?block_id=tuijian_wz

https://www.pianshen.com/article/1256367151/

https://www.cnblogs.com/jnba/p/10832878.html

###



### POM

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

### springboot配置属性

```
spring:
  thymeleaf:    
    prefix: classpath:/templates/   
    suffix: .html
    mode: html  
    cache: false
```

### 配置类

> 在..已经进行了自动配置，无需手动创建配置类，下面为源码展示

