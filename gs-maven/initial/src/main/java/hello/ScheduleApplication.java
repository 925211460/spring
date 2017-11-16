package hello;

import org.springframework.boot.SpringApplication;

/**
 * @EnableScheduling确保创建后台任务执行程序,没有它，没有任何计划
 */
//@SpringBootApplication
//@EnableScheduling
public class ScheduleApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ScheduleApplication.class);
    }
}
