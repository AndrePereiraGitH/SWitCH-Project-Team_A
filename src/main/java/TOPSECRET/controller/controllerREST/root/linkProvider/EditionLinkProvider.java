package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.EditionRESTController;
import TOPSECRET.controller.controllerREST.root.RootLinkProvider;
import TOPSECRET.controller.controllerREST.root.authorizationPolicy.EditionAuthorizationPolicy;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.PublisherID;
import TOPSECRET.domain.ValueObjects.UserId;
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
 * {@link EditionRESTController} endpoints.
 *
 * <p>Links are grouped into two categories:</p>
 * <ul>
 *   <li><b>Public</b> — always included; no authentication required.</li>
 *   <li><b>Private</b> — conditionally included based on the user's permissions.</li>
 * </ul>
 */
@Component
public class EditionLinkProvider implements RootLinkProvider {

    private final EditionAuthorizationPolicy policy;
    private final IUserRepository repository;

    /**
     * Creates a new {@code EditionLinkProvider}.
     *
     * @param policy     the authorization policy used to determine which links to include;
     *                   must not be {@code null}
     * @param repository the repository used to look up the requesting user;
     *                   must not be {@code null}
     */
    public EditionLinkProvider(EditionAuthorizationPolicy policy, IUserRepository repository) {
        this.policy = policy;
        this.repository = repository;
    }

    /**
     * Generates a list of public HATEOAS navigation links that are always available,
     * regardless of the requesting user's identity or permissions.
     *
     * <p>The following links are always included:</p>
     * <ul>
     *   <li>{@code find-edition} — retrieve a specific edition by ID.</li>
     *   <li>{@code find-all-editions} — retrieve all editions.</li>
     * </ul>
     *
     * @return a list of public {@link Link} instances; never {@code null}, never empty
     */
    public List<Link> getPublicLinks() {
        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(EditionRESTController.class)
                .findById(null))
                .withRel("find-edition"));

        links.add(linkTo(methodOn(EditionRESTController.class)
                .findAll())
                .withRel("find-all-editions"));

        links.add(linkTo(methodOn(EditionRESTController.class)
                .findByPublisherId(null))
                .withRel("find-by-publisher-id"));

        links.add(linkTo(methodOn(EditionRESTController.class)
                .findByPublicationTypeId(null))
                .withRel("find-by-publication-type-id"));

        return links;
    }

    /**
     * Generates a list of private HATEOAS navigation links based on the
     * requesting user's permissions.
     *
     * <p>The following link is conditionally included:</p>
     * <ul>
     *   <li>{@code create-edition} — register a new edition in the system.</li>
     * </ul>
     *
     * @param userId the identifier of the requesting user; must not be {@code null}
     * @return a list of private {@link Link} instances; never {@code null}, may be empty
     * @throws NullPointerException      if {@code userId} is {@code null}
     * @throws InstanceNotFoundException if no user is found for the given {@code userId}
     */
    public List<Link> getCreateLinks(UserId userId) throws InstanceNotFoundException {
        Objects.requireNonNull(userId, "UserId cannot be null");

        List<Link> links = new ArrayList<>();

        User user = resolveUser(userId);

        if (policy.canCreateEdition(user)) {
            links.add(linkTo(methodOn(EditionRESTController.class)
                    .createEdition(null, null))
                    .withRel("create-edition"));
        }

        return links;
    }


    /**
     * Resolves a {@link User} from the repository by the given {@link UserId}.
     *
     * @param userId the identifier of the user to resolve; must not be {@code null}
     * @return the resolved {@link User}
     * @throws InstanceNotFoundException if no user is found for the given {@code userId}
     */
    private User resolveUser(UserId userId) throws InstanceNotFoundException {
        return repository.findByID(userId)
                .orElseThrow(() -> new InstanceNotFoundException("User not found"));
    }

    @Override
    public List<Link> getLinks(UserId userId) throws InstanceNotFoundException {
        List<Link> links = new ArrayList<>();

        links.addAll(getPublicLinks());
        links.addAll(getCreateLinks(userId));

        return links;
    }
}
