package TOPSECRET.domain.Author;

import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.CountryID;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


/**
 * Factory responsible for creating {@link Author} instances.
 *
 * <p>This factory centralizes the creation logic of {@code Author} objects,
 * performing minor input normalization before delegating validation to the
 * {@link Author} constructor.</p>
 *
 * <p>In particular, string arguments such as the author's name and biography
 * are trimmed before the {@code Author} is created.</p>
 */
@Service
public class AuthorFactory {
    /**
     * Creates a new {@link Author} instance with the given data.
     *
     * <p>Before creating the author, this method trims the {@code name} and
     * {@code biography} values when they are not {@code null}. Validation of
     * domain rules is delegated to the {@link Author} constructor.</p>
     *
     * @param name the author's name
     * @param countryID the identifier of the author's country of origin
     * @param dateOfBirth the author's date of birth
     * @param biography the author's biography
     * @return a new {@link Author} instance
     * @throws IllegalArgumentException if any argument violates the rules
     *                                  enforced by {@link Author}
     */
    public Author create(String name, CountryID countryID, LocalDate dateOfBirth, String biography) {
        String trimmedName = (name != null) ? name.trim() : null;
        String trimmedBio  = (biography != null) ? biography.trim() : null;
        return new Author(trimmedName, countryID, dateOfBirth, trimmedBio);
    }

    /**
     * Recreates an {@link Author} instance with an existing {@link AuthorId}.
     *
     * <p>
     * This method is intended to be used when rebuilding an author from persisted
     * data, where the identifier already exists and must be preserved.
     * </p>
     *
     * <p>
     * Unlike {@link #create(String, CountryID, LocalDate, String)}, this method
     * does not normalize string values before creating the author. Validation of
     * domain rules is delegated to the {@link Author} constructor.
     * </p>
     *
     * @param id the existing author identifier
     * @param name the author's name
     * @param countryID the identifier of the author's country of origin
     * @param dateOfBirth the author's date of birth
     * @param biography the author's biography
     * @return an {@link Author} instance with the given identifier
     * @throws IllegalArgumentException if any argument violates the rules
     *                                  enforced by {@link Author}
     */
    public Author create(AuthorId id, String name, CountryID countryID, LocalDate dateOfBirth, String biography) {
        return new Author(id, name, countryID, dateOfBirth, biography);
    }
}
