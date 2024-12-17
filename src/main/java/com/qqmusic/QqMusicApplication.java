package com.qqmusic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.qqmusic.mapper")
@SpringBootApplication
public class QqMusicApplication {

    public static void main(String[] args) {
        SpringApplication.run(QqMusicApplication.class, args);
    }

}
