package com.irtrains;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement

@ComponentScan(basePackages = {
        "com.irtrains.train_service.config",
        "com.irtrains.user_service.config",
        "com.irtrains.user_service.service",
        "com.irtrains.user_service.controller",
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
        "com.irtrains.train_service.model.train_coaches",
        "com.irtrains.train_service.model.trainRoute"
})
public class TrainServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(TrainServiceApplication.class, args);
    }

}
