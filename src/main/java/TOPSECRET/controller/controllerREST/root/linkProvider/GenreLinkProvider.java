package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.GenreRESTController;
import TOPSECRET.controller.controllerREST.root.authorizationPolicy.GenreAuthorizationPolicy;
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
 * Provides HATEOAS links related to Genre operations.
 *
 * <p>This class generates links dynamically according to the permissions
 * of the authenticated user. The available links depend on the
 * authorization rules defined in {@link GenreAuthorizationPolicy}.</p>
 */
@Component
public class GenreLinkProvider {

    private final GenreAuthorizationPolicy policy;
    private final IUserRepository repository;

    /**
     * Creates a new Genre link provider.
     *
     * @param policy authorization policy used to validate user permissions
     * @param repository repository used to retrieve users
     */
    public GenreLinkProvider (GenreAuthorizationPolicy policy, IUserRepository repository){
        this.policy = policy;
        this. repository = repository;
    }

    /**
     * Returns the HATEOAS links available for a given user.
     *
     * <p>If the user has permission to add a Genre,
     * a link to the addition endpoint is added.</p>
     *
     * @param id identifier of the user whose permissions are evaluated
     * @return list of links available for the user
     */
    public List<Link> getLinks (UserId id){

        List<Link> links = new ArrayList<>();

        User user = repository.findByID(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if(policy.canCreateGenre(user)){
            links.add(linkTo(methodOn(GenreRESTController.class)
                    .createGenre(null, id))
                    .withRel("get-genre"));
        }

        return links;
    }
}
