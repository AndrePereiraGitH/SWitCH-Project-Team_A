package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.AuctionRESTController;
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
     * Returns the HATEOAS links for the auction collection endpoint ({@code GET /auctions}).
     *
     * <p>Always includes a {@code get-auctions} link for navigating to the auction list.
     * Includes a {@code create-auction} link if the user has permission to create auctions.</p>
     *
     * @param userId the identifier of the requesting user; must not be {@code null}
     * @return a list of applicable {@link Link}s for the auction collection
     * @throws InstanceNotFoundException if no user exists with the given {@code userId}
     */
    public List<Link> getLinksForAuction(UserId userId) throws InstanceNotFoundException {

        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(AuctionRESTController.class)
                .getAuctions(null, null, null, null, false, null))
                .withRel("get-auctions"));

        User user = userRepo.findByID(userId)
                .orElseThrow(() -> new InstanceNotFoundException("User with ID " + userId + " not found"));

        if(policy.canCreateAuction(user)){
            links.add(linkTo(methodOn(AuctionRESTController.class)
                    .createAuction(null, null))
                    .withRel("create-auction"));
        }
        return links;
    }
}
