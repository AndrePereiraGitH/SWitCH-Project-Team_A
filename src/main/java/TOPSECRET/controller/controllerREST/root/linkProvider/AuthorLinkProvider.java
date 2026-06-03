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

@Component
public class AuthorLinkProvider {

    private final AuthorAuthorizationPolicy policy;
    private final IUserRepository repository;

    public AuthorLinkProvider(AuthorAuthorizationPolicy policy, IUserRepository repository) {
        this.policy = policy;
        this.repository = repository;
    }

    /**
     * Returns HATEOAS links for a response about a specific author.
     * Always includes a self link to that author. Conditionally includes
     * a create-author link based on user permissions.
     */
    public List<Link> getLinks(UserId userId) {
        List<Link> links = new ArrayList<>();

        User user = repository.findByID(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (policy.canCreateAuthor(user)) {
            links.add(linkTo(methodOn(AuthorRESTController.class)
                    .createAuthor(null, null))
                    .withRel("create-author"));
        }

        links.add(linkTo(methodOn(AuthorRESTController.class)
                .findById(null, null))
                .withRel("find-author"));

        links.add(linkTo(methodOn(AuthorRESTController.class)
                .findAll(null))
                .withRel("find-all"));

        return links;
    }
}