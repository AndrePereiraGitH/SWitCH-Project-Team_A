package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.BibliographicalDataLinkProvider;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.bibliographicalData.BibliographicalDataMapper;
import TOPSECRET.dto.bibliographicalData.request.requestBibliographicalDataDTO;
import TOPSECRET.dto.bibliographicalData.response.responseBibliographicalDataDTO;
import TOPSECRET.services.BibliographicalDataService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller responsible for managing bibliographical data resources.
 */
@RestController
@Tag(name = "Bibliographical Data", description = "Bibliographical Data management endpoint")
@RequestMapping(path = "/bibliographicalData")
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
     * Retrieves all bibliographical data entries and returns them as a HATEOAS-enabled
     * collection of {@code responseBibliographicalDataDTO}.
     *
     * <p>This endpoint fetches all bibliographical data records, builds a mapping of
     * associated author names, converts the entities into DTOs, and enriches the
     * response with relevant HATEOAS links.</p>
     *
     * @param userId the identifier of the user requesting the bibliographical data
     * @return a {@link ResponseEntity} containing a {@link CollectionModel} of
     *         {@code responseBibliographicalDataDTO} objects with HATEOAS links,
     *         or an appropriate HTTP error status if the request fails
     */
    @GetMapping
    public ResponseEntity<CollectionModel<responseBibliographicalDataDTO>> getAllBibliographicalData(
            @RequestParam String userId) {
        try {
            UserId idOfUser = new UserId(userId);

            List<BibliographicalData> bibliographicalDataList = service.findAll();

            Map<BibliographicalDataId, List<String>> authorNamesMap = service.buildAuthorNamesMap(bibliographicalDataList);

            List<responseBibliographicalDataDTO> responseList = BibliographicalDataMapper.toDTOs(
                    bibliographicalDataList,
                    authorNamesMap);

            for (responseBibliographicalDataDTO dto : responseList) {

                BibliographicalDataId bdId = new BibliographicalDataId(dto.getBdId());

                dto.add(linkTo(methodOn(BibliographicalDataRESTController.class)
                        .getBibliographicalDataById(bdId, userId))
                        .withSelfRel());
            }

            CollectionModel<responseBibliographicalDataDTO> collectionModel = CollectionModel.of(responseList);

            List<Link> links = provider.getLinksForBibliographicalData(idOfUser);
            collectionModel.add(links);

            return ResponseEntity.ok(collectionModel);

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
     */
    @PostMapping("/bibliographical-data")
    public ResponseEntity<responseBibliographicalDataDTO> createBibliographicalData(
            @RequestBody requestBibliographicalDataDTO requestBibliographicalDataDTO,
            @RequestParam String userId) {
        try {
            UserId idOfUser = new UserId(userId);

            List<AuthorId> authorIds = BibliographicalDataMapper.toAuthorId(requestBibliographicalDataDTO.authorName());

            List<GenreId> genreIds = BibliographicalDataMapper.toGenreId(requestBibliographicalDataDTO.genreName());

            BibliographicalData bibliographicalData = service.createAndSave(
                    requestBibliographicalDataDTO.title(),
                    authorIds,
                    requestBibliographicalDataDTO.originalLanguage(),
                    requestBibliographicalDataDTO.publicationDate(),
                    genreIds,
                    requestBibliographicalDataDTO.chunksOfText(),
                    idOfUser
            );

            responseBibliographicalDataDTO result = BibliographicalDataMapper.toDTO(
                    bibliographicalData,
                    service.getAuthors(bibliographicalData));

            List<Link> links = provider.getLinksForBibliographicalData(idOfUser);
            links.forEach(result::add);

            result.add(linkTo(methodOn(BibliographicalDataRESTController.class)
                    .getBibliographicalDataById(bibliographicalData.id(), userId))
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
     * Retrieves bibliographical data by its identifier.
     *
     * <p>The returned DTO includes the bibliographical data, its authors, HATEOAS links
     * available for the given user, and a self link pointing to this endpoint.</p>
     *
     * @param id the identifier of the bibliographical data to retrieve
     * @param userId the identifier of the user requesting the resource, used to resolve available links
     * @return a {@link ResponseEntity} containing the bibliographical data DTO
     * @throws InstanceNotFoundException if no bibliographical data exists for the given {@code id}
     */
    @GetMapping("/bibliographical-data/{id}")
    public ResponseEntity<responseBibliographicalDataDTO> getBibliographicalDataById(
            @PathVariable BibliographicalDataId id,
            @RequestParam String userId) throws InstanceNotFoundException {

        UserId idOfUser = new UserId(userId);

        BibliographicalData bd = service.findById(id);

        responseBibliographicalDataDTO result = BibliographicalDataMapper.toDTO(
                bd,
                service.getAuthors(bd)
        );

        provider.getLinksForBibliographicalData(idOfUser).forEach(result::add);

        result.add(linkTo(methodOn(BibliographicalDataRESTController.class)
                .getBibliographicalDataById(id, userId))
                .withSelfRel());

        return ResponseEntity.ok(result);
    }
}
