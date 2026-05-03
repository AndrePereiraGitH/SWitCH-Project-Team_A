package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.AppraisalEntity.*;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.persistence.repositoryjpa.assembler.AppraisalEntityAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.AppraisalEntityDM;
import TOPSECRET.persistence.repositoryjpa.jpa.IAppraisalEntityRepositoryJPA;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AppraisalEntityRepositoryJPAImpl}.
 *
 * <p>
 * Validates the behavior of the JPA repository implementation including:
 * </p>
 *
 * <ul>
 *     <li>Saving entities through the assembler and JPA repository</li>
 *     <li>Retrieving entities by ID</li>
 *     <li>Retrieving all entities</li>
 *     <li>Checking existence by ID</li>
 *     <li>Deleting entities</li>
 *     <li>Validation of invalid inputs</li>
 * </ul>
 *
 * <p>
 * Ensures correct interaction between layers, including:
 * </p>
 *
 * <ul>
 *     <li>Delegation to the underlying JPA repository</li>
 *     <li>Proper use of the assembler for conversions</li>
 *     <li>Preservation of domain integrity</li>
 * </ul>
 *
 * <p>
 * Tests rely on mocks for the JPA repository and assembler to isolate
 * repository logic and verify interactions.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class AppraisalEntityRepositoryJPAImplTest {

    @Mock
    private  IAppraisalEntityRepositoryJPA jpaRepo;

    @Mock
    private AppraisalEntityAssembler assembler;

    @InjectMocks
    private AppraisalEntityRepositoryJPAImpl repository;

    private AppraisalEntity entity() {return mock(AppraisalEntity.class);}
    private AppraisalEntityDM dm() {return mock(AppraisalEntityDM.class);}
    private AppraisalEntityId id() {return mock(AppraisalEntityId.class);}

    // SAVE
    @Test
    void saveShouldConvertAndPersist() {

        // ARRANGE
        AppraisalEntity entity = entity();
        AppraisalEntityDM dm = dm();

        when(assembler.toDM(entity)).thenReturn(dm);
        when(jpaRepo.save(dm)).thenReturn(dm);
        when(assembler.toDomain(dm)).thenReturn(entity);

        // ACT
        AppraisalEntity result = repository.save(entity);

        // ASSERT
        assertEquals(entity, result);
    }

    @Test
    void saveShouldThrowWhenNull() {

        // ARRANGE

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> repository.save(null));
    }

    // FIND BY ID
    @Test
    void findByIdShouldReturnEntity() {

        // ARRANGE
        AppraisalEntityId id = id();
        AppraisalEntityDM dm = dm();
        AppraisalEntity entity = entity();

        when(jpaRepo.findById(id)).thenReturn(Optional.of(dm));
        when(assembler.toDomain(dm)).thenReturn(entity);

        // ACT
        Optional<AppraisalEntity> result = repository.findById(id);

        // ASSERT
        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    void findByIdShouldReturnEmpty() {

        // ARRANGE
        AppraisalEntityId id = id();
        when(jpaRepo.findById(id)).thenReturn(Optional.empty());

        // ACT
        Optional<AppraisalEntity> result = repository.findById(id);

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void findByIdShouldThrowWhenNull() {

        // ARRANGE

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> repository.findById(null));
    }

    // FIND ALL
    @Test
    void findAllShouldReturnConvertedList() {

        // ARRANGE
        AppraisalEntityDM dm = dm();
        AppraisalEntity entity = entity();

        when(jpaRepo.findAll()).thenReturn(List.of(dm));
        when(assembler.toDomain(dm)).thenReturn(entity);

        // ACT
        List<AppraisalEntity> result = repository.findAll();

        // ASSERT
        assertEquals(1, result.size());
        assertEquals(entity, result.get(0));
    }

    // EXISTS
    @Test
    void existsByIdShouldReturnTrue() {

        // ARRANGE
        AppraisalEntityId id = id();
        when(jpaRepo.existsById(id)).thenReturn(true);

        // ACT
        boolean result = repository.existsById(id);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void existsByIdShouldThrowWhenNull() {

        // ARRANGE

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> repository.existsById(null));
    }

    @Test
    void shouldThrowWhenRepositoryIsNull() {

        // ARRANGE
        AppraisalEntityAssembler assembler = mock(AppraisalEntityAssembler.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new AppraisalEntityRepositoryJPAImpl(null, assembler));
    }

    @Test
    void shouldThrowWhenAssemblerIsNull() {

        // ARRANGE
        IAppraisalEntityRepositoryJPA repo = mock(IAppraisalEntityRepositoryJPA.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new AppraisalEntityRepositoryJPAImpl(repo, null));
    }
    @Test
    void shouldReturnTrueWhenEntityExists() {

        // ARRANGE
        AppraisalEntityId id = mock(AppraisalEntityId.class);

        when(jpaRepo.existsById(id)).thenReturn(true);

        // ACT
        boolean result = repository.existsById(id);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenEntityDoesNotExist() {

        // ARRANGE
        AppraisalEntityId id = mock(AppraisalEntityId.class);

        when(jpaRepo.existsById(id)).thenReturn(false);

        // ACT
        boolean result = repository.existsById(id);

        // ASSERT
        assertFalse(result);
    }
}