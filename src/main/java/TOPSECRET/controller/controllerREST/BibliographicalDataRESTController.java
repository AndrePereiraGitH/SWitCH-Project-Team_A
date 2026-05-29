package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.BibliographicalDataLinkProvider;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.bibliographicalData.BibliographicalDataMapper;
import TOPSECRET.dto.bibliographicalData.request.requestBibliographicalDataDTO;
import TOPSECRET.dto.bibliographicalData.response.responseBibliographicalDataDTO;
import TOPSECRET.services.BibliographicalDataService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller responsible for managing bibliographical data resources.
 */
@RestController
@Tag(name = "Bibliographical Data", description = "Bibliographical Data management endpoint")
@RequestMapping(path = "/BibliographicalData")
public class BibliographicalDataRESTController {

    private final BibliographicalDataService service;
    private final BibliographicalDataLinkProvider provider;

    /**
     * Constructs a new BibliographicalDataRESTController.
     *
     * @param service  the service responsible for bibliographical data operations
     * @param provider the provider responsible for generating HATEOAS links
     */
    @Autowired
    public BibliographicalDataRESTController(
            BibliographicalDataService service,
            BibliographicalDataLinkProvider provider) {
        this.service = service;
        this.provider = provider;
    }

    /**
     * Retrieves all bibliographical data records.
     *
     * <p>Returns a list of {@link responseBibliographicalDataDTO}
     * representing all bibliographical entries available in the system.</p>
     *
     * @return a {@link ResponseEntity} containing the list of bibliographical
     *         data DTOs with HTTP status 200 (OK),
     *         HTTP status 400 (BAD REQUEST) if invalid arguments are provided,
     *         or HTTP status 500 (INTERNAL SERVER ERROR) if an unexpected error occurs
     */
    @GetMapping("/bibliographical-data")
    public ResponseEntity<List<responseBibliographicalDataDTO>> getAllBibliographicalData() {
        try {
            return ResponseEntity.ok(service.createDTOlist(service.findAll()));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Creates and persists bibliographical data for a user.
     *
     * <p>This endpoint receives bibliographical information, converts author and genre
     * names into their respective domain identifiers, and delegates the creation
     * process to the service layer.</p>
     *
     * <p>On successful creation, the response includes the created bibliographical
     * data along with HATEOAS links related to the user's bibliographical resources.</p>
     *
     * @param requestBibliographicalDataDTO the request payload containing:
     *                                      title, author names, original language,
     *                                      publication date, genres, and chunks of text
     * @param userId the identifier of the user creating the bibliographical data
     * @return a {@link ResponseEntity} containing:
     * <ul>
     *     <li><b>201 CREATED</b> with the created bibliographical data</li>
     *     <li><b>400 BAD REQUEST</b> if the request data is invalid</li>
     *     <li><b>403 FORBIDDEN</b> if the user does not have permission</li>
     *     <li><b>500 INTERNAL SERVER ERROR</b> for unexpected errors</li>
     * </ul>
     *
     * @throws IllegalArgumentException if the provided request data is invalid
     * @throws AccessDeniedException if the user is not authorized to perform this action
     */
    @PostMapping("/bibliographical-data")
    public ResponseEntity<responseBibliographicalDataDTO> createBibliographicalData(
            @RequestBody requestBibliographicalDataDTO requestBibliographicalDataDTO,
            @RequestParam UserId userId) {
        try {

            List<AuthorId> authorIds = BibliographicalDataMapper.toAuthorId(requestBibliographicalDataDTO.authorName());

            List<GenreId> genreIds = BibliographicalDataMapper.toGenreId(requestBibliographicalDataDTO.genreName());

            BibliographicalData bibliographicalData = service.createAndSave(
                    requestBibliographicalDataDTO.title(),
                    authorIds,
                    requestBibliographicalDataDTO.originalLanguage(),
                    requestBibliographicalDataDTO.publicationDate(),
                    genreIds,
                    requestBibliographicalDataDTO.chunksOfText(),
                    userId
            );

            responseBibliographicalDataDTO result = BibliographicalDataMapper.toDTO(
                    bibliographicalData,
                    service.getAuthors(bibliographicalData));

            List<Link> links = provider.getLinksForBibliographicalData(userId);
            links.forEach(result::add);
            result.add(linkTo(methodOn(BibliographicalDataRESTController.class)
                    .createBibliographicalData(requestBibliographicalDataDTO, userId)).withSelfRel());

            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
