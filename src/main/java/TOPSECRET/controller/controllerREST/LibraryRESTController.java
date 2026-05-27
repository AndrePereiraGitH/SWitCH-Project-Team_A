package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.LibraryLinkProvider;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.library.LibraryMapper;
import TOPSECRET.dto.library.request.RequestLibraryDTO;
import TOPSECRET.dto.library.response.ResponseLibraryDTO;
import TOPSECRET.services.LibraryService;

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

@RestController
@Tag(name = "Libraries", description = "Library management endpoints")
@RequestMapping(path = "/libraries")
public class LibraryRESTController {
    private final LibraryLinkProvider libLinkProvider;
    private final LibraryService libService;

    @Autowired
    public LibraryRESTController(LibraryLinkProvider libLinkProvider, LibraryService libService) {
        this.libLinkProvider = libLinkProvider;
        this.libService = libService;
    }

    @PostMapping
    public ResponseEntity<ResponseLibraryDTO> createLibrary(@RequestBody RequestLibraryDTO requestLibraryDTO, @RequestParam UserId userId) {
        try {
            Library library = libService.createAndSave(requestLibraryDTO.libraryName(), userId);
            ResponseLibraryDTO result = LibraryMapper.toDTO(library);

            List<Link> links = libLinkProvider.getLinksForLibraries(userId);
            links.forEach(result::add);
            result.add(linkTo(methodOn(LibraryRESTController.class)
                    .createLibrary(requestLibraryDTO, userId)).withSelfRel());

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
