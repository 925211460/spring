package hello.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 计划注释定义了特定方法的运行时间
 *
 * 注意：本示例使用fixedRate，它指定 从这个方法开始执行的时间到后面每次方法调用之间的时间间隔
 * fixedDelay它指定 从这个方法执行结束时间到后面每次方法调用之间的时间间隔
 * 您还可以使用@Scheduled（cron =“...”）表达式来实现更复杂的任务调度。
 */
@Component
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
    }
}
