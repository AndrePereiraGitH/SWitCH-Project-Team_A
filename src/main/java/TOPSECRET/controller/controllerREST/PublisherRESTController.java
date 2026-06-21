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
import org.springframework.http.HttpMethod;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST Controller for managing Publisher resources.
 *
 * <p>This controller provides endpoints for the full lifecycle of a {@link Publisher},
 * including creation and retrieval of publisher entities. It integrates with
 * HATEOAS to provide dynamic hypermedia links based on the requester's permissions.</p>
 *
 * <p>Endpoints are grouped into two categories:</p>
 * <ul>
 *   <li><b>POST /publishers</b> — creation of a new publisher; requires admin privileges.</li>
 *   <li><b>GET /publishers</b> — retrieval of all publishers; publicly accessible.</li>
 *   <li><b>GET /publishers/{id}</b> — retrieval by identifier; publicly accessible.</li>
 *   <li><b>OPTIONS /publishers</b> — discovery of available HTTP methods.</li>
 *   <li><b>OPTIONS /publishers/{id}</b> — discovery of available HTTP methods for a specific publisher.</li>
 * </ul>
 *
 * <p>All responses are enriched with HATEOAS hypermedia links following REST level 3.</p>
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
     * Returns the available HTTP methods for the {@code /publishers} endpoint.
     *
     * <p>This endpoint supports HTTP method discovery. Clients can use it to
     * determine which operations are permitted on the {@code /publishers}
     * collection resource before attempting them.</p>
     *
     * @return 200 OK with an {@code Allow: GET, HEAD, POST, OPTIONS} response header
     */
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<?> optionsPublishers() {
        return ResponseEntity.ok()
                .allow(HttpMethod.GET, HttpMethod.HEAD, HttpMethod.POST, HttpMethod.OPTIONS)
                .build();
    }

    /**
     * Creates and registers a new Publisher in the system.
     *
     * <p>This endpoint is restricted to admin users. The request body is validated
     * and mapped from {@link RequestPublisherDTO} into domain objects, persisted via
     * {@link PublisherService}, and returned as a {@link ResponsePublisherDTO}
     * enriched with HATEOAS links.</p>
     *
     * <p>The response includes:</p>
     * <ul>
     *   <li>A self-link pointing to the created publisher's resource endpoint.</li>
     *   <li>Owner/admin links available to the requesting user.</li>
     * </ul>
     *
     * @param requestPublisherDTO the validated request body containing the publisher data:
     *                            name, VAT number, and contact information
     * @param userId              the identifier of the admin user performing the operation;
     *                            must not be blank
     * @return a {@link ResponseEntity} containing the created {@link ResponsePublisherDTO}
     * with HTTP 201 Created, or an appropriate error status:
     * <ul>
     *   <li>400 Bad Request – invalid input data or malformed arguments</li>
     *   <li>403 Forbidden – user does not have admin privileges</li>
     *   <li>404 Not Found – user does not exist in the system</li>
     *   <li>500 Internal Server Error – unexpected failure during processing</li>
     * </ul>
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
     * Returns the available HTTP methods for the {@code /publishers/{id}} endpoint.
     *
     * <p>This endpoint supports HTTP method discovery. Clients can use it to
     * determine which operations are permitted on a specific publisher resource
     * before attempting them.</p>
     *
     * @return 200 OK with an {@code Allow: GET, HEAD, OPTIONS} response header
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> optionsPublisherById() {
        return ResponseEntity.ok()
                .allow(HttpMethod.GET,  HttpMethod.HEAD, HttpMethod.OPTIONS)
                .build();
    }

    /**
     * Retrieves all publishers registered in the system.
     *
     * <p>This endpoint is publicly accessible and requires no authentication.
     * Each returned publisher is enriched with public HATEOAS navigational links
     * and a self-link pointing to its individual resource endpoint.</p>
     *
     * <p>The response includes:</p>
     * <ul>
     *   <li>A list of all publishers, each with a self-link and public navigation links.</li>
     *   <li>An empty list if no publishers are registered.</li>
     * </ul>
     *
     * @return a {@link ResponseEntity} containing a list of {@link ResponsePublisherDTO}
     * with HTTP 200 OK, or an appropriate error status:
     * <ul>
     *   <li>400 Bad Request – invalid request parameters</li>
     *   <li>500 Internal Server Error – unexpected failure during processing</li>
     * </ul>
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
     * <p>This endpoint is publicly accessible and requires no authentication.
     * The returned resource is enriched with public HATEOAS navigational links
     * and a self-link pointing to this endpoint.</p>
     *
     * <p>The response includes:</p>
     * <ul>
     *   <li>The publisher's full data as a {@link ResponsePublisherDTO}.</li>
     *   <li>Public navigation links available to any client.</li>
     *   <li>A self-link pointing to {@code /publishers/{id}}.</li>
     * </ul>
     *
     * @param id the unique identifier of the publisher to retrieve; must not be blank
     * @return a {@link ResponseEntity} containing the {@link ResponsePublisherDTO}
     * with HTTP 200 OK, or an appropriate error status:
     * <ul>
     *   <li>400 Bad Request – invalid or malformed ID format</li>
     *   <li>404 Not Found – no publisher exists for the given ID</li>
     *   <li>500 Internal Server Error – unexpected failure during processing</li>
     * </ul>
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