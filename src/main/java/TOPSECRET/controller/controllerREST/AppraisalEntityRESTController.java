package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.AppraisalEntityLinkProvider;
import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.appraisalEntity.AppraisalEntityMapper;
import TOPSECRET.dto.appraisalEntity.reponse.ResponseAppraisalEntityDTO;
import TOPSECRET.dto.appraisalEntity.request.RequestAppraisalEntityDTO;
import TOPSECRET.services.AppraisalEntityService;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;


/**
 * REST controller responsible for managing AppraisalEntity resources.
 *
 * <p>This controller exposes endpoints for creating and retrieving
 * AppraisalEntity resources and coordinates the interaction between
 * DTOs, domain objects and application services.</p>
 */
@RestController
@RequestMapping("/appraisals")
public class AppraisalEntityRESTController {

    private final AppraisalEntityService service;
    private final AppraisalEntityLinkProvider provider;

    /**
     * Creates a new AppraisalEntity REST controller.
     *
     * @param service service responsible for AppraisalEntity operations
     * @param provider provider responsible for generating HATEOAS links
     */
    public AppraisalEntityRESTController(AppraisalEntityService service, AppraisalEntityLinkProvider provider) {
        this.service = service;
        this.provider = provider;
    }

    /**
     * Creates and stores a new AppraisalEntity.
     *
     * <p>The request DTO is converted into domain value objects before
     * delegating the creation process to the application service.</p>
     *
     * <p>If the operation succeeds, a response DTO representing the
     * created AppraisalEntity is returned with HTTP status
     * {@code 201 CREATED}.</p>
     *
     * @param dto request data used to create the AppraisalEntity
     * @param userId identifier of the user performing the operation
     * @return a response entity containing the created AppraisalEntity
     *         or an error status when the operation fails
     */
    @PostMapping("/appraisal-entities")
    public ResponseEntity<ResponseAppraisalEntityDTO> registerAppraisalEntity(
            @RequestBody RequestAppraisalEntityDTO dto,
            @RequestParam UserId userId) {

        try {
            Contact contact = AppraisalEntityMapper.toContact(dto);
            List<PublicationTypeId> pubs = AppraisalEntityMapper.toPubTypeIds(dto);
            List<GenreId> genres = AppraisalEntityMapper.toGenreIds(dto);

            AppraisalEntity apEn = service.createAndSave(
                    dto.name(),
                    contact,
                    pubs,
                    genres,
                    userId
            );

            ResponseAppraisalEntityDTO result = AppraisalEntityMapper.toResponse(apEn);

            // Links
            List<Link> links = provider.getLinks(userId);
            links.forEach(result::add);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

