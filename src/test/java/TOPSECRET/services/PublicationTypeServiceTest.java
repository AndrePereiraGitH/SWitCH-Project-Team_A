package TOPSECRET.services;

import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.PublicationType.PublicationTypeFactory;
import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        PublicationType result = service.create(format);

        // ASSERT
        assertEquals(entity, result);
        verify(repository).save(entity);
    }

    // VALIDATION
    @Test
    void shouldThrowWhenMediaFormatIsNull() {

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> service.create(null));
    }

    @Test
    void shouldThrowWhenMediaFormatIsBlank() {

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> service.create(" "));
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
                () -> service.create(format));

        verify(repository, never()).save(any());
    }
}