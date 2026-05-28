package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link PublicationTypeAuthorizationPolicy}.
 *
 * <p>
 * Validates authorization rules related to PublicationType operations.
 * </p>
 *
 * <p>
 * Specifically verifies whether users with different roles are correctly
 * granted or denied permission to create PublicationType entities.
 * </p>
 */
class PublicationTypeAuthorizationPolicyTest {

    private PublicationTypeAuthorizationPolicy policy;

    @Test
    void shouldAllowAdminToCreatePublicationType() {

        // ARRANGE
        policy = new PublicationTypeAuthorizationPolicy();

        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // ACT
        boolean result = policy.canCreatePublicationType(user);

        // ASSERT
        assertTrue(result);

    }

    @Test

    void shouldDenyNonAdminToCreatePublicationType() {

        // ARRANGE
        policy = new PublicationTypeAuthorizationPolicy();

        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // ACT
        boolean result = policy.canCreatePublicationType(user);

        // ASSERT
        assertFalse(result);

    }

}