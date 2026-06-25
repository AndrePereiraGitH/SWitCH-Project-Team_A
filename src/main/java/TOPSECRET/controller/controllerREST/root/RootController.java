package TOPSECRET.controller.controllerREST.root;

import TOPSECRET.controller.controllerREST.root.linkProvider.*;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.user.UserMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller exposing the API root entry point.
 *
 * <p>This is the single entry point of the MiteLovers API. Clients should
 * start here and navigate to other resources by following the hypermedia
 * links returned in the response, rather than constructing URLs manually.</p>
 *
 * <p>This controller aggregates links from every registered {@link RootLinkProvider}
 * implementation. Each provider encapsulates its own authorization policy and only
 * returns links the requesting user is permitted to follow, making the response
 * naturally role-aware.</p>
 *
 * <p>Base path: {@code /}</p>
 */
@RestController
@Tag(name = "Root", description = "API entry point — navigate the full API from here")
@RequestMapping("/")
public class RootController {

    private final List<RootLinkProvider> providers;

    /**
     * Constructs a new {@code RootController} with all registered link providers.
     *
     * <p>Spring automatically injects all beans implementing {@link RootLinkProvider},
     * so registering a new resource's link provider is sufficient to expose its
     * links from the root without any changes to this class.</p>
     *
     * @param providers all resource-specific {@link RootLinkProvider} implementations;
     *                  must not be {@code null}
     */
    @Autowired
    public RootController(List<RootLinkProvider> providers) {
        this.providers = providers;
    }

    /**
     * Returns all hypermedia links available to the requesting user.
     *
     * <p>Always includes a self-link pointing back to this endpoint. All remaining
     * links are assembled by delegating to each registered {@link RootLinkProvider},
     * reflecting the requesting user's role and permissions.</p>
     *
     * <p>HTTP responses:</p>
     * <ul>
     *   <li>{@code 200 OK} — links successfully assembled and returned.</li>
     *   <li>{@code 400 Bad Request} — {@code userId} is null, empty, invalid, or
     *       cannot be resolved to an existing user.</li>
     *   <li>{@code 500 Internal Server Error} — unexpected failure during link assembly.</li>
     * </ul>
     *
     * @param userId the identifier of the requesting user; used to resolve role-appropriate links
     * @return a {@link ResponseEntity} containing a {@link RepresentationModel} populated
     *         with all links available to the requesting user
     */
    @GetMapping
    public ResponseEntity<RepresentationModel<?>> getRoot(
            @RequestParam(required = false) String userId) {

        RepresentationModel<?> root = new RepresentationModel<>();

        root.add(linkTo(methodOn(RootController.class)
                .getRoot(userId))
                .withSelfRel());

        if (userId != null && !userId.isBlank()) {
            try {
                UserId id = UserMapper.toUserId(userId);
                for (RootLinkProvider provider : providers) {
                    provider.getLinks(id).forEach(root::add);
                }
            } catch (IllegalArgumentException | NullPointerException | InstanceNotFoundException e) {
                return ResponseEntity.badRequest().build();
            } catch (Exception e) {
                return ResponseEntity.internalServerError().build();
            }
        }

        return ResponseEntity.ok(root);
    }
}