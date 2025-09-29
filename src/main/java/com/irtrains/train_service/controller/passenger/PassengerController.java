package com.irtrains.train_service.controller.passenger;

import com.irtrains.train_service.model.passenger.passenger;
import com.irtrains.train_service.service.passenger.PassengerService;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/passengers")
@CrossOrigin(origins = "*")
public class PassengerController {

    private final PassengerService passengerService;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
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

    @PostMapping // Used to add a new passenger
    public ResponseEntity<?> addPassenger(@RequestBody passenger passenger) {
        try {
            passenger createdPassenger = passengerService.addPassenger(passenger);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPassenger);
        } catch (Exception e) {
            return error(HttpStatus.BAD_REQUEST, "Failed to add passenger", e);
        }
    }

    @GetMapping("/booking/{bookingId}") // Used to get passengers by booking ID
    public ResponseEntity<?> getPassengersByBookingId(@PathVariable Long bookingId) {
        try {
            List<passenger> passengers = passengerService.getPassengersByBookingId(bookingId);
            if (passengers.isEmpty()) {
                return notFound("No passengers found for booking ID: " + bookingId);
            }
            return ResponseEntity.ok(passengers);
        } catch (Exception e) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve passengers", e);
        }
    }

    @PutMapping("/booking/{bookingId}/cancel") // Used to cancel all passengers by booking ID
    public ResponseEntity<?> cancelPassengersByBookingId(@PathVariable Long bookingId) {
        try {
            passengerService.cancelPassengersByBookingId(bookingId);
            return ResponseEntity.ok().body("All passengers for booking ID " + bookingId + " cancelled successfully.");
        } catch (Exception e) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to cancel passengers", e);
        }
    }

    @PutMapping("/{passengerId}/status") // Used to update a specific passenger's status
    public ResponseEntity<?> updatePassengerStatus(@PathVariable Long passengerId, @RequestParam String status) {
        try {
            passenger updatedPassenger = passengerService.updatePassengerStatus(passengerId, status);
            return ResponseEntity.ok(updatedPassenger);
        } catch (IllegalArgumentException e) {
            return notFound(e.getMessage());
        } catch (Exception e) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update passenger status", e);
        }
    }
}
