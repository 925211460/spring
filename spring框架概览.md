# spring框架概览

## 我们说的spring指的是什么

最开始spring指的是spring framwork，随着时间的推移，其他Spring项目已经建立在Spring框架之上。

Spring框架为不同的应用程序体系结构提供了基础支持，包括消息传递，事务数据和持久性以及Web。它还包括基于Servlet的Spring MVC Web框架，以及Spring WebFlux反应式Web框架。

关于模块的注意事项：Spring的框架jar允许部署到JDK 9的模块路径（“Jigsaw”），为了在使用Jigsaw的应用程序中使用，Spring框架5 jar包含了“Automatic-Module-Name”清单条目，这些清单条目定义了与jar的artifact (名称中间以-分隔)无关的稳定的语言级别模块名称（例如“spring.core”，“spring.context”等）名称，



## Spring和Spring框架的历史

事实上，Spring是Java EE的补充。 Spring编程模型不支持Java EE平台规范;但它集成了精心挑选的EE的个别规格：

- Servlet API ([JSR 340](https://jcp.org/en/jsr/detail?id=340))
- WebSocket API ([JSR 356](https://www.jcp.org/en/jsr/detail?id=356))
- Concurrency Utilities ([JSR 236](https://www.jcp.org/en/jsr/detail?id=236))
- JSON Binding API ([JSR 367](https://jcp.org/en/jsr/detail?id=367))
- Bean Validation ([JSR 303](https://jcp.org/en/jsr/detail?id=303))
- JPA ([JSR 338](https://jcp.org/en/jsr/detail?id=338))
- JMS ([JSR 914](https://jcp.org/en/jsr/detail?id=914))
- as well as JTA/JCA setups for transaction coordination, if necessary.

Spring框架还支持依赖注入（JSR 330）和通用注释（JSR 250）规范，应用程序开发人员可以选择使用这些规范来代替Spring框架提供的Spring特定的机制。

今天，在Spring Boot的帮助下，应用程序以devops和云友好的方式创建，Servlet容器被嵌入并且变得微不足道。从Spring Framework 5开始，WebFlux应用程序甚至不直接使用Servlet API，而是可以在不是Servlet容器的服务器（如Netty）上运行.

除了Spring框架之外，还有其他一些项目，比如Spring Boot，Spring Security，Spring Data，Spring Cloud，Spring Batch等等。记住每个项目都有自己的源代码库，问题跟踪器和释放节奏是很重要的。有关Spring项目的完整列表，请参阅spring.io/projects。



## 设计理念

在每个级别提供选择。 Spring允许您尽可能迟地推迟设计决策。例如，您可以通过配置来切换持久性提供程序，而无需更改代码。许多其他基础架构问题以及与第三方API的集成也是如此.



# spring 核心技术

参考文档的这一部分涵盖了Spring Framework绝对不可或缺的所有技术。

其中最重要的是Spring框架的控制反转（IoC）容器，全面涵盖了Spring的面向方面编程（AOP）技术紧随其后，Spring框架拥有自己的AOP框架，这个框架在概念上很容易理解，并且成功地解决了Java企业编程中80％的AOP需求。

# 1、IoC容器

## 1.1 Spring IoC容器和bean介绍



IoC也被称为依赖注入（DI），这是一个对象通过构造函数参数，工厂方法的参数或在工厂方法构造或返回的对象实例上设置的属性来定义它们它们所处理的其他对象的依赖关系的过程。然后容器在创建bean时注入这些依赖。这个过程从根本上来说是相反的，因此称为控制Inversion of Control（IoC），bean本身通过使用类的直接构造或者像Service Locator模式的机制来控制其依赖的实例化或位置。

org.springframework.beans和org.springframework.context包是Spring Framework的IoC容器的基础。 BeanFactory接口提供了一种高级配置机制，可以管理任何类型的对象。 ApplicationContext是BeanFactory的一个子接口。它增加了与Spring的AOP功能更容易的集成;消息资源处理（用于国际化），事件发布;以及应用程序层特定的上下文就像WebApplicationContext，以用于Web应用程序。

总之，BeanFactory提供了配置框架和基本功能，而ApplicationContext增加了更多的企业特定功能。 ApplicationContext是BeanFactory的一个完整的超集，在本章中专门用于描述Spring的IoC容器。有关使用BeanFactory而不是ApplicationContext的更多信息，请参阅BeanFactory。

在Spring中，构成应用程序的骨干并由Spring IoC容器管理的对象称为bean。 bean是一个实例化，组装并由Spring IoC容器管理的对象。否则，bean只是应用程序中众多对象中的一个。 Bean和它们之间的依赖关系反映在容器使用的配置元数据中。

## 1.2容器概述

接口org.springframework.context.ApplicationContext代表Spring IoC容器，负责实例化，配置和组装上述bean。容器通过读取配置元数据获取什么对象要被实例化，配置和组装。配置元数据以XML，Java注解或Java代码表示。它允许你表达组成你的应用程序的对象以及这些对象之间丰富的相互依赖关系。

Spring提供了几个ApplicationContext接口的实现。在独立应用程序中，通常会创建ClassPathXmlApplicationContext或FileSystemXmlApplicationContext的实例。虽然XML是用于定义配置元数据的传统格式，但您可以指示容器使用Java注解或代码作为元数据格式，通过提供少量的XML配置以声明支持这些额外的元数据格式。

在大多数应用场景中，显式用户代码不需要实例化Spring IoC容器的一个或多个实例。例如，在Web应用程序场景中，应用程序的web.xml文件中的简单八行（或多行）样板Web描述符XML通常就足够了（请参阅Web应用程序的便捷ApplicationContext实例化）。如果您使用的是Spring工具套件Eclipse驱动的开发环境，则只需点击几下鼠标或按键即可轻松创建样板配置。

下图是Spring如何工作的高级视图。您的应用程序类与配置元数据结合在一起，在创建并初始化ApplicationContext之后，您将拥有完全配置且可执行的系统或应用程序。

### 1.2.1配置元数据

如上图所示，Spring IoC容器使用一种形式的配置元数据;这个配置元数据表示作为应用程序开发人员如何告诉Spring容器在应用程序中实例化，配置和组装对象。

传统上，配置元数据是以简单而直观的XML格式提供的，这是本章的大部分内容用来传达Spring IoC容器的关键概念和功能采用的方式。

基于XML的元数据不是唯一允许的配置元数据形式。 Spring IoC容器本身与配置元数据实际写入的格式完全分离。现在许多开发人员为他们的Spring应用程序选择基于Java的配置

有关在Spring容器中使用其他形式的元数据的信息，请参阅：

基于注释的配置：Spring 2.5引入了对基于注释的配置元数据的支持。

基于Java的配置：从Spring 3.0开始，Spring JavaConfig项目提供的许多功能成为核心Spring框架的一部分。因此，您可以使用Java而不是XML文件来定义应用程序类外部的Bean。要使用这些新功能，请参阅@Configuration，@Bean，@Import和@DependsOn注释。

Spring的配置包含至少一个，一般是多个bean定义组成。基于XML的配置元数据将这些bean配置为顶级<beans />元素内的<bean />元素。 基于Java配置通常在@Configuration类中使用@Bean注释的方法。

这些bean定义对应于组成应用程序的实际对象。通常，您可以定义服务层对象，数据访问对象（DAO），Struts Action实例等表示对象，Hibernate SessionFactory，JMS队列等基础结构对象。通常，不会在容器中配置细粒度的域对象，因为创建和加载域对象通常是DAO和业务逻辑的责任。但是，您可以使用Spring与AspectJ的集成来配置在IoC容器控制之外创建的对象。请参阅使用AspectJ以依赖注入域对象与Spring。

以下示例显示了基于XML的配置元数据的基本结构

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
                http://www.springframework.org/schema/beans/spring-beans.xsd">

        <bean id="..." class="...">
                <!-- collaborators and configuration for this bean go here -->
        </bean>

        <bean id="..." class="...">
                <!-- collaborators and configuration for this bean go here -->
        </bean>

        <!-- more bean definitions go here -->

</beans>
```

id属性是用来标识单个bean定义的字符串。 class属性定义了bean的类型并使用完全限定的类名。 id属性的值是指协作对象。本示例中未显示用于引用协作对象的XML;请参阅依赖关系以获取更多信息。

### 1.2.2实例化一个容器

实例化一个Spring IoC容器很简单。提供给ApplicationContext构造函数的位置路径实际上是资源字符串，它允许容器从各种外部资源（如本地文件系统，Java CLASSPATH等等）加载配置元数据。

````java
ApplicationContext context = new ClassPathXmlApplicationContext("services.xml", "daos.xml");
````

在了解了Spring的IoC容器之后，您可能想了解更多关于Spring的 [Resources](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#resources)抽象的知识，如参考资料中所述，它提供了一个以URI语法定义的位置读取InputStream的方便机制。具体来说，`Resource` paths用于构建应用程序上下文，如[Application contexts and Resource paths](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#resources-app-ctx)中所述。

以下示例显示服务层对象（services.xml）配置文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
                http://www.springframework.org/schema/beans/spring-beans.xsd">

        <!-- services -->

        <bean id="petStore" class="org.springframework.samples.jpetstore.services.PetStoreServiceImpl">
                <property name="accountDao" ref="accountDao"/>
                <property name="itemDao" ref="itemDao"/>
                <!-- additional collaborators and configuration for this bean go here -->
        </bean>

        <!-- more bean definitions for services go here -->

</beans>
```

以下示例显示数据访问对象daos.xml文件:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
                http://www.springframework.org/schema/beans/spring-beans.xsd">

        <bean id="accountDao"
                class="org.springframework.samples.jpetstore.dao.jpa.JpaAccountDao">
                <!-- additional collaborators and configuration for this bean go here -->
        </bean>

        <bean id="itemDao" class="org.springframework.samples.jpetstore.dao.jpa.JpaItemDao">
                <!-- additional collaborators and configuration for this bean go here -->
        </bean>

        <!-- more bean definitions for data access objects go here -->

</beans>
```

在前面的例子中，服务层由PetStoreServiceImpl类和JpaAccountDao和JpaItemDao类型的两个数据访问对象（基于JPA对象/关系映射标准）组成。property name元素是指JavaBean属性的名称，ref元素是指另一个bean定义的名称。 id和ref元素之间的这种联系表示协作对象之间的依赖关系。有关配置对象依赖关系的详细信息，请参阅依赖关系。

**编写基于XML的配置元数据**

让bean定义跨越多个XML文件是有用的。通常，每个单独的XML配置文件都代表了架构中的逻辑层或模块

您可以使用应用程序上下文构造函数从所有这些XML片段中加载bean定义。这个构造函数有多个资源位置，如前一节所示。或者，使用一个或多个<import />元素从另一个或多个文件加载bean定义。例如：

```xml
<beans>
        <import resource="services.xml"/>
        <import resource="resources/messageSource.xml"/>
        <import resource="/resources/themeSource.xml"/>

        <bean id="bean1" class="..."/>
        <bean id="bean2" class="..."/>
</beans>
```

在前面的示例中，外部bean定义从三个文件中加载：services.xml，messageSource.xml和themeSource.xml。所有的位置路径都是相对于导入的定义文件而言的，所以services.xml必须和导入的文件位于相同的目录或类路径位置，而messageSource.xml和themeSource.xml必须位于位置之下的资源位置的导入文件。正如你所看到的，一个前导的斜线被忽略，但是鉴于这些路径是相对的，最好不要使用斜线。正在导入的文件的内容（包括顶级<beans />元素）必须是根据Spring架构的有效XML bean定义。

有可能，但不建议使用相对的“../”路径引用父目录中的文件。这样做会创建对当前应用程序之外的文件的依赖关系。特别是，不建议将此引用用于“classpath：”URL（例如“classpath：../ services.xml”），其中运行时解析过程选择“最近”的类路径根，然后查看其父目录。类路径配置更改可能会导致选择不同的，不正确的目录。

您始终可以使用完全限定的资源位置而不是相对路径：例如“file：C：/config/services.xml”或“classpath：/config/services.xml”。但是，请注意，您将应用程序的配置耦合到特定的绝对位置。通常最好保持这种绝对位置的间接性，例如通过在运行时针对JVM系统属性解析的“$ {...}”占位符

import指令是bean名称空间本身提供的一个功能。除了普通的bean定义之外的其他配置特征可以在由Spring提供的XML命名空间的选择中获得，例如， ` "context" and the "util"`命名空间。

**Groovy Bean定义DSL**

作为外部化配置元数据的另一个例子，bean定义也可以在Spring的Groovy Bean Definition DSL中表示，如Grails框架中所知。通常情况下，这样的配置将存在于“.groovy”文件中，结构如下：

```groovy
beans {
    dataSource(BasicDataSource) {
        driverClassName = "org.hsqldb.jdbcDriver"
        url = "jdbc:hsqldb:mem:grailsDB"
        username = "sa"
        password = ""
        settings = [mynew:"setting"]
    }
    sessionFactory(SessionFactory) {
        dataSource = dataSource
    }
    myService(MyService) {
        nestedBean = { AnotherBean bean ->
            dataSource = dataSource
        }
    }
}
```

这种配置风格在很大程度上等价于XML bean定义，甚至支持Spring的XML配置名称空间。它还允许通过“importBeans”指令导入XML bean定义文件。

### 1.2.3使用容器

ApplicationContext是高级工厂的接口，能够维护不同bean及其依赖项的注册表。使用方法`T getBean（String name，Class <T> requiredType）`，可以检索bean的实例。

ApplicationContext使您可以读取bean定义并按如下方式访问它们：

```java
// create and configure beans
ApplicationContext context = new ClassPathXmlApplicationContext("services.xml", "daos.xml");

// retrieve configured instance
PetStoreService service = context.getBean("petStore", PetStoreService.class);

// use configured instance
List<String> userList = service.getUsernameList();
```

使用Groovy配置，bootstrapping看起来非常相似，只是一个Groovy感知的不同的上下文实现类（但也理解XML bean定义）：

```java
ApplicationContext context = new GenericGroovyApplicationContext("services.groovy", "daos.groovy");
```

最灵活的变体是GenericApplicationContext与读者代表组合，使用XML文件的XmlBeanDefinitionReader：

```java
GenericApplicationContext context = new GenericApplicationContext();
new XmlBeanDefinitionReader(context).loadBeanDefinitions("services.xml", "daos.xml");
   context.refresh();
```

或者使用Groovy文件的GroovyBeanDefinitionReader：

```groovy
GenericApplicationContext context = new GenericApplicationContext();
new GroovyBeanDefinitionReader(context).loadBeanDefinitions("services.groovy", "daos.groovy");
   context.refresh();
```

这样的阅读器代表可以在相同的ApplicationContext上混合和匹配，如果需要，可以从不同的配置源读取bean定义。

然后可以使用getBean来检索bean的实例。 ApplicationContext接口还有其他一些检索bean的方法，但理想情况下应用程序代码不应该使用它们。事实上，你的应用程序代码根本就不应该调用getBean（）方法，因此完全不依赖于Spring API。例如，Spring与Web框架的集成为各种Web框架组件（如控制器和JSF托管的Bean）提供了依赖注入，允许您通过元数据（例如自动装配注释）声明对特定Bean的依赖关系。

## 1.3 Bean概述

Spring IoC容器管理一个或多个bean。这些bean是使用您提供给容器的配置元数据创建的，例如，以XML <bean />定义的形式。

在容器本身中，这些bean定义表示为BeanDefinition对象，其中包含以下元数据（以及其他信息）：

1. 包限定的类名：通常是被定义的bean的实际实现类。
2. Bean行为配置元素，这些元素说明bean在容器中的行为（范围，生命周期回调等等）。
3. 引用bean所需的其他bean来完成其工作;这些引用也被称为协作者或依赖关系。
4. 在新创建的对象中设置的其他配置设置，例如，在管理连接池的bean中使用的连接数，或池的大小限制。

这个元数据转化为一组构成每个bean定义的属性。

| Property                 | Explained in…                            |
| ------------------------ | ---------------------------------------- |
| class                    | [Instantiating beans](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-class) |
| name                     | [Naming beans](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-beanname) |
| scope                    | [Bean scopes](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-scopes) |
| constructor arguments    | [Dependency Injection](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-collaborators) |
| properties               | [Dependency Injection](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-collaborators) |
| autowiring mode          | [Autowiring collaborators](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-autowire) |
| lazy-initialization mode | [Lazy-initialized beans](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-lazy-init) |
| initialization method    | [Initialization callbacks](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-lifecycle-initializingbean) |
| destruction method       | [Destruction callbacks](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-lifecycle-disposablebean) |

除了包含有关如何创建特定bean的信息的bean定义外。`ApplicationContext实现还允许用户注册在容器外部创建的现有对象。这是通过getBeanFactory（）方法访问ApplicationContext的BeanFactory来完成的，该方法返回BeanFactory实现的DefaultListableBeanFactory。 DefaultListableBeanFactory通过方法registerSingleton（..）和registerBeanDefinition（..）来支持这种注册`。但是，典型的应用程序只能通过元数据bean定义来定义bean。

*Bean元数据和手动提供的单例实例需要尽可能早地注册，以便容器在自动装配和其他内省步骤中正确推理它们。虽然重写现有的元数据和现有的单例实例在一定程度上得到了支持，但是在运行时注册新的Bean（同时实时访问工厂）并没有得到正式的支持，并且可能导致bean容器中的并发访问异常和/或不一致的状态。*

### 1.3.1 bean的命名

每个bean都有一个或多个标识符。这些标识符在托管bean的容器中必须是唯一的。一个bean通常只有一个标识符，但是如果它需要多个标识符，额外的可以被认为是别名。

在基于XML的配置元数据中，使用id和/或name属性来指定bean标识符。 id属性允许你指定一个id。通常，这些名称是字母（'myBean'，'fooService'等），但也可能包含特殊字符。如果您想向bean引入其他别名，也可以在name属性中指定它们，用逗号（，），分号（;）或空格分隔。作为一个历史记录，在Spring 3.1以前的版本中，id属性被定义为一个xsd：ID类型，它限制了可能的字符。从3.1开始，它被定义为一个xsd：string类型。请注意，bean id唯一性仍然由容器强制执行，尽管不再由XML解析器执行。

您不需要为bean提供名称或标识。如果没有显式提供名称或标识，则容器为该bean生成一个唯一的名称。但是，如果要通过名称引用该bean，通过使用ref元素或Service Locator样式查找，必须提供一个名称。不提供名称的动机与使用内部bean和自动装配协作者有关。

通过类路径中的组件扫描，Spring为未命名的组件生成bean名称，遵循以上规则：本质上，采用简单的类名称并将其初始字符变为小写。但是，在不常见的特殊情况下，如果有多个字符，并且第一个字符和第二个字符都是大写字母，则会保留原始的外壳。这些和java.beans.Introspector.decapitalize（Spring在这里使用的）定义的规则相同。

#### 在bean定义之外为一个bean设置别名

在bean定义本身中，可以通过使用由id属性指定的最多一个名称和name属性中的任意数量的其他名称的组合来为bean提供多个名称。这些名称可以等同于同一个bean的别名，并且在某些情况下非常有用，例如允许应用程序中的每个组件通过使用特定于该组件本身的bean名称来引用公共依赖项。

但是，在指定bean实际定义的地方定义所有别名并不总是足够的。有时候需要为其他地方定义的bean引入一个别名。在大型系统中，在每个子系统之间分配配置的情况通常是这种情况，每个子系统都有自己的一组对象定义。在基于XML的配置元数据中，可以使用<alias />元素来完成此操作。

```xml
<alias name="fromName" alias="toName"/>
```

在这种情况下，同名容器中名为fromName的bean也可以在使用这个别名定义之后被称为toName。

例如，子系统A的配置元数据可以通过名称subsystemA-dataSource来引用数据源。子系统B的配置元数据可以通过名称subsystemB-dataSource引用数据源。在编写使用这两个子系统的主应用程序时，主应用程序通过名称myApp-dataSource引用数据源。要使所有三个名称都与您添加到MyApp配置元数据中的同一个对象相关联，请使用以下别名定义：

```xml
<alias name="subsystemA-dataSource" alias="subsystemB-dataSource"/>
<alias name="subsystemA-dataSource" alias="myApp-dataSource" />
```

现在，每个组件和主应用程序都可以通过唯一的名称引用dataSource，并保证不与其他任何定义冲突（有效地创建名称空间），但它们引用同一个bean。

如果您正在使用Java配置，则可以使用@Bean注释来提供别名，请参阅[Using the @Bean annotation](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-java-bean-annotation)以获取详细信息。

### 1.3.2实例化bean

一个bean定义本质上是一个创建一个或多个对象的配置。当容器被访问时，该容器查看命名的bean的配置，并使用由该bean定义封装的配置元数据来创建（或获取）实际的对象。

如果使用基于XML的配置元数据，则可以指定要在<bean />元素的class属性中实例化的对象的类型（或类）。这个类属性在内部是一个BeanDefinition实例的Class属性，通常是强制的。 （有关例外情况，请参阅 [Instantiation using an instance factory method](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-class-instance-factory-method)和 [Bean definition inheritance](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-child-bean-definitions)。）您可以通过以下两种方式之一使用Class属性：

- 通常，在容器本身通过反射调用其构造函数直接创建bean的情况下，指定要构造的bean类，这与使用new运算符的Java代码有些相同。
- 要指定包含用来创建对象的静态工厂方法的实际类，在容器调用类的静态工厂方法来创建该bean的情况较少。调用静态工厂方法返回的对象类型可以是完全相同的类或另一个类。

```
内部类名称如果要为静态嵌套类配置一个bean定义，则必须使用嵌套类的二进制名称。例如，如果在com.example包中有一个名为Foo的类，并且此Foo类有一个名为Bar的静态嵌套类，那么bean定义上的“class”属性的值将是...com.example.Foo $Bar注意在名称中使用字符来将嵌套类名与外部类名分开
```

#### 用构造函数实例化

当你通过构造函数的方法创建一个bean时，所有的普通类都可以被Spring使用和兼容。也就是说，正在开发的类不需要实现任何特定的接口或以特定的方式编码。只需指定bean类就足够了。但是，根据您用于特定bean的IoC类型，您可能需要一个默认（空）构造函数。	

Spring IoC容器几乎可以管理任何你想要管理的类，它不限于管理真正的JavaBeans。大多数Spring用户更喜欢实际的JavaBeans，它只有一个默认的（无参数）构造函数，以及在容器中的属性之后建模的合适的setter和getter。你也可以在你的容器中有更多非bean风格的类。例如，如果您需要使用绝对不符合JavaBean规范的传统连接池，Spring也可以管理它。

有关为构造函数提供参数（如果需要）和在构造对象后设置对象实例属性的机制的详细信息，请参阅[Injecting Dependencies](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-collaborators)。

#### 使用静态工厂方法实例化

在定义使用静态工厂方法创建的bean时，可以使用class属性来指定包含静态工厂方法的类和factory-method的属性，以指定工厂方法本身的名称。你应该可以调用这个方法（使用后面描述的可选参数）并返回一个活动对象，这个活动对象随后被视为是通过构造函数创建的。这种bean定义的一个用途是在传统代码中调用静态工厂。

下面的bean定义指定了通过调用工厂方法创建的bean。该定义没有指定返回对象的类型（类），而只指定了包含工厂方法的类。在这个例子中，createInstance（）方法必须是一个静态方法。

```xml
<bean id="clientService"
        class="examples.ClientService"
        factory-method="createInstance"/>
```

```java
public class ClientService {
        private static ClientService clientService = new ClientService();
        private ClientService() {}

        public static ClientService createInstance() {
                return clientService;
        }
}
```

有关从工厂返回对象之后向工厂方法提供（可选）参数和设置对象实例属性的机制的详细信息，请参阅[Dependencies and configuration in detail](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-properties-detailed)

#### 使用实例工厂方法实例化

与通过静态工厂方法实例化类似，使用实例工厂方法的实例化从容器调用现有bean的非静态方法来创建新的bean。要使用这种机制，请将类属性留空，并在factory-bean属性中指定当前（或父/祖代）容器中包含创建目标bean的工厂方法的工厂类的名称。使用factory-method属性设置工厂方法本身的名称。

```xml
<!-- the factory bean, which contains a method called createInstance() -->
<bean id="serviceLocator" class="examples.DefaultServiceLocator">
        <!-- inject any dependencies required by this locator bean -->
</bean>

<!-- the bean to be created via the factory bean -->
<bean id="clientService"
        factory-bean="serviceLocator"
        factory-method="createClientServiceInstance"/>
```

```java
public class DefaultServiceLocator {

        private static ClientService clientService = new ClientServiceImpl();

        public ClientService createClientServiceInstance() {
                return clientService;
        }
}
```

一个工厂类也可以拥有多个工厂方法，如下所示：

```xml
<bean id="serviceLocator" class="examples.DefaultServiceLocator">
        <!-- inject any dependencies required by this locator bean -->
</bean>

<bean id="clientService"
        factory-bean="serviceLocator"
        factory-method="createClientServiceInstance"/>

<bean id="accountService"
        factory-bean="serviceLocator"
        factory-method="createAccountServiceInstance"/>
```

```java
public class DefaultServiceLocator {

        private static ClientService clientService = new ClientServiceImpl();

        private static AccountService accountService = new AccountServiceImpl();

        public ClientService createClientServiceInstance() {
                return clientService;
        }

        public AccountService createAccountServiceInstance() {
                return accountService;
        }
}
```

这种方法表明，工厂bean本身可以通过依赖注入（DI）进行管理和配置。详见[Dependencies and configuration in detail](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-properties-detailed).

*在Spring文档中，工厂bean指的是在Spring容器中配置的bean，它将通过实例或静态工厂方法创建对象。相比之下，FactoryBean（注意大写字母）是指特定于Spring的FactoryBean。*

## 1.4。依赖

一个典型的企业应用程序不是由一个单一的对象组成（或Spring的说法中的bean）。即使是最简单的应用程序也有几个对象一起工作来呈现最终用户看作是一个连贯的应用程序。下一节将介绍如何从定义许多独立的bean定义到完全实现的应用程序，在这些应用程序中对象协作实现目标。

### 1.4.1 依赖注入

依赖注入（DI）是一个对象定义它们的依赖（这些对象工作时需要的其它对象）的过程，通过这个过程，对象可以通过构造函数参数，工厂方法的参数或者在构造或返回对象实例后设置的属性来定义它们的依赖关系。然后容器在创建bean时注入这些依赖关系。这个过程从根本上说与bean本身通过使用类的 construction或*Service Locator* pattern来控制它自己的依赖关系的实例化或位置是相反的，因此名为控制反转（IoC）。

代码因为有了DI变得更加清晰，当对象提供依赖时，解耦也更为有效。对象不查找它的依赖关系，不知道依赖关系的位置或类。因此，您的类变得更容易测试，特别是当依赖关系在接口或抽象基类上时，DI允许在单元测试中使用stub or mock implementations.

DI存在两种主要的变体，[Constructor-based dependency injection](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-constructor-injection) and [Setter-based dependency injection](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-setter-injection).

#### 基于构造函数的依赖注入

基于构造器的DI通过容器调用具有多个参数的构造器来完成，每个参数表示一个依赖关系。调用一个具有特定参数的静态工厂方法来构造这个bean几乎是等价的，这个讨论对于构造函数或者静态工厂方法的参数一样的。以下示例显示了只能通过构造函数注入进行依赖注入的类。请注意，这个类没有什么特别之处，它是一个POJO，它不依赖容器特定的接口，基类或注释。、

```java
public class SimpleMovieLister {

        // the SimpleMovieLister has a dependency on a MovieFinder
        private MovieFinder movieFinder;

        // a constructor so that the Spring container can inject a MovieFinder
        public SimpleMovieLister(MovieFinder movieFinder) {
                this.movieFinder = movieFinder;
        }

        // business logic that actually uses the injected MovieFinder is omitted...
}
```

**构造函数的参数解析**

构造函数参数匹配使用参数的类型进行匹配。如果bean定义的构造函数参数中不存在潜在的歧义，那么在bean定义中定义构造函数参数的顺序就是当实例化bean时这些参数被提供给适当构造函数的顺序。考虑以下类：

```java
package x.y;

public class Foo {

        public Foo(Bar bar, Baz baz) {
                // ...
        }
}
```

假设Bar和Baz类没有继承关系，那么就不存在潜在的歧义。因此，以下配置可以正常工作，并且不需要在<constructor-arg />元素中明确指定构造函数参数索引和/或类型。

```java
<beans>
        <bean id="foo" class="x.y.Foo">
                <constructor-arg ref="bar"/>
                <constructor-arg ref="baz"/>
        </bean>

        <bean id="bar" class="x.y.Bar"/>

        <bean id="baz" class="x.y.Baz"/>
</beans>
```

当引用另一个bean时，该类型是已知的，并且可以发生匹配（就像前面的例子那样）。但是当使用一个简单的类型时，如<value> true </ value>，Spring不能确定值的类型，所以不能在没有帮助的情况下按类型进行匹配。考虑以下类：

```java
package examples;

public class ExampleBean {

        // Number of years to calculate the Ultimate Answer
        private int years;

        // The Answer to Life, the Universe, and Everything
        private String ultimateAnswer;

        public ExampleBean(int years, String ultimateAnswer) {
                this.years = years;
                this.ultimateAnswer = ultimateAnswer;
        }
}
```

**构造函数参数类型匹配**

在前面的场景中，如果使用type属性显式指定构造函数参数的类型，则容器可以使用简单类型的类型匹配。例如：

```xml
<bean id="exampleBean" class="examples.ExampleBean">
        <constructor-arg type="int" value="7500000"/>
        <constructor-arg type="java.lang.String" value="42"/>
</bean>
```

**构造函数参数索引**

使用index属性来明确指定构造函数参数的索引。例如：

```xml
<bean id="exampleBean" class="examples.ExampleBean">
        <constructor-arg index="0" value="7500000"/>
        <constructor-arg index="1" value="42"/>
</bean>
```

除了解决多个简单值的歧义之外，指定索引还解决了构造函数具有相同类型的两个参数的不明确性。请注意，该索引是基于0的。

**构造函数的参数名称**

您也可以使用构造函数参数名称来消除歧义：

```xml
<bean id="exampleBean" class="examples.ExampleBean">
        <constructor-arg name="years" value="7500000"/>
        <constructor-arg name="ultimateAnswer" value="42"/>
</bean>
```

请记住，为了使这项工作脱离您的代码，必须使用调试标志进行编译，以便Spring可以从构造函数中查找参数名称。如果你不能用调试标志编译你的代码（或者不想），你可以使用@ConstructorProperties JDK注释来明确地指定你的构造函数参数。示例类将如下所示：

```java
package examples;

public class ExampleBean {

        // Fields omitted

        @ConstructorProperties({"years", "ultimateAnswer"})
        public ExampleBean(int years, String ultimateAnswer) {
                this.years = years;
                this.ultimateAnswer = ultimateAnswer;
        }
}
```

#### **基于Setter的依赖注入**

在调用无参数构造函数或无参数静态工厂方法来实例化您的bean之后，基于Setter的依赖注入通过容器调用bean上的setter方法完成注入。

以下示例显示了一个只能使用纯setter注入进行依赖注入的类。这个类是传统的Java。这是一个POJO，它不依赖于容器特定的接口，基类或注释。

```java
public class SimpleMovieLister {

        // the SimpleMovieLister has a dependency on the MovieFinder
        private MovieFinder movieFinder;

        // a setter method so that the Spring container can inject a MovieFinder
        public void setMovieFinder(MovieFinder movieFinder) {
                this.movieFinder = movieFinder;
        }

        // business logic that actually uses the injected MovieFinder is omitted...
}
```

ApplicationContext支持它所管理的bean的基于构造函数和基于setter的依赖注入。它也支持通过构造方法注入依赖之后，基于setter的依赖注入。您可以以BeanDefinition的形式配置依赖项，您可以使用它与PropertyEditor实例一起使用，将属性从一种格式转换为另一种格式。然而，大多数Spring用户不直接使用这些类（即以编程方式），而是使用XML bean定义，带注释的组件（即用@Component，@Controller等注释的类）或基于Java的@Configuration类的@Bean方法。然后将这些源内部转换为BeanDefinition的实例，并用于加载整个Spring IoC容器实例。

```
使用构造器注入还是setter方式注入

既然可以混合使用基于构造函数和基于setter的依赖注入，那么使用构造函数作为强制依赖的方式或使用setter方法或配置方法作为可选依赖的方式是一个很好的经验法则。请注意，可以使用setter方法上的@Required注释来使该属性成为必需的依赖项。

Spring团队通常主张构造器注入，因为它使得可以将应用程序组件实现为不可变对象，并确保所需的依赖项不为空。而且，构造器注入的组件总是返回到处于完全初始化状态的客户端（调用）代码。作为一个侧面说明，大量的构造函数参数是一种糟糕的代码，这意味着类可能有太多的责任，应该重构，以更好地解决问题的分离。

Setter注入主要只应用于可选的依赖关系，这种依赖可以在类中指定合理的默认值。否则，在代码使用依赖关系的任何地方都必须执行非空的检查。 setter注入的一个好处是setter方法使得这个类的对象可以重新配置或稍后重新注入。通过JMX MBeans进行管理因此是一个引人注目的使用情况。

使用最适合特定类的DI风格。有时，在处理没有源代码的第三方类的时候，选择适合你的方式。例如，如果第三方类不公开任何setter方法，则构造函数注入可能是DI唯一可用的形式。

```

#### 依赖解析过程

容器执行bean依赖解析如下：

- ApplicationContext使用描述所有Bean的配置元数据创建和初始化。配置元数据可以通过XML，Java代码或注释来指定。	
- 对于每个bean，它的依赖关系以属性，构造函数参数或静态工厂方法的参数的形式表示。当bean被实际创建时，这些依赖被提供给bean。
- 每个属性或构造函数参数都是要设置的值的实际定义，或者是对容器中另一个bean的引用。
- 每个属性或构造函数参数的值都从指定的格式转换为该属性或构造函数参数的实际类型。默认情况下，Spring可以将以字符串格式提供的值转换为所有内置类型，例如int，long，String，boolean等。

Spring容器在容器创建时验证每个bean的配置。但是，在实际创建bean之前，bean属性本身不会被设置。 Beans是singleton-scoped的，并且被设置为pre-instantiated（默认的）是在创建容器时创建的。Scopes在[Bean scopes](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-scopes)中定义。否则，只有在请求时才创建bean。创建一个bean可能会导致创建一个bean的集合，因为bean的依赖关系及其依赖关系的依赖性（等等）被创建和分配。请注意，这些依赖项之间的解析不匹配的情况可能会出现较晚，即首次创建受影响的bean。

```
循环依赖
如果您主要使用构造函数注入，则可能会创建一个无法解析的循环依赖性场景。例如：类A需要通过构造函数注入的类B的实例，而类B需要通过构造函数注入的类A的实例。如果将类A和B的Bean配置为相互注入，则Spring IoC容器将在运行时检测到此循环引用，并引发BeanCurrentlyInCreationException。一个可能的解决方案是编辑一些类的源代码，由setter而不是构造函数来配置。或者，避免构造函数注入，只使用setter注入。换句话说，虽然不推荐，但是可以使用setter注入配置循环依赖。与典型情况（没有循环依赖）不同，bean A和bean B之间的循环依赖关系迫使其中一个bean在被完全初始化之前被注入到另一个bean中（一个经典的鸡/蛋情景）。
```

一般来说，你可以相信Spring做正确的事情。它在容器加载时检测配置问题，例如引用不存在的bean和循环依赖关系。当bean实际创建时，Spring会尽可能晚地设置属性并解决依赖关系。这意味着，如果创建该对象或其某个依赖关系时遇到问题，则在请求对象时，已经正确启动的Spring容器可能会稍后出现异常。例如，由于缺少或无效的属性，bean抛出异常。某些配置问题的延迟出现是为什么ApplicationContext实现默认预先实例化单例bean的原因。在创建ApplicationContext时发现配置问题，而不是在以后的代价是需要花费一些前期的时间和内存来创建这些Bean，在你实际需要这些Bean之前。你仍然可以重写这个默认行为，这样singleton bean将会 lazy-initialize，而不是 pre-instantiated。

如果不存在循环依赖关系，那么当一个或多个协作bean被注入到一个依赖bean中时，每个协作bean在被注入到依赖bean之前被完全配置。这意味着如果bean A对bean B有依赖关系，Spring IoC容器在调用bean A上的setter方法之前需要完全配置bean B.换句话说，bean被实例化（如果不是 pre-instantiated的单例），它的依赖关系被设置，并调用相关的生命周期方法（例如配置的 [configured init method](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-lifecycle-initializingbean)或 [InitializingBean callback method](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-lifecycle-initializingbean)）。

#### 依赖注入的例子

以下示例使用基于基于XML的配置元数据，使用setter方式注入依赖。 Spring XML配置文件的一小部分指定了一些bean定义：

```xml
<bean id="exampleBean" class="examples.ExampleBean">
        <!-- setter injection using the nested ref element -->
        <property name="beanOne">
                <ref bean="anotherExampleBean"/>
        </property>

        <!-- setter injection using the neater ref attribute -->
        <property name="beanTwo" ref="yetAnotherBean"/>
        <property name="integerProperty" value="1"/>
</bean>

<bean id="anotherExampleBean" class="examples.AnotherBean"/>
<bean id="yetAnotherBean" class="examples.YetAnotherBean"/>
```

​	

```java
public class ExampleBean {

        private AnotherBean beanOne;

        private YetAnotherBean beanTwo;

        private int i;

        public void setBeanOne(AnotherBean beanOne) {
                this.beanOne = beanOne;
        }

        public void setBeanTwo(YetAnotherBean beanTwo) {
                this.beanTwo = beanTwo;
        }

        public void setIntegerProperty(int i) {
                this.i = i;
        }
}
```

在前面的例子中，setters被声明为匹配在XML文件中指定的属性。以下示例使用基于构造函数的DI：

```xml
<bean id="exampleBean" class="examples.ExampleBean">
        <!-- constructor injection using the nested ref element -->
        <constructor-arg>
                <ref bean="anotherExampleBean"/>
        </constructor-arg>

        <!-- constructor injection using the neater ref attribute -->
        <constructor-arg ref="yetAnotherBean"/>

        <constructor-arg type="int" value="1"/>
</bean>

<bean id="anotherExampleBean" class="examples.AnotherBean"/>
<bean id="yetAnotherBean" class="examples.YetAnotherBean"/>
```

```java
public class ExampleBean {

        private AnotherBean beanOne;

        private YetAnotherBean beanTwo;

        private int i;

        public ExampleBean(
                AnotherBean anotherBean, YetAnotherBean yetAnotherBean, int i) {
                this.beanOne = anotherBean;
                this.beanTwo = yetAnotherBean;
                this.i = i;
        }
}
```

bean定义中指定的构造函数参数将用作ExampleBean构造函数的参数。

现在考虑一下这个例子的一个变种，在这里不是使用构造函数，而是告诉Spring调用静态工厂方法来返回对象的一个实例：

```xml
<bean id="exampleBean" class="examples.ExampleBean" factory-method="createInstance">
        <constructor-arg ref="anotherExampleBean"/>
        <constructor-arg ref="yetAnotherBean"/>
        <constructor-arg value="1"/>
</bean>

<bean id="anotherExampleBean" class="examples.AnotherBean"/>
<bean id="yetAnotherBean" class="examples.YetAnotherBean"/>
```

```java
public class ExampleBean {

        // a private constructor
        private ExampleBean(...) {
                ...
        }

        // a static factory method; the arguments to this method can be
        // considered the dependencies of the bean that is returned,
        // regardless of how those arguments are actually used.
        public static ExampleBean createInstance (
                AnotherBean anotherBean, YetAnotherBean yetAnotherBean, int i) {

                ExampleBean eb = new ExampleBean (...);
                // some other operations...
                return eb;
        }
}
```

静态工厂方法的参数通过<constructor-arg />元素提供，就像构造函数实际使用一样。工厂方法返回的类的类型不必与包含静态工厂方法的类的类型相同，尽管在本例中是这样。一个实例（非静态）工厂方法将以基本相同的方式使用（除了使用factory-bean属性而不是class属性），所以在这里不讨论细节。

### 1.4.2详细的依赖和配置

如上一节所述，您可以将bean属性和构造函数参数定义为对其他托管bean（协作者）的引用，或者将其定义为内联的值。 Spring的基于XML的配置元数据为此支持其<property />和<constructor-arg />元素中的子元素类型。

#### 直接赋值（基本类型，字符串等）

<property />元素的value属性指定一个属性或构造函数参数作为可读的字符串表示。 Spring的 [conversion service](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#core-convert-ConversionService-API)用于将这些值从String转换为属性或参数的实际类型。

```xml
<bean id="myDataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
        <!-- results in a setDriverClassName(String) call -->
        <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://localhost:3306/mydb"/>
        <property name="username" value="root"/>
        <property name="password" value="masterkaoli"/>
</bean>
```

以下示例使用p-namespace进行更简洁的XML配置。

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:p="http://www.springframework.org/schema/p"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">

        <bean id="myDataSource" class="org.apache.commons.dbcp.BasicDataSource"
                destroy-method="close"
                p:driverClassName="com.mysql.jdbc.Driver"
                p:url="jdbc:mysql://localhost:3306/mydb"
                p:username="root"
                p:password="masterkaoli"/>

</beans>
```

前面的XML更简洁，然而，在运行时而不是设计时发现错字，除非您在创建bean定义时使用支持自动属性完成的IDE（如IntelliJ IDEA或Spring Tool Suite（STS））。强烈建议这样的IDE帮助。

您也可以将java.util.Properties实例配置为：

```xml
<bean id="mappings"
        class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">

        <!-- typed as a java.util.Properties -->
        <property name="properties">
                <value>
                        jdbc.driver.className=com.mysql.jdbc.Driver
                        jdbc.url=jdbc:mysql://localhost:3306/mydb
                </value>
        </property>
</bean>
```

Spring容器通过使用JavaBeans PropertyEditor机制将<value />元素中的文本转换为java.util.Properties实例。这是一个很好的捷径，它是Spring团队倾向于使用嵌套的<value />元素超过value属性样式的几个地方之一。

#### idref元素

idref元素简单地说就是将容器中另一个bean的id（字符串值 - 不是引用）传递给<constructor-arg />或<property />元素的防错方法。

```xml
<bean id="theTargetBean" class="..."/>

<bean id="theClientBean" class="...">
        <property name="targetName">
                <idref bean="theTargetBean"/>
        </property>
</bean>
```

上面的bean定义片段与以下片段完全相同（在运行时）：

```xml
<bean id="theTargetBean" class="..." />

<bean id="client" class="...">
        <property name="targetName" value="theTargetBean"/>
</bean>
```

第一种形式优于第二种形式，因为使用idref标签允许容器在部署时验证被引用的命名bean实际存在。在第二个变体中，不会对传递给客户机bean的targetName属性的值执行验证。当客户端bean实际实例化时，才会发现错误（最有可能是致命的结果）。如果客户端bean是一个[prototype ](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-scopes)bean，则此类型错误和生成的异常可能仅在部署容器后才被发现。

 *4.0 bean xsd中不再支持idref元素的local属性，因为它不再提供超过常规bean引用的值。升级到4.0模式时，只需将现有的idref  local引用更改为idref bean。*

<idref />元素带来价值的地方（至少在Spring 2.0之前的版本中），是在ProxyFactoryBean bean定义中的AOP拦截器的配置中。指定拦截器名称时使用<idref />元素可以防止拼写错误。

#### 对其他bean的引用（合作者）

ref元素是<constructor-arg />或<property />定义元素中的最后一个元素。在这里，您将bean的指定属性的值设置为对由容器管理的另一个bean（协作者）的引用。被引用的bean是被设置属性的bean的依赖，并且在属性被设置之前根据需要被初始化。 （如果协作者是单例bean，它可能已经被容器初始化了。）所有引用最终都是对另一个对象的引用。范围和验证取决于您是通过bean，local或parent属性指定另一个对象的id / name。

通过<ref />标签的bean属性指定目标bean是最通用的形式，并允许创建对同一个容器或父容器中的任何bean的引用，而不管它是否在同一个XML文件中。 bean属性的值可以与目标bean的id属性相同，或者是目标bean的name属性中的一个值。

```xml
<ref bean="someBean"/>
```

通过 `parent`属性指定目标bean将创建对当前容器的父容器中的bean的引用。 `parent`属性的值可以与目标bean的id属性相同，也可以与目标bean的name属性中的一个值相同，并且目标bean必须位于当前bean的父容器中。你通过 `parent`属性指定引用bean的方式的主要使用场景是当你有一个容器的层次结构，并且你想用一个代理来包装一个父容器中的现有bean。

```xml
<!-- in the parent context -->
<bean id="accountService" class="com.foo.SimpleAccountService">
        <!-- insert dependencies as required as here -->
</bean>
```

```xml
<!-- in the child (descendant) context -->
<bean id="accountService" <!-- bean name is the same as the parent bean -->
        class="org.springframework.aop.framework.ProxyFactoryBean">
        <property name="target">
                <ref parent="accountService"/> <!-- notice how we refer to the parent bean -->
        </property>
        <!-- insert other configuration and dependencies as required here -->
</bean>
```

```
4.0 bean xsd中不再支持ref元素的local属性，因为它不再提供超过常规bean引用的值。升级到4.0架构时，只需将现有的ref本地引用更改为ref bean。
```

#### 内部类

<property />或<constructor-arg />元素中的<bean />元素定义了一个所谓的内部bean。

```xml
<bean id="outer" class="...">
        <!-- instead of using a reference to a target bean, simply define the target bean inline -->
        <property name="target">
                <bean class="com.example.Person"> <!-- this is the inner bean -->
                        <property name="name" value="Fiona Apple"/>
                        <property name="age" value="25"/>
                </bean>
        </property>
</bean>
```

内部bean定义不需要定义的id或name;如果指定了，容器也不使用这样的值作为标识符。容器在创建时也会忽略范围标志：内部bean始终是匿名的，并且它们始终使用外部bean创建。不可能将内部bean注入到除了封装bean之外的协作bean中，或者独立地访问它们。

作为一个特例，可以从custom scope接收销毁回调，例如，对被包含在一个singleton bean内的request-scoped内部类：内部类实例的创建将被绑定到外部类，但销毁回调允许它参与 request scope的生命周期。这不是一个常见的情况;内部类通常简单地分享外部类的scope。、

#### 集合

在<list />，<set />，<map />和<props />元素中，分别设置Java集合类型List，Set，Map和Properties的属性和参数。

```xml
<bean id="moreComplexObject" class="example.ComplexObject">
        <!-- results in a setAdminEmails(java.util.Properties) call -->
        <property name="adminEmails">
                <props>
                        <prop key="administrator">administrator@example.org</prop>
                        <prop key="support">support@example.org</prop>
                        <prop key="development">development@example.org</prop>
                </props>
        </property>
        <!-- results in a setSomeList(java.util.List) call -->
        <property name="someList">
                <list>
                        <value>a list element followed by a reference</value>
                        <ref bean="myDataSource" />
                </list>
        </property>
        <!-- results in a setSomeMap(java.util.Map) call -->
        <property name="someMap">
                <map>
                        <entry key="an entry" value="just some string"/>
                        <entry key ="a ref" value-ref="myDataSource"/>
                </map>
        </property>
        <!-- results in a setSomeSet(java.util.Set) call -->
        <property name="someSet">
                <set>
                        <value>just some string</value>
                        <ref bean="myDataSource" />
                </set>
        </property>
</bean>
```

*The value of a map key or value, or a set value*也可以是以下任何元素：

```xml
bean | ref | idref | list | set | map | props | value | null
```

**集合合并**

Spring容器也支持集合的合并。应用程序开发人员可以定义父级样式的<list />，<map />，<set />或<props />元素，并且具有子样式<list />，<map />，<set />或<props />元素继承并覆盖父集合中的值。也就是说，子集合的值是合并父集合和子集合元素的结果，子集合元素覆盖父集合中指定的值。

这部分关于合并讨论了parent-child  bean机制。不熟悉定义的读者可能希望在继续之前阅读*relevant section*。

以下示例演示了集合合并：

```xml
<beans>
        <bean id="parent" abstract="true" class="example.ComplexObject">
                <property name="adminEmails">
                        <props>
                                <prop key="administrator">administrator@example.com</prop>
                                <prop key="support">support@example.com</prop>
                        </props>
                </property>
        </bean>
        <bean id="child" parent="parent">
                <property name="adminEmails">
                        <!-- the merge is specified on the child collection definition -->
                        <props merge="true">
                                <prop key="sales">sales@example.com</prop>
                                <prop key="support">support@example.co.uk</prop>
                        </props>
                </property>
        </bean>
<beans>
```

注意在子bean定义的adminEmails属性的<props />元素上使用merge = true属性。当子bean被容器解析并实例化时，生成的实例将具有一个adminEmails属性集合，该集合包含子级的adminEmails集合与父级的adminEmails集合的合并结果。

```xml
administrator=administrator@example.com
sales=sales@example.com
support=support@example.co.uk
```

这种合并行为同样适用于<list />，<map />和<set />集合类型。在<list />元素的特定情况下，与List集合类型相关联的语义（即有序值集合的概念）被保留;父项的值在所有子项列表的值之前。对于Map，Set和Properties集合类型，不存在排序。因此，对于容器在内部使用的关联的Map，Set和Properties实现类型的集合类型，没有任何排序语义。

**集合合并的限制**

您不能合并不同的集合类型（如Map和List），如果您尝试这样做，则会引发适当的Exception。合并属性必须在较低的继承的子定义上指定;在父集合定义上指定合并属性是多余的，不会导致所需的合并。

**强类型的集合**

通过在Java 5中引入泛型类型，您可以使用强类型集合。也就是说，可以声明一个Collection类型，使其只能包含String元素（例如）。如果您使用Spring将强类型集合依赖注入到bean中，则可以利用Spring的类型转换支持，使强类型Collection实例的元素在添加之前转换为相应的类型集合。

```java
public class Foo {

        private Map<String, Float> accounts;

        public void setAccounts(Map<String, Float> accounts) {
                this.accounts = accounts;
        }
}
```

```xml
<beans>
        <bean id="foo" class="x.y.Foo">
                <property name="accounts">
                        <map>
                                <entry key="one" value="9.99"/>
                                <entry key="two" value="2.75"/>
                                <entry key="six" value="3.99"/>
                        </map>
                </property>
        </bean>
</beans>
```

当foo bean的accounts属性准备注入时，强类型Map <String，Float>的元素类型的泛型信息可以通过反射获得。因此，Spring的类型转换基础组件将不同的值元素识别为类型为Float，并将字符串值9.99,2.75和3.99转换为实际的Float类型。

#### 空和空字符串值

Spring把空的参数当作空字符串来处理。以下基于XML的配置元数据片段将email属性设置为空字符串值（“”）。

```xml
<bean class="ExampleBean">
        <property name="email" value=""/>
</bean>
```

前面的例子等同于下面的Java代码：

```java
exampleBean.setEmail("")
```

<null />元素处理空值。例如：

```xml
<bean class="ExampleBean">
        <property name="email">
                <null/>
        </property>
</bean>
```

以上配置相当于以下Java代码：

```java
exampleBean.setEmail(null)
```

#### 用p-namespace命名空间实现XML快捷方式

 p-namespace使您可以使用bean元素的属性来代替嵌套的<property />元素来描述property values和/或引用的bean。

Spring支持基于XML Schema定义的名称空间的可扩展配置格式。本章中讨论的bean配置格式是在XML Schema文档中定义的。但是，p-namespace没有在XSD文件中定义，只存在于Spring的core中。

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:p="http://www.springframework.org/schema/p"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
                http://www.springframework.org/schema/beans/spring-beans.xsd">

        <bean name="classic" class="com.example.ExampleBean">
                <property name="email" value="foo@bar.com"/>
        </bean>

        <bean name="p-namespace" class="com.example.ExampleBean"
                p:email="foo@bar.com"/>
</beans>
```

该示例在bean定义中显示了名为email的 p-namespace 中的一个属性。这告诉Spring包含一个属性声明。如前所述， p-namespace 没有schema 定义，因此您可以将该属性的名称设置为bean的属性名称。

这个下一个例子包含了两个bean定义，它们都引用了另一个bean：

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:p="http://www.springframework.org/schema/p"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
                http://www.springframework.org/schema/beans/spring-beans.xsd">

        <bean name="john-classic" class="com.example.Person">
                <property name="name" value="John Doe"/>
                <property name="spouse" ref="jane"/>
        </bean>

        <bean name="john-modern"
                class="com.example.Person"
                p:name="John Doe"
                p:spouse-ref="jane"/>

        <bean name="jane" class="com.example.Person">
                <property name="name" value="Jane Doe"/>
        </bean>
</beans>
```

正如你所看到的，这个例子不仅包含使用 p-namespace 的属性值，而且还使用特殊的格式来声明属性引用。而第一个bean定义使用<property name =“spouse”ref =“jane”/>来创建一个从bean john到bean jane的引用，而第二个bean定义使用p：spouse-ref =“jane”做与property元素完全一样的东西。在这种情况下，spouse是属性名称，而-ref部分则表明这不是一个基本的值，而是对另一个bean的引用。

p-名称空间不如标准XML格式那么灵活。例如，声明属性引用的格式与以Ref结尾的属性冲突，而标准的XML格式则不会。我们建议您谨慎选择您的方法，并将其传达给您的团队成员，以避免同时使用所有三种方法生成XML文档。

#### 用c-namespace命名空间实现XML快捷方式

与[XML shortcut with the p-namespace](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-p-namespace)类似，Spring 3.1中新引入的c-namespace允许使用内联属性来配置构造函数参数，而不是嵌套constructor-arg元素。

让我们来看一下[Constructor-based dependency injection](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-constructor-injection) 的例子：c：namespace：

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:c="http://www.springframework.org/schema/c"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
                http://www.springframework.org/schema/beans/spring-beans.xsd">

        <bean id="bar" class="x.y.Bar"/>
        <bean id="baz" class="x.y.Baz"/>

        <!-- traditional declaration -->
        <bean id="foo" class="x.y.Foo">
                <constructor-arg ref="bar"/>
                <constructor-arg ref="baz"/>
                <constructor-arg value="foo@bar.com"/>
        </bean>

        <!-- c-namespace declaration -->
        <bean id="foo" class="x.y.Foo" c:bar-ref="bar" c:baz-ref="baz" c:email="foo@bar.com"/>

</beans>
```

c-namespace使用与p-namespace（在引用的bean的尾部加上-ref）相同的约定来通过名称来设置构造函数参数。而且，虽然它没有在XSD架构中定义（但它存在于Spring内核中），也需要声明它。

对于构造函数参数名称不可用的罕见情况（通常如果字节码是在没有调试信息的情况下编译的），可以使用回退参数索引：

```xml
<!-- c-namespace index declaration -->
<bean id="foo" class="x.y.Foo" c:_0-ref="bar" c:_1-ref="baz"/>
```

*由于XML语法，索引表示法需要前导_的存在，因为XML属性名称不能以数字开头（即使某些IDE允许）。*

实际上，构造函数解析机制在匹配参数方面非常有效，除非真正需要，否则我们建议在整个配置中使用名称符号。

#### 复合属性名称

当您设置bean属性时，只要最终属性名称以外的路径的所有组件都不为null，就可以使用复合或嵌套的属性名称。考虑下面的bean定义。

```xml
<bean id="foo" class="foo.Bar">
        <property name="fred.bob.sammy" value="123" />
</bean>
```

foo bean有一个fred属性，fred有一个bob属性，bob有一个sammy属性，最终sammy属性被设置为123.为了使这个工作，foo的fred属性以及fred的bob属性在构造bean之后，一定不能为null，否则抛出NullPointerException异常。

### 1.4.3使用depends-on

如果一个bean是另一个bean的依赖，通常意味着一个bean被设置为另一个的属性。通常，您可以使用基于XML的配置元数据中的<ref />元素来完成此操作。但是，有时bean之间的依赖关系并不那么直接;例如，类中的静态初始化器需要被触发，比如数据库驱动程序注册。 depends-on属性可以明确地强制一个或多被引用的bean在引用它们的bean初始化之前被初始化。以下示例使用depends-on属性来表示对单个bean的依赖关系：

```xml
<bean id="beanOne" class="ExampleBean" depends-on="manager"/>
<bean id="manager" class="ManagerBean" />
```

要表示对多个bean的依赖关系，请提供一个bean名称列表作为depends-on属性的值，用逗号，空格和分号作为有效的分隔符：

```xml
<bean id="beanOne" class="ExampleBean" depends-on="manager,accountDao">
        <property name="manager" ref="manager" />
</bean>

<bean id="manager" class="ManagerBean" />
<bean id="accountDao" class="x.y.jdbc.JdbcAccountDao" />
```

bean定义中的depends-on属性可以指定一个初始化时间依赖项，并且在只有singleton bean的情况下，可以指定相应的销毁时间依赖项。定义与给定bean的依赖关系的依赖bean首先被销毁，然后给定的bean本身被销毁。因此，依赖也可以控制关闭顺序。

### 1.4.4. Lazy-initialized beans

默认情况下，ApplicationContext实现急切地创建和配置所有的[singleton](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-singleton)  bean。通常，这种预先实例化是可取的，因为配置或周围环境中的错误被立即发现，而不是几小时甚至几天之后。当这种行为不可取时，可以通过将bean定义标记为lazy-initialized来防止单例bean的预先实例化。一个 lazy-initialized的bean告诉IoC容器在第一次请求时创建一个bean实例，而不是在启动时。

在XML中，这个行为是由<bean />元素的lazy-init属性控制的;例如：

```xml
<bean id="lazy" class="com.foo.ExpensiveToCreateBean" lazy-init="true"/>
<bean name="not.lazy" class="com.foo.AnotherBean"/>
```

当一个ApplicationContext使用前面的配置时，名为lazy的bean在ApplicationContext启动时并不急于预先实例化，而not.lazy bean则急切地预先实例化。

然而，当一个lazy-initialized的bean是一个*not* lazy-initialized的依赖时，ApplicationContext在启动时会创建lazy-initialized bean，因为它必须满足singleton’s的依赖关系。 lazy-initialized的bean被注入到一个singleton bean中，而不是延迟初始化的。

您还可以通过在<beans />元素上使用default-lazy-init属性来控制容器级别的lazy-initialized;例如：

```xml
<beans default-lazy-init="true">
        <!-- no beans will be pre-instantiated... -->
</beans>
```

### 1.4.5. Autowiring collaborators

Spring容器可以自动联系合作bean之间的关系。您可以允许Spring通过检查ApplicationContext的内容来自动为您的bean解析协作者（其他bean）。自动装配具有以下优点：

自动装配可以显着减少指定属性或构造函数参数的需要。 （其他机制，如本章 [discussed elsewhere in this chapter](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-child-bean-definitions) 讨论的bean模板，在这方面也是有价值的。

自动装配可以随着对象的发展而更新配置。例如，如果您需要向类中添加依赖项，则可以自动满足该依赖项，而无需修改配置。因此，在开发过程中，自动装配可能特别有用，而且在代码库变得更加稳定的情况下，不会否定切换到显式写入的选项。

当使用基于XML的配置元数据[2]时，您可以使用<bean />元素的autowire属性为bean定义指定自动装配模式。自动装配功能有四种模式。您可以指定每个bean的自动装配，可以选择哪些自动装配。





