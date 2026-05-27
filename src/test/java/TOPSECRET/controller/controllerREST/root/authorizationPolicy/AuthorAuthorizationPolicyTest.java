package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AuthorAuthorizationPolicy}.
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
    void canCreateAuthor_shouldReturnFalse_whenUserIsNotAdmin() {
        // ARRANGE
        AuthorAuthorizationPolicy sut = new AuthorAuthorizationPolicy();
        User user = mock(User.class);

        when(user.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // ACT
        boolean result = sut.canCreateAuthor(user);

        // ASSERT
        assertFalse(result);
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