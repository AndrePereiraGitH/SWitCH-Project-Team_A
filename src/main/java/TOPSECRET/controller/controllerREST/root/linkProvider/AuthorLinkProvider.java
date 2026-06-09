package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.AuthorRESTController;
import TOPSECRET.controller.controllerREST.root.authorizationPolicy.AuthorAuthorizationPolicy;
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
 * {@link AuthorRESTController} endpoints.
 *
 * <p>Links are grouped into two categories:</p>
 * <ul>
 *   <li><b>Public</b> — always included; no authentication required.</li>
 *   <li><b>Private</b> — conditionally included based on the user's permissions.</li>
 * </ul>
 */
@Component
public class AuthorLinkProvider {

    private final AuthorAuthorizationPolicy policy;
    private final IUserRepository repository;

    /**
     * Creates a new {@code AuthorLinkProvider}.
     *
     * @param policy     the authorization policy used to determine which links to include;
     *                   must not be {@code null}
     * @param repository the repository used to look up the requesting user;
     *                   must not be {@code null}
     */
    public AuthorLinkProvider(AuthorAuthorizationPolicy policy, IUserRepository repository) {
        this.policy = policy;
        this.repository = repository;
    }

    /**
     * Generates a list of public HATEOAS navigation links that are always available,
     * regardless of the requesting user's identity or permissions.
     *
     * <p>The following links are always included:</p>
     * <ul>
     *   <li>{@code find-author} — retrieve a specific author by ID.</li>
     *   <li>{@code find-all} — retrieve all authors.</li>
     * </ul>
     *
     * @return a list of public {@link Link} instances; never {@code null}, never empty
     */
    public List<Link> getPublicLinks() {
        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(AuthorRESTController.class)
                .findById(null))
                .withRel("find-author"));

        links.add(linkTo(methodOn(AuthorRESTController.class)
                .findAll())
                .withRel("find-all"));

        return links;
    }

    /**
     * Generates a list of private HATEOAS navigation links based on the
     * requesting user's permissions.
     *
     * <p>The following link is conditionally included:</p>
     * <ul>
     *   <li>{@code create-author} — register a new author in the system.</li>
     * </ul>
     *
     * @param userId the identifier of the requesting user; must not be {@code null}
     * @return a list of private {@link Link} instances; never {@code null}, may be empty
     * @throws NullPointerException      if {@code userId} is {@code null}
     * @throws InstanceNotFoundException if no user is found for the given {@code userId}
     */
    public List<Link> getPrivateLinks(UserId userId) throws InstanceNotFoundException {
        Objects.requireNonNull(userId, "UserId cannot be null");

        List<Link> links = new ArrayList<>();

        User user = resolveUser(userId);

        if (policy.canCreateAuthor(user)) {
            links.add(linkTo(methodOn(AuthorRESTController.class)
                    .createAuthor(null, null))
                    .withRel("create-author"));
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
