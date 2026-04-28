package TOPSECRET.persistence.mem;


import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.AppraisalEntity.AppraisalEntityFactory;
import TOPSECRET.domain.AppraisalEntity.IAppraisalEntityRepository;
import TOPSECRET.domain.ValueObjects.AppraisalEntityId;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppraisalEntityRepositoryMemTest {

    // HELPER
    private AppraisalEntityRepositoryMem repo = new AppraisalEntityRepositoryMem();

    private AppraisalEntity entity(String idValue) {
        AppraisalEntity entity = mock(AppraisalEntity.class);
        when(entity.id()).thenReturn(new AppraisalEntityId(idValue));
        return entity;
    }

    // SAVE
    @Test
    void saveShouldAddEntity() {

        // ARRANGE
        AppraisalEntity entity = entity("1");

        // ACT
        repo.save(entity);

        // ASSERT
        assertEquals(1, repo.findAll().size());
    }

    @Test
    void saveShouldReplaceExistingEntity() {

        // ARRANGE
        AppraisalEntity entity1 = entity("1");
        AppraisalEntity entity2 = entity("1");

        repo.save(entity1);

        // ACT
        repo.save(entity2);

        // ASSERT
        assertEquals(1, repo.findAll().size());
    }

    @Test
    void saveShouldThrowWhenNull() {

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> repo.save(null));
    }

    // FIND BY ID
    @Test
    void findByIdShouldReturnEntity() {

        // ARRANGE
        AppraisalEntity entity = entity("1");
        repo.save(entity);

        // ACT
        Optional<AppraisalEntity> result = repo.findById(new AppraisalEntityId("1"));

        // ASSERT
        assertTrue(result.isPresent());
    }

    @Test
    void findByIdShouldReturnEmpty() {

        // ACT
        Optional<AppraisalEntity> result = repo.findById(new AppraisalEntityId("1"));

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void findByIdShouldThrowWhenNull() {

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> repo.findById(null));
    }

    // FIND ALL
    @Test
    void findAllShouldReturnAllEntities() {

        // ARRANGE
        repo.save(entity("1"));
        repo.save(entity("2"));

        // ACT
        List<AppraisalEntity> result = repo.findAll();

        // ASSERT
        assertEquals(2, result.size());
    }

    // EXISTS
    @Test
    void existsByIdShouldReturnTrue() {

        // ARRANGE
        repo.save(entity("1"));

        // ACT
        boolean result = repo.existsById(new AppraisalEntityId("1"));

        // ASSERT
        assertTrue(result);
    }

    @Test
    void existsByIdShouldReturnFalse() {

        // ACT
        boolean result = repo.existsById(new AppraisalEntityId("1"));

        // ASSERT
        assertFalse(result);
    }

    @Test
    void existsByIdShouldThrowWhenNull() {

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> repo.existsById(null));
    }

    // DELETE
    @Test
    void deleteShouldReturnTrueWhenExists() {

        // ARRANGE
        repo.save(entity("1"));

        // ACT
        boolean result = repo.deleteById(new AppraisalEntityId("1"));

        // ASSERT
        assertTrue(result);
    }

    @Test
    void deleteShouldReturnFalseWhenNotExists() {

        // ACT
        boolean result = repo.deleteById(new AppraisalEntityId("1"));

        // ASSERT
        assertFalse(result);
    }

    @Test
    void deleteShouldThrowWhenNull() {

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> repo.deleteById(null));
    }
}


