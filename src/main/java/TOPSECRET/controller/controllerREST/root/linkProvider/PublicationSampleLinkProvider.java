package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.PublicationSampleRESTController;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Provider responsible for generating HATEOAS navigation links for
 * {@link PublicationSampleRESTController} endpoints.
 *
 * <p>Links are generated based on the requesting user's identity and permissions.
 * Fixed navigational links are always included, while conditional links are only
 * added when the user is authorized for the corresponding operation.</p>
 */
@Component
public class PublicationSampleLinkProvider {

    private final PublicationSampleAuthorizationPolicy policy;
    private final IUserRepository userRepository;

    /**
     * Creates a new {@code PublicationSampleLinkProvider}.
     *
     * @param policy         the authorization policy used to determine which links to include
     * @param userRepository the repository used to look up the requesting user
     */
    @Autowired
    public PublicationSampleLinkProvider(PublicationSampleAuthorizationPolicy policy,
                                         IUserRepository userRepository) {
        this.policy = policy;
        this.userRepository = userRepository;
    }

    /**
     * Generates a list of HATEOAS navigation links for the given user.
     *
     * <p>The following links are always included:</p>
     * <ul>
     *   <li>{@code find-by-id} — retrieve a publication sample by its ID.</li>
     *   <li>{@code find-all} — retrieve all publication samples.</li>
     *   <li>{@code find-by-bdId} — retrieve publication samples by bibliographical data ID.</li>
     *   <li>{@code find-by-editionId} — retrieve publication samples by edition ID.</li>
     * </ul>
     *
     * <p>The following links are conditionally included based on the user's permissions:</p>
     * <ul>
     *   <li>{@code create-publication-sample} — create a new publication sample.</li>
     *   <li>{@code find-by-collectionId} — retrieve publication samples by collection ID.</li>
     *   <li>{@code find-in-library} — retrieve user publications assigned to a library.</li>
     *   <li>{@code find-not-in-library} — retrieve user publications not assigned to any library.</li>
     *   <li>{@code find-available-in-library} — retrieve available publications in the user's library.</li>
     *   <li>{@code find-available-for-collection} — retrieve publications eligible for a collection.</li>
     *   <li>{@code add-to-library} — assign a publication sample to the user's library.</li>
     *   <li>{@code assign-to-collection} — assign a publication sample to a collection.</li>
     *   <li>{@code find-by-bdIds} — retrieve publication samples by multiple bibliographical data IDs.</li>
     * </ul>
     *
     * @param userId the identifier of the requesting user; must not be {@code null}
     * @return a list of {@link Link} instances; never {@code null}, may vary based on permissions
     * @throws InstanceNotFoundException if no user is found for the given {@code userId}
     */
    public List<Link> getPublicationSampleLinks(UserId userId) throws InstanceNotFoundException {

        List<Link> links = new ArrayList<>();

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new InstanceNotFoundException("User not found"));

        // ---- With no policy ----

        links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                .findById(null, null))
                .withRel("find-by-id"));

        links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                .findAll(null))
                .withRel("find-all"));

        links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                .findByBibliographicalDataId(null, null))
                .withRel("find-by-bdId"));

        links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                .findByEditionIdIn(null, null))
                .withRel("find-by-editionId"));

        links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                .findByBibliographicalDataIdIn(null, null))
                .withRel("find-by-bdIds"));

        // ---- Policy ----

        if (policy.canCreatePublicationSample(user)) {
            links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                    .createPublicationSample(null, null))
                    .withRel("create-publication-sample"));
        }

        if (policy.canFindByCollectionOfPublicationsId(user)) {
            links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                    .findByCollectionOfPublicationsId(null, null))
                    .withRel("find-by-collectionId"));
        }

        if (policy.canFindByUserIdAndLibraryIdIsNotNull(user)) {
            links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                    .findByUserIdAndLibraryIdIsNotNull(null))
                    .withRel("find-in-library"));
        }

        if (policy.canFindByUserIdAndLibraryIdIsNull(user)) {
            links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                    .findByUserIdAndLibraryIdIsNull(null))
                    .withRel("find-not-in-library"));
        }

        if (policy.canFindByLibraryIdAndPublicationStatusNotIn(user)) {
            links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                    .findByLibraryIdAndPublicationStatusNotIn(null))
                    .withRel("find-available-in-library"));
        }

        if (policy.canFindAvailableForCollection(user)) {
            links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                    .findAvailableForCollection(null, null))
                    .withRel("find-available-for-collection"));
        }

        if (policy.canAddPublicationToLibrary(user)) {
            links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                    .addPublicationToLibrary(null, null))
                    .withRel("add-to-library"));
        }

        if (policy.canAssignPublicationToCollection(user)) {
            links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                    .assignPublicationToCollection(null, null, null))
                    .withRel("assign-to-collection"));
        }

        return links;
    }
}