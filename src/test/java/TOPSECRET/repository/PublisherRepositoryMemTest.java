package TOPSECRET.repository;

import TOPSECRET.domain.Publisher.IPublisherRepository;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.Publisher.PublisherFactory;
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
 * <p>This test class verifies the behaviour of {@link PublisherRepositoryMem}
 * in isolation by mocking its collaborators:
 * <ul>
 *     <li>{@link PublisherFactory} – responsible for creating Publisher objects</li>
 * </ul>
 *
 * <p>By mocking these dependencies, the tests ensure that only the repository
 * logic is being validated, without relying on the behaviour of other domain classes.
 *
 * <p><b>What is tested:</b>
 * <ul>
 *     <li>Constructor validation (null dependencies)</li>
 *     <li>Delegation of publisher creation to {@link PublisherFactory}</li>
 *     <li>Adding publishers to the internal list</li>
 *     <li>Preventing duplicate publishers</li>
 *     <li>Returning an unmodifiable list of publishers</li>
 *     <li>Returning a defensive copy of the publisher list</li>
 *     <li>Finding a publisher by VAT number</li>
 * </ul>
 */
class PublisherRepositoryMemTest {

    private PublisherFactory publisherFactory;

    private final String name = "Penguin Books";
    private final String vatNumber = "123456789";
    private final Contact contact = mock(Contact.class);

    @BeforeEach
    void setup() {
        publisherFactory = mock(PublisherFactory.class);
    }

    @Test
    void shouldThrowExceptionWhenFactoryIsNull() {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PublisherRepositoryMem(null)
        );
        assertEquals("PublisherFactory cannot be null", exception.getMessage());
    }


    @Test
    void shouldDelegateCreationToFactoryAndAddPublisher() {
        // Arrange
        Publisher publisherDouble = mock(Publisher.class);
        when(publisherFactory.create(name, vatNumber, contact)).thenReturn(publisherDouble);

        IPublisherRepository repository = new PublisherRepositoryMem(publisherFactory);

        // Act
        Publisher result = repository.createAndAdd(name, vatNumber, contact);

        // Assert
        assertEquals(publisherDouble, result);
        assertTrue(repository.getAllPublishers().contains(publisherDouble));
    }


    @Test
    void shouldRejectPublisherWhenAlreadyIsExistent() {
        // Arrange

        Publisher pub1 = mock(Publisher.class);

        when(publisherFactory.create(name,vatNumber,contact)).thenReturn(pub1);

        IPublisherRepository repository = new PublisherRepositoryMem(publisherFactory);

        repository.createAndAdd(name,vatNumber,contact);


        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAdd(name, vatNumber, contact));
    }


    @Test
    void shouldAddPublisherWhenDifferentPublisherAlreadyExists() {
        // Arrange
        String existingName = "Existing Publisher";
        String existingVatNumber = "987654321";
        Contact existingContact = mock(Contact.class);

        Publisher existingPublisher = mock(Publisher.class);
        Publisher newPublisher = mock(Publisher.class);

        when(publisherFactory.create(existingName, existingVatNumber, existingContact)).thenReturn(existingPublisher);
        when(publisherFactory.create(name, vatNumber, contact)).thenReturn(newPublisher);

        IPublisherRepository repository = new PublisherRepositoryMem(publisherFactory);
        repository.createAndAdd(existingName, existingVatNumber, existingContact);

        // Act
        Publisher result = repository.createAndAdd(name, vatNumber, contact);

        // Assert
        assertEquals(newPublisher, result);
        assertTrue(repository.getAllPublishers().contains(newPublisher));
        assertTrue(repository.getAllPublishers().contains(existingPublisher));
    }


    @Test
    void shouldReturnUnmodifiableList() {
        // Arrange
        Publisher publisherDouble = mock(Publisher.class);

        when(publisherFactory.create(name, vatNumber, contact)).thenReturn(publisherDouble);

        IPublisherRepository repository = new PublisherRepositoryMem(publisherFactory);
        repository.createAndAdd(name, vatNumber, contact);

        // Act
        List<Publisher> result = repository.getAllPublishers();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(publisherDouble));
    }

    @Test
    void shouldReturnDefensiveCopy() {
        // Arrange
        Publisher publisherDouble = mock(Publisher.class);

        when(publisherFactory.create(name, vatNumber, contact)).thenReturn(publisherDouble);

        IPublisherRepository repository = new PublisherRepositoryMem(publisherFactory);
        repository.createAndAdd(name, vatNumber, contact);

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
        Publisher publisherDouble = mock(Publisher.class);
        when(publisherDouble.id()).thenReturn(new PublisherID(vatNumber));

        when(publisherFactory.create(name, vatNumber, contact)).thenReturn(publisherDouble);

        IPublisherRepository repository = new PublisherRepositoryMem(publisherFactory);
        repository.createAndAdd(name,vatNumber,contact);

        // Act
        Optional<Publisher> result = repository.findByVatNumber(new PublisherID(vatNumber));

        // Assert
        assertTrue(result.isPresent());
        assertEquals(publisherDouble, result.get());
    }

    @Test
    void shouldReturnEmptyWhenVatNumberDoesNotExist() {
        // Arrange
        Publisher publisherDouble = mock(Publisher.class);
        when(publisherDouble.id()).thenReturn(new PublisherID("999999999"));

        when(publisherFactory.create(name, vatNumber, contact)).thenReturn(publisherDouble);

        IPublisherRepository repository = new PublisherRepositoryMem(publisherFactory);
        repository.createAndAdd(name,vatNumber,contact);

        // Act
        Optional<Publisher> result = repository.findByVatNumber(new PublisherID(vatNumber));

        // Assert
        assertTrue(result.isEmpty());
    }

}