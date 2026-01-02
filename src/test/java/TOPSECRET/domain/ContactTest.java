package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContactTest {

    @Test
    void testEmptyConstructor() {

        new Contact();
    }

    @Test
    void testConstructorWithValidArguments () throws InstantiationException {
        // Arrange
        Location location = new Location("Main Street", "12345");

        // Act & Assert
        assertDoesNotThrow(() ->
                new Contact("987654321", "351", "email@test.com", location)
        );
    }

    @Test
    void testConstructorWithNullExtensionShouldThrowException() throws InstantiationException {
        // Arrange
        Location location = new Location("Main Street", "12345");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Contact("987654321", null, "email@test.com", location)
        );
    }

    // Test getPhoneNum
    @Test
    void testShouldReturnPhoneNumber() throws InstantiationException {
        // Arrange
        Location location = new Location("Main Street", "12345");
        Contact contact = new Contact("987654321", "351", "email@test.com", location);

        // Act
        String result = contact.getPhoneNum();

        // Assert
        assertEquals("987654321", result);
    }

    // Test getExtension
    @Test
    void testShouldReturnExtension() throws InstantiationException {
        // Arrange
        Location location = new Location("Main Street", "12345");
        Contact contact = new Contact("987654321", "351", "email@test.com", location);

        // Act
        String result = contact.getExtension();

        // Assert
        assertEquals("351", result);
    }

    // Test getEmail
    @Test
    void testGetEmail_ShouldReturnEmail() throws InstantiationException {
        // Arrange
        Location location = new Location("Main Street", "12345");
        Contact contact = new Contact("987654321", "351", "email@test.com", location);

        // Act
        String result = contact.getEmail();

        // Assert
        assertEquals("email@test.com", result);
    }

    // Test getAddress
    // TODO Change to Address type
    @Test
    void testShouldReturnAddress () throws InstantiationException {
        // Arrange
        Location location = new Location("Main Street", "12345");
        Contact contact = new Contact("987654321", "351", "email@test.com", location);

        // Act
        Location result = contact.getAddress();

        // Assert
        assertEquals(location, result);
    }


    // Test getFullPhoneNumber
    @Test
    void testShouldReturnCorrectFormat() throws InstantiationException {
        // Arrange
        Location location = new Location("Main Street", "12345");
        Contact contact = new Contact("987654321", "351", "email@test.com", location);

        // Act
        String result = contact.getFullPhone();

        // Assert
        assertEquals("351 987654321", result);
    }

    @Test
    void testGetFullContactInfo_ShouldReturnExpectedOutput() throws InstantiationException {
        // Arrange
        Location location = new Location("Main Street", "12345");
        Contact contact = new Contact("987654321", "351", "email@test.com", location);

        // Act
        String result = contact.getFullContactInfo();

        // Assert
        assertTrue(result.contains("351 987654321"));
        assertTrue(result.contains("email@test.com"));
        assertTrue(result.contains("address:"));
    }

}