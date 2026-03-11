package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Countries;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserTest {

    /**
     * Helper method to create a valid User instance for testing purposes.
     */
    private User createValidUser(String username) {

        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);

        return new User(username, contactDouble, profileDouble, UserRole.NORMAL_USER);
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
            User user = createValidUser("Rodolph");

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
        User valid = createValidUser("Rodolph");

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
        User valid = createValidUser("Rodolph");

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
        User valid = createValidUser("Rodolph");

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
        User user = createValidUser("John Doe");

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
        User user = createValidUser("Rodolph");

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
        User user = createValidUser("Rodolph");
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
        User user = createValidUser("userNameTest");

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
        User user = createValidUser("userNameTest");

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
        User user = createValidUser("userNameTest");

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

        User user1 = createValidUser("userNameTest");

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

        User user1 = createValidUser("John Doe");

        User user2 = new User(
                "John Doe",
                user1.getContact(),
                user1.getProfile(),
                UserRole.NORMAL_USER
        );

        assertEquals(user1, user2);
    }

    @Test
    void equals_shouldReturnFalseForNull_orDifferentObject() {

        // arrange
        String username = "Rodolph";


        // act
        User user = createValidUser(username);
       // User user = new User(username, contactDouble, profileDouble, UserRole.NORMAL_USER);

        // arrange
        assertFalse(user.equals(null));
        assertFalse(user.equals("not a user"));

    }

    /**
     * Verifies that hashCode() is equal for users that are considered equal.
     * Ensures equals/hashCode contract compliance.
     */
    @Test
    void testHashCodeShouldBeEqualForEqualUsers() {

        User user1 = createValidUser("John Doe");

        User user2 = createValidUser("John Doe");

        assertEquals(user1.hashCode(), user2.hashCode());
    }


    /**
     * Verifies that equals() returns true when comparing the same object reference
     * (covers the branch: this == o).
     */
    @Test
    void testEqualsShouldReturnTrueWhenSameReference() {

        // Arrange
        User user = createValidUser("John Doe");

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
        User user = createValidUser("John Doe");

        // Act
        int hash = user.hashCode();

        // Assert
        assertEquals(false, hash == 0);
    }



}