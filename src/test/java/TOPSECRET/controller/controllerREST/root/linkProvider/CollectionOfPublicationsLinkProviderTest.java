package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.CollectionOfPublicationsAuthorizationPolicy;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link CollectionOfPublicationsLinkProvider}.
 */
@ExtendWith(MockitoExtension.class)
class CollectionOfPublicationsLinkProviderTest {

    @Mock
    private CollectionOfPublicationsAuthorizationPolicy collectionPolicy;

    @Mock
    private IUserRepository userRepo;

    @InjectMocks
    private CollectionOfPublicationsLinkProvider provider;

    @Test
    void getLinksForCollectionOfPublications_shouldReturnCreateLink_whenUserHasPermission() {
        // ARRANGE
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(collectionPolicy.canCreateCollectionOfPublications(user)).thenReturn(true);

        // ACT
        List<?> result = provider.getLinksForCollectionOfPublications(userId);

        // ASSERT
        assertEquals(1, result.size());
    }

    @Test
    void getLinksForCollectionOfPublications_shouldReturnEmptyList_whenUserHasNoPermission() {
        // ARRANGE
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(collectionPolicy.canCreateCollectionOfPublications(user)).thenReturn(false);

        // ACT
        List<?> result = provider.getLinksForCollectionOfPublications(userId);

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void getLinksForCollectionOfPublications_shouldThrowIllegalArgumentException_whenUserDoesNotExist() {
        // ARRANGE
        UserId userId = mock(UserId.class);
        when(userRepo.findByID(userId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> provider.getLinksForCollectionOfPublications(userId));
    }
}