package TOPSECRET.repository;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.Publisher;
import TOPSECRET.domain.PublisherFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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

    /**
     * Verifies that constructing the repository with a null factory
     * throws an {@link IllegalArgumentException}.
     */
    @Test
    void shouldThrowExceptionWhenFactoryIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PublisherRepositoryMem(null)
        );
        assertEquals("PublisherFactory cannot be null", exception.getMessage());
    }

    /**
     * Verifies that the repository delegates publisher creation
     * to the {@link PublisherFactory} and stores the result
     * in the internal publisher list.
     */
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

    /**
     * Verifies that a publisher with the same identity
     * cannot be added twice to the repository.
     */
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

    /**
     * Verifies that a second publisher with a different identity
     * can be added alongside an existing one.
     */
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

    /**
     * Verifies that {@link PublisherRepositoryMem#getAllPublishers()}
     * returns an unmodifiable list.
     */
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

    /**
     * Verifies that {@link PublisherRepositoryMem#getAllPublishers()}
     * returns a defensive copy of the publisher list.
     */
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

    /**
     * Verifies that {@code findByVatNumber} returns the correct publisher
     * when a matching VAT number exists in the repository.
     */
    @Test
    void shouldFindPublisherByVatNumber() {
        // Arrange
        Publisher publisherDouble = mock(Publisher.class);
        when(publisherDouble.getVatNumber()).thenReturn(vatNumber);

        when(publisherFactory.create(name, vatNumber, contact)).thenReturn(publisherDouble);

        IPublisherRepository repository = new PublisherRepositoryMem(publisherFactory);
        repository.createAndAdd(name,vatNumber,contact);

        // Act
        Publisher result = repository.findByVatNumber(vatNumber);

        // Assert
        assertEquals(publisherDouble, result);
    }

    /**
     * Verifies that {@code findByVatNumber} returns {@code null}
     * when no publisher with the given VAT number exists.
     */
    @Test
    void shouldReturnNullWhenVatNumberDoesNotExist() {
        // Arrange
        Publisher publisherDouble = mock(Publisher.class);
        when(publisherDouble.getVatNumber()).thenReturn("999999999");

        when(publisherFactory.create(name, vatNumber, contact)).thenReturn(publisherDouble);

        IPublisherRepository repository = new PublisherRepositoryMem(publisherFactory);
        repository.createAndAdd(name,vatNumber,contact);

        // Act
        Publisher result = repository.findByVatNumber(vatNumber);

        // Assert
        assertNull(result);
    }

}