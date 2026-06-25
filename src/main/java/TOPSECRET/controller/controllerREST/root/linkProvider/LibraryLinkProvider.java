package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.LibraryRESTController;
import TOPSECRET.controller.controllerREST.root.RootLinkProvider;
import TOPSECRET.controller.controllerREST.root.authorizationPolicy.LibraryAuthorizationPolicy;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Component responsible for providing HATEOAS links related to Library operations.
 * It dynamically generates links based on the user's authorization level and
 * the current state of the application.
 */
@Component
public class LibraryLinkProvider implements RootLinkProvider {

    private final LibraryAuthorizationPolicy authPolicy;
    private final IUserRepository userRepo;

    /**
     * Creates a new {@code LibraryLinkProvider}.
     *
     * @param authPolicy the authorization policy used to determine which links to include;
     *                   must not be {@code null}
     * @param userRepo   the repository used to look up the requesting user;
     *                   must not be {@code null}
     */
    @Autowired
    public LibraryLinkProvider(LibraryAuthorizationPolicy authPolicy, IUserRepository userRepo) {
        this.authPolicy = authPolicy;
        this.userRepo = userRepo;
    }

    /**
     * Generates a list of owner-scoped HATEOAS navigation links based on the
     * requesting user's permissions.
     *
     * <p>The following links are conditionally included:</p>
     * <ul>
     *   <li>{@code create-library} — create a new library for the user.</li>
     *   <li>{@code get-library} — retrieve a library by its unique identifier.</li>
     *   <li>{@code get-library-by-user} — retrieve the library owned by the requesting user.</li>
     * </ul>
     *
     * @param userId the identifier of the requesting user; must not be {@code null}
     * @return a list of owner {@link Link} instances; never {@code null}, may be empty
     * @throws NullPointerException      if {@code userId} is {@code null}
     * @throws InstanceNotFoundException if no user is found for the given {@code userId}
     */
    public List<Link> getOwnerLinks(UserId userId) throws InstanceNotFoundException {
        Objects.requireNonNull(userId, "UserId cannot be null");

        List<Link> links = new ArrayList<>();
        User user = resolveUser(userId);

        if (authPolicy.canCreateLibrary(user)) {
            links.add(linkTo(methodOn(LibraryRESTController.class)
                    .createLibrary(null, null))
                    .withRel("create-library"));
        }
        if (authPolicy.canGetLibrary(user, userId)) {
            links.add(linkTo(methodOn(LibraryRESTController.class)
                    .getLibrary(null, null))
                    .withRel("get-library"));

            links.add(linkTo(methodOn(LibraryRESTController.class)
                    .getLibraryByUserId(userId.toString(), null))
                    .withRel("get-library-by-user"));
        }
        return links;
    }

    /**
     * Generates a list of admin-scoped HATEOAS navigation links based on the
     * requesting user's permissions.
     *
     * <p>The following link is conditionally included:</p>
     * <ul>
     *   <li>{@code get-all-libraries} — retrieve all libraries registered in the system.</li>
     * </ul>
     *
     * @param userId the identifier of the requesting user; must not be {@code null}
     * @return a list of admin {@link Link} instances; never {@code null}, may be empty
     * @throws NullPointerException      if {@code userId} is {@code null}
     * @throws InstanceNotFoundException if no user is found for the given {@code userId}
     */
    public List<Link> getAdminLinks(UserId userId) throws InstanceNotFoundException {
        Objects.requireNonNull(userId, "UserId cannot be null");

        List<Link> links = new ArrayList<>();
        User user = resolveUser(userId);

        if (authPolicy.canGetAllLibrary(user)) {
            links.add(linkTo(methodOn(LibraryRESTController.class)
                    .getAllLibraries(null))
                    .withRel("get-all-libraries"));
        }
        return links;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Aggregates links from both owner and admin groups, in that order.</p>
     */
    @Override
    public List<Link> getLinks(UserId userId) throws InstanceNotFoundException {
        List<Link> links = new ArrayList<>();
        links.addAll(getOwnerLinks(userId));
        links.addAll(getAdminLinks(userId));
        return links;
    }

    /**
     * Resolves a {@link User} from the repository by the given {@link UserId}.
     *
     * @param userId the identifier of the user to resolve; must not be {@code null}
     * @return the resolved {@link User}
     * @throws InstanceNotFoundException if no user is found for the given {@code userId}
     */
    private User resolveUser(UserId userId) throws InstanceNotFoundException {
        return userRepo.findByID(userId)
                .orElseThrow(() -> new InstanceNotFoundException("User not found"));
    }
}