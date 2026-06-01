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
     * individual resource endpoint. Returns HTTP 204 if no libraries exist.</p>
     *
     * @return a {@link ResponseEntity} containing a list of {@link ResponseLibraryDTO},
     *         HTTP 204 if the list is empty, or HTTP 500 if an unexpected error occurs.
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
     * <p>Enriches the response with a self-link and a link to the full library collection.</p>
     *
     * @param id the {@link LibraryId} of the library to retrieve.
     * @return a {@link ResponseEntity} containing the {@link ResponseLibraryDTO} and HTTP 200 status,
     *         HTTP 404 if the library is not found, or HTTP 500 if an unexpected error occurs.
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