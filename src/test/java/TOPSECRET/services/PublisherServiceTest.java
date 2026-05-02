package TOPSECRET.services;

import TOPSECRET.domain.Publisher.IPublisherRepository;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.Publisher.PublisherFactory;
import TOPSECRET.domain.ValueObjects.Contact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PublisherService}.
 *
 * <p>Verifies that {@code createAndSave} correctly delegates to the factory
 * and repository, and rejects null arguments with a {@link NullPointerException}.</p>
 */
@ExtendWith(MockitoExtension.class)
class PublisherServiceTest {

    @Mock
    private PublisherFactory factory;

    @Mock
    private IPublisherRepository repository;

    @InjectMocks
    private PublisherService service;

    // ========== Tests for createAndSave =========================

    @Test
    void createAndSave_shouldReturnSavedPublisher_whenInputIsValid() {
        // Arrange
        Contact contactDouble = mock(Contact.class);
        Publisher publisher = mock(Publisher.class);
        Publisher savedPublisher = mock(Publisher.class);

        when(factory.create("Penguin Books", "123456789", contactDouble)).thenReturn(publisher);
        when(repository.save(publisher)).thenReturn(savedPublisher);

        // Act
        Publisher result = service.createAndSave("Penguin Books", "123456789", contactDouble);

        // Assert
        assertSame(savedPublisher, result);
    }

    @Test
    void createAndSave_shouldThrowException_whenNameIsNull() {
        // Arrange
        Contact contactDouble = mock(Contact.class);

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> service.createAndSave(null, "123456789", contactDouble));
    }

    @Test
    void createAndSave_shouldThrowException_whenVatNumberIsNull() {
        // Arrange
        Contact contactDouble = mock(Contact.class);

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> service.createAndSave("Penguin Books", null, contactDouble));
    }

    @Test
    void createAndSave_shouldThrowException_whenContactIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> service.createAndSave("Penguin Books", "123456789", null));
    }
}
