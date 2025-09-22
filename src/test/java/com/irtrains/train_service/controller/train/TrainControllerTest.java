package com.irtrains.train_service.controller.train;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.irtrains.train_service.model.enums.State;
import com.irtrains.train_service.model.train.train;
import com.irtrains.train_service.service.train.trainService;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("trainController standalone MVC tests")
class TrainControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private trainService trainService;

    @InjectMocks
    private trainController controller;

    private train t1;
    private train t2;
    private train t3;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();

        t1 = buildTrain("12345", "Rajdhani Express", State.MAHARASHTRA, "Mumbai");
        t2 = buildTrain("54321", "Shatabdi Express", State.MAHARASHTRA, "Pune");
        t3 = buildTrain("67890", "Durronto Express", State.KERALA, "Kochi");
    }

    private train buildTrain(String code, String name, State state, String place) {
        train tr = new train();
        tr.setCode(code);
        tr.setName(name);
        tr.setState(state);
        tr.setPlace(place);
        return tr;
    }

    @Nested
    @DisplayName("CRUD")
    class Crud {
        @Test
        @DisplayName("POST /api/trains creates a train")
        void createTrain() throws Exception {
            given(trainService.createTrain(any(train.class))).willReturn(t1);

            mockMvc.perform(post("/api/trains")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(t1)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.code").value("12345"))
                    .andExpect(jsonPath("$.name").value("Rajdhani Express"))
                    .andExpect(jsonPath("$.state").value("MAHARASHTRA"))
                    .andExpect(jsonPath("$.place").value("Mumbai"));

            verify(trainService, times(1)).createTrain(any(train.class));
        }

        @Test
        @DisplayName("PUT /api/trains updates a train")
        void updateTrain() throws Exception {
            train updated = buildTrain("12345", "Rajdhani Express", State.MAHARASHTRA, "Navi Mumbai");
            given(trainService.updateTrain(any(train.class))).willReturn(updated);

            mockMvc.perform(put("/api/trains")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updated)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("12345"))
                    .andExpect(jsonPath("$.place").value("Navi Mumbai"));

            verify(trainService, times(1)).updateTrain(any(train.class));
        }

        @Test
        @DisplayName("GET /api/trains/{id} returns a train")
        void getById() throws Exception {
            given(trainService.findById("12345")).willReturn(Optional.of(t1));

            mockMvc.perform(get("/api/trains/{id}", "12345"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Rajdhani Express"));

            verify(trainService, times(1)).findById("12345");
        }

        @Test
        @DisplayName("DELETE /api/trains/{id} deletes a train")
        void deleteById() throws Exception {
            doNothing().when(trainService).deleteById("12345");

            mockMvc.perform(delete("/api/trains/{id}", "12345"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Train deleted successfully"));

            verify(trainService, times(1)).deleteById("12345");
        }
    }

    @Nested
    @DisplayName("Reads and queries")
    class Queries {
        @Test
        @DisplayName("GET /api/trains returns all trains")
        void getAll() throws Exception {
            given(trainService.findAll()).willReturn(Arrays.asList(t1, t2, t3));

            mockMvc.perform(get("/api/trains"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)))
                    .andExpect(jsonPath("$[0].code").value("12345"))
                    .andExpect(jsonPath("$[1].code").value("54321"))
                    .andExpect(jsonPath("$[2].code").value("67890"));

            verify(trainService, times(1)).findAll();
        }

        @Test
        @DisplayName("GET /api/trains/state/{state} filters by state")
        void byState() throws Exception {
            given(trainService.findByState(State.MAHARASHTRA)).willReturn(Arrays.asList(t1, t2));

            mockMvc.perform(get("/api/trains/state/{state}", "MAHARASHTRA"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].state").value("MAHARASHTRA"))
                    .andExpect(jsonPath("$[1].state").value("MAHARASHTRA"));

            verify(trainService, times(1)).findByState(State.MAHARASHTRA);
        }

        @Test
        @DisplayName("GET /api/trains/place/{place} filters by place")
        void byPlace() throws Exception {
            given(trainService.findByPlace("Mumbai")).willReturn(Collections.singletonList(t1));

            mockMvc.perform(get("/api/trains/place/{place}", "Mumbai"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].place").value("Mumbai"));

            verify(trainService, times(1)).findByPlace("Mumbai");
        }

        @Test
        @DisplayName("GET /api/trains/search/place?place= supports partial place match")
        void searchByPlace() throws Exception {
            given(trainService.findByPlaceContainingIgnoreCase("mum")).willReturn(Collections.singletonList(t1));

            mockMvc.perform(get("/api/trains/search/place").param("place", "mum"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].place").value("Mumbai"));

            verify(trainService, times(1)).findByPlaceContainingIgnoreCase("mum");
        }

        @Test
        @DisplayName("GET /api/trains/search/name?name= supports partial name match")
        void searchByName() throws Exception {
            given(trainService.findByNameContainingIgnoreCase("express")).willReturn(Arrays.asList(t1, t2, t3));

            mockMvc.perform(get("/api/trains/search/name").param("name", "express"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)));

            verify(trainService, times(1)).findByNameContainingIgnoreCase("express");
        }

        @Test
        @DisplayName("GET /api/trains/name/{name} returns exact match by name")
        void byExactName() throws Exception {
            given(trainService.findByName("Rajdhani Express")).willReturn(Optional.of(t1));

            mockMvc.perform(get("/api/trains/name/{name}", "Rajdhani Express"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Rajdhani Express"));

            verify(trainService, times(1)).findByName("Rajdhani Express");
        }

        @Test
        @DisplayName("GET /api/trains/exists/name/{name} returns existence by name")
        void existsByName() throws Exception {
            given(trainService.existsByName("Rajdhani Express")).willReturn(true);

            mockMvc.perform(get("/api/trains/exists/name/{name}", "Rajdhani Express"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"));

            verify(trainService, times(1)).existsByName("Rajdhani Express");
        }

        @Test
        @DisplayName("GET /api/trains/exists/code/{code} returns existence by code")
        void existsByCode() throws Exception {
            given(trainService.existsByCode("12345")).willReturn(true);

            mockMvc.perform(get("/api/trains/exists/code/{code}", "12345"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"));

            verify(trainService, times(1)).existsByCode("12345");
        }

        @Test
        @DisplayName("GET /api/trains/state/{state}/place/{place} filters by both")
        void byStateAndPlace() throws Exception {
            given(trainService.findByStateAndPlace(State.MAHARASHTRA, "Mumbai"))
                    .willReturn(Collections.singletonList(t1));

            mockMvc.perform(get("/api/trains/state/{state}/place/{place}", "MAHARASHTRA", "Mumbai"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].state").value("MAHARASHTRA"))
                    .andExpect(jsonPath("$[0].place").value("Mumbai"));

            verify(trainService, times(1)).findByStateAndPlace(State.MAHARASHTRA, "Mumbai");
        }

        @Test
        @DisplayName("GET /api/trains/search/state-place?state=&place= supports combined search")
        void searchByStateAndPlace() throws Exception {
            given(trainService.findTrainsByStateAndPlaceContaining(State.MAHARASHTRA, "Mum"))
                    .willReturn(Collections.singletonList(t1));

            mockMvc.perform(get("/api/trains/search/state-place")
                            .param("state", "MAHARASHTRA")
                            .param("place", "Mum"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));

            verify(trainService, times(1)).findTrainsByStateAndPlaceContaining(State.MAHARASHTRA, "Mum");
        }

        @Test
        @DisplayName("GET /api/trains/search?searchTerm= global search")
        void searchGlobal() throws Exception {
            given(trainService.searchTrainsByNameOrPlace("Mumbai"))
                    .willReturn(Collections.singletonList(t1));

            mockMvc.perform(get("/api/trains/search").param("searchTerm", "Mumbai"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));

            verify(trainService, times(1)).searchTrainsByNameOrPlace("Mumbai");
        }

        @Test
        @DisplayName("POST /api/trains/states returns by multiple states")
        void byMultipleStates() throws Exception {
            List<State> states = Arrays.asList(State.MAHARASHTRA, State.KERALA);
            given(trainService.findByStateIn(states)).willReturn(Arrays.asList(t1, t3));

            mockMvc.perform(post("/api/trains/states")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(states)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)));

            verify(trainService, times(1)).findByStateIn(states);
        }

        @Test
        @DisplayName("GET /api/trains/count/state/{state} returns count")
        void countByState() throws Exception {
            given(trainService.countTrainsByState(State.MAHARASHTRA)).willReturn(2L);

            mockMvc.perform(get("/api/trains/count/state/{state}", "MAHARASHTRA"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("2"));

            verify(trainService, times(1)).countTrainsByState(State.MAHARASHTRA);
        }

        @Test
        @DisplayName("GET /api/trains/places/state/{state} returns distinct places")
        void distinctPlacesByState() throws Exception {
            given(trainService.findDistinctPlacesByState(State.MAHARASHTRA))
                    .willReturn(Arrays.asList("Mumbai", "Pune"));

            mockMvc.perform(get("/api/trains/places/state/{state}", "MAHARASHTRA"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0]").value("Mumbai"))
                    .andExpect(jsonPath("$[1]").value("Pune"));

            verify(trainService, times(1)).findDistinctPlacesByState(State.MAHARASHTRA);
        }

        @Test
        @DisplayName("GET /api/trains/ordered returns ordered trains")
        void ordered() throws Exception {
            given(trainService.findAllOrderedByStateAndPlace()).willReturn(Arrays.asList(t1, t2, t3));

            mockMvc.perform(get("/api/trains/ordered"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)));

            verify(trainService, times(1)).findAllOrderedByStateAndPlace();
        }

        @Test
        @DisplayName("DELETE /api/trains/code/{code} deletes by code")
        void deleteByCode() throws Exception {
            doNothing().when(trainService).deleteByCode("12345");

            mockMvc.perform(delete("/api/trains/code/{code}", "12345"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Train deleted successfully"));

            verify(trainService, times(1)).deleteByCode("12345");
        }

        @Test
        @DisplayName("DELETE /api/trains/name/{name} deletes by name")
        void deleteByName() throws Exception {
            doNothing().when(trainService).deleteByName("Rajdhani Express");

            mockMvc.perform(delete("/api/trains/name/{name}", "Rajdhani Express"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Train deleted successfully"));

            verify(trainService, times(1)).deleteByName("Rajdhani Express");
        }
    }
}
