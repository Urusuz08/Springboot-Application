package com.irtrains.train_service.controller.train;

import com.irtrains.train_service.model.enums.State;
import com.irtrains.train_service.model.train.train;
import com.irtrains.train_service.service.train.trainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/trains")
@CrossOrigin(origins = "*")
public class trainController {

    @Autowired
    private trainService trainService;

    // Create new train
    @PostMapping
    public ResponseEntity<train> createTrain(@RequestBody train train) {
        try {
            train createdTrain = trainService.createTrain(train);
            return new ResponseEntity<>(createdTrain, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Update train
    @PutMapping
    public ResponseEntity<train> updateTrain(@RequestBody train train) {
        try {
            train updatedTrain = trainService.updateTrain(train);
            return new ResponseEntity<>(updatedTrain, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Get train by ID
    @GetMapping("/{id}")
    public ResponseEntity<train> getTrainById(@PathVariable String id) {
        try {
            Optional<train> train = trainService.findById(id);
            if (train.isPresent()) {
                return new ResponseEntity<>(train.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all trains
    @GetMapping
    public ResponseEntity<List<train>> getAllTrains() {
        try {
            List<train> trains = trainService.findAll();
            return new ResponseEntity<>(trains, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete train by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTrainById(@PathVariable String id) {
        try {
            trainService.deleteById(id);
            return new ResponseEntity<>("Train deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete train", HttpStatus.BAD_REQUEST);
        }
    }

    // Get trains by state
    @GetMapping("/state/{state}")
    public ResponseEntity<List<train>> getTrainsByState(@PathVariable State state) {
        try {
            List<train> trains = trainService.findByState(state);
            return new ResponseEntity<>(trains, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get trains by place
    @GetMapping("/place/{place}")
    public ResponseEntity<List<train>> getTrainsByPlace(@PathVariable String place) {
        try {
            List<train> trains = trainService.findByPlace(place);
            return new ResponseEntity<>(trains, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Search trains by place (partial match)
    @GetMapping("/search/place")
    public ResponseEntity<List<train>> searchTrainsByPlace(@RequestParam String place) {
        try {
            List<train> trains = trainService.findByPlaceContainingIgnoreCase(place);
            return new ResponseEntity<>(trains, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Search trains by name (partial match)
    @GetMapping("/search/name")
    public ResponseEntity<List<train>> searchTrainsByName(@RequestParam String name) {
        try {
            List<train> trains = trainService.findByNameContainingIgnoreCase(name);
            return new ResponseEntity<>(trains, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get train by exact name
    @GetMapping("/name/{name}")
    public ResponseEntity<train> getTrainByName(@PathVariable String name) {
        try {
            Optional<train> train = trainService.findByName(name);
            if (train.isPresent()) {
                return new ResponseEntity<>(train.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Check if train exists by name
    @GetMapping("/exists/name/{name}")
    public ResponseEntity<Boolean> existsByName(@PathVariable String name) {
        try {
            boolean exists = trainService.existsByName(name);
            return new ResponseEntity<>(exists, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Check if train exists by code
    @GetMapping("/exists/code/{code}")
    public ResponseEntity<Boolean> existsByCode(@PathVariable String code) {
        try {
            boolean exists = trainService.existsByCode(code);
            return new ResponseEntity<>(exists, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get trains by state and place
    @GetMapping("/state/{state}/place/{place}")
    public ResponseEntity<List<train>> getTrainsByStateAndPlace(@PathVariable State state, @PathVariable String place) {
        try {
            List<train> trains = trainService.findByStateAndPlace(state, place);
            return new ResponseEntity<>(trains, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Search trains by state and place containing
    @GetMapping("/search/state-place")
    public ResponseEntity<List<train>> searchTrainsByStateAndPlace(@RequestParam State state, @RequestParam String place) {
        try {
            List<train> trains = trainService.findTrainsByStateAndPlaceContaining(state, place);
            return new ResponseEntity<>(trains, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Global search by name or place
    @GetMapping("/search")
    public ResponseEntity<List<train>> searchTrains(@RequestParam String searchTerm) {
        try {
            List<train> trains = trainService.searchTrainsByNameOrPlace(searchTerm);
            return new ResponseEntity<>(trains, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get trains by multiple states
    @PostMapping("/states")
    public ResponseEntity<List<train>> getTrainsByStates(@RequestBody List<State> states) {
        try {
            List<train> trains = trainService.findByStateIn(states);
            return new ResponseEntity<>(trains, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Count trains by state
    @GetMapping("/count/state/{state}")
    public ResponseEntity<Long> countTrainsByState(@PathVariable State state) {
        try {
            Long count = trainService.countTrainsByState(state);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(0L, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get distinct places by state
    @GetMapping("/places/state/{state}")
    public ResponseEntity<List<String>> getDistinctPlacesByState(@PathVariable State state) {
        try {
            List<String> places = trainService.findDistinctPlacesByState(state);
            return new ResponseEntity<>(places, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all trains ordered by state and place
    @GetMapping("/ordered")
    public ResponseEntity<List<train>> getAllTrainsOrdered() {
        try {
            List<train> trains = trainService.findAllOrderedByStateAndPlace();
            return new ResponseEntity<>(trains, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete train by code
    @DeleteMapping("/code/{code}")
    public ResponseEntity<String> deleteTrainByCode(@PathVariable String code) {
        try {
            trainService.deleteByCode(code);
            return new ResponseEntity<>("Train deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete train", HttpStatus.BAD_REQUEST);
        }
    }

    // Delete train by name
    @DeleteMapping("/name/{name}")
    public ResponseEntity<String> deleteTrainByName(@PathVariable String name) {
        try {
            trainService.deleteByName(name);
            return new ResponseEntity<>("Train deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete train", HttpStatus.BAD_REQUEST);
        }
    }
}