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
 *   <li><b>Public</b> — always empty; no user-related links are universally public.</li>
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
     * Returns HATEOAS links scoped to a specific user resource.
     *
     * <p>Always includes a link to retrieve the user by ID.
     * If the user has the ADMIN role, a registration link is also included.</p>
     *
     */
    public List<Link> getLinksForUserResource(UserId requesterId, UserId targetUserId) throws InstanceNotFoundException {

        List<Link> links = new ArrayList<>();

        User requester = resolveUser(requesterId);

        if (authPolicy.canFindUserById(requester, targetUserId)) {
            links.add(linkTo(methodOn(UserRESTController.class)
                    .getUser(targetUserId.getUserId(), requesterId.getUserId()))
                    .withRel("get-user"));
        }

        if (authPolicy.canFindAllUsers(requester)) {
            links.add(linkTo(methodOn(UserRESTController.class)
                    .getAllUsers(requesterId.getUserId()))
                    .withRel("get-all-users"));
        }

        if (authPolicy.canRegisterUser(requester)) {
            links.add(linkTo(methodOn(UserRESTController.class)
                    .registerUser(null, requesterId.getUserId()))
                    .withRel("register-user"));
        }

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

    @Override
    public List<Link> getLinks(UserId requesterId) throws InstanceNotFoundException {

        Objects.requireNonNull(requesterId, "RequesterId cannot be null");

        List<Link> links = new ArrayList<>();

        User requester = resolveUser(requesterId);

        if (authPolicy.canFindAllUsers(requester)) {
            links.add(linkTo(methodOn(UserRESTController.class)
                    .getAllUsers(requesterId.getUserId()))
                    .withRel("get-all-users"));
        }

        if (authPolicy.canRegisterUser(requester)) {
            links.add(linkTo(methodOn(UserRESTController.class)
                    .registerUser(null, requesterId.getUserId()))
                    .withRel("register-user"));
        }return links;
    }
}