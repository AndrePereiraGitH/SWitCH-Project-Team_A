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

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller responsible for managing {@link Edition} resources.
 *
 * <p>Exposes endpoints for creating and retrieving editions, and coordinates
 * the interaction between DTOs, domain objects, application services, and
 * HATEOAS links.</p>
 *
 * <p>Endpoints are grouped into two categories:</p>
 * <ul>
 * <li><b>Private</b> — creation endpoints; require a valid authenticated user.</li>
 * <li><b>Public</b> — retrieval endpoints; no authentication required.</li>
 * </ul>
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
     */
    @Autowired
    public EditionRESTController(EditionService service, EditionLinkProvider provider) {
        this.service = service;
        this.provider = provider;
    }

    // =========================================================================
    // POST — private
    // =========================================================================

    /**
     * Creates a new {@link Edition} and persists it.
     *
     * <p>Maps the incoming DTO to domain objects, delegates creation to the service,
     * and enriches the response with public and private HATEOAS links based on the
     * requester's permissions.</p>
     *
     * @param dto    the request body containing the data required to create the edition
     * @param userId the ID of the user performing the operation
     * @return {@code HTTP 201 CREATED} with the created {@link EditionDTO},
     * {@code HTTP 400 BAD REQUEST} if the input parameters are invalid,
     * {@code HTTP 403 FORBIDDEN} if the user lacks the necessary permissions,
     * {@code HTTP 404 NOT FOUND} if an associated entity cannot be found,
     * or {@code HTTP 500 INTERNAL SERVER ERROR} on unexpected errors
     */
    @PostMapping("")
    public ResponseEntity<EditionDTO> createEdition(
            @RequestBody CreateEditionDTO dto,
            @RequestParam String userId) {

        try {
            UserId id = EditionMapper.toUserId(userId);

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
                    id
            );

            EditionDTO result = toEditionDTO(edition, id);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // =========================================================================
    // GET — public
    // =========================================================================

    /**
     * Retrieves all existing {@link Edition} resources.
     *
     * <p>Each entry in the response list is enriched with a self-link and public
     * navigational links. No authentication required.</p>
     *
     * @return {@code HTTP 200 OK} with a list of {@link EditionDTO},
     * or {@code HTTP 500 INTERNAL SERVER ERROR} on unexpected errors
     */
    @GetMapping("")
    public ResponseEntity<List<EditionDTO>> findAll() {

        try {
            List<Edition> allEditions = service.findAll();


            List<EditionDTO> result = new ArrayList<>();
            for (Edition edition : allEditions) {
                result.add(toEditionDTO(edition));
            }

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a single {@link Edition} by its unique identifier.
     *
     * <p>Returns the edition enriched with a self-link and public navigational links.
     * No authentication required.</p>
     *
     * @param editionId the unique identifier of the edition to retrieve
     * @return {@code HTTP 200 OK} with the {@link EditionDTO},
     * {@code HTTP 404 NOT FOUND} if the edition does not exist or the ID format is invalid,
     * or {@code HTTP 500 INTERNAL SERVER ERROR} on unexpected errors
     */
    @GetMapping("/{editionId}")
    public ResponseEntity<EditionDTO> findById(
            @PathVariable String editionId) {

        try {
            EditionId id = EditionMapper.toEditionId(editionId);

            Edition edition = service.findById(id);

            EditionDTO result = toEditionDTO(edition);

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves all {@link Edition} resources associated with a given publisher.
     *
     * <p>Each entry in the response list is enriched with a self-link and public
     * navigational links. No authentication required.</p>
     *
     * @param publisherId the unique identifier of the publisher to filter by
     * @return {@code HTTP 200 OK} with a list of {@link EditionDTO},
     * {@code HTTP 400 BAD REQUEST} if the publisher ID is invalid,
     * or {@code HTTP 500 INTERNAL SERVER ERROR} on unexpected errors
     */
    @GetMapping("/publisher/{publisherId}")
    public ResponseEntity<List<EditionDTO>> findByPublisherId(
            @PathVariable String publisherId) {

        try {
            PublisherID id = EditionMapper.toPublisherId(publisherId);

            List<Edition> editions = service.findByPublisherId(id);

            List<EditionDTO> result = new ArrayList<>();
            for (Edition edition : editions) {
                result.add(toEditionDTO(edition));
            }

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves the identifiers of all {@link Edition} resources associated
     * with a given publisher.
     *
     * <p>A lightweight endpoint suited for lookups where only the IDs are needed.
     * No authentication required.</p>
     *
     * @param publisherId the unique identifier of the publisher to filter by
     * @return {@code HTTP 200 OK} with a list of {@link EditionIdResponseDTO},
     * {@code HTTP 400 BAD REQUEST} if the publisher ID is invalid,
     * or {@code HTTP 500 INTERNAL SERVER ERROR} on unexpected errors
     */
    @GetMapping("/publisher/{publisherId}/ids")
    public ResponseEntity<List<EditionIdResponseDTO>> findEditionIdsByPublisherId(
            @PathVariable String publisherId) {

        try {
            PublisherID id = EditionMapper.toPublisherId(publisherId);

            List<EditionIdResponseDTO> result = new ArrayList<>();
            for (EditionId editionId : service.findIdsByPublisherId(id)) {
                EditionIdResponseDTO dto = EditionMapper.toEditionIdDTO(editionId);
                dto.add(linkTo(methodOn(EditionRESTController.class)
                        .findById(editionId.getID()))
                        .withSelfRel());
                result.add(dto);
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
     * <p>Each entry in the response list is enriched with a self-link and public
     * navigational links. No authentication required.</p>
     *
     * @param publicationTypeId the unique identifier of the publication type to filter by
     * @return {@code HTTP 200 OK} with a list of {@link EditionDTO},
     * {@code HTTP 400 BAD REQUEST} if the publication type ID is invalid,
     * or {@code HTTP 500 INTERNAL SERVER ERROR} on unexpected errors
     */
    @GetMapping("/publication-type/{publicationTypeId}")
    public ResponseEntity<List<EditionDTO>> findByPublicationTypeId(
            @PathVariable String publicationTypeId) {

        try {
            PublicationTypeId id = EditionMapper.toPublicationTypeId(publicationTypeId);

            List<Edition> editions = service.findByPublicationTypeId(id);

            List<EditionDTO> result = new ArrayList<>();
            for (Edition edition : editions) {
                result.add(toEditionDTO(edition));
            }


            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // =========================================================================
    // Private helpers
    // =========================================================================

    /**
     * Builds an {@link EditionDTO} for a public endpoint, enriching it with
     * public HATEOAS links and a self-link only.
     *
     * <p>No user identity is required. Only public links and a self-link are attached.</p>
     *
     * @param edition the edition to convert; must not be {@code null}
     * @return the populated {@link EditionDTO} with public links attached
     */
    private EditionDTO toEditionDTO(Edition edition)  {
        EditionDTO result = EditionMapper.toDTO(edition);

        List<Link> links = new ArrayList<>(provider.getPublicLinks());

        links.add(linkTo(methodOn(EditionRESTController.class)
                .findById(edition.id().getID()))
                .withSelfRel());

        links.forEach(result::add);

        return result;
    }

    /**
     * Builds an {@link EditionDTO} for a private endpoint, enriching it with
     * public links, private HATEOAS links based on the requesting user's permissions,
     * and a self-link.
     *
     * @param edition the edition to convert; must not be {@code null}
     * @param userId  the ID of the requesting user used to generate contextual links
     * @return the populated {@link EditionDTO} with public and private links attached
     * @throws InstanceNotFoundException if the associated user is not found by the link provider
     */
    private EditionDTO toEditionDTO(Edition edition, UserId userId) throws InstanceNotFoundException {
        EditionDTO result = EditionMapper.toDTO(edition);

        List<Link> links = new ArrayList<>(provider.getPublicLinks());
        links.addAll(provider.getCreateLinks(userId));

        links.add(linkTo(methodOn(EditionRESTController.class)
                .findById(edition.id().getID()))
                .withSelfRel());

        links.forEach(result::add);

        return result;
    }
}