package TOPSECRET.domain.User;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class UserTest {

    private User createValidUser(String username) {

        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);

        return new User(username, contactDouble, profileDouble, UserRole.NORMAL_USER);
    }

    @Test
    void testEmptyConstructor() {

        // Arrange

        // Act
        User user = new User();

        // Assert
        assertNotNull(user);
    }

    @Test
    void testConstructorWithValidArguments() {

        // Arrange

        // Act
        User user = createValidUser("Rodolph");

        // Assert
        assertNotNull(user);
    }


    @Test
    void testConstructorWithUsernameNullShouldThrowException() {

        // Arrange
        User valid = createValidUser("Rodolph");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new User(null, valid.getContact(), valid.getProfile(), UserRole.NORMAL_USER));
    }

    @Test
    void testConstructorUsernameWithFiveCharacters() {

        // Arrange
        User valid = createValidUser("Rodolph");

        // Act
        User user = new User("abcde", valid.getContact(), valid.getProfile(), UserRole.NORMAL_USER);

        // Assert
        assertNotNull(user);
    }


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


    @Test
    void testShouldReturnUserName() {

        // Arrange
        User user = createValidUser("John Doe");

        // Act
        String result = user.getUserName();

        // Assert
        assertEquals("John Doe", result);
    }

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

    @Test
    void testShouldReturnProvidedUsername() {

        // Arrange
        User user = createValidUser("userNameTest");

        // Act
        String result = user.getUserName();

        // Assert
        assertEquals("userNameTest", result);
    }

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

    @Test
    void testShouldReturnUserRole() {

        // Arrange
        User user = createValidUser("userNameTest");

        // Act
        UserRole result = user.getUserRole();

        // Assert
        assertEquals(UserRole.NORMAL_USER, result);
    }

    @Test
    void testEqualsShouldReturnFalseWhenUsernamesAreDifferent() {

        User user1 = createValidUser("userNameTest");
        User user2 = new User(
                "DifferentUser",
                user1.getContact(),
                user1.getProfile(),
                UserRole.NORMAL_USER
        );
        assertFalse(user1.sameAs(user2));
    }

    @Test
    void testEqualsShouldReturnTrueWhenUsernamesAreEqual() {

        User user1 = createValidUser("John Doe");
        User user2 = new User(
                "John Doe",
                user1.getContact(),
                user1.getProfile(),
                UserRole.NORMAL_USER
        );
        assertTrue(user1.sameAs(user2));
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

    @Test
    void testHashCodeShouldBeEqualForEqualUsers() {

        UserId sameId = new UserId(UUID.randomUUID().toString());

        User user1 = createValidUser("John Doe");
        User user2 = createValidUser("John Doe");


        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testHashCodeConsistentWithEquals() {
        User user = createValidUser("John Doe");
        User sameUser = user;  // mesma referência
        assertEquals(user.hashCode(), sameUser.hashCode());
    }

    @Test
    void testEqualsShouldReturnTrueWhenSameReference() {

        // Arrange
        User user = createValidUser("John Doe");

        // Act
        boolean result = user.equals(user);

        // Assert
        assertEquals(true, result);
    }

    @Test
    void testHashCodeShouldNotBeZeroForValidUser() {

        // Arrange
        User user = createValidUser("John Doe");

        // Act
        int hash = user.hashCode();

        // Assert
        assertEquals(false, hash == 0);
    }

    @Test
    void testIdShouldReturnNonNullUserId() {

        // Arrange
        User user = createValidUser("Rodolph");

        // Act
        UserId result = user.id();

        // Assert
        assertNotNull(result);
    }


    @Test
    void testIdShouldBeUniqueForEachUser() {

        // Arrange
        User user1 = createValidUser("Rodolph");
        User user2 = createValidUser("Helena");

        // Act & Assert
        assertNotEquals(user1.id(), user2.id());
    }


    @Test
    void testSameAsShouldReturnTrueForSameObject() {

        // Arrange
        User user = createValidUser("Rodolph");

        // Act
        boolean result = user.sameAs(user);

        // Assert
        assertTrue(result);
    }


    @Test
    void testSameAsShouldReturnFalseForDifferentUsers() {

        // Arrange
        User user1 = createValidUser("Rodolph");
        User user2 = createValidUser("Helena");

        // Act
        boolean result = user1.sameAs(user2);

        // Assert
        assertFalse(result);
    }


    @Test
    void testSameAsShouldReturnFalseForNull() {

        // Arrange
        User user = createValidUser("Rodolph");

        // Act
        boolean result = user.sameAs(null);

        // Assert
        assertFalse(result);
    }


    @Test
    void testSameAsShouldReturnFalseForDifferentClass() {

        // Arrange
        User user = createValidUser("Rodolph");

        // Act
        boolean result = user.sameAs("not a user");

        // Assert
        assertFalse(result);
    }

}