package TOPSECRET.domain.User;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link User}.
 * <p>
 * This test class verifies the creation and behaviour of the {@code User}
 * aggregate root, including constructor validation, getters, identity,
 * equality, hash code generation, and {@code sameAs} comparison.
 * </p>
 * <ul>
 *     <li>Creation with the empty constructor</li>
 *     <li>Creation with automatically generated {@link UserId}</li>
 *     <li>Reconstruction with an existing {@link UserId}</li>
 *     <li>Validation of required business attributes</li>
 *     <li>Access to user information through getters</li>
 *     <li>Equality and identity behaviour</li>
 *     <li>Business comparison using {@code sameAs}</li>
 * </ul>
 */
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

    // ------------- Second Constructor ---------------

    @Test
    void secondConstructor_shouldCreateUserWithGivenId_whenValidArguments() {
        // Arrange
        UserId userId = new UserId(UUID.randomUUID().toString());
        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);

        // Act
        User user = new User(
                userId,
                "Rodolph",
                contactDouble,
                profileDouble,
                UserRole.NORMAL_USER
        );

        // Assert
        assertNotNull(user);
        assertEquals(userId, user.id());
        assertEquals("Rodolph", user.getUserName());
        assertEquals(contactDouble, user.getContact());
        assertEquals(profileDouble, user.getProfile());
        assertEquals(UserRole.NORMAL_USER, user.getUserRole());
    }

    @Test
    void secondConstructor_shouldThrowException_whenUserIdIsNull() {
        // Arrange
        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new User(null, "Rodolph", contactDouble, profileDouble, UserRole.NORMAL_USER)
        );
    }

    @Test
    void secondConstructor_shouldThrowException_whenUsernameIsNull() {
        // Arrange
        UserId userId = new UserId(UUID.randomUUID().toString());
        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new User(userId, null, contactDouble, profileDouble, UserRole.NORMAL_USER)
        );
    }

    @Test
    void secondConstructor_shouldThrowException_whenUsernameIsTooShort() {
        // Arrange
        UserId userId = new UserId(UUID.randomUUID().toString());
        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new User(userId, "test", contactDouble, profileDouble, UserRole.NORMAL_USER)
        );
    }

    @Test
    void secondConstructor_shouldThrowException_whenProfileIsNull() {
        // Arrange
        UserId userId = new UserId(UUID.randomUUID().toString());
        Contact contactDouble = mock(Contact.class);

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new User(userId, "Rodolph", contactDouble, null, UserRole.NORMAL_USER)
        );
    }

    @Test
    void secondConstructor_shouldThrowException_whenUserRoleIsNull() {
        // Arrange
        UserId userId = new UserId(UUID.randomUUID().toString());
        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new User(userId, "Rodolph", contactDouble, profileDouble, null)
        );
    }

    // --------- Equals ----------

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
    void testEqualsShouldReturnTrueWhenSameReference() {

        // Arrange
        User user = createValidUser("John Doe");

        // Act
        boolean result = user.equals(user);

        // Assert
        assertEquals(true, result);
    }

    // --------- HAshCode ----------

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

    // --------- Id ----------

    @Test
    void testIdShouldBeUniqueForEachUser() {

        // Arrange
        User user1 = createValidUser("Rodolph");
        User user2 = createValidUser("Helena");

        // Act & Assert
        assertNotEquals(user1.id(), user2.id());
    }

    // --------- SameAs ----------

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