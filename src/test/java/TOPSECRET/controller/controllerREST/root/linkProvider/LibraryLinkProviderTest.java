package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.LibraryAuthorizationPolicy;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LibraryLinkProviderTest {

    @Mock
    private IUserRepository userRepo;

    @Mock
    private LibraryAuthorizationPolicy authPolicy;

    @Mock
    private UserId userId;

    @Mock
    private User user;

    @InjectMocks
    private LibraryLinkProvider linkProvider;

    // =========================================================================
    // getOwnerLinks
    // =========================================================================

    @Test
    void shouldReturnCreateAndGetLibraryLinks_whenUserCanCreateAndGetLibrary() throws InstanceNotFoundException {

        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateLibrary(user)).thenReturn(true);
        when(authPolicy.canGetLibrary(user, userId)).thenReturn(true);

        List<Link> links = linkProvider.getOwnerLinks(userId);

        assertEquals(3, links.size());
        assertTrue(hasRel(links, "create-library"));
        assertTrue(hasRel(links, "get-library"));
        assertTrue(hasRel(links, "get-library-by-user"));
    }

    @Test
    void shouldReturnOnlyCreateLink_whenUserCannotGetLibrary() throws InstanceNotFoundException {

        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateLibrary(user)).thenReturn(true);
        when(authPolicy.canGetLibrary(user, userId)).thenReturn(false);

        List<Link> links = linkProvider.getOwnerLinks(userId);

        assertEquals(1, links.size());
        assertTrue(hasRel(links, "create-library"));
        assertFalse(hasRel(links, "get-library"));
        assertFalse(hasRel(links, "get-library-by-user"));
    }

    @Test
    void shouldReturnOnlyGetLibraryLinks_whenUserCannotCreate() throws InstanceNotFoundException {

        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateLibrary(user)).thenReturn(false);
        when(authPolicy.canGetLibrary(user, userId)).thenReturn(true);

        List<Link> links = linkProvider.getOwnerLinks(userId);

        assertEquals(2, links.size());
        assertFalse(hasRel(links, "create-library"));
        assertTrue(hasRel(links, "get-library"));
        assertTrue(hasRel(links, "get-library-by-user"));
    }

    @Test
    void shouldReturnEmptyList_whenUserHasNoOwnerPermissions() throws InstanceNotFoundException {

        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateLibrary(user)).thenReturn(false);
        when(authPolicy.canGetLibrary(user, userId)).thenReturn(false);

        List<Link> links = linkProvider.getOwnerLinks(userId);

        assertTrue(links.isEmpty());
    }

    @Test
    void shouldThrowInstanceNotFoundException_whenUserDoesNotExistOnOwnerLinks() {

        when(userRepo.findByID(userId)).thenReturn(Optional.empty());

        assertThrows(InstanceNotFoundException.class,
                () -> linkProvider.getOwnerLinks(userId));
    }

    @Test
    void shouldThrowNullPointerException_whenUserIdIsNullOnOwnerLinks() {

        assertThrows(NullPointerException.class,
                () -> linkProvider.getOwnerLinks(null));
    }

    // =========================================================================
    // getAdminLinks
    // =========================================================================

    @Test
    void shouldReturnGetAllLibrariesLink_whenUserIsAdmin() throws InstanceNotFoundException {

        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canGetAllLibrary(user)).thenReturn(true);

        List<Link> links = linkProvider.getAdminLinks(userId);

        assertEquals(1, links.size());
        assertTrue(hasRel(links, "get-all-libraries"));
    }

    @Test
    void shouldReturnEmptyList_whenUserIsNotAdmin() throws InstanceNotFoundException {

        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canGetAllLibrary(user)).thenReturn(false);

        List<Link> links = linkProvider.getAdminLinks(userId);

        assertTrue(links.isEmpty());
    }

    @Test
    void shouldThrowInstanceNotFoundException_whenUserDoesNotExistOnAdminLinks() {

        when(userRepo.findByID(userId)).thenReturn(Optional.empty());

        assertThrows(InstanceNotFoundException.class,
                () -> linkProvider.getAdminLinks(userId));
    }

    @Test
    void shouldThrowNullPointerException_whenUserIdIsNullOnAdminLinks() {

        assertThrows(NullPointerException.class,
                () -> linkProvider.getAdminLinks(null));
    }

    // =========================================================================
    // getLinks
    // =========================================================================

    @Test
    void shouldReturnAllLinks_whenUserHasAllPermissions() throws InstanceNotFoundException {

        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateLibrary(user)).thenReturn(true);
        when(authPolicy.canGetLibrary(user, userId)).thenReturn(true);
        when(authPolicy.canGetAllLibrary(user)).thenReturn(true);

        List<Link> links = linkProvider.getLinks(userId);

        assertEquals(4, links.size());
        assertTrue(hasRel(links, "create-library"));
        assertTrue(hasRel(links, "get-library"));
        assertTrue(hasRel(links, "get-library-by-user"));
        assertTrue(hasRel(links, "get-all-libraries"));
    }

    @Test
    void shouldReturnOnlyAdminLink_whenUserIsOnlyAdmin() throws InstanceNotFoundException {

        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateLibrary(user)).thenReturn(false);
        when(authPolicy.canGetLibrary(user, userId)).thenReturn(false);
        when(authPolicy.canGetAllLibrary(user)).thenReturn(true);

        List<Link> links = linkProvider.getLinks(userId);

        assertEquals(1, links.size());
        assertTrue(hasRel(links, "get-all-libraries"));
    }

    // =========================================================================
    // Private helper
    // =========================================================================

    private boolean hasRel(List<Link> links, String rel) {
        return links.stream()
                .anyMatch(link -> link.getRel().value().equals(rel));
    }
}