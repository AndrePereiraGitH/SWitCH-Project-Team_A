package TOPSECRET.services;

import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.PublicationType.PublicationTypeFactory;
import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PublicationTypeService}.
 *
 * <p>
 * Validates application service behavior including:
 * </p>
 *
 * <ul>
 *     <li>Creation of PublicationType instances</li>
 *     <li>Validation of input data</li>
 *     <li>Prevention of duplicate publication types</li>
 *     <li>Interaction with repository and factory</li>
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
class PublicationTypeServiceTest {

    // HELPER
    private final IPublicationTypeRepository repository = mock(IPublicationTypeRepository.class);
    private final PublicationTypeFactory factory = mock(PublicationTypeFactory.class);

    private final PublicationTypeService service =
            new PublicationTypeService(repository, factory);

    // CREATE SUCCESS
    @Test
    void shouldCreatePublicationTypeSuccessfully() {

        // ARRANGE
        String format = "Book";
        PublicationType entity = mock(PublicationType.class);

        when(repository.findByMediaFormat(format)).thenReturn(Optional.empty());
        when(factory.create(format)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);

        // ACT
        PublicationType result = service.createAndSave(format);

        // ASSERT
        assertEquals(entity, result);
        verify(repository).save(entity);
    }

    // VALIDATION
    @Test
    void shouldThrowWhenMediaFormatIsNull() {

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> service.createAndSave(null));
    }

    @Test
    void shouldThrowWhenMediaFormatIsBlank() {

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> service.createAndSave(" "));
    }

    @Test

    void shouldThrowWhenRepositoryIsNull() {

        // ARRANGE
        PublicationTypeFactory factory = new PublicationTypeFactory();

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,

                () -> new PublicationTypeService(null, factory));

    }

    @Test
    void shouldThrowWhenFactoryIsNull() {

        // ARRANGE
        IPublicationTypeRepository repo = mock(IPublicationTypeRepository.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new PublicationTypeService(repo, null));
    }

    // DUPLICATE
    @Test
    void shouldThrowWhenPublicationTypeAlreadyExists() {

        // ARRANGE
        String format = "Book";
        PublicationType existing = mock(PublicationType.class);

        when(repository.findByMediaFormat(format)).thenReturn(Optional.of(existing));

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> service.createAndSave(format));

        verify(repository, never()).save(any());
    }
}