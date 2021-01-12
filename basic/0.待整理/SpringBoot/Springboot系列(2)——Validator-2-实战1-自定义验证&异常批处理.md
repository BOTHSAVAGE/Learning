---
title: Springboot系列(2)——Validator-2-实战1-自定义验证&异常批处理
date: 2020-12-29  13:13:12
categories:
- Spring Boot
tags:
- Spring Boot
- 表单验证
- 实战
---

## 摘要

* 全局异常处理
* 自定义字符范围校验，自定义手机号校验

## 介绍

> 自定义两个验证注解：手机验证+选项验证
>
> 此时controller只用在参数加上@Validated就行，不用入侵业务代码，异常处理使用controllerAdvice
>
> 还有在自定义逻辑处理类xxValidator中，可以使用@Autowired注入service等bean

## 代码实现

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
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

### 表单对象

```java
@Data
public class ValidatorVo {
	@Phone
	private String phone;
    @Choose(range = {"a","b","c"})
	private String option;
}
```

### 自定义验证注解

```java
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {PhoneValidate.class})
public @interface Phone {
 
	String regexp() default "";
 
	String message() default "手机格式不正确";
 
	Class<?>[] groups() default { };
 
	Class<? extends Payload>[] payload() default { };
}
```

```java
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {ChooseValidate.class})
public @interface Choose {
 
	String[] range();
 
	String message() default "请输入正确的选项";
 
	Class<?>[] groups() default { };
 
	Class<? extends Payload>[] payload() default { };
}
```

### 自定义逻辑处理类

```java
public class PhoneValidate implements ConstraintValidator<Phone, String> {
 
        @Override
        public void initialize(Phone constraintAnnotation) {}

 
        @Override
	    public boolean isValid(String value, ConstraintValidatorContext context) {
            Pattern p = Pattern.compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(17[0,1,3,6-8])|(18[0-9])|(19[8,9])|(166))[0-9]{8}$");
            Matcher m = p.matcher(value);
            return m.matches();
        }
}
```

```java
public class ChooseValidate implements ConstraintValidator<Choose, String> {
    
        private String[] range = {};
    
        @Override
        public void initialize(Choose constraintAnnotation) {
            range = constraintAnnotation.range();
        }
 
        @Override
	    public boolean isValid(Object object, ConstraintValidatorContext context) {
            //1.如果所修饰对象为空，那么值恒为true
            if(object == null){ return true;}
            //2.若被修饰的对象在这个数组里面，返回true
            List<String> collect Arrays.stream(range).collect(Collectors.toList());
            if(collect.contains(object)){
                return true;
            }
            //3.不包含返回false
            return false;
        }
}
```

### ControllerAdvice

```java
@ControllerAdvice
public class MyGlobalExceptionHandler {
    @ExceptionHandler({BindException.class})
    public String bindException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        return Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
    }
}
```

### Controller

```java
@RestController
@RequestMapping("/valid")
public class ValidatorController {

	@GetMapping("/test")
	public String test(@Validated ValidatorVo vo) {
        String result = "成功";
		return result;
	}
    
}
```

## 参考

[1].[Spring Boot集成Hibernate Validator](https://www.cnblogs.com/sun-fan/p/10599038.html)

[2].[SpringBoot validator 完美实现+统一封装错误提示](https://blog.csdn.net/catoop/article/details/95366348)

[3].[自定义注解--手机号校验](https://blog.csdn.net/caitianchao/article/details/79012129)

[4].[自定义Validator注解验证](https://blog.csdn.net/bairuix/article/details/108290913)

[5].[SpringMVC 中 @ControllerAdvice 注解的三种使用场景！](https://www.cnblogs.com/lenve/p/10748453.html)