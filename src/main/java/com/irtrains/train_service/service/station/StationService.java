package com.irtrains.train_service.service.station;

import com.irtrains.train_service.model.enums.State;
import com.irtrains.train_service.model.station.Station;
import com.irtrains.train_service.repository.station.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public Station createStation(Station s) { return stationRepository.save(s); }

    @Transactional
    public Station updateStation(Station s) { return stationRepository.save(s); }

    public Optional<Station> findById(String code) { return stationRepository.findById(code); }

    public List<Station> findAll() { return stationRepository.findAll(); }

    @Transactional
    public void deleteById(String code) { stationRepository.deleteById(code); }

    public List<Station> findByState(State state) { return stationRepository.findByState(state); }

    public List<Station> findByPlace(String place) { return stationRepository.findByPlace(place); }

    public List<Station> findByPlaceContainingIgnoreCase(String place) { return stationRepository.findByPlaceContainingIgnoreCase(place); }

    public List<Station> findByNameContainingIgnoreCase(String name) { return stationRepository.findByNameContainingIgnoreCase(name); }

    public Optional<Station> findByName(String name) { return stationRepository.findByName(name); }

    public boolean existsByName(String name) { return stationRepository.existsByName(name); }

    public boolean existsByCode(String code) { return stationRepository.existsByCode(code); }

    public List<Station> findByStateAndPlace(State state, String place) { return stationRepository.findByStateAndPlace(state, place); }

    public List<Station> findStationsByStateAndPlaceContaining(State state, String place) { return stationRepository.findStationsByStateAndPlaceContaining(state, place); }

    public List<Station> searchStationsByNameOrPlace(String term) { return stationRepository.searchStationsByNameOrPlace(term); }

    public List<Station> findByStateIn(List<State> states) { return stationRepository.findByStateIn(states); }

    public Long countStationsByState(State state) { return stationRepository.countStationsByState(state); }

    public List<String> findDistinctPlacesByState(State state) { return stationRepository.findDistinctPlacesByState(state); }

    public List<Station> findAllOrderedByStateAndPlace() { return stationRepository.findAllOrderedByStateAndPlace(); }

    @Transactional
    public void deleteByCode(String code) { stationRepository.deleteByCode(code); }

    @Transactional
    public void deleteByName(String name) { stationRepository.deleteByName(name); }
}
