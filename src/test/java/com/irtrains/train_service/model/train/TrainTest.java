package com.irtrains.train_service.model.train;

import com.irtrains.train_service.model.enums.Type;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Train Entity Validation Tests")
class TrainTest {

    private Validator validator;
    private train t;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        t = new train();
        t.setTrainID("12345"); // valid 5-digit per @Pattern
        t.setName("Rajdhani Express");
        t.setType(Type.RAJDHANI);
        t.setSourceStation("BCT");
        t.setDestinationStation("NDLS");
    }

    @Test
    @DisplayName("Valid train passes bean validation")
    void validTrain() {
        Set<ConstraintViolation<train>> violations = validator.validate(t);
        assertTrue(violations.isEmpty(), () -> "Expected no violations, got: " + violations);
    }

    @Test
    @DisplayName("Invalid trainId (not 5 digits) fails validation")
    void invalidTrainId() {
        t.setTrainID("12A45");
        Set<ConstraintViolation<train>> violations = validator.validate(t);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("trainId")));
    }

    @Test
    @DisplayName("Null type fails validation due to @NotNull")
    void nullType() {
        t.setType(null);
        Set<ConstraintViolation<train>> violations = validator.validate(t);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("type")));
    }

    @Test
    @DisplayName("Getters return assigned values")
    void getters() {
        assertEquals("12345", t.getTrainId());
        assertEquals("Rajdhani Express", t.getName());
        assertEquals(Type.RAJDHANI, t.getType());
        assertEquals("BCT", t.getSourceStation());
        assertEquals("NDLS", t.getDestinationStation());
    }
}
