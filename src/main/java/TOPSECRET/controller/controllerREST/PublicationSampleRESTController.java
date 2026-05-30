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
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/publicationSamples")
@Tag(name="PublicationSamples", description= "Publication sample management endpoints")
public class PublicationSampleRESTController {

    private final PublicationSampleService publicationSampleService;
    private final GenreService genreService;
    private final BibliographicalDataService bibliographicalDataService;
    private final AuthorService authorService;
    private final UserService userService;
    private final PublicationSampleLinkProvider publicationSampleLinkProvider;


    @Autowired
    public PublicationSampleRESTController(UserService userService,
            PublicationSampleService publicationSampleService, GenreService genreService, BibliographicalDataService bibliographicalDataService, AuthorService authorService, PublicationSampleLinkProvider publicationSampleLinkProvider) {
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
            @PathVariable UserId userId) {


        BibliographicalDataId bdId = PublicationSampleMapper.toBdId(registerPublicationSampleDTO.bibliographicalDataId());
        EditionId editionId = PublicationSampleMapper.toEditionId(registerPublicationSampleDTO.editionId());
        Condition condition = PublicationSampleMapper.toCondition(registerPublicationSampleDTO.condition());

        try{

            PublicationSample pbSample =
                    publicationSampleService.createAndSave(bdId,userId,editionId,condition);

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


            List<Link> links = publicationSampleLinkProvider.getPublicationSampleLinks(userId);

            links.add(linkTo(methodOn(PublicationSampleRESTController.class).findById(pbSample.id())).withSelfRel());

            links.forEach(result::add);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (InstanceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/{publicationSampleId}")
    public ResponseEntity<PublicationDTO> findById(
            @PathVariable("publicationSampleId") PublicationSampleId publicationSampleId) {

        try{
            PublicationSample pbSample = publicationSampleService.findById(publicationSampleId);

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

            Link selfLink = linkTo(methodOn(PublicationSampleRESTController.class)
                    .findById(publicationSampleId)).withSelfRel();

            Link withRel = linkTo(methodOn(PublicationSampleRESTController.class)
                    .findById(publicationSampleId)).withRel("find-by-id");

            result.add(selfLink);
            result.add(withRel);

            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }





    private List<String> getGenres(BibliographicalData bd) {
        List<String> genres =
                bd.getGenres()
                .stream()
                .map(genreService::findById)
                .map(Genre::getGenreType)
                .toList();

        return genres;
    }


    private List<String> getAuthors(BibliographicalData bd){
        List<String> authors = bd.getAuthors().stream()
                .map(authorId -> authorService.findById(authorId).getName())
                .toList();
        return authors;
    }


    private String getOwner(PublicationSample publicationSample) {
        User user = userService.findByID(publicationSample.getOwnerID());
        String name = user.getUserName();
        return name;
    }



    //    @PostMapping("/publications/{id}/library")
//    public ResponseEntity<PublicationReferenceDTO> addPublicationToMyList(
//            @RequestBody AddPublicationToLibraryDTO addPublicationToLibraryDTO,
//            @PathVariable UserId userId) throws InstanceNotFoundException {
//
//
//        Library library = libraryService.findByUserId(userId)
//                .orElseThrow(() -> new InstanceNotFoundException("Library not found"));
//
//
//        PublicationSampleId pubSampleId =
//                PublicationSampleMapper.toPubId(addPublicationToLibraryDTO.publicationId());
//
//
//        PublicationSampleId publicationSampleId = publicationSampleService.findByLibraryId();
//
//
//        try{
//
//            PublicationSample publicationSample =
//                    publicationSampleService.addPublicationToLibrary(publicationSampleId,library,userId);
//
//
//            BibliographicalDataId bdId = publicationSample.getBibliographicalDataID();
//
//            BibliographicalData bd = bibliographicalDataService.findById(bdId)
//                    .orElseThrow(() -> new InstanceNotFoundException("BibliographicalData not found"));
//
//            PublicationReferenceDTO dto = PublicationSampleMapper.toReferenceDTO(publicationSample,bdId);
//
//
//        }catch(Exception e){}
//    }
}
