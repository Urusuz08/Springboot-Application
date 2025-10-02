package com.irtrains.train_service.model.train;

import com.irtrains.train_service.model.enums.Type;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class TrainTest {

    private Validator validator;
    private train train;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        train = new train();
        train.setTrainID("12345");
        train.setName("Test Train");
        train.setType(Type.EXPRESS);
        train.setSourceStation("SRC");
        train.setDestinationStation("DST");
    }

    @Test
    public void testValidTrain() {
        Set<ConstraintViolation<train>> violations = validator.validate(train);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testTrainIdValidation() {
        train.setTrainID("123"); // Invalid
        Set<ConstraintViolation<train>> violations = validator.validate(train);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("TrainID must be exactly 5 digits (e.g., 12345).", violations.iterator().next().getMessage());

        train.setTrainID("123456"); // Invalid
        violations = validator.validate(train);
        assertFalse(violations.isEmpty());

        train.setTrainID("abcde"); // Invalid
        violations = validator.validate(train);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testTypeNotNull() {
        train.setType(null);
        Set<ConstraintViolation<train>> violations = validator.validate(train);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Types cannot be null.", violations.iterator().next().getMessage());
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals("12345", train.getTrainId());
        assertEquals("Test Train", train.getName());
        assertEquals(Type.EXPRESS, train.getType());
        assertEquals("SRC", train.getSourceStation());
        assertEquals("DST", train.getDestinationStation());
    }
}

