package com.example.daewoo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.daewoo.user")
public class DaewooApplication {

    public static void main(String[] args) {
        SpringApplication.run(DaewooApplication.class, args);
    }
}
