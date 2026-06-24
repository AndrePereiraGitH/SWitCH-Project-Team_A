package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.CityLinkProvider;
import TOPSECRET.domain.City.City;
import TOPSECRET.domain.ValueObjects.CityId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.city.CityMapper;
import TOPSECRET.dto.city.request.RequestCityDTO;
import TOPSECRET.dto.city.response.ResponseCityDTO;
import TOPSECRET.services.CityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;

import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller responsible for dealing with City resources.
 *
 * <p>This controller provides endpoints to create and retrieve City resources,
 * managing the communication between DTOs, domain entities, and application services</p>
 */
@RestController
@Tag(name = "Cities", description = "Cities management endpoint")
@RequestMapping(path = "/cities")
public class CityRESTController {

    private final CityService service;
    private final CityLinkProvider provider;

    /**
     * Creates a new City REST controller.
     *
     * @param service service responsible for City operations
     * @param provider provider responsible for generating HATEOAS links
     */
    public CityRESTController(
            CityService service,
            CityLinkProvider provider) {

        this.service = service;
        this.provider = provider;
    }

    /**
     * Handles OPTIONS requests for the city collection endpoint ({@code /cities}).
     *
     * <p>Returns the HTTP methods allowed for this endpoint in the {@code Allow} response header.
     * {@code GET} and {@code OPTIONS} are always included. {@code POST} is included only if the
     * requesting user is authorized to create cities.</p>
     *
     * @param userId the ID of the user performing the request
     * @return {@code 200 OK} with the {@code Allow} header reflecting the user's permitted methods,
     *         {@code 400 BAD REQUEST} if the user ID is invalid,
     *         or {@code 500 INTERNAL SERVER ERROR} on unexpected failure
     */
    @RequestMapping(value = "", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsRoot(@RequestParam UserId userId) {
        try {
            Set<HttpMethod> allowed = new HashSet<>(List.of(HttpMethod.GET, HttpMethod.OPTIONS));

            if (service.canCreateCity(userId)) {
                allowed.add(HttpMethod.POST);
            }

            return ResponseEntity.ok().allow(allowed.toArray(HttpMethod[]::new)).build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Handles OPTIONS requests for the city detail endpoint ({@code /cities/{id}}).
     *
     * <p>Returns the HTTP methods allowed for this endpoint in the {@code Allow} response header,
     * enabling CORS preflight requests and API discoverability.</p>
     *
     * @param id the unique identifier of the city; used for URL matching
     * @return {@code 200 OK} with the {@code Allow} header set to {@code GET, OPTIONS}
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsById(@PathVariable String id) {
        return ResponseEntity.ok()
                .allow(HttpMethod.GET,
                        HttpMethod.OPTIONS)
                .build();
    }

    /**
     * Creates and stores a new City.
     *
     * <p>The request DTO is converted before delegating the
     * creation process to the application service.</p>
     *
     * <p>If the operation succeeds, a response DTO representing
     * the created City is returned with HTTP status
     * {@code 201 CREATED}.</p>
     *
     * @param dto request data used to create the City
     * @param userId identifier of the user performing the operation
     * @return response entity containing the created City
     *         or an error status when the operation fails
     */
    @PostMapping("")
    public ResponseEntity<ResponseCityDTO> createCity(
            @RequestBody RequestCityDTO dto,
            @RequestParam UserId userId) {
        try {
            City city = service.createAndSave(dto.countryName(), dto.cityName(), userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(toCityDTO(city, userId));

        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves all official cities available in the system.
     *
     * <p>If the operation succeeds, a list of response DTOs
     * is returned with HTTP status {@code 200 OK}.</p>
     *
     * @return list containing all registered cities
     *         or an error status when the operation fails
     */
    @GetMapping("")
    public ResponseEntity<List<ResponseCityDTO>> getCities() {

        try {
            List<City> cities = service.findAll();

            List<ResponseCityDTO> result = new ArrayList<>();
            for (City city : cities) {
                result.add(toCityDTO(city));
            }
            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a City by its identifier.
     *
     * <p>If the operation succeeds, a response DTO representing
     * the found City is returned with HTTP status {@code 200 OK}.</p>
     *
     * @param id identifier of the City to retrieve
     * @return response entity containing the found City
     *         or an error status when the operation fails
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseCityDTO> getCityById(@PathVariable String id) {

        try {
            City city = service.findById(new CityId(id));
            return ResponseEntity.ok(toCityDTO(city));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Builds a {@link ResponseCityDTO} from a {@link City} for public endpoints,
     * enriching it with a self-link and a navigational link to the full city collection.
     *
     * <p>No user identity is required. Only public links are attached.</p>
     *
     * @param city the city to convert; must not be {@code null}
     * @return the populated {@link ResponseCityDTO} with public links attached
     */
    private ResponseCityDTO toCityDTO(City city) {
        ResponseCityDTO dto = CityMapper.toDTO(city, city.getCityName());
        dto.add(linkTo(methodOn(CityRESTController.class)
                .getCityById(city.id().toString())).withSelfRel());
        dto.add(linkTo(methodOn(CityRESTController.class)
                .getCities()).withRel("cities"));
        return dto;
    }

    /**
     * Builds a {@link ResponseCityDTO} from a {@link City} for authenticated endpoints,
     * enriching it with a self-link and contextual HATEOAS links based on the requesting user.
     *
     * <p>Always attaches a self-link. Additional navigational links are resolved
     * from the {@link CityLinkProvider} based on the user's identity and permissions.</p>
     *
     * @param city   the city to convert; must not be {@code null}
     * @param userId the ID of the requesting user used to generate contextual links
     * @return the populated {@link ResponseCityDTO} with contextual links attached
     */
    private ResponseCityDTO toCityDTO(City city, UserId userId) throws InstanceNotFoundException {
        ResponseCityDTO dto = CityMapper.toDTO(city, city.getCityName());
        List<Link> links = new ArrayList<>(provider.getLinks(userId));
        links.add(linkTo(methodOn(CityRESTController.class)
                .getCityById(city.id().toString())).withSelfRel());
        links.forEach(dto::add);
        return dto;
    }
}