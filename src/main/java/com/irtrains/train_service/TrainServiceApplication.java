package com.irtrains.train_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement

@ComponentScan(basePackages = {
        "com.irtrains.train_service.config",
        "com.irtrains.train_service.controller.station",
        "com.irtrains.train_service.controller.train",
        "com.irtrains.train_service.controller.bookings",
        "com.irtrains.train_service.controller.passenger",
        "com.irtrains.train_service.controller.seatAvailability",
        "com.irtrains.train_service.controller.trainCoach",
        "com.irtrains.train_service.service.station",
        "com.irtrains.train_service.service.train",
        "com.irtrains.train_service.service.booking",
        "com.irtrains.train_service.service.passenger",
        "com.irtrains.train_service.service.seatAvailabilityService",
        "com.irtrains.train_service.service.train_coach"
})
@EntityScan(basePackages = {
        "com.irtrains.train_service.model.station",
        "com.irtrains.train_service.model.train",
        "com.irtrains.train_service.model.enums",
        "com.irtrains.train_service.model.booking",
        "com.irtrains.train_service.model.passenger",
        "com.irtrains.train_service.model.trainSeatAvailability",
        "com.irtrains.train_service.model.train_coaches"
})
public class TrainServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(TrainServiceApplication.class, args);
    }

}
