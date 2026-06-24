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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.AccessDeniedException;

import java.util.*;

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
                ResponseAppraisalEntityDTO dto = AppraisalEntityMapper.toDTO(apEn);

                List<Link> links = new ArrayList<>(provider.getPublicLinks());
                links.add(linkTo(methodOn(AppraisalEntityRESTController.class)
                        .findById(apEn.id().toString()))
                        .withSelfRel());

                links.forEach(dto::add);
                apEnDTO.add(dto);
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
     * Returns the HTTP methods allowed on the appraisal entities root resource for the given user.
     *
     * <p>Always includes {@code GET} and {@code OPTIONS}. {@code POST} is included only if the
     * requesting user is authorized to register a new AppraisalEntity.</p>
     *
     * @param userId the ID of the user performing the request
     * @return HTTP 200 with an {@code Allow} header reflecting the user's permitted methods,
     *         HTTP 404 if the user is not found, or HTTP 400 if the user ID is invalid
     */
    @RequestMapping(value = "", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsRoot(@RequestParam String userId) {
        try {
            UserId id = UserMapper.toUserId(userId);

            Set<HttpMethod> allowed = new HashSet<>(List.of(HttpMethod.GET, HttpMethod.OPTIONS));

            if (service.canCreateAppraisalEntity(id)) {
                allowed.add(HttpMethod.POST);
            }

            return ResponseEntity.ok().allow(allowed.toArray(HttpMethod[]::new)).build();

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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

    /**
     * Handles OPTIONS requests for the appraisal entity detail endpoint ({@code /appraisal-entities/{appraisalEntityId}}).
     *
     * <p>Returns the HTTP methods allowed for this endpoint in the {@code Allow} response header,
     * enabling CORS preflight requests and API discoverability.</p>
     *
     * @param appraisalEntityId the unique identifier of the appraisal entity; used for URL matching
     * @return {@code 200 OK} with the {@code Allow} header set to {@code GET, OPTIONS}
     */
    @RequestMapping(value = "/{appraisalEntityId}", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsById(@PathVariable String appraisalEntityId) {
        return ResponseEntity.ok()
                .allow(HttpMethod.GET,
                        HttpMethod.OPTIONS)
                .build();
    }
}

