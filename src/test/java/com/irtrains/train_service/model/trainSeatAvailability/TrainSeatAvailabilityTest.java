package com.irtrains.train_service.model.trainSeatAvailability;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class TrainSeatAvailabilityTest {

    private Validator validator;
    private trainSeatAvailability seatAvailability;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        seatAvailability = new trainSeatAvailability();
        seatAvailability.setSeatAvailabilityId(1);
        seatAvailability.setTrainId("12345");
        seatAvailability.setCoachId("AC");
        seatAvailability.setDateOfJourney(LocalDate.now());
        seatAvailability.setAvailableSeats(100);
        seatAvailability.setTotalSeats(100);
        seatAvailability.setLastUpdated(System.currentTimeMillis());
    }

    @Test
    public void testValidTrainSeatAvailability() {
        Set<ConstraintViolation<trainSeatAvailability>> violations = validator.validate(seatAvailability);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals(1, seatAvailability.getSeatAvailabilityId());
        assertEquals("12345", seatAvailability.getTrainId());
        assertEquals("AC", seatAvailability.getCoachId());
        assertEquals("2025-10-02", seatAvailability.getDateOfJourney());
        assertEquals(100, seatAvailability.getAvailableSeats());
        assertEquals(100, seatAvailability.getTotalSeats());
        assertNotNull(seatAvailability.getLastUpdated());
    }
}

