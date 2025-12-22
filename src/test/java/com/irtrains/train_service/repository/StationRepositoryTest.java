package com.irtrains.train_service.repository;

import com.irtrains.train_service.model.enums.State;
import com.irtrains.train_service.model.Station;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class StationRepositoryTest {

    @Autowired
    private StationRepository stationRepository;

    @Test
    public void testFindByState() {
        Station station = new Station("SBC", "Bangalore", State.KARNATAKA, "Bangalore");
        stationRepository.save(station);
        List<Station> stations = stationRepository.findByState(State.KARNATAKA);
        assertThat(stations).isNotEmpty();
        assertThat(stations.get(0).getState()).isEqualTo(State.KARNATAKA);
    }

    @Test
    public void testFindByPlace() {
        Station station = new Station("MYS", "Mysore", State.KARNATAKA, "Mysore");
        stationRepository.save(station);
        List<Station> stations = stationRepository.findByPlace("Mysore");
        assertThat(stations).isNotEmpty();
        assertThat(stations.get(0).getPlace()).isEqualTo("Mysore");
    }

    @Test
    public void testFindByName() {
        Station station = new Station("MAQ", "Mangalore", State.KARNATAKA, "Mangalore");
        stationRepository.save(station);
        Optional<Station> foundStation = stationRepository.findByName("Mangalore");
        assertThat(foundStation).isPresent();
        assertThat(foundStation.get().getName()).isEqualTo("Mangalore");
    }

    @Test
    public void testExistsByName() {
        Station station = new Station("UDUPI", "UD", State.KARNATAKA, "Udupi");
        stationRepository.save(station);
        boolean exists = stationRepository.existsByName("UD");
        assertThat(exists).isTrue();
    }

    @Test
    public void testExistsByCode() {
        Station station = new Station("BLR", "Bangalore", State.KARNATAKA, "Bangalore");
        stationRepository.save(station);
        boolean exists = stationRepository.existsByCode("BLR");
        assertThat(exists).isTrue();
    }
}

