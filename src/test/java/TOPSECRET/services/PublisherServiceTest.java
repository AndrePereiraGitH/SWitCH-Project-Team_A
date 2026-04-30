package TOPSECRET.services;

import TOPSECRET.domain.Publisher.IPublisherRepository;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.Publisher.PublisherFactory;
import TOPSECRET.domain.ValueObjects.Contact;
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

    // ========== Tests for createAndSave =========================

    @Test
    void createAndSave_shouldReturnSavedPublisher_whenInputIsValid() {
        // Arrange
        PublisherFactory factory = mock(PublisherFactory.class);
        IPublisherRepository repository = mock(IPublisherRepository.class);
        PublisherService service = new PublisherService(factory, repository);
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
        PublisherFactory factory = mock(PublisherFactory.class);
        IPublisherRepository repository = mock(IPublisherRepository.class);
        PublisherService service = new PublisherService(factory, repository);
        Contact contactDouble = mock(Contact.class);

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> service.createAndSave(null, "123456789", contactDouble));
    }

    @Test
    void createAndSave_shouldThrowException_whenVatNumberIsNull() {
        // Arrange
        PublisherFactory factory = mock(PublisherFactory.class);
        IPublisherRepository repository = mock(IPublisherRepository.class);
        PublisherService service = new PublisherService(factory, repository);
        Contact contactDouble = mock(Contact.class);

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> service.createAndSave("Penguin Books", null, contactDouble));
    }

    @Test
    void createAndSave_shouldThrowException_whenContactIsNull() {
        // Arrange
        PublisherFactory factory = mock(PublisherFactory.class);
        IPublisherRepository repository = mock(IPublisherRepository.class);
        PublisherService service = new PublisherService(factory, repository);

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> service.createAndSave("Penguin Books", "123456789", null));
    }
}
