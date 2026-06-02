package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.AuthorLinkProvider;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.author.AuthorMapper;
import TOPSECRET.dto.author.request.CreateAuthorDTO;
import TOPSECRET.dto.author.response.AuthorDTO;
import TOPSECRET.services.AuthorService;
import TOPSECRET.services.CountryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Tag(name = "Authors", description = "Authors management endpoint")
@RequestMapping(path = "/authors")
public class AuthorRESTController {

    private final AuthorService service;
    private final AuthorLinkProvider provider;
    private final CountryService countryService;

    public AuthorRESTController(AuthorService service, AuthorLinkProvider provider, CountryService countryService) {
        this.service = service;
        this.provider = provider;
        this.countryService = countryService;
    }

    @PostMapping("")
    public ResponseEntity<AuthorDTO> createAuthor(
            @RequestBody CreateAuthorDTO dto,
            @RequestParam String userId) {

        try {
            UserId id = AuthorMapper.toUserId(userId);

            Author author = service.createAndSave(
                    dto.name(),
                    AuthorMapper.toCountryId(dto),
                    dto.dateOfBirth(),
                    dto.biography(),
                    id
            );

            Country country = countryService.findById(author.getCountryID());
            AuthorDTO result = AuthorMapper.toDTO(author, country);

            List<Link> links = provider.getLinks(id);
            links.add(linkTo(AuthorRESTController.class)
                    .slash(author.id().toString())
                    .withSelfRel());
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

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> findAll(@RequestParam String userId) {

        try {
            UserId id = AuthorMapper.toUserId(userId);

            List<Author> authors = service.findAll();
            List<AuthorDTO> authorsDTO = new ArrayList<>();

            for (Author author : authors) {
                CountryId countryId = author.getCountryID();
                Country country = countryService.findById(countryId);

                AuthorDTO result = AuthorMapper.toDTO(author, country);

                List<Link> links = provider.getLinks(id);
                links.add(linkTo(methodOn(AuthorRESTController.class)
                        .findById(author.id().toString(), userId))
                        .withSelfRel());

                links.forEach(result::add);

                authorsDTO.add(result);
            }

            return ResponseEntity.status(HttpStatus.OK).body(authorsDTO);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{authorId}")
    public ResponseEntity<AuthorDTO> findById(
            @PathVariable("authorId") String authorId,
            @RequestParam String userId) {

        try {
            AuthorId id = AuthorMapper.toAuthorId(authorId);
            UserId uId = AuthorMapper.toUserId(userId);

            Author author = service.findById(id);
            CountryId countryId = author.getCountryID();
            Country country = countryService.findById(countryId);

            AuthorDTO result = AuthorMapper.toDTO(author, country);

            List<Link> links = provider.getLinks(uId);

            links.add(linkTo(methodOn(AuthorRESTController.class)
                    .findById(author.id().toString(), userId))
                    .withSelfRel());

            links.forEach(result::add);

            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}