package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Countries;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContactTest {

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
}