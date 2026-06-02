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

import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.List;

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

            ResponseCityDTO result = CityMapper.toDTO(city, city.getCityName());

            List<Link> links = new ArrayList<>(provider.getLinks(userId));
            links.add(linkTo(methodOn(CityRESTController.class)
                    .getCityById(city.id().toString())).withSelfRel());
            links.forEach(result::add);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);

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
                ResponseCityDTO dto = CityMapper.toDTO(city, city.getCityName());
                dto.add(linkTo(methodOn(CityRESTController.class)
                        .getCityById(city.id().toString()))
                        .withSelfRel());
                result.add(dto);
            }
            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
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

            ResponseCityDTO result = CityMapper.toDTO(city, city.getCityName());

            result.add(linkTo(methodOn(CityRESTController.class)
                    .getCityById(id)).withSelfRel());
            result.add(linkTo(methodOn(CityRESTController.class)
                    .getCities()).withRel("cities"));

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}