package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link UserDM}.
 */
class UserDMTest {

    @Test
    void constructor_shouldCreateUserDM_whenValidArguments() {
        // Arrange
        UserId userId = mock(UserId.class);
        String username = "Rodolph";
        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);
        UserRole userRole = UserRole.NORMAL_USER;

        // Act
        UserDM userDM = new UserDM(userId, username, contactDouble, profileDouble, userRole);

        // Assert
        assertNotNull(userDM);
        assertEquals(userId, userDM.getId());
        assertEquals(username, userDM.getUserName());
        assertEquals(contactDouble, userDM.getContact());
        assertEquals(profileDouble, userDM.getProfile());
        assertEquals(userRole, userDM.getUserRole());
    }

    @Test
    void constructor_shouldThrowException_whenUserIdIsNull() {
        // Arrange
        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);

        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> new UserDM(null, "Rodolph", contactDouble, profileDouble, UserRole.NORMAL_USER)
        );
    }

    @Test
    void constructor_shouldThrowException_whenUsernameIsNull() {
        // Arrange
        UserId userId = mock(UserId.class);
        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);

        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> new UserDM(userId, null, contactDouble, profileDouble, UserRole.NORMAL_USER)
        );
    }

    @Test
    void constructor_shouldCreateUserDM_whenContactIsNull() {
        // Arrange
        UserId userId = mock(UserId.class);
        Profile profileDouble = mock(Profile.class);

        // Act
        UserDM userDM = new UserDM(userId, "Rodolph", null, profileDouble, UserRole.NORMAL_USER);

        // Assert
        assertNotNull(userDM);
        assertNull(userDM.getContact());
    }

    @Test
    void constructor_shouldThrowException_whenProfileIsNull() {
        // Arrange
        UserId userId = mock(UserId.class);
        Contact contactDouble = mock(Contact.class);

        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> new UserDM(userId, "Rodolph", contactDouble, null, UserRole.NORMAL_USER)
        );
    }

    @Test
    void constructor_shouldThrowException_whenUserRoleIsNull() {
        // Arrange
        UserId userId = mock(UserId.class);
        Contact contactDouble = mock(Contact.class);
        Profile profileDouble = mock(Profile.class);

        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> new UserDM(userId, "Rodolph", contactDouble, profileDouble, null)
        );
    }
}