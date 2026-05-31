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

    private List<String> getGenres(BibliographicalData bd) {
        return bd.getGenres()
                .stream()
                .map(genreService::findById)
                .map(Genre::getGenreType)
                .toList();
    }

    private List<String> getAuthors(BibliographicalData bd) {
        return bd.getAuthors().stream()
                .map(authorId -> authorService.findById(authorId).getName())
                .toList();
    }

    private String getOwner(PublicationSample publicationSample) {
        User user = userService.findByID(publicationSample.getOwnerID());
        return user.getUserName();
    }
}