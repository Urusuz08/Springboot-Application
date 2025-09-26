package com.irtrains.train_service.repository.station;

import com.irtrains.train_service.model.enums.State;
import com.irtrains.train_service.model.station.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station, String> {

    List<Station> findByState(State state);

    List<Station> findByPlace(String place);
    List<Station> findByPlaceContainingIgnoreCase(String place);

    List<Station> findByNameContainingIgnoreCase(String name);

    Optional<Station> findByName(String name);

    boolean existsByName(String name);
    boolean existsByCode(String code);

    List<Station> findByStateAndPlace(State state, String place);

    @Query("SELECT s FROM Station s WHERE s.state = :state AND s.place LIKE %:place%")
    List<Station> findStationsByStateAndPlaceContaining(@Param("state") State state, @Param("place") String place);

    @Query("SELECT s FROM Station s WHERE s.name LIKE %:searchTerm% OR s.place LIKE %:searchTerm%")
    List<Station> searchStationsByNameOrPlace(@Param("searchTerm") String searchTerm);

    @Query("SELECT s FROM Station s WHERE s.state IN :states")
    List<Station> findByStateIn(@Param("states") List<State> states);

    @Query("SELECT COUNT(s) FROM Station s WHERE s.state = :state")
    Long countStationsByState(@Param("state") State state);

    @Query("SELECT DISTINCT s.place FROM Station s WHERE s.state = :state ORDER BY s.place")
    List<String> findDistinctPlacesByState(@Param("state") State state);

    @Query("SELECT s FROM Station s ORDER BY s.state, s.place, s.name")
    List<Station> findAllOrderedByStateAndPlace();

    void deleteByCode(String code);
    void deleteByName(String name);
}
