package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link CollectionOfPublicationsAuthorizationPolicy}.
 */
@ExtendWith(MockitoExtension.class)
class CollectionOfPublicationsAuthorizationPolicyTest {

    @InjectMocks
    private CollectionOfPublicationsAuthorizationPolicy policy;

    // -------------------------------------------------------------------------
    // canCreateCollectionOfPublications
    // -------------------------------------------------------------------------

    @Test
    void canCreateCollectionOfPublications_shouldReturnTrue_whenUserHasNormalUserRole() {
        // ARRANGE
        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // ACT
        boolean result = policy.canCreateCollectionOfPublications(user);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void canCreateCollectionOfPublications_shouldReturnFalse_whenUserDoesNotHaveNormalUserRole() {
        // ARRANGE
        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // ACT
        boolean result = policy.canCreateCollectionOfPublications(user);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void canCreateCollectionOfPublications_shouldReturnFalse_whenUserHasNoRoles() {
        // ARRANGE
        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of());

        // ACT
        boolean result = policy.canCreateCollectionOfPublications(user);

        // ASSERT
        assertFalse(result);
    }

    // -------------------------------------------------------------------------
    // canShareCollectionOfPublications
    // -------------------------------------------------------------------------

    @Test
    void canShareCollectionOfPublications_shouldReturnTrue_whenUserHasNormalUserRole() {
        // ARRANGE
        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // ACT
        boolean result = policy.canShareCollectionOfPublications(user);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void canShareCollectionOfPublications_shouldReturnFalse_whenUserDoesNotHaveNormalUserRole() {
        // ARRANGE
        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // ACT
        boolean result = policy.canShareCollectionOfPublications(user);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void canShareCollectionOfPublications_shouldReturnFalse_whenUserHasNoRoles() {
        // ARRANGE
        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of());

        // ACT
        boolean result = policy.canShareCollectionOfPublications(user);

        // ASSERT
        assertFalse(result);
    }

    // -------------------------------------------------------------------------
    // canMakeCollectionPublicUntil
    // -------------------------------------------------------------------------

    @Test
    void canMakeCollectionPublicUntil_shouldReturnTrue_whenUserHasNormalUserRole() {
        // ARRANGE
        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // ACT
        boolean result = policy.canMakeCollectionPublicUntil(user);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void canMakeCollectionPublicUntil_shouldReturnFalse_whenUserDoesNotHaveNormalUserRole() {
        // ARRANGE
        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // ACT
        boolean result = policy.canMakeCollectionPublicUntil(user);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void canMakeCollectionPublicUntil_shouldReturnFalse_whenUserHasNoRoles() {
        // ARRANGE
        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of());

        // ACT
        boolean result = policy.canMakeCollectionPublicUntil(user);

        // ASSERT
        assertFalse(result);
    }


    // -------------------------------------------------------------------------
    // canViewCollectionOfPublications
    // -------------------------------------------------------------------------

    @Test
    void canViewCollectionOfPublications_shouldReturnTrue_whenUserHasNormalUserRole() {
        // ARRANGE
        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // ACT
        boolean result = policy.canViewCollectionOfPublications(user);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void canViewCollectionOfPublications_shouldReturnFalse_whenUserDoesNotHaveNormalUserRole() {
        // ARRANGE
        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // ACT
        boolean result = policy.canViewCollectionOfPublications(user);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void canViewCollectionOfPublications_shouldReturnFalse_whenUserHasNoRoles() {
        // ARRANGE
        User user = mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of());

        // ACT
        boolean result = policy.canViewCollectionOfPublications(user);

        // ASSERT
        assertFalse(result);
    }
}