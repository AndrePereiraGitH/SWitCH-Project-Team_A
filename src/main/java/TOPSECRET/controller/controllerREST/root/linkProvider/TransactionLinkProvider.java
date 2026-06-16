package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.TransactionRESTController;
import TOPSECRET.controller.controllerREST.root.RootLinkProvider;
import TOPSECRET.controller.controllerREST.root.authorizationPolicy.TransactionAuthorizationPolicy;
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
 * Provides HATEOAS links related to transaction operations.
 *
 * <p>This component is responsible for building the links that should be
 * exposed in transaction-related API responses. Links are divided into
 * public links (available to all users) and buyer-specific links
 * (scoped to a particular buyer's transactions).</p>
 */
@Component
public class TransactionLinkProvider implements RootLinkProvider {

    private final IUserRepository repository;
    private final TransactionAuthorizationPolicy policy;


    /**
     * Creates a new {@code TransactionLinkProvider}.
     *
     * @param policy the authorization policy used to determine which
     *                   transaction actions the user can perform
     * @param repository   the repository used to retrieve users
     */
    @Autowired
    public TransactionLinkProvider(IUserRepository repository, TransactionAuthorizationPolicy policy) {
        this.repository = repository;
        this.policy = policy;
    }

    /**
     * Returns all transaction-related HATEOAS links for the given user.
     *
     * Links exposed:
     * - create-transaction        → any NORMAL_USER
     * - find-transaction-by-id   → NORMAL_USER or ADMIN
     * - my-transactions          → NORMAL_USER or ADMIN
     */
    @Override
    public List<Link> getLinks(UserId userId) throws InstanceNotFoundException {

        Objects.requireNonNull(userId, "UserId cannot be null");

        List<Link> links = new ArrayList<>();
        User user = resolveUser(userId);

        if (policy.canCreateTransaction(user)) {
            links.add(linkTo(methodOn(TransactionRESTController.class)
                    .createTransaction(null, null))
                    .withRel("create-transaction"));
        }

        if (policy.canViewTransaction(user)) {
            links.add(linkTo(methodOn(TransactionRESTController.class)
                    .findById(null, null))
                    .withRel("find-transaction-by-id"));

            links.add(linkTo(methodOn(TransactionRESTController.class)
                    .findAllByBuyerId(userId.getUserId(), null))
                    .withRel("my-transactions"));
        }

        return links;
    }

    // helper
    private User resolveUser (UserId userId) throws InstanceNotFoundException {
        return repository.findByID(userId).orElseThrow(() -> new InstanceNotFoundException("User not found"));
    }
}