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

    @InjectMocks
    private PublisherLinkProvider provider;

    @Test
    void getLinksForPublisher_shouldReturnCreatePublisherLink_whenUserHasPermission() {

        // ARRANGE
        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(userRepo.findByID(userId))
                .thenReturn(Optional.of(user));

        when(authPolicy.canCreatPublisher(user))
                .thenReturn(true);

        // ACT
        List<?> result = provider.getLinksForPublisher(userId);

        // ASSERT
        assertEquals(1, result.size());
    }

    @Test
    void getLinksForPublisher_shouldReturnEmptyList_whenUserHasNoPermission() {

        // ARRANGE
        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(userRepo.findByID(userId))
                .thenReturn(Optional.of(user));

        when(authPolicy.canCreatPublisher(user))
                .thenReturn(false);

        // ACT
        List<?> result = provider.getLinksForPublisher(userId);

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void getLinksForPublisher_shouldReturnEmptyList_whenUserDoesNotExist() {

        // ARRANGE
        UserId userId = mock(UserId.class);

        when(userRepo.findByID(userId))
                .thenReturn(Optional.empty());

        // ACT
        List<?> result = provider.getLinksForPublisher(userId);

        // ASSERT
        assertTrue(result.isEmpty());
    }
}