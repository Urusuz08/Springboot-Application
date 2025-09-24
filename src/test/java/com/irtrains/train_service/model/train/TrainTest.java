package com.irtrains.train_service.model.train;

import com.irtrains.train_service.model.enums.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Train Entity Tests")
class TrainTest {

    private Validator validator;
    private train trainEntity;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        trainEntity = new train();
        trainEntity.setCode("ABCDE"); // valid per regex ^[A-Z]{5}$ in entity
        trainEntity.setName("Rajdhani Express");
        trainEntity.setState(State.MAHARASHTRA);
        trainEntity.setPlace("Mumbai");
    }

    @Test
    @DisplayName("Valid train entity should pass validation")
    void testValidTrain() {
        Set<ConstraintViolation<train>> violations = validator.validate(trainEntity);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Invalid code should fail validation")
    void testInvalidCode() {
        trainEntity.setCode("12A45");
        Set<ConstraintViolation<train>> violations = validator.validate(trainEntity);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Null state should fail validation")
    void testNullState() {
        trainEntity.setState(null);
        Set<ConstraintViolation<train>> violations = validator.validate(trainEntity);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Getters and setters should work")
    void testGettersSetters() {
        assertEquals("ABCDE", trainEntity.getCode());
        assertEquals("Rajdhani Express", trainEntity.getName());
        assertEquals(State.MAHARASHTRA, trainEntity.getState());
        assertEquals("Mumbai", trainEntity.getPlace());
    }
}
