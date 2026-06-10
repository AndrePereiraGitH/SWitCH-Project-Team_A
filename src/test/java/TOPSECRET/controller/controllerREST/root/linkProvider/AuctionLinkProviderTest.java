package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.AuctionAuthorizationPolicy;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.auction.response.AuctionDTO;
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
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link AuctionLinkProvider}.
 *
 * <p>Verifies that HATEOAS links are conditionally included based on the
 * requesting user's permissions, including:</p>
 * <ul>
 *     <li>{@code getPublicLinksForAuction} — always includes {@code get-auctions}
 *         and {@code listing} links, regardless of authentication status;</li>
 *     <li>{@code getPrivateLinksForAuction} — includes {@code place-bid} only when
 *         the user has permission and the auction is {@code ACTIVE};</li>
 *     <li>throws {@link InstanceNotFoundException} when the user does not exist.</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
class AuctionLinkProviderTest {

    @Mock
    private AuctionAuthorizationPolicy policyDouble;

    @Mock
    private IUserRepository userRepoDouble;

    @Mock
    private User userDouble;

    @Mock
    private UserId userIdDouble;

    @Mock
    private AuctionDTO dtoDouble;

    @InjectMocks
    private AuctionLinkProvider linkProvider;

    // =================================================================================================================
    // =========== getPublicLinksForAuction ============================================================================
    // =================================================================================================================

    @Test
    void getPublicLinksForAuction_shouldAlwaysIncludeGetAuctionsAndListingLinks() {
        // Arrange
        when(dtoDouble.getListingId()).thenReturn("listing-123");

        // Act
        List<Link> links = linkProvider.getPublicLinks(dtoDouble);

        // Assert
        assertEquals(2, links.size());
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("get-auctions")));
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("listing")));
    }

    // =================================================================================================================
    // =========== getPrivateLinksForAuction ===========================================================================
    // =================================================================================================================

    @Test
    void getPrivateLinksForAuction_shouldIncludePlaceBidLink_whenUserHasPermissionAndAuctionIsActive() throws InstanceNotFoundException {
        // Arrange
        when(dtoDouble.getAuctionId()).thenReturn("auction-123");
        when(dtoDouble.getAuctionStatus()).thenReturn("ACTIVE");
        when(userRepoDouble.findByID(userIdDouble)).thenReturn(Optional.of(userDouble));
        when(policyDouble.canPlaceBid(userDouble)).thenReturn(true);

        // Act
        List<Link> links = linkProvider.getPrivateLinks(dtoDouble, userIdDouble);

        // Assert
        assertEquals(1, links.size());
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("place-bid")));
    }

    @Test
    void getPrivateLinksForAuction_shouldNotIncludePlaceBidLink_whenUserLacksPermission() throws InstanceNotFoundException {
        // Arrange
        when(userRepoDouble.findByID(userIdDouble)).thenReturn(Optional.of(userDouble));
        when(policyDouble.canPlaceBid(userDouble)).thenReturn(false);

        // Act
        List<Link> links = linkProvider.getPrivateLinks(dtoDouble, userIdDouble);

        // Assert
        assertTrue(links.isEmpty());
    }

    @Test
    void getPrivateLinksForAuction_shouldNotIncludePlaceBidLink_whenAuctionIsNotActive() throws InstanceNotFoundException {
        // Arrange
        when(dtoDouble.getAuctionStatus()).thenReturn("ENDED_WITH_WINNER");
        when(userRepoDouble.findByID(userIdDouble)).thenReturn(Optional.of(userDouble));
        when(policyDouble.canPlaceBid(userDouble)).thenReturn(true);

        // Act
        List<Link> links = linkProvider.getPrivateLinks(dtoDouble, userIdDouble);

        // Assert
        assertTrue(links.isEmpty());
    }

    @Test
    void getPrivateLinksForAuction_shouldThrow_whenUserNotFound() {
        // Arrange
        when(userRepoDouble.findByID(userIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(InstanceNotFoundException.class,
                () -> linkProvider.getPrivateLinks(dtoDouble, userIdDouble));
    }
}