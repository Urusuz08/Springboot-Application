package com.irtrains;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
@ComponentScan(basePackages = {
        "com.irtrains.config",
        "com.irtrains.train_service.config",
        "com.irtrains.user_service.config",
        "com.irtrains.user_service.service",
        "com.irtrains.user_service.controller",
        "com.irtrains.train_service.controller",
        "com.irtrains.train_service.service"
})
public class TrainServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(TrainServiceApplication.class, args);
    }

}
