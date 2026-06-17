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
    // getLinks
    // ================================================================

    @Test
    void getLinks_shouldReturnTwoLinks_whenUserIsAdmin() throws InstanceNotFoundException {
        // Arrange
        UserId requesterIdDouble = mock(UserId.class);
        User requesterDouble = mock(User.class);

        when(userRepository.findByID(requesterIdDouble)).thenReturn(Optional.of(requesterDouble));
        when(authPolicy.canFindAllUsers(requesterDouble)).thenReturn(true);
        when(authPolicy.canRegisterUser(requesterDouble)).thenReturn(true);

        // Act
        List<Link> result = sut.getLinks(requesterIdDouble);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("get-all-users")));
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("register-user")));
    }

    @Test
    void getLinks_shouldReturnEmptyList_whenUserIsNotAdmin() throws InstanceNotFoundException {
        // Arrange
        UserId requesterIdDouble = mock(UserId.class);
        User requesterDouble = mock(User.class);

        when(userRepository.findByID(requesterIdDouble)).thenReturn(Optional.of(requesterDouble));
        when(authPolicy.canFindAllUsers(requesterDouble)).thenReturn(false);
        when(authPolicy.canRegisterUser(requesterDouble)).thenReturn(false);

        // Act
        List<Link> result = sut.getLinks(requesterIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getLinks_shouldThrowInstanceNotFoundException_whenUserDoesNotExist() {
        // Arrange
        UserId requesterIdDouble = mock(UserId.class);

        when(userRepository.findByID(requesterIdDouble)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InstanceNotFoundException.class, () -> sut.getLinks(requesterIdDouble));
    }

    // ================================================================
    // getLinksForUserResource
    // ================================================================

    @Test
    void getLinksForUserResource_shouldReturnThreeLinks_whenRequesterIsAdminAndCanAccessUser()
            throws InstanceNotFoundException {
        // Arrange
        UserId requesterIdDouble = mock(UserId.class);
        UserId targetUserIdDouble = mock(UserId.class);
        User requesterDouble = mock(User.class);

        when(userRepository.findByID(requesterIdDouble)).thenReturn(Optional.of(requesterDouble));

        when(authPolicy.canFindUserById(requesterDouble, targetUserIdDouble)).thenReturn(true);
        when(authPolicy.canFindAllUsers(requesterDouble)).thenReturn(true);
        when(authPolicy.canRegisterUser(requesterDouble)).thenReturn(true);

        // Act
        List<Link> result = sut.getLinksForUserResource(requesterIdDouble, targetUserIdDouble);

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("get-user")));
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("get-all-users")));
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("register-user")));
    }

    @Test
    void getLinksForUserResource_shouldReturnOneLink_whenRequesterCanOnlyAccessOwnUser()
            throws InstanceNotFoundException {
        // Arrange
        UserId requesterIdDouble = mock(UserId.class);
        UserId targetUserIdDouble = mock(UserId.class);
        User requesterDouble = mock(User.class);

        when(userRepository.findByID(requesterIdDouble)).thenReturn(Optional.of(requesterDouble));

        when(authPolicy.canFindUserById(requesterDouble, targetUserIdDouble)).thenReturn(true);
        when(authPolicy.canFindAllUsers(requesterDouble)).thenReturn(false);
        when(authPolicy.canRegisterUser(requesterDouble)).thenReturn(false);

        // Act
        List<Link> result = sut.getLinksForUserResource(requesterIdDouble, targetUserIdDouble);

        // Assert
        assertEquals(1, result.size());
        assertEquals("get-user", result.get(0).getRel().value());
    }

    @Test
    void getLinksForUserResource_shouldReturnEmptyList_whenRequesterCannotAccessUser()
            throws InstanceNotFoundException {
        // Arrange
        UserId requesterIdDouble = mock(UserId.class);
        UserId targetUserIdDouble = mock(UserId.class);
        User requesterDouble = mock(User.class);

        when(userRepository.findByID(requesterIdDouble)).thenReturn(Optional.of(requesterDouble));

        when(authPolicy.canFindUserById(requesterDouble, targetUserIdDouble)).thenReturn(false);
        when(authPolicy.canFindAllUsers(requesterDouble)).thenReturn(false);
        when(authPolicy.canRegisterUser(requesterDouble)).thenReturn(false);

        // Act
        List<Link> result = sut.getLinksForUserResource(requesterIdDouble, targetUserIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getLinksForUserResource_shouldThrowInstanceNotFoundException_whenRequesterDoesNotExist() {
        // Arrange
        UserId requesterIdDouble = mock(UserId.class);
        UserId targetUserIdDouble = mock(UserId.class);

        when(userRepository.findByID(requesterIdDouble)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                InstanceNotFoundException.class,
                () -> sut.getLinksForUserResource(requesterIdDouble, targetUserIdDouble)
        );
    }
}