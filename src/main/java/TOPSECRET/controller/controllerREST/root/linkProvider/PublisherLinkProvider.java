package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.root.PublisherRESTController;
import TOPSECRET.controller.controllerREST.root.authorizationPolicy.PublisherAuthorizationPolicy;
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
 * Component responsible for generating HATEOAS hypermedia links for Publisher-related resources.
 *
 * <p>This provider centralizes the logic for determining which actions a specific user
 * is allowed to perform based on the {@link PublisherAuthorizationPolicy}. It ensures
 * that the API responses are discoverable and adhere to the HATEOAS principle by
 * dynamically providing links based on user permissions.</p>
 */
@Component
public class PublisherLinkProvider {

    private final PublisherAuthorizationPolicy authPolicy;
    private final IUserRepository userRepo;

    @Autowired
    public PublisherLinkProvider(PublisherAuthorizationPolicy authPolicy, IUserRepository userRepo) {
        this.authPolicy = authPolicy;
        this.userRepo = userRepo;
    }

    /**
     * Generates a list of actionable HATEOAS links for a specific user regarding publishers.
     *
     * @param userId The UserId Value Object.
     * @return List of relevant hypermedia links.
     */
    public List<Link> getLinksForPublisher(UserId userId) {

        List<Link> links = new ArrayList<>();

        userRepo.findByID(userId).ifPresent(user -> {
            if (authPolicy.canGetAllPublishers(user)) {
                links.add(linkTo(methodOn(PublisherRESTController.class)
                        .getAllPublishers(userId))
                        .withRel("get-all-publishers"));
            }

            if (authPolicy.canCreatPublisher(user)) {
                links.add(linkTo(methodOn(PublisherRESTController.class)
                        .createPublisher(null, userId))
                        .withRel("create-publisher"));
            }
        });

        return links;
    }
}