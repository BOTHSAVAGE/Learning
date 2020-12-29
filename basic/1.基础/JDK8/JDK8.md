使用Lambda表达式，实际就是创建出该接口的实例对象

![img](https://pic4.zhimg.com/80/v2-a712753b42972e094a548ae02fa82987_720w.jpg？) 

这个可以赋值给变量的方法 ，被称为lambda表达式

**Lambda表达式本身就是一个接口的实现** 

![img](https://pic4.zhimg.com/80/v2-55de66060b4cb70193ddc7fea201b257_720w.jpg？) 

这种只有一个接口函数需要被实现的接口类型，我们叫"函数式接口"，和普通接口区分使用@FunctionalInterface

![img](https://pic3.zhimg.com/80/v2-2c57e7411de227d1eb09c327d01fb766_720w.jpg？) 



lambda的好处：有些情况下，这个借口只需要用到一次，传统的java7必须要求定义一个污染环境的接口实现	

![img](https://pic3.zhimg.com/80/v2-28606f4328308baf7f70a36bd689e5ea_720w.jpg？) 





###### Lambda结合FunctionalInterface Lib, forEach, stream()，method reference等新特性可以使代码变的更加简洁！



在Java 8中有一个函数式接口的包，里面定义了大量可能用到的函数式接口（[java.util.function (Java Platform SE 8 )](https://link.zhihu.com/?target=https%3A//docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html)）。所以，我们在这里压根都不需要定义NameChecker和Executor这两个函数式接口，直接用Java 8函数式接口包里的Predicate<T>和Consumer<T>就可以了——因为他们这一对的接口定义和NameChecker/Executor其实是一样的。

 

 

 

 ![1606466878252](C:\Users\T440p\AppData\Local\Temp\1606466878252.png？)

 ##### 方法引用（reference method）

方法引用仅仅是Lambda的配套服务，主要目的是通过名字来获得Lambda，重复利用已有的方法。  	

Java 8中方法也是一种对象，可以By名字来引用。**不过方法引用的唯一用途是支持Lambda的简写**，使用方法名称来表示Lambda。不能通过方法引用来获得诸如方法签名的相关信息。

## 方法引用的分类

方法引用分为4类，常用的是前两种。方法引用也受到访问控制权限的限制，可以通过在引用位置是否能够调用被引用方法来判断。具体分类信息如下：

- **引用静态方法**
  ContainingClass::staticMethodName
  例子: String::valueOf，对应的Lambda：(s) -> String.valueOf(s)
  比较容易理解，和静态方法调用相比，只是把.换为::
- **引用特定对象的实例方法**
  containingObject::instanceMethodName
  例子: x::toString，对应的Lambda：() -> this.toString()
  与引用静态方法相比，都换为实例的而已
- **引用特定类型的任意对象的实例方法**
  ContainingType::methodName
  例子: String::toString，对应的Lambda：(s) -> s.toString()
  太难以理解了。难以理解的东西，也难以维护。建议还是不要用该种方法引用。
  实例方法要通过对象来调用，方法引用对应Lambda，Lambda的第一个参数会成为调用实例方法的对象。
- **引用构造函数**
  ClassName::new
  例子: String::new，对应的Lambda：() -> new String()
  构造函数本质上是静态方法，只是方法名字比较特殊。

## 方法引用的例子

```
public class MethodReference {
    public static void main(String[] args) {
        // 方法引用::引用构造函数
        PersonFactory factory = new PersonFactory(Person::new);

        List<Person> personList = new ArrayList<Person>();

        Person p1 = factory.getPerson();
        p1.setName("Kobe");
        personList.add(p1);
        Person p2 = factory.getPerson();
        p2.setName("James");
        personList.add(p2);
        Person p3 = factory.getPerson();
        p3.setName("Paul");
        personList.add(p3);

        Person[] persons1 = personList.toArray(new Person[personList.size()]);
        System.out.print("排序前: ");
        printArray(persons1);

        // 方法引用::引用静态方法
        Arrays.sort(persons1, MethodReference::myCompare);
        System.out.print("排序后: ");
        printArray(persons1);
        System.out.println();

        Person[] persons2 = personList.toArray(new Person[personList.size()]);
        System.out.print("排序前: ");
        printArray(persons2);

        // 方法引用::用特定对象的实例方法
        Arrays.sort(persons2, p1::compare);
        System.out.print("排序后: ");
        printArray(persons2);
        System.out.println();

        Person[] persons3 = personList.toArray(new Person[personList.size()]);
        System.out.print("排序前: ");
        printArray(persons3);

        // 方法引用::引用特定类型的任意对象的实例方法
        Arrays.sort(persons3, Person::compareTo);
        System.out.print("排序后: ");
        printArray(persons3);
    }

    public static void printArray(Person[] persons) {
        for (Person p : persons) {
            System.out.print(p.name + "  ");
        }
        System.out.println();
    }

    public static int myCompare(Person p1, Person p2) {
        return p1.getName().compareTo(p2.getName());
    }

    static class Person {
        private String name;

        public Person() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int compare(Person p1, Person p2) {
            return p1.getName().compareTo(p2.getName());
        }

        public int compareTo(Person p) {
            return this.getName().compareTo(p.getName());
        }
    }

    static class PersonFactory {
        private Supplier<Person> supplier;

        public PersonFactory(Supplier<Person> supplier) {
            this.supplier = supplier;
        }

        public Person getPerson() {
            return supplier.get();
        }
    }
}
```

**1、语言新特性**
1.1接口新增默认方法与静态方法

1.1.1  Interface Default Method：For creating a default method in java interface, we need to use “**default**” keyword with the method signature. For example:

```
package com.java8.defaultmethod;



 



public interface Interface1 {



 



	void method1(String str);



	//a default method



	default void log(String str){



		System.out.println("I1 logging::"+str);



	}



}
```

*Notes:类继承多个Interface接口同名方法(如show())时，必须在子类中@Override重写父类show()方法。*

1.1.2 Interface Static Method：interface static method is similar to default method except that we can’t override them in the implementation classes.for example:

```
package com.java8.staticmethod;



public interface MyData {



        static boolean isNull(String str) {



	 System.out.println("Interface Null Check");



	 return str == null ? true : "".equals(str) ? true : false;



        }



}
```

1.1.2.1 Functional Interfaces:含有一个显式声明函数（抽象方法）的接口称为函数接口，注释@FunctionalInterface用作检查代码块,包package `java.util.function，`通常使用lambda expressions来实体化函数接口，for example：

![img](https://img-blog.csdnimg.cn/20190621163334711.png)

**特性说明：**

​    1,函数式接口仅仅只有一个方法(非默认或静态方法)，用于显示转换成ladbma表达式。

​    2, java.lang.Runnable接口 java.util.concurrent.Callable接口是两个最典型的函数式接口。

​    3.如果一个函数式接口添加一个普通方法，就变成了非函数式接口（一般定义的接口）。

​              4.Jdk8 规范里添加了注解@FunctionalInterface来限制函数式接口不能修改为普通的接口.

  **新增了四个重要的函数式接口：****函数****形接口 、****供给形****接口****、****消费型****接口、判断型接口**

​         

![img](https://img-blog.csdnimg.cn/20190621163702954.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2p1bjU1eGl1,size_16,color_FFFFFF,t_70)

 

1.2 新增Lambda表达式

 1.2.1 Lambda表达式（基于函数的匿名表达式）

   ![img](https://img-blog.csdnimg.cn/20190621164101420.png)

  语法：( object str,....)[参数列表]   ->[箭头符号]     代码块或表达式

  **特性：**Lambda 的类型是从使用 Lambda 的上下文推断出来的。上下文中 Lambda 表达式需要的类型称为目标类型（一个 Lambda表达式所在的类的类型。并且必须存在一个目标类型）;  匿名、函数、传递、简洁。

1.3 新增方法引用

1)**构造器引用**

​             ![img](https://img-blog.csdnimg.cn/20190621165349665.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2p1bjU1eGl1,size_16,color_FFFFFF,t_70)

**2)****静态方法引用**

​               ![img](https://img-blog.csdnimg.cn/20190621165422770.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2p1bjU1eGl1,size_16,color_FFFFFF,t_70)

**3)****类****(****任意对象****)****的方法引用**

​                 ![img](https://img-blog.csdnimg.cn/20190621165438265.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2p1bjU1eGl1,size_16,color_FFFFFF,t_70)

**4)****实例对象的方法引用**

​            ![img](https://img-blog.csdnimg.cn/20190621165505167.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2p1bjU1eGl1,size_16,color_FFFFFF,t_70)

**5)****数组引用**

​           ![img](https://img-blog.csdnimg.cn/20190621165521685.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2p1bjU1eGl1,size_16,color_FFFFFF,t_70)

1.4 重复注解

​       Java 5引入了[注解](http://www.javacodegeeks.com/2012/08/java-annotations-explored-explained.html)机制，这一特性就变得非常流行并且广为使用。然而，使用注解的一个限制是相同的注解在同一位置只能声明一次，不能声明多次。Java 8打破了这条规则，引入了重复注解机制，这样相同的注解可以在同一地方声明多次。重复注解机制本身必须用@Repeatable注解.

​              ![img](https://img-blog.csdnimg.cn/20190621170442151.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2p1bjU1eGl1,size_16,color_FFFFFF,t_70)

1.5 扩展注解的支持

​      jdk 8扩展了注解的上下文。现在几乎可以为任何东西添加注解：局部变量、泛型类、父类与接口的实现，就连方法的异常也能添加注解。

​              ![img](https://img-blog.csdnimg.cn/20190621170600607.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2p1bjU1eGl1,size_16,color_FFFFFF,t_70)