package hello;

import hello.domain.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 这个Application类实现了Spring Boot的CommandLineRunner，这意味着它将在应用程序上下文加载之后执行run（）方法
 * Spring Boot支持H2（一种内存中的关系数据库引擎），并自动创建一个连接。因为我们使用的是spring-jdbc，Spring Boot会自动创建一个JdbcTemplate。 @Autowired JdbcTemplate字段会自动加载并使其可用。
 *
 * 首先，使用JdbcTemplate的`execute方法来执行一些DDL
 * 其次，获取字符串列表并使用Java 8流，将它们拆分为Java数组中的名/姓对
 */
//@SpringBootApplication
public class AccessDataApplication implements CommandLineRunner{

    private static final Logger log = LoggerFactory.getLogger(AccessDataApplication.class);

    public static void main(String args[]) {
        SpringApplication.run(AccessDataApplication.class, args);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... strings) throws Exception {

        log.info("Creating tables");

        jdbcTemplate.execute("DROP TABLE customers IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE customers(" +
                "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");

        // Split up the array of whole names into an array of first/last names
        List<Object[]> splitUpNames = Arrays.asList("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long").stream()
                .map(name -> name.split(" "))
                .collect(Collectors.toList());

        // Use a Java 8 stream to print out each tuple of the list
        splitUpNames.forEach(name -> log.info(String.format("Inserting customer record for %s %s", name[0], name[1])));

        // Uses JdbcTemplate's batchUpdate operation to bulk load data
        jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?,?)", splitUpNames);

        log.info("Querying for customer records where first_name = 'Josh':");
        jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM customers WHERE first_name = ?", new Object[] { "Josh" },
                (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"))
        ).forEach(customer -> log.info(customer.toString()));
    }
}
