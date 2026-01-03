package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

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
        Profile profile = new Profile(
                "Test Profile",
                LocalDate.of(1997, 8, 15),
                true
        );

        // Act & Assert
        assertDoesNotThrow(() ->
                new User("ValidName", contact, profile)
        );
    }

    @Test
    void testConstructorWithUsernameNullShouldThrowException() throws InstantiationException {
        // Arrange
        Location location = new Location("Main Street", "12345");
        Contact contact = new Contact("987654321", "351", "email@test.com", location);
        Profile profile = new Profile(
                "Test Profile",
                LocalDate.of(1997, 8, 15),
                true
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new User(null, contact, profile)
        );
    }

    @Test
    void testConstructorUsernameWithFiveCharacters() throws InstantiationException {
        // Arrange
        Location location = new Location("Main Street", "12345");
        Contact contact = new Contact("987654321", "351", "email@test.com", location);
        Profile profile = new Profile(
                "Test Profile",
                LocalDate.of(1997, 8, 15),
                true
        );

        // Act & Assert
        assertDoesNotThrow(() ->
                new User("abcde", contact, profile)
        );
    }

    @Test
    void testConstructorWithUsernameTooShortShouldThrowException() throws InstantiationException {
        // Arrange
        Location location = new Location("Main Street", "12345");
        Contact contact = new Contact("987654321", "351", "email@test.com", location);
        Profile profile = new Profile(
                "Test Profile",
                LocalDate.of(1997, 8, 15),
                true
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new User("test", contact, profile)
        );
    }

    // Test getUserName
    @Test
    void testShouldReturnUserName() throws InstantiationException {
        // Arrange
        Location location = new Location("Main Street", "12345");
        Contact contact = new Contact("987654321", "351", "email@test.com", location);
        Profile profile = new Profile(
                "Test Profile",
                LocalDate.of(1997, 8, 15),
                true
        );
        User user = new User("ValidName", contact, profile);

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
        Profile profile = new Profile(
                "Test Profile",
                LocalDate.of(1997, 8, 15),
                true
        );
        User user = new User("ValidName", contact, profile);

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
        Profile profile = new Profile(
                "Test Profile",
                LocalDate.of(1997, 8, 15),
                true
        );
        User user = new User("ValidName", contact, profile);


        // Act
        Profile result = user.getProfile();

        // Assert
        assertEquals(profile, result);
    }


    // Test getUserName
    @Test
    void testShouldReturnProvidedUsername() throws InstantiationException {
        // Arrange
        Location location = new Location("Main Street", "12345");
        Contact contact = new Contact("987654321", "351", "email@test.com", location);
        Profile profile = new Profile(
                "Test Profile",
                LocalDate.of(1997, 8, 15),
                true
        );
        User user = new User("userNameTest", contact, profile);

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
        Profile profile = new Profile(
                "Test Profile",
                LocalDate.of(1997, 8, 15),
                true
        );
        User user = new User("userNameTest", contact, profile);

        // Act
        String result = user.getUserInfo();

        // Assert
        assertTrue(result.contains("User: userNameTest"));
        assertTrue(result.contains("Contact: "));
        assertTrue(result.contains("Profile: " + profile));
    }
}