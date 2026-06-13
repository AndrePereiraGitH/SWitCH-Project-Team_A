package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.CityRESTController;
import TOPSECRET.controller.controllerREST.root.RootLinkProvider;
import TOPSECRET.controller.controllerREST.root.authorizationPolicy.CityAuthorizationPolicy;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
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
 * {@link CityRESTController} endpoints.
 *
 * <p>Links are generated based on the requesting user's identity and permissions.
 * Public links are always included, while conditional links are only added when
 * the user is authorized for the corresponding operation.</p>
 *
 * <p>Links are grouped into two categories:</p>
 * <ul>
 *   <li><b>Public</b> — always included; no authorization required.</li>
 *   <li><b>Conditional</b> — included when the user is authorized for the corresponding operation.</li>
 * </ul>
 */
@Component
public class CityLinkProvider implements RootLinkProvider {

    private final CityAuthorizationPolicy policy;
    private final IUserRepository repository;

    /**
     * Creates a new {@code CityLinkProvider}.
     *
     * @param policy     the authorization policy used to determine which links to include;
     *                   must not be {@code null}
     * @param repository the repository used to look up the requesting user;
     *                   must not be {@code null}
     */
    public CityLinkProvider (CityAuthorizationPolicy policy, IUserRepository repository){
        this.policy = policy;
        this.repository = repository;
    }

    /**
     * Aggregates all HATEOAS navigation links available to the requesting user.
     *
     * <p>Always includes public links (e.g. {@code cities}) and conditionally
     * includes restricted links based on the user's permissions (e.g. {@code create-city}).</p>
     *
     * @param userId the identifier of the requesting user; must not be {@code null}
     * @return a list of {@link Link} instances; never {@code null}, never empty
     * @throws NullPointerException      if {@code userId} is {@code null}
     * @throws InstanceNotFoundException if no user is found for the given {@code userId}
     */
    @Override
    public List<Link> getLinks(UserId userId) throws InstanceNotFoundException {
        Objects.requireNonNull(userId, "UserId cannot be null");

        List<Link> links = new ArrayList<>();
        links.addAll(getPublicLinks());
        links.addAll(getRestrictedLinks(userId));
        return links;
    }

    /**
     * Generates a list of public HATEOAS navigation links that are always available,
     * regardless of the requesting user's identity or permissions.
     *
     * <p>The following links are always included:</p>
     * <ul>
     *   <li>{@code cities} — retrieve all cities.</li>
     * </ul>
     *
     * @return a list of public {@link Link} instances; never {@code null}, never empty
     */
    public List<Link> getPublicLinks() {
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(CityRESTController.class)
                .getCities()).withRel("cities"));
        return links;
    }

    /**
     * Generates a list of HATEOAS navigation links based on the
     * requesting user's permissions.
     *
     * <p>The following link is conditionally included:</p>
     * <ul>
     *   <li>{@code create-city} — create a new city.</li>
     * </ul>
     *
     * @param userId the identifier of the requesting user; must not be {@code null}
     * @return a list of {@link Link} instances; never {@code null}, may be empty
     * @throws InstanceNotFoundException if no user is found for the given {@code userId}
     */
    private List<Link> getRestrictedLinks(UserId userId) throws InstanceNotFoundException {
        List<Link> links = new ArrayList<>();

        User user = resolveUser(userId);

        if (policy.canCreateCity(user)) {
            links.add(linkTo(methodOn(CityRESTController.class)
                    .createCity(null, userId))
                    .withRel("create-city"));
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
}