package com.irtrains.train_service.model.train_coaches;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class TrainCoachesTest {

    private Validator validator;
    private train_coaches trainCoach;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        trainCoach = new train_coaches();
        trainCoach.setCoachId(1);
        trainCoach.setTrainId("12345");
        trainCoach.setCoachType("AC");
        trainCoach.setTotalAvailableSeats(72);
        trainCoach.setCoachNumber(1);
    }

    @Test
    public void testValidTrainCoach() {
        Set<ConstraintViolation<train_coaches>> violations = validator.validate(trainCoach);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals(1, trainCoach.getCoachId());
        assertEquals("12345", trainCoach.getTrainId());
        assertEquals("AC", trainCoach.getCoachType());
        assertEquals(72, trainCoach.getTotalAvailableSeats());
        assertEquals(1, trainCoach.getCoachNumber());
    }
}

