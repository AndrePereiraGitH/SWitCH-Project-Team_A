package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.LibraryRESTController;
import TOPSECRET.controller.controllerREST.root.authorizationPolicy.LibraryAuthorizationPolicy;
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
 * Component responsible for providing HATEOAS links related to Library operations.
 * It dynamically generates links based on the user's authorization level and
 * the current state of the application.
 */
@Component
public class LibraryLinkProvider {
    private final LibraryAuthorizationPolicy authPolicy;
    private final IUserRepository userRepo;

    @Autowired
    public LibraryLinkProvider(LibraryAuthorizationPolicy authPolicy, IUserRepository userRepo) {
        this.authPolicy = authPolicy;
        this.userRepo = userRepo;
    }

    /**
     * Generates a list of actionable HATEOAS links for a specific user.
     * This method evaluates the {@link LibraryAuthorizationPolicy} to determine
     * if the user has the necessary permissions to perform library-related actions.
     *
     * @param userId The unique identifier of the user for whom the links are being generated.
     * @return A {@link List} of {@link Link} objects representing the allowed actions.
     * If the user is not found or has no permissions, an empty list is returned.
     */
    public List<Link> getLinksForLibraries(UserId userId) {
        List<Link> links = new ArrayList<>();

        userRepo.findByID(userId).ifPresent(user -> {

            if (authPolicy.canCreateLibrary(user)) {

                links.add(linkTo(methodOn(LibraryRESTController.class)
                        .createLibrary(null, userId))
                        .withRel("create-library"));
            }
        });

        return links;
    }
}