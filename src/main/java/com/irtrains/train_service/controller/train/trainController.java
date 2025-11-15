package com.irtrains.train_service.controller.train;

import com.irtrains.train_service.model.enums.Type;
import com.irtrains.train_service.model.train.train;
import com.irtrains.train_service.model.train_route.trainRoute;


import com.irtrains.train_service.model.trainSeatAvailability.trainSeatAvailability;
import com.irtrains.train_service.service.train.trainService;
import com.irtrains.train_service.DTO.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/trains")
@CrossOrigin(origins = "*")
@Validated
public class trainController {

    private final trainService trainService;

    public trainController(trainService trainService) {
        this.trainService = trainService;
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

    /* ========================= Create ========================= */
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody train payload) {
        try {
            train created = trainService.createTrain(payload);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException ex) {
            return error(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        } catch (Exception ex) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error creating train", ex);
        }
    }

    @PostMapping("/coach")
    public ResponseEntity<?> createWithCoaches(@RequestBody CoachDTO coachDTO){
        try {
            CoachDTO created = trainService.createTrainCoach(coachDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException ex) {
            return error(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        } catch (Exception ex) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error creating train with coaches", ex);
        }
    }

    @PostMapping("/avail")
    public ResponseEntity<?> createWithAvailability(){
        try {
            List<trainSeatAvailability> created = trainService.addSeatAvailability();
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException ex) {
            return error(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        } catch (Exception ex) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error creating train with availability", ex);
        }
    }


    @PostMapping("/dto")
    public ResponseEntity<?> create(@RequestBody TrainDTO trainDTO){
        try {
            TrainDTO created = trainService.createTrain(trainDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException ex) {
            return error(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        } catch (Exception ex) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error creating train", ex);
        }
    }

    @PostMapping("/bulk-create")
    public ResponseEntity<?> bulkCreateTrains(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return error(HttpStatus.BAD_REQUEST, "File is empty", null);
        }

        String filename = file.getOriginalFilename();
        try {
            List<train> trains;
            if (filename != null && filename.toLowerCase().endsWith(".json")) {
                trains = trainService.createTrainsFromJson(file.getInputStream());
            } else if (filename != null && filename.toLowerCase().endsWith(".csv")) {
                trains = trainService.createTrainsFromCsv(file.getInputStream());
            } else {
                return error(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported file type. Please upload a .json or .csv file.", null);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(trains);
        } catch (IOException e) {
            return error(HttpStatus.BAD_REQUEST, "Failed to parse the uploaded file.", e);
        } catch (Exception e) {
//            e.printStackTrace();
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred during bulk creation.", e);
        }
    }

    @PostMapping("/coaches/bulk-create")
    public ResponseEntity<?> bulkCreateCoaches(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return error(HttpStatus.BAD_REQUEST, "File is empty", null);
        }

        try {
            trainService.createTrainCoachesFromCsv(file);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Train coaches created successfully from CSV."));
        } catch (IOException e) {
            return error(HttpStatus.BAD_REQUEST, "Failed to parse the uploaded CSV file.", e);
        } catch (Exception e) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred during bulk coach creation.", e);
        }
    }

    @PostMapping("/train/bulk-create")
    public ResponseEntity<?> bulkCreateTrain(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return error(HttpStatus.BAD_REQUEST, "File is empty", null);
        }

        try {
            trainService.processAndSaveRoutes(file);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Train data with routes created successfully from CSV."));
        } catch (IOException e) {
            return error(HttpStatus.BAD_REQUEST, "Failed to parse the uploaded CSV file.", e);
        } catch (Exception e) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred during bulk coach creation.", e);
        }
    }

    /* ========================= Update (full) ========================= */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") String id, @Valid @RequestBody train payload) {
        try {
            if (payload.getTrainId() == null || !payload.getTrainId().equals(id)) {
                // Ensure path id and body id match (integrity)
                return error(HttpStatus.BAD_REQUEST, "trainId in path and body must match", null);
            }
            if (trainService.findById(id).isEmpty()) {
                return error(HttpStatus.NOT_FOUND, "Train with id " + id + " not found", null);
            }
            train updated = trainService.updateTrain(payload);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return error(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        } catch (Exception ex) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error updating train", ex);
        }
    }

    /* ========================= Read ========================= */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        try {
            return trainService.findById(id)
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> error(HttpStatus.NOT_FOUND, "Train with id " + id + " not found", null));
        } catch (Exception ex) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error fetching train", ex);
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getByName(@PathVariable String name) {
        try {
            return trainService.findByName(name)
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> error(HttpStatus.NOT_FOUND, "Train with name '" + name + "' not found", null));
        } catch (Exception ex) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error fetching train by name", ex);
        }
    }

    /* ========================= Existence ========================= */
    @GetMapping("/exists/id/{id}")
    public ResponseEntity<Map<String, Object>> existsById(@PathVariable String id) {
        boolean exists = trainService.existsByTrainId(id);
        Map<String, Object> body = Map.of("trainId", id, "exists", exists);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/exists/name/{name}")
    public ResponseEntity<Map<String, Object>> existsByName(@PathVariable String name) {
        boolean exists = trainService.existsByName(name);
        Map<String, Object> body = Map.of("name", name, "exists", exists);
        return ResponseEntity.ok(body);
    }

    /* ========================= List & Search ========================= */
    @GetMapping
    public ResponseEntity<?> list(@RequestParam(value = "term", required = false) String term,
                                  @RequestParam(value = "type", required = false) Type type,
                                  @RequestParam(value = "source", required = false) String source,
                                  @RequestParam(value = "destination", required = false) String destination) {
        try {
            // Priority: if term provided -> search
            if (term != null && !term.isBlank()) {
                return ResponseEntity.ok(trainService.searchByNameOrId(term));
            }
            // Filter combinations
            if (source != null && destination != null) {
                return ResponseEntity.ok(trainService.findBySourceAndDestination(source, destination));
            }
            if (source != null) {
                return ResponseEntity.ok(trainService.findBySourceStation(source));
            }
            if (destination != null) {
                return ResponseEntity.ok(trainService.findByDestinationStation(destination));
            }
            if (type != null) {
                return ResponseEntity.ok(trainService.findByType(type));
            }
            return ResponseEntity.ok(trainService.findAll());
        } catch (Exception ex) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error listing trains", ex);
        }
    }

//    @GetMapping('/src-dest')
//    public ResponseEntity<?>

    /* ========================= Route Queries ========================= */
    @GetMapping("/route")
    public ResponseEntity<?> route(@RequestParam("from") String from,
                                   @RequestParam("to") String to, @RequestParam("date")LocalDate date) {
        try {
            return ResponseEntity.ok(trainService.findTrains(from, to, date));
        } catch (Exception ex) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error fetching route", ex);
        }
    }

    @GetMapping("/between")
    public ResponseEntity<?> between(@RequestParam("a") String stationA,
                                     @RequestParam("b") String stationB) {
        try {
            return ResponseEntity.ok(trainService.findBidirectionalBetween(stationA, stationB));
        } catch (Exception ex) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error fetching bidirectional trains", ex);
        }
    }

    /* ========================= Deletion ========================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") String id) {
        try {
            if (trainService.findById(id).isEmpty()) {
                return error(HttpStatus.NOT_FOUND, "Train with id " + id + " not found", null);
            }
            trainService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return error(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        } catch (Exception ex) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error deleting train", ex);
        }
    }

    @DeleteMapping("/name/{name}")
    public ResponseEntity<?> deleteByName(@PathVariable String name) {
        try {
            if (trainService.findByName(name).isEmpty()) {
                return error(HttpStatus.NOT_FOUND, "Train with name '" + name + "' not found", null);
            }
            trainService.deleteByName(name);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return error(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        } catch (Exception ex) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error deleting train by name", ex);
        }
    }
}