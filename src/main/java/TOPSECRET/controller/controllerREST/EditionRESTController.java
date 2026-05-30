package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.EditionLinkProvider;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.edition.EditionMapper;
import TOPSECRET.dto.edition.request.CreateEditionDTO;
import TOPSECRET.dto.edition.response.EditionDTO;
import TOPSECRET.dto.edition.response.EditionIdResponseDTO;
import TOPSECRET.services.EditionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * REST controller responsible for managing {@link Edition} resources.
 *
 * <p>Exposes endpoints for creating and retrieving editions.
 * Responses follow the HATEOAS pattern, including hypermedia links
 * that allow clients to navigate related resources.</p>
 *
 * <p>Base path: {@code /editions}</p>
 */
@RestController
@Tag(name = "Editions", description = "Editions management endpoint")
@RequestMapping(path = "/editions")
public class EditionRESTController {

    private final EditionService service;
    private final EditionLinkProvider provider;

    /**
     * Constructs a new {@code EditionRESTController} with the required dependencies.
     *
     * @param service  the service layer responsible for {@link Edition} business logic
     * @param provider the provider responsible for generating HATEOAS links
     *                 for edition responses
     */
    @Autowired
    public EditionRESTController(EditionService service, EditionLinkProvider provider) {
        this.service = service;
        this.provider = provider;
    }

    /**
     * Creates a new {@link Edition} and persists it.
     *
     * <p>The request DTO fields are mapped to domain value objects before
     * delegating creation to the application service. The response includes
     * HATEOAS links provided by {@link EditionLinkProvider} as well as a
     * self-link pointing to this endpoint.</p>
     *
     * @param dto    the request body containing the data required to create the edition
     * @param userId the ID of the user performing the operation, used for
     *               authorization and link generation
     * @return a {@link ResponseEntity} containing:
     *         <ul>
     *           <li>{@code 201 Created} with the created {@link EditionDTO}
     *               and HATEOAS links on success</li>
     *           <li>{@code 400 Bad Request} if the provided arguments are invalid</li>
     *           <li>{@code 403 Forbidden} if the user does not have permission
     *               to perform this operation</li>
     *           <li>{@code 500 Internal Server Error} for any unexpected failure</li>
     *         </ul>
     */
    @PostMapping("")
    public ResponseEntity<EditionDTO> createEdition(
            @RequestBody CreateEditionDTO dto,
            @RequestParam UserId userId) {
        try {
            Edition edition = service.createAndSave(
                    dto.editionNumber(),
                    EditionMapper.toCode(dto),
                    EditionMapper.toPhysicalAttributes(dto),
                    EditionMapper.toPublicationTypeId(dto.publicationTypeId()),
                    EditionMapper.toPublisherId(dto.publisherId()),
                    dto.mediaFormat(),
                    EditionMapper.toEditionLanguage(dto.editionLanguage()),
                    dto.editionDate(),
                    EditionMapper.toTypeOfBinding(dto.typeOfBinding()),
                    userId
            );

            EditionDTO result = EditionMapper.toDTO(edition);

            List<Link> links = new ArrayList<>(provider.getLinks(userId));
            links.add(linkTo(methodOn(EditionRESTController.class)
                    .createEdition(dto, userId)).withSelfRel());
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
     * Retrieves a single {@link Edition} by its unique identifier.
     *
     * <p>The response includes two HATEOAS links:
     * <ul>
     *   <li>A self-link pointing to this endpoint</li>
     *   <li>A {@code find-all} link pointing to the collection endpoint</li>
     * </ul>
     * </p>
     *
     * @param editionId the string representation of the edition's unique identifier,
     *                  provided as a path variable
     * @return a {@link ResponseEntity} containing:
     *         <ul>
     *           <li>{@code 200 OK} with the matching {@link EditionDTO}
     *               and HATEOAS links on success</li>
     *           <li>{@code 404 Not Found} if no edition exists for the given ID</li>
     *           <li>{@code 500 Internal Server Error} for any unexpected failure</li>
     *         </ul>
     */
    @GetMapping("/{editionId}")
    public ResponseEntity<EditionDTO> findById(
            @PathVariable String editionId) {

        try {
            EditionId id = EditionMapper.toEditionId(editionId);

            Edition edition = service.findById(id);

            EditionDTO editionDTO = EditionMapper.toDTO(edition);

            editionDTO.add(linkTo(methodOn(EditionRESTController.class)
                    .findById(editionId))
                    .withSelfRel());

            editionDTO.add(linkTo(methodOn(EditionRESTController.class)
                    .findAll())
                    .withRel("find-all"));

            return ResponseEntity.status(HttpStatus.OK).body(editionDTO);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves all existing {@link Edition} resources.
     *
     * <p>Each entry in the response list includes a self-link pointing to its
     * individual resource endpoint.</p>
     *
     * @return a {@link ResponseEntity} containing:
     *         <ul>
     *           <li>{@code 200 OK} with a list of {@link EditionDTO} objects,
     *               each enriched with a self-link</li>
     *           <li>{@code 204 No Content} if no editions exist</li>
     *           <li>{@code 500 Internal Server Error} for any unexpected failure</li>
     *         </ul>
     */
    @GetMapping("")
    public ResponseEntity<List<EditionDTO>> findAll() {

        try {
            List<Edition> allEditions = service.findAll();

            List<EditionDTO> result = allEditions.stream()
                    .map(edition -> {
                        EditionDTO dto = EditionMapper.toDTO(edition);

                        Link selfLink = linkTo(methodOn(EditionRESTController.class)
                                .findById(edition.id().getID()))
                                .withSelfRel();

                        dto.add(selfLink);
                        return dto;
                    })
                    .toList();

            if (result.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves the identifiers of all {@link Edition} resources associated
     * with a given publisher.
     *
     * <p>This is a lightweight endpoint suited for lookups where only the IDs
     * are needed, avoiding the overhead of fetching full edition data.</p>
     *
     * @param publisherId the string representation of the publisher's unique
     *                    identifier, provided as a path variable
     * @return a {@link ResponseEntity} containing:
     *         <ul>
     *           <li>{@code 200 OK} with a list of edition ID strings on success</li>
     *           <li>{@code 204 No Content} if no editions exist for the given
     *               publisher</li>
     *           <li>{@code 400 Bad Request} if the provided publisher ID is
     *               invalid</li>
     *           <li>{@code 500 Internal Server Error} for any unexpected failure</li>
     *         </ul>
     */
    @GetMapping("/publisher/{publisherId}/ids")
    public ResponseEntity<List<EditionIdResponseDTO>> findEditionIdsByPublisherId(
            @PathVariable String publisherId) {

        try {
            PublisherID id = EditionMapper.toPublisherId(publisherId);

            List<EditionIdResponseDTO> editionIds = service.findIdsByPublisherId(id)
                    .stream()
                    .map(editionId -> {
                        EditionIdResponseDTO dto = EditionMapper.toEditionIdDTO(editionId);

                        Link selfLink = linkTo(methodOn(EditionRESTController.class)
                                .findById(editionId.getID()))
                                .withSelfRel();

                        dto.add(selfLink);
                        return dto;
                    })
                    .toList();

            if (editionIds.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.status(HttpStatus.OK).body(editionIds);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves all {@link Edition} resources associated with a given publisher,
     * returning full edition data.
     *
     * <p>Each entry in the response list includes two HATEOAS links:
     * <ul>
     *   <li>A self-link pointing to the individual edition endpoint</li>
     *   <li>A {@code find-by-publisher} link pointing back to this endpoint</li>
     * </ul>
     * </p>
     *
     * @param publisherId the string representation of the publisher's unique
     *                    identifier, provided as a path variable
     * @return a {@link ResponseEntity} containing:
     *         <ul>
     *           <li>{@code 200 OK} with a list of {@link EditionDTO} objects,
     *               each enriched with HATEOAS links</li>
     *           <li>{@code 204 No Content} if no editions exist for the given
     *               publisher</li>
     *           <li>{@code 400 Bad Request} if the provided publisher ID is
     *               invalid</li>
     *           <li>{@code 500 Internal Server Error} for any unexpected failure</li>
     *         </ul>
     */
    @GetMapping("/publisher/{publisherId}")
    public ResponseEntity<List<EditionDTO>> findByPublisherId(
            @PathVariable String publisherId) {

        try {
            PublisherID id = EditionMapper.toPublisherId(publisherId);

            List<EditionDTO> result = service.findByPublisherId(id)
                    .stream()
                    .map(edition -> {
                        EditionDTO dto = EditionMapper.toDTO(edition);

                        dto.add(linkTo(methodOn(EditionRESTController.class)
                                .findById(edition.id().getID()))
                                .withSelfRel());

                        dto.add(linkTo(methodOn(EditionRESTController.class)
                                .findByPublisherId(publisherId))
                                .withRel("find-by-publisher"));

                        return dto;
                    })
                    .toList();

            if (result.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves all {@link Edition} resources associated with a given publication type.
     *
     * <p>Each entry in the response list includes two HATEOAS links:
     * <ul>
     *   <li>A self-link pointing to the individual edition endpoint</li>
     *   <li>A {@code find-by-publication-type} link pointing back to this endpoint</li>
     * </ul>
     * </p>
     *
     * @param publicationTypeId the string representation of the publication type's
     *                          unique identifier, provided as a path variable
     * @return a {@link ResponseEntity} containing:
     *         <ul>
     *           <li>{@code 200 OK} with a list of {@link EditionDTO} objects,
     *               each enriched with HATEOAS links</li>
     *           <li>{@code 204 No Content} if no editions exist for the given
     *               publication type</li>
     *           <li>{@code 400 Bad Request} if the provided publication type ID
     *               is invalid</li>
     *           <li>{@code 500 Internal Server Error} for any unexpected failure</li>
     *         </ul>
     */
    @GetMapping("/publication-type/{publicationTypeId}")
    public ResponseEntity<List<EditionDTO>> findByPublicationTypeId(
            @PathVariable String publicationTypeId) {

        try {
            PublicationTypeId id = EditionMapper.toPublicationTypeId(publicationTypeId);

            List<EditionDTO> result = service.findByPublicationTypeId(id)
                    .stream()
                    .map(edition -> {
                        EditionDTO dto = EditionMapper.toDTO(edition);

                        dto.add(linkTo(methodOn(EditionRESTController.class)
                                .findById(edition.id().getID()))
                                .withSelfRel());

                        dto.add(linkTo(methodOn(EditionRESTController.class)
                                .findByPublicationTypeId(publicationTypeId))
                                .withRel("find-by-publication-type"));

                        return dto;
                    })
                    .toList();

            if (result.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}