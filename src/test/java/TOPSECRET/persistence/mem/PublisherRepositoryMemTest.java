package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Publisher.IPublisherRepository;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.PublisherID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PublisherRepositoryMem}.
 *
 * <p>Verifies the in-memory repository behaviour: saving, duplicate rejection,
 * existence checks, lookup by VAT number, and list immutability.</p>
 */
class PublisherRepositoryMemTest {

    private Publisher publisher;
    private PublisherID publisherIdDouble;

    @BeforeEach
    void setUp() {
        publisherIdDouble = mock(PublisherID.class);
        publisher = mock(Publisher.class);
        when(publisher.id()).thenReturn(publisherIdDouble);
    }

    @Test
    void save_shouldReturnSamePublisher_whenPublisherDoesNotExist() {
        // Arrange
        IPublisherRepository repository = new PublisherRepositoryMem();

        // Act
        Publisher result = repository.save(publisher);

        // Assert
        assertSame(publisher, result);
    }

    @Test
    void save_shouldAddPublisherToRepository_whenPublisherDoesNotExist() {
        // Arrange
        IPublisherRepository repository = new PublisherRepositoryMem();

        // Act
        repository.save(publisher);

        // Assert
        assertTrue(repository.findAll().contains(publisher));
    }

    @Test
    void save_shouldThrowException_whenPublisherAlreadyExists() {
        // Arrange
        IPublisherRepository repository = new PublisherRepositoryMem();
        repository.save(publisher);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> repository.save(publisher));
    }

    @Test
    void existsById_shouldReturnTrue_whenPublisherExists() {
        // Arrange
        IPublisherRepository repository = new PublisherRepositoryMem();
        repository.save(publisher);

        // Act
        boolean result = repository.existsById(publisherIdDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsById_shouldReturnFalse_whenPublisherDoesNotExist() {
        // Arrange
        IPublisherRepository repository = new PublisherRepositoryMem();

        // Act
        boolean result = repository.existsById(publisherIdDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void findById_shouldReturnPublisher_whenPublisherExists() {
        // Arrange
        IPublisherRepository repository = new PublisherRepositoryMem();
        repository.save(publisher);

        // Act
        Optional<Publisher> result = repository.findById(publisherIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertSame(publisher, result.get());
    }

    @Test
    void findById_shouldReturnEmpty_whenPublisherDoesNotExist() {
        // Arrange
        IPublisherRepository repository = new PublisherRepositoryMem();

        // Act
        Optional<Publisher> result = repository.findById(publisherIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void existsById_shouldReturnFalse_whenDifferentPublisherInRepository() {
        // Arrange
        IPublisherRepository repository = new PublisherRepositoryMem();
        repository.save(publisher);
        PublisherID otherId = mock(PublisherID.class);

        // Act
        boolean result = repository.existsById(otherId);

        // Assert
        assertFalse(result);
    }

    @Test
    void findById_shouldReturnEmpty_whenDifferentPublisherInRepository() {
        // Arrange
        IPublisherRepository repository = new PublisherRepositoryMem();
        repository.save(publisher);
        PublisherID otherId = mock(PublisherID.class);

        // Act
        Optional<Publisher> result = repository.findById(otherId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_shouldReturnAllPublishers() {
        // Arrange
        IPublisherRepository repository = new PublisherRepositoryMem();
        repository.save(publisher);

        // Act
        List<Publisher> result = repository.findAll();

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(publisher));
    }

    @Test
    void findAll_shouldReturnUnmodifiableList() {
        // Arrange
        IPublisherRepository repository = new PublisherRepositoryMem();
        repository.save(publisher);

        // Act
        List<Publisher> result = repository.findAll();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(publisher));
    }
}
