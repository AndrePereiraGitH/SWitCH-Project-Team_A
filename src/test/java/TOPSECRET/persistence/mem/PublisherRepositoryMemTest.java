package TOPSECRET.persistence.mem;

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

    private PublisherRepositoryMem repository;
    private Publisher publisher;
    private PublisherID publisherID;

    @BeforeEach
    void setUp() {
        repository = new PublisherRepositoryMem();
        publisherID = mock(PublisherID.class);
        publisher = mock(Publisher.class);
        when(publisher.id()).thenReturn(publisherID);
    }

    @Test
    void save_shouldReturnSamePublisher_whenPublisherDoesNotExist() {
        // Arrange

        // Act
        Publisher result = repository.save(publisher);

        // Assert
        assertSame(publisher, result);
    }

    @Test
    void save_shouldAddPublisherToRepository_whenPublisherDoesNotExist() {
        // Arrange

        // Act
        repository.save(publisher);

        // Assert
        assertTrue(repository.findAll().contains(publisher));
    }

    @Test
    void save_shouldThrowException_whenPublisherAlreadyExists() {
        // Arrange
        repository.save(publisher);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> repository.save(publisher));
    }

    @Test
    void existsById_shouldReturnTrue_whenPublisherExists() {
        // Arrange
        repository.save(publisher);

        // Act
        boolean result = repository.existsById(publisherID);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsById_shouldReturnFalse_whenPublisherDoesNotExist() {
        // Arrange

        // Act
        boolean result = repository.existsById(publisherID);

        // Assert
        assertFalse(result);
    }

    @Test
    void findByVatNumber_shouldReturnPublisher_whenPublisherExists() {
        // Arrange
        repository.save(publisher);

        // Act
        Optional<Publisher> result = repository.findByVatNumber(publisherID);

        // Assert
        assertTrue(result.isPresent());
        assertSame(publisher, result.get());
    }

    @Test
    void findByVatNumber_shouldReturnEmpty_whenPublisherDoesNotExist() {
        // Arrange

        // Act
        Optional<Publisher> result = repository.findByVatNumber(publisherID);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void existsById_shouldReturnFalse_whenDifferentPublisherInRepository() {
        // Arrange
        repository.save(publisher);
        PublisherID otherId = mock(PublisherID.class);

        // Act
        boolean result = repository.existsById(otherId);

        // Assert
        assertFalse(result);
    }

    @Test
    void findByVatNumber_shouldReturnEmpty_whenDifferentPublisherInRepository() {
        // Arrange
        repository.save(publisher);
        PublisherID otherId = mock(PublisherID.class);

        // Act
        Optional<Publisher> result = repository.findByVatNumber(otherId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_shouldReturnAllPublishers() {
        // Arrange
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
        repository.save(publisher);

        // Act
        List<Publisher> result = repository.findAll();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(publisher));
    }
}