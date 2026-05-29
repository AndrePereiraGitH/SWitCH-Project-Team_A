package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.CountryRESTController;
import TOPSECRET.controller.controllerREST.root.authorizationPolicy.CountryAuthorizationPolicy;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Provides HATEOAS links related to Country operations.
 *
 * <p>This class generates links dynamically according to the permissions
 * of the authenticated user. The available links depend on the
 * authorization rules defined in {@link CountryAuthorizationPolicy}.</p>
 */
@Component
public class CountryLinkProvider {

    private final CountryAuthorizationPolicy policy;
    private final IUserRepository repository;

    /**
     * Creates a new Country link provider.
     *
     * @param policy authorization policy used to validate user permissions
     * @param repository repository used to retrieve users
     */
    public CountryLinkProvider(
            CountryAuthorizationPolicy policy,
            IUserRepository repository) {

        this.policy = policy;
        this.repository = repository;
    }

    /**
     * Returns the HATEOAS links available for a given user.
     *
     * @param id identifier of the user whose permissions are evaluated
     * @return list of links available for the user
     */
    public List<Link> getLinks(UserId id) {

        List<Link> links = new ArrayList<>();

        User user = repository.findByID(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

        // always available
        links.add(linkTo(methodOn(CountryRESTController.class)
                .getCountries())
                .withRel("get-all-countries"));

        if (policy.canRegisterCountry(user)) {

            links.add(linkTo(methodOn(CountryRESTController.class)
                    .registerCountry(null, null))
                    .withRel("register-country"));
        }

        return links;
    }
}