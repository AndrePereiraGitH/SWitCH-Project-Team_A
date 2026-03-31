package TOPSECRET.domain;

import TOPSECRET.domain.ValueObjects.Contact;
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

    /**
     * Verifies that the factory successfully creates a {@link Publisher}
     * when valid attributes are provided.
     *
     * <p>The test checks that:</p>
     * <ul>
     *     <li>The created publisher is not null</li>
     *     <li>The name is correctly assigned</li>
     *     <li>The VAT number is correctly assigned</li>
     *     <li>The contact information is preserved</li>
     * </ul>
     */
    @Test
    void create_with_valid_attributes_should_succeed() {
        PublisherFactory factory = new PublisherFactory();

        Contact contact = mock(Contact.class);

        Publisher publisher = factory.create("Penguin Books", "123456789", contact);

        assertNotNull(publisher);
        assertEquals("Penguin Books", publisher.getName());
        assertEquals("123456789", publisher.getVatNumber());
        assertEquals(contact, publisher.getContact());
    }
    /**
     * Verifies that the {@link PublisherFactory} trims leading
     * and trailing whitespace from the publisher name and VAT number
     * before creating the {@link Publisher}.
     */
    @Test
    void create_should_trim_name_and_vatNumber() {
        PublisherFactory factory = new PublisherFactory();

        Contact contact = mock(Contact.class);

        Publisher publisher = factory.create("  Penguin Books  ", "  123456789  ", contact);

        assertEquals("Penguin Books", publisher.getName());
        assertEquals("123456789", publisher.getVatNumber());
    }

    /**
     * Verifies that creating a publisher with a blank name
     * results in an {@link IllegalArgumentException}.
     *
     * <p>The validation is performed by the {@link Publisher} constructor.</p>
     */
    @Test
    void create_with_blank_name_should_throw_exception() {
        PublisherFactory factory = new PublisherFactory();

        Contact contact = mock(Contact.class);

        assertThrows(IllegalArgumentException.class, () ->
                factory.create("   ", "123456789", contact)
        );
    }

    /**
     * Verifies that creating a publisher with a blank VAT number
     * results in an {@link IllegalArgumentException}.
     *
     * <p>The validation is performed by the {@link Publisher} constructor.</p>
     */
    @Test
    void create_with_blank_vatNumber_should_throw_exception() {
        PublisherFactory factory = new PublisherFactory();

        Contact contact = mock(Contact.class);

        assertThrows(IllegalArgumentException.class, () ->
                factory.create("Penguin Books", "   ", contact)
        );
    }

    /**
     * Verifies that a {@link NullPointerException} is thrown
     * when attempting to create a publisher with a null name.
     */
    @Test
    void create_with_null_name_should_throw_nullPointerException() {
        PublisherFactory factory = new PublisherFactory();

        Contact contact = mock(Contact.class);

        assertThrows(NullPointerException.class, () ->
                factory.create(null, "123456789", contact)
        );
    }

    /**
     * Verifies that a {@link NullPointerException} is thrown
     * when attempting to create a publisher with a null VAT number.
     */
    @Test
    void create_with_null_vatNumber_should_throw_nullPointerException() {
        PublisherFactory factory = new PublisherFactory();

        Contact contact = mock(Contact.class);

        assertThrows(NullPointerException.class, () ->
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
        PublisherFactory factory = new PublisherFactory();

        Publisher publisher = factory.create("Penguin Books", "123456789", null);

        assertNotNull(publisher);
        assertNull(publisher.getContact());
    }
}