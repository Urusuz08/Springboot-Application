package com.irtrains.train_service.controller.train;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.irtrains.train_service.controller.station.StationController;
import com.irtrains.train_service.model.enums.State;
import com.irtrains.train_service.model.station.Station;
import com.irtrains.train_service.service.station.StationService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StationController standalone MVC tests")
class TrainControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private StationService stationService;

    @InjectMocks
    private StationController controller;

    private Station s1;
    private Station s2;
    private Station s3;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();

        s1 = new Station("BCT", "Mumbai Central", State.MAHARASHTRA, "Mumbai");
        s2 = new Station("PUNE", "Pune Junction", State.MAHARASHTRA, "Pune");
        s3 = new Station("ERS", "Ernakulam Jn", State.KERALA, "Kochi");
    }

    @Nested
    @DisplayName("CRUD")
    class Crud {
        @Test
        @DisplayName("POST /api/stations creates a station")
        void createStation() throws Exception {
            given(stationService.createStation(any(Station.class))).willReturn(s1);

            mockMvc.perform(post("/api/stations")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(s1)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.code").value("BCT"))
                    .andExpect(jsonPath("$.name").value("Mumbai Central"))
                    .andExpect(jsonPath("$.state").value("MAHARASHTRA"))
                    .andExpect(jsonPath("$.place").value("Mumbai"));

            verify(stationService, times(1)).createStation(any(Station.class));
        }

        @Test
        @DisplayName("PUT /api/stations updates a station")
        void updateStation() throws Exception {
            Station updated = new Station("BCT", "Mumbai Central", State.MAHARASHTRA, "Navi Mumbai");
            given(stationService.updateStation(any(Station.class))).willReturn(updated);

            mockMvc.perform(put("/api/stations")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updated)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("BCT"))
                    .andExpect(jsonPath("$.place").value("Navi Mumbai"));

            verify(stationService, times(1)).updateStation(any(Station.class));
        }

        @Test
        @DisplayName("GET /api/stations/{code} returns a station")
        void getByCode() throws Exception {
            given(stationService.findById("BCT")).willReturn(Optional.of(s1));

            mockMvc.perform(get("/api/stations/{code}", "BCT"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Mumbai Central"));

            verify(stationService, times(1)).findById("BCT");
        }

        @Test
        @DisplayName("DELETE /api/stations/{code} deletes a station")
        void deleteByCode() throws Exception {
            doNothing().when(stationService).deleteById("BCT");

            mockMvc.perform(delete("/api/stations/{code}", "BCT"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Station deleted successfully"));

            verify(stationService, times(1)).deleteById("BCT");
        }
    }

    @Nested
    @DisplayName("Reads and queries")
    class Queries {
        @Test
        @DisplayName("GET /api/stations returns all stations")
        void getAll() throws Exception {
            given(stationService.findAll()).willReturn(Arrays.asList(s1, s2, s3));

            mockMvc.perform(get("/api/stations"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)))
                    .andExpect(jsonPath("$[0].code").value("BCT"))
                    .andExpect(jsonPath("$[1].code").value("PUNE"))
                    .andExpect(jsonPath("$[2].code").value("ERS"));

            verify(stationService, times(1)).findAll();
        }

        @Test
        @DisplayName("GET /api/stations/state/{state} filters by state")
        void byState() throws Exception {
            given(stationService.findByState(State.MAHARASHTRA)).willReturn(Arrays.asList(s1, s2));

            mockMvc.perform(get("/api/stations/state/{state}", "MAHARASHTRA"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].state").value("MAHARASHTRA"))
                    .andExpect(jsonPath("$[1].state").value("MAHARASHTRA"));

            verify(stationService, times(1)).findByState(State.MAHARASHTRA);
        }

        @Test
        @DisplayName("GET /api/stations/place/{place} filters by place")
        void byPlace() throws Exception {
            given(stationService.findByPlace("Mumbai")).willReturn(Collections.singletonList(s1));

            mockMvc.perform(get("/api/stations/place/{place}", "Mumbai"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].place").value("Mumbai"));

            verify(stationService, times(1)).findByPlace("Mumbai");
        }

        @Test
        @DisplayName("GET /api/stations/search/place?place= supports partial place match")
        void searchByPlace() throws Exception {
            given(stationService.findByPlaceContainingIgnoreCase("mum")).willReturn(Collections.singletonList(s1));

            mockMvc.perform(get("/api/stations/search/place").param("place", "mum"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].place").value("Mumbai"));

            verify(stationService, times(1)).findByPlaceContainingIgnoreCase("mum");
        }

        @Test
        @DisplayName("GET /api/stations/search/name?name= supports partial name match")
        void searchByName() throws Exception {
            given(stationService.findByNameContainingIgnoreCase("junction")).willReturn(Arrays.asList(s2));

            mockMvc.perform(get("/api/stations/search/name").param("name", "junction"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));

            verify(stationService, times(1)).findByNameContainingIgnoreCase("junction");
        }

        @Test
        @DisplayName("GET /api/stations/name/{name} returns exact match by name")
        void byExactName() throws Exception {
            given(stationService.findByName("Mumbai Central")).willReturn(Optional.of(s1));

            mockMvc.perform(get("/api/stations/name/{name}", "Mumbai Central"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Mumbai Central"));

            verify(stationService, times(1)).findByName("Mumbai Central");
        }

        @Test
        @DisplayName("GET /api/stations/exists/name/{name} returns existence by name")
        void existsByName() throws Exception {
            given(stationService.existsByName("Mumbai Central")).willReturn(true);

            mockMvc.perform(get("/api/stations/exists/name/{name}", "Mumbai Central"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"));

            verify(stationService, times(1)).existsByName("Mumbai Central");
        }

        @Test
        @DisplayName("GET /api/stations/exists/code/{code} returns existence by code")
        void existsByCode() throws Exception {
            given(stationService.existsByCode("BCT")).willReturn(true);

            mockMvc.perform(get("/api/stations/exists/code/{code}", "BCT"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"));

            verify(stationService, times(1)).existsByCode("BCT");
        }

        @Test
        @DisplayName("GET /api/stations/state/{state}/place/{place} filters by both")
        void byStateAndPlace() throws Exception {
            given(stationService.findByStateAndPlace(State.MAHARASHTRA, "Mumbai"))
                    .willReturn(Collections.singletonList(s1));

            mockMvc.perform(get("/api/stations/state/{state}/place/{place}", "MAHARASHTRA", "Mumbai"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].state").value("MAHARASHTRA"))
                    .andExpect(jsonPath("$[0].place").value("Mumbai"));

            verify(stationService, times(1)).findByStateAndPlace(State.MAHARASHTRA, "Mumbai");
        }

        @Test
        @DisplayName("GET /api/stations/search/state-place?state=&place= supports combined search")
        void searchByStateAndPlace() throws Exception {
            given(stationService.findStationsByStateAndPlaceContaining(State.MAHARASHTRA, "Mum"))
                    .willReturn(Collections.singletonList(s1));

            mockMvc.perform(get("/api/stations/search/state-place")
                            .param("state", "MAHARASHTRA")
                            .param("place", "Mum"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));

            verify(stationService, times(1)).findStationsByStateAndPlaceContaining(State.MAHARASHTRA, "Mum");
        }

        @Test
        @DisplayName("GET /api/stations/search?searchTerm= global search")
        void searchGlobal() throws Exception {
            given(stationService.searchStationsByNameOrPlace("Mumbai"))
                    .willReturn(Collections.singletonList(s1));

            mockMvc.perform(get("/api/stations/search").param("searchTerm", "Mumbai"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));

            verify(stationService, times(1)).searchStationsByNameOrPlace("Mumbai");
        }

        @Test
        @DisplayName("POST /api/stations/states returns by multiple states")
        void byMultipleStates() throws Exception {
            List<State> states = Arrays.asList(State.MAHARASHTRA, State.KERALA);
            given(stationService.findByStateIn(states)).willReturn(Arrays.asList(s1, s3));

            mockMvc.perform(post("/api/stations/states")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(states)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)));

            verify(stationService, times(1)).findByStateIn(states);
        }

        @Test
        @DisplayName("GET /api/stations/count/state/{state} returns count")
        void countByState() throws Exception {
            given(stationService.countStationsByState(State.MAHARASHTRA)).willReturn(2L);

            mockMvc.perform(get("/api/stations/count/state/{state}", "MAHARASHTRA"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("2"));

            verify(stationService, times(1)).countStationsByState(State.MAHARASHTRA);
        }

        @Test
        @DisplayName("GET /api/stations/places/state/{state} returns distinct places")
        void distinctPlacesByState() throws Exception {
            given(stationService.findDistinctPlacesByState(State.MAHARASHTRA))
                    .willReturn(Arrays.asList("Mumbai", "Pune"));

            mockMvc.perform(get("/api/stations/places/state/{state}", "MAHARASHTRA"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0]").value("Mumbai"))
                    .andExpect(jsonPath("$[1]").value("Pune"));

            verify(stationService, times(1)).findDistinctPlacesByState(State.MAHARASHTRA);
        }

        @Test
        @DisplayName("GET /api/stations/ordered returns ordered stations")
        void ordered() throws Exception {
            given(stationService.findAllOrderedByStateAndPlace()).willReturn(Arrays.asList(s1, s2, s3));

            mockMvc.perform(get("/api/stations/ordered"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)));

            verify(stationService, times(1)).findAllOrderedByStateAndPlace();
        }

        @Test
        @DisplayName("DELETE /api/stations/code/{code} deletes by code (alias)")
        void deleteByCodeAlias() throws Exception {
            doNothing().when(stationService).deleteByCode("BCT");

            mockMvc.perform(delete("/api/stations/code/{code}", "BCT"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Station deleted successfully"));

            verify(stationService, times(1)).deleteByCode("BCT");
        }

        @Test
        @DisplayName("DELETE /api/stations/name/{name} deletes by name")
        void deleteByName() throws Exception {
            doNothing().when(stationService).deleteByName("Mumbai Central");

            mockMvc.perform(delete("/api/stations/name/{name}", "Mumbai Central"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Station deleted successfully"));

            verify(stationService, times(1)).deleteByName("Mumbai Central");
        }
    }
}
