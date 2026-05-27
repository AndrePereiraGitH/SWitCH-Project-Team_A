package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.UserAuthorizationPolicy;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserLinkProvider}.
 *
 * <p>Validates link generation based on user authorization rules.</p>
 */
class UserLinkProviderTest {

    private final UserAuthorizationPolicy authPolicy = mock(UserAuthorizationPolicy.class);
    private final IUserRepository userRepository = mock(IUserRepository.class);

    @Test
    void getLinksForUsers_shouldReturnLink_whenUserIsAuthorized() {
        // Arrange
        UserLinkProvider sut = new UserLinkProvider(authPolicy, userRepository);
        UserId userIdDouble = mock(UserId.class);
        User userDouble = mock(User.class);

        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(userDouble));
        when(authPolicy.canRegisterUser(userDouble)).thenReturn(true);

        // Act
        List<Link> result = sut.getLinksForUsers(userIdDouble);

        // Assert
        assertEquals(1, result.size());
        assertEquals("register-user", result.get(0).getRel().value());
    }

    @Test
    void getLinksForUsers_shouldReturnEmptyList_whenUserIsNotAuthorized() {
        // Arrange
        UserLinkProvider sut = new UserLinkProvider(authPolicy, userRepository);
        UserId userIdDouble = mock(UserId.class);
        User userDouble = mock(User.class);

        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.of(userDouble));
        when(authPolicy.canRegisterUser(userDouble)).thenReturn(false);

        // Act
        List<Link> result = sut.getLinksForUsers(userIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getLinksForUsers_shouldReturnEmptyList_whenUserDoesNotExist() {
        // Arrange
        UserLinkProvider sut = new UserLinkProvider(authPolicy, userRepository);
        UserId userIdDouble = mock(UserId.class);

        when(userRepository.findByID(userIdDouble)).thenReturn(Optional.empty());

        // Act
        List<Link> result = sut.getLinksForUsers(userIdDouble);

        // Assert
        assertTrue(result.isEmpty());
        verifyNoInteractions(authPolicy);
    }
}