package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.UserRESTController;
import TOPSECRET.controller.controllerREST.root.RootLinkProvider;
import TOPSECRET.controller.controllerREST.root.authorizationPolicy.UserAuthorizationPolicy;
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
 * Provides HATEOAS links related to User operations.
 *
 * <p>Generates links dynamically based on the permissions of the requesting user.
 * Implements {@link RootLinkProvider} so the root endpoint can aggregate
 * user-related links alongside all other resource links.</p>
 *
 * <p>Links are grouped into two categories:</p>
 * <ul>
 *   <li><b>Public</b> — always included; no authorization required.</li>
 *   <li><b>Admin</b> — included only when the user has the ADMIN role.</li>
 * </ul>
 */
@Component
public class UserLinkProvider implements RootLinkProvider {

    private final UserAuthorizationPolicy authPolicy;
    private final IUserRepository userRepository;

    /**
     * Creates a new {@code UserLinkProvider}.
     *
     * @param authPolicy     the authorization policy used to validate user permissions
     * @param userRepository the repository used to retrieve users
     */
    @Autowired
    public UserLinkProvider(UserAuthorizationPolicy authPolicy, IUserRepository userRepository) {
        this.authPolicy = authPolicy;
        this.userRepository = userRepository;
    }

    /**
     * Generates public HATEOAS links available to any caller,
     * regardless of the requesting user's identity or permissions.
     *
     * <p>The following links are always included:</p>
     * <ul>
     *   <li>{@code get-user} — retrieve a user by their ID.</li>
     *   <li>{@code get-all-users} — retrieve all users.</li>
     * </ul>
     *
     * @return a list of public {@link Link} instances; never {@code null}
     */
    public List<Link> getPublicLinks() {
        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(UserRESTController.class)
                .getUser(null))
                .withRel("get-user"));

        links.add(linkTo(methodOn(UserRESTController.class)
                .getAllUsers())
                .withRel("get-all-users"));

        return links;
    }

    /**
     * Generates admin-scoped HATEOAS links based on the requesting user's permissions.
     *
     * <p>The following link is conditionally included:</p>
     * <ul>
     *   <li>{@code register-user} — register a new user (ADMIN only).</li>
     * </ul>
     *
     * @param userId the identifier of the requesting user; must not be {@code null}
     * @return a list of admin {@link Link} instances; never {@code null}, may be empty
     * @throws InstanceNotFoundException if no user is found for the given {@code userId}
     */
    public List<Link> getAdminLinks(UserId userId) throws InstanceNotFoundException {
        Objects.requireNonNull(userId, "UserId cannot be null");

        List<Link> links = new ArrayList<>();
        User user = resolveUser(userId);

        if (authPolicy.canRegisterUser(user)) {
            links.add(linkTo(methodOn(UserRESTController.class)
                    .registerUser(null, null))
                    .withRel("register-user"));
        }

        return links;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Aggregates links from public and admin groups.</p>
     */
    @Override
    public List<Link> getLinks(UserId userId) throws InstanceNotFoundException {
        List<Link> links = new ArrayList<>();

        links.addAll(getPublicLinks());
        links.addAll(getAdminLinks(userId));

        return links;
    }

    /**
     * Returns HATEOAS links scoped to a specific user resource.
     *
     * <p>Always includes a link to retrieve the user by ID.
     * If the user has the ADMIN role, a registration link is also included.</p>
     *
     * @param userId the identifier of the user whose permissions are evaluated
     * @return list of {@link Link} available for the user
     */
    public List<Link> getLinksForUsers(UserId userId) {
        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(UserRESTController.class)
                .getUser(userId.getUserId()))
                .withRel("get-user"));

        userRepository.findByID(userId).ifPresent(user -> {
            if (authPolicy.canRegisterUser(user)) {
                links.add(linkTo(methodOn(UserRESTController.class)
                        .registerUser(null, null))
                        .withRel("register-user"));
            }
        });

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
        return userRepository.findByID(userId)
                .orElseThrow(() -> new InstanceNotFoundException("User not found: " + userId));
    }
}