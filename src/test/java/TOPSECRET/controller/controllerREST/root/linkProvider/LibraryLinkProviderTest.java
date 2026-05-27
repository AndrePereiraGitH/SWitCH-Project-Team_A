package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.controller.controllerREST.root.authorizationPolicy.LibraryAuthorizationPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link LibraryLinkProvider} class.
 * These tests verify that HATEOAS links are correctly generated or withheld
 * based on user presence and authorization rules.
 */
class LibraryLinkProviderTest {

    private IUserRepository userRepo;
    private LibraryAuthorizationPolicy authPolicy;
    private LibraryLinkProvider linkProvider;
    private UserId userId;
    private User userStub;

    @BeforeEach
    void setUp() {
        userRepo = mock(IUserRepository.class);
        authPolicy = mock(LibraryAuthorizationPolicy.class);
        linkProvider = new LibraryLinkProvider(authPolicy, userRepo);

        userId = new UserId(UUID.randomUUID().toString());
        userStub = mock(User.class);
    }

    @Test
    void shouldReturnLinkWhenUserIsAuthorized() {
        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.of(userStub));
        when(authPolicy.canCreateLibrary(userStub)).thenReturn(true);

        // Act
        List<Link> links = linkProvider.getLinksForLibraries(userId);

        // Assert
        assertFalse(links.isEmpty(), "Links should not be empty");
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("create-library")));
        verify(authPolicy).canCreateLibrary(userStub);
    }

    @Test
    void shouldReturnEmptyListWhenUserIsNotAuthorized() {
        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.of(userStub));
        when(authPolicy.canCreateLibrary(userStub)).thenReturn(false);

        // Act
        List<Link> links = linkProvider.getLinksForLibraries(userId);

        // Assert
        assertTrue(links.isEmpty(), "Links should be empty for unauthorized users");
    }

    @Test
    void shouldReturnEmptyListWhenUserDoesNotExist() {
        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.empty());

        // Act
        List<Link> links = linkProvider.getLinksForLibraries(userId);

        // Assert
        assertTrue(links.isEmpty(), "Links should be empty if user is not found in repository");
        verifyNoInteractions(authPolicy);
    }
}