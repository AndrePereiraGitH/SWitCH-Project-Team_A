package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.EditionLinkProvider;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.edition.EditionMapper;
import TOPSECRET.dto.edition.request.CreateEditionDTO;
import TOPSECRET.dto.edition.response.EditionDTO;
import TOPSECRET.services.EditionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * REST controller responsible for managing Edition resources.
 *
 * <p>This controller exposes endpoints for creating Edition resources
 * and coordinates the interaction between DTOs, domain objects
 * and application services.</p>
 */
@RestController
@Tag(name = "Editions", description = "Editions management endpoint")
@RequestMapping(path = "/editions")
public class EditionRESTController {

    private EditionService service;
    private EditionLinkProvider provider;
    private EditionService editionService;
    private EditionMapper mapper;
    private EditionLinkProvider editionLinkProvider;

    /**
     * Creates a new Edition REST controller.
     *
     * @param service  service responsible for Edition operations
     * @param provider provider responsible for generating HATEOAS links
     */
    public EditionRESTController(EditionService service, EditionLinkProvider provider, EditionMapper mapper) {
        this.service = service;
        this.provider = provider;
        this.mapper = mapper;
    }

    @Autowired
    public EditionRESTController(EditionService editionService, EditionLinkProvider editionLinkProvider) {
        this.editionService = editionService;
        this.editionLinkProvider = editionLinkProvider;
    }

    /**
     * Creates and stores a new Edition.
     *
     * <p>The request DTO is converted before delegating the
     * creation process to the application service.</p>
     *
     * <p>If the operation succeeds, a response DTO representing
     * the created Edition is returned with HTTP status
     * {@code 201 CREATED}.</p>
     *
     * @param dto    request data used to create the Edition
     * @param userId identifier of the user performing the operation
     * @return response entity containing the created Edition
     *         or an error status when the operation fails
     */
    @PostMapping("")
    public ResponseEntity<EditionDTO> createEdition(
            @RequestBody CreateEditionDTO dto,
            @PathVariable UserId userId) {
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

    @GetMapping("/{editionId}")
    public ResponseEntity<EditionDTO> findById(@RequestParam String editionId, @PathVariable UserId userId) {

        try {
            EditionId id = EditionMapper.toEditionId(editionId);

            Edition edition = editionService.findById(id);

            EditionDTO editionDTO = EditionMapper.toDTO(edition);

            editionDTO.add(linkTo(methodOn(EditionRESTController.class)
                    .findById(editionId, userId))
                    .withSelfRel());

            editionDTO.add(linkTo(methodOn(EditionRESTController.class)
                    .findAll(userId))
                    .withRel("find-all"));

            return ResponseEntity.status(HttpStatus.OK).body(editionDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("")
    public ResponseEntity<List<EditionDTO>> findAll(@PathVariable UserId userId) {

        try {

            List<Edition> allEditions = editionService.findAll();

            List<EditionDTO> result = allEditions.stream()
                    .map(edition -> {
                        EditionDTO dto = EditionMapper.toDTO(edition);

                        Link selfLink = linkTo(methodOn(EditionRESTController.class)
                                .findById(edition.id().getID(), userId))
                                .withSelfRel();

                        dto.add(selfLink);

                        return dto;
                    })
                    .toList();

            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }






}