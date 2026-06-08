package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.ListingAuthorizationPolicy;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
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

@ExtendWith(MockitoExtension.class)
class ListingLinkProviderTest {

    @Mock
    private ListingAuthorizationPolicy authPolicy;

    @Mock
    private IUserRepository userRepo;

    @InjectMocks
    private ListingLinkProvider provider;

    private UserId userId;
    private User user;

    @BeforeEach
    void setUp() {
        userId = mock(UserId.class);
        user = mock(User.class);
    }

    @Test
    void getOwnerLinks_shouldReturnOneLink_whenUserCanCreateListing()
            throws InstanceNotFoundException {

        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateListing(user)).thenReturn(true);

        List<Link> links = provider.getOwnerLinks(userId);

        assertEquals(1, links.size());
    }

    @Test
    void getOwnerLinks_shouldReturnCreateListingRel_whenUserCanCreateListing()
            throws InstanceNotFoundException {

        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateListing(user)).thenReturn(true);

        List<Link> links = provider.getOwnerLinks(userId);

        assertEquals("create-listing", links.get(0).getRel().value());
    }

    @Test
    void getOwnerLinks_shouldReturnEmptyList_whenUserCannotCreateListing()
            throws InstanceNotFoundException {

        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateListing(user)).thenReturn(false);

        List<Link> links = provider.getOwnerLinks(userId);

        assertTrue(links.isEmpty());
    }

    @Test
    void getOwnerLinks_shouldThrowInstanceNotFoundException_whenUserDoesNotExist() {

        UserId userId = mock(UserId.class);

        when(userRepo.findByID(userId)).thenReturn(Optional.empty());

        assertThrows(
                InstanceNotFoundException.class,
                () -> provider.getOwnerLinks(userId)
        );
    }

    @Test
    void getOwnerLinks_shouldThrowNullPointerException_whenUserIdIsNull() {

        assertThrows(
                NullPointerException.class,
                () -> provider.getOwnerLinks(null)
        );
    }

    @Test
    void getPublicLinks_shouldReturnFourLinks() {

        List<Link> links = provider.getPublicLinks();

        assertEquals(4, links.size());
    }

    @Test
    void getPublicLinks_shouldContainFindByIdLink() {

        List<Link> links = provider.getPublicLinks();

        assertTrue(links.stream()
                .anyMatch(link -> link.getRel().value().equals("find-by-id")));
    }

    @Test
    void getPublicLinks_shouldContainFindAllLink() {

        List<Link> links = provider.getPublicLinks();

        assertTrue(links.stream()
                .anyMatch(link -> link.getRel().value().equals("find-all")));
    }

    @Test
    void getPublicLinks_shouldContainGetAllDirectSaleByGenreLink() {

        List<Link> links = provider.getPublicLinks();

        assertTrue(links.stream()
                .anyMatch(link -> link.getRel().value().equals("get-all-direct-sale-by-genre")));
    }

    @Test
    void getPublicLinks_shouldContainGetAllDirectSaleByPublisherLink() {

        List<Link> links = provider.getPublicLinks();

        assertTrue(links.stream()
                .anyMatch(link -> link.getRel().value().equals("get-all-direct-sale-by-publisher")));
    }
}