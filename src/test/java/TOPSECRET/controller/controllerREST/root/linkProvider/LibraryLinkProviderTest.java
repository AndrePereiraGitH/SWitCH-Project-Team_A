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

    @Test
    void shouldReturnAllLinks_whenUserCanGetAllAndCreate() {

        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canGetAllLibrary(user)).thenReturn(true);
        when(authPolicy.canCreateLibrary(user)).thenReturn(true);

        List<Link> links = linkProvider.getLinksForLibraries(userId);

        assertEquals(4, links.size());

        assertTrue(hasRel(links, "get-all-libraries"));
        assertTrue(hasRel(links, "create-library"));
        assertTrue(hasRel(links, "get-library"));
        assertTrue(hasRel(links, "get-library-by-user"));
    }

    @Test
    void shouldReturnGetAllAndDefaultLinks_whenUserCannotCreate() {

        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canGetAllLibrary(user)).thenReturn(true);
        when(authPolicy.canCreateLibrary(user)).thenReturn(false);

        List<Link> links = linkProvider.getLinksForLibraries(userId);

        assertEquals(3, links.size());

        assertTrue(hasRel(links, "get-all-libraries"));
        assertFalse(hasRel(links, "create-library"));
        assertTrue(hasRel(links, "get-library"));
        assertTrue(hasRel(links, "get-library-by-user"));
    }

    @Test
    void shouldReturnCreateAndDefaultLinks_whenUserCannotGetAll() {

        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canGetAllLibrary(user)).thenReturn(false);
        when(authPolicy.canCreateLibrary(user)).thenReturn(true);

        List<Link> links = linkProvider.getLinksForLibraries(userId);

        assertEquals(3, links.size());

        assertFalse(hasRel(links, "get-all-libraries"));
        assertTrue(hasRel(links, "create-library"));
        assertTrue(hasRel(links, "get-library"));
        assertTrue(hasRel(links, "get-library-by-user"));
    }

    @Test
    void shouldReturnOnlyDefaultLinks_whenUserHasNoPermissions() {

        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canGetAllLibrary(user)).thenReturn(false);
        when(authPolicy.canCreateLibrary(user)).thenReturn(false);

        List<Link> links = linkProvider.getLinksForLibraries(userId);

        assertEquals(2, links.size());

        assertFalse(hasRel(links, "get-all-libraries"));
        assertFalse(hasRel(links, "create-library"));
        assertTrue(hasRel(links, "get-library"));
        assertTrue(hasRel(links, "get-library-by-user"));
    }

    @Test
    void shouldReturnOnlyDefaultLinks_whenUserDoesNotExist() {

        when(userRepo.findByID(userId)).thenReturn(Optional.empty());

        List<Link> links = linkProvider.getLinksForLibraries(userId);

        assertEquals(2, links.size());

        assertFalse(hasRel(links, "get-all-libraries"));
        assertFalse(hasRel(links, "create-library"));
        assertTrue(hasRel(links, "get-library"));
        assertTrue(hasRel(links, "get-library-by-user"));
    }

    private boolean hasRel(List<Link> links, String rel) {
        return links.stream()
                .anyMatch(link -> link.getRel().value().equals(rel));
    }
}