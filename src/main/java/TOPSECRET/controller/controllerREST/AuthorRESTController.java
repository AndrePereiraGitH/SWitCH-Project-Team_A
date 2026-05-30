package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.AuthorLinkProvider;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.author.AuthorMapper;
import TOPSECRET.dto.author.request.CreateAuthorDTO;
import TOPSECRET.dto.author.response.AuthorDTO;
import TOPSECRET.services.AuthorService;
import TOPSECRET.services.CountryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceAlreadyExistsException;
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
    private final CountryService countryService;

    public AuthorRESTController(AuthorService service, AuthorLinkProvider provider, CountryService countryService) {
        this.service = service;
        this.provider = provider;
        this.countryService = countryService;
    }


    /**
     * Creates and stores a new Author.
     *
     * @param dto    request data used to create the Author
     * @param userId identifier of the user performing the operation
     * @return response entity containing the created Author
     *         or an error status when the operation fails
     */
    @PostMapping("")
    public ResponseEntity<AuthorDTO> createAuthor(
            @RequestBody CreateAuthorDTO dto,
            @RequestParam UserId userId) {


        try {
            Author author = service.createAndSave(
                    dto.name(),
                    AuthorMapper.toCountryId(dto),
                    dto.dateOfBirth(),
                    dto.biography(),
                    userId
            );

            Country country = countryService.findById(author.getCountryID());

            AuthorDTO result = AuthorMapper.toDTO(author, country);

            List<Link> links = provider.getLinks(userId);

            links.add(linkTo(methodOn(AuthorRESTController.class)
                    .findById(author.id())).withSelfRel());

            links.forEach(result::add);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (InstanceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves all persisted {@link Author} resources.
     *
     * <p>Each author in the response includes a link to its individual
     * resource endpoint.</p>
     *
     * @return response entity containing the list of all authors,
     *         or an error status when the operation fails
     */
    @GetMapping
    public ResponseEntity<List<AuthorDTO>> findAll() {

        try {
            List<Author> authors = service.findAll();
            List<AuthorDTO> authorsDTO = new ArrayList<>();

            for (Author author : authors) {
                CountryId countryId = author.getCountryID();
                Country country = countryService.findById(countryId);

                AuthorDTO authorDTO = AuthorMapper.toDTO(author, country);

                Link selfLink = linkTo(methodOn(AuthorRESTController.class)
                        .findById(author.id()))
                        .withRel("find-by-id");

                authorDTO.add(selfLink);

                authorsDTO.add(authorDTO);
            }

            return ResponseEntity.status(HttpStatus.OK).body(authorsDTO);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a single {@link Author} by its identifier.
     *
     * <p>The response includes a self link and a find-by-id link
     * for HATEOAS navigation.</p>
     *
     * @param authorId the identifier of the author to retrieve
     * @return response entity containing the author,
     *         or an error status when the operation fails
     */
    @GetMapping("/{authorId}")
    public ResponseEntity<AuthorDTO> findById(@PathVariable("authorId") AuthorId authorId) {

        try {
            Author author = service.findById(authorId);
            CountryId countryId = author.getCountryID();
            Country country = countryService.findById(countryId);

            AuthorDTO authorDTO = AuthorMapper.toDTO(author, country);

            Link selfLink = linkTo(methodOn(AuthorRESTController.class)
                    .findById(authorId)).withSelfRel();

            Link withRel = linkTo(methodOn(AuthorRESTController.class)
                    .findById(authorId)).withRel("find-by-id");

            authorDTO.add(selfLink);
            authorDTO.add(withRel);

            return ResponseEntity.status(HttpStatus.OK).body(authorDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}