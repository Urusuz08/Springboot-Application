package com.irtrains.train_service.controller.station;

import com.irtrains.train_service.model.enums.State;
import com.irtrains.train_service.model.station.Station;
import com.irtrains.train_service.service.station.StationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.io.IOException;

@RestController
@RequestMapping("/api/stations")
@CrossOrigin(origins = "*")
public class StationController {

    private final StationService stationService;

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

    public StationController(StationService stationService) {

        this.stationService = stationService;
    }

    // Create new station
    @PostMapping
    public ResponseEntity<Station> createStation(@RequestBody Station station) {
        try {
            Station created = stationService.createStation(station);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/bulk-create")
    public ResponseEntity<?> bulkCreateStations(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String filename = file.getOriginalFilename();
        try {
            List<Station> stations;
            if (filename != null && filename.toLowerCase().endsWith(".json")) {
                stations = stationService.createStationsFromJson(file.getInputStream());
            } else if (filename != null && filename.toLowerCase().endsWith(".csv")) {
                stations = stationService.createStationsFromCsv(file.getInputStream());
            } else {
                return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            }
            return new ResponseEntity<>(stations, HttpStatus.CREATED);
        } catch (Exception e) {
//            e.printStackTrace();
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing file", e);
        }
    }

    // Update station
    @PutMapping
    public ResponseEntity<Station> updateStation(@RequestBody Station station) {
        try {
            Station updated = stationService.updateStation(station);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST );
        }
    }

    // Get station by code
    @GetMapping("/{code}")
    public ResponseEntity<Station> getStationByCode(@PathVariable String code) {
        try {
            Optional<Station> st = stationService.findById(code);
            return st.map(station -> new ResponseEntity<>(station, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all stations
    @GetMapping
    public ResponseEntity<List<Station>> getAllStations() {
        try {
            List<Station> stations = stationService.findAll();
            return new ResponseEntity<>(stations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete station by code
    @DeleteMapping("/{code}")
    public ResponseEntity<String> deleteStationByCode(@PathVariable String code) {
        if(!stationService.existsByCode(code)) {
            return new ResponseEntity<>("Station with code " + code + " does not exist", HttpStatus.NOT_FOUND);
        }
        try {
            stationService.deleteById(code);
            return new ResponseEntity<>("Station deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete station", HttpStatus.BAD_REQUEST);
        }
    }

    // Get stations by state
    @GetMapping("/state/{state}")
    public ResponseEntity<List<Station>> getStationsByState(@PathVariable State state) {
        try {
            List<Station> stations = stationService.findByState(state);
            return new ResponseEntity<>(stations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get stations by place
    @GetMapping("/place/{place}")
    public ResponseEntity<List<Station>> getStationsByPlace(@PathVariable String place) {
        try {
            List<Station> stations = stationService.findByPlace(place);
            return new ResponseEntity<>(stations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Search stations by place (partial match)
    @GetMapping("/search/place")
    public ResponseEntity<List<Station>> searchStationsByPlace(@RequestParam String place) {
        try {
            List<Station> stations = stationService.findByPlaceContainingIgnoreCase(place);
            return new ResponseEntity<>(stations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Search stations by name (partial match)
    @GetMapping("/search/name")
    public ResponseEntity<List<Station>> searchStationsByName(@RequestParam String name) {
        try {
            List<Station> stations = stationService.findByNameContainingIgnoreCase(name);
            return new ResponseEntity<>(stations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get station by exact name
    @GetMapping("/name/{name}")
    public ResponseEntity<Station> getStationByName(@PathVariable String name) {
        try {
            Optional<Station> st = stationService.findByName(name);
            return st.map(station -> new ResponseEntity<>(station, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Check if station exists by name
    @GetMapping("/exists/name/{name}")
    public ResponseEntity<Boolean> existsByName(@PathVariable String name) {
        try {
            boolean exists = stationService.existsByName(name);
            return new ResponseEntity<>(exists, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Check if station exists by code
    @GetMapping("/exists/code/{code}")
    public ResponseEntity<Boolean> existsByCode(@PathVariable String code) {
        try {
            boolean exists = stationService.existsByCode(code);
            return new ResponseEntity<>(exists, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get stations by state and place
    @GetMapping("/state/{state}/place/{place}")
    public ResponseEntity<List<Station>> getStationsByStateAndPlace(@PathVariable State state, @PathVariable String place) {
        try {
            List<Station> stations = stationService.findByStateAndPlace(state, place);
            return new ResponseEntity<>(stations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Search stations by state and place containing
    @GetMapping("/search/state-place")
    public ResponseEntity<List<Station>> searchStationsByStateAndPlace(@RequestParam State state, @RequestParam String place) {
        try {
            List<Station> stations = stationService.findStationsByStateAndPlaceContaining(state, place);
            return new ResponseEntity<>(stations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Global search by name or place
    @GetMapping("/search")
    public ResponseEntity<List<Station>> searchStations(@RequestParam String searchTerm) {
        try {
            List<Station> stations = stationService.searchStationsByNameOrPlace(searchTerm);
            return new ResponseEntity<>(stations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get stations by multiple states
    @PostMapping("/states")
    public ResponseEntity<List<Station>> getStationsByStates(@RequestBody List<State> states) {
        try {
            List<Station> stations = stationService.findByStateIn(states);
            return new ResponseEntity<>(stations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Count stations by state
    @GetMapping("/count/state/{state}")
    public ResponseEntity<Long> countStationsByState(@PathVariable State state) {
        try {
            Long count = stationService.countStationsByState(state);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(0L, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get distinct places by state
    @GetMapping("/places/state/{state}")
    public ResponseEntity<List<String>> getDistinctPlacesByState(@PathVariable State state) {
        try {
            List<String> places = stationService.findDistinctPlacesByState(state);
            return new ResponseEntity<>(places, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all stations ordered by state and place
    @GetMapping("/ordered")
    public ResponseEntity<List<Station>> getAllStationsOrdered() {
        try {
            List<Station> stations = stationService.findAllOrderedByStateAndPlace();
            return new ResponseEntity<>(stations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete station by code (alias)
    @DeleteMapping("/code/{code}")
    public ResponseEntity<String> deleteStationByCodeAlias(@PathVariable String code) {
        try {
            stationService.deleteByCode(code);
            return new ResponseEntity<>("Station deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete station", HttpStatus.BAD_REQUEST);
        }
    }

    // Delete station by name
    @DeleteMapping("/name/{name}")
    public ResponseEntity<String> deleteStationByName(@PathVariable String name) {
        try {
            stationService.deleteByName(name);
            return new ResponseEntity<>("Station deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete station", HttpStatus.BAD_REQUEST);
        }
    }
}
