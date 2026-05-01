package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.User.UserFactory;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.persistence.repositoryjpa.datamodel.UserDM;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAssemblerTest {

    @Mock
    private UserFactory userFactory;

    @Mock
    private User user;

    @Mock
    private UserId userId;

    @Mock
    private Profile profile;

    @InjectMocks
    private UserAssembler sut;

    @Test
    void shouldThrowExceptionWhenUserFactoryIsNull() {
        assertThrows(
                NullPointerException.class,
                () -> new UserAssembler(null)
        );
    }

    @Test
    void shouldConvertUserToDM() {
        // Arrange
        Set<UserRole> roles = Set.of(UserRole.ADMIN);

        when(user.id()).thenReturn(userId);
        when(user.getUserName()).thenReturn("TestUser");
        when(user.getProfile()).thenReturn(profile);
        when(user.getUserRoles()).thenReturn(roles);

        // Act
        UserDM result = sut.toDM(user);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("TestUser", result.getUserName());
        assertEquals(profile, result.getProfile());
        assertEquals(roles, result.getUserRoles());
    }

    @Test
    void shouldThrowExceptionWhenUserIsNull() {
        assertThrows(
                NullPointerException.class,
                () -> sut.toDM(null)
        );
    }

    @Test
    void shouldConvertDMToDomain() {
        // Arrange
        Set<UserRole> roles = Set.of(UserRole.ADMIN);

        UserDM userDM = new UserDM(
                userId,
                "TestUser",
                profile,
                roles
        );

        when(userFactory.create(userId, "TestUser", profile, roles))
                .thenReturn(user);

        // Act
        User result = sut.toDomain(userDM);

        // Assert
        assertNotNull(result);
        assertSame(user, result);
    }

    @Test
    void shouldThrowExceptionWhenDMIsNull() {
        assertThrows(
                NullPointerException.class,
                () -> sut.toDomain(null)
        );
    }
}