package TOPSECRET.domain.Publisher;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.PublisherID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link PublisherFactory}.
 *
 * <p>This test class verifies that the {@link PublisherFactory}
 * correctly creates {@link Publisher} instances and handles
 * different input scenarios.</p>
 *
 * <p>The tests cover:</p>
 * <ul>
 *     <li>Successful creation of a Publisher with valid attributes</li>
 *     <li>Automatic trimming of name and VAT number</li>
 *     <li>Validation of blank and null input values</li>
 *     <li>Behaviour when optional values such as contact are null</li>
 * </ul>
 */
class PublisherFactoryTest {


    @Test
    void create_with_valid_attributes_should_succeed() {
        // Arrange
        PublisherFactory factory = new PublisherFactory();
        Contact contact = mock(Contact.class);

        // Act
        Publisher publisher = factory.create("Penguin Books", "123456789", contact);

        // Assert
        assertNotNull(publisher);
        assertEquals("Penguin Books", publisher.getName());
        assertEquals("123456789", publisher.getVatNumber());
        assertEquals(contact, publisher.getContact());
    }

    @Test
    void create_should_trim_name_and_vatNumber() {
        // Arrange
        PublisherFactory factory = new PublisherFactory();
        Contact contact = mock(Contact.class);

        // Act
        Publisher publisher = factory.create("  Penguin Books  ", "  123456789  ", contact);

        // Assert
        assertEquals("Penguin Books", publisher.getName());
        assertEquals("123456789", publisher.getVatNumber());
    }

    @Test
    void create_with_blank_name_should_throw_exception() {
        // Arrange
        PublisherFactory factory = new PublisherFactory();
        Contact contact = mock(Contact.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                factory.create("   ", "123456789", contact)
        );
    }

    @Test
    void create_with_blank_vatNumber_should_throw_exception() {
        // Arrange
        PublisherFactory factory = new PublisherFactory();
        Contact contact = mock(Contact.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                factory.create("Penguin Books", "   ", contact)
        );
    }


    @Test
    void create_with_null_name_should_throw_IllegalArgumentException() {
        // Arrange
        PublisherFactory factory = new PublisherFactory();
        Contact contact = mock(Contact.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                factory.create(null, "123456789", contact)
        );
    }

    /**
     * Verifies that an {@link IllegalArgumentException} is thrown
     * when attempting to create a publisher with a null VAT number.
     */
    @Test
    void create_with_null_vatNumber_should_throw_IllegalArgumentException() {
        // Arrange
        PublisherFactory factory = new PublisherFactory();
        Contact contact = mock(Contact.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                factory.create("Penguin Books", null, contact)
        );
    }

    /**
     * Verifies that a publisher can still be created when the contact
     * information is null.
     *
     * <p>This behaviour occurs because the {@link Publisher} constructor
     * does not validate the contact field.</p>
     */
    @Test
    void create_with_null_contact_should_succeed_because_publisher_does_not_validate_contact() {
        // Arrange
        PublisherFactory factory = new PublisherFactory();

        // Act
        Publisher publisher = factory.create("Penguin Books", "123456789", null);

        // Assert
        assertNotNull(publisher);
        assertNull(publisher.getContact());
    }

    @Test
    void create_withId_shouldReturnPublisher_whenAllFieldsAreValid() {
        // Arrange
        PublisherFactory factory = new PublisherFactory();
        PublisherID id = new PublisherID("123456789");
        Contact contact = mock(Contact.class);

        // Act
        Publisher result = factory.create(id, "Penguin Books", "123456789", contact);

        // Assert
        assertNotNull(result);
        assertSame(id, result.id());
        assertEquals("Penguin Books", result.getName());
    }
}
