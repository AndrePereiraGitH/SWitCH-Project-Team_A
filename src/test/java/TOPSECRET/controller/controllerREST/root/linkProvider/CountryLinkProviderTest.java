package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.CountryAuthorizationPolicy;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
class CountryLinkProviderTest {

    private CountryAuthorizationPolicy policy;
    private IUserRepository repository;
    private CountryLinkProvider provider;

    private User adminUser;
    private User normalUser;

    private UserId adminUserId;
    private UserId normalUserId;

    @BeforeEach
    void setup() {

        // Arrange
        policy = new CountryAuthorizationPolicy();
        repository = mock(IUserRepository.class);

        provider = new CountryLinkProvider(policy, repository);

        adminUser = mock(User.class);
        normalUser = mock(User.class);

        adminUserId = new UserId("admin-id");
        normalUserId = new UserId("normal-id");

        when(adminUser.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));
        when(normalUser.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));
    }

    // -------------------------------------------------------------------------
    // ADMIN USER
    // -------------------------------------------------------------------------

    @Test
    void adminUser_returnsRegisterCountryLink() {

        // Arrange
        when(repository.findByID(adminUserId)).thenReturn(Optional.of(adminUser));

        // Act
        var links = provider.getLinks(adminUserId);

        // Assert
        assertNotNull(links);

        boolean hasRegisterLink = links.stream()
                .anyMatch(l -> l.getRel().value().equals("register-country"));

        assertTrue(hasRegisterLink);
    }

    // -------------------------------------------------------------------------
    // NORMAL USER
    // -------------------------------------------------------------------------

    @Test
    void normalUser_doesNotReturnRegisterCountryLink() {

        // Arrange
        when(repository.findByID(normalUserId)).thenReturn(Optional.of(normalUser));

        // Act
        var links = provider.getLinks(normalUserId);

        // Assert
        assertNotNull(links);

        boolean hasRegisterLink = links.stream()
                .anyMatch(l -> l.getRel().value().equals("register-country"));

        assertFalse(hasRegisterLink);
    }

    // -------------------------------------------------------------------------
    // USER NOT FOUND
    // -------------------------------------------------------------------------

    @Test
    void userNotFound_throwsException() {

        // Arrange
        when(repository.findByID(adminUserId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> provider.getLinks(adminUserId));
    }
}