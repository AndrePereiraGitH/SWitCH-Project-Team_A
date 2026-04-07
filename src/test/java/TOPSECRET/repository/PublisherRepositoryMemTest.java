package TOPSECRET.repository;

import TOPSECRET.domain.Publisher.IPublisherRepository;
import TOPSECRET.domain.Publisher.PublisherFactory;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.PublisherID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublisherRepositoryMemTest {

    private IPublisherRepository repository;

    private final String vatNumber = "123456789";
    private final String name = "Penguin Books";
    private final Contact contact = mock(Contact.class);

    @BeforeEach
    void setup() {
        repository = new PublisherRepositoryMem();
    }

    @Test
    void shouldSaveAndReturnPublisher() {
        // Arrange
        Publisher publisher = mock(Publisher.class);

        // Act
        Publisher result = repository.save(publisher);

        // Assert
        assertEquals(publisher, result);
        assertTrue(repository.getAllPublishers().contains(publisher));
    }

    @Test
    void shouldRejectDuplicatePublisher() {
        // Arrange
        Contact contact = mock(Contact.class);
        Publisher publisher = new PublisherFactory().create("Penguin Books", "123456789", contact);
        repository.save(publisher);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> repository.save(publisher));
    }

    @Test
    void shouldSavePublisherWhenDifferentOneAlreadyExists() {
        // Arrange
        Publisher publisher1 = mock(Publisher.class);
        Publisher publisher2 = mock(Publisher.class);
        repository.save(publisher1);

        // Act
        Publisher result = repository.save(publisher2);

        // Assert
        assertEquals(publisher2, result);
        assertTrue(repository.getAllPublishers().contains(publisher1));
        assertTrue(repository.getAllPublishers().contains(publisher2));
    }

    @Test
    void shouldReturnUnmodifiableList() {
        // Arrange
        Publisher publisher = mock(Publisher.class);
        repository.save(publisher);

        // Act
        List<Publisher> result = repository.getAllPublishers();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(publisher));
    }

    @Test
    void shouldReturnDefensiveCopy() {
        // Arrange
        Publisher publisher = mock(Publisher.class);
        repository.save(publisher);

        // Act
        List<Publisher> result1 = repository.getAllPublishers();
        List<Publisher> result2 = repository.getAllPublishers();

        // Assert
        assertNotSame(result1, result2);
        assertEquals(result1, result2);
    }

    @Test
    void shouldFindPublisherByVatNumber() {
        // Arrange
        Publisher publisher = mock(Publisher.class);
        when(publisher.id()).thenReturn(new PublisherID(vatNumber));
        repository.save(publisher);

        // Act
        Optional<Publisher> result = repository.findByVatNumber(new PublisherID(vatNumber));

        // Assert
        assertTrue(result.isPresent());
        assertEquals(publisher, result.get());
    }

    @Test
    void shouldReturnEmptyWhenVatNumberDoesNotExist() {
        // Arrange
        Publisher publisher = mock(Publisher.class);
        when(publisher.id()).thenReturn(new PublisherID("999999999"));
        repository.save(publisher);

        // Act
        Optional<Publisher> result = repository.findByVatNumber(new PublisherID(vatNumber));

        // Assert
        assertTrue(result.isEmpty());
    }
}