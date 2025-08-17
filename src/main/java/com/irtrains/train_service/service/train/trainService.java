package com.irtrains.train_service.service.train;

import com.irtrains.train_service.model.enums.State;
import com.irtrains.train_service.model.train.train;
import com.irtrains.train_service.repository.train.trainRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class trainService {

    private final trainRepository trainRepository;

    public trainService(trainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    // Create
    @Transactional
    public train createTrain(train t) {
        return trainRepository.save(t);
    }

    // Update
    @Transactional
    public train updateTrain(train t) {
        return trainRepository.save(t);
    }

    // Read
    public Optional<train> findById(String id) {
        return trainRepository.findById(id);
    }

    public List<train> findAll() {
        return trainRepository.findAll();
    }

    // Delete
    @Transactional
    public void deleteById(String id) {
        trainRepository.deleteById(id);
    }

    // Query methods mirroring repository
    public List<train> findByState(State state) {
        return trainRepository.findByState(state);
    }

    public List<train> findByPlace(String place) {
        return trainRepository.findByPlace(place);
    }

    public List<train> findByPlaceContainingIgnoreCase(String place) {
        return trainRepository.findByPlaceContainingIgnoreCase(place);
    }

    public List<train> findByNameContainingIgnoreCase(String name) {
        return trainRepository.findByNameContainingIgnoreCase(name);
    }

    public Optional<train> findByName(String name) {
        return trainRepository.findByName(name);
    }

    public boolean existsByName(String name) {
        return trainRepository.existsByName(name);
    }

    public boolean existsByCode(String code) {
        return trainRepository.existsByCode(code);
    }

    public List<train> findByStateAndPlace(State state, String place) {
        return trainRepository.findByStateAndPlace(state, place);
    }

    public List<train> findTrainsByStateAndPlaceContaining(State state, String place) {
        return trainRepository.findTrainsByStateAndPlaceContaining(state, place);
    }

    public List<train> searchTrainsByNameOrPlace(String searchTerm) {
        return trainRepository.searchTrainsByNameOrPlace(searchTerm);
    }

    public List<train> findByStateIn(List<State> states) {
        return trainRepository.findByStateIn(states);
    }

    public Long countTrainsByState(State state) {
        return trainRepository.countTrainsByState(state);
    }

    public List<String> findDistinctPlacesByState(State state) {
        return trainRepository.findDistinctPlacesByState(state);
    }

    public List<train> findAllOrderedByStateAndPlace() {
        return trainRepository.findAllOrderedByStateAndPlace();
    }

    @Transactional
    public void deleteByCode(String code) {
        trainRepository.deleteByCode(code);
    }

    @Transactional
    public void deleteByName(String name) {
        trainRepository.deleteByName(name);
    }
}