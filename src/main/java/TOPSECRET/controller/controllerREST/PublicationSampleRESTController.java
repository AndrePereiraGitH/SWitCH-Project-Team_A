package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.PublicationSampleLinkProvider;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
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
    private final PublicationSampleLinkProvider publicationSampleLinkProvider;

    /**
     * Creates a new {@code PublicationSampleRESTController}.
     *
     * @param userService                   the service responsible for user operations
     * @param publicationSampleService      the service responsible for publication sample operations
     * @param genreService                  the service responsible for genre lookups
     * @param bibliographicalDataService    the service responsible for bibliographical data lookups
     * @param authorService                 the service responsible for author lookups
     * @param publicationSampleLinkProvider the provider responsible for generating HATEOAS links
     */
    @Autowired
    public PublicationSampleRESTController(UserService userService,
                                           PublicationSampleService publicationSampleService,
                                           GenreService genreService,
                                           BibliographicalDataService bibliographicalDataService,
                                           AuthorService authorService,
                                           PublicationSampleLinkProvider publicationSampleLinkProvider) {
        this.publicationSampleService = publicationSampleService;
        this.genreService = genreService;
        this.bibliographicalDataService = bibliographicalDataService;
        this.authorService = authorService;
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

            PublicationDTO result = toPublicationDTO(pbSample, id);

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

    // =========================================================================
    // GET — by identity
    // =========================================================================

    /**
     * Retrieves a publication sample by its unique identifier.
     *
     * <p>Returns the publication sample enriched with a self-link and navigational links
     * based on the requester's permissions.</p>
     *
     * @param publicationSampleId the unique identifier of the publication sample to retrieve
     * @param userId              the ID of the user performing the request
     * @return HTTP 200 with the {@link PublicationDTO}, HTTP 404 if not found, or HTTP 500 on error
     */
    @GetMapping("/{publicationSampleId}")
    public ResponseEntity<PublicationDTO> findById(
            @PathVariable("publicationSampleId") PublicationSampleId publicationSampleId,
            @RequestParam String userId) {

        try {
            UserId id = PublicationSampleMapper.toUserId(userId);

            PublicationSample pbSample = publicationSampleService.findById(publicationSampleId);

            PublicationDTO result = toPublicationDTO(pbSample, id);

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EntityNotFoundException | InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // =========================================================================
    // GET — collections
    // =========================================================================

    /**
     * Retrieves all publication samples in the system.
     *
     * <p>Each item in the returned list is enriched with a self-link and navigational
     * links based on the requester's permissions.</p>
     *
     * @param userId the ID of the user performing the request
     * @return HTTP 200 with a list of {@link PublicationDTO}, or an error status
     */
    @GetMapping("")
    public ResponseEntity<List<PublicationDTO>> findAll(@RequestParam String userId) {

        try {
            UserId id = PublicationSampleMapper.toUserId(userId);

            List<PublicationSample> pbSampleList = publicationSampleService.findAll();

            List<PublicationDTO> publicationDTOS = new ArrayList<>();
            for (PublicationSample pbSample : pbSampleList) {
                publicationDTOS.add(toPublicationDTO(pbSample, id));
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
     * <p>Each item in the returned list is enriched with a self-link and navigational
     * links based on the requester's permissions.</p>
     *
     * @param bibliographicalDataId the bibliographical data identifier to filter by
     * @param userId                the ID of the user performing the request
     * @return HTTP 200 with a filtered list of {@link PublicationDTO}, or an error status
     */
    @GetMapping(value = "", params = "bibliographicalDataId")
    public ResponseEntity<List<PublicationDTO>> findByBibliographicalDataId(
            @RequestParam BibliographicalDataId bibliographicalDataId,
            @RequestParam String userId) {

        try {
            UserId id = PublicationSampleMapper.toUserId(userId);

            List<PublicationSample> pbSampleList =
                    publicationSampleService.findByBibliographicalDataId(bibliographicalDataId);

            List<PublicationDTO> publicationDTOS = new ArrayList<>();
            for (PublicationSample pbSample : pbSampleList) {
                publicationDTOS.add(toPublicationDTO(pbSample, id));
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
     * Retrieves all publication samples whose bibliographical data ID is in the given list.
     *
     * <p>Each item in the returned list is enriched with a self-link and navigational
     * links based on the requester's permissions.</p>
     *
     * @param bibliographicalDataIds the list of bibliographical data identifiers to filter by
     * @param userId                 the ID of the user performing the request
     * @return HTTP 200 with a filtered list of {@link PublicationDTO}, or an error status
     */
    @GetMapping(value = "", params = "bibliographicalDataIds")
    public ResponseEntity<List<PublicationDTO>> findByBibliographicalDataIdIn(
            @RequestParam List<BibliographicalDataId> bibliographicalDataIds,
            @RequestParam String userId) {

        try {
            UserId id = PublicationSampleMapper.toUserId(userId);

            List<PublicationSample> pbSampleList =
                    publicationSampleService.findByBibliographicalDataIdIn(bibliographicalDataIds);

            List<PublicationDTO> publicationDTOS = new ArrayList<>();
            for (PublicationSample pbSample : pbSampleList) {
                publicationDTOS.add(toPublicationDTO(pbSample, id));
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
     * Retrieves all publication samples associated with any of the given edition IDs.
     *
     * <p>Each item in the returned list is enriched with a self-link and navigational
     * links based on the requester's permissions.</p>
     *
     * @param editionIds the list of edition identifiers to filter by
     * @param userId     the ID of the user performing the request
     * @return HTTP 200 with a filtered list of {@link PublicationDTO}, or an error status
     */
    @GetMapping(value = "", params = "editionIds")
    public ResponseEntity<List<PublicationDTO>> findByEditionIdIn(
            @RequestParam List<EditionId> editionIds,
            @RequestParam String userId) {

        try {
            UserId id = PublicationSampleMapper.toUserId(userId);

            List<PublicationSample> pbSampleList =
                    publicationSampleService.findByEditionIdIn(editionIds);

            List<PublicationDTO> publicationDTOS = new ArrayList<>();
            for (PublicationSample pbSample : pbSampleList) {
                publicationDTOS.add(toPublicationDTO(pbSample, id));
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
     * Retrieves all publication samples that belong to the specified collection.
     *
     * <p>Requires the requesting user to be authorized to query by collection.
     * Each item in the returned list is enriched with a self-link and navigational
     * links based on the requester's permissions.</p>
     *
     * @param collectionId the unique identifier of the collection to filter by
     * @param userId       the ID of the user performing the request
     * @return HTTP 200 with a filtered list of {@link PublicationDTO}, HTTP 403 if unauthorized,
     *         HTTP 404 if not found, or HTTP 500 on error
     */
    @GetMapping("/{collectionId}/publications")
    public ResponseEntity<List<PublicationDTO>> findByCollectionOfPublicationsId(
            @PathVariable("collectionId") CollectionOfPublicationsId collectionId,
            @RequestParam String userId) {

        try {
            UserId id = PublicationSampleMapper.toUserId(userId);

            List<PublicationSample> pbSampleList =
                    publicationSampleService.findByCollectionOfPublicationsId(collectionId, id);

            List<PublicationDTO> publicationDTOS = new ArrayList<>();
            for (PublicationSample pbSample : pbSampleList) {
                publicationDTOS.add(toPublicationDTO(pbSample, id));
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

    /**
     * Retrieves all publication samples owned by the given user that are assigned to a library.
     *
     * <p>Requires the requesting user to be authorized for this operation.
     * Each item in the returned list is enriched with a self-link and navigational
     * links based on the requester's permissions.</p>
     *
     * @param userId the ID of the user performing the request
     * @return HTTP 200 with a filtered list of {@link PublicationDTO}, HTTP 403 if unauthorized,
     *         HTTP 404 if not found, or HTTP 500 on error
     */
    @GetMapping(value = "", params = "inLibrary")
    public ResponseEntity<List<PublicationDTO>> findByUserIdAndLibraryIdIsNotNull(
            @RequestParam String userId) {

        try {
            UserId id = PublicationSampleMapper.toUserId(userId);

            List<PublicationSample> pbSampleList =
                    publicationSampleService.findByUserIdAndLibraryIdIsNotNull(id);

            List<PublicationDTO> publicationDTOS = new ArrayList<>();
            for (PublicationSample pbSample : pbSampleList) {
                publicationDTOS.add(toPublicationDTO(pbSample, id));
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

    /**
     * Retrieves all publication samples owned by the given user that are not assigned to any library.
     *
     * <p>Requires the requesting user to be authorized for this operation.
     * Each item in the returned list is enriched with a self-link and navigational
     * links based on the requester's permissions.</p>
     *
     * @param userId the ID of the user performing the request
     * @return HTTP 200 with a filtered list of {@link PublicationDTO}, HTTP 403 if unauthorized,
     *         HTTP 404 if not found, or HTTP 500 on error
     */
    @GetMapping(value = "", params = "notInLibrary")
    public ResponseEntity<List<PublicationDTO>> findByUserIdAndLibraryIdIsNull(
            @RequestParam String userId) {

        try {
            UserId id = PublicationSampleMapper.toUserId(userId);

            List<PublicationSample> pbSampleList =
                    publicationSampleService.findByUserIdAndLibraryIdIsNull(id);

            List<PublicationDTO> publicationDTOS = new ArrayList<>();
            for (PublicationSample pbSample : pbSampleList) {
                publicationDTOS.add(toPublicationDTO(pbSample, id));
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

    /**
     * Retrieves all publication samples in the user's library that are neither sold
     * nor listed for sale.
     *
     * <p>Requires the requesting user to be authorized for this operation. The library
     * is resolved internally from the user's identity.
     * Each item in the returned list is enriched with a self-link and navigational
     * links based on the requester's permissions.</p>
     *
     * @param userId the ID of the user performing the request
     * @return HTTP 200 with a filtered list of {@link PublicationDTO}, HTTP 403 if unauthorized,
     *         HTTP 404 if the user or library is not found, or HTTP 500 on error
     */
    @GetMapping(value = "", params = "availableInLibrary")
    public ResponseEntity<List<PublicationDTO>> findByLibraryIdAndPublicationStatusNotIn(
            @RequestParam String userId) {

        try {
            UserId id = PublicationSampleMapper.toUserId(userId);

            List<PublicationSample> pbSampleList =
                    publicationSampleService.findByLibraryIdAndPublicationStatusNotIn(id);

            List<PublicationDTO> publicationDTOS = new ArrayList<>();
            for (PublicationSample pbSample : pbSampleList) {
                publicationDTOS.add(toPublicationDTO(pbSample, id));
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

    /**
     * Retrieves all publication samples owned by the given user that are eligible
     * to be added to the specified collection.
     *
     * <p>A publication sample is considered eligible if it is assigned to a library
     * and not already present in the target collection.
     * Requires the requesting user to be authorized for this operation.
     * Each item in the returned list is enriched with a self-link and navigational
     * links based on the requester's permissions.</p>
     *
     * @param collectionId the unique identifier of the target collection
     * @param userId       the ID of the user performing the request
     * @return HTTP 200 with a filtered list of {@link PublicationDTO}, HTTP 403 if unauthorized,
     *         HTTP 404 if not found, or HTTP 500 on error
     */
    @GetMapping("/{collectionId}/available")
    public ResponseEntity<List<PublicationDTO>> findAvailableForCollection(
            @PathVariable("collectionId") CollectionOfPublicationsId collectionId,
            @RequestParam String userId) {

        try {
            UserId id = PublicationSampleMapper.toUserId(userId);

            List<PublicationSample> pbSampleList =
                    publicationSampleService.findAvailableForCollection(id, collectionId);

            List<PublicationDTO> publicationDTOS = new ArrayList<>();
            for (PublicationSample pbSample : pbSampleList) {
                publicationDTOS.add(toPublicationDTO(pbSample, id));
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

    // =========================================================================
    // PATCH — domain operations
    // =========================================================================

    /**
     * Assigns a publication sample to the user's library.
     *
     * <p>The library is resolved internally from the user's identity — no explicit
     * library identifier is required. Requires the requesting user to be authorized
     * for this operation.</p>
     *
     * @param publicationSampleId the unique identifier of the publication sample to assign
     * @param userId              the ID of the user performing the request
     * @return HTTP 200 with the updated {@link PublicationDTO}, HTTP 403 if unauthorized,
     *         HTTP 404 if the sample, user, or library is not found, or HTTP 500 on error
     */
    @PatchMapping("/{publicationSampleId}/library")
    public ResponseEntity<PublicationDTO> addPublicationToLibrary(
            @PathVariable PublicationSampleId publicationSampleId,
            @RequestParam String userId) {

        try {
            UserId id = PublicationSampleMapper.toUserId(userId);

            PublicationSample pbSample =
                    publicationSampleService.addPublicationToLibrary(publicationSampleId, id);

            PublicationDTO publicationDTO = toPublicationDTO(pbSample, id);

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

    /**
     * Assigns a publication sample to the specified collection.
     *
     * <p>Requires the requesting user to be authorized for this operation.</p>
     *
     * @param publicationSampleId the unique identifier of the publication sample to assign
     * @param collectionId        the unique identifier of the target collection
     * @param userId              the ID of the user performing the request
     * @return HTTP 200 with the updated {@link PublicationDTO}, HTTP 403 if unauthorized,
     *         HTTP 404 if the sample, user, or collection is not found, or HTTP 500 on error
     */
    @PatchMapping("/{publicationSampleId}/collection")
    public ResponseEntity<PublicationDTO> assignPublicationToCollection(
            @PathVariable PublicationSampleId publicationSampleId,
            @RequestParam CollectionOfPublicationsId collectionId,
            @RequestParam String userId) {

        try {
            UserId id = PublicationSampleMapper.toUserId(userId);

            PublicationSample pbSample =
                    publicationSampleService.assignPublicationToCollection(collectionId, publicationSampleId, id);

            PublicationDTO publicationDTO = toPublicationDTO(pbSample, id);

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

    // =========================================================================
    // Private helpers
    // =========================================================================

    /**
     * Builds a {@link PublicationDTO} from a {@link PublicationSample}, enriching it
     * with bibliographical data, authors, genres, owner information, and HATEOAS links.
     *
     * <p>Delegates DTO assembly to {@link PublicationSampleMapper#toPublicationDTO} and
     * attaches a self-link plus navigational links generated by the link provider.</p>
     *
     * @param pbSample the publication sample to convert; must not be {@code null}
     * @param userId   the ID of the requesting user used to generate contextual links
     * @return the populated {@link PublicationDTO} with links attached
     * @throws InstanceNotFoundException if the associated user is not found by the link provider
     */
    private PublicationDTO toPublicationDTO(PublicationSample pbSample, UserId userId)
            throws InstanceNotFoundException {

        BibliographicalData bd = bibliographicalDataService.findById(pbSample.getBibliographicalDataID());

        PublicationDTO result = PublicationSampleMapper.toPublicationDTO(
                pbSample,
                bd,
                getAuthors(bd),
                getGenres(bd),
                getOwner(pbSample));

        List<Link> links = new ArrayList<>(
                publicationSampleLinkProvider.getPublicationSampleLinks(userId));
        links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                .findById(pbSample.id(), userId.toString())).withSelfRel());
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
}