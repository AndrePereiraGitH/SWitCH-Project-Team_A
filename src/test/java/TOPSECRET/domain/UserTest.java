package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testEmptyConstructor() {
        // Arrange

        // Act
        User user = new User();

        // Assert
        assertNotNull(user);
    }

    @Test
    void testConstructorWithValidArguments() throws InstantiationException {
        // Arrange
        Location location = new Location("Main Street", "12345");
        Contact contact = new Contact("987654321", "351", "email@test.com", location);

        // Act & Assert
        assertDoesNotThrow(() ->
                new User("ValidName", contact, "Admin")
        );
    }

    @Test
    void testConstructorWithUsernameNullShouldThrowException() throws InstantiationException {
        // Arrange
        Location location = new Location("Main Street", "12345");
        Contact contact = new Contact("987654321", "351", "email@test.com", location);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new User(null, contact, "Admin")
        );
    }

    @Test
    void testConstructorWithUsernameTooShortShouldThrowException() throws InstantiationException {
        // Arrange
        Location location = new Location("Main Street", "12345");
        Contact contact = new Contact("987654321", "351", "email@test.com", location);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new User("test", contact, "Admin")
        );
    }

    // Test getUserName
    @Test
    void testShouldReturnProvidedUsername() throws InstantiationException {
        // Arrange
        Location location = new Location("Main Street", "12345");
        Contact contact = new Contact("987654321", "351", "email@test.com", location);
        User user = new User("userNameTest", contact, "Admin");

        // Act
        String result = user.getUserName();

        // Assert
        assertEquals("userNameTest", result);
    }

    // Test getUserInfo
    @Test
    void testShouldReturnFormattedString() throws InstantiationException {
        // Arrange
        Location location = new Location("Main Street", "12345");
        Contact contact = new Contact("987654321", "351", "email@test.com", location);
        User user = new User("userNameTest", contact, "Admin");

        // Act
        String result = user.getUserInfo();

        // Assert
        assertTrue(result.contains("User: userNameTest"));
        assertTrue(result.contains("Contact: "));
        assertTrue(result.contains("Profile: Admin"));
    }
}