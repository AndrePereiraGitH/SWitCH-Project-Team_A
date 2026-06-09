package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.PublicationSampleRESTController;
import TOPSECRET.controller.controllerREST.root.RootLinkProvider;
import TOPSECRET.controller.controllerREST.root.authorizationPolicy.PublicationSampleAuthorizationPolicy;
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
 * Provider responsible for generating HATEOAS navigation links for
 * {@link PublicationSampleRESTController} endpoints.
 *
 * <p>Links are generated based on the requesting user's identity and permissions.
 * Public links are always included, while conditional links are only added when
 * the user is authorized for the corresponding operation.</p>
 *
 * <p>Links are grouped into four categories:</p>
 * <ul>
 *   <li><b>Public</b> — always included; no authorization required.</li>
 *   <li><b>Owner</b> — included when the user is authorized to create publication samples.</li>
 *   <li><b>Library</b> — included when the user is authorized for library operations.</li>
 *   <li><b>Collection</b> — included when the user is authorized for collection operations.</li>
 * </ul>
 */
@Component
public class PublicationSampleLinkProvider implements RootLinkProvider {

    private final PublicationSampleAuthorizationPolicy policy;
    private final IUserRepository userRepository;

    /**
     * Creates a new {@code PublicationSampleLinkProvider}.
     *
     * @param policy         the authorization policy used to determine which links to include;
     *                       must not be {@code null}
     * @param userRepository the repository used to look up the requesting user;
     *                       must not be {@code null}
     */
    @Autowired
    public PublicationSampleLinkProvider(PublicationSampleAuthorizationPolicy policy,
                                         IUserRepository userRepository) {
        this.policy = policy;
        this.userRepository = userRepository;
    }

    /**
     * Generates a list of public HATEOAS navigation links that are always available,
     * regardless of the requesting user's identity or permissions.
     *
     * <p>The following links are always included:</p>
     * <ul>
     *   <li>{@code find-by-id} — retrieve a publication sample by its ID.</li>
     *   <li>{@code find-all} — retrieve all publication samples.</li>
     *   <li>{@code find-by-bdId} — retrieve publication samples by bibliographical data ID.</li>
     *   <li>{@code find-by-editionIds} — retrieve publication samples by edition IDs.</li>
     *   <li>{@code find-by-bdIds} — retrieve publication samples by multiple bibliographical data IDs.</li>
     * </ul>
     *
     * @return a list of public {@link Link} instances; never {@code null}, never empty
     */
    public List<Link> getPublicLinks() {

        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                .findById(null))
                .withRel("find-by-id"));

        links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                .findAll())
                .withRel("find-all"));

        links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                .findByBibliographicalDataId(null))
                .withRel("find-by-bdId"));

        links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                .findByEditionIdIn(null))
                .withRel("find-by-editionIds"));

        links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                .findByBibliographicalDataIdIn(null))
                .withRel("find-by-bdIds"));

        return links;
    }

    /**
     * Generates a list of owner-scoped HATEOAS navigation links based on the
     * requesting user's permissions.
     *
     * <p>The following link is conditionally included:</p>
     * <ul>
     *   <li>{@code create-publication-sample} — create a new publication sample.</li>
     * </ul>
     *
     * @param userId the identifier of the requesting user; must not be {@code null}
     * @return a list of owner {@link Link} instances; never {@code null}, may be empty
     * @throws NullPointerException      if {@code userId} is {@code null}
     * @throws InstanceNotFoundException if no user is found for the given {@code userId}
     */
    public List<Link> getOwnerLinks(UserId userId) throws InstanceNotFoundException {
        Objects.requireNonNull(userId, "UserId cannot be null");

        List<Link> links = new ArrayList<>();

        User user = resolveUser(userId);

        if (policy.canCreatePublicationSample(user)) {
            links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                    .createPublicationSample(null, null))
                    .withRel("create-publication-sample"));
        }

        return links;
    }

    /**
     * Generates a list of library-scoped HATEOAS navigation links based on the
     * requesting user's permissions.
     *
     * <p>The following links are conditionally included:</p>
     * <ul>
     *   <li>{@code find-in-library} — retrieve user publications assigned to a library.</li>
     *   <li>{@code find-not-in-library} — retrieve user publications not assigned to any library.</li>
     *   <li>{@code find-available-in-library} — retrieve available publications in the user's library.</li>
     *   <li>{@code add-to-library} — assign a publication sample to the user's library.</li>
     * </ul>
     *
     * @param userId the identifier of the requesting user; must not be {@code null}
     * @return a list of library {@link Link} instances; never {@code null}, may be empty
     * @throws NullPointerException      if {@code userId} is {@code null}
     * @throws InstanceNotFoundException if no user is found for the given {@code userId}
     */
    public List<Link> getLibraryLinks(UserId userId) throws InstanceNotFoundException {
        Objects.requireNonNull(userId, "UserId cannot be null");

        List<Link> links = new ArrayList<>();

        User user = resolveUser(userId);

        if (policy.canFindPublicationsInLibrary(user)) {
            links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                    .findPublicationsInLibrary(null))
                    .withRel("find-in-library"));
        }

        if (policy.canFindPublicationsNotInLibrary(user)) {
            links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                    .findPublicationsNotInLibrary(null))
                    .withRel("find-not-in-library"));
        }

        if (policy.canFindByLibraryIdAndPublicationStatusNotIn(user)) {
            links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                    .findByLibraryIdAndPublicationStatusNotIn(null))
                    .withRel("find-available-in-library"));
        }

        if (policy.canAddPublicationToLibrary(user)) {
            links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                    .addPublicationToLibrary(null, null))
                    .withRel("add-to-library"));
        }

        return links;
    }

    /**
     * Generates a list of collection-scoped HATEOAS navigation links based on the
     * requesting user's permissions.
     *
     * <p>The following links are conditionally included:</p>
     * <ul>
     *   <li>{@code find-by-collectionId} — retrieve publication samples by collection ID.</li>
     *   <li>{@code find-available-for-collection} — retrieve publications eligible for a collection.</li>
     *   <li>{@code assign-to-collection} — assign a publication sample to a collection.</li>
     * </ul>
     *
     * @param userId the identifier of the requesting user; must not be {@code null}
     * @return a list of collection {@link Link} instances; never {@code null}, may be empty
     * @throws NullPointerException      if {@code userId} is {@code null}
     * @throws InstanceNotFoundException if no user is found for the given {@code userId}
     */
    public List<Link> getCollectionLinks(UserId userId) throws InstanceNotFoundException {
        Objects.requireNonNull(userId, "UserId cannot be null");

        List<Link> links = new ArrayList<>();

        User user = resolveUser(userId);

        if (policy.canFindByCollectionOfPublicationsId(user)) {
            links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                    .findByCollectionOfPublicationsId(null, null))
                    .withRel("find-by-collectionId"));
        }

        if (policy.canFindAvailableForCollection(user)) {
            links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                    .findAvailableForCollection(null, null))
                    .withRel("find-available-for-collection"));
        }

        if (policy.canAssignPublicationToCollection(user)) {
            links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                    .assignPublicationToCollection(null, null, null))
                    .withRel("assign-to-collection"));
        }

        return links;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Aggregates links from all four groups in order:
     * public, library, owner, and collection.</p>
     */
    @Override
    public List<Link> getLinks(UserId userId) throws InstanceNotFoundException {
        List<Link> links = new ArrayList<>();

        links.addAll(getPublicLinks());
        links.addAll(getLibraryLinks(userId));
        links.addAll(getOwnerLinks(userId));
        links.addAll(getCollectionLinks(userId));

        return links;
    }


    // =========================================================================
    // Private helpers
    // =========================================================================

    /**
     * Resolves a {@link User} from the repository by the given {@link UserId}.
     *
     * @param userId the identifier of the user to resolve; must not be {@code null}
     * @return the resolved {@link User}
     * @throws InstanceNotFoundException if no user is found for the given {@code userId}
     */
    private User resolveUser(UserId userId) throws InstanceNotFoundException {
        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new InstanceNotFoundException("User not found"));

        return user;
    }

}