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
    void shouldReturnCreateLinkForAdminUser() {

        // ARRANGE
        provider = new PublicationTypeLinkProvider(
                policy,
                userRepository
        );

        UserId userIdDouble = mock(UserId.class);
        User userDouble = mock(User.class);

        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(userDouble));
        when(policy.canCreatePublicationType(userDouble)).thenReturn(true);

        // ACT
        List<Link> links =
                provider.getCreateLinks(userIdDouble);

        // ASSERT
        assertEquals(1, links.size());
        assertEquals("create-publication-type",
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

        UserId userIdDouble = mock(UserId.class);
        User userDouble = mock(User.class);

        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(userDouble));
        when(policy.canCreatePublicationType(userDouble)).thenReturn(false);

        // ACT
        List<Link> links =
                provider.getCreateLinks(userIdDouble);

        // ASSERT
        assertTrue(links.isEmpty());
    }

    @Test
    void shouldReturnPublicLinks() {

        // ARRANGE
        provider = new PublicationTypeLinkProvider(
                policy,
                userRepository
        );
        // ACT
        List<Link> links =
                provider.getPublicLinks();

        // ASSERT
        assertEquals(1, links.size());

        assertEquals(
                "get-all-publication-types",
                links.get(0).getRel().value()
        );
    }

    @Test
    void shouldReturnPublicAndCreateLinksWhenUserHasPermission() {

        // ARRANGE

        provider = new PublicationTypeLinkProvider(
                policy,
                userRepository
        );

        UserId userIdDouble = mock(UserId.class);
        User userDouble = mock(User.class);

        when(userRepository.findByID(userIdDouble))
                .thenReturn(Optional.of(userDouble));

        when(policy.canCreatePublicationType(userDouble))
                .thenReturn(true);

        // ACT
        List<Link> links = provider.getLinks(userIdDouble);

        // ASSERT
        assertEquals(2, links.size());

        assertTrue(
                links.stream()
                        .anyMatch(link ->
                                link.getRel().value()
                                        .equals("get-all-publication-types"))
        );

        assertTrue(
                links.stream()
                        .anyMatch(link ->
                                link.getRel().value()
                                        .equals("create-publication-type"))
        );
    }

    @Test
    void shouldReturnOnlyPublicLinksWhenUserHasNoPermission() {

        // ARRANGE
        provider = new PublicationTypeLinkProvider(
                policy,
                userRepository
        );

        UserId userIdDouble = mock(UserId.class);
        User userDouble = mock(User.class);

        when(userRepository.findByID(userIdDouble))
                .thenReturn(Optional.of(userDouble));

        when(policy.canCreatePublicationType(userDouble))
                .thenReturn(false);

        // ACT
        List<Link> links = provider.getLinks(userIdDouble);

        // ASSERT
        assertEquals(1, links.size());
        assertTrue(
                links.stream()
                        .anyMatch(link ->
                                link.getRel().value()
                                        .equals("get-all-publication-types"))
        );

        assertFalse(
                links.stream()
                        .anyMatch(link ->
                                link.getRel().value()
                                        .equals("create-publication-type"))
        );
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsNull() {

        // ARRANGE
        provider = new PublicationTypeLinkProvider(
                policy,
                userRepository
        );

        // ACT + ASSERT
        assertThrows(
                NullPointerException.class,
                () -> provider.getLinks(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        // ARRANGE

        provider = new PublicationTypeLinkProvider(
                policy,
                userRepository
        );

        UserId userIdDouble = mock(UserId.class);

        when(userRepository.findByID(userIdDouble))
                .thenReturn(Optional.empty());

        // ACT + ASSERT

        assertThrows(
                IllegalArgumentException.class,
                () -> provider.getCreateLinks(userIdDouble)
        );
    }

    @Test
    void shouldThrowExceptionWhenCreateLinksReceivesNullUserId() {

        // ARRANGE

        provider = new PublicationTypeLinkProvider(
                policy,
                userRepository
        );

        // ACT + ASSERT

        assertThrows(
                NullPointerException.class,
                () -> provider.getCreateLinks(null)
        );
    }




}