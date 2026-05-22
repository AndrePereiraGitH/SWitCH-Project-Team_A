package TOPSECRET.dto.City;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.City.CityFactory;
import TOPSECRET.domain.Country.ICountryRepository;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.dto.City.request.RequestCityDTO;
import TOPSECRET.dto.City.response.ResponseCityDTO;

import java.util.Objects;

/**
 * Mapper responsible for converting between {@link City} domain objects
 * and their DTO representations.
 *
 * <p>This is the only class that knows both the domain model and the DTOs.
 * It is responsible for:</p>
 * <ul>
 *     <li>Unwrapping domain Value Objects into primitives ({@code toDTO})</li>
 *     <li>Wrapping primitives back into domain Value Objects ({@code toDomain})</li>
 *     <li>Resolving domain IDs into human-readable names via repositories ({@code toDTO})</li>
 *     <li>Delegating domain object creation to {@link CityFactory}</li>
 * </ul>
 *
 * <p>No business logic is performed here. This class is a pure translation layer.</p>
 */
public final class CityMapper {

    private final CityFactory cityFactory;
    private final ICountryRepository countryRepository;

    /**
     * Constructs a {@code CityMapper} with the required dependencies.
     *
     * @param cityFactory       the factory used to create {@link City} domain objects;
     *                          must not be {@code null}
     * @param countryRepository the repository used to resolve country identifiers
     *                          into human-readable names; must not be {@code null}
     */
    public CityMapper(CityFactory cityFactory, ICountryRepository countryRepository) {
        this.cityFactory = cityFactory;
        this.countryRepository = countryRepository;
    }

    /**
     * Converts a {@link City} domain object into a {@link ResponseCityDTO}.
     *
     * <p>Unwraps all Value Objects into their primitive representations
     * and resolves the country identifier into a human-readable name
     * via {@link ICountryRepository}, ensuring no domain types are
     * exposed outside the domain layer.</p>
     *
     * @param city the domain object to convert; must not be {@code null}
     * @return a {@link ResponseCityDTO} containing the city name and resolved country name
     * @throws NullPointerException if {@code city} is {@code null}
     */
    public ResponseCityDTO toDTO(City city) {
        Objects.requireNonNull(city, "City cannot be null");

        String countryName = countryRepository.findByID(city.getCityCountryID())
                .orElseThrow(() -> new IllegalArgumentException("Country not found"))
                .getCountryName();

        return new ResponseCityDTO(city.getCityName(), countryName);
    }

    /**
     * Converts a {@link RequestCityDTO} into a {@link City} domain object.
     *
     * <p>Wraps the incoming primitives into the appropriate Value Objects
     * and delegates instantiation to {@link CityFactory}.</p>
     *
     * @param dto the request DTO containing the city data; must not be {@code null}
     * @return a new {@link City} domain object
     * @throws NullPointerException     if {@code dto} is {@code null}
     * @throws IllegalArgumentException if any domain constraint is violated
     */
    public City toDomain(RequestCityDTO dto) {
        Objects.requireNonNull(dto, "City DTO cannot be null");
        return cityFactory.create(
                new CountryId(dto.countryId()),
                dto.cityName()
        );
    }
}