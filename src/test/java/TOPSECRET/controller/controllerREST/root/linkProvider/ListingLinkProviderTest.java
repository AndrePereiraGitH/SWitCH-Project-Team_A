package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.ListingAuthorizationPolicy;
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

@ExtendWith(MockitoExtension.class)
class ListingLinkProviderTest {

    @Mock
    private ListingAuthorizationPolicy authPolicy;

    @Mock
    private IUserRepository userRepo;

    @InjectMocks
    private ListingLinkProvider linkProvider;

    @Test
    void getLinksForListings_shouldReturnCreateListingLink_whenUserCanCreateListing()
            throws InstanceNotFoundException {

        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateListing(user)).thenReturn(true);

        List<Link> result = linkProvider.getLinksForListings(userId);

        assertEquals(1, result.size());
        assertEquals("create-listing", result.get(0).getRel().value());

    }

    @Test
    void getLinksForListings_shouldReturnEmptyList_whenUserCannotCreateListing()
            throws InstanceNotFoundException {

        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateListing(user)).thenReturn(false);

        List<Link> result = linkProvider.getLinksForListings(userId);

        assertTrue(result.isEmpty());

    }

    @Test
    void getLinksForListings_shouldThrowInstanceNotFoundException_whenUserDoesNotExist() {
        UserId userId = mock(UserId.class);

        when(userRepo.findByID(userId)).thenReturn(Optional.empty());

        assertThrows(InstanceNotFoundException.class, () ->
                linkProvider.getLinksForListings(userId)
        );

    }

}