package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.PublicationSampleRESTController;
import TOPSECRET.controller.controllerREST.root.authorizationPolicy.PublicationSampleAuthorizationPolicy;

import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PublicationSampleLinkProvider {

    private final PublicationSampleAuthorizationPolicy policy;
    private final IUserRepository userRepository;

    @Autowired
    public PublicationSampleLinkProvider(PublicationSampleAuthorizationPolicy policy, IUserRepository userRepository) {
        this.policy = policy;
        this.userRepository = userRepository;
    }

    public List<Link> getPublicationSampleLinks(UserId userId) throws InstanceNotFoundException {

        List<Link> links = new ArrayList<>();

        User user = userRepository.findByID(userId)
                .orElseThrow(() -> new InstanceNotFoundException("User not found"));

        if(policy.canCreatePublicationSample(user)) {
            links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                    .createPublicationSample(null,null))
                    .withRel("create-publication-sample"));
        }


        links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                .findById(null, null))
                .withRel("find-by-id"));


        links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                .findAll(null))
                .withRel("find-all"));

        links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                .findByBibliographicalDataId(null,null))
                .withRel("find-by-bdId"));

        links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                .findByEditionIdIn(null,null))
                .withRel("find-by-editionId"));


        return links;

    }
}
