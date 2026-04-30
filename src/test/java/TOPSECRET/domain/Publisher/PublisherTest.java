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
    private final Contact contactDouble = mock(Contact.class);
    private PublisherFactory factory;

    @BeforeEach
    void setup() {
        factory = new PublisherFactory();
    }

    // -------------------------------------------------------------------------
    // Constructor (creation)
    // -------------------------------------------------------------------------

    @Test
    void constructorShouldCreatePublisherWhenDataIsValid() {
        // Arrange + Act
        Publisher result = factory.create(name, vatNumber, contactDouble);

        // Assert
        assertNotNull(result);
    }

    @Test
    void constructorShouldThrowAnExceptionWhenNameIsBlank() {
        // Arrange
        String blankName = "";

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(blankName, vatNumber, contactDouble));
    }

    @Test
    void constructorShouldThrowAnExceptionWhenNameIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(null, vatNumber, contactDouble));
    }

    @Test
    void constructorShouldThrowAnExceptionWhenVatNumberIsBlank() {
        // Arrange
        String blankVatNumber = "";

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(name, blankVatNumber, contactDouble));
    }

    @Test
    void constructorShouldThrowAnExceptionWhenVatNumberIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(name, null, contactDouble));
    }

    // -------------------------------------------------------------------------
    // Constructor (reconstruction)
    // -------------------------------------------------------------------------

    @Test
    void reconstructionConstructor_shouldCreatePublisher_whenAllFieldsAreValid() {
        // Arrange
        PublisherID idDouble = mock(PublisherID.class);

        // Act
        Publisher result = new Publisher(idDouble, name, vatNumber, contactDouble);

        // Assert
        assertNotNull(result);
        assertSame(idDouble, result.id());
        assertEquals(name, result.getName());
    }

    @Test
    void reconstructionConstructor_shouldThrowException_whenIdIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Publisher(null, name, vatNumber, contactDouble));
    }

    @Test
    void reconstructionConstructor_shouldThrowException_whenNameIsNull() {
        // Arrange
        PublisherID idDouble = mock(PublisherID.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Publisher(idDouble, null, vatNumber, contactDouble));
    }

    @Test
    void reconstructionConstructor_shouldThrowException_whenNameIsBlank() {
        // Arrange
        PublisherID idDouble = mock(PublisherID.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Publisher(idDouble, "", vatNumber, contactDouble));
    }

    @Test
    void reconstructionConstructor_shouldThrowException_whenContactIsNull() {
        // Arrange
        PublisherID idDouble = mock(PublisherID.class);

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> new Publisher(idDouble, name, vatNumber, null));
    }

    @Test
    void reconstructionConstructor_shouldThrowException_whenVatNumberIsNull() {
        // Arrange
        PublisherID idDouble = mock(PublisherID.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Publisher(idDouble, name, null, contactDouble));
    }

    @Test
    void reconstructionConstructor_shouldThrowException_whenVatNumberIsBlank() {
        // Arrange
        PublisherID idDouble = mock(PublisherID.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Publisher(idDouble, name, "", contactDouble));
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    @Test
    void getName_shouldReturnNameProvidedAtConstruction() {
        // Arrange
        Publisher publisher = factory.create(name, vatNumber, contactDouble);

        // Act
        String result = publisher.getName();

        // Assert
        assertEquals(name, result);
    }

    @Test
    void getVatNumber_shouldReturnVatNumberProvidedAtConstruction() {
        // Arrange
        Publisher publisher = factory.create(name, vatNumber, contactDouble);

        // Act
        String result = publisher.getVatNumber();

        // Assert
        assertEquals(vatNumber, result);
    }

    @Test
    void getContact_shouldReturnContactProvidedAtConstruction() {
        // Arrange
        Publisher publisher = factory.create(name, vatNumber, contactDouble);

        // Act
        Contact result = publisher.getContact();

        // Assert
        assertEquals(contactDouble, result);
    }

    // -------------------------------------------------------------------------
    // id()
    // -------------------------------------------------------------------------

    @Test
    void id_shouldReturnPublisherIDBasedOnVatNumber() {
        // Arrange
        Publisher publisher = factory.create(name, vatNumber, contactDouble);

        // Act
        PublisherID result = publisher.id();

        // Assert
        assertEquals(new PublisherID(vatNumber), result);
    }

    @Test
    void id_shouldNeverReturnNull() {
        // Arrange
        Publisher publisher = factory.create(name, vatNumber, contactDouble);

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
        Publisher publisher = factory.create(name, vatNumber, contactDouble);

        // Act + Assert
        assertEquals(publisher, publisher);
    }

    @Test
    void equals_sameVatNumber_returnsTrue() {
        // Arrange
        Publisher p1 = factory.create(name, "123456", contactDouble);
        Publisher p2 = factory.create("Other Name", "123456", contactDouble);

        // Act + Assert
        assertEquals(p1, p2);
    }

    @Test
    void equals_differentVatNumber_returnsFalse() {
        // Arrange
        Publisher p1 = factory.create(name, "123456", contactDouble);
        Publisher p2 = factory.create(name, "789012", contactDouble);

        // Act + Assert
        assertNotEquals(p1, p2);
    }

    @Test
    void equals_nullObject_returnsFalse() {
        // Arrange
        Publisher publisher = factory.create(name, "123456", contactDouble);

        // Act + Assert
        assertFalse(publisher.equals(null));
    }

    @Test
    void equals_differentClass_returnsFalse() {
        // Arrange
        Publisher publisher = factory.create(name, "123456", contactDouble);

        // Act + Assert
        assertFalse(publisher.equals("string"));
    }

    @Test
    void hashCode_sameVatNumber_sameHashCode() {
        // Arrange
        Publisher p1 = factory.create(name, "123456", contactDouble);
        Publisher p2 = factory.create("Other Name", "123456", contactDouble);

        // Act + Assert
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void hashCode_differentVatNumber_differentHashCode() {
        // Arrange
        Publisher p1 = factory.create(name, "123456", contactDouble);
        Publisher p2 = factory.create(name, "789012", contactDouble);

        // Act + Assert
        assertNotEquals(p1.hashCode(), p2.hashCode());
    }

    // -------------------------------------------------------------------------
    // sameAs()
    // -------------------------------------------------------------------------

    @Test
    void sameAs_sameNameAndContact_returnsTrue() {
        // Arrange
        Publisher p1 = factory.create(name, "123456", contactDouble);
        Publisher p2 = factory.create(name, "789012", contactDouble);

        // Act
        boolean result = p1.sameAs(p2);

        // Assert
        assertTrue(result);
    }

    @Test
    void sameAs_differentName_returnsFalse() {
        // Arrange
        Publisher p1 = factory.create("Publisher A", vatNumber, contactDouble);
        Publisher p2 = factory.create("Publisher B", vatNumber, contactDouble);

        // Act
        boolean result = p1.sameAs(p2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAs_differentContact_returnsFalse() {
        // Arrange
        Contact contactDouble2 = mock(Contact.class);
        Publisher p1 = factory.create(name, "123456", contactDouble);
        Publisher p2 = factory.create(name, "789012", contactDouble2);

        // Act
        boolean result = p1.sameAs(p2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAs_null_returnsFalse() {
        // Arrange
        Publisher publisher = factory.create(name, vatNumber, contactDouble);

        // Act
        boolean result = publisher.sameAs(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAs_differentClass_returnsFalse() {
        // Arrange
        Publisher publisher = factory.create(name, vatNumber, contactDouble);

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
        Publisher publisher = factory.create(name, vatNumber, contactDouble);

        // Act
        Publisher.PublisherDetails details = publisher.getPublisherDetails();

        // Assert
        assertNotNull(details);
    }

    @Test
    void getPublisherDetails_ShouldReturnAllFieldsCorrectly() {
        // Arrange
        Publisher publisher = factory.create(name, vatNumber, contactDouble);

        // Act
        Publisher.PublisherDetails details = publisher.getPublisherDetails();

        // Assert
        assertNotNull(details);
        assertEquals(name, details.name());
        assertEquals(vatNumber, details.vatNumber());
        assertSame(contactDouble, details.contact());
    }

    @Test
    void getPublisherDetails_ReturnsNewInstanceEachTime() {
        // Arrange
        Publisher publisher = factory.create(name, vatNumber, contactDouble);

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
                new Publisher.PublisherDetails(name, vatNumber, contactDouble);

        // Assert
        assertEquals(name, details.name());
        assertEquals(vatNumber, details.vatNumber());
        assertSame(contactDouble, details.contact());
    }

    @Test
    void publisherDetails_equalObjects_shouldHaveSameHashCode() {
        // Arrange
        Publisher.PublisherDetails d1 =
                new Publisher.PublisherDetails(name, vatNumber, contactDouble);
        Publisher.PublisherDetails d2 =
                new Publisher.PublisherDetails(name, vatNumber, contactDouble);

        // Act + Assert
        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    void publisherDetails_EqualsAndHashCodeShouldWorkForEqualObjects() {
        // Arrange
        Contact contactDouble = mock(Contact.class);
        Publisher.PublisherDetails d1 =
                new Publisher.PublisherDetails(name, vatNumber, contactDouble);
        Publisher.PublisherDetails d2 =
                new Publisher.PublisherDetails(name, vatNumber, contactDouble);

        // Act + Assert
        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    void publisherDetails_EqualsAndHashCodeShouldDetectDifferentObjects() {
        // Arrange
        Contact contactDouble = mock(Contact.class);
        Publisher.PublisherDetails d1 =
                new Publisher.PublisherDetails(name, vatNumber, contactDouble);
        Publisher.PublisherDetails d2 =
                new Publisher.PublisherDetails("Different", vatNumber, contactDouble);

        // Act + Assert
        assertNotEquals(d1, d2);
    }
}
