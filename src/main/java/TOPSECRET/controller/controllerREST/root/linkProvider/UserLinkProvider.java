package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.UserRESTController;
import TOPSECRET.controller.controllerREST.root.authorizationPolicy.UserAuthorizationPolicy;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Provides HATEOAS links related to User operations.
 *
 * <p>Generates links dynamically based on the permissions of the requesting user.
 * The available links depend on the authorization rules defined in
 * {@link UserAuthorizationPolicy}.</p>
 */
@Component
public class UserLinkProvider {

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
     * Returns the HATEOAS links available for a given user.
     *
     * <p>Always includes a link to retrieve the user by ID.
     * If the user has permission to register new users, a registration link is also included.</p>
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
}