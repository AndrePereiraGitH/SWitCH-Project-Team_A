package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.PublicationTypeLinkProvider;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.PublicationType.PublicationTypeMapper;
import TOPSECRET.dto.PublicationType.request.CreatePublicationTypeDTO;
import TOPSECRET.dto.PublicationType.response.PublicationTypeResponseDTO;
import TOPSECRET.services.PublicationTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller responsible for managing {@link PublicationType} resources.
 *
 * <p>Exposes endpoints for creating and retrieving publication types.
 * Responses follow the HATEOAS pattern, including hypermedia links
 * that allow clients to navigate related resources.</p>
 *
 * <p>Base path: {@code /publication-types}</p>
 */

@RestController
@Tag(name = "PublicationTypes", description = "Publication Types management endpoints")
@RequestMapping(path = "/publication-types")
public class PublicationTypeRestController {

    private final PublicationTypeService service;
    private final PublicationTypeLinkProvider publicationTypeLinkProvider;

    /**
     * Constructs a new {@code PublicationTypeRestController} with the required dependencies.
     *
     * @param publicationTypeService       the service layer responsible for
     *                                     {@link PublicationType} business logic
     * @param publicationTypeLinkProvider  the provider responsible for generating
     *                                     HATEOAS links for publication type responses
     */
    @Autowired
    public PublicationTypeRestController(
            PublicationTypeService publicationTypeService,
            PublicationTypeLinkProvider publicationTypeLinkProvider
    ) {
        this.service = publicationTypeService;
        this.publicationTypeLinkProvider = publicationTypeLinkProvider;
    }

    /**
     * Creates a new {@link PublicationType} and persists it.
     *
     * <p>The response includes HATEOAS links provided by {@link PublicationTypeLinkProvider}
     * as well as a self-link pointing to this endpoint.</p>
     *
     * @param createPublicationTypeDTO the request body containing the media format
     *                                 of the publication type to be created
     * @param  userId                  the ID of the user performing the operation,
     *                                 used for authorization and link generation
     * @return a {@link ResponseEntity} containing:
     *         <ul>
     *           <li>{@code 201 Created} with the created {@link PublicationTypeResponseDTO}
     *               and HATEOAS links on success</li>
     *           <li>{@code 400 Bad Request} if the provided arguments are invalid</li>
     *           <li>{@code 409 Conflict} if a publication type with the same format
     *               already exists</li>
     *           <li>{@code 403 Forbidden} if the user does not have permission
     *               to perform this operation</li>
     *           <li>{@code 500 Internal Server Error} for any unexpected failure</li>
     *         </ul>
     */
    @PostMapping("")
    public ResponseEntity<PublicationTypeResponseDTO> createPublicationType(
            @RequestBody CreatePublicationTypeDTO createPublicationTypeDTO,
            @RequestParam String userId){

        try {

            UserId id = PublicationTypeMapper.toUserId(userId);
            PublicationType publicationType = service.createAndSave(createPublicationTypeDTO.mediaFormat(), id);

            PublicationTypeResponseDTO result = PublicationTypeMapper.toDTO(publicationType);

            List<Link> links = publicationTypeLinkProvider.getLinksForPublicationType(id);
            links.forEach(result::add);
            result.add(linkTo(methodOn(PublicationTypeRestController.class)
                    .getPublicationTypeId(publicationType.id().toString()))
                    .withSelfRel()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch(InstanceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves all existing {@link PublicationType} resources.
     *
     * <p>Each entry in the response list includes a self-link pointing to its
     * individual resource endpoint.</p>
     *
     * @return a {@link ResponseEntity} containing:
     *         <ul>
     *           <li>{@code 200 OK} with a list of {@link PublicationTypeResponseDTO}
     *               objects, each enriched with a self-link</li>
     *           <li>{@code 204 No Content} if no publication types exist</li>
     *           <li>{@code 400 Bad Request} if the request contains invalid arguments</li>
     *           <li>{@code 500 Internal Server Error} for any unexpected failure</li>
     *         </ul>
     */
    @GetMapping("")
    public ResponseEntity<List<PublicationTypeResponseDTO>> getAllPublicationTypes(){

        try {
            List<PublicationType> publicationTypes = service.findAll();

            List<PublicationTypeResponseDTO> dtoList =
                    publicationTypes.stream()
                            .map(publicationType -> {
                                PublicationTypeResponseDTO dto =
                                        PublicationTypeMapper.toDTO(publicationType);
                                dto.add(
                                        linkTo(
                                                methodOn(PublicationTypeRestController.class)
                                                        .getPublicationTypeId(
                                                                publicationType.id().toString()
                                                        )
                                        ).withSelfRel()
                                );
                                return dto;
                            })
                            .toList();

            if(dtoList.isEmpty()){
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.status(HttpStatus.OK).body(dtoList);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a single {@link PublicationType} by its unique identifier.
     *
     * <p>The response includes two HATEOAS links:
     * <ul>
     *   <li>A self-link pointing to this endpoint</li>
     *   <li>A {@code get-all-publicationTypes} link pointing to the collection endpoint</li>
     * </ul>
     * </p>
     *
     * @param id the unique identifier of the publication type to retrieve
     * @return a {@link ResponseEntity} containing:
     *         <ul>
     *           <li>{@code 200 OK} with the matching {@link PublicationTypeResponseDTO}
     *               and HATEOAS links on success</li>
     *           <li>{@code 404 Not Found} if no publication type exists for the given ID</li>
     *           <li>{@code 500 Internal Server Error} for any unexpected failure</li>
     *         </ul>
     */
    @GetMapping("/{id}")
    public ResponseEntity<PublicationTypeResponseDTO> getPublicationTypeId(
            @PathVariable String id) {

        try {
            PublicationTypeId publicationTypeId = PublicationTypeMapper.toPublicationTypeId(id);

            PublicationType publicationType = service.findById(publicationTypeId);

            PublicationTypeResponseDTO result = PublicationTypeMapper.toDTO(publicationType);

            result.add(linkTo(methodOn(PublicationTypeRestController.class)
                    .getPublicationTypeId(id))
                    .withSelfRel());

            result.add(linkTo(methodOn(PublicationTypeRestController.class)
                    .getAllPublicationTypes())
                    .withRel("get-all-publicationTypes"));

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
