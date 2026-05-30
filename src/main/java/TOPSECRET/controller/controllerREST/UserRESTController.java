package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.UserLinkProvider;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.user.UserMapper;
import TOPSECRET.dto.user.request.RequestUserDTO;
import TOPSECRET.dto.user.response.ResponseUserDTO;
import TOPSECRET.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceAlreadyExistsException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST Controller responsible for managing User resources.
 *
 * <p>Exposes endpoints for user registration and coordinates the interaction
 * between DTOs, domain objects, application services and HATEOAS links.</p>
 */
@RestController
@Tag(name = "Users", description = "User management endpoints")
@RequestMapping(path = "/users")
public class UserRESTController {

    private final UserLinkProvider userLinkProvider;
    private final UserService userService;

    /**
     * Creates a new {@code UserRESTController}.
     *
     * @param userLinkProvider the provider responsible for generating HATEOAS links
     * @param userService      the service responsible for user operations
     */
    @Autowired
    public UserRESTController(UserLinkProvider userLinkProvider, UserService userService) {
        this.userLinkProvider = userLinkProvider;
        this.userService = userService;
    }

    /**
     * Registers a new user in the system.
     *
     * <p>Maps the incoming DTO to domain objects, delegates creation to the service,
     * and enriches the response with HATEOAS links based on the requester's permissions.</p>
     *
     * @param requestUserDTO the DTO containing the new user's data
     * @param userId         the ID of the user performing the registration
     * @return HTTP 201 with the created user DTO, or an error status
     */
    @PostMapping
    public ResponseEntity<ResponseUserDTO> registerUser(
            @RequestBody RequestUserDTO requestUserDTO,
            @RequestParam UserId userId) {

        try {
            Profile profile = UserMapper.toProfile(requestUserDTO);
            Set<UserRole> roles = Set.of(UserMapper.toRole(requestUserDTO.role()));

            User user = userService.createAndSave(
                    requestUserDTO.username(),
                    profile,
                    roles,
                    userId
            );

            ResponseUserDTO result = UserMapper.toDTO(user);

            List<Link> links = userLinkProvider.getLinksForUsers(userId);
            links.forEach(result::add);
            result.add(linkTo(methodOn(UserRESTController.class)
                    .registerUser(requestUserDTO, userId)).withSelfRel());

            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (InstanceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * <p>Returns the user enriched with a self-link and navigational links
     * based on the requester's permissions.</p>
     *
     * @param id the unique identifier of the user to retrieve
     * @return HTTP 200 with the user DTO, HTTP 404 if not found, or HTTP 500 on error
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseUserDTO> getUser(@PathVariable UserId id) {

        try {
            User user = userService.findByID(id);

            ResponseUserDTO result = UserMapper.toDTO(user);

            result.add(linkTo(methodOn(UserRESTController.class)
                    .getUser(id))
                    .withSelfRel());

            List<Link> links = userLinkProvider.getLinksForUsers(id);
            links.forEach(result::add);

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}