package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.TransactionRESTController;
import TOPSECRET.controller.controllerREST.root.RootLinkProvider;
import TOPSECRET.domain.ValueObjects.UserId;
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
 * exposed in transaction-related API responses. Since all authenticated
 * users can create and view transactions, no authorization policy is required.</p>
 */
@Component
public class TransactionLinkProvider implements RootLinkProvider {

    /**
     * Returns the transaction-related HATEOAS links available to a specific buyer.
     *
     * <p>Includes a link to retrieve all transactions made by the given buyer.</p>
     *
     * @param userId the identifier of the buyer whose transaction links should be generated;
     *               must not be {@code null}
     * @return a list of HATEOAS links available to the buyer
     */
    public List<Link> getBuyerLinks(UserId userId) {

        Objects.requireNonNull(userId, "UserId cannot be null");

        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(TransactionRESTController.class)
                .findAllByBuyerId(userId.getUserId(), null))
                .withRel("my-transactions"));

        return links;
    }

    /**
     * Returns the publicly available transaction-related HATEOAS links.
     *
     * <p>Includes links to create a transaction and retrieve a transaction by ID.</p>
     *
     * @return a list of public HATEOAS links for transaction operations
     */
    public List<Link> getPublicLinks() {

        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(TransactionRESTController.class)
                .createTransaction(null))
                .withRel("create-transaction"));

        links.add(linkTo(methodOn(TransactionRESTController.class)
                .findById(null, null))
                .withRel("find-transaction-by-id"));

        return links;
    }

    /**
     * Returns all transaction-related HATEOAS links for a given user,
     * combining public links and buyer-specific links.
     *
     * @param userId the identifier of the user whose links should be generated;
     *               must not be {@code null}
     * @return a combined list of public and buyer-specific HATEOAS links
     * @throws InstanceNotFoundException not thrown by this implementation;
     *                                   declared to satisfy the {@link RootLinkProvider} contract
     */
    @Override
    public List<Link> getLinks(UserId userId) throws InstanceNotFoundException {

        List<Link> links = new ArrayList<>();
        links.addAll(getPublicLinks());
        links.addAll(getBuyerLinks(userId));

        return links;
    }
}