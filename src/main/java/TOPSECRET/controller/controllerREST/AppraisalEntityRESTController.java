package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.AppraisalEntityLinkProvider;
import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.appraisalEntity.AppraisalEntityMapper;
import TOPSECRET.dto.appraisalEntity.response.ResponseAppraisalEntityDTO;
import TOPSECRET.dto.appraisalEntity.request.RequestAppraisalEntityDTO;
import TOPSECRET.dto.user.UserMapper;
import TOPSECRET.services.AppraisalEntityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


/**
 * REST controller responsible for managing AppraisalEntity resources.
 *
 * <p>This controller exposes endpoints for creating and retrieving
 * AppraisalEntity resources and coordinates the interaction between
 * DTOs, domain objects and application services.</p>
 */
@RestController
@Tag(name = "Appraisal Entities", description = "Appraisal Entities management endpoint")
@RequestMapping(path = "/appraisal-entities")
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
     * <p>The response is enriched with HATEOAS links according to
     * the permissions of the requesting user.</p>
     *
     * @param dto request data used to create the AppraisalEntity
     * @param userId identifier of the user performing the operation
     * @return HTTP 201 with the {@link ResponseAppraisalEntityDTO} if created successfully,
     *         HTTP 400 if the provided data is invalid,
     *         HTTP 403 if the user does not have permission to perform this operation,
     *         HTTP 404 if a referenced resource does not exist,
     *         or HTTP 500 on unexpected error
     */
    @PostMapping("")
    public ResponseEntity<ResponseAppraisalEntityDTO> registerAppraisalEntity(
            @RequestBody RequestAppraisalEntityDTO dto,
            @RequestParam String userId) {

        try {
            Contact contact = AppraisalEntityMapper.toContact(dto);
            List<PublicationTypeId> pubs = AppraisalEntityMapper.toPubTypeIds(dto);
            List<GenreId> genres = AppraisalEntityMapper.toGenreIds(dto);
            UserId id = UserMapper.toUserId(userId);

            AppraisalEntity apEn = service.createAndSave(
                    dto.name(),
                    contact,
                    pubs,
                    genres,
                    id
            );

            ResponseAppraisalEntityDTO result = AppraisalEntityMapper.toDTO(apEn);

            // Links
            List<Link> links = provider.getAdminLinks(id);
            links.forEach(result::add);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves all appraisal entities in the system.
     *
     * <p>Each item in the returned list is enriched with a self-link and public
     * navigational links. No authentication required.</p>
     *
     * @return HTTP 200 with a list of {@link ResponseAppraisalEntityDTO}, or an error status
     */
    @GetMapping("")
    public ResponseEntity<List<ResponseAppraisalEntityDTO>> findAll () {

        try {
            List<AppraisalEntity> apEnList = service.findAll();

            List<ResponseAppraisalEntityDTO> apEnDTO = new ArrayList<>();
            for (AppraisalEntity apEn : apEnList){
                apEnDTO.add(AppraisalEntityMapper.toDTO(apEn));

                List<Link> links = new ArrayList<>(provider.getPublicLinks());
                links.add(linkTo(methodOn(AppraisalEntityRESTController.class)
                        .findById(apEn.id().toString()))
                        .withSelfRel());

                apEnDTO.forEach(dto -> links.forEach(dto::add));
            }

            return ResponseEntity.status(HttpStatus.OK).body(apEnDTO);

        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves an appraisal entity by its unique identifier.
     *
     * <p>Returns the publication sample enriched with a self-link and public
     * navigational links. No authentication required.</p>
     *
     * @param apEnId the unique identifier of the appraisal entity to retrieve
     * @return HTTP 200 with the {@link ResponseAppraisalEntityDTO}, HTTP 404 if not found, or HTTP 500 on error
     */
    @GetMapping("/{appraisalEntityId}")
    public ResponseEntity<ResponseAppraisalEntityDTO> findById (
            @PathVariable("appraisalEntityId") String apEnId){

        try{
            AppraisalEntityId id = AppraisalEntityMapper.toApEnId(apEnId);

            AppraisalEntity apEn = service.findById(id);

            ResponseAppraisalEntityDTO result = AppraisalEntityMapper.toDTO(apEn);

            // Links
            List<Link> links = new ArrayList<>(provider.getPublicLinks());
            links.add(linkTo(methodOn(AppraisalEntityRESTController.class)
                    .findById(apEnId))
                    .withSelfRel());

            links.forEach(result::add);

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

