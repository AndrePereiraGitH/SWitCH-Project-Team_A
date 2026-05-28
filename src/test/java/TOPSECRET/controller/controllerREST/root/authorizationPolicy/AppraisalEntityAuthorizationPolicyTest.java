package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AppraisalEntityAuthorizationPolicy}.
 *
 * <p>Validates authorization rules related to AppraisalEntity operations.</p>
 *
 * <p>The policy allows registration only for users with ADMIN role.</p>
 */
class AppraisalEntityAuthorizationPolicyTest {

    @Test
    void shouldReturnTrueWhenUserHasAdminRole() {

        // ARRANGE
        AppraisalEntityAuthorizationPolicy sut = new AppraisalEntityAuthorizationPolicy();

        User user = mock(User.class);

        when(user.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // ACT
        boolean result =
                sut.canRegisterAppraisalEntity(user);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenUserDoesNotHaveAdminRole() {

        // ARRANGE
        AppraisalEntityAuthorizationPolicy sut = new AppraisalEntityAuthorizationPolicy();

        User user = mock(User.class);

        when(user.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // ACT
        boolean result =
                sut.canRegisterAppraisalEntity(user);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenUserHasNoRoles() {

        // ARRANGE
        AppraisalEntityAuthorizationPolicy sut = new AppraisalEntityAuthorizationPolicy();

        User user = mock(User.class);

        when(user.getUserRoles()).thenReturn(Set.of());

        // ACT
        boolean result = sut.canRegisterAppraisalEntity(user);

        // ASSERT
        assertFalse(result);
    }
}