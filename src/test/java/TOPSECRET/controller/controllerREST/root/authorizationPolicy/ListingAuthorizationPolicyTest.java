package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListingAuthorizationPolicyTest {
    private final ListingAuthorizationPolicy policy = new ListingAuthorizationPolicy();

    @Mock
    private User user;

    @Test
    void canCreateListing_shouldReturnTrue_whenUserHasNormalUserRole() {
        // arrange
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // act
        boolean result = policy.canCreateListing(user);

        // assert
        assertTrue(result);
    }

    @Test
    void canCreateListing_shouldReturnFalse_whenUserDoesNotHaveNormalUserRole() {
        // arrange
        when(user.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // act
        boolean result = policy.canCreateListing(user);

        // assert
        assertFalse(result);
    }

    @Test
    void canCreateListing_shouldReturnFalse_whenUserHasNoRoles() {
        // arrange
        when(user.getUserRoles()).thenReturn(Set.of());

        // act
        boolean result = policy.canCreateListing(user);

        // assert
        assertFalse(result);
    }

}