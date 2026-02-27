package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Countries;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContactTest {

    /**
     * Helper method to create a valid Contact.
     */
    private Contact createContact(
            String phone,
            String extension,
            Email email,
            Address address) {

        return new Contact(phone, extension, email, address);
    }

    @Test void testEmptyConstructor() {

        new Contact();
    }

    @Test
    void testConstructorWithValidArguments() {
        // Arrange
        Email email = new Email("test.user", "example.com");
        Address address = new Address(
                "Main Street",
                "10",
                "12345",
                new Country("PORTUGAL")
        );

        // Act & Assert
        assertDoesNotThrow(() ->
                new Contact("987654321", "351", email, address)
        );
    }

    @Test
    void testConstructorWithNullExtensionShouldThrowException() {
        // Arrange
        Email email = new Email("test.user", "example.com");
        Address address = new Address(
                "Main Street",
                "10",
                "12345",
                new Country("PORTUGAL")
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Contact("987654321", null, email, address)
        );
    }

    // Test getPhoneNum
    @Test
    void testShouldReturnPhoneNumber() {
        // Arrange
        Email email = new Email("test.user", "example.com");
        Address address = new Address(
                "Main Street",
                "10",
                "12345",
                new Country("PORTUGAL")
        );
        Contact contact = new Contact("987654321", "351", email, address);

        // Act
        String result = contact.getPhoneNum();

        // Assert
        assertEquals("987654321", result);
    }

    // Test getExtension
    @Test
    void testShouldReturnExtension() {
        // Arrange
        Email email = new Email("test.user", "example.com");
        Address address = new Address(
                "Main Street",
                "10",
                "12345",
                new Country("PORTUGAL")
        );
        Contact contact = new Contact("987654321", "351", email, address);

        // Act
        String result = contact.getExtension();

        // Assert
        assertEquals("351", result);
    }

    // Test getEmail
    @Test
    void testGetEmail_ShouldReturnEmail() {
        // Arrange
        Email email = new Email("test.user", "example.com");
        Address address = new Address(
                "Main Street",
                "10",
                "12345",
                new Country("PORTUGAL")
        );
        Contact contact = new Contact("987654321", "351", email, address);

        // Act
        Email result = contact.getEmail();

        // Assert
        assertEquals(email, result);
    }

    // Test getAddress
    @Test
    void testShouldReturnAddress() {
        // Arrange
        Email email = new Email("test.user", "example.com");
        Address address = new Address(
                "Main Street",
                "10",
                "12345",
                new Country("PORTUGAL")
        );
        Contact contact = new Contact("987654321", "351", email, address);

        // Act
        Address result = contact.getAddress();

        // Assert
        assertEquals(address, result);
    }

    // Test getFullPhoneNumber
    @Test
    void testShouldReturnCorrectFormat() {
        // Arrange
        Email email = new Email("test.user", "example.com");
        Address address = new Address(
                "Main Street",
                "10",
                "12345",
                new Country("PORTUGAL")
        );
        Contact contact = new Contact("987654321", "351", email, address);

        // Act
        String result = contact.getFullPhone();

        // Assert
        assertEquals("351 987654321", result);
    }

    @Test
    void testGetFullContactInfo_ShouldReturnExpectedOutput() {
        // Arrange
        Email email = new Email("test.user", "example.com");
        Address address = new Address(
                "Main Street",
                "10",
                "12345",
                new Country("PORTUGAL")
        );
        Contact contact = new Contact("987654321", "351", email, address);

        // Act
        String result = contact.getFullContactInfo();

        // Assert
        assertTrue(result.contains("351 987654321"));
        assertTrue(result.contains(email.toString()));
        assertTrue(result.contains(address.toString()));
    }

    /**
     * Verifies that two Contacts with same data are equal.
     */
    @Test
    void shouldReturnTrueWhenContactsHaveSameData() {

        // Arrange
        Contact contact1 = createContact("912345678", "351", null, null);
        Contact contact2 = createContact("912345678", "351", null, null);

        // Act
        boolean result = contact1.equals(contact2);

        // Assert
        assertTrue(result);
    }

    /**
     * Verifies that Contacts with different phone numbers are not equal.
     */
    @Test
    void shouldReturnFalseWhenPhoneIsDifferent() {

        Contact contact1 = createContact("111111111", "351", null, null);
        Contact contact2 = createContact("222222222", "351", null, null);

        assertFalse(contact1.equals(contact2));
    }

    /**
     * Verifies that Contacts with different extension are not equal.
     */
    @Test
    void shouldReturnFalseWhenExtensionIsDifferent() {

        Contact contact1 = createContact("912345678", "351", null, null);
        Contact contact2 = createContact("912345678", "44", null, null);

        assertFalse(contact1.equals(contact2));
    }

    /**
     * Verifies equals returns false when comparing with null.
     */
    @Test
    void shouldReturnFalseWhenComparedWithNull() {

        Contact contact = createContact("912345678", "351", null, null);

        assertFalse(contact.equals(null));
    }

    /**
     * Verifies equals returns false when comparing with different type.
     */
    @Test
    void shouldReturnFalseWhenComparedWithDifferentObjectType() {

        Contact contact = createContact("912345678", "351", null, null);

        assertFalse(contact.equals("NotAContact"));
    }

    /**
     * Verifies hashCode is equal for equal objects.
     */
    @Test
    void hashCodeShouldBeEqualWhenObjectsAreEqual() {

        Contact contact1 = createContact("912345678", "351", null, null);
        Contact contact2 = createContact("912345678", "351", null, null);

        assertEquals(contact1.hashCode(), contact2.hashCode());
    }

    /**
     * Verifies hashCode is different when objects differ.
     */
    @Test
    void hashCodeShouldBeDifferentWhenObjectsAreDifferent() {

        Contact contact1 = createContact("111111111", "351", null, null);
        Contact contact2 = createContact("222222222", "351", null, null);

        assertNotEquals(contact1.hashCode(), contact2.hashCode());
    }

    /**
     * Verifies equals works correctly when email and address are null.
     */
    @Test
    void shouldHandleNullEmailAndAddressCorrectly() {

        Contact contact1 = createContact("912345678", "351", null, null);
        Contact contact2 = createContact("912345678", "351", null, null);

        assertTrue(contact1.equals(contact2));
    }
}