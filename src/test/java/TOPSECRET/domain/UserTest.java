package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testEmptyConstructor() {

       new User();
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
    void testConstructorUsernameWithFiveCharacters() throws InstantiationException {
        // Arrange
        Location location = new Location("Main Street", "12345");
        Contact contact = new Contact("987654321", "351", "email@test.com", location);

        // Act & Assert
        assertDoesNotThrow(() ->
                new User("abcde", contact, "Admin")
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
    void testShouldReturnUserName() throws InstantiationException {
        // Arrange
        Location location = new Location("Main Street", "12345");
        Contact contact = new Contact("987654321", "351", "email@test.com", location);
        User user = new User("ValidName", contact, "Admin");

        // Act
        String result = user.getUserName();

        // Assert
        assertEquals("ValidName", result);
    }

    // Test getContact
    @Test
    void testShouldReturnContact() throws InstantiationException {
        // Arrange
        Location location = new Location("Main Street", "12345");
        Contact contact = new Contact("987654321", "351", "email@test.com", location);
        User user = new User("ValidName", contact, "Admin");

        // Act
        Contact result = user.getContact();

        // Assert
        assertEquals(contact, result);
    }

    // Test getProfile
    @Test
    void testShouldReturnProfile() throws InstantiationException {
        // Arrange
        Location location = new Location("Main Street", "12345");
        Contact contact = new Contact("987654321", "351", "email@test.com", location);
        User user = new User("ValidName", contact, "Admin");

        // Act
        String result = user.getProfile();

        // Assert
        assertEquals("Admin", result);
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