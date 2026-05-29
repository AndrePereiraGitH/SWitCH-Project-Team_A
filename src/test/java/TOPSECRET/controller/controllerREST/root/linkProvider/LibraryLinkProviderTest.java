package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.controller.controllerREST.root.authorizationPolicy.LibraryAuthorizationPolicy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link LibraryLinkProvider} class.
 * These tests verify that HATEOAS links are correctly generated or withheld
 * based on user presence and authorization rules.
 */
@ExtendWith(MockitoExtension.class)
class LibraryLinkProviderTest {

    @Mock
    private IUserRepository userRepo;

    @Mock
    private LibraryAuthorizationPolicy authPolicy;

    @Mock
    private UserId userId;

    @Mock
    private UserId userId2;

    @Mock
    private User userStub;

    @Mock
    private User userStub2;

    @InjectMocks
    private LibraryLinkProvider linkProvider;

    // ============ Authorized user ============

    @Test
    void shouldReturnGetAllAndCreateLinks_whenUserIsAuthorized() {
        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.of(userStub));
        when(authPolicy.canCreateLibrary(userStub)).thenReturn(true);

        // Act
        List<Link> links = linkProvider.getLinksForLibraries(userId);

        // Assert
        assertEquals(2, links.size());
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("get-all-libraries")));
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("create-library")));
    }

    @Test
    void shouldReturnGetAllAndCreateLinks_whenSecondUserIsAuthorized() {
        // Arrange
        when(userRepo.findByID(userId2)).thenReturn(Optional.of(userStub2));
        when(authPolicy.canCreateLibrary(userStub2)).thenReturn(true);

        // Act
        List<Link> links = linkProvider.getLinksForLibraries(userId2);

        // Assert
        assertEquals(2, links.size());
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("get-all-libraries")));
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("create-library")));
    }

    // ============ Unauthorized user ============

    @Test
    void shouldReturnOnlyGetAllLink_whenUserIsNotAuthorized() {
        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.of(userStub));
        when(authPolicy.canCreateLibrary(userStub)).thenReturn(false);

        // Act
        List<Link> links = linkProvider.getLinksForLibraries(userId);

        // Assert
        assertEquals(1, links.size());
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("get-all-libraries")));
        assertFalse(links.stream().anyMatch(l -> l.getRel().value().equals("create-library")));
    }

    @Test
    void shouldReturnOnlyGetAllLink_whenSecondUserIsNotAuthorized() {
        // Arrange
        when(userRepo.findByID(userId2)).thenReturn(Optional.of(userStub2));
        when(authPolicy.canCreateLibrary(userStub2)).thenReturn(false);

        // Act
        List<Link> links = linkProvider.getLinksForLibraries(userId2);

        // Assert
        assertEquals(1, links.size());
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("get-all-libraries")));
        assertFalse(links.stream().anyMatch(l -> l.getRel().value().equals("create-library")));
    }

    // ============ User not found ============

    @Test
    void shouldReturnOnlyGetAllLink_whenUserDoesNotExist() {
        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.empty());

        // Act
        List<Link> links = linkProvider.getLinksForLibraries(userId);

        // Assert
        assertEquals(1, links.size());
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("get-all-libraries")));
        assertFalse(links.stream().anyMatch(l -> l.getRel().value().equals("create-library")));
    }

    @Test
    void shouldReturnOnlyGetAllLink_whenSecondUserDoesNotExist() {
        // Arrange
        when(userRepo.findByID(userId2)).thenReturn(Optional.empty());

        // Act
        List<Link> links = linkProvider.getLinksForLibraries(userId2);

        // Assert
        assertEquals(1, links.size());
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("get-all-libraries")));
        assertFalse(links.stream().anyMatch(l -> l.getRel().value().equals("create-library")));
    }
}