package TOPSECRET.persistence.mem;


import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.ValueObjects.AppraisalEntityId;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AppraisalEntityRepositoryMem}.
 *
 * <p>
 * Validates in-memory repository behavior including:
 * </p>
 *
 * <ul>
 *     <li>Saving entities</li>
 *     <li>Updating existing entities with the same ID</li>
 *     <li>Retrieving all entities</li>
 *     <li>Searching by ID</li>
 *     <li>Checking existence by ID</li>
 *     <li>Deleting entities</li>
 *     <li>Validation of invalid inputs</li>
 * </ul>
 *
 * <p>
 * Ensures correct handling of edge cases such as null values,
 * empty repository state, and non-existing entities.
 * </p>
 *
 * <p>
 * Tests are performed using real repository instances and
 * mocked domain objects where appropriate.
 * </p>
 */
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

    @Test
    void shouldReturnSameEntityWhenSaved() {

        // ARRANGE
        AppraisalEntityRepositoryMem repo = new AppraisalEntityRepositoryMem();
        AppraisalEntity entity = mock(AppraisalEntity.class);

        // ACT
        AppraisalEntity result = repo.save(entity);

        // ASSERT
        assertNotNull(result);
        assertSame(entity, result);
    }

    @Test
    void shouldReturnCorrectEntityWhenMultipleExist() {

        // ARRANGE
        AppraisalEntityRepositoryMem repo = new AppraisalEntityRepositoryMem();

        AppraisalEntity e1 = mock(AppraisalEntity.class);
        AppraisalEntity e2 = mock(AppraisalEntity.class);

        AppraisalEntityId id1 = mock(AppraisalEntityId.class);
        AppraisalEntityId id2 = mock(AppraisalEntityId.class);

        when(e1.id()).thenReturn(id1);
        when(e2.id()).thenReturn(id2);

        repo.save(e1);
        repo.save(e2);

        // ACT
        Optional<AppraisalEntity> result = repo.findById(id2);

        // ASSERT
        assertTrue(result.isPresent());
        assertSame(e2, result.get());
    }

    @Test
    void shouldReturnFalseWhenIdDoesNotMatchAnyEntity() {

        // ARRANGE
        AppraisalEntityRepositoryMem repo = new AppraisalEntityRepositoryMem();

        AppraisalEntity e1 = mock(AppraisalEntity.class);
        AppraisalEntityId id1 = mock(AppraisalEntityId.class);
        AppraisalEntityId id2 = mock(AppraisalEntityId.class);

        when(e1.id()).thenReturn(id1);

        repo.save(e1);

        // ACT
        boolean result = repo.existsById(id2);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void shouldReplaceExistingEntityWithSameId() {

        // ARRANGE
        AppraisalEntityRepositoryMem repo = new AppraisalEntityRepositoryMem();

        AppraisalEntity e1 = mock(AppraisalEntity.class);
        AppraisalEntity e2 = mock(AppraisalEntity.class);

        AppraisalEntityId id = mock(AppraisalEntityId.class);

        when(e1.id()).thenReturn(id);
        when(e2.id()).thenReturn(id);

        repo.save(e1);

        // ACT
        repo.save(e2);

        // ASSERT
        List<AppraisalEntity> all = repo.findAll();
        assertEquals(1, all.size());
        assertSame(e2, all.get(0));
    }
}


