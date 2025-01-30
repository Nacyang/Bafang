package com.bafang;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.bafang.mapper")
@Slf4j
@EnableCaching
@EnableScheduling
public class BafangApplication {

    public static void main(String[] args) {
        SpringApplication.run(BafangApplication.class, args);
    }

}
