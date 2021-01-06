## 摘要

* 介绍SpringBoot常见校验规则
* 小Demo

## 校验规则

```java
@Null   //被注释的元素必须为 null
@NotNull    //被注释的元素必须不为 null
@AssertTrue     //被注释的元素必须为 true
@AssertFalse    //被注释的元素必须为 false
@Min(value)     //被注释的元素必须是一个数字，其值必须大于等于指定的最小值
@Max(value)     //被注释的元素必须是一个数字，其值必须小于等于指定的最大值
@DecimalMin(value)  //被注释的元素必须是一个数字，其值必须大于等于指定的最小值
@DecimalMax(value)  //被注释的元素必须是一个数字，其值必须小于等于指定的最大值
@Size(max=, min=)  // 被注释的元素的大小必须在指定的范围内
@Digits (integer, fraction)     //数值范围
@Past   //被注释的元素必须是一个过去的日期
@Future     //被注释的元素必须是一个将来的日期
@Pattern(regex=,flag=)  //被注释的元素必须符合指定的正则表达式
Hibernate Validator //附加的 constraint
@NotBlank(message =)   //验证字符串非null，且长度必须大于0
@Email  //被注释的元素必须是电子邮箱地址
@Length(min=,max=)  //被注释的字符串的大小必须在指定的范围内
@NotEmpty   //被注释的字符串的必须非空
@Range(min=,max=,message=)  //被注释的元素必须在合适的范围内
```

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

	@NotNull(message = "不能为空")
	@Size(max = 16, min = 6, message = "字符串长度需要在6-16之间")
	private String name;
	@Max(value = 100, message = "最大100")
	@Min(value = 18, message = "最小18")
	private String age;
}
```

### Controller

```java
@RestController
@RequestMapping("/valid")
public class ValidatorController {

	@GetMapping("/test")
	public String test(@RequestBody @Validated ValidatorVo vo,BindingResult b) {
        String result;
        result = b.hasErrors()?"ok":"false";
		return result;
	}
    
}
```

### PostMan示意

![image-20210106172601725](https://gitee.com/BothSavage/PicGo/raw/master/image-20210106172601725.png)

## 参考

[1].[Spring Boot集成Hibernate Validator](https://www.cnblogs.com/sun-fan/p/10599038.html)

[2].[SpringBoot validator 完美实现+统一封装错误提示](https://blog.csdn.net/catoop/article/details/95366348)