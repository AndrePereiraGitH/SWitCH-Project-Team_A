package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.CollectionOfPublicationsRESTController;
import TOPSECRET.controller.controllerREST.root.RootLinkProvider;
import TOPSECRET.controller.controllerREST.root.authorizationPolicy.CollectionOfPublicationsAuthorizationPolicy;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.hateoas.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.management.InstanceNotFoundException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
public class CollectionOfPublicationsLinkProvider implements RootLinkProvider {

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
     * Returns a list of public HATEOAS links that are always available,
     * regardless of the requesting user's identity or permissions.
     *
     * <p>The following links are always included:</p>
     * <ul>
     *   <li>{@code find-all-public} — retrieve all currently public collections.</li>
     *   <li>{@code find-public-by-genre} — retrieve public collections filtered by genre.</li>
     *   <li>{@code find-by-id} — retrieve a collection by its identifier.</li>
     * </ul>
     *
     * @return a list of public {@link Link} instances; never {@code null}, never empty
     */
    public List<Link> getPublicLinks() {

        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(CollectionOfPublicationsRESTController.class)
                .findAllPublicCollections())
                .withRel("find-all-public"));

        links.add(linkTo(methodOn(CollectionOfPublicationsRESTController.class)
                .findPublicCollectionsByGenreId(null))
                .withRel("find-public-by-genre"));

        links.add(linkTo(methodOn(CollectionOfPublicationsRESTController.class)
                .getCollectionById(null))
                .withRel("find-by-id"));

        return links;
    }

    /**
     * Returns a list of owner-scoped HATEOAS links based on the requesting
     * user's permissions.
     *
     * <p>The following links are conditionally included:</p>
     * <ul>
     *   <li>{@code create-collection-of-publications} — included when the user
     *       is authorized to create collections.</li>
     *   <li>{@code find-own-collections} — included when the user is authorized
     *       to view their own collections.</li>
     * </ul>
     *
     * @param userId the identifier of the requesting user; must not be {@code null}
     * @return a list of owner {@link Link} instances; never {@code null}, may be empty
     * @throws NullPointerException      if {@code userId} is {@code null}
     * @throws InstanceNotFoundException if no user is found for the given {@code userId}
     */
    public List<Link> getOwnerLinks(UserId userId) throws InstanceNotFoundException {
        Objects.requireNonNull(userId, "userId cannot be null");

        List<Link> links = new ArrayList<>();

        User user = userRepo.findByID(userId)
                .orElseThrow(() -> new InstanceNotFoundException("User not found: " + userId));

        if (collectionPolicy.canCreateCollectionOfPublications(user)) {
            links.add(linkTo(methodOn(CollectionOfPublicationsRESTController.class)
                    .createCollectionOfPublications(null, null))
                    .withRel("create-collection-of-publications"));
        }

        if (collectionPolicy.canViewOwnCollections(user)) {
            links.add(linkTo(methodOn(CollectionOfPublicationsRESTController.class)
                    .findCollectionsByOwnerId(null))
                    .withRel("find-own-collections"));
        }

        return links;
    }

    /**
     * Returns all HATEOAS links available to the given user for the
     * collection of publications resource.
     *
     * <p>Always includes public navigational links. Owner links are
     * conditionally included based on the user's role.</p>
     *
     * @param userId the identifier of the requesting user; must not be {@code null}
     * @return a list of {@link Link} instances available to the user;
     * never {@code null}, may be empty
     * @throws InstanceNotFoundException if the user cannot be resolved
     */
    @Override
    public List<Link> getLinks(UserId userId) throws InstanceNotFoundException {
        List<Link> links = new ArrayList<>();
        links.addAll(getPublicLinks());
        links.addAll(getOwnerLinks(userId));
        return links;
    }
}
