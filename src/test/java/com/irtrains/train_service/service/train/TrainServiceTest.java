package com.irtrains.train_service.service.train;

import com.irtrains.train_service.model.enums.Type;
import com.irtrains.train_service.model.train.train;
import com.irtrains.train_service.repository.train.TrainRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("trainService unit tests")
class TrainServiceTest {

    @Mock
    private TrainRepository repo;

    @InjectMocks
    private trainService service;

    private train t1;
    private train t2;

    @BeforeEach
    void init() {
        t1 = new train();
        t1.setTrainID("12345");
        t1.setName("Rajdhani Express");
        t1.setType(Type.RAJDHANI);
        t1.setSourceStation("BCT");
        t1.setDestinationStation("NDLS");

        t2 = new train();
        t2.setTrainID("54321");
        t2.setName("Shatabdi Express");
        t2.setType(Type.SHATABDI);
        t2.setSourceStation("NDLS");
        t2.setDestinationStation("BCT");
    }

    @Nested
    @DisplayName("Create")
    class Create {
        @Test
        @DisplayName("createTrain persists when id and name are unique")
        void createOk() {
            when(repo.existsByTrainId("12345")).thenReturn(false);
            when(repo.existsByName("Rajdhani Express")).thenReturn(false);
            when(repo.save(t1)).thenReturn(t1);

            train created = service.createTrain(t1);
            assertEquals("12345", created.getTrainId());
            verify(repo).save(t1);
        }

        @Test
        @DisplayName("createTrain throws when id exists")
        void createDuplicateId() {
            when(repo.existsByTrainId("12345")).thenReturn(true);
            assertThrows(IllegalArgumentException.class, () -> service.createTrain(t1));
            verify(repo, never()).save(any());
        }

        @Test
        @DisplayName("createTrain rethrows on constraint violation")
        void createConstraintViolation() {
            when(repo.existsByTrainId("12345")).thenReturn(false);
            when(repo.existsByName("Rajdhani Express")).thenReturn(false);
            when(repo.save(any(train.class))).thenThrow(new DataIntegrityViolationException("dup"));
            assertThrows(IllegalArgumentException.class, () -> service.createTrain(t1));
        }
    }

    @Nested
    @DisplayName("Update")
    class Update {
        @Test
        @DisplayName("updateTrain updates existing record")
        void updateOk() {
            when(repo.findById("12345")).thenReturn(Optional.of(t1));
            when(repo.save(any(train.class))).thenReturn(t1);

            t1.setName("Rajdhani Exp");
            train updated = service.updateTrain(t1);
            assertEquals("Rajdhani Exp", updated.getName());
            verify(repo).save(any(train.class));
        }

        @Test
        @DisplayName("updateTrain throws when not found")
        void updateNotFound() {
            when(repo.findById("12345")).thenReturn(Optional.empty());
            assertThrows(IllegalArgumentException.class, () -> service.updateTrain(t1));
        }
    }

    @Nested
    @DisplayName("Reads & Queries")
    class ReadsQueries {
        @Test
        void findByIdOk() {
            when(repo.findById("12345")).thenReturn(Optional.of(t1));
            assertTrue(service.findById("12345").isPresent());
        }

        @Test
        void findAllOk() {
            when(repo.findAll()).thenReturn(List.of(t1, t2));
            assertEquals(2, service.findAll().size());
        }

        @Test
        void findByNameOk() {
            when(repo.findByName("Rajdhani Express")).thenReturn(Optional.of(t1));
            assertTrue(service.findByName("Rajdhani Express").isPresent());
        }

        @Test
        void findByTypeOk() {
            when(repo.findByType(Type.RAJDHANI)).thenReturn(List.of(t1));
            assertEquals(1, service.findByType(Type.RAJDHANI).size());
        }

        @Test
        void routeQueries() {
            when(repo.findBySourceStation("BCT")).thenReturn(List.of(t1));
            when(repo.findByDestinationStation("NDLS")).thenReturn(List.of(t1));
            when(repo.findBySourceStationAndDestinationStation("BCT", "NDLS")).thenReturn(List.of(t1));
            when(repo.findBidirectionalBetween("BCT", "NDLS")).thenReturn(List.of(t1, t2));

            assertEquals(1, service.findBySourceStation("BCT").size());
            assertEquals(1, service.findByDestinationStation("NDLS").size());
            assertEquals(1, service.findBySourceAndDestination("BCT", "NDLS").size());
            assertEquals(2, service.findBidirectionalBetween("BCT", "NDLS").size());
        }

        @Test
        void searchByNameOrId() {
            when(repo.searchByNameOrId("raj")).thenReturn(List.of(t1));
            assertEquals(1, service.searchByNameOrId(" raj ").size());
            assertTrue(service.searchByNameOrId(" ").isEmpty());
        }
    }

    @Nested
    @DisplayName("Existence & Deletion")
    class ExistenceDeletion {
        @Test
        void existsMethods() {
            when(repo.existsByName("Rajdhani Express")).thenReturn(true);
            when(repo.existsByTrainId("12345")).thenReturn(true);
            assertTrue(service.existsByName("Rajdhani Express"));
            assertTrue(service.existsByTrainId("12345"));
            assertFalse(service.existsByTrainId(null));
        }

        @Test
        void deleteByIdIdempotent() {
            when(repo.existsByTrainId("99999")).thenReturn(false);
            service.deleteById("99999");
            verify(repo, never()).deleteById(anyString());
        }

        @Test
        void deleteByIdNullThrows() {
            assertThrows(IllegalArgumentException.class, () -> service.deleteById(""));
        }

        @Test
        void deleteByNameOk() {
            service.deleteByName("Rajdhani Express");
            verify(repo).deleteByName("Rajdhani Express");
        }
    }
}
