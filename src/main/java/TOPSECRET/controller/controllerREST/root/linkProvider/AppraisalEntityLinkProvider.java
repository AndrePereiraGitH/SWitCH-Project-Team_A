package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.AppraisalEntityRESTController;
import TOPSECRET.controller.controllerREST.root.authorizationPolicy.AppraisalEntityAuthorizationPolicy;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import java.util.List;
import org.springframework.hateoas.Link;

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
public class AppraisalEntityLinkProvider {

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
    public List<Link> getLinks (UserId id){

        List<Link> links = new ArrayList<>();

        User user = repository.findByID(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if(policy.canRegisterAppraisalEntity(user)){
            links.add(linkTo(methodOn(AppraisalEntityRESTController.class)
                    .create(null, id))
                    .withRel("register-appraisal-entity"));
        }

        return links;
    }

}
