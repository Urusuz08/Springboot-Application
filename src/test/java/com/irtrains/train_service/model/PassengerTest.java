package com.irtrains.train_service.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class PassengerTest {

    private Validator validator;
    private passenger passenger;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        passenger = new passenger();
        passenger.setPassengerId(1L);
        passenger.setBookingId(1L);
        passenger.setName("Test Passenger");
        passenger.setAge(30);
        passenger.setGender("Male");
        passenger.setCoachNumber(1);
        passenger.setSeatNumber("S1");
        passenger.setBerthType("LOWER");
        passenger.setSeatStatus("CONFIRMED");
    }

    @Test
    public void testValidPassenger() {
        Set<ConstraintViolation<passenger>> violations = validator.validate(passenger);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testAgeValidation() {
        passenger.setAge(-1); // Invalid
        Set<ConstraintViolation<passenger>> violations = validator.validate(passenger);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Age cannot be negative.", violations.iterator().next().getMessage());

        passenger.setAge(150); // Invalid
        violations = validator.validate(passenger);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Age seems unrealistic.", violations.iterator().next().getMessage());
    }

    @Test
    public void testGenderValidation() {
        passenger.setGender("InvalidGender"); // Invalid
        Set<ConstraintViolation<passenger>> violations = validator.validate(passenger);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals(1L, passenger.getPassengerId());
        assertEquals(1L, passenger.getBookingId());
        assertEquals("Test Passenger", passenger.getName());
        assertEquals(30, passenger.getAge());
        assertEquals("Male", passenger.getGender());
        assertEquals(1, passenger.getCoachNumber());
        assertEquals("S1", passenger.getSeatNumber());
        assertEquals("LOWER", passenger.getBerthType());
        assertEquals("CONFIRMED", passenger.getSeatStatus());
    }
}

