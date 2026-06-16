package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.UserAuthorizationPolicy;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceAlreadyExistsException;
import org.springframework.security.access.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
    private final UserAuthorizationPolicy userAuthorizationPolicy;

    /**
     * Creates a new {@code UserRESTController}.
     *
     * @param userLinkProvider the provider responsible for generating HATEOAS links
     * @param userService      the service responsible for user operations
     */
    @Autowired
    public UserRESTController(UserLinkProvider userLinkProvider, UserService userService, UserAuthorizationPolicy userAuthorizationPolicy) {
        this.userLinkProvider = userLinkProvider;
        this.userService = userService;
        this.userAuthorizationPolicy = userAuthorizationPolicy;
    }

    /**
     * Registers a new user in the system.
     *
     * <p>Maps the incoming DTO to domain objects, delegates creation to the service,
     * and enriches the response with HATEOAS links and a self-link pointing to the
     * created user resource.</p>
     *
     * @param requestUserDTO the DTO containing the new user's data
     * @param requesterId         the ID of the user performing the registration
     * @return HTTP 201 with the created user DTO and HATEOAS links,
     *         HTTP 400 if the request contains invalid data,
     *         HTTP 403 if the requester lacks permission,
     *         HTTP 409 if a user with the same identity already exists,
     *         or HTTP 500 for unexpected errors
     */
    @PostMapping
    public ResponseEntity<ResponseUserDTO> registerUser(
            @RequestBody RequestUserDTO requestUserDTO,
            @RequestParam String requesterId) {

        try {
            UserId requesterIdVO = UserMapper.toUserId(requesterId);

            Profile profile = UserMapper.toProfile(requestUserDTO);
            Set<UserRole> roles = Set.of(UserMapper.toRole(requestUserDTO.role()));

            User user = userService.createAndSave(
                    requestUserDTO.username(),
                    profile,
                    roles,
                    requesterIdVO
            );

            ResponseUserDTO result = UserMapper.toDTO(user);

            result.add(linkTo(methodOn(UserRESTController.class)
                    .getUser(user.id().getUserId(), requesterId))
                    .withSelfRel());

            userLinkProvider
                    .getLinksForUserResource(requesterIdVO, user.id())
                    .forEach(result::add);

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
     * Retrieves all users in the system.
     *
     * <p>Returns the full list of users, each enriched with public HATEOAS links
     * and a self-link pointing back to this endpoint.</p>
     *
     * @return HTTP 200 with the list of user DTOs (may be empty),
     *         or HTTP 500 on unexpected error
     */
    @GetMapping
    public ResponseEntity<List<ResponseUserDTO>> getAllUsers(@RequestParam String requesterId) {

        try {
            UserId requesterIdVO = UserMapper.toUserId(requesterId);

            List<User> allUsers = userService.findAll(requesterIdVO);
            List<ResponseUserDTO> result = new ArrayList<>();

            for (User user : allUsers) {
                ResponseUserDTO dto = UserMapper.toDTO(user);

                dto.add(linkTo(methodOn(UserRESTController.class)
                        .getUser(user.id().getUserId(), requesterId))
                        .withSelfRel());

                userLinkProvider
                        .getLinksForUserResource(requesterIdVO, user.id())
                        .forEach(dto::add);

                result.add(dto);
            }

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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
     * @return HTTP 200 with the user DTO, HTTP 400 if the ID is malformed,
     *         HTTP 404 if not found, or HTTP 500 on error
     */

    @GetMapping("/{id}")
    public ResponseEntity<ResponseUserDTO> getUser(@PathVariable String id, @RequestParam String requesterId) {

        try {
            UserId targetUserId = UserMapper.toUserId(id);
            UserId requesterIdVO = UserMapper.toUserId(requesterId);

            User targetUser = userService.findByIDNew(targetUserId, requesterIdVO);

            ResponseUserDTO result = UserMapper.toDTO(targetUser);

            result.add(linkTo(methodOn(UserRESTController.class)
                    .getUser(id, requesterId))
                    .withSelfRel());

            userLinkProvider
                    .getLinksForUserResource(requesterIdVO, targetUserId)
                    .forEach(result::add);

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}