package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
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

    private final String name = "Asas";
    private final String vatNumber = "123456";
    private final Contact contact = mock(Contact.class);
    private PublisherFactory factory;

    @BeforeEach
    void setup() {
        factory = new PublisherFactory();
    }

    /**
     * Verifies that a Publisher is created successfully
     * when valid data is provided.
     */
    @Test
    void constructorShouldCreatePublisherWhenDataIsValid() {
        // Arrange + Act
        Publisher result = factory.create(name, vatNumber, contact);

        // Assert
        assertNotNull(result);
    }

    /**
     * Verifies that an exception is thrown when the name is blank.
     */
    @Test
    void constructorShouldThrowAnExceptionWhenNameIsBlank() {
        // Arrange
        String blankName = "";

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(blankName, vatNumber, contact));
    }

    /**
     * Verifies that an exception is thrown when the VAT number is blank.
     */
    @Test
    void constructorShouldThrowAnExceptionWhenVatNumberIsBlank() {
        // Arrange
        String blankVatNumber = "";

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(name, blankVatNumber, contact));
    }

    /**
     * Verifies that getVatNumber returns the VAT number
     * provided during construction.
     */
    @Test
    void getVatNumber_shouldReturnVatNumber() {
        // Arrange
        Publisher publisher = factory.create(name, vatNumber, contact);

        // Act
        String result = publisher.getVatNumber();

        // Assert
        assertEquals(vatNumber, result);
    }

    /**
     * Verifies that getName returns the name
     * provided during construction.
     */
    @Test
    void getName_shouldReturnName() {
        // Arrange
        Publisher publisher = factory.create(name, vatNumber, contact);

        // Act
        String result = publisher.getName();

        // Assert
        assertEquals(name, result);
    }

    /**
     * Verifies that getContact returns the same contact reference
     * provided during construction.
     */
    @Test
    void getContact_shouldReturnContact() {
        // Arrange
        Publisher publisher = factory.create(name, vatNumber, contact);

        // Act
        Contact result = publisher.getContact();

        // Assert
        assertEquals(contact, result);
    }

    /**
     * Verifies that equals returns true for the same instance.
     */
    @Test
    void equals_sameInstance_returnsTrue() {
        // Arrange
        Publisher publisher = factory.create(name, vatNumber, contact);

        // Act + Assert
        assertEquals(publisher, publisher);
    }

    /**
     * Verifies that two publishers with the same VAT number are equal.
     */
    @Test
    void equals_sameVatNumber_returnsTrue() {
        // Arrange
        Publisher p1 = factory.create(name, "123456", contact);
        Publisher p2 = factory.create("Other Name", "123456", contact);

        // Act + Assert
        assertEquals(p1, p2);
    }

    /**
     * Verifies that two publishers with different VAT numbers are not equal.
     */
    @Test
    void equals_differentVatNumber_returnsFalse() {
        // Arrange
        Publisher p1 = factory.create(name, "123456", contact);
        Publisher p2 = factory.create(name, "789012", contact);

        // Act + Assert
        assertNotEquals(p1, p2);
    }

    /**
     * Verifies that equals returns false when compared with null.
     */
    @Test
    void equals_nullObject_returnsFalse() {
        // Arrange
        Publisher publisher = factory.create(name, "123456", contact);

        // Act + Assert
        assertFalse(publisher.equals(null));
    }

    /**
     * Verifies that equals returns false when compared
     * with an object of a different class.
     */
    @Test
    void equals_differentClass_returnsFalse() {
        // Arrange
        Publisher publisher = factory.create(name, "123456", contact);

        // Act + Assert
        assertFalse(publisher.equals("string"));
    }

    /**
     * Verifies that equal publishers produce the same hash code.
     */
    @Test
    void hashCode_sameVatNumber_sameHashCode() {
        // Arrange
        Publisher p1 = factory.create(name, "123456", contact);
        Publisher p2 = factory.create("Other Name", "123456", contact);

        // Act + Assert
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    /**
     * Verifies that publishers with different VAT numbers
     * produce different hash codes.
     */
    @Test
    void hashCode_differentVatNumber_differentHashCode() {
        // Arrange
        Publisher p1 = factory.create(name, "123456", contact);
        Publisher p2 = factory.create(name, "789012", contact);

        // Act + Assert
        assertNotEquals(p1.hashCode(), p2.hashCode());
    }

    /**
     * Verifies that getPublisherDetails never returns null.
     */
    @Test
    void getPublisherDetails_ShouldNeverReturnNull() {
        // Arrange
        Publisher publisher = factory.create(name, vatNumber, contact);

        // Act
        Publisher.PublisherDetails details = publisher.getPublisherDetails();

        // Assert
        assertNotNull(details);
    }

    /**
     * Verifies that getPublisherDetails returns all fields correctly.
     */
    @Test
    void getPublisherDetails_ShouldReturnAllFieldsCorrectly() {
        // Arrange
        Publisher publisher = factory.create(name, vatNumber, contact);

        // Act
        Publisher.PublisherDetails details = publisher.getPublisherDetails();

        // Assert
        assertNotNull(details);
        assertEquals(name, details.name());
        assertEquals(vatNumber, details.vatNumber());
        assertSame(contact, details.contact());
    }

    /**
     * Verifies that getPublisherDetails returns a new instance on each call.
     */
    @Test
    void getPublisherDetails_ReturnsNewInstanceEachTime() {
        // Arrange
        Publisher publisher = factory.create(name, vatNumber, contact);

        // Act
        Publisher.PublisherDetails d1 = publisher.getPublisherDetails();
        Publisher.PublisherDetails d2 = publisher.getPublisherDetails();

        // Assert
        assertNotSame(d1, d2);
    }

    /**
     * Verifies that PublisherDetails stores all values correctly.
     */
    @Test
    void publisherDetails_ShouldStoreValuesCorrectly() {
        // Arrange + Act
        Publisher.PublisherDetails details =
                new Publisher.PublisherDetails(name, vatNumber, contact);

        // Assert
        assertEquals(name, details.name());
        assertEquals(vatNumber, details.vatNumber());
        assertSame(contact, details.contact());
    }

    /**
     * Verifies that two equal PublisherDetails instances produce the same hash code.
     */
    @Test
    void publisherDetails_equalObjects_shouldHaveSameHashCode() {
        // Arrange
        Publisher.PublisherDetails d1 =
                new Publisher.PublisherDetails(name, vatNumber, contact);
        Publisher.PublisherDetails d2 =
                new Publisher.PublisherDetails(name, vatNumber, contact);

        // Act + Assert
        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    /**
     * Verifies that equals and hashCode work correctly for equal PublisherDetails objects.
     */
    @Test
    void publisherDetails_EqualsAndHashCodeShouldWorkForEqualObjects() {
        // Arrange
        Contact mockContact = mock(Contact.class);
        Publisher.PublisherDetails d1 =
                new Publisher.PublisherDetails(name, vatNumber, mockContact);
        Publisher.PublisherDetails d2 =
                new Publisher.PublisherDetails(name, vatNumber, mockContact);

        // Act + Assert
        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    /**
     * Verifies that equals detects inequality between PublisherDetails with different names.
     */
    @Test
    void publisherDetails_EqualsAndHashCodeShouldDetectDifferentObjects() {
        // Arrange
        Contact mockContact = mock(Contact.class);
        Publisher.PublisherDetails d1 =
                new Publisher.PublisherDetails(name, vatNumber, mockContact);
        Publisher.PublisherDetails d2 =
                new Publisher.PublisherDetails("Different", vatNumber, mockContact);

        // Act + Assert
        assertNotEquals(d1, d2);
    }
}