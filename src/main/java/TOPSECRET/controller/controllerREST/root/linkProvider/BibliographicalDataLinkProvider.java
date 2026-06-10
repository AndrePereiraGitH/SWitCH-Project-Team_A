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
     * Creates and returns the collection of HATEOAS links available to a
     * registered user for bibliographical data resources.
     * <p>
     * This method includes all public bibliographical data links and, depending
     * on the user's authorization level, may also include links for privileged
     * actions such as creating bibliographical data entries.
     *
     * @param userId the identifier of the registered user whose permissions
     *               will be evaluated
     * @return a {@link List} of {@link Link} objects containing the endpoints
     *         accessible to the specified user
     */
    public List<Link> getRegisteredUserLinksForBibliographicalData(UserId userId){

        List<Link> links = getPublicLinksForBibliographicalData();

        userRepo.findByID(userId).ifPresent(user -> {

            // Authorized user actions
            if (authorizationPolicy.canCreateBibliographicalData(user)) {
                links.add(linkTo(methodOn(BibliographicalDataRESTController.class)
                        .createBibliographicalData(null, null))
                        .withRel("create-bibliographical-data"));
            }
        });

        return links;
    }

    /**
     * Creates and returns the collection of public HATEOAS links related to
     * bibliographical data resources.
     * <p>
     * The generated links provide access to:
     * <ul>
     *   <li>Retrieving all bibliographical data entries</li>
     *   <li>Retrieving a bibliographical data entry by its identifier</li>
     * </ul>
     *
     * @return a {@link List} of {@link Link} objects containing the available
     *         public bibliographical data endpoints
     */
    public List<Link> getPublicLinksForBibliographicalData (){

        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(BibliographicalDataRESTController.class)
                .getAllBibliographicalData())
                .withRel("get-bibliographical-data"));

        links.add(linkTo(methodOn(BibliographicalDataRESTController.class)
                .getBibliographicalDataById(null))
                .withRel("get-bibliographical-data-by-id"));

        return links;
    }
}
