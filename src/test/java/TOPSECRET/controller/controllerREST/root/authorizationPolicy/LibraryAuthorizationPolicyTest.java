package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class LibraryAuthorizationPolicyTest {

    //-------can Create Library-----------
    @Test
    void shouldReturnTrueWhenUserHasNormalUserRole() {
        // Arrange
        LibraryAuthorizationPolicy policy = new LibraryAuthorizationPolicy();

        User user = Mockito.mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // Act &  Assert
        assertTrue(policy.canCreateLibrary(user));
    }

    @Test
    void shouldReturnFalseWhenUserHasDifferentRole() {
        // Arrange
        LibraryAuthorizationPolicy policy = new LibraryAuthorizationPolicy();

        User user = Mockito.mock(User.class);

        when(user.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // Act &  Assert
        assertFalse(policy.canCreateLibrary(user));
    }

    @Test
    void shouldThrowNullPointerExceptionWhenUserIsNull() {
        LibraryAuthorizationPolicy policy = new LibraryAuthorizationPolicy();

        assertThrows(NullPointerException.class,
                () -> policy.canCreateLibrary(null));
    }

    //-------can get all Libraries-----------
    @Test
    void canGetAllShouldReturnTrueWhenUserHasNormalUserRole() {
        // Arrange
        LibraryAuthorizationPolicy policy = new LibraryAuthorizationPolicy();

        User user = Mockito.mock(User.class);
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // Act &  Assert
        assertTrue(policy.canGetAllLibrary(user));
    }

    @Test
    void canGetAllShouldReturnFalseWhenUserHasDifferentRole() {
        // Arrange
        LibraryAuthorizationPolicy policy = new LibraryAuthorizationPolicy();

        User user = Mockito.mock(User.class);

        when(user.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // Act &  Assert
        assertFalse(policy.canGetAllLibrary(user));
    }

    @Test
    void canGetAllShouldThrowNullPointerExceptionWhenUserIsNull() {
        LibraryAuthorizationPolicy policy = new LibraryAuthorizationPolicy();

        assertThrows(NullPointerException.class,
                () -> policy.canGetAllLibrary(null));
    }
}