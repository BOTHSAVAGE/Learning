## 摘要

* 实现简单的权限控制
  * 实现简单的日志

## 权限控制

### pom

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

### annotation

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PermissionAnnotation{}
```

### aspect

```java
@Aspect
@Component
@Order(1)
public class PermissionFirstAdvice {

    @Pointcut("@annotation(xx.xx.xx.PermissionAnnotation)")
    private void permissionCheck() {
    }

    @Around("permissionCheck()")
    public Object permissionCheckFirst(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println(System.currentTimeMillis());

        //获取请求参数
        Object[] objects = joinPoint.getArgs();
        String userName = (String) objects[0];

        if (!userName.equals("admin")) {
            return "失败";
        }
        return joinPoint.proceed();
    }
}
```

### controller

```java
@RestController
@RequestMapping(value = "/permission")
public class TestController {
    
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    @PermissionsAnnotation()
    public String getGroupList(@RequestParam String userName) {
        return "Hello "+userName;
    }
}
```

## 日志

### pom

```xml
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.5.RELEASE</version>
        <relativePath/>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>5.5.6</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.70</version>
        </dependency>
    </dependencies>
```

### aspect

```java
@Aspect
@Component
public class OperLogAspect {

    //操作切入点
    @Pointcut("@annotation(com.bothsavage.annotation.OperLog)")
    public void operLogPoinCut() {}


    //正常返回通知
    @AfterReturning(value = "operLogPoinCut()", returning = "keys")
    public void saveOperLog(JoinPoint joinPoint, Object keys) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        OperationLog operlog = new OperationLog();
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = method.getName();
            OperLog opLog = method.getAnnotation(OperLog.class);
            methodName = className + "." + methodName;
            Map<String, String> rtnMap = converMap(request.getParameterMap());
            String params = JSON.toJSONString(rtnMap);

            operlog.setOperId(IdUtil.randomUUID());
            operlog.setOperModul(opLog.operModul());
            operlog.setOperType(opLog.operType());
            operlog.setOperDesc(opLog.operDesc());
            operlog.setOperMethod(methodName); // 请求方法
            operlog.setOperRequParam(params); // 请求参数
            operlog.setOperRespParam(JSON.toJSONString(keys)); // 返回结果
            operlog.setOperUri(request.getRequestURI()); // 请求URI
            operlog.setOperCreateTime(new Date()); // 创建时间

            //打印日志
            System.out.println(JSON.toJSONString(operlog));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
        //转换request 请求参数
    public Map<String, String> converMap(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }

    //转换异常信息为字符串
    public String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements) {
        StringBuffer strbuff = new StringBuffer();
        for (StackTraceElement stet : elements) {
            strbuff.append(stet + "\n");
        }
        String message = exceptionName + ":" + exceptionMessage + "\n\t" + strbuff.toString();
        return message;
    }
}
```

### 实体类

```java
package com.bothsavage.entity;

import lombok.Data;

import java.util.Date;

@Data
public class OperationLog {
    private String operId;
    private String operModul;
    private String operType;
    private String operDesc;
    private String OperMethod;
    private String OperRequParam;
    private String OperRespParam;
    private String OperUserId;
    private String OperUserName;
    private String OperIp;
    private String OperUri;
    private Date OperCreateTime;
    private String OperVer;
}
```

### annotation

```java
@Target(ElementType.METHOD) //注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented
public @interface OperLog {
    String operModul() default ""; // 操作模块
    String operType() default "";  // 操作类型
    String operDesc() default "";  // 操作说明
}
```

### controller

```java
@RestController
public class TestController {

    @GetMapping("/test/{testName}")
    @OperLog(operModul = "测试模块",operType = "test",operDesc = "这个是用来测试的")
    public String test(@PathVariable String testName){
        return  "hello"+testName;
    }
}
```



## 参考

[1].[把自己牛逼到了，在SpringBoot用AOP切面实现一个权限校验...](https://mp.weixin.qq.com/s/2e8x9n4p49kZzM2Fr2cTVw)

[2].[Spring AOP 实现功能权限校验功能](https://blog.csdn.net/houxuehan/article/details/51745175?utm_medium=distribute.pc_relevant.none-task-blog-baidujs_title-3&spm=1001.2101.3001.4242)

[3].[SpringAop实现权限校验与日志打印](https://blog.csdn.net/sinat_34620530/article/details/103993158?utm_medium=distribute.pc_relevant_t0.none-task-blog-searchFromBaidu-1.control&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-searchFromBaidu-1.control)

[4].[基于Spring AOP实现的权限控制](https://www.cnblogs.com/sxkgeek/p/9985929.html)

[5].[使用SpringBoot AOP 记录操作日志、异常日志](https://www.cnblogs.com/wm-dv/p/11735828.html)

