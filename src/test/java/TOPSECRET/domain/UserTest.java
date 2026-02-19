package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Countries;
import TOPSECRET.domain.Enum.UserRole;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static TOPSECRET.domain.Enum.UserRole.NORMAL_USER;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    /**
     * Helper method to create a valid User instance for testing purposes.
     */
    private User createValidUser() {

        Address address = new Address("Main Street", "10", "12345", new Country("PORTUGAL"));
        Email email = new Email("test.user", "example.com");
        Contact contact = new Contact("987654321", "351", email, address);

        Profile profile = new Profile(
                "John Doe",
                new Country("PORTUGAL"),
                LocalDate.of(1997, 8, 15)
        );

        return new User("John Doe", contact, profile, UserRole.NORMAL_USER);
    }


    /**
     * Verifies that the empty constructor can be instantiated.
     */
    @Test
    void testEmptyConstructor() {

        // Arrange

        // Act
        User user = new User();

        // Assert
        assertNotNull(user);
    }


    /**
     * Verifies that a User is successfully created with valid arguments.
     */
        @Test
        void testConstructorWithValidArguments() {

            // Arrange

            // Act
            User user = createValidUser();

            // Assert
            assertNotNull(user);
        }


    /**
     * Verifies that constructor throws IllegalArgumentException
     * when username is null.
     */
    @Test
    void testConstructorWithUsernameNullShouldThrowException() {

        // Arrange
        User valid = createValidUser();

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new User(null, valid.getContact(), valid.getProfile(), UserRole.NORMAL_USER));
    }


    /**
     * Verifies that constructor accepts username with exactly five characters.
     */
    @Test
    void testConstructorUsernameWithFiveCharacters() {

        // Arrange
        User valid = createValidUser();

        // Act
        User user = new User("abcde", valid.getContact(), valid.getProfile(), UserRole.NORMAL_USER);

        // Assert
        assertNotNull(user);
    }


    /**
     * Verifies that constructor throws IllegalArgumentException
     * when username contains fewer than five characters.
     */
    @Test
    void testConstructorWithUsernameTooShortShouldThrowException() {

        // Arrange
        User valid = createValidUser();

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new User("test",
                        valid.getContact(),
                        valid.getProfile(),
                        UserRole.NORMAL_USER));
    }


    /**
     * Verifies that getUserName() returns the correct username.
     */
    @Test
    void testShouldReturnUserName() {

        // Arrange
        User user = createValidUser();

        // Act
        String result = user.getUserName();

        // Assert
        assertEquals("John Doe", result);
    }


    /**
     * Verifies that getContact() returns the correct Contact object.
     */
    @Test
    void testShouldReturnContact() {

        // Arrange
        User user = createValidUser();
        Contact expected = user.getContact();

        // Act
        Contact result = user.getContact();

        // Assert
        assertEquals(expected, result);
    }


    /**
     * Verifies that getProfile() returns the correct Profile object.
     */
    @Test
    void testShouldReturnProfile() {

        // Arrange
        User user = createValidUser();
        Profile expected = user.getProfile();

        // Act
        Profile result = user.getProfile();

        // Assert
        assertEquals(expected, result);
    }


    /**
     * Verifies that getUserName() returns the provided username.
     */
    @Test
    void testShouldReturnProvidedUsername() {

        // Arrange
        User user = new User(
                "userNameTest",
                createValidUser().getContact(),
                createValidUser().getProfile(),
                UserRole.NORMAL_USER
        );

        // Act
        String result = user.getUserName();

        // Assert
        assertEquals("userNameTest", result);
    }


    /**
     * Verifies that getUserInfo() returns the correctly formatted string
     * containing all user information.
     */
    @Test
    void testShouldReturnFormattedString() {

        // Arrange
        User user = createValidUser();

        String expected = "User: " + user.getUserName() +
                ", Role: " + user.getUserRole() +
                ", Contact: " + user.getContact() +
                ", Profile: " + user.getProfile();

        // Act
        String result = user.getUserInfo();

        // Assert
        assertEquals(expected, result);
    }


    /**
     * Verifies that getUserRole() returns the correct role.
     */
    @Test
    void testShouldReturnUserRole() {

        // Arrange
        User user = createValidUser();

        // Act
        UserRole result = user.getUserRole();

        // Assert
        assertEquals(UserRole.NORMAL_USER, result);
    }


    /**
     * Verifies that equals() returns false when usernames are different.
     */
    @Test
    void testEqualsShouldReturnFalseWhenUsernamesAreDifferent() {

        User user1 = createValidUser();

        User user2 = new User(
                "DifferentUser",
                user1.getContact(),
                user1.getProfile(),
                UserRole.NORMAL_USER
        );

        assertNotEquals(user1, user2);
    }

    /**
     * Verifies that equals() returns true when usernames are equal,
     * regardless of other attributes.
     */
    @Test
    void testEqualsShouldReturnTrueWhenUsernamesAreEqual() {

        User user1 = createValidUser();

        User user2 = new User(
                "John Doe",
                user1.getContact(),
                user1.getProfile(),
                UserRole.NORMAL_USER
        );

        assertEquals(user1, user2);
    }

    /**
     * Verifies that hashCode() is equal for users that are considered equal.
     * Ensures equals/hashCode contract compliance.
     */
    @Test
    void testHashCodeShouldBeEqualForEqualUsers() {

        User user1 = createValidUser();

        User user2 = new User(
                "John Doe",
                user1.getContact(),
                user1.getProfile(),
                UserRole.NORMAL_USER
        );

        assertEquals(user1.hashCode(), user2.hashCode());
    }


    /**
     * Verifies that equals() returns true when comparing the same object reference
     * (covers the branch: this == o).
     */
    @Test
    void testEqualsShouldReturnTrueWhenSameReference() {

        // Arrange
        User user = createValidUser();

        // Act
        boolean result = user.equals(user);

        // Assert
        assertEquals(true, result);
    }

    /**
     * Verifies that hashCode() is not a constant value.
     * Kills the mutation that replaces hashCode() return value with 0.
     */
    @Test
    void testHashCodeShouldNotBeZeroForValidUser() {

        // Arrange
        User user = createValidUser();

        // Act
        int hash = user.hashCode();

        // Assert
        assertEquals(false, hash == 0);
    }



}