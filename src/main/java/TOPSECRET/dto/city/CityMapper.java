package TOPSECRET.dto.city;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.dto.city.request.RequestCityDTO;
import TOPSECRET.dto.city.response.ResponseCityDTO;

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
 * </ul>
 *
 * <p>No business logic is performed here. This class is a pure translation layer.</p>
 * <p>This class is stateless. All methods are static and receive their
 * dependencies as parameters.</p>
 */
public final class CityMapper {

    private CityMapper() {}

    /**
     * Converts a {@link City} domain object into a {@link ResponseCityDTO}.
     *
     * <p>Unwraps all Value Objects into their primitive representations
     * and resolves the country identifier into a human-readable name.
     * ensuring no domain types are exposed outside the domain layer.</p>
     *
     * @param city the domain object to convert; must not be {@code null}
     * @param countryName the resolved human-readable country name; must not be {@code null}
     * @return a {@link ResponseCityDTO} containing the city name and country name
     * @throws NullPointerException if any argument is {@code null}
     */
    public static ResponseCityDTO toDTO(City city, String countryName) {
        Objects.requireNonNull(city, "City cannot be null");
        Objects.requireNonNull(countryName, "Country name cannot be null");

        return new ResponseCityDTO(city.getCityName(), countryName);
    }

    /**
     * Extracts and wraps the country identifier from a {@link RequestCityDTO}
     * into a {@link CountryId} Value Object.
     *
     * @param dto the request DTO; must not be {@code null}
     * @return a {@link CountryId} wrapping the DTO's country identifier
     * @throws NullPointerException if {@code dto} is {@code null}
     */
    public static CountryId toCountryId(RequestCityDTO dto) {
        Objects.requireNonNull(dto, "DTO cannot be null");
        return new CountryId(dto.countryId());
    }
}