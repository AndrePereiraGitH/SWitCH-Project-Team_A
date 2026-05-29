package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.BibliographicalDataRESTController;
import TOPSECRET.controller.controllerREST.root.authorizationPolicy.BibliographicalDataAuthorizationPolicy;
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
 * Provides HATEOAS links related to bibliographical data resources
 * according to the authenticated user's authorization level.
 *
 * <p>This component centralizes the creation of REST links exposed to clients,
 * ensuring that only actions permitted by the authorization policy are included
 * in the response.</p>
 *
 * <p>The generated links may include:</p>
 * <ul>
 *     <li>Publicly accessible bibliographical data retrieval operations</li>
 *     <li>User-authorized bibliographical data creation operations</li>
 * </ul>
 */
@Component
public class BibliographicalDataLinkProvider {

    private final BibliographicalDataAuthorizationPolicy authorizationPolicy;
    private final IUserRepository userRepo;

    /**
     * Constructs a {@code BibliographicalDataLinkProvider}.
     *
     * @param authPolicy the authorization policy used to validate user permissions
     * @param userRepo the repository used to retrieve user information
     */
    @Autowired
    public BibliographicalDataLinkProvider (BibliographicalDataAuthorizationPolicy authPolicy, IUserRepository userRepo) {
        this.authorizationPolicy = authPolicy;
        this.userRepo = userRepo;
    }

    /**
     * Generates a list of HATEOAS links available to the specified user.
     *
     * <p>The returned links depend on the user's authorization level:</p>
     * <ul>
     *     <li>All users receive a link for retrieving bibliographical data</li>
     *     <li>Authorized users additionally receive a link for creating
     *     bibliographical data</li>
     * </ul>
     *
     * @param userId the identifier of the user requesting available actions
     * @return a list of HATEOAS links available to the user
     */
    public List<Link> getLinksForBibliographicalData(UserId userId){

        List<Link> links = new ArrayList<>();

        // Public / universally available action
        links.add(linkTo(methodOn(BibliographicalDataRESTController.class)
                .getAllBibliographicalData(userId))
                .withRel("get-bibliographical-data"));

        userRepo.findByID(userId).ifPresent(user -> {

            // Authorized user actions
            if (authorizationPolicy.canCreateBibliographicalData(user)) {
                links.add(linkTo(methodOn(BibliographicalDataRESTController.class)
                        .createBibliographicalData(null, userId))
                        .withRel("create-bibliographical-data"));
            }
        });

        return links;
    }
}
