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

Spring框架还支持依赖注入（JSR 330）和通用注解（JSR 250）规范，应用程序开发人员可以选择使用这些规范来代替Spring框架提供的Spring特定的机制。

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

基于注解的配置：Spring 2.5引入了对基于注解的配置元数据的支持。

基于Java的配置：从Spring 3.0开始，Spring JavaConfig项目提供的许多功能成为核心Spring框架的一部分。因此，您可以使用Java而不是XML文件来定义应用程序类外部的Bean。要使用这些新功能，请参阅@Configuration，@Bean，@Import和@DependsOn注解。

Spring的配置包含至少一个，一般是多个bean定义组成。基于XML的配置元数据将这些bean配置为顶级<beans />元素内的<bean />元素。 基于Java配置通常在@Configuration类中使用@Bean注解的方法。

这些bean定义对应于组成应用程序的实际对象。通常，您可以定义服务层对象，数据访问对象（DAO），Struts Action实例等表现层对象，Hibernate SessionFactory，JMS队列等基础设施对象。通常，不会在容器中配置细粒度的域对象，因为创建和加载域对象通常是DAO和业务逻辑的责任。但是，您可以使用Spring与AspectJ的集成来配置在IoC容器控制之外创建的对象。请参阅使用AspectJ以依赖注入域对象与Spring。

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

然后可以使用getBean来检索bean的实例。 ApplicationContext接口还有其他一些检索bean的方法，但理想情况下应用程序代码不应该使用它们。事实上，你的应用程序代码根本就不应该调用getBean（）方法，因此完全不依赖于Spring API。例如，Spring与Web框架的集成为各种Web框架组件（如控制器和JSF托管的Bean）提供了依赖注入，允许您通过元数据（例如自动装配注解）声明对特定Bean的依赖关系。

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

如果您正在使用Java配置，则可以使用@Bean注解来提供别名，请参阅[Using the @Bean annotation](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-java-bean-annotation)以获取详细信息。

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

基于构造器的DI通过容器调用具有多个参数的构造器来完成，每个参数表示一个依赖关系。调用一个具有特定参数的静态工厂方法来构造这个bean几乎是等价的，这个讨论对于构造函数或者静态工厂方法的参数一样的。以下示例显示了只能通过构造函数注入进行依赖注入的类。请注意，这个类没有什么特别之处，它是一个POJO，它不依赖容器特定的接口，基类或注解。、

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

请记住，为了使这项工作脱离您的代码，必须使用调试标志进行编译，以便Spring可以从构造函数中查找参数名称。如果你不能用调试标志编译你的代码（或者不想），你可以使用@ConstructorProperties JDK注解来明确地指定你的构造函数参数。示例类将如下所示：

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

以下示例显示了一个只能使用纯setter注入进行依赖注入的类。这个类是传统的Java。这是一个POJO，它不依赖于容器特定的接口，基类或注解。

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

ApplicationContext支持它所管理的bean的基于构造函数和基于setter的依赖注入。它也支持通过构造方法注入依赖之后，基于setter的依赖注入。您可以以BeanDefinition的形式配置依赖项，您可以使用它与PropertyEditor实例一起使用，将属性从一种格式转换为另一种格式。然而，大多数Spring用户不直接使用这些类（即以编程方式），而是使用XML bean定义，带注解的组件（即用@Component，@Controller等注解的类）或基于Java的@Configuration类的@Bean方法。然后将这些源内部转换为BeanDefinition的实例，并用于加载整个Spring IoC容器实例。

```
使用构造器注入还是setter方式注入

既然可以混合使用基于构造函数和基于setter的依赖注入，那么使用构造函数作为强制依赖的方式或使用setter方法或配置方法作为可选依赖的方式是一个很好的经验法则。请注意，可以使用setter方法上的@Required注释来使该属性成为必需的依赖项。

Spring团队通常主张构造器注入，因为它使得可以将应用程序组件实现为不可变对象，并确保所需的依赖项不为空。而且，构造器注入的组件总是返回到处于完全初始化状态的客户端（调用）代码。作为一个侧面说明，大量的构造函数参数是一种糟糕的代码，这意味着类可能有太多的责任，应该重构，以更好地解决问题的分离。

Setter注入主要只应用于可选的依赖关系，这种依赖可以在类中指定合理的默认值。否则，在代码使用依赖关系的任何地方都必须执行非空的检查。 setter注入的一个好处是setter方法使得这个类的对象可以重新配置或稍后重新注入。通过JMX MBeans进行管理因此是一个引人注目的使用情况。

使用最适合特定类的DI风格。有时，在处理没有源代码的第三方类的时候，选择适合你的方式。例如，如果第三方类不公开任何setter方法，则构造函数注入可能是DI唯一可用的形式。

```

#### 依赖解析过程

容器执行bean依赖解析如下：

- ApplicationContext使用描述所有Bean的配置元数据创建和初始化。配置元数据可以通过XML，Java代码或注解来指定。	
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

当使用基于XML的配置元数据[2]时，您可以使用<bean />元素的autowire属性为bean定义指定自动装配模式。自动装配功能有四种模式。您可以指定每个bean的autowire，可以选择哪些自动装配。

|    Mode     |               Explanation                |
| :---------: | :--------------------------------------: |
|     no      | （默认）无自动装配。 Bean引用必须通过一个ref元素来定义。建议不要在更大的部署中更改默认设置，因为明确的指定的引用bean提供了更好的控制和清晰度。它在一定程度上记录了一个系统的结构。 |
|   byName    | 按属性名称自动装配。 Spring会查找与需要自动装配的属性同名的bean。例如，如果一个bean定义被设置为autowire by name，并且它包含一个master属性（也就是说，它有一个setMaster（..）方法），Spring会查找名为master的bean定义，并使用它来设置属性。 |
|   byType    | 如果容器中只有一个与将被注入的属性类型相同的bean，则允许属性自动装配。如果存在多个，则会引发一个致命异常，这表明您不能使用该bean的byType注入类型。如果没有匹配的bean，什么都不会发生。该property未设置 |
| constructor | 类似于byType，但适用于构造函数参数。如果容器中不存在唯一的构造函数参数类型的bean，则会引发致命错误。 |

使用byType或*constructor* autowiring模式，您可以连线数组和类型集合。在这种情况下，容器中所有符合预期类型的自动装配候选都被提供来满足依赖关系。如果预期的键类型是字符串，则可以自动装载强类型的映射。自动装配的Maps值将由所有与预期类型匹配的bean实例组成，Maps键将包含相应的bean名称。

您可以将自动装配行为与自动装配完成后执行的依赖关系检查相结合。

#### 自动装配的局限和缺点

自动装配在项目中始终使用时效果最佳。如果通常不使用自动装配，仅使用它来连接一个或两个bean定义可能会使开发人员感到困惑。

考虑自动装配的局限和缺点：

- property和constructor-arg中的显式依赖关系总是覆盖自动装配。您不能自动调用所谓的简单属性，例如基本类型，字符串和类（以及这种简单属性的数组）。spring设计就是这样的限制。
- 自动装配不如准确指定。虽然，如上表所述，虽然Spring在小心避免歧义情况下的揣测造成的不期望的结果，但Spring管理的对象之间的关系不再被明确地记录下来。
- 对于可能从Spring容器生成文档的工具，这些bean之间的信息可能不可用。
- 容器中的多个bean定义可以匹配由setter方法或构造函数参数指定的类型，以便自动装配。对于数组，集合或map，这不一定是个问题。然而对于期望单一值的依赖，这种歧义是解决不了的。如果没有唯一的bean定义可用，则抛出异常。

在最后一种情况下，你有几个选择：

- 放弃autowiring以支持显式指定依赖关系。
- 通过将autowire-candidate属性设置为false，避免autowiring  bean定义，如下一节所述。
- 指定单个bean定义作为主要候选者，通过将其<bean />元素的`primary` 属性设置为true。
- 如 [Annotation-based container configuration](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-annotation-config).中所述，通过并使用基于注解的配置实现更加细粒度的控制。

#### 从自动装配中排除一个bean

在每个bean的基础上，您可以从autowiring中排除一个bean。在Spring的XML格式中，将<bean />元素的autowire-candidate属性设置为false;该容器使该特定的bean定义对autowiring基础组件设施不可用（包括诸如@Autowired之类的注解样式配置）。

*autowire-candidate属性仅影响type-based autowiring。它不会影响by name的显式引用，即使指定的bean的autowire-candidate属性为false，也可以被自动注入。因此，如果名称匹配，by name autowiring将注入一个bean。*

您还可以根据对bean名称进行模式匹配来限制autowire candidates。顶层的<beans />元素在其default-autowire-candidates属性中接受一个或多个模式。例如，要将autowire候选者状态限制为名称以Repository结尾的任何Bean，请提供* Repository的值。要提供多种模式，请在逗号分隔它们。对于bean定义autowire-candidate属性，显式值true或false总是优先的，对于这样的bean，模式匹配规则不起作用。

这些技术对于不想通过自动装配被注入其他bean的bean是有用的。这并不意味着排除的bean本身不能使用自动装配进行配置。相反，bean本身是不能被自动装配进其他bean的。

### 1.4.6. Method injection

在大多数应用场景中，容器中的大部分bean的都是[singletons](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-singleton).。当单例bean需要与另一个单例bean协作，或者非单例bean需要与另一个非单例bean协作时，通常通过将一个bean定义为另一个的属性来处理依赖。单当bean的生命周期不同时会出现问题。假设也许在A的每个方法调用上单例bean A需要使用非单例（原型）bean B。但是容器只创建一个单例bean A，因此只有一次机会来设置属性。所以每当需要时，容器不能向bean A提供bean B的新实例。

一个解决方案是放弃一些控制反转。您可以通过实现ApplicationContextAware接口[make bean A aware of the container](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-aware)，并且每当bean A需要时，通过[making a getBean("B") call to the container](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-client) 请求新的bean B实例。以下是这种方法的一个例子：

```java
// a class that uses a stateful Command-style class to perform some processing
package fiona.apple;

// Spring-API imports
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class CommandManager implements ApplicationContextAware {

        private ApplicationContext applicationContext;

        public Object process(Map commandState) {
                // grab a new instance of the appropriate Command
                Command command = createCommand();
                // set the state on the (hopefully brand new) Command instance
                command.setState(commandState);
                return command.execute();
        }

        protected Command createCommand() {
                // notice the Spring API dependency!
                return this.applicationContext.getBean("command", Command.class);
        }

        public void setApplicationContext(
                        ApplicationContext applicationContext) throws BeansException {
                this.applicationContext = applicationContext;
        }
}
```

前面的内容是不可取的，因为业务代码知道并耦合到Spring框架。方法注入是Spring IoC容器的一个高级特性，它允许以简洁的方式处理这个用例

您可以在此[this blog entry](https://spring.io/blog/2004/08/06/method-injection/)中阅读更多关于方法注入的动机。

#### 查找方法注入

查找方法注入是覆盖容器管理的bean上的方法的能力，用于返回容器中另一个指定名称的bean的查找结果。查找通常包含一个prototype bean，如前一节所述。 Spring Framework通过使用CGLIB库中的字节码动态生成覆盖该方法的子类，从而实现了此方法注入。

- 为了使这个动态子类工作，Spring bean容器将继承的类不能是final，被覆盖的方法也不能是final。
- 对具有抽象方法的类进行单元测试需要您自己创建该类的子类，并提供抽象方法的实现。
- 组件扫描也需要具体的方法，这需要具体的类来提取。
- 另一个关键的限制是 lookup methods不能和工厂方法一起使用，特别是不能和configuration类中的@Bean方法一起使用，因为在这种情况下容器不负责创建实例，因此不能创建运行时生成的子类。

查看前面代码片断中的CommandManager类，可以看到Spring容器将动态地覆盖createCommand（）方法的实现。，在重做的例子中可以看到你的CommandManager类不会有任何的Spring依赖关系：

```java
package fiona.apple;

// no more Spring imports!

public abstract class CommandManager {

        public Object process(Object commandState) {
                // grab a new instance of the appropriate Command interface
                Command command = createCommand();
                // set the state on the (hopefully brand new) Command instance
                command.setState(commandState);
                return command.execute();
        }

        // okay... but where is the implementation of this method?
        protected abstract Command createCommand();
}
```

在包含要注入的方法的客户端类（本例中为CommandManager）中，要注入的方法需要以下形式的签名：

```xml
<public|protected> [abstract] <return-type> theMethodName(no-arguments);
```

如果方法是抽象的，则动态生成的子类将实现该方法。否则，动态生成的子类将覆盖原始类中定义的具体方法。例如：

```xml
<!-- a stateful bean deployed as a prototype (non-singleton) -->
<bean id="myCommand" class="fiona.apple.AsyncCommand" scope="prototype">
        <!-- inject dependencies here as required -->
</bean>

<!-- commandProcessor uses statefulCommandHelper -->
<bean id="commandManager" class="fiona.apple.CommandManager">
        <lookup-method name="createCommand" bean="myCommand"/>
</bean>
```

标识为commandManager的bean在需要myCommand bean的新实例时调用自己的方法createCommand（）。您必须小心地将myCommand bean作为原型部署，如果这实际上是需要的话。如果它是[singleton](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-singleton)，则每次返回myCommand bean的同一个实例。

或者，在基于注解的组件模型中，您可以通过@Lookup注解声明一个查找方法：

```java
public abstract class CommandManager {

        public Object process(Object commandState) {
                Command command = createCommand();
                command.setState(commandState);
                return command.execute();
        }

        @Lookup("myCommand")
        protected abstract Command createCommand();
}
```

或者，更习惯地说，您可能依赖于目标bean根据查找方法的声明返回类型得到解决：

```java
public abstract class CommandManager {

        public Object process(Object commandState) {
                MyCommand command = createCommand();
                command.setState(commandState);
                return command.execute();
        }

        @Lookup
        protected abstract MyCommand createCommand();
}
```

请注意，您通常会使用具体的存根实现来声明这样的带注解的查找方法，以使它们与Spring的组件扫描规则兼容，规则中抽象类在默认情况下被忽略。这个规则不适用于显式注册或显式导入的bean。

```
访问不同范围的目标bean的另一种方式是ObjectFactory / Provider注入点。查看Scoped beans as dependencies。

感兴趣的读者也可以找到ServiceLocatorFactoryBean（在org.springframework.beans.factory.config包中）。
```

#### 任意方法替换

与查找方法注入相比，不太有用的方法注入形式是能够用另一个方法实现来替换托管bean中的任意方法。用户可以安全地跳过本节的其余部分，直到实际需要功能为止。

## 1.5. Bean scopes

当你创建一个bean定义时，你创建一个配方用来创建由这个bean定义所定义的类的实例。 bean定义是一个配方的想法是很重要的，因为这意味着，像一个类一样，你可以从一个配方创建许多对象实例。

您不仅可以控制要插入到从特定的bean定义创建的对象中的各种依赖项和配置值，还可以控制从特定的bean定义创建的对象的scope。这种方法功能强大且灵活，您可以选择通过配置创建的对象的范围，而不必在Java类级别上控制对象的scope。 Bean可以被定义被部署在多个作用域中的一个作用域中：Spring框架支持六个作用域，其中五个作用域只有在使用Web感知的ApplicationContext才可以使用。

下面的scope开箱即用。您也可以创建[a custom scope.](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-custom)。

| Scope                                    | Description                              |
| ---------------------------------------- | ---------------------------------------- |
| [singleton](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-singleton) | （默认）对于每个Spring IoC容器，将单个bean定义的范围作用于单个对象实例 |
| [prototype](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-prototype) | 将单个bean定义的范围作用于任意个对象实例                   |
| [request](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-request) | 将单个bean定义作用于单个HTTP请求的生命周期;也就是说，每个HTTP请求都有自己的bean实例，这个实例是在单个bean定义的后面创建的。只有在Web感知的Spring ApplicationContext的上下文中才有效。 |
| [session](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-session) | 将单个bean定义作用于HTTP SESSION的生命周期。只有在Web感知的Spring ApplicationContext的上下文中才有效。 |
| [application](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-application) | 将单个bean定义作用于ServletContext的生命周期。只有在Web感知的Spring ApplicationContext的上下文中才有效。 |
| [websocket](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/web.html#websocket-stomp-websocket-scope) | 将单个bean定义作用于Websocket的生命周期。只有在Web感知的Spring ApplicationContext的上下文中才有效。 |

*从Spring 3.0开始，一个thread scope是可用的，但是默认情况下没有注册的。有关更多信息，请参阅[`SimpleThreadScope`](https://docs.spring.io/spring-framework/docs/5.0.1.RELEASE/javadoc-api/org/springframework/context/support/SimpleThreadScope.html)的文档。有关如何注册它或任何其他自定义作用域的说明，请参阅使用[Using a custom scope](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-custom-using)。*

### 1.5.1. The singleton scope

只一个singleton bean的共享实例被管理，并且具有与该bean定义匹配的id或ids的对该bean的所有请求都会导致Spring容器返回一个特定的bean实例。

换句话说，当你定义一个bean定义并将其作为一个singleton作用域时，Spring IoC容器明确创建了该bean定义所定义的对象的一个实例。这个实例被存储在这样的singleton beans的缓存中，并且对该指定名称的bean的所有后续请求和引用都会返回缓存的对象。

Spring的singleton bean概念与“四人帮”（GoF）模式书中定义的Singleton模式不同。 GoF Singleton对对象的范围进行硬编码，以便每个ClassLoader创建一个特定类的唯一一个实例。 Spring singleton的scope最好按每个容器和每个bean来描述。这意味着如果你在一个Spring容器中为一个特定的类定义了一个bean，那么Spring容器将创建该bean定义所定义的类的一个且只有一个实例。singleton scope是Spring中的默认作用域。要在XML中将bean定义为单例，您可以编写如下的代码：

```xml
<bean id="accountService" class="com.foo.DefaultAccountService"/>

<!-- the following is equivalent, though redundant (singleton scope is the default) -->
<bean id="accountService" class="com.foo.DefaultAccountService" scope="singleton"/>
```

### 1.5.2. The prototype scope

bean部署的prototype scope导致每次对指定bean的请求都会创建一个新的bean实例。也就是说，该bean被注入到另一个bean中，或者通过容器上的getBean（）方法调用来请求它。通常，有状态bean的作用域使用prototype scope，无状态的bean的作用域使用singleton scope。

下图说明了Spring的prototype scope。数据访问对象（DAO）通常不配置为prototype，因为典型的DAO不具有任何对话状态。、

以下示例在XML中将bean定义为prototype：

```xml
<bean id="accountService" class="com.foo.DefaultAccountService" scope="prototype"/>
```

与其他范围相比，Spring不管理 prototype bean的整个生命周期：容器实例化，配置和组装原型对象，并将其交给客户端，对该原型实例没有后续的记录。因此，虽然不管scope是什么，在所有对象上都会调用初始化生命周期回调方法，但是在prototype scope的情况下，不会调用配置的销毁生命周期额的回调。客户端代码必须清理prototype scope的对象，并释放原型bean持有的昂贵资源。为了让Spring容器释放原型范围bean所拥有的资源，可以尝试使用一个自定义 [bean post-processor](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-extension-bpp)，后者包含需要清理的bean的引用。

在某些方面，Spring容器在prototype scope的bean方面的角色是Java new运算符的替代。所有的生命周期管理都必须由客户来处理。 （有关Spring容器中bean的生命周期的详细信息，请参阅[Lifecycle callbacks](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-lifecycle)。

### 1.5.3依赖 prototype bean的Singleton  bean

当您使用引用prototype bean的singleton bean时，请注意依赖关系在实例化时被解析。因此，如果你依赖注入一个prototype bean到一个singleton bean中，一个新的原型bean被实例化，然后依赖注入到单例bean中。原型实例是唯一提供给单例范围bean的实例。

但是，假设您希望singleton bean在运行时重复获取prototype bean的新实例。你不能依赖注入一个prototype bean到你的单例bean中，因为这个注入只在Spring容器正在实例化singleton bean时发生一次。如果您在运行时不止一次需要一个原型bean的新实例，请参阅[Method injection](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-method-injection)

### 1.5.4. Request, session, application, and WebSocket scopes

The `request`, `session`, `application`, and `websocket` scopes仅在使用web-aware Spring ApplicationContext实现（如XmlWebApplicationContext）时才可用。如果您将这些范围与常规的Spring IoC容器（如ClassPathXmlApplicationContext）一起使用，则会抛出IllegalStateException异常，提示未知的bean范围。

#### 初始web配置

为了支持Request, session, application, and WebSocket级的bean的scope，在定义bean之前需要一些次要的初始配置。 （`singleton` and `prototype`不需要此初始设置。）

你如何完成这个初始设置取决于特定的Servlet环境。

如果你在Spring Web MVC中访问范围化的bean，实际上，在由Spring DispatcherServlet处理的请求中，不需要特殊的设置：DispatcherServlet已经公开了所有相关的状态。

如果您使用Servlet 2.5 Web容器，并且在Spring的DispatcherServlet之外处理请求（例如，在使用JSF或Struts时），则需要注册org.springframework.web.context.request.RequestContextListener ServletRequestListener。对于Servlet 3.0，这可以通过WebApplicationInitializer接口以编程方式完成。或者，对于较老的容器，将以下声明添加到Web应用程序的web.xml文件中：

```xml
<web-app>
        ...
        <listener>
                <listener-class>
                        org.springframework.web.context.request.RequestContextListener
                </listener-class>
        </listener>
        ...
</web-app>
```

或者，如果你的监听器设置有问题，可以考虑使用Spring的RequestContextFilter。过滤器映射取决于周围的Web应用程序配置，因此您必须根据需要进行更改。

```xml
<web-app>
        ...
        <filter>
                <filter-name>requestContextFilter</filter-name>
                <filter-class>org.springframework.web.filter.RequestContextFilter</filter-class>
        </filter>
        <filter-mapping>
                <filter-name>requestContextFilter</filter-name>
                <url-pattern>/*</url-pattern>
        </filter-mapping>
        ...
</web-app>
```

DispatcherServlet，RequestContextListener和RequestContextFilter的作用都完全相同，即将HTTP请求对象绑定到处理该请求的线程。这使得request和session作用域的bean可以向下作用到调用链。

#### Request scope

考虑以下用于bean定义的XML配置：

```xml
<bean id="loginAction" class="com.foo.LoginAction" scope="request"/>
```

Spring容器对每个HTTP请求都使用loginAction bean定义来创建新的LoginAction bean的新实例。也就是说，loginAction bean的作用域是HTTP请求级别。您可以根据需要更改所创建实例的内部状态，因为从同一个loginAction bean定义创建的其他实例将不会看到这些状态变化;他们对单独的request是没有关系的。当请求完成处理时，作用于该请求的bean将被丢弃。、

使用注解驱动的组件或Java Config时，可以使用@RequestScope注解将组件分配到请求范围。

```java
@RequestScope
@Component
public class LoginAction {
        // ...
}
```

#### Session scope

考虑以下用于bean定义的XML配置：

```xml
<bean id="userPreferences" class="com.foo.UserPreferences" scope="session"/>
```

Spring容器在单个HTTP session的生命周期中使用userPreferences bean定义来创建UserPreferences bean的新实例。换句话说，userPreferences bean在HTTP session级别有效。与request bean一样，您可以根据需要更改所创建实例的内部状态，因为使用同一userPreferences bean定义创建的实例的其他HTTP Session实例看不到这些更改，因为它们对于每个HTTP会话是独立的。当HTTP会话最终被丢弃时，作用于该特定HTTP会话的bean也被丢弃。

使用注解驱动组件或Java Config时，可以使用@SessionScope注解将组件分配给会话作用域。

```java
@SessionScope
@Component
public class UserPreferences {
        // ...
}
```

#### Application scope

考虑以下用于bean定义的XML配置：

```xml
<bean id="appPreferences" class="com.foo.AppPreferences" scope="application"/>
```

Spring容器对整个Web应用程序使用appPreferences bean定义只创建一个AppPreferences bean的新实例。也就是说，appPreferences bean的作用域在ServletContext级别，作为常规的ServletContext属性存储。这有点类似于Spring单例bean，但在两个重要方面有所不同：它对每个ServletContext只有一个，而不是对每个Spring的“ApplicationContext”只有一个（在任何给定的Web应用程序中可能有几个），它实际上是暴露的，因此作为ServletContext属性可见。

使用注解驱动组件或Java Config时，可以使用@ApplicationScope注解将组件分配给应用程序范围。

```java
@ApplicationScope
@Component
public class AppPreferences {
        // ...
}
```

#### 作用域bean作为依赖

Spring IoC容器不仅管理对象（bean）的实例化，还管理协作者（或依赖关系）的关联。如果你想把一个HTTP request scope的bean注入到另一个更长的作用域的bean中，你可以选择注入一个AOP代理来代替作用域bean。也就是说，您需要注入一个代理对象，该对象暴露与作用域对象相同的公共接口，但也可以从相关作用域（例如HTTP请求）中检索真实的目标对象，并将方法调用委托给实际对象。

```
您也可以在范围为singleton的bean之间使用<aop：scoped-proxy />，然后通过一个可序列化的中间代理引用目标bean，因此可以在反序列化中重新获得目标单例bean。

当针对prototype scope的bean声明<aop：scoped-proxy />时，共享代理上的每个方法调用都将导致创建一个新的目标实例，然后请求将被转发到该实例。

此外，在lifecycle-safe方式中范围化代理不是从较短范围访问Beans的唯一方式。您也可以简单地声明您的注入点（即constructor/setter argument or autowired field）为ObjectFactory <MyTargetBean>，允许getObject（）调用每次按需检索当前实例 - 不需要一致保持实例或单独存储。

作为一个扩展的变体，你可以声明ObjectProvider <MyTargetBean>，它提供了几个额外的访问变体，包括getIfAvailable和getIfUnique。

JSR-330变种被称为Provider，与Provider <MyTargetBean>声明和相应的get（）一起调用用于每个请求。有关JSR-330的更多详细信息，请参阅here。
```

以下示例中的配置只有一行，但对了解为什么以及其后面的怎么做很重要。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:aop="http://www.springframework.org/schema/aop"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
                http://www.springframework.org/schema/beans/spring-beans.xsd
                http://www.springframework.org/schema/aop
                http://www.springframework.org/schema/aop/spring-aop.xsd">

        <!-- an HTTP Session-scoped bean exposed as a proxy -->
        <bean id="userPreferences" class="com.foo.UserPreferences" scope="session">
                <!-- instructs the container to proxy the surrounding bean -->
                <aop:scoped-proxy/>
        </bean>

        <!-- a singleton-scoped bean injected with a proxy to the above bean -->
        <bean id="userService" class="com.foo.SimpleUserService">
                <!-- a reference to the proxied userPreferences bean -->
                <property name="userPreferences" ref="userPreferences"/>
        </bean>
</beans>
```

要创建这样一个代理，可以将一个子<aop：scoped-proxy />元素插入到一个有作用域的bean定义中（请参阅 [Choosing the type of proxy to create](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-other-injection-proxies)和[XML Schema-based configuration](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#xsd-schemas)）。为什么在`request`, `session`级别定义的bean需要<aop：scoped-proxy />元素？让我们来看看下面的singleton bean的定义，并将其与您需要为上述范围定义的内容进行对比（请注意，以下userPreferences bean定义不完整）。

```xml
<bean id="userPreferences" class="com.foo.UserPreferences" scope="session"/>

<bean id="userManager" class="com.foo.UserManager">
        <property name="userPreferences" ref="userPreferences"/>
</bean>
```

在前面的例子中，singleton bean userManager注入了对HTTP Session范围的bean userPreferences的引用。这里的要点是userManager bean是一个singleton：它将被实例化，每个容器只实例化一次，它的依赖关系（在这种情况下，只有一个，userPreferences bean）也只被注入一次。这意味着userManager bean只能在完全相同的userPreferences对象上运行，也就是最初注入的对象。

在将生命较短的作用域bean注入到更长寿命的作用域bean中时上面的行为不是我们想要的，例如将HTTP session scope的依赖bean注入到singleton bean中。相反，您需要一个single  userManager对象，并且在HTTP Session的生命周期中，您需要一个特定于HTTP Session的userPreferences对象。因此，容器创建一个代理对象，该对象暴露与UserPreferences类（理想情况下为UserPreferences实例的对象）完全相同的公共接口，该对象可以从作用域机制（HTTP请求，Session等）中获取真实的UserPreferences对象。容器将这个代理对象注入到userManager bean中，而不关注这个UserPreferences引用是一个代理。在这个例子中，当一个UserManager实例在依赖注入的UserPreferences对象上调用一个方法时，它实际上是在代理上调用一个方法。然后，代理从HTTP会话中（本例中）获取真实的UserPreferences对象，并将方法调用委托给获取的实际UserPreferences对象。

因此，在将请求和会话范围的bean注入协作对象时，您需要以下正确且完整的配置：

```xml
<bean id="userPreferences" class="com.foo.UserPreferences" scope="session">
        <aop:scoped-proxy/>
</bean>

<bean id="userManager" class="com.foo.UserManager">
        <property name="userPreferences" ref="userPreferences"/>
</bean>
```

#### 选择要创建的代理类型

默认情况下，当Spring容器为使用<aop：scoped-proxy />元素标记的bean创建代理时，将创建一个基于CGLIB的类代理。

```
CGLIB代理只拦截public方法调用！不要使用这种代理的非公开方法;它们不会被委托给实际的作用域对象。
```

另外，可以把<aop：scoped-proxy />元素的proxy-target-class属性指定为false来配置Spring容器，以便为scope的bean创建标准的基于JDK接口的代理。使用基于JDK接口的代理意味着您不需要应用程序类路径中的其他库来实现这种代理。但是，这也意味着作用域bean的类必须至少实现一个接口，而注入了作用域bean的所有协作者必须通过一个接口引用该作用域bean。

```xml
<!-- DefaultUserPreferences implements the UserPreferences interface -->
<bean id="userPreferences" class="com.foo.DefaultUserPreferences" scope="session">
        <aop:scoped-proxy proxy-target-class="false"/>
</bean>

<bean id="userManager" class="com.foo.UserManager">
        <property name="userPreferences" ref="userPreferences"/>
</bean>
```

For more detailed information about choosing class-based or interface-based proxying, see [Proxying mechanisms](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#aop-proxying).

## 1.6定制一个bean的特性

### 1.6.1。生命周期回调

为了影响容器对bean的生命周期的管理，bean可以实现Spring InitializingBean和DisposableBean接口。容器为前者调用afterPropertiesSet（），后者调用destroy（）来允许bean在初始化和销毁bean时执行某些操作。

*JSR-250 @PostConstruct和@PreDestroy注解通常被认为是在现代Spring应用程序中接收生命周期回调的最佳实践。使用这些注解意味着你的bean没有耦合到Spring特定的接口。有关详细信息，请参阅 [@PostConstruct and @PreDestroy](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-postconstruct-and-predestroy-annotations).*
*如果您不想使用JSR-250注解，但是仍然想要移除耦合，请考虑使用对象定义元数据中的init-method和destroy-method。*

在内部，Spring框架使用BeanPostProcessor实现来处理它可以找到的任何回调接口，并调用适当的方法。如果您需要自定义功能或其他生命周期行为，Spring不提供开箱即用的功能，您可以自己实现BeanPostProcessor。有关更多信息，请参阅 [Container Extension Points](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-extension)。

除了初始化和销毁回调之外，Spring管理对象还可以实现`Lifecycle`接口，以便这些对象可以参与由容器自己的生命周期驱动的启动和关闭过程。

生命周期回调接口在本节中描述。

#### 初始化回调

org.springframework.beans.factory.InitializingBean接口允许bean在所有必要的属性都被容器设置之后执行初始化工作。 InitializingBean接口指定一个方法：

```java
void afterPropertiesSet() throws Exception;
```

建议您不要使用InitializingBean接口，因为它将代码耦合到Spring。替代方案是使用[`@PostConstruct`](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-postconstruct-and-predestroy-annotations)注解或指定一个POJO初始化方法。对于基于XML的配置元数据，可以使用init-method属性指定具有void no-argument 的方法的名称。使用Java配置，您可以使用@Bean的initMethod属性，请参阅接收生命周期回调。例如，以下内容：

```xml
<bean id="exampleInitBean" class="examples.ExampleBean" init-method="init"/>
```

```java
public class ExampleBean {

        public void init() {
                // do some initialization work
        }
}
```

和下面的是完全一样的

```xml
<bean id="exampleInitBean" class="examples.AnotherExampleBean"/>
```

```java
public class AnotherExampleBean implements InitializingBean {

        public void afterPropertiesSet() {
                // do some initialization work
        }
}
```

但不会将代码耦合到Spring。

#### 销毁回调

实现org.springframework.beans.factory.DisposableBean接口允许一个bean在包含它的容器被销毁时得到回调。 DisposableBean接口指定一个方法：

```java
void destroy() throws Exception;
```

建议您不要使用DisposableBean回调接口，因为它不必要地将代码耦合到Spring。替代方案是在回调方法上使用@PreDestroy注解或指定bean定义支持的通用方法。使用基于XML的配置元数据时，可以使用<bean />上的destroy-method属性指定。使用Java配置，您可以使用@Bean的destroyMethod属性，请参阅[Receiving lifecycle callbacks](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-java-lifecycle-callbacks).。例如，下面的定义：

```xml
<bean id="exampleInitBean" class="examples.ExampleBean" destroy-method="cleanup"/>
```

```java
public class ExampleBean {

        public void cleanup() {
                // do some destruction work (like releasing pooled connections)
        }
}
```

与以下内容完全相同：

```xml
<bean id="exampleInitBean" class="examples.AnotherExampleBean"/>
```

```java
public class AnotherExampleBean implements DisposableBean {

        public void destroy() {
                // do some destruction work (like releasing pooled connections)
        }
}
```

但不会将代码耦合到Spring。

*一个<bean>元素的destroy-method属性可以被赋予一个特殊的（推断的）值，它指示Spring自动检测特定的bean类（实现java.lang.AutoCloseable或java.io.Closeable的任何类）上的public close或shutdown方法。 io.Closeable将因此匹配）。这个特殊的（推断的）值也可以在一个<beans>元素的default-destroy-method属性上设置，以将这个行为应用到整个bean集合（参见[Default initialization and destroy methods](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-lifecycle-default-init-destroy-methods)）。请注意，这中行为是采用Java配置方式时的默认行为。*

#### 默认初始化和销毁方法

当你编写不使用特定于Spring的InitializingBean和DisposableBean回调接口的初始化和销毁方法回调函数时，通常会使用诸如init（），initialize（），dispose（）等名称编写方法。理想情况下，这些生命周期回调方法的名称在整个项目中都是标准化的，这样所有的开发人员都可以使用相同的方法名称并确保一致性。

您可以配置Spring容器来查找指定的初始化和销毁每个bean的回调方法名称。这意味着作为应用程序开发人员，您可以编写应用程序类并使用方法名为init（）的初始化回调，而无需为每个bean定义配置init-method =“init”属性。 Spring IoC容器在创建bean时（根据前面描述的标准生命周期回调协议）调用该方法。此功能还强制规范了初始化和销毁回调方法的命名规则。

假设你的初始化回调方法被命名为init（），并且销毁回调方法被命名为destroy（）。在下面的例子中，你的类将类似于下面的类。

```xml
public class DefaultBlogService implements BlogService {

        private BlogDao blogDao;

        public void setBlogDao(BlogDao blogDao) {
                this.blogDao = blogDao;
        }

        // this is (unsurprisingly) the initialization callback method
        public void init() {
                if (this.blogDao == null) {
                        throw new IllegalStateException("The [blogDao] property must be set.");
                }
        }
}
```

```java
<beans default-init-method="init">

        <bean id="blogService" class="com.foo.DefaultBlogService">
                <property name="blogDao" ref="blogDao" />
        </bean>

</beans>
```

顶层<beans />元素属性中default-init-method属性的存在使Spring IoC容器能够识别出一个名为init的方法作为初始化方法的回调。当一个bean被创建和组装时，如果bean类有这样一个方法，它会在适当的时候被调用。

类似地，您可以使用顶级<beans />元素上的default-destroy-method属性来配置destroy方法回调（即在XML中）。

如果现有bean类已经具有与默认设置回调方法不同的回调方法，那么可以通过使用<bean />的init-method和destroy-method属性指定方法名称（即XML）来覆盖缺省值本身。

Spring容器保证了一个配置好的初始化回调函数在bean被提供了所有的依赖关系后立即被调用。因此初始化回调在原始bean引用上被调用，这意味着AOP拦截器等等还没有被应用到bean。目标bean首先被完全创建，然后应用一个带有拦截器链的AOP代理（例如）。如果目标bean和代理是分别定义的，那么你的代码甚至可以绕过代理与原始目标bean进行交互。因此，将拦截器应用到init方法将是没有意义的，因为这样做会将目标bean的生命周期与其代理/拦截器耦合在一起，这在代码直接与原始目标bean交互时没有意义。

#### 联合生命周期机制

从Spring 2.5开始，您有三个控制bean生命周期行为的选项：InitializingBean和DisposableBean回调接口;自定义init（）和destroy（）方法;和@PostConstruct和@PreDestroy注解。你可以结合这些机制来控制给定的bean。

为同一个bean配置多个生命周期机制，使用不同的初始化方法，如下所示：

```jade
如果为bean配置了多个生命周期机制，并且每个机制都配置了不同的方法名称，那么每个配置的方法都按以下列出的顺序执行。但是，如果在多个生命周期机制中配置了相同的方法名称（例如初始化方法的init（）），则该方法将执行一次，如前一部分所述。
```

为同一个bean配置多个生命周期机制，使用不同的初始化方法，如下所示：

- Methods annotated with `@PostConstruct`
- `afterPropertiesSet()` as defined by the `InitializingBean` callback interface
- A custom configured `init()` method

销毁方法以相同的顺序被调用：

- Methods annotated with `@PreDestroy`
- `destroy()` as defined by the `DisposableBean` callback interface
- A custom configured `destroy()` method

#### Startup and shutdown 回调

`Lifecycle` interface为任何具有自己生命周期的对象（例如启动和停止一些后台进程）定义基本方法：

```java
public interface Lifecycle {

        void start();

        void stop();

        boolean isRunning();
}
```

任何Spring管理的对象都可以实现该接口。然后，当ApplicationContext本身接收到start和stop信号，例如对于运行时的停止/重新启动脚本，它会将这些调用级联到在context中定义的所有Lifecycle实现。它通过委托给一个LifecycleProcessor来完成：

```java
public interface LifecycleProcessor extends Lifecycle {

        void onRefresh();

        void onClose();
}
```

请注意，LifecycleProcessor本身就是`Lifecycle` interface的扩展。它还增加了两个其他的方法来回应正在refresh和close的context。

```
请注意，常规的org.springframework.context.Lifecycle接口只是一个明确的start/stop通知的简单协定，并不意味着在context refresh时自动启动。为了解决这个问题可以考虑实现org.springframework.context.SmartLifecycle，而不是对特定bean的自动启动（包括启动阶段）进行细粒度的控制。另外，请注意，stop通知不保证在销毁之前发生：在正常shutdown时，所有生命周期bean将在受到通用销毁回调之前首先收到stop通知;但是，在上下文的生命周期中进行热刷新或中止刷新尝试时，只会调用销毁方法。
```

启动和关闭调用的顺序可能很重要。如果任何两个对象之间存在“依赖关系”，则依赖方将在其依赖关系之后启动，并且在依赖关系之前停止。但是，有时直接的依赖关系是未知的。您可能只知道某种类型的对象应该在另一种类型的对象之前启动。在这些情况下，SmartLifecycle接口定义了另一个选项，即在超级接口Phased上定义的getPhase（）方法。

```java
public interface Phased {

        int getPhase();
}
```

```java
public interface SmartLifecycle extends Lifecycle, Phased {

        boolean isAutoStartup();

        void stop(Runnable callback);
}
```

启动时，阶段最低的对象首先启动，停止时相反。因此，一个实现SmartLifecycle并且其getPhase（）方法返回Integer.MIN_VALUE的对象将是第一个开始和最后一个停止的对象。另一方面，Integer.MAX_VALUE的阶段值将指示该对象应该最后开始并且首先停止（可能是因为它取决于其他进程正在运行）。当考虑阶段值时，知道没有实现SmartLifecycle的任何“正常”生命周期对象的默认阶段值是0也是重要的。因此，任何负阶段值将表明对象应该在那些标准组件之前开始在他们之后停止），反之亦然，对于任何正阶段值。

正如您所看到的，SmartLifecycle定义的stop方法接受回调。任何实现必须在关闭过程完成后调用该回调的run（）方法。这样可以在需要时进行异步关闭，因为LifecycleProcessor接口的默认实现DefaultLifecycleProcessor将等待每个阶段中的对象组的超时值来调用该回调。默认的每个阶段超时是30秒。您可以通过在上下文中定义一个名为“lifecycleProcessor”的bean来覆盖默认的生命周期处理器实例。如果您只想修改超时值，那么定义以下就足够了：

```xml
<bean id="lifecycleProcessor" class="org.springframework.context.support.DefaultLifecycleProcessor">
        <!-- timeout value in milliseconds -->
        <property name="timeoutPerShutdownPhase" value="10000"/>
</bean>
```

如上所述，LifecycleProcessor接口也定义了用于refresh和close上下文的回调方法。后者将驱动关闭过程，就好像Lifecyce bean的stop（）被显式调用一样，但是只有当上下文关闭时才会发生。另一方面，'刷新'回调使SmartLifecycle bean的另一个功能可以被实现。当上下文刷新时（在所有对象被实例化和初始化之后），该回调将被调用，并且在那时default lifecycle processor将检查由每个SmartLifecycle对象的isAutoStartup（）方法返回的布尔值。如果为“true”，那么该对象将在那一刻开始，而不是等待显式调用上下文或自己的start（）方法（与上下文刷新不同，对于标准的context实现，start过程并不会自动执行） 。 “阶段”值以及任何“依赖”关系将按照上述相同的方式确定启动顺序。

#### 在非web应用程序正常的关闭Spring IoC容器

```
本节仅适用于非Web应用程序。 Spring的基于Web的ApplicationContext实现已经有适当的代码来在相关的Web应用程序关闭时正常关闭Spring IoC容器。
```

如果您在非Web应用程序环境中使用Spring的IoC容器，例如，在客户端桌面环境;您在JVM中注册了一个关闭钩子。这样做可以确保正常关闭并在单例bean上调用相关的销毁方法，从而释放所有资源。当然，你仍然必须正确配置和实现这些销毁回调。

要注册一个关闭钩子，可以调用ConfigurableApplicationContext接口上声明的registerShutdownHook（）方法：

```java
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class Boot {

        public static void main(final String[] args) throws Exception {
                ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");

                // add a shutdown hook for the above context...
                ctx.registerShutdownHook();

                // app runs here...

                // main method exits, hook is called prior to the app shutting down...
        }
}
```

### 1.6.2. ApplicationContextAware and BeanNameAware

当一个ApplicationContext创建一个实现org.springframework.context.ApplicationContextAware接口的对象实例时，该实例保持了对该ApplicationContext的引用

```java
public interface ApplicationContextAware {

        void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
```

因此，bean可以以编程方式操作创建它们的ApplicationContext，通过ApplicationContext接口或通过将对ApplicationContex的引用强制转换为此接口的已知子类（如ConfigurableApplicationContext），这些子类具有其他的功能。一个用途是对其他bean的程序化获取。有时这种能力是有用的;然而，一般来说，你应该避免它，因为它将代码耦合到Spring，并且不遵循控制反转，控制反转中协作者作为属性被提供给bean。 ApplicationContext的其他方法提供对文件资源的访问，发布应用程序事件和访问MessageSource。这些附加功能在 [Additional capabilities of the ApplicationContext](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#context-introduction)进行了介绍。

从Spring 2.5开始，自动装配是获得ApplicationContext引用的另一种方法。 “传统”构造函数和byType自动装配模式（如 [Autowiring collaborators](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-autowire)中所述）可以分别为构造函数参数或setter方法参数提供ApplicationContext类型的依赖。为了获得更多的灵活性，包括自动装配字段和多个参数方法的功能，请使用新的基于注解的自动装配功能。如果这样做的话，ApplicationContext被自动装入一个字段，构造函数参数或方法参数，如果所涉及的字段，构造函数或方法携带@Autowired注解，则该参数将期望ApplicationContext类型。有关更多信息，请参阅[@Autowired](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-autowired-annotation)。

当一个ApplicationContext创建一个实现了org.springframework.beans.factory.BeanNameAware接口的类时，这个类包含了一个在其关联的对象定义中定义的名字的引用。

```java
public interface BeanNameAware {

        void setBeanName(String name) throws BeansException;
}
```

setBeanName()回调在正常的bean属性填充之后，但在初始化回调（如InitializingBean afterPropertiesSet或自定义init方法）之前调用。

### 1.6.3其他Aware接口

除了上面讨论的ApplicationContextAware和BeanNameAware之外，Spring提供了一系列的Aware接口，允许bean向容器指示他们需要一定的基础设施依赖。最重要的Aware接口总结如下 - 作为一般规则，名称是依赖类型的一个很好的指示：

| Name                             | Injected Dependency                      | Explained in…                            |
| -------------------------------- | ---------------------------------------- | ---------------------------------------- |
| `ApplicationContextAware`        | Declaring `ApplicationContext`           | [ApplicationContextAware and BeanNameAware](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-aware) |
| `ApplicationEventPublisherAware` | Event publisher of the enclosing `ApplicationContext` | [Additional capabilities of the ApplicationContext](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#context-introduction) |
| `BeanClassLoaderAware`           | Class loader used to load the bean classes. | [Instantiating beans](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-class) |
| `BeanFactoryAware`               | Declaring `BeanFactory`                  | [ApplicationContextAware and BeanNameAware](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-aware) |
| `BeanNameAware`                  | Name of the declaring bean               | [ApplicationContextAware and BeanNameAware](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-aware) |
| `BootstrapContextAware`          | Resource adapter `BootstrapContext`the container runs in. Typically available only in JCA aware `ApplicationContext`s | [JCA CCI](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/integration.html#cci) |
| `LoadTimeWeaverAware`            | Defined *weaver* for processing class definition at load time | [Load-time weaving with AspectJ in the Spring Framework](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#aop-aj-ltw) |
| `MessageSourceAware`             | Configured strategy for resolving messages (with support for parametrization and internationalization) | [Additional capabilities of the ApplicationContext](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#context-introduction) |
| `NotificationPublisherAware`     | Spring JMX notification publisher        | [Notifications](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/integration.html#jmx-notifications) |
| `ResourceLoaderAware`            | Configured loader for low-level access to resources | [Resources](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#resources) |
| `ServletConfigAware`             | Current `ServletConfig` the container runs in. Valid only in a web-aware Spring `ApplicationContext` | [Spring MVC](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/web.html#mvc) |
| `ServletContextAware`            | Current `ServletContext` the container runs in. Valid only in a web-aware Spring `ApplicationContext` | [Spring MVC](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/web.html#mvc) |

再次注意，这些接口的使用将您的代码绑定到Spring API，并且不遵循控制反转样式。因此，它们被推荐用于需要编程访问容器的基础设施bean。

## 1.7 Bean定义继承

一个bean定义可以包含很多配置信息，包括构造函数参数，属性值和特定于容器的信息，如初始化方法，静态工厂方法名称等等。子bean定义继承父定义的配置数据。子定义可以覆盖一些值，或者根据需要添加其他值。使用父和子bean定义可以节省大量的代码。实际上，这是一种模板形式

如果以编程方式使用ApplicationContext接口，则子Bean定义由ChildBeanDefinition类表示。大多数用户在这个级别上不使用它们，而是用类似ClassPathXmlApplicationContext的方式声明性地配置bean定义。当使用基于XML的配置元数据时，通过使用parent属性指定子bean定义，并指定父bean作为此属性的值。

```xml
<bean id="inheritedTestBean" abstract="true"
                class="org.springframework.beans.TestBean">
        <property name="name" value="parent"/>
        <property name="age" value="1"/>
</bean>

<bean id="inheritsWithDifferentClass"
                class="org.springframework.beans.DerivedTestBean"
                parent="inheritedTestBean" init-method="initialize">
        <property name="name" value="override"/>
        <!-- the age property value of 1 will be inherited from parent -->
</bean>
```

如果没有指定子Bean定义，子bean定义将使用父定义中的bean类，但也可以覆盖它。在后一种情况下，子bean类必须与父bean兼容，也就是说，它必须接受父bean的属性值。

子bean定义继承了父级的scope，构造函数参数值，属性值和方法重写，并且可以添加新的值。您指定的任何scope，初始化方法，销毁方法和/或静态工厂方法设置都将覆盖相应的父级设置。

其余的设置总是从子定义中获取：*depends on*, *autowire mode*, *dependency check*, *singleton*, *lazy init*

上例通过使用abstract属性将父bean定义明确地标记为抽象。如果父定义没有指定一个类，那么显式地标记父bean定义为抽象的是必需的，如下所示：

```xml
<bean id="inheritedTestBeanWithoutClass" abstract="true">
        <property name="name" value="parent"/>
        <property name="age" value="1"/>
</bean>

<bean id="inheritsWithClass" class="org.springframework.beans.DerivedTestBean"
                parent="inheritedTestBeanWithoutClass" init-method="initialize">
        <property name="name" value="override"/>
        <!-- age will inherit the value of 1 from the parent bean definition-->
</bean>
```

父bean不能被自己实例化，因为它是不完整的，而且它也被显式地标记为抽象的。当定义像这样抽象时，它只能用作一个纯粹的模板bean定义，作为子定义的父定义。试图单独使用这样一个抽象的父bean，通过引用它作为另一个bean的ref属性，或者使用父bean id进行显式的getBean（）调用，将返回一个错误。同样，容器的内部preInstantiateSingletons（）方法也会忽略定义为抽象的bean定义。

```
ApplicationContext默认预先实例化所有的singletons 。因此，如果你有一个（父）bean定义，你打算只用作模板，并且这个定义指定了一个类，那么重要的是（至少对于单例bean），你必须确保将abstract属性设置为true ，否则应用程序上下文将实际（尝试）预先实例化抽象bean。
```
## 1.8 容器扩展点

通常，应用程序开发人员不需要继承ApplicationContext实现类。相反，Spring IoC容器可以通过插入特殊集成接口的实现来扩展。接下来的几节将介绍这些集成接口。

### 1.8.1。使用BeanPostProcessor定制bean

BeanPostProcessor接口定义了您可以实现的回调方法，以提供您自己的（或覆盖容器的默认）实例化逻辑，依赖关系解析逻辑等等。如果您想在Spring容器完成实例化，配置和初始化bean之后实现一些自定义逻辑，则可以插入一个或多个BeanPostProcessor实现。

您可以配置多个BeanPostProcessor实例，并且可以通过设置order属性来控制这些BeanPostProcessor执行的顺序。只有在BeanPostProcessor实现Ordered接口的情况下，才能设置此属性;如果你编写自己的BeanPostProcessor，你应该考虑实现Ordered接口。有关更多详细信息，请参阅BeanPostProcessor和Ordered接口的javadocs。另请参阅以下 [programmatic registration of `BeanPostProcessor`s](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-programmatically-registering-beanpostprocessors).。

```
BeanPostProcessors对bean（或对象）实例进行操作;也就是说，Spring IoC容器实例化一个bean实例，然后BeanPostProcessors做它们的工作。

BeanPostProcessors的范围是每个容器。这只与使用容器层次结构有关。如果你在一个容器中定义了一个BeanPostProcessor，它只会在那个容器中处理这个bean。换句话说，在一个容器中定义的bean不会被另一个容器中定义的BeanPostProcessor进行后处理，即使这两个容器都是同一层次结构的一部分。


```

要更改实际的bean定义（即定义bean的蓝图），您需要使用BeanFactoryPostProcessor， [Customizing configuration metadata with a BeanFactoryPostProcessor](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-extension-factory-postprocessors).

org.springframework.beans.factory.config.BeanPostProcessor接口包含两个确定的回调方法。当这样的类被注册为容器的后处理器时，对于由容器创建的每个bean实例，后处理器在容器初始化方法（如InitializingBean的afterPropertiesSet（）方法以及容器声明的init方法）之前，以及任何bean初始化回调之后被调用。后处理器可以对bean实例执行任何操作，也包括完全忽略回调。一个bean后处理器通常检查回调接口，或者可能用一个代理包装一个bean。一些Spring AOP基础结构类被实现为bean后处理器，以提供代理包装逻辑。

ApplicationContext自动检测实现BeanPostProcessor接口的配置元数据中定义的任何Bean。 ApplicationContext将这些bean注册为后处理器，以便稍后在创建bean时调用它们。 Bean后处理器可以像任何其他bean一样部署在容器中。

请注意，在configuration类中使用@Bean factory method声明BeanPostProcessor时，工厂方法的返回类型应该是实现类本身，或者至少是org.springframework.beans.factory.config.BeanPostProcessor接口，这样才可以清楚地指示该bean的后处理器性质。否则，在完全创建它之前，ApplicationContext将不能通过类型进行自动检测到它的性质。由于BeanPostProcessor需要尽早实例化以适用于上下文中其他bean的初始化，因此这种类型提前检测是至关重要的。

```
以编程方式注册BeanPostProcessors
尽管BeanPostProcessor注册的推荐方法是通过ApplicationContext自动检测（如上所述），但也可以使用addBeanPostProcessor方法以编程方式将其注册到ConfigurableBeanFactory。当需要在注册之前评估条件逻辑，或者甚至跨层次结构中的上下文复制Bean后处理器时，这可能非常有用。但请注意，以编程方式添加的BeanPostProcessors不遵守Ordered接口。在这里，注册的顺序决定了执行的顺序。还要注意，通过程序注册的BeanPostProcessors总是在通过自动检测注册的BeanPostProcessors之前进行处理，而不管任何明确的排序。
```



```以下示例显示如何在ApplicationContext中编写，注册和使用BeanPostProcessors
BeanPostProcessors和AOP自动代理
实现BeanPostProcessor接口的类是特殊的，并且被容器区别对待。作为ApplicationContext的特殊启动阶段的一部分，它们直接引用的所有BeanPostProcessors和Bean都将在启动时实例化。接下来，所有BeanPostProcessors都以已排序的方式注册，并应用于容器中的所有其他bean。由于AOP自动代理被实现为一个BeanPostProcessor本身，所以BeanPostProcessors和它们直接引用的bean都不适合自动代理，因此没有编入它们的方面。
对于任何这样的bean，您应该看到一条信息性日志消息：“Bean foo不适合被所有BeanPostProcessor接口处理（例如：不适合自动代理）”。
请注意，如果你使用自动装配或@Resource（可能会回退到自动装配）将Bean注入到BeanPostProcessor中，Spring可能会在搜索类型匹配的依赖时注入不符合预期的bean，因此使这个BeanPostProcessor不适用于自动代理或其他类型bean后期处理。例如，如果你有一个用@Resource注解的依赖项，其中的field/setter名字并不直接对应于一个bean的声明名称，并且没有使用name属性，那么Spring将按照类型来匹配其他bean。
```

以下示例显示如何在ApplicationContext中编写，注册和使用BeanPostProcessors

#### 示例：Hello World，BeanPostProcessor风格

这第一个例子说明了基本用法。该示例显示了一个自定义的BeanPostProcessor实现，该实现调用每个bean的toString（）方法，因为它是由容器创建的，并将结果字符串打印到系统控制台。

```java
public class InstantiationTracingBeanPostProcessor implements BeanPostProcessor {

        // simply return the instantiated bean as-is
        public Object postProcessBeforeInitialization(Object bean, String beanName) {
                return bean; // we could potentially return any object reference here...
        }

        public Object postProcessAfterInitialization(Object bean, String beanName) {
                System.out.println("Bean '" + beanName + "' created : " + bean.toString());
                return bean;
        }
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:lang="http://www.springframework.org/schema/lang"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
                http://www.springframework.org/schema/beans/spring-beans.xsd
                http://www.springframework.org/schema/lang
                http://www.springframework.org/schema/lang/spring-lang.xsd">

        <lang:groovy id="messenger"
                        script-source="classpath:org/springframework/scripting/groovy/Messenger.groovy">
                <lang:property name="message" value="Fiona Apple Is Just So Dreamy."/>
        </lang:groovy>

        <!--
        when the above bean (messenger) is instantiated, this custom
        BeanPostProcessor implementation will output the fact to the system console
        -->
        <bean class="scripting.InstantiationTracingBeanPostProcessor"/>

</beans>
```

注意InstantiationTracingBeanPostProcessor是如何被简单定义的。它甚至没有名字，因为它是一个bean，它可以像任何其他bean一样依赖注入。 （前面的配置也定义了一个由Groovy脚本支持的bean。Spring动态语言支持在 [Dynamic language support](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/languages.html#dynamic-language)一章中有详细介绍。

以下简单的Java应用程序执行上述代码和配置：

```java
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scripting.Messenger;

public final class Boot {

        public static void main(final String[] args) throws Exception {
                ApplicationContext ctx = new ClassPathXmlApplicationContext("scripting/beans.xml");
                Messenger messenger = (Messenger) ctx.getBean("messenger");
                System.out.println(messenger);
        }

}
```

前面的应用程序的输出类似于以下内容：

```
Bean 'messenger' created : org.springframework.scripting.groovy.GroovyMessenger@272961
org.springframework.scripting.groovy.GroovyMessenger@272961
```

#### 示例：RequiredAnnotationBeanPostProcessor

将回调接口或注解与自定义BeanPostProcessor实现结合使用是扩展Spring IoC容器的常用方法。 Spring的RequiredAnnotationBeanPostProcessor就是一个例子，它是Spring发行版的一个BeanPostProcessor实现，它确保用（任意）注解标记的bean的属性确实被（配置为）依赖注入一个值。

### 1.8.2。使用BeanFactoryPostProcessor定制配置元数据

下一个我们将要看的扩展点是org.springframework.beans.factory.config.BeanFactoryPostProcessor。这个接口的语义与BeanPostProcessor类似，主要区别在于：BeanFactoryPostProcessor在bean配置元数据上操作;也就是说，Spring IoC容器允许BeanFactoryPostProcessor读取配置元数据并在容器实例化除BeanFactoryPostProcessor之外的任何Bean之前对其进行更改。

您可以配置多个BeanFactoryPostProcessor，并且可以通过设置order属性来控制这些BeanFactoryPostProcessor执行的顺序。但是，只有BeanFactoryPostProcessor实现Ordered接口，才能设置此属性。如果你编写自己的BeanFactoryPostProcessor，你应该考虑实现Ordered接口。请参阅BeanFactoryPostProcessor和Ordered接口的javadoc以获取更多详细信息。

```
如果要更改实际的bean实例（即从配置元数据创建的对象），则需要使用BeanPostProcessor（如上面的“使用BeanPostProcessor定制bean”所述）。虽然技术上可以在BeanFactoryPostProcessor中使用bean实例（例如，使用BeanFactory.getBean（）），但这样做会导致bean过早实例化，从而违反标准容器生命周期。这可能会导致负面影响，如绕过bean后处理。
另外，BeanFactoryPostProcessors的范围是每个容器。这只与使用容器层次结构有关。如果在一个容器中定义了一个BeanFactoryPostProcessor，它将只应用于该容器中的bean定义。一个容器中的Bean定义将不会由另一个容器中的BeanFactoryPostProcessors进行后处理，即使这两个容器都是同一层次结构的一部分。
```

当在ApplicationContext中声明一个bean工厂后处理器时，它会自动执行，以便将更改应用于定义容器的配置元数据。 Spring包含许多预定义的bean工厂后处理器，例如PropertyOverrideConfigurer和PropertyPlaceholderConfigurer。例如，也可以使用自定义BeanFactoryPostProcessor来注册自定义属性编辑器。

ApplicationContext自动检测部署到其中的实现BeanFactoryPostProcessor接口的任何bean。它在适当的时候使用这些bean作为bean工厂后处理器。您可以像任何其他bean一样部署这些后处理器bean.

```
和BeanPostProcessors一样，您通常不希望将BeanFactoryPostProcessors配置为延迟初始化。如果没有其他Bean引用Bean（Factory）PostProcessor，则该后处理器根本不会被实例化。因此，将其标记为延迟初始化将被忽略，即使您在<beans />元素的声明中将default-lazy-init属性设置为true，Bean（Factory）PostProcessor也不会懒加载。
```

#### 例如：类名替换PropertyPlaceholderConfigurer

您可以使用PropertyPlaceholderConfigurer，通过使用标准Java Properties格式的独立文件，从bean定义中外部化属性值。这样做使得部署应用程序的人员可以自定义特定于环境的属性，如数据库URL和密码，而无需修改容器的主XML定义文件或容器相关的文件。

考虑以下基于XML的配置元数据片段，其中定义了包含占位符值的数据源。该示例显示了从外部属性文件配置的属性。在运行时，一个PropertyPlaceholderConfigurer被应用到将替换DataSource的一些属性的元数据。要替换的值被指定为遵循Ant / log4j / JSP EL样式的$ {property-name}形式的占位符。

```xml
<bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
        <property name="locations" value="classpath:com/foo/jdbc.properties"/>
</bean>

<bean id="dataSource" destroy-method="close"
                class="org.apache.commons.dbcp.BasicDataSource">
        <property name="driverClassName" value="${jdbc.driverClassName}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
</bean>
```

实际值来自标准Java属性格式中的另一个文件：

```properties
jdbc.driverClassName=org.hsqldb.jdbcDriver
jdbc.url=jdbc:hsqldb:hsql://production:9002
jdbc.username=sa
jdbc.password=root
```

因此，字符串$ {jdbc.username}在运行时被替换为值“sa”，同样适用于与属性文件中的键匹配的其他占位符值。 PropertyPlaceholderConfigurer检查大多数属性和bean定义属性中的占位符。此外，占位符前缀和后缀可以自定义。

使用Spring 2.5中引入的context名称空间，可以使用专用的配置元素来配置属性占位符。一个或多个路径可以逗号分隔提供给location属性。

```xml
<context:property-placeholder location="classpath:com/foo/jdbc.properties"/>
```

PropertyPlaceholderConfigurer不仅在您指定的属性文件中查找属性。默认情况下，如果在指定的属性文件中找不到属性，它也会检查Java系统属性。您可以通过使用以下三个支持的整数值之一来设置configurer的systemPropertiesMode属性来自定义此行为：

- *never* (0): Never check system properties
- *fallback* (1): Check system properties if not resolvable in the specified properties files. This is the default.
- *override* (2): Check system properties first, before trying the specified properties files. This allows system properties to override any other property source.

有关更多信息，请参阅PropertyPlaceholderConfigurer javadocs。

```xml
您可以使用PropertyPlaceholderConfigurer替换类名，当您必须在运行时选择特定的实现类时，这有时很有用。例如：
<bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
        <property name="locations">
                <value>classpath:com/foo/strategy.properties</value>
        </property>
        <property name="properties">
                <value>custom.strategy.class=com.foo.DefaultStrategy</value>
        </property>
</bean>

<bean id="serviceStrategy" class="${custom.strategy.class}"/>
如果类不能在运行时被解析为一个有效的类，当bean将要创建时，它将会解析失败，非延迟初始化的bean在ApplicationContext 的preInstantiateSingletons（）阶段将发生这种情况。
```

#### 例子：PropertyOverrideConfigurer

PropertyOverrideConfigurer（另一个bean工厂后处理器）类似于PropertyPlaceholderConfigurer，但与后者不同，原始定义可以具有缺省值或根本没有值对于所有的bean properties。如果重写的属性文件没有某个bean属性的条目，则使用默认的上下文定义。

请注意，bean定义并不知道被重写，所以从XML定义文件中不会立即明显地看到覆盖配置器正在被使用。在为同一个bean属性定义不同值的多个PropertyOverrideConfigurer实例的情况下，最后一个获胜，这是由于重载机制。

属性文件配置行采用以下格式：

```properties
beanName.property=value
```

例如：

```properties
dataSource.driverClassName=com.mysql.jdbc.Driver
dataSource.url=jdbc:mysql:mydb
```

这个示例文件可以用于容器定义，该容器定义包含一个名为dataSource的bean，该bean具有*driver* 和url属性。

复合属性名称也是被支持的，只要被重写的最后一个属性除外的路径的每个组件都已经是非空的（大概是由构造函数初始化的）。在这个例子中...

```properties
foo.fred.bob.sammy=123
```

在上面的例子中，foo bean的fred属性的bob属性的sammy属性被设置为标量值123。foo、fred、bob应该提前被初始化。

```
指定的覆盖值总是文字值;它们不被转换成bean引用。当XML bean定义中的原始值指定一个bean引用时，这个约定也适用。
```

使用Spring 2.5中引入的context名称空间，可以使用专用的配置元素来配置属性覆盖.

```xml
<context:property-override location="classpath:override.properties"/>
```

### 1.8.3使用FactoryBean定制实例化逻辑

为自己就是工厂的对象实现org.springframework.beans.factory.FactoryBean接口。

FactoryBean接口是Spring IoC容器实例化逻辑的可插入点。如果你有复杂的初始化代码，用Java（而不是（可能）冗长的XML）来表达更好，你可以创建自己的FactoryBean，在该类中写入复杂的初始化，然后将自定义的FactoryBean插入到容器中。

FactoryBean接口提供了三种方法：

- Object getObject（）：返回这个工厂创建的对象的一个实例。实例可能是共享的，这取决于这个工厂是否返回singletons或prototypes
- boolean isSingleton（）：如果此FactoryBean返回单例，则返回true，否则返回false
- getObjectType（）：返回getObject（）方法返回的对象类型，如果事先不知道类型，则返回null

FactoryBean的概念和接口被用在Spring框架的许多地方。 FactoryBean接口的50多个实现与Spring本身一起提供。

当你需要问一个实际的FactoryBean实例本身而不是它产生的bean时，需要在调用ApplicationContext的getBean（）方法时,在bean id前面加上$。这样做之后，对于具有myBean id的给定FactoryBean，在容器上调用getBean（“myBean”）将返回FactoryBean的产品;而调用getBean（“&myBean")将返回FactoryBean实例本身。

## 1.9基于注解的容器配置

注解是否比XML更好地配置Spring？

引入基于注解的配置引发了这种方法是否比XML更好的讨论。每种方法都有其优缺点，通常由开发者决定哪种策略更适合他们。由于它们被定义的方式，注解在其声明中提供了很多的上下文，从而导致更短，更简洁的配置。但是，XML擅长于连接组件而不涉及其源代码或重新编译它们。一些开发人员更喜欢接近源代码的配置，而另一些开发人员则认为注解类不再是POJO，而且配置变得分散，难以控制。

无论选择什么，Spring都可以兼容这两种风格，甚至可以将它们混合在一起。值得指出的是，通过[JavaConfig](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-java) 选项，Spring允许以非侵入方式使用注解，而不涉及目标组件的源代码，而在工具方面，[Spring Tool Suite](https://spring.io/tools/sts).支持所有配置样式。

XML配置的替代方法由基于注解的配置提供，该配置依赖字节码元数据来连接组件而不是角括号声明。开发人员不是使用XML来描述bean依赖，而是通过在相关的类，方法或字段声明中使用注解将配置移动到组件类本身中。如 [Example: The RequiredAnnotationBeanPostProcessor](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-extension-bpp-examples-rabpp)所述：将BeanPostProcessor与注解一起使用是扩展Spring IoC容器的常用方法。例如，Spring 2.0引入了用@Required注解强制实现所需属性注入的可能性。 Spring 2.5使得遵循相同的通用方法来驱动Spring的依赖注入成为可能。从本质上讲，@ Aututired注解提供了与[Autowiring collaborators](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-factory-autowire)中描述的相同的功能，但具有更细致的控制和更广泛的适用性。 Spring 2.5还增加了对@PostConstruct和@PreDestroy等JSR-250注解的支持。 Spring 3.0增加了对javax.inject包（例如@Inject和@Named）中包含的JSR-330（Dependency Injection for Java）注解的支持。关于这些注解的细节可以参考 [relevant section](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-standard-annotations).。

```
注入注入是在XML注入之前执行的，因此，如果属性通过两种方式都配置了，则后者的配置将覆盖前者的属性。
```

与往常一样，您可以将它们注册为单独的bean定义，但是也可以通过在基于XML的Spring配置中包含以下标签来隐式注册它们（请注意包含context名称空间）：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:context="http://www.springframework.org/schema/context"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
                http://www.springframework.org/schema/beans/spring-beans.xsd
                http://www.springframework.org/schema/context
                http://www.springframework.org/schema/context/spring-context.xsd">

        <context:annotation-config/>

</beans>
```

隐式注册的后处理器包括AutowiredAnnotationBeanPostProcessor，CommonAnnotationBeanPostProcessor，PersistenceAnnotationBeanPostProcessor，以及前面提到的RequiredAnnotationBeanPostProcessor。

<context：annotation-config />只在定义bean的应用程序context中查找注解。这意味着，如果您将<context：annotation-config />放在一个DispatcherServlet的WebApplicationContext中，它只会检查controllers中的@Autowired bean，而不是您的services。有关更多信息，请参阅[DispatcherServlet](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/web.html#mvc-servlet)。

### 1.9.1. @Required

@Required注解适用于bean property setter方法，如下例所示：

```xml
public class SimpleMovieLister {

        private MovieFinder movieFinder;

        @Required
        public void setMovieFinder(MovieFinder movieFinder) {
                this.movieFinder = movieFinder;
        }

        // ...
}
```

这个注解简单地表明受影响的bean属性必须在配置时通过bean定义中的显式属性值或通过autowiring来注入。如果受影响的bean属性尚未填充，容器将引发异常;这可以实现提前和明确的失败，避免后面的NullPointerExceptions等。仍然建议你将断言放入bean类本身，例如放入一个init方法。这样做即使在容器外部使用该类时也会强制执行那些必需的引用和值。

### 1.9.2. @Autowired

在下面的例子中，JSR 330的@Inject注解可以用来代替Spring的@Autowired注解。在[here](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-standard-annotations) 看到更多的细节。

您可以将@Autowired注解应用于构造函数

```xml
public class MovieRecommender {

        private final CustomerPreferenceDao customerPreferenceDao;

        @Autowired
        public MovieRecommender(CustomerPreferenceDao customerPreferenceDao) {
                this.customerPreferenceDao = customerPreferenceDao;
        }

        // ...
}
```

从Spring Framework 4.3开始，如果目标bean只定义了一个构造函数，那么这个构造函数的@Autowired注解就不再需要了。但是，如果有几个构造函数可用，则必须至少注明一个构造函数来告诉容器使用哪一个。

正如所料，您也可以将@Autowired注解应用于“传统”setter方法

```java
public class SimpleMovieLister {

        private MovieFinder movieFinder;

        @Autowired
        public void setMovieFinder(MovieFinder movieFinder) {
                this.movieFinder = movieFinder;
        }

        // ...
}
```

您也可以将注解应用于具有任意名称和/或多个参数的方法：

```java
public class MovieRecommender {

        private MovieCatalog movieCatalog;

        private CustomerPreferenceDao customerPreferenceDao;

        @Autowired
        public void prepare(MovieCatalog movieCatalog,
                        CustomerPreferenceDao customerPreferenceDao) {
                this.movieCatalog = movieCatalog;
                this.customerPreferenceDao = customerPreferenceDao;
        }

        // ...
}
```

您也可以将@Autowired应用于字段，甚至可以将其与构造函数混合使用：

```java
public class MovieRecommender {

        private final CustomerPreferenceDao customerPreferenceDao;

        @Autowired
        private MovieCatalog movieCatalog;

        @Autowired
        public MovieRecommender(CustomerPreferenceDao customerPreferenceDao) {
                this.customerPreferenceDao = customerPreferenceDao;
        }

        // ...
}
```

```
确保您的目标组件（例如，MovieCatalog，CustomerPreferenceDao）能够在在容器中找到，并且类型和@ Autowired注解的目标组件类型一致。否则由于在运行时找不到类型匹配，注入可能会失败。

对于XML定义的bean或通过类路径扫描找到的组件类，容器通常会提前知道具体的类型。但是，对于@Bean工厂方法，您需要确保声明的返回类型具有足够的表现力。对于实现多个接口的组件或可能由其实现类型引用的组件，请考虑在工厂方法中声明最具体的返回类型（至少符合注入点引用的bean的类型）。

```

通过将注解添加到需要该类型的数组的字段或方法，可以提供ApplicationContext中特定类型的所有Bean：

```java
public class MovieRecommender {

        @Autowired
        private MovieCatalog[] movieCatalogs;

        // ...
}
```

类型化的集合也是如此：

```java
public class MovieRecommender {

        private Set<MovieCatalog> movieCatalogs;

        @Autowired
        public void setMovieCatalogs(Set<MovieCatalog> movieCatalogs) {
                this.movieCatalogs = movieCatalogs;
        }

        // ...
}
```

您的bean可以实现org.springframework.core.Ordered接口，或者如果希望数组或列表中的项目按特定顺序排序，则可以使用@Order或标准@Priority注解。

甚至map也可以自动装配，只要预期的键类型是字符串。 Map值将包含期望类型的所有bean，并且键将包含相应的bean名称.

```java
public class MovieRecommender {

        private Map<String, MovieCatalog> movieCatalogs;

        @Autowired
        public void setMovieCatalogs(Map<String, MovieCatalog> movieCatalogs) {
                this.movieCatalogs = movieCatalogs;
        }

        // ...
}
```

默认情况下，如果没有候选bean可用，自动装配失败;默认行为是将使用自动装配的方法，构造函数和字段视为指示所需的依赖关系。这种行为可以改变，如下所示。

```java
public class SimpleMovieLister {

        private MovieFinder movieFinder;

        @Autowired(required = false)
        public void setMovieFinder(MovieFinder movieFinder) {
                this.movieFinder = movieFinder;
        }

        // ...
}
```

```
每个类只有一个带注解的构造函数可以被标记为required，但是多个构造函数的注解可以被标记为non-required。在这种情况下，每个构造函数都被视为候选人，而Spring使用的是最贪婪的构造函数，即它的依赖关系可以得到满足，并且具有最多参数的构造函数。
和@Required注解相比推荐使用@Autowired的required属性。required属性表示该属性不是自动装配所需的，如果该属性不能被自动装配，该属性将被忽略。另一方面， @Required所强调的是依赖必须被注入不管以容器所支持的任何手段。如果没有值被注入，则会引发相应的异常。
```

您也可以使用@Autowired在众所周知的可解析依赖项的接口：BeanFactory，ApplicationContext，Environment，ResourceLoader，ApplicationEventPublisher和MessageSource。这些接口及其扩展接口（如ConfigurableApplicationContext或ResourcePatternResolver）会自动解析，无需进行特殊设置。

```java
public class MovieRecommender {

        @Autowired
        private ApplicationContext context;

        public MovieRecommender() {
        }

        // ...
}
```

```
@Autowired，@Inject，@Resource和@Value注解由Spring BeanPostProcessor实现处理，这意味着您不能在自己的BeanPostProcessor或BeanFactoryPostProcessor类型（如果有）中应用这些注解。这些类型必须通过XML或使用Spring @Bean方法明确依赖。
```
### 1.9.3使用@Primary细粒度控制基于注解的自动装配

因为按类型自动装配可能导致多个候选依赖，所以通常有必要对选择过程有更多的控制权。一种方法是用Spring的@Primary注解来实现这一点。 @Primary表示当多个bean满足自动装配的依赖类型时，应该给予一个特定的bean优先。如果候选依赖中只有一个primary bean，那么这将是自动导入的bean.

假设我们有如下配置，将firstMovieCatalog定义为主MovieCatalog

```java
@Configuration
public class MovieConfiguration {

        @Bean
        @Primary
        public MovieCatalog firstMovieCatalog() { ... }

        @Bean
        public MovieCatalog secondMovieCatalog() { ... }

        // ...
}
```

有了这样的配置，下面的MovieRecomder将被自动装配成第一个MovieMatalog

```java
public class MovieRecommender {

        @Autowired
        private MovieCatalog movieCatalog;

        // ...
}
```

相应的bean定义如下所示。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:context="http://www.springframework.org/schema/context"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
                http://www.springframework.org/schema/beans/spring-beans.xsd
                http://www.springframework.org/schema/context
                http://www.springframework.org/schema/context/spring-context.xsd">

        <context:annotation-config/>

        <bean class="example.SimpleMovieCatalog" primary="true">
                <!-- inject any dependencies required by this bean -->
        </bean>

        <bean class="example.SimpleMovieCatalog">
                <!-- inject any dependencies required by this bean -->
        </bean>

        <bean id="movieRecommender" class="example.MovieRecommender"/>

</beans>
```

### 1.9.4使用@Qualifier对基于注解的自动装配进行细粒度控制

当根据类型进行自动转配时，并且有多个候选依赖，如果多个候选依赖中有一个能够确定则使用@Primary是一种有效的方法。当需要对选择自动装配过程进行更多的控制时，可以使用Spring的@Qualifier注解。您可以将限定符值与特定参数相关联，缩小匹配类型的集合，以便为每个参数选择特定的bean。在最简单的情况下，@Qualifier参数值可以是一个简单的描述性的值：

```java
public class MovieRecommender {

        @Autowired
        @Qualifier("main")
        private MovieCatalog movieCatalog;

        // ...
}
```

@Qualifier注解也可以在单独的构造函数参数或方法参数中指定：

```java
public class MovieRecommender {

        private MovieCatalog movieCatalog;

        private CustomerPreferenceDao customerPreferenceDao;

        @Autowired
        public void prepare(@Qualifier("main")MovieCatalog movieCatalog,
                        CustomerPreferenceDao customerPreferenceDao) {
                this.movieCatalog = movieCatalog;
                this.customerPreferenceDao = customerPreferenceDao;
        }

        // ...
}
```

相应的bean定义如下所示。具有qualifier值“main”的bean将与具有相同qualifier值的构造函数参数对应。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:context="http://www.springframework.org/schema/context"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
                http://www.springframework.org/schema/beans/spring-beans.xsd
                http://www.springframework.org/schema/context
                http://www.springframework.org/schema/context/spring-context.xsd">

        <context:annotation-config/>

        <bean class="example.SimpleMovieCatalog">
                <qualifier value="main"/>

                <!-- inject any dependencies required by this bean -->
        </bean>

        <bean class="example.SimpleMovieCatalog">
                <qualifier value="action"/>

                <!-- inject any dependencies required by this bean -->
        </bean>

        <bean id="movieRecommender" class="example.MovieRecommender"/>

</beans>
```

对于回退匹配，bean名称被视为默认限定符值。因此，您可以使用id“main”而不是嵌套的qualifier元素来定义bean，也能得到相同的匹配结果。但是，虽然可以使用这个约定来通过名称来引用特定的bean，但@Autowired根本上是在根据类型驱动注入的前提下再根据限定符的值来注入依赖的。这意味着限定符值（即使是bean名称后备）首先要在符合匹配的类型的bean中再限定依赖。良好的限定符值是“main”或“EMEA”或“persistent”，表示与bean id无关的特定组件的特征，如前面的例子在匿名bean定义的情况下可以自动生成。

限定符也适用于类型的集合，如上所述，例如，Set <MovieCatalog>。在这种情况下，根据声明的限定符将所有匹配的bean注入集合。这意味着限定词不一定是唯一的;他们只是构成过滤标准。例如，您可以使用相同的限定符值“action”定义多个MovieCatalog bean，所有这些都将被注入到由@Qualifier（“action”）注解的Set <MovieCatalog>中。

```
如果您打算按名称驱动注解注入，则不要使用@Autowired，即使在技术上能够通过@Qualifier值引用bean名称实现。推荐使用JSR-250 @Resource注解，它的语义定义是标识一个特定的目标组件通过它的唯一名称，组件的类型与匹配过程无关。 @Autowired具有相当不同的语义：在按类型选择候选bean之后，然后才根据指定的字符串限定符值在这些类型选择的候选者选择。
对于自己被定义为collection/map或数组类型的bean，@Resource是一个很好的解决方案，它通过唯一名称引用特定的集合或数组bean。也就是说，从4.3开始，只要元素类型信息保存在@Bean返回类型签名或集合继承层次结构中，就可以通过Spring的@Autowired类型匹配算法来匹配collection/map和数组类型。在这种情况下，可以使用限定符值在同一类型的集合中进行选择，如前一段所述。
从4.3开始，@Autowired也考虑自引用注入，即引用当前注入的bean。请注意，自我注入是一个后备;对其他组件的正常依赖关系始终具有优先权。从这个意义上说，自我引用不参与正常的候选依赖选择，因此尤其不是主要的;相反，它们总是最低优先。在实践中，仅使用自引用作为最后的手段，例如，用于通过bean的事务代理来调用同一实例上的其他方法：考虑在这种情况下将受影响的方法分解为单独的委托bean。或者，使用@Resource可以通过当前它的唯一名称获取代理返回到当前bean。
@Autowired适用于字段，构造函数和多参数方法，允许通过qualifier 注解在参数级缩小依赖选择范围。相比之下，@Resource仅支持的字段和具有单个参数的setter方法。因此，如果注入目标是构造函数或多参数方法，则坚持使用限定符。

```

您可以创建自己的自定义限定符注解。只需定义一个注解并在您的定义中提供@Qualifier注解：

```java
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface Genre {

        String value();
}
```

然后，您可以在自动注入的字段和参数上提供自定义限定符：

```java
public class MovieRecommender {

        @Autowired
        @Genre("Action")
        private MovieCatalog actionCatalog;

        private MovieCatalog comedyCatalog;

        @Autowired
        public void setComedyCatalog(@Genre("Comedy") MovieCatalog comedyCatalog) {
                this.comedyCatalog = comedyCatalog;
        }

        // ...
}
```

接下来，提供候选bean定义的信息。您可以将<qualifier />标记添加为<bean />标记的子元素，然后指定type和value以匹配您的自定义限定符注解。该type与注解的完全限定的类名相匹配。或者，如果不存在相互冲突名称的风险，则可以使用短名称。下面的例子演示了这两种方式。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:context="http://www.springframework.org/schema/context"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
                http://www.springframework.org/schema/beans/spring-beans.xsd
                http://www.springframework.org/schema/context
                http://www.springframework.org/schema/context/spring-context.xsd">

        <context:annotation-config/>

        <bean class="example.SimpleMovieCatalog">
                <qualifier type="Genre" value="Action"/>
                <!-- inject any dependencies required by this bean -->
        </bean>

        <bean class="example.SimpleMovieCatalog">
                <qualifier type="example.Genre" value="Comedy"/>
                <!-- inject any dependencies required by this bean -->
        </bean>

        <bean id="movieRecommender" class="example.MovieRecommender"/>

</beans>
```

在[Classpath scanning and managed components](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-classpath-scanning)中，您将看到一个基于注解的替代方法来提供XML中的限定符元数据。具体来说，请参阅 [Providing qualifier metadata with annotations](https://docs.spring.io/spring/docs/5.0.1.RELEASE/spring-framework-reference/core.html#beans-scanning-qualifiers)。

在某些情况下，使用没有value的注解可能就足够了。当注解用于更通用的用途时，这可能是有用的，并且可以应用于多种不同类型的依赖关系。例如，您可能会提供一个*offline*catalog ，在没有Internet连接可用时将被搜索。首先定义简单的注解：

```java
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface Offline {

}
```

加注解到将要被自动装配的字段或属性

```java
public class MovieRecommender {

        @Autowired
        @Offline
        private MovieCatalog offlineCatalog;

        // ...
}
```

现在，bean定义只需要一个限定符类型：

```xml
<bean class="example.SimpleMovieCatalog">
        <qualifier type="Offline"/>
        <!-- inject any dependencies required by this bean -->
</bean>
```

您还可以定义接受指定属性的自定义qualifier 注解，而不是简单的value属性。如果在一个要自动装配的字段或参数上指定了多个属性值，则一个bean定义必须匹配所有这些属性值才能被认为是一个自动装配候选。作为一个例子，考虑下面的注解定义：

```java
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface MovieQualifier {

        String genre();

        Format format();

}
```

在这个例子中，Format是一个枚举：

```java
public enum Format {
        VHS, DVD, BLURAY
}
```

要自动装配的字段使用自定义限定符注解，并包含所有属性的值：type和format。

```java
public class MovieRecommender {

        @Autowired
        @MovieQualifier(format=Format.VHS, genre="Action")
        private MovieCatalog actionVhsCatalog;

        @Autowired
        @MovieQualifier(format=Format.VHS, genre="Comedy")
        private MovieCatalog comedyVhsCatalog;

        @Autowired
        @MovieQualifier(format=Format.DVD, genre="Action")
        private MovieCatalog actionDvdCatalog;

        @Autowired
        @MovieQualifier(format=Format.BLURAY, genre="Comedy")
        private MovieCatalog comedyBluRayCatalog;

        // ...
}
```

最后，bean定义应该包含匹配的qualifier值。这个例子还演示了bean meta属性可以用来代替<qualifier />子元素。如果可用的话，<qualifier />及其属性优先，但是如果不存在这样的qualifier，自动装配机制将回退到<meta />标记中提供的值，如以下示例中的最后两个bean定义。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:context="http://www.springframework.org/schema/context"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
                http://www.springframework.org/schema/beans/spring-beans.xsd
                http://www.springframework.org/schema/context
                http://www.springframework.org/schema/context/spring-context.xsd">

        <context:annotation-config/>

        <bean class="example.SimpleMovieCatalog">
                <qualifier type="MovieQualifier">
                        <attribute key="format" value="VHS"/>
                        <attribute key="genre" value="Action"/>
                </qualifier>
                <!-- inject any dependencies required by this bean -->
        </bean>

        <bean class="example.SimpleMovieCatalog">
                <qualifier type="MovieQualifier">
                        <attribute key="format" value="VHS"/>
                        <attribute key="genre" value="Comedy"/>
                </qualifier>
                <!-- inject any dependencies required by this bean -->
        </bean>

        <bean class="example.SimpleMovieCatalog">
                <meta key="format" value="DVD"/>
                <meta key="genre" value="Action"/>
                <!-- inject any dependencies required by this bean -->
        </bean>

        <bean class="example.SimpleMovieCatalog">
                <meta key="format" value="BLURAY"/>
                <meta key="genre" value="Comedy"/>
                <!-- inject any dependencies required by this bean -->
        </bean>

</beans>
```

### 1.9.5使用泛型作为自动装配限定符

除了@Qualifier注解之外，还可以使用Java泛型作为隐式形式的限定条件。例如，假设您有以下配置：

```java
@Configuration
public class MyConfiguration {

    @Bean
    public StringStore stringStore() {
        return new StringStore();
    }

    @Bean
    public IntegerStore integerStore() {
        return new IntegerStore();
    }
}
```

假设上面的Bean实现了一个通用接口，即Store <String>和Store <Integer>，那么可以使用@Autowire 自动装配Store接口，而泛型将用作限定符：

```java
@Autowired
private Store<String> s1; // <String> qualifier, injects the stringStore bean

@Autowired
private Store<Integer> s2; // <Integer> qualifier, injects the integerStore bean
```

泛型限定符在自动装配Lists, Maps and Arrays时也适用：

```java
// Inject all Store beans as long as they have an <Integer> generic
// Store<String> beans will not appear in this list
@Autowired
private List<Store<Integer>> s;
```

### 1.9.6 CustomAutowireConfigurer

CustomAutowireConfigurer是一个BeanFactoryPostProcessor，它使您能够注册自己的自定义限定符注解类型，即使它们没有用Spring的@Qualifier注解。

```xml
<bean id="customAutowireConfigurer"
        class="org.springframework.beans.factory.annotation.CustomAutowireConfigurer">
    <property name="customQualifierTypes">
        <set>
            <value>example.CustomQualifier</value>
        </set>
    </property>
</bean
```

AutowireCandidateResolver通过以下方式确定自动装配候选依赖：

- the `autowire-candidate` value of each bean definition
- any `default-autowire-candidates` pattern(s) available on the `<beans/>` element
- the presence of `@Qualifier` annotations and any custom annotations registered with the `CustomAutowireConfigurer`

当多个bean被认定为autowire候选者时，选择的原则如下：如果在候选者中恰好一个bean定义具有被设置为true的`primary` 属性，则将被选择。

### 1.9.7. @Resource

Spring还支持在字段上或者bean属性setter方法上使用JSR-250中的 @Resource进行注入。这是Java EE 5和6中的常见模式，例如在JSF 1.2托管的bean或JAX-WS 2.0端点中。 Spring也支持在Spring管理对象中使用这个模式。

@Resource有一个name属性，默认情况下，Spring将该值解释为要注入的bean名称。换句话说，它遵循 *by-name* 语义，正如在这个例子中所演示的那样：

```java
public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Resource(name="myMovieFinder")
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }
}
```

如果没有明确指定名称，则默认名称是从字段名称或setter方法派生的。如果是字段，则使用字段名称;在setter方法的情况下，它采用bean属性名称。所以下面的例子将把名为“movieFinder”的bean注入到它的setter方法中：

```java
public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Resource
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }
}
```

```
注解的name属性值通过了解ApplicationContext的CommonAnnotationBeanPostProcessor被解析为一个bean名称。如果你明确的配置了Spring的SimpleJndiBeanFactory，名称可以通过JNDI来解析。但是，建议您依赖默认行为，并简单地使用Spring的JNDI查找功能来保留间接级别。
```

在@Resource用法的特殊情况下，没有显式指定name属性的值，类似于@Autowired，@Resource查找primary类型匹配而不是特定名称的bean，并且解析众所周知的可解析依赖关系：BeanFactory，ApplicationContext，ResourceLoader，ApplicationEventPublisher，和MessageSource**接口。**

因此，在以下示例中，customerPreferenceDao字段首先查找名为customerPreferenceDao的bean，然后返回到CustomerPreferenceDao类型的主类型匹配。 “context”字段是基于已知的可解析依赖类型ApplicationContext注入的。

```java
public class MovieRecommender {

    @Resource
    private CustomerPreferenceDao customerPreferenceDao;

    @Resource
    private ApplicationContext context;

    public MovieRecommender() {
    }

    // ...
}
```

### 1.9.8. @PostConstruct and @PreDestroy

CommonAnnotationBeanPostProcessor不仅可以识别@Resource注解，还可以识别JSR-250生命周期注解。在Spring 2.5中引入的对这些注解的支持提供了在[initialization callbacks](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-factory-lifecycle-initializingbean)和[destruction callbacks](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-factory-lifecycle-disposablebean)中描述的另一种替代方法。假设CommonAnnotationBeanPostProcessor是在Spring ApplicationContext中注册的，那么在生命周期的相同点上携带这些注解之一的方法被调用，就像调用相应的Spring生命周期接口方法或显式声明的回调方法一样。在下面的示例中，缓存将在初始化时预填充，并在销毁时清除。

有关组合各种生命周期机制的效果的详细信息，请参阅 [Combining lifecycle mechanisms](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-factory-lifecycle-combined-effects).

## 1.10. Classpath scanning and managed components

本章中的大多数示例都使用XML来指定在Spring容器中生成每个BeanDefinition的配置元数据。上一节（基于注解的容器配置）演示了如何通过源代码级注解提供大量的配置元数据。但是，即使在这些示例中，“基本”bean定义也是在XML文件中明确定义的，而注解仅驱动依赖注入。本节介绍通过扫描类路径隐式检测候选组件的选项。候选组件是与过滤条件相匹配的类，并具有在容器中注册的相应的bean定义。这消除了使用XML来执行bean注册的需要;相反，您可以使用注解（例如@Component），AspectJ类型表达式或您自己的自定义过滤条件来选择哪些类将具有注册到容器的bean定义。

```
从Spring 3.0开始，Spring JavaConfig项目提供的许多功能都是核心Spring框架的一部分。这使您可以使用Java定义bean，而不是使用传统的XML文件。查看@Configuration，@Bean，@Import和@DependsOn注解，了解如何使用这些新功能的示例。
```

### 1.10.1. @Component 以进一步的结构性注解

@ repository注解是一个实现存储库的角色或刻板印象(也称为数据访问对象或DAO)。在使用这个标记是异常中描述异常的自动翻译翻译。

@Repository注解是任何实现存储库（也称为数据访问对象或DAO）的角色或构造型的类的标记。这个标记的用法是在[Exception translation](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/data-access.html#orm-exception-translation)中描述的异常的自动转换。

Spring提供了更多的构造型注解：@Component，@Service和@Controller。 @Component是任何Spring管理组件的通用注解类型。对于更具体的用例，@Repository，@Service和@Controller是@Component的细化，例如，分别在持久层，服务层和表示层。因此，你可以使用@Component注解你的组件类，但如果用@Repository，@Service或者@Controller来注解它们，这样你的类就更适合用工具处理或者与切面关联。例如，这些结构型注解为切入点提供了理想的目标。 @Repository，@Service和@Controller也可能在Spring Framework的未来版本中携带额外的语义。因此，如果您在为您的服务层使用@Component或@Service之间进行选择，@Service显然是更好的选择。同样，如上所述，已经支持@Repository作为持久层自动异常转换的标记。

### 1.10.2. Meta-annotations

Spring提供的许多注解可以在您自己的代码中用作元注解。元注解只是一个可以应用于另一个注解的注解。例如，上面提到的@Service注解用了@Component元注解：

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component // Spring will see this and treat @Service in the same way as @Component
public @interface Service {

    // ....
}
```

元注解也可以结合使用来创建注解。例如，Spring MVC的@RestController注解由@Controller和@ResponseBody组成。

另外，组合的注解可以可选地重新声明来自元注解的属性以允许用户定制。当你只想暴露元注解属性的一个子集时，这可能特别有用。例如，Spring的@SessionScope注解将scope名称硬编码为session，但仍允许定制proxyMode。

```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Scope(WebApplicationContext.SCOPE_SESSION)
public @interface SessionScope {

    /**
     * Alias for {@link Scope#proxyMode}.
     * <p>Defaults to {@link ScopedProxyMode#TARGET_CLASS}.
     */
    @AliasFor(annotation = Scope.class)
    ScopedProxyMode proxyMode() default ScopedProxyMode.TARGET_CLASS;

}
```

然后可以使用@SessionScope，而无需声明proxyMode，如下所示：

```java
@Service
@SessionScope
public class SessionScopedService {
    // ...
}
```

或者为proxyMode重写一个值，如下所示：

```java
@Service
@SessionScope(proxyMode = ScopedProxyMode.INTERFACES)
public class SessionScopedUserService implements UserService {
    // ...
}
```

有关更多详细信息，请参阅Spring Annotation Programming Model](https://github.com/spring-projects/spring-framework/wiki/Spring-Annotation-Programming-Model)wiki页面。

### 1.10.3自动检测类并注册bean定义

Spring可以自动检测构造类，并使用ApplicationContext注册相应的BeanDefinitions。例如，以下两个类可以使用这种自动检测：

```java
@Service
public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Autowired
    public SimpleMovieLister(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }
}
```

```java
@Repository
public class JpaMovieFinder implements MovieFinder {
    // implementation elided for clarity
}
```

要自动检测这些类并注册相应的bean，需要将@ComponentScan添加到您的@Configuration类，其中basePackages属性是这两个类的公共父包。 （或者，您可以指定包含每个类的父包的以逗号/分号/空格分隔的列表。）

```java
@Configuration
@ComponentScan(basePackages = "org.example")
public class AppConfig  {
    ...
}
```

```
为简洁起见，上面也使用了注解的value属性，即@ComponentScan（“org.example”）
```

以下是使用XML的替代方法

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd">

    <context:component-scan base-package="org.example"/>

</beans>
```

```
使用<context：component-scan>隐式启用<context：annotation-config>的功能。当使用<context：component-scan>时，通常不需要包含<context：annotation-config>元素。
```

```
classpath包的扫描要求类路径中存在相应的目录条目。在使用Ant构建JAR时，请确保不要激活JAR任务的files-only开关。此外，在某些环境中，类路径目录基于安全策略可能不会暴露，例如， JDK 1.7.0_45和更高版本（需要在您的清单中设置“可信库”设置;请参阅http://stackoverflow.com/questions/19394570/java-jre-7u45-breaks-classloader-getresources）上的独立应用程序。

在JDK 9的模块路径（Jigsaw）中，Spring的类路径扫描一般按预期工作。但是，请确保您的组件类在module-info描述符中导出;如果你希望Spring调用类的非公共成员，请确保它们是“opened”的（即在module-info描述符中使用opens 声明而不是exports声明）。
```

此外，当您使用组件component-scan element时，AutowiredAnnotationBeanPostProcessor和CommonAnnotationBeanPostProcessor都是隐式包含的。这意味着这两个组件是自动检测和连接在一起的 - 所有这些都没有XML提供的任何bean配置元数据.

```
您可以通过将annotation-config属性的值包含为false来禁用AutowiredAnnotationBeanPostProcessor和CommonAnnotationBeanPostProcessor的注册。
```

### 1.10.4使用过滤器来自定义扫描

默认情况下，使用@Component，@Repository，@Service，@Controller注解或者本身使用@Component注解的自定义注解的类是被检测的候选组件。但是，只需应用自定义过滤器即可修改和扩展此行为。将过滤器添加为@ComponentScan注解的includeFilters或excludeFilters参数（或者作为component-scan元素的include-filter或exclude-filter子元素）。每个过滤器元素都需要类型和表达式属性。下表介绍了过滤选项。

| Filter Type          | Example Expression           | Description                              |
| -------------------- | ---------------------------- | ---------------------------------------- |
| annotation (default) | `org.example.SomeAnnotation` | An annotation to be present at the type level in target components. |
| assignable           | `org.example.SomeClass`      | A class (or interface) that the target components are assignable to (extend/implement). |
| aspectj              | `org.example..*Service+`     | An AspectJ type expression to be matched by the target components. |
| regex                | `org\.example\.Default.*`    | A regex expression to be matched by the target components class names. |
| custom               | `org.example.MyTypeFilter`   | A custom implementation of the `org.springframework.core.type .TypeFilter` interface. |

以下示例显示了忽略所有@Repository注解，而是使用"stub" repositories 的配置。

```java
@Configuration
@ComponentScan(basePackages = "org.example",
        includeFilters = @Filter(type = FilterType.REGEX, pattern = ".*Stub.*Repository"),
        excludeFilters = @Filter(Repository.class))
public class AppConfig {
    ...
}
```

等价的XML配置

```xml
<beans>
    <context:component-scan base-package="org.example">
        <context:include-filter type="regex"
                expression=".*Stub.*Repository"/>
        <context:exclude-filter type="annotation"
                expression="org.springframework.stereotype.Repository"/>
    </context:component-scan>
</beans>
```

```
您还可以通过在注解中设置useDefaultFilters = false或者将use-default-filters =“false”设置为<component-scan />元素的属性来禁用默认过滤器。这将实际上禁用自动检测用@Component，@Repository，@Service，@Controller或@Configuration注解的类。
```

### 1.10.5. Defining bean metadata within components

Spring组件也可以将bean定义元数据提供给容器。您可以使用与@Configuration注解中相同的@Bean注解来定义bean元数据。这是一个简单的例子：

```java
@Component
public class FactoryMethodComponent {

    @Bean
    @Qualifier("public")
    public TestBean publicInstance() {
        return new TestBean("publicInstance");
    }

    public void doWork() {
        // Component method implementation omitted
    }
}
```

这个类是一个Spring组件，其doWork（）方法中包含特定于应用程序的代码。但是，它也提供了一个bean定义，它具有引用方法publicInstance（）的工厂方法。 @Bean注解定义工厂方法和其他bean定义属性，例如通过@Qualifier注解定义限定符值。其他可以指定的方法级别注解是@Scope，@Lazy和自定义限定符注解。

```
@Lazy注解除了用于组件初始化的角色之外，还可以放置在标有@Autowired或@Inject的注入点上。在这种情况下，它导致注入一个 lazy-resolution代理。
```

如前所述，支持自动装配的字段和方法，并支持自动装配@Bean方法：

```java
@Component
public class FactoryMethodComponent {

    private static int i;

    @Bean
    @Qualifier("public")
    public TestBean publicInstance() {
        return new TestBean("publicInstance");
    }

    // use of a custom qualifier and autowiring of method parameters
    @Bean
    protected TestBean protectedInstance(
            @Qualifier("public") TestBean spouse,
            @Value("#{privateInstance.age}") String country) {
        TestBean tb = new TestBean("protectedInstance", 1);
        tb.setSpouse(spouse);
        tb.setCountry(country);
        return tb;
    }

    @Bean
    private TestBean privateInstance() {
        return new TestBean("privateInstance", i++);
    }

    @Bean
    @RequestScope
    public TestBean requestScopedInstance() {
        return new TestBean("requestScopedInstance", 3);
    }
}
```

该示例将另一个名为privateInstance的bean的Age属性的值自动装配到String方法参数country。 Spring表达式语言元素通过符号#{ <expression> }来定义属性的值，对于@Value注解，表达式解析器会预先配置为在解析表达式文本时查找bean名称。

从Spring Framework 4.3开始，您还可以声明一个类型为InjectionPoint的工厂方法参数（或其更具体的子类DependencyDescriptor），以便访问触发创建当前Bean的请求注入点。请注意，这只适用于bean实例实际创建的时候，注入已有实例的时候此注入点没有意义。因此，这个特性对prototype范围的bean来说是最有意义的。对于其他作用域，工厂方法将只能看到在给定范围内触发创建新的bean实例的注入点：例如，触发创建lazy singleton bean的依赖。在这种情况下使用提供的注入点元数据。

```java
@Component
public class FactoryMethodComponent {

    @Bean @Scope("prototype")
    public TestBean prototypeInstance(InjectionPoint injectionPoint) {
        return new TestBean("prototypeInstance for " + injectionPoint.getMember());
    }
}
```

常规Spring组件中的@Bean方法的处理方式与Spring @Configuration类中的对应方法不同。不同之处在于，@Component类不会使用CGLIB来拦截方法和字段的调用。 CGLIB代理是通过调用@Configuration类中的@Bean方法中的方法或字段来创建对依赖的bean元数据引用的手段;这种方法用普通的Java语法是不能调用的，而是通过容器来提供正常的生命周期管理和Spring bean的代理即使在通过对@Bean方法的编程调用来引用其他bean的情况下。相比之下，在普通的@Component类中调用@Bean方法中的方法或字段具有标准的Java语义，没有特别的CGLIB处理或其他约束应用。

```
您可以将@Bean方法声明为静态的，允许调用它们而不创建包含其配置的类作为实例。当定义 post-processor beans时，这是特别有意义的。 BeanFactoryPostProcessor或BeanPostProcessor类型，因为这样的bean将在容器生命周期的早期初始化，并且应该避免在那个时候触发配置的其他部分
请注意，对静态@Bean方法的调用永远不会被容器拦截，即使是@Configuration类中的静态@bean方法也不会被（见上）所拦截。这是由于技术限制：CGLIB子类只能覆盖非静态方法。因此，直接调用另一个@Bean方法将具有标准的Java语义，结果是独立的实例直接从工厂方法本身返回。
@Bean方法的Java语言可见性不会对Spring容器中最后的bean定义产生直接的影响。你可以自由地声明你的工厂方法，就像你在非@Configuration中看到的一样，也可以在任何地方用静态方法。然而，@Configuration类中的常规@Bean方法需要被覆盖，即不能将它们声明为private或final。
@Bean方法也将在给定组件或配置类的基类上发现，以及在Java 8中在接口中声明的默认方法将由给定组件或配置类实现。这使得在组合复杂的配置安排方面有很大的灵活性，从Spring 4.2开始，甚至可以通过Java 8默认方法进行多重继承。
最后，请注意，单个类可能会为同一个bean持有多个@Bean方法，这取决于运行时可用的依赖关系，可以使用多个工厂方法。这与在其他配置方案中选择“最贪婪的”构造函数或工厂方法的算法相同：在构建时将选择具有最大可满足依赖项数的变体，类似于容器在多个@Autowired构造函数之间进行选择。
```

### 1.10.6Naming autodetected components

当一个组件作为扫描过程的一部分被自动检测时，它的bean名称由该扫描器已知的BeanNameGenerator策略生成。默认情况下，包含name属性值的任何Spring构造型注解（@Component，@Repository，@Service和@Controller）都会将该名称提供给相应的bean定义。

如果这样的注解不包含任何name属性值或其他任何检测到的组件（例如自定义过滤器发现的那些），那么默认的bean名称生成器会返回小写形式的non-qualified类名称。例如，如果检测到以下两个组件，名称将是myMovieLister和movieFinderImpl：

```java
@Service("myMovieLister")
public class SimpleMovieLister {
    // ...
}
@Repository
public class MovieFinderImpl implements MovieFinder {
    // ...
}
```

```
如果你不想依赖默认的bean命名策略，你可以提供一个自定义的bean命名策略。首先，实现BeanNameGenerator接口，并确保包含默认的无参数构造函数。然后，在配置扫描器时提供 fully-qualified的类名称：
```

```java
@Configuration
@ComponentScan(basePackages = "org.example", nameGenerator = MyNameGenerator.class)
public class AppConfig {
    ...
}
```

```xml
<beans>
    <context:component-scan base-package="org.example"
        name-generator="org.example.MyNameGenerator" />
</beans>
```

作为一般规则，当其他组件可能正在对其进行明确引用时，请考虑在注解中指定该名称。另一方面，只要容器负责注入，自动生成的名称就足够了。

### 1.10.7. Providing a scope for autodetected components

与一般的Spring管理组件一样，自动检测组件的默认和最常见的范围是singleton。但是，有时您需要一个可以通过@Scope注解来指定的不同范围。只需在注解中提供scope的name属性值：

```java
@Scope("prototype")
@Repository
public class MovieFinderImpl implements MovieFinder {
    // ...
}
```

For details on web-specific scopes, see [Request, session, application, and WebSocket scopes](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-other).

要为scope解析提供自定义策略，而不是依赖annotation-based的方法，请实现ScopeMetadataResolver接口，并确保包含默认的无参数构造函数。然后，在配置扫描器时提供fully-qualified的类名称：

```java
@Configuration
@ComponentScan(basePackages = "org.example", scopeResolver = MyScopeResolver.class)
public class AppConfig {
    ...
}
```

```xml
<beans>
    <context:component-scan base-package="org.example"
            scope-resolver="org.example.MyScopeResolver" />
</beans>
```

当使用某些non-singleton scope时，可能需要为作用域对象生成代理。 [Scoped beans as dependencies](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-factory-scopes-other-injection)中描述了原因。为此， component-scan 元素上有一个scoped-proxy属性。三个可能的值是：no，interfaces和targetClass。例如，以下配置将导致标准的JDK动态代理。

```java
@Configuration
@ComponentScan(basePackages = "org.example", scopedProxy = ScopedProxyMode.INTERFACES)
public class AppConfig {
    ...
}
```

```xml
<beans>
    <context:component-scan base-package="org.example"
        scoped-proxy="interfaces" />
</beans>
```

### 1.10.8. Providing qualifier metadata with annotations

@Qualifier注解在 [Fine-tuning annotation-based autowiring with qualifiers](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-autowired-annotation-qualifiers)进行了讨论。本节中的示例演示如何使用@Qualifier注解和自定义限定符注解在解析自动导入依赖候选时提供细粒度的控制。因为这些示例基于XML bean定义，所以使用XML中bean元素的`qualifier` 或meta子元素在候选bean定义上提供限定符元数据。当依靠类路径扫描来自动检测compent时，可以在候选类上使用type-level 的注解提供限定符元数据。以下三个示例演示了这种技术：

```java
@Component
@Qualifier("Action")
public class ActionMovieCatalog implements MovieCatalog {
    // ...
}
```

```java
@Component
@Genre("Action")
public class ActionMovieCatalog implements MovieCatalog {
    // ...
}
```

```java
@Component
@Offline
public class CachingMovieCatalog implements MovieCatalog {
    // ...
}
```

```
与大多数基于注解的替代方案一样，请记住，注释元数据绑定到类定义本身，而使用XML允许多个相同类型的bean在其限定符元数据中提供变体，因为使用xml配置时，元数据是与每个实例绑定在一起的，而不是与每个类绑定在一起。
```

### 1.10.9. Generating an index of candidate components

虽然类路径扫描速度非常快，但通过在编译时创建静态候选列表，可以提高大型应用程序的启动性能。在这种模式下，应用程序的所有模块必须使用这种机制，当ApplicationContext检测到这样的索引时，它将自动使用它而不是扫描类路径。

要生成索引，只需向每个包含目标组件（在组件扫描目录下）的模块添加一个附加依赖项即可：

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context-indexer</artifactId>
        <version>5.0.2.RELEASE</version>
        <optional>true</optional>
    </dependency>
</dependencies>
```

该过程将生成将META-INF / spring.components文件,这个文件会被包含在jar中。

```
在IDE中使用此模式时，必须将spring-context-indexer注册为注释处理器，以确保在更新候选组件时索引是最新的。
```

```
当在类路径上找到META-INF / spring.components时，索引会自动启用。如果索引对于某些库（或用例）是部分可用的，但不能为整个应用程序构建，则可以通过设置spring.index.ignore为true来回退到常规的类路径配置（即根本没有索引），spring.index.ignore可以作为系统属性，也可以在classpath根目录下的spring.properties文件中设置。
```

## 1.11. Using JSR 330 Standard Annotations

从Spring 3.0开始，Spring提供对JSR-330标准注释（依赖注入）的支持。这些注释以与Spring注释相同的方式被扫描。你只需要在你的类路径中有相关的jars。

 如果您正在使用Maven，则可以在标准Maven存储库（http://repo1.maven.org/maven2/javax/inject/javax.inject/1/）中使用javax.inject工件。您可以将以下依赖项添加到您的文件pom.xml中：

```xml
<dependency>
    <groupId>javax.inject</groupId>
    <artifactId>javax.inject</artifactId>
    <version>1</version>
</dependency>
```

### 1.11.1. Dependency Injection with @Inject and @Named

可以使用@ javax.inject.Inject来代替@Autowired，如下所示：

```java
import javax.inject.Inject;

public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Inject
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    public void listMovies() {
        this.movieFinder.findMovies(...);
        ...
    }
}
```

与@Autowired一样，可以在 field level, method level and constructor-argument level使用@Inject。此外，您可以将注入点声明为Provider，允许按需访问较短scope的Bean，也允许通过一个Provider.get（）方法对其他Bean的lazy访问。以上示例的一个变体：

```java
import javax.inject.Inject;
import javax.inject.Provider;

public class SimpleMovieLister {

    private Provider<MovieFinder> movieFinder;

    @Inject
    public void setMovieFinder(Provider<MovieFinder> movieFinder) {
        this.movieFinder = movieFinder;
    }

    public void listMovies() {
        this.movieFinder.get().findMovies(...);
        ...
    }
}
```

如果您想为应该注入的依赖项使用限定名称，则应该按如下方式使用@Named注释：

```java
import javax.inject.Inject;
import javax.inject.Named;

public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Inject
    public void setMovieFinder(@Named("main") MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // ...
}
```

像@Autowired一样，@Inject也可以和java.util.Optional或@Nullable一起使用。这在这里更适用于，因为@Inject没有`required` 的属性。

```java
public class SimpleMovieLister {

    @Inject
    public void setMovieFinder(Optional<MovieFinder> movieFinder) {
        ...
    }
}
```

```java
public class SimpleMovieLister {

    @Inject
    public void setMovieFinder(@Nullable MovieFinder movieFinder) {
        ...
    }
}
```

### 1.11.2. @Named and @ManagedBean: standard equivalents to the @Component annotation



可以使用@ javax.inject.Named或javax.annotation.ManagedBean代替@Component，如下所示：

```java
import javax.inject.Inject;
import javax.inject.Named;

@Named("movieListener")  // @ManagedBean("movieListener") could be used as well
public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Inject
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // ...
}
```

使用@Component而不指定组件的名称是很常见的。 @Named可以以类似的方式使用：

```java
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Inject
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // ...
}
```

使用@Named或@ManagedBean时，可以像使用Spring注释一样使用组件扫描：

```java
@Configuration
@ComponentScan(basePackages = "org.example")
public class AppConfig  {
    ...
}
```

与@Component相比，JSR-330 @Named和JSR-250 ManagedBean注释是能用来与其他注解组合。请使用Spring的stereotype 类型的注解来构建自定义组件注释。

### 1.11.3. Limitations of JSR-330 standard annotations

使用标准注释时，知道一些重要的功能不可用是很重要的，如下表所示：

| Spring              | javax.inject.*        | javax.inject restrictions / comments     |
| ------------------- | --------------------- | ---------------------------------------- |
| @Autowired          | @Inject               | `@Inject` has no 'required' attribute; can be used with Java 8’s `Optional`instead. |
| @Component          | @Named / @ManagedBean | JSR-330 does not provide a composable model, just a way to identify named components. |
| @Scope("singleton") | @Singleton            | The JSR-330 default scope is like Spring’s `prototype`. However, in order to keep it consistent with Spring’s general defaults, a JSR-330 bean declared in the Spring container is a `singleton` by default. In order to use a scope other than `singleton`, you should use Spring’s `@Scope`annotation. `javax.inject` also provides a [@Scope](http://download.oracle.com/javaee/6/api/javax/inject/Scope.html) annotation. Nevertheless, this one is only intended to be used for creating your own annotations. |
| @Qualifier          | @Qualifier / @Named   | `javax.inject.Qualifier` is just a meta-annotation for building custom qualifiers. Concrete String qualifiers (like Spring’s `@Qualifier` with a value) can be associated through `javax.inject.Named`. |
| @Value              | -                     | no equivalent                            |
| @Required           | -                     | no equivalent                            |
| @Lazy               | -                     | no equivalent                            |
| ObjectFactory       | Provider              | `javax.inject.Provider` is a direct alternative to Spring’s `ObjectFactory`, just with a shorter `get()` method name. It can also be used in combination with Spring’s `@Autowired`or with non-annotated constructors and setter methods. |

### 1.12 基于Java的容器配置

Spring新的Java-configuration支持的中心构件是@Configuration注释的类和@ Bean注释的方法。

@Bean注释用于指示一个方法实例化，配置和初始化一个新的对象，以便由Spring IoC容器管理。对于熟悉Spring的<beans /> XML配置的人来说，@Bean注释和<bean />元素具有相同的作用。你可以在任何Spring @Component类中使用@Bean注释的方法，但是它们通常和@Configuration bean一起使用。

用@Configuration注解类的主要目的是表明它将作为bean定义的来源。此外，@Configuration类允许通过简单地调用同一个类中的其他@Bean方法来定义bean间的依赖关系。最简单的可能的@Configuration类将如下所示：

```java
@Configuration
public class AppConfig {

    @Bean
    public MyService myService() {
        return new MyServiceImpl();
    }
}
```

上面的AppConfig类将等同于下面的Spring <beans /> XML：

```xml
<beans>
    <bean id="myService" class="com.acme.services.MyServiceImpl"/>
</beans>
```

## 1.12基于Java的容器配置

### 1.12.1基本概念：@Bean和@Configuration

Spring新的Java配置支持的核心构件是`@Configuration`-annotated classes and `@Bean`-annotated methods.



```
                      Full @Configuration vs 'lite' @Bean mode?
当@Bean方法在没有使用@Configuration注释的类中声明时，它们被称为在“精简”模式下处理。在@Component中甚至在普通的旧类中声明的Bean方法将被认为是“精简的”，在这种情况下@bean方法与 包含它的class的主要目的不同，它仅仅是一种奖励。例如，service组件可以通过在每个适用的service组件中的一个额外的@Bean方法向容器暴露管理视图。在这种情况下，@Bean方法是一个简单的通用工厂方法机制。

与full @Configuration不同，lite @Bean方法不能声明bean间依赖关系。相反，他们对包含它们的组件的内部状态和可选的参数进行操作。因此这样的@Bean方法不应该调用其他的@Bean方法;每个lite @Bean方法实际上只是一个产生特定bean引用的工厂方法，没有任何特殊的运行时语义。这种方式的优点是，在运行时不需要应用CGLIB子类，所以在类设计方面没有限制（即，包含@bean方法类可能是final的）。

在一般情况下，@Bean方法将在@Configuration类中声明，以确保始终使用“full”模式，因此跨方法引用将重定向到容器的生命周期管理。这将防止相同的@Bean方法通过常规的Java调用被意外地调用，这有助于减少以“lite”模式操作时造成的难以追踪的微妙错误
```

@Bean和@Configuration注解将在下面的章节中深入讨论。首先，我们将介绍使用基于Java的配置创建Spring容器的各种方法。

### 1.12.2使用AnnotationConfigApplicationContext实例化Spring容器

下面的章节介绍了Spring的AnnotationConfigApplicationContext，Spring 3.0中的新特性。这个多功能的ApplicationContext实现不仅可以接受@Configuration类作为输入，还可以接受普通的@Component类和使用JSR-330元数据注释的类。

当提供@Configuration类作为输入时，@Configuration类本身被注册为一个bean定义，并且类中所有声明的@Bean方法也被注册为bean定义。

当提供@Component和JSR-330类时，它们被注册为bean定义，并且假定在必要时在这些类中使用了DI元数据（例如@Autowired或@Inject）。

#### Simple construction

与实例化ClassPathXmlApplicationContext时使用Spring XML文件作为输入的方式大致相同，在实例化AnnotationConfigApplicationContext时，@Configuration类可以用作输入。这允许完全无XML的使用Spring容器：

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
    MyService myService = ctx.getBean(MyService.class);
    myService.doStuff();
}
```

如上所述，AnnotationConfigApplicationContext不仅限于使用@Configuration类。任何@Component或JSR-330注释类都可以作为输入提供给它的构造函数。例如：

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(MyServiceImpl.class, Dependency1.class, Dependency2.class);
    MyService myService = ctx.getBean(MyService.class);
    myService.doStuff();
}
```

上面假设MyServiceImpl，Dependency1和Dependency2使用Spring依赖注入注释，例如@Autowired。

#### 以编程方式使用register（Class <？> ...）方法构建容器

AnnotationConfigApplicationContext可以使用无参数构造函数实例化，然后使用register（）方法进行配置。以编程方式构建AnnotationConfigApplicationContext时，此方法特别有用。

```java
public static void main(String[] args) {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    ctx.register(AppConfig.class, OtherConfig.class);
    ctx.register(AdditionalConfig.class);
    ctx.refresh();
    MyService myService = ctx.getBean(MyService.class);
    myService.doStuff();
}
```

#### 使用scan(String…)方法允许组件扫描

要启用组件扫描，只需注释您的@Configuration类，如下所示：

```java
@Configuration
@ComponentScan(basePackages = "com.acme")
public class AppConfig  {
    ...
}
```

```xml
有经验的Spring用户将熟悉与以上java配置相同的xml配置：
<beans>
    <context:component-scan base-package="com.acme"/>
</beans>
```

在上面的示例中，将扫描com.acme包，查找任何@ Component注释的类，这些类将在容器中注册为Spring bean定义。 AnnotationConfigApplicationContext公开scan（String ...）方法以允许相同的组件扫描功能：

```java
public static void main(String[] args) {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    ctx.scan("com.acme");
    ctx.refresh();
    MyService myService = ctx.getBean(MyService.class);
}
```

```
请记住@Configuration类是使用@Component进行元注释的，所以它们是组件扫描的候选对象！在上面的例子中，假设AppConfig是在com.acme包（或下面的任何包）中声明的，它将在调用scan（）期间被扫描，并且在refresh（）后，其所有的@Bean方法将被处理并在容器中注册为bean定义
```

#### 使用AnnotationConfigWebApplicationContext支持Web应用程序

AnnotationConfigWebApplicationContext是AnnotationConfigApplicationContext的WebApplicationContext变体。当配置Spring ContextLoaderListener servlet监听器，Spring MVC DispatcherServlet等时，可以使用这个实现。接下来是配置典型Spring MVC Web应用程序的web.xml代码片段。请注意contextClass context-param和init-param的使用：

```xml
<web-app>
    <!-- Configure ContextLoaderListener to use AnnotationConfigWebApplicationContext
        instead of the default XmlWebApplicationContext -->
    <context-param>
        <param-name>contextClass</param-name>
        <param-value>
            org.springframework.web.context.support.AnnotationConfigWebApplicationContext
        </param-value>
    </context-param>

    <!-- Configuration locations must consist of one or more comma- or space-delimited
        fully-qualified @Configuration classes. Fully-qualified packages may also be
        specified for component-scanning -->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>com.acme.AppConfig</param-value>
    </context-param>

    <!-- Bootstrap the root application context as usual using ContextLoaderListener -->
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <!-- Declare a Spring MVC DispatcherServlet as usual -->
    <servlet>
        <servlet-name>dispatcher</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!-- Configure DispatcherServlet to use AnnotationConfigWebApplicationContext
            instead of the default XmlWebApplicationContext -->
        <init-param>
            <param-name>contextClass</param-name>
            <param-value>
                org.springframework.web.context.support.AnnotationConfigWebApplicationContext
            </param-value>
        </init-param>
        <!-- Again, config locations must consist of one or more comma- or space-delimited
            and fully-qualified @Configuration classes -->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>com.acme.web.MvcConfig</param-value>
        </init-param>
    </servlet>

    <!-- map all requests for /app/* to the dispatcher servlet -->
    <servlet-mapping>
        <servlet-name>dispatcher</servlet-name>
        <url-pattern>/app/*</url-pattern>
    </servlet-mapping>
</web-app>
```
### 1.12.3使用@Bean注释

@Bean是一个方法级别的注释，与XML <bean />元素类似。该注解支持<bean />提供的一些属性，比如： [init-method](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-factory-lifecycle-initializingbean), [destroy-method](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-factory-lifecycle-disposablebean), [autowiring](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-factory-autowire)和name。

您可以在@Context-Annotated或@Component-Annotation类中使用@Bean注释。

#### 声明一个bean

要声明一个bean，只需使用@Bean注释来注释一个方法即可。您可以使用此方法在指定为方法返回值的类型的ApplicationContext中注册一个bean定义。默认情况下，这个bean的名字将和方法名一样。以下是一个@Bean方法声明的简单示例：

```java
@Configuration
public class AppConfig {

    @Bean
    public TransferServiceImpl transferService() {
        return new TransferServiceImpl();
    }
}
```

上述配置完全等同于以下Spring XML：

```xml
<beans>
    <bean id="transferService" class="com.acme.TransferServiceImpl"/>
</beans>
```

这两个声明都在ApplicationContext中创建了一个名为transferService的bean，绑定到TransferServiceImpl类型的对象实例：

```
transferService -> com.acme.TransferServiceImpl
```

你也可以用接口（或基类）返回类型声明你的@Bean方法：

```java
@Configuration
public class AppConfig {

    @Bean
    public TransferService transferService() {
        return new TransferServiceImpl();
    }
}
```

但是，这会将提前类型预测的可见性限制为指定的接口类型（TransferService），只有相应的单例bean实例化后容器才能知道具体类型（TransferServiceImpl）。Non-lazy  singleton bean根据它们的声明顺序被实例化，所以你可能会看到不同的类型匹配结果，这取决于当另一个组件试图通过一个non-declared 类型来匹配的情况（比如@Autowired TransferServiceImpl，它只有当bean已经被实例化后才会解析“transferService” ）。

```
如果您始终使用声明的service接口来引用您的类型，那么您的@Bean返回类型可以安全地适用于该设计决策。但是，对于实现多个接口的组件或可能由其实现类型引用的组件，声明最具体的返回类型是（至少与引用您的bean的注入点要求相同）是比较安全的。
```

#### Bean的依赖关系

@Bean注释的方法可以有任意数量的参数来描述构建bean所需的依赖关系。例如，如果我们的TransferService需要一个AccountRepository，我们可以通过一个方法参数实现这个依赖：

```java
@Configuration
public class AppConfig {

    @Bean
    public TransferService transferService(AccountRepository accountRepository) {
        return new TransferServiceImpl(accountRepository);
    }
}
```

这种解析机制与constructor-based的依赖注入非常相似，请参阅 [the relevant section](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-constructor-injection) 了解更多细节。

#### 接收生命周期回调

任何使用@Bean注释定义的类都支持常规生命周期回调，并且可以使用JSR-250中的@PostConstruct和@PreDestroy注释，请参阅 [JSR-250 annotations](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-postconstruct-and-predestroy-annotations)以获取更多详细信息。

常规的Spring [lifecycle](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-factory-nature) 回调也被完全支持。如果一个bean实现了InitializingBean，DisposableBean或者Lifecycle，它们各自相应的方法被容器调用。

诸如 [BeanFactoryAware](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-beanfactory), [BeanNameAware](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-factory-aware), [MessageSourceAware](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#context-functionality-messagesource),[ApplicationContextAware](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-factory-aware)等aware标准接口集合也完全受支持。

@Bean注解支持指定任意的初始化和销毁回调方法，就像Spring XML对bean元素的init-method和destroy-method属性一样：

```java
public class Foo {

    public void init() {
        // initialization logic
    }
}

public class Bar {

    public void cleanup() {
        // destruction logic
    }
}

@Configuration
public class AppConfig {

    @Bean(initMethod = "init")
    public Foo foo() {
        return new Foo();
    }

    @Bean(destroyMethod = "cleanup")
    public Bar bar() {
        return new Bar();
    }
}
```

```java
默认情况下，使用具有public close or shutdown方法的Java配置定义的bean将自动获得一个销毁回调。如果你有一个public close或shutdown方法，并且你不希望在容器关闭的时候调用它，只需要在你的bean定义中添加@Bean（destroyMethod =“”）来禁用默认模式。
您可能希望为通过JNDI获取的资源默认不执行销毁操作，因为其生命周期是在应用程序之外进行管理的，不应该由spring容器销毁。尤其是，确保始终对DataSource执行此操作，因为在Java EE应用程序服务器上，这是已知的问题。
@Bean(destroyMethod="")
public DataSource dataSource() throws NamingException {
    return (DataSource) jndiTemplate.lookup("MyDS");
}
此外，使用@Bean方法，通常会选择使用编程式JNDI查找：使用Spring的JndiTemplate / JndiLocatorDelegate helper或直接使用JNDI InitialContext，而不使用JndiObjectFactoryBean变体，因为这将强制您将返回类型声明为FactoryBean类型，而不是实际的目标类型，这使得在其他的@Bean方法中用来引用所提供的资源的交叉引用调用更加困难。
```

当然，就上面的Foo而言，在构造过程中直接调用init（）方法同样有效：

```java
@Configuration
public class AppConfig {

    @Bean
    public Foo foo() {
        Foo foo = new Foo();
        foo.init();
        return foo;
    }

    // ...
}
```

```
当您直接使用Java进行工作时，您可以对对象进行任何操作，而不总是需要依靠容器生命周期！
```

#### 指定bean作用域(scope)

##### 使用@Scope注释

您可以指定使用@Bean注释定义的bean应该具有特定的作用域。您可以使用[Bean Scopes](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-factory-scopes) 部分中指定的任何标准作用域。

他的默认范围是singleton，但你可以用@Scope注解来覆盖它：

```java
@Configuration
public class MyConfiguration {

    @Bean
    @Scope("prototype")
    public Encryptor encryptor() {
        // ...
    }
}
```

##### @Scope和scoped-proxy

Spring提供了一个通过scope代理来处理作用域依赖的简便方法。使用XML配置时创建这种代理的最简单方法是<aop：scoped-proxy />元素。在Java中配置bean用@Scope的proxyMode属性提供了与xml配置等效的支持。默认是没有代理（ScopedProxyMode.NO），但您可以指定ScopedProxyMode.TARGET_CLASS或ScopedProxyMode.INTERFACES。

如果您使用Java将scope代理示例从XML参考文档（请参阅前面的链接）移植到我们的@Bean，那么它看起来如下所示

```java
// an HTTP Session-scoped bean exposed as a proxy
@Bean
@SessionScope
public UserPreferences userPreferences() {
    return new UserPreferences();
}

@Bean
public Service userService() {
    UserService service = new SimpleUserService();
    // a reference to the proxied userPreferences bean
    service.setUserPreferences(userPreferences());
    return service;
}
```

#### 自定义bean名称

默认情况下，配置类使用@Bean方法的名称作为配置的bean的名称。然而，这个功能可以被覆盖，使用name属性。

```java
@Configuration
public class AppConfig {

    @Bean(name = "myFoo")
    public Foo foo() {
        return new Foo();
    }
}
```

#### Bean aliasing

正如在 [Naming beans](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-beanname)中所讨论的，有时希望给单个bean多个名称，否则称为bean别名。 @Bean注释的name属性接受一个String数组。

```java
@Configuration
public class AppConfig {

    @Bean(name = { "dataSource", "subsystemA-dataSource", "subsystemB-dataSource" })
    public DataSource dataSource() {
        // instantiate, configure and return DataSource bean...
    }
}
```

#### Bean description

有时，提供一个更详细的bean的文本描述是有帮助的。当bean暴露（可能通过JMX）用于监视目的时，这可能特别有用。

要向@Bean添加描述，可以使用[`@Description`](https://docs.spring.io/spring-framework/docs/5.0.2.RELEASE/javadoc-api/org/springframework/context/annotation/Description.html)注释：

```java
@Configuration
public class AppConfig {

    @Bean
    @Description("Provides a basic example of a bean")
    public Foo foo() {
        return new Foo();
    }
}
```

### 1.12.4使用@Configuration注释

   @Configuration是一个类级别的注释，表明一个对象是一个bean定义的来源。 @Configuration类通过public @Bean注释的方法声明bean。调用@Configuration类上的@Bean方法也可以用来定义bean间的依赖关系。请参阅基本概念： [Basic concepts: @Bean and @Configuration](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-java-basic-concepts)以进行一般性介绍。

#### 注入bean间依赖关系

当@Beans彼此依赖时，表达这种依赖就像一个bean方法调用另一个一样简单：

```java
@Configuration
public class AppConfig {

    @Bean
    public Foo foo() {
        return new Foo(bar());
    }

    @Bean
    public Bar bar() {
        return new Bar();
    }
}
```

在上面的例子中，foo bean通过 constructor injection接收到对bar的引用。

```
这种声明bean间依赖关系的方式只有在@Bean方法声明在同一个@Configuration类中时才起作用。你不能用简单的@Component类声明bean间的依赖关系。
```

#### Lookup method injection

如前所述，[lookup method injection](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-factory-method-injection)是一种很少使用的高级功能。在singleton-scoped 的bean对 prototype-scoped的bean具有依赖关系的情况下，这是非常有用的。

```java
public abstract class CommandManager {
    public Object process(Object commandState) {
        // grab a new instance of the appropriate Command interface
        Command command = createCommand();
        // set the state on the (hopefully brand new) Command instance
        command.setState(commandState);
        return command.execute();
    }

    // okay... but where is the implementation of this method?
    protected abstract Command createCommand();
}
```

使用Java配置支持，您可以创建CommandManager的子类，其中抽象createCommand（）方法被重写，以便查找新的（原型）命令对象：

```java
@Bean
@Scope("prototype")
public AsyncCommand asyncCommand() {
    AsyncCommand command = new AsyncCommand();
    // inject dependencies here as required
    return command;
}

@Bean
public CommandManager commandManager() {
    // return new anonymous implementation of CommandManager with command() overridden
    // to return a new prototype Command object
    return new CommandManager() {
        protected Command createCommand() {
            return asyncCommand();
        }
    }
}
```

#### 有关基于Java的配置如何在内部工作的更多信息

以下示例显示了一个被调用了两次的@Bean注释方法：

```java
@Configuration
public class AppConfig {

    @Bean
    public ClientService clientService1() {
        ClientServiceImpl clientService = new ClientServiceImpl();
        clientService.setClientDao(clientDao());
        return clientService;
    }

    @Bean
    public ClientService clientService2() {
        ClientServiceImpl clientService = new ClientServiceImpl();
        clientService.setClientDao(clientDao());
        return clientService;
    }

    @Bean
    public ClientDao clientDao() {
        return new ClientDaoImpl();
    }
}
```

clientDao（）在clientService1（）中被调用一次，在clientService2（）中被调用一次。由于此方法创建ClientDaoImpl的一个新实例并返回它，通常会认为会有2个实例产生（每个service调用都产生一个）。按理来说这肯定会有问题：因为在Spring中，实例化的bean默认情况下具有单例作用域，但现在好像产生了两个ClientDao实例。这就是魔法的来源：所有的@Configuration类在容器启动时使用CGLIB产生了一个子类代理。在子类中，child方法在调用父方法并创建一个新实例之前首先检查容器是否有缓存的（范围）bean。请注意，从Spring 3.2开始，不再需要将CGLIB添加到类路径中，因为CGLIB类已经在org.springframework.cglib下重新打包，并直接包含在Spring-Core JAR中。

```
以上行为可能会根据你的bean的scope而有所不同。我们在这里讨论singletons。
```

```
由于CGLIB是在容器启动时动态添加功能，因此这里有一些限制，特别是 configuration类不能是final的,否则无法产生代理。但是，从4.3开始，任何构造函数都可以在配置类上使用，包括对默认注入使用@Autowired或单个非默认构造函数声明。
如果您希望避免任何CGLIB限制，请考虑在非配置类上声明@Bean方法，例如使用简单的@Component类。 这种情况下@Bean方法之间的跨方法调用将不会被拦截，因此您必须完全依赖于构造函数或方法级别的依赖注入。
```

### 1.12.5。编写基于Java的配置

#### 使用@Import注释

就像在Spring XML文件中使用<import />元素来帮助模块化配置一样，@Import注解允许从另一个配置类加载@Bean定义：

```java
@Configuration
public class ConfigA {

    @Bean
    public A a() {
        return new A();
    }
}

@Configuration
@Import(ConfigA.class)
public class ConfigB {

    @Bean
    public B b() {
        return new B();
    }
}
```

现在，在实例化上下文时，不需要同时指定ConfigA.class和ConfigB.class，只需要显式地提供ConfigB：

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(ConfigB.class);

    // now both beans A and B will be available...
    A a = ctx.getBean(A.class);
    B b = ctx.getBean(B.class);
}
```

这种方法简化了容器的实例化，因为只有一个类需要处理，而不是要求开发人员在构建过程中记住大量的@Configuration类。

```
从Spring Framework 4.2开始，@Import还支持对常规组件类的引用，类似于AnnotationConfigApplicationContext.register方法。如果您想避免组件扫描，使用一些configuration类作为明确定义所有组件的入口点，@import的这个功能会特别有用。
```

##### 使用@import注解引入的配置类中的Bean定义如何被注入

上面的例子可以工作，但是太简单。在大多数实际情况中，bean将在配置类中相互依赖。当使用XML时，这本身不是问题，因为不涉及编译器，只需声明ref =“someBean”并相信Spring将在容器初始化期间解决这个问题。当然，在使用@Configuration类时，Java编译器会在配置模型上设置约束，因为对其他bean的引用必须是有效的Java语法。

幸运的是，解决这个问题很简单。正如 [we already discussed](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-java-dependencies)，@Bean方法可以有任意数量的描述bean依赖关系的参数。让我们考虑一个更现实的场景，其中有几个@Configuration类，每个类都依赖于在其他类中声明的bean：

```java
@Configuration
public class ServiceConfig {

    @Bean
    public TransferService transferService(AccountRepository accountRepository) {
        return new TransferServiceImpl(accountRepository);
    }
}

@Configuration
public class RepositoryConfig {

    @Bean
    public AccountRepository accountRepository(DataSource dataSource) {
        return new JdbcAccountRepository(dataSource);
    }
}

@Configuration
@Import({ServiceConfig.class, RepositoryConfig.class})
public class SystemTestConfig {

    @Bean
    public DataSource dataSource() {
        // return new DataSource
    }
}

public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SystemTestConfig.class);
    // everything wires up across configuration classes...
    TransferService transferService = ctx.getBean(TransferService.class);
    transferService.transfer(100.00, "A123", "C456");
}
```

还有另一种方法可以达到同样的结果。请记住，@Configuration类最终只是容器中的另一个bean：这意味着他们可以像其他任何bean一样利用@Autowired和@Value注入等等！

```
确保以这种方式注入的依赖关系只是最简单的一种。 因为@Configuration类在上下文的初始化过程中相当早的被处理，强制以这种方式注入依赖可能会导致意外的早期初始化。只要有可能，就像上面的例子那样，采取基于参数的注入。
此外，通过@Bean定义BeanPostProcessor和BeanFactoryPostProcessor时要特别小心。这种@bean方法应该通常被声明为静态的@Bean方法，而不触发包含它的配置类的实例化。否则，@Autowired和@Value将不能在配置类本身上工作，因为它太早创建为一个bean实例。
```

```java
@Configuration
public class ServiceConfig {

    @Autowired
    private AccountRepository accountRepository;

    @Bean
    public TransferService transferService() {
        return new TransferServiceImpl(accountRepository);
    }
}

@Configuration
public class RepositoryConfig {

    private final DataSource dataSource;

    @Autowired
    public RepositoryConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public AccountRepository accountRepository() {
        return new JdbcAccountRepository(dataSource);
    }
}

@Configuration
@Import({ServiceConfig.class, RepositoryConfig.class})
public class SystemTestConfig {

    @Bean
    public DataSource dataSource() {
        // return new DataSource
    }
}

public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SystemTestConfig.class);
    // everything wires up across configuration classes...
    TransferService transferService = ctx.getBean(TransferService.class);
    transferService.transfer(100.00, "A123", "C456");
}
```

```
@Configuration类中的构造函数注入仅在Spring Framework 4.3中受支持。另请注意，如果目标bean只定义了一个构造函数，则不需要指定@Autowired;在上面的例子中，RepositoryConfig的构造函数不需要@Autowired。
```
##### 为了便于看清楚依赖关系，完全限定导入的beans

在上面的场景中，使用@Autowired运行良好，并提供了所希望的配置模块化，但是确定自动注入的bean定义的确切位置仍然有些模糊。例如，作为一名开发人员查看ServiceConfig，您如何确切知道@Autowired AccountRepository bean的声明位置？这在代码中并不明确，这可能只是一种情况。请记住，[Spring Tool Suite](https://spring.io/tools/sts) 提供的工具可以呈现图表，显示所有东西 是如何连接- 这可能就是您所需要的。另外，您的Java IDE可以轻松找到AccountRepository类型的所有声明和用法，并且会快速向您显示返回该类型的@Bean方法的位置。

如果这种歧义是不可接受的，并且您希望从IDE中的一个@Configuration类直接导航到另一个@Configuration类，请考虑自动装配配置类本身：

```java
@Configuration
public class ServiceConfig {

    @Autowired
    private RepositoryConfig repositoryConfig;

    @Bean
    public TransferService transferService() {
        // navigate 'through' the config class to the @Bean method!
        return new TransferServiceImpl(repositoryConfig.accountRepository());
    }
}
```

在上面的情况中，定义AccountRepository的地方是完全明确的。但是，ServiceConfig现在与RepositoryConfig紧密耦合;这是权衡。通过使用基于接口的或基于抽象类的@Configuration类，可以稍微缓解这种紧密耦合。考虑以下：

```java
@Configuration
public class ServiceConfig {

    @Autowired
    private RepositoryConfig repositoryConfig;

    @Bean
    public TransferService transferService() {
        return new TransferServiceImpl(repositoryConfig.accountRepository());
    }
}

@Configuration
public interface RepositoryConfig {

    @Bean
    AccountRepository accountRepository();
}

@Configuration
public class DefaultRepositoryConfig implements RepositoryConfig {

    @Bean
    public AccountRepository accountRepository() {
        return new JdbcAccountRepository(...);
    }
}

@Configuration
@Import({ServiceConfig.class, DefaultRepositoryConfig.class})  // import the concrete config!
public class SystemTestConfig {

    @Bean
    public DataSource dataSource() {
        // return DataSource
    }

}

public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SystemTestConfig.class);
    TransferService transferService = ctx.getBean(TransferService.class);
    transferService.transfer(100.00, "A123", "C456");
}
```

现在，ServiceConfig与具体的DefaultRepositoryConfig耦合程度降低了，内置的IDE工具仍然有用：开发人员可以轻松获得RepositoryConfig实现的类型层次结构。通过这种方式，导航@Configuration类及其依赖关系与导航基于接口的代码没有什么不同。

```
如果你想影响某些bean的启动创建顺序，可以考虑把它们中的一些声明为@Lazy（用于在第一次访问时创建，而不是在启动时创建）或者使用@DependsOn标记，表示依赖于其他bean（确保特定的其他bean将在当前的bean之前创建）
```

#### 有条件地包含@Configuration类或@Bean方法

根据一些任意的系统状态，有条件地启用或禁用一个完整的@Configuration类，甚至单个的@Bean方法通常是有用的。一个常见的例子是，只有在Spring环境中启用了特定的配置时，才使用@Profile注释来激活bean（有关详细信息，请参阅 [Bean definition profiles](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-definition-profiles)）。

@Profile注释实际上是使用一个更为灵活的注释@Conditional来实现的。 @Conditional注释表示特定的org.springframework.context.annotation.Condition实现，在@Bean注册之前应该参考对应的Condition实现类。

Condition接口的实现只是提供一个返回true或false的matches（...）方法。例如，下面是用于@Profile的实际Condition实现：

```java
@Override
public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    if (context.getEnvironment() != null) {
        // Read the @Profile annotation attributes
        MultiValueMap<String, Object> attrs = metadata.getAllAnnotationAttributes(Profile.class.getName());
        if (attrs != null) {
            for (Object value : attrs.get("value")) {
                if (context.getEnvironment().acceptsProfiles(((String[]) value))) {
                    return true;
                }
            }
            return false;
        }
    }
    return true;
}
```

有关更多详细信息，请参阅[`@Conditional` javadocs](https://docs.spring.io/spring-framework/docs/5.0.2.RELEASE/javadoc-api/org/springframework/context/annotation/Conditional.html)。

#### 结合Java和XML配置

Spring的@Configuration类支持的目的并不是100％完全替代Spring XML的。 Spring XML命名空间等一些工具仍然是配置容器的理想方法。在XML方便或必要的情况下，您可以选择：使用ClassPathXmlApplicationContext以“以XML为中心”的方式实例化容器，或者使用AnnotationConfigApplicationContext以及使用@ImportResource注解导入需要的XML配置实现“以Java为中心”的方式实例化容器。

##### 以XML为中心使用@Configuration类

最好从XML引导Spring容器，并以特别的方式包含@Configuration类。例如，在使用Spring XML的大型现有代码库中，根据需要创建@Configuration类并将其包含在现有的XML文件中将更容易。下面你会发现在这种“以XML为中心”的情况下使用@Configuration类的选项。

###### 将@Configuration类声明为普通的Spring <bean />元素

请记住，@Configuration类最终也是容器中的bean定义。在这个例子中，我们创建了一个名为AppConfig的@Configuration类，并将其作为<bean />定义包含在system-test-config.xml中。由于<context：annotation-config />被打开，容器将识别@Configuration注释并正确处理在AppConfig中声明的@Bean方法。

```java
@Configuration
public class AppConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public AccountRepository accountRepository() {
        return new JdbcAccountRepository(dataSource);
    }

    @Bean
    public TransferService transferService() {
        return new TransferService(accountRepository());
    }
}
```

**system-test-config.xml**:

```xml
<beans>
    <!-- enable processing of annotations such as @Autowired and @Configuration -->
    <context:annotation-config/>
    <context:property-placeholder location="classpath:/com/acme/jdbc.properties"/>

    <bean class="com.acme.AppConfig"/>

    <bean class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>
</beans>
```

**jdbc.properties**:

```
jdbc.url=jdbc:hsqldb:hsql://localhost/xdb
jdbc.username=sa
jdbc.password=
```

```java
public static void main(String[] args) {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/com/acme/system-test-config.xml");
    TransferService transferService = ctx.getBean(TransferService.class);
    // ...
}
```

```
在上面的system-test-config.xml中，AppConfig <bean />没有声明一个id元素。然而这样做是可以接受的，而且没有必要考虑到其他bean将不会引用它，并且不太可能通过名称明确地从容器中获取它。同样，对于DataSource bean，它只是通过类型被自动装配，所以不需要显式的bean id。
```

###### 使用<context：component-scan />来获取@Configuration类

因为@Configuration是用@Component进行元注释的，所以@ Configuration-annotated类自动成为组件扫描的候选对象。使用与上面相同的场景，我们可以重新定义system-test-config.xml以利用组件扫描。请注意，在这种情况下，我们不需要显式声明<context：annotation-config />，因为<context：component-scan />启用了相同的功能。

**system-test-config.xml**:

```xml
<beans>
    <!-- picks up and registers AppConfig as a bean definition -->
    <context:component-scan base-package="com.acme"/>
    <context:property-placeholder location="classpath:/com/acme/jdbc.properties"/>

    <bean class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>
</beans>
```

##### 以@Configuration类为中心利用@ImportResource使用XML

在@Configuration类是配置容器的主要机制的应用程序中，仍然可能有必要使用少量XML。在这些场景中，只需使用@ImportResource，并根据需要定义尽可能多的XML。这样做可以实现“以Java为中心”的方式来配置容器，并将XML保持最低限度。

```java
@Configuration
@ImportResource("classpath:/com/acme/properties-config.xml")
public class AppConfig {

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        return new DriverManagerDataSource(url, username, password);
    }
}
```

```xml
properties-config.xml
<beans>
    <context:property-placeholder location="classpath:/com/acme/jdbc.properties"/>
</beans>
```

```properties
jdbc.properties
jdbc.url=jdbc:hsqldb:hsql://localhost/xdb
jdbc.username=sa
jdbc.password=
```

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
    TransferService transferService = ctx.getBean(TransferService.class);
    // ...
}
```

## 1.13. Environment抽象

[`Environment`](https://docs.spring.io/spring-framework/docs/5.0.2.RELEASE/javadoc-api/org/springframework/core/env/Environment.html)是一个集成在容器中的抽象，它模拟了应用程序环境的两个关键方面：[*profiles*](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-definition-profiles) and [*properties*](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-property-source-abstraction).



*profile* 是只有在给定*profile* 处于活动状态时才能在容器中注册的命名的，有逻辑的bean定义集合。Beans 可以被分配给配置文件，不管bean是以XML还是通过注释来定义的。与配置文件相关的Environment对象的作用是确定哪些配置文件（如果有的话）当前处于活动状态，以及哪些配置文件（如果有）在默认情况下应该处于活动状态。

Properties在几乎所有应用程序中都扮演着重要的角色，它可能来源于各种来源：properties files, JVM system properties, system environment variables, JNDI, servlet context parameters, ad-hoc Properties objects, Maps,等等。与Properties相关的Environment对象的作用是为用户提供一个方便的服务接口，用于配置属性来源并解析属性。

### 1.13.1 Bean定义profiles

Bean定义profiles是核心容器中的一种机制，这种机制允许在不同的环境中注册不同的bean。环境这个词对于不同的用户来说意味着不同的东西，这个特性可以帮助很多实践场景，包括：

- 在开发环境中使用内存数据源，而在QA或生产环境中查找来自JNDI的相同数据源
- 仅在将应用程序部署到性能环境中时才注册监视基础构件
- 为客户A和客户B部署注册bean的自定义实现

让我们考虑需要数据源的实际应用中的第一个用例。在测试环境中，配置可能如下所示：

```java
@Bean
public DataSource dataSource() {
    return new EmbeddedDatabaseBuilder()
        .setType(EmbeddedDatabaseType.HSQL)
        .addScript("my-schema.sql")
        .addScript("my-test-data.sql")
        .build();
}
```

现在让我们考虑如何将这个应用程序部署到QA或生产环境中，假定应用程序的数据源将被注册到生产应用程序服务器的JNDI目录中。我们的dataSource bean现在看起来像这样：

```java
@Bean(destroyMethod="")
public DataSource dataSource() throws Exception {
    Context ctx = new InitialContext();
    return (DataSource) ctx.lookup("java:comp/env/jdbc/datasource");
}
```

问题是如何根据当前的环境来使用这两种配置。随着时间的推移，Spring用户设计了许多方法来完成这个任务，通常依赖于系统环境变量和包含$ {placeholder}的XML <import ></import>语句的组合，这些$ {placeholder}根据相应环境变量的值解析正确的配置文件路径。 Bean 定义 profiles是提供解决此问题的核心容器功能。

如果我们概括一下上面特定于环境的bean定义的示例，我们最终需要在特定的上下文中注册某些bean定义，而不是在其他上下文下注册。你可以说你想在情况A中注册一个特定的bean定义配置文件，而在情况B中需要注册一个不同的配置文件。让我们先看看如何更新我们的配置来实现这个需求。

#### @Profile

当一个或多个指定的profiles处于激活状态时，[`@Profile`](https://docs.spring.io/spring-framework/docs/5.0.2.RELEASE/javadoc-api/org/springframework/context/annotation/Profile.html) 注解允许您指示该组件有资格注册。使用我们上面的例子，我们可以重写dataSource配置如下：

```java
@Configuration
@Profile("development")
public class StandaloneDataConfig {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.HSQL)
            .addScript("classpath:com/bank/config/sql/schema.sql")
            .addScript("classpath:com/bank/config/sql/test-data.sql")
            .build();
    }
}
```

```java
@Configuration
@Profile("production")
public class JndiDataConfig {

    @Bean(destroyMethod="")
    public DataSource dataSource() throws Exception {
        Context ctx = new InitialContext();
        return (DataSource) ctx.lookup("java:comp/env/jdbc/datasource");
    }
}
```

```
如前所述，使用@Bean方法，通常会选择使用编程式JNDI查找：使用Spring的JndiTemplate / JndiLocatorDelegate helper或上面显示的直接JNDI InitialContext用法，而不是JndiObjectFactoryBean变体，因为这会强制您声明返回类型为FactoryBean类型。
```

可以将@Profile用作创建自定义组合注释的元注释。以下示例定义了可用作@Profile（“production”）的替换的自定义@Production注释：

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Profile("production")
public @interface Production {
}
```

```
如果@Configuration类用@Profile注解，则除非一个或多个指定的配置文件处于激活状态，否则与该类关联的所有@Bean方法和@Import注释将被忽略。如果一个@Component或者@Configuration类被标记为@Profile（{“p1”，“p2”}）那么除非配置文件'p1'和/或'p2'被激活，否则这个类将不会被注册/处理，。如果一个给定的配置文件以NOT运算符（！）作为前缀，那么如果该配置文件没有激活，注释的元素将被注册。例如，给定@Profile（{“p1”，“！p2”}），则如果配置文件“p1”处于激活状态或配置文件“p2”未激活，则会发生注册。
```

也可以在方法级别声明@Profile仅包括配置类的某一个特定的bean，例如用于特定bean的替代变体：

```java
@Configuration
public class AppConfig {

    @Bean("dataSource")
    @Profile("development")
    public DataSource standaloneDataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.HSQL)
            .addScript("classpath:com/bank/config/sql/schema.sql")
            .addScript("classpath:com/bank/config/sql/test-data.sql")
            .build();
    }

    @Bean("dataSource")
    @Profile("production")
    public DataSource jndiDataSource() throws Exception {
        Context ctx = new InitialContext();
        return (DataSource) ctx.lookup("java:comp/env/jdbc/datasource");
    }
}
```

```
在@Bean方法上使用@Profile时，可能会有一个特殊情况：对于重载的具有相同Java方法名称的@Bean方法（类似于构造函数重载），需要在所有重载的方法上一致地声明@Profile条件。如果条件不一致，那么只有重载方法中第一个声明的条件才起作用。因此@Profile不能用于一个重载的方法，并且使用特定的参数签名；同一个bean的所有工厂方法之间的解析算法在创建时遵循Spring的构造函数解析算法。
如果您想定义具有不同 profile条件的备用bean，请使用不同的Java方法名称，通过@Bean的name属性指向相同的bean名称，如上例所示。如果参数签名都是相同的（例如，所有的变体都有无参的工厂方法），那么这是在有效的Java类中实现这种安排的唯一方法（因为只能有一个方法具有特定的名字和参数签名）。
```

#### XML bean定义profiles

XML对应的是<beans>元素的profile属性。上面的示例配置可以用两个XML文件重写，如下所示：

```xml
<beans profile="development"
    xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:jdbc="http://www.springframework.org/schema/jdbc"
    xsi:schemaLocation="...">

    <jdbc:embedded-database id="dataSource">
        <jdbc:script location="classpath:com/bank/config/sql/schema.sql"/>
        <jdbc:script location="classpath:com/bank/config/sql/test-data.sql"/>
    </jdbc:embedded-database>
</beans>
```

```xml
<beans profile="production"
    xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:jee="http://www.springframework.org/schema/jee"
    xsi:schemaLocation="...">

    <jee:jndi-lookup id="dataSource" jndi-name="java:comp/env/jdbc/datasource"/>
</beans>
```

也可以避免配置拆分而是在同一个文件中和嵌套<beans />元素：

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:jdbc="http://www.springframework.org/schema/jdbc"
    xmlns:jee="http://www.springframework.org/schema/jee"
    xsi:schemaLocation="...">

    <!-- other bean definitions -->

    <beans profile="development">
        <jdbc:embedded-database id="dataSource">
            <jdbc:script location="classpath:com/bank/config/sql/schema.sql"/>
            <jdbc:script location="classpath:com/bank/config/sql/test-data.sql"/>
        </jdbc:embedded-database>
    </beans>

    <beans profile="production">
        <jee:jndi-lookup id="dataSource" jndi-name="java:comp/env/jdbc/datasource"/>
    </beans>
</beans>
```

spring-bean.xsd被限制为只允许<beans>元素作为文件中的最后一个元素。这应该有助于提供灵活性，而不会在XML文件中造成混乱。

#### 激活一个profile

现在我们已经更新了配置，我们仍然需要指示Spring哪个profile处于激活状态。如果我们现在立刻启动我们的示例应用程序，我们会看到一个NoSuchBeanDefinitionException抛出，因为容器找不到名为dataSource的Spring bean。

激活一个配置文件可以通过几种方式来完成，但最直接的方法就是通过一个ApplicationContext对其进行编程：

```java
AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
ctx.getEnvironment().setActiveProfiles("development");
ctx.register(SomeConfig.class, StandaloneDataConfig.class, JndiDataConfig.class);
ctx.refresh();
```

另外，配置文件还可以通过spring.profiles.active属性声明激活，该属性可以通过system environment variables, JVM system properties, servlet context parameters in `web.xml`, or even as an entry in JNDI (see [PropertySource abstraction](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-property-source-abstraction)) 。在集成测试中，可以通过spring-test模块中的@ActiveProfiles注释声明激活profile（请参阅 [Context configuration with environment profiles](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-ctx-management-env-profiles)）。

请注意，配置文件不是一个“或 - 或”的命题;一次可以激活多个配置文件。以编程方式，只需向setActiveProfiles（）方法提供多个配置文件名称，该方法接受String ... varargs：

```java
ctx.getEnvironment().setActiveProfiles("profile1", "profile2");
```

spring.profiles.active可以接受逗号分隔的profile名称列表：

```java
-Dspring.profiles.active="profile1,profile2"
```

#### Default profile

默认profile表示默认启用的profile。考虑以下：

```xml
@Configuration
@Profile("default")
public class DefaultDataConfig {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.HSQL)
            .addScript("classpath:com/bank/config/sql/schema.sql")
            .build();
    }
}
```

如果没有profile被激活，上面的dataSource将被创建;这可以看作是为一个或多个bean提供默认定义的一种方式。如果启用了任何profile，则默认profile将不适用。

默认profile的名称可以使用环境中的setDefaultProfiles（）更改，或者使用spring.profiles.default属性声明性地更改。

### 1.13.2PropertySource抽象

Spring的`Environment` 抽象提供了对可配置的Property资源的层次结构的搜索操作。为了充分解释，请考虑以下内容：

```java
ApplicationContext ctx = new GenericApplicationContext();
Environment env = ctx.getEnvironment();
boolean containsFoo = env.containsProperty("foo");
System.out.println("Does my environment contain the 'foo' property? " + containsFoo);
```

在上面的代码片段中，我们看到了一个高级的方式来询问Spring是否为当前环境定义了foo属性。为了回答这个问题，Environment对象对一组PropertySource对象执行搜索。一个[`PropertySource`](https://docs.spring.io/spring-framework/docs/5.0.2.RELEASE/javadoc-api/org/springframework/core/env/PropertySource.html)是对任意key-value对的简单抽象，Spring的[`StandardEnvironment`](https://docs.spring.io/spring-framework/docs/5.0.2.RELEASE/javadoc-api/org/springframework/core/env/StandardEnvironment.html)配置了两个PropertySource对象，一个表示一组JVM系统属性（一个System.getProperties（）），另一个表示一组系统环境变量（一个System.getenv（））。

这些默认property 源用于StandardEnvironment，用于独立应用程序。 StandardServletEnvironment填充了额外的默认property 源，包括servlet配置和servlet上下文参数。它可以选择启用JndiPropertySource。有关详细信息，请参阅javadocs。

这些默认property 源用于StandardEnvironment，用于独立应用程序。 [`StandardServletEnvironment`](https://docs.spring.io/spring-framework/docs/5.0.2.RELEASE/javadoc-api/org/springframework/web/context/support/StandardServletEnvironment.html)填充了额外的默认property 源，包括servlet配置和servlet上下文参数。它可以选择启用[`JndiPropertySource`](https://docs.spring.io/spring-framework/docs/5.0.2.RELEASE/javadoc-api/org/springframework/jndi/JndiPropertySource.html)。有关详细信息，请参阅javadocs。

具体来说，当使用StandardEnvironment时，如果在运行时存在foo系统属性或foo环境变量，则对env.containsProperty（“foo”）的调用将返回true。



```
属性搜索是分层执行的。默认情况下，系统属性优先于环境变量，所以如果在调用env.getProperty（“foo”）期间foo属性碰巧设置在两个地方，则系统属性值将优先被查找，并优先返回。请注意，属性值不会被合并，而是被前面的条目完全覆盖。
对于公共的StandardServletEnvironment，完整的层次结构如下所示，最高优先级条目位于顶部：
ServletConfig parameters (if applicable, e.g. in case of a DispatcherServlet context)

ServletContext parameters (web.xml context-param entries)

JNDI environment variables ("java:comp/env/" entries)

JVM system properties ("-D" command-line arguments)

JVM system environment (操作系统environment variables)
```

最重要的是，整个机制是可配置的。也许你有一个自定义的properties 来源，你想集成到这个搜索机制中。没问题 - 只需实现并实例化您自己的PropertySource并将其添加到当前环境的PropertySources集合：

```java
ConfigurableApplicationContext ctx = new GenericApplicationContext();
MutablePropertySources sources = ctx.getEnvironment().getPropertySources();
sources.addFirst(new MyPropertySource());
```

在上面的代码中，MyPropertySource在搜索中的优先级最高。如果它包含foo属性，则会在任何其他PropertySource中的foo属性之前被检测到并返回。  [`MutablePropertySources`](https://docs.spring.io/spring-framework/docs/5.0.2.RELEASE/javadoc-api/org/springframework/core/env/MutablePropertySources.html) API公开了许多允许精确操作属性源集的方法。

### 1.13.3. @PropertySource

[`@PropertySource`](https://docs.spring.io/spring-framework/docs/5.0.2.RELEASE/javadoc-api/org/springframework/context/annotation/PropertySource.html)注释提供了一个方便的声明机制，用于将一个PropertySource添加到Spring的Environment中。

给定一个包含键/值对testbean.name = myTestBean的文件“app.properties”，以下@Configuration类使用@PropertySource引入properties文件，以便对testBean.getName（）的调用返回“myTestBean”。

```java
@Configuration
@PropertySource("classpath:/com/myco/app.properties")
public class AppConfig {

    @Autowired
    Environment env;

    @Bean
    public TestBean testBean() {
        TestBean testBean = new TestBean();
        testBean.setName(env.getProperty("testbean.name"));
        return testBean;
    }
}
```

存在于@PropertySource resource  location中的任何$ {...}占位符将根据已经在environment中注册的一组属性中解析。例如：

```java
@Configuration
@PropertySource("classpath:/com/${my.placeholder:default/path}/app.properties")
public class AppConfig {

    @Autowired
    Environment env;

    @Bean
    public TestBean testBean() {
        TestBean testBean = new TestBean();
        testBean.setName(env.getProperty("testbean.name"));
        return testBean;
    }
}
```

假设“my.placeholder”存在于已经注册的一个 property sources中，例如系统属性或环境变量，占位符将被解析为相应的值。如果不是，则默认使用“default / path”。如果没有指定默认值，并且无法解析属性，则会抛出IllegalArgumentException异常。

### 1.13.4 在语句中的占位符解析

以前，元素中占位符的值只能根据JVM系统属性或环境变量来解决。现在不再是这种情况。由于Environment 抽象被集成到整个容器中，通过它可以轻松地路由占位符的解析。这意味着你可以用任何你喜欢的方式配置解析过程：改变搜索系统属性和环境变量的优先级，或者完全删除它们;根据需要添加您自己的 property源。

具体而言，只要在“环境”中可用，无论客户属性是在哪里定义的，以下语句都可以工作：

```xml
<beans>
    <import resource="com/bank/service/${customer}-config.xml"/>
</beans>
```

## 1.14注册一个LoadTimeWeaver

Spring使用LoadTimeWeaver在类加载到Java虚拟机（JVM）时动态转换类。

要启用load-time weaving，请将@EnableLoadTimeWeaving添加到其中一个@Configuration类中：

```java
@Configuration
@EnableLoadTimeWeaving
public class AppConfig {
}
```

或者对于XML配置，使用 `context:load-time-weaver`元素：

```xml
<beans>
    <context:load-time-weaver/>
</beans>
```

一旦为ApplicationContext配置了LoadTimeWeaver。该ApplicationContext中的任何bean都实现了LoadTimeWeaverAware，从而接收对load-time weaver 实例的引用。这与 [Spring’s JPA support](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/data-access.html#orm-jpa) 相结合是非常有用的，因为JPA类转换必需load-time weaving。有关更多详细信息，请参阅LocalContainerEntityManagerFactoryBean javadocs。有关AspectJ加载时织入的更多信息，请参阅 [Load-time weaving with AspectJ in the Spring Framework](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#aop-aj-ltw).。

## 1.15 ApplicationContext的附加功能

正如本章介绍中所讨论的，org.springframework.beans.factory包提供了用于管理和操作bean的基本功能，包括以编程的方式管理和操纵bean。 org.springframework.context包添加了扩展了BeanFactory接口的ApplicationContext接口，并扩展了其他接口，以更加面向应用程序框架的风格提供附加功能。许多人以完全声明的方式使用ApplicationContext，而不是以编程方式创建它，而是依赖诸如ContextLoader之类的支持类来把自动实例化ApplicationContext作为Java EE Web应用程序正常启动过程的一部分。

为了以更加面向框架的风格增强BeanFactory功能，context package 还提供了以下功能：



- 通过MessageSource接口访问i18n风格的message。
- 通过ResourceLoader接口访问资源，如URL和文件。
- 通过使用ApplicationEventPublisher接口，将事件发布到实现ApplicationListener接口的bean。
- 加载多个（分层）contexts，允许每个context通过HierarchicalBeanFactory接口关注一个特定层，例如应用程序的Web层。

### 1.15.1使用MessageSource进行国际化

ApplicationContext接口扩展了一个名为MessageSource的接口，因此提供了国际化（i18n）功能。 Spring还提供了接口HierarchicalMessageSource，它可以分层解析消息。这些接口一起为Spring消息解析提供了基础。这些接口上定义的方法包括：

- String getMessage（String code，Object [] args，String default，Locale loc）：用于从MessageSource中检索消息的基本方法。当找不到指定语言环境的消息时，将使用默认消息。使用标准库提供的MessageFormat功能，传入的任何参数都将成为替换值。
- String getMessage（String code，Object [] args，Locale loc）：与前面的方法基本相同，但有一点不同：不能指定默认消息;如果无法找到消息，则抛出NoSuchMessageException
- String getMessage（MessageSourceResolvable resolvable，Locale locale）：在前面的方法中使用的所有属性被包装在名为MessageSourceResolvable的类中，您可以在该方法使用这个类

当一个ApplicationContext被加载时，它会自动搜索上下文中定义的MessageSource bean。该bean必须具有名称messageSource。如果找到这样一个bean，所有对前面方法的调用都被委托给这个bean。如果没有找到message source，则ApplicationContext将尝试查找包含具有相同名称的bean的父项。如果是这样，它将使用该Bean作为MessageSource。如果ApplicationContext找不到任何message source，则会实例化一个空的DelegatingMessageSource，以便能够接受对上面定义的方法的调用。

Spring提供了两个MessageSource实现，ResourceBundleMessageSource和StaticMessageSource。两者都实现了HierarchicalMessageSource以进行嵌套消息传递。 StaticMessageSource很少使用，但提供了编程方式来添加消息到源。以下示例中显示了ResourceBundleMessageSource：

```xml
<beans>
    <bean id="messageSource"
            class="org.springframework.context.support.ResourceBundleMessageSource">
        <property name="basenames">
            <list>
                <value>format</value>
                <value>exceptions</value>
                <value>windows</value>
            </list>
        </property>
    </bean>
</beans>
```

在这个例子中，假设你在你的classpath中定义了三个资源包，分别叫做format，exceptions和windows。任何解析消息的请求都将以JDK标准方式通过ResourceBundles解析消息。为了实现这个例子的目的，假设上述两个资源包文件的内容是...

```properties
# in format.properties
message=Alligators rock!
```

```properties
# in exceptions.properties
argument.required=The {0} argument is required.
```

以下示例中显示了执行MessageSource功能的程序。请记住，所有ApplicationContext实现也是MessageSource实现，因此可以转换为MessageSource接口。

```java
public static void main(String[] args) {
    MessageSource resources = new ClassPathXmlApplicationContext("beans.xml");
    String message = resources.getMessage("message", null, "Default", null);
    System.out.println(message);
}
```

从上述程序产生的输出将是...

```
Alligators rock!
```

总结一下，MessageSource被定义在一个名为beans.xml的文件中，该文件存在于你的classpath的根目录下。 messageSource bean定义通过其basenames属性来引用许多资源包。在列表中传递给basenames属性的三个文件作为文件存在于类路径的根目录，分别称为format.properties，exceptions.properties和windows.properties。

下面的示例展示了用于消息查找的参数;这些参数将被转换为字符串并插入到查找消息中的占位符中。

```xml
<beans>

    <!-- this MessageSource is being used in a web application -->
    <bean id="messageSource" class="org.springframework.context.support.ResourceBundleMessageSource">
        <property name="basename" value="exceptions"/>
    </bean>

    <!-- lets inject the above MessageSource into this POJO -->
    <bean id="example" class="com.foo.Example">
        <property name="messages" ref="messageSource"/>
    </bean>

</beans>
```

```java
public class Example {

    private MessageSource messages;

    public void setMessages(MessageSource messages) {
        this.messages = messages;
    }

    public void execute() {
        String message = this.messages.getMessage("argument.required",
            new Object [] {"userDao"}, "Required", null);
        System.out.println(message);
    }
}
```

调用execute（）方法的结果输出将是...

```
The userDao argument is required.
```

关于国际化（i18n），Spring的各种MessageSource实现遵循与标准的JDK ResourceBundle相同的语言环境解析和回退规则。简而言之，继续前面定义的messageSource示例，如果要解析英国（en-GB）语言环境的消息，则需要分别创建名为format_en_GB.properties，exceptions_en_GB.properties和windows_en_GB.properties的文件。

```properties
# in exceptions_en_GB.properties
argument.required=Ebagum lad, the {0} argument is required, I say, required.
```

```java
public static void main(final String[] args) {
    MessageSource resources = new ClassPathXmlApplicationContext("beans.xml");
    String message = resources.getMessage("argument.required",
        new Object [] {"userDao"}, "Required", Locale.UK);
    System.out.println(message);
}
```

从上面的程序运行得到的输出将是...

```
Ebagum lad, the 'userDao' argument is required, I say, required.
```

您还可以使用MessageSourceAware接口来获取对已定义的任何MessageSource的引用。任何ApplicationContext中定义并实现MessageSourceAware接口的的bean在创建和配置bean时都会注入应用程序上下文的MessageSource。

```
作为ResourceBundleMessageSource的替代方法，Spring提供了一个ReloadableResourceBundleMessageSource类。这个变体支持相同的bundle文件格式，但比标准的基于JDK的ResourceBundleMessageSource实现更灵活。特别的是，它允许从任何Spring资源位置（而不仅仅是从类路径）读取文件，并支持热重载bundle属性文件（同时高效地缓存它们之间）。查看ReloadableResourceBundleMessageSource javadoc获取详细信息。
```