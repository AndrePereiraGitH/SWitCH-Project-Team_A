package TOPSECRET.controller.controllerREST.root;

import TOPSECRET.controller.controllerREST.root.linkProvider.PublisherLinkProvider;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.Contact;
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
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

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
     * Returns all publishers in the system.
     *
     * <p>This endpoint is used for discovery and listing purposes and is
     * exposed in HATEOAS links via {@link PublisherLinkProvider}.</p>
     *
     * @param userId the user requesting the operation
     * @return list of all publishers
     */
    @GetMapping
    public ResponseEntity<List<ResponsePublisherDTO>> getAllPublishers(
            @RequestParam UserId userId) {

        try {
            List<Publisher> publishers = publisherService.findAll();
            List<ResponsePublisherDTO> result = PublisherMapper.toDTOList(publishers);

            List<Link> links = publisherLinkProvider.getLinksForPublisher(userId);
            result.forEach(dto -> links.forEach(dto::add));

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}