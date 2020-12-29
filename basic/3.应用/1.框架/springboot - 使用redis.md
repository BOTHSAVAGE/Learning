[SpringBoot 2.x 开发案例之妹子图接入 Redis 缓存 ](https://blog.52itstyle.vip/archives/5177/)

https://blog.csdn.net/zzhongcy/article/details/102584028

###



### POM

```
 <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-redis</artifactId>
 </dependency>
```

配置文件

```
spring.redis.database=0
spring.redis.host=127.0.0.1
spring.redis.port=6379
spring.redis.password=123456
spring.redis.lettuce.pool.max-active=8
spring.redis.lettuce.pool.max-wait=-1
spring.redis.lettuce.pool.max-idle=8
spring.redis.lettuce.pool.min-idle=0
spring.redis.timeout=30000
```

```java
@Autowired
private RedisTemplate<String, Serializable> redisTemplate;


redisTemplate.opsForValue().set(key, new User(1,"pjmike",20));

```

