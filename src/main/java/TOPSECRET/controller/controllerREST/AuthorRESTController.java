package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.AuthorLinkProvider;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.author.AuthorMapper;
import TOPSECRET.dto.author.request.CreateAuthorDTO;
import TOPSECRET.dto.author.response.AuthorDTO;
import TOPSECRET.services.AuthorService;
import TOPSECRET.services.CountryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST Controller responsible for managing Author resources.
 *
 * <p>Exposes endpoints for creating and querying authors, and coordinates the
 * interaction between DTOs, domain objects, application services, and HATEOAS links.</p>
 *
 * <p>Endpoints are grouped into three categories:</p>
 * <ul>
 *   <li><b>Public</b> — retrieval endpoints; no authentication required.</li>
 *   <li><b>Private</b> — creation endpoints; require a valid authenticated user.</li>
 *   <li><b>OPTIONS</b> — discoverability endpoints; conditionally reflect the requesting user's permitted methods.</li>
 * </ul>
 */
@RestController
@Tag(name = "Authors", description = "Authors management endpoint")
@RequestMapping(path = "/authors")
public class AuthorRESTController {

    private final AuthorService service;
    private final AuthorLinkProvider provider;
    private final CountryService countryService;

    /**
     * Creates a new {@code AuthorRESTController}.
     *
     * @param service        the service responsible for author operations
     * @param provider       the provider responsible for generating HATEOAS links
     * @param countryService the service responsible for country lookups
     */
    public AuthorRESTController(AuthorService service, AuthorLinkProvider provider, CountryService countryService) {
        this.service = service;
        this.provider = provider;
        this.countryService = countryService;
    }

    // =========================================================================
    // POST — private
    // =========================================================================

    /**
     * Creates a new author in the system.
     *
     * <p>Maps the incoming DTO to domain objects, delegates creation to the service,
     * and enriches the response with public and private HATEOAS links based on the
     * requester's permissions.</p>
     *
     * @param dto    the DTO containing the new author's data
     * @param userId the ID of the user performing the creation
     * @return HTTP 201 with the created {@link AuthorDTO}, or an error status
     */
    @PostMapping("")
    public ResponseEntity<AuthorDTO> createAuthor(
            @RequestBody @Valid CreateAuthorDTO dto,
            @RequestParam String userId) {

        try {
            UserId id = AuthorMapper.toUserId(userId);

            Author author = service.createAndSave(
                    dto.name(),
                    AuthorMapper.toCountryId(dto),
                    dto.dateOfBirth(),
                    dto.biography(),
                    id
            );

            AuthorDTO result = toAuthorDTO(author, id);

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

    // =========================================================================
    // GET — public
    // =========================================================================

    /**
     * Retrieves all authors in the system.
     *
     * <p>Each item in the returned list is enriched with a self-link and public
     * navigational links. No authentication required.</p>
     *
     * @return HTTP 200 with a list of {@link AuthorDTO}, or an error status
     */
    @GetMapping("")
    public ResponseEntity<List<AuthorDTO>> findAll() {

        try {
            List<Author> authors = service.findAll();
            List<AuthorDTO> result = new ArrayList<>();

            for (Author author : authors) {
                result.add(toAuthorDTO(author));
            }

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves an author by their unique identifier.
     *
     * <p>Returns the author enriched with a self-link and public navigational links.
     * No authentication required.</p>
     *
     * @param authorId the unique identifier of the author to retrieve
     * @return HTTP 200 with the {@link AuthorDTO}, HTTP 404 if not found, or HTTP 500 on error
     */
    @GetMapping("/{authorId}")
    public ResponseEntity<AuthorDTO> findById(
            @PathVariable("authorId") String authorId) {

        try {
            AuthorId id = AuthorMapper.toAuthorId(authorId);

            Author author = service.findById(id);

            AuthorDTO result = toAuthorDTO(author);

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // =========================================================================
    // OPTIONS
    // =========================================================================

    /**
     * Returns the HTTP methods allowed on the authors root resource for the given user.
     *
     * <p>Always includes {@code GET} and {@code OPTIONS}. {@code POST} is included only if the
     * requesting user is authorized to create authors.</p>
     *
     * @param userId the ID of the user performing the request
     * @return HTTP 200 with an {@code Allow} header reflecting the user's permitted methods,
     *         or HTTP 400 if the user ID is invalid
     */
    @RequestMapping(value = "", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsRoot(@RequestParam String userId) {
        try {
            UserId id = AuthorMapper.toUserId(userId);

            Set<HttpMethod> allowed = new HashSet<>(List.of(HttpMethod.GET, HttpMethod.OPTIONS));

            if (service.canCreateAuthor(id)) {
                allowed.add(HttpMethod.POST);
            }

            return ResponseEntity.ok().allow(allowed.toArray(HttpMethod[]::new)).build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Returns the HTTP methods allowed on the {@code /{authorId}} resource.
     *
     * <p>This endpoint is public. {@code GET} and {@code OPTIONS} are always allowed.</p>
     *
     * @param authorId the unique identifier of the author
     * @return HTTP 200 with an {@code Allow} header containing {@code GET} and {@code OPTIONS}
     */
    @RequestMapping(value = "/{authorId}", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsById(@PathVariable String authorId) {
        return ResponseEntity.ok()
                .allow(HttpMethod.GET, HttpMethod.OPTIONS)
                .build();
    }

    // =========================================================================
    // Private helpers
    // =========================================================================

    /**
     * Builds an {@link AuthorDTO} for a public endpoint, enriching it with
     * country information and public HATEOAS links only.
     *
     * <p>No user identity is required. Only public links and a self-link are attached.</p>
     *
     * @param author the author to convert; must not be {@code null}
     * @return the populated {@link AuthorDTO} with public links attached
     * @throws InstanceNotFoundException if any dependent lookup fails
     */
    private AuthorDTO toAuthorDTO(Author author) throws InstanceNotFoundException {
        Country country = countryService.findById(author.getCountryID());

        AuthorDTO result = AuthorMapper.toDTO(author, country);

        List<Link> links = new ArrayList<>(provider.getPublicLinks());

        links.add(linkTo(methodOn(AuthorRESTController.class)
                .findById(author.id().toString()))
                .withSelfRel());

        links.forEach(result::add);

        return result;
    }

    /**
     * Builds an {@link AuthorDTO} for a private endpoint, enriching it with
     * country information, public links, and private HATEOAS links based on
     * the requesting user's permissions.
     *
     * @param author the author to convert; must not be {@code null}
     * @param userId the ID of the requesting user used to generate contextual links
     * @return the populated {@link AuthorDTO} with public and private links attached
     * @throws InstanceNotFoundException if the associated user is not found by the link provider
     */
    private AuthorDTO toAuthorDTO(Author author, UserId userId) throws InstanceNotFoundException {
        Country country = countryService.findById(author.getCountryID());

        AuthorDTO result = AuthorMapper.toDTO(author, country);

        List<Link> links = new ArrayList<>(provider.getPublicLinks());
        links.addAll(provider.getPrivateLinks(userId));

        links.add(linkTo(methodOn(AuthorRESTController.class)
                .findById(author.id().toString()))
                .withSelfRel());

        links.forEach(result::add);

        return result;
    }
}