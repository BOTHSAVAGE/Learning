



[springboot整合shiro（完整版）](https://www.jianshu.com/p/7f724bec3dc3)

# [Spring Boot：整合Shiro权限框架](https://www.cnblogs.com/xifengxiaoma/p/11061142.html)



## 摘要

认证、授权、加密、会话管理

- 验证用户身份
- 用户访问权限控制
- 支持单点登录(SSO)功能
- 可以响应认证、访问控制，或Session事件
- 支持提供“Remember Me”服务



特别是对以下的功能支持：

- Web支持：Shiro 提供的 web 支持 api ，可以很轻松的保护 web 应用程序的安全。

- 缓存：缓存是 Apache Shiro 保证安全操作快速、高效的重要手段。

- 并发：Apache Shiro 支持多线程应用程序的并发特性。

- 测试：支持单元测试和集成测试，确保代码和预想的一样安全。

- “Run As”：这个功能允许用户在许可的前提下假设另一个用户的身份。

- “Remember Me”：跨 session 记录用户的身份，只有在强制需要时才需要登录。

  

- Authentication（认证）：用户身份识别，通常被称为用户“登录”。
- Authorization（授权）：访问控制。比如某个用户是否具有某个操作的使用权限。
- Session Management（会话管理）：特定于用户的会话管理,甚至在非web 应用程序。
- Cryptography（加密）：在对数据源使用加密算法加密的同时，保证易于使用。

## 简介



* subject 用户

* ######  SecurityManager：安全管理器，管理所有Subject，可以配合内部安全组件。(类似于SpringMVC中的DispatcherServlet)

* ######  Realms：用于进行权限信息的验证，一般需要自己实现。

  ###### 细分功能

   1. Authentication：身份认证/登录(账号密码验证)。

  ###### 2. Authorization：授权，即角色或者权限验证。

  ###### 3. Session Manager：会话管理，用户登录后的session相关管理。

  ###### 4. Cryptography：加密，密码加密等。

  ###### 5. Web Support：Web支持，集成Web环境。

  ###### 6. Caching：缓存，用户信息、角色、权限等缓存到如redis等缓存中。

  ###### 7. Concurrency：多线程并发验证，在一个线程中开启另一个线程，可以把权限自动传播过去。

  ###### 8. Testing：测试支持；

  ###### 9. Run As：允许一个用户假装为另一个用户（如果他们允许）的身份进行访问。

  ###### 10. Remember Me：记住我，登录后，下次再来的话不用登录了。

   

Maven

 ```xml
<dependency>
      <groupId>org.apache.shiro</groupId>
      <artifactId>shiro-spring</artifactId>
      <version>1.6.0</version>
</dependency>
 ```
conifg

```java
@Configuration
public class shiroConfig {
    
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }

    //将自己的验证方式加入容器
    @Bean
    public CustomRealm myShiroRealm() {
        CustomRealm customRealm = new CustomRealm();
        return customRealm;
    }

    //权限管理，配置主要是Realm的管理认证
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        return securityManager;
    }

    //Filter工厂，设置对应的过滤条件和跳转条件
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> map = new HashMap<>();
        //登出
        map.put("/logout", "logout");
        //对所有用户认证
        map.put("/**", "authc");
        //登录
        shiroFilterFactoryBean.setLoginUrl("/login");
        //首页
        shiroFilterFactoryBean.setSuccessUrl("/index");
        //错误页面，认证不通过跳转
        shiroFilterFactoryBean.setUnauthorizedUrl("/error");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }

  
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
```

这里都是manytomany

POJO

```java
public class User {
    private String id;
    private String userName;
    private String password;
    private Set<Role> roles;
}
```

```java
public class Role {
    private String id;
    private String roleName;
    private Set<Permissions> permissions;
}
```

```java
public class Permissions {
    private String id;
    private String permissionsName;
}
```

权限管理器

```java
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private LoginService loginService;

    //权限配置类
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String name = (String) principalCollection.getPrimaryPrincipal();
        User user = loginService.getUserByName(name);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        for (Role role : user.getRoles()) {
        //添加角色
        simpleAuthorizationInfo.addRole(role.getRoleName());
        //添加权限
        for (Permissions permissions : role.getPermissions()) {        simpleAuthorizationInfo.addStringPermission(permissions.getPermissionsName()); } }
        return simpleAuthorizationInfo;
    }


    //认证配置类
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (StringUtils.isEmpty(authenticationToken.getPrincipal())) {
            return null;
        }
        //获取用户信息
        String name = authenticationToken.getPrincipal().toString();
        User user = loginService.getUserByName(name);
        if (user == null) {
            //这里返回后会报出对应异常
            return null;
        } else {
            //这里验证authenticationToken和simpleAuthenticationInfo的信息
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(name, user.getPassword().toString(), getName());
            return simpleAuthenticationInfo;
        }
    }
}
```

controller

```java
package com.wsl.controller;

import com.wsl.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LoginController {

    @GetMapping("/login")
    public String login(User user) {
        if (StringUtils.isEmpty(user.getUserName()) || StringUtils.isEmpty(user.getPassword())) {
            return "请输入用户名和密码！";
        }
        //用户认证信息
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(
                user.getUserName(),
                user.getPassword()
        );
        try {
            //进行验证，这里可以捕获异常，然后返回对应信息
            subject.login(usernamePasswordToken);
//            subject.checkRole("admin");
//            subject.checkPermissions("query", "add");
        } catch (UnknownAccountException e) {
            log.error("用户名不存在！", e);
            return "用户名不存在！";
        } catch (AuthenticationException e) {
            log.error("账号或密码错误！", e);
            return "账号或密码错误！";
        } catch (AuthorizationException e) {
            log.error("没有权限！", e);
            return "没有权限";
        }
        return "login success";
    }

    @RequiresRoles("admin")
    @GetMapping("/admin")
    public String admin() {
        return "admin success!";
    }

    @RequiresPermissions("query")
    @GetMapping("/index")
    public String index() {
        return "index success!";
    }

    @RequiresPermissions("add")
    @GetMapping("/add")
    public String add() {
        return "add success!";
    }
}

```



```
@ControllerAdvice
@Slf4j
public class MyExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    public String ErrorHandler(AuthorizationException e) {
        log.error("没有通过权限验证！", e);
        return "没有通过权限验证！";
    }
}
```

Exception

##### 1. AuthenticationException 认证异常

Shiro在登录认证过程中，认证失败需要抛出的异常。 AuthenticationException包含以下子类：

###### 1.1. CredentitalsException 凭证异常

IncorrectCredentialsException             不正确的凭证
 ExpiredCredentialsException               凭证过期

###### 1.2. AccountException 账号异常

ConcurrentAccessException:      并发访问异常（多个用户同时登录时抛出）
 UnknownAccountException:        未知的账号
 ExcessiveAttemptsException:     认证次数超过限制
 DisabledAccountException:       禁用的账号
 LockedAccountException:     账号被锁定
 UnsupportedTokenException:      使用了不支持的Token

##### 2. AuthorizationException: 授权异常

Shiro在登录认证过程中，授权失败需要抛出的异常。 AuthorizationException包含以下子类：

###### 2.1.  UnauthorizedException:

抛出以指示请求的操作或对请求的资源的访问是不允许的。

###### 2.2. UnanthenticatedException:

当尚未完成成功认证时，尝试执行授权操作时引发异常。

 

 

 

 

 



   ```

   ```