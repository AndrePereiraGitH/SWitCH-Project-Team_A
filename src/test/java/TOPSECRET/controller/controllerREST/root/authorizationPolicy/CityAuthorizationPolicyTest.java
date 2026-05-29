package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CityAuthorizationPolicy}.
 *
 * <p>Validates permission logic for City-related operations,
 * ensuring only users with the correct role are authorized.</p>
 */
class CityAuthorizationPolicyTest {

    @Test
    void canCreateCity_shouldReturnTrue_whenUserIsAdmin() {

        // ARRANGE
        CityAuthorizationPolicy sut = new CityAuthorizationPolicy();
        User doubleUser = mock(User.class);
        when(doubleUser.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // ACT
        boolean result = sut.canCreateCity(doubleUser);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void canCreateCity_shouldReturnFalse_whenUserIsNotAdmin() {

        // ARRANGE
        CityAuthorizationPolicy sut = new CityAuthorizationPolicy();
        User doubleUser = mock(User.class);
        when(doubleUser.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // ACT
        boolean result = sut.canCreateCity(doubleUser);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void canCreateCity_shouldReturnFalse_whenUserHasNoRoles() {

        // ARRANGE
        CityAuthorizationPolicy sut = new CityAuthorizationPolicy();
        User doubleUser = mock(User.class);
        when(doubleUser.getUserRoles()).thenReturn(Set.of());
        // ACT
        boolean result = sut.canCreateCity(doubleUser);

        // ASSERT
        assertFalse(result);
    }
}