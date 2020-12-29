## 参考

[1].[SpringBoot启动原理及相关流程](https://zhuanlan.zhihu.com/p/63413398)



## 启动方式

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

* @SpringBootConfiguration 
* @EnableAutoConfiguration
  * @import注解导入selector类
  * @AutoConfigurationPackage
* @ComponentScan
  * 如果不指定扫描范围，范围默认为修饰类所在package

### @EnableAutoConfiguration详解

**AutoConfigurationPackage注解：**

```
static class Registrar implements ImportBeanDefinitionRegistrar,DeterminableImports {
 
@Override
public void registerBeanDefinitions(AnnotationMetadata metadata,BeanDefinitionRegistry registry) {
 register(registry, new PackageImport(metadata).getPackageName());
}
```

它其实是注册了一个Bean的定义。new PackageImport(metadata).getPackageName()，它其实返回了当前主程序类的 同级以及子级 的包组件。  

![](https://raw.githubusercontent.com/BOTHSAVAGE/PicGo/master/image/20201223110427.png)

最主要就是ImportSelector下面的selectImports 方法，以下是AutoconfigurationImportSelector的实现

```
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        if (!this.isEnabled(annotationMetadata)) {
            return NO_IMPORTS;
        } else {
            AutoConfigurationMetadata autoConfigurationMetadata = AutoConfigurationMetadataLoader.loadMetadata(this.beanClassLoader);
            AnnotationAttributes attributes = this.getAttributes(annotationMetadata);
            List<String> configurations = this.getCandidateConfigurations(annotationMetadata, attributes);
            configurations = this.removeDuplicates(configurations);
            Set<String> exclusions = this.getExclusions(annotationMetadata, attributes);
            this.checkExcludedClasses(configurations, exclusions);
            configurations.removeAll(exclusions);
            configurations = this.filter(configurations, autoConfigurationMetadata);
            this.fireAutoConfigurationImportEvents(configurations, exclusions);
            return StringUtils.toStringArray(configurations);
        }
    }
```

就是加载了META-INF/spring.factories ，借助全局搜索可以很快的扎到这个文件



其中，最关键的要属@Import(EnableAutoConfigurationImportSelector.class)，借助EnableAutoConfigurationImportSelector，@EnableAutoConfiguration可以帮助SpringBoot应用将所有符合条件的@Configuration配置都加载到当前SpringBoot创建并使用的IoC容器。就像一只“八爪鱼”一样。 

https://pic3.zhimg.com/80/v2-9891ca0f880c5f19d4790e73b519d8e6_720w.jpg



读取完了后就是加载

SpringFactoriesLoader ，这个类的主要作用就是去指定的配置文件META-INF/spring.factories加载配置。 

```
public abstract class SpringFactoriesLoader {
//...
 public static <T> List<T> loadFactories(Class<T> factoryClass, ClassLoader classLoader) {
 ...
 }
  
  
 public static List<String> loadFactoryNames(Class<?> factoryClass, ClassLoader classLoader) {
 ....
 }
}
```

从classpath中搜寻所有的META-INF/spring.factories配置文件，并将其中org.springframework.boot.autoconfigure.EnableutoConfiguration对应的配置项通过反射（Java Refletion）实例化为对应的标注了@Configuration的JavaConfig形式的IoC容器配置类，然后汇总为一个并加载到IoC容器。 

![img](https://pic2.zhimg.com/80/v2-06b55c7e6f6376eedd746d104cd0c699_720w.jpg) 



**深入探索SpringApplication执行流程**

SpringApplication的run方法的实现是我们本次旅程的主要线路，该方法的主要流程大体可以归纳如下：

1） 如果我们使用的是SpringApplication的静态run方法，那么，这个方法里面首先要创建一个SpringApplication对象实例，然后调用这个创建好的SpringApplication的实例方法。在SpringApplication实例初始化的时候，它会提前做几件事情：

```
public static ConfigurableApplicationContext run(Object[] sources, String[] args) {
    return new SpringApplication(sources).run(args);
}
```

- 根据classpath里面是否存在某个特征类（org.springframework.web.context.ConfigurableWebApplicationContext）来决定是否应该创建一个为Web应用使用的ApplicationContext类型。
- 使用SpringFactoriesLoader在应用的classpath中查找并加载所有可用的ApplicationContextInitializer。
- 使用SpringFactoriesLoader在应用的classpath中查找并加载所有可用的ApplicationListener。
- 推断并设置main方法的定义类。
- 

```
@SuppressWarnings({ "unchecked", "rawtypes" })
private void initialize(Object[] sources) {
if (sources != null && sources.length > 0) {
this.sources.addAll(Arrays.asList(sources));
}
this.webEnvironment = deduceWebEnvironment();
setInitializers((Collection) getSpringFactoriesInstances(
ApplicationContextInitializer.class));
setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
this.mainApplicationClass = deduceMainApplicationClass();
}
```

- 2） SpringApplication实例初始化完成并且完成设置后，就开始执行run方法的逻辑了，方法执行伊始，首先遍历执行所有通过SpringFactoriesLoader可以查找到并加载的SpringApplicationRunListener。调用它们的started()方法，告诉这些SpringApplicationRunListener，“嘿，SpringBoot应用要开始执行咯！”。
- 

```
public ConfigurableApplicationContext run(String... args) {
StopWatch stopWatch = new StopWatch();
stopWatch.start();
ConfigurableApplicationContext context = null;
FailureAnalyzers analyzers = null;
configureHeadlessProperty();
SpringApplicationRunListeners listeners = getRunListeners(args);
listeners.starting();
try {
ApplicationArguments applicationArguments = new DefaultApplicationArguments(
args);
ConfigurableEnvironment environment = prepareEnvironment(listeners,
applicationArguments);
Banner printedBanner = printBanner(environment);
context = createApplicationContext();
analyzers = new FailureAnalyzers(context);
prepareContext(context, environment, listeners, applicationArguments,
printedBanner);
 // 核心点：会打印springboot的启动标志，直到server.port端口启动
refreshContext(context);
afterRefresh(context, applicationArguments);
listeners.finished(context, null);
stopWatch.stop();
if (this.logStartupInfo) {
new StartupInfoLogger(this.mainApplicationClass)
.logStarted(getApplicationLog(), stopWatch);
}
return context;
}
catch (Throwable ex) {
handleRunFailure(context, listeners, analyzers, ex);
throw new IllegalStateException(ex);
}
}
```

- 3） 创建并配置当前Spring Boot应用将要使用的Environment（包括配置要使用的PropertySource以及Profile）。

```
private ConfigurableEnvironment prepareEnvironment(SpringApplicationRunListeners listeners,ApplicationArguments applicationArguments) {
 // Create and configure the environment
 ConfigurableEnvironment environment = getOrCreateEnvironment();
 configureEnvironment(environment, applicationArguments.getSourceArgs());
 listeners.environmentPrepared(environment);
 if (!this.webEnvironment) {
 environment = new EnvironmentConverter(getClassLoader()).convertToStandardEnvironmentIfNecessary(environment);
 }
 return environment;
}
```

4） 遍历调用所有SpringApplicationRunListener的environmentPrepared()的方法，告诉他们：“当前SpringBoot应用使用的Environment准备好了咯！”。

```
public void environmentPrepared(ConfigurableEnvironment environment) {
 for (SpringApplicationRunListener listener : this.listeners) {
 listener.environmentPrepared(environment);
 }
}
```

5） 如果SpringApplication的showBanner属性被设置为true，则打印banner。

```
private Banner printBanner(ConfigurableEnvironment environment) {
 if (this.bannerMode == Banner.Mode.OFF) {
 return null;
 }
 ResourceLoader resourceLoader = this.resourceLoader != null ? this.resourceLoader: new DefaultResourceLoader(getClassLoader());
 SpringApplicationBannerPrinter bannerPrinter = new SpringApplicationBannerPrinter(resourceLoader, this.banner);
 if (this.bannerMode == Mode.LOG) {
 return bannerPrinter.print(environment, this.mainApplicationClass, logger);
 }
 return bannerPrinter.print(environment, this.mainApplicationClass, System.out);
}
```

6） 根据用户是否明确设置了applicationContextClass类型以及初始化阶段的推断结果，决定该为当前SpringBoot应用创建什么类型的ApplicationContext并创建完成，然后根据条件决定是否添加ShutdownHook，决定是否使用自定义的BeanNameGenerator，决定是否使用自定义的ResourceLoader，当然，最重要的，将之前准备好的Environment设置给创建好的ApplicationContext使用。

7） ApplicationContext创建好之后，SpringApplication会再次借助Spring-FactoriesLoader，查找并加载classpath中所有可用的ApplicationContext-Initializer，然后遍历调用这些ApplicationContextInitializer的initialize（applicationContext）方法来对已经创建好的ApplicationContext进行进一步的处理。

```
@SuppressWarnings({ "rawtypes", "unchecked" })
protected void applyInitializers(ConfigurableApplicationContext context) {
 for (ApplicationContextInitializer initializer : getInitializers()) {
 Class<?> requiredType = GenericTypeResolver.resolveTypeArgument(initializer.getClass(), ApplicationContextInitializer.class);
 Assert.isInstanceOf(requiredType, context, "Unable to call initializer.");
 initializer.initialize(context);
 }
}
```

8） 遍历调用所有SpringApplicationRunListener的contextPrepared()方法。

```
private void prepareContext(ConfigurableApplicationContext context,ConfigurableEnvironment environment, SpringApplicationRunListeners listeners,<br>ApplicationArguments applicationArguments, Banner printedBanner) {
 context.setEnvironment(environment);
 postProcessApplicationContext(context);
 applyInitializers(context);
 listeners.contextPrepared(context);
 if (this.logStartupInfo) {
 logStartupInfo(context.getParent() == null);
 logStartupProfileInfo(context);
 }
// Add boot specific singleton beans
 context.getBeanFactory().registerSingleton("springApplicationArguments",applicationArguments);
 if (printedBanner != null) {
 context.getBeanFactory().registerSingleton("springBootBanner", printedBanner);
 }
// Load the sources
 Set<Object> sources = getSources();
 Assert.notEmpty(sources, "Sources must not be empty");
 load(context, sources.toArray(new Object[sources.size()]));
 listeners.contextLoaded(context);
}
```

9)最核心的一步，将之前通过@EnableAutoConfiguration获取的所有配置以及其他形式的IoC容器配置加载到已经准备完毕的ApplicationContext。

```
private void prepareAnalyzer(ConfigurableApplicationContext context,FailureAnalyzer analyzer) {
 if (analyzer instanceof BeanFactoryAware) {
 ((BeanFactoryAware) analyzer).setBeanFactory(context.getBeanFactory());
 }
}
```

10） 遍历调用所有SpringApplicationRunListener的contextLoaded()方法。

```
public void contextLoaded(ConfigurableApplicationContext context) {
 for (SpringApplicationRunListener listener : this.listeners) {
 listener.contextLoaded(context);
 }
}
```

11) 调用ApplicationContext的refresh()方法，完成IoC容器可用的最后一道工序。

```
private void refreshContext(ConfigurableApplicationContext context) {
 refresh(context);
 if (this.registerShutdownHook) {
 try {
 context.registerShutdownHook();
 }catch (AccessControlException ex) {
 // Not allowed in some environments.
 }
 }
}
```

12） 查找当前ApplicationContext中是否注册有CommandLineRunner，如果有，则遍历执行它们。

```
private void callRunners(ApplicationContext context, ApplicationArguments args) {
 List<Object> runners = new ArrayList<Object>();
 runners.addAll(context.getBeansOfType(ApplicationRunner.class).values());
 runners.addAll(context.getBeansOfType(CommandLineRunner.class).values());
 AnnotationAwareOrderComparator.sort(runners);
 for (Object runner : new LinkedHashSet<Object>(runners)) {
 if (runner instanceof ApplicationRunner) {
 callRunner((ApplicationRunner) runner, args);
 }
 if (runner instanceof CommandLineRunner) {
 callRunner((CommandLineRunner) runner, args);
 }
 }
}
```

13） 正常情况下，遍历执行SpringApplicationRunListener的finished()方法、（如果整个过程出现异常，则依然调用所有SpringApplicationRunListener的finished()方法，只不过这种情况下会将异常信息一并传入处理）

去除事件通知点后，整个流程如下：

```
public void finished(ConfigurableApplicationContext context, Throwable exception) {
 for (SpringApplicationRunListener listener : this.listeners) {
 callFinishedListener(listener, context, exception);
 }
}
```

![img](https://pic3.zhimg.com/80/v2-f141ab1b96e40846222fbb9e1c4bb102_720w.jpg)

## **总结**

到此，SpringBoot的核心组件完成了基本的解析，综合来看，大部分都是Spring框架背后的一些概念和实践方式，SpringBoot只是在这些概念和实践上对特定的场景事先进行了固化和升华，而也恰恰是这些固化让我们开发基于Sping框架的应用更加方便高效。