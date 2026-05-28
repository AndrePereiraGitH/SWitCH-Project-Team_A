package TOPSECRET.controller.controllerREST.root.AuthorizationPolicy;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.BibliographicalDataAuthorizationPolicy;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BibliographicalDataAuthorizationPolicyTest {

    @Test
    void canCreateBibliographicalDataWhenUserHasNormalUserRole() {
        // Arrange
        User user = mock(User.class);
        BibliographicalDataAuthorizationPolicy policy = new BibliographicalDataAuthorizationPolicy();

        when(user.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // Act
        boolean result = policy.canCreateBibliographicalData(user);

        // Assert
        assertTrue(result);
    }

    @Test
    void cannotCreateBibliographicalDataWhenUserDoesNotHaveNormalUserRole() {
        // Arrange
        User user = mock(User.class);
        BibliographicalDataAuthorizationPolicy policy = new BibliographicalDataAuthorizationPolicy();

        when(user.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // Act
        boolean result = policy.canCreateBibliographicalData(user);

        // Assert
        assertFalse(result);
    }

    @Test
    void canCreateBibliographicalDataWhenUserHasMultipleRolesIncludingNormalUser() {
        // Arrange
        User user = mock(User.class);
        BibliographicalDataAuthorizationPolicy policy = new BibliographicalDataAuthorizationPolicy();

        when(user.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN, UserRole.NORMAL_USER));

        // Act
        boolean result = policy.canCreateBibliographicalData(user);

        // Assert
        assertTrue(result);
    }

    @Test
    void cannotCreateBibliographicalDataWhenUserHasNoRoles() {
        // Arrange
        User user = mock(User.class);
        BibliographicalDataAuthorizationPolicy policy = new BibliographicalDataAuthorizationPolicy();

        when(user.getUserRoles()).thenReturn(Collections.emptySet());

        // Act
        boolean result = policy.canCreateBibliographicalData(user);

        // Assert
        assertFalse(result);
    }
}
