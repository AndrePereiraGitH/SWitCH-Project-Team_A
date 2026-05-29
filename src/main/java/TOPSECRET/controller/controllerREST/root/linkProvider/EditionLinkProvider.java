package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.EditionRESTController;
import TOPSECRET.controller.controllerREST.root.authorizationPolicy.EditionAuthorizationPolicy;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Provides HATEOAS links related to Edition operations.
 *
 * <p>This class generates links dynamically according to the permissions
 * of the authenticated user. The available links depend on the
 * authorization rules defined in {@link EditionAuthorizationPolicy}.</p>
 */
@Component
public class EditionLinkProvider {

    private final EditionAuthorizationPolicy policy;
    private final IUserRepository repository;

    /**
     * Creates a new Edition link provider.
     *
     * @param policy     authorization policy used to validate user permissions
     * @param repository repository used to retrieve users
     */
    public EditionLinkProvider(EditionAuthorizationPolicy policy, IUserRepository repository) {
        this.policy = policy;
        this.repository = repository;
    }

    /**
     * Returns the HATEOAS links available for a given user.
     *
     * <p>If the user has permission to create an Edition,
     * a link to the creation endpoint is added.</p>
     *
     * @param id identifier of the user whose permissions are evaluated
     * @return list of links available for the user
     * @throws IllegalArgumentException if the user is not found
     */
    public List<Link> getLinks(UserId id) {
        List<Link> links = new ArrayList<>();
        User user = repository.findByID(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (policy.canCreateEdition(user)) {
            links.add(linkTo(methodOn(EditionRESTController.class)
                    .createEdition(null, null))
                    .withRel("create-edition"));
        }
        return links;
    }
}
