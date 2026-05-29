package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.CountryAuthorizationPolicy;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CountryLinkProvider}.
 *
 * <p>Verifies that HATEOAS links are correctly generated
 * based on user authorization rules.</p>
 */
@ExtendWith(MockitoExtension.class)
class CountryLinkProviderTest {

    @Mock
    private CountryAuthorizationPolicy policy;

    @Mock
    private IUserRepository repository;

    @InjectMocks
    private CountryLinkProvider provider;

    // -------------------------------------------------------------------------
    // ADMIN USER
    // -------------------------------------------------------------------------

    @Test
    void shouldReturnTwoLinks_whenUserHasPermission() {

        // ARRANGE
        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canRegisterCountry(user)).thenReturn(true);

        // ACT
        int result = provider.getLinks(userId).size();

        // ASSERT
        assertEquals(2, result);
    }

    // -------------------------------------------------------------------------
    // NORMAL USER
    // -------------------------------------------------------------------------

    @Test
    void shouldReturnOnlyDefaultLink_whenUserHasNoPermission() {

        // ARRANGE
        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canRegisterCountry(user)).thenReturn(false);

        // ACT
        int result = provider.getLinks(userId).size();

        // ASSERT
        assertEquals(1, result);
    }

    // -------------------------------------------------------------------------
    // USER NOT FOUND
    // -------------------------------------------------------------------------

    @Test
    void shouldThrowException_whenUserDoesNotExist() {

        // ARRANGE
        UserId userId = mock(UserId.class);
        when(repository.findByID(userId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> provider.getLinks(userId));
    }
}