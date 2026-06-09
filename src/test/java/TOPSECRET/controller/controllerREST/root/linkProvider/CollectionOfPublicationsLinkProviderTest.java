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
import org.springframework.hateoas.Link;

import javax.management.InstanceNotFoundException;
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

// -------------------------------------------------------------------------
// getPublicLinks
// -------------------------------------------------------------------------

    @Test
    void getPublicLinks_shouldReturnThreeLinks() {
        // ACT
        List<Link> result = provider.getPublicLinks();

        // ASSERT
        assertEquals(3, result.size());
    }

    @Test
    void getPublicLinks_shouldContainFindAllPublicLink() {
        // ACT
        List<Link> result = provider.getPublicLinks();

        // ASSERT
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("find-all-public")));
    }

    @Test
    void getPublicLinks_shouldContainFindPublicByGenreLink() {
        // ACT
        List<Link> result = provider.getPublicLinks();

        // ASSERT
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("find-public-by-genre")));
    }

    @Test
    void getPublicLinks_shouldContainFindByIdLink() {
        // ACT
        List<Link> result = provider.getPublicLinks();

        // ASSERT
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("find-by-id")));
    }

    // -------------------------------------------------------------------------
// getOwnerLinks
// -------------------------------------------------------------------------

    @Test
    void getOwnerLinks_shouldReturnTwoLinks_whenUserHasAllPermissions()
            throws InstanceNotFoundException {
        // ARRANGE
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(collectionPolicy.canCreateCollectionOfPublications(user)).thenReturn(true);
        when(collectionPolicy.canViewOwnCollections(user)).thenReturn(true);

        // ACT
        List<Link> result = provider.getOwnerLinks(userId);

        // ASSERT
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("create-collection-of-publications")));
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("find-own-collections")));
    }

    @Test
    void getOwnerLinks_shouldReturnEmptyList_whenUserHasNoPermissions()
            throws InstanceNotFoundException {
        // ARRANGE
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(collectionPolicy.canCreateCollectionOfPublications(user)).thenReturn(false);
        when(collectionPolicy.canViewOwnCollections(user)).thenReturn(false);

        // ACT
        List<Link> result = provider.getOwnerLinks(userId);

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void getOwnerLinks_shouldThrowInstanceNotFoundException_whenUserDoesNotExist() {
        // ARRANGE
        UserId userId = mock(UserId.class);
        when(userRepo.findByID(userId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(InstanceNotFoundException.class,
                () -> provider.getOwnerLinks(userId));
    }

    @Test
    void getOwnerLinks_shouldThrowNullPointerException_whenUserIdIsNull() {
        // ACT & ASSERT
        assertThrows(NullPointerException.class,
                () -> provider.getOwnerLinks(null));
    }
}