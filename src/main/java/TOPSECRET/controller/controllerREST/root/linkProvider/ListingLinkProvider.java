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
     *                   listing actions the user can perform
     * @param userRepo   the repository used to retrieve users
     */
    @Autowired
    public ListingLinkProvider(ListingAuthorizationPolicy authPolicy, IUserRepository userRepo) {
        this.authPolicy = authPolicy;
        this.userRepo = userRepo;
    }

    /**
     * Returns the listing-related HATEOAS links available for the given user.
     *
     * <p>If the user is allowed to create listings, a {@code create-listing}
     * link is included in the returned list. If the user does not have
     * permission, an empty list is returned.</p>
     *
     * @param userId the identifier of the user whose listing links should be generated
     * @return a list of HATEOAS links available to the user
     * @throws InstanceNotFoundException if no user exists with the given identifier
     */
    public List<Link> getOwnerLinks(UserId userId) throws InstanceNotFoundException {

        Objects.requireNonNull(userId, "UserId cannot be null");

        List<Link> links = new ArrayList<>();
        User user = userRepo.findByID(userId).orElseThrow(() -> new InstanceNotFoundException("User not found"));

        if (authPolicy.canCreateListing(user)) {
            links.add(linkTo(methodOn(ListingRESTController.class)
                    .createListing(null, null))
                    .withRel("create-listing"));

        }

        return links;
    }


    public List<Link> getAuctionLinks(UserId userId) throws InstanceNotFoundException {
        User user = userRepo.findByID(userId).orElseThrow(() -> new InstanceNotFoundException("User not found"));

        List<Link> links = new ArrayList<>();

        if (authPolicy.canCreateListing(user)) {
            links.add(linkTo(methodOn(AuctionRESTController.class)
                    .createAuction(null, null))
                    .withRel("create-auction"));

        }


        return links;
    }

    public List<Link> getPublicLinks() {

        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(ListingRESTController.class)
                .findById(null))
                .withRel("find-by-id"));

        links.add(linkTo(methodOn(ListingRESTController.class)
                .findAll())
                .withRel("find-all"));

        links.add(linkTo(methodOn(ListingRESTController.class)
                .getAllDirectSaleByGenre(null))
                .withRel("get-all-direct-sale-by-genre"));

        links.add(linkTo(methodOn(ListingRESTController.class)
                .getAllDirectSaleByPublisher(null))
                .withRel("get-all-direct-sale-by-publisher"));

        return links;

    }

    @Override
    public List<Link> getLinks(UserId userId) throws InstanceNotFoundException {

        List<Link> links = new ArrayList<>();
        links.addAll(getOwnerLinks(userId));
        links.addAll(getPublicLinks());
        links.addAll(getAuctionLinks(userId));

        return links;
    }
}
