package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link Contact}.
 *
 * <p>
 * Validates the behavior of the Contact value object including:
 * </p>
 *
 * <ul>
 *     <li>Creation with valid data</li>
 *     <li>Validation of constructor arguments</li>
 *     <li>Equality and value comparison</li>
 *     <li>hashCode consistency</li>
 * </ul>
 *
 * <p>
 * Ensures that the value object enforces its invariants and behaves
 * correctly according to value-based equality semantics.
 * </p>
 *
 * <p>
 * Tests focus on validation rules, immutability, and correctness
 * of the encapsulated data.
 * </p>
 */
class ContactTest {

    //Double for City class
    private final CityId anyCity = mock(CityId.class);

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
        // ARRANGE
        Email email = new Email("test.user", "example.com");
        Address address = new Address(
                "Main Street",
                "10",
                "12345",
                anyCity
        );

        // ACT & ASSERT
        assertDoesNotThrow(() ->
                new Contact("987654321", "351", email, address)
        );
    }

    @Test
    void testConstructorWithNullExtensionShouldThrowException() {
        // ARRANGE
        Email email = new Email("test.user", "example.com");
        Address address = new Address(
                "Main Street",
                "10",
                "12345",
                anyCity
        );

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new Contact("987654321", null, email, address)
        );
    }

    // Test getPhoneNum
    @Test
    void testShouldReturnPhoneNumber() {
        // ARRANGE
        Email email = new Email("test.user", "example.com");
        Address address = new Address(
                "Main Street",
                "10",
                "12345",
                anyCity
        );
        Contact contact = new Contact("987654321", "351", email, address);

        // ACT
        String result = contact.getPhoneNum();

        // ASSERT
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
        // ARRANGE
        Email email = new Email("test.user", "example.com");
        Address address = new Address(
                "Main Street",
                "10",
                "12345",
                anyCity
        );
        Contact contact = new Contact("987654321", "351", email, address);

        // ACT
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
    void shouldReturnFalseWhenOneExtensionIsNullViaEmptyConstructor() {

        // ARRANGE
        Contact contact1 = new Contact();
        Contact contact2 = createContact("912345678", "351", null, null);

        // ACT
        boolean result = contact1.equals(contact2);

        // Assert
        assertFalse(result);
    }

    // Test getEmail
    @Test
    void testGetEmail_ShouldReturnEmail() {
        // ARRANGE
        Email email = new Email("test.user", "example.com");
        Address address = new Address(
                "Main Street",
                "10",
                "12345",
                anyCity
        );
        Contact contact = new Contact("987654321", "351", email, address);

        // ACT
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

    // Test getAddress
    @Test
    void testShouldReturnAddress() {
        // ARRANGE
        Email email = new Email("test.user", "example.com");
        Address address = new Address(
                "Main Street",
                "10",
                "12345",
                anyCity
        );
        Contact contact = new Contact("987654321", "351", email, address);

        // ACT
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
        // ARRANGE
        Email email = new Email("test.user", "example.com");
        Address address = new Address(
                "Main Street",
                "10",
                "12345",
                anyCity
        );
        Contact contact = new Contact("987654321", "351", email, address);

        // ACT
        String result = contact.getFullPhone();

        // Assert
        assertEquals("351 987654321", result);
    }

    @Test
    void testGetFullContactInfo_ShouldReturnExpectedOutput() {
        // ARRANGE
        Email email = new Email("test.user", "example.com");
        Address address = new Address(
                "Main Street",
                "10",
                "12345",
                anyCity
        );
        Contact contact = new Contact("987654321", "351", email, address);

        // ACT
        String result = contact.getFullContactInfo();

        // Assert
        assertTrue(result.contains("351 987654321"));
        assertTrue(result.contains(email.toString()));
        assertTrue(result.contains(address.toString()));
    }

    @Test
    void shouldReturnTrueWhenContactsHaveSameData() {

        // ARRANGE
        Contact contact1 = createContact("912345678", "351", null, null);
        Contact contact2 = createContact("912345678", "351", null, null);

        // ACT
        boolean result = contact1.equals(contact2);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenPhoneIsDifferent() {

        // ARRANGE
        Contact contact1 = createContact("111111111", "351", null, null);
        Contact contact2 = createContact("222222222", "351", null, null);

        // ACT + ASSERT
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

    @Test
    void shouldReturnFalseWhenExtensionIsDifferent() {

        // ARRANGE
        Contact contact1 = createContact("912345678", "351", null, null);
        Contact contact2 = createContact("912345678", "44", null, null);

        // ACT + ASSERT
        assertFalse(contact1.equals(contact2));
    }

    @Test
    void shouldReturnFalseWhenComparedWithNull() {

        // ARRANGE
        Contact contact = createContact("912345678", "351", null, null);

        // ACT + ASSERT
        assertFalse(contact.equals(null));
    }

    @Test
    void shouldReturnFalseWhenComparedWithDifferentObjectType() {

        // ARRANGE
        Contact contact = createContact("912345678", "351", null, null);

        // ACT + ASSERT
        assertFalse(contact.equals("NotAContact"));
    }

    @Test
    void hashCodeShouldBeEqualWhenObjectsAreEqual() {

        // ARRANGE
        Contact contact1 = createContact("912345678", "351", null, null);
        Contact contact2 = createContact("912345678", "351", null, null);

        // ACT + ASSERT
        assertEquals(contact1.hashCode(), contact2.hashCode());
    }

    @Test
    void hashCodeShouldBeDifferentWhenObjectsAreDifferent() {

        // ARRANGE
        Contact contact1 = createContact("111111111", "351", null, null);
        Contact contact2 = createContact("222222222", "351", null, null);

        // ACT + ASSERT
        assertNotEquals(contact1.hashCode(), contact2.hashCode());
    }

    @Test
    void shouldHandleNullEmailAndAddressCorrectly() {

        // ARRANGE
        Contact contact1 = createContact("912345678", "351", null, null);
        Contact contact2 = createContact("912345678", "351", null, null);

        // ACT + ASSERT
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
    void hashCodeShouldBeConsistent() {

        // ARRANGE
        Contact contact = createContact("912345678", "351", null, null);

        // ACT
        int h1 = contact.hashCode();
        int h2 = contact.hashCode();

        // ASSERT
        assertEquals(h1, h2);
    }

    @Test
    void equalObjectsMustHaveSameHashCode() {

        // ARRANGE
        Contact c1 = createContact("912345678", "351", null, null);
        Contact c2 = createContact("912345678", "351", null, null);

        // ACT + ASSERT
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void equalsShouldBeSymmetric() {

        // ARRANGE
        Contact c1 = createContact("912345678", "351", null, null);
        Contact c2 = createContact("912345678", "351", null, null);

        // ACT + ASSERT
        assertTrue(c1.equals(c2));
        assertTrue(c2.equals(c1));
    }
}
