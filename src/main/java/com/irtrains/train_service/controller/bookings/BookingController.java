package com.irtrains.train_service.controller.bookings;

import com.irtrains.train_service.model.booking.Booking;
import com.irtrains.train_service.service.booking.BookingService;

import com.irtrains.train_service.service.station.StationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    private <T> ResponseEntity<T> notFound(String msg) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    private ResponseEntity<Map<String, Object>> error(HttpStatus status, String message, Exception ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        if (ex != null) body.put("detail", ex.getMessage());
        return ResponseEntity.status(status).body(body);
    }

    @PostMapping //Used to create a new booking
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        try {
            Booking createdBooking = bookingService.createBooking(booking);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
        } catch (Exception e) {
            return error(HttpStatus.BAD_REQUEST, "Failed to create booking", e);
        }
    }

    @PutMapping("/{pnrNumber}/cancel") //Used to cancel an existing booking by PNR number
    public ResponseEntity<?> cancelBooking(@PathVariable String pnrNumber) {
        try {
            bookingService.cancelBooking(pnrNumber);
            return ResponseEntity.ok().body("Booking with PNR " + pnrNumber + " cancelled successfully.");
        } catch (RuntimeException e) {
            return notFound(e.getMessage());
        } catch (Exception e) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to cancel booking", e);
        }
    }

    @GetMapping("/{pnrNumber}") //Used to get booking details by PNR number
    public ResponseEntity<?> getBookingByPnrNumber(@PathVariable String pnrNumber) {
        try {
            Booking booking = bookingService.getBookingByPnrNumber(pnrNumber);
            return ResponseEntity.ok(booking);
        } catch (RuntimeException e) {
            return notFound(e.getMessage());
        } catch (Exception e) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve booking", e);
        }
    }

    @GetMapping("/user/{userId}") //Used to get all bookings for a specific user by user ID
    public ResponseEntity<?> getBookingsByUserId(@PathVariable String userId) {
        try {
            List<Booking> bookings = bookingService.getBookingsByUserId(userId);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve bookings", e);
        }
    }


}
