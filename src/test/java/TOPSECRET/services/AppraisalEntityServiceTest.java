package TOPSECRET.services;

import TOPSECRET.domain.AppraisalEntity.*;
import TOPSECRET.domain.ValueObjects.*;

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
 * Unit tests for {@link AppraisalEntityService}.
 *
 * <p>
 * Validates application service behavior including:
 * </p>
 *
 * <ul>
 *     <li>Creation of AppraisalEntity instances</li>
 *     <li>Update operations and state changes</li>
 *     <li>Validation of input data</li>
 *     <li>Prevention of invalid or duplicate operations</li>
 * </ul>
 *
 * <p>
 * Ensures that business rules are enforced before delegating
 * persistence operations to the repository layer.
 * </p>
 *
 * <p>
 * Tests rely on mocks for repository and factory to isolate
 * service logic and verify interactions.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class AppraisalEntityServiceTest {

    @Mock
    private IAppraisalEntityRepository repository;

    @Mock
    private AppraisalEntityFactory factory;

    @InjectMocks
    private AppraisalEntityService service;

    // HELPERS
    private Contact contact() {return mock(Contact.class);}
    private List<PublicationTypeId> pubs() {return List.of(mock(PublicationTypeId.class));}
    private List<GenreID> genres() {return List.of(mock(GenreID.class));}
    private AppraisalEntity entity() {return mock(AppraisalEntity.class);}
    private AppraisalEntityId id() {return new AppraisalEntityId("1");}

    // CREATE
    @Test
    void create_shouldCreateAndSaveEntity() {

        // ARRANGE
        Contact contact = mock(Contact.class);
        PublicationTypeId pub = mock(PublicationTypeId.class);
        GenreID genre = mock(GenreID.class);
        AppraisalEntity entity = mock(AppraisalEntity.class);

        when(factory.create(anyString(), any(), anyList(), anyList()))
                .thenReturn(entity);

        when(repository.save(entity)).thenReturn(entity);

        // ACT
        AppraisalEntity result = service.createAndSave(
                "Test",
                contact,
                List.of(pub),
                List.of(genre)
        );

        // ASSERT
        assertEquals(entity, result);
    }

    // UPDATE
    @Test
    void updateShouldUpdateAndSaveEntity() {
        // ARRANGE
        AppraisalEntity entity = entity();

        when(repository.findById(id())).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);

        // ACT
        AppraisalEntity result = service.update(
                id(),
                "NewName",
                contact(),
                pubs(),
                genres()
        );

        // ASSERT
        assertEquals(entity, result);
    }

    @Test
    void updateShouldThrowWhenNotFound() {

        // ARRANGE
        when(repository.findById(id())).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                service.update(id(), "Name", contact(), pubs(), genres())
        );
    }

    @Test
    void updateShouldThrowWhenIdNull() {
        // ARRANGE

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                service.update(null, "Name", contact(), pubs(), genres())
        );
    }
}