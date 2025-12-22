package com.irtrains.train_service.controller;

import com.irtrains.train_service.model.trainSeatAvailability;
import com.irtrains.train_service.service.SeatAvailabilityService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.time.*;

import java.util.*;

@RestController
@RequestMapping("/api/seatAvailability")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SeatAvailabilityController {

    private final SeatAvailabilityService seatAvailabilityService;

    public SeatAvailabilityController(SeatAvailabilityService seatAvailabilityService) {
        this.seatAvailabilityService = seatAvailabilityService;
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

    @PostMapping
    public ResponseEntity<?> addSeatAvailability(@RequestBody trainSeatAvailability seatAvailability) {
        try {
            trainSeatAvailability createdSeatAvailability = seatAvailabilityService.addSeatAvailability(seatAvailability);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSeatAvailability);
        } catch (Exception e) {
            return error(HttpStatus.BAD_REQUEST, "Failed to add seat availability", e);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> getSeatAvailability(@RequestParam String trainId, @RequestParam String coachId, @RequestParam LocalDate dateOfJourney) {
        try {
            trainSeatAvailability seatAvailability = seatAvailabilityService.getSeatAvailabilityByTrainIdAndCoachIdAndDateOfJourney(trainId, coachId, dateOfJourney);
            if (seatAvailability == null) {
                return notFound("Seat availability not found for the given criteria");
            }
            return ResponseEntity.ok(seatAvailability);
        } catch (Exception e) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve seat availability", e);
        }
    }

    @GetMapping("/train/{trainId}")
    public ResponseEntity<?> getSeatAvailabilityByTrainId(@PathVariable String trainId) {
        try {
            trainSeatAvailability seatAvailability = seatAvailabilityService.getSeatAvailabilityByTrainId(trainId);
            if (seatAvailability == null) {
                return notFound("Seat availability not found for train ID: " + trainId);
            }
            return ResponseEntity.ok(seatAvailability);
        } catch (Exception e) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve seat availability", e);
        }
    }

    @PutMapping("/{seatAvailabilityId}")
    public ResponseEntity<?> updateSeatAvailability(@PathVariable Integer seatAvailabilityId, @RequestBody trainSeatAvailability seatAvailabilityDetails) {
        try {
            trainSeatAvailability updatedSeatAvailability = seatAvailabilityService.updateSeatAvailability(seatAvailabilityId, seatAvailabilityDetails);
            return ResponseEntity.ok(updatedSeatAvailability);
        } catch (RuntimeException e) {
            return notFound(e.getMessage());
        } catch (Exception e) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update seat availability", e);
        }
    }

    @DeleteMapping("/{seatAvailabilityId}")
    public ResponseEntity<?> deleteSeatAvailability(@PathVariable Integer seatAvailabilityId) {
        try {
            seatAvailabilityService.deleteSeatAvailability(seatAvailabilityId);
            return ResponseEntity.ok().body("Seat availability with ID " + seatAvailabilityId + " deleted successfully.");
        } catch (Exception e) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete seat availability", e);
        }
    }

}
