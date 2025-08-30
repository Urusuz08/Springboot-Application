package com.irtrains.train_service.repository.train;

import com.irtrains.train_service.model.enums.State;
import com.irtrains.train_service.model.train.train;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Train Repository Tests")
class TrainRepoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private trainRepository trainRepository;

    private train train1;
    private train train2;
    private train train3;
    private train train4;

    @BeforeEach
    void setUp() {
        // Create test data
        train1 = new train();
        train1.setCode("12345");
        train1.setName("Rajdhani Express");
        train1.setState(State.MAHARASHTRA);
        train1.setPlace("Mumbai");

        train2 = new train();
        train2.setCode("54321");
        train2.setName("Shatabdi Express");
        train2.setState(State.MAHARASHTRA);
        train2.setPlace("Pune");

        train3 = new train();
        train3.setCode("11111");
        train3.setName("Kerala Express");
        train3.setState(State.KERALA);
        train3.setPlace("Kochi");

        train4 = new train();
        train4.setCode("22222");
        train4.setName("Gujarat Mail");
        train4.setState(State.GUJARAT);
        train4.setPlace("Ahmedabad");

        // Persist test data
        entityManager.persistAndFlush(train1);
        entityManager.persistAndFlush(train2);
        entityManager.persistAndFlush(train3);
        entityManager.persistAndFlush(train4);
    }

    @Test
    @DisplayName("Find trains by state")
    void testFindByState() {
        List<train> maharashtraTrains = trainRepository.findByState(State.MAHARASHTRA);
        assertEquals(2, maharashtraTrains.size());
        assertTrue(maharashtraTrains.stream().allMatch(t -> t.getState() == State.MAHARASHTRA));
    }

    @Test
    @DisplayName("Find trains by place")
    void testFindByPlace() {
        List<train> mumbaiTrains = trainRepository.findByPlace("Mumbai");
        assertEquals(1, mumbaiTrains.size());
        assertEquals("Mumbai", mumbaiTrains.get(0).getPlace());
    }

    @Test
    @DisplayName("Find trains by place containing ignore case")
    void testFindByPlaceContainingIgnoreCase() {
        List<train> trainsWithPune = trainRepository.findByPlaceContainingIgnoreCase("pune");
        assertEquals(1, trainsWithPune.size());
        assertEquals("Pune", trainsWithPune.get(0).getPlace());
    }

    @Test
    @DisplayName("Find trains by name containing ignore case")
    void testFindByNameContainingIgnoreCase() {
        List<train> expressTrains = trainRepository.findByNameContainingIgnoreCase("express");
        assertEquals(3, expressTrains.size());
    }

    @Test
    @DisplayName("Find train by exact name")
    void testFindByName() {
        Optional<train> foundTrain = trainRepository.findByName("Rajdhani Express");
        assertTrue(foundTrain.isPresent());
        assertEquals("12345", foundTrain.get().getCode());
    }

    @Test
    @DisplayName("Find train by exact name - not found")
    void testFindByNameNotFound() {
        Optional<train> foundTrain = trainRepository.findByName("Non Existent Train");
        assertFalse(foundTrain.isPresent());
    }

    @Test
    @DisplayName("Check if train exists by name")
    void testExistsByName() {
        assertTrue(trainRepository.existsByName("Rajdhani Express"));
        assertFalse(trainRepository.existsByName("Non Existent Train"));
    }

    @Test
    @DisplayName("Check if train exists by code")
    void testExistsByCode() {
        assertTrue(trainRepository.existsByCode("12345"));
        assertFalse(trainRepository.existsByCode("99999"));
    }

    @Test
    @DisplayName("Find trains by state and place")
    void testFindByStateAndPlace() {
        List<train> trains = trainRepository.findByStateAndPlace(State.MAHARASHTRA, "Mumbai");
        assertEquals(1, trains.size());
        assertEquals("Rajdhani Express", trains.get(0).getName());
    }

    @Test
    @DisplayName("Find trains by state and place containing")
    void testFindTrainsByStateAndPlaceContaining() {
        List<train> trains = trainRepository.findTrainsByStateAndPlaceContaining(State.MAHARASHTRA, "Mum");
        assertEquals(1, trains.size());
        assertEquals("Mumbai", trains.get(0).getPlace());
    }

    @Test
    @DisplayName("Search trains by name or place")
    void testSearchTrainsByNameOrPlace() {
        List<train> trains = trainRepository.searchTrainsByNameOrPlace("Kerala");
        assertEquals(1, trains.size());
        assertEquals("Kerala Express", trains.get(0).getName());
    }

    @Test
    @DisplayName("Find trains by multiple states")
    void testFindByStateIn() {
        List<State> states = Arrays.asList(State.KERALA, State.GUJARAT);
        List<train> trains = trainRepository.findByStateIn(states);
        assertEquals(2, trains.size());
    }

    @Test
    @DisplayName("Count trains by state")
    void testCountTrainsByState() {
        Long count = trainRepository.countTrainsByState(State.MAHARASHTRA);
        assertEquals(2L, count);
    }

    @Test
    @DisplayName("Find distinct places by state")
    void testFindDistinctPlacesByState() {
        List<String> places = trainRepository.findDistinctPlacesByState(State.MAHARASHTRA);
        assertEquals(2, places.size());
        assertTrue(places.contains("Mumbai"));
        assertTrue(places.contains("Pune"));
    }

    @Test
    @DisplayName("Find all trains ordered by state and place")
    void testFindAllOrderedByStateAndPlace() {
        List<train> trains = trainRepository.findAllOrderedByStateAndPlace();
        assertEquals(4, trains.size());
        // Verify ordering (should be sorted by state, then place, then name)
        assertNotNull(trains);
    }

    @Test
    @DisplayName("Save new train")
    void testSaveNewTrain() {
        train newTrain = new train();
        newTrain.setCode("99999");
        newTrain.setName("Test Express");
        newTrain.setState(State.TAMIL_NADU);
        newTrain.setPlace("Chennai");

        train savedTrain = trainRepository.save(newTrain);
        assertNotNull(savedTrain);
        assertEquals("99999", savedTrain.getCode());

        Optional<train> foundTrain = trainRepository.findById("99999");
        assertTrue(foundTrain.isPresent());
    }

    @Test
    @DisplayName("Update existing train")
    void testUpdateExistingTrain() {
        Optional<train> existingTrain = trainRepository.findById("12345");
        assertTrue(existingTrain.isPresent());

        train trainToUpdate = existingTrain.get();
        trainToUpdate.setPlace("New Mumbai");

        train updatedTrain = trainRepository.save(trainToUpdate);
        assertEquals("New Mumbai", updatedTrain.getPlace());
    }

    @Test
    @Transactional
    @DisplayName("Delete train by code")
    void testDeleteByCode() {
        assertTrue(trainRepository.existsByCode("12345"));
        trainRepository.deleteByCode("12345");
        trainRepository.flush();
        assertFalse(trainRepository.existsByCode("12345"));
    }

    @Test
    @Transactional
    @DisplayName("Delete train by name")
    void testDeleteByName() {
        assertTrue(trainRepository.existsByName("Shatabdi Express"));
        trainRepository.deleteByName("Shatabdi Express");
        trainRepository.flush();
        assertFalse(trainRepository.existsByName("Shatabdi Express"));
    }

    @Test
    @DisplayName("Find by ID")
    void testFindById() {
        Optional<train> foundTrain = trainRepository.findById("12345");
        assertTrue(foundTrain.isPresent());
        assertEquals("Rajdhani Express", foundTrain.get().getName());
    }

    @Test
    @DisplayName("Find all trains")
    void testFindAll() {
        List<train> allTrains = trainRepository.findAll();
        assertEquals(4, allTrains.size());
    }
}
