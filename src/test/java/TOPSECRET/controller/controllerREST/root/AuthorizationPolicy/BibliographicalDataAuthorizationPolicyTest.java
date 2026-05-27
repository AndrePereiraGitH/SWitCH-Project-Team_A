package TOPSECRET.controller.controllerREST.root.AuthorizationPolicy;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.BibliographicalDataAuthorizationPolicy;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BibliographicalDataAuthorizationPolicyTest {

    @Mock
    private User user;

    @InjectMocks
    private BibliographicalDataAuthorizationPolicy policy;

    @Test
    void canCreateBibliographicalDataWhenUserHasNormalUserRole() {
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        boolean result = policy.canCreateBibliographicalData(user);

        assertTrue(result);
    }

    @Test
    void cannotCreateBibliographicalDataWhenUserDoesNotHaveNormalUserRole() {
        when(user.getUserRoles())
                .thenReturn(Set.of(UserRole.ADMIN));

        boolean result = policy.canCreateBibliographicalData(user);

        assertFalse(result);
    }

    @Test
    void canCreateBibliographicalDataWhenUserHasMultipleRolesIncludingNormalUser() {
        when(user.getUserRoles())
                .thenReturn(Set.of(UserRole.ADMIN, UserRole.NORMAL_USER));

        boolean result = policy.canCreateBibliographicalData(user);

        assertTrue(result);
    }

    @Test
    void cannotCreateBibliographicalDataWhenUserHasNoRoles() {
        when(user.getUserRoles()).thenReturn(Collections.emptySet());

        boolean result = policy.canCreateBibliographicalData(user);

        assertFalse(result);
    }
}
