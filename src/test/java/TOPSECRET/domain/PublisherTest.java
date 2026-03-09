package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link Publisher}.
 *
 * <p>This test class verifies the behaviour of the {@link Publisher} domain object
 * in isolation. The {@link Contact} dependency is mocked because the Publisher
 * class does not depend on the internal behaviour of Contact, only on its reference.</p>
 *
 * <p>The tests cover:</p>
 * <ul>
 *     <li>Successful creation with valid data</li>
 *     <li>Validation of blank name and VAT number</li>
 *     <li>Getter behaviour</li>
 *     <li>Equality and hash code based on VAT number</li>
 * </ul>
 */
class PublisherTest {

    // Arrange
    private final String name = "Asas";
    private final String vatNumber = "123456";
    private final Contact contact = mock(Contact.class);

    /**
     * Verifies that a Publisher is created successfully
     * when valid data is provided.
     */
    @Test
    void constructorShouldCreatePublisherWhenDataIsValid() {
        Publisher result = new Publisher(name, vatNumber, contact);
        assertNotNull(result);
    }

    /**
     * Verifies that an exception is thrown when the name is blank.
     */
    @Test
    void constructorShouldThrowAnExceptionWhenNameIsBlank() {
        String blankName = "";

        assertThrows(IllegalArgumentException.class,
                () -> new Publisher(blankName, vatNumber, contact));
    }

    /**
     * Verifies that an exception is thrown when the VAT number is blank.
     */
    @Test
    void constructorShouldThrowAnExceptionWhenVatNumberIsBlank() {
        String blankVatNumber = "";

        assertThrows(IllegalArgumentException.class,
                () -> new Publisher(name, blankVatNumber, contact));
    }

    /**
     * Verifies that getVatNumber returns the VAT number
     * provided during construction.
     */
    @Test
    void getVatNumber_shouldReturnVatNumber() {
        Publisher publisher = new Publisher(name, vatNumber, contact);

        String result = publisher.getVatNumber();

        assertEquals(vatNumber, result);
    }

    /**
     * Verifies that getName returns the name
     * provided during construction.
     */
    @Test
    void getName_shouldReturnName() {
        Publisher publisher = new Publisher(name, vatNumber, contact);

        String result = publisher.getName();

        assertEquals(name, result);
    }

    /**
     * Verifies that getContact returns the same contact reference
     * provided during construction.
     */
    @Test
    void getContact_shouldReturnContact() {
        Publisher publisher = new Publisher(name, vatNumber, contact);

        Contact result = publisher.getContact();

        assertEquals(contact, result);
    }

    /**
     * Verifies that equals returns true for the same instance.
     */
    @Test
    void equals_sameInstance_returnsTrue() {
        Publisher publisher = new Publisher(name, vatNumber, contact);

        assertEquals(publisher, publisher);
    }

    /**
     * Verifies that two publishers with the same VAT number are equal.
     */
    @Test
    void equals_sameVatNumber_returnsTrue() {
        Publisher p1 = new Publisher(name, "123456", contact);
        Publisher p2 = new Publisher("Other Name", "123456", contact);

        assertEquals(p1, p2);
    }

    /**
     * Verifies that two publishers with different VAT numbers are not equal.
     */
    @Test
    void equals_differentVatNumber_returnsFalse() {
        Publisher p1 = new Publisher(name, "123456", contact);
        Publisher p2 = new Publisher(name, "789012", contact);

        assertNotEquals(p1, p2);
    }

    /**
     * Verifies that equals returns false when compared with null.
     */
    @Test
    void equals_nullObject_returnsFalse() {
        Publisher publisher = new Publisher(name, "123456", contact);

        assertFalse(publisher.equals(null));
    }

    /**
     * Verifies that equals returns false when compared
     * with an object of a different class.
     */
    @Test
    void equals_differentClass_returnsFalse() {
        Publisher publisher = new Publisher(name, "123456", contact);

        assertFalse(publisher.equals("string"));
    }

    /**
     * Verifies that equal publishers produce the same hash code.
     */
    @Test
    void hashCode_sameVatNumber_sameHashCode() {
        Publisher p1 = new Publisher(name, "123456", contact);
        Publisher p2 = new Publisher("Other Name", "123456", contact);

        assertEquals(p1.hashCode(), p2.hashCode());
    }

    /**
     * Verifies that publishers with different VAT numbers
     * produce different hash codes.
     */
    @Test
    void hashCode_differentVatNumber_differentHashCode() {
        Publisher p1 = new Publisher(name, "123456", contact);
        Publisher p2 = new Publisher(name, "789012", contact);

        assertNotEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void getPublisherDetails_ShouldNeverReturnNull() {
        Publisher publisher = new Publisher(name, vatNumber, contact);

        Publisher.PublisherDetails details = publisher.getPublisherDetails();

        assertNotNull(details);
    }

    @Test
    void getPublisherDetails_ShouldReturnAllFieldsCorrectly() {
        Publisher publisher = new Publisher(name, vatNumber, contact);

        Publisher.PublisherDetails details = publisher.getPublisherDetails();

        assertNotNull(details);
        assertEquals(name, details.name());
        assertEquals(vatNumber, details.vatNumber());
        assertSame(contact, details.contact());
    }

    @Test
    void getPublisherDetails_ReturnsNewInstanceEachTime() {
        Publisher publisher = new Publisher(name, vatNumber, contact);

        Publisher.PublisherDetails d1 = publisher.getPublisherDetails();
        Publisher.PublisherDetails d2 = publisher.getPublisherDetails();

        assertNotSame(d1, d2);
    }

    @Test
    void publisherDetails_ShouldStoreValuesCorrectly() {
        Publisher.PublisherDetails details =
                new Publisher.PublisherDetails(name, vatNumber, contact);

        assertEquals(name, details.name());
        assertEquals(vatNumber, details.vatNumber());
        assertSame(contact, details.contact());
    }
    @Test
    void publisherDetails_equalObjects_shouldHaveSameHashCode() {
        Publisher.PublisherDetails d1 =
                new Publisher.PublisherDetails(name, vatNumber, contact);
        Publisher.PublisherDetails d2 =
                new Publisher.PublisherDetails(name, vatNumber, contact);

        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    void publisherDetails_EqualsAndHashCodeShouldWorkForEqualObjects() {
        Contact mockContact = mock(Contact.class);

        Publisher.PublisherDetails d1 =
                new Publisher.PublisherDetails(name, vatNumber, mockContact);
        Publisher.PublisherDetails d2 =
                new Publisher.PublisherDetails(name, vatNumber, mockContact);

        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    void publisherDetails_EqualsAndHashCodeShouldDetectDifferentObjects() {
        Contact mockContact = mock(Contact.class);

        Publisher.PublisherDetails d1 =
                new Publisher.PublisherDetails(name, vatNumber, mockContact);
        Publisher.PublisherDetails d2 =
                new Publisher.PublisherDetails("Different", vatNumber, mockContact);

        assertNotEquals(d1, d2);
    }
}