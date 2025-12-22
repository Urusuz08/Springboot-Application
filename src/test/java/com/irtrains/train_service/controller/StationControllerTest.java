package com.irtrains.train_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.irtrains.train_service.model.enums.State;
import com.irtrains.train_service.model.Station;
import com.irtrains.train_service.service.StationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StationController.class)
public class StationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StationService stationService;

    @Autowired
    private ObjectMapper objectMapper;

    private Station station;

    @BeforeEach
    void setUp() {
        station = new Station("SBC", "Bangalore", State.KARNATAKA, "Bangalore");
    }

    @Test
    void testCreateStation() throws Exception {
        when(stationService.createStation(any(Station.class))).thenReturn(station);

        mockMvc.perform(post("/api/stations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(station)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("SBC"));
    }

    @Test
    void testUpdateStation() throws Exception {
        when(stationService.updateStation(any(Station.class))).thenReturn(station);

        mockMvc.perform(put("/api/stations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(station)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SBC"));
    }

    @Test
    void testGetStationByCode() throws Exception {
        when(stationService.findById("SBC")).thenReturn(Optional.of(station));

        mockMvc.perform(get("/api/stations/SBC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SBC"));
    }

    @Test
    void testGetStationByCode_NotFound() throws Exception {
        when(stationService.findById("SBC")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/stations/SBC"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllStations() throws Exception {
        when(stationService.findAll()).thenReturn(Collections.singletonList(station));

        mockMvc.perform(get("/api/stations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("SBC"));
    }

    @Test
    void testDeleteStationByCode() throws Exception {
        when(stationService.existsByCode("SBC")).thenReturn(true);

        mockMvc.perform(delete("/api/stations/SBC"))
                .andExpect(status().isOk())
                .andExpect(content().string("Station deleted successfully"));
    }

    @Test
    void testDeleteStationByCode_NotFound() throws Exception {
        when(stationService.existsByCode("SBC")).thenReturn(false);

        mockMvc.perform(delete("/api/stations/SBC"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetStationsByState() throws Exception {
        when(stationService.findByState(State.KARNATAKA)).thenReturn(Collections.singletonList(station));

        mockMvc.perform(get("/api/stations/state/KARNATAKA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].state").value("KARNATAKA"));
    }

    @Test
    void testGetStationsByPlace() throws Exception {
        when(stationService.findByPlace("Bangalore")).thenReturn(Collections.singletonList(station));

        mockMvc.perform(get("/api/stations/place/Bangalore"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].place").value("Bangalore"));
    }
}

