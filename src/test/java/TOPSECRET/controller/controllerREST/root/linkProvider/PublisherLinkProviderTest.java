package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.PublisherAuthorizationPolicy;
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
 * Unit tests for {@link PublisherLinkProvider}.
 */
@ExtendWith(MockitoExtension.class)
class PublisherLinkProviderTest {

    @Mock
    private PublisherAuthorizationPolicy authPolicy;

    @Mock
    private IUserRepository userRepo;

    @Mock
    private UserId userId;

    @Mock
    private User user;

    @InjectMocks
    private PublisherLinkProvider provider;

    // =========================================================================
    // getPublicLinks
    // =========================================================================

    @Test
    void shouldAlwaysReturnTwoPublicLinks() {
        List<Link> links = provider.getPublicLinks();

        assertEquals(2, links.size());
        assertTrue(hasRel(links, "get-all-publishers"));
        assertTrue(hasRel(links, "get-publisher"));
    }

    // =========================================================================
    // getAdminLinks
    // =========================================================================

    @Test
    void shouldReturnCreatePublisherLink_whenUserIsAdmin() throws InstanceNotFoundException {

        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreatePublisher(user)).thenReturn(true);

        List<Link> links = provider.getAdminLinks(userId);

        assertEquals(1, links.size());
        assertTrue(hasRel(links, "create-publisher"));
    }

    @Test
    void shouldReturnEmptyList_whenUserIsNotAdmin() throws InstanceNotFoundException {

        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreatePublisher(user)).thenReturn(false);

        List<Link> links = provider.getAdminLinks(userId);

        assertTrue(links.isEmpty());
    }

    @Test
    void shouldThrowInstanceNotFoundException_whenUserDoesNotExist() {

        when(userRepo.findByID(userId)).thenReturn(Optional.empty());

        assertThrows(InstanceNotFoundException.class,
                () -> provider.getAdminLinks(userId));
    }

    @Test
    void shouldThrowNullPointerException_whenUserIdIsNull() {

        assertThrows(NullPointerException.class,
                () -> provider.getAdminLinks(null));
    }

    // =========================================================================
    // getLinks
    // =========================================================================

    @Test
    void shouldReturnAllLinks_whenUserIsAdmin() throws InstanceNotFoundException {

        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreatePublisher(user)).thenReturn(true);

        List<Link> links = provider.getLinks(userId);

        assertEquals(3, links.size());
        assertTrue(hasRel(links, "get-all-publishers"));
        assertTrue(hasRel(links, "get-publisher"));
        assertTrue(hasRel(links, "create-publisher"));
    }

    @Test
    void shouldReturnOnlyPublicLinks_whenUserIsNotAdmin() throws InstanceNotFoundException {

        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreatePublisher(user)).thenReturn(false);

        List<Link> links = provider.getLinks(userId);

        assertEquals(2, links.size());
        assertTrue(hasRel(links, "get-all-publishers"));
        assertTrue(hasRel(links, "get-publisher"));
        assertFalse(hasRel(links, "create-publisher"));
    }

    @Test
    void shouldThrowInstanceNotFoundException_whenUserDoesNotExistOnGetLinks() {

        when(userRepo.findByID(userId)).thenReturn(Optional.empty());

        assertThrows(InstanceNotFoundException.class,
                () -> provider.getLinks(userId));
    }

    // =========================================================================
    // Private helper
    // =========================================================================

    private boolean hasRel(List<Link> links, String rel) {
        return links.stream()
                .anyMatch(link -> link.getRel().value().equals(rel));
    }
}