package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.CountryRESTController;
import TOPSECRET.controller.controllerREST.root.RootLinkProvider;
import TOPSECRET.controller.controllerREST.root.authorizationPolicy.CountryAuthorizationPolicy;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * HATEOAS link provider for Country resources.
 *
 * <p>This component is responsible for exposing hypermedia navigation links
 * related to Country operations in a role-aware manner.</p>
 *
 * <p>Links are dynamically generated based on:
 * <ul>
 *     <li>Public operations available to all users</li>
 *     <li>Private operations restricted by user permissions</li>
 * </ul>
 * </p>
 *
 * <p>This provider is automatically discovered by Spring and aggregated
 * by the {@code RootController}, contributing to the API root entry point
 * following HATEOAS principles.</p>
 *
 * <p>The design separates link generation into:
 * <ul>
 *     <li>Public links (no authentication required)</li>
 *     <li>Private links (require authenticated user and authorization)</li>
 *     <li>Combined links (final aggregation for API root)</li>
 * </ul>
 * </p>
 */
@Component
public class CountryLinkProvider implements RootLinkProvider {

    private final CountryAuthorizationPolicy policy;
    private final IUserRepository repository;

    /**
     * Creates a new {@code CountryLinkProvider}.
     *
     * @param policy authorization policy used to determine access
     *               to protected country-related operations
     * @param repository repository used to retrieve user information
     *                  for authorization evaluation
     */
    public CountryLinkProvider(
            CountryAuthorizationPolicy policy,
            IUserRepository repository) {

        this.policy = policy;
        this.repository = repository;
    }

    // -------------------------------------------------------
    // PUBLIC LINKS (no authentication required)
    // -------------------------------------------------------
    /**
     * Returns hypermedia links for operations accessible to all users,
     * regardless of authentication or role.
     *
     * <p>These links represent entry-level navigation for the Country resource.</p>
     *
     * @return a list of publicly available {@link Link} instances
     */
    public List<Link> getPublicLinks() {

        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(CountryRESTController.class)
                .findById(null))
                .withRel("find-country-by-id"));

        links.add(linkTo(methodOn(CountryRESTController.class)
                .findAll())
                .withRel("get-all-countries"));

        return links;
    }

    // -------------------------------------------------------
    // PRIVATE LINKS (requires user + permissions)
    // -------------------------------------------------------
    /**
     * Returns hypermedia links for operations restricted by user permissions.
     *
     * <p>These links are only included when the user satisfies authorization
     * rules defined in {@link CountryAuthorizationPolicy}.</p>
     *
     * @param userId authenticated user used for authorization checks
     * @return a list of privileged {@link Link} instances; may be empty
     */
    public List<Link> getPrivateLinks(UserId userId) throws InstanceNotFoundException {

        User user = resolveUser(userId);

        List<Link> links = new ArrayList<>();

        if (policy.canRegisterCountry(user)) {
            links.add(linkTo(methodOn(CountryRESTController.class)
                    .registerCountry(null, null))
                    .withRel("register-country"));
        }

        return links;
    }

    // -------------------------------------------------------
    // COMBINED LINKS (used by RootController)
    // -------------------------------------------------------

    @Override
    public List<Link> getLinks(UserId id) throws InstanceNotFoundException {

        List<Link> links = new ArrayList<>();

        links.addAll(getPublicLinks());
        links.addAll(getPrivateLinks(id));

        return links;
    }

    // =========================================================================
    // Private helpers
    // =========================================================================

    /**
     * Resolves a {@link User} from the repository using the given {@link UserId}.
     *
     * <p>This helper centralizes user lookup logic so that repository access
     * is performed in a single location within the provider.</p>
     *
     * @param userId the identifier of the user to resolve; must not be {@code null}
     * @return the resolved {@link User}
     * @throws InstanceNotFoundException if no user exists with the given identifier
     */
    private User resolveUser(UserId userId) throws InstanceNotFoundException {

        return repository.findByID(userId)
                .orElseThrow(() ->
                        new InstanceNotFoundException("User not found: " + userId));
    }
}