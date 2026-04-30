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
 *     <li>Behaviour when required fields such as contact are null</li>
 * </ul>
 */
class PublisherFactoryTest {

    @Test
    void create_with_valid_attributes_should_succeed() {
        // Arrange
        PublisherFactory factory = new PublisherFactory();
        Contact contactDouble = mock(Contact.class);

        // Act
        Publisher publisher = factory.create("Penguin Books", "123456789", contactDouble);

        // Assert
        assertNotNull(publisher);
        assertEquals("Penguin Books", publisher.getName());
        assertEquals("123456789", publisher.getVatNumber());
        assertEquals(contactDouble, publisher.getContact());
    }

    @Test
    void create_should_trim_name_and_vatNumber() {
        // Arrange
        PublisherFactory factory = new PublisherFactory();
        Contact contactDouble = mock(Contact.class);

        // Act
        Publisher publisher = factory.create("  Penguin Books  ", "  123456789  ", contactDouble);

        // Assert
        assertEquals("Penguin Books", publisher.getName());
        assertEquals("123456789", publisher.getVatNumber());
    }

    @Test
    void create_with_blank_name_should_throw_exception() {
        // Arrange
        PublisherFactory factory = new PublisherFactory();
        Contact contactDouble = mock(Contact.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                factory.create("   ", "123456789", contactDouble)
        );
    }

    @Test
    void create_with_blank_vatNumber_should_throw_exception() {
        // Arrange
        PublisherFactory factory = new PublisherFactory();
        Contact contactDouble = mock(Contact.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                factory.create("Penguin Books", "   ", contactDouble)
        );
    }

    @Test
    void create_with_null_name_should_throw_IllegalArgumentException() {
        // Arrange
        PublisherFactory factory = new PublisherFactory();
        Contact contactDouble = mock(Contact.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                factory.create(null, "123456789", contactDouble)
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
        Contact contactDouble = mock(Contact.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                factory.create("Penguin Books", null, contactDouble)
        );
    }

    @Test
    void create_with_null_contact_should_throw_exception() {
        // Arrange
        PublisherFactory factory = new PublisherFactory();

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> factory.create("Penguin Books", "123456789", null));
    }

    @Test
    void create_withId_shouldReturnPublisher_whenAllFieldsAreValid() {
        // Arrange
        PublisherFactory factory = new PublisherFactory();
        PublisherID idDouble = mock(PublisherID.class);
        Contact contactDouble = mock(Contact.class);

        // Act
        Publisher result = factory.create(idDouble, "Penguin Books", "123456789", contactDouble);

        // Assert
        assertNotNull(result);
        assertSame(idDouble, result.id());
        assertEquals("Penguin Books", result.getName());
    }
}
