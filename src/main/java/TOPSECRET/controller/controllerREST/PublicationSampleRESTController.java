package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.PublicationSampleLinkProvider;
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
 * <p>Exposes endpoints for creating and querying publication samples, and coordinates
 * the interaction between DTOs, domain objects, application services, and HATEOAS links.</p>
 *
 * <p>Endpoints are grouped into two categories:</p>
 * <ul>
 *   <li><b>POST</b> — creation of a new publication sample.</li>
 *   <li><b>GET</b> — retrieval by identity, full collection, or filtered queries.</li>
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
     * @param userService                    the service responsible for user operations
     * @param publicationSampleService       the service responsible for publication sample operations
     * @param genreService                   the service responsible for genre lookups
     * @param bibliographicalDataService     the service responsible for bibliographical data lookups
     * @param authorService                  the service responsible for author lookups
     * @param publicationSampleLinkProvider  the provider responsible for generating HATEOAS links
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
            @RequestParam UserId userId) {

        BibliographicalDataId bdId = PublicationSampleMapper.toBdId(registerPublicationSampleDTO.bibliographicalDataId());
        EditionId editionId = PublicationSampleMapper.toEditionId(registerPublicationSampleDTO.editionId());
        Condition condition = PublicationSampleMapper.toCondition(registerPublicationSampleDTO.condition());

        try {
            PublicationSample pbSample =
                    publicationSampleService.createAndSave(bdId, userId, editionId, condition);

            PublicationDTO result = toPublicationDTO(pbSample, userId);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (InstanceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

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
            @RequestParam UserId userId) {

        try {
            PublicationSample pbSample = publicationSampleService.findById(publicationSampleId);

            PublicationDTO result = toPublicationDTO(pbSample, userId);

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

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
    public ResponseEntity<List<PublicationDTO>> findAll(@RequestParam UserId userId) {

        try {
            List<PublicationSample> pbSampleList = publicationSampleService.findAll();

            List<PublicationDTO> publicationDTOS = new ArrayList<>();
            for (PublicationSample pbSample : pbSampleList) {
                publicationDTOS.add(toPublicationDTO(pbSample, userId));
            }

            return ResponseEntity.status(HttpStatus.OK).body(publicationDTOS);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (InstanceNotFoundException e) {
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
            @RequestParam UserId userId) {

        try {
            List<PublicationSample> pbSampleList =
                    publicationSampleService.findByBibliographicalDataId(bibliographicalDataId);

            List<PublicationDTO> publicationDTOS = new ArrayList<>();
            for (PublicationSample pbSample : pbSampleList) {
                publicationDTOS.add(toPublicationDTO(pbSample, userId));
            }

            return ResponseEntity.status(HttpStatus.OK).body(publicationDTOS);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (InstanceNotFoundException e) {
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
            @RequestParam UserId userId) {

        try {
            List<PublicationSample> pbSampleList =
                    publicationSampleService.findByBibliographicalDataIdIn(bibliographicalDataIds);

            List<PublicationDTO> publicationDTOS = new ArrayList<>();
            for (PublicationSample pbSample : pbSampleList) {
                publicationDTOS.add(toPublicationDTO(pbSample, userId));
            }

            return ResponseEntity.status(HttpStatus.OK).body(publicationDTOS);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (InstanceNotFoundException e) {
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
            @RequestParam UserId userId) {

        try {
            List<PublicationSample> pbSampleList =
                    publicationSampleService.findByEditionIdIn(editionIds);

            List<PublicationDTO> publicationDTOS = new ArrayList<>();
            for (PublicationSample pbSample : pbSampleList) {
                publicationDTOS.add(toPublicationDTO(pbSample, userId));
            }

            return ResponseEntity.status(HttpStatus.OK).body(publicationDTOS);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (InstanceNotFoundException e) {
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
     * @param pbSample the publication sample to convert; must not be {@code null}
     * @param userId   the ID of the requesting user used to generate contextual links
     * @return the populated {@link PublicationDTO} with links attached
     * @throws InstanceNotFoundException if the associated bibliographical data or user is not found
     */
    private PublicationDTO toPublicationDTO(PublicationSample pbSample, UserId userId)
            throws InstanceNotFoundException {

        BibliographicalData bd = bibliographicalDataService.findById(pbSample.getBibliographicalDataID());

        PublicationDTO result = new PublicationDTO(
                pbSample.id().toString(),
                bd.getTitle(),
                getAuthors(bd),
                bd.getOriginalLanguage().toString(),
                getGenres(bd),
                getOwner(pbSample),
                pbSample.getCondition().toString()
        );

        List<Link> links = new ArrayList<>(
                publicationSampleLinkProvider.getPublicationSampleLinks(userId));
        links.add(linkTo(methodOn(PublicationSampleRESTController.class)
                .findById(pbSample.id(), userId)).withSelfRel());
        links.forEach(result::add);

        return result;
    }

    /**
     * Resolves the genre type strings for each genre associated with the given bibliographical data.
     *
     * @param bd the bibliographical data whose genres are to be resolved
     * @return a list of genre type strings; never {@code null}, may be empty
     */
    private List<String> getGenres(BibliographicalData bd) {
        return bd.getGenres()
                .stream()
                .map(genreService::findById)
                .map(Genre::getGenreType)
                .toList();
    }

    /**
     * Resolves the author names for each author associated with the given bibliographical data.
     *
     * @param bd the bibliographical data whose authors are to be resolved
     * @return a list of author name strings; never {@code null}, may be empty
     */
    private List<String> getAuthors(BibliographicalData bd) {
        return bd.getAuthors().stream()
                .map(authorId -> authorService.findById(authorId).getName())
                .toList();
    }

    /**
     * Resolves the username of the owner of the given publication sample.
     *
     * @param publicationSample the publication sample whose owner is to be resolved
     * @return the username of the owner
     */
    private String getOwner(PublicationSample publicationSample) {
        User user = userService.findByID(publicationSample.getOwnerID());
        return user.getUserName();
    }
}