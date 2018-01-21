# Testing

Spring团队主张采用测试驱动开发（TDD）方法来进行软件开发，因此Spring涵盖了对集成测试的支持（以及单元测试的最佳实践）。 Spring团队发现，正确使用IoC确实可以使单元测试和集成测试更容易（因为setter方法和相应的类的构造函数的存在使得它们更容易在测试中连接在一起，而无需设置服务定位器注册表等等）...专门用于测试的本章节也希望能够使你相信这一点。

# 1.spring测试简介

测试是企业软件开发不可或缺的一部分。本章重点介绍IoC原理对 [unit testing](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#unit-testing)的增值以及Spring框架对 [integration testing](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#integration-testing)支持的好处。 （企业对测试的彻底处理超出了本参考手册的范围。）

# 2.单元测试

依赖注入应该使你的代码相比较于传统的Java EE开发更少地依赖容器。组成应用程序的POJO应该可以在JUnit或TestNG测试中测试，对象只需使用new操作符实例化，不需要Spring或任何其他容器。您可以使用模拟对象（连同其他有价值的测试技术）来单独测试您的代码。如果您遵循Spring的体系结构建议，您的代码库的清晰分层和组件化将有助于更轻松地进行单元测试。例如，可以通过存根或模拟DAO或Repository接口来测试服务层对象，而无需在运行单元测试时访问持久数据。

真正的单元测试通常运行得非常快，因为不需要运行时基础设置。将真正的单元测试强调为开发方法的一部分将会提高您的工作效率。您可能不需要测试章节的这一部分来帮助您为基于IoC的应用程序编写有效的单元测试。但是，对于某些单元测试场景，Spring Framework提供了以下mock对象和测试支持类。

## 2.1. Mock Objects

### 2.1.1. Environment

org.springframework.mock.env包中包含Environment和PropertySource抽象的模拟实现（请参阅 [Bean definition profiles](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-definition-profiles)和[PropertySource abstraction](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-property-source-abstraction)）。 MockEnvironment和MockPropertySource对于开发依赖于environment特定属性的代码的容器外测试很有用。

### 2.1.2. JNDI

org.springframework.mock.jndi包中包含JNDI SPI的实现，您可以使用该实现为测试套件或独立应用程序设置简单的JNDI环境。例如，如果JDBC DataSources在测试代码中与Java EE容器中的JNDI名称绑定到相同的JNDI名称，则可以在测试场景中重用应用程序代码和配置，而无需进行修改。

### 2.1.3. Servlet API

org.springframework.mock.web包中包含了一组全面的Servlet API mock 对象，可用于测试web contexts, controllers, and filters。这些模拟对象被用于Spring的Web MVC框架，通常比动态模拟对象（如 [EasyMock](http://www.easymock.org/)）或替代Servlet API模拟对象（如[MockObjects](http://www.mockobjects.com/)）更方便使用。

从Spring Framework 5.0开始，org.springframework.mock.web中的模拟对象基于Servlet 4.0 API。

Spring MVC测试框架建立在mock  Servlet API对象上，为Spring MVC提供一个集成测试框架。请参阅[*Spring MVC Test*](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#spring-mvc-test-framework).。

### 2.1.4  Spring Web反应式

org.springframework.mock.http.server.reactive包含用于WebFlux应用程序的ServerHttpRequest和ServerHttpResponse的模拟实现。包org.springframework.mock.web.server包含一个模拟的ServerWebExchange，它依赖于那些模拟的request和response对象

MockServerHttpRequest和MockServerHttpResponse都和特定于服务器的实现一样，继承于同一个抽象基类，并与他们共享行为。例如，模拟request一旦创建就是不可变的，但是您可以使用ServerHttpRequest中的mutate（）方法来创建一个修改的实例。

为了使模拟response正确地执行写入并返回一个写入完成句柄（即Mono <Void>），默认情况下它使用`Flux` with `cache().then()`，Flux缓存数据并使其可用于测试中的断言。应用程序可以设置自定义写入功能，例如测试无限流。

[WebTestClient](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#webtestclient) 建立在模拟请求和响应上，为不使用HTTP服务器的WebFlux应用程序提供支持。这个客户端也可以用于运行服务器的端到端测试。

## 2.2 单元测试支持类

### 2.2.1 一般测试工具

org.springframework.test.util包中包含几个用于单元和集成测试的通用实用程序。

开发人员在测试时，测试涉及使用的应用程序代码以下情况。

ReflectionTestUtils是基于反射的实用程序方法的集合。当开发人员在测试包含像下面列出的测试情况的应用程序时，当需要更改常量值，设置非公共字段，调用非公共配置方法或调用非公共配置、生命周期回调方法的测试场景中，需要使用ReflectionTestUtils。

- ORM框架（如JPA和Hibernate）包含私有或受保护的字段访问，而不是域实体中属性的公共设置方法。
- Spring对@Autowired，@Inject和@Resource等注解的支持，这些注解为私有或受保护的属性、setter方法和配置方法提供依赖注入。
- 生命周期回调方法使用@PostConstruct和@PreDestroy等注释。

AopTestUtils是AOP相关工具方法的集合。这些方法可以用来获取隐藏在一个或多个Spring代理后面的底层目标对象的引用。例如，如果您已经使用EasyMock或Mockito等库将bean配置为动态模拟，并且将模拟包装在Spring代理中，则可能需要直接访问基础模拟对象引用以配置期望并执行验证。对于Spring的核心AOP工具类，请参阅AopUtils和AopProxyUtils。

### 2.2.2. Spring MVC

org.springframework.test.web包包含ModelAndViewAssert，您可以将其与JUnit，TestNG或任何其他测试框架结合使用，用于处理Spring MVC ModelAndView对象的单元测试。

要将Spring MVC Controller作为POJO进行单元测试，请使用ModelAndViewAssert和Spring的 [Servlet API mocks](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#mock-objects-servlet)中的MockHttpServletRequest，MockHttpSession等。对于使用WebApplicationContext配置的Spring MVC和REST控制器的彻底集成测试，请使用[*Spring MVC Test Framework*](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#spring-mvc-test-framework)。

# 3.集成测试

## 3.1 概览

无需部署到应用程序服务器或连接到其他企业基础架构即可执行一些集成测试，这一点非常重要。这将使您能够测试诸如：

- 正确连接Spring IoC容器上下文。
- 使用JDBC或ORM工具访问数据。这将包括诸如SQL语句的正确性，Hibernate查询，JPA实体映射等等。

Spring框架为spring-test模块中的集成测试提供了一流的支持。实际JAR文件的名称可能包含发行版本，也可能是long org.springframework.test的形式，具体取决于您从何处获取（请参阅“[section on Dependency Management](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#dependency-management) ”一节以获取解释）。这个库包含org.springframework.test包，其中包含用Spring容器进行集成测试的有用的类。此测试不依赖于应用程序服务器或其他部署环境。这样的测试比单元测试运行速度要慢，但要比等效的Selenium测试或依赖部署到应用程序服务器的远程测试快得多。

在Spring 2.5和更高版本中，单元和集成测试支持以注解驱动的 [Spring TestContext Framework](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-framework)框架的形式提供。 TestContext框架不受所使用的实际测试框架的影响，因此允许在各种环境中进行测试，包括JUnit，TestNG等等。

3.2 集成测试的目标

- To manage [Spring IoC container caching](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testing-ctx-management) between test execution.
- To provide [Dependency Injection of test fixture instances](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testing-fixture-di).
- To provide [transaction management](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testing-tx) appropriate to integration testing.
- To supply [Spring-specific base classes](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testing-support-classes) that assist developers in writing integration tests.

### 测试框架中的fixture概念解释

接下来的几节将介绍每个目标并提供实现和配置详细信息的链接。

| @Before      | 在每个@Test方法开始执行之前执行一次（存在多个测试方法时将会执行多次），被注解的方法必须是公有的 |
| ------------ | ---------------------------------------- |
| @After       | 在每个@Test方法执行结束之后执行一次，其他与@Before类似        |
| @BeforeClass | 在所有@Test方法开始执行之前执行一次（一共只会执行一次），被注解的方法必须是公有且静态的 |
| @AfterClass  | 在所有@Test方法执行结束之后执行一次，其他与@BeforeClass类似   |

使用**@Before**、**@After**、**@BeforeClass**、**@AfterClass**注解的方法不需要使用**@Test**，因为它们并非测试，而是fixture。需要注意的是，被**@BeforeClass**、**@AfterClass**注解的方法必须是公共且静态的

### 3.2.1 上下文管理和缓存

Spring TestContext框架提供了Spring ApplicationContexts和WebApplicationContexts的一致加载以及这些上下文的缓存。支持加载上下文的缓存很重要，因为启动时间可能成为一个问题 - 不是因为Spring本身的开销，而是因为Spring容器实例化的对象需要时间来实例化。例如，具有50到100个Hibernate映射文件的项目可能需要10到20秒才能加载映射文件，并且在每个测试fixture中运行每个测试之前产生的成本会导致整体测试运行速度降低，从而降低开发人员的工作效率。

测试类通常为XML或Groovy配置元数据（通常在类路径中）声明一个resource location数组，或者用于配置应用程序的一组注释类。这些location或类与用于生产部署的web.xml或其他配置文件中指定的位置或类相同或相似。

默认情况下，ApplicationContext一旦加载，为每个测试都会重用ApplicationContext。因此，每个测试suite只需要一次设置的成本，而后续的测试执行速度要快得多。在这种情况下，术语“suite”意味着所有的测试都运行在同一个JVM中 - 例如，对于给定的项目或模块，所有的测试都是从Ant，Maven或Gradle构建的。在不太可能的情况下，测试会破坏应用程序上下文并需要重新加载 - 例如，通过修改bean定义或应用程序对象的状态 - 可以配置TestContext框架以重新加载配置并重建应用程序上下文，然后执行下一步测试。

See [Context management](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-ctx-management) and [Context caching](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-ctx-management-caching) with the TestContext framework.

### 3.2.2 测试fixture的依赖注入

当TestContext框架加载你的应用程序上下文时，它可以通过依赖注入来选择性地配置你的测试类的实例。这为使用应用程序上下文中预先配置的beans配置测试fixtures 提供了一个方便的机制。这里的强大优势是，您可以在各种测试场景（例如，配置Spring管理的对象图，事务代理，数据源等）中重复使用应用程序上下文，从而避免为个别测试用例复制复杂的测试fixture配置。

作为一个例子，考虑一下我们有一个类HibernateTitleRepository，为Title domain实体实现数据访问逻辑的场景。我们希望编写测试以下内容的集成测试：

Spring配置：基本的，所有与HibernateTitleRepository bean的配置相关的东西是否都是正确并存在的？

Hibernate映射文件配置：是否正确映射了所有东西，并且是正确的lazy-loading设置？

HibernateTitleRepository的逻辑：这个类的配置实例是否按预期执行逻辑？

使用 [TestContext framework](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-fixture-di).查看测试fixture的依赖注入。

### 3.2.3 事务管理

访问真实数据库的测试中的一个常见问题是测试对持久性存储的状态的影响。即使您正在使用开发环境数据库，对状态的更改也可能影响将来的测试。而且，许多操作（例如插入或修改持久数据）不能在事务之外执行（或验证）。

TestContext框架解决了这个问题。默认情况下，框架将为每个测试创建并回滚事务。您只需编写可以假定事务存在的代码即可。如果您在测试中调用事务代理对象，那么它们将根据其配置的事务语义执行正确行为。另外，如果一个测试方法在测试管理的事务内部运行时删除了所选表的内容，事务将默认回滚，数据库将返回到测试之前的状态。事务支持通过在测试的应用程序上下文中定义的PlatformTransactionManager bean提供。

如果你想要一个事务提交 - 不经常的，但偶尔有用，当你想要一个特定的测试插入或修改数据库 -  TestContext框架可以被指示使事务提交，而不是回滚，通过[`@Commit`](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#integration-testing-annotations)注释。

See transaction management with the [TestContext framework](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-tx).

### 3.2.4 集成测试的支持类

Spring TestContext框架提供了几个抽象支持类，可以简化集成测试的编写。这些基础测试类为测试框架提供了定义良好的钩子以及方便的实例变量和方法，使您能够访问：

用于执行显式bean查找或测试整个上下文的状态的ApplicationContext。

一个JdbcTemplate，用于执行SQL语句来查询数据库。在执行数据库相关的应用程序代码之前和之后，可以使用这种查询来确认数据库状态，Spring确保这些查询在与应用程序代码相同的事务范围内运行。当与ORM工具一起使用时，一定要避免 [false positives](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-tx-false-positives)。

另外，您可能希望创建自定义的，应用程序范围的超类，其中包含特定于项目的实例变量和方法。

请参阅 [TestContext framework](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-support-classes)的支持类。

## 3.3    JDBC测试支持

org.springframework.test.jdbc包中包含JdbcTestUtils，它是JDBC相关实用工具函数的集合，旨在简化标准数据库测试场景。具体而言，JdbcTestUtils提供了以下静态实用程序方法。

- `countRowsInTable(..)`: counts the number of rows in the given table
- `countRowsInTableWhere(..)`: counts the number of rows in the given table, using the provided `WHERE` clause
- `deleteFromTables(..)`: deletes all rows from the specified tables
- `deleteFromTableWhere(..)`: deletes rows from the given table, using the provided `WHERE` clause
- `dropTables(..)`: drops the specified tables

请注意，*AbstractTransactionalJUnit4SpringContextTests and AbstractTransactionalTestNGSpringContextTests*提供了方便的方法，将其委托给JdbcTestUtils中的上述方法

spring-jdbc模块提供对配置和启动嵌入式数据库的支持，嵌入式数据库可用于与数据库交互的集成测试。有关详细信息，请参阅 [Embedded database support](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/data-access.html#jdbc-embedded-database-support) and [Testing data access logic with an embedded database](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/data-access.html#jdbc-embedded-database-dao-testing).

## 3.4 注解

### 3.4.1  Spring测试注解

Spring框架提供了以下一组Spring特定的注释，你可以在你的单元和集成测试中和TestContext框架一起使用这些注解。有关更多信息，请参阅相应的javadocs，其中包括缺省属性值，属性别名等。

#### @BootstrapWith

@BootstrapWith是一个类级别的注释，用于配置如何引导Spring TestContext框架。具体来说，@ BootstrapWith用于指定一个自定义的TestContextBootstrapper。请参阅[Bootstrapping the TestContext framework](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-bootstrapping)部分以获取更多详细信息。

#### @ContextConfiguration

@ContextConfiguration定义了用于确定如何为集成测试加载和配置ApplicationContext的类级元数据。具体而言，@ContextConfiguration用于声明应用程序上下文resource `locations`或用于加载上下文的注释类。

resource `locations`通常是位于classpath中的XML配置文件或Groovy脚本;而注释类通常是@Configuration类。但是，资源位置也可以指文件系统中的文件和脚本，注释类可以是组件类等等。

```java
@ContextConfiguration("/test-config.xml")
public class XmlApplicationContextTests {
    // class body...
}
```

```java
@ContextConfiguration(classes = TestConfig.class)
public class ConfigClassApplicationContextTests {
    // class body...
}
```

除了声明资源位置或注释类之外，还可以使用@ContextConfiguration来声明ApplicationContextInitializer类。

```java
@ContextConfiguration(initializers = CustomContextIntializer.class)
public class ContextInitializerTests {
    // class body...
}
```

@ContextConfiguration默认支持子类继承父类声明的资源位置或配置类以及由超类声明的上下文初始化器。

有关更多详细信息，请参阅 [Context management](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-ctx-management)和@ContextConfiguration javadocs。

#### @WebAppConfiguration

@WebAppConfiguration是一个类级别的注释，用于声明集成测试加载的ApplicationContext应该是一个WebApplicationContext。在测试类上另外使用@WebAppConfiguration可确保为测试类加载的是WebApplicationContext，意味着将使用默认值“file：src / main / webapp”作为Web应用程序根目录的路径（即 *resource base path*）。 *resource base path*将在后面的场景中使用，创建一个MockServletContext，作为测试的WebApplicationContext的ServletContext。

```java
@ContextConfiguration
@WebAppConfiguration
public class WebAppTests {
    // class body...
}
```

要覆盖默认值，请通过value属性指定不同的resource base path。支持classpath：和file：资源前缀。如果没有提供资源前缀，则路径被假定为文件系统资源。

```java
@ContextConfiguration
@WebAppConfiguration("classpath:test-web-resources")
public class WebAppTests {
    // class body...
}
```

请注意，@WebAppConfiguration必须与@ContextConfiguration一起使用，不管在单个测试类中，还是在测试类层次结构中使用。有关更多详细信息，请参阅@WebAppConfiguration javadocs。

#### @ContextHierarchy

@ContextHierarchy是一个类级别的注释，用于为集成测试定义ApplicationContexts的层次结构。应该使用一个或多个@ContextConfiguration实例列表来声明@ContextHierarchy，每个实例都在上下文层次结构中定义一个级别。以下示例演示了在单个测试类中使用@ContextHierarchy;不过，@ContextHierarchy也可以在测试类层次结构中使用。

```java
@ContextHierarchy({
    @ContextConfiguration("/parent-config.xml"),
    @ContextConfiguration("/child-config.xml")
})
public class ContextHierarchyTests {
    // class body...
}
```

```java
@WebAppConfiguration
@ContextHierarchy({
    @ContextConfiguration(classes = AppConfig.class),
    @ContextConfiguration(classes = WebConfig.class)
})
public class WebIntegrationTests {
    // class body...
}
```

如果需要合并或覆盖测试类层次结构中给定级别的上下文层次结构的配置，则必须通过在类层次结构中的每个对应级别的@ContextConfiguration中的name属性中提供相同的值来明确命名该级别。有关更多示例，请参阅 [Context hierarchies](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-ctx-management-ctx-hierarchies)和@ContextHierarchy javadocs。

#### @ActiveProfiles

@ActiveProfiles是一个类级别的注解，用于在集成测试加载ApplicationContext时声明哪些bean定义配置文件应该被激活。

```java
@ContextConfiguration
@ActiveProfiles("dev")
public class DeveloperTests {
    // class body...
}
```

```java
@ContextConfiguration
@ActiveProfiles({"dev", "integration"})
public class DeveloperIntegrationTests {
    // class body...
}
```

@ActiveProfiles默认支持继承由超类声明的激活bean definition profiles。还可以通过实现一个自定义的ActiveProfilesResolver并通过@ActiveProfiles的resolver属性注册它来以编程方式解析激活bean definition profiles。

有关示例和更多详细信息，请参阅[Context configuration with environment profiles](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-ctx-management-env-profiles)和@ActiveProfiles javadocs。

#### @TestPropertySource

@TestPropertySource是一个类级注释，用于配置属性文件的位置和内联属性，以将其添加到Environment中的PropertySources集合中，用于为集成测试加载的ApplicationContext。

Test property sources的优先级高于从操作系统环境或Java系统属性加载的属性，以及通过@PropertySource或以编程方式声明性添加的属性源。因此，可以使用Test property sources来有选择地覆盖在system and application property sources.中定义的属性。此外，内联属性比从resource  location加载的属性具有更高的优先级。

以下示例演示如何声明classpath中的属性文件。

```java
@ContextConfiguration
@TestPropertySource("/test.properties")
public class MyIntegrationTests {
    // class body...
}
```

以下示例演示如何声明内联属性。

```java
@ContextConfiguration
@TestPropertySource(properties = { "timezone = GMT", "port: 4242" })
public class MyIntegrationTests {
    // class body...
}
```

#### @DirtiesContext

@DirtiesContext表示底层的Spring ApplicationContext在执行一个测试（即以某种方式修改或损坏 - 例如，改变了单例bean的状态）期间已经被修改了，并且应该被关闭。当应用程序上下文被标记为dirty时，它将从测试框架的缓存中移除并关闭。因此，底层的Spring容器将会重建上下文，用于需要具有相同配置元数据的上下文的任何后续测试。

@DirtiesContext可以在同一个类或类层次结构中用作类级别或方法级别的注释。在这种情况下，根据配置的methodMode和classMode，ApplicationContext在任何此类注释方法之前或之后或当前测试类之前或之后被标记为dirty。

下面的例子解释了什么时候上下文会被各种配置情况标记为dirty：

- Before the current test class, when declared on a class with class mode set to `BEFORE_CLASS`.

  ```java
  @DirtiesContext(classMode = BEFORE_CLASS)
  public class FreshContextTests {
      // some tests that require a new Spring container
  }
  ```

- After the current test class, when declared on a class with class mode set to `AFTER_CLASS` (i.e., the default class mode).

  ```java
  @DirtiesContext
  public class ContextDirtyingTests {
      // some tests that result in the Spring container being dirtied
  }
  ```

- Before each test method in the current test class, when declared on a class with class mode set to `BEFORE_EACH_TEST_METHOD.`

  ```java
  @DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
  public class FreshContextTests {
      // some tests that require a new Spring container
  }
  ```

- After each test method in the current test class, when declared on a class with class mode set to `AFTER_EACH_TEST_METHOD.`

  ```java
  @DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
  public class ContextDirtyingTests {
      // some tests that result in the Spring container being dirtied
  }
  ```

- Before the current test, when declared on a method with the method mode set to `BEFORE_METHOD`.

  ```java
  @DirtiesContext(methodMode = BEFORE_METHOD)
  @Test
  public void testProcessWhichRequiresFreshAppCtx() {
      // some logic that requires a new Spring container
  }
  ```

- After the current test, when declared on a method with the method mode set to `AFTER_METHOD` (i.e., the default method mode).

  ```java
  @DirtiesContext
  @Test
  public void testProcessWhichDirtiesAppCtx() {
      // some logic that results in the Spring container being dirtied
  }
  ```

  如果在通过@ContextHierarchy将某个上下文配置为上下文层次结构的一部分的测试中使用@DirtiesContext，则可以使用hierarchyMode标志来控制上下文高速缓存的清除方式。默认情况下，将使用穷举算法来清除上下文高速缓存，不仅包括当前级别，还包括共享当前测试所共有的祖先上下文的所有其他上下文层级;驻留在公共祖先上下文的子层次中的所有ApplicationContext将被从上下文高速缓存中移除并关闭。如果穷举算法对于特定的用例是过度的，则可以指定更简单的*current level* 算法，如下所示。

```java
@ContextHierarchy({
    @ContextConfiguration("/parent-config.xml"),
    @ContextConfiguration("/child-config.xml")
})
public class BaseTests {
    // class body...
}

public class ExtendedTests extends BaseTests {

    @Test
    @DirtiesContext(hierarchyMode = CURRENT_LEVEL)
    public void test() {
        // some logic that results in the child context being dirtied
    }
}
```

有关EXHAUSTIVE和CURRENT_LEVEL算法的更多详细信息，请参阅DirtiesContext.HierarchyMode javadocs。

#### @TestExecutionListeners

@TestExecutionListeners定义了用于配置TestConecutionListener实现的类级别元数据，该实现应该在TestContextManager中注册。通常，@ TestExecutionListeners与@ContextConfiguration结合使用。

```java
@ContextConfiguration
@TestExecutionListeners({CustomTestExecutionListener.class, AnotherTestExecutionListener.class})
public class CustomTestExecutionListenerTests {
    // class body...
}
```

@TestExecutionListeners默认支持继承的侦听器。查看javadocs的例子和进一步的细节。

#### @Commit

@Commit表示在测试方法完成之后，事务性测试方法的事务应该被提交。 @Commit可以用作@Rollback（false）的直接替换，以便更明确地传达代码的意图。类似于@Rollback，@Commit也可以被声明为类级别或方法级别的注释。

```java
@Commit
@Test
public void testProcessWithoutRollback() {
    // ...
}
```

#### @Rollback

@Rollback表示在测试方法完成之后是否应该回滚事务性测试方法的事务。如果为true，则事务回滚;否则，事务被提交（另见@Commit）。即使未明确声明@Rollback，Spring TestContext框架中集成测试的rollback语义也会默认为true。

当声明为类级注释时，@Rollback为测试类层次结构中的所有测试方法定义默认的rollback语义。当被声明为方法级别的注解时，@Rollback为特定的测试方法定义了回滚语义，可能会覆盖类级别的@Rollback或@Commit语义。

```java
@Rollback(false)
@Test
public void testProcessWithoutRollback() {
    // ...
}
```

#### @BeforeTransaction

@BeforeTransaction指出那些使用Spring的@Transactional注解配置事务的测试方法启动事务之前，应该执行带此注解的void方法。从Spring Framework 4.3开始，@BeforeTransaction方法不需要是public的，并且可以在基于Java 8的接口默认方法中声明。

```java
@BeforeTransaction
void beforeTransaction() {
    // logic to be executed before a transaction is started
}
```

#### @AfterTransaction

@AfterTransaction表示那些使用Spring的@Transactional注解配置事务的测试方法结束事务之后，应该执行带此注解的void方法。从Spring Framework 4.3开始，@AfterTransaction方法不需要是public的，可以在基于Java 8的接口默认方法中声明。

```java
@AfterTransaction
void afterTransaction() {
    // logic to be executed after a transaction has ended
}
```

#### @Sql

@Sql用于注释测试类或测试方法，以在集成测试期间配置针对给定数据库执行的SQL脚本。

```java
@Test
@Sql({"/test-schema.sql", "/test-user-data.sql"})
public void userTest {
    // execute code that relies on the test schema and test data
}
```

请参阅 [Executing SQL scripts declaratively with @Sql](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-executing-sql-declaratively)以获取更多详细信息。

#### @SqlConfig

@SqlConfig定义用于确定如何解析和执行通过@Sql注释配置的SQL脚本的元数据。

```java
@Test
@Sql(
    scripts = "/test-user-data.sql",
    config = @SqlConfig(commentPrefix = "`", separator = "@@")
)
public void userTest {
    // execute code that relies on the test data
}
```

#### @SqlGroup

@SqlGroup是一个集合了几个@Sql注释的容器注释。可以本地使用@SqlGroup，声明几个嵌套的@Sql注释，或者可以将其与Java 8对可重复注释的支持结合使用，这样的话@Sql可以在相同的类或方法上多次声明，隐式地生成此容器注释。

```java
@Test
@SqlGroup({
    @Sql(scripts = "/test-schema.sql", config = @SqlConfig(commentPrefix = "`")),
    @Sql("/test-user-data.sql")
)}
public void userTest {
    // execute code that uses the test schema and test data
}
```

### 3.4.2 标准注解支持

Spring TestContext框架的所有配置的标准语义支持以下注释。请注意，这些注释不是特定于测试，可以在Spring框架的任何地方使用。

- `@Autowired`
- `@Qualifier`
- `@Resource` (javax.annotation) *if JSR-250 is present*
- `@ManagedBean` (javax.annotation) *if JSR-250 is present*
- `@Inject` (javax.inject) *if JSR-330 is present*
- `@Named` (javax.inject) *if JSR-330 is present*
- `@PersistenceContext` (javax.persistence) *if JPA is present*
- `@PersistenceUnit` (javax.persistence) *if JPA is present*
- `@Required`
- `@Transactional`

```
JSR-250生命周期注解
在Spring TestContext框架中@PostConstruct和@PreDestroy可以在ApplicationContext中配置的任何应用程序组件上与标准语义一起使用;但是，这些生命周期注释在实际的测试类中使用有限。
如果一个测试类中的方法用@PostConstruct注解，那么这个方法将会在底层测试框架的任何一个before方法之前被执行（例如，用JUnit Jupiter的@BeforeEach注解的方法），并且这个方法将适用于测试中的每个测试方法类。另一方面，如果一个测试类中的方法用@PreDestroy注解，那么这个方法将永远不会被执行。因此，在测试类中，建议使用底层测试框架的测试生命周期回调，而不是@PostConstruct和@PreDestroy。
```

3.4.3 Spring JUnit 4测试注解

以下注解仅在与 [SpringRunner](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-junit4-runner), [Spring’s JUnit 4 rules](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-junit4-rules), or [Spring’s JUnit 4 support classes](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-support-classes-junit4)一起使用时才受支持。

#### @IfProfileValue

@IfProfileValue表示添加了该注解的测试只对特定的测试environment生效。如果配置的ProfileValueSource为此注解的name所提供的值返回匹配值，则测试启用。否则，测试将被禁用并被有效忽略。

@IfProfileValue可以应用于class级别，方法级别或两者。 @IfProfileValue的类级使用优先于该类或其子类中的任何方法的方法级使用。具体而言，如果在class级别和方法级别都启用了测试，则启用测试; @IfProfileValue的缺失意味着测试被隐式启用。这与JUnit 4的@Ignore注释的语义类似，不同的是@Ignore的存在总是禁用一个测试。

```java
@IfProfileValue(name="java.vendor", value="Oracle Corporation")
@Test
public void testProcessWhichRunsOnlyOnOracleJvm() {
    // some logic that should run only on Java VMs from Oracle Corporation
}
```

或者，您可以使用values的列表（包含OR语义）配置@IfProfileValue，以在JUnit 4环境中为测试组实现TestNG类似的支持。考虑下面的例子：

```java
@IfProfileValue(name="test-groups", values={"unit-tests", "integration-tests"})
@Test
public void testProcessWhichRunsForUnitOrIntegrationTestGroups() {
    // some logic that should run only for unit and integration test groups
}
```

#### @ProfileValueSourceConfiguration

@ProfileValueSourceConfiguration是一个类级注释，它指定在检索通过@IfProfileValue注释配置的profile value时要使用的ProfileValueSource的类型。如果未为测试声明@ProfileValueSourceConfiguration，则默认使用SystemProfileValueSource。

```java
@ProfileValueSourceConfiguration(CustomProfileValueSource.class)
public class CustomProfileValueSourceTests {
    // class body...
}
```

#### @Timed

@Timed表示注释的测试方法必须在指定的时间段（毫秒）内完成执行。如果文本执行时间超过指定的时间段，则测试失败。

时间段包括测试方法本身的执行，测试的任何重复（参见@Repeat）以及测试fixture的设置或拆卸。

```java
@Timed(millis=1000)
public void testProcessWithOneSecondTimeout() {
    // some logic that should not take longer than 1 second to execute
}
```

Spring的@Timed注释与JUnit 4的@Test（timeout = ...）支持有不同的语义。具体而言，由于JUnit 4处理测试执行超时的方式（也就是说，通过在单独的线程中执行测试方法），如果测试时间过长，@ Test（timeout = ...）会提前使测试失败。另一方面，Spring的@Timed不会提前使测试失败，而是在失败前等待测试完成。

#### @Repeat

要重复执行的范围包括测试方法本身的执行以及测试fixture的设置或拆卸。

```java
@Repeat(10)
@Test
public void testProcessRepeatedly() {
    // ...
}
```

### 3.4.4. Spring JUnit Jupiter Testing Annotations

仅当与SpringExtension和JUnit Jupiter（即JUnit 5中的编程模块）结合使用时才支持以下注释。

#### @SpringJUnitConfig

@SpringJUnitConfig是一个组合的注释，它将JUnit Jupiter的@ExtendWith（SpringExtension.class）与Spring TestContext框架中的@ContextConfiguration结合在一起。它可以在类级别用作@ContextConfiguration的插入替换。关于配置选项，@ContextConfiguration和@SpringJUnitConfig的唯一区别是可以通过@SpringJUnitConfig中的value属性来声明带注释的类。

```java
@SpringJUnitConfig(TestConfig.class)
class ConfigurationClassJUnitJupiterSpringTests {
    // class body...
}
```

```java
@SpringJUnitConfig(locations = "/test-config.xml")
class XmlJUnitJupiterSpringTests {
    // class body...
}
```

有关更多详细信息，请参阅[Context management](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-ctx-management) 以及用于@SpringJUnitConfig和@ContextConfiguration的javadoc。

#### @SpringJUnitWebConfig

@SpringJUnitWebConfig是一个组合的注释，它将JUnit Jupiter的@ExtendWith（SpringExtension.class）与Spring TestContext框架的@ContextConfiguration和@WebAppConfiguration结合在一起。它可以在类级别用作@ContextConfiguration和@WebAppConfiguration的插入替换。关于配置选项，@ContextConfiguration和@SpringJUnitWebConfig的唯一区别是可以通过@SpringJUnitWebConfig中的value属性来声明带注释的类。另外，来自@WebAppConfiguration的value属性只能通过@SpringJUnitWebConfig中的resourcePath属性覆盖。

```java
@SpringJUnitWebConfig(TestConfig.class)
class ConfigurationClassJUnitJupiterSpringWebTests {
    // class body...
}
```

```java
@SpringJUnitWebConfig(locations = "/test-config.xml")
class XmlJUnitJupiterSpringWebTests {
    // class body...
}
```

有关更多详细信息，请参阅[Context management](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-ctx-management)以及@SpringJUnitWebConfig，@ContextConfiguration和@WebAppConfiguration的javadoc。

#### @EnabledIf

@EnabledIf用于表示已注释的JUnit Jupiter测试类或测试方法已启用，并且如果提供的表达式的计算结果为true，则应该执行。具体来说，如果表达式的计算结果为Boolean.TRUE或者一个等于“true”的字符串（忽略大小写），测试将被启用。在类级别应用时，该类中的所有测试方法也会默认自动启用。

表达式可以是以下任何一种。

- [Spring Expression Language](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#expressions) (SpEL) expression – for example:
  - `@EnabledIf("#{systemProperties['os.name'].toLowerCase().contains('mac')}")`
- Placeholder for a property available in the Spring [`Environment`](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-environment) – for example:
  - `@EnabledIf("${smoke.tests.enabled}")`
- Text literal – for example:
  - `@EnabledIf("true")`

但是，请注意，由于@EnabledIf（“false”）等同于@Disabled，而@EnabledIf（“true”）在逻辑上是无意义的，所以如果表达式的结果是false实际上是没有价值的。

@EnabledIf可以用作元注释来创建自定义组合注释。例如，可以按如下方式创建自定义的@EnabledOnMac注释。

```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@EnabledIf(
    expression = "#{systemProperties['os.name'].toLowerCase().contains('mac')}",
    reason = "Enabled on Mac OS"
)
public @interface EnabledOnMac {}
```

#### @DisabledIf

@DisabledIf用于表示已注释的JUnit Jupiter测试类或测试方法被禁用，并且如果提供的表达式的计算结果为true，则不应执行。具体而言，如果表达式求值为Boolean.TRUE或者一个等于“true”的字符串（忽略大小写），则测试将被禁用。在类级应用时，该类中的所有测试方法也会自动禁用。

表达式可以是以下任何一种。

- [Spring Expression Language](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#expressions) (SpEL) expression – for example:
  - `@DisabledIf("#{systemProperties['os.name'].toLowerCase().contains('mac')}")`
- Placeholder for a property available in the Spring [`Environment`](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-environment) – for example:
  - `@DisabledIf("${smoke.tests.disabled}")`
- Text literal – for example:
  - `@DisabledIf("true")`

但是请注意，由于@DisabledIf（“true”）等同于@Disabled，而@DisabledIf（“false”）在逻辑上是无意义的，所以如果表达式的结果是false实际上是没有价值的

@DisabledIf可以用作元注释来创建自定义组合注释。例如，可以创建一个自定义@DisabledOnMac注释，如下所示。

```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@DisabledIf(
    expression = "#{systemProperties['os.name'].toLowerCase().contains('mac')}",
    reason = "Disabled on Mac OS"
)
public @interface DisabledOnMac {}
```

### 3.4.5. 测试中的元注解支持

可以将大多数与测试相关的注释用作[meta-annotations](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-meta-annotations)，以便创建自定义的注释并减少跨测试套件的配置重复。

以下每个可以用作与TestContext框架结合的元注释。

- `@BootstrapWith`
- `@ContextConfiguration`
- `@ContextHierarchy`
- `@ActiveProfiles`
- `@TestPropertySource`
- `@DirtiesContext`
- `@WebAppConfiguration`
- `@TestExecutionListeners`
- `@Transactional`
- `@BeforeTransaction`
- `@AfterTransaction`
- `@Commit`
- `@Rollback`
- `@Sql`
- `@SqlConfig`
- `@SqlGroup`
- `@Repeat` *(only supported on JUnit 4)*
- `@Timed` *(only supported on JUnit 4)*
- `@IfProfileValue` *(only supported on JUnit 4)*
- `@ProfileValueSourceConfiguration` *(only supported on JUnit 4)*
- `@SpringJUnitConfig` *(only supported on JUnit Jupiter)*
- `@SpringJUnitWebConfig` *(only supported on JUnit Jupiter)*
- `@EnabledIf` *(only supported on JUnit Jupiter)*
- `@DisabledIf` (only supported on JUnit Jupiter)

例如，如果我们发现我们在基于JUnit 4的测试套件中重复以下配置...

```java
@RunWith(SpringRunner.class)
@ContextConfiguration({"/app-config.xml", "/test-data-access-config.xml"})
@ActiveProfiles("dev")
@Transactional
public class OrderRepositoryTests { }

@RunWith(SpringRunner.class)
@ContextConfiguration({"/app-config.xml", "/test-data-access-config.xml"})
@ActiveProfiles("dev")
@Transactional
public class UserRepositoryTests { }
```

我们可以通过引入一个自定义的注释来减少上面的重复，这个注释集中了Spring的通用测试配置，如下所示：

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ContextConfiguration({"/app-config.xml", "/test-data-access-config.xml"})
@ActiveProfiles("dev")
@Transactional
public @interface TransactionalDevTestConfig { }
```

然后，我们可以使用我们的自定义@TransactionalDevTestConfig注释来简化各个基于JUnit 4的测试类的配置，如下所示：

```java
@RunWith(SpringRunner.class)
@TransactionalDevTestConfig
public class OrderRepositoryTests { }

@RunWith(SpringRunner.class)
@TransactionalDevTestConfig
public class UserRepositoryTests { }
```

如果我们使用JUnit Jupiter编写测试，我们可以进一步减少代码重复，因为JUnit 5中的注释也可以用作元注释。例如，如果我们发现我们在基于JUnit Jupiter的测试套件中重复以下配置...

```java
@ExtendWith(SpringExtension.class)
@ContextConfiguration({"/app-config.xml", "/test-data-access-config.xml"})
@ActiveProfiles("dev")
@Transactional
class OrderRepositoryTests { }

@ExtendWith(SpringExtension.class)
@ContextConfiguration({"/app-config.xml", "/test-data-access-config.xml"})
@ActiveProfiles("dev")
@Transactional
class UserRepositoryTests { }
```

我们可以通过引入一个自定义组合注释来减少上述重复，这个注释集中了Spring和JUnit Jupiter的常见测试配置，如下所示：

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(SpringExtension.class)
@ContextConfiguration({"/app-config.xml", "/test-data-access-config.xml"})
@ActiveProfiles("dev")
@Transactional
public @interface TransactionalDevTestConfig { }
```

然后，我们可以使用我们的自定义@TransactionalDevTestConfig注释来简化各个基于JUnit Jupiter的测试类的配置，如下所示：

```java
@TransactionalDevTestConfig
class OrderRepositoryTests { }

@TransactionalDevTestConfig
class UserRepositoryTests { }
```

由于JUnit Jupiter支持使用@Test，@RepeatedTest，ParameterizedTest等作为元注释，所以也可以在测试方法级别创建自定义的注释。例如，如果我们希望创建一个将JUnit Jupiter的@Test和@Tag注释与Spring的@Transactional注解相结合的注释，我们可以创建一个@TransactionalIntegrationTest注解，如下所示。

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Transactional
@Tag("integration-test") // org.junit.jupiter.api.Tag
@Test // org.junit.jupiter.api.Test
public @interface TransactionalIntegrationTest { }
```

然后我们可以使用我们自定义的@TransactionalIntegrationTest注解来简化各个基于JUnit Jupiter的测试方法的配置，如下所示：

```java
@TransactionalIntegrationTest
void saveOrder() { }

@TransactionalIntegrationTest
void deleteOrder() { }
```

有关更多详细信息，请参阅[Spring Annotation Programming Model](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#annotation-programming-model)。

## 3.5  Spring TestContext框架

Spring TestContext框架（位于org.springframework.test.context包中）提供了通用的，注解驱动的单元和集成测试支持，它与所使用的测试框架无关。 TestContext框架也非常重视约定优于配置，合理的默认值可以通过基于注释的配置来覆盖。

除了通用测试基础架构外，TestContext框架还为JUnit 4，JUnit Jupiter（又名，JUnit 5）和TestNG提供了明确的支持。对于JUnit 4和TestNG，Spring提供了抽象的支持类。此外，Spring为JUnit 4提供了一个自定义的JUnit Runner和自定义JUnit规则，以及JUnit Jupiter的一个自定义扩展，允许编写所谓的POJO测试类。 POJO测试类不需要扩展特定的类层次结构，如抽象支持类。

以下部分提供了TestContext框架的内部概述。如果您只对使用框架感兴趣，而不希望使用自定义侦听器或自定义加载器进行扩展，则可以直接转到配置（[context management](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-ctx-management), [dependency injection](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-fixture-di), [transaction management](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-tx)）， [support classes](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-support-classes)和[annotation support](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#integration-testing-annotations)部分。



概念：

测试类（TestCase）：一个包含一个或是多个测试的类，在Junit中就是指的是包含那些带有@Test注解的方法的类，同一样也被称作“测试用例”;

测试集（TestSuite）：测试集是把多个相关测试归入一个组的表达方式，在Junit中，如果我们没有明确的定义一个测试集，那么Juint会自动的提供一个测试集，一个测试集一般将同一个包的测试类归入一组；

测试运行器（TestRunner）：执行测试集的程序

### 3.5.3. TestExecutionListener configuration

Spring提供了以下默认注册的TestExecutionListener实现，完全按照这个顺序。

- `ServletTestExecutionListener`: configures Servlet API mocks for a `WebApplicationContext`
- `DirtiesContextBeforeModesTestExecutionListener`: handles the `@DirtiesContext` annotation for *before* modes
- `DependencyInjectionTestExecutionListener`: provides dependency injection for the test instance
- `DirtiesContextTestExecutionListener`: handles the `@DirtiesContext` annotation for *after* modes
- `TransactionalTestExecutionListener`: provides transactional test execution with default rollback semantics
- `SqlScriptsTestExecutionListener`: executes SQL scripts configured via the `@Sql` annotation

#### 注册自定义TestExecutionListeners

自定义TestExecutionListeners可以通过@TestExecutionListeners注解为测试类及其子类注册。有关详细信息和示例，请参阅[annotation support](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#integration-testing-annotations) 和@TestExecutionListeners的javadocs。

#### 自动发现默认的TestExecutionListeners

通过@TestExecutionListeners注册自定义TestExecutionListeners适用于在有限的测试场景中使用的自定义侦听器;但是，如果需要在测试套件中使用自定义侦听器，则会变得非常麻烦。自Spring Framework 4.1以来，通过SpringFactoriesLoader机制自动发现默认TestExecutionListener实现来解决此问题。

具体来说，spring-test模块在其META-INF / spring.factories属性文件中的org.springframework.test.context.TestExecutionListener项下声明所有核心默认TestExecutionListener。第三方框架和开发人员可以通过自己的META-INF / spring.factories属性文件以相同的方式将自己的TestExecutionListeners贡献给默认监听器列表。

#### Ordering TestExecutionListeners

略

#### 合并TestExecutionListeners

如果通过@TestExecutionListeners注册自定义TestExecutionListener，则默认侦听器将不会被注册。在大多数常见的测试场景中，这会强制开发人员除了自定义侦听器之外还手动声明所有默认侦听器。以下清单演示了这种配置风格

```java
@ContextConfiguration
@TestExecutionListeners({
    MyCustomTestExecutionListener.class,
    ServletTestExecutionListener.class,
    DirtiesContextBeforeModesTestExecutionListener.class,
    DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    SqlScriptsTestExecutionListener.class
})
public class MyTest {
    // class body...
}
```

这种方法面临的挑战是它要求开发人员确切地知道默认注册了哪些监听器。而且，默认侦听器的集合从版本到版本是不同的 - 例如，Spring Framework 4.1中引入了SqlScriptsTestExecutionListener，Spring Framework 4.2中引入了DirtiesContextBeforeModesTestExecutionListener。此外，像Spring Security这样的第三方框架通过前面提到的[automatic discovery mechanism](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-tel-config-automatic-discovery)来注册他们自己的默认TestExecutionListeners。

为了避免必须知道并重新声明所有默认监听器，可以将@TestExecutionListeners的mergeMode属性设置为MergeMode.MERGE_WITH_DEFAULTS。 MERGE_WITH_DEFAULTS表示本地声明的侦听器应与默认侦听器合并。合并算法可确保不需要在列表中声明默认监听器，并按照 [Ordering TestExecutionListeners](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-tel-config-ordering)中所述，根据AnnotationAwareOrderComparator的语义对结果集合的侦听器进行排序。如果一个监听器实现Ordered或者用@Order注解，则可以影响它与默认值合并的位置;否则，在合并时，本地声明的侦听器将简单地追加到缺省侦听器的列表中。

例如，如果前面示例中的MyCustomTestExecutionListener类将其顺序值（例如500）配置为小于ServletTestExecutionListener（恰好为1000）的顺序，则可以将MyCustomTestExecutionListener自动合并到默认列表在ServletTestExecutionListener前面，前面的例子可以用下面的代替。

```java
@ContextConfiguration
@TestExecutionListeners(
    listeners = MyCustomTestExecutionListener.class,
    mergeMode = MERGE_WITH_DEFAULTS
)
public class MyTest {
    // class body...
}
```



### 3.5.4 上下文管理

每个TestContext为它所负责的测试实例提供上下文管理和缓存支持。测试实例不会自动获得对配置的ApplicationContext的访问权限。但是，如果一个测试类实现了ApplicationContextAware接口，那么该测试实例则会拥有一个对ApplicationContext的引用。请注意，AbstractJUnit4SpringContextTests和AbstractTestNGSpringContextTests实现了ApplicationContextAware，因此可以自动提供对ApplicationContext的访问。

@Autowired ApplicationContext

作为实现ApplicationContextAware接口的替代方法，您可以通过字段或setter方法中的@Autowired注释为您的测试类注入应用程序上下文。例如：

```java
@RunWith(SpringRunner.class)
@ContextConfiguration
public class MyTest {

    @Autowired
    private ApplicationContext applicationContext;

    // class body...
}
```

同样，如果您的测试类配置为加载WebApplicationContext，则可以将Web应用程序上下文注入到您的测试中，如下所示：

```java
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration
public class MyWebAppTest {
    @Autowired
    private WebApplicationContext wac;

    // class body...
}
```

默认情况下，DependencyInjectionTestExecutionListener自动被配置以提供通过@Autowired依赖注入的功能（请参阅[Dependency injection of test fixtures](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-fixture-di)）。

使用TestContext框架的测试类不需要扩展任何特定的类或实现特定的接口来配置它们的应用程序上下文。相反，配置是通过在类级别声明@ContextConfiguration注释来实现的。如果您的测试类没有显式声明应用程序上下文资源location或注释类，则配置的ContextLoader将确定如何从默认位置或默认配置类加载上下文。除了上下文资源位置和注释类以外，还可以通过应用程序上下文initializers来配置应用程序上下文。

以下部分将介绍如何通过XML配置文件，Groovy脚本，带批注的类（通常是@Configuration类）或使用Spring的@ContextConfiguration批注的上下文initializers来配置ApplicationContext。或者，您可以实现和配置您自己的自定义SmartContextLoader高级用例。

#### Context configuration with XML resources

要使用XML配置文件为您的测试类加载ApplicationContext，请使用@ContextConfiguration注释您的测试类，并使用包含XML配置元数据资源位置的数组来配置locations属性。相对路径（例如“context.xml”）将被视为相对于测试类的包的classpath资源。以斜杠开始的路径被视为绝对类路径位置，例如“/org/example/config.xml”。表示资源URL的路径（即，以classpath：，file：，http：等为前缀的路径）将被原样使用。

```java
@RunWith(SpringRunner.class)
// ApplicationContext will be loaded from "/app-config.xml" and
// "/test-config.xml" in the root of the classpath
@ContextConfiguration(locations={"/app-config.xml", "/test-config.xml"})
public class MyTest {
    // class body...
}
```

@ContextConfiguration使用标准的Java value属性作为locations属性的别名。因此，如果您不需要在@ContextConfiguration中声明其他属性，则可以省略locations属性名称的声明，并使用以下示例中演示的简写格式声明资源位置。

```java
@RunWith(SpringRunner.class)
@ContextConfiguration({"/app-config.xml", "/test-config.xml"})
public class MyTest {
    // class body...
}
```

​	如果您从@ContextConfiguration注解中没有配置location和value属性值，则TestContext框架将尝试检测默认的XML资源位置。具体来说，GenericXmlContextLoader和GenericXmlWebContextLoader将根据测试类的名称检测默认位置。如果您的类名为com.example.MyTest，则GenericXmlContextLoader会从“classpath：com / example / MyTest-context.xml”加载您的应用程序上下文。

```java
package com.example;

@RunWith(SpringRunner.class)
// ApplicationContext will be loaded from
// "classpath:com/example/MyTest-context.xml"
@ContextConfiguration
public class MyTest {
    // class body...
}
```

#### Context configuration with Groovy scripts

略

#### Context configuration with annotated classes

要使用带注释的类为测试类加载ApplicationContext（请参阅 [Java-based container configuration](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-java)），请使用@ContextConfiguration注释测试类，并使用对带注释的类的引用的数组来配置classes属性。

```java
@RunWith(SpringRunner.class)
// ApplicationContext will be loaded from AppConfig and TestConfig
@ContextConfiguration(classes = {AppConfig.class, TestConfig.class})
public class MyTest {
    // class body...
}
```

```
Annotated Classes
术语Annotated Classes可以指以下任何一种
A class annotated with @Configuration

A component (i.e., a class annotated with @Component, @Service, @Repository, etc.)

A JSR-330 compliant class that is annotated with javax.inject annotations

Any other class that contains @Bean-methods
请参阅@Configuration和@Bean的javadoc以获取关于Annotated Classes的配置和语义的更多信息，特别注意`@ Bean` Lite模式的讨论。
```

如果您没有再@ContextConfiguration注释配置classes属性，那么TestContext框架将尝试检测默认配置类的存在。具体来说，AnnotationConfigContextLoader和AnnotationConfigWebContextLoader将在测试类中检测符合配置类实现要求的的所有static嵌套类，符合配置类实现要求在@Configuration javadocs中指定。在以下示例中，OrderServiceTest类声明一个名为Config的static嵌套配置类，该类将自动用于加载测试类的ApplicationContext。请注意，Config类的名称是任意的。此外，如果需要，测试类可以包含多个static嵌套配置类。

```java
@RunWith(SpringRunner.class)
// ApplicationContext will be loaded from the
// static nested Config class
@ContextConfiguration
public class OrderServiceTest {

    @Configuration
    static class Config {

        // this bean will be injected into the OrderServiceTest class
        @Bean
        public OrderService orderService() {
            OrderService orderService = new OrderServiceImpl();
            // set properties, etc.
            return orderService;
        }
    }

    @Autowired
    private OrderService orderService;

    @Test
    public void testOrderService() {
        // test the orderService
    }

}
```

#### Mixing XML, Groovy scripts, and annotated classes

略

#### Context configuration with context initializers

要使用上下文initializers为您的测试类配置ApplicationContext，请使用@ContextConfiguration注释测试类，并使用包含对实现ApplicationContextInitializer的类的引用的数组来配置initializers属性。声明的上下文初initializers将被用于初始化测试类的ConfigurableApplicationContext。请注意，每个声明的initializers支持的具体ConfigurableApplicationContext类型必须与使用的SmartContextLoader创建的ApplicationContext的类型（通常为GenericApplicationContext）兼容。而且，调用初始化器的顺序取决于它们是否实现Spring的Ordered接口，还是用Spring的@Order注解或标准的@Priority注解来注释。

```java
@RunWith(SpringRunner.class)
// ApplicationContext will be loaded from TestConfig
// and initialized by TestAppCtxInitializer
@ContextConfiguration(
    classes = TestConfig.class,
    initializers = TestAppCtxInitializer.class)
public class MyTest {
    // class body...
}
```

还可以完全省略@ContextConfiguration中的XML配置文件，Groovy脚本或注释类的声明，而只声明ApplicationContextInitializer类，initializer然后负责在上下文中注册bean  - 例如，通过以编程方式从XML加载bean定义文件或配置类。

```java
@RunWith(SpringRunner.class)
// ApplicationContext will be initialized by EntireAppInitializer
// which presumably registers beans in the context
@ContextConfiguration(initializers = EntireAppInitializer.class)
public class MyTest {
    // class body...
}
```

#### Context configuration inheritance

@ContextConfiguration支持boolean inheritLocations和inheritInitializers属性，这些属性表示是否应该继承超类声明的资源位置或注释类和上下文初始化器。两个标志的默认值都是true。这意味着测试类类将会继承任何父类提供的资源位置或注释类以及的上下文初始化器。具体而言，测试类的资源位置或注释类将附加到由超类声明的资源位置或注释类的列表中。类似地，给定测试类的初始化器将被添加到由测试超类定义的初始化器集合中。因此，子类可以选择扩展资源位置，注释类或上下文初始化器。

如果@ContextConfiguration中的inheritLocations或inheritInitializers属性设置为false，则测试类的资源位置或注释类和上下文初始化程序将分别有效地替换由超类定义的配置。

在以下使用XML资源位置的示例中，ExtendedTest的ApplicationContext将按照“base-config.xml”和“extended-config.xml”的顺序加载。在“extended-config.xml”中定义的Bean因此可以覆盖（即替换）在“base-config.xml”中定义的bean。

```java
@RunWith(SpringRunner.class)
// ApplicationContext will be loaded from "/base-config.xml"
// in the root of the classpath
@ContextConfiguration("/base-config.xml")
public class BaseTest {
    // class body...
}

// ApplicationContext will be loaded from "/base-config.xml" and
// "/extended-config.xml" in the root of the classpath
@ContextConfiguration("/extended-config.xml")
public class ExtendedTest extends BaseTest {
    // class body...
}
```
同样，在以下使用带注释的类配置的示例中，ExtendedTest的ApplicationContext将按顺序从BaseConfig和ExtendedConfig类中加载。在ExtendedConfig中定义的Bean因此可以覆盖（即替换）在BaseConfig中定义的Bean。

```java
@RunWith(SpringRunner.class)
// ApplicationContext will be loaded from BaseConfig
@ContextConfiguration(classes = BaseConfig.class)
public class BaseTest {
    // class body...
}

// ApplicationContext will be loaded from BaseConfig and ExtendedConfig
@ContextConfiguration(classes = ExtendedConfig.class)
public class ExtendedTest extends BaseTest {
    // class body...
}
```

在以下使用上下文initializers的示例中，ExtendedTest的ApplicationContext将使用BaseInitializer和ExtendedInitializer进行初始化。但是请注意，调用initializers的顺序取决于它们是否实现Spring的Ordered接口，还是用Spring的@Order注解或标准的@Priority注解来注释。

```java
@RunWith(SpringRunner.class)
// ApplicationContext will be initialized by BaseInitializer
@ContextConfiguration(initializers = BaseInitializer.class)
public class BaseTest {
    // class body...
}

// ApplicationContext will be initialized by BaseInitializer
// and ExtendedInitializer
@ContextConfiguration(initializers = ExtendedInitializer.class)
public class ExtendedTest extends BaseTest {
    // class body...
}
```

#### Context configuration with environment profiles

Spring 3.1（a.k.a.，bean定义配置文件）引入了对environments and profiles概念的支持，并且可以配置集成测试激活特定的bean定义配置文件(profile)以适应各种测试场景。这是通过使用@ActiveProfiles注释来注释一个测试类，并提供加载测试的ApplicationContext时应激活的配置文件列表来实现的。

```
@ActiveProfiles可以与新的SmartContextLoader SPI的任何实现一起使用，但不支持旧的ContextLoader SPI的实现。
```

我们来看看一些使用XML配置和@Configuration类的例子。

```xml
<!-- app-config.xml -->
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:jdbc="http://www.springframework.org/schema/jdbc"
    xmlns:jee="http://www.springframework.org/schema/jee"
    xsi:schemaLocation="...">

    <bean id="transferService"
            class="com.bank.service.internal.DefaultTransferService">
        <constructor-arg ref="accountRepository"/>
        <constructor-arg ref="feePolicy"/>
    </bean>

    <bean id="accountRepository"
            class="com.bank.repository.internal.JdbcAccountRepository">
        <constructor-arg ref="dataSource"/>
    </bean>

    <bean id="feePolicy"
        class="com.bank.service.internal.ZeroFeePolicy"/>

    <beans profile="dev">
        <jdbc:embedded-database id="dataSource">
            <jdbc:script
                location="classpath:com/bank/config/sql/schema.sql"/>
            <jdbc:script
                location="classpath:com/bank/config/sql/test-data.sql"/>
        </jdbc:embedded-database>
    </beans>

    <beans profile="production">
        <jee:jndi-lookup id="dataSource" jndi-name="java:comp/env/jdbc/datasource"/>
    </beans>

    <beans profile="default">
        <jdbc:embedded-database id="dataSource">
            <jdbc:script
                location="classpath:com/bank/config/sql/schema.sql"/>
        </jdbc:embedded-database>
    </beans>

</beans>
```

```java
package com.bank.service;

@RunWith(SpringRunner.class)
// ApplicationContext will be loaded from "classpath:/app-config.xml"
@ContextConfiguration("/app-config.xml")
@ActiveProfiles("dev")
public class TransferServiceTest {

    @Autowired
    private TransferService transferService;

    @Test
    public void testTransferService() {
        // test the transferService
    }
}
```

当TransferServiceTest运行时，它的ApplicationContext将从classpath root目录下的app-config.xml配置文件中加载。如果您检查app-config.xml，您会注意到accountRepository bean对datasource bean有依赖关系;然而，dataSource没有被定义为顶级bean。相反，dataSource被定义三次：in the *production* profile, the *dev* profile, and the *default* profile.

通过使用@ActiveProfiles（“dev”）注释TransferServiceTest，我们指示Spring TestContext框架加载ApplicationContext并将活动profile设置为{“dev”}。因此，嵌入式数据库将被创建并填充测试数据，并且accountRepository bean将连接到对dev datasource的引用。这可能就是我们在集成测试中需要的。

将bean分配给default profile有时很有用。只有在没有其他配置文件被激活的情况下，才会包含defaule profile文件中的bean。这可以用来定义在应用程序默认状态下使用的备用bean。例如，您可以明确地为production和dev profile提供一个datasource，但是当这些配置文件都不是活动的时，将内存数据源定义为缺省值。

以下代码清单演示了如何实现相同的配置和集成测试，但使用@Configuration类而不是XML。

```java
@Configuration
@Profile("dev")
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

```java
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

```java
@Configuration
public class TransferServiceConfig {

    @Autowired DataSource dataSource;

    @Bean
    public TransferService transferService() {
        return new DefaultTransferService(accountRepository(), feePolicy());
    }

    @Bean
    public AccountRepository accountRepository() {
        return new JdbcAccountRepository(dataSource);
    }

    @Bean
    public FeePolicy feePolicy() {
        return new ZeroFeePolicy();
    }

}
```

```java
package com.bank.service;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        TransferServiceConfig.class,
        StandaloneDataConfig.class,
        JndiDataConfig.class,
        DefaultDataConfig.class})
@ActiveProfiles("dev")
public class TransferServiceTest {

    @Autowired
    private TransferService transferService;

    @Test
    public void testTransferService() {
        // test the transferService
    }
}
```

在这个变体中，我们将XML配置分成四个独立的@Configuration类：

- `TransferServiceConfig`: acquires a `dataSource` via dependency injection using `@Autowired`
- `StandaloneDataConfig`: defines a `dataSource` for an embedded database suitable for developer tests
- `JndiDataConfig`: defines a `dataSource` that is retrieved from JNDI in a production environment
- `DefaultDataConfig`: defines a `dataSource` for a default embedded database in case no profile is active

与基于XML的配置示例一样，我们仍然使用@ActiveProfiles（“dev”）注释TransferServiceTest，但是这次我们通过@ContextConfiguration注解指定所有四个configuration类。测试类本身本身保持完全不变。

通常情况下，在一个给定的项目中,多个测试类会使用相同的一组配置文件。因此，为了避免@ActiveProfiles注释的重复声明，可以在基类上声明@ActiveProfiles一次，子类将自动从基类继承@ActiveProfiles配置。在以下示例中，@ActiveProfiles（以及其他注释）的声明已移至抽象超类AbstractIntegrationTest。

```java
package com.bank.service;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        TransferServiceConfig.class,
        StandaloneDataConfig.class,
        JndiDataConfig.class,
        DefaultDataConfig.class})
@ActiveProfiles("dev")
public abstract class AbstractIntegrationTest {
}
```

```java
package com.bank.service;

// "dev" profile inherited from superclass
public class TransferServiceTest extends AbstractIntegrationTest {

    @Autowired
    private TransferService transferService;

    @Test
    public void testTransferService() {
        // test the transferService
    }
}
```

@ActiveProfiles也支持可用于禁用活动配置文件继承的inheritProfiles属性。

```java
package com.bank.service;

// "dev" profile overridden with "production"
@ActiveProfiles(profiles = "production", inheritProfiles = false)
public class ProductionTransferServiceTest extends AbstractIntegrationTest {
    // test body
}
```

此外，有时还需要以编程方式解析激活配置文件，而不是声明性的 - 例如，基于：

- the current operating system
- whether tests are being executed on a continuous integration build server
- the presence of certain environment variables
- the presence of custom class-level annotations
- etc.

要以编程方式解决激活bean定义配置文件的问题，只需简单地实现一个自定义的ActiveProfilesResolver并通过@ActiveProfiles的`resolver` 属性注册。以下示例演示如何实现并注册一个自定义的OperatingSystemActiveProfilesResolver。有关更多信息，请参阅相应的javadocs。

```java
package com.bank.service;

// "dev" profile overridden programmatically via a custom resolver
@ActiveProfiles(
    resolver = OperatingSystemActiveProfilesResolver.class,
    inheritProfiles = false)
public class TransferServiceTest extends AbstractIntegrationTest {
    // test body
}
```

```java
package com.bank.service.test;

public class OperatingSystemActiveProfilesResolver implements ActiveProfilesResolver {

    @Override
    String[] resolve(Class<?> testClass) {
        String profile = ...;
        // determine the value of profile based on the operating system
        return new String[] {profile};
    }
}
```

#### Context configuration with test property sources

Spring 3.1在框架中引入了对environment 的property sources继承层次结构的支持，因此Spring 4.1集成测试可以用特定于测试的property sources来配置。与@Configuration类上使用的@PropertySource注释相比，可以在测试类上声明@TestPropertySource注释，以声明测试properties files 或 *inlined* properties的resource locations。这些测试property sources 将被添加到具有该注解的集成测试的ApplicationContext的 environment的PropertySources集合中。

```
@TestPropertySource可以与SmartContextLoader SPI的任何实现一起使用，但不支持旧版本的ContextLoader SPI的实现。
SmartContextLoader的实现通过MergedContextConfiguration中的getPropertySourceLocations（）和getPropertySourceProperties（）方法获取合并的测试 property source值。
```

##### 声明测试property sources

测试properties 文件可以通过@TestPropertySource的locations或value属性进行配置，如以下示例所示。

支持传统和基于XML的属性properties 格式，例如“classpath：/com/example/test.properties”或“file：///path/to/file.xml”。

每个path将被解释为一个Spring资源。一个普通的路径 - 例如“test.properties” - 将被视为一个classpath资源，它是相对于定义测试类的包而言的。以斜线开始的路径将被视为绝对类路径资源，例如：“/org/example/test.xml”。将使用指定的资源协议来加载引用URL的路径（例如，以classpath :, file :, http：等前缀的路径）。资源位置通配符（例如* /。属性）是不允许的：每个location都必须定位一个.properties或.xml资源。

```java
@ContextConfiguration
@TestPropertySource("/test.properties")
public class MyIntegrationTests {
    // class body...
}
```

以key value对形式的内联属性可以通过@TestPropertySource的`properties` 属性进行配置，如以下示例所示。所有键值对都将作为具有最高优先级的单个测试PropertySource添加到封闭`Environment` 中。

键值对的支持语法与为Java properties 文件中的entries 定义的语法相同：

- `"key=value"`
- `"key:value"`
- `"key value"`

```java
@ContextConfiguration
@TestPropertySource(properties = {"timezone = GMT", "port: 4242"})
public class MyIntegrationTests {
    // class body...
}
```

##### 默认属性文件检测

如果将@TestPropertySource声明为空注释（即，没有显式的`locations` or `properties`），框架将尝试检测相对于声明了注释的类的默认属性文件。例如，如果注释的测试类是com.example.MyTest，则相应的默认属性文件是“classpath：com / example / MyTest.properties”。如果无法检测到默认值，则会抛出IllegalStateException异常。

##### 优先权

测试property sources的优先级高于从操作系统环境或Java系统属性加载的属性，以及通过@PropertySource或以编程方式声明性添加的property sources。因此，可以使用测试property sources来有选择地覆盖在系统和应用程序property sources中定义的属性。此外，内联属性比从资源位置加载的属性具有更高的优先级。

在以下示例中，`timezone` and `port`属性以及“/test.properties”中定义的任何属性将覆盖在系统和应用程序属性源中定义的同名属性。此外，如果“/test.properties”文件定义了`timezone` and `port`属性的entry，则这些文件将被通过properties 属性声明的内联属性覆盖。

```java
@ContextConfiguration
@TestPropertySource(
    locations = "/test.properties",
    properties = {"timezone = GMT", "port: 4242"}
)
public class MyIntegrationTests {
    // class body...
}
```

##### 继承和覆盖测试property sources

@TestPropertySource支持boolean inheritLocations和inheritProperties属性，这些属性表示属性文件的resource locations和超类声明的内联属性是否应该被继承。两个标志的默认值都是true。这意味着一个测试类将会继承超类声明的locations和内联属性。具体而言，测试类的locations和内联属性将附加到由超类声明的locations和内联属性。因此，子类可以选择扩展locations和内联属性。请注意，后面出现的属性将覆盖之前出现的名称相同的属性。另外，之前讨论的优先规则也适用于继承的测试property sources。

如果@TestPropertySource中的inheritLocations或inheritProperties属性设置为false，则测试类locations或内联属性将分别替代由超类定义的配置。

在以下示例中，BaseTest的ApplicationContext将仅使用“base.properties”文件作为测试属性源加载。相反，ExtendedTest的ApplicationContext将使用“base.properties”和“extended.properties”文件作为测试属性源位置加载。

```java
@TestPropertySource("base.properties")
@ContextConfiguration
public class BaseTest {
    // ...
}

@TestPropertySource("extended.properties")
@ContextConfiguration
public class ExtendedTest extends BaseTest {
    // ...
}
```

在以下示例中，BaseTest的ApplicationContext将仅使用内联的key1属性加载。相反，ExtendedTest的ApplicationContext将使用内联的key1和key2属性加载。

```java
@TestPropertySource(properties = "key1 = value1")
@ContextConfiguration
public class BaseTest {
    // ...
}

@TestPropertySource(properties = "key2 = value2")
@ContextConfiguration
public class ExtendedTest extends BaseTest {
    // ...
}
```

#### 加载WebApplicationContext

Spring 3.2引入了在集成测试中加载WebApplicationContext的支持。要指示TestContext框架加载WebApplicationContext而不是标准的ApplicationContext，只需使用@WebAppConfiguration注释相应的测试类.

在您的测试类中如果存在@WebAppConfiguration则表示TestContext框架（TCF）应该为您的集成测试加载WebApplicationContext（WAC）。在后台，TCF确保创建一个MockServletContext并将其提供给测试的WAC。默认情况下，MockServletContext的base resource location将被设置为“src / main / webapp”。这被解析为相对于JVM根目录（即，通常是项目的路径）。如果您熟悉Maven项目中Web应用程序的目录结构，就会知道“src / main / webapp”是WAR根目录的默认位置。如果您需要重写此默认值，只需提供@WebAppConfiguration批注的替代路径（例如，@ WebAppConfiguration（“src / test / webapp”））。如果你希望从classpath而不是文件系统引用base resource location，只需使用Spring的classpath：前缀即可。

请注意Spring对WebApplicationContexts的测试支持与对标准ApplicationContexts的支持相同。使用WebApplicationContext进行测试时，可以通过@ContextConfiguration自由声明XML配置文件，Groovy脚本或@Configuration类。您当然也可以自由使用任何其他测试注释，例如@ActiveProfiles，@ TestExecutionListeners，@Sql，@Rollback等。

以下示例演示了加载WebApplicationContext的各种配置选项。

```java
@RunWith(SpringRunner.class)

// defaults to "file:src/main/webapp"
@WebAppConfiguration

// detects "WacTests-context.xml" in same package
// or static nested @Configuration class
@ContextConfiguration

public class WacTests {
    //...
}
```

上面的例子演示了TestContext框架对约定优先配置的支持。如果使用@WebAppConfiguration注释测试类而不指定resource base path，resource base path将有效地默认为“file：src / main / webapp”。同样的，如果你声明了@ContextConfiguration而没有指定resource base path，注释类或上下文initializers，Spring将尝试使用约定来检测你的配置是否存在（例如，WacTests类中的“WacTests-context.xml”或静态嵌套的@Configuration类）。

```java
@RunWith(SpringRunner.class)

// file system resource
@WebAppConfiguration("webapp")

// classpath resource
@ContextConfiguration("/spring/test-servlet-config.xml")

public class WacTests {
    //...
}
```

本示例演示如何使用@WebConfiguration显式声明resource base path，并使用@ContextConfiguration显式声明XML资源位置。这里需要注意的是这两个注释路径的不同语义。默认情况下，@WebAppConfiguration资源路径是基于file system的;而@ContextConfiguration资源位置是基于classpath的。

```java
@RunWith(SpringRunner.class)

// classpath resource
@WebAppConfiguration("classpath:test-web-resources")

// file system resource
@ContextConfiguration("file:src/main/webapp/WEB-INF/servlet-config.xml")

public class WacTests {
    //...
}
```

在第三个例子中，我们看到我们可以通过指定一个Spring资源前缀来覆盖这两个注释的默认资源语义。将本例中的注释与前面的例子进行对比。

##### Working with Web Mocks

为了提供全面的Web测试支持，Spring 3.2引入了默认启用的[TestExecutionListener](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-key-abstractions)。在对WebApplicationContext进行测试时，TestExecutionListener会在每个测试方法之前通过Spring Web的RequestContextHolder设置默认thread-local状态，并基于@WebAppConfiguration配置的基本资源路径创建MockHttpServletRequest，MockHttpServletResponse和ServletWebRequest。 ServletTestExecutionListener也确保MockHttpServletResponse和ServletWebRequest可以被注入到测试实例中，一旦测试完成，ServletTestExecutionListener就清除thread-local状态。

一旦你为你的测试类加载了一个WebApplicationContext，你可能会发现你需要和Web mock交互 - 例如，设置你的测试fixture或在调用Web组件之后执行断言。以下示例演示了哪些mock可以自动装入您的测试实例。请注意，WebApplicationContext和MockServletContext都在测试suite中缓存;而其他的模拟则由ServletTestExecutionListener在每个测试方法中进行管理。

Injecting mocks

```java
@WebAppConfiguration
@ContextConfiguration
public class WacTests {

    @Autowired
    WebApplicationContext wac; // cached

    @Autowired
    MockServletContext servletContext; // cached

    @Autowired
    MockHttpSession session;

    @Autowired
    MockHttpServletRequest request;

    @Autowired
    MockHttpServletResponse response;

    @Autowired
    ServletWebRequest webRequest;

    //...
}
```

#### Context caching

一旦TestContext框架为测试类加载ApplicationContext（或WebApplicationContext），该上下文将被缓存，并在所有后续测试中重用，这些测试类在相同的测试套件中声明相同的唯一的上下文配置。要理解缓存如何工作，了解唯一和测试套件的含义非常重要。

ApplicationContext可以通过用于加载它的配置参数的组合来唯一标识。因此，使用配置参数的唯一组合来生成缓存上下文的key。 TestContext框架使用以下配置参数来构建上下文缓存key：

- `locations` *(from @ContextConfiguration)*
- `classes` *(from @ContextConfiguration)*
- `contextInitializerClasses` *(from @ContextConfiguration)*
- `contextCustomizers` *(from ContextCustomizerFactory)*
- `contextLoader` *(from @ContextConfiguration)*
- `parent` *(from @ContextHierarchy)*
- `activeProfiles` *(from @ActiveProfiles)*
- `propertySourceLocations` *(from @TestPropertySource)*
- `propertySourceProperties` *(from @TestPropertySource)*
- `resourceBasePath` *(from @WebAppConfiguration)*

例如，如果TestClassA为@ContextConfiguration的locations（或value）属性指定了{“app-config.xml”，“test-config.xml”}，则TestContext框架将加载相应的ApplicationContext并将其存储在static上下文中缓存中，缓存的key完全基于这些locations的值。因此，如果TestClassB还为其locations（显式指定或通过继承隐式指定）定义{“app-config.xml”，“test-config.xml”}，而且未定义@WebAppConfiguration、`@WebAppConfiguration`、a different `ContextLoader`, different active profiles, different context initializers, different test property sources, or a different parent context,那么相同的ApplicationContext将被两个测试类共享。这意味着加载应用程序上下文的设置成本只会发生一次（对于每个测试suite），并且后续的测试执行速度要快得多。

```
Test suites and forked processes
Spring TestContext框架将应用程序上下文存储在static缓存中。这意味着，上下文是从上存储在一个静态变量中。换句话说，如果测试在相互独立的进程中执行，那么静态缓存将在每个测试用例之间被清除，这将禁用缓存机制。
为了从缓存机制中受益，所有测试都必须在同一个进程或测试套件中运行。这可以通过在IDE内把所有测试当做一组测试来实现。同样，当使用Ant，Maven或Gradle等构建框架执行测试时，确保构建框架不在测试之间分叉是非常重要的。例如，如果Maven Surefire插件的forkMode设置为always或pertest，则TestContext框架将无法缓存测试类之间的应用程序上下文，后果就是构建过程的运行速度会变慢。
```

自从Spring Framework 4.3以来，上下文缓存的大小默认为32。每当达到最大值时，则使用最近最少使用（LRU）驱逐策略去除和关闭陈旧的上下文。可以通过设置名为spring.test.context.cache.maxSize的JVM系统属性，从命令行或构建脚本配置max size。作为替代，可以通过SpringProperties API以编程方式设置相同的属性。

由于在给定的测试suite中加载了大量的应用程序上下文，可能会导致suite花费不必要的时间来执行，所以确切知道有多少上下文已经加载和高速缓存通常是有益的。要查看底层上下文缓存的统计信息，只需将org.springframework.test.context.cache日志记录类别的日志级别设置为DEBUG即可。

在不太可能的情况下，测试会破坏应用程序上下文并需要重新加载 - 例如，通过修改bean定义或应用程序对象的状态 - 您可以使用@DirtiesContext注释您的测试类或测试方法（请参阅在 [Spring Testing Annotations](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#integration-testing-annotations-spring)中关于@DirtiesContext的讨论）。这指示Spring在执行下一个测试之前从缓存中删除上下文并重建应用上下文。请注意，对于@DirtiesContext注释的支持是由默认启用的DirtiesContextBeforeModesTestExecutionListener和DirtiesContextTestExecutionListener提供的。

#### Context hierarchies

在编写依赖加载的Spring ApplicationContext的集成测试时，通常只需要对单个上下文进行测试即可;然而，有时甚至必须对ApplicationContexts的层次结构进行测试。例如，如果您正在开发Spring MVC Web应用程序，则通常会通过Spring的ContextLoaderListener加载根WebApplicationContext，并通过Spring的DispatcherServlet加载子WebApplicationContext。这会产生父子上下文层次结构，其中共享组件和基础结构配置在根上下文中声明，并由特定于Web的组件在子上下文中使用。另一个例子可以在Spring Batch应用程序中找到，在这些应用程序中，您经常有一个父上下文，该父上下文为共享批处理基础结构提供配置，也有一个子上下文用于配置特定的批处理job。

自Spring Framework 3.2.2以来，可以通过@ContextHierarchy注释来声明使用上下文层次结构的集成测试，不管是在单独的测试类还是在测试类层次结构中。如果在测试类层次结构中的多个类上声明了context hierarchy，则还可以合并或覆盖context hierarchy中特定的命名级别的上下文配置。在层次结构中合并给定级别配置时，配置资源类型（即XML配置文件或注释类）必须一致;否则，在使用不同资源类型配置的上下文分层结构中具有不同级别也是完全可以接受的。

以下基于JUnit 4的示例演示了需要使用上下文层次结构的集成测试的常见配置方案。

##### Single test class with context hierarchy

ControllerIntegrationTests通过声明由两个级别组成的上下文层次结构来表示一个典型的Spring MVC Web应用程序的集成测试场景，一个用于根WebApplicationContext（使用TestAppConfig @Configuration类加载），另一个用于dispatcher servlet WebApplicationContext（使用WebConfig @Configuration类）。自动装配到测试实例中的WebApplicationContext是用于子上下文（即，层次结构中最低的上下文）的WebApplicationContext。

```java
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextHierarchy({
    @ContextConfiguration(classes = TestAppConfig.class),
    @ContextConfiguration(classes = WebConfig.class)
})
public class ControllerIntegrationTests {

    @Autowired
    private WebApplicationContext wac;

    // ...
}
```

##### Class hierarchy with implicit parent context

以下测试类定义了测试类层次结构中的上下文层次结构。 AbstractWebTests在Spring支持的Web应用程序中声明了根WebApplicationContext的配置。但是请注意，AbstractWebTests不会声明@ContextHierarchy;因此，AbstractWebTests的子类可以选择性是参与context层次结构，还是简单地遵循@ContextConfiguration的标准语义。 SoapWebServiceTests和RestWebServiceTests都扩展了AbstractWebTests并通过@ContextHierarchy并定义了一个context层次结构。结果是将加载三个应用程序context（@ContextConfiguration的每个声明一个），并且基于AbstractWebTests中的configuration 加载的应用程序context将被设置为为具体子类加载的每个context的父context。

```java
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext.xml")
public abstract class AbstractWebTests {}

@ContextHierarchy(@ContextConfiguration("/spring/soap-ws-config.xml")
public class SoapWebServiceTests extends AbstractWebTests {}

@ContextHierarchy(@ContextConfiguration("/spring/rest-ws-config.xml")
public class RestWebServiceTests extends AbstractWebTests {}
```

##### Class hierarchy with merged context hierarchy configuration

以下类展示了使用named层次结构级别来合并context层次结构中特定级别的配置。 BaseTests定义了层次结构中的两个层次，即`parent` and `child`。 ExtendedTests扩展了BaseTests并指示Spring TestContext框架合并child层次结构级别的context配置，只需确保通过@ContextConfiguration中的name属性声明的名称都是“child”。结果是将加载三个应用程序上下文：一个用于“/app-config.xml”，一个用于“/user-config.xml”，一个用于{“/user-config.xml”，“/ order- config.xml中“}。与前面的示例一样，从“/app-config.xml”加载的应用程序上下文将被设置为从“/user-config.xml”和{“/user-config.xml， /order-config.xml”}”加载的上下文的父上下文。

```java
@RunWith(SpringRunner.class)
@ContextHierarchy({
    @ContextConfiguration(name = "parent", locations = "/app-config.xml"),
    @ContextConfiguration(name = "child", locations = "/user-config.xml")
})
public class BaseTests {}

@ContextHierarchy(
    @ContextConfiguration(name = "child", locations = "/order-config.xml")
)
public class ExtendedTests extends BaseTests {}
```

##### Class hierarchy with overridden context hierarchy  configuration

与前面的示例相反，此示例演示了如何通过将@ContextConfiguration中的inheritLocations标志设置为false来覆盖上下文层次结构中给定命名级别的配置。因此，ExtendedTests的应用程序上下文将仅从“/test-user-config.xml”加载，并将其父context设置为从“/app-config.xml”加载的上下文。

```java
@RunWith(SpringRunner.class)
@ContextHierarchy({
    @ContextConfiguration(name = "parent", locations = "/app-config.xml"),
    @ContextConfiguration(name = "child", locations = "/user-config.xml")
})
public class BaseTests {}

@ContextHierarchy(
    @ContextConfiguration(
        name = "child",
        locations = "/test-user-config.xml",
        inheritLocations = false
))
public class ExtendedTests extends BaseTests {}
```

Dirtying a context within a context hierarchy

如果具有@DirtiesContext注解的测试类的上下文被配置在上下文分层结构中，那么可以使用hierarchyMode flag来控制如何清除上下文高速缓存。有关更多详细信息，请参阅 in [Spring Testing Annotations](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#integration-testing-annotations-spring)中的讨论@DirtiesContext和@DirtiesContext javadocs。

### 3.5.5. test fixtures的依赖注入

当您使用DependencyInjectionTestExecutionListener（默认配置）时，您的测试实例的依赖关系将从@ContextConfiguration配置的应用程序上下文中的beans中注入。您可以使用setter注入，field注入或两者，取决于您选择的注释类型以及是否将它们放置在setter方法或field上。为了与Spring 2.5和3.0中引入的注解支持保持一致，可以使用Spring的@Autowired注解或JSR 330中的@Inject注解。

```
TestContext框架不会校验测试实例实例化的方式。因此，对构造函数使用@Autowired或@Inject对测试类没有影响。
```

因为@Autowired被用来 [*autowiring by type*](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/core.html#beans-factory-autowire)，所以如果你有多个相同类型的bean定义，那么你不能依靠这种方法来注入特定的bean。在这种情况下，您可以使用@Autowired和@Qualifier。从Spring 3.0开始，您也可以选择@Inject和@Named一起使用。或者，如果您的测试类可以访问其ApplicationContext，则可以使用（例如）调用applicationContext.getBean（“titleRepository”）来执行显式查找。

如果您不想将依赖注入应用于您的测试实例，则不要使用@Autowired或@Inject注释字段或setter方法。或者，可以通过使用@TestExecutionListeners显式配置测试类并从侦听器列表中省略DependencyInjectionTestExecutionListener.class来完全禁用依赖注入。

考虑一下测试HibernateTitleRepository类的场景，如[Goals](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#integration-testing-goals)章节所述。接下来的两个代码清单演示了在字段和setter方法上使用@Autowired。应用程序上下文配置在所有示例代码清单之后呈现。

```
以下代码清单中的依赖注入行为不是特定于JUnit 4.相同的DI技术可以与任何测试框架结合使用。

下面的示例调用静态断言方法，如assertNotNull（），但不用前缀Assert调用。在这种情况下，假定该方法通过导入的静态声明正确导入，该示例中未显示。
```

第一个代码清单显示了使用@Autowired进行字段注入的测试类的基于JUnit 4的实现。

```java
@RunWith(SpringRunner.class)
// specifies the Spring configuration to load for this test fixture
@ContextConfiguration("repository-config.xml")
public class HibernateTitleRepositoryTests {

    // this instance will be dependency injected by type
    @Autowired
    private HibernateTitleRepository titleRepository;

    @Test
    public void findById() {
        Title title = titleRepository.findById(new Long(10));
        assertNotNull(title);
    }
}
```

或者，您可以将类配置为使用@Autowired进行setter注入，如下所示。

```java
@RunWith(SpringRunner.class)
// specifies the Spring configuration to load for this test fixture
@ContextConfiguration("repository-config.xml")
public class HibernateTitleRepositoryTests {

    // this instance will be dependency injected by type
    private HibernateTitleRepository titleRepository;

    @Autowired
    public void setTitleRepository(HibernateTitleRepository titleRepository) {
        this.titleRepository = titleRepository;
    }

    @Test
    public void findById() {
        Title title = titleRepository.findById(new Long(10));
        assertNotNull(title);
    }
}
```

前面的代码清单使用@ContextConfiguration注释引用的相同的XML（即repository-config.xml），它看起来像这样：

```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- this bean will be injected into the HibernateTitleRepositoryTests class -->
    <bean id="titleRepository" class="com.foo.repository.hibernate.HibernateTitleRepository">
        <property name="sessionFactory" ref="sessionFactory"/>
    </bean>

    <bean id="sessionFactory" class="org.springframework.orm.hibernate5.LocalSessionFactoryBean">
        <!-- configuration elided for brevity -->
    </bean>

</beans>
```

​	

```
如果测试类是从Spring提供的测试基类继承而来的，那么您的应用程序上下文中可能会定义多个相同类型的bean：例如多个DataSource bean。在这种情况下，您可以重写setter方法，并使用@Qualifier注释来指定特定的目标bean，如下所示，但也要确保将子类重写超类中的setter方法。
// ...

    @Autowired
    @Override
    public void setDataSource(@Qualifier("myDataSource") DataSource dataSource) {
        super.setDataSource(dataSource);
    }

// ...
指定的qualifier 值指示要注入的特定的DataSource bean，将匹配类型的集合缩小到特定的bean。qualifier的值在相应的<bean>定义中匹配<qualifier>声明。 bean name被用作后备qualifier值，所以你可以通过name有效地指向特定的bean（如上所示，假定“myDataSource”是bean的id）。
```

### 3.5.7事务管理

在TestContext框架中，事务由默认配置的TransactionalTestExecutionListener管理，即使您没有在测试类上显式声明@TestExecutionListener。但是，为了支持事务，必须在通过@ContextConfiguration语义加载的ApplicationContext中配置一个PlatformTransactionManager bean（更多细节在下面提供）。另外，您必须在类或方法级别为测试类声明Spring的@Transactional注释。

#### Test-managed的事务

*Test-managed*事务是通过TransactionalTestExecutionListener声明式管理的事务，或者通过TestTransaction（见下文）以编程方式进行管理的事务。这样的事务不应该与Spring-managed 的事务（即，被加载用于测试的ApplicationContext内的Spring直接管理的事务）或*application-managed *事务（即，在测试类中以编程方式调用应用程序代码的方式管理的事务）相混淆。 Spring-managed和application-managed事务通常会参与Test-managed事务;但是，如果Spring管理的或应用程序管理的事务使用REQUIRED或SUPPORTS以外的任何传播类型进行配置事务，应该小心（有关详细信息，请参阅[transaction propagation](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/data-access.html#tx-propagation) 的讨论）。

#### 启用和禁用事务

使用@Transactional注释一个测试方法会导致测试在事务中运行，默认情况下，测试完成后会自动回滚。如果一个测试类用@Transactional注解，那么这个类层次结构中的每个测试方法都将在一个事务中运行。没有用@Transactional（在类或方法级别）注释的测试方法将不会在事务中运行。此外，使用@Transactional进行注释但将传播类型设置为NOT_SUPPORTED的测试将不会在事务中运行。

请注意，*AbstractTransactionalJUnit4SpringContextTests and AbstractTransactionalTestNGSpringContextTests *为类级别的事务支持已经预配置好了的。

以下示例演示了为基于Hibernate的UserRepository编写集成测试的常见方案。正如在 [Transaction rollback and commit behavior](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-tx-rollback-and-commit-behavior)中所解释的，在执行createUser（）方法之后，不需要清理数据库，因为对数据库所做的任何更改都将由TransactionalTestExecutionListener自动回滚。有关其他示例，请参阅 [PetClinic Example](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testing-examples-petclinic)。

```java
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class HibernateUserRepositoryTests {

    @Autowired
    HibernateUserRepository repository;

    @Autowired
    SessionFactory sessionFactory;

    JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    public void createUser() {
        // track initial state in test database:
        final int count = countRowsInTable("user");

        User user = new User(...);
        repository.save(user);

        // Manual flush is required to avoid false positive in test
        sessionFactory.getCurrentSession().flush();
        assertNumUsers(count + 1);
    }

    protected int countRowsInTable(String tableName) {
        return JdbcTestUtils.countRowsInTable(this.jdbcTemplate, tableName);
    }

    protected void assertNumUsers(int expected) {
        assertEquals("Number of rows in the [user] table.", expected, countRowsInTable("user"));
    }
}
```

#### 事务回滚和提交行为

默认情况下，测试完成后会自动回滚测试。然而，事务提交和回滚行为可以通过@Commit和@Rollback注释声明性地配置。有关更多详细信息，请参阅[annotation support](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#integration-testing-annotations)部分中的相应条目。

#### 编程式事务管理

自Spring Framework 4.1以来，可以通过TestTransaction中的静态方法以编程方式与test-managed事务进行交互。例如，TestTransaction可用于测试方法里面，方法之前和方法之后，用于开始或结束当前的test-managed事务，或配置当前的测试管理的事务以进行回滚或提交。只要启用TransactionalTestExecutionListener，对TestTransaction的支持都是自动可用的。

以下示例演示了TestTransaction的一些功能。有关更多详细信息，请参阅TestTransaction的javadocs。

```java
@ContextConfiguration(classes = TestConfig.class)
public class ProgrammaticTransactionManagementTests extends
        AbstractTransactionalJUnit4SpringContextTests {

    @Test
    public void transactionalTest() {
        // assert initial state in test database:
        assertNumUsers(2);

        deleteFromTables("user");

        // changes to the database will be committed!
        TestTransaction.flagForCommit();
        TestTransaction.end();
        assertFalse(TestTransaction.isActive());
        assertNumUsers(0);

        TestTransaction.start();
        // perform other actions against the database that will
        // be automatically rolled back after the test completes...
    }

    protected void assertNumUsers(int expected) {
        assertEquals("Number of rows in the [user] table.", expected, countRowsInTable("user"));
    }
}
```

#### 在事务之外执行代码

有时您需要在事务性测试方法之前或之后但在事务性上下文之外执行某些代码 - 例如，在执行测试之前验证初始数据库状态，或者在测试执行之后验证预期的事务性commit行为（如果测试事务配置为commit）。 TransactionalTestExecutionListener完全支持这种情况通过使用@BeforeTransaction和@AfterTransaction注解。使用这些注释之一简单地注释测试类中的任何void方法或测试接口中的任何void default方法，TransactionalTestExecutionListener将会确保*before transaction method* or *after transaction method* 在适当的时候执行。

```
任何before methods（例如用JUnit Jupiter的@BeforeEach注释的方法）和任何after methods（例如用JUnit Jupiter注解的@AfterEach方法）都将在事务中执行。另外，使用@BeforeTransaction或@AfterTransaction注解的方法自然不会针对未配置为在事务内运行的测试方法执行。
```
#### 配置事务管理器

TransactionalTestExecutionListener需要在Spring ApplicationContext中为测试类定义一个PlatformTransactionManager bean。如果在测试类的ApplicationContext中有多个PlatformTransactionManager实例，则可以通过@Transactional（“myTxMgr”）或@Transactional（transactionManager =“myTxMgr”）声明*qualifier* ，或者可以通过@Configuration类来实现TransactionManagementConfigurer。有关用于在测试类的ApplicationContext中查找事务管理器的算法的详细信息，请参阅TestContextTransactionUtils.retrieveTransactionManager（）的javadocs

#### 演示所有与事务相关的注解

以下基于JUnit 4的示例展示了一个虚构的集成测试场景，突出显示所有与事务相关的注释。这个例子不是为了演示这些注解的最佳实践，而是为了演示如何使用这些注释。有关更多信息和配置示例，请参阅 [annotation support](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#integration-testing-annotations)部分。  [Transaction management for `@Sql`](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-executing-sql-declaratively-tx)包含另一个使用@Sql的示例，用于执行声明性SQL脚本，并使用默认的事务回滚语义。

```java
@RunWith(SpringRunner.class)
@ContextConfiguration
@Transactional(transactionManager = "txMgr")
@Commit
public class FictitiousTransactionalTest {

    @BeforeTransaction
    void verifyInitialDatabaseState() {
        // logic to verify the initial state before a transaction is started
    }

    @Before
    public void setUpTestDataWithinTransaction() {
        // set up test data within the transaction
    }

    @Test
    // overrides the class-level @Commit setting
    @Rollback
    public void modifyDatabaseWithinTransaction() {
        // logic which uses the test data and modifies database state
    }

    @After
    public void tearDownWithinTransaction() {
        // execute "tear down" logic within the transaction
    }

    @AfterTransaction
    void verifyFinalDatabaseState() {
        // logic to verify the final state after transaction has rolled back
    }

}
```

```java
测试ORM代码时避免误报
当您测试操作Hibernate会话状态或JPA持久性上下文状态的应用程序代码时，请确保在执行该代码的测试方法中刷新基础工作单元。如果无法刷新底层的工作单元，可能会产生误报：您的测试可能会通过，但是相同的代码会在实时生产环境中引发异常。在以下基于Hibernate的示例测试用例中，一个方法演示了一个误报，另一个方法正确地公开了刷新会话的结果。请注意，这适用于使用基于内存的工作单元的任何ORM框架。
// ...

@Autowired
SessionFactory sessionFactory;

@Transactional
@Test // no expected exception!
public void falsePositive() {
    updateEntityInHibernateSession();
    // False positive: an exception will be thrown once the Hibernate
    // Session is finally flushed (i.e., in production code)
}

@Transactional
@Test(expected = ...)
public void updateWithSessionFlush() {
    updateEntityInHibernateSession();
    // Manual flush is required to avoid false positive in test
    sessionFactory.getCurrentSession().flush();
}

// ...

Or for JPA:
// ...

@PersistenceContext
EntityManager entityManager;

@Transactional
@Test // no expected exception!
public void falsePositive() {
    updateEntityInJpaPersistenceContext();
    // False positive: an exception will be thrown once the JPA
    // EntityManager is finally flushed (i.e., in production code)
}

@Transactional
@Test(expected = ...)
public void updateWithEntityManagerFlush() {
    updateEntityInJpaPersistenceContext();
    // Manual flush is required to avoid false positive in test
    entityManager.flush();
}

// ...
```

### 3.5.10  TestContext框架支持类

#### Spring JUnit 4 Runner

Spring TestContext框架通过自定义runner（JUnit 4.12或更高版本支持）提供了与JUnit 4的完全集成。通过使用@RunWith（SpringJUnit4ClassRunner.class）或较短的@RunWith（SpringRunner.class）变体注解测试类，开发人员可以实现标准的基于JUnit 4的单元和集成测试，同时获得TestContext框架的好处，例如支持加载应用程序上下文，测试实例的依赖注入，测试方法事务执行等等。如果你想使用Spring TestContext框架和JUnit 4的Parameterized runner或第三方runner（如MockitoJUnitRunner），你可以选择使用[Spring’s support for JUnit rules](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-junit4-rules) 。

以下代码片段显示了配置测试类以使用自定义Spring Runner运行的最少配置。 @TestExecutionListeners配置了一个空列表，以禁用默认监听器，否则将需要通过@ContextConfiguration配置一个ApplicationContext。

```java
@RunWith(SpringRunner.class)
@TestExecutionListeners({})
public class SimpleTest {

    @Test
    public void testMethod() {
        // execute test logic...
    }
}
```

#### Spring JUnit 4 Rules

org.springframework.test.context.junit4.rules包提供了以下JUnit 4  rules(基于JUnit 4.12或更高版本）。

- `SpringClassRule`
- `SpringMethodRule`

SpringClassRule是一个支持Spring TestContext框架的class-level功能的JUnit TestRule;而SpringMethodRule是一个支持Spring TestContext框架的实例级别和方法级别的功能的JUnit MethodRule。

与SpringRunner相比，Spring基于rule的JUnit支持具有独立于任何org.junit.runner.Runner实现的优点，因此可以与现有的替代runners（例如JUnit 4的Parameterized runner或第三方runner MockitoJUnitRunner)结合使用。

为了支持TestContext框架的全部功能，SpringClassRule必须与SpringMethodRule结合使用。以下示例演示了在集成测试中声明这些rule的正确方法。

```java
// Optionally specify a non-Spring Runner via @RunWith(...)
@ContextConfiguration
public class IntegrationTest {

    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Test
    public void testMethod() {
        // execute test logic...
    }
}
```

#### JUnit 4 support classes

org.springframework.test.context.junit4包为基于JUnit 4的测试用例（在JUnit 4.12或更高版本上支持）提供了以下支持类。

- `AbstractJUnit4SpringContextTests`
- `AbstractTransactionalJUnit4SpringContextTests`

AbstractJUnit4SpringContextTests是一个抽象的基础测试类，它集成了Spring TestContext框架和在JUnit 4环境中支持测试的特定ApplicationContext。继承AbstractJUnit4SpringContextTests时，可以访问protected applicationContext实例变量，该变量可用于执行显式bean look up或测试整个上下文的状态。

AbstractTransactionalJUnit4SpringContextTests是AbstractJUnit4SpringContextTests的抽象事务扩展，为JDBC访问添加了一些便利功能。这个类需要在ApplicationContext中定义一个javax.sql.DataSource bean和一个PlatformTransactionManager bean。扩展AbstractTransactionalJUnit4SpringContextTests时，您可以访问protected jdbcTemplate实例变量，该实例变量可用于执行SQL语句来查询数据库。在执行数据库相关的应用程序代码之前和之后，可以使用这种查询来确认数据库状态，并确保这些查询在与应用程序代码在相同的事务范围内运行。当与ORM工具一起使用时，一定要避免 [false positives](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-tx-false-positives)。如[JDBC Testing Support](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#integration-testing-support-jdbc)中所述，AbstractTransactionalJUnit4SpringContextTests还提供了使用上述jdbcTemplate委托JdbcTestUtils中的方法的便捷方法。此外，AbstractTransactionalJUnit4SpringContextTests提供了一个executeSqlScript（..）方法，用于对配置的DataSource执行SQL脚本。

这些类是扩展的一个方便。如果您不希望测试类绑定到特定于Spring的类层次结构，则可以使用@RunWith（SpringRunner.class）或 [Spring’s JUnit rules](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-junit4-rules)来配置自定义测试类。

#### SpringExtension for JUnit Jupiter

Spring TestContext Framework提供了与JUnit 5中引入的JUnit Jupiter测试框架的完全集成。通过使用@ExtendWith（SpringExtension.class）注释测试类，开发人员可以实现标准的基于JUnit Jupiter的单元和集成测试，同时获得TestContext的好处框架，如支持加载应用程序上下文，测试实例的依赖注入，测试方法的事务执行等等。

此外，由于JUnit Jupiter中丰富的扩展API，Spring能够提供以下功能还有超出Spring支持的JUnit 4和TestNG的功能集。

- Dependency injection for test constructors, test methods, and test lifecycle callback methods
  - See [Dependency Injection with the SpringExtension](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#testcontext-junit-jupiter-di) for further details.
- Powerful support for [*conditional test execution*](http://junit.org/junit5/docs/current/user-guide/#extensions-conditions) based on SpEL expressions, environment variables, system properties, etc.
  - See the documentation for `@EnabledIf` and `@DisabledIf` in [Spring JUnit Jupiter Testing Annotations](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#integration-testing-annotations-junit-jupiter) for further details and examples.
- Custom *composed annotations* that combine annotations from Spring **and** JUnit Jupiter.
  - See the `@TransactionalDevTestConfig` and `@TransactionalIntegrationTest` examples in [Meta-Annotation Support for Testing](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#integration-testing-annotations-meta) for further details.

下面的代码清单演示了如何配置一个测试与@ContextConfiguration一起使用SpringExtension。

```java
// Instructs JUnit Jupiter to extend the test with Spring support.
@ExtendWith(SpringExtension.class)
// Instructs Spring to load an ApplicationContext from TestConfig.class
@ContextConfiguration(classes = TestConfig.class)
class SimpleTests {

    @Test
    void testMethod() {
        // execute test logic...
    }
}
```

由于JUnit 5中的注释也可以用作元注释，因此Spring能够提供@SpringJUnitConfig和@SpringJUnitWebConfig注释，以简化测试ApplicationContext和JUnit Jupiter的配置。

例如，下面的示例使用@SpringJUnitConfig来减少前面示例中使用的配置数量。

```java
// Instructs Spring to register the SpringExtension with JUnit
// Jupiter and load an ApplicationContext from TestConfig.class
@SpringJUnitConfig(TestConfig.class)
class SimpleTests {

    @Test
    void testMethod() {
        // execute test logic...
    }
}
```

同样，下面的示例使用@SpringJUnitWebConfig创建一个WebApplicationContext，以便与JUnit Jupiter一起使用。

```java
// Instructs Spring to register the SpringExtension with JUnit
// Jupiter and load a WebApplicationContext from TestWebConfig.class
@SpringJUnitWebConfig(TestWebConfig.class)
class SimpleWebTests {

    @Test
    void testMethod() {
        // execute test logic...
    }
}
```

有关更多详细信息，请参阅 [Spring JUnit Jupiter Testing Annotations](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#integration-testing-annotations-junit-jupiter)中的@SpringJUnitConfig和@SpringJUnitWebConfig的文档。

#### Dependency Injection with the SpringExtension

SpringExtension从JUnit Jupiter实现了[`ParameterResolver`](http://junit.org/junit5/docs/current/user-guide/#extensions-parameter-resolution) 扩展API，允许Spring为测试类的构造函数，测试方法和测试生命周期回调方法提供依赖注入。

具体来说，SpringExtension能够将测试的ApplicationContext的依赖注入到用@BeforeAll，@AfterAll，@BeforeEach，@AfterEach，@ Test，@RepeatedTest，@ParameterizedTest等注释的测试构造函数和方法中。

##### Constructor Injection

如果JUnit Jupiter测试类的构造函数中的参数类型为ApplicationContext（或其子类型），或者使用@Autowired，@Qualifier或@Value注释或元注释，那么Spring将为该特定参数与测试的ApplicationContext中相应的bean相关联。如果所有参数都应该由Spring提供，那么也可以使用@Autowired直接对测试构造函数进行注释。

```
如果测试类的构造函数本身是用@Autowired注解的，那么Spring将负责解析构造函数中的所有参数。因此，将不会用到注册到JUnit Jupiter的ParameterResolver解析这样的构造函数的参数。
```

在下面的例子中，Spring将从TestConfig.class加载的ApplicationContext中注入OrderService bean到OrderServiceIntegrationTests构造函数中。还要注意，这个特性允许测试依赖是final的，因此是不可变的。

```java
@SpringJUnitConfig(TestConfig.class)
class OrderServiceIntegrationTests {

    private final OrderService orderService;

    @Autowired
    OrderServiceIntegrationTests(OrderService orderService) {
        this.orderService = orderService.
    }

    // tests that use the injected OrderService
}
```

##### Method Injection

如果JUnit Jupiter测试类的方法或生命周期回调方法中的参数类型为ApplicationContext（或其子类型），或者使用@Autowired，@Qualifier或@Value进行注释或元注释，则Spring将为该特定的参数注入测试的ApplicationContext中的bean。

在以下示例中，Spring将从TestConfig.class加载的ApplicationContext中将OrderService注入到deleteOrder（）测试方法中。

```java
@SpringJUnitConfig(TestConfig.class)
class OrderServiceIntegrationTests {

    @Test
    void deleteOrder(@Autowired OrderService orderService) {
        // use orderService from the test's ApplicationContext
    }
}
```

由于JUnit Jupiter中的ParameterResolver支持的健壮性，所以也可以将多个依赖项注入单个方法，不仅从Spring中，而且还从JUnit Jupiter本身或其他第三方扩展中注入。

以下示例演示如何让Spring和JUnit Jupiter同时将依赖项注入到placeOrderRepeatedly（）测试方法中。请注意，使用JUnit Jupiter中的@RepeatedTest可以使测试方法获得对RepetitionInfo的访问权限。

```java
@SpringJUnitConfig(TestConfig.class)
class OrderServiceIntegrationTests {

    @RepeatedTest(10)
    void placeOrderRepeatedly(RepetitionInfo repetitionInfo,
            @Autowired OrderService orderService) {

        // use orderService from the test's ApplicationContext
        // and repetitionInfo from JUnit Jupiter
    }
}
```

#### TestNG support classes

略

## 3.6. Spring MVC Test Framework

Spring MVC测试框架为测试Spring MVC代码提供了一流的支持，通过使用可以与JUnit，TestNG或任何其他测试框架一起使用的API提供这种支持。这个框架建立在spring-test模块的[Servlet API mock objects](https://docs.spring.io/spring-framework/docs/5.0.2.RELEASE/javadoc-api/org/springframework/mock/web/package-summary.html) 上，因此不需要使用运行的Servlet容器。它使用DispatcherServlet来提供完整的Spring MVC运行时行为，并支持使用TestContext框架加载实际的Spring配置还支持独立模式，在独立模式下，可以手动实例化控制器并一次一个地进行测试。

Spring MVC Test还为测试使用RestTemplate的代码提供了客户端支持。客户端测试模拟服务器响应，也不使用正在运行的服务器。

Spring Boot提供了一个选项，可以编写包含正在运行的服务器的完整的end-to-end集成测试。如果这是您的目标，请查看 [Spring Boot reference page](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html#boot-features-testing-spring-boot-applications)。有关out-of-container and end-to-end集成测试之间差异的更多信息，请参阅 [Differences between Out-of-Container and End-to-End Integration Tests](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#spring-mvc-test-vs-end-to-end-integration-tests)。

### 3.6.1 服务器端测试

使用JUnit或TestNG为Spring MVC controller编写简单的单元测试非常简单：只需实例化controller，为其注入mocked或stubbed 的依赖关系，并根据需要调用传递MockHttpServletRequest，MockHttpServletResponse等的方法。但是，在编写这样的单元测试时，还有很多不能测试到的东西：例如request mappings, data binding, type conversion, validation等等。此外，其他controller方法（如@InitBinder，@ModelAttribute和@ExceptionHandler）也可能作为request处理生命周期的一部分被调用。

Spring MVC Test的目标是通过处理request并通过实际的DispatcherServlet生成响应，为测试controller提供一种有效的方法。

Spring MVC测试建立在spring-test模块中的 ["mock" implementations of the Servlet API](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#mock-objects-servlet)上 。这允许执行请求并生成响应，而不需要在Servlet容器中运行。在大多数情况下，所有的东西都应该像运行时一样工作，并且有一些值得注意的例外情况，正如在 [Differences between Out-of-Container and End-to-End Integration Tests](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#spring-mvc-test-vs-end-to-end-integration-tests).中所解释的那样。下面是一个基于JUnit Jupiter的使用Spring MVC测试的例子：

```java
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitWebConfig(locations = "test-servlet-context.xml")
class ExampleTests {

    private MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void getAccount() throws Exception {
        this.mockMvc.perform(get("/accounts/1")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.name").value("Lee"));
    }
}
```

上面的测试依赖于TestContext框架的WebApplicationContext支持，用于从位于与测试类相同的包中的XML配置文件加载Spring配置，但也支持基于Java和Groovy的配置。请参阅这些[sample tests](https://github.com/spring-projects/spring-framework/tree/master/spring-test/src/test/java/org/springframework/test/web/servlet/samples/context)。

MockMvc实例用于对“/ accounts / 1”执行GET请求，并验证结果响应的状态为200、内容类型为“application / json”、响应主体具有名为“name”的JSON属性值“Lee”。 jsonPath语法是通过 [JsonPath project](https://github.com/jayway/JsonPath)项目支持的。还有很多其他选项可用于验证执行请求的结果,将在下面讨论。

#### 静态导入

上面例子中的通用API需要一些静态导入，例如MockMvcRequestBuilders。*，MockMvcResultMatchers。*和MockMvcBuilders。找到这些类的简单方法是搜索匹配“MockMvc *”的类型。如果使用Eclipse，请确保将它们添加为Eclipse preferences 中的“favorite static members”，设置路径是Java Preferences→Content Assist→Favorites。这将允许在输入静态方法名称的第一个字符后使用内容提示。其他IDE（例如IntelliJ）可能不需要任何其他配置。只需检查静态成员的代码提示支持。

#### 创建MockMvc的两种方式

创建MockMvc实例有两个主要方式。首先是通过TestContext框架加载Spring MVC配置，该框架加载Spring配置，并将WebApplicationContext注入到测试中以用于构建MockMvc实例：

```java
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration("my-servlet-context.xml")
public class MyWebTests {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    // ...

}
```

第二种方式是简单地创建一个controller实例而不加载Spring配置。相对于MVC JavaConfig或MVC命名空间而言，基本的默认配置是自动创建的，并且可以在一定程度上进行自定义：

```java
public class MyWebTests {

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new AccountController()).build();
    }

    // ...

}
```

##### 你应该使用哪个设置选项？

“webAppContextSetup”会加载实际的Spring MVC配置，从而产生更完整的集成测试。由于TestContext框架缓存了加载的Spring配置，因此即使在您的测试套件中引入了更多的测试，也可以帮助保持测试的快速运行。此外，您可以通过Spring配置将mock service注入到controller中，以便只专注于测试Web层。下面是一个使用Mockito声明mocked service的例子：

```java
<bean id="accountService" class="org.mockito.Mockito" factory-method="mock">
    <constructor-arg value="org.example.AccountService"/>
</bean>
```

然后，您可以将mocked service注入测试，以便设置和验证期望值：

```java
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration("test-servlet-context.xml")
public class AccountTests {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    // ...

}
```

就像任何“集成 vs单元测试”的辩论一样，没有正确与错误的答案。但是，使用“standaloneSetup”确实意味着需要额外的“webAppContextSetup”测试来验证您的Spring MVC配置。或者，您可以选择使用“webAppContextSetup”编写所有测试，以便始终根据实际的Spring MVC配置进行测试。

#### Setup Features

无论您使用哪个MockMvc  builder，所有MockMvcBuilder实现都提供了一些常用和非常有用的功能。例如，您可以为所有请求声明Accept header ，并期望所有响应中的状态为200以及Content-Type header ，如下所示：

```java
// static import of MockMvcBuilders.standaloneSetup

MockMVc mockMvc = standaloneSetup(new MusicController())
        .defaultRequest(get("/").accept(MediaType.APPLICATION_JSON))
        .alwaysExpect(status().isOk())
        .alwaysExpect(content().contentType("application/json;charset=UTF-8"))
        .build();
```

另外，第三方框架（和应用程序）可能会pre-package setup指令，例如通过MockMvcConfigurer预设置的指令 。 Spring框架有一个这样的内置实现，有助于跨请求保存和重用HTTP session。它可以使用如下：

```java
// static import of SharedHttpSessionConfigurer.sharedHttpSession

MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
        .apply(sharedHttpSession())
        .build();

// Use mockMvc to perform requests...
```

有关所有MockMvc构建器功能的列表，请参阅ConfigurableMockMvcBuilder，或者使用IDE来浏览可用的选项。

#### 执行请求

使用任何HTTP method执行请求很容易：

```java
mockMvc.perform(post("/hotels/{id}", 42).accept(MediaType.APPLICATION_JSON));
```

你也可以执行文件上传请求,内部使用的是MockMultipartHttpServletRequest，这样就没有实际的multipart 请求解析，而是你必须设置它：

```java
mockMvc.perform(multipart("/doc").file("a1", "ABC".getBytes("UTF-8")));
```

您可以在URI模板样式中指定查询参数：

```java
mockMvc.perform(get("/hotels?foo={foo}", "bar"));
```

或者你可以添加表示表单参数查询的Servlet请求参数：

```java
mockMvc.perform(get("/hotels").param("foo", "bar"));
```

如果应用程序代码依赖于Servlet请求参数，并且不会显式检查查询字符串（最常见的情况），那么使用哪个方式并不重要。请记住，URI模板提供的查询参数将被解码，而通过param（...）方法提供的请求参数已经被解码。

在大多数情况下，最好从请求URI中省略context路径和Servlet路径。如果您必须使用完整请求URI进行测试，请确保分别设置contextPath和servletPath，以便请求映射可以正常工作：

```java
mockMvc.perform(get("/app/main/hotels/{id}").contextPath("/app").servletPath("/main"))
```

看上面的例子，设置contextPath和servletPath与每个执行的请求将是麻烦的。相反，您可以设置默认的请求属性：

```java
public class MyWebTests {

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = standaloneSetup(new AccountController())
            .defaultRequest(get("/")
            .contextPath("/app").servletPath("/main")
            .accept(MediaType.APPLICATION_JSON).build();
    }
```

上述属性将影响通过MockMvc实例执行的每个请求。如果在给定的请求中也指定了相同的属性，它将覆盖默认值。这就是为什么默认请求中的HTTP方法和URI不重要，因为它们必须在每个请求中指定。

#### 定义期望

期望值可以通过在执行请求后附加一个或多个.andExpect（..）来定义：

```java
mockMvc.perform(get("/accounts/1")).andExpect(status().isOk());
```

MockMvcResultMatchers提供了许多期望，其中一些进一步嵌套更详细的期望。

期望分为两大类。第一类断言验证响应的属性：例如，response status, headers, and content。这些是最重要的结果。

第二类期望超出了response的信息。这些断言允许检查Spring MVC特定的方面，比如哪个控制器方法处理请求，是否引发和处理了异常，model的内容是什么，选择了什么视图(view)，添加了哪些flash属性等等。他们还允许检查Servlet特定的方面，如请求和会话属性。

以下测试演示如何断言绑定或验证失败：

```java
mockMvc.perform(post("/persons"))
    .andExpect(status().isOk())
    .andExpect(model().attributeHasErrors("person"));
```

在编写测试时，很多时候转储执行请求的结果是很有用的。这可以如下完成，其中print（）是从MockMvcResultHandlers静态导入的：

```java
mockMvc.perform(post("/persons"))
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(model().attributeHasErrors("person"));
```

只要请求处理不会导致未处理的异常，print（）方法就会将所有可用的结果数据打印到System.out。 Spring Framework 4.2引入了log（）方法和print（）方法的两个额外变体，一个接受OutputStream，另一个接受Writer。例如，调用print（System.err）将打印结果数据到System.err;而调用print（myWriter）将把结果数据打印到一个自定义writer。如果您希望将结果数据记录下来而不是打印出来，只需调用log（）方法，该方法会将结果数据记录为org.springframework.test.web.servlet.result日志记录目录下的单个DEBUG消息。

在某些情况下，您可能希望直接访问结果否则就无法验证。这可以通过在所有其他期望之后追加.andReturn（）来实现：

```java
MvcResult mvcResult = mockMvc.perform(post("/persons")).andExpect(status().isOk()).andReturn();
// ...
```

如果所有测试都重复相同的期望，那么在构建MockMvc实例时，您可以设置一个共同期望：

```java
standaloneSetup(new SimpleController())
    .alwaysExpect(status().isOk())
    .alwaysExpect(content().contentType("application/json;charset=UTF-8"))
    .build()
```

请注意，通常的期望总是被应用，并且不能被重写，除非另外创建一个单独的MockMvc实例。

当JSON响应内容包含使用 [Spring HATEOAS](https://github.com/spring-projects/spring-hateoas)创建的超媒体链接时，可以使用JsonPath表达式验证生成的链接：

```java
mockMvc.perform(get("/people").accept(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$.links[?(@.rel == 'self')].href").value("http://localhost:8080/people"));
```

当XML响应内容包含使用 [Spring HATEOAS](https://github.com/spring-projects/spring-hateoas)创建的超媒体链接时，可以使用XPath表达式验证生成的链接：

```java
Map<String, String> ns = Collections.singletonMap("ns", "http://www.w3.org/2005/Atom");
mockMvc.perform(get("/handle").accept(MediaType.APPLICATION_XML))
    .andExpect(xpath("/person/ns:link[@rel='self']/@href", ns).string("http://localhost:8080/people"));
```

#### 过滤器注册

设置MockMvc实例时，可以注册一个或多个Servlet过滤器实例：

```java
mockMvc = standaloneSetup(new PersonController()).addFilters(new CharacterEncodingFilter()).build();
```

已注册的过滤器将通过来自spring-test的MockFilterChain调用，最后一个过滤器将会把请求委托给DispatcherServlet。

#### Out-of-Container and End-to-End集成测试之间的差异

正如之前提到的，Spring MVC Test是建立在spring-test模块的Servlet API mock对象之上的，并且不使用正在运行的Servlet容器。因此，与运行实际客户端和服务器的完整end-to-end集成测试相比，存在一些重要差异。

考虑这种差异的最简单的方法是从一个空的MockHttpServletRequest开始。你添加什么，请求就会有相应的特性。可能令你感到意外的事情是，默认情况下请求没有no `jsessionid` cookie, no forwarding, error, or async dispatches(调度)，因此没有实际的JSP渲染。相反，“forwarded”和“redirected”的URL被保存在MockHttpServletResponse中，并且可以用期望断言。

这意味着如果您使用的是JSP，您可以验证请求被转发到的JSP页面，但不会有任何HTML呈现。换句话说，JSP将不会被调用。但是请注意，所有其他不依赖于转发的呈现技术（如Thymeleaf和Freemarker）都会按照预期将HTML呈现给响应主体。通过@ResponseBody方法呈现JSON，XML和其他格式也是如此。

或者，您可以考虑Spring Boot中通过@WebIntegrationTest提供的完整的端到端集成测试支持。请参阅[Spring Boot reference](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html#boot-features-testing-spring-boot-applications)。

每种实现都有优点和缺点。 Spring MVC Test提供的操作在经典单元测试到完整集成测试的范围内是不同的。可以肯定的是，Spring MVC Test中没有任何操作属于经典单元测试的范畴，但它们有点接近。例如，可以通过向控制器注入模拟服务来把Web层和service层隔离，在这种情况下，您只是通过DispatcherServlet测试Web层，但使用实际的Spring配置，就像您可能独立于数据访问层以上的层而测试数据访问层一样。或者，您可以一次使用专注于一个控制器的独立设置，并手动提供使其工作所需的配置。

使用Spring MVC Test的另一个重要的区别是概念上这样的测试是在服务器端的内部，所以如果使用HandlerExceptionResolver处理了异常，你可以检查使用了什么handler ，model的内容是什么，err，等等。这意味着编写期望变得更容易，因为服务器不像通过实际的HTTP客户端进行测试时那样是黑盒子。这通常是经典单元测试的一个优点，即编写，推理和调试起来更容易，但不能代替完全集成测试的需要。同时重要的是不要忽视响应是最重要的检查的事实。总而言之，即使在同一个项目中，也有多种风格和测试策略。

#### 更多的服务器端测试示例

这个框架自己的测试包括许多旨在演示如何使用Spring MVC测试的 [many sample tests](https://github.com/spring-projects/spring-framework/tree/master/spring-test/src/test/java/org/springframework/test/web/servlet/samples)。浏览这些例子以获得更多的想法。另外[spring-mvc-showcase](https://github.com/spring-projects/spring-mvc-showcase) 基于Spring MVC测试框架提供了完整的测试覆盖。

### 3.6.2. HtmlUnit Integration

Spring提供了 [MockMvc](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#spring-mvc-test-server) and [HtmlUnit](http://htmlunit.sourceforge.net/)之间的集成。这简化了基于HTML的视图时执行end to end测试。这种集成使开发人员能够：

- Easily test HTML pages using tools such as [HtmlUnit](http://htmlunit.sourceforge.net/), [WebDriver](http://seleniumhq.org/projects/webdriver/), & [Geb](http://www.gebish.org/manual/current/testing.html#spock_junit__testng) without the need to deploy to a Servlet container
- Test JavaScript within pages
- Optionally test using mock services to speed up testing
- Share logic between in-container end-to-end tests and out-of-container integration tests

```
MockMvc使用不依赖于Servlet容器（例如Thymeleaf，FreeMarker等）的模板技术，但由于它们依赖于Servlet容器，因此MockMvc不适用于使用JSP。
```

#### Why HtmlUnit Integration?

想到的最明显的问题是，“为什么我需要这个？”。通过探索一个非常基本的示例应用程序可以找到答案。假设您有一个支持Message对象的CRUD操作的Spring MVC Web应用程序。该应用程序还支持所有Message的分页。你将如何去测试它？

使用Spring MVC测试，我们可以轻松测试是否可以创建Message。

```java
MockHttpServletRequestBuilder createMessage = post("/messages/")
        .param("summary", "Spring Rocks")
        .param("text", "In case you didn't know, Spring Rocks!");

mockMvc.perform(createMessage)
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/messages/123"));
```

如果我们想测试用于创建Message的表单视图呢？例如，假设我们的表单看起来像下面的代码片段：

```html
<form id="messageForm" action="/messages/" method="post">
    <div class="pull-right"><a href="/messages/">Messages</a></div>

    <label for="summary">Summary</label>
    <input type="text" class="required" id="summary" name="summary" value="" />

    <label for="text">Message</label>
    <textarea id="text" name="text"></textarea>

    <div class="form-actions">
        <input type="submit" value="Create" />
    </div>
</form>
```

我们如何确保我们的表格能够产生正确的请求来创建新的Message呢？天真的尝试看起来像这样：

```java
mockMvc.perform(get("/messages/form"))
        .andExpect(xpath("//input[@name='summary']").exists())
        .andExpect(xpath("//textarea[@name='text']").exists());
```

这个测试有一些明显的缺点。如果我们更新我们的控制器来使用参数`message` 而不是text，即使HTML表单与控制器不同步，我们的表单测试也会继续通过。要解决这个问题我们可以结合我们的两个测试

```java
String summaryParamName = "summary";
String textParamName = "text";
mockMvc.perform(get("/messages/form"))
        .andExpect(xpath("//input[@name='" + summaryParamName + "']").exists())
        .andExpect(xpath("//textarea[@name='" + textParamName + "']").exists());

MockHttpServletRequestBuilder createMessage = post("/messages/")
        .param(summaryParamName, "Spring Rocks")
        .param(textParamName, "In case you didn't know, Spring Rocks!");

mockMvc.perform(createMessage)
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/messages/123"));
```

这会降低我们测试错误的风险，但是仍然存在一些问题。

- 如果我们的网页上有多个表单呢？无可否认，我们可以更新我们的xpath表达式，但是我们考虑的因素越多，它们就越复杂（字段是否是正确的类型？是否启用了字段？等等）。
- 另一个问题是，我们正在做我们期望的工作的两倍。我们必须首先验证视图，然后我们提交与我们刚刚验证的参数相同的视图。理想情况下，这可以一次完成。
- 最后，还有一些我们仍然无法解释的东西。例如，如果表单有我们希望测试的JavaScript验证呢？

总体问题是测试一个网页不只是涉及一个单一的交互。相反，它是用户如何与网页交互以及该网页如何与其他资源交互的组合。例如，表单视图的结果被用作用户创建消息的输入。另外，我们的表单视图可能会利用影响页面行为的额外资源，例如JavaScript验证。

##### Integration testing to the rescue?

为了解决上述问题，我们可以执行end to end的集成测试，但是这有一些明显的缺点。考虑测试允许我们分页查看消息的视图。我们可能需要以下测试。

- 我们的页面是否显示通知给用户，用于指示当消息为空时没有结果可用的情况？
- 我们的页面是否正确显示一条消息？
- 我们的页面是否正确支持分页？

为了建立这些测试，我们需要确保我们的数据库中包含正确的消息。这导致了一些额外的挑战。

- 确保在数据库中有正确的消息可能是乏味的;考虑外键约束。
- 由于每个测试都需要确保数据库处于正确的状态，所以测试可能变慢。
- 由于我们的数据库需要处于特定状态，所以我们不能并行运行测试。
- 对诸如自动生成的ID，时间戳等进行断言可能很困难。

这些挑战并不意味着我们应该放弃全面的端到端集成测试。相反，我们可以通过重构我们的详细测试来减少端到端集成测试的数量，以便使用更快，更可靠且无副作用的mock service。然后，我们可以实施少量真正的端到端集成测试，验证简单的工作流程，确保一切正常工作。

##### Enter HtmlUnit Integration

那么我们如何才能在测试页面的交互和测试套件中保持良好的性能之间取得平衡呢？答案是：“通过整合MockMvc与HtmlUnit”。

##### HtmlUnit Integration Options

有很多方法可以将MockMvc与HtmlUnit集成:

- [MockMvc and HtmlUnit](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#spring-mvc-test-server-htmlunit-mah):如果要使用原始HtmlUnit库，请使用此实现。
- [MockMvc and WebDriver](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#spring-mvc-test-server-htmlunit-webdriver):使用此实现可以简化集成测试和end to end测试之间的开发和重用代码。
- [MockMvc and Geb](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#spring-mvc-test-server-htmlunit-geb):如果您想要使用Groovy进行测试，简化开发并在集成测试和端到端测试之间重用代码，请使用此选项。

#### MockMvc and HtmlUnit

本节介绍如何整合MockMvc和HtmlUnit。如果要使用原始HtmlUnit库，请使用此实现。

##### MockMvc and HtmlUnit Setup

首先，确保你已经包含了一个测试依赖项net.sourceforge.html：htmlunit。为了在Apache HttpComponents 4.5中使用HtmlUnit，您将需要使用HtmlUnit 2.18或更高版本。

我们可以使用MockMvcWebClientBuilder轻松创建一个与MockMvc集成的HtmlUnit WebClient，如下所示。

```java
@Autowired
WebApplicationContext context;

WebClient webClient;

@Before
public void setup() {
    webClient = MockMvcWebClientBuilder
            .webAppContextSetup(context)
            .build();
}
```

这是使用MockMvcWebClientBuilder的一个简单例子。有关高级用法，请参阅 [Advanced MockMvcWebClientBuilder](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#spring-mvc-test-server-htmlunit-mah-advanced-builder)

这将确保任何引用localhost作为server的URL将被引导到我们的MockMvc实例，而不需要真正的HTTP连接。任何其他的URL将会像平常一样使用网络连接来请求。这使我们能够轻松测试CDN的使用。

##### MockMvc和HtmlUnit使用

现在我们可以像平常一样使用HtmlUnit，但是不需要将我们的应用程序部署到Servlet容器。例如，我们可以请求视图创建一个消息，如下所示。

```java
HtmlPage createMsgFormPage = webClient.getPage("http://localhost/messages/form");
```

默认的上下文路径是“”。或者，我们可以指定上下文路径，如 [Advanced MockMvcWebClientBuilder](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#spring-mvc-test-server-htmlunit-mah-advanced-builder).中所示。

一旦我们有了HtmlPage的引用，我们就可以填写表单并提交它来创建一个消息。

```java
HtmlForm form = createMsgFormPage.getHtmlElementById("messageForm");
HtmlTextInput summaryInput = createMsgFormPage.getHtmlElementById("summary");
summaryInput.setValueAttribute("Spring Rocks");
HtmlTextArea textInput = createMsgFormPage.getHtmlElementById("text");
textInput.setText("In case you didn't know, Spring Rocks!");
HtmlSubmitInput submit = form.getOneHtmlElementByAttribute("input", "type", "submit");
HtmlPage newMessagePage = submit.click();
```

最后，我们可以验证新消息是否成功创建。以下断言使用[AssertJ](https://joel-costigliola.github.io/assertj/) 库。

```java
assertThat(newMessagePage.getUrl().toString()).endsWith("/messages/123");
String id = newMessagePage.getHtmlElementById("id").getTextContent();
assertThat(id).isEqualTo("123");
String summary = newMessagePage.getHtmlElementById("summary").getTextContent();
assertThat(summary).isEqualTo("Spring Rocks");
String text = newMessagePage.getHtmlElementById("text").getTextContent();
assertThat(text).isEqualTo("In case you didn't know, Spring Rocks!");
```

这可以通过多种方式改进我们的[MockMvc test](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#spring-mvc-test-server-htmlunit-mock-mvc-test)。首先，我们不再需要明确地验证我们的表单，然后创建一个类似于表单的请求。相反，我们请求表单，填写并提交，从而大大减少了开销。

另一个重要的因素是[HtmlUnit uses the Mozilla Rhino engine](http://htmlunit.sourceforge.net/javascript.html)来评估JavaScript。这意味着我们可以在我们的页面中测试JavaScript的行为！

有关使用HtmlUnit的更多信息，请参阅 [HtmlUnit documentation](http://htmlunit.sourceforge.net/gettingStarted.html) 

##### Advanced MockMvcWebClientBuilder

在目前为止的例子中，我们以最简单的方式使用MockMvcWebClientBuilder，构建基于Spring TestContext框架为我们加载的WebApplicationContext的WebClient。这个实现在这里重复。

```java
@Autowired
WebApplicationContext context;

WebClient webClient;

@Before
public void setup() {
    webClient = MockMvcWebClientBuilder
            .webAppContextSetup(context)
            .build();
}
```

我们也可以指定其他配置选项。

```java
WebClient webClient;

@Before
public void setup() {
    webClient = MockMvcWebClientBuilder
        // demonstrates applying a MockMvcConfigurer (Spring Security)
        .webAppContextSetup(context, springSecurity())
        // for illustration only - defaults to ""
        .contextPath("")
        // By default MockMvc is used for localhost only;
        // the following will use MockMvc for example.com and example.org as well
        .useMockMvcForHosts("example.com","example.org")
        .build();
}
```

作为替代，我们可以通过单独配置MockMvc实例并将其提供给MockMvcWebClientBuilder执行完全相同的设置，如下所示。

```java
MockMvc mockMvc = MockMvcBuilders
        .webAppContextSetup(context)
        .apply(springSecurity())
        .build();

webClient = MockMvcWebClientBuilder
        .mockMvcSetup(mockMvc)
        // for illustration only - defaults to ""
        .contextPath("")
        // By default MockMvc is used for localhost only;
        // the following will use MockMvc for example.com and example.org as well
        .useMockMvcForHosts("example.com","example.org")
        .build();
```

这是比较冗长的，但通过使用MockMvc实例构建WebClient，我们可以轻松掌握MockMvc的全部功能。

有关创建MockMvc实例的其他信息，请参阅[Setup Choices](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#spring-mvc-test-server-setup-options)。

#### MockMvc and WebDriver

略

#### MockMvc and Geb

略

### 3.6.3. Client-Side REST Tests

客户端测试可用于测试内部使用RestTemplate的代码。我们的想法是声明预期的请求并提供“存根”响应，以便您可以专注于单独测试代码，即不运行服务器。这里是一个例子：

```java
RestTemplate restTemplate = new RestTemplate();

MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplate).build();
mockServer.expect(requestTo("/greeting")).andRespond(withSuccess());

// Test code that uses the above RestTemplate ...

mockServer.verify();
```

在上面的示例中，Client-Side REST测试的核心类MockRestServiceServer使用自定义ClientHttpRequestFactory配置RestTemplate，该ClientHttpRequestFactory根据期望断言实际请求并返回“存根”响应。在这种情况下，我们希望请求“/ greeting”，并希望返回“text / plain”内容的200响应。我们可以根据需要定义额外的预期请求和存根响应。当预期的请求和存根响应被定义时，RestTemplate可以照常在客户端代码中使用。在测试结束时，可以使用mockServer.verify（）来验证所有期望是否已经满足。

默认情况下，请求按在预期中声明的顺序进行判断。您可以在构建服务器时设置ignoreExpectOrder选项，在这种情况下，将检查所有期望（按顺序）以找到给定请求的匹配项。这意味着请求可以以任何顺序进行。这里是一个例子：

```java
server = MockRestServiceServer.bindTo(restTemplate).ignoreExpectOrder(true).build();
```

即使默认的无序请求，每个请求也只允许执行一次。 expect方法提供了一个重载的变体，它接受指定计数范围的ExpectedCount参数，`once`, `manyTimes`, `max`, `min`,`between`，等等。这里是一个例子：

```java
RestTemplate restTemplate = new RestTemplate();

MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplate).build();
mockServer.expect(times(2), requestTo("/foo")).andRespond(withSuccess());
mockServer.expect(times(3), requestTo("/bar")).andRespond(withSuccess());

// ...

mockServer.verify();
```

请注意，如果ignoreExpectOrder未设置（默认），因此请求按预期声明的顺序，并且该顺序仅适用于任何预期的请求的第一个。例如，如果“/ foo”预期是2次，然后是“/ bar”3次，那么在第一个请求“/ bar”之前应该有一个“/ foo”的请求，但是后面的“/ foo”和“/bar”的请求就没有先后顺序了。

作为上述所有例子的替代方案，客户端测试支持还提供了ClientHttpRequestFactory实现，该实现可以配置到RestTemplate中以将其绑定到MockMvc实例。这样做允许使用实际的服务器端逻辑处理请求，但不运行服务器。这里是一个例子：

```java
MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
this.restTemplate = new RestTemplate(new MockMvcClientHttpRequestFactory(mockMvc));

// Test code that uses the above RestTemplate ...

mockServer.verify();
```

#### Static Imports

就像服务器端测试一样，客户端测试的通用API需要一些静态导入。这些通过搜索“MockRest *”很容易找到。 Eclipse用户应在Eclipse首选项下的Java → Editor → Content Assist → Favorites中将“MockRestRequestMatchers。*”和“MockRestResponseCreators。*”添加为“favorite static members”。这允许在输入静态方法名称的第一个字符后使用代码提醒。其他IDE（例如IntelliJ）可能不需要任何其他配置。只需检查静态成员的代码完成支持。

#### Further Examples of Client-side REST Tests

Spring MVC Test自己的测试包括客户端REST测试的[example tests](https://github.com/spring-projects/spring-framework/tree/master/spring-test/src/test/java/org/springframework/test/web/client/samples) 。

## 3.7. WebTestClient

WebTestClient是一个用于测试Web服务器的非阻塞的反应式的客户端。WebTestClient内部使用反应式的[WebClient](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/web-reactive.html#webflux-webclient) 执行请求，并提供流畅的API来验证响应。 WebTestClient可以通过HTTP连接连接到任何服务器。它也可以通过[mock request and response](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#mock-objects-web-reactive)直接绑定到WebFlux应用程序，而不需要HTTP服务器。

### 3.7.1. Setup

要创建WebTestClient，您必须选择几个服务器设置选项之一。实际上，您可以与WebFlux应用程序绑定或使用绝对URL连接到正在运行的服务器。

#### Bind to controller

使用此服务器设置一次测试一个@Controller：

```java
    client = WebTestClient.bindToController(new TestController()).build();
```

上面的例子将加载[WebFlux Java config](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/web-reactive.html#webflux-config) 并注册给定的控制器。最终的WebFlux应用程序将使用模拟请求和响应对象而不需要HTTP服务器的情况下进行测试。在builder上有更多的方法来定制默认的WebFlux Java配置。

#### Bind to RouterFunction

使用 [RouterFunction](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/web-reactive.html#webflux-fn)设置服务器：

```java
    RouterFunction<?> route = ...
    client = WebTestClient.bindToRouterFunction(route).build();
```

在内部，提供的配置将传递给RouterFunctions.toWebHandler。生成的WebFlux应用程序将使用模拟请求和响应对象而不需要HTTP服务器的情况下进行测试。

#### Bind to ApplicationContext

使用应用程序的Spring配置或其子集中设置一个服务器：

```java
    @RunWith(SpringRunner.class)
    @ContextConfiguration(classes = WebConfig.class) 
    public class MyTests {

        @Autowired
        private ApplicationContext context; 

        private WebTestClient client;

        @Before
        public void setUp() {
            client = WebTestClient.bindToApplicationContext(context).build(); 
        }
    }
```

在内部，提供的配置传递给WebHttpHandlerBuilder来设置请求处理链，请参阅[WebHandler API](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/web-reactive.html#webflux-web-handler-api) 获取更多细节。生成的WebFlux应用程序将使用模拟请求和响应对象而不需要HTTP服务器的情况下进行测试。

#### Bind to server

此服务器设置选项允许您连接到正在运行的服务器：

```java
    client = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
```

#### Client builder

除上述服务器设置选项外，还可以配置客户端选项，包括base URL, default headers, client filters等。在bindToServer之后，这些选项随时可用。对于所有其他设置方式，您需要使用configureClient（）从server设置转换到客户端配置，如下所示：

```java
    client = WebTestClient.bindToController(new TestController())
            .configureClient()
            .baseUrl("/test")
            .build();
```

### 3.7.2. Writing tests

WebTestClient是围绕 [WebClient](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/web-reactive.html#webflux-webclient)的一个shell。它提供了一个相同的API，直到通过exchange（）执行请求。 exchange（）之后是一个链接的API工作流程来验证响应。

通常，您首先声明响应状态和标题：

```java
    client.get().uri("/persons/1")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
            // ...
```

然后指定如何解码和使用响应主体：

- `expectBody(Class<T>)` — decode to single object.
- `expectBodyList(Class<T>)` — decode and collect objects to `List<T>`.
- `expectBody()` — decode to `byte[]` for [JSON content](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/testing.html#webtestclient-json) or empty body.

然后你可以对返回结果使用内建的断言。这里是一个例子：

```java
    client.get().uri("/persons")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(Person.class).hasSize(3).contains(person);
```

你可以超越内置的断言，并创建自己的：

```java
    client.get().uri("/persons/1")
            .exchange()
            .expectStatus().isOk()
            .expectBody(Person.class)
            .consumeWith(result -> {
                // custom assertions (e.g. AssertJ)...
            });
```

您也可以退出工作流程并获得结果：

```java
    EntityExchangeResult<Person> result = client.get().uri("/persons/1")
            .exchange()
            .expectStatus().isOk()
            .expectBody(Person.class)
            .returnResult();
```

当您需要使用泛型解码为目标类型时，请查找接受[ParameterizedTypeReference](https://docs.spring.io/spring-framework/docs/5.0.2.RELEASE/javadoc-api/org/springframework/core/ParameterizedTypeReference.html)而不是Class <T>的重载方法。

#### No content

如果响应没有内容，或者你不在乎，使用Void.class确保释放资源：

```java
    client.get().uri("/persons/123")
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(Void.class);
```

或者，如果您要断言没有响应内容，请使用以下命令：

```java
    client.post().uri("/persons")
            .body(personMono, Person.class)
            .exchange()
            .expectStatus().isCreated()
            .expectBody().isEmpty;
```

#### JSON content

当你使用expectBody（）时，响应被当做byte []处理。这对原始内容断言很有用。例如，您可以使用 [JSONAssert](http://jsonassert.skyscreamer.org/)来验证JSON内容：

```java
    client.get().uri("/persons/1")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .json("{\"name\":\"Jane\"}")
```

您也可以使用JSONPath表达式：

```java
    client.get().uri("/persons")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$[0].name").isEqualTo("Jane")
            .jsonPath("$[1].name").isEqualTo("Jason");
```

#### Streaming responses

要测试很大的流的响应（例如“text / event-stream”，“application / stream json”），您需要在响应status和header 断言之后立即通过returnResult退出API链，如下所示：

```java
    FluxExchangeResult<MyEvent> result = client.get().uri("/events")
            .accept(TEXT_EVENT_STREAM)
            .exchange()
            .expectStatus().isOk()
            .returnResult(MyEvent.class);
```

现在，您可以使用Flux <T>，然后在满足测试对象的某个时刻取消，Flux <T>可以在解码对象到来时断言它们。我们建议使用reactor-test模块中的StepVerifier来完成这个工作，例如：

```java
    Flux<Event> eventFux = result.getResponseBody();

    StepVerifier.create(eventFlux)
            .expectNext(person)
            .expectNextCount(4)
            .consumeNextWith(p -> ...)
            .thenCancel()
            .verify();
```

#### Request body

当谈到构建请求时，WebTestClient提供了和WebClient完全相同的API，实现主要是一个简单的传递。请参阅 [WebClient documentation](https://docs.spring.io/spring/docs/5.0.2.RELEASE/spring-framework-reference/web-reactive.html#webflux-client-body)，了解如何准备一个包含提交表单数据，multipart requests等内容的请求。

## 3.8. PetClinic Example

 [GitHub](https://github.com/spring-projects/spring-petclinic)上提供的Spring的PetClinic应用程序演示了JUnit 4环境中Spring TestContext框架的几个特性。大多数测试功能都包含在AbstractClinicTests中，下面显示了部分代码：

```java
import static org.junit.Assert.assertEquals;
// import ...

@ContextConfiguration
public abstract class AbstractClinicTests extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    protected Clinic clinic;

    @Test
    public void getVets() {
        Collection<Vet> vets = this.clinic.getVets();
        assertEquals("JDBC query must show the same number of vets",
            super.countRowsInTable("VETS"), vets.size());
        Vet v1 = EntityUtils.getById(vets, Vet.class, 2);
        assertEquals("Leary", v1.getLastName());
        assertEquals(1, v1.getNrOfSpecialties());
        assertEquals("radiology", (v1.getSpecialties().get(0)).getName());
        // ...
    }

    // ...
}
```

注意：

- 此测试用例扩展了AbstractTransactionalJUnit4SpringContextTests类，从中继承了依赖注入（通过DependencyInjectionTestExecutionListener）和事务行为（通过TransactionalTestExecutionListener）的配置。
- `clinic` 实例变量 - 被测试的应用程序对象 - 通过@Autowired依赖注入。
- getVets（）方法说明如何使用继承的countRowsInTable（）方法轻松验证给定数据表中的行数，从而验证正在测试的应用程序代码的正确行为。这样可以进行更强大的测试，并减少对精确测试数据的依赖。例如，您可以在数据库中添加额外的行而不破坏测试。
- 像许多使用数据库的集成测试一样，AbstractClinicTests中的大多数测试依赖于测试用例运行之前数据库中已有的最小数据量。或者，您也可以选择在测试用例的测试fixture中填充数据库 - 同样在与测试相同的事务中。

PetClinic应用程序支持三种数据访问技术：JDBC，Hibernate和JPA。通过声明@ContextConfiguration而没有指定任何特定的resource location，AbstractClinicTests类将从默认位置AbstractClinicTests-context.xml加载应用程序上下文，该位置声明一个公共的DataSource。子类必须指定声明PlatformTransactionManager和Clinic的具体实现的其他上下文 resource location。

例如，PetClinic测试的Hibernate实现包含以下实现。对于这个例子，HibernateClinicTests不包含一行代码：我们只需要声明@ContextConfiguration，并且测试类是从AbstractClinicTests继承的。由于@ContextConfiguration在没有任何特定的资源位置的情况下被声明，所以Spring TestContext框架从所有在AbstractClinicTests-context.xml中定义的bean（即继承的location）和HibernateClinicTests-context.xml中加载一个应用程序上下文，带有HibernateClinicTests-context.xml可能会覆盖AbstractClinicTests-context.xml中定义的bean。

```java
@ContextConfiguration
public class HibernateClinicTests extends AbstractClinicTests { }
```

在大规模的应用程序中，Spring配置通常被分割成多个文件。因此，对于所有特定于应用程序的集成测试，配置位置通常在公共基类中指定。这样的基类还可以添加有用的实例变量 - 自然地由依赖注入来填充 - 例如在应用程序使用Hibernate的情况下的SessionFactory。

尽可能在集成测试中使用与部署环境中完全相同的Spring配置文件。一个可能的差异在于数据库连接池和事务基础构件。如果您正在部署一个完整的应用程序到服务器，则可能会使用其连接池（通过JNDI提供）和JTA实现。因此，在生产中，您将使用JndiObjectFactoryBean或<jee：jndi-lookup>作为DataSource和JtaTransactionManager。 JNDI和JTA在容器外部集成测试中将不可用，因此您应该使用像Commons DBCP BasicDataSource和DataSourceTransactionManager或HibernateTransactionManager这样的组合。您可以将此不一样行为分解为单个XML文件，并在应用程序服务器和“本地”配置之间进行选择，以便与测试和生产环境之间都相同的所有其他配置分开。另外，建议使用属性文件进行数据库连接设置。查看PetClinic应用程序的例子。

# 4. Further Resources

有关测试的更多信息，请查阅以下资源：

- [JUnit](http://www.junit.org/): "*A programmer-oriented testing framework for Java*". Used by the Spring Framework in its test suite.
- [TestNG](http://testng.org/): A testing framework inspired by JUnit with added support for annotations, test groups, data-driven testing, distributed testing, etc.
- [AssertJ](https://joel-costigliola.github.io/assertj/): "*Fluent assertions for Java*" including support for Java 8 lambdas, streams, etc.
- [Mock Objects](https://en.wikipedia.org/wiki/Mock_Object): Article in Wikipedia.
- [MockObjects.com](http://www.mockobjects.com/): Web site dedicated to mock objects, a technique for improving the design of code within test-driven development.
- [Mockito](http://mockito.org/): Java mock library based on the [test spy](http://xunitpatterns.com/Test%20Spy.html) pattern.
- [EasyMock](http://www.easymock.org/): Java library "*that provides Mock Objects for interfaces (and objects through the class extension) by generating them on the fly using Java’s proxy mechanism.*" Used by the Spring Framework in its test suite.
- [JMock](http://www.jmock.org/): Library that supports test-driven development of Java code with mock objects.
- [DbUnit](http://dbunit.sourceforge.net/): JUnit extension (also usable with Ant and Maven) targeted for database-driven projects that, among other things, puts your database into a known state between test runs.
- [The Grinder](http://grinder.sourceforge.net/): Java load testing framework.