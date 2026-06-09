package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link EditionAuthorizationPolicy}.
 *
 * <p>No changes required for Sprint 7 — policy logic is unchanged.</p>
 */
class EditionAuthorizationPolicyTest {

    @Test
    void canCreateEdition_shouldReturnTrue_whenUserHasOnlyAdminRole() {

        // ARRANGE
        EditionAuthorizationPolicy sut = new EditionAuthorizationPolicy();
        User user = mock(User.class);

        when(user.getUserRoles())
                .thenReturn(Set.of(UserRole.ADMIN));

        // ACT
        boolean result = sut.canCreateEdition(user);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void canCreateEdition_shouldReturnTrue_whenUserHasOnlyNormalUserRole() {

        // ARRANGE
        EditionAuthorizationPolicy sut = new EditionAuthorizationPolicy();
        User user = mock(User.class);

        when(user.getUserRoles())
                .thenReturn(Set.of(UserRole.NORMAL_USER));

        // ACT
        boolean result = sut.canCreateEdition(user);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void canCreateEdition_shouldReturnFalse_whenUserHasNoRoles() {

        // ARRANGE
        EditionAuthorizationPolicy sut = new EditionAuthorizationPolicy();
        User user = mock(User.class);

        when(user.getUserRoles())
                .thenReturn(Set.of());

        // ACT
        boolean result = sut.canCreateEdition(user);

        // ASSERT
        assertFalse(result);
    }

    // =========================================================
   // canFindByPublisherId
   // =========================================================

    @Test
    void canFindByPublisherId_shouldReturnTrue_whenUserIsNormalUser() {

        // ARRANGE
        EditionAuthorizationPolicy sut = new EditionAuthorizationPolicy();
        User user = mock(User.class);

        when(user.getUserRoles())
                .thenReturn(Set.of(UserRole.NORMAL_USER));

        // ACT
        boolean result = sut.canFindByPublisherId(user);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void canFindByPublisherId_shouldReturnFalse_whenUserIsAdmin() {

        // ARRANGE
        EditionAuthorizationPolicy sut = new EditionAuthorizationPolicy();
        User user = mock(User.class);

        when(user.getUserRoles())
                .thenReturn(Set.of(UserRole.ADMIN));

        // ACT
        boolean result = sut.canFindByPublisherId(user);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void canFindByPublisherId_shouldReturnFalse_whenUserHasNoRoles() {

        // ARRANGE
        EditionAuthorizationPolicy sut = new EditionAuthorizationPolicy();
        User user = mock(User.class);

        when(user.getUserRoles())
                .thenReturn(Set.of());

        // ACT
        boolean result = sut.canFindByPublisherId(user);

        // ASSERT
        assertFalse(result);
    }
    // =========================================================
// canFindByPublicationTypeId
// =========================================================

    @Test
    void canFindByPublicationTypeId_shouldReturnTrue_whenUserIsNormalUser() {

        // ARRANGE
        EditionAuthorizationPolicy sut = new EditionAuthorizationPolicy();
        User user = mock(User.class);

        when(user.getUserRoles())
                .thenReturn(Set.of(UserRole.NORMAL_USER));

        // ACT
        boolean result = sut.canFindByPublicationTypeId(user);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void canFindByPublicationTypeId_shouldReturnFalse_whenUserIsAdmin() {

        // ARRANGE
        EditionAuthorizationPolicy sut = new EditionAuthorizationPolicy();
        User user = mock(User.class);

        when(user.getUserRoles())
                .thenReturn(Set.of(UserRole.ADMIN));

        // ACT
        boolean result = sut.canFindByPublicationTypeId(user);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void canFindByPublicationTypeId_shouldReturnFalse_whenUserHasNoRoles() {

        // ARRANGE
        EditionAuthorizationPolicy sut = new EditionAuthorizationPolicy();
        User user = mock(User.class);

        when(user.getUserRoles())
                .thenReturn(Set.of());

        // ACT
        boolean result = sut.canFindByPublicationTypeId(user);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void canFindByPublisherId_shouldReturnTrue_whenUserHasMultipleRoles() {

        // ARRANGE
        EditionAuthorizationPolicy sut = new EditionAuthorizationPolicy();
        User user = mock(User.class);

        when(user.getUserRoles())
                .thenReturn(Set.of(UserRole.ADMIN, UserRole.NORMAL_USER));

        // ACT
        boolean result = sut.canFindByPublisherId(user);

        // ASSERT
        assertTrue(result);
    }
}
