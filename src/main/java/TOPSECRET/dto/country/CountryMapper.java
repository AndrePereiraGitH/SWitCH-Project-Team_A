package TOPSECRET.dto.country;

import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.country.response.CountryResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Utility mapper class responsible for converting {@link Country}
 * domain entities into {@link CountryResponseDTO} objects used in
 * the presentation layer.
 *
 * <p>This class helps maintain a clear separation between the domain
 * model and the API layer by transforming internal domain objects
 * into simplified DTO representations suitable for external exposure.</p>
 *
 * <p>The mapper provides static methods for converting both individual
 * {@link Country} objects and collections of countries into their
 * corresponding response DTOs.</p>
 *
 * <p>All mapping operations validate input arguments and throw
 * {@link NullPointerException} when null values are provided.</p>
 */
public class CountryMapper {

    /**
     * Converts a single {@link Country} domain entity into a
     * {@link CountryResponseDTO}.
     *
     * <p>The generated DTO contains the country identifier and
     * country name extracted from the domain object.</p>
     *
     * @param country the domain entity to convert
     * @return a response DTO representing the given country
     * @throws NullPointerException if {@code country} is null
     */
    public static CountryResponseDTO toDTO(Country country){

        Objects.requireNonNull(country, "Country cannot be null");

        return new CountryResponseDTO(
                country.id().getCountryID(),
                country.getCountryName()
        );
    }

    /**
     * Converts a list of {@link Country} domain entities into a list
     * of {@link CountryResponseDTO} objects.
     *
     * <p>Each country in the provided collection is individually mapped
     * using {@link #toDTO(Country)}.</p>
     *
     * @param countries the list of domain entities to convert
     * @return a list of response DTOs representing the given countries
     * @throws NullPointerException if {@code countries} is null
     */
    public static List<CountryResponseDTO> toDTOs(List<Country> countries){
            Objects.requireNonNull(countries, "Countries list cannot be null");

            List<CountryResponseDTO> dtos = new ArrayList<>();

            for (Country country : countries){
                dtos.add(toDTO(country));
            }

            return dtos;
        }

    /**
     * Converts a raw {@link String} representation of a user identifier
     * into a {@link UserId} domain value object.
     *
     * <p>This method acts as a simple adapter between external string-based
     * inputs (e.g., request parameters or DTO fields) and the strongly-typed
     * {@code UserId} value object used in the domain layer.</p>
     *
     * @param userId the string representation of the user identifier
     * @return a {@code UserId} value object wrapping the provided string
     * @throws IllegalArgumentException if the provided {@code userId} is
     *         null, blank, or otherwise invalid according to {@link UserId}
     */
    public static UserId toUserId (String userId){

        return new UserId(userId);
    }
}
