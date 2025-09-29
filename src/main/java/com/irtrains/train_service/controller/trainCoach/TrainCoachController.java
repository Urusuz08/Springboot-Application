package com.irtrains.train_service.controller.trainCoach;

import com.irtrains.train_service.service.train_coach.TrainCoachService;
import com.irtrains.train_service.model.train_coaches.train_coaches;
import org.springframework.http.*;

import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
@RequestMapping("/api/train-coaches")
@CrossOrigin(origins = "*")
public class TrainCoachController {

    private final TrainCoachService trainCoachService;

    public TrainCoachController(TrainCoachService trainCoachService) {
        this.trainCoachService = trainCoachService;
    }
    /* ========================= Helpers ========================= */
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

    @PostMapping // Used to add a new train coach
    public ResponseEntity<?> addTrainCoach(@RequestBody train_coaches trainCoach) {
        try {
            train_coaches createdTrainCoach = trainCoachService.addTrainCoach(trainCoach);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTrainCoach);
        } catch (Exception e) {
            return error(HttpStatus.BAD_REQUEST, "Failed to add train coach", e);
        }
    }
    @GetMapping("/train/{trainId}") // Used to get train coaches by train ID
    public ResponseEntity<?> getTrainCoachesByTrainId(@PathVariable String trainId) {
        try {
            List<train_coaches> trainCoaches = trainCoachService.getTrainCoachesByTrainId(trainId);
            if (trainCoaches.isEmpty()) {
                return notFound("No train coaches found for train ID: " + trainId);
            }
            return ResponseEntity.ok(trainCoaches);
        } catch (Exception e) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve train coaches", e);
        }
    }
    @PutMapping("/{coachId}") // Used to update a train coach by coach ID
    public ResponseEntity<?> updateTrainCoach(@PathVariable Integer coachId, @RequestBody train_coaches trainCoach) {
        try {
            trainCoach.setCoachId(coachId);
            train_coaches updatedTrainCoach = trainCoachService.updateTrainCoach(trainCoach);
            return ResponseEntity.ok(updatedTrainCoach);
        } catch (RuntimeException e) {
            return notFound(e.getMessage());
        } catch (Exception e) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update train coach", e);
        }
    }
    @DeleteMapping("/{coachId}") // Used to delete a train coach by coach ID
    public ResponseEntity<?> deleteTrainCoach(@PathVariable Integer coachId) {
        try {
            trainCoachService.deleteTrainCoachById(coachId);
            return ResponseEntity.ok().body("Train coach with ID " + coachId + " deleted successfully.");
        } catch (Exception e) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete train coach", e);
        }
    }

    @GetMapping("/{coachId}")
    public ResponseEntity<?> getTrainCoachById(@PathVariable Integer coachId) {
        try {
            Optional<train_coaches> trainCoach = trainCoachService.getcoachById(coachId);
            return trainCoach.map(ResponseEntity::ok)
                    .orElse(notFound("Train coach not found with ID: " + coachId));
        } catch (Exception e) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve train coach", e);
        }
    }

//    @GetMapping
//    public ResponseEntity<?> getAllTrainCoaches() {
//        try {
//            List<train_coaches> trainCoaches = trainCoachService.getAllTrainCoaches();
//            if (trainCoaches.isEmpty()) {
//                return notFound("No train coaches found");
//            }
//            return ResponseEntity.ok(trainCoaches);
//        } catch (Exception e) {
//            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve train coaches", e);
//        }
//    }

    @GetMapping("/type/{coachType}")
    public ResponseEntity<?> getTrainCoachesByCoachType(@PathVariable String coachType) {
        try {
            List<train_coaches> trainCoaches = trainCoachService.getTrainCoachesByCoachType(coachType);
            if (trainCoaches.isEmpty()) {
                return notFound("No train coaches found for type: " + coachType);
            }
            return ResponseEntity.ok(trainCoaches);
        } catch (Exception e) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve train coaches", e);
        }
    }

    @GetMapping("/train/{trainId}/coach/{coachNumber}")
    public ResponseEntity<?> getTrainCoachByTrainIdAndCoachNumber(@PathVariable String trainId, @PathVariable Integer coachNumber) {
        try {
            Optional<train_coaches> trainCoach = trainCoachService.getTrainCoachByTrainIdAndCoachNumber(trainId, coachNumber);
            return trainCoach.map(ResponseEntity::ok)
                    .orElse(notFound("Train coach not found for train ID " + trainId + " and coach number " + coachNumber));
        } catch (Exception e) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve train coach", e);
        }
    }

}
