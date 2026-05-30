package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.AuthorRESTController;
import TOPSECRET.controller.controllerREST.root.authorizationPolicy.AuthorAuthorizationPolicy;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Provides HATEOAS links related to Author operations.
 *
 * <p>This class generates links dynamically according to the permissions
 * of the authenticated user. The available links depend on the
 * authorization rules defined in {@link AuthorAuthorizationPolicy}.</p>
 */
@Component
public class AuthorLinkProvider {

    private final AuthorAuthorizationPolicy policy;
    private final IUserRepository repository;

    /**
     * Creates a new Author link provider.
     *
     * @param policy     authorization policy used to validate user permissions
     * @param repository repository used to retrieve users
     */
    public AuthorLinkProvider(AuthorAuthorizationPolicy policy, IUserRepository repository) {
        this.policy = policy;
        this.repository = repository;
    }

    /**
     * Returns the HATEOAS links available for a given user.
     *
     * <p>If the user has permission to add an Author,
     * a link to the addition endpoint is added.</p>
     *
     * @param id identifier of the user whose permissions are evaluated
     * @return list of links available for the user
     */
//    public List<Link> getLinks(UserId id) {
//        List<Link> links = new ArrayList<>();
//        User user = repository.findByID(id)
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//        if (policy.canCreateAuthor(user)) {
//            links.add(linkTo(methodOn(AuthorRESTController.class)
//                    .createAuthor(null, id))
//                    .withRel("create-author"));
//        }
//        return links;
//    }
}