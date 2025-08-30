package com.irtrains.train_service.controller.train;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.irtrains.train_service.model.enums.State;
import com.irtrains.train_service.model.train.train;
import com.irtrains.train_service.service.train.trainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = trainController.class,
           excludeAutoConfiguration = {SecurityAutoConfiguration.class}) // Exclude security auto-configuration
@AutoConfigureWebMvc
@DisplayName("Train Controller Tests")
class TrainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private trainService trainService;

    @Autowired
    private ObjectMapper objectMapper;

    private train testTrain1;
    private train testTrain2;
    private List<train> trainList;

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

        trainList = Arrays.asList(testTrain1, testTrain2);
    }

    @Test
    @DisplayName("POST /api/trains - Create train successfully")
    void testCreateTrain_Success() throws Exception {
        when(trainService.createTrain(any(train.class))).thenReturn(testTrain1);

        mockMvc.perform(post("/api/trains")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTrain1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("12345"))
                .andExpect(jsonPath("$.name").value("Rajdhani Express"))
                .andExpect(jsonPath("$.state").value("MAHARASHTRA"))
                .andExpect(jsonPath("$.place").value("Mumbai"));

        verify(trainService, times(1)).createTrain(any(train.class));
    }

    @Test
    @DisplayName("POST /api/trains - Create train with error")
    void testCreateTrain_Error() throws Exception {
        when(trainService.createTrain(any(train.class))).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/api/trains")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTrain1)))
                .andExpect(status().isBadRequest());

        verify(trainService, times(1)).createTrain(any(train.class));
    }

    @Test
    @DisplayName("PUT /api/trains - Update train successfully")
    void testUpdateTrain_Success() throws Exception {
        testTrain1.setPlace("New Mumbai");
        when(trainService.updateTrain(any(train.class))).thenReturn(testTrain1);

        mockMvc.perform(put("/api/trains")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTrain1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.place").value("New Mumbai"));

        verify(trainService, times(1)).updateTrain(any(train.class));
    }

    @Test
    @DisplayName("PUT /api/trains - Update train with error")
    void testUpdateTrain_Error() throws Exception {
        when(trainService.updateTrain(any(train.class))).thenThrow(new RuntimeException("Update failed"));

        mockMvc.perform(put("/api/trains")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTrain1)))
                .andExpect(status().isBadRequest());

        verify(trainService, times(1)).updateTrain(any(train.class));
    }

    @Test
    @DisplayName("GET /api/trains/{id} - Get train by ID successfully")
    void testGetTrainById_Success() throws Exception {
        when(trainService.findById("12345")).thenReturn(Optional.of(testTrain1));

        mockMvc.perform(get("/api/trains/12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("12345"))
                .andExpect(jsonPath("$.name").value("Rajdhani Express"));

        verify(trainService, times(1)).findById("12345");
    }

    @Test
    @DisplayName("GET /api/trains/{id} - Train not found")
    void testGetTrainById_NotFound() throws Exception {
        when(trainService.findById("99999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/trains/99999"))
                .andExpect(status().isNotFound());

        verify(trainService, times(1)).findById("99999");
    }

    @Test
    @DisplayName("GET /api/trains/{id} - Internal server error")
    void testGetTrainById_Error() throws Exception {
        when(trainService.findById("12345")).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/api/trains/12345"))
                .andExpect(status().isInternalServerError());

        verify(trainService, times(1)).findById("12345");
    }

    @Test
    @DisplayName("GET /api/trains - Get all trains successfully")
    void testGetAllTrains_Success() throws Exception {
        when(trainService.findAll()).thenReturn(trainList);

        mockMvc.perform(get("/api/trains"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].code").value("12345"))
                .andExpect(jsonPath("$[1].code").value("54321"));

        verify(trainService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/trains - Get all trains with error")
    void testGetAllTrains_Error() throws Exception {
        when(trainService.findAll()).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/api/trains"))
                .andExpect(status().isInternalServerError());

        verify(trainService, times(1)).findAll();
    }

    @Test
    @DisplayName("DELETE /api/trains/{id} - Delete train successfully")
    void testDeleteTrainById_Success() throws Exception {
        doNothing().when(trainService).deleteById("12345");

        mockMvc.perform(delete("/api/trains/12345"))
                .andExpect(status().isOk())
                .andExpect(content().string("Train deleted successfully"));

        verify(trainService, times(1)).deleteById("12345");
    }

    @Test
    @DisplayName("DELETE /api/trains/{id} - Delete train with error")
    void testDeleteTrainById_Error() throws Exception {
        doThrow(new RuntimeException("Delete failed")).when(trainService).deleteById("12345");

        mockMvc.perform(delete("/api/trains/12345"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to delete train"));

        verify(trainService, times(1)).deleteById("12345");
    }

    @Test
    @DisplayName("GET /api/trains/state/{state} - Get trains by state")
    void testGetTrainsByState() throws Exception {
        when(trainService.findByState(State.MAHARASHTRA)).thenReturn(trainList);

        mockMvc.perform(get("/api/trains/state/MAHARASHTRA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(trainService, times(1)).findByState(State.MAHARASHTRA);
    }

    @Test
    @DisplayName("GET /api/trains/place/{place} - Get trains by place")
    void testGetTrainsByPlace() throws Exception {
        when(trainService.findByPlace("Mumbai")).thenReturn(Arrays.asList(testTrain1));

        mockMvc.perform(get("/api/trains/place/Mumbai"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].place").value("Mumbai"));

        verify(trainService, times(1)).findByPlace("Mumbai");
    }

    @Test
    @DisplayName("GET /api/trains/search/place - Search trains by place")
    void testSearchTrainsByPlace() throws Exception {
        when(trainService.findByPlaceContainingIgnoreCase("mum")).thenReturn(Arrays.asList(testTrain1));

        mockMvc.perform(get("/api/trains/search/place").param("place", "mum"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(trainService, times(1)).findByPlaceContainingIgnoreCase("mum");
    }

    @Test
    @DisplayName("GET /api/trains/search/name - Search trains by name")
    void testSearchTrainsByName() throws Exception {
        when(trainService.findByNameContainingIgnoreCase("express")).thenReturn(trainList);

        mockMvc.perform(get("/api/trains/search/name").param("name", "express"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(trainService, times(1)).findByNameContainingIgnoreCase("express");
    }

    @Test
    @DisplayName("GET /api/trains/name/{name} - Get train by name successfully")
    void testGetTrainByName_Success() throws Exception {
        when(trainService.findByName("Rajdhani Express")).thenReturn(Optional.of(testTrain1));

        mockMvc.perform(get("/api/trains/name/Rajdhani Express"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Rajdhani Express"));

        verify(trainService, times(1)).findByName("Rajdhani Express");
    }

    @Test
    @DisplayName("GET /api/trains/name/{name} - Train not found by name")
    void testGetTrainByName_NotFound() throws Exception {
        when(trainService.findByName("Non Existent Train")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/trains/name/Non Existent Train"))
                .andExpect(status().isNotFound());

        verify(trainService, times(1)).findByName("Non Existent Train");
    }

    @Test
    @DisplayName("GET /api/trains/exists/name/{name} - Check if train exists by name")
    void testExistsByName() throws Exception {
        when(trainService.existsByName("Rajdhani Express")).thenReturn(true);

        mockMvc.perform(get("/api/trains/exists/name/Rajdhani Express"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(trainService, times(1)).existsByName("Rajdhani Express");
    }

    @Test
    @DisplayName("GET /api/trains/exists/code/{code} - Check if train exists by code")
    void testExistsByCode() throws Exception {
        when(trainService.existsByCode("12345")).thenReturn(true);

        mockMvc.perform(get("/api/trains/exists/code/12345"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(trainService, times(1)).existsByCode("12345");
    }

    @Test
    @DisplayName("GET /api/trains/state/{state}/place/{place} - Get trains by state and place")
    void testGetTrainsByStateAndPlace() throws Exception {
        when(trainService.findByStateAndPlace(State.MAHARASHTRA, "Mumbai")).thenReturn(Arrays.asList(testTrain1));

        mockMvc.perform(get("/api/trains/state/MAHARASHTRA/place/Mumbai"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(trainService, times(1)).findByStateAndPlace(State.MAHARASHTRA, "Mumbai");
    }

    @Test
    @DisplayName("GET /api/trains/search/state-place - Search trains by state and place")
    void testSearchTrainsByStateAndPlace() throws Exception {
        when(trainService.findTrainsByStateAndPlaceContaining(State.MAHARASHTRA, "Mum"))
                .thenReturn(Arrays.asList(testTrain1));

        mockMvc.perform(get("/api/trains/search/state-place")
                .param("state", "MAHARASHTRA")
                .param("place", "Mum"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(trainService, times(1)).findTrainsByStateAndPlaceContaining(State.MAHARASHTRA, "Mum");
    }

    @Test
    @DisplayName("GET /api/trains/search - Global search by name or place")
    void testSearchTrains() throws Exception {
        when(trainService.searchTrainsByNameOrPlace("Kerala")).thenReturn(Arrays.asList(testTrain1));

        mockMvc.perform(get("/api/trains/search").param("searchTerm", "Kerala"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(trainService, times(1)).searchTrainsByNameOrPlace("Kerala");
    }

    @Test
    @DisplayName("POST /api/trains/states - Get trains by multiple states")
    void testGetTrainsByStates() throws Exception {
        List<State> states = Arrays.asList(State.MAHARASHTRA, State.KERALA);
        when(trainService.findByStateIn(states)).thenReturn(trainList);

        mockMvc.perform(post("/api/trains/states")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(states)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(trainService, times(1)).findByStateIn(states);
    }

    @Test
    @DisplayName("DELETE /api/trains/code/{code} - Delete train by code")
    void testDeleteTrainByCode() throws Exception {
        doNothing().when(trainService).deleteByCode("12345");

        mockMvc.perform(delete("/api/trains/code/12345"))
                .andExpect(status().isOk())
                .andExpect(content().string("Train deleted successfully"));

        verify(trainService, times(1)).deleteByCode("12345");
    }

    @Test
    @DisplayName("DELETE /api/trains/name/{name} - Delete train by name")
    void testDeleteTrainByName() throws Exception {
        doNothing().when(trainService).deleteByName("Rajdhani Express");

        mockMvc.perform(delete("/api/trains/name/Rajdhani Express"))
                .andExpect(status().isOk())
                .andExpect(content().string("Train deleted successfully"));

        verify(trainService, times(1)).deleteByName("Rajdhani Express");
    }

    @Test
    @DisplayName("GET /api/trains/count/state/{state} - Count trains by state")
    void testCountTrainsByState() throws Exception {
        when(trainService.countTrainsByState(State.MAHARASHTRA)).thenReturn(2L);

        mockMvc.perform(get("/api/trains/count/state/MAHARASHTRA"))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));

        verify(trainService, times(1)).countTrainsByState(State.MAHARASHTRA);
    }

    @Test
    @DisplayName("GET /api/trains/places/state/{state} - Get distinct places by state")
    void testGetDistinctPlacesByState() throws Exception {
        List<String> places = Arrays.asList("Mumbai", "Pune");
        when(trainService.findDistinctPlacesByState(State.MAHARASHTRA)).thenReturn(places);

        mockMvc.perform(get("/api/trains/places/state/MAHARASHTRA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0]").value("Mumbai"))
                .andExpect(jsonPath("$[1]").value("Pune"));

        verify(trainService, times(1)).findDistinctPlacesByState(State.MAHARASHTRA);
    }

    @Test
    @DisplayName("GET /api/trains/ordered - Get all trains ordered")
    void testGetAllTrainsOrdered() throws Exception {
        when(trainService.findAllOrderedByStateAndPlace()).thenReturn(trainList);

        mockMvc.perform(get("/api/trains/ordered"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(trainService, times(1)).findAllOrderedByStateAndPlace();
    }

    @Test
    @DisplayName("Error handling for service exceptions")
    void testServiceExceptionHandling() throws Exception {
        when(trainService.findByState(State.MAHARASHTRA)).thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(get("/api/trains/state/MAHARASHTRA"))
                .andExpect(status().isInternalServerError());

        verify(trainService, times(1)).findByState(State.MAHARASHTRA);
    }
}
