package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.CollectionOfPublicationsLinkProvider;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.collectionOfPublications.CollectionOfPublicationsMapper;
import TOPSECRET.dto.collectionOfPublications.request.RequestCollectionOfPublicationsDTO;
import TOPSECRET.dto.collectionOfPublications.response.ResponseCollectionOfPublicationsDTO;
import TOPSECRET.services.CollectionOfPublicationsService;
import jakarta.validation.Valid;
import org.springframework.hateoas.Link;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.List;

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
     * Creates a new private collection of publications for the given user.
     *
     * <p>Delegates creation to {@link CollectionOfPublicationsService} and returns
     * the persisted collection as a {@link ResponseCollectionOfPublicationsDTO},
     * enriched with HATEOAS links permitted for the requesting user.</p>
     *
     * @param requestCollectionDTO the request body containing the collection details;
     *                             must not be {@code null}
     * @param userId               the identifier of the user creating the collection;
     *                             must not be {@code null}
     *                             must not be {@code null}
     * @return a {@link ResponseEntity} containing the created {@link ResponseCollectionOfPublicationsDTO}
     * with HTTP status {@code 201 Created} on success,
     * {@code 400 Bad Request} if the input is invalid,
     * {@code 403 Forbidden} if the user lacks permission, or
     * {@code 500 Internal Server Error} if an unexpected error occurs
     */
    @PostMapping("collection-of-publications")
    public ResponseEntity<ResponseCollectionOfPublicationsDTO> createCollectionOfPublications(
            @RequestBody @Valid RequestCollectionOfPublicationsDTO requestCollectionDTO,
            @RequestParam UserId userId) {

        GenreId genreId = CollectionOfPublicationsMapper.toGenreId(requestCollectionDTO.genreId());

        try {
            CollectionOfPublications collection = collectionService.createAndSave(
                    requestCollectionDTO.collectionName(), genreId, userId);

            ResponseCollectionOfPublicationsDTO result = CollectionOfPublicationsMapper.toDTO(collection);

            List<Link> links = linkProvider.getLinksForCollectionOfPublications(userId);

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
     * Makes the specified collection of publications permanently public.
     *
     * <p>Delegates the operation to {@link CollectionOfPublicationsService} and returns
     * the result as a boolean response body.</p>
     *
     * @param collectionId the identifier of the collection to make public;
     *                     must not be {@code null}
     * @param userId       the identifier of the user requesting the operation;
     *                     must not be {@code null}
     * @return a {@link ResponseEntity} containing {@code true} if the collection was changed,
     *         {@code false} if it was already public, with HTTP status {@code 200 OK} on success,
     *         {@code 400 Bad Request} if the input is invalid,
     *         {@code 403 Forbidden} if the user lacks permission, or
     *         {@code 500 Internal Server Error} if an unexpected error occurs
     */
    @PatchMapping("/{collectionId}/make-public")
    public ResponseEntity<Boolean> makeCollectionPublic(
            @PathVariable CollectionOfPublicationsId collectionId,
            @RequestParam UserId userId) {

        try {
            boolean result = collectionService.makeCollectionPublic(collectionId, userId);
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
     * <p>Delegates the operation to {@link CollectionOfPublicationsService} and returns
     * the result as a boolean response body.</p>
     *
     * @param collectionId the identifier of the collection to make public;
     *                     must not be {@code null}
     * @param publicUntil  the date and time until which the collection should remain public;
     *                     must not be {@code null} and must be in the future
     * @param userId       the identifier of the user requesting the operation;
     *                     must not be {@code null}
     * @return a {@link ResponseEntity} containing {@code true} if the collection was changed,
     *         {@code false} if it was already public until the same date and time,
     *         with HTTP status {@code 200 OK} on success,
     *         {@code 400 Bad Request} if the input is invalid,
     *         {@code 403 Forbidden} if the user lacks permission, or
     *         {@code 500 Internal Server Error} if an unexpected error occurs
     */
    @PatchMapping("/{collectionId}/make-public-until")
    public ResponseEntity<Boolean> makeCollectionPublicUntil(
            @PathVariable CollectionOfPublicationsId collectionId,
            @RequestParam LocalDateTime publicUntil,
            @RequestParam UserId userId) {

        try {
            boolean result = collectionService.makeCollectionPublicUntil(
                    collectionId, publicUntil, userId);
            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}