package TOPSECRET.domain.User;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link User}.
 */
class UserTest {

    private User createValidUser(String username) {
        Profile profileDouble = mock(Profile.class);

        return new User(
                username,
                profileDouble,
                Set.of(UserRole.NORMAL_USER)
        );
    }

    @Test
    void testEmptyConstructor() {
        User user = new User();

        assertNotNull(user);
    }

    @Test
    void testConstructorWithValidArguments() {
        User user = createValidUser("Rodolph");

        assertNotNull(user);
    }

    @Test
    void testConstructorWithUsernameNullShouldThrowException() {
        User valid = createValidUser("Rodolph");

        assertThrows(IllegalArgumentException.class,
                () -> new User(null, valid.getProfile(), Set.of(UserRole.NORMAL_USER)));
    }

    @Test
    void testConstructorUsernameWithFiveCharacters() {
        User valid = createValidUser("Rodolph");

        User user = new User("abcde",  valid.getProfile(), Set.of(UserRole.NORMAL_USER));

        assertNotNull(user);
    }

    @Test
    void testConstructorWithUsernameTooShortShouldThrowException() {
        User valid = createValidUser("Rodolph");

        assertThrows(IllegalArgumentException.class,
                () -> new User("test", valid.getProfile(), Set.of(UserRole.NORMAL_USER)));
    }

    @Test
    void testConstructorWithUserRolesNullShouldThrowException() {
        User valid = createValidUser("Rodolph");

        assertThrows(IllegalArgumentException.class,
                () -> new User(
                        "Rodolph",
                        valid.getProfile(),
                        null
                ));
    }

    @Test
    void testConstructorWithUserRolesEmptyShouldThrowException() {
        User valid = createValidUser("Rodolph");

        assertThrows(IllegalArgumentException.class,
                () -> new User(
                        "Rodolph",
                        valid.getProfile(),
                        Set.of()
                ));
    }

    @Test
    void testShouldReturnUserName() {
        User user = createValidUser("John Doe");

        assertEquals("John Doe", user.getUserName());
    }


    @Test
    void testShouldReturnProfile() {
        User user = createValidUser("Rodolph");

        assertEquals(user.getProfile(), user.getProfile());
    }

    @Test
    void testShouldReturnProvidedUsername() {
        User user = createValidUser("userNameTest");

        assertEquals("userNameTest", user.getUserName());
    }

    @Test
    void testShouldReturnFormattedString() {
        User user = createValidUser("userNameTest");

        String expected = "User: " + user.getUserName() +
                ", Role: " + user.getUserRoles() +
                ", Profile: " + user.getProfile();

        assertEquals(expected, user.getUserInfo());
    }

    @Test
    void testShouldReturnUserRoles() {
        User user = createValidUser("userNameTest");

        assertEquals(Set.of(UserRole.NORMAL_USER), user.getUserRoles());
    }

    // ------------- Second Constructor ---------------

    @Test
    void secondConstructor_shouldCreateUserWithGivenId_whenValidArguments() {
        UserId userIdDouble = mock(UserId.class);
        Profile profileDouble = mock(Profile.class);
        Set<UserRole> roles = Set.of(UserRole.NORMAL_USER);

        User user = new User(
                userIdDouble,
                "Rodolph",
                profileDouble,
                roles
        );

        assertNotNull(user);
        assertEquals(userIdDouble, user.id());
    }

    @Test
    void secondConstructor_shouldThrowException_whenUserIdIsNull() {
        Profile profileDouble = mock(Profile.class);

        assertThrows(
                IllegalArgumentException.class,
                () -> new User(
                        null,
                        "Rodolph",
                        profileDouble,
                        Set.of(UserRole.NORMAL_USER)
                )
        );
    }

    @Test
    void secondConstructor_shouldThrowException_whenUsernameIsNull() {
        UserId userIdDouble = mock(UserId.class);
        Profile profileDouble = mock(Profile.class);

        assertThrows(
                IllegalArgumentException.class,
                () -> new User(
                        userIdDouble,
                        null,
                        profileDouble,
                        Set.of(UserRole.NORMAL_USER)
                )
        );
    }

    @Test
    void secondConstructor_shouldThrowException_whenUsernameIsTooShort() {
        UserId userIdDouble = mock(UserId.class);
        Profile profileDouble = mock(Profile.class);

        assertThrows(
                IllegalArgumentException.class,
                () -> new User(
                        userIdDouble,
                        "test",
                        profileDouble,
                        Set.of(UserRole.NORMAL_USER)
                )
        );
    }

    @Test
    void secondConstructor_shouldThrowException_whenProfileIsNull() {
        UserId userIdDouble = mock(UserId.class);

        assertThrows(
                IllegalArgumentException.class,
                () -> new User(
                        userIdDouble,
                        "Rodolph",
                        null,
                        Set.of(UserRole.NORMAL_USER)
                )
        );
    }

    @Test
    void secondConstructor_shouldThrowException_whenUserRolesIsNull() {
        UserId userIdDouble = mock(UserId.class);
        Profile profileDouble = mock(Profile.class);

        assertThrows(
                IllegalArgumentException.class,
                () -> new User(
                        userIdDouble,
                        "Rodolph",
                        profileDouble,
                        null
                )
        );
    }

    @Test
    void secondConstructor_shouldThrowException_whenUserRolesIsEmpty() {
        UserId userIdDouble = mock(UserId.class);
        Profile profileDouble = mock(Profile.class);

        assertThrows(
                IllegalArgumentException.class,
                () -> new User(
                        userIdDouble,
                        "Rodolph",
                        profileDouble,
                        Set.of()
                )
        );
    }

    // --------- Equals / SameAs ----------

    @Test
    void testSameAsShouldReturnFalseWhenUsernamesAreDifferent() {
        User user1 = createValidUser("userNameTest");

        User user2 = new User(
                "DifferentUser",
                user1.getProfile(),
                Set.of(UserRole.NORMAL_USER)
        );

        assertFalse(user1.sameAs(user2));
    }

    @Test
    void testSameAsShouldReturnTrueWhenBusinessAttributesAreEqual() {
        User user1 = createValidUser("John Doe");

        User user2 = new User(
                "John Doe",
                user1.getProfile(),
                Set.of(UserRole.NORMAL_USER)
        );

        assertTrue(user1.sameAs(user2));
    }

    @Test
    void equals_shouldReturnFalseForNull_orDifferentObject() {
        User user = createValidUser("Rodolph");

        assertNotEquals(null, user);
        assertNotEquals("not a user", user);
    }

    @Test
    void testEqualsShouldReturnTrueWhenSameReference() {
        User user = createValidUser("John Doe");

        assertTrue(user.equals(user));
    }

    // --------- HashCode ----------

    @Test
    void testHashCodeShouldBeDifferentForDifferentUsers() {
        User user1 = createValidUser("John Doe");
        User user2 = createValidUser("John Doe");

        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testHashCodeConsistentWithEquals() {
        User user = createValidUser("John Doe");
        User sameUser = user;

        assertEquals(user.hashCode(), sameUser.hashCode());
    }

    @Test
    void testHashCodeShouldNotBeZeroForValidUser() {
        User user = createValidUser("John Doe");

        assertNotEquals(0, user.hashCode());
    }

    // --------- Id ----------

    @Test
    void testIdShouldReturnNonNullUserId() {
        User user = createValidUser("Rodolph");

        assertNotNull(user.id());
    }

    @Test
    void testIdShouldBeUniqueForEachUser() {
        User user1 = createValidUser("Rodolph");
        User user2 = createValidUser("Helena");

        assertNotEquals(user1.id(), user2.id());
    }

    // --------- SameAs ----------

    @Test
    void testSameAsShouldReturnTrueForSameObject() {
        User user = createValidUser("Rodolph");

        assertTrue(user.sameAs(user));
    }

    @Test
    void testSameAsShouldReturnFalseForDifferentUsers() {
        User user1 = createValidUser("Rodolph");
        User user2 = createValidUser("Helena");

        assertFalse(user1.sameAs(user2));
    }

    @Test
    void testSameAsShouldReturnFalseForNull() {
        User user = createValidUser("Rodolph");

        assertFalse(user.sameAs(null));
    }

    @Test
    void testSameAsShouldReturnFalseForDifferentClass() {
        User user = createValidUser("Rodolph");

        assertFalse(user.sameAs("not a user"));
    }
}