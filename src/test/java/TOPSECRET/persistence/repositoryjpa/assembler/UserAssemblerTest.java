package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.User.UserFactory;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.persistence.repositoryjpa.datamodel.UserDM;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserAssembler}.
 */
class UserAssemblerTest {

    @Test
    void shouldThrowExceptionWhenUserFactoryIsNull() {
        // Arrange

        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> new UserAssembler(null)
        );
    }

    @Test
    void shouldConvertUserToDM() {
        // Arrange
        UserFactory userFactoryDouble = mock(UserFactory.class);
        User userDouble = mock(User.class);
        UserId userIdDouble = mock(UserId.class);
        Profile profileDouble = mock(Profile.class);
        Set<UserRole> roles = Set.of(UserRole.ADMIN);

        when(userDouble.id()).thenReturn(userIdDouble);
        when(userDouble.getUserName()).thenReturn("TestUser");
              when(userDouble.getProfile()).thenReturn(profileDouble);
        when(userDouble.getUserRoles()).thenReturn(roles);

        UserAssembler sut = new UserAssembler(userFactoryDouble);

        // Act
        UserDM result = sut.toDM(userDouble);

        // Assert
        assertNotNull(result);
        assertEquals(userIdDouble, result.getId());
        assertEquals("TestUser", result.getUserName());
              assertEquals(profileDouble, result.getProfile());
        assertEquals(roles, result.getUserRoles());
    }

    @Test
    void shouldThrowExceptionWhenUserIsNull() {
        // Arrange
        UserFactory userFactoryDouble = mock(UserFactory.class);
        UserAssembler sut = new UserAssembler(userFactoryDouble);

        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> sut.toDM(null)
        );
    }

    @Test
    void shouldConvertDMToDomain() {
        // Arrange
        UserFactory userFactoryDouble = mock(UserFactory.class);
        User userDouble = mock(User.class);
        UserId userIdDouble = mock(UserId.class);
        Profile profileDouble = mock(Profile.class);
        Set<UserRole> roles = Set.of(UserRole.ADMIN);

        UserDM userDM = new UserDM(
                userIdDouble,
                "TestUser",
                       profileDouble,
                roles
        );

        when(userFactoryDouble.create(userIdDouble, "TestUser", profileDouble, roles
        )).thenReturn(userDouble);

        UserAssembler sut = new UserAssembler(userFactoryDouble);

        // Act
        User result = sut.toDomain(userDM);

        // Assert
        assertNotNull(result);
        assertSame(userDouble, result);
    }

    @Test
    void shouldThrowExceptionWhenDMIsNull() {
        // Arrange
        UserFactory userFactoryDouble = mock(UserFactory.class);
        UserAssembler sut = new UserAssembler(userFactoryDouble);

        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> sut.toDomain(null)
        );
    }
}