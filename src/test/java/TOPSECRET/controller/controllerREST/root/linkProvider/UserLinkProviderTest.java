package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.UserAuthorizationPolicy;
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
 * Unit tests for {@link UserLinkProvider}.
 *
 * <p>Validates link generation based on user authorization rules.</p>
 */
@ExtendWith(MockitoExtension.class)
class UserLinkProviderTest {

    @Mock
    private UserAuthorizationPolicy authPolicy;

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserLinkProvider sut;

    // ================================================================
    // getPublicLinks
    // ================================================================

    @Test
    void getPublicLinks_shouldReturnEmptyList() {
        // Act
        List<Link> result = sut.getPublicLinks();

        // Assert
        assertTrue(result.isEmpty());
    }

    // ================================================================
    // getAdminLinks
    // ================================================================

    @Test
    void getAdminLinks_shouldReturnThreeLinks_whenUserIsAdmin() throws InstanceNotFoundException {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        User userDouble = mock(User.class);

        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(userDouble));
        when(authPolicy.canRegisterUser(userDouble)).thenReturn(true);

        // Act
        List<Link> result = sut.getAdminLinks(userIdDouble);

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("get-user")));
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("get-all-users")));
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("register-user")));
    }

    @Test
    void getAdminLinks_shouldReturnEmptyList_whenUserIsNotAdmin() throws InstanceNotFoundException {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        User userDouble = mock(User.class);

        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(userDouble));
        when(authPolicy.canRegisterUser(userDouble)).thenReturn(false);

        // Act
        List<Link> result = sut.getAdminLinks(userIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getAdminLinks_shouldThrowInstanceNotFoundException_whenUserDoesNotExist() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);

        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InstanceNotFoundException.class, () -> sut.getAdminLinks(userIdDouble));
    }

    // ================================================================
    // getLinks
    // ================================================================

    @Test
    void getLinks_shouldReturnThreeLinks_whenUserIsAdmin() throws InstanceNotFoundException {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        User userDouble = mock(User.class);

        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(userDouble));
        when(authPolicy.canRegisterUser(userDouble)).thenReturn(true);

        // Act
        List<Link> result = sut.getLinks(userIdDouble);

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("get-user")));
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("get-all-users")));
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("register-user")));
    }

    @Test
    void getLinks_shouldReturnEmptyList_whenUserIsNotAdmin() throws InstanceNotFoundException {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        User userDouble = mock(User.class);

        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(userDouble));
        when(authPolicy.canRegisterUser(userDouble)).thenReturn(false);

        // Act
        List<Link> result = sut.getLinks(userIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getLinks_shouldThrowInstanceNotFoundException_whenUserDoesNotExist() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);

        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InstanceNotFoundException.class, () -> sut.getLinks(userIdDouble));
    }

    // ================================================================
    // getLinksForUsers
    // ================================================================

    @Test
    void getLinksForUsers_shouldReturnTwoLinks_whenUserIsAuthorized() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        User userDouble = mock(User.class);

        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(userDouble));
        when(authPolicy.canRegisterUser(userDouble)).thenReturn(true);

        // Act
        List<Link> result = sut.getLinksForUsers(userIdDouble);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("get-user")));
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("register-user")));
    }

    @Test
    void getLinksForUsers_shouldReturnOneLink_whenUserIsNotAuthorized() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);
        User userDouble = mock(User.class);

        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(userDouble));
        when(authPolicy.canRegisterUser(userDouble)).thenReturn(false);

        // Act
        List<Link> result = sut.getLinksForUsers(userIdDouble);

        // Assert
        assertEquals(1, result.size());
        assertEquals("get-user", result.get(0).getRel().value());
    }

    @Test
    void getLinksForUsers_shouldReturnOneLink_whenUserDoesNotExist() {
        // Arrange
        UserId userIdDouble = mock(UserId.class);

        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.empty());

        // Act
        List<Link> result = sut.getLinksForUsers(userIdDouble);

        // Assert
        assertEquals(1, result.size());
        assertEquals("get-user", result.get(0).getRel().value());
    }
}