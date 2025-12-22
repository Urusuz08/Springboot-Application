package com.irtrains.train_service.model;

import com.irtrains.train_service.model.enums.State;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StationTest {

    @Test
    void testNoArgsConstructor() {
        Station station = new Station();
        assertThat(station).isNotNull();
    }

    @Test
    void testAllArgsConstructor() {
        Station station = new Station("SBC", "Bangalore", State.KARNATAKA, "Bangalore");
        assertThat(station.getCode()).isEqualTo("SBC");
        assertThat(station.getName()).isEqualTo("Bangalore");
        assertThat(station.getState()).isEqualTo(State.KARNATAKA);
        assertThat(station.getPlace()).isEqualTo("Bangalore");
    }

    @Test
    void testSettersAndGetters() {
        Station station = new Station();
        station.setCode("MYS");
        station.setName("Mysore");
        station.setState(State.KARNATAKA);
        station.setPlace("Mysore");

        assertThat(station.getCode()).isEqualTo("MYS");
        assertThat(station.getName()).isEqualTo("Mysore");
        assertThat(station.getState()).isEqualTo(State.KARNATAKA);
        assertThat(station.getPlace()).isEqualTo("Mysore");
    }
}
