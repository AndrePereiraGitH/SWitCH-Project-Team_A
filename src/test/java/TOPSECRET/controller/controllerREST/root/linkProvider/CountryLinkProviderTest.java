package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.CountryAuthorizationPolicy;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Optional;

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

    @Mock
    private User user;

    @Mock
    private UserId userId;

    @InjectMocks
    private CountryLinkProvider provider;

    // -------------------------------------------------------------------------
    // ADMIN USER
    // -------------------------------------------------------------------------

    @Test
    void shouldReturnTwoLinks_whenUserHasPermission() throws InstanceNotFoundException{

        // ARRANGE
        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canRegisterCountry(user)).thenReturn(true);

        // ACT
        List<Link> result = provider.getLinks(userId);

        // ASSERT
        assertEquals(3, result.size());

        assertTrue(result.stream()
                .anyMatch(l -> l.getRel().value().equals("get-all-countries")));

        assertTrue(result.stream()
                .anyMatch(l -> l.getRel().value().equals("register-country")));
    }

    // -------------------------------------------------------------------------
    // NORMAL USER
    // -------------------------------------------------------------------------

    @Test
    void shouldReturnOnlyDefaultLink_whenUserHasNoPermission() throws InstanceNotFoundException{

        // ARRANGE
        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canRegisterCountry(user)).thenReturn(false);

        // ACT
        List<Link> result = provider.getLinks(userId);

        //ASSERT
        assertEquals(2, result.size());

        assertTrue(result.stream()
                .anyMatch(l -> l.getRel().value().equals("get-all-countries")));
    }

    // -------------------------------------------------------------------------
    // USER NOT FOUND
    // -------------------------------------------------------------------------

    @Test
    void shouldThrowException_whenUserDoesNotExist() {

        // ARRANGE
        when(repository.findByID(userId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(InstanceNotFoundException.class,
                () -> provider.getLinks(userId));
    }
}