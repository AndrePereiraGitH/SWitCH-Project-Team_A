package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link GenreAuthorizationPolicy}.
 */
class GenreAuthorizationPolicyTest {

    @Test
    void canCreateGenre_shouldReturnTrue_whenUserIsAdmin() {
        // ARRANGE
        GenreAuthorizationPolicy sut = new GenreAuthorizationPolicy();
        User user = mock(User.class);

        when(user.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // ACT
        boolean result = sut.canCreateGenre(user);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void canCreateGenre_shouldReturnTrue_whenUserHasMultipleRolesIncludingAdmin() {

        // ARRANGE
        GenreAuthorizationPolicy sut =
                new GenreAuthorizationPolicy();

        User user = mock(User.class);

        when(user.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN, UserRole.NORMAL_USER));

        // ACT
        boolean result = sut.canCreateGenre(user);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void canCreateGenre_shouldReturnFalse_whenUserIsNotAdmin() {
        // ARRANGE
        GenreAuthorizationPolicy sut = new GenreAuthorizationPolicy();
        User user = mock(User.class);

        when(user.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // ACT
        boolean result = sut.canCreateGenre(user);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void canCreateGenre_shouldReturnFalse_whenUserHasNoRoles() {
        // ARRANGE
        GenreAuthorizationPolicy sut = new GenreAuthorizationPolicy();
        User user = mock(User.class);

        when(user.getUserRoles()).thenReturn(Set.of());

        // ACT
        boolean result = sut.canCreateGenre(user);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void canCreateGenre_shouldReturnConsistentResult_whenCalledTwice() {
        // ARRANGE
        GenreAuthorizationPolicy sut = new GenreAuthorizationPolicy();
        User user = mock(User.class);

        when(user.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // ACT
        boolean first = sut.canCreateGenre(user);
        boolean second = sut.canCreateGenre(user);

        // ASSERT
        assertTrue(first);
        assertEquals(first, second);
    }
}