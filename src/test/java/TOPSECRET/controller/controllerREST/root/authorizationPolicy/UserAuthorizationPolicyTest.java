package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserAuthorizationPolicy}.
 *
 * <p>Validates that only ADMIN users are permitted to register new users.</p>
 */
class UserAuthorizationPolicyTest {

    @Test
    void canRegisterUser_shouldReturnTrue_whenUserIsAdmin() {
        // ARRANGE
        UserAuthorizationPolicy policy = new UserAuthorizationPolicy();
        User userDouble = mock(User.class);
        when(userDouble.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // ACT
        boolean result = policy.canRegisterUser(userDouble);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void canRegisterUser_shouldReturnFalse_whenUserIsNormalUser() {
        // ARRANGE
        UserAuthorizationPolicy policy = new UserAuthorizationPolicy();
        User userDouble = mock(User.class);
        when(userDouble.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // ACT
        boolean result = policy.canRegisterUser(userDouble);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void canRegisterUser_shouldReturnTrue_whenUserHasAdminAndOtherRoles() {
        // ARRANGE
        UserAuthorizationPolicy policy = new UserAuthorizationPolicy();
        User userDouble = mock(User.class);
        when(userDouble.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN, UserRole.NORMAL_USER));

        // ACT
        boolean result = policy.canRegisterUser(userDouble);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void canRegisterUser_shouldReturnFalse_whenUserHasNoRoles() {
        // ARRANGE
        UserAuthorizationPolicy policy = new UserAuthorizationPolicy();
        User userDouble = mock(User.class);
        when(userDouble.getUserRoles()).thenReturn(Set.of());

        // ACT
        boolean result = policy.canRegisterUser(userDouble);

        // ASSERT
        assertFalse(result);
    }
}