package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.CityLinkProvider;
import TOPSECRET.domain.City.City;
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
import java.util.List;

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

            List<Link> links = provider.getLinks(userId);
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

            List<ResponseCityDTO> result = CityMapper.toDTOs(cities);

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}