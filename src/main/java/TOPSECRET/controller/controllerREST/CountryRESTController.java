package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.CountryLinkProvider;
import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.ValueObjects.CountryId;
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
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;

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
     * Returns the HTTP methods supported by the Country collection resource.
     *
     * <p>This endpoint allows clients to discover which operations are
     * available on the {@code /countries} resource without invoking them.</p>
     *
     * <p>The response includes an {@code Allow} header indicating the
     * supported HTTP methods:</p>
     * <ul>
     *     <li>{@code GET} - retrieve all countries</li>
     *     <li>{@code POST} - create a new country</li>
     *     <li>{@code OPTIONS} - discover supported operations</li>
     * </ul>
     *
     * @return a response containing the supported HTTP methods for the
     *         Country collection resource
     */
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> options() {
        return ResponseEntity.ok()
                .allow(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS)
                .build();
    }

    /**
     * Returns the HTTP methods supported by an individual Country resource.
     *
     * <p>This endpoint allows clients to discover which operations are
     * available on a specific {@code /countries/{id}} resource without
     * invoking them.</p>
     *
     * <p>The response includes an {@code Allow} header indicating the
     * supported HTTP methods:</p>
     * <ul>
     *     <li>{@code GET} - retrieve a country by its identifier</li>
     *     <li>{@code OPTIONS} - discover supported operations</li>
     * </ul>
     *
     * <p>This endpoint is intended to support resource discoverability
     * and improve client navigation in accordance with REST principles.</p>
     *
     * @return a response containing the supported HTTP methods for an
     *         individual Country resource
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsById() {
        return ResponseEntity.ok()
                .allow(HttpMethod.GET, HttpMethod.OPTIONS)
                .build();
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
    public ResponseEntity<CountryResponseDTO> registerCountry(
            @RequestBody RegisterCountryDTO dto,
            @RequestParam String userId) {

        try {

            UserId user = CountryMapper.toUserId(userId);

            Country country = service.createAndSave(
                    dto.countryName(),
                    user
            );

            CountryResponseDTO result = CountryMapper.toDTO(country);

            List<Link> links = provider.getLinks(user);

            links.forEach(result::add);

            result.add(linkTo(methodOn(CountryRESTController.class)
                    .findById(country.id().getCountryID()))
                    .withSelfRel());

            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (InstanceAlreadyExistsException e) {

            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        } catch (AccessDeniedException e) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        } catch (IllegalArgumentException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (InstanceNotFoundException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

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
    public ResponseEntity<List<CountryResponseDTO>> findAll() {

        try {
            List<Country> countries = service.findAll();

            List<CountryResponseDTO> result = countries.stream()
                    .map(country -> {
                        CountryResponseDTO dto = CountryMapper.toDTO(country);

                        dto.add(linkTo(methodOn(CountryRESTController.class)
                                .findById(country.id().getCountryID()))
                                .withSelfRel());

                        return dto;
                    })
                    .toList();

            if (result.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a single country by its unique identifier.
     *
     * <p>This endpoint fetches a {@link Country} from the system using its ID,
     * converts it into a {@link CountryResponseDTO}, and enriches it with
     * HATEOAS links for navigation.</p>
     *
     * <p>Included links:
     * <ul>
     *     <li>Self link to this country resource</li>
     *     <li>Link to retrieve all countries</li>
     * </ul>
     * </p>
     *
     * <p>HTTP responses:
     * <ul>
     *     <li>{@code 200 OK} if the country is found</li>
     *     <li>{@code 404 NOT FOUND} if no country exists with the given ID</li>
     *     <li>{@code 500 INTERNAL SERVER ERROR} for unexpected failures</li>
     * </ul>
     * </p>
     *
     * @param id the unique identifier of the country
     * @return a response entity containing the country DTO with links,
     *         or an error status if the operation fails
     */
    @GetMapping("/{id}")
    public ResponseEntity<CountryResponseDTO> findById(@PathVariable String id) {

        try {
            CountryId countryId = CountryMapper.toCountryId(id);

            Country country = service.findById(countryId);

            CountryResponseDTO result = CountryMapper.toDTO(country);

            result.add(linkTo(methodOn(CountryRESTController.class)
                    .findById(id))
                    .withSelfRel());

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}