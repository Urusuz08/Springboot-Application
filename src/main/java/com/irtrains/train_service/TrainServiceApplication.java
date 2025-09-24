package com.irtrains.train_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = {
        "com.irtrains.train_service.config.train",
        "com.irtrains.train_service.controller.station",
        "com.irtrains.train_service.service.station"
})
@EntityScan(basePackages = {
        "com.irtrains.train_service.model.station",
        "com.irtrains.train_service.model.enums"
})
public class TrainServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrainServiceApplication.class, args);
    }

}
