package com.irtrains.train_service.service.train;

import com.irtrains.train_service.model.enums.Type;
import com.irtrains.train_service.model.train.train;
import com.irtrains.train_service.repository.train.TrainRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for Train aggregate. Provides validation, transactional boundaries and
 * a stable API surface decoupled from controller & persistence details.
 */
@Service
@Transactional(readOnly = true)
public class trainService {

    private final TrainRepository trainRepository;

    public trainService(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    /* ===================== Creation ===================== */
    @Transactional
    public train createTrain(train t) {
        validateNewTrain(t);
        try {
            return trainRepository.save(t);
        } catch (DataIntegrityViolationException ex) {
            // Re-throw with clearer message while preserving root cause
            throw new IllegalArgumentException("Train constraints violated (duplicate id or name)", ex);
        }
    }

    /* ===================== Update ===================== */
    @Transactional
    public train updateTrain(train t) {
        if (t.getTrainId() == null || t.getTrainId().isBlank()) {
            throw new IllegalArgumentException("trainId is required for update");
        }
        train existing = trainRepository.findById(t.getTrainId())
                .orElseThrow(() -> new IllegalArgumentException("Train with id " + t.getTrainId() + " not found"));

        // Preserve fields not provided if partial updates are allowed (currently all required, so direct copy)
        existing.setName(t.getName());
        existing.setType(t.getType());
        existing.setSourceStation(t.getSourceStation());
        existing.setDestinationStation(t.getDestinationStation());

        try {
            return trainRepository.save(existing);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Update violates unique constraints (possibly name)", ex);
        }
    }

    /* ===================== Retrieval ===================== */
    public Optional<train> findById(String id) { // PK lookup
        return trainRepository.findById(id);
    }

    public List<train> findAll() {
        return trainRepository.findAll();
    }

    public Optional<train> findByName(String name) {
        return trainRepository.findByName(name);
    }

    public List<train> findByType(Type type) {
        return trainRepository.findByType(type);
    }

    public List<train> findBySourceStation(String sourceStation) {
        return trainRepository.findBySourceStation(sourceStation);
    }

    public List<train> findByDestinationStation(String destinationStation) {
        return trainRepository.findByDestinationStation(destinationStation);
    }

    public List<train> findBySourceAndDestination(String source, String destination) {
        return trainRepository.findBySourceStationAndDestinationStation(source, destination);
    }

    public List<train> findBidirectionalBetween(String stationA, String stationB) {
        return trainRepository.findBidirectionalBetween(stationA, stationB);
    }

    public List<train> searchByNameOrId(String term) {
        if (term == null || term.isBlank()) return List.of();
        return trainRepository.searchByNameOrId(term.trim());
    }

    /* ===================== Existence ===================== */
    public boolean existsByName(String name) {
        return name != null && trainRepository.existsByName(name);
    }

    public boolean existsByTrainId(String trainId) {
        return trainId != null && trainRepository.existsByTrainId(trainId);
    }

    /* ===================== Deletion ===================== */
    @Transactional
    public void deleteById(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("trainId is required for delete");
        if (!trainRepository.existsByTrainId(id)) return; // idempotent
        trainRepository.deleteById(id);
    }

    @Transactional
    public void deleteByTrainId(String trainId) { // alias for clarity
        deleteById(trainId);
    }

    @Transactional
    public void deleteByName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name is required for delete");
        trainRepository.deleteByName(name);
    }

    /* ===================== Validation Helpers ===================== */
    private void validateNewTrain(train t) {
        if (t == null) throw new IllegalArgumentException("Train cannot be null");
        if (t.getTrainId() == null || t.getTrainId().isBlank()) {
            throw new IllegalArgumentException("trainId is required");
        }
        if (existsByTrainId(t.getTrainId())) {
            throw new IllegalArgumentException("Train with id " + t.getTrainId() + " already exists");
        }
        if (t.getName() == null || t.getName().isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (existsByName(t.getName())) {
            throw new IllegalArgumentException("Train with name '" + t.getName() + "' already exists");
        }
        if (t.getType() == null) {
            throw new IllegalArgumentException("type is required");
        }
        if (t.getSourceStation() == null || t.getSourceStation().isBlank()) {
            throw new IllegalArgumentException("sourceStation is required");
        }
        if (t.getDestinationStation() == null || t.getDestinationStation().isBlank()) {
            throw new IllegalArgumentException("destinationStation is required");
        }
    }
}