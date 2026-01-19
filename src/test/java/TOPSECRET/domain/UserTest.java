package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Country;
import TOPSECRET.domain.Enum.UserRole;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static TOPSECRET.domain.Enum.UserRole.NORMAL_USER;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testEmptyConstructor() {

       new User();
    }

    @Test
    void testConstructorWithValidArguments() {
        // Arrange
        Address address = new Address("Main Street", "10", "12345", Country.PORTUGAL);
        Email email = new Email("test.user", "example.com");
        Contact contact = new Contact("987654321", "351", email, address);

        Profile profile = new Profile(
                "Test Profile",
                Country.PORTUGAL,
                LocalDate.of(1997, 8, 15)
                );

        // Act & Assert
        assertDoesNotThrow(() ->
                new User("ValidName", contact, profile, UserRole.NORMAL_USER)
        );
    }

    @Test
    void testConstructorWithUsernameNullShouldThrowException() {
        // Arrange
        Address address = new Address("Main Street", "10", "12345", Country.PORTUGAL);
        Email email = new Email("test.user", "example.com");
        Contact contact = new Contact("987654321", "351", email, address);

        Profile profile = new Profile(
                "Test Profile",
                Country.PORTUGAL,
                LocalDate.of(1997, 8, 15)
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new User(null, contact, profile, UserRole.NORMAL_USER)
        );
    }

    @Test
    void testConstructorUsernameWithFiveCharacters() {
        // Arrange
        Address address = new Address("Main Street", "10", "12345", Country.PORTUGAL);
        Email email = new Email("test.user", "example.com");
        Contact contact = new Contact("987654321", "351", email, address);

        Profile profile = new Profile(
                "Test Profile",
                Country.PORTUGAL,
                LocalDate.of(1997, 8, 15)
        );

        // Act & Assert
        assertDoesNotThrow(() ->
                new User("abcde", contact, profile, UserRole.NORMAL_USER)
        );
    }

    @Test
    void testConstructorWithUsernameTooShortShouldThrowException() {
        // Arrange
        Address address = new Address("Main Street", "10", "12345", Country.PORTUGAL);
        Email email = new Email("test.user", "example.com");
        Contact contact = new Contact("987654321", "351", email, address);

        Profile profile = new Profile(
                "Test Profile",
                Country.PORTUGAL,
                LocalDate.of(1997, 8, 15)
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new User("test", contact, profile, UserRole.NORMAL_USER)
        );
    }

    @Test
    void testShouldReturnUserName() {
        // Arrange
        Address address = new Address("Main Street", "10", "12345", Country.PORTUGAL);
        Email email = new Email("test.user", "example.com");
        Contact contact = new Contact("987654321", "351", email, address);

        Profile profile = new Profile(
                "Test Profile",
                Country.PORTUGAL,
                LocalDate.of(1997, 8, 15)
        );

        User user = new User("ValidName", contact, profile, UserRole.NORMAL_USER);

        // Act
        String result = user.getUserName();

        // Assert
        assertEquals("ValidName", result);
    }

    @Test
    void testShouldReturnContact() {
        // Arrange
        Address address = new Address("Main Street", "10", "12345", Country.PORTUGAL);
        Email email = new Email("test.user", "example.com");
        Contact contact = new Contact("987654321", "351", email, address);

        Profile profile = new Profile(
                "Test Profile",
                Country.PORTUGAL,
                LocalDate.of(1997, 8, 15)
        );

        User user = new User("ValidName", contact, profile, UserRole.NORMAL_USER);

        // Act
        Contact result = user.getContact();

        // Assert
        assertEquals(contact, result);
    }

    @Test
    void testShouldReturnProfile() {
        // Arrange
        Address address = new Address("Main Street", "10", "12345", Country.PORTUGAL);
        Email email = new Email("test.user", "example.com");
        Contact contact = new Contact("987654321", "351", email, address);

        Profile profile = new Profile(
                "Test Profile",
                Country.PORTUGAL,
                LocalDate.of(1997, 8, 15)
        );

        User user = new User("ValidName", contact, profile, UserRole.NORMAL_USER);

        // Act
        Profile result = user.getProfile();

        // Assert
        assertEquals(profile, result);
    }

    @Test
    void testShouldReturnProvidedUsername() {
        // Arrange
        Address address = new Address("Main Street", "10", "12345", Country.PORTUGAL);
        Email email = new Email("test.user", "example.com");
        Contact contact = new Contact("987654321", "351", email, address);

        Profile profile = new Profile(
                "Test Profile",
                Country.PORTUGAL,
                LocalDate.of(1997, 8, 15)
        );

        User user = new User("userNameTest", contact, profile, UserRole.NORMAL_USER);

        // Act
        String result = user.getUserName();

        // Assert
        assertEquals("userNameTest", result);
    }

    @Test
    void testShouldReturnFormattedString() {
        // Arrange
        Address address = new Address("Main Street", "10", "12345", Country.PORTUGAL);
        Email email = new Email("test.user", "example.com");
        Contact contact = new Contact("987654321", "351", email, address);

        Profile profile = new Profile(
                "Test Profile",
                Country.PORTUGAL,
                LocalDate.of(1997, 8, 15)
        );

        User user = new User("userNameTest", contact, profile, UserRole.NORMAL_USER);

        // Act
        String result = user.getUserInfo();

        // Assert
        assertTrue(result.contains("User: userNameTest"));
        assertTrue(result.contains("Contact: "));
        assertTrue(result.contains("Profile: " + profile));
    }
}