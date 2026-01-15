package com.aerienne.gestion.repository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.aerienne.gestion.model.SimpleEntity;

@DataJpaTest
class SimpleEntityRepositoryTest {
    @Autowired
    private SimpleEntityRepository simpleEntityRepository;

    @Test
    void testSaveAndFindById() {
        SimpleEntity entity = new SimpleEntity();
        entity.setId(1L);
        entity.setName("TestName");
        simpleEntityRepository.save(entity);

        Optional<SimpleEntity> found = simpleEntityRepository.findById(1L);
        assertTrue(found.isPresent());
        assertEquals("TestName", found.get().getName());
    }
}
