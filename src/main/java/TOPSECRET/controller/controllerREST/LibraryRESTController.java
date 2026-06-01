package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.LibraryLinkProvider;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.library.LibraryMapper;
import TOPSECRET.dto.library.request.RequestLibraryDTO;
import TOPSECRET.dto.library.response.ResponseLibraryDTO;
import TOPSECRET.services.LibraryService;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@Tag(name = "Libraries", description = "Library management endpoints")
@RequestMapping(path = "/libraries")
public class LibraryRESTController {

    private final LibraryLinkProvider libLinkProvider;
    private final LibraryService libService;

    @Autowired
    public LibraryRESTController(LibraryLinkProvider libLinkProvider, LibraryService libService) {
        this.libLinkProvider = libLinkProvider;
        this.libService = libService;
    }

    /**
     * Creates and registers a new Library in the system.
     *
     * @param requestLibraryDTO the data transfer object containing the new library's details.
     * @param userId            the unique identifier of the user performing the creation.
     * @return a {@link ResponseEntity} containing the created {@link ResponseLibraryDTO} and HTTP 201 status,
     *         or an error status (400, 403, 500) if the operation fails.
     */
    @PostMapping
    public ResponseEntity<ResponseLibraryDTO> createLibrary(
            @RequestBody RequestLibraryDTO requestLibraryDTO,
            @RequestParam UserId userId) {

        try {
            Library library = libService.createAndSave(requestLibraryDTO.libraryName(), userId);
            ResponseLibraryDTO result = LibraryMapper.toDTO(library);

            List<Link> links = libLinkProvider.getLinksForLibraries(userId);
            links.forEach(result::add);

            result.add(linkTo(methodOn(LibraryRESTController.class)
                    .getLibrary(library.id()))
                    .withSelfRel());

            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Returns all libraries registered in the system.
     *
     * <p>Each item in the response is enriched with a self-link pointing to its
     * individual resource endpoint.</p>
     *
     * <p>Responses:</p>
     * <ul>
     *     <li>200 OK – when libraries exist</li>
     *     <li>204 No Content – when no libraries are found</li>
     *     <li>400 Bad Request – for invalid input</li>
     *     <li>500 Internal Server Error – for unexpected errors</li>
     * </ul>
     *
     * @return a {@link ResponseEntity} containing a list of {@link ResponseLibraryDTO},
     *         or an appropriate HTTP status if no content or an error occurs.
     */
    @GetMapping
    public ResponseEntity<List<ResponseLibraryDTO>> getAllLibraries() {

        try {
            List<Library> libraries = libService.findAll();

            List<ResponseLibraryDTO> result = libraries.stream()
                    .map(library -> {
                        ResponseLibraryDTO dto = LibraryMapper.toDTO(library);
                        dto.add(linkTo(methodOn(LibraryRESTController.class)
                                .getLibrary(library.id()))
                                .withSelfRel());
                        return dto;
                    })
                    .toList();

            if (result.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Returns a single Library identified by its unique ID.
     *
     * <p>The response is enriched with:</p>
     * <ul>
     *     <li>a self-link to the resource itself</li>
     *     <li>a link to the full library collection</li>
     * </ul>
     *
     * <p>Responses:</p>
     * <ul>
     *     <li>200 OK – when the library is found</li>
     *     <li>404 Not Found – when no library exists for the given ID</li>
     *     <li>400 Bad Request – for invalid ID format or input</li>
     *     <li>500 Internal Server Error – for unexpected errors</li>
     * </ul>
     *
     * @param id the {@link LibraryId} of the library to retrieve.
     * @return a {@link ResponseEntity} containing the {@link ResponseLibraryDTO}
     *         if found, or an appropriate HTTP status otherwise.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseLibraryDTO> getLibrary(
            @PathVariable LibraryId id) {

        try {
            Library library = libService.findById(id);
            ResponseLibraryDTO result = LibraryMapper.toDTO(library);

            result.add(linkTo(methodOn(LibraryRESTController.class)
                    .getLibrary(id))
                    .withSelfRel());

            result.add(linkTo(methodOn(LibraryRESTController.class)
                    .getAllLibraries())
                    .withRel("get-all-libraries"));

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}