package com.irtrains.train_service.model.booking;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class BookingTest {

    private Validator validator;
    private Booking booking;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        booking = new Booking();
        booking.setBookingId(1L);
        booking.setPnrNumber("PNR12345");
        booking.setUserId("user123");
        booking.setContactNumber("1234567890");
        booking.setEmail("test@example.com");
        booking.setTrainId("12345");
        booking.setSourceStationCode("SRC");
        booking.setDestinationStationCode("DST");
        booking.setCoachType("AC");
        booking.setTravelDate(new Date());
        booking.setBookingDate(new Date());
        booking.setStatus("CONFIRMED");
        booking.setFare(1000.0);
    }

    @Test
    public void testValidBooking() {
        Set<ConstraintViolation<Booking>> violations = validator.validate(booking);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testTrainIdValidation() {
        booking.setTrainId("123"); // Invalid
        Set<ConstraintViolation<Booking>> violations = validator.validate(booking);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("TrainID must be exactly 5 digits (e.g., 12345).", violations.iterator().next().getMessage());
    }

    @Test
    public void testStationCodeValidation() {
        booking.setSourceStationCode("S"); // Invalid
        Set<ConstraintViolation<Booking>> violations = validator.validate(booking);
        assertFalse(violations.isEmpty());

        booking.setSourceStationCode("SRC"); // Valid
        violations = validator.validate(booking);
        assertTrue(violations.isEmpty());

        booking.setDestinationStationCode("D"); // Invalid
        violations = validator.validate(booking);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals(1L, booking.getBookingId());
        assertEquals("PNR12345", booking.getPnrNumber());
        assertEquals("user123", booking.getUserId());
        assertEquals("1234567890", booking.getContactNumber());
        assertEquals("test@example.com", booking.getEmail());
        assertEquals("12345", booking.getTrainId());
        assertEquals("SRC", booking.getSourceStationCode());
        assertEquals("DST", booking.getDestinationStationCode());
        assertEquals("AC", booking.getCoachType());
        assertNotNull(booking.getTravelDate());
        assertNotNull(booking.getBookingDate());
        assertEquals("CONFIRMED", booking.getStatus());
        assertEquals(1000.0, booking.getFare());
    }
}

