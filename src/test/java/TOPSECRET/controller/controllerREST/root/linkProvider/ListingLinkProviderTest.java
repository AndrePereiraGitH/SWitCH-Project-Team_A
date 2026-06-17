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

    // =========================================================================
    // getAuthenticatedUserLinks — create-listing
    // =========================================================================

    @Test
    void getAuthenticatedUserLinks_shouldReturnOneLink_whenUserCanCreateListingOnly()
            throws InstanceNotFoundException {

        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateListing(user)).thenReturn(true);
        when(authPolicy.canBuyListing(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getAuthenticatedUserLinks(userId);

        // Assert
        assertEquals(1, links.size());
    }

    @Test
    void getAuthenticatedUserLinks_shouldReturnCreateListingRel_whenUserCanCreateListing()
            throws InstanceNotFoundException {

        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateListing(user)).thenReturn(true);
        when(authPolicy.canBuyListing(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getAuthenticatedUserLinks(userId);

        // Assert
        assertTrue(links.stream()
                .anyMatch(link -> link.getRel().value().equals("create-listing")));
    }

    @Test
    void getAuthenticatedUserLinks_shouldReturnEmptyList_whenUserHasNoPermissions()
            throws InstanceNotFoundException {

        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateListing(user)).thenReturn(false);
        when(authPolicy.canBuyListing(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getAuthenticatedUserLinks(userId);

        // Assert
        assertTrue(links.isEmpty());
    }

    // =========================================================================
    // getAuthenticatedUserLinks — buy-listing
    // =========================================================================

    @Test
    void getAuthenticatedUserLinks_shouldReturnOneLink_whenUserCanBuyListingOnly()
            throws InstanceNotFoundException {

        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateListing(user)).thenReturn(false);
        when(authPolicy.canBuyListing(user)).thenReturn(true);

        // Act
        List<Link> links = provider.getAuthenticatedUserLinks(userId);

        // Assert
        assertEquals(1, links.size());
    }

    @Test
    void getAuthenticatedUserLinks_shouldReturnBuyListingRel_whenUserCanBuyListing()
            throws InstanceNotFoundException {

        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateListing(user)).thenReturn(false);
        when(authPolicy.canBuyListing(user)).thenReturn(true);

        // Act
        List<Link> links = provider.getAuthenticatedUserLinks(userId);

        // Assert
        assertTrue(links.stream()
                .anyMatch(link -> link.getRel().value().equals("buy-listing")));
    }

    @Test
    void getAuthenticatedUserLinks_shouldReturnTwoLinks_whenUserCanCreateAndBuyListing()
            throws InstanceNotFoundException {

        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateListing(user)).thenReturn(true);
        when(authPolicy.canBuyListing(user)).thenReturn(true);

        // Act
        List<Link> links = provider.getAuthenticatedUserLinks(userId);

        // Assert
        assertEquals(2, links.size());
    }

    // =========================================================================
    // getAuthenticatedUserLinks — errors
    // =========================================================================

    @Test
    void getAuthenticatedUserLinks_shouldThrowInstanceNotFoundException_whenUserDoesNotExist() {

        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                InstanceNotFoundException.class,
                () -> provider.getAuthenticatedUserLinks(userId)
        );
    }

    @Test
    void getAuthenticatedUserLinks_shouldThrowNullPointerException_whenUserIdIsNull() {

        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> provider.getAuthenticatedUserLinks(null)
        );
    }

    // =========================================================================
    // getPublicLinks
    // =========================================================================

    @Test
    void getPublicLinks_shouldReturnFiveLinks() {

        // Act
        List<Link> links = provider.getPublicLinks();

        // Assert
        assertEquals(5, links.size());
    }

    @Test
    void getPublicLinks_shouldContainFindByIdLink() {

        // Act
        List<Link> links = provider.getPublicLinks();

        // Assert
        assertTrue(links.stream()
                .anyMatch(link -> link.getRel().value().equals("find-by-id")));
    }

    @Test
    void getPublicLinks_shouldContainFindAllLink() {

        // Act
        List<Link> links = provider.getPublicLinks();

        // Assert
        assertTrue(links.stream()
                .anyMatch(link -> link.getRel().value().equals("find-all")));
    }

    @Test
    void getPublicLinks_shouldContainGetAllDirectSaleByGenreLink() {

        // Act
        List<Link> links = provider.getPublicLinks();

        // Assert
        assertTrue(links.stream()
                .anyMatch(link -> link.getRel().value().equals("get-all-direct-sale-by-genre")));
    }

    @Test
    void getPublicLinks_shouldContainGetAllDirectSaleByPublisherLink() {

        // Act
        List<Link> links = provider.getPublicLinks();

        // Assert
        assertTrue(links.stream()
                .anyMatch(link -> link.getRel().value().equals("get-all-direct-sale-by-publisher")));
    }

    @Test
    void getPublicLinks_shouldContainGetListingsByPublicationLink() {

        // Act
        List<Link> links = provider.getPublicLinks();

        // Assert
        assertTrue(links.stream()
                .anyMatch(link -> link.getRel().value().equals("get-listings-by-publication")));
    }

    // =========================================================================
    // getAuctionLinks
    // =========================================================================

    @Test
    void getAuctionLinks_shouldReturnOneLink_whenUserCanCreateListing()
            throws InstanceNotFoundException {

        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateListing(user)).thenReturn(true);

        // Act
        List<Link> links = provider.getAuctionLinks(userId);

        // Assert
        assertEquals(1, links.size());
    }

    @Test
    void getAuctionLinks_shouldReturnCreateAuctionRel_whenUserCanCreateListing()
            throws InstanceNotFoundException {

        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateListing(user)).thenReturn(true);

        // Act
        List<Link> links = provider.getAuctionLinks(userId);

        // Assert
        assertTrue(links.stream()
                .anyMatch(link -> link.getRel().value().equals("create-auction")));
    }

    @Test
    void getAuctionLinks_shouldReturnEmptyList_whenUserCannotCreateListing()
            throws InstanceNotFoundException {

        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateListing(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getAuctionLinks(userId);

        // Assert
        assertTrue(links.isEmpty());
    }

    @Test
    void getAuctionLinks_shouldThrowInstanceNotFoundException_whenUserDoesNotExist() {

        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                InstanceNotFoundException.class,
                () -> provider.getAuctionLinks(userId)
        );
    }

    @Test
    void getAuctionLinks_shouldThrowNullPointerException_whenUserIdIsNull() {

        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> provider.getAuctionLinks(null)
        );
    }

    // =========================================================================
    // getLinks
    // =========================================================================

    @Test
    void getLinks_shouldReturnEightLinks_whenUserCanCreateAndBuyListing()
            throws InstanceNotFoundException {

        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateListing(user)).thenReturn(true);
        when(authPolicy.canBuyListing(user)).thenReturn(true);

        // Act
        List<Link> links = provider.getLinks(userId);

        // Assert
        assertEquals(8, links.size());
    }

    @Test
    void getLinks_shouldReturnFiveLinks_whenUserHasNoPermissions()
            throws InstanceNotFoundException {

        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateListing(user)).thenReturn(false);
        when(authPolicy.canBuyListing(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getLinks(userId);

        // Assert
        assertEquals(5, links.size());
    }

    @Test
    void getLinks_shouldReturnSixLinks_whenUserCanOnlyBuyListing()
            throws InstanceNotFoundException {

        // Arrange
        when(userRepo.findByID(userId)).thenReturn(Optional.of(user));
        when(authPolicy.canCreateListing(user)).thenReturn(false);
        when(authPolicy.canBuyListing(user)).thenReturn(true);

        // Act
        List<Link> links = provider.getLinks(userId);

        // Assert
        assertEquals(6, links.size());
    }
}