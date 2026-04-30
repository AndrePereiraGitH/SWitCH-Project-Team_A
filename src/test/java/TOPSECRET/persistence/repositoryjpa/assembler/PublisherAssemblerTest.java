package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.Publisher.PublisherFactory;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.PublisherID;
import TOPSECRET.persistence.repositoryjpa.datamodel.PublisherDM;
import org.junit.jupiter.api.BeforeEach;
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

    private PublisherFactory factory;
    private PublisherAssembler assembler;

    @BeforeEach
    void setUp() {
        factory = mock(PublisherFactory.class);
        assembler = new PublisherAssembler(factory);
    }

    // ========== Tests for toDM =================================

    @Test
    void toDM_shouldReturnPublisherDMWithCorrectFields_whenPublisherIsValid() {
        // Arrange
        PublisherID id = mock(PublisherID.class);
        Contact contact = mock(Contact.class);
        Publisher publisher = mock(Publisher.class);
        when(publisher.id()).thenReturn(id);
        when(publisher.getName()).thenReturn("Penguin Books");
        when(publisher.getContact()).thenReturn(contact);

        // Act
        PublisherDM result = assembler.toDM(publisher);

        // Assert
        assertSame(id, result.getId());
        assertEquals("Penguin Books", result.getName());
        assertSame(contact, result.getContact());
    }

    @Test
    void toDM_shouldThrowException_whenPublisherIsNull() {
        // Arrange + Act + Assert
        assertThrows(NullPointerException.class, () -> assembler.toDM(null));
    }

    // ========== Tests for toDomain =============================

    @Test
    void toDomain_shouldReturnPublisher_whenDMIsValid() {
        // Arrange
        PublisherID id = mock(PublisherID.class);
        Contact contact = mock(Contact.class);
        Publisher expectedPublisher = mock(Publisher.class);
        PublisherDM dm = mock(PublisherDM.class);

        when(dm.getId()).thenReturn(id);
        when(dm.getName()).thenReturn("Penguin Books");
        when(dm.getContact()).thenReturn(contact);
        when(id.getId()).thenReturn("123456789");
        when(factory.create(id, "Penguin Books", "123456789", contact)).thenReturn(expectedPublisher);

        // Act
        Publisher result = assembler.toDomain(dm);

        // Assert
        assertSame(expectedPublisher, result);
    }

    @Test
    void toDomain_shouldThrowException_whenDMIsNull() {
        // Arrange + Act + Assert
        assertThrows(NullPointerException.class, () -> assembler.toDomain(null));
    }
}