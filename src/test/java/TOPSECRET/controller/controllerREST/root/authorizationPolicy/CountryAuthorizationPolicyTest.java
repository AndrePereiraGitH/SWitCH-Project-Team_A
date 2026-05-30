package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CountryAuthorizationPolicy}.
 *
 * <p>
 * This test suite verifies the authorization rules for country creation.
 * Only ADMIN users should be allowed to register countries.
 * </p>
 *
 * <p>
 * Each test follows the AAA pattern:
 * <ul>
 *     <li><b>Arrange</b> - setup mock user and policy</li>
 *     <li><b>Act</b> - execute method under test</li>
 *     <li><b>Assert</b> - verify result</li>
 * </ul>
 * </p>
 */
class CountryAuthorizationPolicyTest {

    private CountryAuthorizationPolicy policy;


    // -------------------------------------------------
    // canRegisterCountry
    // -------------------------------------------------

    @Test
    void admin_canRegisterCountry_shouldReturnTrue() {

        // Arrange
        CountryAuthorizationPolicy policy = new CountryAuthorizationPolicy();

        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // Act
        boolean result = policy.canRegisterCountry(user);

        // Assert
        assertTrue(result);
    }

    @Test
    void normalUser_cannotRegisterCountry_shouldReturnFalse() {

        // Arrange
        CountryAuthorizationPolicy policy = new CountryAuthorizationPolicy();

        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // Act
        boolean result = policy.canRegisterCountry(user);

        // Assert
        assertFalse(result);
    }

    @Test
    void user_withNoRoles_shouldReturnFalse() {

        // Arrange
        CountryAuthorizationPolicy policy = new CountryAuthorizationPolicy();

        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of());

        // Act
        boolean result = policy.canRegisterCountry(user);

        // Assert
        assertFalse(result);
    }
}