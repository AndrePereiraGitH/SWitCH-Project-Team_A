package TOPSECRET.domain.Publisher;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.PublisherID;
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
 *     <li>Validation of null and blank name and VAT number</li>
 *     <li>Getter behaviour</li>
 *     <li>Identity via {@link PublisherID}</li>
 *     <li>Equality and hash code based on {@link PublisherID}</li>
 *     <li>Business identity via sameAs</li>
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

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    @Test
    void constructorShouldCreatePublisherWhenDataIsValid() {
        // Arrange + Act
        Publisher result = factory.create(name, vatNumber, contact);

        // Assert
        assertNotNull(result);
    }

    @Test
    void constructorShouldThrowAnExceptionWhenNameIsBlank() {
        // Arrange
        String blankName = "";

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(blankName, vatNumber, contact));
    }

    @Test
    void constructorShouldThrowAnExceptionWhenNameIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(null, vatNumber, contact));
    }

    @Test
    void constructorShouldThrowAnExceptionWhenVatNumberIsBlank() {
        // Arrange
        String blankVatNumber = "";

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(name, blankVatNumber, contact));
    }

    @Test
    void constructorShouldThrowAnExceptionWhenVatNumberIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(name, null, contact));
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    @Test
    void getName_shouldReturnNameProvidedAtConstruction() {
        // Arrange
        Publisher publisher = factory.create(name, vatNumber, contact);

        // Act
        String result = publisher.getName();

        // Assert
        assertEquals(name, result);
    }

    @Test
    void getVatNumber_shouldReturnVatNumberProvidedAtConstruction() {
        // Arrange
        Publisher publisher = factory.create(name, vatNumber, contact);

        // Act
        String result = publisher.getVatNumber();

        // Assert
        assertEquals(vatNumber, result);
    }

    @Test
    void getContact_shouldReturnContactProvidedAtConstruction() {
        // Arrange
        Publisher publisher = factory.create(name, vatNumber, contact);

        // Act
        Contact result = publisher.getContact();

        // Assert
        assertEquals(contact, result);
    }

    // -------------------------------------------------------------------------
    // id()
    // -------------------------------------------------------------------------

    @Test
    void id_shouldReturnPublisherIDBasedOnVatNumber() {
        // Arrange
        Publisher publisher = factory.create(name, vatNumber, contact);

        // Act
        PublisherID result = publisher.id();

        // Assert
        assertEquals(new PublisherID(vatNumber), result);
    }

    @Test
    void id_shouldNeverReturnNull() {
        // Arrange
        Publisher publisher = factory.create(name, vatNumber, contact);

        // Act
        PublisherID result = publisher.id();

        // Assert
        assertNotNull(result);
    }

    // -------------------------------------------------------------------------
    // equals() and hashCode()
    // -------------------------------------------------------------------------

    @Test
    void equals_sameInstance_returnsTrue() {
        // Arrange
        Publisher publisher = factory.create(name, vatNumber, contact);

        // Act + Assert
        assertEquals(publisher, publisher);
    }

    @Test
    void equals_sameVatNumber_returnsTrue() {
        // Arrange
        Publisher p1 = factory.create(name, "123456", contact);
        Publisher p2 = factory.create("Other Name", "123456", contact);

        // Act + Assert
        assertEquals(p1, p2);
    }

    @Test
    void equals_differentVatNumber_returnsFalse() {
        // Arrange
        Publisher p1 = factory.create(name, "123456", contact);
        Publisher p2 = factory.create(name, "789012", contact);

        // Act + Assert
        assertNotEquals(p1, p2);
    }

    @Test
    void equals_nullObject_returnsFalse() {
        // Arrange
        Publisher publisher = factory.create(name, "123456", contact);

        // Act + Assert
        assertFalse(publisher.equals(null));
    }

    @Test
    void equals_differentClass_returnsFalse() {
        // Arrange
        Publisher publisher = factory.create(name, "123456", contact);

        // Act + Assert
        assertFalse(publisher.equals("string"));
    }


    @Test
    void hashCode_sameVatNumber_sameHashCode() {
        // Arrange
        Publisher p1 = factory.create(name, "123456", contact);
        Publisher p2 = factory.create("Other Name", "123456", contact);

        // Act + Assert
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void hashCode_differentVatNumber_differentHashCode() {
        // Arrange
        Publisher p1 = factory.create(name, "123456", contact);
        Publisher p2 = factory.create(name, "789012", contact);

        // Act + Assert
        assertNotEquals(p1.hashCode(), p2.hashCode());
    }

    // -------------------------------------------------------------------------
    // sameAs()
    // -------------------------------------------------------------------------

    @Test
    void sameAs_sameName_andSameContact_returnsTrue() {
        // Arrange
        Publisher p1 = factory.create(name, "123456", contact);
        Publisher p2 = factory.create(name, "789012", contact);

        // Act
        boolean result = p1.sameAs(p2);

        // Assert
        assertTrue(result);
    }

    @Test
    void sameAs_differentName_returnsFalse() {
        // Arrange
        Publisher p1 = factory.create("Publisher A", vatNumber, contact);
        Publisher p2 = factory.create("Publisher B", vatNumber, contact);

        // Act
        boolean result = p1.sameAs(p2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAs_differentContact_returnsFalse() {
        // Arrange
        Contact contact2 = mock(Contact.class);
        Publisher p1 = factory.create(name, "123456", contact);
        Publisher p2 = factory.create(name, "789012", contact2);

        // Act
        boolean result = p1.sameAs(p2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAs_null_returnsFalse() {
        // Arrange
        Publisher publisher = factory.create(name, vatNumber, contact);

        // Act
        boolean result = publisher.sameAs(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAs_differentClass_returnsFalse() {
        // Arrange
        Publisher publisher = factory.create(name, vatNumber, contact);

        // Act
        boolean result = publisher.sameAs("string");

        // Assert
        assertFalse(result);
    }

    // -------------------------------------------------------------------------
    // getPublisherDetails()
    // -------------------------------------------------------------------------

    @Test
    void getPublisherDetails_ShouldNeverReturnNull() {
        // Arrange
        Publisher publisher = factory.create(name, vatNumber, contact);

        // Act
        Publisher.PublisherDetails details = publisher.getPublisherDetails();

        // Assert
        assertNotNull(details);
    }

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

    // -------------------------------------------------------------------------
    // PublisherDetails record
    // -------------------------------------------------------------------------

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