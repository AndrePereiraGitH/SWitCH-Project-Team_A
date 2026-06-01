package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.CollectionOfPublicationsLinkProvider;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.collectionOfPublications.CollectionOfPublicationsMapper;
import TOPSECRET.dto.collectionOfPublications.request.RequestCollectionOfPublicationsDTO;
import TOPSECRET.dto.collectionOfPublications.response.PublicCollectionOfPublicationsDTO;
import TOPSECRET.dto.collectionOfPublications.response.ResponseCollectionOfPublicationsDTO;
import TOPSECRET.services.CollectionOfPublicationsService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.hateoas.Link;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller for managing collections of publications.
 *
 * <p>Exposes HTTP endpoints for collection of publications-related operations,
 * delegating business logic to {@link CollectionOfPublicationsService} and
 * dynamically generating HATEOAS links via {@link CollectionOfPublicationsLinkProvider}.</p>
 */
@RestController
@Tag(name = "Collection Of Publications", description = "Collection Of Publications management endpoints")
@RequestMapping(path = "/collection-of-publications")
public class CollectionOfPublicationsRESTController {

    private final CollectionOfPublicationsService collectionService;
    private final CollectionOfPublicationsLinkProvider linkProvider;

    /**
     * Constructs a new {@code CollectionOfPublicationsRESTController} with the required dependencies.
     *
     * @param service      the service handling collection of publications business logic;
     *                     must not be {@code null}
     * @param linkProvider the provider responsible for generating HATEOAS links;
     *                     must not be {@code null}
     */
    @Autowired
    public CollectionOfPublicationsRESTController(
            CollectionOfPublicationsService service, CollectionOfPublicationsLinkProvider linkProvider) {

        this.collectionService = service;
        this.linkProvider = linkProvider;
    }

    /**
     * Creates a new collection of publications for the specified owner.
     *
     * <p>This endpoint receives the collection data, validates the request,
     * converts the incoming DTO values into their corresponding domain value
     * objects, and delegates the creation and persistence of the collection
     * to the application service.</p>
     *
     * <p>Upon successful creation, a {@link ResponseCollectionOfPublicationsDTO}
     * is returned enriched with HATEOAS links, including a self link to the
     * newly created collection and additional collection-related links.</p>
     *
     * @param requestCollectionDTO the request payload containing the collection
     *                             name and genre identifier; must be valid
     * @param ownerId the identifier of the user who owns the collection
     * @return a {@link ResponseEntity} containing the created
     *         {@link ResponseCollectionOfPublicationsDTO} and HTTP status:
     *         <ul>
     *             <li>{@code 201 CREATED} if the collection is successfully created</li>
     *             <li>{@code 400 BAD REQUEST} if the provided data is invalid</li>
     *             <li>{@code 403 FORBIDDEN} if the user is not authorized to create the collection</li>
     *             <li>{@code 500 INTERNAL SERVER ERROR} if an unexpected error occurs</li>
     *         </ul>
     */
    @PostMapping("/")
    public ResponseEntity<ResponseCollectionOfPublicationsDTO> createCollectionOfPublications(
            @RequestBody @Valid RequestCollectionOfPublicationsDTO requestCollectionDTO,
            @RequestParam String ownerId) {

        GenreId genreId = CollectionOfPublicationsMapper.toGenreId(requestCollectionDTO.genreId());
        UserId userId = CollectionOfPublicationsMapper.toUserId(ownerId);

        try {
            CollectionOfPublications collection = collectionService.createAndSave(
                    requestCollectionDTO.collectionName(), genreId, userId);

            ResponseCollectionOfPublicationsDTO responseDTO = CollectionOfPublicationsMapper.toDTO(collection);

            List<Link> links = linkProvider.getLinksForCollectionOfPublications(userId);

            links.add(linkTo(methodOn(CollectionOfPublicationsRESTController.class)
                    .getCollectionById(collection.id().toString(), userId.toString()))
                    .withSelfRel());

            links.forEach(responseDTO::add);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Makes the specified collection of publications permanently public.
     *
     * <p>Delegates the operation to {@link CollectionOfPublicationsService}, then retrieves
     * the updated collection and returns it as a {@link ResponseCollectionOfPublicationsDTO},
     * enriched with HATEOAS links permitted for the requesting user.</p>
     *
     * @param collectionId the identifier of the collection to make public;
     *                     must not be {@code null}
     * @param userId       the identifier of the user requesting the operation;
     *                     must not be {@code null}
     * @return a {@link ResponseEntity} containing the updated {@link ResponseCollectionOfPublicationsDTO}
     * with HTTP status {@code 200 OK} on success,
     * {@code 400 Bad Request} if the input is invalid,
     * {@code 403 Forbidden} if the user lacks permission, or
     * {@code 500 Internal Server Error} if an unexpected error occurs
     */
    @PatchMapping("/{collectionId}/make-public")
    public ResponseEntity<ResponseCollectionOfPublicationsDTO> makeCollectionPublic(
            @PathVariable CollectionOfPublicationsId collectionId,
            @RequestParam UserId userId) {

        try {
            collectionService.makeCollectionPublic(collectionId, userId);

            CollectionOfPublications collection = collectionService.findById(collectionId);

            ResponseCollectionOfPublicationsDTO result = CollectionOfPublicationsMapper.toDTO(collection);

            List<Link> links = linkProvider.getLinksForCollectionOfPublications(userId);
            links.forEach(result::add);

            result.add(linkTo(methodOn(CollectionOfPublicationsRESTController.class)
                    .makeCollectionPublic(collectionId, userId))
                    .withSelfRel());

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Makes the specified collection of publications public until the given date and time.
     *
     * <p>Delegates the operation to {@link CollectionOfPublicationsService}, then retrieves
     * the updated collection and returns it as a {@link ResponseCollectionOfPublicationsDTO},
     * enriched with HATEOAS links permitted for the requesting user.</p>
     *
     * @param collectionId the identifier of the collection to make public;
     *                     must not be {@code null}
     * @param publicUntil  the date and time until which the collection should remain public;
     *                     must not be {@code null} and must be in the future
     * @param userId       the identifier of the user requesting the operation;
     *                     must not be {@code null}
     * @return a {@link ResponseEntity} containing the updated {@link ResponseCollectionOfPublicationsDTO}
     * with HTTP status {@code 200 OK} on success,
     * {@code 400 Bad Request} if the input is invalid,
     * {@code 403 Forbidden} if the user lacks permission, or
     * {@code 500 Internal Server Error} if an unexpected error occurs
     */
    @PatchMapping("/{collectionId}/make-public-until")
    public ResponseEntity<ResponseCollectionOfPublicationsDTO> makeCollectionPublicUntil(
            @PathVariable CollectionOfPublicationsId collectionId,
            @RequestParam LocalDateTime publicUntil,
            @RequestParam UserId userId) {

        try {
            collectionService.makeCollectionPublicUntil(collectionId, publicUntil, userId);

            CollectionOfPublications collection = collectionService.findById(collectionId);

            ResponseCollectionOfPublicationsDTO result = CollectionOfPublicationsMapper.toDTO(collection);

            List<Link> links = linkProvider.getLinksForCollectionOfPublications(userId);
            links.forEach(result::add);

            result.add(linkTo(methodOn(CollectionOfPublicationsRESTController.class)
                    .makeCollectionPublicUntil(collectionId, publicUntil, userId))
                    .withSelfRel());

            return ResponseEntity.status(HttpStatus.OK).body(result);


        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves all currently public collections associated with a given genre.
     *
     * <p>A collection is considered public if it has been explicitly made public
     * and its visibility period has not expired. Collections that are private or
     * whose public visibility period has elapsed are not included in the result.</p>
     *
     * <p>The provided genre identifier is converted into a {@link GenreId} value
     * object and used to retrieve matching collections through the application
     * service. The resulting domain objects are mapped into
     * {@link PublicCollectionOfPublicationsDTO} instances before being returned.</p>
     *
     * @param genreId the identifier of the genre whose public collections are to
     *                be retrieved; must not be {@code null}
     * @return a {@link ResponseEntity} containing a list of
     *         {@link PublicCollectionOfPublicationsDTO} and:
     *         <ul>
     *             <li>{@code 200 OK} if the operation succeeds</li>
     *             <li>{@code 400 BAD REQUEST} if the provided genre identifier is invalid</li>
     *             <li>{@code 500 INTERNAL SERVER ERROR} if an unexpected error occurs</li>
     *         </ul>
     */
    @GetMapping("/public")
    public ResponseEntity<List<PublicCollectionOfPublicationsDTO>>
    findPublicCollectionsByGenreId(@RequestParam String genreId) {

        try {

            GenreId genreIdVO =
                    CollectionOfPublicationsMapper.toGenreId(genreId);

            List<CollectionOfPublications> collections =
                    collectionService.findPublicCollectionsByGenreId(genreIdVO);

            List<PublicCollectionOfPublicationsDTO> response =
                    CollectionOfPublicationsMapper.toPublicDTOList(collections);

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a collection of publications by its unique identifier.
     *
     * <p>This endpoint retrieves the requested
     * {@link CollectionOfPublications}, converts it into a
     * {@link ResponseCollectionOfPublicationsDTO}, and enriches the response
     * with HATEOAS links available to the requesting user.</p>
     *
     * <p>A self link pointing to the retrieved collection is also included
     * in the response.</p>
     *
     * @param collectionId the identifier of the collection to retrieve;
     *                     must correspond to an existing collection
     * @param userId       the identifier of the user requesting the resource;
     *                     used to determine which HATEOAS links should be exposed
     * @return a {@link ResponseEntity} containing the
     *         {@link ResponseCollectionOfPublicationsDTO} and:
     *         <ul>
     *             <li>{@code 200 OK} if the collection is successfully retrieved</li>
     *             <li>{@code 404 NOT FOUND} if no collection exists with the given identifier</li>
     *             <li>{@code 500 INTERNAL SERVER ERROR} if an unexpected error occurs</li>
     *         </ul>
     */
    @GetMapping("/{collectionId}")
    public ResponseEntity<ResponseCollectionOfPublicationsDTO> getCollectionById(
            @PathVariable String collectionId, @RequestParam String userId) {

        try {
            CollectionOfPublicationsId collectionIdVo = CollectionOfPublicationsMapper.toCollectionId(collectionId);
            UserId userIdVO = CollectionOfPublicationsMapper.toUserId(userId);
            CollectionOfPublications collection = collectionService.findById(collectionIdVo);
            ResponseCollectionOfPublicationsDTO responseDTO = CollectionOfPublicationsMapper.toDTO(collection);

            List<Link> links = linkProvider.getLinksForCollectionOfPublications(userIdVO);

            links.add(linkTo(methodOn(CollectionOfPublicationsRESTController.class)
                    .getCollectionById(collectionId, userId)).withSelfRel());

            links.forEach(responseDTO::add);

            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);

        } catch (InstanceNotFoundException | EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}