package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.CityRESTController;
import TOPSECRET.controller.controllerREST.root.authorizationPolicy.CityAuthorizationPolicy;
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
 * Provides HATEOAS links related to City operations.
 *
 * <p>This class generates links according to the permissions of the authenticated user,
 * which rules are defined in {@link CityAuthorizationPolicy}.</p>
 */
@Component
public class CityLinkProvider {

    private final CityAuthorizationPolicy policy;
    private final IUserRepository repository;

    /**
     * Creates a new City link provider.
     *
     * @param policy authorization policy used to verify user permissions
     * @param repository repository used to retrieve users
     */
    public CityLinkProvider (CityAuthorizationPolicy policy, IUserRepository repository){
        this.policy = policy;
        this.repository = repository;
    }

    /**
     * Returns the HATEOAS links available for a given user.
     *
     * <p>If the user has the authorization to create a City,
     * a link to the creation endpoint is included.</p>
     *
     * @param id identifier of the user whose permissions are verified
     * @return list of links available for the user
     */
    public List<Link> getLinks (UserId id){

        List<Link> links = new ArrayList<>();

        User user = repository.findByID(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if(policy.canCreateCity(user)){
            links.add(linkTo(methodOn(CityRESTController.class)
                    .createCity(null, id))
                    .withRel("create-city"));
        }
        return links;
    }
}