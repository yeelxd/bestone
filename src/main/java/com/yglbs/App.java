package com.yglbs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 * exclude排除Spring Boot启动时默认数据源和Jpa的注册
 * (exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
 * @author yeelxd
 * @date 2018-02-28
 */
@SpringBootApplication
@MapperScan("com.yglbs.mapper")
public class App {

    private static Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
