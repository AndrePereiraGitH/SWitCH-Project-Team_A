package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.PublisherLinkProvider;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.PublisherID;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.publisher.PublisherMapper;
import TOPSECRET.dto.publisher.request.RequestPublisherDTO;
import TOPSECRET.dto.publisher.response.ResponsePublisherDTO;
import TOPSECRET.dto.user.UserMapper;
import TOPSECRET.services.PublisherService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
 * REST Controller for managing Publisher resources.
 *
 * <p>This controller provides endpoints for the lifecycle of a {@link Publisher},
 * including creation and retrieval of publisher entities. It integrates with
 * HATEOAS to provide dynamic hypermedia links based on the requester's permissions.</p>
 *
 * <p>Endpoints are grouped into two categories:</p>
 * <ul>
 *   <li><b>POST</b> — creation of a new publisher; requires admin privileges.</li>
 *   <li><b>GET</b> — retrieval by identity or full collection; publicly accessible.</li>
 * </ul>
 */
@RestController
@Tag(name = "Publishers", description = "Publisher management endpoints")
@RequestMapping(path = "/publishers")
public class PublisherRESTController {

    private final PublisherLinkProvider publisherLinkProvider;
    private final PublisherService publisherService;

    @Autowired
    public PublisherRESTController(PublisherLinkProvider publisherLinkProvider, PublisherService publisherService) {
        this.publisherLinkProvider = publisherLinkProvider;
        this.publisherService = publisherService;
    }

    /**
     * Creates and registers a new Publisher in the system.
     *
     * <p>The request is mapped from {@link RequestPublisherDTO} into domain objects,
     * persisted via {@link PublisherService}, and returned as a
     * {@link ResponsePublisherDTO} enriched with HATEOAS links.</p>
     *
     * <p>Responses:</p>
     * <ul>
     *     <li>201 Created – Publisher successfully created</li>
     *     <li>400 Bad Request – invalid input data or user not found</li>
     *     <li>403 Forbidden – user is not authorized to perform this action</li>
     *     <li>404 Not Found – user does not exist</li>
     *     <li>500 Internal Server Error – unexpected failure</li>
     * </ul>
     *
     * @param requestPublisherDTO the publisher data to create
     * @param userId              the identifier of the user performing the operation
     * @return a {@link ResponseEntity} containing the created {@link ResponsePublisherDTO}
     * or an appropriate HTTP error status
     */
    @PostMapping
    public ResponseEntity<ResponsePublisherDTO> createPublisher(
            @Valid @RequestBody RequestPublisherDTO requestPublisherDTO,
            @NotBlank @RequestParam String userId) {

        try {
            UserId id = UserMapper.toUserId(userId);

            Contact contact = PublisherMapper.toContact(requestPublisherDTO);

            Publisher publisher = publisherService.createAndSave(
                    requestPublisherDTO.name(),
                    requestPublisherDTO.vatNumber(),
                    contact,
                    id
            );

            ResponsePublisherDTO result = PublisherMapper.toDTO(publisher);

            List<Link> links = publisherLinkProvider.getLinks(id);
            links.forEach(result::add);

            result.add(linkTo(methodOn(PublisherRESTController.class)
                    .getPublisher(publisher.id().getId()))
                    .withSelfRel());

            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves all publishers registered in the system.
     *
     * <p>Each returned publisher is enriched with public HATEOAS navigational links
     * and a self-link pointing to its individual resource endpoint.</p>
     *
     * <p>No authentication required.</p>
     *
     * <p>Responses:</p>
     * <ul>
     *     <li>200 OK – list of publishers returned; may be empty</li>
     *     <li>400 Bad Request – invalid request parameters</li>
     *     <li>500 Internal Server Error – unexpected failure</li>
     * </ul>
     *
     * @return a {@link ResponseEntity} containing a list of {@link ResponsePublisherDTO}
     * or an appropriate HTTP error status
     */
    @GetMapping
    public ResponseEntity<List<ResponsePublisherDTO>> getAllPublishers() {

        try {
            List<Publisher> publishers = publisherService.findAll();

            List<Link> publicLinks = new ArrayList<>(publisherLinkProvider.getPublicLinks());

            List<ResponsePublisherDTO> result = publishers.stream()
                    .map(publisher -> {
                        ResponsePublisherDTO dto = PublisherMapper.toDTO(publisher);
                        publicLinks.forEach(dto::add);
                        dto.add(linkTo(methodOn(PublisherRESTController.class)
                                .getPublisher(publisher.id().getId()))
                                .withSelfRel());
                        return dto;
                    })
                    .toList();

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a single Publisher by its unique identifier.
     *
     * <p>The returned resource is enriched with public HATEOAS navigational links
     * and a self-link pointing to this endpoint.</p>
     *
     * <p>No authentication required.</p>
     *
     * <p>Responses:</p>
     * <ul>
     *     <li>200 OK – publisher found and returned</li>
     *     <li>400 Bad Request – invalid ID format</li>
     *     <li>404 Not Found – no publisher exists for the given ID</li>
     *     <li>500 Internal Server Error – unexpected failure</li>
     * </ul>
     *
     * @param id the unique identifier of the publisher to retrieve
     * @return a {@link ResponseEntity} containing the {@link ResponsePublisherDTO}
     * or an appropriate HTTP error status
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponsePublisherDTO> getPublisher(
            @NotBlank @PathVariable String id) {

        try {
            PublisherID publisherId = PublisherMapper.toPublisherId(id);
            Publisher publisher = publisherService.findById(publisherId);
            ResponsePublisherDTO result = PublisherMapper.toDTO(publisher);


            List<Link> links = new ArrayList<>(publisherLinkProvider.getPublicLinks());
            links.forEach(result::add);

            result.add(linkTo(methodOn(PublisherRESTController.class)
                    .getPublisher(publisherId.getId()))
                    .withSelfRel());

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}