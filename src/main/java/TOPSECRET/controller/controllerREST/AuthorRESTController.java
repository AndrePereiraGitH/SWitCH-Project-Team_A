package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.AuthorLinkProvider;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.author.AuthorMapper;
import TOPSECRET.dto.author.request.CreateAuthorDTO;
import TOPSECRET.dto.author.response.AuthorDTO;
import TOPSECRET.services.AuthorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * REST controller responsible for managing Author resources.
 *
 * <p>This controller exposes endpoints for creating Author resources
 * and coordinates the interaction between DTOs, domain objects
 * and application services.</p>
 */
@RestController
@Tag(name = "Authors", description = "Authors management endpoint")
@RequestMapping(path = "/authors")
public class AuthorRESTController {

    private final AuthorService service;
    private final AuthorLinkProvider provider;

    /**
     * Creates a new Author REST controller.
     *
     * @param service  service responsible for Author operations
     * @param provider provider responsible for generating HATEOAS links
     */
    public AuthorRESTController(AuthorService service, AuthorLinkProvider provider) {
        this.service = service;
        this.provider = provider;
    }

    /**
     * Creates and stores a new Author.
     *
     * @param dto    request data used to create the Author
     * @param userId identifier of the user performing the operation
     * @return response entity containing the created Author
     *         or an error status when the operation fails
     */
//    @PostMapping("")
//    public ResponseEntity<AuthorDTO> createAuthor(
//            @RequestBody CreateAuthorDTO dto,
//            @RequestParam UserId userId) {
//        try {
//            Author author = service.createAndSave(
//                    dto.name(),
//                    AuthorMapper.toCountryId(dto),
//                    AuthorMapper.toDateOfBirth(dto),
//                    dto.biography(),
//                    userId
//            );
//            Country country = service.resolveCountry(author.getCountryID());
//            AuthorDTO result = AuthorMapper.toDTO(author, country);
//            List<Link> links = new ArrayList<>(provider.getLinks(userId));
//            links.add(linkTo(methodOn(AuthorRESTController.class)
//                    .createAuthor(dto, userId)).withSelfRel());
//            links.forEach(result::add);
//            return ResponseEntity.status(HttpStatus.CREATED).body(result);
//        } catch (AccessDeniedException e) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        } catch (InstanceAlreadyExistsException e) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).build();
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
}