package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);
    public static void main(String[] args) {
        //region 此应用是服务消费者,因为jackon在classpath下，所以restTemplate会使用它把接口返回的json字符串转换为Quote对象
        //第一种实现方式，类上面不需要加@springBootApplication注解
        //RestTemplate restTemplate = new RestTemplate();
        //Quote quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
        //log.info(quote.toString());

        //第二种实现方式，使用spring boot管理应用的生命周期
        //1.添加@SpringBootApplication注解
        //2
        SpringApplication.run(Application.class, args);
        //3使用RestTemplateBuilder构建RestTemplate，并且把它加入到容器中，方便测试，RestTemplateBuilder由spring自动注入
        //4最后，我们将RestTemplate移动到一个CommandLineRunner回调函数，以便在启动时由Spring Boot执行
        //endregion
    }


/*    //<-----3
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    //<-----4
    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
        return args -> {
            Quote quote = restTemplate.getForObject(
                    "http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
            log.info(quote.toString());
        };
    }*/
}