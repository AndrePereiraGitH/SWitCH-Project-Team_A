package TOPSECRET.dto.author;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.ICountryRepository;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.dto.author.response.AuthorDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Mapper responsible for converting {@link Author} domain objects
 * into {@link AuthorDTO} response objects used in the presentation layer.
 *
 * <p>This class ensures a clear separation between the domain model and the API layer
 * by transforming domain entities into simplified DTO representations that are safe
 * to expose externally.</p>
 *
 * <p>It supports mapping both single {@link Author} objects and collections of authors.</p>
 */
public class AuthorMapper {

    private final ICountryRepository countryRepo;

    /**
     * Constructs an {@code AuthorMapper} with the required dependencies.
     *
     * @param countryRepo the repository used to resolve country identifiers
     */
    public AuthorMapper(ICountryRepository countryRepo) {
        this.countryRepo = countryRepo;
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

    /**
     * Converts a list of {@link Author} domain objects into a list of {@link AuthorDTO} responses.
     *
     * <p>Each author in the provided collection is individually mapped
     * using {@link #toDTO(Author)}.</p>
     *
     * @param authors the list of domain objects to convert; must not be {@code null}
     * @return a list of {@link AuthorDTO} representing the given authors
     * @throws NullPointerException if {@code authors} is null
     */
    public List<AuthorDTO> toDTOs(List<Author> authors) {
        Objects.requireNonNull(authors, "Authors list cannot be null");
        List<AuthorDTO> dtos = new ArrayList<>();
        for (Author author : authors) {
            dtos.add(toDTO(author));
        }
        return dtos;
    }
}