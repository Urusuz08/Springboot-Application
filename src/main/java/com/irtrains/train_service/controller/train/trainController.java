package com.irtrains.train_service.controller.train;

import com.irtrains.train_service.model.enums.Type;
import com.irtrains.train_service.model.train.train;
import com.irtrains.train_service.service.train.trainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
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

    /* ========================= Route Queries ========================= */
    @GetMapping("/route")
    public ResponseEntity<?> route(@RequestParam("from") String from,
                                   @RequestParam("to") String to) {
        try {
            return ResponseEntity.ok(trainService.findBySourceAndDestination(from, to));
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