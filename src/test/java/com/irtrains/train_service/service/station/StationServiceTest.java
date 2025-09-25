package com.irtrains.train_service.service.station;

import com.irtrains.train_service.model.enums.State;
import com.irtrains.train_service.model.station.Station;
import com.irtrains.train_service.repository.station.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StationServiceTest {

    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private StationService stationService;

    private Station station;

    @BeforeEach
    void setUp() {
        station = new Station("SBC", "Bangalore", State.KARNATAKA, "Bangalore");
    }

    @Test
    void testCreateStation() {
        when(stationRepository.save(any(Station.class))).thenReturn(station);
        Station created = stationService.createStation(station);
        assertThat(created.getCode()).isEqualTo(station.getCode());
        verify(stationRepository, times(1)).save(station);
    }

    @Test
    void testFindById() {
        when(stationRepository.findById("SBC")).thenReturn(Optional.of(station));
        Optional<Station> found = stationService.findById("SBC");
        assertThat(found).isPresent();
        assertThat(found.get().getCode()).isEqualTo("SBC");
    }

    @Test
    void testFindAll() {
        when(stationRepository.findAll()).thenReturn(Collections.singletonList(station));
        List<Station> stations = stationService.findAll();
        assertThat(stations).hasSize(1);
        assertThat(stations.get(0).getCode()).isEqualTo("SBC");
    }

    @Test
    void testDeleteById() {
        stationService.deleteById("SBC");
        verify(stationRepository, times(1)).deleteById("SBC");
    }
}

