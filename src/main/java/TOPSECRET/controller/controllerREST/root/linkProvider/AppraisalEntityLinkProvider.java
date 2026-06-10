package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.AppraisalEntityRESTController;
import TOPSECRET.controller.controllerREST.root.RootLinkProvider;
import TOPSECRET.controller.controllerREST.root.authorizationPolicy.AppraisalEntityAuthorizationPolicy;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Provides HATEOAS links related to AppraisalEntity operations.
 *
 * <p>This class generates links dynamically according to the permissions
 * of the authenticated user. The available links depend on the
 * authorization rules defined in {@link AppraisalEntityAuthorizationPolicy}.</p>
 */
@Component
public class AppraisalEntityLinkProvider implements RootLinkProvider {

    private final AppraisalEntityAuthorizationPolicy policy;
    private final IUserRepository repository;

    /**
     * Creates a new AppraisalEntity link provider.
     *
     * @param policy authorization policy used to validate user permissions
     * @param repository repository used to retrieve users
     */
    public AppraisalEntityLinkProvider (AppraisalEntityAuthorizationPolicy policy, IUserRepository repository){
        this.policy = policy;
        this.repository = repository;
    }

    /**
     * Returns the HATEOAS links available for a given user.
     *
     * <p>If the user has permission to register an AppraisalEntity,
     * a link to the registration endpoint is added.</p>
     *
     * @param id identifier of the user whose permissions are evaluated
     * @return list of links available for the user
     */
    public List<Link> getAdminLinks (UserId id){

        List<Link> links = new ArrayList<>();

        User user = repository.findByID(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

        if(policy.canRegisterAppraisalEntity(user)){
            links.add(linkTo(methodOn(AppraisalEntityRESTController.class)
                    .registerAppraisalEntity(null, id.toString()))
                    .withRel("register-appraisal-entity"));
        }

        return links;
    }

    /**
     * Generates a list of public HATEOAS navigation links that are always available,
     * regardless of the requesting user's identity or permissions.
     *
     * <p>The following links are always included:</p>
     * <ul>
     *   <li>{@code find-by-id} — retrieve an appraisal entity by its ID.</li>
     *   <li>{@code find-all} — retrieve all appraisal entities.</li>
     * </ul>
     *
     * @return a list of public {@link Link} instances; never {@code null}, never empty
     */
    public List<Link> getPublicLinks() {

        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(AppraisalEntityRESTController.class)
                .findById(null))
                .withRel("find-by-id"));

        links.add(linkTo(methodOn(AppraisalEntityRESTController.class)
                .findAll())
                .withRel("find-all"));

        return links;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Aggregates links from all four groups in order:
     * public, library, owner, and collection.</p>
     */
    @Override
    public List<Link> getLinks(UserId userId) throws InstanceNotFoundException {
        List<Link> links = new ArrayList<>();

        links.addAll(getPublicLinks());
        links.addAll(getAdminLinks(userId));

        return links;
    }

}
