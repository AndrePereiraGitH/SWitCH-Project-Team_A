package TOPSECRET.dto.Country;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.dto.Country.Response.CountryResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Mapper class responsible for converting {@link Country} domain objects
 * into {@link CountryResponseDTO} data transfer objects used in the presentation layer.
 *
 * <p>This class ensures a clear separation between the domain model and the API layer
 * by transforming domain entities into simplified DTO representations that are safe
 * to expose externally.</p>
 *
 * <p>It supports mapping both single {@link Country} objects and collections of countries.</p>
 */

public class CountryMapper {

    /**
     * Converts a {@link Country} domain object into a {@link CountryResponseDTO}.
     *
     * @param country the country domain object to convert
     * @return a DTO representing the given country
     * @throws NullPointerException if {@code country} is null
     */
    public CountryResponseDTO toDTO(Country country){

        Objects.requireNonNull(country, "Country cannot be null");

        return new CountryResponseDTO(
                country.id().getCountryID(),
                country.getCountryName()
        );
    }

    /**
     * Converts a list of {@link Country} domain objects into a list of {@link CountryResponseDTO}.
     *
     * @param countries the list of country domain objects to convert
     * @return a list of DTOs representing the given countries
     * @throws NullPointerException if {@code countries} is null
     */
    public List<CountryResponseDTO> toDTOs(List<Country> countries){
            Objects.requireNonNull(countries, "Countries list cannot be null");

            List<CountryResponseDTO> dtos = new ArrayList<>();

            for (Country country : countries){
                dtos.add(toDTO(country));
            }

            return dtos;
        }
}
