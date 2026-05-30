package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AuthorAuthorizationPolicy}.
 *
 * <p>This class verifies the behaviour of the authorization policy
 * responsible for determining whether a user is allowed to create
 * an Author.</p>
 *
 * <p>The following scenarios are covered:</p>
 * <ul>
 *   <li>Users with the {@code ADMIN} role are granted permission</li>
 *   <li>Users with the {@code NORMAL_USER} role are granted permission</li>
 *   <li>Users with both roles are granted permission</li>
 *   <li>Users with no roles are denied permission</li>
 *   <li>The result is consistent across multiple calls</li>
 * </ul>
 */
class AuthorAuthorizationPolicyTest {

    @Test
    void canCreateAuthor_shouldReturnTrue_whenUserIsAdmin() {
        // ARRANGE
        AuthorAuthorizationPolicy sut = new AuthorAuthorizationPolicy();
        User user = mock(User.class);

        when(user.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // ACT
        boolean result = sut.canCreateAuthor(user);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void canCreateAuthor_shouldReturnTrue_whenUserIsNormalUser() {
        // ARRANGE
        AuthorAuthorizationPolicy sut = new AuthorAuthorizationPolicy();
        User user = mock(User.class);

        when(user.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // ACT
        boolean result = sut.canCreateAuthor(user);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void canCreateAuthor_shouldReturnTrue_whenUserHasMultipleRolesIncludingAdmin() {
        // ARRANGE
        AuthorAuthorizationPolicy sut = new AuthorAuthorizationPolicy();
        User user = mock(User.class);

        when(user.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN, UserRole.NORMAL_USER));

        // ACT
        boolean result = sut.canCreateAuthor(user);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void canCreateAuthor_shouldReturnFalse_whenUserHasNoRoles() {
        // ARRANGE
        AuthorAuthorizationPolicy sut = new AuthorAuthorizationPolicy();
        User user = mock(User.class);

        when(user.getUserRoles()).thenReturn(Set.of());

        // ACT
        boolean result = sut.canCreateAuthor(user);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void canCreateAuthor_shouldReturnConsistentResult_whenCalledTwice() {
        // ARRANGE
        AuthorAuthorizationPolicy sut = new AuthorAuthorizationPolicy();
        User user = mock(User.class);

        when(user.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // ACT
        boolean first = sut.canCreateAuthor(user);
        boolean second = sut.canCreateAuthor(user);

        // ASSERT
        assertTrue(first);
        assertEquals(first, second);
    }
}