package TOPSECRET.domain.ValueObjects;

import TOPSECRET.domain.City.City;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ContactTest {

    //Double for City class
    private final CityID anyCity = mock(CityID.class);

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

    @Test
    void testEmptyConstructor() {

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
                anyCity
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
                anyCity
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
                anyCity
        );
        Contact contact = new Contact("987654321", "351", email, address);

        // Act
        String result = contact.getPhoneNum();

        // Assert
        assertEquals("987654321", result);
    }

    @Test
    void shouldReturnFalseWhenPhoneNumberIsNullInOneObject() {

        // ARRANGE
        Contact contact1 = createContact(null, "351", null, null);
        Contact contact2 = createContact("912345678", "351", null, null);

        // ACT
        boolean result = contact1.equals(contact2);

        // ASSERT
        assertFalse(result);
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
                anyCity
        );
        Contact contact = new Contact("987654321", "351", email, address);

        // Act
        String result = contact.getExtension();

        // Assert
        assertEquals("351", result);
    }

    @Test
    void shouldReturnFalseWhenExtensionIsNullInOneObject() {

        // ARRANGE
        Contact contact1 = new Contact();
        Contact contact2 = createContact("912345678", "351", null, null);

        // ACT
        boolean result = contact1.equals(contact2);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenOneExtensionIsNullAndOtherIsNot() {

        // ARRANGE
        Contact contact1 = new Contact();
        Contact contact2 = createContact("912345678", "351", null, null);

        // ACT
        boolean result = contact1.equals(contact2);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenExtensionIsNullInFirstContactButNotInSecond() {

        // Arrange
        Contact contact1 = new Contact();
        Contact contact2 = new Contact(null, "351", null, null);

        // Act
        boolean result = contact1.equals(contact2);

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenOneExtensionIsNullViaEmptyConstructor() {

        // Arrange
        Contact contact1 = new Contact();
        Contact contact2 = createContact("912345678", "351", null, null);

        // Act
        boolean result = contact1.equals(contact2);

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenBothExtensionsAreNullViaEmptyConstructor() {

        // Arrange
        Contact contact1 = new Contact();
        Contact contact2 = new Contact();

        // Act
        boolean result = contact1.equals(contact2);

        // Assert
        assertTrue(result);
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
                anyCity
        );
        Contact contact = new Contact("987654321", "351", email, address);

        // Act
        Email result = contact.getEmail();

        // Assert
        assertEquals(email, result);
    }

    @Test
    void shouldReturnFalseWhenEmailIsNullInOneObject() {

        // ARRANGE
        Email email = new Email("user", "mail.com");

        Contact contact1 = createContact("912345678", "351", null, null);
        Contact contact2 = createContact("912345678", "351", email, null);

        // ACT
        boolean result = contact1.equals(contact2);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenEmailIsDifferent() {

        // ARRANGE
        Email email1 = new Email("user", "mail.com");
        Email email2 = new Email("other", "mail.com");

        Contact contact1 = createContact("912345678", "351", email1, null);
        Contact contact2 = createContact("912345678", "351", email2, null);

        // ACT
        boolean result = contact1.equals(contact2);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenOneEmailIsNullAndOtherIsNot() {

        // ARRANGE
        Email email = new Email("user", "mail.com");

        Contact contact1 = createContact("912345678", "351", email, null);
        Contact contact2 = createContact("912345678", "351", null, null);

        // ACT
        boolean result = contact1.equals(contact2);

        // ASSERT
        assertFalse(result);
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
                anyCity
        );
        Contact contact = new Contact("987654321", "351", email, address);

        // Act
        Address result = contact.getAddress();

        // Assert
        assertEquals(address, result);
    }

    @Test
    void shouldReturnFalseWhenAddressIsDifferent() {

        // ARRANGE
        Address address1 = new Address(
                "Street A",
                "1",
                "1000",
                anyCity
        );

        Address address2 = new Address(
                "Street B",
                "2",
                "2000",
                anyCity
        );

        Contact contact1 = createContact("912345678", "351", null, address1);
        Contact contact2 = createContact("912345678", "351", null, address2);

        // ACT
        boolean result = contact1.equals(contact2);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenOneAddressIsNullAndOtherIsNot() {

        // ARRANGE
        Address address = new Address(
                "Street A",
                "1",
                "1000",
                anyCity
        );

        Contact contact1 = createContact("912345678", "351", null, address);
        Contact contact2 = createContact("912345678", "351", null, null);

        // ACT
        boolean result = contact1.equals(contact2);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenAddressIsNullInOneObject() {

        // ARRANGE
        Address address = new Address(
                "Street",
                "10",
                "12345",
                anyCity
        );

        Contact contact1 = createContact("912345678", "351", null, null);
        Contact contact2 = createContact("912345678", "351", null, address);

        // ACT
        boolean result = contact1.equals(contact2);

        // ASSERT
        assertFalse(result);
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
                anyCity
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
                anyCity
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

    @Test
    void shouldReturnTrueWhenComparingSameInstance() {

        // ARRANGE
        Contact contact = createContact("912345678", "351", null, null);

        // ACT
        boolean result = contact.equals(contact);

        // ASSERT
        assertTrue(result);
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

    @Test
    void hashCodeShouldBeDifferentWhenEmailIsDifferent() {

        // ARRANGE
        Email email1 = new Email("user", "mail.com");
        Email email2 = new Email("other", "mail.com");

        Contact contact1 = createContact("912345678", "351", email1, null);
        Contact contact2 = createContact("912345678", "351", email2, null);

        // ACT
        int hash1 = contact1.hashCode();
        int hash2 = contact2.hashCode();

        // ASSERT
        assertNotEquals(hash1, hash2);
    }

    @Test
    void hashCodeShouldBeDifferentWhenExtensionDiffers() {

        // ARRANGE
        Contact contact1 = createContact("912345678", "351", null, null);
        Contact contact2 = createContact("912345678", "44", null, null);

        // ACT
        int hash1 = contact1.hashCode();
        int hash2 = contact2.hashCode();

        // ASSERT
        assertNotEquals(hash1, hash2);
    }

    @Test
    void hashCodeShouldBeDifferentWhenEmailDiffers() {

        // ARRANGE
        Email email1 = new Email("user", "mail.com");
        Email email2 = new Email("other", "mail.com");

        Contact contact1 = createContact("912345678", "351", email1, null);
        Contact contact2 = createContact("912345678", "351", email2, null);

        // ACT
        int hash1 = contact1.hashCode();
        int hash2 = contact2.hashCode();

        // ASSERT
        assertNotEquals(hash1, hash2);
    }

    @Test
    void shouldReturnTrueWhenAddressIsEqualAndNotNull() {

        // ARRANGE
        Address address = new Address(
                "Main Street",
                "10",
                "12345",
                anyCity
        );

        Contact contact1 = createContact("912345678", "351", null, address);
        Contact contact2 = createContact("912345678", "351", null, address);

        // ACT
        boolean result = contact1.equals(contact2);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void hashCodeShouldMatchExpectedFormula() {

        // ARRANGE
        Email email = new Email("test.user", "example.com");
        Address address = new Address(
                "Main Street",
                "10",
                "12345",
                anyCity
        );

        Contact contact = createContact("987654321", "351", email, address);

        int expected = 17;
        expected = 31 * expected + "987654321".hashCode();
        expected = 31 * expected + "351".hashCode();
        expected = 31 * expected + email.hashCode();
        expected = 31 * expected + address.hashCode();

        // ACT
        int result = contact.hashCode();

        // ASSERT
        assertEquals(expected, result);
    }

    @Test
    void hashCodeShouldMatchExpectedFormulaWhenEmailAndAddressAreNull() {

        // ARRANGE
        Contact contact = createContact("912345678", "351", null, null);

        int expected = 17;
        expected = 31 * expected + "912345678".hashCode();
        expected = 31 * expected + "351".hashCode();
        expected = 31 * expected + 0;
        expected = 31 * expected + 0;

        // ACT
        int result = contact.hashCode();

        // ASSERT
        assertEquals(expected, result);
    }

}