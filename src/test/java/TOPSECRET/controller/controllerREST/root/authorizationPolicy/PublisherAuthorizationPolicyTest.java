package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PublisherAuthorizationPolicy}.
 */
@ExtendWith(MockitoExtension.class)
class PublisherAuthorizationPolicyTest {

    @InjectMocks
    private PublisherAuthorizationPolicy policy;

    @Test
    void canCreatePublisher_shouldReturnTrue_whenUserHasAdminRole() {

        // ARRANGE
        User user = mock(User.class);

        when(user.getUserRoles())
                .thenReturn(Set.of(UserRole.ADMIN));

        // ACT
        boolean result = policy.canCreatePublisher(user);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void canCreatePublisher_shouldReturnFalse_whenUserDoesNotHaveAdminRole() {

        // ARRANGE
        User user = mock(User.class);

        when(user.getUserRoles())
                .thenReturn(Set.of(UserRole.NORMAL_USER));

        // ACT
        boolean result = policy.canCreatePublisher(user);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void canCreatePublisher_shouldReturnFalse_whenUserHasNoRoles() {

        // ARRANGE
        User user = mock(User.class);

        when(user.getUserRoles())
                .thenReturn(Set.of());

        // ACT
        boolean result = policy.canCreatePublisher(user);

        // ASSERT
        assertFalse(result);
    }
}