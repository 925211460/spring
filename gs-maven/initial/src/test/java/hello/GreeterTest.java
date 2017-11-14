package hello;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

/**
 * Maven使用名为“surefire”的插件来运行单元测试。这个插件的默认配置编译并运行src / test / java中名称匹配* Test的所有类
 * 你可以像这样在命令行上运行mvn test测试
 */
public class GreeterTest {

    private Greeter greeter = new Greeter();

    @Test
    public void greeterSaysHello() {
        assertThat(greeter.sayHello(), containsString("Hello"));
    }

}