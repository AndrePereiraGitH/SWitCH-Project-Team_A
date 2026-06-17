package TOPSECRET.controller.controllerREST.root.linkProvider;


import TOPSECRET.controller.controllerREST.AuctionRESTController;
import TOPSECRET.controller.controllerREST.ListingRESTController;
import TOPSECRET.controller.controllerREST.root.RootLinkProvider;
import TOPSECRET.controller.controllerREST.root.authorizationPolicy.ListingAuthorizationPolicy;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Provides HATEOAS links related to listing operations.
 *
 * <p>This component is responsible for building the links that should be
 * exposed to a user according to their permissions. It uses
 * {@link ListingAuthorizationPolicy} to decide which listing-related actions
 * are available for the given user.</p>
 */
@Component
public class ListingLinkProvider implements RootLinkProvider {

    private final ListingAuthorizationPolicy authPolicy;
    private final IUserRepository userRepo;

    /**
     * Creates a new {@code ListingLinkProvider}.
     *
     * @param authPolicy the authorization policy used to determine which
     * listing actions the user can perform
     * @param userRepo   the repository used to retrieve users
     */
    @Autowired
    public ListingLinkProvider(ListingAuthorizationPolicy authPolicy, IUserRepository userRepo) {
        this.authPolicy = authPolicy;
        this.userRepo = userRepo;
    }

    /**
     * Returns the listing-related HATEOAS links available for an authenticated user.
     *
     * <p>If the user is allowed to create listings, a {@code create-listing}
     * link is included. If the user is allowed to buy listings, a
     * {@code buy-listing} link is included. If the user has neither permission,
     * an empty list is returned.</p>
     *
     * @param userId the identifier of the user whose listing links should be generated
     * @return a list of HATEOAS links available to the authenticated user
     * @throws InstanceNotFoundException if no user exists with the given identifier
     * @throws NullPointerException      if the provided {@code userId} is null
     */
    public List<Link> getAuthenticatedUserLinks(UserId userId) throws InstanceNotFoundException {

        Objects.requireNonNull(userId, "UserId cannot be null");

        List<Link> links = new ArrayList<>();
        User user = resolveUser(userId);

        if (authPolicy.canCreateListing(user)) {
            links.add(linkTo(methodOn(ListingRESTController.class)
                    .createListing(null, null))
                    .withRel("create-listing"));
        }

        if (authPolicy.canBuyListing(user)) {
            links.add(linkTo(methodOn(ListingRESTController.class)
                    .markListingAsSold(null, null))
                    .withRel("buy-listing"));
        }

        return links;
    }

    /**
     * Returns the auction-related HATEOAS links available for an authenticated user.
     *
     * <p>If the user is permitted to create listings according to the authorization policy,
     * a {@code create-auction} link is included.</p>
     *
     * @param userId the identifier of the user whose auction links should be generated
     * @return a list of HATEOAS links for auction operations available to the user
     * @throws InstanceNotFoundException if no user exists with the given identifier
     * @throws NullPointerException      if the provided {@code userId} is null
     */
    public List<Link> getAuctionLinks(UserId userId) throws InstanceNotFoundException {

        Objects.requireNonNull(userId, "UserId cannot be null");

        User user = resolveUser(userId);

        List<Link> links = new ArrayList<>();

        if (authPolicy.canCreateListing(user)) {
            links.add(linkTo(methodOn(AuctionRESTController.class)
                    .createAuction(null, null))
                    .withRel("create-auction"));
        }

        return links;
    }

    /**
     * Returns the listing-related HATEOAS links that are publicly accessible.
     *
     * <p>These links do not require user authentication or specific system permissions.
     * Included links point to general operations such as finding listings by ID,
     * retrieving all listings, and filtering listings by genre, publisher, or publication.</p>
     *
     * @return a list of public HATEOAS links
     */
    public List<Link> getPublicLinks() {

        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(ListingRESTController.class)
                .findById(null))
                .withRel("find-by-id"));

        links.add(linkTo(methodOn(ListingRESTController.class)
                .findAll(null, null, null, null, null))
                .withRel("find-all"));

        links.add(linkTo(methodOn(ListingRESTController.class)
                .getAllDirectSaleByGenre(null))
                .withRel("get-all-direct-sale-by-genre"));

        links.add(linkTo(methodOn(ListingRESTController.class)
                .getAllDirectSaleByPublisher(null))
                .withRel("get-all-direct-sale-by-publisher"));

        links.add(linkTo(methodOn(ListingRESTController.class)
                .findListingsByPublication(null))
                .withRel("get-listings-by-publication"));

        return links;
    }

    /**
     * Aggregates and returns all HATEOAS links (authenticated, public, and auction)
     * available for the specified user.
     *
     * @param userId the identifier of the user requesting the links
     * @return a comprehensive list of all applicable HATEOAS links for the user
     * @throws InstanceNotFoundException if no user exists with the given identifier
     * @throws NullPointerException      if the provided {@code userId} is null
     */
    @Override
    public List<Link> getLinks(UserId userId) throws InstanceNotFoundException {

        List<Link> links = new ArrayList<>();
        links.addAll(getAuthenticatedUserLinks(userId));
        links.addAll(getPublicLinks());
        links.addAll(getAuctionLinks(userId));

        return links;
    }

    /**
     * Resolves and retrieves a {@link User} entity based on the provided {@link UserId}.
     *
     * @param userId the identifier of the user to retrieve
     * @return the resolved {@code User} entity
     * @throws InstanceNotFoundException if the user cannot be found in the repository
     */
    private User resolveUser(UserId userId) throws InstanceNotFoundException {
        return userRepo.findByID(userId).orElseThrow(() ->new InstanceNotFoundException("User not found"));
    }
}