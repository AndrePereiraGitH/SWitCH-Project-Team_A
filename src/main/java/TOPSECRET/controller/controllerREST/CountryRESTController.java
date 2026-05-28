package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.CountryLinkProvider;
import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.country.CountryMapper;
import TOPSECRET.dto.country.request.RegisterCountryDTO;
import TOPSECRET.dto.country.response.CountryResponseDTO;
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

/**
 * REST controller responsible for managing Country resources.
 *
 * <p>This controller exposes endpoints for creating and retrieving
 * Country resources and coordinates the interaction between
 * DTOs, domain objects and application services.</p>
 */
@RestController
@Tag(name = "Countries", description = "Countries management endpoint")
@RequestMapping(path = "/countries")
public class CountryRESTController {

    private final CountryService service;
    private final CountryLinkProvider provider;

    /**
     * Creates a new Country REST controller.
     *
     * @param service service responsible for Country operations
     * @param provider provider responsible for generating HATEOAS links
     */
    public CountryRESTController(
            CountryService service,
            CountryLinkProvider provider) {

        this.service = service;
        this.provider = provider;
    }

    /**
     * Creates and stores a new Country.
     *
     * <p>The request DTO is converted before delegating the
     * creation process to the application service.</p>
     *
     * <p>If the operation succeeds, a response DTO representing
     * the created Country is returned with HTTP status
     * {@code 201 CREATED}.</p>
     *
     * @param dto request data used to create the Country
     * @param userId identifier of the user performing the operation
     * @return response entity containing the created Country
     *         or an error status when the operation fails
     */
    @PostMapping("")
    public ResponseEntity<CountryResponseDTO> register(
            @RequestBody RegisterCountryDTO dto,
            @RequestParam UserId userId) {

        try {

            Country country = service.createAndSave(
                    dto.countryName(),
                    userId
            );

            CountryResponseDTO result = CountryMapper.toDTO(country);

            List<Link> links = new ArrayList<>(provider.getLinks(userId));

            links.add(linkTo(methodOn(CountryRESTController.class)
                    .register(dto, userId)).withSelfRel());

            links.forEach(result::add);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (InstanceAlreadyExistsException e) {

            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        } catch (AccessDeniedException e) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        } catch (IllegalArgumentException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves all countries available in the system.
     *
     * <p>If the operation succeeds, a list of response DTOs
     * is returned with HTTP status {@code 200 OK}.</p>
     *
     * @return list containing all registered countries
     *         or an error status when the operation fails
     */
    @GetMapping("")
    public ResponseEntity<List<CountryResponseDTO>> getCountries() {

        try {

            List<Country> countries = service.findAll();

            List<CountryResponseDTO> result = CountryMapper.toDTOs(countries);

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}