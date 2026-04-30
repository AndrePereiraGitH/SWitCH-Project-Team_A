package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.Publisher.PublisherFactory;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.PublisherID;
import TOPSECRET.persistence.repositoryjpa.datamodel.PublisherDM;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PublisherAssembler}.
 *
 * <p>Verifies the conversion between {@link Publisher} domain objects
 * and {@link PublisherDM} persistence entities in both directions.</p>
 */
class PublisherAssemblerTest {

    // ========== Tests for toDM =================================

    @Test
    void toDM_shouldReturnPublisherDMWithCorrectFields_whenPublisherIsValid() {
        // Arrange
        PublisherFactory factory = mock(PublisherFactory.class);
        PublisherAssembler assembler = new PublisherAssembler(factory);
        PublisherID publisherIdDouble = mock(PublisherID.class);
        Contact contact = mock(Contact.class);
        Publisher publisher = mock(Publisher.class);
        when(publisher.id()).thenReturn(publisherIdDouble);
        when(publisher.getName()).thenReturn("Penguin Books");
        when(publisher.getContact()).thenReturn(contact);

        // Act
        PublisherDM result = assembler.toDM(publisher);

        // Assert
        assertSame(publisherIdDouble, result.getId());
        assertEquals("Penguin Books", result.getName());
        assertSame(contact, result.getContact());
    }

    @Test
    void toDM_shouldThrowException_whenPublisherIsNull() {
        // Arrange
        PublisherFactory factory = mock(PublisherFactory.class);
        PublisherAssembler assembler = new PublisherAssembler(factory);

        // Act + Assert
        assertThrows(NullPointerException.class, () -> assembler.toDM(null));
    }

    // ========== Tests for toDomain =============================

    @Test
    void toDomain_shouldReturnPublisher_whenDMIsValid() {
        // Arrange
        PublisherFactory factory = mock(PublisherFactory.class);
        PublisherAssembler assembler = new PublisherAssembler(factory);
        PublisherID publisherIdDouble = mock(PublisherID.class);
        Contact contact = mock(Contact.class);
        Publisher expectedPublisher = mock(Publisher.class);
        PublisherDM dm = mock(PublisherDM.class);

        when(dm.getId()).thenReturn(publisherIdDouble);
        when(dm.getName()).thenReturn("Penguin Books");
        when(dm.getContact()).thenReturn(contact);
        when(publisherIdDouble.getId()).thenReturn("123456789");
        when(factory.create(publisherIdDouble, "Penguin Books", "123456789", contact)).thenReturn(expectedPublisher);

        // Act
        Publisher result = assembler.toDomain(dm);

        // Assert
        assertSame(expectedPublisher, result);
    }

    @Test
    void toDomain_shouldThrowException_whenDMIsNull() {
        // Arrange
        PublisherFactory factory = mock(PublisherFactory.class);
        PublisherAssembler assembler = new PublisherAssembler(factory);

        // Act + Assert
        assertThrows(NullPointerException.class, () -> assembler.toDomain(null));
    }
}
