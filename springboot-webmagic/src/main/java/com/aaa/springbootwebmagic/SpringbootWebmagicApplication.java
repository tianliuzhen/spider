package com.aaa.springbootwebmagic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
@MapperScan(basePackages ={"com.aaa.springbootwebmagic.service"})
public class SpringbootWebmagicApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootWebmagicApplication.class, args);
    }

}
