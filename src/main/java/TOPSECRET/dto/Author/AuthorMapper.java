package TOPSECRET.dto.Author;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.AuthorFactory;
import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.ICountryRepository;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.dto.Author.request.CreateAuthorDTO;
import TOPSECRET.dto.Author.response.AuthorDTO;

import java.time.LocalDate;
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
 */
public class AuthorMapper {

    private final ICountryRepository countryRepo;
    private final AuthorFactory authorFactory;

    /**
     * Constructs an {@code AuthorMapper} with the required dependencies.
     *
     * @param countryRepo   the repository used to resolve country identifiers
     * @param authorFactory the factory used to create {@link Author} domain objects
     */
    public AuthorMapper(ICountryRepository countryRepo, AuthorFactory authorFactory) {
        this.countryRepo = countryRepo;
        this.authorFactory = authorFactory;
    }

    /**
     * Converts a {@link CreateAuthorDTO} request into a new {@link Author} domain object.
     *
     * <p>The country identifier provided in the DTO is validated against the repository.
     * The date of birth is parsed from ISO format (yyyy-MM-dd).
     * Domain object creation is delegated to {@link AuthorFactory}.</p>
     *
     * @param dto the request DTO containing the author's data; must not be {@code null}
     * @return a new {@link Author} domain object
     * @throws NullPointerException     if {@code dto} is null
     * @throws IllegalArgumentException if the country ID does not match any registered country
     * @throws java.time.format.DateTimeParseException if the date of birth is not in ISO format
     */
    public Author toDomain(CreateAuthorDTO dto) {
        Objects.requireNonNull(dto, "CreateAuthorDTO cannot be null");
        CountryId countryId = new CountryId(dto.countryId());
        countryRepo.findByID(countryId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid country ID: " + dto.countryId()));
        LocalDate dob = LocalDate.parse(dto.dateOfBirth());
        return authorFactory.create(dto.name(), countryId, dob, dto.biography());
    }

    /**
     * Converts an {@link Author} domain object into an {@link AuthorDTO} response.
     *
     * <p>All domain value objects are unwrapped into plain {@link String} primitives.
     * The author's country is resolved via {@link ICountryRepository} to return
     * the human-readable country name rather than the raw identifier.</p>
     *
     * @param author the domain object to convert; must not be {@code null}
     * @return an {@link AuthorDTO} representing the author's data
     * @throws NullPointerException     if {@code author} is null
     * @throws IllegalArgumentException if the author's country ID cannot be resolved
     */
    public AuthorDTO toDTO(Author author) {
        Objects.requireNonNull(author, "Author cannot be null");
        CountryId countryId = author.getCountryID();
        Country country = countryRepo.findByID(countryId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Country not found for ID: " + countryId.getCountryID()));
        return new AuthorDTO(
                author.id().getId(),
                author.getName(),
                country.getCountryName(),
                author.getDateOfBirth().toString(),
                author.getBiography()
        );
    }
}