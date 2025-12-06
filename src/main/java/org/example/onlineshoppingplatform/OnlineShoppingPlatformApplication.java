package org.example.onlineshoppingplatform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.example.onlineshoppingplatform.mapper")
public class OnlineShoppingPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineShoppingPlatformApplication.class, args);
    }

}
