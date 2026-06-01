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
 * Navigational links pointing to available endpoints are always included, while
 * the {@code create-publication-sample} link is only added when the user is
 * authorized to create publication samples.</p>
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
     *   <li>{@code find-by-id} — link to retrieve a publication sample by its ID.</li>
     *   <li>{@code find-all} — link to retrieve all publication samples.</li>
     *   <li>{@code find-by-bdId} — link to retrieve publication samples by bibliographical data ID.</li>
     *   <li>{@code find-by-editionId} — link to retrieve publication samples by edition ID.</li>
     * </ul>
     *
     * <p>The following link is conditionally included:</p>
     * <ul>
     *   <li>{@code create-publication-sample} — included only if the user is authorized
     *       to create publication samples.</li>
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

        if (policy.canCreatePublicationSample(user)) {
            links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                    .createPublicationSample(null, null))
                    .withRel("create-publication-sample"));
        }

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

        return links;
    }
}