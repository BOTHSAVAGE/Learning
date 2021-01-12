springboot响应式

  使用少量资源，编写拥有极高吞吐量，承载大并发的的应用



spring5的重大升级



响应式编程



jdk1.8 接口的默认实现

那么适配器编程就变得十分容易了，因为不需要实现不要的方法



Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run".  









灰度发布







springboot-starter-parent 是做依赖管理的

parent的爸爸是dependencies 里面有很多properties，几乎申明了所有的依赖版本号









spring-boot-starter.*   场景



各种配置都有默认的值

* 配置文件的值最终会绑定的每一个类上，这个类会在容器中创建对象



功能都是按需配置（引入场景才会自动配置）

* 落地实现（springboot-autoconfigure）



```
@Configuration(proxyBeanMethods = true) 这个是最大更新
```

如果false就不会去检查依赖，启动很快





@import 会调用.class的无参构造器并注入





@conditional

@importResource

* 把bean.xml里面的东西注入





@configurationproperty(prefix = "")

@component（如果全局配置@EnableConfigurationProperties，就比用每一个去指定component）

