package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.AuctionAuthorizationPolicy;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.AuctionId;
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
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link AuctionLinkProvider}.
 *
 * <p>Verifies that HATEOAS links are conditionally included based on the
 * requesting user's permissions, including:</p>
 * <ul>
 *     <li>{@code getLinksForAuction} — always includes a {@code get-auctions} link;
 *         includes a {@code create-auction} link only when the user has permission;</li>
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
    private AuctionId auctionIdDouble;

    @InjectMocks
    private AuctionLinkProvider linkProvider;

    // =================================================================================================================
    // =========== getLinksForAuction ==================================================================================
    // =================================================================================================================

    @Test
    void getLinksForAuction_shouldIncludeCreateAuctionLink_whenUserHasPermission() throws InstanceNotFoundException {
        // Arrange
        when(userRepoDouble.findByID(userIdDouble)).thenReturn(Optional.of(userDouble));
        when(policyDouble.canCreateAuction(userDouble)).thenReturn(true);

        // Act
        List<Link> links = linkProvider.getLinksForAuction(userIdDouble);

        // Assert
        assertEquals(2, links.size());
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("get-auctions")));
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("create-auction")));
    }

    @Test
    void getLinksForAuction_shouldReturnEmptyList_whenUserDoesNotHavePermission() throws InstanceNotFoundException {
        // Arrange
        when(userRepoDouble.findByID(userIdDouble)).thenReturn(Optional.of(userDouble));
        when(policyDouble.canCreateAuction(userDouble)).thenReturn(false);

        // Act
        List<Link> links = linkProvider.getLinksForAuction(userIdDouble);

        // Assert
        assertEquals(1, links.size());
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("get-auctions")));
        assertFalse(links.stream().anyMatch(l -> l.getRel().value().equals("create-auction")));
    }

    @Test
    void getLinksForAuction_shouldThrow_whenUserNotFound() {
        // Arrange
        when(userRepoDouble.findByID(userIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(InstanceNotFoundException.class,
                () -> linkProvider.getLinksForAuction(userIdDouble));
    }
}