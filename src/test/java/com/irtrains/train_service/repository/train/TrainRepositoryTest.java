package com.irtrains.train_service.repository.train;

import com.irtrains.train_service.model.enums.Type;
import com.irtrains.train_service.model.train.train;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = {
        "spring.datasource.train.url=jdbc:h2:mem:trainRepoTestDb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false",
        "spring.datasource.train.driver-class-name=org.h2.Driver",
        "spring.datasource.train.username=sa",
        "spring.datasource.train.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.liquibase.enabled=false"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureTestEntityManager
@DisplayName("TrainRepository JPA tests")
class TrainRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private TrainRepository repo;

    private train make(String id, String name, Type type, String src, String dst) {
        train t = new train();
        t.setTrainID(id);
        t.setName(name);
        t.setType(type);
        t.setSourceStation(src);
        t.setDestinationStation(dst);
        return t;
    }

    @Test
    void saveAndFindByIdAndName() {
        train t = make("12345", "Rajdhani Express", Type.RAJDHANI, "BCT", "NDLS");
        em.persistAndFlush(t);

        assertTrue(repo.findById("12345").isPresent());
        assertNotNull(repo.findByTrainId("12345"));
        assertTrue(repo.findByName("Rajdhani Express").isPresent());
        assertTrue(repo.existsByTrainId("12345"));
        assertTrue(repo.existsByName("Rajdhani Express"));
    }

    @Test
    void typeAndRouteQueries() {
        train a = make("11111", "Shatabdi Express", Type.SHATABDI, "NDLS", "BCT");
        train b = make("22222", "Rajdhani Express", Type.RAJDHANI, "BCT", "NDLS");
        em.persist(a);
        em.persist(b);
        em.flush();

        List<train> byType = repo.findByType(Type.RAJDHANI);
        assertEquals(1, byType.size());

        assertEquals(1, repo.findBySourceStation("BCT").size());
        assertEquals(1, repo.findByDestinationStation("NDLS").size());
        assertEquals(1, repo.findBySourceStationAndDestinationStation("BCT", "NDLS").size());

        List<train> bothWays = repo.findBidirectionalBetween("BCT", "NDLS");
        assertEquals(2, bothWays.size());
    }

    @Test
    void searchByNameOrIdAndDelete() {
        train a = make("33333", "Kerala Express", Type.EXPRESS, "ERS", "SBC");
        em.persistAndFlush(a);

        assertEquals(1, repo.searchByNameOrId("Ker").size());
        assertEquals(1, repo.searchByNameOrId("333").size());

        repo.deleteByTrainId("33333");
        assertFalse(repo.findById("33333").isPresent());
    }
}
