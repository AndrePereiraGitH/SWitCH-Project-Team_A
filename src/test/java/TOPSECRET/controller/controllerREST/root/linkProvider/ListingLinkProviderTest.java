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
    void getLinksForListings_shouldReturnBaseLinksAndCreateListingLink_whenUserCanCreateListing()
            throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateListing(user)).thenReturn(true);

        // Act
        List<Link> result = linkProvider.getLinksForListings(userId);

        // Assert
        assertEquals(5, result.size());

        assertTrue(result.stream().anyMatch(link ->
                link.getRel().value().equals("find-by-id")));

        assertTrue(result.stream().anyMatch(link ->
                link.getRel().value().equals("find-all")));

        assertTrue(result.stream().anyMatch(link ->
                link.getRel().value().equals("get-all-direct-sale-by-genre")));

        assertTrue(result.stream().anyMatch(link ->
                link.getRel().value().equals("get-all-direct-sale-by-publisher")));

        assertTrue(result.stream().anyMatch(link ->
                link.getRel().value().equals("create-listing")));
    }

    @Test
    void getLinksForListings_shouldReturnOnlyBaseLinks_whenUserCannotCreateListing()
            throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateListing(user)).thenReturn(false);

        // Act
        List<Link> result = linkProvider.getLinksForListings(userId);

        // Assert
        assertEquals(4, result.size());

        assertTrue(result.stream().anyMatch(link ->
                link.getRel().value().equals("find-by-id")));

        assertTrue(result.stream().anyMatch(link ->
                link.getRel().value().equals("find-all")));

        assertTrue(result.stream().anyMatch(link ->
                link.getRel().value().equals("get-all-direct-sale-by-genre")));

        assertTrue(result.stream().anyMatch(link ->
                link.getRel().value().equals("get-all-direct-sale-by-publisher")));

        assertFalse(result.stream().anyMatch(link ->
                link.getRel().value().equals("create-listing")));
    }
    @Test
    void getLinksForListings_shouldThrowInstanceNotFoundException_whenUserDoesNotExist() {

        // Arrange
        UserId userId = mock(UserId.class);

        when(userRepo.findByID(userId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(InstanceNotFoundException.class, () ->
                linkProvider.getLinksForListings(userId)
        );
    }
}