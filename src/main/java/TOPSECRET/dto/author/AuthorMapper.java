package TOPSECRET.dto.author;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.dto.author.request.CreateAuthorDTO;
import TOPSECRET.dto.author.response.AuthorDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Mapper responsible for converting between {@link CreateAuthorDTO} request objects,
 * {@link Author} domain objects, and {@link AuthorDTO} response objects.
 *
 * <p>This class is the only component that is aware of both the API layer
 * and the domain layer. It centralizes all conversion logic, ensuring that:</p>
 * <ul>
 *   <li>Domain value objects are never exposed through the API boundary</li>
 *   <li>Request primitives are correctly wrapped into domain value objects</li>
 *   <li>Domain objects are flattened into API-safe response representations</li>
 * </ul>
 *
 * <p>All methods are static — this class is stateless and holds no dependencies.</p>
 */
public class AuthorMapper {

    private AuthorMapper() {}

    /**
     * Extracts and converts the country identifier from a {@link CreateAuthorDTO}.
     *
     * @param dto the request DTO; must not be {@code null}
     * @return a {@link CountryId} value object
     */
    public static CountryId toCountryId(CreateAuthorDTO dto) {
        return new CountryId(dto.countryId());
    }

    /**
     * Extracts and parses the date of birth from a {@link CreateAuthorDTO}.
     *
     * @param dto the request DTO; must not be {@code null}
     * @return a {@link LocalDate} parsed from ISO format (yyyy-MM-dd)
     */
    public static LocalDate toDateOfBirth(CreateAuthorDTO dto) {
        return LocalDate.parse(dto.dateOfBirth());
    }

    /**
     * Converts an {@link Author} domain object and its resolved {@link Country}
     * into an {@link AuthorDTO} response.
     *
     * <p>All domain value objects are unwrapped into plain {@link String} primitives.</p>
     *
     * @param author  the domain object to convert; must not be {@code null}
     * @param country the resolved country of the author; must not be {@code null}
     * @return an {@link AuthorDTO} representing the author's data
     * @throws NullPointerException if {@code author} or {@code country} is null
     */
    public static AuthorDTO toDTO(Author author, Country country) {
        Objects.requireNonNull(author, "Author cannot be null");
        Objects.requireNonNull(country, "Country cannot be null");
        return new AuthorDTO(
                author.id().getId(),
                author.getName(),
                country.getCountryName(),
                author.getDateOfBirth().toString(),
                author.getBiography()
        );
    }

    /**
     * Converts a list of {@link Author} domain objects into a list of {@link AuthorDTO} responses.
     *
     * @param authors   the list of domain objects to convert; must not be {@code null}
     * @param countries the list of resolved countries; must not be {@code null}
     * @return a list of {@link AuthorDTO} representing the given authors
     * @throws NullPointerException if {@code authors} or {@code countries} is null
     */
    public static List<AuthorDTO> toDTOs(List<Author> authors, List<Country> countries) {
        Objects.requireNonNull(authors, "Authors list cannot be null");
        Objects.requireNonNull(countries, "Countries list cannot be null");
        List<AuthorDTO> dtos = new ArrayList<>();
        for (int i = 0; i < authors.size(); i++) {
            dtos.add(toDTO(authors.get(i), countries.get(i)));
        }
        return dtos;
    }
}