package TOPSECRET.controller.controllerREST.root;

import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.hateoas.Link;

import javax.management.InstanceNotFoundException;
import java.util.List;

/**
 * Contract for providing HATEOAS links scoped to a specific resource,
 * based on the requesting user's identity and permissions.
 *
 * <p>All resource-specific link providers implement this interface,
 * allowing {@link RootController} to treat them uniformly when
 * assembling the full set of root-level links.</p>
 *
 * <p>Each implementation is responsible for its own authorization logic —
 * public links are always included, while restricted links are conditionally
 * added based on the requesting user's role.</p>
 */
public interface RootLinkProvider {

    /**
     * Returns the HATEOAS links available to the given user for this resource.
     *
     * @param userId the identifier of the requesting user; must not be {@code null}
     * @return a list of {@link Link} instances available to the user;
     *         never {@code null}, may be empty
     * @throws InstanceNotFoundException if the user cannot be resolved
     */
    List<Link> getLinks(UserId userId) throws InstanceNotFoundException;
}