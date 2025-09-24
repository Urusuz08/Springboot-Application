package com.irtrains.train_service.repository.train;

import com.irtrains.train_service.model.enums.Type;
import com.irtrains.train_service.model.train.train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainRepository extends JpaRepository<train, String> {
    // Primary key is trainId (String) handled by JpaRepository

    // Lookups by natural unique business keys / attributes
    Optional<train> findByTrainId(String trainId); // redundant (same as findById) but explicit
    Optional<train> findByName(String name);

    // Existence checks
    boolean existsByTrainId(String trainId); // redundant but convenient
    boolean existsByName(String name);

    // Attribute based queries
    List<train> findByType(Type type);
    List<train> findBySourceStation(String sourceStation);
    List<train> findByDestinationStation(String destinationStation);
    List<train> findBySourceStationAndDestinationStation(String sourceStation, String destinationStation);

    // Partial text search on name or trainId
    @Query("SELECT t FROM train t WHERE lower(t.name) LIKE lower(concat('%', :term, '%')) OR t.trainId LIKE concat('%', :term, '%')")
    List<train> searchByNameOrId(@Param("term") String term);

    // Fetch all trains running between two stations irrespective of direction
    @Query("SELECT t FROM train t WHERE (t.sourceStation = :stationA AND t.destinationStation = :stationB) OR (t.sourceStation = :stationB AND t.destinationStation = :stationA)")
    List<train> findBidirectionalBetween(@Param("stationA") String stationA, @Param("stationB") String stationB);

    // Delete operations
    void deleteByTrainId(String trainId); // same as deleteById but explicit
    void deleteByName(String name);
}