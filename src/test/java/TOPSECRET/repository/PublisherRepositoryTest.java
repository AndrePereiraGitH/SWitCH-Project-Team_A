package TOPSECRET.repository;

import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Publisher;
import TOPSECRET.domain.PublisherFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PublisherRepository}.
 *
 * <p>This test class verifies the behaviour of {@link PublisherRepository}
 * in isolation by mocking its collaborators:
 * <ul>
 *     <li>{@link PublisherFactory} – responsible for creating Publisher objects</li>
 *     <li>{@link List} – the internal storage of publishers</li>
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
 * </ul>
 */
class PublisherRepositoryTest {

    // Mocked dependencies
    private final List<Publisher> publishers = mock(ArrayList.class);
    private final PublisherFactory publisherFactory = mock(PublisherFactory.class);

    // Repository under test
    private final PublisherRepository repository = new PublisherRepository(publishers, publisherFactory);

    private final String name = "Penguin Books";
    private final String vatNumber = "123456789";
    private final Contact contact = mock(Contact.class);

    /**
     * Verifies that the repository constructor throws an exception
     * when the list of publishers is null.
     */
    @Test
    void shouldThrowExceptionWhenListIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PublisherRepository(null, publisherFactory)
        );
        assertEquals("Publisher list cannot be null", exception.getMessage());
    }

    /**
     * Verifies that the repository delegates publisher creation
     * to the {@link PublisherFactory} and stores the result
     * in the internal publisher list.
     */
    @Test
    void shouldThrowExceptionWhenFactoryIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PublisherRepository(publishers, null)
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
        Publisher mockPublisher = mock(Publisher.class);

        when(publisherFactory.create(name, vatNumber, contact)).thenReturn(mockPublisher);

        // Needed because the repository iterates over the list
        when(publishers.iterator()).thenReturn(Collections.emptyIterator());

        Publisher result = repository.createAndAdd(name, vatNumber, contact);

        assertEquals(mockPublisher, result);
        verify(publisherFactory).create(name, vatNumber, contact);
        verify(publishers).add(mockPublisher);
    }

    /**
     * Verifies that a publisher with the same VAT number
     * cannot be added twice to the repository.
     */
    @Test
    void shouldRejectPublisherWhenAlreadyExists() {
        Publisher mockPublisher = mock(Publisher.class);

        when(publisherFactory.create(name, vatNumber, contact)).thenReturn(mockPublisher);
        when(publishers.iterator()).thenAnswer(invocation -> List.of(mockPublisher).iterator());

        Publisher result = repository.createAndAdd(name, vatNumber, contact);

        assertNull(result);
        verify(publishers, never()).add(any());
    }

    /**
     * Verifies that exceptions thrown by {@link PublisherFactory}
     * are propagated and no publisher is added to the repository.
     */
    @Test
    void shouldPropagateExceptionFromFactory() {
        when(publisherFactory.create(name, vatNumber, contact))
                .thenThrow(new IllegalArgumentException("Invalid data"));

        assertThrows(IllegalArgumentException.class,
                () -> repository.createAndAdd(name, vatNumber, contact));

        verify(publishers, never()).add(any());
    }

    /**
     * Verifies that {@link PublisherRepository#getAllPublishers()}
     * returns an unmodifiable list.
     */
    @Test
    void shouldReturnUnmodifiableList() {
        Publisher mockPublisher = mock(Publisher.class);
        when(publishers.toArray()).thenReturn(new Object[]{mockPublisher});

        List<Publisher> result = repository.getAllPublishers();

        assertThrows(UnsupportedOperationException.class, () -> result.add(mockPublisher));
    }
    /**
     * Verifies that {@link PublisherRepository#getAllPublishers()}
     * returns a defensive copy of the publisher list.
     */

    @Test
    void shouldReturnDefensiveCopy() {
        PublisherRepository realRepository = new PublisherRepository(new ArrayList<>(), publisherFactory);
        Publisher mockPublisher = mock(Publisher.class);

        when(publisherFactory.create(name, vatNumber, contact)).thenReturn(mockPublisher);
        realRepository.createAndAdd(name, vatNumber, contact);

        List<Publisher> result1 = realRepository.getAllPublishers();
        List<Publisher> result2 = realRepository.getAllPublishers();

        assertNotSame(result1, result2);
        assertEquals(result1, result2);
    }
    /**
     * Verifies that findByVatNumber returns the correct publisher
     * when a matching VAT number exists.
     */
    @Test
    void shouldFindPublisherByVatNumber() {
        Publisher mockPublisher = mock(Publisher.class);

        when(mockPublisher.getVatNumber()).thenReturn(vatNumber);
        when(publishers.iterator()).thenReturn(List.of(mockPublisher).iterator());

        Publisher result = repository.findByVatNumber(vatNumber);

        assertEquals(mockPublisher, result);
    }

    /**
     * Verifies that findByVatNumber returns null
     * when no publisher with the given VAT number exists.
     */
    @Test
    void shouldReturnNullWhenVatNumberDoesNotExist() {
        Publisher mockPublisher = mock(Publisher.class);

        when(mockPublisher.getVatNumber()).thenReturn("999999999");
        when(publishers.iterator()).thenReturn(List.of(mockPublisher).iterator());

        Publisher result = repository.findByVatNumber(vatNumber);

        assertNull(result);
    }

    @Test
    void findByVatNumber_shouldReturnNullWhenRepositoryEmpty() {
        when(publishers.iterator()).thenReturn(Collections.emptyIterator());

        Publisher result = repository.findByVatNumber(vatNumber);

        assertNull(result);
    }

    @Test
    void shouldAddPublisherWhenDifferentPublisherExists() {
        Publisher existingPublisher = mock(Publisher.class);
        Publisher newPublisher = mock(Publisher.class);

        when(publisherFactory.create(name, vatNumber, contact)).thenReturn(newPublisher);
        when(publishers.iterator()).thenReturn(List.of(existingPublisher).iterator());

        Publisher result = repository.createAndAdd(name, vatNumber, contact);

        assertEquals(newPublisher, result);
        verify(publishers).add(newPublisher);
    }
}