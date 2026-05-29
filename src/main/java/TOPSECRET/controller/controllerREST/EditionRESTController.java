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
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    private final EditionService service;
    private final EditionLinkProvider provider;

    /**
     * Creates a new Edition REST controller.
     *
     * @param service  service responsible for Edition operations
     * @param provider provider responsible for generating HATEOAS links
     */
    public EditionRESTController(EditionService service, EditionLinkProvider provider) {
        this.service = service;
        this.provider = provider;
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
            @RequestParam UserId userId) {
        try {
            Edition edition = service.createAndSave(
                    dto.editionNumber(),
                    EditionMapper.toCode(dto),
                    EditionMapper.toPhysicalAttributes(dto),
                    EditionMapper.toPublicationTypeId(dto),
                    EditionMapper.toPublisherId(dto),
                    dto.mediaFormat(),
                    EditionMapper.toEditionLanguage(dto),
                    EditionMapper.toEditionDate(dto),
                    EditionMapper.toTypeOfBinding(dto),
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
}