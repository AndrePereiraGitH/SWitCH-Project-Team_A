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
    void shouldReturnCreateAndGetLibraryLinks_whenUserCanCreateAndGetLibrary()
            throws InstanceNotFoundException {

        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateLibrary(user)).thenReturn(true);
        when(authPolicy.canGetLibrary(user, userId)).thenReturn(true);

        // Act
        List<Link> links = linkProvider.getOwnerLinks(userId);

        // Assert
        assertEquals(3, links.size());
        assertTrue(hasRel(links, "create-library"));
        assertTrue(hasRel(links, "get-library"));
        assertTrue(hasRel(links, "get-library-by-user"));
        assertTrue(hrefContains(links, "create-library", "/libraries"));
        assertTrue(hrefContains(links, "get-library", "/libraries"));
        assertTrue(hrefContains(links, "get-library-by-user", "/libraries/by-user"));
    }

    @Test
    void shouldReturnOnlyCreateLink_whenUserCannotGetLibrary()
            throws InstanceNotFoundException {

        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateLibrary(user)).thenReturn(true);
        when(authPolicy.canGetLibrary(user, userId)).thenReturn(false);

        // Act
        List<Link> links = linkProvider.getOwnerLinks(userId);

        // Assert
        assertEquals(1, links.size());
        assertTrue(hasRel(links, "create-library"));
        assertFalse(hasRel(links, "get-library"));
        assertFalse(hasRel(links, "get-library-by-user"));
    }

    @Test
    void shouldReturnOnlyGetLibraryLinks_whenUserCannotCreate()
            throws InstanceNotFoundException {

        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateLibrary(user)).thenReturn(false);
        when(authPolicy.canGetLibrary(user, userId)).thenReturn(true);

        // Act
        List<Link> links = linkProvider.getOwnerLinks(userId);

        // Assert
        assertEquals(2, links.size());
        assertFalse(hasRel(links, "create-library"));
        assertTrue(hasRel(links, "get-library"));
        assertTrue(hasRel(links, "get-library-by-user"));
    }

    @Test
    void shouldReturnEmptyList_whenUserHasNoOwnerPermissions()
            throws InstanceNotFoundException {

        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateLibrary(user)).thenReturn(false);
        when(authPolicy.canGetLibrary(user, userId)).thenReturn(false);

        // Act
        List<Link> links = linkProvider.getOwnerLinks(userId);

        // Assert
        assertTrue(links.isEmpty());
    }

    @Test
    void shouldThrowInstanceNotFoundException_whenUserDoesNotExistOnOwnerLinks() {

        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(InstanceNotFoundException.class,
                () -> linkProvider.getOwnerLinks(userId));
    }

    @Test
    void shouldThrowNullPointerException_whenUserIdIsNullOnOwnerLinks() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> linkProvider.getOwnerLinks(null));
    }

    // =========================================================================
    // getAdminLinks
    // =========================================================================

    @Test
    void shouldReturnGetAllLibrariesLink_whenUserIsAdmin()
            throws InstanceNotFoundException {

        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canGetAllLibrary(user)).thenReturn(true);

        // Act
        List<Link> links = linkProvider.getAdminLinks(userId);

        // Assert
        assertEquals(1, links.size());
        assertTrue(hasRel(links, "get-all-libraries"));
        assertTrue(hrefContains(links, "get-all-libraries", "/libraries"));
    }

    @Test
    void shouldReturnEmptyList_whenUserIsNotAdmin()
            throws InstanceNotFoundException {

        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canGetAllLibrary(user)).thenReturn(false);

        // Act
        List<Link> links = linkProvider.getAdminLinks(userId);

        // Assert
        assertTrue(links.isEmpty());
    }

    @Test
    void shouldThrowInstanceNotFoundException_whenUserDoesNotExistOnAdminLinks() {

        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(InstanceNotFoundException.class,
                () -> linkProvider.getAdminLinks(userId));
    }

    @Test
    void shouldThrowNullPointerException_whenUserIdIsNullOnAdminLinks() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> linkProvider.getAdminLinks(null));
    }

    // =========================================================================
    // getLinks
    // =========================================================================

    @Test
    void shouldReturnAllLinks_whenUserHasAllPermissions()
            throws InstanceNotFoundException {

        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateLibrary(user)).thenReturn(true);
        when(authPolicy.canGetLibrary(user, userId)).thenReturn(true);
        when(authPolicy.canGetAllLibrary(user)).thenReturn(true);

        // Act
        List<Link> links = linkProvider.getLinks(userId);

        // Assert
        assertEquals(4, links.size());
        assertTrue(hasRel(links, "create-library"));
        assertTrue(hasRel(links, "get-library"));
        assertTrue(hasRel(links, "get-library-by-user"));
        assertTrue(hasRel(links, "get-all-libraries"));
    }

    @Test
    void shouldReturnOwnerLinksBeforeAdminLinks_whenUserHasAllPermissions()
            throws InstanceNotFoundException {

        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateLibrary(user)).thenReturn(true);
        when(authPolicy.canGetLibrary(user, userId)).thenReturn(true);
        when(authPolicy.canGetAllLibrary(user)).thenReturn(true);

        // Act
        List<Link> links = linkProvider.getLinks(userId);

        // Assert
        assertEquals("create-library", links.get(0).getRel().value());
        assertEquals("get-library", links.get(1).getRel().value());
        assertEquals("get-library-by-user", links.get(2).getRel().value());
        assertEquals("get-all-libraries", links.get(3).getRel().value());
    }

    @Test
    void shouldReturnOnlyAdminLink_whenUserIsOnlyAdmin()
            throws InstanceNotFoundException {

        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateLibrary(user)).thenReturn(false);
        when(authPolicy.canGetLibrary(user, userId)).thenReturn(false);
        when(authPolicy.canGetAllLibrary(user)).thenReturn(true);

        // Act
        List<Link> links = linkProvider.getLinks(userId);

        // Assert
        assertEquals(1, links.size());
        assertTrue(hasRel(links, "get-all-libraries"));
    }

    @Test
    void shouldReturnEmptyList_whenUserHasNoPermissions()
            throws InstanceNotFoundException {

        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateLibrary(user)).thenReturn(false);
        when(authPolicy.canGetLibrary(user, userId)).thenReturn(false);
        when(authPolicy.canGetAllLibrary(user)).thenReturn(false);

        // Act
        List<Link> links = linkProvider.getLinks(userId);

        // Assert
        assertTrue(links.isEmpty());
    }

    // =========================================================================
    // Private helpers
    // =========================================================================

    private boolean hasRel(List<Link> links, String rel) {
        return links.stream()
                .anyMatch(link -> link.getRel().value().equals(rel));
    }

    private boolean hrefContains(List<Link> links, String rel, String urlFragment) {
        return links.stream()
                .filter(link -> link.getRel().value().equals(rel))
                .anyMatch(link -> link.getHref().contains(urlFragment));
    }
}