package com.mendes.desafio_tecnico_backend_nuven.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import com.mendes.desafio_tecnico_backend_nuven.model.Location;
import com.mendes.desafio_tecnico_backend_nuven.repository.LocationRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LocationRepositoryUnitTest {

    @Autowired
    private LocationRepository locationRepository;

    @Test
    @Sql(scripts = "/data/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void CreateAndFindAllByCreatedDateLocation() {
        List<Location> allLocations = locationRepository.findAllByOrderByCreatedAtAsc();

        assertNotNull(allLocations);
        assertInstanceOf(List.class, allLocations);
        assertEquals(2, allLocations.get(0).getId());
        assertEquals(1, allLocations.get(1).getId());
    }
}
