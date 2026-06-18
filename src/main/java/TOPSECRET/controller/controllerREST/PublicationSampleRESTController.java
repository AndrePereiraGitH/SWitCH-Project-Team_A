package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.PublicationSampleLinkProvider;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.publicationSample.PublicationSampleMapper;
import TOPSECRET.dto.publicationSample.request.RegisterPublicationSampleDTO;
import TOPSECRET.dto.publicationSample.response.PublicationDTO;
import TOPSECRET.services.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST Controller responsible for managing PublicationSample resources.
 *
 * <p>Exposes endpoints for creating, querying, and mutating publication samples,
 * and coordinates the interaction between DTOs, domain objects, application services,
 * and HATEOAS links.</p>
 *
 * <p>Endpoints are grouped into three categories:</p>
 * <ul>
 *   <li><b>POST</b> — creation of a new publication sample.</li>
 *   <li><b>GET</b> — retrieval by identity, full collection, or filtered queries.</li>
 *   <li><b>PATCH</b> — domain operations that mutate the state of a publication sample.</li>
 * </ul>
 */
@RestController
@RequestMapping(path = "/publicationSamples")
@Tag(name = "PublicationSamples", description = "Publication sample management endpoints")
public class PublicationSampleRESTController {

    private final PublicationSampleService publicationSampleService;
    private final GenreService genreService;
    private final BibliographicalDataService bibliographicalDataService;
    private final AuthorService authorService;
    private final UserService userService;
    private final EditionService editionService;
    private final PublicationSampleLinkProvider publicationSampleLinkProvider;

    /**
     * Enumerates the link contexts that drive which HATEOAS link groups are
     * attached to a {@link PublicationDTO} response.
     *
     * <ul>
     *   <li>{@code OWNER} — public and owner links.</li>
     *   <li>{@code LIBRARY} — public and library links.</li>
     *   <li>{@code COLLECTION} — public and collection links.</li>
     * </ul>
     */
    private enum LinkContext {
        OWNER,
        LIBRARY,
        COLLECTION
    }

    /**
     * Creates a new {@code PublicationSampleRESTController}.
     *
     * @param userService                   the service responsible for user operations
     * @param publicationSampleService      the service responsible for publication sample operations
     * @param genreService                  the service responsible for genre lookups
     * @param bibliographicalDataService    the service responsible for bibliographical data lookups
     * @param authorService                 the service responsible for author lookups
     * @param editionService                the service responsible for edition lookups
     * @param publicationSampleLinkProvider the provider responsible for generating HATEOAS links
     */
    @Autowired
    public PublicationSampleRESTController(UserService userService,
                                           PublicationSampleService publicationSampleService,
                                           GenreService genreService,
                                           BibliographicalDataService bibliographicalDataService,
                                           AuthorService authorService,
                                           EditionService editionService,
                                           PublicationSampleLinkProvider publicationSampleLinkProvider) {
        this.publicationSampleService = publicationSampleService;
        this.genreService = genreService;
        this.bibliographicalDataService = bibliographicalDataService;
        this.authorService = authorService;
        this.editionService = editionService;
        this.publicationSampleLinkProvider = publicationSampleLinkProvider;
        this.userService = userService;
    }

    // =========================================================================
    // POST
    // =========================================================================

    /**
     * Creates a new publication sample in the system.
     *
     * <p>Maps the incoming DTO to domain objects, delegates creation to the service,
     * and enriches the response with HATEOAS links based on the requester's permissions.</p>
     *
     * @param registerPublicationSampleDTO the DTO containing the new publication sample's data
     * @param userId                       the ID of the user performing the creation
     * @return HTTP 201 with the created {@link PublicationDTO}, or an error status
     */
    @PostMapping("")
    public ResponseEntity<PublicationDTO> createPublicationSample(
            @RequestBody RegisterPublicationSampleDTO registerPublicationSampleDTO,
            @RequestParam String userId) {

        try {
            UserId id = PublicationSampleMapper.toUserId(userId);
            BibliographicalDataId bdId = PublicationSampleMapper.toBdId(registerPublicationSampleDTO.bibliographicalDataId());
            EditionId editionId = PublicationSampleMapper.toEditionId(registerPublicationSampleDTO.editionId());
            Condition condition = PublicationSampleMapper.toCondition(registerPublicationSampleDTO.condition());

            PublicationSample pbSample =
                    publicationSampleService.createAndSave(bdId, id, editionId, condition);

            PublicationDTO result = toPublicationDTO(pbSample, id, LinkContext.OWNER);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (InstanceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (EntityNotFoundException | InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(value = "", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsRoot() {
        return ResponseEntity.ok()
                .allow(HttpMethod.GET,
                        HttpMethod.POST,
                        HttpMethod.OPTIONS)
                .build();
    }

    // =========================================================================
    // GET — by identity
    // =========================================================================

    /**
     * Retrieves a publication sample by its unique identifier.
     *
     * <p>Returns the publication sample enriched with a self-link and public
     * navigational links. No authentication required.</p>
     *
     * @param publicationSampleId the unique identifier of the publication sample to retrieve
     * @return HTTP 200 with the {@link PublicationDTO}, HTTP 404 if not found, or HTTP 500 on error
     */
    @GetMapping("/{publicationSampleId}")
    public ResponseEntity<PublicationDTO> findById(
            @PathVariable("publicationSampleId") String publicationSampleId) {

        try {
            PublicationSampleId pbSampleId = PublicationSampleMapper.toPubId(publicationSampleId);

            PublicationSample pbSample = publicationSampleService.findById(pbSampleId);

            PublicationDTO result = toPublicationDTO(pbSample);

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException | InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(value = "/{publicationSampleId}", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsById() {
        return ResponseEntity.ok()
                .allow(HttpMethod.GET,
                        HttpMethod.OPTIONS)
                .build();
    }

    // =========================================================================
    // GET — collections
    // =========================================================================

    /**
     * Retrieves all publication samples in the system.
     *
     * <p>Each item in the returned list is enriched with a self-link and public
     * navigational links. No authentication required.</p>
     *
     * @return HTTP 200 with a list of {@link PublicationDTO}, or an error status
     */
    @GetMapping("")
    public ResponseEntity<List<PublicationDTO>> findAll() {

        try {
            List<PublicationSample> pbSampleList = publicationSampleService.findAll();

            List<PublicationDTO> publicationDTOS = new ArrayList<>();
            for (PublicationSample pbSample : pbSampleList) {
                publicationDTOS.add(toPublicationDTO(pbSample));
            }

            return ResponseEntity.status(HttpStatus.OK).body(publicationDTOS);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException | InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves all publication samples associated with the given bibliographical data ID.
     *
     * <p>Each item in the returned list is enriched with a self-link and public
     * navigational links. No authentication required.</p>
     *
     * @param bibliographicalDataId the bibliographical data identifier to filter by
     * @return HTTP 200 with a filtered list of {@link PublicationDTO}, or an error status
     */
    @GetMapping("/by-bibliographical-data/{bibliographicalDataId}")
    public ResponseEntity<List<PublicationDTO>> findByBibliographicalDataId(
            @RequestParam String bibliographicalDataId) {

        try {
            BibliographicalDataId bdId = PublicationSampleMapper.toBibliographicalDataId(bibliographicalDataId);

            List<PublicationSample> pbSampleList =
                    publicationSampleService.findByBibliographicalDataId(bdId);

            List<PublicationDTO> publicationDTOS = new ArrayList<>();
            for (PublicationSample pbSample : pbSampleList) {
                publicationDTOS.add(toPublicationDTO(pbSample));
            }

            return ResponseEntity.status(HttpStatus.OK).body(publicationDTOS);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException | InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(value = "/by-bibliographical-data/{bibliographicalDataId}", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsByBibliographicalDataId() {
        return ResponseEntity.ok()
                .allow(HttpMethod.GET,
                        HttpMethod.OPTIONS)
                .build();
    }

    /**
     * Retrieves all publication samples whose bibliographical data ID is in the given list.
     *
     * <p>Each item in the returned list is enriched with a self-link and public
     * navigational links. No authentication required.</p>
     *
     * @param bibliographicalDataIds the list of bibliographical data identifiers to filter by
     * @return HTTP 200 with a filtered list of {@link PublicationDTO}, or an error status
     */
    @GetMapping("/by-bibliographical-data")
    public ResponseEntity<List<PublicationDTO>> findByBibliographicalDataIdIn(
            @RequestParam List<String> bibliographicalDataIds) {

        try {
            List<BibliographicalDataId> bdIds = PublicationSampleMapper.toBibliographicalDataIds(bibliographicalDataIds);

            List<PublicationSample> pbSampleList =
                    publicationSampleService.findByBibliographicalDataIdIn(bdIds);

            List<PublicationDTO> publicationDTOS = new ArrayList<>();
            for (PublicationSample pbSample : pbSampleList) {
                publicationDTOS.add(toPublicationDTO(pbSample));
            }

            return ResponseEntity.status(HttpStatus.OK).body(publicationDTOS);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException | InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(value = "/by-bibliographical-data", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsByBibliographicalDataIdIn() {
        return ResponseEntity.ok()
                .allow(HttpMethod.GET,
                        HttpMethod.OPTIONS)
                .build();
    }

    /**
     * Retrieves all publication samples associated with any of the given edition IDs.
     *
     * <p>Each item in the returned list is enriched with a self-link and public
     * navigational links. No authentication required.</p>
     *
     * @param editionIds the list of edition identifiers to filter by
     * @return HTTP 200 with a filtered list of {@link PublicationDTO}, or an error status
     */
    @GetMapping("/by-edition")
    public ResponseEntity<List<PublicationDTO>> findByEditionIdIn(
            @RequestParam List<String> editionIds) {

        try {
            List<EditionId> edIds = PublicationSampleMapper.toEditionIds(editionIds);

            List<PublicationSample> pbSampleList =
                    publicationSampleService.findByEditionIdIn(edIds);

            List<PublicationDTO> publicationDTOS = new ArrayList<>();
            for (PublicationSample pbSample : pbSampleList) {
                publicationDTOS.add(toPublicationDTO(pbSample));
            }

            return ResponseEntity.status(HttpStatus.OK).body(publicationDTOS);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException | InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(value = "/by-edition", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsByEditionIdIn() {
        return ResponseEntity.ok()
                .allow(HttpMethod.GET,
                        HttpMethod.OPTIONS)
                .build();
    }

    /**
     * Retrieves all publication samples that belong to the specified collection.
     *
     * <p>Requires the requesting user to be authorized to query by collection.
     * Each item in the returned list is enriched with a self-link and collection
     * navigational links based on the requester's permissions.</p>
     *
     * @param collectionId the unique identifier of the collection to filter by
     * @param userId       the ID of the user performing the request
     * @return HTTP 200 with a filtered list of {@link PublicationDTO}, HTTP 403 if unauthorized,
     * HTTP 404 if not found, or HTTP 500 on error
     */
    @GetMapping("/{collectionId}/publications")
    public ResponseEntity<List<PublicationDTO>> findByCollectionOfPublicationsId(
            @PathVariable("collectionId") String collectionId,
            @RequestParam String userId) {

        try {
            UserId id = PublicationSampleMapper.toUserId(userId);

            CollectionOfPublicationsId cpId = PublicationSampleMapper.toCollectionOfPublicationsId(collectionId);

            List<PublicationSample> pbSampleList =
                    publicationSampleService.findByCollectionOfPublicationsId(cpId, id);

            List<PublicationDTO> publicationDTOS = new ArrayList<>();
            for (PublicationSample pbSample : pbSampleList) {
                publicationDTOS.add(toPublicationDTO(pbSample, id, LinkContext.COLLECTION));
            }

            return ResponseEntity.status(HttpStatus.OK).body(publicationDTOS);

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException | InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(value = "/{collectionId}/publications", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsByCollectionOfPublicationsId() {
        return ResponseEntity.ok()
                .allow(HttpMethod.GET,
                        HttpMethod.OPTIONS)
                .build();
    }

    /**
     * Retrieves all publication samples owned by the given user.
     *
     * <p>Requires the requesting user to be authorized for this operation.
     * Each item in the returned list is enriched with a self-link and owner
     * navigational links based on the requester's permissions.</p>
     *
     * @param userId the ID of the user performing the request
     * @return HTTP 200 with a list of {@link PublicationDTO}, HTTP 403 if unauthorized,
     * HTTP 404 if the user is not found, or HTTP 500 on error
     */
    @GetMapping("/my-publications")
    public ResponseEntity<List<PublicationDTO>> findAllPublicationsOwnedByUser(
            @RequestParam String userId) {

        try {
            UserId id = PublicationSampleMapper.toUserId(userId);

            List<PublicationSample> allPublicationsOwnedByUser =
                    publicationSampleService.findAllPublicationsOwnedByUser(id);

            List<PublicationDTO> publicationDTOS = new ArrayList<>();

            for (PublicationSample pbSample : allPublicationsOwnedByUser) {
                publicationDTOS.add(toPublicationDTO(pbSample, id, LinkContext.OWNER));
            }

            return ResponseEntity.status(HttpStatus.OK).body(publicationDTOS);

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException | InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(value = "/my-publications", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsMyPublications() {
        return ResponseEntity.ok()
                .allow(HttpMethod.GET,
                        HttpMethod.OPTIONS)
                .build();
    }

    /**
     * Retrieves all publication samples owned by the given user that are assigned to a library.
     *
     * <p>Requires the requesting user to be authorized for this operation.
     * Each item in the returned list is enriched with a self-link and library
     * navigational links based on the requester's permissions.</p>
     *
     * @param userId the ID of the user performing the request
     * @return HTTP 200 with a filtered list of {@link PublicationDTO}, HTTP 403 if unauthorized,
     * HTTP 404 if not found, or HTTP 500 on error
     */
    @GetMapping("/in-library")
    public ResponseEntity<List<PublicationDTO>> findPublicationsInLibrary(
            @RequestParam String userId) {

        try {
            UserId id = PublicationSampleMapper.toUserId(userId);

            List<PublicationSample> pbSampleList =
                    publicationSampleService.findPublicationsInLibrary(id);

            List<PublicationDTO> publicationDTOS = new ArrayList<>();
            for (PublicationSample pbSample : pbSampleList) {
                publicationDTOS.add(toPublicationDTO(pbSample, id, LinkContext.LIBRARY));
            }

            return ResponseEntity.status(HttpStatus.OK).body(publicationDTOS);

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException | InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(value = "/in-library", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsInLibrary() {
        return ResponseEntity.ok()
                .allow(HttpMethod.GET,
                        HttpMethod.OPTIONS)
                .build();
    }

    /**
     * Retrieves all publication samples owned by the given user that are not assigned to any library.
     *
     * <p>Requires the requesting user to be authorized for this operation.
     * Each item in the returned list is enriched with a self-link and owner
     * navigational links based on the requester's permissions.</p>
     *
     * @param userId the ID of the user performing the request
     * @return HTTP 200 with a filtered list of {@link PublicationDTO}, HTTP 403 if unauthorized,
     *         HTTP 404 if not found, or HTTP 500 on error
     */
    @GetMapping("/not-in-library")
    public ResponseEntity<List<PublicationDTO>> findPublicationsNotInLibrary(
            @RequestParam String userId) {

        try {
            UserId id = PublicationSampleMapper.toUserId(userId);

            List<PublicationSample> pbSampleList =
                    publicationSampleService.findPublicationsNotInLibrary(id);

            List<PublicationDTO> publicationDTOS = new ArrayList<>();
            for (PublicationSample pbSample : pbSampleList) {
                publicationDTOS.add(toPublicationDTO(pbSample, id, LinkContext.OWNER));
            }

            return ResponseEntity.status(HttpStatus.OK).body(publicationDTOS);

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException | InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(value = "/not-in-library", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsNotInLibrary() {
        return ResponseEntity.ok()
                .allow(HttpMethod.GET,
                        HttpMethod.OPTIONS)
                .build();
    }

    /**
     * Retrieves all publication samples in the user's library that are neither sold
     * nor listed for sale.
     *
     * <p>Requires the requesting user to be authorized for this operation. The library
     * is resolved internally from the user's identity. Each item in the returned list
     * is enriched with a self-link and library navigational links based on the
     * requester's permissions.</p>
     *
     * @param userId the ID of the user performing the request
     * @return HTTP 200 with a filtered list of {@link PublicationDTO}, HTTP 403 if unauthorized,
     * HTTP 404 if the user or library is not found, or HTTP 500 on error
     */
    @GetMapping("/available-in-library")
    public ResponseEntity<List<PublicationDTO>> findByLibraryIdAndPublicationStatusNotIn(
            @RequestParam String userId) {

        try {
            UserId id = PublicationSampleMapper.toUserId(userId);

            List<PublicationSample> pbSampleList =
                    publicationSampleService.findByLibraryIdAndPublicationStatusNotIn(id);

            List<PublicationDTO> publicationDTOS = new ArrayList<>();
            for (PublicationSample pbSample : pbSampleList) {
                publicationDTOS.add(toPublicationDTO(pbSample, id, LinkContext.LIBRARY));
            }

            return ResponseEntity.status(HttpStatus.OK).body(publicationDTOS);

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException | InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(value = "/available-in-library", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsAvailableInLibrary() {
        return ResponseEntity.ok()
                .allow(HttpMethod.GET,
                        HttpMethod.OPTIONS)
                .build();
    }

    /**
     * Retrieves all publication samples owned by the given user that are eligible
     * to be added to the specified collection.
     *
     * <p>A publication sample is considered eligible if it is assigned to a library
     * and not already present in the target collection. Requires the requesting user
     * to be authorized for this operation. Each item in the returned list is enriched
     * with a self-link and collection navigational links based on the requester's
     * permissions.</p>
     *
     * @param collectionId the unique identifier of the target collection
     * @param userId       the ID of the user performing the request
     * @return HTTP 200 with a filtered list of {@link PublicationDTO}, HTTP 403 if unauthorized,
     * HTTP 404 if not found, or HTTP 500 on error
     */
    @GetMapping("/{collectionId}/available")
    public ResponseEntity<List<PublicationDTO>> findAvailableForCollection(
            @PathVariable("collectionId") String collectionId,
            @RequestParam String userId) {

        try {
            UserId id = PublicationSampleMapper.toUserId(userId);

            CollectionOfPublicationsId cpId = PublicationSampleMapper.toCollectionOfPublicationsId(collectionId);

            List<PublicationSample> pbSampleList =
                    publicationSampleService.findAvailableForCollection(id, cpId);

            List<PublicationDTO> publicationDTOS = new ArrayList<>();
            for (PublicationSample pbSample : pbSampleList) {
                publicationDTOS.add(toPublicationDTO(pbSample, id, LinkContext.COLLECTION));
            }

            return ResponseEntity.status(HttpStatus.OK).body(publicationDTOS);

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException | InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(value = "/{collectionId}/available", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsAvailableForCollection() {
        return ResponseEntity.ok()
                .allow(HttpMethod.GET,
                        HttpMethod.OPTIONS)
                .build();
    }


    /**
     * Retrieves all publication samples belonging to the specified public collection.
     *
     * <p>Unlike the owner-scoped collection endpoint, this method does not require the
     * requesting user to own the collection. The collection must be currently public;
     * if it is private, access is denied.</p>
     *
     * @param collectionId the unique identifier of the public collection to retrieve
     *                     publications from; must not be {@code null}
     * @param userId       the identifier of the requesting user; must not be {@code null}
     * @return a {@link ResponseEntity} containing a list of {@link PublicationDTO} and:
     * <ul>
     *     <li>{@code 200 OK} if the operation succeeds</li>
     *     <li>{@code 400 BAD REQUEST} if any identifier is invalid</li>
     *     <li>{@code 403 FORBIDDEN} if the collection is not public or the user lacks permission</li>
     *     <li>{@code 404 NOT FOUND} if the collection or user does not exist</li>
     *     <li>{@code 500 INTERNAL SERVER ERROR} if an unexpected error occurs</li>
     * </ul>
     */
    @GetMapping("/{collectionId}/shared-publications")
    public ResponseEntity<List<PublicationDTO>> findPublicationSampleInSharedCollection(
            @PathVariable("collectionId") String collectionId,
            @RequestParam String userId) {

        try {
            UserId id = PublicationSampleMapper.toUserId(userId);

            CollectionOfPublicationsId cpId = PublicationSampleMapper.toCollectionOfPublicationsId(collectionId);

            List<PublicationSample> pbSampleList = publicationSampleService.findPublicationsInPublicCollection(cpId, id);

            List<PublicationDTO> publicationDTOS = new ArrayList<>();
            for (PublicationSample pbSample : pbSampleList) {
                publicationDTOS.add(toPublicationDTO(pbSample));
            }

            return ResponseEntity.status(HttpStatus.OK).body(publicationDTOS);

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException | InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(value = "/{collectionId}/shared-publications", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsPublicationSampleInSharedCollection() {
        return ResponseEntity.ok()
                .allow(HttpMethod.GET,
                        HttpMethod.OPTIONS)
                .build();
    }

    // =========================================================================
    // PATCH — domain operations
    // =========================================================================

    /**
     * Assigns a publication sample to the user's library.
     *
     * <p>The library is resolved internally from the user's identity — no explicit
     * library identifier is required. Requires the requesting user to be authorized
     * for this operation and to own the publication sample.</p>
     *
     * @param publicationSampleId the unique identifier of the publication sample to assign
     * @param userId              the ID of the user performing the request
     * @return HTTP 200 with the updated {@link PublicationDTO}, HTTP 403 if unauthorized,
     * HTTP 404 if the sample, user, or library is not found, or HTTP 500 on error
     */
    @PatchMapping("/{publicationSampleId}/library")
    public ResponseEntity<PublicationDTO> addPublicationToLibrary(
            @PathVariable String publicationSampleId,
            @RequestParam String userId) {

        try {
            UserId id = PublicationSampleMapper.toUserId(userId);

            PublicationSampleId pbId = PublicationSampleMapper.toPubId(publicationSampleId);

            PublicationSample pbSample =
                    publicationSampleService.addPublicationToLibrary(pbId, id);

            PublicationDTO publicationDTO = toPublicationDTO(pbSample, id, LinkContext.LIBRARY);

            return ResponseEntity.status(HttpStatus.OK).body(publicationDTO);

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException | InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(value = "/{publicationSampleId}/library", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsLibrary() {
        return ResponseEntity.ok()
                .allow(HttpMethod.PATCH,
                        HttpMethod.OPTIONS)
                .build();
    }

    /**
     * Assigns a publication sample to the specified collection.
     *
     * <p>Requires the requesting user to be authorized for this operation and to
     * own the publication sample.</p>
     *
     * @param publicationSampleId the unique identifier of the publication sample to assign
     * @param collectionId        the unique identifier of the target collection
     * @param userId              the ID of the user performing the request
     * @return HTTP 200 with the updated {@link PublicationDTO}, HTTP 403 if unauthorized,
     * HTTP 404 if the sample, user, or collection is not found, or HTTP 500 on error
     */
    @PatchMapping("/{publicationSampleId}/collection")
    public ResponseEntity<PublicationDTO> assignPublicationToCollection(
            @PathVariable String publicationSampleId,
            @RequestParam String collectionId,
            @RequestParam String userId) {

        try {
            UserId id = PublicationSampleMapper.toUserId(userId);

            CollectionOfPublicationsId cpId = PublicationSampleMapper.toCollectionOfPublicationsId(collectionId);

            PublicationSampleId pbId = PublicationSampleMapper.toPubId(publicationSampleId);

            PublicationSample pbSample =
                    publicationSampleService.assignPublicationToCollection(cpId, pbId, id);

            PublicationDTO publicationDTO = toPublicationDTO(pbSample, id, LinkContext.COLLECTION);

            return ResponseEntity.status(HttpStatus.OK).body(publicationDTO);

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException | InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(value = "/{publicationSampleId}/collection", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsCollection() {
        return ResponseEntity.ok()
                .allow(HttpMethod.PATCH,
                        HttpMethod.OPTIONS)
                .build();
    }

    // =========================================================================
    // Private helpers
    // =========================================================================

    /**
     * Builds a {@link PublicationDTO} from a {@link PublicationSample}, enriching it
     * with bibliographical data, authors, genres, owner information, and HATEOAS links
     * appropriate to the given {@link LinkContext}.
     *
     * <p>Always attaches public links. Owner, library, or collection links are added
     * based on the context.</p>
     *
     * @param pbSample the publication sample to convert; must not be {@code null}
     * @param userId   the ID of the requesting user used to generate contextual links
     * @param context  the link context that determines which additional link groups are attached
     * @return the populated {@link PublicationDTO} with links attached
     * @throws InstanceNotFoundException if the associated user is not found by the link provider
     */
    private PublicationDTO toPublicationDTO(PublicationSample pbSample, UserId userId, LinkContext context)
            throws InstanceNotFoundException {

        BibliographicalData bd = bibliographicalDataService.findById(pbSample.getBibliographicalDataID());

        PublicationDTO result = PublicationSampleMapper.toPublicationDTO(
                pbSample,
                bd,
                getEdition(pbSample),
                getAuthors(bd),
                getGenres(bd),
                getOwner(pbSample));

        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                .findById(pbSample.id().getID())).withSelfRel());

        links.addAll(publicationSampleLinkProvider.getPublicLinks());

        switch (context) {
            case OWNER      -> links.addAll(publicationSampleLinkProvider.getOwnerLinks(userId));
            case LIBRARY    -> links.addAll(publicationSampleLinkProvider.getLibraryLinks(userId));
            case COLLECTION -> links.addAll(publicationSampleLinkProvider.getCollectionLinks(userId));
            default         -> {}
        }

        links.forEach(result::add);

        return result;
    }

    /**
     * Builds a {@link PublicationDTO} from a {@link PublicationSample} for public endpoints,
     * enriching it with bibliographical data, authors, genres, owner information, and public
     * HATEOAS links only.
     *
     * <p>No user identity is required. Only public links and a self-link are attached.</p>
     *
     * @param pbSample the publication sample to convert; must not be {@code null}
     * @return the populated {@link PublicationDTO} with public links attached
     * @throws InstanceNotFoundException if any dependent lookup fails
     */
    private PublicationDTO toPublicationDTO(PublicationSample pbSample)
            throws InstanceNotFoundException {

        BibliographicalData bd = bibliographicalDataService.findById(pbSample.getBibliographicalDataID());

        PublicationDTO result = PublicationSampleMapper.toPublicationDTO(
                pbSample,
                bd,
                getEdition(pbSample),
                getAuthors(bd),
                getGenres(bd),
                getOwner(pbSample));

        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                .findById(pbSample.id().getID())).withSelfRel());

        links.addAll(publicationSampleLinkProvider.getPublicLinks());

        links.forEach(result::add);

        return result;
    }

    /**
     * Resolves all {@link Genre} instances associated with the given bibliographical data.
     *
     * @param bd the bibliographical data whose genres are to be resolved
     * @return a list of {@link Genre} instances; never {@code null}, may be empty
     */
    private List<Genre> getGenres(BibliographicalData bd) {
        return bd.getGenres()
                .stream()
                .map(genreService::findById)
                .toList();
    }

    /**
     * Resolves all {@link Author} instances associated with the given bibliographical data.
     *
     * @param bd the bibliographical data whose authors are to be resolved
     * @return a list of {@link Author} instances; never {@code null}, may be empty
     */
    private List<Author> getAuthors(BibliographicalData bd) {
        return bd.getAuthors().stream()
                .map(authorService::findById)
                .toList();
    }

    /**
     * Resolves the {@link User} who owns the given publication sample.
     *
     * @param publicationSample the publication sample whose owner is to be resolved
     * @return the {@link User} who owns the publication sample
     */
    private User getOwner(PublicationSample publicationSample) {
        return userService.findByID(publicationSample.getOwnerID());
    }

    /**
     * Retrieves the {@link Edition} associated with the given {@link PublicationSample}.
     *
     * @param publicationSample the publication sample from which to extract the edition ID
     * @return the {@link Edition} corresponding to the publication sample's edition ID
     */
    private Edition getEdition(PublicationSample publicationSample) {
        return editionService.findById(publicationSample.getEditionID());
    }
}