package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.PublisherLinkProvider;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.PublisherID;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.publisher.PublisherMapper;
import TOPSECRET.dto.publisher.request.RequestPublisherDTO;
import TOPSECRET.dto.publisher.response.ResponsePublisherDTO;
import TOPSECRET.services.PublisherService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST Controller for managing Publisher resources.
 *
 * <p>This controller provides endpoints for the lifecycle of a {@link Publisher},
 * including creation and management of publisher entities. It integrates with
 * HATEOAS to provide dynamic hypermedia links based on the requester's permissions.</p>
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
     * <p>This method maps the incoming DTO to domain objects, saves the publisher via the service,
     * and enriches the response with HATEOAS links tailored to the provided user's permissions.</p>
     *
     * @param requestPublisherDTO the data transfer object containing the new publisher's details.
     * @param userId              the unique identifier of the user performing the creation.
     * @return a {@link ResponseEntity} containing the created {@link ResponsePublisherDTO} and HTTP 201 status,
     * or an error status (400, 403, 500) if the operation fails.
     */
    @PostMapping
    public ResponseEntity<ResponsePublisherDTO> createPublisher(
            @RequestBody RequestPublisherDTO requestPublisherDTO,
            @RequestParam UserId userId) {

        try {
            Contact contact = PublisherMapper.toContact(requestPublisherDTO);

            Publisher publisher = publisherService.createAndSave(
                    requestPublisherDTO.name(),
                    requestPublisherDTO.vatNumber(),
                    contact,
                    userId
            );

            ResponsePublisherDTO result = PublisherMapper.toDTO(publisher);

            List<Link> links = publisherLinkProvider.getLinksForPublisher(userId);
            links.forEach(result::add);

            result.add(linkTo(methodOn(PublisherRESTController.class)
                    .createPublisher(requestPublisherDTO, userId))
                    .withSelfRel());

            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Returns all publishers registered in the system.
     *
     * <p>Each item in the response is enriched with a self-link pointing to its
     * individual resource endpoint. Returns HTTP 204 if no publishers exist.</p>
     *
     * @return a {@link ResponseEntity} containing a list of {@link ResponsePublisherDTO},
     * HTTP 204 if the list is empty, or HTTP 500 if an unexpected error occurs.
     */
    @GetMapping
    public ResponseEntity<List<ResponsePublisherDTO>> getAllPublishers() {

        try {
            List<Publisher> publishers = publisherService.findAll();

            List<ResponsePublisherDTO> result = publishers.stream()
                    .map(publisher -> {
                        ResponsePublisherDTO dto = PublisherMapper.toDTO(publisher);
                        dto.add(linkTo(methodOn(PublisherRESTController.class)
                                .getPublisher(publisher.id()))
                                .withSelfRel());
                        return dto;
                    })
                    .toList();

            if (result.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Returns a single Publisher identified by its unique ID.
     *
     * <p>Enriches the response with a self-link and a link to the full publisher collection.</p>
     *
     * @param id the {@link PublisherID} of the publisher to retrieve.
     * @return a {@link ResponseEntity} containing the {@link ResponsePublisherDTO} and HTTP 200 status,
     * HTTP 404 if the publisher is not found, or HTTP 500 if an unexpected error occurs.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponsePublisherDTO> getPublisher(
            @PathVariable PublisherID id) {

        try {
            Publisher publisher = publisherService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Publisher not found"));

            ResponsePublisherDTO result = PublisherMapper.toDTO(publisher);

            result.add(linkTo(methodOn(PublisherRESTController.class)
                    .getPublisher(id))
                    .withSelfRel());

            result.add(linkTo(methodOn(PublisherRESTController.class)
                    .getAllPublishers())
                    .withRel("get-all-publishers"));

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}