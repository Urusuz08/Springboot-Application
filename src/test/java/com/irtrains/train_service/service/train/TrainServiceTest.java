package com.irtrains.train_service.service.train;

import com.irtrains.train_service.model.enums.State;
import com.irtrains.train_service.model.train.train;
import com.irtrains.train_service.repository.train.TrainRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Train Service Tests")
class TrainServiceTest {

    @Mock
    private TrainRepository trainRepository;

    @InjectMocks
    private trainService trainService;

    private train testTrain1;
    private train testTrain2;
    private train testTrain3;

    @BeforeEach
    void setUp() {
        testTrain1 = new train();
        testTrain1.setCode("12345");
        testTrain1.setName("Rajdhani Express");
        testTrain1.setState(State.MAHARASHTRA);
        testTrain1.setPlace("Mumbai");

        testTrain2 = new train();
        testTrain2.setCode("54321");
        testTrain2.setName("Shatabdi Express");
        testTrain2.setState(State.MAHARASHTRA);
        testTrain2.setPlace("Pune");

        testTrain3 = new train();
        testTrain3.setCode("11111");
        testTrain3.setName("Kerala Express");
        testTrain3.setState(State.KERALA);
        testTrain3.setPlace("Kochi");
    }

    @Test
    @DisplayName("Create train should save and return train")
    void testCreateTrain() {
        when(trainRepository.save(testTrain1)).thenReturn(testTrain1);

        train result = trainService.createTrain(testTrain1);

        assertNotNull(result);
        assertEquals("12345", result.getCode());
        assertEquals("Rajdhani Express", result.getName());
        verify(trainRepository, times(1)).save(testTrain1);
    }

    @Test
    @DisplayName("Update train should save and return updated train")
    void testUpdateTrain() {
        testTrain1.setPlace("New Mumbai");
        when(trainRepository.save(testTrain1)).thenReturn(testTrain1);

        train result = trainService.updateTrain(testTrain1);

        assertNotNull(result);
        assertEquals("New Mumbai", result.getPlace());
        verify(trainRepository, times(1)).save(testTrain1);
    }

    @Test
    @DisplayName("Find by ID should return train when exists")
    void testFindByIdExists() {
        when(trainRepository.findById("12345")).thenReturn(Optional.of(testTrain1));

        Optional<train> result = trainService.findById("12345");

        assertTrue(result.isPresent());
        assertEquals("Rajdhani Express", result.get().getName());
        verify(trainRepository, times(1)).findById("12345");
    }

    @Test
    @DisplayName("Find by ID should return empty when not exists")
    void testFindByIdNotExists() {
        when(trainRepository.findById("99999")).thenReturn(Optional.empty());

        Optional<train> result = trainService.findById("99999");

        assertFalse(result.isPresent());
        verify(trainRepository, times(1)).findById("99999");
    }

    @Test
    @DisplayName("Find all should return list of trains")
    void testFindAll() {
        List<train> trains = Arrays.asList(testTrain1, testTrain2, testTrain3);
        when(trainRepository.findAll()).thenReturn(trains);

        List<train> result = trainService.findAll();

        assertEquals(3, result.size());
        assertEquals("Rajdhani Express", result.get(0).getName());
        verify(trainRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Delete by ID should call repository delete")
    void testDeleteById() {
        doNothing().when(trainRepository).deleteById("12345");

        trainService.deleteById("12345");

        verify(trainRepository, times(1)).deleteById("12345");
    }

    @Test
    @DisplayName("Find by state should return trains in that state")
    void testFindByState() {
        List<train> maharashtraTrains = Arrays.asList(testTrain1, testTrain2);
        when(trainRepository.findByState(State.MAHARASHTRA)).thenReturn(maharashtraTrains);

        List<train> result = trainService.findByState(State.MAHARASHTRA);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(t -> t.getState() == State.MAHARASHTRA));
        verify(trainRepository, times(1)).findByState(State.MAHARASHTRA);
    }

    @Test
    @DisplayName("Find by place should return trains in that place")
    void testFindByPlace() {
        List<train> mumbaiTrains = Arrays.asList(testTrain1);
        when(trainRepository.findByPlace("Mumbai")).thenReturn(mumbaiTrains);

        List<train> result = trainService.findByPlace("Mumbai");

        assertEquals(1, result.size());
        assertEquals("Mumbai", result.get(0).getPlace());
        verify(trainRepository, times(1)).findByPlace("Mumbai");
    }

    @Test
    @DisplayName("Find by place containing ignore case should work")
    void testFindByPlaceContainingIgnoreCase() {
        List<train> trains = Arrays.asList(testTrain1);
        when(trainRepository.findByPlaceContainingIgnoreCase("mum")).thenReturn(trains);

        List<train> result = trainService.findByPlaceContainingIgnoreCase("mum");

        assertEquals(1, result.size());
        assertEquals("Mumbai", result.get(0).getPlace());
        verify(trainRepository, times(1)).findByPlaceContainingIgnoreCase("mum");
    }

    @Test
    @DisplayName("Find by name containing ignore case should work")
    void testFindByNameContainingIgnoreCase() {
        List<train> expressTrains = Arrays.asList(testTrain1, testTrain2, testTrain3);
        when(trainRepository.findByNameContainingIgnoreCase("express")).thenReturn(expressTrains);

        List<train> result = trainService.findByNameContainingIgnoreCase("express");

        assertEquals(3, result.size());
        verify(trainRepository, times(1)).findByNameContainingIgnoreCase("express");
    }

    @Test
    @DisplayName("Find by name should return train when exists")
    void testFindByName() {
        when(trainRepository.findByName("Rajdhani Express")).thenReturn(Optional.of(testTrain1));

        Optional<train> result = trainService.findByName("Rajdhani Express");

        assertTrue(result.isPresent());
        assertEquals("12345", result.get().getCode());
        verify(trainRepository, times(1)).findByName("Rajdhani Express");
    }

    @Test
    @DisplayName("Find by name should return empty when not exists")
    void testFindByNameNotExists() {
        when(trainRepository.findByName("Non Existent Train")).thenReturn(Optional.empty());

        Optional<train> result = trainService.findByName("Non Existent Train");

        assertFalse(result.isPresent());
        verify(trainRepository, times(1)).findByName("Non Existent Train");
    }

    @Test
    @DisplayName("Exists by name should return true when exists")
    void testExistsByNameTrue() {
        when(trainRepository.existsByName("Rajdhani Express")).thenReturn(true);

        boolean result = trainService.existsByName("Rajdhani Express");

        assertTrue(result);
        verify(trainRepository, times(1)).existsByName("Rajdhani Express");
    }

    @Test
    @DisplayName("Exists by name should return false when not exists")
    void testExistsByNameFalse() {
        when(trainRepository.existsByName("Non Existent Train")).thenReturn(false);

        boolean result = trainService.existsByName("Non Existent Train");

        assertFalse(result);
        verify(trainRepository, times(1)).existsByName("Non Existent Train");
    }

    @Test
    @DisplayName("Exists by code should return true when exists")
    void testExistsByCodeTrue() {
        when(trainRepository.existsByCode("12345")).thenReturn(true);

        boolean result = trainService.existsByCode("12345");

        assertTrue(result);
        verify(trainRepository, times(1)).existsByCode("12345");
    }

    @Test
    @DisplayName("Exists by code should return false when not exists")
    void testExistsByCodeFalse() {
        when(trainRepository.existsByCode("99999")).thenReturn(false);

        boolean result = trainService.existsByCode("99999");

        assertFalse(result);
        verify(trainRepository, times(1)).existsByCode("99999");
    }

    @Test
    @DisplayName("Find by state and place should work")
    void testFindByStateAndPlace() {
        List<train> trains = Arrays.asList(testTrain1);
        when(trainRepository.findByStateAndPlace(State.MAHARASHTRA, "Mumbai")).thenReturn(trains);

        List<train> result = trainService.findByStateAndPlace(State.MAHARASHTRA, "Mumbai");

        assertEquals(1, result.size());
        assertEquals("Rajdhani Express", result.get(0).getName());
        verify(trainRepository, times(1)).findByStateAndPlace(State.MAHARASHTRA, "Mumbai");
    }

    @Test
    @DisplayName("Find trains by state and place containing should work")
    void testFindTrainsByStateAndPlaceContaining() {
        List<train> trains = Arrays.asList(testTrain1);
        when(trainRepository.findTrainsByStateAndPlaceContaining(State.MAHARASHTRA, "Mum"))
                .thenReturn(trains);

        List<train> result = trainService.findTrainsByStateAndPlaceContaining(State.MAHARASHTRA, "Mum");

        assertEquals(1, result.size());
        assertEquals("Mumbai", result.get(0).getPlace());
        verify(trainRepository, times(1)).findTrainsByStateAndPlaceContaining(State.MAHARASHTRA, "Mum");
    }

    @Test
    @DisplayName("Search trains by name or place should work")
    void testSearchTrainsByNameOrPlace() {
        List<train> trains = Arrays.asList(testTrain3);
        when(trainRepository.searchTrainsByNameOrPlace("Kerala")).thenReturn(trains);

        List<train> result = trainService.searchTrainsByNameOrPlace("Kerala");

        assertEquals(1, result.size());
        assertEquals("Kerala Express", result.get(0).getName());
        verify(trainRepository, times(1)).searchTrainsByNameOrPlace("Kerala");
    }

    @Test
    @DisplayName("Find by state in should work")
    void testFindByStateIn() {
        List<State> states = Arrays.asList(State.KERALA, State.GUJARAT);
        List<train> trains = Arrays.asList(testTrain3);
        when(trainRepository.findByStateIn(states)).thenReturn(trains);

        List<train> result = trainService.findByStateIn(states);

        assertEquals(1, result.size());
        verify(trainRepository, times(1)).findByStateIn(states);
    }

    @Test
    @DisplayName("Count trains by state should work")
    void testCountTrainsByState() {
        when(trainRepository.countTrainsByState(State.MAHARASHTRA)).thenReturn(2L);

        Long result = trainService.countTrainsByState(State.MAHARASHTRA);

        assertEquals(2L, result);
        verify(trainRepository, times(1)).countTrainsByState(State.MAHARASHTRA);
    }

    @Test
    @DisplayName("Find distinct places by state should work")
    void testFindDistinctPlacesByState() {
        List<String> places = Arrays.asList("Mumbai", "Pune");
        when(trainRepository.findDistinctPlacesByState(State.MAHARASHTRA)).thenReturn(places);

        List<String> result = trainService.findDistinctPlacesByState(State.MAHARASHTRA);

        assertEquals(2, result.size());
        assertTrue(result.contains("Mumbai"));
        assertTrue(result.contains("Pune"));
        verify(trainRepository, times(1)).findDistinctPlacesByState(State.MAHARASHTRA);
    }

    @Test
    @DisplayName("Find all ordered by state and place should work")
    void testFindAllOrderedByStateAndPlace() {
        List<train> trains = Arrays.asList(testTrain1, testTrain2, testTrain3);
        when(trainRepository.findAllOrderedByStateAndPlace()).thenReturn(trains);

        List<train> result = trainService.findAllOrderedByStateAndPlace();

        assertEquals(3, result.size());
        verify(trainRepository, times(1)).findAllOrderedByStateAndPlace();
    }

    @Test
    @DisplayName("Delete by code should call repository delete")
    void testDeleteByCode() {
        doNothing().when(trainRepository).deleteByCode("12345");

        trainService.deleteByCode("12345");

        verify(trainRepository, times(1)).deleteByCode("12345");
    }

    @Test
    @DisplayName("Delete by name should call repository delete")
    void testDeleteByName() {
        doNothing().when(trainRepository).deleteByName("Rajdhani Express");

        trainService.deleteByName("Rajdhani Express");

        verify(trainRepository, times(1)).deleteByName("Rajdhani Express");
    }

    @Test
    @DisplayName("Service should handle null inputs gracefully")
    void testHandleNullInputs() {
        when(trainRepository.save(null)).thenThrow(new IllegalArgumentException("Train cannot be null"));

        assertThrows(IllegalArgumentException.class, () -> trainService.createTrain(null));
        verify(trainRepository, times(1)).save(null);
    }

    @Test
    @DisplayName("Service should handle repository exceptions")
    void testHandleRepositoryExceptions() {
        when(trainRepository.findById("12345")).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> trainService.findById("12345"));
        verify(trainRepository, times(1)).findById("12345");
    }
}
