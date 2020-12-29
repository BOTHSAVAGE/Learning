https://zhuanlan.zhihu.com/p/54146400





springboot深度依赖注解来完成自动装配工作



SpringBoot 对 Controller 注解进行了特殊处理，它会将 Controller 注解的类当成 URL 处理器注册到 Servlet 的请求处理器中，在创建 Tomcat Server 时，会将请求处理器传递进去。HelloController 就是如此被自动装配进 Tomcat 的。 