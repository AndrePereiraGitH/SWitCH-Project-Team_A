package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.AuctionRESTController;
import TOPSECRET.controller.controllerREST.ListingRESTController;
import TOPSECRET.controller.controllerREST.root.authorizationPolicy.AuctionAuthorizationPolicy;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.auction.response.AuctionDTO;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


/**
 * Provides HATEOAS links for {@link AuctionDTO} responses,
 * based on the requesting user's permissions defined in {@link AuctionAuthorizationPolicy}.
 *
 * <p>Links are conditionally included depending on the user's {@link UserRole},
 * ensuring that clients only see actions they are authorized to perform.</p>
 */
@Component
public class AuctionLinkProvider {

    private final AuctionAuthorizationPolicy policy;
    private final IUserRepository userRepo;

    /**
     * Constructs a new {@code AuctionLinkProvider} with the required dependencies.
     *
     * @param policy   the authorization policy used to determine link visibility;
     *                 must not be {@code null}
     * @param userRepo the repository used to retrieve the requesting user;
     *                 must not be {@code null}
     */
    public AuctionLinkProvider(AuctionAuthorizationPolicy policy, IUserRepository userRepo) {
        this.policy = policy;
        this.userRepo = userRepo;
    }

    /**
     * Returns the public HATEOAS links for a given {@link AuctionDTO}.
     *
     * <p>These links are always included regardless of the requesting user's role
     * or authentication status, and are safe to expose to non-registered users.</p>
     *
     * <p>Includes:</p>
     * <ul>
     *   <li>{@code get-auctions} — navigates to the auction collection</li>
     *   <li>{@code listing} — navigates to the listing associated with the auction</li>
     * </ul>
     *
     * @param dto the auction DTO to generate links for; must not be {@code null}
     * @return a list of public {@link Link}s for the given auction
     */
    public List<Link> getPublicLinks(AuctionDTO dto) {
        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(AuctionRESTController.class)
                .getAuctions(null, null, null, null, false))
                .withRel("get-auctions"));

        links.add(linkTo(methodOn(ListingRESTController.class)
                .findById(dto.getListingId(), null))
                .withRel("listing"));

        return links;
    }

    /**
     * Returns the private HATEOAS links for a given {@link AuctionDTO},
     * based on the requesting user's permissions defined in {@link AuctionAuthorizationPolicy}.
     *
     * <p>These links are only included for authenticated users with the appropriate role.</p>
     *
     * <p>Includes:</p>
     * <ul>
     *   <li>{@code place-bid} — available to {@link UserRole#NORMAL_USER} only,
     *       and only when the auction status is {@code ACTIVE}</li>
     * </ul>
     *
     * @param dto    the auction DTO to generate links for; must not be {@code null}
     * @param userId the identifier of the requesting user; must not be {@code null}
     * @return a list of private {@link Link}s applicable to the given user and auction state
     * @throws InstanceNotFoundException if no user exists with the given {@code userId}
     */
    public List<Link> getPrivateLinks(AuctionDTO dto, UserId userId) throws InstanceNotFoundException {
        List<Link> links = new ArrayList<>();

        User user = userRepo.findByID(userId)
                .orElseThrow(() -> new InstanceNotFoundException("User with ID " + userId + " not found"));

        if (policy.canPlaceBid(user) && dto.getAuctionStatus().equals("ACTIVE")) {
            links.add(linkTo(methodOn(AuctionRESTController.class)
                    .placeBid(dto.getAuctionId(), null, null))
                    .withRel("place-bid"));
        }

        return links;
    }
}
