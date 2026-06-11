package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.PublisherRESTController;
import TOPSECRET.controller.controllerREST.root.RootLinkProvider;
import TOPSECRET.controller.controllerREST.root.authorizationPolicy.PublisherAuthorizationPolicy;
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
 * Component responsible for providing HATEOAS links related to Publisher operations.
 *
 * <p>Links are generated based on the user's authorization level and
 * grouped into two categories:</p>
 * <ul>
 *   <li><b>Public</b> — always included; no authorization required.</li>
 *   <li><b>Admin</b> — included when the user is authorized for admin operations.</li>
 * </ul>
 */
@Component
public class PublisherLinkProvider implements RootLinkProvider {

    private final PublisherAuthorizationPolicy authPolicy;
    private final IUserRepository userRepo;

    /**
     * Creates a new {@code PublisherLinkProvider}.
     *
     * @param authPolicy the authorization policy used to determine which links to include;
     *                   must not be {@code null}
     * @param userRepo   the repository used to look up the requesting user;
     *                   must not be {@code null}
     */
    @Autowired
    public PublisherLinkProvider(PublisherAuthorizationPolicy authPolicy, IUserRepository userRepo) {
        Objects.requireNonNull(authPolicy, "authPolicy cannot be null");
        Objects.requireNonNull(userRepo, "userRepo cannot be null");
        this.authPolicy = authPolicy;
        this.userRepo = userRepo;
    }

    /**
     * Generates a list of public HATEOAS navigation links that are always available,
     * regardless of the requesting user's identity or permissions.
     *
     * <p>The following links are always included:</p>
     * <ul>
     *   <li>{@code get-all-publishers} — retrieve all publishers in the system.</li>
     *   <li>{@code get-publisher} — retrieve a publisher by its unique identifier.</li>
     * </ul>
     *
     * @return a list of public {@link Link} instances; never {@code null}, never empty
     */
    public List<Link> getPublicLinks() {
        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(PublisherRESTController.class)
                .getAllPublishers())
                .withRel("get-all-publishers"));

        links.add(linkTo(methodOn(PublisherRESTController.class)
                .getPublisher(null))
                .withRel("get-publisher"));

        return links;
    }

    /**
     * Generates a list of admin-scoped HATEOAS navigation links based on the
     * requesting user's permissions.
     *
     * <p>The following link is conditionally included:</p>
     * <ul>
     *   <li>{@code create-publisher} — create a new publisher in the system.</li>
     * </ul>
     *
     * @param userId the identifier of the requesting user; must not be {@code null}
     * @return a list of admin {@link Link} instances; never {@code null}, may be empty
     * @throws NullPointerException      if {@code userId} is {@code null}
     * @throws InstanceNotFoundException if no user is found for the given {@code userId}
     */
    public List<Link> getAdminLinks(UserId userId) throws InstanceNotFoundException {
        Objects.requireNonNull(userId, "UserId cannot be null");

        List<Link> links = new ArrayList<>();

        User user = userRepo.findByID(userId)
                .orElseThrow(() -> new InstanceNotFoundException("User not found"));

        if (authPolicy.canCreatePublisher(user)) {
            links.add(linkTo(methodOn(PublisherRESTController.class)
                    .createPublisher(null, null))
                    .withRel("create-publisher"));
        }

        return links;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Aggregates links from both public and admin groups, in that order.</p>
     */
    @Override
    public List<Link> getLinks(UserId userId) throws InstanceNotFoundException {
        List<Link> links = new ArrayList<>();
        links.addAll(getPublicLinks());
        links.addAll(getAdminLinks(userId));
        return links;
    }
}