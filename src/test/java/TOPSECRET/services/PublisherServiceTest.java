package TOPSECRET.services;

import TOPSECRET.domain.Publisher.IPublisherRepository;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.Publisher.PublisherFactory;
import TOPSECRET.domain.ValueObjects.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PublisherService}.
 *
 * <p>Verifies that {@code createAndSave} correctly delegates to the factory
 * and repository, and rejects null arguments with a {@link NullPointerException}.</p>
 */
class PublisherServiceTest {

    private PublisherFactory factory;
    private IPublisherRepository repository;
    private PublisherService service;

    @BeforeEach
    void setUp() {
        factory = mock(PublisherFactory.class);
        repository = mock(IPublisherRepository.class);
        service = new PublisherService(factory, repository);
    }

    // ========== Tests for createAndSave =========================

    @Test
    void createAndSave_shouldReturnSavedPublisher_whenInputIsValid() {
        // Arrange
        Contact contact = mock(Contact.class);
        Publisher publisher = mock(Publisher.class);
        Publisher savedPublisher = mock(Publisher.class);

        when(factory.create("Penguin Books", "123456789", contact)).thenReturn(publisher);
        when(repository.save(publisher)).thenReturn(savedPublisher);

        // Act
        Publisher result = service.createAndSave("Penguin Books", "123456789", contact);

        // Assert
        assertSame(savedPublisher, result);
    }

    @Test
    void createAndSave_shouldThrowException_whenNameIsNull() {
        // Arrange
        Contact contact = mock(Contact.class);

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> service.createAndSave(null, "123456789", contact));
    }

    @Test
    void createAndSave_shouldThrowException_whenVatNumberIsNull() {
        // Arrange
        Contact contact = mock(Contact.class);

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> service.createAndSave("Penguin Books", null, contact));
    }

    @Test
    void createAndSave_shouldThrowException_whenContactIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> service.createAndSave("Penguin Books", "123456789", null));
    }
}
