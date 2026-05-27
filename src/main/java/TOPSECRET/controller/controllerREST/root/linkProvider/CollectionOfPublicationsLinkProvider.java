package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.CollectionOfPublicationsRESTController;
import TOPSECRET.controller.controllerREST.root.authorizationPolicy.CollectionOfPublicationsAuthorizationPolicy;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.hateoas.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


import java.util.ArrayList;
import java.util.List;

/**
 * Component responsible for dynamically generating HATEOAS links for
 * collection of publications-related operations, based on the authenticated
 * user's authorization policy.
 *
 * <p>Links are only included in the response if the user holds the required
 * role for the corresponding operation, as determined by
 * {@link CollectionOfPublicationsAuthorizationPolicy}.</p>
 */
@Component
public class CollectionOfPublicationsLinkProvider {

    private final CollectionOfPublicationsAuthorizationPolicy collectionPolicy;
    private final IUserRepository userRepo;

    /**
     * Constructs a new {@code CollectionOfPublicationsLinkProvider} with the required dependencies.
     *
     * @param collectionPolicy the authorization policy used to determine which links to expose;
     *                         must not be {@code null}
     * @param userRepo         the repository used to retrieve the authenticated user;
     *                         must not be {@code null}
     */
    @Autowired
    public CollectionOfPublicationsLinkProvider(
            CollectionOfPublicationsAuthorizationPolicy collectionPolicy, IUserRepository userRepo) {
        this.collectionPolicy = collectionPolicy;
        this.userRepo = userRepo;
    }

    /**
     * Returns a list of HATEOAS links available to the given user for collection of
     * publications-related operations.
     *
     * <p>Each link is conditionally included based on whether the user is authorized
     * to perform the corresponding operation, as evaluated by
     * {@link CollectionOfPublicationsAuthorizationPolicy}.</p>
     *
     * @param userId the identifier of the authenticated user; must not be {@code null}
     * @return a list of {@link Link} objects representing the permitted operations;
     *         may be empty if the user has no authorized operations
     * @throws IllegalArgumentException if no user is found for the given {@code userId}
     *
     */
    public List<Link> getLinksForCollectionOfPublications(UserId userId) {

        List<Link> links = new ArrayList<>();

        User user = userRepo.findByID(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        if (collectionPolicy.canCreateCollectionOfPublications(user)) {
            links.add(linkTo(methodOn(CollectionOfPublicationsRESTController.class)
                    .createCollectionOfPublications(null, null, null))
                    .withRel("create-collection-of-publications"));
        }

        return links;
    }
}
