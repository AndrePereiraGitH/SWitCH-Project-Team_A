package TOPSECRET.controller.controllerREST;


import TOPSECRET.controller.controllerREST.root.linkProvider.GenreLinkProvider;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.genre.GenreMapper;
import TOPSECRET.dto.genre.request.GenreRequestDTO;
import TOPSECRET.dto.genre.response.GenreResponseDTO;
import TOPSECRET.dto.user.UserMapper;
import TOPSECRET.services.GenreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.AccessDeniedException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * REST controller responsible for managing Genre resources.
 *
 * <p>This controller exposes endpoints for creating and retrieving
 * Genre resources and coordinates the interaction between
 * DTOs, domain objects and application services.</p>
 */
@RestController
@Tag(name = "Genres", description = "Genres management endpoint")
@RequestMapping(path = "/genres")
public class GenreRESTController {

    private final GenreService service;
    private final GenreLinkProvider provider;

    /**
     * Creates a new Genre REST controller.
     *
     * @param service service responsible for Genre operations
     * @param provider provider responsible for generating HATEOAS links
     */
    public GenreRESTController(
            GenreService service,
            GenreLinkProvider provider) {

        this.service = service;
        this.provider = provider;
    }

    /**
     * Creates and stores a new Genre.
     *
     * <p>The request DTO is converted before delegating the
     * creation process to the application service.</p>
     *
     * <p>If the operation succeeds, a response DTO representing
     * the created Genre is returned with HTTP status
     * {@code 201 CREATED}.</p>
     *
     * @param dto request data used to create the Genre
     * @param userId identifier of the user performing the operation
     * @return response entity containing the created Genre
     *         or an error status when the operation fails
     */
    @PostMapping("")
    public ResponseEntity<GenreResponseDTO> createGenre(
            @RequestBody GenreRequestDTO dto,
            @RequestParam String userId) {

        try {
            UserId id = UserMapper.toUserId(userId);
            Genre genre = service.createAndSave(dto.genreName(), id);

            GenreResponseDTO result = GenreMapper.toDTO(genre);

            List<Link> links = provider.getLinks(id);
            links.forEach(result::add);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves all official genres available in the system.
     *
     * <p>If the operation succeeds, a list of response DTOs
     * is returned with HTTP status {@code 200 OK}.</p>
     *
     * @return list containing all registered genres
     *         or an error status when the operation fails
     */
    @GetMapping("")
    public ResponseEntity<List<GenreResponseDTO>> getGenres() {

        try {
            List<Genre> genres = service.findAll();

            List<GenreResponseDTO> result = GenreMapper.toDTOs(genres);

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Returns the HTTP methods allowed on the genres root resource for the given user.
     *
     * <p>Always includes {@code GET} and {@code OPTIONS}. {@code POST} is included only if the
     * requesting user is authorized to create a new Genre.</p>
     *
     * @param userId the ID of the user performing the request
     * @return HTTP 200 with an {@code Allow} header reflecting the user's permitted methods,
     *         or HTTP 400 if the user is not found or the user ID is invalid
     */
    @RequestMapping(value = "", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsRoot(@RequestParam String userId) {
        try {
            UserId id = UserMapper.toUserId(userId);

            Set<HttpMethod> allowed = new HashSet<>(List.of(HttpMethod.GET, HttpMethod.OPTIONS));

            if (service.canCreateGenre(id)) {
                allowed.add(HttpMethod.POST);
            }

            return ResponseEntity.ok().allow(allowed.toArray(HttpMethod[]::new)).build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}