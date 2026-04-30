package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;

import java.util.Set;

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
         Profile profileDouble = mock(Profile.class);
        Set<UserRole> roles = Set.of(UserRole.NORMAL_USER);

        // Act
        UserDM userDM = new UserDM(userId, username , profileDouble, roles);

        // Assert
        assertNotNull(userDM);
        assertEquals(userId, userDM.getId());
        assertEquals(username, userDM.getUserName());
        assertEquals(profileDouble, userDM.getProfile());
        assertEquals(roles, userDM.getUserRoles());
    }

    @Test
    void constructor_shouldThrowException_whenUserIdIsNull() {
        // Arrange
         Profile profileDouble = mock(Profile.class);

        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> new UserDM(null, "Rodolph" , profileDouble, Set.of(UserRole.NORMAL_USER))
        );
    }

    @Test
    void constructor_shouldThrowException_whenUsernameIsNull() {
        // Arrange
        UserId userId = mock(UserId.class);
         Profile profileDouble = mock(Profile.class);

        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> new UserDM(userId, null , profileDouble, Set.of(UserRole.NORMAL_USER))
        );
    }


    @Test
    void constructor_shouldThrowException_whenProfileIsNull() {
        // Arrange
        UserId userId = mock(UserId.class);
 
        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> new UserDM(userId, "Rodolph" , null, Set.of(UserRole.NORMAL_USER))
        );
    }

    @Test
    void constructor_shouldThrowException_whenUserRolesIsNull() {
        // Arrange
        UserId userId = mock(UserId.class);
         Profile profileDouble = mock(Profile.class);

        // Act & Assert
        assertThrows(
               IllegalArgumentException.class,
                () -> new UserDM(userId, "Ines" , profileDouble, null)
        );
    }

    @Test
    void constructor_shouldThrowException_whenUserRolesIsEmpty() {
        // Arrange
        UserId userId = mock(UserId.class);
         Profile profileDouble = mock(Profile.class);

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new UserDM(userId, "Ines" , profileDouble, Set.of())
        );
    }
}