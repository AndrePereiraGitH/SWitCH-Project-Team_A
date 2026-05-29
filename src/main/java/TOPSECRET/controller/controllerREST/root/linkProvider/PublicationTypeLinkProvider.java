package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.PublicationTypeRestController;
import TOPSECRET.controller.controllerREST.root.authorizationPolicy.PublicationTypeAuthorizationPolicy;
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
 * Provides HATEOAS links related to PublicationType resources.
 */
@Component
public class PublicationTypeLinkProvider {

    private final PublicationTypeAuthorizationPolicy policy;
    private final IUserRepository userRepository;

    public PublicationTypeLinkProvider(PublicationTypeAuthorizationPolicy policy, IUserRepository userRepository){
        this.policy = policy;
        this.userRepository = userRepository;
    }

    /**
     * Returns the available links for PublicationType operations.
     *
     * @param userId user performing the operation
     * @return list of HATEOAS links
     */
    public List<Link> getLinksForPublicationType(UserId userId) {

        List<Link> links = new ArrayList<>();

        User user = userRepository.findByID(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        if(policy.canCreatePublicationType(user)){
            links.add(linkTo(methodOn(PublicationTypeRestController.class)
                    .createPublicationType(null, null))
                    .withRel("create-publication-type"));
        }

        return links;
    }
}