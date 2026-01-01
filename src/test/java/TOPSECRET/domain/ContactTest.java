package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContactTest {

    @Test
    void testEmptyConstructor() {
        // Arrange not needed

        // Act
        Contact contact = new Contact();

        // Assert
        assertNotNull(contact);
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