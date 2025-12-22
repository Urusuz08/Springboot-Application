package com.irtrains.train_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.irtrains.train_service.model.enums.Type;
import com.irtrains.train_service.model.train;
import com.irtrains.train_service.service.trainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("trainController MVC tests")
class TrainControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private trainService service;

    @InjectMocks
    private trainController controller;

    private train t1;
    private train t2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();

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
    @DisplayName("Create & Update")
    class CreateUpdate {
        @Test
        @DisplayName("POST /api/trains creates a train")
        void createTrain() throws Exception {
            given(service.createTrain(org.mockito.ArgumentMatchers.any(train.class))).willReturn(t1);

            mockMvc.perform(post("/api/trains")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(t1)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.trainId").value("12345"))
                    .andExpect(jsonPath("$.name").value("Rajdhani Express"))
                    .andExpect(jsonPath("$.type").value("RAJDHANI"))
                    .andExpect(jsonPath("$.sourceStation").value("BCT"))
                    .andExpect(jsonPath("$.destinationStation").value("NDLS"));

            verify(service).createTrain(org.mockito.ArgumentMatchers.any(train.class));
        }

        @Test
        @DisplayName("PUT /api/trains/{id} updates a train when id matches body")
        void updateTrain() throws Exception {
            when(service.findById("12345")).thenReturn(Optional.of(t1));
            when(service.updateTrain(org.mockito.ArgumentMatchers.any(train.class))).thenReturn(t1);

            mockMvc.perform(put("/api/trains/{id}", "12345")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(t1)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.trainId").value("12345"));

            verify(service).updateTrain(org.mockito.ArgumentMatchers.any(train.class));
        }

        @Test
        @DisplayName("PUT /api/trains/{id} returns 400 when path/body id mismatch")
        void updateTrainBadRequestOnIdMismatch() throws Exception {
            train mismatch = new train();
            mismatch.setTrainID("00000");
            mismatch.setName("X");
            mismatch.setType(Type.OTHER);
            mismatch.setSourceStation("A");
            mismatch.setDestinationStation("B");

            mockMvc.perform(put("/api/trains/{id}", "12345")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(mismatch)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message", containsString("trainId in path and body must match")));
        }
    }

    @Nested
    @DisplayName("Reads & Queries")
    class Reads {
        @Test
        @DisplayName("GET /api/trains/{id} returns a train")
        void getById() throws Exception {
            when(service.findById("12345")).thenReturn(Optional.of(t1));

            mockMvc.perform(get("/api/trains/{id}", "12345"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Rajdhani Express"));

            verify(service).findById("12345");
        }

        @Test
        @DisplayName("GET /api/trains/name/{name} returns by name")
        void getByName() throws Exception {
            when(service.findByName("Rajdhani Express")).thenReturn(Optional.of(t1));

            mockMvc.perform(get("/api/trains/name/{name}", "Rajdhani Express"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.trainId").value("12345"));
        }

        @Test
        @DisplayName("GET /api/trains?term= filters by term")
        void searchByTerm() throws Exception {
            when(service.searchByNameOrId("raj")).thenReturn(List.of(t1));

            mockMvc.perform(get("/api/trains").param("term", "raj"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].name").value("Rajdhani Express"));
        }

        @Test
        @DisplayName("GET /api/trains?source=&destination= filters by route")
        void filterByRoute() throws Exception {
            when(service.findBySourceAndDestination("BCT", "NDLS")).thenReturn(List.of(t1));

            mockMvc.perform(get("/api/trains")
                            .param("source", "BCT")
                            .param("destination", "NDLS"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].trainId").value("12345"));
        }

        @Test
        @DisplayName("GET /api/trains?type= filters by type")
        void filterByType() throws Exception {
            when(service.findByType(Type.RAJDHANI)).thenReturn(List.of(t1));

            mockMvc.perform(get("/api/trains").param("type", "RAJDHANI"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].type").value("RAJDHANI"));
        }

        @Test
        @DisplayName("GET /api/trains/route?from=&to= returns trains")
        void route() throws Exception {
            when(service.findBySourceAndDestination("BCT", "NDLS")).thenReturn(List.of(t1));

            mockMvc.perform(get("/api/trains/route")
                            .param("from", "BCT")
                            .param("to", "NDLS"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));
        }

        @Test
        @DisplayName("GET /api/trains/between?a=&b= returns bidirectional trains")
        void between() throws Exception {
            when(service.findBidirectionalBetween("BCT", "NDLS")).thenReturn(List.of(t1, t2));

            mockMvc.perform(get("/api/trains/between")
                            .param("a", "BCT")
                            .param("b", "NDLS"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)));
        }

        @Test
        @DisplayName("GET /api/trains/exists/id/{id} returns existence map")
        void existsById() throws Exception {
            when(service.existsByTrainId("12345")).thenReturn(true);

            mockMvc.perform(get("/api/trains/exists/id/{id}", "12345"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.trainId").value("12345"))
                    .andExpect(jsonPath("$.exists").value(true));
        }

        @Test
        @DisplayName("GET /api/trains/exists/name/{name} returns existence map")
        void existsByName() throws Exception {
            when(service.existsByName("Rajdhani Express")).thenReturn(true);

            mockMvc.perform(get("/api/trains/exists/name/{name}", "Rajdhani Express"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Rajdhani Express"))
                    .andExpect(jsonPath("$.exists").value(true));
        }
    }

    @Nested
    @DisplayName("Deletion")
    class Deletion {
        @Test
        @DisplayName("DELETE /api/trains/{id} returns 204 when deleted")
        void deleteByIdShouldReturn204() throws Exception {
            when(service.findById("12345")).thenReturn(Optional.of(t1));

            mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/trains/{id}", "12345"))
                    .andExpect(status().isNoContent());

            verify(service).deleteById("12345");
        }
    }
}
