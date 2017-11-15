package hello.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2017/11/15 0015.
 * 用于测试事务
 *
 * 你的应用程序实际上是零配置。Spring Boot会在classpath和h2上检测到spring-jdbc，并会自动为你创建一个DataSource和一个JdbcTemplate
 * 由于这样的基础设施现在可用，而且您没有专门的配置，所以也会为您创建一个DataSourceTransactionManager，这是拦截@Transactional注释方法的组件
 */
@Component
public class BookingService {
    private final static Logger logger = LoggerFactory.getLogger(BookingService.class);

    private final JdbcTemplate jdbcTemplate;

    public BookingService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //此注解表示该方法中任意一个操作失败都会把整个操作回滚，并抛出异常
    @Transactional
    public void book(String... persons) {
        for (String person : persons) {
            logger.info("Booking " + person + " in a seat...");
            jdbcTemplate.update("insert into BOOKINGS(FIRST_NAME) values (?)", person);
        }
    }

    public List<String> findAllBookings() {
        return jdbcTemplate.query("select FIRST_NAME from BOOKINGS",
                (rs, rowNum) -> rs.getString("FIRST_NAME"));
    }
}
