package com.icbc;

import com.icbc.myspider.MySpider;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan("com.icbc.mapper")
@Slf4j
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        ConfigurableApplicationContext context = app.run(args);
        log.info("started: {}", context);
        MySpider mySpider = context.getBean("mySpider", MySpider.class);
        mySpider.start();
    }
}
