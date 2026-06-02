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
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@Tag(name = "Libraries", description = "Library management endpoints")
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
     * <p>This endpoint creates a new library associated with the given user.</p>
     *
     * @param requestLibraryDTO the validated request body containing the library creation data.
     * @param userId the identifier of the user creating the library. Must not be blank.
     * @return a {@link ResponseEntity} containing the created {@link ResponseLibraryDTO}
     * and HTTP 201 status if successful, or an error status:
     * <ul>
     *     <li>400 Bad Request – if input validation fails or arguments are invalid</li>
     *     <li>403 Forbidden – if the user is not allowed to create a library</li>
     *     <li>500 Internal Server Error – for unexpected failures</li>
     * </ul>
     */
    @PostMapping("/libraries")
    public ResponseEntity<ResponseLibraryDTO> createLibrary(
            @Valid @RequestBody RequestLibraryDTO requestLibraryDTO,
            @NotBlank @RequestParam String userId) {


        try {
            UserId id = LibraryMapper.toUserId(userId);

            Library library = libService.createAndSave(requestLibraryDTO.libraryName(), id);
            ResponseLibraryDTO response = LibraryMapper.toDTO(library);

            response.add(libLinkProvider.getLinksForLibraries(id));

            response.add(linkTo(methodOn(LibraryRESTController.class)
                    .getLibrary(library.id().toString()))
                    .withSelfRel());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (InstanceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves all libraries registered in the system.
     *
     * <p>Each returned library is enriched with a HATEOAS self-link pointing to its
     * individual resource endpoint.</p>
     *
     * @return a {@link ResponseEntity} containing a list of {@link ResponseLibraryDTO}
     * and HTTP 200 OK when libraries exist, or:
     * <ul>
     *     <li>204 No Content – when no libraries are found</li>
     *     <li>400 Bad Request – for invalid request parameters (rare)</li>
     *     <li>500 Internal Server Error – for unexpected failures</li>
     * </ul>
     */
    @GetMapping("/libraries")
    public ResponseEntity<List<ResponseLibraryDTO>> getAllLibraries() {

        try {
            List<Library> libraries = libService.findAll();

            List<ResponseLibraryDTO> response = libraries.stream()
                    .map(library -> {
                        ResponseLibraryDTO dto = LibraryMapper.toDTO(library);
                        dto.add(linkTo(methodOn(LibraryRESTController.class)
                                .getLibrary(library.id().toString()))
                                .withSelfRel());
                        return dto;
                    })
                    .toList();

            if (response.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a single Library by its unique identifier.
     *
     * <p>The returned resource includes HATEOAS links:</p>
     * <ul>
     *     <li>self link</li>
     * </ul>
     *
     * @param id the unique identifier of the library to retrieve.
     * @return a {@link ResponseEntity} containing the {@link ResponseLibraryDTO} if found,
     * or an error status:
     * <ul>
     *     <li>200 OK – when the library is found</li>
     *     <li>404 Not Found – when no library exists for the given ID</li>
     *     <li>400 Bad Request – if the ID format is invalid</li>
     *     <li>500 Internal Server Error – for unexpected failures</li>
     * </ul>
     */
    @GetMapping("/libraries/{id}")
    public ResponseEntity<ResponseLibraryDTO> getLibrary(
            @PathVariable String id) {


        try {
            LibraryId libraryId = LibraryMapper.toLibraryId(id);

            Library library = libService.findById(libraryId);
            ResponseLibraryDTO result = LibraryMapper.toDTO(library);

            result.add(linkTo(methodOn(LibraryRESTController.class)
                    .getLibrary(libraryId.toString()))
                    .withSelfRel());

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves the library associated with a specific user.
     *
     * <p>This endpoint resolves a library using the owning user's identifier.</p>
     *
     * <p>The returned resource includes HATEOAS links:</p>
     * <ul>
     *     <li>self link</li>
     * </ul>
     *
     * @param id the unique identifier of the user whose library is to be retrieved.
     * @return a {@link ResponseEntity} containing the {@link ResponseLibraryDTO} if found,
     * or an error status:
     * <ul>
     *     <li>200 OK – when the library is found</li>
     *     <li>404 Not Found – when no library exists for the given user</li>
     *     <li>400 Bad Request – if the user ID format is invalid</li>
     *     <li>500 Internal Server Error – for unexpected failures</li>
     * </ul>
     */
    @GetMapping("/user/{id}/library")
    public ResponseEntity<ResponseLibraryDTO> getLibraryByUserId(@PathVariable String id) {


        try {
            UserId userId = LibraryMapper.toUserId(id);

            Library library = libService.findByUserId(userId);
            ResponseLibraryDTO result = LibraryMapper.toDTO(library);

            result.add(linkTo(methodOn(LibraryRESTController.class)
                    .getLibraryByUserId(id))
                    .withSelfRel());

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}