package TOPSECRET.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PublisherTest {

    // ==============================================
    // ================ Set Up ======================
    // ==============================================

    String name = "Asas";
    String vatNumber = "123456";

    Contact contact;   // mocked
    Publisher publisher;

    @BeforeEach
    void setUp() {
        contact = mock(Contact.class);
        publisher = new Publisher(name, vatNumber, contact);
    }

    // ==============================================
    // ================ Tests =======================
    // ==============================================

    // Tests for getPublisherDetails -----------------------------------------------------------------------------------

    @Test
    void getPublisherDetails_ShouldNeverReturnNull() {
        // Act
        Publisher.PublisherDetails details = publisher.getPublisherDetails();

        // Assert
        assertNotNull(details);
    }

    @Test
    void getPublisherDetails_ShouldReturnAllFieldsCorrectly() {
        // Act
        Publisher.PublisherDetails details = publisher.getPublisherDetails();

        // Assert
        assertEquals(name, details.name());
        assertEquals(vatNumber, details.vatNumber());
        assertEquals(contact, details.contact());
    }

    @Test
    void getPublisherDetails_ReturnsNewInstanceEachTime() {
        // Act
        Publisher.PublisherDetails d1 = publisher.getPublisherDetails();
        Publisher.PublisherDetails d2 = publisher.getPublisherDetails();

        // Assert
        assertNotSame(d1, d2);
    }

    @Test
    void getPublisherDetails_ShouldNotModifyPublisherState() {
        // Act
        publisher.getPublisherDetails();

        // Assert
        assertEquals(name, publisher.getName());
        assertEquals(vatNumber, publisher.getVatNumber());
        assertEquals(contact, publisher.getContact());
    }

    // Tests for PublisherDetails record -------------------------------------------------------------------------------

    @Test
    void publisherDetails_ShouldStoreValuesCorrectly() {
        // Act
        Publisher.PublisherDetails details =
                new Publisher.PublisherDetails(name, vatNumber, contact);

        // Assert
        assertEquals(name, details.name());
        assertEquals(vatNumber, details.vatNumber());
        assertEquals(contact, details.contact());
    }

    @Test
    void publisherDetails_EqualsAndHashCodeShouldWorkForEqualObjects() {
        // Arrange
        Contact mockContact = mock(Contact.class);

        // Act
        Publisher.PublisherDetails d1 =
                new Publisher.PublisherDetails(name, vatNumber, mockContact);
        Publisher.PublisherDetails d2 =
                new Publisher.PublisherDetails(name, vatNumber, mockContact);

        // Assert
        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    void publisherDetails_EqualsAndHashCodeShouldDetectDifferentObjects() {
        // Arrange
        Contact mockContact = mock(Contact.class);

        // Act
        Publisher.PublisherDetails d1 =
                new Publisher.PublisherDetails(name, vatNumber, mockContact);
        Publisher.PublisherDetails d2 =
                new Publisher.PublisherDetails("Different", vatNumber, mockContact);

        // Assert
        assertNotEquals(d1, d2);
    }

    @Test
    void publisherDetails_ShouldBeImmutableReferenceHolder() {
        // Arrange
        Contact mockContact = mock(Contact.class);

        // Act
        Publisher.PublisherDetails details =
                new Publisher.PublisherDetails(name, vatNumber, mockContact);

        // Assert
        assertSame(mockContact, details.contact());
    }
}