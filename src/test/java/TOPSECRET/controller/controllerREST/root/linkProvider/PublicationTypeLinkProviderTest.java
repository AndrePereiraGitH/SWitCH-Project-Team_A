package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.PublicationTypeAuthorizationPolicy;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PublicationTypeLinkProvider}.
 */
class PublicationTypeLinkProviderTest {

    private PublicationTypeAuthorizationPolicy policy;
    private IUserRepository userRepository;
    private PublicationTypeLinkProvider provider;

    @BeforeEach
    void setUp() {

        policy = mock(PublicationTypeAuthorizationPolicy.class);
        userRepository = mock(IUserRepository.class);
    }

    @Test
    void shouldReturnPublicationTypeLinksForAdminUser() {

        // ARRANGE

        provider = new PublicationTypeLinkProvider(
                policy,
                userRepository
        );

        UserId userId = mock(UserId.class);

        User user = mock(User.class);

        when(userRepository.findByID(userId))
                .thenReturn(Optional.of(user));

        when(policy.canCreatePublicationType(user))
                .thenReturn(true);

        // ACT
        List<Link> links =
                provider.getLinksForPublicationType(userId);

        // ASSERT
        assertNotNull(links);
        assertEquals(1, links.size());
        assertEquals(
                "create-publication-type",
                links.get(0).getRel().value()
        );
    }

    @Test
    void shouldReturnEmptyListWhenUserHasNoPermission() {

        // ARRANGE

        provider = new PublicationTypeLinkProvider(
                policy,
                userRepository
        );

        UserId userId = mock(UserId.class);

        User user = mock(User.class);

        when(userRepository.findByID(userId))
                .thenReturn(Optional.of(user));

        when(policy.canCreatePublicationType(user))
                .thenReturn(false);

        // ACT
        List<Link> links =
                provider.getLinksForPublicationType(userId);

        // ASSERT
        assertNotNull(links);
        assertTrue(links.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        // ARRANGE

        provider = new PublicationTypeLinkProvider(
                policy,
                userRepository
        );

        UserId userId = mock(UserId.class);

        when(userRepository.findByID(userId))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> provider.getLinksForPublicationType(userId)
        );
    }
}