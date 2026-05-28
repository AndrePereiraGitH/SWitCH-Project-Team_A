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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void shouldReturnGetAllAndCreatePublisherLinks_whenUserHasPermission() {
        // ARRANGE
        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreatePublisher(user)).thenReturn(true);

        // ACT
        int result = provider.getLinksForPublisher(userId).size();

        // ASSERT
        assertEquals(2, result); // get-all-publishers + create-publisher
    }

    @Test
    void shouldReturnOnlyGetAllPublishersLink_whenUserHasNoPermission() {
        // ARRANGE
        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreatePublisher(user)).thenReturn(false);

        // ACT
        int result = provider.getLinksForPublisher(userId).size();

        // ASSERT
        assertEquals(1, result); // get-all-publishers only
    }

    @Test
    void shouldReturnOnlyGetAllPublishersLink_whenUserDoesNotExist() {
        // ARRANGE
        UserId userId = mock(UserId.class);

        when(userRepo.findByID(userId)).thenReturn(Optional.empty());

        // ACT
        int result = provider.getLinksForPublisher(userId).size();

        // ASSERT
        assertEquals(1, result); // get-all-publishers is always added
    }
}