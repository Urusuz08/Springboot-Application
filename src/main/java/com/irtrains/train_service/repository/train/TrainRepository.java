package com.irtrains.train_service.repository.train;

import com.irtrains.train_service.model.enums.State;
import com.irtrains.train_service.model.train.train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainRepository extends JpaRepository<train, String> {

    // Find trains by state
    List<train> findByState(State state);

    // Find trains by place/city
    List<train> findByPlace(String place);
    List<train> findByPlaceContainingIgnoreCase(String place);

    // Find trains by name (partial search)
    List<train> findByNameContainingIgnoreCase(String name);

    // Find train by exact name
    Optional<train> findByName(String name);

    // Check if train exists
    boolean existsByName(String name);
    boolean existsByCode(String code);

    // Find trains by state and place
    List<train> findByStateAndPlace(State state, String place);

    // Custom queries for complex searches
    @Query("SELECT t FROM train t WHERE t.state = :state AND t.place LIKE %:place%")
    List<train> findTrainsByStateAndPlaceContaining(@Param("state") State state, @Param("place") String place);

    @Query("SELECT t FROM train t WHERE t.name LIKE %:searchTerm% OR t.place LIKE %:searchTerm%")
    List<train> searchTrainsByNameOrPlace(@Param("searchTerm") String searchTerm);

    // Find trains by multiple states
    @Query("SELECT t FROM train t WHERE t.state IN :states")
    List<train> findByStateIn(@Param("states") List<State> states);

    // Count trains by state
    @Query("SELECT COUNT(t) FROM train t WHERE t.state = :state")
    Long countTrainsByState(@Param("state") State state);

    // Get all unique places for a state
    @Query("SELECT DISTINCT t.place FROM train t WHERE t.state = :state ORDER BY t.place")
    List<String> findDistinctPlacesByState(@Param("state") State state);

    // Admin operations
    @Query("SELECT t FROM train t ORDER BY t.state, t.place, t.name")
    List<train> findAllOrderedByStateAndPlace();

    // Delete operations
    void deleteByCode(String code);
    void deleteByName(String name);
}