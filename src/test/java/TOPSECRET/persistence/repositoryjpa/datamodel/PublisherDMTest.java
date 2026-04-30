package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.PublisherID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PublisherDM}.
 *
 * <p>Verifies that the constructor correctly initialises all fields
 * and rejects null arguments with a {@link NullPointerException}.</p>
 */
class PublisherDMTest {

    @Test
    void constructor_shouldCreatePublisherDM_whenAllFieldsAreValid() {
        // Arrange
        PublisherID publisherIdDouble = mock(PublisherID.class);
        Contact contact = mock(Contact.class);

        // Act
        PublisherDM dm = new PublisherDM(publisherIdDouble, "Penguin Books", contact);

        // Assert
        assertSame(publisherIdDouble, dm.getId());
        assertEquals("Penguin Books", dm.getName());
        assertSame(contact, dm.getContact());
    }

    @Test
    void constructor_shouldThrowException_whenIdIsNull() {
        // Arrange
        Contact contact = mock(Contact.class);

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> new PublisherDM(null, "Penguin Books", contact));
    }

    @Test
    void constructor_shouldThrowException_whenNameIsNull() {
        // Arrange
        PublisherID publisherIdDouble = mock(PublisherID.class);
        Contact contact = mock(Contact.class);

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> new PublisherDM(publisherIdDouble, null, contact));
    }

    @Test
    void constructor_shouldThrowException_whenContactIsNull() {
        // Arrange
        PublisherID publisherIdDouble = mock(PublisherID.class);

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> new PublisherDM(publisherIdDouble, "Penguin Books", null));
    }

    @Test
    void noArgsConstructor_shouldCreateInstance_forJpaUsage() {
        // Act
        PublisherDM dm = new PublisherDM();

        // Assert
        assertNotNull(dm);
    }
}
